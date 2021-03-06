package org.jphototagger.program.app;

import org.openide.util.lookup.ServiceProvider;

/**
 * @author Elmar Baumann
 */
@ServiceProvider(service = org.jphototagger.api.applifecycle.AppStartupLock.class)
public final class AppStartupLockImpl implements org.jphototagger.api.applifecycle.AppStartupLock {

    @Override
    public boolean forceStartupLock() {
        return AppStartupLock.forceLock();
    }

    @Override
    public boolean isStartupLocked() {
        return AppStartupLock.isLocked();
    }

    @Override
    public boolean lockStartup() {
        return AppStartupLock.lock();
    }

    @Override
    public boolean unlockStartup() {
        return AppStartupLock.unlock();
    }
}
