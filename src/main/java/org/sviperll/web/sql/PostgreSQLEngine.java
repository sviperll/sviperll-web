/*
 * Copyright (C) 2013 Victor Nazarov <asviraspossible@gmail.com>
 */

package org.sviperll.web.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class PostgreSQLEngine implements SQLEngine {
    @Override
    public BasicSQLQuery selectFrom(String selectSQL, String fromSQL) {
        return new PostgreSQLBasicQuery(selectSQL, fromSQL);
    }

    private static class PostgreSQLBasicQuery implements BasicSQLQuery {
        private final String selectSQL;
        private final String fromSQL;
        private final List<String> whereSQLList = new ArrayList<String>();

        private PostgreSQLBasicQuery(String selectSQL, String fromSQL) {
            this.selectSQL = selectSQL;
            this.fromSQL = fromSQL;
        }

        @Override
        public void addWhereClause(String whereSQL) {
            whereSQLList.add(whereSQL);
        }

        @Override
        public FinalSQLQuery skipAndLimit(String orderSQL, int offset, int limit) {
            StringBuilder sb = createStringBuiler();
            sb.append(" ORDER BY ");
            sb.append(orderSQL);
            sb.append(" LIMIT ");
            sb.append(limit);
            sb.append(" OFFSET ");
            sb.append(offset);
            return new PostgreSQLFinalQuery(sb.toString());
        }

        @Override
        public FinalSQLQuery anySingleRecord() {
            StringBuilder sb = createStringBuiler();
            sb.append(" LIMIT 1");
            return new PostgreSQLFinalQuery(sb.toString());
        }

        private StringBuilder createStringBuiler() {
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT ");
            sb.append(selectSQL);
            sb.append(" FROM ");
            sb.append(fromSQL);
            Iterator<String> i = whereSQLList.iterator();
            if (i.hasNext()) {
                sb.append(" WHERE (");
                sb.append(i.next());
                while (i.hasNext()) {
                    sb.append(") AND (");
                    sb.append(i.next());
                }
                sb.append(")");
            }
            return sb;
        }

        @Override
        public BasicSQLQuery clone() {
            BasicSQLQuery result = new PostgreSQLBasicQuery(selectSQL, fromSQL);
            for (String whereClause: whereSQLList) {
                result.addWhereClause(whereClause);
            }
            return result;
        }
    }

    private static class PostgreSQLFinalQuery implements FinalSQLQuery {
        private final String sql;

        private PostgreSQLFinalQuery(String sql) {
            this.sql = sql;
        }

        @Override
        public PreparedStatement prepareFor(Connection connection) throws SQLException {
            return connection.prepareStatement(sql);
        }
    }
}
