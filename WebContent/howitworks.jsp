<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="./header.jsp" %>

	<div class="container">
		<p /> 
		<h2>Nasıl Çalışır?</h2>
	
		<p/> ALIŞ emirleri, kripto para piyasalarında tanımlayacağınız seviyelere göre otomatik oluşturulur. Gerçekleşen ALIŞ işlemleriniz, yine belirleyeceğiniz bir seviyeden SATIŞ için takibe başlar.
		<p/> Takip seviyesi yükseldikçe sizin SATIŞ fiyatınız da yükselir. Piyasa fiyatının, takip seviyesinin sizin belirleyeceğiniz kadar altına düştüğü zaman SATIŞ işlemi gerçekleşir.
		<p/> ALIŞ yönlü işlemlerle başlayarak, takip eden satış mantığı ile ALGORİTMİK işlem yapmanızı sağlar. İşlemlerinizi bir kere tanımlayın, bırakın KriptoRobo takip etsin. Detaylar için ALGO sayfasını ziyaret edin.
	
		<p/> İşlemlerinizin gerçekleşmesi için hesabınızın olduğu piyasanın ALIM-SATIM ve diğer sorgu API'lerine (borsalar tarafından sağlanan ve dış programlar ile kullanımına olanak sağlayan altyapı) sahip olması gerekmektedir.
	
	
		<h3>ALGORİTMALAR</h3>
	
		<b>Ortalamaya Göre Takip Eden Satış Algoritması (Simple Average _ Trailing Stop)</b>
	
		<p/> Çalışma mantığı: Sistemde kayıtlı saat aralıklarından sizin seçeceğiniz zaman aralığının ortalaması alınır (açılış+yüksek+düşük+kapanış / 4)
		<p/> Otomatik belirlenen bu ortalamadan sizin belirleyeceğiniz oran seviyesi kadar aşağıda ALIŞ emri oluşturur. Alış emri işlem için seçtiğiniz piyasaya gönderilir.
		<p/> Alış emrinin gerçekleşmesi durumunda, yine sizin ilk başta belirlemiş olduğunuz oran seviyesi kadar, ilk ortalamanın üzerindeki bir fiyatı TAKİP fiyatı olarak belirler.
		<p/> TAKİP fiyatına ulaşıldığında, bu fiyattan belirli oranda düşüş olması durumunda piyasa emri oluşturarak pozisyonunuzu kapatır.
		<p/> TAKİP fiyatı belirlediğiniz oranda düşmez ve yükselmeye devam ederse TAKİP fiyatınız da yükselerek, piyasayı takip eder.
		<p/> Yüzde oranı girişi:  Yüzde kaç aşağıdan alım istiyorsanız bu değeri bir (1) den çıkartarak, ortalamanın üzerinde istiyorsanız bir (1) e ekleyerek vermelisiniz.
		<p/> Ör: %2 aşağıdan alım emri istiyorsanız 0.98 (1-%2) yazmalı, %3 (1+%3) yukarıdan satış istiyorsanız 1.03 yazmalısınız.
	
	
		<h3>ÖRNEK:</h3>
		<h4>TAKİP ALGORİTMASI KULLANIM ÖNERİSİ</h4>
		<p/> <b>Piramitleyerek Alış:</b>
		<p/> Belirlediğiniz kripto paranın, her düşüşünde artan miktarlarda pozisyon alabilir, yükselişlerde yine artan miktarlarda satış gerçekleştirebilirsiniz.
		
		<p/> <b>Bu işlemi şu şekilde yapabilirsiniz:</b>
		<p/> GÜNLÜK aralıkta fiyat takip ederek, bir gün önce gerçekleşen fiyat ortalamasının (KriptoRobo'ya özel algoritma ile belirlenen)
		<p/> - %2 seviyesinde 100 TL, -%3 seviyesinde 200 TL, -%4 seviyesinde 300 TL şeklinde artan tutarlı alış emirleri oluşturabilirsiniz.
		
		<p/> Bu emirlerin TAKİP fiyatını yine ortalamaya göre +%2 seviyesine 100TL, +%3 seviyesine 200TL, +%4 seviyesine 300TL ve yukarı doğru TAKİP emirleri oluşturur,
		<p/> Bu seviyelerin %1 altına düşmesi durumunda SATIŞ gerçekleşmesini sağlayabilirsiniz.
		<p/> Piramitleme yöntemi, düşen fiyat seviyelerinden daha fazla kripto para almanızı sağlar. Aldığınız pozisyonları da yükseldikçe taşımanızı sağlar. Bu algoritmayı bir kere kaydetmeniz yeterlidir. KriotoRobo gerisini halleder.
		
	
	
		<h3>UYARI:</h3>
		<p/> İŞLEMLERİNİZİN İLGİLİ BORSAYA İLETİLMESİ İÇİN AŞAĞIDAKİLERE DİKKAT ETMENİZ GEREKLİDİR;
		<p/> API ANAHTARINIZIN DOĞRU OLDUĞUNA EMİN EDİN,
		<p/> ALIŞ İÇİN GEREKLİ BAKİYENİZİN OLDUĞUNA EMİN OLUN,
		<p/> GERÇEKLEŞEN ALIŞ SONRASI ALGONUN İSTENEN SEVİYEDEN SATIŞ YAPABİLMESİ İÇİN GEREKLİ BAKİYENİN BULUNDUĞUNA (FARKLI BİR ZAMANDA SATIŞ İŞLEMİ YAPMADIĞINIZA) DİKKAT EDİN,
		<p/> ALGORİTMA TARAFINDAN OLUŞTURULAN ALIŞ EMRİNİN BORSAYA İLETİLDİĞİNİ İŞLEM YAPITIĞINIZ BORSANIN SİTESİNDEN KONTROL EDİN.

	</div>
	

<%@include file="./footer.jsp" %>		