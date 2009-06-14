package de.elmar_baumann.imv.view.panels;

import de.elmar_baumann.imv.app.AppIcons;
import de.elmar_baumann.imv.UserSettings;
import de.elmar_baumann.imv.database.DatabaseImageFiles;
import de.elmar_baumann.imv.event.ProgressEvent;
import de.elmar_baumann.imv.event.ProgressListener;
import de.elmar_baumann.imv.resource.Bundle;
import de.elmar_baumann.imv.tasks.DatabaseCompress;
import de.elmar_baumann.imv.tasks.RecordsWithNotExistingFilesDeleter;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import javax.swing.Icon;
import javax.swing.JLabel;

/**
 * Database maintainance tasks.
 * 
 * @author  Elmar Baumann <eb@elmar-baumann.de>
 * @version 2008/11/08
 */
public final class DatabaseMaintainancePanel extends javax.swing.JPanel
        implements ProgressListener {

    private static final Icon iconFinished = AppIcons.getIcon(
            "icon_finished.png"); // NOI18N
    private final Stack<Runnable> runnables = new Stack<Runnable>();
    private final Map<Class, JLabel> finishedLabelOfRunnable =
            new HashMap<Class, JLabel>();
    private volatile boolean stop = false;
    private volatile boolean canClose = true;

    /** Creates new form DatabaseMaintainancePanel */
    public DatabaseMaintainancePanel() {
        initComponents();
        postInitComponents();
    }

    private void postInitComponents() {
        finishedLabelOfRunnable.put(DatabaseCompress.class,
                labelFinishedCompressDatabase);
        finishedLabelOfRunnable.put(DatabaseImageFiles.class,
                labelFinishedDeleteRecordsOfNotExistingFilesInDatabase);
    }

    private void setProgressbarStart(ProgressEvent evt) {
        if (evt.isIndeterminate()) {
            progressBar.setIndeterminate(true);
        } else {
            progressBar.setMinimum(evt.getMinimum());
            progressBar.setMaximum(evt.getMaximum());
            progressBar.setValue(evt.getValue());
        }
    }

    private void setProgressbarEnd(ProgressEvent evt) {
        progressBar.setIndeterminate(false);
        progressBar.setValue(evt.getValue());
    }

    public void getsVisible(boolean visible) {
        if (visible) {
            resetIcons();
            setEnabledButtonStartMaintain();
        }
    }

    private void resetIcons() {
        labelFinishedCompressDatabase.setIcon(null);
        labelFinishedDeleteRecordsOfNotExistingFilesInDatabase.setIcon(null);
        progressBar.setValue(0);
    }

    private void setEnabledButtonStartMaintain() {
        buttonStartMaintain.setEnabled(
                checkBoxCompressDatabase.isSelected() ||
                checkBoxDeleteRecordsOfNotExistingFilesInDatabase.isSelected());
    }

    private void setCanClose(boolean can) {
        canClose = can;
    }

    public boolean canClose() {
        return canClose;
    }

    private synchronized void startNextThread() {
        if (runnables.size() > 0) {
            Thread thread = new Thread(runnables.pop());
            thread.setPriority(UserSettings.INSTANCE.getThreadPriority());
            thread.setName("Database maintainance next task" + " @ " + // NOI18N
                    getClass().getName());
            thread.start();
        }
    }

    private void setRunnablesAreRunning(boolean running) {
        buttonAbortAction.setEnabled(running);
        buttonStartMaintain.setEnabled(!running);
        setCanClose(!running);
    }

    private void startMaintain() {
        addRunnables();
        setRunnablesAreRunning(true);
        stop = false;
        startNextThread();
    }

    private void addRunnables() {
        runnables.clear();
        // reverse order of checkboxes because the runnables are in a stack
        if (checkBoxCompressDatabase.isSelected()) {
            DatabaseCompress databaseCompress = new DatabaseCompress();
            databaseCompress.addProgressListener(this);
            runnables.push(databaseCompress);
        }
        if (checkBoxDeleteRecordsOfNotExistingFilesInDatabase.isSelected()) {
            RecordsWithNotExistingFilesDeleter deleter =
                    new RecordsWithNotExistingFilesDeleter();
            deleter.addProgressListener(this);
            runnables.push(deleter);
        }
    }

    private void checkStopEvent(ProgressEvent evt) {
        if (stop) {
            evt.stop();
        }
    }

    @Override
    public void progressStarted(ProgressEvent evt) {
        labelMessage.setText(evt.getInfo().toString());
        buttonAbortAction.setEnabled(true);
        setProgressbarStart(evt);
        buttonAbortAction.setEnabled(
                !(evt.getSource() instanceof DatabaseCompress));
        checkStopEvent(evt);
    }

    @Override
    public void progressPerformed(ProgressEvent evt) {
        progressBar.setValue(evt.getValue());
        checkStopEvent(evt);
    }

    @Override
    public void progressEnded(ProgressEvent evt) {
        setProgressbarEnd(evt);
        labelMessage.setText(evt.getInfo().toString());
        Object source = evt.getSource();
        assert source != null;
        Class sourceClass = source.getClass();
        JLabel labelFinished = finishedLabelOfRunnable.get(sourceClass);
        if (labelFinished != null) {
            labelFinished.setIcon(iconFinished);
        }
        if (runnables.size() > 0) {
            startNextThread();
        } else {
            setRunnablesAreRunning(false);
        }
    }

    private void handleButtonAbortActionPerformed() {
        stop = true;
        synchronized (runnables) {
            runnables.clear();
            setRunnablesAreRunning(false);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelMaintainanceTasks = new javax.swing.JPanel();
        checkBoxDeleteRecordsOfNotExistingFilesInDatabase = new javax.swing.JCheckBox();
        labelFinishedDeleteRecordsOfNotExistingFilesInDatabase = new javax.swing.JLabel();
        checkBoxCompressDatabase = new javax.swing.JCheckBox();
        labelFinishedCompressDatabase = new javax.swing.JLabel();
        panelMaintainMessages = new javax.swing.JPanel();
        labelMessage = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        buttonAbortAction = new javax.swing.JButton();
        buttonStartMaintain = new javax.swing.JButton();

        panelMaintainanceTasks.setBorder(javax.swing.BorderFactory.createTitledBorder(Bundle.getString("DatabaseMaintainancePanel.panelMaintainanceTasks.border.title"))); // NOI18N

        checkBoxDeleteRecordsOfNotExistingFilesInDatabase.setMnemonic('e');
        checkBoxDeleteRecordsOfNotExistingFilesInDatabase.setText(Bundle.getString("DatabaseMaintainancePanel.checkBoxDeleteRecordsOfNotExistingFilesInDatabase.text")); // NOI18N
        checkBoxDeleteRecordsOfNotExistingFilesInDatabase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxDeleteRecordsOfNotExistingFilesInDatabaseActionPerformed(evt);
            }
        });

        labelFinishedDeleteRecordsOfNotExistingFilesInDatabase.setPreferredSize(new java.awt.Dimension(16, 16));

        checkBoxCompressDatabase.setMnemonic('k');
        checkBoxCompressDatabase.setText(Bundle.getString("DatabaseMaintainancePanel.checkBoxCompressDatabase.text")); // NOI18N
        checkBoxCompressDatabase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxCompressDatabaseActionPerformed(evt);
            }
        });

        labelFinishedCompressDatabase.setPreferredSize(new java.awt.Dimension(16, 16));

        javax.swing.GroupLayout panelMaintainanceTasksLayout = new javax.swing.GroupLayout(panelMaintainanceTasks);
        panelMaintainanceTasks.setLayout(panelMaintainanceTasksLayout);
        panelMaintainanceTasksLayout.setHorizontalGroup(
            panelMaintainanceTasksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMaintainanceTasksLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelMaintainanceTasksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(checkBoxDeleteRecordsOfNotExistingFilesInDatabase)
                    .addComponent(checkBoxCompressDatabase))
                .addGap(18, 18, 18)
                .addGroup(panelMaintainanceTasksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelFinishedCompressDatabase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelFinishedDeleteRecordsOfNotExistingFilesInDatabase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelMaintainanceTasksLayout.setVerticalGroup(
            panelMaintainanceTasksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMaintainanceTasksLayout.createSequentialGroup()
                .addComponent(checkBoxDeleteRecordsOfNotExistingFilesInDatabase)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(checkBoxCompressDatabase))
            .addGroup(panelMaintainanceTasksLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelFinishedDeleteRecordsOfNotExistingFilesInDatabase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addComponent(labelFinishedCompressDatabase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        panelMaintainMessages.setBorder(javax.swing.BorderFactory.createTitledBorder(Bundle.getString("DatabaseMaintainancePanel.panelMaintainMessages.border.title"))); // NOI18N

        labelMessage.setPreferredSize(new java.awt.Dimension(0, 16));

        javax.swing.GroupLayout panelMaintainMessagesLayout = new javax.swing.GroupLayout(panelMaintainMessages);
        panelMaintainMessages.setLayout(panelMaintainMessagesLayout);
        panelMaintainMessagesLayout.setHorizontalGroup(
            panelMaintainMessagesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMaintainMessagesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelMessage, javax.swing.GroupLayout.DEFAULT_SIZE, 343, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelMaintainMessagesLayout.setVerticalGroup(
            panelMaintainMessagesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMaintainMessagesLayout.createSequentialGroup()
                .addComponent(labelMessage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        buttonAbortAction.setMnemonic('o');
        buttonAbortAction.setText(Bundle.getString("DatabaseMaintainancePanel.buttonAbortAction.text")); // NOI18N
        buttonAbortAction.setEnabled(false);
        buttonAbortAction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAbortActionActionPerformed(evt);
            }
        });

        buttonStartMaintain.setMnemonic('s');
        buttonStartMaintain.setText(Bundle.getString("DatabaseMaintainancePanel.buttonStartMaintain.text")); // NOI18N
        buttonStartMaintain.setEnabled(false);
        buttonStartMaintain.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonStartMaintainActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelMaintainMessages, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelMaintainanceTasks, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(buttonAbortAction)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonStartMaintain))
                    .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, 375, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelMaintainanceTasks, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelMaintainMessages, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonStartMaintain)
                    .addComponent(buttonAbortAction))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

