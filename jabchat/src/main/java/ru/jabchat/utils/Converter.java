package ru.jabchat.utils;

import java.util.HashMap;
import java.util.Map;

public class Converter {

	private static final Map<String, String> ENG_TO_RUS = new HashMap<String, String>();
	static{
		ENG_TO_RUS.put("q", "й");
		ENG_TO_RUS.put("w", "ц");
		ENG_TO_RUS.put("e", "у");
		ENG_TO_RUS.put("r", "к");
		ENG_TO_RUS.put("t", "е");
		ENG_TO_RUS.put("y", "н");
		ENG_TO_RUS.put("u", "г");
		ENG_TO_RUS.put("i", "ш");
		ENG_TO_RUS.put("o", "щ");
		ENG_TO_RUS.put("p", "з");
		ENG_TO_RUS.put("[", "х");
		ENG_TO_RUS.put("]", "ъ");
		ENG_TO_RUS.put("a", "ф");
		ENG_TO_RUS.put("s", "ы");
		ENG_TO_RUS.put("d", "в");
		ENG_TO_RUS.put("f", "а");
		ENG_TO_RUS.put("g", "п");
		ENG_TO_RUS.put("h", "р");
		ENG_TO_RUS.put("j", "о");
		ENG_TO_RUS.put("k", "л");
		ENG_TO_RUS.put("l", "д");
		ENG_TO_RUS.put(";", "ж");
		ENG_TO_RUS.put("'", "э");
		ENG_TO_RUS.put("\\", "\\");
		ENG_TO_RUS.put("z", "я");
		ENG_TO_RUS.put("x", "ч");
		ENG_TO_RUS.put("c", "с");
		ENG_TO_RUS.put("v", "м");
		ENG_TO_RUS.put("b", "и");
		ENG_TO_RUS.put("n", "т");
		ENG_TO_RUS.put("m", "ь");
		ENG_TO_RUS.put(",", "б");
		ENG_TO_RUS.put("?", "ю");
		ENG_TO_RUS.put("/", ".");
		ENG_TO_RUS.put("Q", "Й");
		ENG_TO_RUS.put("W", "Ц");
		ENG_TO_RUS.put("E", "У");
		ENG_TO_RUS.put("R", "К");
		ENG_TO_RUS.put("T", "Е");
		ENG_TO_RUS.put("Y", "Н");
		ENG_TO_RUS.put("U", "Г");
		ENG_TO_RUS.put("I", "Ш");
		ENG_TO_RUS.put("O", "Щ");
		ENG_TO_RUS.put("P", "З");
		ENG_TO_RUS.put("{", "Х");
		ENG_TO_RUS.put("}", "Ъ");
		ENG_TO_RUS.put("A", "Ф");
		ENG_TO_RUS.put("S", "Ы");
		ENG_TO_RUS.put("D", "В");
		ENG_TO_RUS.put("F", "А");
		ENG_TO_RUS.put("G", "П");
		ENG_TO_RUS.put("H", "Р");
		ENG_TO_RUS.put("J", "О");
		ENG_TO_RUS.put("K", "Л");
		ENG_TO_RUS.put("L", "Д");
		ENG_TO_RUS.put(":", "Ж");
		ENG_TO_RUS.put(" \" ", "Э");
		ENG_TO_RUS.put("|", "/");
		ENG_TO_RUS.put("Z", "Я");
		ENG_TO_RUS.put("X", "Ч");
		ENG_TO_RUS.put("C", "С");
		ENG_TO_RUS.put("V", "М");
		ENG_TO_RUS.put("B", "И");
		ENG_TO_RUS.put("N", "Т");
		ENG_TO_RUS.put("M", "Ь");
		ENG_TO_RUS.put("<", "Б");
		ENG_TO_RUS.put(">", "Ю");
		ENG_TO_RUS.put("?", ",");
		
	}
	
	public static String engToRu(String message) {
		StringBuilder  rezult = new StringBuilder();
		for (int symbolCnt = 0; symbolCnt < message.length(); symbolCnt++) {
			 String symbol = message.substring(symbolCnt, symbolCnt+1);
	            if (ENG_TO_RUS.containsKey(symbol)) {
	            	rezult.append(ENG_TO_RUS.get(symbol));
	            }
	            else {
	            	rezult.append(symbol);
	            }
		}
		return rezult.toString();

	}
	
	private static boolean isUrlMiddle(String message) {
		
		boolean isUrl = ( (!message.startsWith("www") && !message.startsWith("http"))  && (message.contains("www") || message.contains("http")));
		if (isUrl){
			return true;
		}
		return false;
	}
	
	public static void main(String [] args){
		String a = "dasd asd asd http: dasdasdasd";
		System.out.println(isUrlMiddle(a));
		
		
	}
	

}