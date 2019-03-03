<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.by.robo.utils.NumberUtils"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="com.by.robo.model.Account"%>
<%@page import="com.by.robo.helper.BtcTurkHelper"%>
<%@include file="./header.jsp" %>

	<div class="container">
		<p />
		<h2>Cüzdanım</h2>
		
		<% if (userObj.getPublicKey() != null) { 

			Account a = new BtcTurkHelper().AccountBalance(userObj.getId());
			
			BigDecimal btcPrice = AlgoServer.getLastTick(PairSymbol.BTCTRY).getLast();
			BigDecimal ethPrice = AlgoServer.getLastTick(PairSymbol.ETHTRY).getLast();
			
			BigDecimal btcAmount = a.getBTCBalance().multiply(btcPrice);
			BigDecimal ethAmount = a.getETHBalance().multiply(ethPrice);
		%>	
		
		<div class="col-md-6">
			<table class="table table-bordered">
			  <thead>
			    <tr>
			      <th scope="col">Sembol</th>
			      <th scope="col" class="text-right">Bakiye</th>
			      <th scope="col" class="text-right">Blokaj</th>
			      <th scope="col" class="text-right">Net</th>
			      <th scope="col" class="text-right">Tutar</th>
			    </tr>
			  </thead>
			  <tbody>
			    <tr>
			      <th scope="row">TRY</th>
			      <td class="text-right"><%= a.getTRYBalance() %></td>
			      <td class="text-right"><%= a.getTRYBlockage() %></td>
			      <td class="text-right"><%= a.getTRYNetBalance() %></td>
			      <td class="text-right"><%= a.getTRYBalance() %></td>
			    </tr>
			    <tr>
			      <th scope="row">BTC</th>
			      <td class="text-right"><%= a.getBTCBalance() %></td>
			      <td class="text-right"><%= a.getBTCBlockage() %></td>
			      <td class="text-right"><%= a.getBTCNetBalance() %></td>
			      <td class="text-right"><%= NumberUtils.formatDec2(btcAmount) %></td>
			    </tr>
			    <tr>
			      <th scope="row">ETH</th>
			      <td class="text-right"><%= a.getETHBalance() %></td>
			      <td class="text-right"><%= a.getETHBlockage() %></td>
			      <td class="text-right"><%= a.getETHNetBalance() %></td>
			      <td class="text-right"><%= NumberUtils.formatDec2(ethAmount) %></td>
			    </tr>
			    <tr>
			      <td class="text-right">&nbsp;</td>
			      <td class="text-right">&nbsp;</td>
			      <td class="text-right">&nbsp;</td>
			      <td class="text-right">&nbsp;</td>
			      <td class="text-right font-weight-bold bg-success text-white"><%= NumberUtils.formatDec2(a.getTRYBalance().add(btcAmount).add(ethAmount)) %></td>
			    </tr>
			  </tbody>
			</table>	
		</div>
		
		<% } %>
	</div>

<%@include file="./footer.jsp" %>