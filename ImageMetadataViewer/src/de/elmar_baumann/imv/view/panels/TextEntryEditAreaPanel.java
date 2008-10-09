package de.elmar_baumann.imv.view.panels;

import de.elmar_baumann.imv.data.TextEntry;
import de.elmar_baumann.imv.database.metadata.Column;
import de.elmar_baumann.imv.resource.Bundle;
import de.elmar_baumann.lib.component.TabLeavingTextArea;
import java.awt.Color;

/**
 * Panel zum Eingeben mehrzeiliger Texte.
 * 
 * @author  Elmar Baumann <eb@elmar-baumann.de>
 * @version 2008/09/18
 */
public class TextEntryEditAreaPanel extends javax.swing.JPanel
    implements TextEntry {

    private Column column;
    private Color editableBackground;

    public TextEntryEditAreaPanel(Column column) {
        this.column = column;
        initComponents();
        editableBackground = textAreaEdit.getBackground();
        setPropmt();
    }

    @Override
    public String getText() {
        return textAreaEdit.getText();
    }

    @Override
    public void setText(String text) {
        textAreaEdit.setText(text);
    }

    @Override
    public Column getColumn() {
        return column;
    }

    private void setPropmt() {
        labelPrompt.setText(column.getDescription());
    }

    @Override
    public boolean isEmpty() {
        return textAreaEdit.getText().isEmpty();
    }

    @Override
    public void focus() {
        textAreaEdit.requestFocus();
    }

    @Override
    public void setEditable(boolean editable) {
        textAreaEdit.setEditable(editable);
        textAreaEdit.setBackground(editable ? editableBackground : getBackground());
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        labelPrompt = new javax.swing.JLabel();
        scrollPaneEdit = new javax.swing.JScrollPane();
        textAreaEdit = new TabLeavingTextArea();

        setLayout(new java.awt.GridBagLayout());

        labelPrompt.setText(Bundle.getString("TextEntryEditAreaPanel.labelPrompt.text")); // NOI18N
        labelPrompt.setToolTipText(column.getLongerDescription());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weighty = 1.0;
        add(labelPrompt, gridBagConstraints);

        textAreaEdit.setColumns(20);
        textAreaEdit.setLineWrap(true);
        textAreaEdit.setRows(2);
        textAreaEdit.setTabSize(4);
        textAreaEdit.setWrapStyleWord(true);
        scrollPaneEdit.setViewportView(textAreaEdit);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(scrollPaneEdit, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel labelPrompt;
    private javax.swing.JScrollPane scrollPaneEdit;
    public javax.swing.JTextArea textAreaEdit;
    // End of variables declaration//GEN-END:variables
}
