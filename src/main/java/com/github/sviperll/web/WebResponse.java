/*
 * Copyright (C) 2013 Victor Nazarov <asviraspossible@gmail.com>
 */

package com.github.sviperll.web;

import com.github.sviperll.web.Renderable.Renderer;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class WebResponse extends HttpServletResponseWrapper {
    private Layout layout;

    public WebResponse(HttpServletResponse response, Layout layout) {
        super(response);
        this.layout = layout;
    }

    public void render(Renderable renderable) throws IOException {
        setContentType("text/html");
        setCharacterEncoding("UTF-8");
        PrintWriter writer = getHtmlWriter();
        try {
            Renderer renderer = renderable.createRenderer(writer);
            renderer.render();
        } finally {
            writer.close();
        }
    }

    public PrintWriter getHtmlWriter() throws IOException {
        setContentType("text/html");
        setCharacterEncoding("UTF-8");
        HtmlWriter htmlWriter = new HtmlWriter(getWriter(), layout);
        htmlWriter.printHeader();
        return htmlWriter;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }

    public void sendRedirect(ResourcePath path) throws IOException {
        sendRedirect(path.toString());
    }

    private static class HtmlWriter extends PrintWriter {
        private final Layout layout;
        public HtmlWriter(Writer writer, Layout layout) {
            super(writer);
            this.layout = layout;
        }

        private void printHeader() {
            Renderer renderer = layout.headerRenderer(this);
            renderer.render();
        }

        private void printFooter() {
            Renderer renderer = layout.footerRenderer(this);
            renderer.render();
        }

        @Override
        public void close() {
            try {
                printFooter();
            } finally {
                super.close();
            }
        }
    }
}
