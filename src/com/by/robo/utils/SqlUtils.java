package com.by.robo.utils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class SqlUtils {
    public static String getHtmlTable(ResultSet results)
            throws SQLException {
        
        StringBuilder htmlTableSB = new StringBuilder();
        //Interface ResultSetMetaData
        ResultSetMetaData rsmd = results.getMetaData();
        int columnCount = rsmd.getColumnCount();

        htmlTableSB.append("<table class=\"table table-striped\">");

        // add header row
        htmlTableSB.append("<tr>");
        for (int i = 1; i <= columnCount; i++) {
            htmlTableSB.append("<th>");
            htmlTableSB.append(rsmd.getColumnName(i)); 
            htmlTableSB.append("</th>");
        }
        htmlTableSB.append("</tr>");

        // check if there is a row after & move cursor
        while (results.next()) {
            htmlTableSB.append("<tr>");
            
            for (int i = 1; i <= columnCount; i++) {
                htmlTableSB.append("<td>");
                htmlTableSB.append(results.getString(i));
                htmlTableSB.append("</td>");
            }
            
            htmlTableSB.append("</tr>");
        }

        htmlTableSB.append("</table>");
        return htmlTableSB.toString();
    }
}
