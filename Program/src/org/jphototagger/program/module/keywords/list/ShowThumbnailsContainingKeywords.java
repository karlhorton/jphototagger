package org.jphototagger.program.module.keywords.list;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.jphototagger.api.windows.MainWindowManager;
import org.jphototagger.api.windows.WaitDisplayer;
import org.jphototagger.domain.repository.ImageFilesRepository;
import org.jphototagger.domain.thumbnails.OriginOfDisplayedThumbnails;
import org.jphototagger.domain.thumbnails.ThumbnailsPanelSettings;
import org.jphototagger.lib.awt.EventQueueUtil;
import org.jphototagger.lib.util.Bundle;
import org.jphototagger.program.module.thumbnails.ThumbnailsPanel;
import org.jphototagger.program.resource.GUI;
import org.openide.util.Lookup;

/**
 * Displays in the {@code ThumbnailsPanel} thumbnails with specific keywords.
 *
 * @author Elmar Baumann
 */
public final class ShowThumbnailsContainingKeywords implements Runnable {

    private final ImageFilesRepository repo = Lookup.getDefault().lookup(ImageFilesRepository.class);
    private final List<String> keywords;
    private final ThumbnailsPanelSettings tnPanelSettings;

    /**
     * Creates a new instance of this class.
     *
     * @param keywords one of that keywords a image must have to be displayed
     * @param settings
     */
    public ShowThumbnailsContainingKeywords(List<String> keywords, ThumbnailsPanelSettings settings) {
        if (keywords == null) {
            throw new NullPointerException("keywords == null");
        }

        this.keywords = new ArrayList<>(keywords);
        tnPanelSettings = settings;
    }

    @Override
    public void run() {
        EventQueueUtil.invokeInDispatchThread(new Runnable() {

            @Override
            public void run() {
                WaitDisplayer waitDisplayer = Lookup.getDefault().lookup(WaitDisplayer.class);
                waitDisplayer.show();
                setFilesToThumbnailsPanel();
                waitDisplayer.hide();
            }
        });
    }

    private void setFilesToThumbnailsPanel() {
        List<File> imageFiles = new ArrayList<>(getImageFilesOfSelectedKeywords());

        if (imageFiles != null) {
            ThumbnailsPanel tnPanel = GUI.getThumbnailsPanel();

            tnPanel.setFiles(imageFiles, OriginOfDisplayedThumbnails.FILES_MATCHING_A_KEYWORD);
            tnPanel.applyThumbnailsPanelSettings(tnPanelSettings);
        }
    }

    private Set<File> getImageFilesOfSelectedKeywords() {
        // Faster than using 2 different DB queries if only 1 keyword is
        // selected
        if (keywords.size() == 1) {
            setTitle(keywords.get(0));

            return repo.findImageFilesContainingDcSubject(keywords.get(0), false);
        } else if (keywords.size() > 1) {
            setTitle(keywords);

            return repo.findImageFilesContainingSomeOfDcSubjects(keywords);
        }

        return null;
    }

    private void setTitle(List<String> keywords) {
        String keywordPathString = KeywordsListControllerUtil.keywordPathString(keywords);
        String title = Bundle.getString(ShowThumbnailsContainingKeywords.class, "ShowThumbnailsContainingKeywords.AppFrame.Title.Keywords.Path",
                keywordPathString);
        MainWindowManager mainWindowManager = Lookup.getDefault().lookup(MainWindowManager.class);
        mainWindowManager.setMainWindowTitle(title);
    }

    private void setTitle(String keyword) {
        String title = Bundle.getString(ShowThumbnailsContainingKeywords.class, "ShowThumbnailsContainingKeywords.AppFrame.Title.Keyword",
                keyword);
        MainWindowManager mainWindowManager = Lookup.getDefault().lookup(MainWindowManager.class);
        mainWindowManager.setMainWindowTitle(title);
    }
}
