package de.elmar_baumann.imv.view.panels;

import de.elmar_baumann.imv.AppIcons;
import de.elmar_baumann.imv.data.SavedSearch;
import de.elmar_baumann.imv.data.SavedSearchPanel;
import de.elmar_baumann.imv.data.SavedSearchParamStatement;
import de.elmar_baumann.imv.database.metadata.Column;
import de.elmar_baumann.imv.database.metadata.DatabaseMetadataUtil;
import de.elmar_baumann.imv.database.metadata.ParamStatement;
import de.elmar_baumann.imv.database.metadata.Table;
import de.elmar_baumann.imv.database.metadata.exif.ColumnExifIdFiles;
import de.elmar_baumann.imv.database.metadata.exif.TableExif;
import de.elmar_baumann.imv.database.metadata.file.ColumnFilesFilename;
import de.elmar_baumann.imv.database.metadata.file.ColumnFilesId;
import de.elmar_baumann.imv.database.metadata.file.TableFiles;
import de.elmar_baumann.imv.database.metadata.xmp.ColumnXmpId;
import de.elmar_baumann.imv.database.metadata.xmp.ColumnXmpIdFiles;
import de.elmar_baumann.imv.database.metadata.xmp.TableXmp;
import de.elmar_baumann.imv.event.ListenerProvider;
import de.elmar_baumann.imv.event.SearchEvent;
import de.elmar_baumann.imv.event.SearchListener;
import de.elmar_baumann.imv.resource.Bundle;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author  Elmar Baumann <eb@elmar-baumann.de>
 */
