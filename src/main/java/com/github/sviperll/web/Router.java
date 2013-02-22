/*
 * Copyright (C) 2013 Victor Nazarov <asviraspossible@gmail.com>
 */

package com.github.sviperll.web;

public interface Router<T> extends ResourceFormatter<T> {
    T parseResource(ResourcePath path) throws ResourceParserException;

    static class ResourceParserException extends Exception {

        public ResourceParserException(String message) {
            super(message);
        }

        public ResourceParserException(String message, Exception ex) {
            super(message, ex);
        }
    }
}
