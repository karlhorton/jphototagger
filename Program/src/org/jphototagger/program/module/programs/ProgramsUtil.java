package org.jphototagger.program.module.programs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import org.jdesktop.swingx.JXList;
import org.jphototagger.domain.programs.Program;
import org.jphototagger.domain.repository.ProgramsRepository;
import org.jphototagger.lib.swing.util.ListUtil;
import org.jphototagger.program.resource.GUI;
import org.openide.util.Lookup;

/**
 * @author Elmar Baumann
 */
public final class ProgramsUtil {

    /**
     * Moves in a list with {@code Program}s the selected progam up and reorders
     * all program's sequence numbers in the repository.
     * <p>
     * Does nothing if the program can't be moved: No list item or the first
     * list item is selected.
     *
     * @param listPrograms list with {@code DefaultListModel} as model and
     *                     {@code Program} as values
     */
    public static void moveProgramUp(JXList listPrograms) {
        if (listPrograms == null) {
            throw new NullPointerException("listPrograms == null");
        }
        int selectedIndex = listPrograms.getSelectedIndex();
        int modelIndex = listPrograms.convertIndexToModel(selectedIndex);
        int upIndex = modelIndex - 1;
        boolean programIsSelected = selectedIndex >= 0;
        @SuppressWarnings("unchecked")
        DefaultListModel<Object> model = (DefaultListModel) listPrograms.getModel();
        if (programIsSelected && (upIndex >= 0)) {
            ListUtil.swapModelElements(model, upIndex, modelIndex);
            reorderPrograms(model);
            listPrograms.setSelectedIndex(upIndex);
        }
    }

    /**
     * Moves in a list with {@code Program}s the selected progam down and
     * reorders all program's sequence numbers in the repository.
     * <p>
     * Does nothing if the program can't be moved: No list item or the last
     * list item is selected.
     *
     * @param listPrograms list with {@code DefaultListModel} as model and
     *                     {@code Program} as values
     */
    public static void moveProgramDown(JXList listPrograms) {
        if (listPrograms == null) {
            throw new NullPointerException("listPrograms == null");
        }
        @SuppressWarnings("unchecked")
        DefaultListModel<Object> model = (DefaultListModel) listPrograms.getModel();
        int size = model.getSize();
        int selectedIndex = listPrograms.getSelectedIndex();
        int modelIndex = listPrograms.convertIndexToModel(selectedIndex);
        int downIndex = modelIndex + 1;
        boolean programIsSelected = selectedIndex >= 0;
        if (programIsSelected && (downIndex < size)) {
            ListUtil.swapModelElements(model, downIndex, modelIndex);
            reorderPrograms(model);
            listPrograms.setSelectedIndex(downIndex);
        }
    }

    /**
     * Reorders in the repository the programs sequence number to match their
     * order a list model.
     *
     * @param model model with {@code Program}s as elements
     */
    public static void reorderPrograms(DefaultListModel<?> model) {
        if (model == null) {
            throw new NullPointerException("model == null");
        }
        int size = model.getSize();
        List<Program> programs = new ArrayList<>(size);
        for (int sequenceNo = 0; sequenceNo < size; sequenceNo++) {
            Program program = (Program) model.get(sequenceNo);
            program.setSequenceNumber(sequenceNo);
            programs.add(program);
        }
        ProgramsRepository repo = Lookup.getDefault().lookup(ProgramsRepository.class);
        for (Program program : programs) {
            repo.updateProgram(program);
        }
    }

    public static class ReorderListener implements ListDataListener {

        private volatile boolean listenToModel = true;
        private final DefaultListModel<?> model;

        public ReorderListener(DefaultListModel<?> model) {
            if (model == null) {
                throw new NullPointerException("model == null");
            }
            this.model = model;
            model.addListDataListener(this);
        }

        public void setListenToModel(boolean listenToModel) {
            this.listenToModel = listenToModel;
        }

        @Override
        public void intervalAdded(ListDataEvent e) {
            reorder();
        }

        @Override
        public void intervalRemoved(ListDataEvent e) {
            reorder();
        }

        @Override
        public void contentsChanged(ListDataEvent e) {
            reorder();
        }

        private void reorder() {
            if (listenToModel) {
                listenToModel = false;
                reorderPrograms(model);
                listenToModel = true;
            }
        }
    }

    public static Program addProgram() {
        ProgramPropertiesDialog dlg = new ProgramPropertiesDialog(false);

        dlg.setVisible(true);

        if (dlg.isAccepted()) {
            Program program = dlg.getProgram();
            ProgramsRepository repo = Lookup.getDefault().lookup(ProgramsRepository.class);

            repo.saveProgram(program);

            return program;
        }

        return null;
    }

    /**
     *
     * @param program             may be null
     * @param waitForTermination
     */
    public static void openSelectedFilesWidth(Program program, boolean waitForTermination) {
        if (program != null) {
            StartPrograms startPrograms = new StartPrograms();
            List<File> selectedImageFiles = GUI.getSelectedImageFiles();

            startPrograms.startProgram(program, selectedImageFiles, waitForTermination);
        }
    }

    private ProgramsUtil() {
    }
}
