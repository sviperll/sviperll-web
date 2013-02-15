/*
 * Copyright (C) 2013 Victor Nazarov <asviraspossible@gmail.com>
 */

package org.sviperll.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class WebRequest extends HttpServletRequestWrapper {
    public WebRequest(HttpServletRequest request) {
        super(request);
    }

    @SuppressWarnings("unchecked")
    public Parameters getParameters() {
        return new Parameters(getParameterMap());
    }

    public boolean isGet() {
        return getMethod().equals("GET");
    }

    public boolean isPost() {
        return getMethod().equals("POST");
    }
}
