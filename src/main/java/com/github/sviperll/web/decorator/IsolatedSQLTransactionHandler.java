/*
 * Copyright (C) 2013 Victor Nazarov <asviraspossible@gmail.com>
 */

package com.github.sviperll.web.decorator;

import com.github.sviperll.repository.SQLConnection;
import com.github.sviperll.repository.SQLTransactionIsolationLevel;
import java.io.IOException;
import java.sql.SQLException;
import com.github.sviperll.web.WebServlet.RequestHandler;

public class IsolatedSQLTransactionHandler<T> implements RequestHandler<T> {
    private final RequestHandler<T> handler;
    private final SQLConnection connection;
    private final SQLTransactionIsolationLevel isolationLevel;
    public IsolatedSQLTransactionHandler(RequestHandler<T> handler, SQLConnection connection, SQLTransactionIsolationLevel isolationLevel) {
        this.handler = handler;
        this.connection = connection;
        this.isolationLevel = isolationLevel;
    }

    @Override
    public void processRequest(T resource) throws IOException {
        try {
            connection.beginTransaction(isolationLevel);
            try {
                handler.processRequest(resource);
                connection.commitTransaction();
            } finally {
                connection.rollbackTransactionIfNotCommited();
            }
        } catch (SQLException ex) {
            throw new IOException(ex);
        }
    }
}
