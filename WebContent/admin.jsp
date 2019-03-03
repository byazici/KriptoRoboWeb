<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.by.robo.enums.UserRole"%>
<%@page import="org.apache.catalina.tribes.util.Logs"%>
<%@page import="com.by.robo.utils.SysUtils"%>
<%@page import="com.by.robo.server.AlgoServerImp"%>
<%
	String message = "";
	boolean running = false;
	StringBuffer serverlogs = null;

	if (request.getParameter("start") != null){
		AlgoServerImp.startServer();	
		message = "Server started";
		
	} else if (request.getParameter("stop") != null){
		AlgoServerImp.stopServer();	
		message = "Server stopped";
		
	} else if (request.getParameter("logs") != null){
		message = "logs";
		serverlogs = SysUtils.readLogFile();

	} else if (request.getParameter("status") != null){
		message = "status";
	}
	
	if (AlgoServerImp.isRunning()){
		running = true;
	} else {
		running = false;		
	};

%><%@include file="./header.jsp" %>

<%  //
	// admin auth control
	if (!new UserHelper().userHasRole(userObj, UserRole.ADMIN)){ response.sendError(401, "unauthorized access"); return; }
	//
	// %>

	<div class="container">
		<p />
		<h2>Admin</h2>

	    <% if (message.length() > 0) { %>
	   	 <div class="alert alert-warning alert-dismissible fade show" role="alert">
	   	 	<%= message %>
	   	 	<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	   	 </div>
	    <% } %>

		<div class="row">
			<div class="col-sm-4">
				<div class="card">
				  <h4 class="card-header">Robo Server</h4>
				  <div class="card-body">
  				    <p class="card-text">Start/stop robo server</p>
  				    <form method="post" action="admin.jsp">
				    		<input type="submit" class="btn btn-success" <%= running ? " disabled" :"" %> name="start" value="Start" />
				    		<input type="submit" class="btn btn-danger" <%= !running ? " disabled" :"" %> name="stop" value="Stop" />
				    		<span class="badge"><%= running ? "running" : "not running" %></span>
					</form>
				  </div>
				</div>
			</div>	 
			   
			<div class="col-sm-4">
				<div class="card">
				  <h4 class="card-header">Robo Admin</h4>
				  <div class="card-body">
				    <p class="card-text">Admin utilities</p>
				    <form method="post" action="admin.jsp">
				    		<input type="submit" class="btn btn-primary" disabled name="status" value="Status" />
				    		<input type="submit" class="btn btn-primary" disabled name="logs" value="Logs" />
					</form>
				  </div>
				</div>
			</div>	    
			
			<div class="col-sm-4">
				<div class="card">
				  <h4 class="card-header">Stats</h4>
				  <div class="card-body">
				    <p class="card-text">Admin numbers</p>
						<button type="button" class="btn btn-success">
						  Success<span class="badge badge-light"><%= AlgoServer.getSuccessCount()  %></span>
						</button>
				    
						<button type="button" class="btn btn-warning">
						  Fail <span class="badge badge-light"><%= AlgoServer.getFailCount()  %></span>
						</button>

				  </div>
				</div>
			</div>			
		</div>
		
		
		<p />
		<div class="row">
			<div class="col-sm-4">
				<div class="card">
				  <h4 class="card-header">Algo Status Def</h4>
				  <div class="card-body">
				    <p class="card-text">List of status values</p>
						<p class="badge badge-pill badge-dark">01 - NEW</p>
						<p class="badge badge-pill badge-primary">02 - BUY_OPEN</p>
						<p class="badge badge-pill badge-warning">03 - TRIG_WAIT</p>
						<p class="badge badge-pill badge-info">04 - SELL_WAIT</p>
						<p class="badge badge-pill badge-info">05 - SELL_OPEN</p>
						<p class="badge badge-pill badge-success">10 - DONE</p>
						<p class="badge badge-pill badge-danger">20 - ERROR</p>
						<p class="badge badge-pill badge-muted">30 - CANCEL</p>
						<p class="badge badge-pill badge-muted">99 - ALL</p>
				  </div>
				</div>
			</div>	
			
			<div class="col-sm-4">
				<div class="card">
				  <h4 class="card-header">Order Status Def</h4>
				  <div class="card-body">
				    <p class="card-text">List of status values</p>
						<span class="badge badge-pill badge-primary">01 - NEW</span>
						<span class="badge badge-pill badge-success">02 - RLZ</span> 
						<span class="badge badge-pill badge-danger">03 - CANCEL</span>
						<span class="badge badge-pill badge-muted">99 - ALL</span>
				  </div>
				</div>
			</div>
			
			<div class="col-sm-4">
				<div class="card">
				  <h4 class="card-header">Up Time</h4>
				  <div class="card-body">
				    <p class="card-text">Server Startup Time </p>
						<button type="button" class="btn btn-dark">
						  <%= DateUtils.formatAsDateTime(AlgoServer.getStartTime()) %>
						</button>
						<button type="button" class="btn badge-muted">
							<%= DateUtils.getDiff(AlgoServer.getStartTime()) %>
						</button>
				 </div>
				</div>
			</div>

		</div>


		<% if (serverlogs != null) { %>
			<p />	
			<div class="row">
				<div class="col-sm-12">
					<div class="card">
					  <h4 class="card-header">Server Logs</h4>
					  <div class="card-body">
	  				    <p class="card-text">
	  				    		<pre style="font-size:12px; height: 400px; max-height: 400px; overflow-y: scroll;"><%= serverlogs.toString() %></pre>
						</p>					
					  </div>
					</div>
				</div>			
			</div>
		<% } %>
		
		
	</div>


<%@include file="./footer.jsp" %>