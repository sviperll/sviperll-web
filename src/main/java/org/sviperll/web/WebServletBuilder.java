/*
 * Copyright (C) 2013 Victor Nazarov <asviraspossible@gmail.com>
 */

package org.sviperll.web;

import java.util.logging.Logger;
import org.sviperll.web.WebServlet.LayoutFactory;
import org.sviperll.web.WebServlet.RequestEnvironmentFactory;
import org.sviperll.web.decorator.HttpBasicAuthenticationHandler.AuthenticationCredentials;
import org.sviperll.web.decorator.RequestEnvironmentFactoryDecorator;

public class WebServletBuilder {
    public static <T, R extends Router<T>, V extends LayoutFactory<T>> WebServletBuilder createInstance(RequestEnvironmentFactory<T, R, V> environmentFactory) {
        return new WebServletBuilder(RequestEnvironmentFactoryDecorator.createInstance(environmentFactory));
    }

    private final RequestEnvironmentFactoryDecorator<?, ?, ?> decorator;
    public WebServletBuilder(RequestEnvironmentFactoryDecorator<?, ?, ?> decorator) {
        this.decorator = decorator;
    }

    public void log(Logger logger) {
        decorator.log(logger);
    }

    public void httpBasicAuthentication(AuthenticationCredentials credentials) {
        decorator.httpBasicAuthentication(credentials);
    }

    public WebServlet getWebServlet() {
        return WebServlet.createInstance(decorator.getRequestEnvironmentFactory());
    }
}
