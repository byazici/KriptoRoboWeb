<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.by.robo.web.UserWao"%>
<%@page import="com.by.robo.model.Result"%>
<%@include file="./header.jsp" %>

<%
String message = null;
String messageClass = null;
User user = UserWao.getUser(userObj.getId());

if (request.getParameter("saveKeys") != null){
	String publicKey = request.getParameter("publicKey");
	String privateKey = request.getParameter("privateKey");
	
	Result result = UserWao.saveApiKeys(userObj, publicKey, privateKey);
	if (result != null && result.isSuccess()){
		message = "Keys saved succesfully.";
		messageClass = "alert-success";			
	} else {
		if (result == null) {
			message = "Keys save error! Please try again.";
			messageClass = "alert-warning";			
		} else {
			message = "Keys save error! Reason: " + result.getErrorCode() + ":" + result.getErrorMsg();
			messageClass = "alert-warning";			
		}
	}
} else if (request.getParameter("savePassword") != null){
	String currentPass = request.getParameter("currentPass");
	String newPass = request.getParameter("newPass"); 
	String newPass2 = request.getParameter("newPass2"); 
	
	Result result = UserWao.updatePassword(userObj, currentPass, newPass, newPass2);
	if (result != null && result.isSuccess()){
		message = "Password saved succesfully.";
		messageClass = "alert-success";			
	} else {
		message = "Password save error! Reason: " + result.getErrorCode() + ":" + result.getErrorMsg();
		messageClass = "alert-warning";			
	}
}


%>
	<div class="container">
		<p />
		<h2>Ayarlar</h2>
		
		<% if (message != null) { %>
			<div class="alert  alert-dismissible <%= messageClass %> fade show" role="alert">
			  <%= message %>
			  <button type="button" class="close" data-dismiss="alert" aria-label="Close">
			    <span aria-hidden="true">&times;</span>
			  </button>
			</div>
		<% } %>
		
		
			<div class="card">
			  <h4 class="card-header">Api Tanımları</h4>
			  <div class="card-body">
 				    <p class="card-text">Açık anahtar ve gizli anahtarınızı tanımlayabilirsiniz.</p>

					<form action="settings.jsp" method="post" class="form-inline">
					  <div class="form-group mx-sm-3">
					    <label for="public" class="sr-only">Açık Anahtar (Public Key)</label>
					    <input type="text" class="form-control" size="40" name="publicKey" value="<%= user.getPublicKey() != null ? user.getPublicKey() : "" %>" placeholder="Public Key">
					  </div>
					  <div class="form-group mx-sm-3">
					    <label for="private" class="sr-only">Gizli Anahtar (Private Key)</label>
					    <input type="text" class="form-control" size="40" name="privateKey" value="<%= user.getPrivateKey() != null ? user.getPrivateKey() : "" %>" placeholder="Private Key">
					  </div>
					  <button type="submit" name="saveKeys" class="btn btn-primary">Save</button>
					  
					  &nbsp;<button type="button" class="btn btn-primary" data-toggle="modal" data-target="#myModal">?</button>

						<div id="myModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
						  <div class="modal-dialog">
						    <div class="modal-content">
						        <div class="modal-body">
						            <img src="./images/btcturk-help.png" width="800" class="img-responsive">
						        </div>
						    </div>
						  </div>
						</div>
					</form> 

			  </div>
			</div>		
		
			<p />
			<div class="card">
			  <h4 class="card-header">Şifre</h4>
			  <div class="card-body">
 				    <p class="card-text">Şifrenizi güncelleyebilirsiniz..</p>
 				    <p class="card-text">
 				    En az 8 karakterden oluşan şifreniz şu kurallara uymalı: 
 				    <br/>En az bir sayı içermeli
 				    <br/>En az bir büyük harf içermeli
 				    <br/>En az bir küçük harf içermeli
 				    <br/>En az bir özel karakter içermeli (!@#$%^&*()_+-=[]{};’:”\|,.<>/?) with min 8 chars length. </p>

					<form action="settings.jsp" method="post" class="form-inline">
					  <div class="form-group mx-sm-2">
					    <label for="currentPass" class="sr-only">Mevcut Şifre</label>
					    <input type="password" class="form-control" name="currentPass" placeholder="Mevcut Şifre">
					  </div>
					  <div class="form-group mx-sm-2">
					    <label for="newPass" class="sr-only">Yeni Şifre</label>
					    <input type="password" class="form-control" name="newPass" placeholder="Yeni Şifre">
					  </div>
					  <div class="form-group mx-sm-2">
					    <label for="newPass2" class="sr-only">Yeni Şifre (tekrar)</label>
					    <input type="password" class="form-control" name="newPass2" placeholder="Yeni Şifre">
					  </div>
					  <button type="submit" name="savePassword" class="btn btn-primary">Kaydet</button>
					</form>	
			  </div>
			</div>				
		
		
	
	</div>
	

<%@include file="./footer.jsp" %>