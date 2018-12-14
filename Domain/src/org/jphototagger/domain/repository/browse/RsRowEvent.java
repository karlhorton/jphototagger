package org.jphototagger.domain.repository.browse;

import java.util.Objects;

/**
 * @author Elmar Baumann
 */
public final class RsRowEvent implements ResultSetBrowserEvent {

    private final Object[] rowData;

    public RsRowEvent(Object[] rowData) {
        this.rowData = Objects.requireNonNull(rowData, "rowData == null");
    }

    @Override
    public void publish(ResultSetBrowser browser) {
        browser.row(rowData);
    }
}
