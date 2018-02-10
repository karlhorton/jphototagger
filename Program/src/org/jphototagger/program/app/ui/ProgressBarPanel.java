package org.jphototagger.program.app.ui;

import java.awt.event.MouseListener;
import java.util.Set;
import org.jphototagger.api.concurrent.Cancelable;
import org.jphototagger.api.progress.ProgressEvent;
import org.jphototagger.api.progress.ProgressHandle;
import org.jphototagger.domain.event.listener.ListenerSupport;
import org.jphototagger.lib.awt.EventQueueUtil;

/**
 * @author Elmar Baumann
 */
public class ProgressBarPanel extends javax.swing.JPanel implements ProgressHandle {

    private static final long serialVersionUID = 1L;
    private final ListenerSupport<ProgressBarPanelListener> ls = new ListenerSupport<>();
    private final Cancelable cancelable;

    public ProgressBarPanel() {
        this(null);
    }

    public ProgressBarPanel(Cancelable cancelable) {
        this.cancelable = cancelable;
        initComponents();
    }

    @Override
    public void progressStarted(final ProgressEvent evt) {
        setName("Progress bar for " + evt.getSource());
        EventQueueUtil.invokeInDispatchThread(new Runnable() {

            @Override
            public void run() {
                notifyProgressStarted();
                setEventToProgressBar(evt);
            }
        });
    }

    @Override
    public void progressPerformed(final ProgressEvent evt) {
        EventQueueUtil.invokeInDispatchThread(new Runnable() {

            @Override
            public void run() {
                setEventToProgressBar(evt);
            }
        });
    }

    @Override
    public void progressEnded() {
        EventQueueUtil.invokeInDispatchThread(new Runnable() {

            @Override
            public void run() {
                progressBar.setValue(0);
                progressBar.setString("");
                progressBar.setStringPainted(false);
                progressBar.setIndeterminate(false);
                buttonCancelProgress.setEnabled(false);
                notifyProgressEnded();
            }
        });
    }

    private void setEventToProgressBar(final ProgressEvent evt) {
        if (evt.isIndeterminate()) {
            progressBar.setIndeterminate(true);
        } else {
            progressBar.setMinimum(evt.getMinimum());
            progressBar.setMaximum(evt.getMaximum());
            progressBar.setValue(evt.getValue());
        }
        progressBar.setStringPainted(evt.isStringPainted());
        progressBar.setString(evt.getStringToPaint());
    }

    private void cancel() {
        if (cancelable != null) {
            cancelable.cancel();
            buttonCancelProgress.setEnabled(false);
        }
    }

    void addProgressBarPanelListener(ProgressBarPanelListener listener) {
        ls.add(listener);
    }

    void removeProgressBarPanelListener(ProgressBarPanelListener listener) {
        ls.remove(listener);
    }

    private void notifyProgressStarted() {
        Set<ProgressBarPanelListener> listeners = ls.get();
        for (ProgressBarPanelListener listener : listeners) {
            listener.progressStarted(this);
        }
    }

    private void notifyProgressEnded() {
        Set<ProgressBarPanelListener> listeners = ls.get();
        for (ProgressBarPanelListener listener : listeners) {
            listener.progressEnded(this);
        }
    }

    /**
     * Adds the mouse listener to the progress bar too.
     * @param l
     */
    @Override
    public void addMouseListener(MouseListener l) {
        super.addMouseListener(l);
        progressBar.addMouseListener(l);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        progressBar = new javax.swing.JProgressBar();
        buttonCancelProgress = new javax.swing.JButton();

        setName("Form"); // NOI18N
        setLayout(new java.awt.GridBagLayout());

        progressBar.setMaximumSize(new java.awt.Dimension(300, 14));
        progressBar.setName("progressBar"); // NOI18N
        progressBar.setPreferredSize(new java.awt.Dimension(250, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weighty = 1.0;
        add(progressBar, gridBagConstraints);

        buttonCancelProgress.setIcon(org.jphototagger.resources.Icons.getIcon("icon_cancel.png"));
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/jphototagger/program/app/ui/Bundle"); // NOI18N
        buttonCancelProgress.setToolTipText(bundle.getString("ProgressBarPanel.buttonCancelProgress.toolTipText")); // NOI18N
        buttonCancelProgress.setBorder(null);
        buttonCancelProgress.setContentAreaFilled(false);
        buttonCancelProgress.setEnabled(cancelable != null);
        buttonCancelProgress.setName("buttonCancelProgress"); // NOI18N
        buttonCancelProgress.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCancelProgressActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        add(buttonCancelProgress, gridBagConstraints);
    }//GEN-END:initComponents

    private void buttonCancelProgressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCancelProgressActionPerformed
        cancel();
    }//GEN-LAST:event_buttonCancelProgressActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonCancelProgress;
    private javax.swing.JProgressBar progressBar;
    // End of variables declaration//GEN-END:variables
}
