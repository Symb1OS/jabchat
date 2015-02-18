package ru.jabchat.server.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import ru.jabchat.server.mapper.ChatRowMapper;
import ru.jabchat.server.model.ChatModel;
import ru.jabchat.utils.Config;

public class ChatDao {

	private static final String INSERT = "INSERT INTO VBR_IFRS.CHAT(ID, NAME, MESSAGE, SEND_TIME) VALUES (DEFAULT, ?, ?, ?)";
	private static final String SELECT_ALL = "SELECT * FROM VBR_IFRS.CHAT WHERE ID > ? ORDER BY SEND_TIME";
	private static final String COUNT_ROWS_LOGIN = "SELECT MAX(ID)  as ID FROM VBR_IFRS.CHAT";
	
	private JdbcTemplate jdbc;
	
	public ChatDao(){
		
		BasicDataSource dataSource = Config.getDataSource();
		jdbc = new JdbcTemplate(dataSource);
		
		try {
			dataSource.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void insertMessage(ChatModel chatModel){
		jdbc.update(INSERT, chatModel.getUserName(), chatModel.getMessage(), chatModel.getSendTime());
	}
	
	@SuppressWarnings("deprecation")
	public int getLastRow(){
		return jdbc.queryForInt(COUNT_ROWS_LOGIN);
	}
	
	public List<ChatModel> getListMessages(Integer id){
		return jdbc.query(SELECT_ALL, new Object[]{id}, new ChatRowMapper());
		
	}
	
}