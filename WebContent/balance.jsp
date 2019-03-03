<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.by.robo.dao.BalanceDao"%>
<%@page import="com.by.robo.model.BalanceDaily"%>
<%@page import="com.by.robo.utils.DateUtils"%>
<%@page import="com.by.robo.enums.TrueFalse"%>
<%@page import="com.by.robo.utils.NumberUtils"%>
<%@page import="com.by.robo.model.Order"%>
<%@page import="java.util.List"%>
<%@page import="com.by.robo.enums.OrderStatus"%>
<%@page import="com.by.robo.dao.OrderDao"%>
<%@include file="./header.jsp" %>
<%
	List<BalanceDaily> balance = BalanceDao.getBalanceList(userObj.getId());
%>
	<div class="container">
		<p />
		<h2>Hesap Özeti</h2>
		<p class="card-text">Her gece 00:00'da hesaplanır.</p>
		
		<p />
		<table class="table table-sm table-bordered sortable">
		<caption>Son bir ayı kapsamaktadır.</caption>
		
		 <thead class="thead-light">
		   <tr>
		     <th scope="col">Tarih</th>
		     <th scope="col">TRY Bakiye</th>
		     <th scope="col">BTC Adet</th>
		     <th scope="col">BTC Bakiye</th>
		     <th scope="col">ETH Adet</th>
		     <th scope="col">ETH Bakiye</th>
		     <th scope="col">Overall (TRY)</th>
		     <th scope="col">Overall (BTC)</th>
		   </tr>
		 </thead>
		 <tbody>	
		<% if (balance!=null){
			for(BalanceDaily b : balance) {  
		%>
		    <tr>
		      <td><%= DateUtils.formatAsDate(b.getTickDate()) %></td>
		      <td><%= NumberUtils.formatDec2(b.getTryBalance()) %></td>
		      <td><%= NumberUtils.formatDec6(b.getBtcBalance()) %></td>
		      <td><%= NumberUtils.formatDec2(b.getBtcAmount()) %></td>
		      <td><%= NumberUtils.formatDec6(b.getEthBalance()) %></td>
		      <td><%= NumberUtils.formatDec2(b.getEthAmount()) %></td>
		      <td><%= NumberUtils.formatDec2(b.getTryAmount().add(b.getBtcAmount()).add(b.getEthAmount())) %></td>
		      <td><%= NumberUtils.formatDec6(b.getOverallBtc()) %></td>
		    </tr>			
		<% }
		}
		%>
		  </tbody>
		</table>		
	</div>

<%@include file="./footer.jsp" %>