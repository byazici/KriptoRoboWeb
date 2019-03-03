<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="./header.jsp" %>

	<div class="container">
		<p />
		<h2>Yeni Algo</h2>
		
		<form action="create_.jsp" method="post" name="newAlgoForm" id="newAlgoForm">
		<input type="hidden" name="add" />
		<input type="hidden" name="priceDuration" value="0" />
		
		  <div class="form-group row">
		    <label for="inputEmail3" class="col-sm-2 col-form-label">Algo Adı</label>
		    <div class="col-sm-4">
		      <input type="text" class="form-control" id="name" name="name">
		    </div>
		    <div class="col-sm-6">
				<button  tabindex="-1" type="button" class="btn btn-secondary" data-toggle="popover" data-html="true" data-trigger="focus" title="Algo Adı" data-content="
				Algoritmanızı takip etmek için bir isim verin.
				"> ? </button>
		    </div>
		  </div>

		  <div class="form-group row">
		    <label for="inputEmail3" class="col-sm-2 col-form-label">Algo Türü</label>
		    <div class="col-sm-4">
		       <select class="form-control" id="exampleFormControlSelect1" name="algoType" id="algoType">
			      <option>LongOnly</option>
			    </select>
		    </div>
		    <div class="col-sm-6">
				<button tabindex="-1" type="button" class="btn btn-secondary" data-toggle="popover" data-html="true" data-trigger="focus" title="Algo Türü" data-content="
				Şu an sadece 'LongOnly' türünü destekliyoruz.
				<br/><br/> <em>LongOnly</em> algoritmasında fiyat yükselişi takip edilerek kazanç sağlanması hedeflenir. 
				"> ? </button>
		    </div>
		  </div>
		  
		  <div class="form-group row">
		    <label for="inputEmail3" class="col-sm-2 col-form-label">Kripto Cinsi</label>
		    <div class="col-sm-4">
		       <select class="form-control" id="pairSymbol" name="pairSymbol">
			      <option value="">Seçiniz</option>
			      <option value="BTCTRY" selected>BTC</option>
			      <option value="ETHTRY">ETH</option>
			    </select>
		    </div>
		    <div class="col-sm-6">
				<button tabindex="-1" type="button" class="btn btn-secondary" data-toggle="popover" data-html="true" data-trigger="focus" title="Döviz Cinsi" data-content="
				BTC veya ETH kripto dövizlerine emir verebilirsiniz.
				"> ? </button>
		    </div>
		  </div>		  


		  <div class="form-group row">
		    <label for="inputEmail3" class="col-sm-2 col-form-label">Yatırım Tutarı</label>
		    <div class="col-sm-4">
		       <input type="text" class="form-control" id="maxAmount" name="maxAmount">
		    </div>
		    <div class="col-sm-6">
				<button tabindex="-1" type="button" class="btn btn-secondary" data-toggle="popover" data-html="true" data-trigger="focus" title="Yatırım Tutarı" data-content="
				Algo için üretilecek alış emrinin tutarı. 
				"> ? </button>
		    </div>
		  </div>
		  	  
		  <div id="div_fiyat">
			  <div class="form-group row">
			    <label for="inputEmail3" class="col-sm-2 col-form-label">Baz Fiyat</label>
			    <div class="col-sm-2">
				     <select class="form-control" id="priceDuration" name="priceDuration"></select>
		    	 	</div>
			    <div class="col-sm-2">
		       		<input type="text" class="form-control" id="basePrice" name="basePrice">
		       	</div>
	
			    <div class="col-sm-6">
					<button tabindex="-1" type="button" class="btn btn-secondary" data-toggle="popover" data-html="true" data-trigger="focus" title="Baz Fiyat" data-content="
					Baz fiyat belirleyeceğiniz alış, satış ve tetik oranıyla çarpılarak alış ve satış emirleri oluşturulacaktır.
					"> ? </button>
			    </div>
			  </div>				  

			   <div class="form-group row">
				    <label for="inputEmail3" class="col-sm-2 col-form-label">Alış Oranı</label>
				    <div class="col-sm-1">- %</div>
				    <div class="col-sm-1">
				       <input type="text" class="form-control" id="buyRate" name="buyRate">
				    </div>
				    <div class="col-sm-2">
				       <input type="text" class="form-control" id="buyPrice" readonly="readonly">
				    </div>
				    <div class="col-sm-6">
						<button tabindex="-1" type="button" class="btn btn-secondary" data-toggle="popover" data-html="true" data-trigger="focus" title="Alış Oranı" data-content="
						Alış oranı x baz fiyat ile alış emri üretilir. Girdiğiniz oran negatiftir.
						<br/><br/> <em>Örnek: </em> Baz fiyat=1000, alış oranı =-%1 ise; Alış Fiyatı = 1000 x (100-1)/100 = 990 TL olur.
						"> ? </button>
				    </div>
			  </div>
 
 
			   <div class="form-group row">
				    <label for="inputEmail3" class="col-sm-2 col-form-label">Satış Oranı</label>
				    <div class="col-sm-1">+ %</div>
				    <div class="col-sm-1">
				       <input type="text" class="form-control" id="sellRate" name="sellRate">
				    </div>
				    <div class="col-sm-2">
				       <input type="text" class="form-control" id="sellPrice" readonly="readonly">
				    </div>
				    <div class="col-sm-6">
						<button tabindex="-1" type="button" class="btn btn-secondary" data-toggle="popover" data-html="true" data-trigger="focus" title="Satış Oranı" data-content="
						Satış oranı x baz fiyat ile satış emri üretilir.
						<br/><br/> <em>Örnek: </em> Baz fiyat=1000, satış oranı= +%3 ise; Satış Fiyatı = 1000 x (100+3)/100 = 1030 TL olur.
						"> ? </button>
				    </div>
			  </div>
			  
			  			  			  
			   <div class="form-group row">
				    <label for="inputEmail3" class="col-sm-2 col-form-label">Tetik Oranı</label>
				    <div class="col-sm-1">+ %</div>
				    <div class="col-sm-1">
				       <input type="text" class="form-control" id="trigRate" name="trigRate">
				    </div>
				    <div class="col-sm-2">
				       <input type="text" class="form-control" id="trigPrice" readonly="readonly">
				    </div>
				    <div class="col-sm-6">
						<button tabindex="-1" type="button" class="btn btn-secondary" data-toggle="popover" data-html="true" data-trigger="focus" title="Tetik Oranı" data-content="
						Tetik oranı x baz fiyat ile tetik fiyatı belirlenir.
						<br/><br/> <em>Örnek: </em> Baz fiyat=1000, tetik oranı= +%5 ise; Tetik Fiyatı = 1000 x (100+3)/100 = 1050 TL olur.
						<br/><br/>Satış emrinin aktifleşmesi için fiyatın 1050,00 TL'ye yükselmesi gerekmektedir. Fiyat daha yukarı giderse satış fiyatı da otomatik yükselir. Böylece kazanç yükselir.
						"> ? </button>
				    </div>
			  </div>
			  
		  
			  <div class="form-group row" id="">
			    <label for="inputEmail3" class="col-sm-2 col-form-label">Takip Süresi</label>
			    <div class="col-sm-4">
			       <select class="form-control" name="duration" id="duration">
				      <option value="">Seçiniz</option>
				      <option value="1">1 saat</option>
				      <option value="2">2 saat</option>
				      <option value="4">4 saat</option>
				      <option value="8">8 saat</option>
				      <option value="12">12 saat</option>
				      <option value="24">24 saat</option>
				    </select>
			    </div>
			    <div class="col-sm-6">
					<button tabindex="-1" type="button" class="btn btn-secondary" data-toggle="popover" data-html="true" data-trigger="focus" title="Takip Süresi" data-content="
					Algonun alış emri bu süre boyunca gerçekleşmezse iptal edilip o anki oranlarla yeni alış emri oluşturulur.
					"> ? </button>
			    </div>
			  </div>						  	  
		  </div>
		  
		  <div>
	  		 <div id="newAlgoAlert" class="alert alert-success d-none"></div>
			 <div id="newAlgoAlertErr" class="alert alert-danger d-none"></div>
		  </div>


		  <div class="form-group row">
		  	<div class="col-sm-2">&nbsp;</div>
		  	<button type="reset" class="btn btn-warning col-sm-2" value="Reset">Temizle</button> &nbsp;&nbsp;
			 <button type="submit" class="btn btn-primary col-sm-2" id="addButton" data-toggle="modal" data-target="#exampleModal">Algo Oluştur</button>

		  	<div class="col-sm-6"></div>
		  </div>
	  </form>

	</div>
		<script>
			
			$(function() {
				
			    $('#pairSymbol').change(function(){
				    	let dropdown = $('#priceDuration');
				    	const url = 'price_.jsp?symbol=' + $('#pairSymbol').val();
	
				    	$.getJSON(url, function (data) {
					    	dropdown.empty();
					    	dropdown.append('<option selected="true" value="">Manuel Fiyat</option>');
					    	dropdown.prop('selectedIndex', 0);

				    		$.each(data, function (key, entry) {
				    	    		dropdown.append($('<option></option>').attr('value', entry.priceVal.toFixed(2)).text(entry.priceText));
				    	 	 })
				    	  
				    	 	 
				    	 	$('#priceDuration')
				    		$('#priceDuration').trigger("change");
				    	  
				    	});	
				    	
			    	});		
				
			    $('#basePrice').change(function(){
			    		$('#buyRate').trigger("change");
			    		$('#sellRate').trigger("change");
			    		$('#trigRate').trigger("change");

			    	});		


			    $('#priceDuration').change(function(){
			        if($('#priceDuration').val() == '') {
			            $('#basePrice').removeAttr('readonly');
			            $('#basePrice').val(''); 
			            
			          //  $('#buyPrice').val('');
			          //  $('#buyRate').val('');
			          //  $('#sellPrice').val('');
			          //  $('#sellRate').val('');
			          //  $('#trigPrice').val('');
			          //  $('#trigRate').val('');

			        } else {
			            $('#basePrice').attr("readonly", true);
			            $('#basePrice').val($('#priceDuration').val()); 
			        }

			        $('#basePrice').trigger("change");
			    });			
			    

			    
			    $('#confirmButton').click(function(){
			    		var buyPrice =  ($('#buyPrice').val() != "") ?  $('#buyPrice').val() :  $('#buyPriceManuel').val();
			    		var sellPrice =  ($('#sellPrice').val() != "") ?  $('#sellPrice').val() :  $('#sellPriceManuel').val();
			    		var trigPrice =  ($('#trigPrice').val() != "") ?  $('#trigPrice').val() :  $('#trigPriceManuel').val();
			    		var qty = ($('#maxAmount').val() / buyPrice).toFixed(6);
				    	
			    		var msg = "<strong>" + $('#name').val() + "</strong> isimli " + $('#pairSymbol').val() + " algonuz,";
			    		msg += "<br \>"; 
			    		msg += "<br \> " + qty + " x " + buyPrice + " TL <span class='text-success'>ALIŞ</span>";
			    		msg += "<br \> " + qty + " x " + sellPrice + " TL <span class='text-danger'>SATIŞ</span> <em>(tetik fiyatı: " + trigPrice + " TL)</em>";
			    		msg += "<br \><br \> emirleri üretilecektir. ";
			    		msg += "Olası en az kar <mark>" + Math.round((sellPrice - buyPrice ) * qty, 2) + " TL</mark>"; 
			    					    		
			    		$('#confirm').html(msg);
			    		
			    });

			    $('#buyRate').change(function(){
			    		if ($('#buyRate').val() != "" && $('#basePrice').val() != ""){
				   	 	$('#buyPrice').val( ((1- $('#buyRate').val() / 100.0) * $('#basePrice').val()).toFixed(2)  ); 
			    		} else {
			    			$('#buyPrice').val("");
			    		}
			    });			

			    $('#sellRate').change(function(){
				    	if ($('#sellRate').val() != "" && $('#basePrice').val() != ""){
				    		$('#sellPrice').val( ((1+ $('#sellRate').val() / 100.0) * $('#basePrice').val()).toFixed(2)  );
				    	} else {
				    		$('#sellPrice').val("");
				    	}
			    });			

			    $('#trigRate').change(function(){
			    		if ($('#trigRate').val() != "" && $('#basePrice').val() != ""){
			    			$('#trigPrice').val( ((1+ $('#trigRate').val() / 100.0) * $('#basePrice').val()).toFixed(2)  );	
			    		} else {
			    			$('#trigPrice').val("");
			    		}
			    });	
			});		

			 (function($){
			        function processForm( e ){
			         $("#newAlgoAlertErr").addClass("d-none");
		        	 	 $("#newAlgoAlert").addClass("d-none");
		        	 	 $("#addButton").prop('disabled', true);
		        	 	 
			            $.ajax({
			                url: 'create_.jsp',
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
		
			$(document).ready(function(){
			    //$('#div_oran').hide(); 
			    //$('#div_fiyat').hide();
			    $('[data-toggle="tooltip"]').tooltip(); 
			    $('#pairSymbol').trigger("change");
			});
		</script>

<%@include file="./footer.jsp" %>		