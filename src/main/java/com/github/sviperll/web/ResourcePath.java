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

    private final String[] segments;
    public ResourcePath(String[] segments) {
        this.segments = segments;
    }

    public String[] segments() {
        return segments;
    }

    public String segment(int index) {
        return segments[index];
    }

    public String segmentWithoutPrefix(ResourcePath prefix, int index) {
        return withoutPrefix(prefix).segment(index);
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

    public boolean matchesWithoutPrefix(ResourcePath prefix, String... segmentPatterns) {
        return withoutPrefix(prefix).matches(segmentPatterns);
    }

    public boolean isRoot() {
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
            if (this.segments.length != that.segments.length)
                return false;
            for (int i = 0; i < this.segments().length; i++)
                if (!this.segments[i].equals(that.segments[i]))
                    return false;
            return true;
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Arrays.deepHashCode(this.segments);
        return hash;
    }

    public boolean startsWith(ResourcePath prefix) {
        if (this.segments.length < prefix.segments.length)
            return false;
        for (int i = 0; i < prefix.segments.length; i++)
            if (!this.segments[i].equals(prefix.segments[i]))
                return false;
        return true;
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
}
