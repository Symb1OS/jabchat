package ru.jabchat.utils;
import org.apache.commons.dbcp.BasicDataSource;


public class Config {

	public static BasicDataSource getDataSource(){
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName("com.ibm.db2.jcc.DB2Driver");
		ds.setUrl("jdbc:db2://10.42.222.68:50000/FINANZ");
		ds.setUsername("srv_abx");
		ds.setPassword("PFvPiFR@");
		return ds;
	}
	
}