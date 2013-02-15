/*
 * Copyright (C) 2013 Victor Nazarov <asviraspossible@gmail.com>
 */

package org.sviperll.web.decorator;

import java.io.IOException;
import org.sviperll.web.Router;
import org.sviperll.web.WebEnvironment;
import org.sviperll.web.WebServlet.LayoutFactory;
import org.sviperll.web.WebServlet.RequestEnvironment;
import org.sviperll.web.WebServlet.RequestHandler;
import org.sviperll.web.decorator.HttpBasicAuthenticationHandler.AuthenticationCredentials;

public class HttpBasicAuthenticationRequestEnvironment<T, R extends Router<T>, V extends LayoutFactory<T>> implements RequestEnvironment<T, R, V> {
    private final RequestEnvironment<T, R, V> requestEnvironment;
    private final AuthenticationCredentials credentials;
    public HttpBasicAuthenticationRequestEnvironment(RequestEnvironment<T, R, V> requestEnvironment, AuthenticationCredentials credentials) {
        this.requestEnvironment = requestEnvironment;
        this.credentials = credentials;
    }

    @Override
    public R createRouter() {
        return requestEnvironment.createRouter();
    }

    @Override
    public V createViewDefinition(R router) throws IOException {
        return requestEnvironment.createViewDefinition(router);
    }

    @Override
    public RequestHandler<T> createRequestHandler(R router, V views, WebEnvironment web) throws IOException {
        return new HttpBasicAuthenticationHandler<T>(requestEnvironment.createRequestHandler(router, views, web), credentials, web);
    }

    @Override
    public void close() throws IOException {
        requestEnvironment.close();
    }
}
