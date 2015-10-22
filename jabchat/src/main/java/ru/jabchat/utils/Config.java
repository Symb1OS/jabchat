package ru.jabchat.utils;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

import org.apache.commons.dbcp.BasicDataSource;

public class Config {

	private static final String DRIVER_NAME   = "";
	private static final String URL_DATA_BASE = "";
	private static final String USERNAME 	  = "";
	private static final String PASSWORD   	  = "@";

	public static BasicDataSource getDataSource() {
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName(DRIVER_NAME);
		ds.setUrl(URL_DATA_BASE);
		ds.setUsername(USERNAME);
		ds.setPassword(PASSWORD);
		return ds;
	}
	
	public static void setProxy() {

    	System.setProperty("java.net.useSystemProxies", "true");
        System.setProperty("http.proxyHost", 	 	    "");
        System.setProperty("http.proxyPort",  			"");
        System.setProperty("http.proxyUser", 	 		"");
        System.setProperty("http.proxyPassword", 		"");

		Authenticator.setDefault(new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("", "".toCharArray());
			}
		});
        
	}
	
	public static StringCrypter getInstance(){
		return new StringCrypter(new byte[]{1,2,5,6,8,9,7,8});
	}

}
