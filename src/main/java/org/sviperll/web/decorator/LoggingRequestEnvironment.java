/*
 * Copyright (C) 2013 Victor Nazarov <asviraspossible@gmail.com>
 */

package org.sviperll.web.decorator;

import java.io.IOException;
import java.util.logging.Logger;
import org.sviperll.web.Router;
import org.sviperll.web.WebEnvironment;
import org.sviperll.web.WebServlet.WebViews;
import org.sviperll.web.WebServlet.RequestEnvironment;
import org.sviperll.web.WebServlet.RequestHandler;

public class LoggingRequestEnvironment<T, R extends Router<T>, V extends WebViews<T, R>> implements RequestEnvironment<T, R, V> {
    private final RequestEnvironment<T, R, V> requestEnvironment;
    private final Logger logger;
    public LoggingRequestEnvironment(RequestEnvironment<T, R, V> requestEnvironment, Logger logger) {
        this.requestEnvironment = requestEnvironment;
        this.logger = logger;
    }

    @Override
    public R createRouter() {
        return requestEnvironment.createRouter();
    }

    @Override
    public V createViews(R router) throws IOException {
        return requestEnvironment.createViews(router);
    }

    @Override
    public RequestHandler<T> createRequestHandler(V views, WebEnvironment web) throws IOException {
        return new LoggingHandler<T>(requestEnvironment.createRequestHandler(views, web), logger, views.router(), web);
    }

    @Override
    public void close() throws IOException {
        requestEnvironment.close();
    }
}
