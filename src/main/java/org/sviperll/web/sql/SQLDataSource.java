/*
 * Copyright (C) 2013 Victor Nazarov <asviraspossible@gmail.com>
 */

package org.sviperll.web.sql;

import javax.sql.DataSource;

public interface SQLDataSource extends DataSource {
    SQLEngine getEngine();
}
