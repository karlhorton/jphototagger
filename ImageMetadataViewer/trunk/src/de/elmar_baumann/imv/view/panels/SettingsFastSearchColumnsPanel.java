package de.elmar_baumann.imv.view.panels;

import de.elmar_baumann.imv.UserSettings;
import de.elmar_baumann.imv.database.metadata.ColumnUtil;
import de.elmar_baumann.imv.database.metadata.selections.FastSearchColumns;
import de.elmar_baumann.imv.event.ListenerProvider;
import de.elmar_baumann.imv.event.UserSettingsChangeEvent;
import de.elmar_baumann.imv.model.ListModelSelectedColumns;
import de.elmar_baumann.imv.resource.Bundle;
import de.elmar_baumann.imv.types.Persistence;
import de.elmar_baumann.lib.component.CheckList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

/**
 *
 * @author  Elmar Baumann <eb@elmar-baumann.de>
 */
public final class SettingsFastSearchColumnsPanel extends javax.swing.JPanel
    implements ActionListener, Persistence {

    private final CheckList list = new CheckList();
    private final ListModelSelectedColumns model = new ListModelSelectedColumns(
        FastSearchColumns.get());
    private final ListenerProvider listenerProvider = ListenerProvider.getInstance();

    /** Creates new form SettingsFastSearchColumnsPanel */
    public SettingsFastSearchColumnsPanel() {
        initComponents();
        postInitComponents();
    }

    private void postInitComponents() {
        list.setModel(model);
        list.setSelectionMode(
            ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        list.addActionListener(this);
    }

    @Override
    public void readPersistent() {
        list.setSelectedItemsWithText(
            ColumnUtil.getDescriptionsOfColumns(
            UserSettings.getInstance().getFastSearchColumns()), true);
    }

    @Override
    public void writePersistent() {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        listenerProvider.notifyUserSettingsChangeListener(getUserSettingsChangeEvent());
    }

    private UserSettingsChangeEvent getUserSettingsChangeEvent() {
        boolean selected = list.getSelectionCount() > 0;
        UserSettingsChangeEvent evt = new UserSettingsChangeEvent(selected
            ? UserSettingsChangeEvent.Type.FastSearchColumns
            : UserSettingsChangeEvent.Type.NoFastSearchColumns, this);
        evt.setFastSearchColumns(ColumnUtil.getSelectedColumns(list));
        evt.setNoFastSearchColumns(!selected);
        return evt;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelFastSearchColumns = new javax.swing.JLabel();
        scrollPaneFastSearchColumns = new JScrollPane(list);

        labelFastSearchColumns.setFont(new java.awt.Font("Dialog", 0, 11));
        labelFastSearchColumns.setText(Bundle.getString("SettingsFastSearchColumnsPanel.labelFastSearchColumns.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelFastSearchColumns)
                .addContainerGap(35, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(scrollPaneFastSearchColumns, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelFastSearchColumns)
                .addContainerGap(147, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addGap(38, 38, 38)
                    .addComponent(scrollPaneFastSearchColumns, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE)
                    .addContainerGap()))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel labelFastSearchColumns;
    private javax.swing.JScrollPane scrollPaneFastSearchColumns;
    // End of variables declaration//GEN-END:variables
}
