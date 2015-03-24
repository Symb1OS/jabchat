package ru.jabchat.server.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import ru.jabchat.server.model.ChatModel;
import ru.jabchat.utils.Config;
import ru.jabchat.utils.StringCrypter;

public class ChatRowMapper implements RowMapper<ChatModel> {

	private StringCrypter crypter = Config.getInstance();
	
	@Override
	public ChatModel mapRow(ResultSet rs, int i) throws SQLException {
		ChatModel chat = new ChatModel();
		chat.setId(rs.getInt("ID"));
		chat.setUserName(crypter.decrypt(rs.getString("NAME")));
		chat.setMessage(crypter.decrypt(rs.getString("MESSAGE")));
		chat.setSendTime(rs.getTimestamp("SEND_TIME"));
		chat.setColor( rs.getInt("COLOR") );
		return chat;
	}
}