public final class AdvancedSearchPanel extends javax.swing.JPanel
    implements SearchListener {

    private final List<SearchColumnPanel> searchColumnPanels = new LinkedList<SearchColumnPanel>();
    private List<SearchListener> searchListeners = new ArrayList<SearchListener>();
    private String searchName = ""; // NOI18N
    private boolean isSavedSearch = false;
    private ListenerProvider listenerProvider;

    /** Creates new form AdvancedSearchPanel */
    public AdvancedSearchPanel() {
        initComponents();
        postInitComponents();
    }

    private void postInitComponents() {
        listenerProvider = ListenerProvider.getInstance();
        searchListeners = listenerProvider.getSearchListeners();
        panelColumn1.setOperatorsEnabled(false);
        initSearchColumnPanelArray();
        listenToSearchPanels();
    }

    public void willDispose() {
        checkChanged();
        isSavedSearch = false;
        setSearchName(""); // NOI18N
    }

    private boolean checkIsSearchValid() {
        boolean canSearch = false;
        int count = searchColumnPanels.size();
        int index = 0;
        while (!canSearch && index < count) {
            canSearch = !searchColumnPanels.get(index++).getValue().isEmpty();
        }
        canSearch = canSearch && checkBrackets();
        if (!canSearch) {
            JOptionPane.showMessageDialog(this,
                Bundle.getString("AdvancedSearchDialog.ErrorMessage.InvalidQuery"),
                Bundle.getString("AdvancedSearchDialog.ErrorMessage.InvalidQuery.Title"),
                JOptionPane.ERROR_MESSAGE,
                AppIcons.getMediumAppIcon());
        }
        return canSearch;
    }

    private boolean checkBrackets() {
        int countOpenBrackets = 0;
        int countClosedBrackets = 0;
        for (SearchColumnPanel panel : searchColumnPanels) {
            countOpenBrackets += panel.getCountOpenBrackets();
            countClosedBrackets += panel.getCountClosedBrackets();
        }
        return countOpenBrackets == countClosedBrackets;
    }

    private void initSearchColumnPanelArray() {
        searchColumnPanels.add(panelColumn1);
        searchColumnPanels.add(panelColumn2);
        searchColumnPanels.add(panelColumn3);
        searchColumnPanels.add(panelColumn4);
        searchColumnPanels.add(panelColumn5);
    }

    private void listenToSearchPanels() {
        for (SearchColumnPanel panel : searchColumnPanels) {
            panel.addSearchListener(this);
        }
    }

    private SavedSearchParamStatement getParamStatementData() {
        SavedSearchParamStatement data = new SavedSearchParamStatement();
        ParamStatement stmt = getSql();
        data.setQuery(stmt.isQuery());
        data.setSql(stmt.getSql());
        List<String> values = stmt.getValuesAsStringList();
        data.setValues(values.size() > 0 ? values : null);
        return data;
    }

    private void notifySearch() {
        SearchEvent event = new SearchEvent(SearchEvent.Type.START);
        SavedSearch data = new SavedSearch();
        data.setParamStatements(getParamStatementData());
        event.setData(data);
        for (SearchListener listener : searchListeners) {
            listener.actionPerformed(event);
        }
    }

    private void notifySave(SavedSearch search) {
        SearchEvent event = new SearchEvent(SearchEvent.Type.SAVE);
        event.setData(search);
        event.setForceOverwrite(isSavedSearch);
        for (SearchListener listener : searchListeners) {
            listener.actionPerformed(event);
        }
    }

    private void notifyNameChanged() {
        SearchEvent event = new SearchEvent(SearchEvent.Type.NAME_CHANGED);
        event.setSearchName(searchName);
        for (SearchListener listener : searchListeners) {
            listener.actionPerformed(event);
        }
    }

    /**
     * Setzt eine gespeicherte Suche.
     * 
     * @param search Gespeicherte Suche
     */
    public void setSavedSearch(SavedSearch search) {
        emptyPanels();
        isSavedSearch = true;
        setSearchName(search.getName());
        setSavedSearchToPanels(search.getPanels());
    }

    private void setSavedSearchToPanels(List<SavedSearchPanel> data) {
        if (data != null) {
            int panelSize = searchColumnPanels.size();
            int dataSize = data.size();
            for (int dataIndex = 0; dataIndex < dataSize; dataIndex++) {
                if (dataIndex < panelSize) {
                    searchColumnPanels.get(dataIndex).setSavedSearchData(
                        data.get(dataIndex));
                }
            }
        }
    }

    private void emptyPanels() {
        checkChanged();
        for (SearchColumnPanel panel : searchColumnPanels) {
            panel.reset();
        }
    }

    private void checkChanged() {
        if (isSavedSearch && isChanged()) {
            if (JOptionPane.showConfirmDialog(
                this,
                Bundle.getString("AdvancedSearchDialog.ConfirmMessage.SaveChanges"),
                Bundle.getString("AdvancedSearchDialog.ConfirmMessage.SaveChanges.Title"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                AppIcons.getMediumAppIcon()) ==
                JOptionPane.YES_OPTION) {
                saveSearch();
            }
        }
    }

    private boolean isChanged() {
        for (SearchColumnPanel panel : searchColumnPanels) {
            if (panel.isChanged()) {
                return true;
            }
        }
        return false;
    }

    private void saveAs() {
        if (isSavedSearch) {
            isSavedSearch = false;
            saveSearch();
            isSavedSearch = true;
        } else {
            saveSearch();
        }
    }

    private boolean saveSearch() {
        if (checkIsSearchValid()) {
            String name = askSearchName();
            if (name != null && !name.isEmpty()) {
                setSearchName(name);
                setPanelsUnchanged();
                notifySave(getSavedSearch(name));
                return true;
            }
        }
        return false;
    }

    private String askSearchName() {
        if (isSavedSearch) {
            return searchName;
        } else {
            return JOptionPane.showInputDialog(
                Bundle.getString("AdvancedSearchDialog.Input.SearchName"),
                searchName);
        }
    }

    private void setPanelsUnchanged() {
        for (SearchColumnPanel panel : searchColumnPanels) {
            panel.setChanged(false);
        }
    }

    private SavedSearch getSavedSearch(String name) {
        SavedSearch search = new SavedSearch();
        SavedSearchParamStatement paramStatementData = getParamStatementData();
        paramStatementData.setName(name);
        search.setParamStatements(paramStatementData);
        search.setPanels(getPanelData());
        return search;
    }

    private List<SavedSearchPanel> getPanelData() {
        List<SavedSearchPanel> panelData = new ArrayList<SavedSearchPanel>();
        int size = searchColumnPanels.size();
        for (int index = 0; index < size; index++) {
            SearchColumnPanel panel = searchColumnPanels.get(index);
            SavedSearchPanel data = panel.getSavedSearchData();
            data.setPanelIndex(index);
            panelData.add(data);
        }
        return panelData.size() > 0 ? panelData : null;
    }

    private void search() {
        if (checkIsSearchValid()) {
            notifySearch();
        }
    }

    private void setSearchName(String name) {
        searchName = name;
        notifyNameChanged();
    }

    private ParamStatement getSql() {
        StringBuffer statement = getStartSelectFrom();
        List<String> values = new ArrayList<String>();
        ParamStatement stmt = new ParamStatement();

        appendFrom(statement);
        appendWhere(statement, values);
        stmt.setSql(statement.toString());
        stmt.setValues(values.toArray());
        stmt.setIsQuery(true);

        return stmt;
    }

    private StringBuffer getStartSelectFrom() {
        Column columnFilename = ColumnFilesFilename.getInstance();
        String columnNameFilename = columnFilename.getName();
        String tableNameFiles = columnFilename.getTable().getName();

        return new StringBuffer("SELECT DISTINCT " + tableNameFiles + "." + // NOI18N
            columnNameFilename + " FROM"); // NOI18N
    }

    private List<Column> getColumns() {
        List<Column> columns = new ArrayList<Column>();
        for (SearchColumnPanel panel : searchColumnPanels) {
            if (panel.hasSql()) {
                columns.add(panel.getSelectedColumn());
            }
        }
        return columns;
    }

    private void appendWhere(StringBuffer statement, List<String> values) {
        statement.append(" WHERE"); // NOI18N
        int index = 0;
        for (SearchColumnPanel panel : searchColumnPanels) {
            if (panel.hasSql()) {
                panel.setIsFirst(index++ == 0);
                statement.append(panel.getSqlString());
                values.add(panel.getValue());
            }
        }
    }

    private void appendFrom(StringBuffer statement) {
        List<Table> allTables =
            DatabaseMetadataUtil.getUniqueTablesOfColumnArray(getColumns());
        Column.ReferenceDirection back = Column.ReferenceDirection.BACKWARDS;
        List<Table> refsXmpTables = DatabaseMetadataUtil.getTablesWithReferenceTo(allTables, TableXmp.getInstance(), back);

        statement.append(" " + TableFiles.getInstance().getName()); // NOI18N

        if (allTables.contains(TableExif.getInstance())) {
            statement.append(getJoinFiles(TableExif.getInstance(),
                ColumnExifIdFiles.getInstance()));
        }

        if (allTables.contains(TableXmp.getInstance()) ||
            !refsXmpTables.isEmpty()) {
            statement.append(getJoinFiles(TableXmp.getInstance(),
                ColumnXmpIdFiles.getInstance()));
        }

        String xmpJoinCol =
            TableXmp.getInstance().getName() + "." + // NOI18N
            ColumnXmpId.getInstance().getName();
        appendInnerJoin(statement, refsXmpTables, TableXmp.getInstance(),
            xmpJoinCol);
    }

    private String getJoinFiles(Table joinTable, Column joinColumn) {
        return " INNER JOIN " + joinTable.getName() + " ON " + // NOI18N
            joinTable.getName() + "." + joinColumn.getName() + // NOI18N
            " = " + TableFiles.getInstance().getName() + // NOI18N
            "." + ColumnFilesId.getInstance().getName(); // NOI18N
    }

    private void appendInnerJoin(StringBuffer statement,
        List<Table> refsTables, Table referredTable, String joinCol) {
        for (Table refsTable : refsTables) {
            Column refColumn = refsTable.getJoinColumnsFor(referredTable).get(0);
            statement.append(" INNER JOIN " + refsTable.getName() + " ON " + // NOI18N
                refsTable.getName() + "." + refColumn.getName() + " = " + // NOI18N
                joinCol);
        }
    }

    @Override
    public void actionPerformed(SearchEvent evt) {
        if (evt.getType().equals(SearchEvent.Type.START)) {
            search();
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelColumns = new javax.swing.JPanel();
        panelColumn1 = new de.elmar_baumann.imv.view.panels.SearchColumnPanel();
        panelColumn2 = new de.elmar_baumann.imv.view.panels.SearchColumnPanel();
        panelColumn3 = new de.elmar_baumann.imv.view.panels.SearchColumnPanel();
        panelColumn4 = new de.elmar_baumann.imv.view.panels.SearchColumnPanel();
        panelColumn5 = new de.elmar_baumann.imv.view.panels.SearchColumnPanel();
        panelButtons = new javax.swing.JPanel();
        buttonSaveSearch = new javax.swing.JButton();
        buttonSaveAs = new javax.swing.JButton();
        buttonReset = new javax.swing.JButton();
        buttonSearch = new javax.swing.JButton();

        javax.swing.GroupLayout panelColumnsLayout = new javax.swing.GroupLayout(panelColumns);
        panelColumns.setLayout(panelColumnsLayout);
        panelColumnsLayout.setHorizontalGroup(
            panelColumnsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelColumn5, javax.swing.GroupLayout.DEFAULT_SIZE, 683, Short.MAX_VALUE)
            .addComponent(panelColumn4, javax.swing.GroupLayout.DEFAULT_SIZE, 683, Short.MAX_VALUE)
            .addComponent(panelColumn3, javax.swing.GroupLayout.DEFAULT_SIZE, 683, Short.MAX_VALUE)
            .addComponent(panelColumn2, javax.swing.GroupLayout.DEFAULT_SIZE, 683, Short.MAX_VALUE)
            .addComponent(panelColumn1, javax.swing.GroupLayout.DEFAULT_SIZE, 683, Short.MAX_VALUE)
        );
        panelColumnsLayout.setVerticalGroup(
            panelColumnsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelColumnsLayout.createSequentialGroup()
                .addComponent(panelColumn1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelColumn2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelColumn3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelColumn4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelColumn5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        buttonSaveSearch.setFont(new java.awt.Font("Dialog", 0, 12));
        buttonSaveSearch.setMnemonic('p');
        buttonSaveSearch.setText(Bundle.getString("AdvancedSearchPanel.buttonSaveSearch.text")); // NOI18N
        buttonSaveSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSaveSearchActionPerformed(evt);
            }
        });

        buttonSaveAs.setFont(new java.awt.Font("Dialog", 0, 12));
        buttonSaveAs.setText(Bundle.getString("AdvancedSearchPanel.buttonSaveAs.text")); // NOI18N
        buttonSaveAs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSaveAsActionPerformed(evt);
            }
        });

        buttonReset.setFont(new java.awt.Font("Dialog", 0, 12));
        buttonReset.setMnemonic('e');
        buttonReset.setText(Bundle.getString("AdvancedSearchPanel.buttonReset.text")); // NOI18N
        buttonReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonResetActionPerformed(evt);
            }
        });

        buttonSearch.setFont(new java.awt.Font("Dialog", 0, 12));
        buttonSearch.setMnemonic('s');
        buttonSearch.setText(Bundle.getString("AdvancedSearchPanel.buttonSearch.text")); // NOI18N
        buttonSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSearchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelButtonsLayout = new javax.swing.GroupLayout(panelButtons);
        panelButtons.setLayout(panelButtonsLayout);
        panelButtonsLayout.setHorizontalGroup(
            panelButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelButtonsLayout.createSequentialGroup()
                .addComponent(buttonSaveSearch)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonSaveAs)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonReset)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonSearch))
        );
        panelButtonsLayout.setVerticalGroup(
            panelButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(buttonSaveSearch)
                .addComponent(buttonSaveAs)
                .addComponent(buttonReset)
                .addComponent(buttonSearch))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 707, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelColumns, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelButtons, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 210, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelColumns, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelButtons, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

