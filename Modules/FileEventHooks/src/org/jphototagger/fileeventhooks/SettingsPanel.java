package org.jphototagger.fileeventhooks;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.UIManager;

import org.openide.util.Lookup;

import org.jphototagger.api.preferences.Preferences;
import org.jphototagger.lib.swing.FileChooserHelper;
import org.jphototagger.lib.swing.FileChooserProperties;
import org.jphototagger.lib.io.FileUtil;
import org.jphototagger.lib.util.Bundle;
import org.jphototagger.lib.util.StringUtil;

/**
 * @author Elmar Baumann
 */
public class SettingsPanel extends javax.swing.JPanel {

    private static final long serialVersionUID = 1L;
    private final Map<JTextField, String> keyOfTextField = new HashMap<JTextField, String>(4);
    private final Map<JButton, JTextField> textFieldOfRemoveButton = new HashMap<JButton, JTextField>(4);
    private static final String LAST_CHOOSEN_DIR_KEY = "Module.FileEventHooks.LastChoosenDir";
    private String lastChoosenDirectory;
    private static final Color TEXTFIELD_FOREGROUND = getTextFieldForeground();

    public SettingsPanel() {
        initComponents();
        postInitComponents();
    }

    private static Color getTextFieldForeground() {
        Color foreground = UIManager.getColor("TextField.foreground");

        return foreground == null ? Color.BLACK : foreground;
    }

    private void postInitComponents() {
        initKeyOfTextFieldMap();
        initTextFieldOfRemoveButtonMap();
        initLastChoosenDirectory();
        setScriptsToTextFields();
    }

    private void initKeyOfTextFieldMap() {
        keyOfTextField.put(textFieldFileCopied, FileEventHooksPreferencesKeys.FILE_COPIED_KEY);
        keyOfTextField.put(textFieldFileDeleted, FileEventHooksPreferencesKeys.FILE_DELETED_KEY);
        keyOfTextField.put(textFieldFileMoved, FileEventHooksPreferencesKeys.FILE_MOVED_KEY);
        keyOfTextField.put(textFieldFileRenamed, FileEventHooksPreferencesKeys.FILE_RENAMED_KEY);
    }

    private void initTextFieldOfRemoveButtonMap() {
        textFieldOfRemoveButton.put(buttonRemoveScriptFileCopied, textFieldFileCopied);
        textFieldOfRemoveButton.put(buttonRemoveScriptFileDeleted, textFieldFileDeleted);
        textFieldOfRemoveButton.put(buttonRemoveScriptFileMoved, textFieldFileMoved);
        textFieldOfRemoveButton.put(buttonRemoveScriptFileRenamed, textFieldFileRenamed);
    }

    private void initLastChoosenDirectory() {
        Preferences preferences = Lookup.getDefault().lookup(Preferences.class);
        String prefString = preferences.getString(LAST_CHOOSEN_DIR_KEY);

        lastChoosenDirectory = StringUtil.hasContent(prefString) ? prefString : "";
    }

    private void setScriptsToTextFields() {
        Preferences preferences = Lookup.getDefault().lookup(Preferences.class);

        setScriptToTextField(preferences.getString(FileEventHooksPreferencesKeys.FILE_COPIED_KEY), textFieldFileCopied);
        setScriptToTextField(preferences.getString(FileEventHooksPreferencesKeys.FILE_DELETED_KEY), textFieldFileDeleted);
        setScriptToTextField(preferences.getString(FileEventHooksPreferencesKeys.FILE_MOVED_KEY), textFieldFileMoved);
        setScriptToTextField(preferences.getString(FileEventHooksPreferencesKeys.FILE_RENAMED_KEY), textFieldFileRenamed);
        setRemoveButtonsEnabled();
    }

    private void setScriptToTextField(String script, JTextField textField) {
        if (!StringUtil.hasContent(script)) {
            return;
        }

        boolean fileExists = FileUtil.existsFile(new File(script));

        textField.setText(script);
        textField.setForeground(fileExists ? TEXTFIELD_FOREGROUND : Color.RED);
    }

    private void chooseScript(JTextField textField) {
        File scriptFile = chooseScriptFile();

        if (scriptFile == null) {
            return;
        }

        Preferences preferences = Lookup.getDefault().lookup(Preferences.class);
        String key = keyOfTextField.get(textField);
        String scriptFilePath = scriptFile.getAbsolutePath();

        preferences.setString(key, scriptFilePath);
        textField.setText(scriptFilePath);
        setRemoveButtonsEnabled();
    }

