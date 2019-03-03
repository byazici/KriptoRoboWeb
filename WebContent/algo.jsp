<%@page import="com.by.robo.helper.AlgoHelper"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.by.robo.web.AlgoWao"%>
<%@page import="com.by.robo.utils.StringUtils"%>
<%@page import="com.by.robo.helper.OrderHelper"%>
<%@page import="com.by.robo.model.Order"%>
<%@page import="com.by.robo.model.Result"%>
<%@page import="com.by.robo.utils.NumberUtils"%>
<%@page import="java.util.Date"%>
<%@page import="com.by.robo.utils.DateUtils"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="com.by.robo.enums.TrueFalse"%>
<%@page import="com.by.robo.enums.PairSymbol"%>
<%@page import="com.by.robo.model.Algo"%>
<%@page import="java.util.List"%>
<%@page import="com.by.robo.enums.AlgoStatus"%>
<%@include file="./header.jsp" %>

<%	String message = null;
	String messageClass = null;

	if (request.getParameter("retry") != null && request.getParameter("algoId") != null){
		String id = request.getParameter("algoId");
		if (AlgoWao.retryAlgo(userObj, Integer.valueOf(id))){
			message = "#" + id + " algo tekrar edildi.";
			messageClass = "alert-success";			
		} else {
			message = "#" + id + " algo tekrar denemesinde hata alındı!";
			messageClass = "alert-danger";	
		}
	} else if (request.getParameter("delete") != null && request.getParameter("algoId") != null){
		String id = request.getParameter("algoId");
		Result r = AlgoWao.cancelAlgo(userObj, Integer.valueOf(id));
		if (r != null && r.isSuccess()){
			message = "#" + id + " algo iptal edildi.";
			messageClass = "alert-success";			
		} else {
			message = "#" + id + " algo iptal edilemedi!" + (r != null ? r.getErrorMsg() : "");
			messageClass = "alert-danger";	
		}
	} 


	AlgoStatus as = null;
	List<Algo> algoList = null;
	if (request.getParameter("done") != null){
		as = AlgoStatus.DONE;
		algoList = AlgoWao.getAlgoList(userObj, as);
	} else if (request.getParameter("cancel") != null){
		as = AlgoStatus.CANCEL;
		algoList = AlgoWao.getAlgoList(userObj, as);
	} else {
		algoList = AlgoWao.getAlgoList(userObj, null);
	}
	
	BigDecimal btcPrice = AlgoServer.getLastTick(PairSymbol.BTCTRY).getLast();
	BigDecimal ethPrice = AlgoServer.getLastTick(PairSymbol.ETHTRY).getLast();
	
