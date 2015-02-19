package ru.jabchat.utils;

import java.awt.Color;
import java.io.Serializable;

public class Settings implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Color myColor;
	private Color otherColor;

	public Color getMyColor() {
		return myColor;
	}

	public void setMyColor(Color myColor) {
		this.myColor = myColor;
	}

	public Color getOtherColor() {
		return otherColor;
	}

	public void setOtherColor(Color otherColor) {
		this.otherColor = otherColor;
	}
	
	public boolean isNull(){
		if (myColor == null && otherColor == null){
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "Settings [myColor=" + myColor + ", otherColor=" + otherColor
				+ "]";
	}

}