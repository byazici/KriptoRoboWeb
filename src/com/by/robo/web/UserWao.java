package com.by.robo.web;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.by.robo.cache.UserCache;
import com.by.robo.dao.UserDao;
import com.by.robo.dao.UserTokenDao;
import com.by.robo.enums.TokenStatus;
import com.by.robo.enums.UserRole;
import com.by.robo.enums.UserStatus;
import com.by.robo.helper.BtcTurkHelper;
import com.by.robo.helper.UserHelper;
import com.by.robo.model.Result;
import com.by.robo.model.User;
import com.by.robo.model.UserStats;
import com.by.robo.model.UserToken;
import com.by.robo.utils.DateUtils;
import com.by.robo.utils.MailUtils;
import com.by.robo.utils.PasswordUtil;
import com.by.robo.utils.RandomString;
import com.by.robo.utils.RecaptchaUtil;
import com.by.robo.utils.TcknUtils;
import com.by.robo.utils.WebUtils;

public class UserWao {
	private final static Logger logger = LoggerFactory.getLogger(UserWao.class);
	private final static BigDecimal maxAmt = new BigDecimal(10000);
	private final static int maxAlgo = 5;
	private final static String PASSWORD_REGEXP = 
			"^" 
			+ "(?=.*\\d)"
			+ "(?=.*[a-z])"
			+ "(?=.*[A-Z])"
			+ "(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?])"
			+ "."
			+ "{8,}"
			+ "$";
	
	
	public static User getUser(int userId) {
		return UserDao.getUser(userId);
	}
	
	public static Result saveApiKeys(User user, String publicKey, String privateKey) {
		Result result = new Result(false);
		try {
			// TODO check if this keys used for another user!
			
			// try new key
			if (!new BtcTurkHelper().testKeys(user.getId(), publicKey, privateKey)) {
				result.setErrorCode("10");
				result.setErrorMsg("Api keys are invalid!");
			} else {
				if (!UserDao.updateKeys(user.getId(), publicKey, privateKey)) {
					result.setErrorCode("20");
					result.setErrorMsg("Error saving keys!");
				} else {
					UserCache.updateUser(user.getId());
					result.setSuccess(true);
				};
			}

		} catch (Exception e) {
			result.setErrorCode("-1");
			result.setErrorMsg(e.getMessage());
		}

		return result;
	}
	
	public static Result setPassword(String token, String recaptcha, String newPass, String newPass2) {
		Result result = new Result(true);
		UserToken userToken = null;
		UserHelper helper = new UserHelper();
		
		
		// 1. recaptcha kontrolü
		if (!RecaptchaUtil.verify(recaptcha)) {
			result.setErrorMsg("Lütfen 'ben robot değilim' seçeneğini işaretleyiniz!");
			result.setSuccess(false);
		}

		// 2. token kontrolü
		if (result.isSuccess()) {
			userToken = UserTokenDao.getActiveToken(token);
			if (!UserTokenDao.updateStatus(userToken.getId(), TokenStatus.READ)) {
				result.setErrorMsg("Genel bir hata oluştu!");
				result.setSuccess(false);
			}
		}
		
		// 3. şifre kontrolü
		if (result.isSuccess()) {
			if (newPass == null || newPass.length() == 0 || newPass2 == null || newPass2.length() == 0 ) {
				result.setSuccess(false);
				result.setErrorMsg("Lütfen zorunlu alanları doldurunuz.");				
			} else if (! newPass.equals(newPass2)) {
				result.setSuccess(false);
				result.setErrorMsg("Girdiğiniz şifreler aynı değil !");				
			} else if (!newPass.matches(PASSWORD_REGEXP)) { 
				result.setSuccess(false);
				result.setErrorMsg("Lütfen şifre kurallarına uygun bir şifre belirleyin!");		
			} else if (!helper.updatePassword(userToken.getUserId(), newPass)) {
				result.setSuccess(false);
				result.setErrorMsg("Şifre tanımlanamadı !");
			}
		}
		
		// 4. token güncelle
		if (result.isSuccess()) {
			if (!UserTokenDao.updateStatus(userToken.getId(), TokenStatus.DONE)) {
				result.setErrorMsg("Genel bir hata oluştu!");
				result.setSuccess(false);
			}
		}
		
		
		// 5. kullanıcıyı aç
		if (result.isSuccess()) {
			// kullanıcıyı aktif hale getir.
			if (! UserDao.updateStatus(userToken.getUserId(), UserStatus.ACTIVE)) {
				result.setErrorMsg("Genel bir hata oluştu!");
				result.setSuccess(false);
			}
			
			// eğer deneme sayısı aşıldıysa onu da sıfırla
			if (! UserDao.resetWrongLogin(userToken.getUserId())) {
				result.setErrorMsg("Genel bir hata oluştu!");
				result.setSuccess(false);
			}
		}		


		if (result.isSuccess()) {
			result.setErrorMsg("Yeni şifre belirlendi.");
			result.setSuccess(true);
		}

		return result;
	}

