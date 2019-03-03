<%@page import="com.by.robo.dao.FeedbackDao"%>
<%@page import="com.by.robo.model.Feedback"%>
<%@page import="com.by.robo.utils.WebUtils"%>
<%@page import="com.by.robo.utils.MailUtils"%>
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
		String name = request.getParameter("name");
		String mail = request.getParameter("mail");
		String subject = request.getParameter("subject");
		String message = request.getParameter("message");

		if (WebUtils.hasAnyEmpty(name,mail,subject,message)){
			respCode  = "0";
			respMsg  = "Lütfen gerekli alanları doldurunuz!";
			
		} else if ( ! WebUtils.isValidMail(mail) ) {
	
			respCode  = "0";
			respMsg  = "Lütfen e-posta adresini kontrol ediniz.!";
		} else {
			Feedback f = new Feedback();
			f.setName(name);
			f.setMail(mail);
			f.setSubject(subject);
			f.setMessage(message);
			f.setInsertDate(DateUtils.getCurrentDate());
			f.setStatus(1);
			
			boolean result = FeedbackDao.insertFeedback(f);
			if (result){
				respCode  = "1";
				respMsg  = "Mesajınız bize ulaştı. Ekibimiz size dönüş yapacaktır.";
			}else{
				respCode  = "0";
				respMsg  = "Mesajınızı kaydedemedik!";
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