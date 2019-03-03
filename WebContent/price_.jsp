<%@page import="java.util.LinkedHashMap"%>
<%@page import="org.json.JSONArray"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.by.robo.dao.AlgoDao"%>
<%@page import="org.json.JSONObject"%>
<%@page import="com.by.robo.web.AlgoWao"%>
<%@page import="com.by.robo.model.Result"%>
<%@page import="com.by.robo.enums.TrueFalse"%>
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
	// TODO unauth access 'i engelle.
	String output;

	String symbol = request.getParameter("symbol");
	PairSymbol pair = PairSymbol.setValue(symbol);
	if (pair == null){
		output = "";
	} else{
		JSONArray json = new JSONArray();
		LinkedHashMap<String, BigDecimal> map = AlgoDao.getPriceDropdown(pair);
		
		int i = 0;
		for (HashMap.Entry<String, BigDecimal> entry : map.entrySet()) {
		    String key = entry.getKey();
		    BigDecimal value = entry.getValue();
		    
		    JSONObject o = new JSONObject();
		    o.put("priceVal", value);
		    o.put("priceText", key);
		    json.put(i, o);
		    i++;
		}
		output = json.toString(2);
	}
	
	out.write(output);

%>