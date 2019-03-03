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
	OrderStatus os = OrderStatus.OPEN;
	if (request.getParameter("rlz") != null){
		os = OrderStatus.RLZ;
	} else	if (request.getParameter("cancel") != null){
		os = OrderStatus.CANCEL;
	} else	if (request.getParameter("all") != null){
		os = OrderStatus.ALL;
	}
	List<Order> orderList = OrderWao.getOrderList(userObj, os);

%>
	<div class="container">
		<p />
		<h2>Emirler</h2>
		
		<div class="btn-group" role="group">
		<form action="orders.jsp" method="post">
			  <button type="submit" name="all" class="btn btn-secondary<%= (os == OrderStatus.ALL) ? " btn-lg" : "" %>">Hepsi</button>
			  <button type="submit" name="open" class="btn btn-primary<%= (os == OrderStatus.OPEN) ? " btn-lg" : "" %>">Açık</button>
			  <button type="submit" name="rlz" class="btn btn-success<%= (os == OrderStatus.RLZ) ? " btn-lg" : "" %>">Gerçekleşen</button>
			  <button type="submit" name="cancel" class="btn btn-danger<%= (os == OrderStatus.CANCEL) ? " btn-lg" : "" %>">İptal</button>
		</form>
		</div>
		
		<p />
		<table class="table table-sm table-bordered sortable">
		  <caption>Emir Listesi</caption>
		
		 <thead class="thead-light">
		   <tr>
		     <th scope="col">#</th>
		     <th scope="col">Sembol</th>
		     <th scope="col">A/S</th>
		     <th scope="col">Oluşturulma</th>
		     <th scope="col">Gerçekleşme</th>
		     <th scope="col">İptal</th>
		     <th scope="col">Fiyat</th>
		     <th scope="col">Adet</th>
		     <th scope="col">Tutar</th>
		     <th scope="col">Gerç. Tutar</th>
		     <th scope="col">Ref Kodu</th>
		     <th scope="col">Emir Tipi</th>
		     <th scope="col">Durum</th>
		     <th scope="col">Algo No</th>
		     <th scope="col">Açıklama</th>
		   </tr>
		 </thead>
		 <tbody>	
		<% for(Order order : orderList) {  
			String badge = "primary";
			if (order.getStatus() == OrderStatus.RLZ){
				badge = "success";
			} else if (order.getStatus() == OrderStatus.CANCEL){
				badge = "danger";
			}
		
		
		%>
		    <tr>
		      <td><%= order.getId() %></td>
		      <td><%= order.getPairSymbol() %></td>
		      <td><%= order.getBuySell() %></td>
		      <td><%= DateUtils.formatAsDateTime(order.getCreateDate()) %></td>
		      <td><%= DateUtils.formatAsDateTime(order.getRlzDate()) %></td>
		      <td><%= DateUtils.formatAsDateTime(order.getCancelDate()) %></td>
		      <td><%= NumberUtils.formatDec2(order.getPrice()) %></td>
		      <td><%= NumberUtils.formatDec4(order.getAmount()) %></td>
		      <td><%= NumberUtils.formatDec4(order.getTotal()) %></td>
		      <td><%= NumberUtils.formatDec4(order.getRlzTotal()) %></td>
		      <td><%= order.getTradeRef() %></td>
		      <td><%= order.getMarketOrder() == TrueFalse.TRUE ? "Market" : "Limit" %></td>
		      <td><span class="badge badge-pill badge-<%=badge%>"><%= order.getStatus() %></span></td>
		      <td><%= order.getAlgoId() %></td>
		      <td><%= order.getDescr() %></td>
		    </tr>			
		<% } %>
		  </tbody>
		</table>		
	</div>

<%@include file="./footer.jsp" %>