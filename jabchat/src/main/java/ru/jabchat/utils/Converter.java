package ru.jabchat.utils;

import java.util.HashMap;
import java.util.Map;

public class Converter {

	private static final Map<String, String> END_TO_RUS = new HashMap<String, String>();
	static{
		END_TO_RUS.put("q", "й");
		END_TO_RUS.put("w", "ц");
		END_TO_RUS.put("e", "у");
		END_TO_RUS.put("r", "к");
		END_TO_RUS.put("t", "е");
		END_TO_RUS.put("y", "н");
		END_TO_RUS.put("u", "г");
		END_TO_RUS.put("i", "ш");
		END_TO_RUS.put("o", "щ");
		END_TO_RUS.put("p", "з");
		END_TO_RUS.put("[", "х");
		END_TO_RUS.put("]", "ъ");
		END_TO_RUS.put("a", "ф");
		END_TO_RUS.put("s", "ы");
		END_TO_RUS.put("d", "в");
		END_TO_RUS.put("f", "а");
		END_TO_RUS.put("g", "п");
		END_TO_RUS.put("h", "р");
		END_TO_RUS.put("j", "о");
		END_TO_RUS.put("k", "л");
		END_TO_RUS.put("l", "д");
		END_TO_RUS.put(";", "ж");
		END_TO_RUS.put("'", "э");
		END_TO_RUS.put("\\", "\\");
		END_TO_RUS.put("z", "я");
		END_TO_RUS.put("x", "ч");
		END_TO_RUS.put("c", "с");
		END_TO_RUS.put("v", "м");
		END_TO_RUS.put("b", "и");
		END_TO_RUS.put("n", "т");
		END_TO_RUS.put("m", "ь");
		END_TO_RUS.put(",", "б");
		END_TO_RUS.put("?", "ю");
		END_TO_RUS.put("/", ".");
		END_TO_RUS.put("Q", "Й");
		END_TO_RUS.put("W", "Ц");
		END_TO_RUS.put("E", "У");
		END_TO_RUS.put("R", "К");
		END_TO_RUS.put("T", "Е");
		END_TO_RUS.put("Y", "Н");
		END_TO_RUS.put("U", "Г");
		END_TO_RUS.put("I", "Ш");
		END_TO_RUS.put("O", "Щ");
		END_TO_RUS.put("P", "З");
		END_TO_RUS.put("{", "Х");
		END_TO_RUS.put("}", "Ъ");
		END_TO_RUS.put("A", "Ф");
		END_TO_RUS.put("S", "Ы");
		END_TO_RUS.put("D", "В");
		END_TO_RUS.put("F", "А");
		END_TO_RUS.put("G", "П");
		END_TO_RUS.put("H", "Р");
		END_TO_RUS.put("J", "О");
		END_TO_RUS.put("K", "Л");
		END_TO_RUS.put("L", "Д");
		END_TO_RUS.put(":", "Ж");
		END_TO_RUS.put(" \" ", "Э");
		END_TO_RUS.put("|", "/");
		END_TO_RUS.put("Z", "Я");
		END_TO_RUS.put("X", "Ч");
		END_TO_RUS.put("C", "С");
		END_TO_RUS.put("V", "М");
		END_TO_RUS.put("B", "И");
		END_TO_RUS.put("N", "Т");
		END_TO_RUS.put("M", "Ь");
		END_TO_RUS.put("<", "Б");
		END_TO_RUS.put(">", "Ю");
		END_TO_RUS.put("?", ",");
		
	}
	
	public static String engToRu(String message) {
		StringBuilder  rezult = new StringBuilder();
		for (int symbolCnt = 0; symbolCnt < message.length(); symbolCnt++) {
			 String symbol = message.substring(symbolCnt, symbolCnt+1);
	            if (END_TO_RUS.containsKey(symbol)) {
	            	rezult.append(END_TO_RUS.get(symbol));
	            }
	            else {
	            	rezult.append(symbol);
	            }
		}
		return rezult.toString();

	}
	
}