package ru.jabchat.server.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import ru.jabchat.server.mapper.UserRowMapper;
import ru.jabchat.server.model.UserModel;
import ru.jabchat.utils.Config;

public class UserDao {
	
	private static final String SELECT_ID   	 = "SELECT ID FROM VBR_IFRS.CHAT_USERS US WHERE US.IP = ?";
	private static final String SET_STATUS  	 = "UPDATE VBR_IFRS.CHAT_USERS US SET STATUS = ? WHERE US.ID = ?";
	private static final String SET_PARAM 	 	 = "UPDATE VBR_IFRS.CHAT_USERS US SET STATUS = ?, NAME = ?, COLOR = ?  WHERE US.ID = ?";
	private static final String INS_PARAM   	 = "INSERT INTO VBR_IFRS.CHAT_USERS(ID, IP, NAME, STATUS, COLOR ) VALUES (DEFAULT, ?, ?, ?, ?)";
	private static final String SELECT_ALL  	 = "SELECT * FROM VBR_IFRS.CHAT_USERS";
	private static final String SELECT_USER 	 = "SELECT * FROM VBR_IFRS.CHAT_USERS WHERE IP = ?";
	private static final String SELECT_OFF_USERS = "SELECT * FROM VBR_IFRS.CHAT_USERS WHERE status like '%off%'";
	private static final String OFF_EXIST		 = "SELECT CASE WHEN EXISTS(SELECT * FROM VBR_IFRS.CHAT_USERS WHERE STATUS LIKE '%off%') THEN 1 ELSE 0 END AS FLAG FROM SYSIBM.SYSDUMMY1";
	private static final String DEFAULT_NAME 	 = "SELECT * FROM VBR_IFRS.CHAT_USERS WHERE IP = ?";
	
	//private static final String COLOR			 = "SELECT CASE WHEN EXISTS(SELECT * FROM VBR_IFRS.CHAT_USERS WHERE STATUS LIKE '%off%') THEN 1 ELSE 0 END AS FLAG FROM SYSIBM.SYSDUMMY1";
	private static final boolean [] STATUS = {false, true};
	
	
	private JdbcTemplate jdbc;
	
	public UserDao(){
		
		BasicDataSource dataSource = Config.getDataSource();
		this.jdbc = new JdbcTemplate(dataSource);
		
		try {
			dataSource.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
	}
	
	public UserModel getUser(String ip){
		return jdbc.queryForObject(SELECT_USER, new Object[]{ip}, new UserRowMapper());
	}
	
	public List<UserModel> getOffUsers(){
		return jdbc.query(SELECT_OFF_USERS, new UserRowMapper());
		
	}
	
	@SuppressWarnings("deprecation")
	public boolean offExists(){
		int flag = jdbc.queryForInt(OFF_EXIST);
		return STATUS[flag];
		
	}
	
	@SuppressWarnings("deprecation")
	private int getId(String ip){
		return jdbc.queryForInt(SELECT_ID, ip );
	}
	
	public List<UserModel> getUsers(){
		return jdbc.query(SELECT_ALL, new UserRowMapper());
	}
	
	public UserModel login(String ip, String userName, String color){
		try{
			int id = getId(ip);
			jdbc.update(SET_PARAM, "on", userName,  color, id );
			return getUser(ip);
		}catch(org.springframework.dao.EmptyResultDataAccessException e){
			jdbc.update(INS_PARAM, ip, userName,  "on", color );
			return getUser(ip);
		}
	}
	
	public UserModel getDefaultName(String ip){
		return jdbc.queryForObject(DEFAULT_NAME, new Object[]{ip}, new UserRowMapper());
		
	}
	
	public void disconnect(UserModel user){
		jdbc.update(SET_STATUS, "off",  user.getId() );
	}
	
}