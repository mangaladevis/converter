package com.bubbles.learning.output;

import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileOutputHandler implements OutputHandler {
	private String filenameOut;
	private String output;
	private static final Logger LOGGER = Logger.getLogger(FileOutputHandler.class.getName());

	public FileOutputHandler(String filenameOut, String output) {
		this.filenameOut = filenameOut;
		this.output = output;
	}

	@Override
	public void write() throws IOException {
		try {
			FileWriter file = new FileWriter(filenameOut);
			file.write(output);
			file.close();
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Failed to write file " + filenameOut, e);
			throw new IllegalStateException(e);
		}
	}
}
