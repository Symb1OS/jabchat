package ru.jabchat.utils;

public class UrlPoint{
	
	private String message;
	
	private int start;
	private int end;
	
	private boolean isUrl;
	
	public UrlPoint(){
		
	}

	public UrlPoint(int start, int end, String message, boolean isUrl){
		this.start = start;
		this.end = end;
		this.message = message;
		this.isUrl = isUrl;
	}
	
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public boolean isUrl() {
		return isUrl;
	}
	public void setUrl(boolean isUrl) {
		this.isUrl = isUrl;
	}
	@Override
	public String toString() {
		return "UrlPoint [message=" + message + ", start=" + start + ", end="
				+ end + ", isUrl=" + isUrl + "]";
	}
}