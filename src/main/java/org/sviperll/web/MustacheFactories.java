/*
 * Copyright (C) 2013 Victor Nazarov <asviraspossible@gmail.com>
 */

package org.sviperll.web;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.MustacheFactory;

public class MustacheFactories {
    public static final MustacheFactory DEFAULT = new DefaultMustacheFactory();

    private MustacheFactories() {
    }
}
