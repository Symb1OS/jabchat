package ru.jabchat.utils;

import java.util.ArrayList;
import java.util.List;


public class MiddleUrl {
	
	private List<UrlPoint> points = new ArrayList<UrlPoint>();
	
	public MiddleUrl(String message){
		
		String tempMessage = message.replaceAll("http://", "www.");
		tempMessage = tempMessage.replaceAll("https://", "www.");
		
		int stuck = 0;
		int index = 0;
		while(true){
			//System.out.println("=============================================");
			stuck++;
			if (stuck > 5) {
			//	System.out.println("ЗАСТРЯЛИ!");
				break;
			}
		
			UrlPoint noUrlPoint = new UrlPoint();
			UrlPoint urlPoint = new UrlPoint();
			
			int startNoUrl  = index;
			int endNoUrl = tempMessage.indexOf("www", index);
			
			if(endNoUrl == -1){
				endNoUrl = tempMessage.length();
			}
			
			if(tempMessage.indexOf("www", index) != 0){
				noUrlPoint = new UrlPoint(startNoUrl, endNoUrl, tempMessage.substring(startNoUrl, endNoUrl), false);
			//	System.out.println(noUrlPoint);
				points.add(noUrlPoint);
			}
			
			int startUrl = tempMessage.indexOf("www", index);	
			int endUrl = tempMessage.indexOf(" ", startUrl); 
			boolean isStartUrl = (startUrl != -1);
			boolean isEndUrl = (endUrl != -1);
			if (isStartUrl && isEndUrl){
				urlPoint = new UrlPoint(startUrl, endUrl, tempMessage.substring(startUrl, endUrl), true);
				//System.out.println(urlPoint);
				points.add(urlPoint);
				index = endUrl;
					
			}else if ( (startUrl != -1) && (endUrl == -1)) {
				urlPoint = new UrlPoint(startUrl, tempMessage.length(), tempMessage.substring(startUrl, tempMessage.length()), true);
				//System.out.println(urlPoint);
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
		
		String middle = "zazazazazaza fasdasd asdasdasda";
		
		MiddleUrl middleUrl = new MiddleUrl(middle);
			System.out.println(middleUrl.getPoints());
			
		System.out.println("Done");
	}

}