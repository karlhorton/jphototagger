package org.jphototagger.program.app.update;

import java.util.Arrays;
import java.util.Collection;
import org.jphototagger.api.windows.MainWindowMenuProvider;
import org.jphototagger.api.windows.MenuItemProvider;
import org.jphototagger.lib.api.MainWindowMenuProviderAdapter;
import org.jphototagger.lib.api.MenuItemProviderImpl;
import org.openide.util.lookup.ServiceProvider;

/**
 * @author Elmar Baumann
 */
@ServiceProvider(service = MainWindowMenuProvider.class)
public final class MenuLayer extends MainWindowMenuProviderAdapter {

    @Override
    public Collection<? extends MenuItemProvider> getHelpMenuItems() {
        return Arrays.asList(new MenuItemProviderImpl(new UpdateCheckAction(), 1000, true));
    }
}
