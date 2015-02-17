package ru.jabchat;

import java.util.List;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public class ChatDao {

	private static final String INSERT = "INSERT INTO VBR_IFRS.CHAT(ID, NAME, MESSAGE, SEND_TIME) VALUES (DEFAULT, ?, ?, ?)";
	
	private static final String SELECT_ALL = "SELECT * FROM VBR_IFRS.CHAT WHERE ID > ? ORDER BY SEND_TIME";
	
	private static final String COUNT_ROWS_LOGIN = "SELECT MAX(ID)  as ID FROM VBR_IFRS.CHAT";
	
	private JdbcTemplate jdbc;
	
	private StringCrypter crypter;
	
	public ChatDao(){
		
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName("com.ibm.db2.jcc.DB2Driver");
		ds.setUrl("jdbc:db2://10.42.222.68:50000/FINANZ");
		ds.setUsername("srv_abx");
		ds.setPassword("PFvPiFR@");
		
		jdbc = new JdbcTemplate(ds);
		crypter = new StringCrypter(new byte[]{1,4,5,6,8,9,7,8});
	}
	
	public void insertMessage(ChatModel chatModel){
		jdbc.update(INSERT,chatModel.getUserName(), chatModel.getMessage(), chatModel.getSendTime());
	}
	
	public int getLastRow(){
		return jdbc.queryForInt(COUNT_ROWS_LOGIN);
	}
	
	public List<ChatModel> getListMessages(Integer id){
		return jdbc.query(SELECT_ALL, new Object[]{id}, new ChatRowMapper());
		
	}
	
}