	public static Result updatePassword(User user, String oldPass, String newPass, String newPass2) {
		Result result = new Result(false);
		User userDb = UserDao.getUser(user.getId());
		UserHelper helper = new UserHelper();

		try {
			if (userDb == null) {
				result.setErrorCode("10");
				result.setErrorMsg("Kullanıcı bulunamadı!");				
			} else if (oldPass == null || oldPass.length() == 0 
					|| newPass == null || newPass.length() == 0 
					|| newPass2 == null || newPass2.length() == 0 ) {
				result.setErrorCode("20");
				result.setErrorMsg("Lütfen gerekli alanları doldurunuz!");				
			} else if (! newPass.equals(newPass2)) {
				result.setErrorCode("30");
				result.setErrorMsg("Yeni şifreler uyuşmuyor!");				
			} else if (! PasswordUtil.verifyPassword(oldPass, userDb.getUserPass()))  {
				result.setErrorCode("50");
				result.setErrorMsg("Eski şifre uyuşmuyor!");				
			} else if (!newPass.matches(PASSWORD_REGEXP)) { 
				result.setErrorCode("40");
				result.setErrorMsg("Lütfen güçlü bir şifre belirleyiniz!");		
			} else if (!helper.updatePassword(user.getId(), newPass)) {
				result.setErrorCode("60");
				result.setErrorMsg("Şifre kaydedilemedi!");
			} else {
				// UserCache.updateUser(user.getId());  gerek yok.
				result.setErrorCode("0");
				result.setSuccess(true);
				MailUtils.sendEmailToUser(user.getId(), "Güvenlik Uyarısı", "Şifreniz değiştirildi. Eğer bunu siz yaptıysanız bu bilgilendirmeyi dikkate almayınız. Eğer siz değilseniz derhal şifrenizi değiştirin.");
			}

		} catch (Exception e) {
			result.setErrorCode("-1");
			result.setErrorMsg(e.getMessage());
		}
		
		return result;		
	}
	
	public static Result registerUser(String mail, String tckn, String first, String last, String year, String recaptcha, String policy) {
		Result result = new Result(false);
		User user = new User();
		UserTokenDao dao = new UserTokenDao();
		UserToken ut = new UserToken();
		
		// TODO: yeni başvuruyle gelen tckn içeride daha önceden varsa ne olacak? Şimdilik izin veriliyor.
		
		try {
			user.setUserMail(mail);
			user.setTckn(tckn);
			user.setFirstName(first);
			user.setLastName(last);
			user.setBirthYear(year);
			user.setRoles(UserRole.CUSTOMER.getValue());
			user.setStatus(UserStatus.APPROVE);
			user.setMaxAlgo(maxAlgo);
			user.setMaxAmt(maxAmt);
			User oldUser = UserDao.getUserByMail(mail);

			if (!RecaptchaUtil.verify(recaptcha)) {
				// 1. recaptcha kontrolü
				result.setErrorMsg("Lütfen 'ben robot değilim' seçeneğini işaretleyiniz!");

			} else if (WebUtils.hasAnyEmpty(mail,tckn,first,last,year)) {
				// 2 form kontrol
				result.setErrorMsg("Lütfen tüm bilgileri doldurunuz!");
				
			} else if (policy == null) {
				// 3 policy kontrol
				result.setErrorMsg("Lütfen 'kullanım sözleşmesi' seçeneğini işaretleyiniz!");

			} else if (!year.matches("^[0-9]{4}$")) {
				// 4 policy kontrol
				result.setErrorMsg("Doğum yılınızı 4 haneli olarak giriniz!");

			} else if (DateUtils.getYearDiff(Integer.parseInt(year)) < 18) {
				// 5 policy kontrol
				result.setErrorMsg("Sitemize üye olabilmeniz için 18 yaşınızı tamamlamış olmalısınız.");

			} else if (!TcknUtils.TcKimlikNoSorgula(Long.parseLong(tckn), first, last, Integer.parseInt(year))){
				// 6. Tckn kontrolü
				result.setErrorMsg("Tc kimlik no, adınız ve soyadınız https://www.nvi.gov.tr üzerinden kontrol edileceğinden tüm bilgilerin doğru olması gerekmektedir.");

			} else if (oldUser != null && oldUser.getStatus() != UserStatus.APPROVE) {
				// 7. e-mail önceden kullanılmış mı?
				
				// eğer yeni kayıt olmak isteyen kişiniin maili önceden var ve TCKN uuyorsa
				if (oldUser.getTckn() == tckn) {
					String body = "Merhaba, \n";
					body += "http://www.kriptorobo.com/ web sitemizden yeni üyelik için bir başvuru yapıldı. \n";
					body += "Fakat girilen e-postaya bağlı bir mevcut hesap bulunuyor. \n";
					body += "Hesabınızı tekrar kullanabilmek için şifrenizi biliyorsanız giriş yapın, bilmiyorsanız şifre sıfırlama adımını takip ediniz. \n";
					body += "\n\n Şifre sıfırlamak için; http://www.kriptorobo.com/KriptoRoboWeb/passReset.jsp linkini kullanabilirsiniz. \n";
					MailUtils.sendEmail(oldUser.getUserMail(), "Kriptorobo Üyelik Uyarısı", body);			// TODO send şifre reset mail
				} else {
					String body = "Merhaba, \n";
					body += "http://www.kriptorobo.com/ web sitemizden yeni üyelik için bir başvuru yapıldı. \n";
					body += "Fakat girilen e-postaya bağlı bir mevcut hesap bulunuyor. \n";
					body += "Başvuru yaptığınız Tc Kimlik No ile mevcut hesabın Tc Kimlik No bilgileri aynı olmadığı için devam edemiyoruz.  \n";
					body += "Ya bu e-posta adresine ait kimlik nosuyla başvuru yapmanız veya başka bir mail adresiyle başvurmanız gerekmektedir.";
					MailUtils.sendEmail(oldUser.getUserMail(), "Kriptorobo Üyelik Uyarısı", body);
				}
				

			} else if (DateUtils.getYearDiff(Integer.valueOf(year)) < 18) {
				// 5. 18 yaş kontrolü
				result.setErrorMsg("18 yaşından küçüklerin üyeliğini kabul edemiyoruz!");
				
			} else {
				// 7. Kaydet
				boolean bool = false;
				int userId = 0;
				if (oldUser != null) {
					bool = UserDao.updateStatus(oldUser.getId(), UserStatus.APPROVE);
				} else {
					userId = UserDao.insertUser(user);
					bool = true;
				}

				ut.setUserId(userId);
				ut.setToken(new RandomString(64).nextString());
				ut.setStatus(TokenStatus.NEW);				
								
				if (bool && dao.insertUpdateToken(ut)) {
					String body = "Merhaba, \n";
					body += "https://www.kriptorobo.com/ sitemizden " + user.getUserMail() + " adresiyle bir başvuru yapıldı. \n";
					body += "Üyeliğinizi aktif hale getirmek aşağıdaki linki tıklayarak devam edebilirsiniz \n";
					body += "\n";
					body += "Aktivasyon linki: https://www.kriptorobo.com/KriptoRoboWeb/password.jsp?t=" + ut.getToken() +" \n";
					MailUtils.sendEmail(user.getUserMail(), "Kriptorobo E-posta Aktivasyonu", body);	

					result.setSuccess(true);
					result.setErrorMsg("Üye kaydı başarıyla tamamlandı.");						
				} else {
					result.setErrorMsg("Bir hata oluştu. İlgileniyoruz, lütfen daha sonra tekrar deneyiniz.");
				}
			}
						
		} catch (Exception e) {
			logger.error("error here", e);
			result.setSuccess(false);
			result.setErrorMsg("Bir hata oluştu. İlgileniyoruz, lütfen daha sonra tekrar deneyiniz.");
		}

		return result;	
	}

