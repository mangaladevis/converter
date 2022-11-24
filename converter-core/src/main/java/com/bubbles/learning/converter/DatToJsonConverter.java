package com.bubbles.learning.converter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bubbles.learning.JsonUtil;
import com.bubbles.learning.input.FileInputHandler;
import com.fasterxml.jackson.core.JsonProcessingException;

public final class DatToJsonConverter {
	private final static Logger LOGGER = Logger.getLogger(FileInputHandler.class.getName());

	private DatToJsonConverter() {
	}

	public static String convert(List<String> datInput) {
		List<Map<String, Object>> datInputSplit = new ArrayList<Map<String, Object>>();
		for (String inputLine : datInput) {
			datInputSplit.add(DatToJsonConverter.convert(inputLine));
		}

		try {
			return JsonUtil.toJsonString(datInputSplit);
		} catch (JsonProcessingException e) {
			LOGGER.log(Level.SEVERE, "Failed while processing json.", e);
			throw new IllegalStateException(e);
		}
	}

	private static Map<String, Object> convert(String input) {
		List<String> tokens = new ArrayList<String>();
		Map<String, Object> parsedDatInput = new LinkedHashMap<String, Object>();
		tokens.addAll(splitByComma(input));
		for (String string : tokens) {
			if (!string.contains("[")) {
				String key = string.substring(0, string.indexOf("="));
				Object myObject = string.substring(string.indexOf("=") + 1, string.length());
				parsedDatInput.put(key, myObject);
			} else {
				String key = string.substring(0, string.indexOf("="));
				Object myObject = convert(string.substring(string.indexOf("[") + 1, string.length() - 1));
				parsedDatInput.put(key, myObject);
			}
		}
		return parsedDatInput;
	}

	private static List<String> splitByComma(String input) {
		List<String> tokens = new ArrayList<String>();
		ArrayList<String> squareBracketList = new ArrayList<String>();
		int startPosition = 0;
		boolean isInSquareBracket = false;
		for (int currentPosition = 0; currentPosition < input.length(); currentPosition++) {
			if (input.charAt(currentPosition) == '[') {
				squareBracketList.add("[");
				isInSquareBracket = true;
			} else if (input.charAt(currentPosition) == ']') {
				if (!squareBracketList.isEmpty()) {
					squareBracketList.remove(squareBracketList.size() - 1);
				}
				if (squareBracketList.isEmpty()) {
					isInSquareBracket = false;
				}
			} else if (input.charAt(currentPosition) == ',' && !isInSquareBracket) {
				tokens.add(input.substring(startPosition, currentPosition));
				startPosition = currentPosition + 1;
			}
		}
		tokens.add(input.substring(startPosition));
		return tokens;
	}
}
