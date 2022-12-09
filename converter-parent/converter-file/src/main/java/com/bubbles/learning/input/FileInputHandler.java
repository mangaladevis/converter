package com.bubbles.learning.input;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileInputHandler implements InputHandler {
	private String filenameIn;

	public FileInputHandler(String filenameIn) {
		super();
		this.filenameIn = filenameIn;
	}

	@Override
	public List<String> read() throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new FileReader(filenameIn));
		String inputLine;
		List<String> datInput = new ArrayList<>();

		while ((inputLine = bufferedReader.readLine()) != null) {
			datInput.add(inputLine);
		}
		bufferedReader.close();
		return datInput;
	}
}