private void buttonSaveSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSaveSearchActionPerformed
    saveSearch();
}//GEN-LAST:event_buttonSaveSearchActionPerformed

private void buttonSaveAsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSaveAsActionPerformed
    saveAs();
}//GEN-LAST:event_buttonSaveAsActionPerformed

private void buttonResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonResetActionPerformed
    emptyPanels();
}//GEN-LAST:event_buttonResetActionPerformed

private void buttonSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSearchActionPerformed
    search();
}//GEN-LAST:event_buttonSearchActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonReset;
    private javax.swing.JButton buttonSaveAs;
    private javax.swing.JButton buttonSaveSearch;
    private javax.swing.JButton buttonSearch;
    private javax.swing.JPanel panelButtons;
    private de.elmar_baumann.imv.view.panels.SearchColumnPanel panelColumn1;
    private de.elmar_baumann.imv.view.panels.SearchColumnPanel panelColumn2;
    private de.elmar_baumann.imv.view.panels.SearchColumnPanel panelColumn3;
    private de.elmar_baumann.imv.view.panels.SearchColumnPanel panelColumn4;
    private de.elmar_baumann.imv.view.panels.SearchColumnPanel panelColumn5;
    private javax.swing.JPanel panelColumns;
    // End of variables declaration//GEN-END:variables
}
