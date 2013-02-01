/*
 * Copyright (C) 2013 Victor Nazarov <asviraspossible@gmail.com>
 */

package org.sviperll.web;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class Parameters {
    private static final String[] EMPTY_STRING_ARRAY = new String[] {};
    public static Parameters decodeURLQuery(String queryString, String encoding) {
        Map<String, List<String>> map = new TreeMap<String, List<String>>();
        String[] assignments = queryString.split("&", -1);
        for (String assignment: assignments) {
            String[] pair = assignment.split("=", 2);
            String name = pair[0];
            String value = pair[1];
            List<String> list = map.get(name);
            if (list == null) {
                list = new ArrayList<String>();
                map.put(name, list);
            }
            list.add(value);
        }
        Map<String, String[]> result = new TreeMap<String, String[]>();
        for (Entry<String, List<String>> entry: map.entrySet()) {
            List<String> list = entry.getValue();
            result.put(entry.getKey(), list.toArray(new String[list.size()]));
        }
        return new Parameters(result);
    }

    private final Map<String, String[]> parameterMap;

    public Parameters(Map<String, String[]> parameterMap) {
        this.parameterMap = parameterMap;
    }

    public String encodeURLQuery(String encoding) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        Iterator<Entry<String, String[]>> i = parameterMap().entrySet().iterator();
        if (i.hasNext()) {
            Entry<String, String[]> entry = i.next();
            for (String value: entry.getValue()) {
                sb.append(URLEncoder.encode(entry.getKey(), encoding));
                sb.append("=");
                sb.append(URLEncoder.encode(value, encoding));
            }
            while (i.hasNext()) {
                sb.append("&");
                entry = i.next();
                for (String value: entry.getValue()) {
                    sb.append(URLEncoder.encode(entry.getKey(), encoding));
                    sb.append("=");
                    sb.append(URLEncoder.encode(value, encoding));
                }
            }
        }
        return sb.toString();
    }

    public Map<String, String[]> parameterMap() {
        return parameterMap;
    }

    public String[] getParameterValues(String name) {
        String[] values = parameterMap.get(name);
        return values == null ? EMPTY_STRING_ARRAY : values;
    }

    public String getParameterValue(String name) {
        String[] values = getParameterValues(name);
        return values.length == 0 ? null : values[values.length - 1];
    }
}
