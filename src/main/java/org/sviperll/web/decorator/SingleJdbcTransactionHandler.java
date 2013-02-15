/*
 * Copyright (C) 2013 Victor Nazarov <asviraspossible@gmail.com>
 */

package org.sviperll.web.decorator;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import org.sviperll.web.WebServlet.RequestHandler;

public class SingleJdbcTransactionHandler<T> implements RequestHandler<T> {
    private final RequestHandler<T> handler;
    private final Connection connection;
    private final int isolationLevel;
    public SingleJdbcTransactionHandler(RequestHandler<T> handler, Connection connection, int isolationLevel) {
        this.handler = handler;
        this.connection = connection;
        this.isolationLevel = isolationLevel;
    }

    @Override
    public void processRequest(T resource) throws IOException {
        try {
            int previousIsolationLevel = connection.getTransactionIsolation();
            connection.setTransactionIsolation(isolationLevel);
            try {
                boolean autoCommit = connection.getAutoCommit();
                connection.setAutoCommit(false);
                try {
                    boolean rollback = true;
                    try {
                        handler.processRequest(resource);
                        connection.commit();
                        rollback = false;
                    } finally {
                        if (rollback)
                            connection.rollback();
                    }
                } finally {
                    connection.setAutoCommit(autoCommit);
                }
            } finally {
                connection.setTransactionIsolation(previousIsolationLevel);
            }
        } catch (SQLException ex) {
            throw new IOException(ex);
        }
    }
}
