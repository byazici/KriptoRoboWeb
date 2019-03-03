<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="./header.jsp" %>

	<div class="container">
		<p />
		<div class="card">
			  <h4 class="card-header">Version History (<%= AlgoServer.appVersion %>)</h4>

			  <div class="card-body">
			  	<h5 class="card-title">v0.9.2/16.09.2018</h5>
				<p class="card-text"> 
					<ul>
						<li>Detaylı algo giriş ekranı eklendi.</li>
						<li>Türkçe dil düzdltmeleri yapıldı.</li>
					</ul>
				</p>
			  </div>

			  <div class="card-body">
			  	<h5 class="card-title">v0.9.1/13.09.2018</h5>
				<p class="card-text"> 
					<ul>
						<li>Hata giderme.</li>
					</ul>
				</p>
			  </div>

			  <div class="card-body">
			  	<h5 class="card-title">v0.9/06.09.2018</h5>
				<p class="card-text"> 
					<ul>
						<li>SSL desteği eklendi.</li>
					</ul>
				</p>
			  </div>

			  <div class="card-body">
			  	<h5 class="card-title">v0.8</h5>
				<p class="card-text"> 
					<ul>
						<li>Başarılı üye girişinde bilgi e-postası gönderimi eklendi.</li>
						<li>Hatalı üye girişleri için yönetici uyarıları eklendi.</li>
					</ul>
				</p>
			  </div>

			  <div class="card-body">
			  	<h5 class="card-title">v0.7</h5>
				<p class="card-text"> 
					<ul>
						<li>Hatalar giderildi.</li>
						<li>Bilgi e-postaları iyileştirildi.</li>
					</ul>
				</p>
			  </div>

			  <div class="card-body">
			  	<h5 class="card-title">v0.6</h5>
				<p class="card-text"> 
					<ul>
						<li>Giriş ekranına istatistikler eklendi.</li>
						<li>Açık algo limiti ve açık tutar limiti eklendi.</li>
						<li>Hata giderme.</li>
						<li>SSL desteği eklendi</li>
						<li>İletişim formu eklendi.</li>
					</ul>
				</p>
			  </div>

			  <div class="card-body">
			  	<h5 class="card-title">v0.5</h5>
				<p class="card-text"> 
					<ul>
						<li>Üyelik fonksiyonu geliştirildi.</li>
						<li>Şifre değiştirme fonksiyonu geliştirildi.</li>
						<li>Giriş sayfasına ek bilgiler eklendi.</li>
						<li>Algo listesine MTM (Mark to market) kolonu eklendi.</li>
						<li>Miktar gösterim hatası giderildi (null)</li>
						<li>Sell Wait durumundaki algonun iptal hatası giderildi.</li>
						
					</ul>
				</p>
			  </div>

			  <div class="card-body">
			  	<h5 class="card-title">v0.4</h5>
				<p class="card-text"> 
					<ul>
						<li>Password change support</li>
						<li>Version history added</li>
						<li>Algo and order list order changed to desc</li>
						<li>Balance daily cal error solved.</li>
						<li>Trigger bekleyen emirler silinebilir yapıldı.</li>
						<li>Güçlü şifre algoritması güncellendi</li>
						<li>Recaptcha desteği eklendi.</li>
						<li>Algo listesi yenilendi</li>
						<li>Türkçe çevirileri yapıdlı</li>
						<li>Cüzdan ve hesap özeti menüleri sağdaki kişisel menüye taşındı.</li>
					</ul>
				</p>
			  </div>
			  
			  <div class="card-body">
			  	<h5 class="card-title">v0.3</h5>
				<p class="card-text"> 
					<ul>
						<li>User login support</li>
						<li>Live ticks menu added</li>
						<li>Dashboard algo text added</li>
					</ul>
				</p>
			  </div>
		</div>	


	</div>
	

<%@include file="./footer.jsp" %>		