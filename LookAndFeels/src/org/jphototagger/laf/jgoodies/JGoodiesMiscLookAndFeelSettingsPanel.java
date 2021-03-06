package org.jphototagger.laf.jgoodies;

import java.awt.Component;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import org.bushe.swing.event.EventBus;
import org.jphototagger.api.preferences.Preferences;
import org.jphototagger.lib.api.LookAndFeelChangedEvent;
import org.jphototagger.lib.swing.PanelExt;
import org.jphototagger.lib.util.Bundle;
import org.jphototagger.lib.util.SystemUtil;
import org.jphototagger.resources.UiFactory;
import org.openide.util.Lookup;

/**
 * @author Elmar Baumann
 */
public class JGoodiesMiscLookAndFeelSettingsPanel extends PanelExt {

    private static final long serialVersionUID = 1L;
    private static final Map<String, String> PREF_KEY_CLASSNAMES = new LinkedHashMap<>();
    public static final String WINDOWS_LAF_CLASSNAME = "com.jgoodies.looks.windows.WindowsLookAndFeel";
    private boolean listen = true;

    static {
        PREF_KEY_CLASSNAMES.put("JGoodiesMiscLookAndFeel.PlasticLookAndFeel", "com.jgoodies.looks.plastic.PlasticLookAndFeel");
        PREF_KEY_CLASSNAMES.put("JGoodiesMiscLookAndFeel.Plastic3DLookAndFeel", "com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
        PREF_KEY_CLASSNAMES.put("JGoodiesMiscLookAndFeel.PlasticXPLookAndFeel", "com.jgoodies.looks.plastic.PlasticXPLookAndFeel");
        PREF_KEY_CLASSNAMES.put("JGoodiesMiscLookAndFeel.WindowsLookAndFeel", WINDOWS_LAF_CLASSNAME);
    }

    public JGoodiesMiscLookAndFeelSettingsPanel() {
        initComponents();
        postInitComponents();
    }

    private void postInitComponents() {
        restoreLookAndFeel();
    }

    private void changeLookAndFeel() {
        persistLookAndFeel();
        JGoodiesMiscLookAndFeelProvider provider = new JGoodiesMiscLookAndFeelProvider();
        provider.setLookAndFeel();
        EventBus.publish(new LookAndFeelChangedEvent(this, provider));
    }

    private void persistLookAndFeel() {
        Preferences prefs = Lookup.getDefault().lookup(Preferences.class);
        prefs.setString(JGoodiesMiscLookAndFeelProvider.PREF_KEY, (String) comboBoxClassNames.getSelectedItem());
    }

    private void restoreLookAndFeel() {
        Preferences prefs = Lookup.getDefault().lookup(Preferences.class);
        String laf = prefs.containsKey(JGoodiesMiscLookAndFeelProvider.PREF_KEY)
                ? prefs.getString(JGoodiesMiscLookAndFeelProvider.PREF_KEY)
                : JGoodiesMiscLookAndFeelProvider.DEFAULT_LAF_CLASSNAME;
        listen = false;
        comboBoxClassNames.setSelectedItem(laf);
        listen = true;
    }

    private final ComboBoxModel<String> classnamesComboBoxModel = new DefaultComboBoxModel<String>() {

        private static final long serialVersionUID = 1L;

        {
            for (String key : PREF_KEY_CLASSNAMES.keySet()) {
                String classname = PREF_KEY_CLASSNAMES.get(key);
                boolean isWindowsLafClassname = WINDOWS_LAF_CLASSNAME.equals(classname);
                boolean add = !isWindowsLafClassname || isWindowsLafClassname && SystemUtil.isWindows();
                if (add) {
                    addElement(classname);
                }
            }
        }

    };

    private final ListCellRenderer<String> classnamesListCellRenderer = new ListCellRenderer<String> () {

        private final DefaultListCellRenderer delegate = new DefaultListCellRenderer();

        @Override
        public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) delegate.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            label.setText(Bundle.getString(JGoodiesMiscLookAndFeelSettingsPanel.class, value));
            return label;
        }

    };

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        labelClassNames = UiFactory.label();
        comboBoxClassNames = UiFactory.comboBox();

        setLayout(new java.awt.GridBagLayout());

        labelClassNames.setLabelFor(comboBoxClassNames);
        labelClassNames.setText(Bundle.getString(getClass(), "JGoodiesMiscLookAndFeelSettingsPanel.labelClassNames.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = UiFactory.insets(5, 5, 5, 0);
        add(labelClassNames, gridBagConstraints);

        comboBoxClassNames.setModel(classnamesComboBoxModel);
        comboBoxClassNames.setRenderer(classnamesListCellRenderer);
        comboBoxClassNames.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxClassNamesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = UiFactory.insets(5, 5, 5, 5);
        add(comboBoxClassNames, gridBagConstraints);
    }

    private void comboBoxClassNamesActionPerformed(java.awt.event.ActionEvent evt) {
        if (listen) {
            changeLookAndFeel();
        }
    }

    private javax.swing.JComboBox<String> comboBoxClassNames;
    private javax.swing.JLabel labelClassNames;
}
