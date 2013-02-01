/*
 * Copyright (C) 2013 Victor Nazarov <asviraspossible@gmail.com>
 */

package org.sviperll.web;

import java.io.PrintWriter;
import org.sviperll.web.Renderable.Renderer;

public interface Layout {
    Renderer headerRenderer(PrintWriter pw);
    Renderer footerRenderer(PrintWriter pw);
}
