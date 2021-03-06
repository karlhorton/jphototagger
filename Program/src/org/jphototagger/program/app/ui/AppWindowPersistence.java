package org.jphototagger.program.app.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JList;
import javax.swing.JTree;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.jphototagger.api.applifecycle.AppWillExitEvent;
import org.jphototagger.api.preferences.Preferences;
import org.jphototagger.api.preferences.PreferencesHints;
import org.jphototagger.lib.awt.EventQueueUtil;
import org.jphototagger.program.module.keywords.KeywordsPanel;
import org.jphototagger.program.resource.GUI;
import org.openide.util.Lookup;

/**
 * @author Elmar Baumann
 */
public final class AppWindowPersistence implements ComponentListener {

    // Strings has to be equals to that in AppPanel!
    private static final String KEY_DIVIDER_LOCATION_MAIN = "AppPanel.DividerLocationMain";
    private static final String KEY_DIVIDER_LOCATION_THUMBNAILS = "AppPanel.DividerLocationThumbnails";
    private static final String KEY_KEYWORDS_VIEW = "AppPanel.KeywordsView";
    private static final String APP_PANEL_TREE_DIRECTORIES = "org.jphototagger.program.app.ui.AppPanel.treeDirectories";
    private static final String APP_PANEL_TREE_FAVORITES = "org.jphototagger.program.app.ui.AppPanel.treeFavorites";
    private static final String APP_PANEL_TREE_SEL_KEYWORDS = "org.jphototagger.program.app.ui.AppPanel.treeSelKeywords";
    private static final String APP_PANEL_TREE_EDIT_KEYWORDS = "AppPanel.Keywords.Tree";
    private static final String APP_PANEL_TREE_MISC_METADATA = "org.jphototagger.program.app.ui.AppPanel.treeMiscMetadata";
    private static final String APP_PANEL_TREE_TIMELINE = "org.jphototagger.program.app.ui.AppPanel.treeTimeline";
    private static final String APP_PANEL_LIST_IMAGE_COLLECTIONS = "org.jphototagger.program.app.ui.AppPanel.listImageCollections";
    private static final String APP_PANEL_LIST_NO_METADATA = "org.jphototagger.program.app.ui.AppPanel.listNoMetadata";
    private static final String APP_PANEL_LIST_SAVED_SEARCHES = "org.jphototagger.program.app.ui.AppPanel.listSavedSearches";
    private static final String APP_PANEL_LIST_SEL_KEYWORDS = "org.jphototagger.program.app.ui.AppPanel.listSelKeywords";
    private final Component cardSelKeywordsList = GUI.getAppPanel().getCardSelKeywordsList();
    private final Component cardSelKeywordsTree = GUI.getAppPanel().getCardSelKeywordsTree();
    private final Map<Component, String> NAME_OF_CARD = new HashMap<>(2);
    private final Map<String, Component> CARD_OF_NAME = new HashMap<>(2);

    // Not a singleton: init() gets cards of AppPanel that is not static
    public AppWindowPersistence() {
        init();
        listen();
    }

    private void init() {

        // Strings has to be equal to the card names in AppPanel
        // (errors on renamings)!
        NAME_OF_CARD.put(cardSelKeywordsList, "flatKeywords");
        NAME_OF_CARD.put(cardSelKeywordsTree, "keywordsTree");

        for (Component c : NAME_OF_CARD.keySet()) {
            CARD_OF_NAME.put(NAME_OF_CARD.get(c), c);
        }
    }

    private void listen() {
        AnnotationProcessor.process(this);
        cardSelKeywordsList.addComponentListener(this);
        cardSelKeywordsTree.addComponentListener(this);
    }

    @Override
    public void componentShown(ComponentEvent evt) {
        Component c = evt.getComponent();
        boolean isSelKeywordsCard = isSelKeywordsCard(c);
        boolean knownCardName = NAME_OF_CARD.containsKey(c);
        if (isSelKeywordsCard && knownCardName) {
            Preferences prefs = Lookup.getDefault().lookup(Preferences.class);
            prefs.setString(KEY_KEYWORDS_VIEW, NAME_OF_CARD.get(c));
        }
    }

    private boolean isSelKeywordsCard(Component c) {
        return (c == cardSelKeywordsList) || (c == cardSelKeywordsTree);
    }

