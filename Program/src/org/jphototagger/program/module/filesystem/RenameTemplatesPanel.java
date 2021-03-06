package org.jphototagger.program.module.filesystem;

import java.io.File;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.jphototagger.api.preferences.Preferences;
import org.jphototagger.domain.repository.RenameTemplatesRepository;
import org.jphototagger.domain.repository.Repository;
import org.jphototagger.domain.repository.event.renametemplates.RenameTemplateDeletedEvent;
import org.jphototagger.domain.repository.event.renametemplates.RenameTemplateInsertedEvent;
import org.jphototagger.domain.repository.event.renametemplates.RenameTemplateUpdatedEvent;
import org.jphototagger.domain.templates.RenameTemplate;
import org.jphototagger.lib.swing.DialogExt;
import org.jphototagger.lib.swing.PanelExt;
import org.jphototagger.lib.swing.util.ComboBoxUtil;
import org.jphototagger.lib.swing.util.ComponentUtil;
import org.jphototagger.lib.swing.util.MnemonicUtil;
import org.jphototagger.lib.util.Bundle;
import org.jphototagger.resources.Icons;
import org.jphototagger.resources.UiFactory;
import org.openide.util.Lookup;

/**
 * @author Elmar Baumann
 */
public class RenameTemplatesPanel extends PanelExt implements ListDataListener {

    private static final long serialVersionUID = 1L;
    private static final String KEY_SEL_TEMPLATE = "RenameDialog.SelectedTemplate";
    private final FilenameFormatArray filenameFormatArray = new FilenameFormatArray();
    private File fileForExampleFilename = new File(Bundle.getString(RenameTemplatesPanel.class, "RenameTemplatesPanel.FileForExampleFilename"));
    private boolean listen;
    private boolean dirty;

    public RenameTemplatesPanel() {
        initComponents();
        postInitComponents();
    }

    private void postInitComponents() {
        MnemonicUtil.setMnemonics(this);
        initCheckDirtyDialog();
        setComboBoxModels();
        setRenameTemplate();
        setEnabledRenameTemplateButtons();
        readProperties();
        comboBoxRenameTemplates.getModel().addListDataListener(this);
        listen = true;
    }

    private void initCheckDirtyDialog() {
        MnemonicUtil.setMnemonics(checkDirtyDialog);
        checkDirtyDialog.setIconImages(Icons.getAppIcons());
        checkDirtyDialog.setModal(true);
        checkDirtyDialog.pack();
        checkDirtyDialog.setLocationRelativeTo(this);
    }

    private void setComboBoxModels() {
        comboBoxAtBegin.setModel(getComboBoxModel());
        comboBoxInTheMiddle.setModel(getComboBoxModel());
        comboBoxAtEnd.setModel(getComboBoxModel());
    }

    private ComboBoxModel<Object> getComboBoxModel() {
        DefaultComboBoxModel<Object> model = new DefaultComboBoxModel<>();
        model.addElement(new FilenameFormatConstantString(""));
        model.addElement(new FilenameFormatNumberSequence(1, 1, 4));
        model.addElement(new FilenameFormatFileName());
        model.addElement(new FilenameFormatDate("-"));
        model.addElement(new FilenameFormatDateTime("-"));
        model.addElement(new FilenameFormatEmptyString());
        return model;
    }

    public File getFileForExampleFilename() {
        return fileForExampleFilename;
    }

    public void setFileForExampleFilename(File fileForExampleFilename) {
        if (fileForExampleFilename == null) {
            throw new NullPointerException("fileForExampleFilename == null");
        }
        this.fileForExampleFilename = fileForExampleFilename;
        if (fileForExampleFilename.exists()) {
            filenameFormatArray.setFile(fileForExampleFilename);
        }
    }

    public FilenameFormatArray getFilenameFormatArray() {
        return filenameFormatArray;
    }

    private void readProperties() {
        Preferences prefs = Lookup.getDefault().lookup(Preferences.class);
        if (prefs != null) {
            prefs.applySelectedIndex(KEY_SEL_TEMPLATE, comboBoxRenameTemplates);
        }
    }

    public void showExampleFilename() {
        setFileToFilenameFormats(fileForExampleFilename);
        setFilenameFormatArray(fileForExampleFilename);
        labelBeforeFilename.setText(fileForExampleFilename.getName());
        labelAfterFilename.setText(filenameFormatArray.format());
    }

    private void saveAsRenameTemplate() {
        listen = false;
        RenameTemplate template = createTemplate();
        boolean inserted = RenameTemplateUtil.insert(template); // Has not to be dirty to be inserted (copy to another name)
        dirty = dirty && !inserted;
        listen = true;
    }

    private void renameRenameTemplate() {
        listen = false;
        Object selItem = comboBoxRenameTemplates.getSelectedItem();
        if (selItem instanceof RenameTemplate) {
            boolean renamed = RenameTemplateUtil.rename((RenameTemplate) selItem); // Has not to be dirty to be renamed
            dirty = dirty && !renamed;
        }
        listen = true;
    }

