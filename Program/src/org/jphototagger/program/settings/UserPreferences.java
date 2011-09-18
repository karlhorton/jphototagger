package org.jphototagger.program.settings;

import java.io.File;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jphototagger.api.file.FilenameTokens;
import org.jphototagger.domain.repository.FileRepositoryProvider;
import org.jphototagger.lib.util.PropertiesFile;
import org.jphototagger.lib.util.Settings;
import org.jphototagger.program.app.AppInfo;
import org.jphototagger.program.app.update.UpdateUserSettings;

/**
 * Stores user settings in a single {@code java.util.Properties} instance.
 *
 * @author Elmar Baumann
 */
final class UserPreferences {

    private static final String DOMAIN_NAME = "de.elmar_baumann"; // When changing see comment for AppInfo.PROJECT_NAME
    // NEVER CHANGE PROPERTIES_FILENAME!
    private static final String PROPERTIES_FILENAME = "Settings.properties";
    private final Properties properties = new Properties();
    private final PropertiesFile propertiesFile = new PropertiesFile(DOMAIN_NAME, AppInfo.PROJECT_NAME, PROPERTIES_FILENAME, properties);
    private final Settings settings = new Settings(properties);
    static final UserPreferences INSTANCE = new UserPreferences();

    private UserPreferences() {
        propertiesFile.readFromFile();
        UpdateUserSettings.update(properties);
        settings.removeKeysWithEmptyValues();
        writeToFile();
    }

    Settings getSettings() {
        return settings;
    }

    void writeToFile() {
        try {
            propertiesFile.writeToFile();
        } catch (Exception ex) {
            Logger.getLogger(UserPreferences.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    String getSettingsDirectoryName() {
        return propertiesFile.getDirectoryName();
    }

    String getDatabaseDirectoryName() {
        return properties.containsKey(FileRepositoryProvider.KEY_FILE_REPOSITORY_DIRECTORY)
                ? settings.getString(FileRepositoryProvider.KEY_FILE_REPOSITORY_DIRECTORY)
                : getDefaultDatabaseDirectoryName();
    }

    String getDefaultDatabaseDirectoryName() {
        return getSettingsDirectoryName();
    }

    String getDatabaseFileName(FilenameTokens name) {
        if (name == null) {
            throw new NullPointerException("name == null");
        }

        if (!name.equals(FilenameTokens.FULL_PATH) && !name.equals(FilenameTokens.FULL_PATH_NO_SUFFIX)) {
            throw new IllegalArgumentException("Illegal argument: " + name);
        }

        String directoryName = getDatabaseDirectoryName();
        String fileBasename = getDatabaseBasename();
        boolean isFullPath = name.equals(FilenameTokens.FULL_PATH);
        String suffix = isFullPath ? ".data" : "";

        return directoryName + File.separator + fileBasename + suffix;
    }

    static String getDatabaseBasename() {
        return "database";
    }

    String getThumbnailsDirectoryName() {
        return getDatabaseDirectoryName() + File.separator + getThumbnailsDirectoryBasename();
    }

    static String getThumbnailsDirectoryBasename() {
        return "thumbnails";
    }
}