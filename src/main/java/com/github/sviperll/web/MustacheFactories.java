/*
 * Copyright (C) 2013 Victor Nazarov <asviraspossible@gmail.com>
 */

package com.github.sviperll.web;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.MustacheFactory;

public class MustacheFactories {
    private static final MustacheFactory INSTANCE = new DefaultMustacheFactory();

    public static MustacheFactory getInstance() {
        return INSTANCE;
    }

    private MustacheFactories() {
    }
}