	public static Result forgotPassword(String mail, String recaptcha) {
		Result result = new Result(true);
		User user = null;
		UserTokenDao dao = new UserTokenDao();
		UserToken ut = new UserToken();
		
		try {

			// 1. recaptcha kontrolü
			if (!RecaptchaUtil.verify(recaptcha)) {
				result.setErrorMsg("Lütfen 'ben robot değilim' seçeneğini işaretleyiniz!");
				result.setSuccess(false);
			}

			// 2. input kontrol
			if (result.isSuccess()) {
				 if (WebUtils.hasAnyEmpty(mail)) {
					result.setErrorMsg("Lütfen üye olduğunuz e-posta adresinizi giriniz!");
					result.setSuccess(false);
				 }
			}
			
			// 2. mail kontrol
			if (result.isSuccess()) {
				user = UserDao.getUserByMail(mail);
				 if (user != null) {
					ut.setUserId(user.getId());
					ut.setToken(new RandomString(64).nextString());
					ut.setStatus(TokenStatus.NEW);		
					
					if (!dao.insertUpdateToken(ut)) {
						result.setErrorMsg("Genel bir hata oluştu.");
						result.setSuccess(false);
					} else {
						String body = "Merhaba, \n";
						body += "http://www.kriptorobo.com/ sitemizden " + user.getUserMail() + " adresiyle şifre yenilemesi için başvuru yapıldı. \n";
						body += "Yeni şifrenizi belirlemek için aşağıdaki linki tıklayarak devam edebilirsiniz \n";
						body += "\n";
						body += "Aktivasyon linki: http://www.kriptorobo.com/KriptoRoboWeb/password.jsp?t=" + ut.getToken() +" \n";
						MailUtils.sendEmail(user.getUserMail(), "Kriptorobo Şifre Yenileme", body);	
					}
				 } else {
					// kötü niyete karşı bulamadık demiyoruz.
				 }
			}
			
			// 3. token oluştur
			if (result.isSuccess()) {
				result.setErrorMsg("Şifre oluşturma talebi alındı.");						
				result.setSuccess(true); 
			}
			
		} catch (Exception e) {
			logger.error("error here", e);
			result.setSuccess(false);
			result.setErrorMsg("Bir hata oluştu. İlgileniyoruz, lütfen daha sonra tekrar deneyiniz.");
		}

		return result;	
	}
	
	public static UserStats getUserStats(User user) {
		return UserDao.getUserStats(user);
	}
}
