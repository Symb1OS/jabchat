package ru.jabchat.server.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import ru.jabchat.server.model.UserModel;
import ru.jabchat.utils.Config;
import ru.jabchat.utils.StringCrypter;

public class UserRowMapper implements RowMapper<UserModel> {

	private StringCrypter crypter = Config.getInstance();
	
	@Override
	public UserModel mapRow(ResultSet rs, int i) throws SQLException {
		UserModel user = new UserModel();
		user.setId( rs.getInt("ID") );
		user.setIp( crypter.decrypt( rs.getString("IP")) );
		user.setUserName( crypter.decrypt( rs.getString("NAME") ) );
		user.setStatus( rs.getString("STATUS")  );
		user.setColor( rs.getInt("COLOR")  );
		return user;
	}
}
