package ru.jabchat.server;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import ru.jabchat.utils.StringCrypter;

public class ChatRowMapper implements RowMapper<ChatModel> {

	private StringCrypter crypter = new StringCrypter(new byte[]{1,4,5,6,8,9,7,8});
	
	@Override
	public ChatModel mapRow(ResultSet rs, int i) throws SQLException {
		ChatModel chat = new ChatModel();
		chat.setId(rs.getInt("ID"));
		chat.setUserName(crypter.decrypt(rs.getString("NAME")));
		chat.setMessage(crypter.decrypt(rs.getString("MESSAGE")));
		chat.setSendTime(rs.getTimestamp("SEND_TIME"));
		
		return chat;
	}
}