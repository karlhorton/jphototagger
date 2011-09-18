package org.jphototagger.program.controller.thumbnail;

import java.io.File;
import java.util.Collections;

import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;

import org.jphototagger.domain.repository.event.imagefiles.ImageFileDeletedEvent;
import org.jphototagger.domain.repository.event.xmp.XmpDeletedEvent;
import org.jphototagger.domain.repository.event.xmp.XmpInsertedEvent;
import org.jphototagger.domain.repository.event.xmp.XmpUpdatedEvent;
import org.jphototagger.domain.thumbnails.event.ThumbnailUpdatedEvent;
import org.jphototagger.lib.awt.EventQueueUtil;
import org.jphototagger.program.cache.ThumbnailCache;
import org.jphototagger.program.cache.XmpCache;
import org.jphototagger.program.resource.GUI;

/**
 *
 * @author Elmar Baumann
 */
public final class ThumbnailsRepositoryChangesController {

    public ThumbnailsRepositoryChangesController() {
        listen();
    }

    private void listen() {
        AnnotationProcessor.process(this);
    }

    private void updateXmpCache(final File imageFile) {
        EventQueueUtil.invokeInDispatchThread(new Runnable() {

            @Override
            public void run() {
                XmpCache.INSTANCE.remove(imageFile);
                XmpCache.INSTANCE.notifyUpdate(imageFile);
            }
        });
    }

    @EventSubscriber(eventClass = XmpInsertedEvent.class)
    public void xmpInserted(XmpInsertedEvent evt) {
        updateXmpCache(evt.getImageFile());
    }

    @EventSubscriber(eventClass = XmpUpdatedEvent.class)
    public void xmpUpdated(XmpUpdatedEvent evt) {
        updateXmpCache(evt.getImageFile());
    }

    @EventSubscriber(eventClass = XmpDeletedEvent.class)
    public void xmpDeleted(XmpDeletedEvent evt) {
        updateXmpCache(evt.getImageFile());
    }

    @EventSubscriber(eventClass = ThumbnailUpdatedEvent.class)
    public void thumbnailUpdated(ThumbnailUpdatedEvent evt) {
        final File imageFile = evt.getImageFile();

        EventQueueUtil.invokeInDispatchThread(new Runnable() {

            @Override
            public void run() {
                ThumbnailCache.INSTANCE.remove(imageFile);
                ThumbnailCache.INSTANCE.notifyUpdate(imageFile);
            }
        });
    }

    @EventSubscriber(eventClass = ImageFileDeletedEvent.class)
    public void imageFileDeleted(final ImageFileDeletedEvent evt) {
        EventQueueUtil.invokeInDispatchThread(new Runnable() {

            @Override
            public void run() {
                GUI.getThumbnailsPanel().removeFiles(Collections.singleton(evt.getImageFile()));
            }
        });
    }
}