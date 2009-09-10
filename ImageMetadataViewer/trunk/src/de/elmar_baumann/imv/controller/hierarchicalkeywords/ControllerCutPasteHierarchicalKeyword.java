package de.elmar_baumann.imv.controller.hierarchicalkeywords;

import de.elmar_baumann.imv.datatransfer.Flavors;
import de.elmar_baumann.imv.datatransfer.TransferHandlerTreeHierarchicalKeywords;
import de.elmar_baumann.imv.model.TreeModelHierarchicalKeywords;
import de.elmar_baumann.imv.view.panels.HierarchicalKeywordsPanel;
import de.elmar_baumann.imv.view.popupmenus.PopupMenuHierarchicalKeywords;
import de.elmar_baumann.lib.datatransfer.TransferableObject;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JMenuItem;
import javax.swing.TransferHandler.TransferSupport;
import javax.swing.tree.DefaultMutableTreeNode;

// The implementation can't paste the nodes to the system clipboard and let do
// the work the panel's transfer handler, because the affected nodes when using
// the popup menu may not be the selected nodes as expected by the panel's
// transfer handler.
/**
 * Listens to the menu items {@link PopupMenuHierarchicalKeywords#getMenuItemCut()},
 * {@link PopupMenuHierarchicalKeywords#getMenuItemPaste()} and on action
 * cuts a keyword to the clipboard or pastes a cutted item.
 *
 * The key action Ctrl+X and Ctrl+V are handled by the JTree.
 *
 * @author  Elmar Baumann <eb@elmar-baumann.de>
 * @version 2009-09-10
 */
public class ControllerCutPasteHierarchicalKeyword implements ActionListener {

    private final HierarchicalKeywordsPanel panel;
    private final PopupMenuHierarchicalKeywords popup =
            PopupMenuHierarchicalKeywords.INSTANCE;
    private final JMenuItem itemCut = popup.getMenuItemCut();
    private final JMenuItem itemPaste = popup.getMenuItemPaste();

    public ControllerCutPasteHierarchicalKeyword(HierarchicalKeywordsPanel panel) {
        this.panel = panel;
    }

    // Do not extend ControllerHierarchicalKeywords and using localAction
    // because listening to 2 actions: cut is only 1 line of code - too less
    // to implement a separate class
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        Object lastPathComponent = popup.getTreePath().getLastPathComponent();
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) lastPathComponent;
        if (source == itemCut) {
            HierarchicalKeywordTreeNodesClipboard.INSTANCE.setContent(node);
        } else if (source == itemPaste) {
            paste(node);
        }
    }

    private void paste(DefaultMutableTreeNode node) {
        HierarchicalKeywordTreeNodesClipboard hkClipboard =
                HierarchicalKeywordTreeNodesClipboard.INSTANCE;
        if (hkClipboard.hasContent()) {
            Transferable trans = new TransferableObject(
                    new ArrayList<DefaultMutableTreeNode>(
                    hkClipboard.getContent()),
                    Flavors.HIERARCHICAL_KEYWORDS_FLAVOR);
            TransferHandlerTreeHierarchicalKeywords.moveKeywords(
                    new TransferSupport(panel, trans),
                    (TreeModelHierarchicalKeywords) panel.getTree().getModel(),
                    node);
            hkClipboard.empty();
        }
    }
}