<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.by.robo.helper.UserHelper"%>
<%@page import="com.by.robo.model.User"%>
<%
	User userObj = null;
	String token = (String) request.getSession().getAttribute(UserHelper.USER_TOKEN);
	
	if (token != null && !request.getSession().isNew()){
		userObj = new UserHelper().getUserByToken(token);
		new UserHelper().logoutUser(userObj) ;
	} else {
		new UserHelper().logoutUser(null) ;
	}

	request.getSession().invalidate();
	response.sendRedirect("./index.jsp");
	return;
%>