    public void readAppFrameFromProperties() {
        final AppFrame appFrame = GUI.getAppFrame();
        final Preferences prefs = Lookup.getDefault().lookup(Preferences.class);
        final String key = appFrame.getClass().getName();
        EventQueueUtil.invokeInDispatchThread(new Runnable() {
            @Override
            public void run() {
                if (prefs.containsLocationKey(key) && prefs.containsSizeKey(key)) {
                    prefs.applySize(key, appFrame);
                    prefs.applyLocation(key, appFrame);
                } else {
                    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                    double width = (double) screenSize.width * 0.8;
                    double height = (double) screenSize.height * 0.8;
                    appFrame.setSize((int) width, (int) height);
                    appFrame.setLocationRelativeTo(null);
                    GUI.getAppPanel().getSplitPaneMain().setDividerLocation((int) (width * 0.2));
                    GUI.getAppPanel().getSplitPaneThumbnailsMetadata().setDividerLocation((int) (width * 0.6));
                }
            }
        });
    }

    public void readAppPanelFromProperties() {
        EventQueueUtil.invokeInDispatchThread(new Runnable() {

            @Override
            public void run() {
                AppPanel appPanel = GUI.getAppPanel();
                Preferences prefs = Lookup.getDefault().lookup(Preferences.class);
                prefs.applyComponentSettings(appPanel, getAppPanelSettingsHints());
                setInitKeywordsView(appPanel);
            }
        });
    }

    private static PreferencesHints getAppPanelSettingsHints() {
        PreferencesHints hints = new PreferencesHints();

        // Lists setTree by readList...() / writeListProperties() or other classes
        hints.addKeyToExclude(APP_PANEL_LIST_IMAGE_COLLECTIONS);
        hints.addKeyToExclude(APP_PANEL_LIST_NO_METADATA);
        hints.addKeyToExclude(APP_PANEL_LIST_SAVED_SEARCHES);
        hints.addKeyToExclude(APP_PANEL_LIST_SEL_KEYWORDS);

        // Trees setTree by readTree...() / writeTreeProperties() or other classes
        hints.addKeyToExclude(APP_PANEL_TREE_DIRECTORIES);
        hints.addKeyToExclude(APP_PANEL_TREE_EDIT_KEYWORDS);
        hints.addKeyToExclude(APP_PANEL_TREE_FAVORITES);
        hints.addKeyToExclude(APP_PANEL_TREE_MISC_METADATA);
        hints.addKeyToExclude(APP_PANEL_TREE_SEL_KEYWORDS);
        hints.addKeyToExclude(APP_PANEL_TREE_TIMELINE);

        return hints;
    }

    private void setInitKeywordsView(AppPanel appPanel) {
        KeywordsPanel panelEditKeywords = appPanel.getPanelEditKeywords();
        panelEditKeywords.setKeyCard("AppPanel.Keywords.Card");
        panelEditKeywords.setKeyTree("AppPanel.Keywords.Tree");
        panelEditKeywords.readProperties();

        // Strings has to be equal to the card names in AppPanel
        // (errors on renamings)!
        String name = "keywordsTree";
        Preferences prefs = Lookup.getDefault().lookup(Preferences.class);

        if (prefs.containsKey(KEY_KEYWORDS_VIEW)) {
            String s = prefs.getString(KEY_KEYWORDS_VIEW);
            if (s.equals("flatKeywords") || s.equals("keywordsTree")) {
                name = s;
            }
        }

        Component c = CARD_OF_NAME.get(name);
        if (c == cardSelKeywordsList) {
            appPanel.displaySelKeywordsList(AppPanel.SelectAlso.NOTHING_ELSE);
        } else if (c == cardSelKeywordsTree) {
            appPanel.displaySelKeywordsTree(AppPanel.SelectAlso.NOTHING_ELSE);
        } else {
            assert false;
        }
    }

    @EventSubscriber(eventClass = AppWillExitEvent.class)
    public void appWillExit(AppWillExitEvent evt) {
        writeAppProperties();
    }

