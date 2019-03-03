package com.by.robo.utils;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.by.robo.dao.UserDao;
import com.by.robo.model.User;

public class MailUtils {
	final static Logger logger = LoggerFactory.getLogger(MailUtils.class);

	static final String fromEmail = "hello@kriptorobo.com"; 
	static final String password = "3N+NBE_Z#5}2";
	private static final String adminMain = "hello@kriptorobo.com";

	public static void main(String[] args) {
		  sendEmail("burcinyazici@gmail.com","SSLEmail Testing Subject", "<h1>SSLEmail Testing Body</h1>");
	}

	public static void sendEmailToUser(int userId, String subject, String body) {
		User user = UserDao.getUser(userId);
		if (user == null) {
			logger.error("User not found to send mail! UserId : " + userId);
		} else {
			sendEmail(user.getUserMail(), subject, body);
		}
	}
	
	public static void sendEmailToAdmin(String subject, String body) {
		sendEmail(adminMain, subject, body);
	}
	
	public static void sendEmail(String toEmail, String subject, String body) {
		Properties props = new Properties();
		props.put("mail.smtp.host", "mail.kriptorobo.com"); //SMTP Host
		props.put("mail.smtp.socketFactory.port", "465"); //SSL Port
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); //SSL Factory Class
		props.put("mail.smtp.auth", "true"); //Enabling SMTP Authentication
		props.put("mail.smtp.port", "465"); //SMTP Port
		
		Authenticator auth = new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(fromEmail, password);
			}	
		};
		
		Session session = Session.getDefaultInstance(props, auth);
        sendEmail(session, toEmail,subject, body);		
	}

	@SuppressWarnings("unused")
	private static void sendAttachmentEmail(Session session, String toEmail, String subject, String body){
		try{
	         MimeMessage msg = new MimeMessage(session);
	         msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
		     msg.addHeader("format", "flowed");
		     msg.addHeader("Content-Transfer-Encoding", "8bit");
		      
		     msg.setFrom(new InternetAddress("no_reply@journaldev.com", "NoReply-JD"));

		     msg.setReplyTo(InternetAddress.parse("no_reply@journaldev.com", false));

		     msg.setSubject(subject, "UTF-8");

		     msg.setSentDate(new Date());

		     msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
		      
	         // Create the message body part
	         BodyPart messageBodyPart = new MimeBodyPart();

	         // Fill the message
	         messageBodyPart.setText(body);
	         
	         // Create a multipart message for attachment
	         Multipart multipart = new MimeMultipart();

	         // Set text message part
	         multipart.addBodyPart(messageBodyPart);

	         // Second part is attachment
	         messageBodyPart = new MimeBodyPart();
	         String filename = "abc.txt";
	         DataSource source = new FileDataSource(filename);
	         messageBodyPart.setDataHandler(new DataHandler(source));
	         messageBodyPart.setFileName(filename);
	         multipart.addBodyPart(messageBodyPart);

	         // Send the complete message parts
	         msg.setContent(multipart);

	         // Send message
	         Transport.send(msg);
	         System.out.println("EMail Sent Successfully with attachment!!");
	      }catch (MessagingException e) {
	         e.printStackTrace();
	      } catch (UnsupportedEncodingException e) {
			 e.printStackTrace();
	      }
	}

	/**
	 * Utility method to send simple HTML email
	 * @param session
	 * @param toEmail
	 * @param subject
	 * @param body
	 */
	private static void sendEmail(Session session, String toEmail, String subject, String body){
		try
	    {
		  MimeMessage msg = new MimeMessage(session);
		  //set message headers
		  msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
		  msg.addHeader("format", "flowed");
		  msg.addHeader("Content-Transfer-Encoding", "8bit");
		
		  msg.setFrom(new InternetAddress("hello@kriptorobo.com", "KriptoRobo.com Alert"));
		  msg.setReplyTo(InternetAddress.parse("hello@kriptorobo.com", false));
		  msg.setSubject(subject, "UTF-8");
		  msg.setContent(body, "text/html; charset=utf-8");
		  //msg.setText(body, "UTF-8");
		  msg.setSentDate(new Date());
		
		  msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
		  //System.out.println("Message is ready");
		  Transport.send(msg);  
		
		  //System.out.println("EMail Sent Successfully!!");
	    }
	    catch (Exception e) {
	      e.printStackTrace();
	    }
	}

	@SuppressWarnings("unused")
	private static void sendImageEmail(Session session, String toEmail, String subject, String body){
		try{
	         MimeMessage msg = new MimeMessage(session);
	         msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
		     msg.addHeader("format", "flowed");
		     msg.addHeader("Content-Transfer-Encoding", "8bit");
		      
		     msg.setFrom(new InternetAddress("no_reply@journaldev.com", "NoReply-JD"));

		     msg.setReplyTo(InternetAddress.parse("no_reply@journaldev.com", false));

		     msg.setSubject(subject, "UTF-8");

		     msg.setSentDate(new Date());

		     msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
		      
	         // Create the message body part
	         BodyPart messageBodyPart = new MimeBodyPart();

	         messageBodyPart.setText(body);
	         
	         // Create a multipart message for attachment
	         Multipart multipart = new MimeMultipart();

	         // Set text message part
	         multipart.addBodyPart(messageBodyPart);

	         // Second part is image attachment
	         messageBodyPart = new MimeBodyPart();
	         String filename = "image.png";
	         DataSource source = new FileDataSource(filename);
	         messageBodyPart.setDataHandler(new DataHandler(source));
	         messageBodyPart.setFileName(filename);
	         //Trick is to add the content-id header here
	         messageBodyPart.setHeader("Content-ID", "image_id");
	         multipart.addBodyPart(messageBodyPart);

	         //third part for displaying image in the email body
	         messageBodyPart = new MimeBodyPart();
	         messageBodyPart.setContent("<h1>Attached Image</h1>" +
	        		     "<img src='cid:image_id'>", "text/html");
	         multipart.addBodyPart(messageBodyPart);
	         
	         //Set the multipart message to the email message
	         msg.setContent(multipart);

	         // Send message
	         Transport.send(msg);
	         System.out.println("EMail Sent Successfully with image!!");
	      }catch (MessagingException e) {
	         e.printStackTrace();
	      } catch (UnsupportedEncodingException e) {
			 e.printStackTrace();
		}
	}
}