    private void deleteRenameTemplate() {
        listen = false;
        Object selItem = comboBoxRenameTemplates.getSelectedItem();
        if (selItem instanceof RenameTemplate) {
            boolean deleted = RenameTemplateUtil.delete((RenameTemplate) selItem); // Has not to be dirty to be deleted
            dirty = dirty && !deleted;
            if (deleted) {
                setRenameTemplate();
            }
        }
        listen = true;
    }

    private void updateRenameTemplate() {
        listen = false;
        Object selItem = comboBoxRenameTemplates.getSelectedItem();
        if (selItem instanceof RenameTemplate) {
            RenameTemplate template = (RenameTemplate) selItem;
            setValuesToTemplate(template);
            RenameTemplateUtil.update(template);
        }
        listen = true;
    }

    private void setRenameTemplate() {
        Object selItem = comboBoxRenameTemplates.getSelectedItem();
        if (selItem instanceof RenameTemplate) {
            setTemplate((RenameTemplate) selItem);
            setEnabledConstantTextFields();
            showExampleFilename();
        }
    }

    private void setTemplate(RenameTemplate template) {
        listen = false;
        spinnerStartNumber.getModel().setValue(template.getStartNumber());
        spinnerNumberStepWidth.getModel().setValue(template.getStepWidth());
        spinnerNumberCount.getModel().setValue(template.getNumberCount());
        ComboBoxUtil.selectString(comboBoxDateDelimiter.getModel(), template.getDateDelimiter());
        select(template.getFormatClassAtBegin(), comboBoxAtBegin);
        textFieldDelim1.setText(template.getDelimiter1());
        select(template.getFormatClassInTheMiddle(), comboBoxInTheMiddle);
        textFieldDelim2.setText(template.getDelimiter2());
        select(template.getFormatClassAtEnd(), comboBoxAtEnd);
        textFieldAtBegin.setText(template.getTextAtBegin());
        textFieldInTheMiddle.setText(template.getTextInTheMiddle());
        textFieldAtEnd.setText(template.getTextAtEnd());
        listen = true;
    }

    private void select(Class<?> formatClass, JComboBox<?> comboBox) {
        ComboBoxModel<?> model = comboBox.getModel();
        int size = model.getSize();
        boolean selected = false;
        int index = 0;
        while (!selected && (index < size)) {
            Object element = model.getElementAt(index);
            index++;
            if (element.getClass().equals(formatClass)) {
                model.setSelectedItem(element);
                selected = true;
            }
        }
    }

    private void setEnabledConstantTextFields() {
        textFieldAtBegin.setEditable(comboBoxAtBegin.getSelectedItem() instanceof FilenameFormatConstantString);
        textFieldInTheMiddle.setEditable(comboBoxInTheMiddle.getSelectedItem() instanceof FilenameFormatConstantString);
        textFieldAtEnd.setEditable(comboBoxAtEnd.getSelectedItem() instanceof FilenameFormatConstantString);
    }

    private RenameTemplate createTemplate() {
        RenameTemplate template = new RenameTemplate();
        setValuesToTemplate(template);
        return template;
    }

    private void setValuesToTemplate(RenameTemplate template) {
        template.setStartNumber((Integer) spinnerStartNumber.getModel().getValue());
        template.setStepWidth((Integer) spinnerNumberStepWidth.getModel().getValue());
        template.setNumberCount((Integer) spinnerNumberCount.getModel().getValue());
        template.setDateDelimiter(getDateDelimiter());
        template.setFormatClassAtBegin(comboBoxAtBegin.getSelectedItem().getClass());
        template.setDelimiter1(textFieldDelim1.getText());
        template.setFormatClassInTheMiddle(comboBoxInTheMiddle.getSelectedItem().getClass());
        template.setDelimiter2(textFieldDelim2.getText());
        template.setFormatClassAtEnd(comboBoxAtEnd.getSelectedItem().getClass());
        template.setTextAtBegin(textFieldAtBegin.getText());
        template.setTextInTheMiddle(textFieldInTheMiddle.getText());
        template.setTextAtEnd(textFieldAtEnd.getText());
    }

    private void setEnabledRenameTemplateButtons() {
        Object selValue = comboBoxRenameTemplates.getSelectedItem();
        boolean templateSelected = selValue instanceof RenameTemplate;
        buttonRenameRenameTemplate.setEnabled(templateSelected);
        buttonDeleteRenameTemplate.setEnabled(templateSelected);
        buttonUpdateRenameTemplate.setEnabled(templateSelected);
    }

    private void setFileToFilenameFormats(File file) {
        setFilenameFormatToSelectedItem(comboBoxAtBegin, file, textFieldAtBegin.getText().trim());
        setFilenameFormatToSelectedItem(comboBoxInTheMiddle, file, textFieldInTheMiddle.getText().trim());
        setFilenameFormatToSelectedItem(comboBoxAtEnd, file, textFieldAtEnd.getText().trim());
    }