    private File chooseScriptFile() {
        FileChooserProperties props = new FileChooserProperties();

        props.currentDirectoryPath(lastChoosenDirectory);
        props.dialogTitle(Bundle.getString(SettingsPanel.class, "SettingsPanel.FileChooser.Title"));
        props.multiSelectionEnabled(false);
        props.fileSelectionMode(JFileChooser.FILES_ONLY);
        props.propertyKeyPrefix("Module.FileEventHooks.FileChooser");

        File scriptFile = FileChooserHelper.chooseFile(props);

        if (scriptFile != null) {
            String dirOfScriptFile = scriptFile.getParent();

            if (dirOfScriptFile != null) {
                lastChoosenDirectory = dirOfScriptFile;

                Preferences preferences = Lookup.getDefault().lookup(Preferences.class);

                preferences.setString(LAST_CHOOSEN_DIR_KEY, lastChoosenDirectory);
            }
        }

        return scriptFile;
    }

    private void setRemoveButtonsEnabled() {
        for (JButton button : textFieldOfRemoveButton.keySet()) {
            JTextField textField = textFieldOfRemoveButton.get(button);
            String script = textField.getText();
            button.setEnabled(StringUtil.hasContent(script));
        }
    }

    private final Action removeScriptFileAction = new AbstractAction() {
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();

            if (source instanceof JButton) {
                JButton button = (JButton) source;
                JTextField textField = textFieldOfRemoveButton.get(button);
                if (textField != null) {
                    textField.setText("");
                    String key = keyOfTextField.get(textField);
                    if (key != null) {
                        Preferences preferences = Lookup.getDefault().lookup(Preferences.class);
                        preferences.removeKey(key);
                    }
                    setRemoveButtonsEnabled();
                }
            }
        }
    };

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        labelGeneralInfo = new org.jdesktop.swingx.JXLabel();
        panelFileCopied = new javax.swing.JPanel();
        textFieldFileCopied = new javax.swing.JTextField();
        buttonChooseScriptFileCopied = new javax.swing.JButton();
        buttonRemoveScriptFileCopied = new javax.swing.JButton();
        labelParameterInfoFileCopied = new org.jdesktop.swingx.JXLabel();
        panelFileRenamed = new javax.swing.JPanel();
        textFieldFileRenamed = new javax.swing.JTextField();
        buttonChooseScriptFileRenamed = new javax.swing.JButton();
        buttonRemoveScriptFileRenamed = new javax.swing.JButton();
        labelParameterInfoFileRenamed = new org.jdesktop.swingx.JXLabel();
        panelFileMoved = new javax.swing.JPanel();
        textFieldFileMoved = new javax.swing.JTextField();
        buttonChooseScriptFileMoved = new javax.swing.JButton();
        buttonRemoveScriptFileMoved = new javax.swing.JButton();
        labelParameterInfoFileMoved = new org.jdesktop.swingx.JXLabel();
        panelFileDeleted = new javax.swing.JPanel();
        textFieldFileDeleted = new javax.swing.JTextField();
        buttonChooseScriptFileDeleted = new javax.swing.JButton();
        buttonRemoveScriptFileDeleted = new javax.swing.JButton();
        labelParameterInfoFileDeleted = new org.jdesktop.swingx.JXLabel();
        labelPathInfo = new org.jdesktop.swingx.JXLabel();

        setName("Form"); // NOI18N
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });
        setLayout(new java.awt.GridBagLayout());

        labelGeneralInfo.setLineWrap(true);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/jphototagger/fileeventhooks/Bundle"); // NOI18N
        labelGeneralInfo.setText(bundle.getString("SettingsPanel.labelGeneralInfo.text")); // NOI18N
        labelGeneralInfo.setName("labelGeneralInfo"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 10);
        add(labelGeneralInfo, gridBagConstraints);

        panelFileCopied.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("SettingsPanel.panelFileCopied.border.title"))); // NOI18N
        panelFileCopied.setName("panelFileCopied"); // NOI18N
        panelFileCopied.setLayout(new java.awt.GridBagLayout());

        textFieldFileCopied.setEditable(false);
        textFieldFileCopied.setName("textFieldFileCopied"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        panelFileCopied.add(textFieldFileCopied, gridBagConstraints);

        buttonChooseScriptFileCopied.setText(bundle.getString("SettingsPanel.buttonChooseScriptFileCopied.text")); // NOI18N
        buttonChooseScriptFileCopied.setName("buttonChooseScriptFileCopied"); // NOI18N
        buttonChooseScriptFileCopied.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonChooseScriptFileCopiedActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        panelFileCopied.add(buttonChooseScriptFileCopied, gridBagConstraints);

        buttonRemoveScriptFileCopied.setAction(removeScriptFileAction);
        buttonRemoveScriptFileCopied.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/jphototagger/fileeventhooks/icons/icon_delete.png"))); // NOI18N
        buttonRemoveScriptFileCopied.setToolTipText(bundle.getString("SettingsPanel.buttonRemoveScriptFileCopied.toolTipText")); // NOI18N
        buttonRemoveScriptFileCopied.setEnabled(false);
        buttonRemoveScriptFileCopied.setName("buttonRemoveScriptFileCopied"); // NOI18N
        buttonRemoveScriptFileCopied.setPreferredSize(new java.awt.Dimension(16, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        panelFileCopied.add(buttonRemoveScriptFileCopied, gridBagConstraints);

        labelParameterInfoFileCopied.setText(bundle.getString("SettingsPanel.labelParameterInfoFileCopied.text")); // NOI18N
        labelParameterInfoFileCopied.setName("labelParameterInfoFileCopied"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panelFileCopied.add(labelParameterInfoFileCopied, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 10);
        add(panelFileCopied, gridBagConstraints);

        panelFileRenamed.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("SettingsPanel.panelFileRenamed.border.title"))); // NOI18N
        panelFileRenamed.setName("panelFileRenamed"); // NOI18N
        panelFileRenamed.setLayout(new java.awt.GridBagLayout());

        textFieldFileRenamed.setEditable(false);
        textFieldFileRenamed.setName("textFieldFileRenamed"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        panelFileRenamed.add(textFieldFileRenamed, gridBagConstraints);

        buttonChooseScriptFileRenamed.setText(bundle.getString("SettingsPanel.buttonChooseScriptFileRenamed.text")); // NOI18N
        buttonChooseScriptFileRenamed.setName("buttonChooseScriptFileRenamed"); // NOI18N
        buttonChooseScriptFileRenamed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonChooseScriptFileRenamedActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        panelFileRenamed.add(buttonChooseScriptFileRenamed, gridBagConstraints);

        buttonRemoveScriptFileRenamed.setAction(removeScriptFileAction);
        buttonRemoveScriptFileRenamed.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/jphototagger/fileeventhooks/icons/icon_delete.png"))); // NOI18N
        buttonRemoveScriptFileRenamed.setToolTipText(bundle.getString("SettingsPanel.buttonRemoveScriptFileRenamed.toolTipText")); // NOI18N
        buttonRemoveScriptFileRenamed.setEnabled(false);
        buttonRemoveScriptFileRenamed.setName("buttonRemoveScriptFileRenamed"); // NOI18N
        buttonRemoveScriptFileRenamed.setPreferredSize(new java.awt.Dimension(16, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        panelFileRenamed.add(buttonRemoveScriptFileRenamed, gridBagConstraints);

        labelParameterInfoFileRenamed.setText(bundle.getString("SettingsPanel.labelParameterInfoFileRenamed.text")); // NOI18N
        labelParameterInfoFileRenamed.setName("labelParameterInfoFileRenamed"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panelFileRenamed.add(labelParameterInfoFileRenamed, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 10);
        add(panelFileRenamed, gridBagConstraints);

        panelFileMoved.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("SettingsPanel.panelFileMoved.border.title"))); // NOI18N
        panelFileMoved.setName("panelFileMoved"); // NOI18N
        panelFileMoved.setLayout(new java.awt.GridBagLayout());

        textFieldFileMoved.setEditable(false);
        textFieldFileMoved.setName("textFieldFileMoved"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        panelFileMoved.add(textFieldFileMoved, gridBagConstraints);

        buttonChooseScriptFileMoved.setText(bundle.getString("SettingsPanel.buttonChooseScriptFileMoved.text")); // NOI18N
        buttonChooseScriptFileMoved.setName("buttonChooseScriptFileMoved"); // NOI18N
        buttonChooseScriptFileMoved.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonChooseScriptFileMovedActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        panelFileMoved.add(buttonChooseScriptFileMoved, gridBagConstraints);

        buttonRemoveScriptFileMoved.setAction(removeScriptFileAction);
        buttonRemoveScriptFileMoved.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/jphototagger/fileeventhooks/icons/icon_delete.png"))); // NOI18N
        buttonRemoveScriptFileMoved.setToolTipText(bundle.getString("SettingsPanel.buttonRemoveScriptFileMoved.toolTipText")); // NOI18N
        buttonRemoveScriptFileMoved.setEnabled(false);
        buttonRemoveScriptFileMoved.setName("buttonRemoveScriptFileMoved"); // NOI18N
        buttonRemoveScriptFileMoved.setPreferredSize(new java.awt.Dimension(16, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        panelFileMoved.add(buttonRemoveScriptFileMoved, gridBagConstraints);

        labelParameterInfoFileMoved.setText(bundle.getString("SettingsPanel.labelParameterInfoFileMoved.text")); // NOI18N
        labelParameterInfoFileMoved.setName("labelParameterInfoFileMoved"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panelFileMoved.add(labelParameterInfoFileMoved, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 10);
        add(panelFileMoved, gridBagConstraints);

        panelFileDeleted.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("SettingsPanel.panelFileDeleted.border.title"))); // NOI18N
        panelFileDeleted.setName("panelFileDeleted"); // NOI18N
        panelFileDeleted.setLayout(new java.awt.GridBagLayout());

        textFieldFileDeleted.setEditable(false);
        textFieldFileDeleted.setName("textFieldFileDeleted"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        panelFileDeleted.add(textFieldFileDeleted, gridBagConstraints);

        buttonChooseScriptFileDeleted.setText(bundle.getString("SettingsPanel.buttonChooseScriptFileDeleted.text")); // NOI18N
        buttonChooseScriptFileDeleted.setName("buttonChooseScriptFileDeleted"); // NOI18N
        buttonChooseScriptFileDeleted.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonChooseScriptFileDeletedActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        panelFileDeleted.add(buttonChooseScriptFileDeleted, gridBagConstraints);

        buttonRemoveScriptFileDeleted.setAction(removeScriptFileAction);
        buttonRemoveScriptFileDeleted.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/jphototagger/fileeventhooks/icons/icon_delete.png"))); // NOI18N
        buttonRemoveScriptFileDeleted.setToolTipText(bundle.getString("SettingsPanel.buttonRemoveScriptFileDeleted.toolTipText")); // NOI18N
        buttonRemoveScriptFileDeleted.setEnabled(false);
        buttonRemoveScriptFileDeleted.setName("buttonRemoveScriptFileDeleted"); // NOI18N
        buttonRemoveScriptFileDeleted.setPreferredSize(new java.awt.Dimension(16, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        panelFileDeleted.add(buttonRemoveScriptFileDeleted, gridBagConstraints);

        labelParameterInfoFileDeleted.setText(bundle.getString("SettingsPanel.labelParameterInfoFileDeleted.text")); // NOI18N
        labelParameterInfoFileDeleted.setName("labelParameterInfoFileDeleted"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panelFileDeleted.add(labelParameterInfoFileDeleted, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 10);
        add(panelFileDeleted, gridBagConstraints);

        labelPathInfo.setText(bundle.getString("SettingsPanel.labelPathInfo.text")); // NOI18N
        labelPathInfo.setName("labelPathInfo"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        add(labelPathInfo, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void buttonChooseScriptFileCopiedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonChooseScriptFileCopiedActionPerformed
        chooseScript(textFieldFileCopied);
    }//GEN-LAST:event_buttonChooseScriptFileCopiedActionPerformed

    private void buttonChooseScriptFileRenamedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonChooseScriptFileRenamedActionPerformed
        chooseScript(textFieldFileRenamed);
    }//GEN-LAST:event_buttonChooseScriptFileRenamedActionPerformed

    private void buttonChooseScriptFileMovedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonChooseScriptFileMovedActionPerformed
        chooseScript(textFieldFileMoved);
    }//GEN-LAST:event_buttonChooseScriptFileMovedActionPerformed

    private void buttonChooseScriptFileDeletedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonChooseScriptFileDeletedActionPerformed
        chooseScript(textFieldFileDeleted);
    }//GEN-LAST:event_buttonChooseScriptFileDeletedActionPerformed

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        setScriptsToTextFields();
    }//GEN-LAST:event_formComponentShown
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonChooseScriptFileCopied;
    private javax.swing.JButton buttonChooseScriptFileDeleted;
    private javax.swing.JButton buttonChooseScriptFileMoved;
    private javax.swing.JButton buttonChooseScriptFileRenamed;
    private javax.swing.JButton buttonRemoveScriptFileCopied;
    private javax.swing.JButton buttonRemoveScriptFileDeleted;
    private javax.swing.JButton buttonRemoveScriptFileMoved;
    private javax.swing.JButton buttonRemoveScriptFileRenamed;
    private org.jdesktop.swingx.JXLabel labelGeneralInfo;
    private org.jdesktop.swingx.JXLabel labelParameterInfoFileCopied;
    private org.jdesktop.swingx.JXLabel labelParameterInfoFileDeleted;
    private org.jdesktop.swingx.JXLabel labelParameterInfoFileMoved;
    private org.jdesktop.swingx.JXLabel labelParameterInfoFileRenamed;
    private org.jdesktop.swingx.JXLabel labelPathInfo;
    private javax.swing.JPanel panelFileCopied;
    private javax.swing.JPanel panelFileDeleted;
    private javax.swing.JPanel panelFileMoved;
    private javax.swing.JPanel panelFileRenamed;
    private javax.swing.JTextField textFieldFileCopied;
    private javax.swing.JTextField textFieldFileDeleted;
    private javax.swing.JTextField textFieldFileMoved;
    private javax.swing.JTextField textFieldFileRenamed;
    // End of variables declaration//GEN-END:variables
}
