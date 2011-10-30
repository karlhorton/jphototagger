package org.jphototagger.program.module.thumbnails;

import java.awt.Component;


import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;

import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

import org.jphototagger.api.preferences.Preferences;
import org.jphototagger.api.windows.StatusLineElementProvider;
import org.jphototagger.domain.thumbnails.event.ThumbnailZoomChangedEvent;
import org.jphototagger.domain.thumbnails.event.ThumbnailsChangedEvent;
import org.jphototagger.domain.thumbnails.event.ThumbnailsSelectionChangedEvent;
import org.jphototagger.lib.util.Bundle;
import org.jphototagger.program.resource.GUI;
import org.jphototagger.program.settings.AppPreferencesKeys;

/**
 * @author Elmar Baumann
 */
@ServiceProvider(service = StatusLineElementProvider.class)
public class ThumbnailsInfoPanel extends javax.swing.JPanel implements StatusLineElementProvider {

    private static final long serialVersionUID = 1L;
    private int thumbnailZoom;
    private int thumbnailCount;
    private int selectionCount;

    public ThumbnailsInfoPanel() {
        initComponents();
        postInitComponents();
    }

    private void postInitComponents() {
        initThumbnailZoom();
        listen();
    }

    private void initThumbnailZoom() {
        Preferences preferences = Lookup.getDefault().lookup(Preferences.class);
        Integer value = preferences.getInt(AppPreferencesKeys.KEY_THUMBNAILS_ZOOM);

        thumbnailZoom = value.equals(Integer.MIN_VALUE) ? 100 : value;
    }

    private void listen() {
        AnnotationProcessor.process(this);
    }

    @EventSubscriber(eventClass = ThumbnailsSelectionChangedEvent.class)
    public void thumbnailsSelectionChanged(final ThumbnailsSelectionChangedEvent evt) {
        selectionCount = evt.getSelectionCount();
        setCount();
    }

    @EventSubscriber(eventClass = ThumbnailsChangedEvent.class)
    public void thumbnailsChanged(final ThumbnailsChangedEvent evt) {
        setCount();
    }

    @EventSubscriber(eventClass = ThumbnailZoomChangedEvent.class)
    public void thumbnailZoomChanged(ThumbnailZoomChangedEvent evt) {
        thumbnailZoom = evt.getZoomValue();
        setLabel();
    }

    private void setCount() {
        thumbnailCount = GUI.getThumbnailsPanel().getFileCount();
        setLabel();
    }

    private void setLabel() {
        String info = Bundle.getString(ThumbnailsInfoPanel.class,
                "ThumbnailsInfoPanel.Text", thumbnailCount, selectionCount, thumbnailZoom);

        thumbnailInfoLabel.setText(info);
        thumbnailInfoLabel.setToolTipText(info);
    }

    @Override
    public Component getStatusLineElement() {
        return this;
    }

    @Override
    public int getPosition() {
        return 0;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        thumbnailInfoLabel = new javax.swing.JLabel();

        setName("Form"); // NOI18N
        setLayout(new java.awt.GridBagLayout());

        thumbnailInfoLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        thumbnailInfoLabel.setName("thumbnailInfoLabel"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        add(thumbnailInfoLabel, gridBagConstraints);
    }//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel thumbnailInfoLabel;
    // End of variables declaration//GEN-END:variables
}