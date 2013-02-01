/*
 * Copyright (C) 2013 Victor Nazarov <asviraspossible@gmail.com>
 */

package org.sviperll.web.handler;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import org.sviperll.web.WebServlet.RequestHandler;

public class JdbcConnectionClosingHandler<T> implements RequestHandler<T> {
    private final RequestHandler<T> handler;
    private final Connection connection;
    public JdbcConnectionClosingHandler(RequestHandler<T> handler, Connection connection) {
        this.handler = handler;
        this.connection = connection;
    }

    @Override
    public void processRequest(T resource) throws IOException {
        handler.processRequest(resource);
    }

    @Override
    public void close() throws IOException {
        try {
            handler.close();
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                throw new IOException(ex);
            }
        }
    }
}
