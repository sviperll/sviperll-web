/*
 * Copyright (C) 2013 Victor Nazarov <asviraspossible@gmail.com>
 */

package org.sviperll.web.handler;

import java.io.IOException;
import org.sviperll.io.Base64;
import org.sviperll.io.Charsets;
import org.sviperll.web.WebEnvironment;
import org.sviperll.web.WebServlet.RequestHandler;

public class HttpBasicAuthenticationHandler<T> implements RequestHandler<T> {
    private static final String BASIC_METHOD_STRING = "Basic";
    private final RequestHandler<T> handler;
    private final WebEnvironment environment;
    private final AuthenticationCredentials credentials;
    public HttpBasicAuthenticationHandler(RequestHandler<T> handler, AuthenticationCredentials credentials, WebEnvironment environment) {
        this.handler = handler;
        this.environment = environment;
        this.credentials = credentials;
    }

    @Override
    public void close() throws IOException {
        handler.close();
    }

    private boolean isAuthenticated() {
        String header = environment.request().getHeader("Authorization");
        if (header == null)
            return false;
        else {
            header = header.trim();
            if (!header.startsWith(BASIC_METHOD_STRING))
                return false;
            else {
                header = header.substring(BASIC_METHOD_STRING.length()).trim();
                return header.equals(credentials.authenticationString);
            }
        }
    }

    private void sendNotAuthenticated() throws IOException {
        environment.response().setHeader("WWW-Authenticate", "Basic realm=\"" + credentials.realm + "\"");
        environment.response().sendError(401);
    }

    @Override
    public void processRequest(T resource) throws IOException {
        if (isAuthenticated())
            handler.processRequest(resource);
        else
            sendNotAuthenticated();
    }

    public static class AuthenticationCredentials {
        public static AuthenticationCredentials createInstance(String realm, String user, String password) {
            String authenticationString = Base64.encode(user + ":" + password, Charsets.UTF8);
            return new AuthenticationCredentials(realm, authenticationString);
        }

        private final String realm;
        private final String authenticationString;
        private AuthenticationCredentials(String realm, String authenticationString) {
            this.realm = realm;
            this.authenticationString = authenticationString;
        }
    }
}
