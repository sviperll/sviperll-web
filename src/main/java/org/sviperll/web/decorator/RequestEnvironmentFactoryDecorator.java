/*
 * Copyright (C) 2013 Victor Nazarov <asviraspossible@gmail.com>
 */

package org.sviperll.web.decorator;

import java.io.IOException;
import java.util.logging.Logger;
import org.sviperll.web.Router;
import org.sviperll.web.WebServlet.WebViews;
import org.sviperll.web.WebServlet.RequestEnvironment;
import org.sviperll.web.WebServlet.RequestEnvironmentFactory;
import org.sviperll.web.decorator.HttpBasicAuthenticationHandler.AuthenticationCredentials;

public class RequestEnvironmentFactoryDecorator<T, R extends Router<T>, V extends WebViews<T, R>> {
    public static <T, R extends Router<T>, V extends WebViews<T, R>> RequestEnvironmentFactoryDecorator<T, R, V> createInstance(RequestEnvironmentFactory<T, R, V> environmentFactory) {
        return new RequestEnvironmentFactoryDecorator<T, R, V>(environmentFactory);
    }

    private RequestEnvironmentFactory<T, R, V> environmentFactory;
    public RequestEnvironmentFactoryDecorator(RequestEnvironmentFactory<T, R, V> environmentFactory) {
        this.environmentFactory = environmentFactory;
    }

    public void log(Logger logger) {
        environmentFactory = new LoggingRequestEnvironmentFactory<T, R, V>(environmentFactory, logger);
    }

    public void httpBasicAuthentication(AuthenticationCredentials credentials) {
        environmentFactory = new HttpBasicAuthenticationRequestEnvironmentFactory<T, R, V>(environmentFactory, credentials);
    }

    public RequestEnvironmentFactory<T, R, V> getRequestEnvironmentFactory() {
        return environmentFactory;
    }

    private static class LoggingRequestEnvironmentFactory<T, R extends Router<T>, V extends WebViews<T, R>> implements RequestEnvironmentFactory<T, R, V> {
        private final RequestEnvironmentFactory<T, R, V> environmentFactory;
        private final Logger logger;

        public LoggingRequestEnvironmentFactory(RequestEnvironmentFactory<T, R, V> environmentFactory, Logger logger) {
            this.environmentFactory = environmentFactory;
            this.logger = logger;
        }

        @Override
        public RequestEnvironment<T, R, V> createRequestEnvironment() throws IOException {
            return new LoggingRequestEnvironment<T, R, V>(environmentFactory.createRequestEnvironment(), logger);
        }
    }

    private static class HttpBasicAuthenticationRequestEnvironmentFactory<T, R extends Router<T>, V extends WebViews<T, R>> implements RequestEnvironmentFactory<T, R, V> {
        private final RequestEnvironmentFactory<T, R, V> environmentFactory;
        private final AuthenticationCredentials credentials;

        public HttpBasicAuthenticationRequestEnvironmentFactory(RequestEnvironmentFactory<T, R, V> environmentFactory, AuthenticationCredentials credentials) {
            this.environmentFactory = environmentFactory;
            this.credentials = credentials;
        }

        @Override
        public RequestEnvironment<T, R, V> createRequestEnvironment() throws IOException {
            return new HttpBasicAuthenticationRequestEnvironment<T, R, V>(environmentFactory.createRequestEnvironment(), credentials);
        }
    }
}
