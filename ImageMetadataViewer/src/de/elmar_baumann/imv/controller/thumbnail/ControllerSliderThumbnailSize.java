/*
 * JPhotoTagger tags and finds images fast
 * Copyright (C) 2009 by the developer team, resp. Elmar Baumann<eb@elmar-baumann.de>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package de.elmar_baumann.imv.controller.thumbnail;

import de.elmar_baumann.imv.UserSettings;
import de.elmar_baumann.imv.event.listener.impl.ListenerProvider;
import de.elmar_baumann.imv.event.listener.ThumbnailsPanelListener;
import de.elmar_baumann.imv.event.UserSettingsChangeEvent;
import de.elmar_baumann.imv.event.listener.UserSettingsChangeListener;
import de.elmar_baumann.imv.resource.GUI;
import de.elmar_baumann.imv.view.frames.AppFrame;
import de.elmar_baumann.imv.view.panels.AppPanel;
import de.elmar_baumann.imv.view.panels.ThumbnailsPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Controls the slider which changes the size of the thumbnails
 *
 * @author  Elmar Baumann <eb@elmar-baumann.de>
 * @version 2008-10-12
 */
public final class ControllerSliderThumbnailSize
        implements ActionListener, ChangeListener, ThumbnailsPanelListener,
                   UserSettingsChangeListener {

    private final AppFrame appFrame = GUI.INSTANCE.getAppFrame();
    private final AppPanel appPanel = GUI.INSTANCE.getAppPanel();
    private final ThumbnailsPanel thumbnailsPanel =
            appPanel.getPanelThumbnails();
    private final JSlider slider = appPanel.getSliderThumbnailSize();
    private static final int STEP_WIDTH = 1;
    private static final int LARGER_STEP_WIDTH = 10;
    private static final int MIN_MAGINFICATION_PERCENT = 10;
    private static final int MAX_MAGINFICATION_PERCENT = 100;
    private static final String KEY_SLIDER_VALUE =
            ControllerSliderThumbnailSize.class.getName() + "." + "SliderValue"; // NOI18N
    private int currentValue = 100;
    private int maxThumbnailWidth =
            UserSettings.INSTANCE.getMaxThumbnailLength();

    public ControllerSliderThumbnailSize() {
        initSlider();
        listen();
    }

    private void listen() {
        thumbnailsPanel.addThumbnailsPanelListener(this);
        slider.addChangeListener(this);
        ListenerProvider.INSTANCE.addUserSettingsChangeListener(this);
        appFrame.getMenuItemThumbnailSizeDecrease().addActionListener(this);
        appFrame.getMenuItemThumbnailSizeIncrease().addActionListener(this);
    }

    private void initSlider() {
        readProperties();
        slider.setMinimum(MIN_MAGINFICATION_PERCENT);
        slider.setMaximum(MAX_MAGINFICATION_PERCENT);
        slider.setMajorTickSpacing(STEP_WIDTH);
        slider.setMinorTickSpacing(STEP_WIDTH);
        slider.setValue(currentValue);
        setThumbnailWidth();
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        handleSliderMoved();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(appFrame.getMenuItemThumbnailSizeDecrease())) {
            moveSlider(LARGER_STEP_WIDTH, false);
        } else if (e.getSource().equals(
                appFrame.getMenuItemThumbnailSizeIncrease())) {
            moveSlider(LARGER_STEP_WIDTH, true);
        }
    }

    private void moveSlider(int stepWidth, boolean increase) {
        if (increase) {
            addToSliderValue(stepWidth);
        } else {
            addToSliderValue(-stepWidth);
        }
    }

    private void addToSliderValue(int increment) {
        int value = slider.getValue();
        int newValue = Math.min(Math.max(value + increment,
                                         MIN_MAGINFICATION_PERCENT),
                                MAX_MAGINFICATION_PERCENT);
        slider.setValue(newValue);
    }

    @Override
    public void thumbnailsSelectionChanged() {
        // ignore
    }

    @Override
    public void thumbnailsChanged() {
        setThumbnailWidth();
    }

    @Override
    public void applySettings(UserSettingsChangeEvent evt) {
        if (evt.getType().equals(
                UserSettingsChangeEvent.Type.MAX_THUMBNAIL_WIDTH)) {
            maxThumbnailWidth = evt.getMaxThumbnailWidth();
            setThumbnailWidth();
        }
    }

    private void handleSliderMoved() {
        int value = slider.getValue();
        synchronized (this) {
            if (value % STEP_WIDTH == 0 && value != currentValue) {
                currentValue = value;
                writeProperties();
                setThumbnailWidth();
            }
        }
    }

    private void readProperties() {
        Integer value = UserSettings.INSTANCE.getSettings().getInt(
                KEY_SLIDER_VALUE);
        if (!value.equals(Integer.MIN_VALUE)) {
            currentValue = value;
        }
    }

    private void setThumbnailWidth() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                int width = (int) ((double) maxThumbnailWidth *
                        ((double) currentValue / 100.0));
                thumbnailsPanel.setThumbnailWidth(width);
            }
        });
    }

    private void writeProperties() {
        UserSettings.INSTANCE.getSettings().setInt(currentValue,
                KEY_SLIDER_VALUE);
        UserSettings.INSTANCE.writeToFile();
    }
}