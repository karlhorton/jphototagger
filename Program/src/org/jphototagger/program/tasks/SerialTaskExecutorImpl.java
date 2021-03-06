package org.jphototagger.program.tasks;

import org.jphototagger.api.concurrent.SerialTaskExecutor;
import org.openide.util.lookup.ServiceProvider;

/**
 * @author Elmar Baumann
 */
@ServiceProvider(service = SerialTaskExecutor.class)
public final class SerialTaskExecutorImpl implements SerialTaskExecutor {

    @Override
    public void addTask(Runnable runnable) {
        JptSerialExecutor.INSTANCE.add(runnable);
    }

    @Override
    public int getTaskCount() {
        return JptSerialExecutor.INSTANCE.getCount();
    }

    @Override
    public void cancelAllTasks() {
        JptSerialExecutor.INSTANCE.cancelCurrentTasks();
    }
}
