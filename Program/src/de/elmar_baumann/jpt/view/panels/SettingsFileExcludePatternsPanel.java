/*
 * JPhotoTagger tags and finds images fast
 * Copyright (C) 2009 by the developer team, resp. Elmar Baumann<eb@elmar-baumann.de>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package de.elmar_baumann.jpt.view.panels;

import de.elmar_baumann.jpt.database.DatabaseFileExcludePattern;
import de.elmar_baumann.jpt.event.ProgressEvent;
import de.elmar_baumann.jpt.event.listener.ProgressListener;
import de.elmar_baumann.jpt.model.ListModelFileExcludePatterns;
import de.elmar_baumann.jpt.resource.Bundle;
import de.elmar_baumann.jpt.types.Persistence;
import java.awt.event.KeyEvent;
import java.util.List;

/**
 *
 * @author  Elmar Baumann <eb@elmar-baumann.de>
 * @version 2008-11-02
 */
public final class SettingsFileExcludePatternsPanel extends javax.swing.JPanel
        implements ProgressListener, Persistence {

    private static final String ADD_INFO_TEXT =
            Bundle.getString("FileExcludePatternPanel.AddInfoText"); // NOI18N
    private final DatabaseFileExcludePattern db =
            DatabaseFileExcludePattern.INSTANCE;
    private final ListModelFileExcludePatterns model =
            new ListModelFileExcludePatterns();
    private boolean isUpdateDatabase = false;
    private boolean stop = false;

    /** Creates new form SettingsFileExcludePatternsPanel */
    public SettingsFileExcludePatternsPanel() {
        initComponents();
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            stop = false;
            isUpdateDatabase = false;
            setEnabledButtons();
        } else {
            cancelUpdateDatabase();
        }
        super.setVisible(visible);
    }

    private void deletePattern() {
        String pattern = (String) listPattern.getSelectedValue();
        model.deletePattern(pattern);
        setEnabledButtons();
    }

    private void insertPattern() {
        String input = textFieldInputPattern.getText().trim();
        if (canInsertPattern(input)) {
            model.insertPattern(input);
        }
        setEnabledButtons();
    }

    private boolean canInsertPattern(String input) {
        return !input.isEmpty() && !input.equals(ADD_INFO_TEXT);
    }

    private void setEnabledButtonInsertPattern() {
        buttonInsertPattern.setEnabled(
                canInsertPattern(textFieldInputPattern.getText().trim()));
    }

    private void setEnabledButtons() {
        int size = model.getSize();
        buttonDeletePattern.setEnabled(size > 0);
        setEnabledButtonInsertPattern();
        buttonUpdateDatabase.setEnabled(size > 0 && !isUpdateDatabase);
        buttonCancelUpdateDatabase.setEnabled(isUpdateDatabase);
    }

    private void setSelectedPatternToInput() {
        String pattern = (String) listPattern.getSelectedValue();
        if (pattern != null) {
            textFieldInputPattern.setText(pattern);
        }
    }

    private void updateDatabase() {
        List<String> patterns = model.getPatterns();
        if (patterns.size() > 0) {
            isUpdateDatabase = true;
            stop = false;
            setEnabledButtons();
            db.deleteFilesWithPattern(patterns, this);
        }
    }

    private void cancelUpdateDatabase() {
        stop = true;
    }

    private void checkStopEvent(ProgressEvent evt) {
        if (stop) {
            evt.stop();
        }
    }

    @Override
    public void progressStarted(ProgressEvent evt) {
        progressBarUpdateDatabase.setMinimum(evt.getMinimum());
        progressBarUpdateDatabase.setMaximum(evt.getMaximum());
        progressBarUpdateDatabase.setValue(evt.getValue());
        checkStopEvent(evt);
    }

    @Override
    public void progressPerformed(ProgressEvent evt) {
        progressBarUpdateDatabase.setValue(evt.getValue());
        checkStopEvent(evt);
    }

    @Override
    public void progressEnded(ProgressEvent evt) {
        progressBarUpdateDatabase.setValue(evt.getValue());
        isUpdateDatabase = false;
        stop = false;
        setEnabledButtons();
    }

    @Override
    public void readProperties() {
    }

    @Override
    public void writeProperties() {
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelInfoList = new javax.swing.JLabel();
        scrollPaneListPattern = new javax.swing.JScrollPane();
        listPattern = new javax.swing.JList();
        textFieldInputPattern = new javax.swing.JTextField();
        buttonDeletePattern = new javax.swing.JButton();
        buttonInsertPattern = new javax.swing.JButton();
        labelInfoDatabase = new javax.swing.JLabel();
        progressBarUpdateDatabase = new javax.swing.JProgressBar();
        buttonCancelUpdateDatabase = new javax.swing.JButton();
        buttonUpdateDatabase = new javax.swing.JButton();

        labelInfoList.setText(Bundle.getString("SettingsFileExcludePatternsPanel.labelInfoList.text")); // NOI18N

        listPattern.setModel(model);
        listPattern.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        listPattern.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                listPatternValueChanged(evt);
            }
        });
        scrollPaneListPattern.setViewportView(listPattern);

        textFieldInputPattern.setText(ADD_INFO_TEXT);
        textFieldInputPattern.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textFieldInputPatternKeyReleased(evt);
            }
        });

        buttonDeletePattern.setMnemonic('e');
        buttonDeletePattern.setText(Bundle.getString("SettingsFileExcludePatternsPanel.buttonDeletePattern.text")); // NOI18N
        buttonDeletePattern.setToolTipText(Bundle.getString("SettingsFileExcludePatternsPanel.buttonDeletePattern.toolTipText")); // NOI18N
        buttonDeletePattern.setEnabled(false);
        buttonDeletePattern.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonDeletePatternActionPerformed(evt);
            }
        });

        buttonInsertPattern.setMnemonic('h');
        buttonInsertPattern.setText(Bundle.getString("SettingsFileExcludePatternsPanel.buttonInsertPattern.text")); // NOI18N
        buttonInsertPattern.setToolTipText(Bundle.getString("SettingsFileExcludePatternsPanel.buttonInsertPattern.toolTipText")); // NOI18N
        buttonInsertPattern.setEnabled(false);
        buttonInsertPattern.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonInsertPatternActionPerformed(evt);
            }
        });

        labelInfoDatabase.setForeground(new java.awt.Color(0, 0, 255));
        labelInfoDatabase.setText(Bundle.getString("SettingsFileExcludePatternsPanel.labelInfoDatabase.text")); // NOI18N

        buttonCancelUpdateDatabase.setText(Bundle.getString("SettingsFileExcludePatternsPanel.buttonCancelUpdateDatabase.text")); // NOI18N
        buttonCancelUpdateDatabase.setEnabled(false);
        buttonCancelUpdateDatabase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCancelUpdateDatabaseActionPerformed(evt);
            }
        });

        buttonUpdateDatabase.setMnemonic('d');
        buttonUpdateDatabase.setText(Bundle.getString("SettingsFileExcludePatternsPanel.buttonUpdateDatabase.text")); // NOI18N
        buttonUpdateDatabase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonUpdateDatabaseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(progressBarUpdateDatabase, javax.swing.GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE)
                    .addComponent(scrollPaneListPattern, javax.swing.GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE)
                    .addComponent(labelInfoList)
                    .addComponent(textFieldInputPattern, javax.swing.GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(buttonDeletePattern)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonInsertPattern))
                    .addComponent(labelInfoDatabase, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(buttonCancelUpdateDatabase)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonUpdateDatabase)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelInfoList)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollPaneListPattern, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textFieldInputPattern, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonInsertPattern)
                    .addComponent(buttonDeletePattern))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelInfoDatabase, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(progressBarUpdateDatabase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonUpdateDatabase)
                    .addComponent(buttonCancelUpdateDatabase))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

