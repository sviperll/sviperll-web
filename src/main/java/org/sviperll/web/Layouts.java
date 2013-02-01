/*
 * Copyright (C) 2013 Victor Nazarov <asviraspossible@gmail.com>
 */

package org.sviperll.web;

import java.io.PrintWriter;
import org.sviperll.web.Renderable.Renderer;

public class Layouts {
    private static final Layout CHROMELESS = new ChromelessLayout();
    public static Layout chromeless() {
        return CHROMELESS;
    }

    public static Renderable embedRenderableInto(Renderable renderable, Layout layout) {
        return new EmbeddedRenderable(renderable, layout);
    }

    private Layouts() {
    }

    private static class ChromelessLayout implements Layout {
        @Override
        public Renderer headerRenderer(PrintWriter pw) {
            return new EmptyRenderer();
        }

        @Override
        public Renderer footerRenderer(PrintWriter pw) {
            return new EmptyRenderer();
        }

        private static class EmptyRenderer implements Renderer {
            @Override
            public void render() {
            }
        }
    }

    private static class EmbeddedRenderable implements Renderable {
        private final Renderable renderable;
        private final Layout layout;

        private EmbeddedRenderable(Renderable renderable, Layout layout) {
            this.renderable = renderable;
            this.layout = layout;
        }

        @Override
        public Renderer createRenderer(final PrintWriter pw) {
            final Renderer header = layout.headerRenderer(pw);
            final Renderer body = renderable.createRenderer(pw);
            final Renderer footer = layout.footerRenderer(pw);
            return new Renderer() {
                @Override
                public void render() {
                    header.render();
                    body.render();
                    footer.render();
                }
            };
        }
    }
}
