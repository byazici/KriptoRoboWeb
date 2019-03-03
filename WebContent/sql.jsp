<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.SQLException"%>
<%@page import="com.by.robo.utils.SqlUtils"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.by.robo.utils.ConnectionFactory"%>
<%@page import="com.by.robo.model.Account"%>
<%@page import="com.by.robo.helper.BtcTurkHelper"%>
<%@include file="./header.jsp" %>
<%  //
	// admin auth control
	if (!new UserHelper().userHasRole(userObj, UserRole.ADMIN)){ response.sendError(401, "unauthorized access"); return; }
	//
	// %>

	<div class="container">
		<p />
		<h2>SQL</h2>
<%

	String sqlResult = "";
	String sqlStatement = request.getParameter("sqlStatement");
	if (sqlStatement == null) sqlStatement = "select * from algo where status not in (10,30)";
	
	if (request.getParameter("execute") != null){
		Connection connection = null;
		PreparedStatement statement = null;
		
		try {
		    connection = ConnectionFactory.getConnection();
		    statement = connection.prepareStatement(sqlStatement);
		    sqlStatement = sqlStatement.trim();
		    
		    if (sqlStatement.length() >= 6) {
		        String sqlType = sqlStatement.substring(0, 6);

		        if (sqlType.equalsIgnoreCase("select")) {
		            // create the HTML for the result set
		            ResultSet resultSet= statement.executeQuery();
		            sqlResult = SqlUtils.getHtmlTable(resultSet);
		            resultSet.close();
		            
		        } else {
		            int i = statement.executeUpdate(sqlStatement);
		            if (i == 0) { // a DDL statement
		                sqlResult = 
		                        "<p>The statement executed successfully.</p>";
		         
		            } else { // an INSERT, UPDATE, or DELETE statement
		                sqlResult = 
		                        "<p>The statement executed successfully.<br>"
		                        + i + " row(s) affected.</p>";
		            }
		        }
		    }
		    
		} catch (ClassNotFoundException e) {
		    sqlResult = "<p>Error loading the database driver: <br>"
		            + e.getMessage() + "</p>";
		    
		} catch (SQLException e) {
		    sqlResult = "<p>Error executing the SQL statement: <br>"
		            + e.getMessage() + "</p>";
		} finally{
			ConnectionFactory.closeConnPrep(statement, connection);
		}
		
	}

%>	
		<h4>SQL statement:</h4>
		<form action="sql.jsp" method="post">
	    		<textarea name="sqlStatement" cols="80" rows="4"><%= sqlStatement %></textarea>
	    		<p />
	    		<input class="btn btn-primary "type="submit" name="execute" value="Execute">
		</form>
	
		<h4>SQL result:</h4>
		<div class="small">
			<%= sqlResult %>
		</div>
		

	</div>

<%@include file="./footer.jsp" %>