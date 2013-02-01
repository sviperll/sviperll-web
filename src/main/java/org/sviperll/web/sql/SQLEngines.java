/*
 * Copyright (C) 2013 Victor Nazarov <asviraspossible@gmail.com>
 */

package org.sviperll.web.sql;

public class SQLEngines {
    public static SQLEngine forURL(String JdbcURL) {
        if (JdbcURL.startsWith("jdbc:mysql:"))
            return new MySQLEngine();
        else if (JdbcURL.startsWith("jdbc:oracle:"))
            return new OracleSQLEngine();
        else if (JdbcURL.startsWith("jdbc:postgresql:"))
            return new PostgreSQLEngine();
        else
            throw new UnsupportedSQLEngineException();
    }

    private SQLEngines() {
    }

    public static class UnsupportedSQLEngineException extends RuntimeException {
    }
}
