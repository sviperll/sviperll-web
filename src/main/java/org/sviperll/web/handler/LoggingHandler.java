/*
 * Copyright (C) 2013 Victor Nazarov <asviraspossible@gmail.com>
 */

package org.sviperll.web.handler;

import java.io.IOException;
import java.util.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.sviperll.web.ResourceFormatter;
import org.sviperll.web.WebServlet.RequestHandler;

public class LoggingHandler<T> implements RequestHandler<T> {
    private final RequestHandler<T> handler;
    private final ResourceFormatter<T> locator;
    private final Logger logger;
    public LoggingHandler(RequestHandler<T> handler, Logger logger, ResourceFormatter<T> locator) {
        this.handler = handler;
        this.locator = locator;
        this.logger = logger;
    }

    @Override
    public void processRequest(T resource) throws IOException {
        try {
            long startTime = System.currentTimeMillis();
            String location = locator.formatResource(resource);
            try {
                handler.processRequest(resource);
            } finally {
                long duration = System.currentTimeMillis() - startTime;
                logger.log(Level.INFO, "Processed {0} in {1} ms", new Object[] {location, duration});
            }
        } catch (Exception ex) {
            Formatter formatter = new Formatter();
            formatter.format("Exception in {0}", locator.formatResource(resource));
            logger.log(Level.SEVERE, formatter.toString(), ex);
            if (ex instanceof IOException)
                throw (IOException)ex;
            else if (ex instanceof RuntimeException)
                throw (RuntimeException)ex;
            else
                throw new RuntimeException(ex);
        }
    }

    @Override
    public void close() throws IOException {
        handler.close();
    }
}
