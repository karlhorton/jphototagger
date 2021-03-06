package org.jphototagger.program.module.keywords;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTree;
import javax.swing.ListModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXTree;
import org.jphototagger.api.concurrent.SerialTaskExecutor;
import org.jphototagger.domain.image.ImageFile;
import org.jphototagger.domain.metadata.SelectedFilesMetaDataEditor;
import org.jphototagger.domain.metadata.keywords.Keyword;
import org.jphototagger.domain.metadata.xmp.FileXmp;
import org.jphototagger.domain.metadata.xmp.Xmp;
import org.jphototagger.domain.metadata.xmp.XmpDcSubjectsSubjectMetaDataValue;
import org.jphototagger.domain.metadata.xmp.XmpLastModifiedMetaDataValue;
import org.jphototagger.domain.metadata.xmp.XmpSidecarFileResolver;
import org.jphototagger.domain.metadata.xmp.XmpToImageWriters;
import org.jphototagger.domain.repository.ImageFilesRepository;
import org.jphototagger.domain.repository.KeywordsRepository;
import org.jphototagger.domain.repository.SaveOrUpdate;
import org.jphototagger.lib.awt.EventQueueUtil;
import org.jphototagger.lib.concurrent.HelperThread;
import org.jphototagger.lib.swing.MessageDisplayer;
import org.jphototagger.lib.swing.util.ListUtil;
import org.jphototagger.lib.swing.util.TreeUtil;
import org.jphototagger.lib.util.ArrayUtil;
import org.jphototagger.lib.util.Bundle;
import org.jphototagger.lib.util.StringUtil;
import org.jphototagger.program.app.ui.AppPanel;
import org.jphototagger.program.factory.ModelFactory;
import org.jphototagger.program.misc.InputHelperDialog;
import org.jphototagger.program.misc.SaveXmp;
import org.jphototagger.program.module.keywords.tree.KeywordsTreeCellRenderer;
import org.jphototagger.program.module.keywords.tree.KeywordsTreeModel;
import org.jphototagger.program.resource.GUI;
import org.jphototagger.xmp.XmpMetadata;
import org.openide.util.Lookup;

/**
 * Helper for hierarchical keywords and Dublin Core subjects ("flat" keywords).
 * <p>
 * <strong>Keyword</strong> means a hierarchical keyword, <strong>DC (Dublin
 * Core) subject</strong> a "flat" keyword.
 *
 * @author Elmar Baumann
 */
public final class KeywordsUtil {

    private static final Logger LOGGER = Logger.getLogger(KeywordsUtil.class.getName());
    private static final ImageFilesRepository IMAGE_FILE_REPO = Lookup.getDefault().lookup(ImageFilesRepository.class);
    private static final XmpSidecarFileResolver XMP_SIDECAR_FILE_RESOLVER = Lookup.getDefault().lookup(XmpSidecarFileResolver.class);

    private KeywordsUtil() {
    }

    /**
     * Adds the keyword - contained as user object in a d.m. tree node -
     * and all it's parents to the metadata edit panel.
     *
     * @param node node with keyword. <em>All parents of that node have to be an
     *             instance of {@code DefaultMutableTreeNode}!</em>
     */
    public static void addKeywordsToEditPanel(final DefaultMutableTreeNode node) {
        if (node == null) {
            throw new NullPointerException("node == null");
        }

        EventQueueUtil.invokeInDispatchThread(new Runnable() {

            @Override
            public void run() {
                SelectedFilesMetaDataEditor editor = Lookup.getDefault().lookup(SelectedFilesMetaDataEditor.class);
                List<String> keywordStrings = getKeywordStrings(node, true);
                for (String keyword : keywordStrings) {
                    editor.setOrAddText(XmpDcSubjectsSubjectMetaDataValue.INSTANCE, keyword);
                }
                if (keywordStrings.size() > 1) {
                    // else leaf is first element
                    Collections.reverse(keywordStrings);
                }
            }
        });
    }

    /**
     * Inserts into the repository a dublin core subject if it does not already
     * exist.
     *
     * @param dcSubject subject
     */
    public static void insertDcSubject(String dcSubject) {
        if (!IMAGE_FILE_REPO.existsDcSubject(dcSubject)) {
            IMAGE_FILE_REPO.saveDcSubject(dcSubject);
        }
    }