private void checkBoxDeleteRecordsOfNotExistingFilesInDatabaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxDeleteRecordsOfNotExistingFilesInDatabaseActionPerformed
    setEnabledButtonStartMaintain();
}//GEN-LAST:event_checkBoxDeleteRecordsOfNotExistingFilesInDatabaseActionPerformed

private void checkBoxCompressDatabaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxCompressDatabaseActionPerformed
    setEnabledButtonStartMaintain();
}//GEN-LAST:event_checkBoxCompressDatabaseActionPerformed

private void buttonStartMaintainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonStartMaintainActionPerformed
    startMaintain();
}//GEN-LAST:event_buttonStartMaintainActionPerformed

private void buttonAbortActionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAbortActionActionPerformed
    handleButtonAbortActionPerformed();
}//GEN-LAST:event_buttonAbortActionActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonAbortAction;
    private javax.swing.JButton buttonStartMaintain;
    private javax.swing.JCheckBox checkBoxCompressDatabase;
    private javax.swing.JCheckBox checkBoxDeleteRecordsOfNotExistingFilesInDatabase;
    private javax.swing.JLabel labelFinishedCompressDatabase;
    private javax.swing.JLabel labelFinishedDeleteRecordsOfNotExistingFilesInDatabase;
    private javax.swing.JLabel labelMessage;
    private javax.swing.JPanel panelMaintainMessages;
    private javax.swing.JPanel panelMaintainanceTasks;
    private javax.swing.JProgressBar progressBar;
    // End of variables declaration//GEN-END:variables
}