    private void setFilenameFormatToSelectedItem(JComboBox<?> comboBox, File file, String fmt) {
        ComboBoxModel<?> model = comboBox.getModel();
        FilenameFormat format = (FilenameFormat) model.getSelectedItem();
        format.setFile(file);
        format.setFormat(fmt);
        if (format instanceof FilenameFormatNumberSequence) {
            FilenameFormatNumberSequence f = (FilenameFormatNumberSequence) format;
            f.setStart((Integer) spinnerStartNumber.getValue());
            f.setIncrement((Integer) spinnerNumberStepWidth.getValue());
            f.setCountDigits((Integer) spinnerNumberCount.getValue());
        } else if (format instanceof FilenameFormatDate) {
            FilenameFormatDate f = (FilenameFormatDate) format;
            f.setDelimiter(getDateDelimiter());
        } else if (format instanceof FilenameFormatDateTime) {
            FilenameFormatDateTime f = (FilenameFormatDateTime) format;
            f.setDelimiter(getDateDelimiter());
        }
    }

    private void setFilenameFormatArray(File file) {
        filenameFormatArray.clear();
        filenameFormatArray.addFormat((FilenameFormat) comboBoxAtBegin.getSelectedItem());
        filenameFormatArray.addFormat(new FilenameFormatConstantString(textFieldDelim1.getText().trim()));
        filenameFormatArray.addFormat((FilenameFormat) comboBoxInTheMiddle.getSelectedItem());
        filenameFormatArray.addFormat(new FilenameFormatConstantString(textFieldDelim2.getText().trim()));
        filenameFormatArray.addFormat((FilenameFormat) comboBoxAtEnd.getSelectedItem());
        FilenameFormatFilenamePostfix postfix = new FilenameFormatFilenamePostfix();
        postfix.setFile(file);
        filenameFormatArray.addFormat(postfix);
    }

    private String getDateDelimiter() {
        return (String) comboBoxDateDelimiter.getModel().getSelectedItem();
    }

    @Override
    public void intervalAdded(ListDataEvent evt) {
        setEnabledRenameTemplateButtons();
    }

    @Override
    public void intervalRemoved(ListDataEvent evt) {
        setEnabledRenameTemplateButtons();
    }

    @Override
    public void contentsChanged(ListDataEvent evt) {
        setEnabledRenameTemplateButtons();
    }

    public void checkDirty() {
        if (dirty) {
            buttonCheckDirtyDialogCreateNew.requestFocusInWindow();
            checkDirtyDialog.setVisible(true);
            checkDirtyDialog.toFront();
            ComponentUtil.parentWindowToFront(this);
        }
    }

    private void startNumberChanged() {
        if (listen) {
            dirty = true;
            showExampleFilename();
        }
    }

    private void numberStepWidthChanged() {
        if (listen) {
            dirty = true;
            showExampleFilename();
        }
    }

    private void numberCountChanged() {
        if (listen) {
            dirty = true;
            showExampleFilename();
        }
    }

    private void dateDelimiterChanged() {
        if (listen) {
            dirty = true;
            showExampleFilename();
            setEnabledConstantTextFields();
        }
    }

    private void atBeginChanged() {
        if (listen) {
            dirty = true;
            showExampleFilename();
            setEnabledConstantTextFields();
        }
    }

    private void delimiter1Changed() {
        if (listen) {
            dirty = true;
            showExampleFilename();
        }
    }

    private void inTheMiddleChanged() {
        if (listen) {
            dirty = true;
            showExampleFilename();
            setEnabledConstantTextFields();
        }
    }

    private void delimiter2Changed() {
        if (listen) {
            dirty = true;
            showExampleFilename();
        }
    }

    private void atEndChanged() {
        if (listen) {
            dirty = true;
            showExampleFilename();
            setEnabledConstantTextFields();
        }
    }

    private void textAtBeginChanged() {
        if (listen) {
            dirty = true;
            showExampleFilename();
        }
    }

    private void textInTheMiddleChanged() {
        if (listen) {
            dirty = true;
            showExampleFilename();
        }
    }

    private void textAtEndChanged() {
        if (listen) {
            dirty = true;
            showExampleFilename();
        }
    }

    private void renameTemplateChanged() {
        if (listen) {
            checkDirty();
            Preferences prefs = Lookup.getDefault().lookup(Preferences.class);
            prefs.setSelectedIndex(KEY_SEL_TEMPLATE, comboBoxRenameTemplates);
            setRenameTemplate();
            setEnabledRenameTemplateButtons();
        }
    }

    private static class RenameTemplatesComboBoxModel extends DefaultComboBoxModel<Object> {

