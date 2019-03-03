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

	String respCode = "0";
	String respMsg = "?";
  
	try{

		// security check
		User userObj = null;
		String token = (String) request.getSession().getAttribute(UserHelper.USER_TOKEN);

		if (token != null && !request.getSession().isNew()){
			userObj = new UserHelper().getUserByToken(token);
		}
		
		if (userObj == null || userObj.getRoles() == 0){
			out.write("?");
			return;
		}

		if (request.getParameter("add") != null){
			String duration = request.getParameter("duration"); // hour
			String priceDuration = request.getParameter("priceDuration"); // hour
			String name = request.getParameter("name");
			String pair = request.getParameter("pairSymbol");
			String maxAmount = request.getParameter("maxAmount");
			String buyRate = request.getParameter("buyRate");
			String sellRate = request.getParameter("sellRate");
			String trigRate = request.getParameter("trigRate");
			//String repeated = request.getParameter("repeated");
			String basePrice = request.getParameter("basePrice");

			
			if (
					(duration == null || duration.length() == 0) ||
					(priceDuration == null || priceDuration.length() == 0) ||
					(name == null || name.length() == 0) ||
					(pair == null || pair.length() == 0) ||
					(maxAmount == null || maxAmount.length() == 0) ||
					(buyRate == null || buyRate.length() == 0) ||
					(sellRate == null || sellRate.length() == 0) ||
					(trigRate == null || trigRate.length() == 0) ||
					// (repeated == null || repeated.length() == 0)	 ||
					(basePrice == null || basePrice.length() == 0)				
					
				){
				respCode  = "0";
				respMsg  = "Lütfen gerekli alanları doldurunuz!";
				
			} else if (maxAmount.contains(",") || 
					buyRate.contains(",") ||
					sellRate.contains(",") ||
					trigRate.contains(",")) {
				
				respCode  = "0";
				respMsg  = "Lütfen sayıları ##.## formatında giriniz!";
			} else {
				BigDecimal ratio = new BigDecimal(0.01);
				BigDecimal buyRateInput = BigDecimal.ONE.subtract(new BigDecimal(buyRate).multiply(ratio)); // ex: 0.04 -> 0.96
				BigDecimal sellRateInput = BigDecimal.ONE.add(new BigDecimal(sellRate).multiply(ratio)); // ex: 0.02 -> 1.02
				BigDecimal trigRateInput = BigDecimal.ONE.add(new BigDecimal(trigRate).multiply(ratio)); // ex: 0.01 -> 1.01
			
				Result result = AlgoWao.createAlgo(
						userObj,
						name,
						PairSymbol.setValue(pair), 
						new BigDecimal(maxAmount), 
						buyRateInput, 
						sellRateInput, 
						trigRateInput, 
						TrueFalse.FALSE, 	// TrueFalse.setValue(Integer.valueOf(repeated).intValue()), 
						Integer.valueOf(duration).intValue(), 
						0,
						Integer.valueOf(priceDuration).intValue(),
						new BigDecimal(basePrice));

				if (result != null && result.isSuccess()){
					respCode  = "1";
					respMsg  = "Algo kaydedildi.";
				}else{
					respCode  = "0";
					respMsg  = result.getErrorCode() + "-" + result.getErrorMsg();
				}	
			}	
		}

		org.json.JSONObject obj = new org.json.JSONObject();
		obj.put("respCode", respCode);
		obj.put("respMsg", respMsg);
		out.write (obj.toString()); 

	} catch (Exception e) {
		out.write(e.getMessage());
		e.printStackTrace();
	}
	
		
%>