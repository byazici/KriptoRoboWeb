<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.by.robo.helper.UserHelper"%>
<%@page import="com.by.robo.model.User"%>
<%@page import="java.math.RoundingMode"%>
<%@page import="com.by.robo.utils.DateUtils"%>
<%@page import="com.by.robo.utils.NumberUtils"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="com.by.robo.enums.PairSymbol"%>
<%@page import="com.by.robo.server.AlgoServer"%>
<%
			
		PairSymbol pairSymbol = null;
		String pairIcon = null;
		String symbol = request.getParameter("symbol");
		if (symbol != null) pairSymbol = PairSymbol.setValue(symbol);
		if (pairSymbol == null) pairSymbol = PairSymbol.BTCTRY;
		
		if (pairSymbol.equals(PairSymbol.BTCTRY)){
			pairIcon = "<img src=\"images/btc.png\">";
		} else {
			pairIcon = "<img src=\"images/eth.png\">";
		}

		BigDecimal p = AlgoServer.getLastTick(pairSymbol).getLast();

		String price = "";
		if (p != null) price = NumberUtils.formatDec2(p);

		String icon = null;

		BigDecimal l = AlgoServer.getLastTick24h(pairSymbol).getLast();
		if (l != null && p != null){
			BigDecimal percentage = p.subtract(l).multiply(BigDecimal.valueOf(100)).divide(p, 2, RoundingMode.HALF_UP);
			
			if (percentage.compareTo(BigDecimal.ZERO) < 0){
				price += "(%" + NumberUtils.formatDec2(percentage) + ")";
				icon = "<img src=\"images/down.png\">";
			} else {
				price += "(%" + NumberUtils.formatDec2(percentage) + ")";
				icon = "<img src=\"images/up.png\">";
			}
			
		} 
		
		if (price != null && l != null &&  price.length() > 0 ){
			out.write("<small>" + pairIcon + " " + icon + " " + price + "</small>");
		} else {
			out.write("<small>" + pairIcon + " x ???.?? (%?.??)</small>");
		}

		
%>