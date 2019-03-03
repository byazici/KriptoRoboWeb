<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.by.robo.enums.UserRole"%>
<%@page import="com.by.robo.helper.UserHelper"%>
<%@page import="com.by.robo.enums.PairSymbol"%>
<%@page import="com.by.robo.model.Tick"%>
<%@page import="com.by.robo.server.AlgoServer"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.TimeZone"%>
<%@page import="com.by.robo.model.User"%>

<% 
	request.setCharacterEncoding("UTF-8");


	User userObj = null;
	String token = (String) request.getSession().getAttribute(UserHelper.USER_TOKEN);

	if (token != null && !request.getSession().isNew()){
		userObj = new UserHelper().getUserByToken(token);
	}
	
	if (userObj == null || userObj.getRoles() == 0){
		response.sendRedirect("login.jsp");
		return;
	}

// 	Object o =  request.getSession().getAttribute("user");
//	if (o == null || request.getSession().isNew() || request.getSession().getAttribute("loggedin") == null){
//		response.sendRedirect("login.jsp");
//		return;
//	} 

	String greetings = userObj.getUserMail();
	
%><!doctype html>
<html lang="tr">
  <head>
    <title>KriptoRobo</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/css/bootstrap.min.css" integrity="sha384-PsH8R72JQ3SOdhVi3uxftmaW6Vc51MKb0q5P2rRUpPvrszuE4W1povHYgTpBfshb" crossorigin="anonymous">
	<link rel="stylesheet"  href="./css/master.css">
    <link rel="stylesheet" type="text/css" href="./css/bootstrap-sortable.css">
	<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.7.1/Chart.bundle.min.js" integrity="sha256-N4u5BjTLNwmGul6RgLoESPNqDFVUibVuOYhP4gJgrew=" crossorigin="anonymous"></script>
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
  </head>
  <body>
	  
	<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
		<a class="navbar-brand h1 mb-0" href="./index.jsp">KriptoRobo.com <sup class="text-muted">beta</sup></a>
	  
	  <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
	    <span class="navbar-toggler-icon"></span>
	  </button>
	
	  <div class="collapse navbar-collapse" id="navbarSupportedContent">
	    <ul class="navbar-nav mr-auto">
	      <li class="nav-item">
	        <a class="nav-link" href="./algo.jsp">Algolar</a>
	      </li>
	      <li class="nav-item">
	        <a class="nav-link" href="./orders.jsp">Emirler</a>
	      </li>
	      <li class="nav-item">
	        <a class="nav-link" href="./btest.jsp">Geri Test</a>
	      </li>

	      <li class="nav-item dropdown">
	        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
	          Grafik
	        </a>
	        <div class="dropdown-menu" aria-labelledby="navbarDropdown">
				<a class="dropdown-item" href="./ticks.jsp">Fiyat</a>
				<a class="dropdown-item" href="./live.jsp">Canlı</a>
	        </div>
	      </li>
	      
	      <li class="nav-item dropdown">
	        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
	          Yardım
	        </a>
	        <div class="dropdown-menu" aria-labelledby="navbarDropdown">
				<a class="dropdown-item" href="howitworks.jsp">Nasıl Çalışır?</a>
				<a class="dropdown-item" href="faq.jsp">Sıkça Sorulan Sorular</a>
	        </div>
	      </li>	      
	      
	      
	      <% if (new UserHelper().userHasRole(userObj, UserRole.ADMIN)) { %>
		      <li class="nav-item">
		        <a class="nav-link text-warning" href="./admin.jsp">Admin</a>
		      </li>
	      <% } %>	      
	      
	    </ul>  
	    
		<% if (AlgoServer.isTesting() ) { %>
	        <span class="bg-danger text-white">dev</span>
		<% } %>	      
		<div class="pr-2">
			<button type="button" id="livePriceETHTRY" class="btn btn-outline-dark text-light">&nbsp;</button>
			<button type="button" id="livePriceBTCTRY" class="btn btn-outline-dark text-light">&nbsp;</button>
		</div>	    
	   
	    <form class="form-inline my-2 my-lg-0">
			<div class="btn-group">
			  <button type="button" class="btn btn-secondary dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
			    <% out.write (greetings);  %>
			  </button>
			  <div class="dropdown-menu">
		        <a class="dropdown-item" href="./wallet.jsp">Cüzdanım</a>
		        <a class="dropdown-item" href="./balance.jsp">Hesap Özeti</a>
			    <div class="dropdown-divider"></div>
			    
			    <a class="dropdown-item disabled" href="myprofile.jsp">Profilim</a>
			    <a class="dropdown-item" href="settings.jsp">Ayarlar</a>
			    <div class="dropdown-divider"></div>
			    
			    <a class="dropdown-item" href="./logout.jsp">Çıkış</a>
			  </div>
			</div>	    
	    </form>
	  </div>
	</nav>
	
	<% if (userObj.getPublicKey() == null) { %>
	<div class="alert  alert-dismissible alert-danger fade show" role="alert">
		Lütfen "açık anahtar" ve "gizli anahtar" tanımlamanızı yapınız! (Sağ Üst menü -> Ayarlar)
	  Please save your public key and private key to use functions! (right top menu -> Settings)
	  <button type="button" class="close" data-dismiss="alert" aria-label="Close">
	    <span aria-hidden="true">&times;</span>
	  </button>
	</div>
	<% } %>
