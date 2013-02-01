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

public class OracleSQLEngine implements SQLEngine {
    @Override
    public BasicSQLQuery selectFrom(String selectSQL, String fromSQL) {
        return new MySQLBasicQuery(selectSQL, fromSQL);
    }

    private static class MySQLBasicQuery implements BasicSQLQuery {
        private final String selectSQL;
        private final String fromSQL;
        private final List<String> whereSQLList = new ArrayList<String>();

        private MySQLBasicQuery(String selectSQL, String fromSQL) {
            this.selectSQL = selectSQL;
            this.fromSQL = fromSQL;
        }

        @Override
        public void addWhereClause(String whereSQL) {
            whereSQLList.add(whereSQL);
        }

        @Override
        public FinalSQLQuery skipAndLimit(String orderSQL, int offset, int limit) {
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT * FROM (");
            fillStringBuiler(sb);
            sb.append(" ORDER BY ");
            sb.append(orderSQL);
            sb.append(") WHERE ");
            sb.append(offset);
            sb.append(" < ROWNUM AND ROWNUM <= ");
            sb.append(limit);
            return new MySQLFinalQuery(sb.toString());
        }

        @Override
        public FinalSQLQuery anySingleRecord() {
            MySQLBasicQuery query = new MySQLBasicQuery(selectSQL, fromSQL);
            query.whereSQLList.addAll(this.whereSQLList);
            query.addWhereClause("ROWNUM <= 1");
            StringBuilder sb = new StringBuilder();
            query.fillStringBuiler(sb);
            return new MySQLFinalQuery(sb.toString());
        }

        private void fillStringBuiler(StringBuilder sb) {
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
        }

        @Override
        public BasicSQLQuery clone() {
            MySQLBasicQuery result = new MySQLBasicQuery(selectSQL, fromSQL);
            result.whereSQLList.addAll(this.whereSQLList);
            return result;
        }
    }

    private static class MySQLFinalQuery implements FinalSQLQuery {
        private final String sql;

        private MySQLFinalQuery(String sql) {
            this.sql = sql;
        }

        @Override
        public PreparedStatement prepareFor(Connection connection) throws SQLException {
            return connection.prepareStatement(sql);
        }
    }
}