    /**
     * Inserts into the repository a Dublin Core keyword via user input.
     */
    public static void insertDcSubject() {
        String info = Bundle.getString(KeywordsUtil.class, "KeywordsUtil.Input.InsertDcSubject");
        String input = "";
        String dcSubject = MessageDisplayer.input(info, input);
        if ((dcSubject != null) && checkExistsDcSubject(dcSubject)) {
            if (IMAGE_FILE_REPO.saveDcSubject(dcSubject)) {
                insertDcSubjectAsKeyword(dcSubject);
            } else {
                String message = Bundle.getString(KeywordsUtil.class, "KeywordsUtil.Error.InsertDcSubject", dcSubject);
                MessageDisplayer.error(null, message);
            }
        }
    }

    private static void insertDcSubjectAsKeyword(String keyword) {
        KeywordsRepository repo = Lookup.getDefault().lookup(KeywordsRepository.class);
        if (!repo.existsKeyword(keyword)) {
            repo.saveKeyword(new Keyword(null, null, keyword, true));
        }
    }

    private static boolean checkExistsDcSubject(String dcSubject) {
        if (IMAGE_FILE_REPO.existsDcSubject(dcSubject)) {
            String message = Bundle.getString(KeywordsUtil.class, "KeywordsUtil.Error.DcSubjectExists", dcSubject);
            MessageDisplayer.error(null, message);
            return false;
        }
        return true;
    }