    private void writeAppProperties() {
        AppPanel appPanel = GUI.getAppPanel();
        Preferences prefs = Lookup.getDefault().lookup(Preferences.class);
        prefs.setComponent(appPanel, getAppPanelSettingsHints());
        prefs.setInt(KEY_DIVIDER_LOCATION_MAIN, appPanel.getSplitPaneMain().getDividerLocation());
        prefs.setInt(KEY_DIVIDER_LOCATION_THUMBNAILS, appPanel.getSplitPaneThumbnailsMetadata().getDividerLocation());
        appPanel.getPanelEditKeywords().writeProperties();
        // Later than settings.setTree(appPanel, null)!
        writeTreeProperties(appPanel);
        writeListProperties(appPanel);
    }

    /**
     * To call after the model has been created.
     */
    public static void readTreeSelKeywords() {
        read(GUI.getSelKeywordsTree(), APP_PANEL_TREE_SEL_KEYWORDS);
    }

    /**
     * To call after the model has been created.
     */
    public static void readTreeEditKeywords() {
        read(GUI.getEditKeywordsTree(), APP_PANEL_TREE_EDIT_KEYWORDS);
    }

    /**
     * To call after the model has been created.
     */
    public static void readTreeMiscMetadata() {
        read(GUI.getMiscMetadataTree(), APP_PANEL_TREE_MISC_METADATA);
    }

    /**
     * To call after the model has been created.
     */
    public static void readTreeTimeline() {
        read(GUI.getTimelineTree(), APP_PANEL_TREE_TIMELINE);
    }

    /**
     * To call after the model has been created.
     */
    public static void readTreeDirectories() {
        read(GUI.getDirectoriesTree(), APP_PANEL_TREE_DIRECTORIES);
    }

    /**
     * To call after the model has been created.
     */
    public static void readListSavedSearches() {
        read(GUI.getSavedSearchesList(), APP_PANEL_LIST_SAVED_SEARCHES);
    }

    /**
     * To call after the model has been created.
     */
    public static void readListImageCollections() {
        read(GUI.getImageCollectionsList(), APP_PANEL_LIST_IMAGE_COLLECTIONS);
    }

    /**
     * To call after the model has been created.
     */
    public static void readListSelKeywords() {
        read(GUI.getSelKeywordsList(), APP_PANEL_LIST_SEL_KEYWORDS);
    }

    private static void read(JTree tree, String key) {
        Preferences prefs = Lookup.getDefault().lookup(Preferences.class);

        prefs.applyTreeSettings(key, tree);
    }

    private static void read(JList<?> list, String key) {
        Preferences prefs = Lookup.getDefault().lookup(Preferences.class);
        prefs.applySelectedIndices(key, list);
    }

    // Independent from renamings
    private void writeTreeProperties(AppPanel appPanel) {
        write(appPanel.getTreeSelKeywords(), APP_PANEL_TREE_SEL_KEYWORDS);
        write(appPanel.getTreeEditKeywords(), APP_PANEL_TREE_EDIT_KEYWORDS);
        write(appPanel.getTreeMiscMetadata(), APP_PANEL_TREE_MISC_METADATA);
        write(appPanel.getTreeTimeline(), APP_PANEL_TREE_TIMELINE);
        write(appPanel.getTreeDirectories(), APP_PANEL_TREE_DIRECTORIES);
        write(appPanel.getTreeFavorites(), APP_PANEL_TREE_FAVORITES);
    }

    // Independent from renamings
    private void writeListProperties(AppPanel appPanel) {
        write(appPanel.getListSavedSearches(), APP_PANEL_LIST_SAVED_SEARCHES);
        write(appPanel.getListImageCollections(), APP_PANEL_LIST_IMAGE_COLLECTIONS);
        write(appPanel.getListSelKeywords(), APP_PANEL_LIST_SEL_KEYWORDS);
    }

    private void write(JTree tree, String key) {
        Preferences prefs = Lookup.getDefault().lookup(Preferences.class);

        prefs.setTree(key, tree);
    }

    private void write(JList<?> list, String key) {
        Preferences prefs = Lookup.getDefault().lookup(Preferences.class);
        prefs.setSelectedIndices(key, list);
    }

    @Override
    public void componentResized(ComponentEvent evt) {
        // ignore
    }

    @Override
    public void componentMoved(ComponentEvent evt) {
        // ignore
    }

    @Override
    public void componentHidden(ComponentEvent evt) {
        // ignore
    }
}
