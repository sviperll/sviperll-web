/*
 * Copyright (C) 2013 Victor Nazarov <asviraspossible@gmail.com>
 */

package com.github.sviperll.web.decorator;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.github.sviperll.web.ResourceFormatter;
import com.github.sviperll.web.WebEnvironment;
import com.github.sviperll.web.WebServlet.RequestHandler;

public class LoggingHandler<T> implements RequestHandler<T> {
    private final RequestHandler<T> handler;
    private final ResourceFormatter<T> locator;
    private final WebEnvironment web;
    private final Logger logger;
    public LoggingHandler(RequestHandler<T> handler, Logger logger, ResourceFormatter<T> locator, WebEnvironment web) {
        this.handler = handler;
        this.locator = locator;
        this.web = web;
        this.logger = logger;
    }

    @Override
    public void processRequest(T resource) throws IOException {
        String location = locator.formatResource(resource);
        String parameters = "";
        String method = web.request().getMethod();
        if (web.request().isGet()) {
            parameters = web.request().getParameters().encodeURLQuery("UTF-8");
            if (!parameters.isEmpty())
                parameters = "?" + parameters;
        }
        try {
            long startTime = System.currentTimeMillis();
            try {
                handler.processRequest(resource);
            } finally {
                long duration = System.currentTimeMillis() - startTime;
                Object[] messageParameters = new Object[] {method, location, parameters, duration};
                logger.log(Level.INFO, "Processed {0} to {1}{2} in {3} ms", messageParameters);
            }
        } catch (Exception ex) {
            String template = "Exception in {0} to {1}{2}";
            String message = MessageFormat.format(template, method, location, parameters);
            logger.log(Level.SEVERE, message, ex);
            if (ex instanceof IOException)
                throw (IOException)ex;
            else if (ex instanceof RuntimeException)
                throw (RuntimeException)ex;
            else
                throw new RuntimeException(ex);
        }
    }
}