%>

	<div class="container">
		<p />
		<h2>Algo</h2>
		<div class="btn-group" role="group">
		<form action="algo.jsp" method="post">
			  <button type="submit" name="open" class="btn btn-secondary<%= (as == null) ? " btn-lg" : "" %>">Açık</button>
			  <button type="submit" name="done" class="btn btn-primary<%= (as == AlgoStatus.DONE) ? " btn-lg" : "" %>">Gerçekleşen</button>
			  <button type="submit" name="cancel" class="btn btn-danger<%= (as == AlgoStatus.CANCEL) ? " btn-lg" : "" %>">İptal</button>
			  <button type="button" class="btn btn-success ml-5" data-toggle="modal" data-target="#newAlgoModal" onclick="resetForm();">Yeni Algo</button>
			  <a href="create.jsp" class="btn btn-success ml-1">Yeni Algo (detaylı)</a>
		</form>
		</div>

		<% if (message != null) { %>
			<p />
			<div class="alert  alert-dismissible <%= messageClass %> fade show" role="alert">
			  <%= message %>
			  <button type="button" class="close" data-dismiss="alert" aria-label="Close">
			    <span aria-hidden="true">&times;</span>
			  </button>
			</div>
		<% } %>
		
		<p />
		<table class="table table-sm table-bordered sortable">
		  <caption>Algoritmalar</caption>
		
		 <thead class="thead-light">
		   <tr>
		     <th scope="col">#</th>
		     <th scope="col">Algo</th>
		     <th scope="col">Sembol</th>
		     <th scope="col" class="text-right">Miktar</th>
		     <th scope="col" class="text-right">Tutar</th>
		     <th scope="col" class="text-right">Alış</th>
		     <th scope="col" class="text-right">Tetik</th>
		     <th scope="col" class="text-right">Satış</th>
		     <th scope="col" class="text-right">MTM</th>
		     <th scope="col" class="text-right">Kar</th>
		     <th scope="col">Süre</th>
		     
		     <% if (as != null && as.equals(AlgoStatus.DONE)) { %> 
		     <th scope="col">Alış Gerç.</th>
		     <th scope="col">Satış Gerç.</th>
		     <% } %>
		     <th scope="col">Durum</th>
		     <th scope="col" data-defaultsort="disabled">&nbsp;</th>
		   </tr>
		 </thead>
		 <tbody>	
		<% for(Algo algo : algoList) { 
			String badge = AlgoWao.getAlgoBadge(algo); 
		%> 
		    <tr>
		      <td><%= algo.getId() %></td>
		      <td><%= algo.getName() %></td>
		      <td><%= algo.getPairSynbol() %></td>
		      <td class="text-right"><%= NumberUtils.formatDec6(algo.getBuyAmt()) %></td>
		      <td class="text-right"><%= NumberUtils.formatDec2(algo.getMaxAmount()) %></td> 
		      <td class="text-right"><%= NumberUtils.formatDec2(algo.getBuyPrice()) %></td>
		      <td class="text-right"><%= NumberUtils.formatDec2(algo.getTrigPrice()) %></td>
		      <td class="text-right"><%= NumberUtils.formatDec2(algo.getSellPrice()) %></td>
		      
		      <%
				BigDecimal mtm = null;
		      	try{
			      	if (algo.getBuyPrice() != null && algo.getBuyAmt() != null){
				      	if (algo.getPairSynbol() == PairSymbol.BTCTRY){
				      		mtm = btcPrice.subtract(algo.getBuyPrice()).multiply(algo.getBuyAmt());
				      	} else {
				      		mtm = ethPrice.subtract(algo.getBuyPrice()).multiply(algo.getBuyAmt());
				      	}
			      	} else {
			      		mtm = BigDecimal.ZERO;		      				
			      	}
		      	} catch (Exception e){
		      		mtm = BigDecimal.ZERO;
		      	}
		      %>
		      <td class="text-right"><%= NumberUtils.formatDec2(mtm) %></td>
		      <td class="text-right"><%= AlgoWao.getProfit(algo) %></td>
		       
		      <td><%
		      	if (algo.getStatus() == AlgoStatus.TRIG_WAIT 
		      		|| algo.getStatus() == AlgoStatus.SELL_WAIT 
		      		|| algo.getStatus() == AlgoStatus.SELL_OPEN){
		      		out.write("&nbsp;");
		      	} else {
		      		//out.write(DateUtils.formatAsTime(algo.getExpireDate()) + " (" + DateUtils.getRemaining(algo.getExpireDate())  + ")");
		      		out.write(DateUtils.getRemaining(algo.getExpireDate()));
				} %>
			 </td>
			 
		     <% if (as != null && as.equals(AlgoStatus.DONE)) { %> 
			 <td><%= DateUtils.formatAsDateTime(algo.getBuyRlzDate()) %></td>
			 <td><%= DateUtils.formatAsDateTime(algo.getSellRlzDate()) %></td>
			<% } %>			 
			 
			  <td><span class="badge badge-pill badge-<%=badge%>"><%= algo.getStatus() %></span></td>
		      <td class="flex-nowrap">
		      	<form action="algo.jsp" method="post">
			      	<a href="#"><img src="./icons/info.svg" class="icon" alt="info" data-container="body" tabindex="0" data-html="true" data-toggle="popover" data-trigger="focus" title="Details" data-content="<%
			      	String info = "<pre>";
			      	info += "Create    : " + DateUtils.formatAsDateTime(algo.getCreateDate()) + "\n";
			      	info += "Update    : " + DateUtils.formatAsDateTime(algo.getLastCreate()) + "\n";
			      	info += "B/S/T     : " + algo.getBuyRate() + "/" + algo.getSellRate() + "/" + algo.getTrigRate() + "<br/>";
			      	info += "Ort.Fiyat : " +NumberUtils.formatDec2(algo.getAvgPrice()) + "<br/>";
			      	info += "Ref ID    : " + algo.getRefId() + "<br/>";
			      	info += "Buy ID    : " + algo.getBuyId() + "<br/>";
			      	info += "Sell ID   : " + algo.getSellId() + "<br/>";
			      	info += "Duration  : " + algo.getDuration() + "<br/>";
			      	info += "Price Dur : " + algo.getPriceDuration() + "<br/>";
			      	info += "Açıklama  : " + algo.getDescr() + "<br/>";
			      	info += "</pre>";
			      	
			      	out.write (info);
			      	%>"></a>
	
			      	<% if (algo.getStatus() != AlgoStatus.SELL_OPEN){ %>
			      	  <input type="hidden" name="algoId" value="<%= algo.getId() %>">
	    			  	  <button type="submit" name="delete" onclick="return confirm('Are you sure?')" class="button border-0" alt="trash"><img src="./icons/trash.svg" class="icon" alt="trash"></button>
			      	<% 	}	%>
			      	<% if (algo.getStatus() == AlgoStatus.ERROR && algo.getBuyId() == 0){ %>
			      	  <input type="hidden" name="algoId" value="<%= algo.getId() %>">
	    			  	  <button type="submit" name="retry" onclick="return confirm('Are you sure?')" class="button border-0" alt="retry"><img src="./icons/reload.svg" class="icon" alt="reload"></button>
			      	<% 	}	%>
		      	</form>
		      	
			      	</td>
			    </tr>			
			<% } %>
			  </tbody>
		</table>
	</div>

	<div class="modal fade" id="newAlgoModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
	  <div class="modal-dialog" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <h5 class="modal-title" id="exampleModalLongTitle">Yeni Algo</h5>
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
	          <span aria-hidden="true">&times;</span>
	        </button>
	      </div>
	      
		<form action="algo_.jsp" method="post" name="newAlgoForm" id="newAlgoForm">
		<input type="hidden" name="add" />
	      
	      <div class="modal-body">
	      
	      
			  <div class="form-group row">
			    <label for="" class="col-sm-5 col-form-label">Algo Name</label>
			    <div class="col-sm-7">
			      <input type="text" name="name" class="form-control" placeholder="">
			    </div>
			  </div>
			
			  <div class="form-group row">
			    <label for="pairSymbol" class="col-sm-5 col-form-label">Sembol</label>
			    <div class="col-sm-7">
				    <select class="form-control" name="pairSymbol">
				      <option value="BTCTRY">BTCTRY</option>
				      <option value="ETHTRY">ETHTRY</option>
				    </select>				    
			    </div>
			  </div>
			
			  <div class="form-group row">
			    <label for="maxAmount" class="col-sm-5 col-form-label">Maks Tutar</label>
			    <div class="col-sm-7">
			      <input type="text" name="maxAmount" class="form-control" placeholder="">			    
			    </div>
			  </div>

			  <div class="form-group row">
			    <label for="buyRate" class="col-sm-5 col-form-label">Alış Oranı</label>
			    <div class="col-sm-7">
			      <input type="text" name="buyRate" class="form-control" placeholder="">
			    </div>
			  </div>

			  <div class="form-group row">
			    <label for="trigRate" class="col-sm-5 col-form-label">Tetik Oranı</label>
			    <div class="col-sm-7">
			      <input type="text" name="trigRate" class="form-control" placeholder="">
			    </div>
			  </div>
			  
			  <div class="form-group row">
			    <label for="sellRate" class="col-sm-5 col-form-label">Satış Oranı</label>
			    <div class="col-sm-7">
			      <input type="text" name="sellRate" class="form-control" placeholder="">
			    </div>
			  </div>
			  
			  <div class="form-group row">
			    <label for="duration" class="col-sm-5 col-form-label">Algo Süresi</label>
			    <div class="col-sm-7">
				    <select class="form-control" name="duration">
				      <option value="">Seçiniz</option>
				      <option value="1">1 saat</option>
				      <option value="2">2 saat</option>
				      <option value="4">4 saat</option>
				      <option value="8">8 saat</option>
				      <option value="12">12 saat</option>
				      <option value="24">1 gün</option>
				    </select> 
			    </div>
			  </div>			  			  
			  		
			  <div class="form-group row">
			    <label for="priceDuration" class="col-sm-5 col-form-label">Ort. Fiyat</label>
			    <div class="col-sm-7">
				    <select class="form-control" name="priceDuration">
				      <option value="">Seçiniz</option>
				      <option value="1">Önceki saat</option>
				      <option value="2">Önceki 2 saat</option>
				      <option value="4">Önceki 4 saat</option>
				      <option value="8">Önceki 8 saat</option>
				      <option value="12">Önceki 12 saat</option>
				      <option value="24">Önceki 1 gün</option>
				    </select> 
			    </div>
			  </div>	
			<div id="newAlgoAlert" class="alert alert-success d-none"></div>
			<div id="newAlgoAlertErr" class="alert alert-danger d-none"></div>
  				  		
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-secondary" data-dismiss="modal">İptal</button>
	  	    <button type="submit" id="addButton" class="btn btn-primary">Kaydet</button>
	      </div>
		</form>	
		
	<script>
	
	 (function($){
	        function processForm( e ){
	         $("#newAlgoAlertErr").addClass("d-none");
        	 	 $("#newAlgoAlert").addClass("d-none");
        	 	 $("#addButton").prop('disabled', true);
        	 	 
	            $.ajax({
	                url: 'algo_.jsp',
	                dataType: 'json',
	                type: 'post',
	                data: $(this).serialize(),
	                success: function( data, textStatus, jQxhr ){
	                	
	                		respCode = data['respCode'];
	                		respMsg = data['respMsg'];
		                	
		                	if (respCode == 1){
			                	 $("#newAlgoAlert").removeClass("d-none").html(respMsg);
			                	 location.href='algo.jsp';
			                	 
		                	} else {
		               	 	 $("#addButton").prop('disabled', false);
			                	 $("#newAlgoAlertErr").removeClass("d-none").html(respMsg);
		                	}
	                	
	                },
	                error: function( jqXhr, textStatus, errorThrown ){
	               	 	 $("#addButton").prop('disabled', false);
		                	 $("#newAlgoAlertErr").removeClass("d-none").html(errorThrown);
	                }
	            });

	            e.preventDefault();
	        }

	        $('#newAlgoForm').submit( processForm );
	    })(jQuery);
	
	 
	 function resetForm(){
		 $("#newAlgoForm").trigger("reset");
	 }
	
	</script>
     
	    </div>
	  </div>
	</div>
	

<%@include file="./footer.jsp" %>