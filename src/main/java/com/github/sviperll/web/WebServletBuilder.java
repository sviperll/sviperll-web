/*
 * Copyright (C) 2013 Victor Nazarov <asviraspossible@gmail.com>
 */

package com.github.sviperll.web;

import java.util.logging.Logger;
import com.github.sviperll.web.WebServlet.WebViews;
import com.github.sviperll.web.WebServlet.RequestEnvironmentFactory;
import com.github.sviperll.web.decorator.HttpBasicAuthenticationHandler.AuthenticationCredentials;
import com.github.sviperll.web.decorator.RequestEnvironmentFactoryDecorator;

public class WebServletBuilder {
    public static <T, R extends Router<T>, V extends WebViews<T, R>> WebServletBuilder createInstance(RequestEnvironmentFactory<T, R, V> environmentFactory) {
        return new WebServletBuilder(RequestEnvironmentFactoryDecorator.createInstance(environmentFactory));
    }

    private final RequestEnvironmentFactoryDecorator<?, ?, ?> decorator;
    public WebServletBuilder(RequestEnvironmentFactoryDecorator<?, ?, ?> decorator) {
        this.decorator = decorator;
    }

    public void log(Logger logger) {
        decorator.log(logger);
    }

    public void requireHttpBasicAuthentication(AuthenticationCredentials credentials) {
        decorator.requireHttpBasicAuthentication(credentials);
    }

    public WebServlet getWebServlet() {
        return WebServlet.createInstance(decorator.getRequestEnvironmentFactory());
    }
}
