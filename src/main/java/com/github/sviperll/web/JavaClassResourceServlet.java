/*
 * Copyright (C) 2013 Victor Nazarov <asviraspossible@gmail.com>
 */

package com.github.sviperll.web;

import java.io.IOException;
import java.io.InputStream;
import javax.activation.MimetypesFileTypeMap;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.github.sviperll.collection.ByteBufferAllocator;
import com.github.sviperll.collection.ByteBufferAllocator.ByteBuffer;

public class JavaClassResourceServlet extends HttpServlet {
    private static String concatPath(String base, String relative) {
        if (base.endsWith("/") && relative.startsWith("/"))
            return base + relative.substring(1);
        else if (base.endsWith("/") || relative.startsWith("/"))
            return base + relative;
        else
            return base + "/" + relative;
    }
    private final JavaClassResourceLoader loader;
    private final MimetypesFileTypeMap mimeTypes;
    private final JavaClassResourcePath path;
    public JavaClassResourceServlet(JavaClassResourceLoader loader, MimetypesFileTypeMap mimeTypes, JavaClassResourcePath path) {
        this.loader = loader;
        this.mimeTypes = mimeTypes;
        this.path = path;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String relativePath = req.getRequestURI().substring(path.requestPathPrefix.length());
        String resourcePath = concatPath(path.rootResourceFolder, relativePath);
        InputStream resource = loader.classLoader.getResourceAsStream(resourcePath);
        if (resource == null)
            resp.sendError(404);
        else {
            try {
                resp.setContentType(mimeTypes.getContentType(resourcePath));
                ServletOutputStream outputStream = resp.getOutputStream();
                try {
                    ByteBuffer buffer = loader.bufferPool.allocateByteBuffer();
                    try {
                        int n;
                        while ((n = resource.read(buffer.array())) >= 0)
                            outputStream.write(buffer.array(), 0, n);
                    } finally {
                        buffer.free();
                    }
                } finally {
                    outputStream.close();
                }
            } finally {
                resource.close();
            }
        }
    }

    public static class JavaClassResourceLoader {
        private final ByteBufferAllocator bufferPool;
        private final Class<?> classLoader;

        public JavaClassResourceLoader(Class<?> classLoader, ByteBufferAllocator bufferPool) {
            this.classLoader = classLoader;
            this.bufferPool = bufferPool;
        }
    }

    public static class JavaClassResourcePath {
        private final String rootResourceFolder;
        private final String requestPathPrefix;

        public JavaClassResourcePath(String rootResourceFolder, String requestPathPrefix) {
            this.rootResourceFolder = rootResourceFolder;
            this.requestPathPrefix = requestPathPrefix;
        }
    }
}
