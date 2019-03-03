<%@page import="java.math.BigDecimal"%>
<%@page import="com.by.robo.web.UserWao"%>
<%@page import="com.by.robo.model.UserStats"%>
<%@page import="com.by.robo.helper.AlgoHelper"%>
<%@page import="com.by.robo.utils.NumberUtils"%>
<%@page import="com.by.robo.model.Algo"%>
<%@page import="java.util.List"%>
<%@page import="com.by.robo.dao.AlgoDao"%>
<%@page import="com.by.robo.web.AlgoWao"%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="./header.jsp" %>

	<% UserStats stats = UserWao.getUserStats(userObj);  %>

	<div class="container">

		<p />
		<div class="row">
		
			<div class="col-sm-6">
				<div class="card">
				  <h5 class="card-header">Merhaba <%= userObj.getFirstName() %> !</h5>
				  <div class="card-body">
				 	 Kriptorobo ile bol kazançlı günler dileriz.
					<br/><small>(Kriptorobo <%= AlgoServer.appVersion %>)</small>
				  </div>
			    </div>
		    </div>	 
		
 
			<div class="col-sm-6">
				<div class="card">
				  <h5 class="card-header">Algo Limitleriniz</h5>
					<div class="card-body p-0">
						<div class="card">
							<div class="card-body">
								 <div class="row">
						    			<div class="col">
								       <h5>Kalan Algo Sayısı</h5>
								       <%= stats.getMaxAlgo().subtract(stats.getOpenAlgo()) %> adet
								    </div>
								    <div class="col">
								      <h5>Kalan Algo Tutarı</h5>
								      <%= NumberUtils.formatDec2(stats.getMaxAmt().subtract(stats.getOpenAmt())) %> TL
								    </div>
								 </div>
							</div>
						</div>				  
					</div>
			    </div>
		    </div>	 
		</div>


		<p />
		<div class="card">
			<h5 class="card-header">Özet</h5>
		  	<div class="card-body p-0">
				<div class="card">
					<div class="card-body">
						 <div class="row">
				    			<div class="col">
						       <h5>Açık Algo</h5>
						       <%= stats.getOpenAlgo() %>
						    </div>
						    <div class="col">
						      <h5>Gerçekleşen Algo</h5>
						      <%= stats.getRlzAlgo() %>
						    </div>
						    <div class="col">
						      <h5>Toplam Algo Kar</h5>
						      <%= NumberUtils.formatDec2(stats.getProfit()) %> TL
						    </div>
						    <div class="col">
						      <h5>Yatırım Verimi</h5>
						      % <%= NumberUtils.formatDec2(stats.getRate().multiply(new BigDecimal(100))) %>
						    </div>
						 </div>
					</div>
				</div>				  
			</div>
		</div>

		<p />
		<div class="row">
		
			<div class="col-sm-6">
				<div class="card">
				  <h5 class="card-header">Son Girilen Algolar</h5>
				  <div class="card-body p-0">
				  <%
				  	List<Algo> algoList = AlgoDao.getLatestAlgo(userObj, 10);
				  %>
				  	<p />
				  	<table class="table table-sm table-bordered">
						<thead class="thead-light">
			   				<tr>
							     <th scope="col">#</th>
							     <th scope="col">Algo</th>
							     <th scope="col">Tarih</th>
							     <th scope="col">Adet</th>
							     <th scope="col">Durum</th>
							</tr>
						</thead>
						<tbody>	
						<% for(Algo algo : algoList) { 
						  	String badge = AlgoWao.getAlgoBadge(algo); 						
						%>
							<tr>
						      <td><%= algo.getId() %></td>
						      <td><%= algo.getName() %></td>
						      <td><%= DateUtils.formatAsDateTime(algo.getCreateDate()) %></td>
						      <td class="text-right"><%= NumberUtils.formatDec6(algo.getBuyAmt()) + " " + algo.getPairSynbol() %></td>
						      <td><span class="badge badge-pill badge-<%=badge%>"><%= algo.getStatus() %></span></td>
							</tr>	
						<% } %>					
						</tbody>
					</table>
				</div>
			</div>
		</div>	 
		
 
		<div class="col-sm-6">
			<div class="card">
				<h5 class="card-header">Son Gerçekleşen Algolar</h5>
			  	<div class="card-body p-0">
			  	<%
			  		algoList = AlgoDao.getLatestDoneAlgo(userObj, 10);
			  	%>
				  	<p />
				  	<table class="table table-sm table-bordered">
						<thead class="thead-light">
			   				<tr>
							     <th scope="col">#</th>
							     <th scope="col">Algo</th>
							     <th scope="col">Tarih</th>
							     <th scope="col">Adet</th>
							     <th scope="col">Kar</th>
							</tr>
						</thead>
						<tbody>	
						<% for(Algo algo : algoList) { 
						  	String badge = AlgoWao.getAlgoBadge(algo); 						
						%>
							<tr>
						      <td><%= algo.getId() %></td>
						      <td><%= algo.getName() %></td>
						      <td><%= DateUtils.formatAsDateTime(algo.getCreateDate()) %></td>
						      <td class="text-right"><%= NumberUtils.formatDec6(algo.getBuyAmt()) + " " + algo.getPairSynbol() %></td>
						      <td class="text-right"><%= AlgoWao.getProfit(algo) %></td>
							</tr>	
						<% } %>					
						</tbody>
					</table>
				</div>
			</div>
		</div>
		
	</div>	
</div>			
			

<%@include file="./footer.jsp" %>