        private static final long serialVersionUID = 1L;
        private final RenameTemplatesRepository renameTemplatesRepo = Lookup.getDefault().lookup(RenameTemplatesRepository.class);

        private RenameTemplatesComboBoxModel() {
            addElements();
            listen();
        }

        private void listen() {
            AnnotationProcessor.process(this);
        }

        private void addElements() {
            Repository repo = Lookup.getDefault().lookup(Repository.class);
            if (repo == null || !repo.isInit()) {
                return;
            }
            for (RenameTemplate template : renameTemplatesRepo.findAllRenameTemplates()) {
                addElement(template);
            }
        }

        private void updateTemplate(RenameTemplate template) {
            int index = getIndexOf(template);
            if (index >= 0) {
                ((RenameTemplate) getElementAt(index)).set(template);
                fireContentsChanged(this, index, index);
            }
        }

        private void insertTemplate(RenameTemplate template) {
            addElement(template);
            setSelectedItem(template);
        }

        private void deleteTemplate(RenameTemplate template) {
            removeElement(template);
        }

        @EventSubscriber(eventClass = RenameTemplateDeletedEvent.class)
        public void templateDeleted(final RenameTemplateDeletedEvent evt) {
            deleteTemplate(evt.getTemplate());
        }

        @EventSubscriber(eventClass = RenameTemplateInsertedEvent.class)
        public void templateInserted(final RenameTemplateInsertedEvent evt) {
            insertTemplate(evt.getTemplate());
        }

        @EventSubscriber(eventClass = RenameTemplateUpdatedEvent.class)
        public void templateUpdated(final RenameTemplateUpdatedEvent evt) {
            updateTemplate(evt.getTemplate());
        }
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        checkDirtyDialog = new DialogExt();
        checkDirtyDialog.setPersistSizeAndLocation(false);
        labelDirtyConfirmSaveChanges = UiFactory.label();
        panelDirtyButtons = UiFactory.panel();
        buttonCheckDirtyDialogCreateNew = UiFactory.button();
        buttonCheckDirtyDialogUpdate = UiFactory.button();
        buttonCheckDirtyDialogReject = UiFactory.button();
        panelNumbers = UiFactory.panel();
        panelNumbersContents = UiFactory.panel();
        labelStartNumber = UiFactory.label();
        spinnerStartNumber = UiFactory.spinner();
        labelNumberStepWidth = UiFactory.label();
        spinnerNumberStepWidth = UiFactory.spinner();
        labelNumberCount = UiFactory.label();
        spinnerNumberCount = UiFactory.spinner();
        panelDateDelimiter = UiFactory.panel();
        labelDateDelim = UiFactory.label();
        comboBoxDateDelimiter = UiFactory.comboBox();
        panelDefineName = UiFactory.panel();
        panelDefineNameContents = UiFactory.panel();
        labelAtBegin = UiFactory.label();
        labelDelim1 = UiFactory.label();
        labelInTheMid = UiFactory.label();
        labelDelim2 = UiFactory.label();
        labelAtEnd = UiFactory.label();
        comboBoxAtBegin = UiFactory.comboBox();
        textFieldDelim1 = UiFactory.textField();
        comboBoxInTheMiddle = UiFactory.comboBox();
        textFieldDelim2 = UiFactory.textField();
        comboBoxAtEnd = UiFactory.comboBox();
        textFieldAtBegin = UiFactory.textField();
        textFieldInTheMiddle = UiFactory.textField();
        textFieldAtEnd = UiFactory.textField();
        panelExample = UiFactory.panel();
        panelExampleContents = UiFactory.panel();
        labelBefore = UiFactory.label();
        labelBeforeFilename = UiFactory.label();
        labelAfter = UiFactory.label();
        labelAfterFilename = UiFactory.label();
        panelRenameTemplates = UiFactory.panel();
        panelRenameTemplatesContents = UiFactory.panel();
        comboBoxRenameTemplates = UiFactory.comboBox();
        panelButtonsRenameTemplates = UiFactory.panel();
        buttonSaveRenameTemplate = UiFactory.button();
        buttonRenameRenameTemplate = UiFactory.button();
        buttonDeleteRenameTemplate = UiFactory.button();
        buttonUpdateRenameTemplate = UiFactory.button();

        checkDirtyDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        checkDirtyDialog.setTitle(Bundle.getString(getClass(), "RenameTemplatesPanel.checkDirtyDialog.title")); // NOI18N
        checkDirtyDialog.getContentPane().setLayout(new java.awt.GridBagLayout());

