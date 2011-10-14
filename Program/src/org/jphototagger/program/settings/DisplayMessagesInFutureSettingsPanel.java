package org.jphototagger.program.settings;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JCheckBox;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.openide.util.Lookup;

import org.jphototagger.api.preferences.DisplayMessageInFuturePreferencesKeys;
import org.jphototagger.api.preferences.DisplayMessageInFuturePreferencesKeys.KeyInfo;
import org.jphototagger.api.preferences.Preferences;
import org.jphototagger.api.preferences.PreferencesChangedEvent;
import org.jphototagger.lib.swing.util.ComponentUtil;

/**
 * @author Elmar Baumann
 */
public class DisplayMessagesInFutureSettingsPanel extends javax.swing.JPanel {

    private static final long serialVersionUID = 1L;
    private final Map<JCheckBox, String> keyOfCheckBox = new HashMap<JCheckBox, String>();
    private final Map<String, JCheckBox> checkBoxOfKey = new HashMap<String, JCheckBox>();
    private boolean listenToCheckBox;

    public DisplayMessagesInFutureSettingsPanel() {
        initComponents();
        postInitComponents();
    }

    private void postInitComponents() {
        addCheckBoxes();
        initCheckBoxes();
        listenToCheckBox = true;
        AnnotationProcessor.process(this);
    }

    private void addCheckBoxes() {
        Collection<KeyInfo> keyInfos = lookupKeyInfos();
        int size = keyInfos.size();
        int index = 0;

        for (KeyInfo keyInfo : keyInfos) {
            JCheckBox checkBox = new JCheckBox(keyInfo.getLocalizedDisplayName());
            String key = keyInfo.getKey();

            checkBox.addActionListener(changeListener);
            keyOfCheckBox.put(checkBox, key);
            checkBoxOfKey.put(key, checkBox);
            index++;
            addCheckBox(checkBox, index == size);
        }
    }

    private Collection<KeyInfo> lookupKeyInfos() {
        Set<KeyInfo> keyInfos = new LinkedHashSet<KeyInfo>();
        Collection<? extends DisplayMessageInFuturePreferencesKeys> allKeys =
                Lookup.getDefault().lookupAll(DisplayMessageInFuturePreferencesKeys.class);

        if (allKeys == null) {
            return keyInfos;
        }

        for (DisplayMessageInFuturePreferencesKeys key : allKeys) {
            keyInfos.addAll(key.getKeyInfos());
        }

        return keyInfos;
    }

    private void addCheckBox(JCheckBox checkBox, boolean isLast) {
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weightx = 1.0;
        gbc.weighty = isLast ? 1.0 : 0.0;
        gbc.insets = new Insets(0, 5, 3, 5);

        add(checkBox, gbc);
    }

    private void initCheckBoxes() {
        Preferences prefs = Lookup.getDefault().lookup(Preferences.class);

        if (prefs == null) {
            return;
        }

        for (JCheckBox checkBox : keyOfCheckBox.keySet()) {
            String key = keyOfCheckBox.get(checkBox);
            boolean keyDefined = prefs.containsKey(key);

            checkBox.setSelected(keyDefined ? prefs.getBoolean(key) : true);
        }
    }

    @EventSubscriber(eventClass = PreferencesChangedEvent.class)
    public void userPropertyChanged(PreferencesChangedEvent evt) {
        String propertyKey = evt.getKey();

        if (AppPreferencesKeys.KEY_DISPLAY_IN_FUTURE_WARN_ON_EQUAL_BASENAMES.equals(propertyKey)) {
            listenToCheckBox = false;
            JCheckBox checkBox = checkBoxOfKey.get(propertyKey);
            if (checkBox != null) {
                boolean newValue = (Boolean) evt.getNewValue();
                checkBox.setSelected(newValue);
                ComponentUtil.forceRepaint(checkBox);
            }
            listenToCheckBox = true;
        }
    }

    private ActionListener changeListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (listenToCheckBox) {
                JCheckBox checkBox = (JCheckBox) e.getSource();
                String key = keyOfCheckBox.get(checkBox);
                Preferences prefs = Lookup.getDefault().lookup(Preferences.class);

                prefs.setBoolean(key, checkBox.isSelected());
            }
        }
    };

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {//GEN-BEGIN:initComponents

        setLayout(new java.awt.GridBagLayout());
    }//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}