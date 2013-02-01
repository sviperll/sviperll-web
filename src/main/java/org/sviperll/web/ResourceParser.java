/*
 * Copyright (C) 2013 Victor Nazarov <asviraspossible@gmail.com>
 */

package org.sviperll.web;

public interface ResourceParser<T> {
    T parseResource(String[] resourcePath) throws ResourceParserException;

    static class ResourceParserException extends Exception {

        public ResourceParserException(String message) {
            super(message);
        }
    }
}
