/*
 * Copyright (C) 2013 Victor Nazarov <asviraspossible@gmail.com>
 */

package org.sviperll.web;

public class WebEnvironment {
    private final WebRequest request;
    private final WebResponse response;
    public WebEnvironment(WebRequest request, WebResponse response) {
        this.request = request;
        this.response = response;
    }

    public WebRequest request() {
        return request;
    }

    public WebResponse response() {
        return response;
    }

}
