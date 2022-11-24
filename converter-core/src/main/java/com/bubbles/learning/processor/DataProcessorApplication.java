package com.bubbles.learning.processor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bubbles.learning.converter.DatToJsonConverter;
import com.bubbles.learning.input.FileInputHandler;
import com.bubbles.learning.output.FileOutputHandler;

public class DataProcessorApplication {
	public static void main(String[] args) {
		List<String> datInput = new ArrayList<>();
		final Logger LOGGER = Logger.getLogger(FileInputHandler.class.getName());

		try {
			datInput = new FileInputHandler("data/input/input.dat").read();
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Failed while processing input file.", e);
		}

		try {
			new FileOutputHandler("data/output/output.json", DatToJsonConverter.convert(datInput)).write();
			System.out.println("file created");
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Failed while processing output file.", e);
		}
	}
}