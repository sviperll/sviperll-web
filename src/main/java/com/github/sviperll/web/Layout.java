/*
 * Copyright (C) 2013 Victor Nazarov <asviraspossible@gmail.com>
 */

package com.github.sviperll.web;

import java.io.PrintWriter;
import com.github.sviperll.web.Renderable.Renderer;

public interface Layout {
    Renderer headerRenderer(PrintWriter pw);
    Renderer footerRenderer(PrintWriter pw);
}
