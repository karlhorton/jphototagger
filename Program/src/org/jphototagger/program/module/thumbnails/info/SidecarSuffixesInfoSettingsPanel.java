package org.jphototagger.program.module.thumbnails.info;

import java.awt.event.KeyEvent;
import javax.swing.DefaultListModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.bushe.swing.event.EventBus;
import org.jphototagger.api.preferences.Preferences;
import org.jphototagger.lib.swing.PanelExt;
import org.jphototagger.lib.swing.util.DocumentUtil;
import org.jphototagger.lib.swing.util.ListUtil;
import org.jphototagger.lib.swing.util.MnemonicUtil;
import org.jphototagger.lib.util.Bundle;
import org.jphototagger.lib.util.StringUtil;
import org.jphototagger.resources.UiFactory;
import org.openide.util.Lookup;

/**
 * @author Elmar Baumann
 */
public class SidecarSuffixesInfoSettingsPanel extends PanelExt {

    public static final String KEY_FILE_SUFFIXES = "ThumbnailInfo.AdditionalSidecarSuffixes";
    private static final long serialVersionUID = 1L;
    private final Preferences prefs = Lookup.getDefault().lookup(Preferences.class);
    private final SuffixListModel suffixesListModel = new SuffixListModel();

    public SidecarSuffixesInfoSettingsPanel() {
        initComponents();
        postInitComponents();
    }

    private void postInitComponents() {
        listSuffixes.addListSelectionListener(removeSuffixesListSelectionListener);
        textFieldSuffix.getDocument().addDocumentListener(addSuffixDocumentListener);
        MnemonicUtil.setMnemonics(this);
    }

    private void addSuffix() {
        String suffix = textFieldSuffix.getText();
        if (StringUtil.hasContent(suffix)) {
            suffixesListModel.addSuffix(suffix.trim());
        }
    }

    private void removeSelectedSuffixes() {
        for (String suffix : listSuffixes.getSelectedValuesList()) {
            suffixesListModel.removeSuffix(suffix);
        }
    }

    private class SuffixListModel extends DefaultListModel<String> {

        private static final long serialVersionUID = 1L;

        private SuffixListModel() {
            addElements();
        }

        private void removeSuffix(String suffix) {
            if (contains(suffix)) {
                removeElement(suffix);
                prefs.setStringCollection(KEY_FILE_SUFFIXES, ListUtil.getElements(this));
                EventBus.publish(new SidecarSuffixInfoRemovedEvent(this, suffix));
            }
        }

        private void addSuffix(String suffix) {
            if (!containsSuffix(suffix)) {
                addElement(suffix);
                prefs.setStringCollection(KEY_FILE_SUFFIXES, ListUtil.getElements(this));
                EventBus.publish(new SidecarSuffixInfoAddedEvent(this, suffix));
            }
        }

        private boolean containsSuffix(String suffix) {
            String suffixUpperCase = suffix.toUpperCase();
            for (String s : ListUtil.getElements(this)) {
                if (suffixUpperCase.equals(s.toUpperCase())) {
                    return true;
                }
            }
            return false;
        }

        private void addElements() {
            for (String suffix : prefs.getStringCollection(KEY_FILE_SUFFIXES)) {
                addElement(suffix);
            }
        }

    }

    private final DocumentListener addSuffixDocumentListener = new DocumentListener() {

        @Override
        public void insertUpdate(DocumentEvent e) {
            enableAddSuffixButton(e);
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            enableAddSuffixButton(e);
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            enableAddSuffixButton(e);
        }

        private void enableAddSuffixButton(DocumentEvent e) {
            String text = DocumentUtil.getText(e);
            buttonAddSuffix.setEnabled(StringUtil.hasContent(text));
        }

    };

    private final ListSelectionListener removeSuffixesListSelectionListener = new ListSelectionListener() {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
                buttonRemoveSuffix.setEnabled(listSuffixes.getSelectedIndex() >= 0);
            }
        }
    };

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panelContent = UiFactory.panel();
        labelInfo = UiFactory.label();
        scrollPaneSuffixes = UiFactory.scrollPane();
        listSuffixes = UiFactory.list();
        labelSuffix = UiFactory.label();
        textFieldSuffix = UiFactory.textField();
        buttonAddSuffix = UiFactory.button();
        buttonRemoveSuffix = UiFactory.button();

        setLayout(new java.awt.GridBagLayout());

        panelContent.setLayout(new java.awt.GridBagLayout());

        labelInfo.setText(Bundle.getString(getClass(), "SidecarSuffixesInfoSettingsPanel.labelInfo.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        panelContent.add(labelInfo, gridBagConstraints);

        listSuffixes.setModel(suffixesListModel);
        listSuffixes.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                listSuffixesKeyPressed(evt);
            }
        });
        scrollPaneSuffixes.setViewportView(listSuffixes);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = UiFactory.insets(5, 0, 0, 0);
        panelContent.add(scrollPaneSuffixes, gridBagConstraints);

        labelSuffix.setLabelFor(textFieldSuffix);
        labelSuffix.setText(Bundle.getString(getClass(), "SidecarSuffixesInfoSettingsPanel.labelSuffix.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = UiFactory.insets(5, 0, 0, 0);
        panelContent.add(labelSuffix, gridBagConstraints);

        textFieldSuffix.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textFieldSuffixKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = UiFactory.insets(5, 5, 0, 0);
        panelContent.add(textFieldSuffix, gridBagConstraints);

        buttonAddSuffix.setText(Bundle.getString(getClass(), "SidecarSuffixesInfoSettingsPanel.buttonAddSuffix.text")); // NOI18N
        buttonAddSuffix.setEnabled(false);
        buttonAddSuffix.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAddSuffixActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = UiFactory.insets(5, 5, 0, 0);
        panelContent.add(buttonAddSuffix, gridBagConstraints);

        buttonRemoveSuffix.setText(Bundle.getString(getClass(), "SidecarSuffixesInfoSettingsPanel.buttonRemoveSuffix.text")); // NOI18N
        buttonRemoveSuffix.setEnabled(false);
        buttonRemoveSuffix.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonRemoveSuffixActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = UiFactory.insets(5, 5, 0, 0);
        panelContent.add(buttonRemoveSuffix, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = UiFactory.insets(10, 10, 10, 10);
        add(panelContent, gridBagConstraints);
    }

    private void textFieldSuffixKeyPressed(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            addSuffix();
        }
    }

    private void buttonAddSuffixActionPerformed(java.awt.event.ActionEvent evt) {
        addSuffix();
    }

    private void buttonRemoveSuffixActionPerformed(java.awt.event.ActionEvent evt) {
        removeSelectedSuffixes();
    }

    private void listSuffixesKeyPressed(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
            removeSelectedSuffixes();
        }
    }

    private javax.swing.JButton buttonAddSuffix;
    private javax.swing.JButton buttonRemoveSuffix;
    private javax.swing.JLabel labelInfo;
    private javax.swing.JLabel labelSuffix;
    private javax.swing.JList<String> listSuffixes;
    private javax.swing.JPanel panelContent;
    private javax.swing.JScrollPane scrollPaneSuffixes;
    private javax.swing.JTextField textFieldSuffix;
}
