package org.jphototagger.maintainance;

import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.jdesktop.swingx.JXLabel;
import org.jphototagger.domain.repository.ThumbnailsRepository;
import org.jphototagger.lib.swing.PanelExt;
import org.jphototagger.lib.swing.util.MnemonicUtil;
import org.jphototagger.lib.util.Bundle;
import org.jphototagger.resources.UiFactory;
import org.openide.util.Lookup;

/**
 * @author Elmar Baumann
 */
public class MaintainanceCachesPanel extends PanelExt {

    private static final long serialVersionUID = 1L;

    public MaintainanceCachesPanel() {
        initComponents();
        postInitComponents();
    }

    private void postInitComponents() {
        MnemonicUtil.setMnemonics(this);
    }

    private void compactThumbnailsRepository() {
        ThumbnailsRepository  repo = Lookup.getDefault().lookup(ThumbnailsRepository.class);
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        repo.compact();
        setCursor(Cursor.getDefaultCursor());
    }

    private void initComponents() {
        GridBagConstraints gridBagConstraints;

        panelInfo = UiFactory.panel();
        labelInfo = UiFactory.jxLabel();
        buttonClearExifCache = UiFactory.button();
        buttonClearExifCache.setAction(ClearExifCacheAction.INSTANCE);
        labelInfoClearExifCache = UiFactory.jxLabel();
        buttonClearIptcIgnoreCache = UiFactory.button();
        buttonClearIptcIgnoreCache.setAction(ClearIptcIgnoreCacheAction.INSTANCE);
        labelInfoClearIptcIgnoreCache = UiFactory.jxLabel();
        buttonCompactThumbnailsRepository = UiFactory.button();
        labelInfoCompactThumbnailsRepository = UiFactory.jxLabel();
        panelPadding = UiFactory.panel();

        setName("MaintainanceCachesPanel"); // NOI18N
        setLayout(new GridBagLayout());

        panelInfo.setBorder(BorderFactory.createTitledBorder(Bundle.getString(getClass(), "MaintainanceCachesPanel.panelInfo.border.title"))); // NOI18N
        panelInfo.setName("panelInfo"); // NOI18N
        panelInfo.setLayout(new GridBagLayout());

        labelInfo.setText(Bundle.getString(getClass(), "MaintainanceCachesPanel.labelInfo.text")); // NOI18N
        labelInfo.setName("labelInfo"); // NOI18N
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = UiFactory.insets(5, 5, 5, 5);
        panelInfo.add(labelInfo, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = UiFactory.insets(5, 5, 5, 5);
        add(panelInfo, gridBagConstraints);

        buttonClearExifCache.setText(Bundle.getString(getClass(), "MaintainanceCachesPanel.buttonClearExifCache.text")); // NOI18N
        buttonClearExifCache.setName("buttonClearExifCache"); // NOI18N
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = UiFactory.insets(10, 5, 0, 0);
        add(buttonClearExifCache, gridBagConstraints);

        labelInfoClearExifCache.setText(Bundle.getString(getClass(), "MaintainanceCachesPanel.labelInfoClearExifCache.text")); // NOI18N
        labelInfoClearExifCache.setName("labelInfoClearExifCache"); // NOI18N
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = UiFactory.insets(10, 10, 0, 5);
        add(labelInfoClearExifCache, gridBagConstraints);

        buttonClearIptcIgnoreCache.setText(Bundle.getString(getClass(), "MaintainanceCachesPanel.buttonClearIptcIgnoreCache.text")); // NOI18N
        buttonClearIptcIgnoreCache.setName("buttonClearIptcIgnoreCache"); // NOI18N
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = UiFactory.insets(10, 5, 0, 0);
        add(buttonClearIptcIgnoreCache, gridBagConstraints);

        labelInfoClearIptcIgnoreCache.setText(Bundle.getString(getClass(), "MaintainanceCachesPanel.labelInfoClearIptcIgnoreCache.text")); // NOI18N
        labelInfoClearIptcIgnoreCache.setName("labelInfoClearIptcIgnoreCache"); // NOI18N
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = UiFactory.insets(10, 10, 0, 5);
        add(labelInfoClearIptcIgnoreCache, gridBagConstraints);

        buttonCompactThumbnailsRepository.setText(Bundle.getString(getClass(), "MaintainanceCachesPanel.buttonCompactThumbnailsRepository.text")); // NOI18N
        buttonCompactThumbnailsRepository.setName("buttonCompactThumbnailsRepository"); // NOI18N
        buttonCompactThumbnailsRepository.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                buttonCompactThumbnailsRepositoryActionPerformed(evt);
            }
        });
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = UiFactory.insets(10, 5, 0, 0);
        add(buttonCompactThumbnailsRepository, gridBagConstraints);

        labelInfoCompactThumbnailsRepository.setText(Bundle.getString(getClass(), "MaintainanceCachesPanel.labelInfoCompactThumbnailsRepository.text")); // NOI18N
        labelInfoCompactThumbnailsRepository.setName("labelInfoCompactThumbnailsRepository"); // NOI18N
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = UiFactory.insets(10, 10, 0, 5);
        add(labelInfoCompactThumbnailsRepository, gridBagConstraints);

        panelPadding.setName("panelPadding"); // NOI18N
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 0.5;
        add(panelPadding, gridBagConstraints);
    }

    private void buttonCompactThumbnailsRepositoryActionPerformed(ActionEvent evt) {
        compactThumbnailsRepository();
    }

    private JButton buttonClearExifCache;
    private JButton buttonClearIptcIgnoreCache;
    private JButton buttonCompactThumbnailsRepository;
    private JXLabel labelInfo;
    private JXLabel labelInfoClearExifCache;
    private JXLabel labelInfoClearIptcIgnoreCache;
    private JXLabel labelInfoCompactThumbnailsRepository;
    private JPanel panelInfo;
    private JPanel panelPadding;
}
