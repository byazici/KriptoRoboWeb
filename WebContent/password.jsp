<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.by.robo.web.UserWao"%>
<%@page import="com.by.robo.model.Result"%>
<%@page import="com.by.robo.utils.RecaptchaUtil"%>
<%@page import="com.by.robo.helper.UserHelper"%>
<%@page import="com.by.robo.model.User"%>
<%
	request.setCharacterEncoding("UTF-8");

boolean showForm = true;
boolean showResult = false;
String message = "";
String Msgclass = "";
String token = request.getParameter("t"); 
String newPass = request.getParameter("newPass"); 
String newPass2 = request.getParameter("newPass2"); 
String recaptcha = request.getParameter("g-recaptcha-response");


if (token == null || token.length() == 0){
	message = "Kullanıcının onaylanamadı! Lütfen size gelen e-postadaki linke tekrar tıklayınız. Hata devam ederse yeniden başvurunuz.";
	Msgclass = "alert-danger";
	showForm = false;
}

if (request.getParameter("password") != null){
	Result result = UserWao.setPassword(token, recaptcha, newPass, newPass2);  
	if (result != null && result.isSuccess() ){
		showForm = false;
		showResult = true;
		Msgclass = "alert-success";
	} else {
		message = result.getErrorMsg();
		Msgclass = "alert-danger";
		showForm = true;
		showResult = false;
	}
	
	showResult = true;
}

%><!doctype html>

<html lang="tr">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

  <title>KriptoRobo</title>
  <meta name="description" content="KriptoRobo">
  <meta name="author" content="KriptoRobo">

  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/css/bootstrap.min.css" integrity="sha384-PsH8R72JQ3SOdhVi3uxftmaW6Vc51MKb0q5P2rRUpPvrszuE4W1povHYgTpBfshb" crossorigin="anonymous">

  <!--[if lt IE 9]>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/html5shiv/3.7.3/html5shiv.js"></script>
  <![endif]-->
  
  <script src='https://www.google.com/recaptcha/api.js'></script>
</head>

  <body>
	<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
		<a class="navbar-brand h1 mb-0" href="./index.jsp">KriptoRobo.com</a>
	</nav> 

	<div class="container"> 
		<p />
	    
	    <div class="row justify-content-center">
  			<div class="col-sm-16">

			    <% if (message.length() > 0) { %>
			   	 <div class="alert <%= Msgclass %> fade show" role="alert">
			   	 	<%= message %>
			   	 </div> 
			    <% } %>
			    			  
			  <% if (showForm) { %>

				<div class="card">
				  <div class="card-header">
				     <h4 class="card-title text-center">Şifre Tanımlama</h4>
				     <h6 class="text-center">Yeni bir şifre belirleyin.</h6>
				  </div>
				  
				  
				  <div class="card-body align-self-center">
				    <form method="post" action="password.jsp">
				    		<input type="hidden" name="t" value="<%= token  %>" />
					  <div class="form-group row">
					     <label for="newPass" class="col-sm-5 col-form-label">Şifre</label> 
					     <div class="col-sm-7">
						     <input type="password" name="newPass" class="form-control">
					     </div>
					  </div>
					  
					  <div class="form-group row">
					     <label for="newPass2" class="col-sm-5 col-form-label">Şifre Tekrarı</label> 
					     <div class="col-sm-7">
						     <input type="password" name="newPass2" class="form-control">
					     </div>
					  </div>	
					  
					 <p />
					  <div class="form-group row">
					     <label for="rules" class="col-sm-5 col-form-label">Şifre Kuralları</label> 
					     <div class="col-sm-7" name="rules">
								<li>8 hane uzunluğunda olmalı</li>
								<li>1 sayı</li>
								<li>1 küçük harf</li>
								<li>1 büyük harf</li>
								<li>1 adet özel karakter (!@#$%^&*()_+-=[]{};’:”\|,.<>/?)</li>
					     </div>
					  </div>

					<p />
				  	<div class="form-group row justify-content-center"> 
						<div class="g-recaptcha" data-sitekey="6LdNjT8UAAAAANH87dosaZ5E8dJ8LwKoU2IjO30G"></div>
					</div>
					
					  
					<p />
					  <div class="form-group row"> 
					     <div class="col-sm-12">
						    <input type="submit" class="btn btn-success btn-block" name="password" value="Şifre Oluştur"/>
						</div>
					  </div>
							
					</form>	    
				</div>	  
		   </div>
				<% } else if (showResult){ %>
				
			 <div class="row justify-content-center">
				<div class="card">
				  <div class="card-header">
				     <h4 class="card-title text-center">Şifre Tanımlama</h4>
				     <h6 class="text-center">Yeni şifren tanımlandı.</h6>
				  </div>
				
				 <div class="row p-2"> 
				     <div class="col-12">
					   Yeni şifreniz tanımlandı. 
					   <p />Şimdi <a href="login.jsp">giriş</a> yapabilirsiniz.
					   <p />Teşekkürler... 
					</div>
				  </div>
				</div>
				<% } %>
		 </div>
	</div>
</div>


    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.3/umd/popper.min.js" integrity="sha384-vFJXuSJphROIrBnz7yo7oB41mKfc8JzQZiCq4NCceLEaO4IHwicKwpJf9c9IpFgh" crossorigin="anonymous"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/js/bootstrap.min.js" integrity="sha384-alpBpkh1PFOepccYVYDB4do5UnbKysX5WZXm3XxPqe5iKTfUKjNkCk9SaVuEZflJ" crossorigin="anonymous"></script>
  </body>
</html>
