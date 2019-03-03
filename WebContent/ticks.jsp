<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="com.by.robo.utils.DateUtils"%>
<%@page import="java.util.LinkedList"%>
<%@page import="java.util.TreeSet"%>
<%@page import="java.util.Set"%>
<%@page import="com.by.robo.dao.TickDao"%>
<%@page import="java.util.Map"%>
<%@include file="./header.jsp" %>
	<%
		String tickType = null;
		String title = null;
		LinkedList<String> ticks = null;
		PairSymbol p = PairSymbol.BTCTRY;
		
		if (request.getParameter("symbol") != null && request.getParameter("symbol").equals("ETH")){
			p = PairSymbol.ETHTRY;
		} 
		
		if (request.getParameter("daily") != null){
	    		tickType = "daily";
	    		title = p.getValue() + " Günlük L/H/O/C";
	    		ticks = TickDao.getTickDayGraph(p, 24);
		} else	{
			tickType = "hourly";
	    		title = p.getValue() + " Saatlik L/H/O/C";
	    		ticks = TickDao.getTickHourGraph(p, 24);
		}
	%>
		
	<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
		<script type="text/javascript">
	
	      // Load the Visualization API and the corechart package.
	      google.charts.load('current', {'packages':['corechart']});
	
	      // Set a callback to run when the Google Visualization API is loaded.
	      google.charts.setOnLoadCallback(drawChart);
	
	      // Callback that creates and populates a data table,
	      // instantiates the pie chart, passes in the data and
	      // draws it.
	      function drawChart() {

	        // Create the data table.
	        var data = new google.visualization.DataTable();
	//        data.addColumn('date', 'Tarih');
	//        data.addColumn('number', 'open');
	//        data.addColumn('number', 'max');
	//        data.addColumn('number', 'min');
	//        data.addColumn('number', 'close');
	//        data.addColumn('number', 'avg');
	       // data.addRows([
	       var data = google.visualization.arrayToDataTable([ 
	    	   
	        <%
		    //String chartDateObj = request.getParameter("chartDate");
       		//String refresh = request.getParameter("refresh");
       		//String last24 = request.getParameter("last24");

   			/*Date chartDate = null;
        		if (chartDateObj != null){
        			chartDate = new SimpleDateFormat("yyyy-MM-dd").parse(chartDateObj);
	        	} else {
	        		chartDate = DateUtils.getToday();
	        	}
        */

        		String graph = "";
        		for(String tick : ticks) {
        			graph = graph.concat(tick);
        		}
        		
        		if (graph != null && graph.length() > 1){
        			graph = graph.trim();
        			
        			if (graph.substring(graph.length() - 1).equals(",")){
    	        			graph = graph.substring(0, graph.length() - 1);		// remove last , char.	
        			}
        		}
	        %>
	        		<%= graph %>
	
	        ], true);
	
	       var options = {
	    		  	  title   : '<%= title %>',   
	    		   	  width   : 1200,
                  height  : 600	,
	    		      legend  :'none'
	    	        //  ,bar: { groupWidth: '100%' } // Remove space between bars.
	    		    };	       
	       
	       var options3 = {
	    	          legend: 'none',
	    	          bar: { groupWidth: '100%' }, // Remove space between bars.
	    	          candlestick: {
	    	            fallingColor: { strokeWidth: 0, fill: '#a52714' }, // red
	    	            risingColor: { strokeWidth: 0, fill: '#0f9d58' }   // green
	    	          }
	    	        };


	        // Set chart options
	        var options2 = {'title':'Last 24 hours, average price',
	                       'width':1000,
	                       'height':800	,
	
	                       //curveType: 'function',
	                       legend: { position: 'bottom' },	      
	                       pointSize: 8,
	                       pointShape: 'diamond',
	                       hAxis: {
	                    	   		gridlines: {
	                               count: -1,
	                               units: {
	                                 days: {format: ['MMM dd']},
	                                 hours: {format: ['HH', 'ha']},
	                               }
	                             },   

	                           //gridlines: {count: 100}
	                         },
							vAxis: {
								  viewWindow: {
									//min: 31000,
									//max: 32000
								  }
								}
						   };
	
	        // Instantiate and draw our chart, passing in some options.
	        var chart = new google.visualization.CandlestickChart(document.getElementById('chart_div'));
	        chart.draw(data, options);
	      }
	    </script>
	    
	<div class="container">
		<p />
		<h2>Fiyatlar</h2>
				
		<form action="ticks.jsp" method="post">
			<div class="row mx-auto">
				<div class="col-lg-3 btn-group" data-toggle="buttons">
				  <label class="btn btn-secondary <%= (p.equals(PairSymbol.BTCTRY)) ? "active" : "" %>">
				    <input type="radio" name="symbol" value="BTC" <%= (p.equals(PairSymbol.BTCTRY)) ? "checked" : "" %> autocomplete="off">BTC
				  </label>
				  <label class="btn btn-secondary <%= (p.equals(PairSymbol.ETHTRY)) ? "active" : "" %>">
				    <input type="radio" name="symbol" value="ETH" <%= (p.equals(PairSymbol.ETHTRY)) ? "checked" : "" %> autocomplete="off">ETH
				  </label>
				</div>
				
				<div class="col-lg-3">
					<button type="submit" name="hourly" class="btn btn-primary<%= (tickType.equals("hourly")) ? " btn-lg" : "" %>">Saatlik</button>
					<button type="submit" name="daily" class="btn btn-danger<%= (tickType.equals("daily")) ? " btn-lg" : "" %>">Günlük</button>
				</div>
		
			</div>	
		</form>				
		
	    <form method="post" action="ticks.jsp">
			    <div class="col-2">
<%  //			<div class="form-row">
	//		      <input type="text" name="chartDate" class="form-control" placeholder="Date" value="<%= DateUtils.formatAsDate(chartDate) % >">
	//		    </div>
 
 %>			   
			    <div class="col-2">
			    </div>
			 </div>	    	
		</form>		
		
		<div id="chart_div"></div>

	</div>

<%@include file="./footer.jsp" %>