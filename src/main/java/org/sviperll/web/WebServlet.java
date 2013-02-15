/*
 * Copyright (C) 2012 Victor Nazarov <asviraspossible@gmail.com>
 */

package org.sviperll.web;

import java.io.Closeable;
import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.sviperll.web.Router.ResourceParserException;

public class WebServlet extends HttpServlet {
    public static <T, R extends Router<T>, V extends LayoutFactory<T>> WebServlet createInstance(RequestEnvironmentFactory<T, R, V> environment) {
        return new WebServlet(new RequestEnvironmentHolder<T, R, V>(environment));
    }

    private final RequestEnvironmentHolder<?, ?, ?> requestEnvironment;

    public WebServlet(RequestEnvironmentHolder<?, ?, ?> requestEnvironment) {
        this.requestEnvironment = requestEnvironment;
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        requestEnvironment.respond(ResourcePath.createInstance(req.getRequestURI()), req, resp);
    }

    public interface RequestHandler<T> {
        void processRequest(T resource) throws IOException;
    }

    public interface RequestEnvironmentFactory<T, R extends Router<T>, V extends LayoutFactory<T>> {
        RequestEnvironment<T, R, V> createRequestEnvironment() throws IOException;
    }

    public interface LayoutFactory<T> {
        Layout createLayout(T resource);
    }

    public interface RequestEnvironment<T, R extends Router<T>, V extends LayoutFactory<T>> extends Closeable {
        R createRouter();
        V createViewDefinition(R router) throws IOException;
        RequestHandler<T> createRequestHandler(R router, V views, WebEnvironment web) throws IOException;
    }

    public static class RequestEnvironmentHolder<T, R extends Router<T>, V extends LayoutFactory<T>> {
        public static <T, R extends Router<T>, V extends LayoutFactory<T>> RequestEnvironmentHolder<T, R, V> createInstance(RequestEnvironmentFactory<T, R, V> environment) {
            return new RequestEnvironmentHolder<T, R, V>(environment);
        }

        private final RequestEnvironmentFactory<T, R, V> requestEnvironmentFactory;

        public RequestEnvironmentHolder(RequestEnvironmentFactory<T, R, V> requestEnvironmentFactory) {
            this.requestEnvironmentFactory = requestEnvironmentFactory;
        }

        void respond(ResourcePath resourcePath, HttpServletRequest req, HttpServletResponse resp) throws IOException {
            RequestEnvironment<T, R, V> environment = requestEnvironmentFactory.createRequestEnvironment();
            try {
                R router = environment.createRouter();
                T resource = router.parseResource(resourcePath);
                V views = environment.createViewDefinition(router);
                Layout layout = views.createLayout(resource);
                WebEnvironment web = new WebEnvironment(new WebRequest(req), new WebResponse(resp, layout));
                RequestHandler<T> handler = environment.createRequestHandler(router, views, web);
                handler.processRequest(resource);
            } catch (ResourceParserException ex) {
                resp.sendError(404);
            } finally {
                environment.close();
            }
        }
    }
}
