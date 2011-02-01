/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jphototagger.plugin.flickrupload;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import org.jphototagger.lib.componentutil.ComponentUtil;
import org.jphototagger.lib.componentutil.MnemonicUtil;
import org.jphototagger.lib.dialog.Dialog;
import org.jphototagger.plugin.flickrupload.FlickrImageInfoPanel.ImageInfo;

/**
 *
 *
 * @author Elmar Baumann
 */
public class FlickrImageInfoDialog extends Dialog {
    private static final long     serialVersionUID = 6349275951817414186L;
    private final ResourceBundle  bundle           =
            ResourceBundle.getBundle(
                "org/jphototagger/plugin/flickrupload/Bundle");
    private final List<ImageInfo> imageInfos       = new ArrayList<ImageInfo>();
    private boolean               upload;
    private final Properties      properties;

    public FlickrImageInfoDialog() {
        super(ComponentUtil.getFrameWithIcon(), true);
        properties = null;
        initComponents();
        postInitComponents();
    }

    public FlickrImageInfoDialog(Properties properties) {
        super(ComponentUtil.getFrameWithIcon(), true);
        this.properties = properties;
        initComponents();
        postInitComponents();
    }

    private void postInitComponents() {
        MnemonicUtil.setMnemonics((Container) this);
    }

    public void addImage(ImageInfo imageInfo) {
        if (imageInfo == null) {
            throw new NullPointerException("imageInfo == null");
        }

        imageInfos.add(imageInfo);
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            addImageInfoPanels();
            readPersistent();
        } else {
            writePersistent();
        }

        super.setVisible(visible);
    }

    private void writePersistent() {
        if (properties != null) {
            new org.jphototagger.lib.util.Settings(properties).setSizeAndLocation(this);
        }
    }

    private void readPersistent() {
        if (properties != null) {
            new org.jphototagger.lib.util.Settings(properties).applySizeAndLocation(this);
        }
    }

    private void addImageInfoPanels() {
        int size  = imageInfos.size();
        int gridy = -1;

        for (int i = 0; i < size; i++) {
            FlickrImageInfoPanel panel = new FlickrImageInfoPanel(imageInfos.get(i));
            boolean              even  = i % 2 != 0;
            int                  gridx = even ? 1 : 0;

            gridy = even ? gridy : gridy + 1;
            
            GridBagConstraints gbc   = getConstraints(gridx, gridy);

            if (i >= size - 2) {
                gbc.gridheight = GridBagConstraints.REMAINDER;
                gbc.weighty    = 1.0;
                gbc.insets     = new Insets(10, gridx == 0 ? 10 : 5, 10, 10);
            }

            panelImageInfos.add(panel, gbc);
        }
    }

    private GridBagConstraints getConstraints(int gridx, int gridy) {
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.anchor  = GridBagConstraints.NORTHWEST;
        gbc.weightx = 0.5;
        gbc.gridx   = gridx;
        gbc.gridy   = gridy;
        gbc.insets  = new Insets(10, gridx == 0 ? 10 : 5, 0, 10);

        gbc.fill    = GridBagConstraints.HORIZONTAL;

        return gbc;
    }

    public boolean isUpload() {
        return upload;
    }

    public List<ImageInfo> getUploadImages() {
        List<ImageInfo> infos = new ArrayList<ImageInfo>();

        if (upload) {
            int count = panelImageInfos.getComponentCount();

            for (int i = 0; i < count; i++) {
                FlickrImageInfoPanel panel = (FlickrImageInfoPanel)
                                                panelImageInfos.getComponent(i);
                if (panel.isUpload()) {
                    infos.add(panel.getImageInfo());
                }
            }
        }

        return infos;
    }

    private void selectImages(boolean select) {
        int count = panelImageInfos.getComponentCount();

        for (int i = 0; i < count; i++) {
            FlickrImageInfoPanel panel = (FlickrImageInfoPanel)
                                                panelImageInfos.getComponent(i);
            panel.setUpload(select);
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

        scrollPane = new javax.swing.JScrollPane();
        panelImageInfos = new javax.swing.JPanel();
        buttonCancel = new javax.swing.JButton();
        buttonUpload = new javax.swing.JButton();
        buttonSelectAll = new javax.swing.JButton();
        buttonSelectNone = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/jphototagger/plugin/flickrupload/Bundle"); // NOI18N
        setTitle(bundle.getString("FlickrImageInfoDialog.title")); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        panelImageInfos.setLayout(new java.awt.GridBagLayout());
        scrollPane.setViewportView(panelImageInfos);

        buttonCancel.setText(bundle.getString("FlickrImageInfoDialog.buttonCancel.text")); // NOI18N
        buttonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCancelActionPerformed(evt);
            }
        });

        buttonUpload.setText(bundle.getString("FlickrImageInfoDialog.buttonUpload.text")); // NOI18N
        buttonUpload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonUploadActionPerformed(evt);
            }
        });

        buttonSelectAll.setText(bundle.getString("FlickrImageInfoDialog.buttonSelectAll.text")); // NOI18N
        buttonSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSelectAllActionPerformed(evt);
            }
        });

        buttonSelectNone.setText(bundle.getString("FlickrImageInfoDialog.buttonSelectNone.text")); // NOI18N
        buttonSelectNone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSelectNoneActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(scrollPane, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 494, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(buttonSelectNone)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonSelectAll)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonCancel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonUpload)))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {buttonCancel, buttonSelectAll, buttonSelectNone, buttonUpload});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonUpload)
                    .addComponent(buttonCancel)
                    .addComponent(buttonSelectAll)
                    .addComponent(buttonSelectNone))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buttonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCancelActionPerformed
        setVisible(false);
    }//GEN-LAST:event_buttonCancelActionPerformed

    private void buttonUploadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonUploadActionPerformed
        upload = true;
        setVisible(false);
    }//GEN-LAST:event_buttonUploadActionPerformed

    private void buttonSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSelectAllActionPerformed
        selectImages(true);
    }//GEN-LAST:event_buttonSelectAllActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        setVisible(false);
    }//GEN-LAST:event_formWindowClosing

    private void buttonSelectNoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSelectNoneActionPerformed
        selectImages(false);
    }//GEN-LAST:event_buttonSelectNoneActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                FlickrImageInfoDialog dialog = new FlickrImageInfoDialog();
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonCancel;
    private javax.swing.JButton buttonSelectAll;
    private javax.swing.JButton buttonSelectNone;
    private javax.swing.JButton buttonUpload;
    private javax.swing.JPanel panelImageInfos;
    private javax.swing.JScrollPane scrollPane;
    // End of variables declaration//GEN-END:variables
}