package ru.jabchat.utils;

import org.apache.commons.dbcp.BasicDataSource;

public class Config {

	private static final String DRIVER_NAME = "com.ibm.db2.jcc.DB2Driver";
	private static final String URL_DATA_BASE = "jdbc:db2://10.42.222.68:50000/FINANZ";
	private static final String USERNAME = "srv_abx";
	private static final String PASSWORD = "PFvPiFR@";

	public static BasicDataSource getDataSource() {
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName(DRIVER_NAME);
		ds.setUrl(URL_DATA_BASE);
		ds.setUsername(USERNAME);
		ds.setPassword(PASSWORD);
		return ds;
	}

}