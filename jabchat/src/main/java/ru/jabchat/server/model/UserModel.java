package ru.jabchat.server.model;

import java.sql.Timestamp;

public class UserModel {

	private Integer id;
	private String ip;
	private String userName;
	private String Status;
	
	public UserModel() {
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}

	@Override
	public String toString() {
		return "UserModel [id=" + id + ", ip=" + ip + ", userName=" + userName
				+ ", Status=" + Status + "]";
	}
	
}