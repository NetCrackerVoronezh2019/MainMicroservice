package com.mainmicroservice.mainmicroservice.Services;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailService {
	
	@Autowired
	private JavaMailSender mailSender;
	
	public void SendMessage(String subject, String text, String to,String name) throws MessagingException
	{
		/*
		SimpleMailMessage message=new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(subject);
		message.setText("<h1>"+text+"</h1>");
		mailSender.send(message);
		*/
		 MimeMessage message = mailSender.createMimeMessage();
		 
	        boolean multipart = true;
	         
	        MimeMessageHelper helper = new MimeMessageHelper(message, multipart, "UTF-8");
	        String htmlMsg = "<h3>Здравствуйте "+name+" !</h3>"+
	        "Для подтверждения регистрации аккаунта перейдите по "+
	        		"<a href="+text+">ccылке</a>";
	               
	         
	        message.setContent(htmlMsg, "text/html; charset=UTF-8");
	         
	        helper.setTo(to);
	         
	        helper.setSubject(subject);
	         
	     
	        this.mailSender.send(message);
	 
	}

}
