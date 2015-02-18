package ru.jabchat.server.model;

import java.sql.Timestamp;

public class UserModel {

	private int id;
	private String ip;
	private String userName;
	private String status;
	
	public UserModel(){}
	
	public UserModel(int id, String ip, String userName, String status) {
		this.id 		= id;
		this.ip 		= ip;
		this.userName 	= userName;
		this.status 	= status;
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
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "UserModel [id=" + id + ", ip=" + ip + ", userName=" + userName
				+ ", Status=" + status + "]";
	}
	
}