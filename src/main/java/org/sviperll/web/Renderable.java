/*
 * Copyright (C) 2013 Victor Nazarov <asviraspossible@gmail.com>
 */

package org.sviperll.web;

import java.io.PrintWriter;

public interface Renderable {
    Renderer createRenderer(PrintWriter pw);

    public interface Renderer {
        void render();
    }
}
