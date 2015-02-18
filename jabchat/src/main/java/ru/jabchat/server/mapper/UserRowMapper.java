package ru.jabchat.server.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import ru.jabchat.server.model.UserModel;

public class UserRowMapper implements RowMapper<UserModel> {

	@Override
	public UserModel mapRow(ResultSet rs, int i) throws SQLException {
		UserModel user = new UserModel();
		user.setId( rs.getInt("ID") );
		user.setIp( rs.getString("IP") );
		user.setUserName( rs.getString("NAME") );
		user.setStatus( rs.getString("STATUS") );
		return user;
	}
}
