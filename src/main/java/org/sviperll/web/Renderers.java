/*
 * Copyright (C) 2013 Victor Nazarov <asviraspossible@gmail.com>
 */

package org.sviperll.web;

import com.github.mustachejava.Mustache;
import java.io.Writer;
import org.sviperll.web.Renderable.Renderer;

public class Renderers {
    public static Renderer createRenderer(final Writer writer, final Mustache template, final Object scope) {
        return new Renderer() {
            @Override
            public void render() {
                template.execute(writer, scope);
            }
        };
    }

    public static Renderer createRenderer(final Writer writer, final Mustache template, final Object[] scopes) {
        return new Renderer() {
            @Override
            public void render() {
                template.execute(writer, scopes);
            }
        };
    }

    private Renderers() {
    }
}
