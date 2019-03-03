<%@page import="com.by.robo.enums.UserRole"%>
<%@page import="com.by.robo.helper.UserHelper"%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.by.robo.enums.PairSymbol"%>
<%@page import="com.by.robo.server.AlgoServer"%>
<%@page import="com.by.robo.utils.DateUtils"%>
<%@page import="java.util.Date"%><%@
page import="java.text.SimpleDateFormat"%><%@
page import="java.util.TimeZone" %><% 
 %>
  

	<div class="container">
		<p />
		<p />
		<div class="p-3 mb-2 bg-light text-dark text-center">
			<a href="howitworks.jsp">Nasıl Çalışır?</a> |
			<a href="faq.jsp">Sıkça Sorulan Sorular</a> |
			<a href="mailto:hello@kriptorobo.com">İletişim</a>

			<p />
			<a href="policy.jsp">Kullanıcı Sözleşmesi</a> | 
			<a href="privacy.jsp">Gizlilik</a> |
			<a href="version.jsp">Versiyon Tarihçesi</a>
			
						
			<p />
			<div class="font-weight-light">© 2017 - <%= DateUtils.getCurrentYear()  %> kriptoRobo.com, tüm hakları saklıdır.</div>
					
			<p />
		</div>
		
		<div class="fixed-bottom text-right font-weight-light small footer bg-white">
				<%= DateUtils.defaultTimeZone.getDisplayName() %> / <%= new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(DateUtils.getCurrentDate()) %> (<mark><%= AlgoServer.appVersion %></mark>)
     	</div> 

	</div>

    <script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js"></script>

    <script src="https://unpkg.com/popper.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/js/bootstrap.min.js" integrity="sha384-alpBpkh1PFOepccYVYDB4do5UnbKysX5WZXm3XxPqe5iKTfUKjNkCk9SaVuEZflJ" crossorigin="anonymous"></script>
    
    <script src='./js/moment.min.js'></script>
    <script src='./js/bootstrap-sortable.js'></script>
    

    <script>
    $(function () {
    	  $('[data-toggle="popover"]').popover()
    	})
    	
    	 $('[data-toggle="popover"]').on('click', function(e) {e.preventDefault(); return true;});

	

  	updateTicks();
    
    function updateTicks(){
		updateTickValue("BTCTRY");
		updateTickValue("ETHTRY");    	
    }
    
    	setInterval(function(){
    		updateTicks();
    	}, 15000);

 			
 	function updateTickValue(curr){
	   $.ajax({
	        url: "tickPrice.jsp",
	        data: {
	            symbol: curr
	        },
	        type: "GET",
	        dataType: "text",
	        success: function (data) {
	            var result = data;
	            
	            var currValue = $('#livePrice' + curr).html();
	            if (currValue != data){
		            $('#livePrice' + curr).html(data);
		            $('#livePrice' + curr).animate({color: "#FFFF00"}, 1000)
		            .animate({color: "#FFFFFF"}, 1000);
	            }		            
	        },
	        error: function (xhr, status) {
	            console.log("Price update error!");
	        },
	        complete: function (xhr, status) {
	           //
	        }
	    }); 		
 	}


    	
    	
    </script>    
<!-- Global site tag (gtag.js) - Google Analytics -->
<script async src="https://www.googletagmanager.com/gtag/js?id=UA-613306-4"></script>
<script>
  window.dataLayer = window.dataLayer || [];
  function gtag(){dataLayer.push(arguments);}
  gtag('js', new Date());

  gtag('config', 'UA-613306-4');
</script>
    
  </body>
</html>