    public static void saveKeywordsToImageFile(List<String> keywordStrings, File imageFile) {
        if (keywordStrings == null) {
            throw new NullPointerException("keywordStrings == null");
        }
        if (imageFile == null) {
            throw new NullPointerException("imageFile == null");
        }
        if (!imageFile.exists()) {
            return;
        }
        Xmp xmp = null;
        try {
            xmp = XmpMetadata.getXmpFromSidecarFileOf(imageFile);
        } catch (IOException ex) {
            Logger.getLogger(KeywordsUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (xmp == null) {
            xmp = new Xmp();
        }
        for (String keyword : keywordStrings) {
            if (!xmp.containsValue(XmpDcSubjectsSubjectMetaDataValue.INSTANCE, keyword)) {
                xmp.setValue(XmpDcSubjectsSubjectMetaDataValue.INSTANCE, keyword);
            }
        }
        List<FileXmp> saveList = new ArrayList<>();
        saveList.add(new FileXmp(imageFile, xmp));
        SaveXmp.save(saveList);
    }

    /**
     * Returns a keyword - contained as user object in a default mutable tree
     * node - and all it's parents.
     *
     * @param node node with keyword. <em>All parents of that node have to be
     *             instances of {@code DefaultMutableTreeNode}!</em>
     * @param real true if only real keywords shall be added
     * @return     all keywords or empty list
     */
    public static List<Keyword> getKeywords(DefaultMutableTreeNode node, boolean real) {
        List<Keyword> list = new ArrayList<>();
        DefaultMutableTreeNode n = node;
        while (n != null) {
            Object userObject = n.getUserObject();
            if (userObject instanceof Keyword) {
                Keyword keyword = (Keyword) userObject;
                if (!real || (real && keyword.isReal())) {
                    list.add(keyword);
                }
            }
            TreeNode parent = n.getParent();
            n = (parent instanceof DefaultMutableTreeNode)
                    ? (DefaultMutableTreeNode) parent
                    : null;
        }
        return list;
    }

    /**
     * Returns a keyword - contained as user object in a default mutable tree
     * node - and all it's parents as a list of strings.
     *
     * @param node node with keyword. <em>All parents of that node have to be
     *             instances of {@code DefaultMutableTreeNode}!</em>
     * @param real true if only real keywords shall be added
     * @return     all keywords as strings
     */
    public static List<String> getKeywordStrings(DefaultMutableTreeNode node, boolean real) {
        List<String> list = new ArrayList<>();
        for (Keyword keyword : getKeywords(node, real)) {
            list.add(keyword.getName());
        }

        return list;
    }

    /**
     * Selects in {@code AppPanel#getTreeSelKeywords()} a node with a specific
     * keyword.
     *
     * @param tree    tree with {@code KeywordsTreeModel} and all
     *                nodes of the type {@code DefaultMutableTreeNode}
     * @param keyword keyword to select
     */
    @SuppressWarnings("unchecked")
    public static void selectNode(final JTree tree, final Keyword keyword) {
        if (tree == null) {
            throw new NullPointerException("tree == null");
        }
        if (keyword == null) {
            throw new NullPointerException("keyword == null");
        }

        EventQueueUtil.invokeInDispatchThread(new Runnable() {

            @Override
            public void run() {
                KeywordsTreeModel model = ModelFactory.INSTANCE.getModel(KeywordsTreeModel.class);
                DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
                DefaultMutableTreeNode selNode = null;
                for (Enumeration<DefaultMutableTreeNode> e = root.breadthFirstEnumeration();
                        (selNode == null) && e.hasMoreElements();) {
                    DefaultMutableTreeNode node = e.nextElement();
                    Object userObject = node.getUserObject();

                    if (userObject instanceof Keyword) {
                        Keyword hkw = (Keyword) userObject;

                        if (hkw.equals(keyword)) {
                            selNode = node;
                        }
                    }
                }

                if (selNode != null) {
                    tree.setSelectionPath(new TreePath(selNode.getPath()));
                }
            }
        });
    }

    /**
     * Returns all names of the keyword's parents.
     *
     * @param  keyword keyword
     * @param  real    true if only real keyword names shall be added
     * @return         parent names
     */
    public static List<String> getParentKeywordNames(Keyword keyword, boolean real) {
        List<String> names = new ArrayList<>();
        KeywordsRepository repo = Lookup.getDefault().lookup(KeywordsRepository.class);
        List<Keyword> parents = repo.findParentKeywords(keyword);
        for (Keyword parent : parents) {
            boolean add = !real || (real && parent.isReal());

            if (add) {
                names.add(parent.getName());
            }
        }
        return names;
    }

    public static void addHighlightKeywords(final Collection<String> keywords) {
        if (keywords == null) {
            throw new NullPointerException("keywords == null");
        }

        EventQueueUtil.invokeInDispatchThread(new Runnable() {

            @Override
            public void run() {
                for (KeywordsTreeCellRenderer treeCellRendererKeywords : getCellRenderer()) {
                    treeCellRendererKeywords.addSelImgKeywords(keywords);
                }
            }
        });
    }

    public static void removeHighlightKeyword(final String keyword) {
        EventQueueUtil.invokeInDispatchThread(new Runnable() {

            @Override
            public void run() {
                for (KeywordsTreeCellRenderer treeCellRendererKeywords : getCellRenderer()) {
                    treeCellRendererKeywords.removeSelImgKeyword(keyword);
                }
            }
        });
    }

    private static List<KeywordsTreeCellRenderer> getCellRenderer() {
        List<KeywordsTreeCellRenderer> renderer = new ArrayList<>();
        for (JTree tree : getKeywordTrees()) {
            TreeCellRenderer treeCellRenderer = tree.getCellRenderer();
            if (treeCellRenderer instanceof JXTree.DelegatingRenderer) {
                treeCellRenderer = ((JXTree.DelegatingRenderer) treeCellRenderer).getDelegateRenderer();

            }
            if (treeCellRenderer instanceof KeywordsTreeCellRenderer) {
                renderer.add((KeywordsTreeCellRenderer) treeCellRenderer);
            }
        }
        return renderer;
    }

    private static List<JTree> getKeywordTrees() {
        return Arrays.<JTree>asList(GUI.getAppPanel().getTreeEditKeywords(), GUI.getAppPanel().getTreeSelKeywords(),
                InputHelperDialog.INSTANCE.getPanelKeywords().getTree());
    }

    public static void selectInSelKeywordsList(final List<Integer> modelIndices) {
        if (modelIndices == null) {
            throw new NullPointerException("indices == null");
        }
        EventQueueUtil.invokeInDispatchThread(new Runnable() {

            @Override
            public void run() {
                JXList selKeywordsList = GUI.getAppPanel().getListSelKeywords();
                selKeywordsList.clearSelection();
                GUI.getAppPanel().displaySelKeywordsList(AppPanel.SelectAlso.SEL_KEYWORDS_TAB);
                if (!modelIndices.isEmpty()) {
                    List<Integer> listIndices = ListUtil.convertModelIndicesToListIndices(modelIndices, selKeywordsList);
                    selKeywordsList.setSelectedIndices(ArrayUtil.toIntArray(listIndices));
                    selKeywordsList.ensureIndexIsVisible(listIndices.get(0));
                }
            }
        });
    }

    public static List<String> getSelectedKeywordsFromList(JXList keywordsList) {
        if (keywordsList == null) {
            throw new NullPointerException("keywordsList == null");
        }
        List<String> selectedKeywords = new ArrayList<>();
        ListModel<?> listModel = keywordsList.getModel();
        int[] selectedIndices = keywordsList.getSelectedIndices();
        for (int selectedIndex : selectedIndices) {
            int modelIndex = keywordsList.convertIndexToModel(selectedIndex);
            Object selectedElement = listModel.getElementAt(modelIndex);

            if (selectedElement instanceof String) {
                selectedKeywords.add((String) selectedElement);
            }
        }

        return selectedKeywords;
    }

    public static void expandAllTreesTo(final DefaultMutableTreeNode node) {
        if (node == null) {
            throw new NullPointerException("node == null");
        }

        EventQueueUtil.invokeInDispatchThread(new Runnable() {

            @Override
            public void run() {
                for (JTree tree : getKeywordTrees()) {
                    TreeUtil.expandPath(tree, new TreePath(node.getPath()));
                }
            }
        });
    }

    /**
     * Renames in the repository and all sidecar files a Dublin Core subject.
     *
     * @param fromName old name
     * @param toName   new name
     */
    public static void renameDcSubject(String fromName, String toName) {
        if (fromName == null) {
            throw new NullPointerException("fromName == null");
        }
        if (toName == null) {
            throw new NullPointerException("toName == null");
        }
        if (!fromName.equals(toName)) {
            SerialTaskExecutor executor = Lookup.getDefault().lookup(SerialTaskExecutor.class);
            RenameDcSubject renameDcSubject = new RenameDcSubject(fromName, toName);

            executor.addTask(renameDcSubject);
        }
    }

    /**
     * Renames in the repository and all sidecar files a dublin core subject.
     *
     * @param keyword keyword
     */
    public static void deleteDcSubject(String keyword) {
        if (keyword == null) {
            throw new NullPointerException("keyword == null");
        }
        SerialTaskExecutor executor = Lookup.getDefault().lookup(SerialTaskExecutor.class);
        DeleteDcSubject deleteDcSubject = new DeleteDcSubject(keyword);
        executor.addTask(deleteDcSubject);
    }

    private static void updateXmp(Xmp xmp, File file, File sidecarFile) {
        if (XmpMetadata.writeXmpToSidecarFile(xmp, sidecarFile)) {
            XmpToImageWriters.write(sidecarFile, file);
            ImageFile imageFile = new ImageFile();
            imageFile.setFile(file);
            xmp.setValue(XmpLastModifiedMetaDataValue.INSTANCE, sidecarFile.lastModified());
            imageFile.setXmp(xmp);
            imageFile.addToSaveIntoRepository(SaveOrUpdate.XMP);
            IMAGE_FILE_REPO.saveOrUpdateImageFile(imageFile);
        }
    }

    private static class DeleteDcSubject extends HelperThread {

        private final String dcSubject;
        private volatile boolean cancel;

        DeleteDcSubject(String keyword) {
            super("JPhotoTagger: Deleting keyword");
            this.dcSubject = keyword;
            setInfo(Bundle.getString(DeleteDcSubject.class, "KeywordsUtil.Info.Delete"));
        }

        @Override
        public void run() {
            List<File> imageFiles = new ArrayList<>(IMAGE_FILE_REPO.findImageFilesContainingDcSubject(dcSubject, false));
            logStartDelete(dcSubject);
            progressStarted(0, 0, imageFiles.size(), null);
            int size = imageFiles.size();
            int index;
            for (index = 0; !cancel && !isInterrupted() && (index < size); index++) {
                File imageFile = imageFiles.get(index);
                File sidecarFile = XMP_SIDECAR_FILE_RESOLVER.suggestXmpSidecarFile(imageFile);
                Xmp xmp = null;
                try {
                    xmp = XmpMetadata.getXmpFromSidecarFileOf(imageFile);
                } catch (IOException ex) {
                    Logger.getLogger(KeywordsUtil.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (xmp != null) {
                    xmp.removeValue(XmpDcSubjectsSubjectMetaDataValue.INSTANCE, dcSubject);
                    updateXmp(xmp, imageFile, sidecarFile);
                }
                progressPerformed(index, sidecarFile.getName());
            }
            checkRepository();
            progressEnded(index);
        }

        private static void logStartDelete(String keyword) {
            LOGGER.log(Level.INFO, "Deleting keyword ''{0}'' from the repository and all XMP sidecar files", keyword);
        }

        @Override
        public void cancel() {
            cancel = true;
        }

        private void checkRepository() {
            if (IMAGE_FILE_REPO.existsDcSubject(dcSubject)) {
                IMAGE_FILE_REPO.deleteDcSubject(dcSubject);
            }
        }
    }

    private static class RenameDcSubject extends HelperThread {
        private final String toName;
        private final String fromName;
        private volatile boolean cancel;

        private RenameDcSubject(String fromName, String toName) {
            super("JPhotoTagger: Renaming DC subject");
            this.fromName = fromName;
            this.toName = toName;
            setInfo(Bundle.getString(RenameDcSubject.class, "KeywordsUtil.Info.Rename"));
        }

        @Override
        public void run() {
            List<File> imageFiles = new ArrayList<>(IMAGE_FILE_REPO.findImageFilesContainingDcSubject(fromName, false));
            logStartRename(fromName, toName);
            progressStarted(0, 0, imageFiles.size(), null);
            int size = imageFiles.size();
            int index;
            for (index = 0; !cancel && !isInterrupted() && (index < size); index++) {
                File imageFile = imageFiles.get(index);
                File sidecarFile = XMP_SIDECAR_FILE_RESOLVER.suggestXmpSidecarFile(imageFile);
                Xmp xmp = null;
                try {
                    xmp = XmpMetadata.getXmpFromSidecarFileOf(imageFile);
                } catch (IOException ex) {
                    Logger.getLogger(KeywordsUtil.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (xmp != null) {
                    xmp.removeValue(XmpDcSubjectsSubjectMetaDataValue.INSTANCE, fromName);
                    xmp.setValue(XmpDcSubjectsSubjectMetaDataValue.INSTANCE, toName);
                    updateXmp(xmp, imageFile, sidecarFile);
                }
                progressPerformed(index + 1, sidecarFile.getName());
            }
            deleteKeyword();
            checkCase();
            progressEnded(index);
        }

        private void deleteKeyword() {
            if (!IMAGE_FILE_REPO.isDcSubjectReferenced(fromName)) {
                IMAGE_FILE_REPO.deleteDcSubject(fromName);
            }
        }

        private static void logStartRename(String fromName, String toName) {
            LOGGER.log(Level.INFO,
                    "Rename keyword ''{0}'' into ''{1}'' in the repository and all XMP sidecar files",
                    new Object[]{fromName, toName});
        }

        @Override
        public void cancel() {
            cancel = true;
        }

        // When renaming keywords to the same name but different case,
        // e.g. "FROM" -> "from", the database may contain the old or both
        // cases, because keywords are not case sensitive. So let have
        // all keywords the same case in the database.
        private void checkCase() {
            if (!fromName.toLowerCase().equals(toName.toLowerCase()) || fromName.equals(toName)) {
                return;
            }
            try {
                if (StringUtil.hasContent(fromName) && StringUtil.hasContent(toName)) {
                    IMAGE_FILE_REPO.updateRenameAllDcSubjects(fromName, toName);
                }
            } catch (Throwable t) {
                Logger.getLogger(RenameDcSubject.class.getName()).log(Level.SEVERE, null, t);
            }
        }
    }
}
