package org.jphototagger.laf.defaults;

import java.awt.Component;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import org.jphototagger.api.windows.LookAndFeelProvider;
import org.jphototagger.laf.LafUtil;
import org.jphototagger.lib.util.Bundle;
import org.openide.util.lookup.ServiceProvider;

/**
 * @author Elmar Baumann
 */
@ServiceProvider(service = LookAndFeelProvider.class)
public final class WindowsClassicLookAndFeelProvider implements LookAndFeelProvider {

    private static final String LAF_CLASSNAME = LafUtil.findLookuAndFeel("WindowsClassicLookAndFeel", "com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
    private static final Logger LOGGER = Logger.getLogger(SystemLookAndFeelProvider.class.getName());

    @Override
    public String getDisplayname() {
        return Bundle.getString(WindowsClassicLookAndFeelProvider.class, "WindowsClassicLookAndFeelProvider.Displayname");
    }

    @Override
    public String getDescription() {
        return Bundle.getString(WindowsClassicLookAndFeelProvider.class, "WindowsClassicLookAndFeelProvider.Description");
    }

    @Override
    public Component getPreferencesComponent() {
        return null;
    }

    @Override
    public String getPreferencesKey() {
        return "WindowsClassicLookAndFeelProvider"; // Do never change this!
    }

    @Override
    public boolean canInstall() {
        return LafUtil.canInstall(LAF_CLASSNAME);
    }

    @Override
    public void setLookAndFeel() {
        LOGGER.info("Setting Java Windows Classic Look and Feel");
        try {
            UIManager.setLookAndFeel(LAF_CLASSNAME);
        } catch (Throwable t) {
            LOGGER.log(Level.SEVERE, null, t);
        }
    }

    @Override
    public int getPosition() {
        return 10000;
    }
}
