package ru.jabchat.utils;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

import org.apache.commons.dbcp.BasicDataSource;

public class Config {

	private static final String DRIVER_NAME   = "com.ibm.db2.jcc.DB2Driver";
	private static final String URL_DATA_BASE = "jdbc:db2://10.42.222.68:50000/FINANZ";
	private static final String USERNAME 	  = "srv_abx";
	private static final String PASSWORD   	  = "PFvPiFR@";

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
        System.setProperty("http.proxyHost", 	 "10.41.77.151");
        System.setProperty("http.proxyPort",  	 "8080");
        System.setProperty("http.proxyUser", 	 "dkx60pi");
        System.setProperty("http.proxyPassword", "699GuH691");

		Authenticator.setDefault(new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("dkx60pi", "699GuH691".toCharArray());
			}
		});
        
	}

}