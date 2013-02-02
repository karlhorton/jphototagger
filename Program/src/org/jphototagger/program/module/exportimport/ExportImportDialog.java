package org.jphototagger.program.module.exportimport;


import javax.swing.SwingUtilities;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.jphototagger.lib.api.LookAndFeelChangedEvent;
import org.jphototagger.lib.swing.Dialog;
import org.jphototagger.lib.util.Bundle;
import org.jphototagger.program.module.exportimport.ExportImportPanel.ExportImportListener;
import org.jphototagger.program.resource.GUI;

/**
 * @author Elmar Baumann
 */
public class ExportImportDialog extends Dialog implements ExportImportListener {

    private static final long serialVersionUID = 1L;
    private final ExportImportContext context;

    public ExportImportDialog(ExportImportContext context) {
        super(GUI.getAppFrame());
        setPreferencesKey("ExportImportDialog");
        if (context == null) {
            throw new NullPointerException("context == null");
        }
        this.context = context;
        initComponents();
        postInitComponents();
    }

    private void postInitComponents() {
        setTitle();
        setHelpPage();
        panelExportImport.setContext(context);
        panelExportImport.addListener(this);
        AnnotationProcessor.process(this);
    }

    private void setTitle() {
        setTitle(context.equals(ExportImportContext.EXPORT)
                 ? Bundle.getString(ExportImportDialog.class, "ExportImportDialog.Title.Export")
                 : Bundle.getString(ExportImportDialog.class, "ExportImportDialog.Title.Import"));
    }

    private void setHelpPage() {
        setHelpPageUrl(context.equals(ExportImportContext.EXPORT)
                ? Bundle.getString(ExportImportDialog.class, "ExportImportDialog.HelpPage.Export")
                : Bundle.getString(ExportImportDialog.class, "ExportImportDialog.HelpPage.Import"));
    }

    @Override
    public void done() {
        setVisible(false);
    }

    @EventSubscriber(eventClass = LookAndFeelChangedEvent.class)
    public void lookAndFeelChanged(LookAndFeelChangedEvent evt) {
        SwingUtilities.updateComponentTreeUI(this);
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {//GEN-BEGIN:initComponents

        panelExportImport = new org.jphototagger.program.module.exportimport.ExportImportPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/jphototagger/program/module/exportimport/Bundle"); // NOI18N
        setTitle(bundle.getString("ExportImportDialog.title")); // NOI18N
        setName("Form"); // NOI18N

        panelExportImport.setName("panelExportImport"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelExportImport, javax.swing.GroupLayout.DEFAULT_SIZE, 463, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelExportImport, javax.swing.GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                ExportImportDialog dialog =
                        new ExportImportDialog(ExportImportContext.EXPORT);

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
    private org.jphototagger.program.module.exportimport.ExportImportPanel panelExportImport;
    // End of variables declaration//GEN-END:variables
}
