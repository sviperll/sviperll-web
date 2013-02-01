/*
 * Copyright (C) 2013 Victor Nazarov <asviraspossible@gmail.com>
 */

package org.sviperll.web.sql;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.beans.PropertyVetoException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

public class DataSources {
    public static SQLDataSource createDataSource(Class<?> driver, String url) throws SQLException {
        try {
            ComboPooledDataSource pool = new ComboPooledDataSource();
            pool.setDriverClass(driver.getName());
            pool.setJdbcUrl(url);
            pool.setMaxStatements(180);
            return new DataSourceSQLDataSource(SQLEngines.forURL(url), pool);
        } catch (PropertyVetoException ex) {
            throw new SQLException(ex);
        }
    }

    public static class DataSourceSQLDataSource implements SQLDataSource {
        private final SQLEngine engine;
        private final DataSource dataSource;
        public DataSourceSQLDataSource(SQLEngine engine, DataSource dataSource) {
            this.engine = engine;
            this.dataSource = dataSource;
        }

        @Override
        public SQLEngine getEngine() {
            return engine;
        }

        @Override
        public Connection getConnection() throws SQLException {
            return dataSource.getConnection();
        }

        @Override
        public Connection getConnection(String username, String password) throws SQLException {
            return dataSource.getConnection(username, password);
        }

        @Override
        public PrintWriter getLogWriter() throws SQLException {
            return dataSource.getLogWriter();
        }

        @Override
        public void setLogWriter(PrintWriter out) throws SQLException {
            dataSource.setLogWriter(out);
        }

        @Override
        public void setLoginTimeout(int seconds) throws SQLException {
            dataSource.setLoginTimeout(seconds);
        }

        @Override
        public int getLoginTimeout() throws SQLException {
            return dataSource.getLoginTimeout();
        }

        @Override
        public <T> T unwrap(Class<T> iface) throws SQLException {
            return dataSource.unwrap(iface);
        }

        @Override
        public boolean isWrapperFor(Class<?> iface) throws SQLException {
            return dataSource.isWrapperFor(iface);
        }

    }

    private DataSources() {
    }
}
