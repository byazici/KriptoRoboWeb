<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="./header.jsp" %>

		<div class="card">

				<!-- TradingView Widget BEGIN -->
				<script type="text/javascript" src="https://s3.tradingview.com/tv.js"></script>
				<script type="text/javascript">
				new TradingView.widget({
				  "width": 980,
				  "height": 610,
				  "symbol": "BITFINEX:BTCUSD *USDTRY",
				  "interval": "1",
				  "timezone": "Europe/Istanbul",
				  "theme": "Light",
				  "style": "1",
				  "locale": "en",
				  "toolbar_bg": "#f1f3f6",
				  "enable_publishing": false,
				  "allow_symbol_change": true,
				  "hideideas": true
				});
				</script>
				<!-- TradingView Widget END -->
	

<%@include file="./footer.jsp" %>