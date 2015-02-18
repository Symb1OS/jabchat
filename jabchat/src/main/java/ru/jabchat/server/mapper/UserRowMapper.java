package ru.jabchat.server.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import ru.jabchat.server.model.UserModel;
import ru.jabchat.utils.StringCrypter;

public class UserRowMapper implements RowMapper<UserModel> {

	private StringCrypter crypter = new StringCrypter(new byte[]{1,4,5,6,8,9,7,8});
	
	@Override
	public UserModel mapRow(ResultSet rs, int i) throws SQLException {
		UserModel user = new UserModel();
		user.setId( rs.getInt("ID") );
		user.setIp( crypter.decrypt( rs.getString("IP")) );
		user.setUserName( crypter.decrypt( rs.getString("NAME") ) );
		user.setStatus( rs.getString("STATUS")  );
		return user;
	}
}
