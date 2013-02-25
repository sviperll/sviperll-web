/*
 * Copyright (C) 2013 Victor Nazarov <asviraspossible@gmail.com>
 */

package com.github.sviperll.web;

import java.util.Arrays;

public class ResourcePath {

    public static ResourcePath parseResourcePath(String path) {
        if (path.equals("/"))
            return new ResourcePath(new String[] {});
        else {
            if (path.startsWith("/"))
                path = path.substring(1);
            return new ResourcePath(path.split("/"));
        }
    }

    public static ResourcePath createInstance(String... segments) {
        return new ResourcePath(segments);
    }
    private final String[] segments;
    public ResourcePath(String[] segments) {
        this.segments = segments;
    }

    public String[] segments() {
        return segments;
    }

    /**
     * Each segment pattern can be either a string or null.
     * If segment pattern is a string it matches exact string in segments.
     * If segment pattern is null it matches any segment.
     *
     * @param segmentPatterns, array of segmentPatters, not null
     * @return true if
     * <li> all patterns matches all segments and
     * <li> number of segments and segment patterns is the same.
     */
    public boolean matches(String... segmentPatterns) {
        if (segments.length != segmentPatterns.length)
            return false;
        for (int i = 0; i < segments.length; i++) {
            String segmentPattern = segmentPatterns[i];
            if (!(segmentPattern == null || segments[i].equals(segmentPattern)))
                return false;
        }
        return true;
    }

    public boolean matches(ResourcePath prefix, String... segmentPatterns) {
        int prefixLength = prefix.segments.length;
        if (segments.length != prefixLength + segmentPatterns.length)
            return false;
        for (int i = 0; i < segments.length; i++) {
            String segmentPattern = i < prefixLength ? prefix.segments[i] : segmentPatterns[i - prefixLength];
            if (!(segmentPattern == null || segments[i].equals(segmentPattern)))
                return false;
        }
        return true;
    }

    public boolean isEmpty() {
        return segments.length == 0;
    }

    public ResourcePath subResource(String... subsegments) {
        String[] result = Arrays.copyOf(segments, segments.length + subsegments.length);
        System.arraycopy(subsegments, 0, result, segments.length, subsegments.length);
        return new ResourcePath(result);
    }

    public String path() {
        return toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String s: segments) {
            sb.append("/");
            sb.append(s);
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object thatObject) {
        if (this == thatObject)
            return true;
        else if (!(thatObject instanceof ResourcePath))
            throw new IllegalArgumentException("Object is not ResourcePath: " + thatObject);
        else {
            ResourcePath that = (ResourcePath)thatObject;
            return this.matches(that);
        }
    }

    public boolean startsWith(ResourcePath prefix) {
        if (this.segments.length < prefix.segments.length)
            return false;
        for (int i = 0; i < prefix.segments.length; i++)
            if (!this.segments[i].equals(prefix.segments[i]))
                return false;
        return true;
    }

    public boolean startsWith(String... segments) {
        return startsWith(new ResourcePath(segments));
    }

    public ResourcePath withoutPrefix(ResourcePath prefix) {
        if (this.segments.length < prefix.segments.length)
            throw new IllegalArgumentException("Incorect prefix: " + prefix);
        for (int i = 0; i < prefix.segments.length; i++)
            if (!this.segments[i].equals(prefix.segments[i]))
                throw new IllegalArgumentException("Incorect prefix: " + prefix);
        String[] result = Arrays.copyOfRange(segments, prefix.segments.length, segments.length);
        return new ResourcePath(result);
    }

    public ResourcePath withoutPrefix(String... segments) {
        return withoutPrefix(new ResourcePath(segments));
    }
}
