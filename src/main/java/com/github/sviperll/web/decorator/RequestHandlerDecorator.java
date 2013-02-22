/*
 * Copyright (C) 2013 Victor Nazarov <asviraspossible@gmail.com>
 */

package com.github.sviperll.web.decorator;

import java.sql.Connection;
import java.util.logging.Logger;
import com.github.sviperll.web.ResourceFormatter;
import com.github.sviperll.web.WebEnvironment;
import com.github.sviperll.web.WebServlet.RequestHandler;
import com.github.sviperll.web.decorator.HttpBasicAuthenticationHandler.AuthenticationCredentials;

public class RequestHandlerDecorator<T> {
    public static <T> RequestHandlerDecorator<T> createInstance(RequestHandler<T> handler) {
        return new RequestHandlerDecorator<T>(handler);
    }

    private RequestHandler<T> handler;
    public RequestHandlerDecorator(RequestHandler<T> handler) {
        this.handler = handler;

    }

    public void requireHttpBasicAuthentication(AuthenticationCredentials credentials, WebEnvironment web) {
        handler = new HttpBasicAuthenticationHandler<T>(handler, credentials, web);
    }

    public void isolateTransactions(Connection connection, int isolationLevel) {
        handler = new IsolatedTransactionHandler<T>(handler, connection, isolationLevel);
    }

    public void log(Logger logger, ResourceFormatter<T> locator, WebEnvironment web) {
        handler = new LoggingHandler<T>(handler, logger, locator, web);
    }

    public RequestHandler<T> decoratedHandler() {
        return handler;
    }
}
