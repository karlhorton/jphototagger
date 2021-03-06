package org.jphototagger.userdefinedfilters;

import org.jphototagger.api.modules.Module;
import org.jphototagger.api.modules.ModuleDescription;
import org.jphototagger.lib.util.Bundle;

/**
 * @author Elmar Baumann
 */
public final class ModuleInstaller implements Module, ModuleDescription {

    @Override
    public void init() {
        // ignore
    }

    @Override
    public void remove() {
        // ignore
    }

    @Override
    public String getLocalizedDescription() {
        return Bundle.getString(ModuleInstaller.class, "ModuleInstaller.Description");
    }

    @Override
    public String toString() {
        return Bundle.getString(ModuleInstaller.class, "ModuleInstaller.Name");
    }
}
