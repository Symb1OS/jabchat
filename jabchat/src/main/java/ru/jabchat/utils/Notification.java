package ru.jabchat.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;

import ru.jabchat.server.dao.UserDao;
import ru.jabchat.server.model.UserModel;

public class Notification {
	
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
			if (ip.equals("10.38.190.228")) {
				eMails.add("Vasily.Litvinenko@vwfs.com");
			} else if (ip.equals("10.38.190.227")) {
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
				email.setHostName("10.42.222.73");
				email.setSmtpPort(25);
				email.setAuthentication("test123", "passwordqqq");
				email.addTo(eMail, "");
				email.setFrom("Vasily.Litvinenko@vwfs.com");
				email.setSubject("У вас новое сообщение от " + fromUser);
				email.setMsg("Содержание сообщения: " + "\n" + message);
				email.send();
			}

		} catch (EmailException e) {
			e.printStackTrace();
		}

	}
}