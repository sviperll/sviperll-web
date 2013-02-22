/*
 * Copyright (C) 2013 Victor Nazarov <asviraspossible@gmail.com>
 */

package com.github.sviperll.web.decorator;

import java.io.IOException;
import com.github.sviperll.io.Base64;
import com.github.sviperll.io.Charsets;
import com.github.sviperll.web.WebEnvironment;
import com.github.sviperll.web.WebServlet.RequestHandler;

public class HttpBasicAuthenticationHandler<T> implements RequestHandler<T> {
    private static final String BASIC_METHOD_STRING = "Basic";
    private final RequestHandler<T> handler;
    private final AuthenticationCredentials credentials;
    private final WebEnvironment web;
    public HttpBasicAuthenticationHandler(RequestHandler<T> handler, AuthenticationCredentials credentials, WebEnvironment web) {
        this.handler = handler;
        this.credentials = credentials;
        this.web = web;
    }

    private boolean isAuthenticated() {
        String header = web.request().getHeader("Authorization");
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
        web.response().setHeader("WWW-Authenticate", "Basic realm=\"" + credentials.realm + "\"");
        web.response().sendError(401);
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