private void listPatternValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_listPatternValueChanged
    setSelectedPatternToInput();
    setEnabledButtons();
}//GEN-LAST:event_listPatternValueChanged

private void textFieldInputPatternKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textFieldInputPatternKeyReleased
    if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
        insertPattern();
    } else {
        setEnabledButtonInsertPattern();
    }
}//GEN-LAST:event_textFieldInputPatternKeyReleased

private void buttonCancelUpdateDatabaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCancelUpdateDatabaseActionPerformed
    cancelUpdateDatabase();
}//GEN-LAST:event_buttonCancelUpdateDatabaseActionPerformed

private void buttonDeletePatternActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDeletePatternActionPerformed
    deletePattern();
}//GEN-LAST:event_buttonDeletePatternActionPerformed

private void buttonUpdateDatabaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonUpdateDatabaseActionPerformed
    updateDatabase();
}//GEN-LAST:event_buttonUpdateDatabaseActionPerformed

private void buttonInsertPatternActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonInsertPatternActionPerformed
    insertPattern();
}//GEN-LAST:event_buttonInsertPatternActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonCancelUpdateDatabase;
    private javax.swing.JButton buttonDeletePattern;
    private javax.swing.JButton buttonInsertPattern;
    private javax.swing.JButton buttonUpdateDatabase;
    private javax.swing.JLabel labelInfoDatabase;
    private javax.swing.JLabel labelInfoList;
    private javax.swing.JList listPattern;
    private javax.swing.JProgressBar progressBarUpdateDatabase;
    private javax.swing.JScrollPane scrollPaneListPattern;
    private javax.swing.JTextField textFieldInputPattern;
    // End of variables declaration//GEN-END:variables
}