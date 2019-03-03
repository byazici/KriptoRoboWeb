<%@page import="com.by.robo.web.BackTestWao"%>
<%@page import="com.by.robo.model.BackTestResult"%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.by.robo.web.OrderWao"%>
<%@page import="com.by.robo.utils.DateUtils"%>
<%@page import="com.by.robo.enums.TrueFalse"%>
<%@page import="com.by.robo.utils.NumberUtils"%>
<%@page import="com.by.robo.model.Order"%>
<%@page import="java.util.List"%>
<%@page import="com.by.robo.enums.OrderStatus"%>
<%@page import="com.by.robo.dao.OrderDao"%>
<%@include file="./header.jsp" %>
<%
	BackTestResult result = null;
	if (request.getParameter("calc") != null){
		result = BackTestWao.getBackTest(userObj);
	}
%>
	<div class="container">
		<p />
		<h2>Geri Test</h2>
		
		<p /><b>Geri Test (backtest) Nedir? </b> <br />"Geçmişteki fiyatlar üzerinden belli bir algo verseydim kazancım ne olurdu?" sorusuna yanıt vermek için kullanılan bir analiz türüdür.

		<p />Yeni versiyon ile sizlerle...

		<% if (result != null){ %>

			<table class="table table-sm table-bordered sortable">
				  <caption>Geri Test Sonucu</caption>
				
				 <thead class="thead-light">
				   <tr>
				     <th scope="col">Alış Emirleri</th>
				     <th scope="col">Satış Emirleri</th>
				     <th scope="col">Kar Zarar</th>
				     <th scope="col">Komisyon</th>
				     <th scope="col">Net Kar Zarar</th>
				   </tr>
				 </thead>
				 <tbody>	
				    <tr>
				      <td><%= result.getBuyOrderCount() %></td>
				      <td><%= result.getSellOrderCount() %></td>
				      <td><%= NumberUtils.formatDec2(result.getSumProfit()) %></td>
				      <td><%= NumberUtils.formatDec2(result.getSumCommAmt()) %></td>
				      <td><%= NumberUtils.formatDec2(result.getSumProfitNet()) %></td>
				    </tr>
				  </tbody>
			</table>				

		<% }  %>

	</div>

<%@include file="./footer.jsp" %>