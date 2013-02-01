/*
 * Copyright (C) 2013 Victor Nazarov <asviraspossible@gmail.com>
 */

package org.sviperll.web.handler;

import java.sql.Connection;
import java.util.logging.Logger;
import org.sviperll.web.ResourceFormatter;
import org.sviperll.web.WebEnvironment;
import org.sviperll.web.WebServlet.RequestHandler;
import org.sviperll.web.handler.HttpBasicAuthenticationHandler.AuthenticationCredentials;

public class RequestHandlerDecorator<T> {

    public static <T> RequestHandlerDecorator<T> createInstance(RequestHandler<T> handler) {
        return new RequestHandlerDecorator<T>(handler);
    }

    private RequestHandler<T> handler;
    public RequestHandlerDecorator(RequestHandler<T> handler) {
        this.handler = handler;

    }

    public void httpBasicAuthentication(AuthenticationCredentials credentials, WebEnvironment environment) {
        handler = new HttpBasicAuthenticationHandler<T>(handler, credentials, environment);
    }

    public void closeJdbcConnection(Connection connection) {
        handler = new JdbcConnectionClosingHandler<T>(handler, connection);
    }

    public void singleJdbcTransaction(Connection connection, int isolationLevel) {
        handler = new SingleJdbcTransactionHandler<T>(handler, connection, isolationLevel);
    }

    public void log(Logger logger, ResourceFormatter<T> locator) {
        handler = new LoggingHandler<T>(handler, logger, locator);
    }

    public RequestHandler<T> handler() {
        return handler;
    }
}