        labelDirtyConfirmSaveChanges.setText(Bundle.getString(getClass(), "RenameTemplatesPanel.labelDirtyConfirmSaveChanges.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = UiFactory.insets(10, 10, 0, 10);
        checkDirtyDialog.getContentPane().add(labelDirtyConfirmSaveChanges, gridBagConstraints);

        panelDirtyButtons.setLayout(new java.awt.GridBagLayout());

        buttonCheckDirtyDialogCreateNew.setText(Bundle.getString(getClass(), "RenameTemplatesPanel.buttonCheckDirtyDialogCreateNew.text")); // NOI18N
        buttonCheckDirtyDialogCreateNew.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCheckDirtyDialogCreateNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panelDirtyButtons.add(buttonCheckDirtyDialogCreateNew, gridBagConstraints);

        buttonCheckDirtyDialogUpdate.setText(Bundle.getString(getClass(), "RenameTemplatesPanel.buttonCheckDirtyDialogUpdate.text")); // NOI18N
        buttonCheckDirtyDialogUpdate.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCheckDirtyDialogUpdateActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = UiFactory.insets(0, 5, 0, 0);
        panelDirtyButtons.add(buttonCheckDirtyDialogUpdate, gridBagConstraints);

        buttonCheckDirtyDialogReject.setText(Bundle.getString(getClass(), "RenameTemplatesPanel.buttonCheckDirtyDialogReject.text")); // NOI18N
        buttonCheckDirtyDialogReject.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCheckDirtyDialogRejectActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = UiFactory.insets(0, 5, 0, 0);
        panelDirtyButtons.add(buttonCheckDirtyDialogReject, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = UiFactory.insets(5, 10, 10, 10);
        checkDirtyDialog.getContentPane().add(panelDirtyButtons, gridBagConstraints);

        setLayout(new java.awt.GridBagLayout());

        panelNumbers.setBorder(javax.swing.BorderFactory.createTitledBorder(Bundle.getString(getClass(), "RenameTemplatesPanel.panelNumbers.border.title"))); // NOI18N
        panelNumbers.setLayout(new java.awt.GridBagLayout());

        panelNumbersContents.setLayout(new java.awt.GridBagLayout());

        labelStartNumber.setLabelFor(spinnerStartNumber);
        labelStartNumber.setText(Bundle.getString(getClass(), "RenameTemplatesPanel.labelStartNumber.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panelNumbersContents.add(labelStartNumber, gridBagConstraints);

        spinnerStartNumber.setModel(new SpinnerNumberModel(1, 1, 999999, 1));
        spinnerStartNumber.addChangeListener(new javax.swing.event.ChangeListener() {
            @Override
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spinnerStartNumberStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = UiFactory.insets(0, 5, 0, 0);
        panelNumbersContents.add(spinnerStartNumber, gridBagConstraints);

        labelNumberStepWidth.setLabelFor(spinnerNumberStepWidth);
        labelNumberStepWidth.setText(Bundle.getString(getClass(), "RenameTemplatesPanel.labelNumberStepWidth.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = UiFactory.insets(0, 5, 0, 0);
        panelNumbersContents.add(labelNumberStepWidth, gridBagConstraints);

        spinnerNumberStepWidth.setModel(new SpinnerNumberModel(1, 1, 99, 1));
        spinnerNumberStepWidth.addChangeListener(new javax.swing.event.ChangeListener() {
            @Override
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spinnerNumberStepWidthStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = UiFactory.insets(0, 5, 0, 0);
        panelNumbersContents.add(spinnerNumberStepWidth, gridBagConstraints);

        labelNumberCount.setLabelFor(spinnerNumberCount);
        labelNumberCount.setText(Bundle.getString(getClass(), "RenameTemplatesPanel.labelNumberCount.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = UiFactory.insets(0, 5, 0, 0);
        panelNumbersContents.add(labelNumberCount, gridBagConstraints);

        spinnerNumberCount.setModel(new SpinnerNumberModel(3, 1, 7, 1));
        spinnerNumberCount.addChangeListener(new javax.swing.event.ChangeListener() {
            @Override
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spinnerNumberCountStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = UiFactory.insets(0, 5, 0, 0);
        panelNumbersContents.add(spinnerNumberCount, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = UiFactory.insets(5, 5, 5, 5);
        panelNumbers.add(panelNumbersContents, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        add(panelNumbers, gridBagConstraints);

        panelDateDelimiter.setBorder(javax.swing.BorderFactory.createTitledBorder(Bundle.getString(getClass(), "RenameTemplatesPanel.panelDateDelimiter.border.title"))); // NOI18N
        panelDateDelimiter.setLayout(new java.awt.GridBagLayout());

        labelDateDelim.setLabelFor(comboBoxDateDelimiter);
        labelDateDelim.setText(Bundle.getString(getClass(), "RenameTemplatesPanel.labelDateDelim.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = UiFactory.insets(5, 5, 0, 5);
        panelDateDelimiter.add(labelDateDelim, gridBagConstraints);

        comboBoxDateDelimiter.setModel(new DefaultComboBoxModel<>(new Object[] {"", "-", ".", "/"}));
        comboBoxDateDelimiter.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxDateDelimiterActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = UiFactory.insets(5, 5, 5, 5);
        panelDateDelimiter.add(comboBoxDateDelimiter, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = UiFactory.insets(5, 0, 0, 0);
        add(panelDateDelimiter, gridBagConstraints);

        panelDefineName.setBorder(javax.swing.BorderFactory.createTitledBorder(Bundle.getString(getClass(), "RenameTemplatesPanel.panelDefineName.border.title"))); // NOI18N
        panelDefineName.setLayout(new java.awt.GridBagLayout());

        panelDefineNameContents.setLayout(new java.awt.GridBagLayout());

        labelAtBegin.setLabelFor(comboBoxAtBegin);
        labelAtBegin.setText(Bundle.getString(getClass(), "RenameTemplatesPanel.labelAtBegin.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panelDefineNameContents.add(labelAtBegin, gridBagConstraints);

        labelDelim1.setLabelFor(textFieldDelim1);
        labelDelim1.setText(Bundle.getString(getClass(), "RenameTemplatesPanel.labelDelim1.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = UiFactory.insets(0, 10, 0, 0);
        panelDefineNameContents.add(labelDelim1, gridBagConstraints);

        labelInTheMid.setLabelFor(comboBoxInTheMiddle);
        labelInTheMid.setText(Bundle.getString(getClass(), "RenameTemplatesPanel.labelInTheMid.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = UiFactory.insets(0, 10, 0, 0);
        panelDefineNameContents.add(labelInTheMid, gridBagConstraints);

        labelDelim2.setLabelFor(textFieldDelim2);
        labelDelim2.setText(Bundle.getString(getClass(), "RenameTemplatesPanel.labelDelim2.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = UiFactory.insets(0, 10, 0, 0);
        panelDefineNameContents.add(labelDelim2, gridBagConstraints);

        labelAtEnd.setLabelFor(comboBoxAtEnd);
        labelAtEnd.setText(Bundle.getString(getClass(), "RenameTemplatesPanel.labelAtEnd.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.insets = UiFactory.insets(0, 10, 0, 0);
        panelDefineNameContents.add(labelAtEnd, gridBagConstraints);

        comboBoxAtBegin.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxAtBeginActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = UiFactory.insets(5, 0, 0, 0);
        panelDefineNameContents.add(comboBoxAtBegin, gridBagConstraints);

        textFieldDelim1.setColumns(1);
        textFieldDelim1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textFieldDelim1.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textFieldDelim1KeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = UiFactory.insets(5, 10, 0, 0);
        panelDefineNameContents.add(textFieldDelim1, gridBagConstraints);

        comboBoxInTheMiddle.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxInTheMiddleActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = UiFactory.insets(5, 10, 0, 0);
        panelDefineNameContents.add(comboBoxInTheMiddle, gridBagConstraints);

        textFieldDelim2.setColumns(1);
        textFieldDelim2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textFieldDelim2.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textFieldDelim2KeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = UiFactory.insets(5, 10, 0, 0);
        panelDefineNameContents.add(textFieldDelim2, gridBagConstraints);

        comboBoxAtEnd.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxAtEndActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = UiFactory.insets(5, 10, 0, 0);
        panelDefineNameContents.add(comboBoxAtEnd, gridBagConstraints);

        textFieldAtBegin.setColumns(15);
        textFieldAtBegin.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textFieldAtBeginKeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.insets = UiFactory.insets(5, 0, 0, 0);
        panelDefineNameContents.add(textFieldAtBegin, gridBagConstraints);

        textFieldInTheMiddle.setColumns(15);
        textFieldInTheMiddle.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textFieldInTheMiddleKeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.insets = UiFactory.insets(5, 10, 0, 0);
        panelDefineNameContents.add(textFieldInTheMiddle, gridBagConstraints);

        textFieldAtEnd.setColumns(15);
        textFieldAtEnd.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textFieldAtEndKeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.insets = UiFactory.insets(5, 10, 0, 0);
        panelDefineNameContents.add(textFieldAtEnd, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = UiFactory.insets(5, 5, 5, 5);
        panelDefineName.add(panelDefineNameContents, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = UiFactory.insets(5, 0, 0, 0);
        add(panelDefineName, gridBagConstraints);

        panelExample.setBorder(javax.swing.BorderFactory.createTitledBorder(Bundle.getString(getClass(), "RenameTemplatesPanel.panelExample.border.title"))); // NOI18N
        panelExample.setLayout(new java.awt.GridBagLayout());

        panelExampleContents.setLayout(new java.awt.GridBagLayout());

        labelBefore.setText(Bundle.getString(getClass(), "RenameTemplatesPanel.labelBefore.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panelExampleContents.add(labelBefore, gridBagConstraints);

        labelBeforeFilename.setText(" "); // NOI18N
        labelBeforeFilename.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = UiFactory.insets(0, 5, 0, 0);
        panelExampleContents.add(labelBeforeFilename, gridBagConstraints);

        labelAfter.setText(Bundle.getString(getClass(), "RenameTemplatesPanel.labelAfter.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = UiFactory.insets(5, 0, 0, 0);
        panelExampleContents.add(labelAfter, gridBagConstraints);

        labelAfterFilename.setText(" "); // NOI18N
        labelAfterFilename.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = UiFactory.insets(5, 5, 0, 0);
        panelExampleContents.add(labelAfterFilename, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = UiFactory.insets(5, 5, 5, 5);
        panelExample.add(panelExampleContents, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = UiFactory.insets(5, 0, 0, 0);
        add(panelExample, gridBagConstraints);

        panelRenameTemplates.setBorder(javax.swing.BorderFactory.createTitledBorder(Bundle.getString(getClass(), "RenameTemplatesPanel.panelRenameTemplates.border.title"))); // NOI18N
        panelRenameTemplates.setLayout(new java.awt.GridBagLayout());

        panelRenameTemplatesContents.setLayout(new java.awt.GridBagLayout());

        comboBoxRenameTemplates.setModel(new RenameTemplatesComboBoxModel());
        comboBoxRenameTemplates.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxRenameTemplatesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        panelRenameTemplatesContents.add(comboBoxRenameTemplates, gridBagConstraints);

        panelButtonsRenameTemplates.setLayout(new java.awt.GridLayout(1, 0, UiFactory.scale(5), 0));

        buttonSaveRenameTemplate.setText(Bundle.getString(getClass(), "RenameTemplatesPanel.buttonSaveRenameTemplate.text")); // NOI18N
        buttonSaveRenameTemplate.setToolTipText(Bundle.getString(getClass(), "RenameTemplatesPanel.buttonSaveRenameTemplate.toolTipText")); // NOI18N
        buttonSaveRenameTemplate.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSaveRenameTemplateActionPerformed(evt);
            }
        });
        panelButtonsRenameTemplates.add(buttonSaveRenameTemplate);

        buttonRenameRenameTemplate.setText(Bundle.getString(getClass(), "RenameTemplatesPanel.buttonRenameRenameTemplate.text")); // NOI18N
        buttonRenameRenameTemplate.setToolTipText(Bundle.getString(getClass(), "RenameTemplatesPanel.buttonRenameRenameTemplate.toolTipText")); // NOI18N
        buttonRenameRenameTemplate.setEnabled(false);
        buttonRenameRenameTemplate.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonRenameRenameTemplateActionPerformed(evt);
            }
        });
        panelButtonsRenameTemplates.add(buttonRenameRenameTemplate);

        buttonDeleteRenameTemplate.setText(Bundle.getString(getClass(), "RenameTemplatesPanel.buttonDeleteRenameTemplate.text")); // NOI18N
        buttonDeleteRenameTemplate.setToolTipText(Bundle.getString(getClass(), "RenameTemplatesPanel.buttonDeleteRenameTemplate.toolTipText")); // NOI18N
        buttonDeleteRenameTemplate.setEnabled(false);
        buttonDeleteRenameTemplate.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonDeleteRenameTemplateActionPerformed(evt);
            }
        });
        panelButtonsRenameTemplates.add(buttonDeleteRenameTemplate);

        buttonUpdateRenameTemplate.setText(Bundle.getString(getClass(), "RenameTemplatesPanel.buttonUpdateRenameTemplate.text")); // NOI18N
        buttonUpdateRenameTemplate.setToolTipText(Bundle.getString(getClass(), "RenameTemplatesPanel.buttonUpdateRenameTemplate.toolTipText")); // NOI18N
        buttonUpdateRenameTemplate.setEnabled(false);
        buttonUpdateRenameTemplate.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonUpdateRenameTemplateActionPerformed(evt);
            }
        });
        panelButtonsRenameTemplates.add(buttonUpdateRenameTemplate);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = UiFactory.insets(10, 0, 0, 0);
        panelRenameTemplatesContents.add(panelButtonsRenameTemplates, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = UiFactory.insets(5, 5, 5, 5);
        panelRenameTemplates.add(panelRenameTemplatesContents, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = UiFactory.insets(5, 0, 0, 0);
        add(panelRenameTemplates, gridBagConstraints);
    }

    private void spinnerStartNumberStateChanged(javax.swing.event.ChangeEvent evt) {
        startNumberChanged();
    }

    private void spinnerNumberStepWidthStateChanged(javax.swing.event.ChangeEvent evt) {
        numberStepWidthChanged();
    }

    private void spinnerNumberCountStateChanged(javax.swing.event.ChangeEvent evt) {
        numberCountChanged();
    }

    private void comboBoxDateDelimiterActionPerformed(java.awt.event.ActionEvent evt) {
        dateDelimiterChanged();
    }

    private void comboBoxAtBeginActionPerformed(java.awt.event.ActionEvent evt) {
        atBeginChanged();
    }

    private void textFieldAtBeginKeyReleased(java.awt.event.KeyEvent evt) {
        textAtBeginChanged();
    }

    private void textFieldDelim1KeyReleased(java.awt.event.KeyEvent evt) {
        delimiter1Changed();
    }

    private void comboBoxInTheMiddleActionPerformed(java.awt.event.ActionEvent evt) {
        inTheMiddleChanged();
    }

    private void textFieldInTheMiddleKeyReleased(java.awt.event.KeyEvent evt) {
        textInTheMiddleChanged();
    }

    private void textFieldDelim2KeyReleased(java.awt.event.KeyEvent evt) {
        delimiter2Changed();
    }

    private void comboBoxAtEndActionPerformed(java.awt.event.ActionEvent evt) {
        atEndChanged();
    }

    private void textFieldAtEndKeyReleased(java.awt.event.KeyEvent evt) {
        textAtEndChanged();
    }

    private void comboBoxRenameTemplatesActionPerformed(java.awt.event.ActionEvent evt) {
        renameTemplateChanged();
    }

    private void buttonSaveRenameTemplateActionPerformed(java.awt.event.ActionEvent evt) {
        saveAsRenameTemplate();
    }

    private void buttonRenameRenameTemplateActionPerformed(java.awt.event.ActionEvent evt) {
        renameRenameTemplate();
    }

    private void buttonDeleteRenameTemplateActionPerformed(java.awt.event.ActionEvent evt) {
        deleteRenameTemplate();
    }

    private void buttonUpdateRenameTemplateActionPerformed(java.awt.event.ActionEvent evt) {
        updateRenameTemplate();
    }

    private void buttonCheckDirtyDialogCreateNewActionPerformed(java.awt.event.ActionEvent evt) {
        checkDirtyDialog.setVisible(false);
        saveAsRenameTemplate();
    }

    private void buttonCheckDirtyDialogUpdateActionPerformed(java.awt.event.ActionEvent evt) {
        checkDirtyDialog.setVisible(false);
        updateRenameTemplate();
    }

    private void buttonCheckDirtyDialogRejectActionPerformed(java.awt.event.ActionEvent evt) {
        checkDirtyDialog.setVisible(false);
        dirty = false;
    }

    private javax.swing.JButton buttonCheckDirtyDialogCreateNew;
    private javax.swing.JButton buttonCheckDirtyDialogReject;
    private javax.swing.JButton buttonCheckDirtyDialogUpdate;
    private javax.swing.JButton buttonDeleteRenameTemplate;
    private javax.swing.JButton buttonRenameRenameTemplate;
    private javax.swing.JButton buttonSaveRenameTemplate;
    private javax.swing.JButton buttonUpdateRenameTemplate;
    private DialogExt checkDirtyDialog;
    private javax.swing.JComboBox<Object> comboBoxAtBegin;
    private javax.swing.JComboBox<Object> comboBoxAtEnd;
    private javax.swing.JComboBox<Object> comboBoxDateDelimiter;
    private javax.swing.JComboBox<Object> comboBoxInTheMiddle;
    private javax.swing.JComboBox<Object> comboBoxRenameTemplates;
    private javax.swing.JLabel labelAfter;
    private javax.swing.JLabel labelAfterFilename;
    private javax.swing.JLabel labelAtBegin;
    private javax.swing.JLabel labelAtEnd;
    private javax.swing.JLabel labelBefore;
    private javax.swing.JLabel labelBeforeFilename;
    private javax.swing.JLabel labelDateDelim;
    private javax.swing.JLabel labelDelim1;
    private javax.swing.JLabel labelDelim2;
    private javax.swing.JLabel labelDirtyConfirmSaveChanges;
    private javax.swing.JLabel labelInTheMid;
    private javax.swing.JLabel labelNumberCount;
    private javax.swing.JLabel labelNumberStepWidth;
    private javax.swing.JLabel labelStartNumber;
    private javax.swing.JPanel panelButtonsRenameTemplates;
    private javax.swing.JPanel panelDateDelimiter;
    private javax.swing.JPanel panelDefineName;
    private javax.swing.JPanel panelDefineNameContents;
    private javax.swing.JPanel panelDirtyButtons;
    private javax.swing.JPanel panelExample;
    private javax.swing.JPanel panelExampleContents;
    private javax.swing.JPanel panelNumbers;
    private javax.swing.JPanel panelNumbersContents;
    private javax.swing.JPanel panelRenameTemplates;
    private javax.swing.JPanel panelRenameTemplatesContents;
    private javax.swing.JSpinner spinnerNumberCount;
    private javax.swing.JSpinner spinnerNumberStepWidth;
    private javax.swing.JSpinner spinnerStartNumber;
    private javax.swing.JTextField textFieldAtBegin;
    private javax.swing.JTextField textFieldAtEnd;
    private javax.swing.JTextField textFieldDelim1;
    private javax.swing.JTextField textFieldDelim2;
    private javax.swing.JTextField textFieldInTheMiddle;
}
