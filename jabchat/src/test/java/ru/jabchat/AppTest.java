package ru.jabchat;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Unit test for simple App.
 */
public class AppTest{
		
		public static void main(String [] args){
			
			InetAddress ip;
			
			try {
				
				ip = InetAddress.getLocalHost();
				System.out.println(ip.toString());
				
			}catch(UnknownHostException e){
				e.printStackTrace();
			}
	
		}
}
