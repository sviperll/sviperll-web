/*
 * Copyright (C) 2012 Victor Nazarov <asviraspossible@gmail.com>
 */

package org.sviperll.web;

import java.io.Closeable;
import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.sviperll.web.ResourceParser.ResourceParserException;
import org.sviperll.web.WebServlet.RequestHandler.RequestHandlerFactory;

public class WebServlet extends HttpServlet {
    private final ResourceDefinition<?> resourceDefinition;
    private final Layout layout;

    public WebServlet(ResourceDefinition<?> resourceDefinition, Layout layout) {
        this.resourceDefinition = resourceDefinition;
        this.layout = layout;
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        WebEnvironment environment = new WebEnvironment(new WebRequest(req), new WebResponse(resp, layout));
        String path = req.getRequestURI();
        if (path.startsWith("/"))
            path = path.substring(1);
        String[] resourcePath = path.split("/");
        Responder responder = resourceDefinition.createResponder(environment);
        responder.respond(resourcePath);
    }

    public interface RequestHandler<T> extends Closeable {
        void processRequest(T resource) throws IOException;

        interface RequestHandlerFactory<T> {
            RequestHandler<T> openRequestHandler(WebEnvironment environment) throws IOException;
        }
    }

    public static class ResourceDefinition<T> {
        private final ResourceParser<T> resourceParser;
        private final RequestHandlerFactory<T> handlerFactory;

        public ResourceDefinition(ResourceParser<T> resourceParser, RequestHandlerFactory<T> handlerFactory) {
            this.resourceParser = resourceParser;
            this.handlerFactory = handlerFactory;
        }

        Responder createResponder(WebEnvironment environment) {
            return new Responder<T>(resourceParser, handlerFactory, environment);
        }
    }

    private static class Responder<T> {
        private final RequestHandlerFactory<T> handlerFactory;
        private final WebEnvironment environment;
        private final ResourceParser<T> resourceParser;

        Responder(ResourceParser<T> resourceParser, RequestHandlerFactory<T> handlerFactory, WebEnvironment environment) {
            this.handlerFactory = handlerFactory;
            this.environment = environment;
            this.resourceParser = resourceParser;
        }

        void respond(String[] resourcePath) throws IOException {
            try {
                T resource = resourceParser.parseResource(resourcePath);
                RequestHandler<T> handler = handlerFactory.openRequestHandler(environment);
                try {
                    handler.processRequest(resource);
                } finally {
                    handler.close();
                }
            } catch (ResourceParserException ex) {
                throw new IOException(ex);
            }
        }
    }
}
