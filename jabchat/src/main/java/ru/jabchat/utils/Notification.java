package ru.jabchat.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;

import ru.jabchat.server.dao.UserDao;
import ru.jabchat.server.model.UserModel;

public class Notification {
	
	private static final String MAIL_HOSTNAME = "10.42.222.73";
	private static final int 	MAIL_PORT 	  =  25; 
	private static final String MAIL_LOGIN 	  = "test123";
	private static final String MAIL_PASSWORD = "passwordqqq";
	private static final String MAIL_FROM	  = "Vasily.Litvinenko@vwfs.com";
	
	private static final String IP_V 		  = "10.38.190.228";
	private static final String IP_F		  = "10.38.190.227";

	private String fromUser;
	
	private String message;
	
	private UserDao dao = new UserDao();
	
	private List<String> eMails;
	
	public Notification(String fromUser, String message) {

		this.eMails = new ArrayList<String>();
		this.fromUser = fromUser;
		this.message = message;
		
		List<UserModel> offUsers = dao.getOffUsers();
		for (UserModel user : offUsers) {
		
			String ip = user.getIp();
			if (ip.equals(IP_V)) {
				eMails.add("Vasily.Litvinenko@vwfs.com");
			} else if (ip.equals(IP_F)) {
				eMails.add("Fedor.Murashko@vwfs.com");
			}
			else {
				System.out.println("АНАЛИТИКА СЛАМАЛАСЬ((");
			}
		}
	}
	
	public void sendMail() {

		try {
			
			for (String eMail : eMails) {

				MultiPartEmail email = new MultiPartEmail();
				email.setHostName(MAIL_HOSTNAME);
				email.setSmtpPort(MAIL_PORT);
				email.setAuthentication(MAIL_LOGIN, MAIL_PASSWORD);
				email.addTo(eMail, "");
				email.setFrom(MAIL_FROM);
				email.setSubject("У вас новое сообщение от " + fromUser);
				email.setMsg("Содержание сообщения: " + "\n" + message);
				email.send();
			}

		} catch (EmailException e) {
			e.printStackTrace();
		}

	}
}