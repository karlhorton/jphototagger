package org.jphototagger.lib.swing;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.jphototagger.lib.swing.util.ComponentUtil;
import org.jphototagger.lib.swing.util.MnemonicUtil;
import org.jphototagger.lib.util.Bundle;
import org.jphototagger.resources.UiFactory;

/**
 * @param <T> Object type
 * @author Elmar Baumann
 */
public class ObjectsSelectionDialog<T> extends DialogExt {

    private static final long serialVersionUID = 1L;
    private final DefaultListModel<T> listModel = new DefaultListModel<>();
    private final List<T> selectedObjects = new ArrayList<>();
    private boolean accepted;

    public ObjectsSelectionDialog() {
        super(ComponentUtil.findFrameWithIcon(), true);
        init();
    }

    public ObjectsSelectionDialog(JDialog owner) {
        super(owner, true);
        init();
    }

    private void init() {
        initComponents();
        postInitComponents();
    }

    private void postInitComponents() {
        MnemonicUtil.setMnemonics(this);
        list.addListSelectionListener(objectSelectionListener);
        list.addMouseListener(doubleClickListener);
        setOkButtonEnabled();
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setObjects(Collection<? extends T> objects) {
        if (objects == null) {
            throw new NullPointerException("objects == null");
        }
        listModel.clear();
        for (T object : objects) {
            listModel.addElement(object);
        }
    }

    public List<T> getSelectedObjects() {
        return Collections.unmodifiableList(selectedObjects);
    }

    /**
     * @param selectionMode {@link javax.swing.ListSelectionModel#SINGLE_SELECTION} or
     * {@link javax.swing.ListSelectionModel#SINGLE_INTERVAL_SELECTION} or
     * {@link javax.swing.ListSelectionModel#MULTIPLE_INTERVAL_SELECTION}
     */
    public void setSelectionMode(int selectionMode) {
        list.setSelectionMode(selectionMode);
    }

    private final ListSelectionListener objectSelectionListener = new ListSelectionListener() {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
                setSelectedObjects();
                setOkButtonEnabled();
            }
        }
    };

    private void setOkButtonEnabled() {
        boolean objectSelected = list.getSelectedIndex() >= 0;
        buttonOk.setEnabled(objectSelected);
    }

    private void setSelectedObjects() {
        selectedObjects.clear();
        selectedObjects.addAll(list.getSelectedValuesList());
    }

    private final MouseListener doubleClickListener = new MouseAdapter() {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (MouseEventUtil.isDoubleClick(e)) {
                setSelectedObjects();
                acceptInput();
            }
        }
    };

    private void acceptInput() {
        if (!selectedObjects.isEmpty()) {
            accepted = true;
            dispose();
        }
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        scrollPane = UiFactory.scrollPane();
        list = UiFactory.list();
        buttonOk = UiFactory.button();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        list.setModel(listModel);
        scrollPane.setViewportView(list);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = UiFactory.insets(5, 5, 0, 5);
        getContentPane().add(scrollPane, gridBagConstraints);

        buttonOk.setText(Bundle.getString(getClass(), "ObjectsSelectionDialog.buttonOk.text")); // NOI18N
        buttonOk.setEnabled(false);
        buttonOk.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonOkActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = UiFactory.insets(5, 5, 5, 5);
        getContentPane().add(buttonOk, gridBagConstraints);

        pack();
    }

    private void buttonOkActionPerformed(java.awt.event.ActionEvent evt) {
        acceptInput();
    }

    private javax.swing.JButton buttonOk;
    private javax.swing.JList<T> list;
    private javax.swing.JScrollPane scrollPane;

}
