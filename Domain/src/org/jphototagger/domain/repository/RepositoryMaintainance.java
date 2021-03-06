package org.jphototagger.domain.repository;

import org.jphototagger.api.function.Consumer;
import org.jphototagger.domain.repository.browse.ResultSetBrowserEvent;

/**
 * @author Elmar Baumann
 */
public interface RepositoryMaintainance {

    boolean compressRepository();

    int deleteNotReferenced1n();

    /**
     * Iterates through a SQL query result.
     *
     * @param sql      SELECT SQL statement
     * @param consumer consumer of the iteration events
     */
    void browse(String sql, Consumer<ResultSetBrowserEvent> consumer);
}
