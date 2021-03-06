package ru.jabchat.server.model;

import java.sql.Timestamp;

public class ChatModel {

	private Integer id;
	private String userName;
	private String message;
	private Timestamp sendTime;
	private int color; 
	
	public ChatModel() {

	}

	public ChatModel(String userName, String message, Timestamp sendTime) {
		this.userName = userName;
		this.message = message;
		this.sendTime = sendTime;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Timestamp getSendTime() {
		return sendTime;
	}

	public void setSendTime(Timestamp sendTime) {
		this.sendTime = sendTime;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}
	
	@Override
	public String toString() {
		return "ChatModel [id=" + id + ", userName=" + userName + ", message="
				+ message + ", sendTime=" + sendTime + "]";
	}
}