/*
 * Copyright (C) 2013 Victor Nazarov <asviraspossible@gmail.com>
 */

package org.sviperll.web.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface SQLEngine {
    BasicSQLQuery selectFrom(String selectSQL, String fromSQL);

    public interface BasicSQLQuery extends Cloneable {
        void addWhereClause(String whereSQL);
        FinalSQLQuery skipAndLimit(String orderSQL, int offset, int limit);
        FinalSQLQuery anySingleRecord();
        public BasicSQLQuery clone();
    }

    public interface FinalSQLQuery {
        PreparedStatement prepareFor(Connection connection) throws SQLException;
    }
}
