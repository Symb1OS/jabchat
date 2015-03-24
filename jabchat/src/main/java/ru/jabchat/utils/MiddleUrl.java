package ru.jabchat.utils;

import java.util.ArrayList;
import java.util.List;


public class MiddleUrl {
	
	private List<UrlPoint> points = new ArrayList<UrlPoint>();
	
	public MiddleUrl(String message){
		
		String sourceMessage = message.replaceAll("http://", "www.");
		sourceMessage = sourceMessage.replaceAll("https://", "www.");
		
		int index = 0;
		while(true){
			
			UrlPoint noUrlPoint = new UrlPoint();
			UrlPoint urlPoint = new UrlPoint();
			
			int startNoUrl  = index;
			int endNoUrl = sourceMessage.indexOf("www", index);
			
			boolean noLinks = endNoUrl == -1; 
			if( noLinks){
				endNoUrl = sourceMessage.length();
			}
			
			boolean beginNotWww = sourceMessage.indexOf("www", index) != 0; 
			if( beginNotWww ){
				noUrlPoint = new UrlPoint(startNoUrl, endNoUrl, sourceMessage.substring(startNoUrl, endNoUrl), false);
				points.add(noUrlPoint);
			}
			
			int startUrl = sourceMessage.indexOf("www", index);	
			int endUrl = sourceMessage.indexOf(" ", startUrl); 
			boolean isStartUrl = (startUrl != -1);
			boolean isEndUrl = (endUrl != -1);
			boolean isEndStr = (startUrl != -1) && (endUrl == -1); 
			if (isStartUrl && isEndUrl){
				urlPoint = new UrlPoint(startUrl, endUrl, sourceMessage.substring(startUrl, endUrl), true);
				points.add(urlPoint);
				index = endUrl;
					
			}else if ( isEndStr ) {
				urlPoint = new UrlPoint(startUrl, sourceMessage.length(), sourceMessage.substring(startUrl, sourceMessage.length()), true);
				points.add(urlPoint);
				break;
			}else {
				break;
			}
		}
		
	}
	
	public List<UrlPoint> getPoints() {
		return points;
	}

	public void setPoints(List<UrlPoint> points) {
		this.points = points;
	}

	public static boolean isUrlMiddle(String message) {
		
		boolean isUrl = ( (!message.startsWith("www") && !message.startsWith("http"))  && (message.contains("www") || message.contains("http")));
		if (isUrl){
			return true;
		}
		return false;
	}
	
	public static void main(String[] args) {
		
		String middle = "www.google.com мы ведём с собой кота asdasdasda www.google.com tratata";
		
		MiddleUrl middleUrl = new MiddleUrl(middle);
			System.out.println(middleUrl.getPoints());
			
		System.out.println("Done");
	}

}