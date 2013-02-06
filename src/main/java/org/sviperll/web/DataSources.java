/*
 * Copyright (C) 2013 Victor Nazarov <asviraspossible@gmail.com>
 */

package org.sviperll.web;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.beans.PropertyVetoException;
import java.sql.SQLException;
import javax.sql.DataSource;

public class DataSources {
    public static DataSource createDataSource(Class<?> driver, String url) throws SQLException {
        try {
            ComboPooledDataSource pool = new ComboPooledDataSource();
            pool.setDriverClass(driver.getName());
            pool.setJdbcUrl(url);
            pool.setMaxStatements(180);
            return pool;
        } catch (PropertyVetoException ex) {
            throw new SQLException(ex);
        }
    }

    private DataSources() {
    }
}
