/*
 * Copyright (C) 2013 Victor Nazarov <asviraspossible@gmail.com>
 */

package org.sviperll.web.decorator;

import java.io.IOException;
import java.util.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.sviperll.web.ResourceFormatter;
import org.sviperll.web.WebEnvironment;
import org.sviperll.web.WebServlet.RequestHandler;

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
                Object[] messageParameters = new Object[] {web.request().getMethod(), location, parameters, duration};
                logger.log(Level.INFO, "Processed {0} to {1}{2} in {3} ms", messageParameters);
            }
        } catch (Exception ex) {
            Formatter formatter = new Formatter();
            Object[] messageParameters = new Object[] {web.request().getMethod(), location, parameters};
            formatter.format("Exception in {0} to {1}{2}", messageParameters);
            logger.log(Level.SEVERE, formatter.toString(), ex);
            if (ex instanceof IOException)
                throw (IOException)ex;
            else if (ex instanceof RuntimeException)
                throw (RuntimeException)ex;
            else
                throw new RuntimeException(ex);
        }
    }
}
