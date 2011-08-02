package org.jphototagger.program.factory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jphototagger.lib.awt.EventQueueUtil;
import org.jphototagger.lib.componentutil.MessageLabel;
import org.jphototagger.lib.util.Bundle;
import org.jphototagger.program.resource.GUI;

/**
 *
 *
 * @author Elmar Baumann
 */
final class Support {

    private final Map<Class<?>, List<Object>> OBJECT_INSTANCES_OF_CLASS = new HashMap<Class<?>, List<Object>>();
    private static final Logger LOGGER = Logger.getLogger(Support.class.getName());

    @SuppressWarnings("unchecked")
    synchronized <T> List<T> getAll(Class<T> clazz) {
        return Collections.unmodifiableList((List<T>) OBJECT_INSTANCES_OF_CLASS.get(clazz));
    }

    @SuppressWarnings("unchecked")
    synchronized <T> T getFirst(Class<T> clazz) {
        List<T> instances = (List<T>) OBJECT_INSTANCES_OF_CLASS.get(clazz);

        return (instances == null)
               ? null
               : instances.get(0);
    }

    synchronized void add(Object instance) {
        List<Object> instances = OBJECT_INSTANCES_OF_CLASS.get(instance.getClass());

        if (instances == null) {
            instances = new ArrayList<Object>();
            OBJECT_INSTANCES_OF_CLASS.put(instance.getClass(), instances);
        }

        instances.add(instance);
    }

    static void setStatusbarInfo(final String message) {
        EventQueueUtil.invokeInDispatchThread(new Runnable() {
            @Override
            public void run() {
                GUI.getAppPanel().setStatusbarText(message, MessageLabel.MessageType.INFO, 2000);
            }
        });
    }

    /**
     * Checks whether a factory is initialized more than one times. Logs an
     * error message if a factory was previously initialized.
     *
     * @param  c    Factory class
     * @param  init true if the factory is already initialized
     * @return      true if not initialized
     */
    static boolean checkInit(Class<?> c, boolean init) {
        if (init) {
            LOGGER.log(Level.WARNING, "{0}: Initalized Meta-Factory again!", c.getName());

            return false;
        }

        return true;
    }
}
