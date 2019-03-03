<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.by.robo.web.UserWao"%>
<%@page import="com.by.robo.model.Result"%>
<%@page import="com.by.robo.utils.RecaptchaUtil"%>
<%@page import="com.by.robo.helper.UserHelper"%>
<%@page import="com.by.robo.model.User"%>
<%
	request.setCharacterEncoding("UTF-8");

boolean showForm = true;
String message = "";
String Msgclass = "";

if (request.getParameter("register") != null){
	String mail = request.getParameter("mail");
	String tckn = request.getParameter("tckn");
	String first = request.getParameter("firstName");
	String last = request.getParameter("lastName");
	String year = request.getParameter("birthYear");
	String recaptcha = request.getParameter("g-recaptcha-response");
	String policy = request.getParameter("policy");
	
	Result result = UserWao.registerUser(mail, tckn, first, last, year, recaptcha, policy);
	if (!result.isSuccess()){
		Msgclass = "alert-danger";
		message = result.getErrorMsg();
	} else {
		Msgclass = "alert-success";
		message = result.getErrorMsg();
		showForm = false;
	}
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
			   	 <div class="alert <%= Msgclass %> alert-dismissible fade show" role="alert">
			   	 	<%= message %>
			   	 	<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
			   	 </div> 
			    <% } %>
			    			  
			  <% if (showForm) { %>

				<div class="card">
				  <div class="card-header">
				     <h4 class="card-title text-center">Yeni Üye</h4>
				     <h6 class="text-center">Zaten üye iseniz <a href="login.jsp">buradan</a> giriş yapabilirsiniz.</h6>
				  </div>
				  
				  
				  <div class="card-body align-self-center">
				    <form method="post" action="register.jsp">
					  <div class="form-group row">
					     <label for="mail" class="col-sm-5 col-form-label">E-Posta</label> 
					     <div class="col-sm-7">
						     <input type="text" name="mail" class="form-control" >
					     </div>
					  </div>
					  
					  <div class="form-group row">
					     <label for="tckn" class="col-sm-5 col-form-label">TC Kimlik No</label> 
					     <div class="col-sm-7">
						     <input type="text" name="tckn" class="form-control">
					     </div>
					  </div>					  
					  
					  <div class="form-group row">
					     <label for="firstName" class="col-sm-5 col-form-label">Adınız</label> 
					     <div class="col-sm-7">
						     <input type="text" name="firstName" class="form-control">
					     </div>
					  </div>
					  
					  <div class="form-group row">
					     <label for="lastName" class="col-sm-5 col-form-label">Soyadınız</label> 
					     <div class="col-sm-7">
						     <input type="text" name="lastName" class="form-control">
					     </div>
					  </div>

					  <div class="form-group row">
					     <label for="birthYear" class="col-sm-5 col-form-label" >Doğum Yılınız</label> 
					     <div class="col-sm-7">
						     <input type="text" name="birthYear" class="form-control" placeholder="Ör: 1970">
					     </div>
					  </div>

					<p />
				  	<div class="form-group row justify-content-center"> 
						<div class="g-recaptcha" data-sitekey="6LdNjT8UAAAAANH87dosaZ5E8dJ8LwKoU2IjO30G"></div>
					</div>
					
					<p />
					<div class="custom-control custom-checkbox">
					  <input type="checkbox" class="form-check-input" name="policy" value="1" />
					  <label class="form-check-label" for="policy"><a href="policy.jsp" target="_new">Kullanım Sözleşmesi</a>'ni okudum ve kabul ediyorum.</label>
					</div>		   
					  
					<p />
					  <div class="form-group row"> 
					     <div class="col-sm-12">
						    <input type="submit" class="btn btn-success btn-block" name="register" value="Üye Ol"/>
						</div>
					  </div>
							
					</form>	    
				</div>	  
		   </div>
				<% } else { %>
				
			 <div class="row justify-content-center">
  			 <div class="col-sm-12">
				
					
					<div class="card">
					  <div class="card-header">
					     <h4 class="card-title text-center">Yeni Üye</h4>
					     <h6 class="text-center">E-postanızı kontrol ediniz!</h6>
					  </div>
					
					 <div class="row p-2"> 
					     <div class="col-sm-12">
						   Hesabınız başarıyla açıldı. E-posta adresinize bir mesaj gönderdik. 
						   <p />Mesajdaki linke tıklayarak üyeliğinizi aktif hale getirebilirsiniz.
						   <p />Eğer mail ulaşmaz ise "spam" filtrelerinize bakınız ! 
						</div>
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
