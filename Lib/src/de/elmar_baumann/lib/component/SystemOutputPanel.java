/*
 * JPhotoTagger tags and finds images fast.
 * Copyright (C) 2009-2010 by the JPhotoTagger developer team.
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package de.elmar_baumann.lib.component;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

// Most parts of code from http://unserializableone.blogspot.com/2009/01/redirecting-systemout-and-systemerr-to.html
/**
 * Writes an the system's standard output to a text area.
 *
 * Starts after calling {@link #caputure()}.
 *
 * @author  Elmar Baumann
 * @version 2009-05-31
 */
public class SystemOutputPanel extends JPanel {

    private static volatile int     MAX_CHAR_COUNT   = 100000;
    private static volatile int     MAX_CHARS_EXESS  = 1000;
    private static final    long    serialVersionUID = -6566007491609362675L;
    private                 boolean capture;

    /**
     * Default constructor.
     *
     * Call later {@link #caputure()}.
     */
    public SystemOutputPanel() {
        initComponents();
    }

    /**
     * Starts to capture the system's standard output and standard error stream.
     * Redirects it via {@link System#setOut(java.io.PrintStream)} and
     * {@link System#setErr(java.io.PrintStream)}.
     */
    public synchronized void caputure() {
        if (!capture) {
            redirectSystemStreams();
        }
    }

    private void copyToClipboard() {
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
                new StringSelection(textArea.getText()), null);
    }

    private void deleteText() {
        textArea.setText("");
    }

    private void updateTextArea(final String text) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                textArea.append(text);
                textArea.setCaretPosition(textArea.getDocument().getLength());
                int excess = textArea.getDocument().getLength() - MAX_CHAR_COUNT;
                if (excess >= MAX_CHARS_EXESS) {
                    textArea.replaceRange("", 0, excess);
                }
            }
        });
    }

    private void redirectSystemStreams() {
        OutputStream out = new OutputStream() {

            @Override
            public void write(int b) throws IOException {
                updateTextArea(String.valueOf((char) b));
            }

            @Override
            public void write(byte[] b, int off, int len) throws IOException {
                updateTextArea(new String(b, off, len));
            }

            @Override
            public void write(byte[] b) throws IOException {
                write(b, 0, b.length);
            }
        };

        System.setOut(new PrintStream(out, true));
        System.setErr(new PrintStream(out, true));
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
        textArea = new javax.swing.JTextArea();
        buttonDelete = new javax.swing.JButton();
        buttonCopyToClipboard = new javax.swing.JButton();

        textArea.setColumns(1);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setTabSize(4);
        textArea.setWrapStyleWord(true);
        scrollPane.setViewportView(textArea);

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("de/elmar_baumann/lib/resource/properties/Bundle");
        buttonDelete.setText(bundle.getString("SystemOutputPanel.buttonDelete.text"));
        buttonDelete.setToolTipText(bundle.getString("SystemOutputPanel.buttonDelete.toolTipText"));
        buttonDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonDeleteActionPerformed(evt);
            }
        });

        buttonCopyToClipboard.setText(bundle.getString("SystemOutputPanel.buttonCopyToClipboard.text"));
        buttonCopyToClipboard.setToolTipText(bundle.getString("SystemOutputPanel.buttonCopyToClipboard.toolTipText"));
        buttonCopyToClipboard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCopyToClipboardActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(buttonDelete)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonCopyToClipboard))
            .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonCopyToClipboard)
                    .addComponent(buttonDelete)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void buttonCopyToClipboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCopyToClipboardActionPerformed
        copyToClipboard();
    }//GEN-LAST:event_buttonCopyToClipboardActionPerformed

    private void buttonDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDeleteActionPerformed
        deleteText();
    }//GEN-LAST:event_buttonDeleteActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonCopyToClipboard;
    private javax.swing.JButton buttonDelete;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JTextArea textArea;
    // End of variables declaration//GEN-END:variables
}
