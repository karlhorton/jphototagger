package org.jphototagger.program.app.update.tables.v0;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.jphototagger.api.core.Storage;
import org.jphototagger.domain.programs.Program;
import org.jphototagger.domain.repository.ProgramType;
import org.jphototagger.domain.repository.ProgramsRepository;
import org.jphototagger.lib.util.Bundle;
import org.jphototagger.program.app.SplashScreen;
import org.jphototagger.program.database.Database;
import org.openide.util.Lookup;

/**
 *
 *
 * @author Elmar Baumann
 */
final class UpdateTablesPrograms extends Database {

    private static final String KEY_OTHER_IMAGE_OPEN_APPS = "UserSettings.OtherImageOpenApps";
    private static final String KEY_DEFAULT_IMAGE_OPEN_APP = "UserSettings.DefaultImageOpenApp";
    private final ProgramsRepository repo = Lookup.getDefault().lookup(ProgramsRepository.class);

    UpdateTablesPrograms() {
    }

    void update(Connection con) throws SQLException {
        startMessage();
        moveOtherImageOpenApps();
        moveDefaultImageOpenApp();
        SplashScreen.INSTANCE.removeMessage();
    }

    private void moveDefaultImageOpenApp() {
        Storage storage = Lookup.getDefault().lookup(Storage.class);

        if (storage.containsKey(KEY_DEFAULT_IMAGE_OPEN_APP)) {
            String defaultApp = storage.getString(KEY_DEFAULT_IMAGE_OPEN_APP).trim();

            if (!defaultApp.isEmpty()) {
                File file = new File(defaultApp);
                Program defaultIoApp = new Program(file, file.getName());

                defaultIoApp.setSequenceNumber(0);

                if (repo.insertProgram(defaultIoApp)) {
                    storage.removeKey(KEY_DEFAULT_IMAGE_OPEN_APP);

                    List<Program> programs = repo.getAllPrograms(ProgramType.PROGRAM);
                    int sequenceNo = 0;

                    for (Program program : programs) {
                        if (sequenceNo > 0) {
                            program.setSequenceNumber(sequenceNo);
                            repo.updateProgram(program);
                        }

                        sequenceNo++;
                    }
                }
            }
        }
    }

    private void moveOtherImageOpenApps() {
        Storage storage = Lookup.getDefault().lookup(Storage.class);
        List<String> filepaths = storage.getStringCollection(KEY_OTHER_IMAGE_OPEN_APPS);

        if (filepaths.size() > 0) {

            for (String filepath : filepaths) {
                File file = new File(filepath);

                repo.insertProgram(new Program(file, file.getName()));
            }

            storage.removeStringCollection(KEY_OTHER_IMAGE_OPEN_APPS);
        }
    }

    private void startMessage() {
        SplashScreen.INSTANCE.setMessage(Bundle.getString(UpdateTablesPrograms.class, "UpdateTablesPrograms.Info"));
    }
}
