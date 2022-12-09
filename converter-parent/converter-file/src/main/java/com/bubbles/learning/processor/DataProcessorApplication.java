package com.bubbles.learning.processor;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bubbles.learning.converter.DatToJsonConverter;
import com.bubbles.learning.input.FileInputHandler;
import com.bubbles.learning.output.FileOutputHandler;

public class DataProcessorApplication {
	private static final String INPUT_DIR = System.getProperty("input-dir", "data/input/");
	private static final String OUTPUT_DIR = System.getProperty("output-dir", "data/output/");
	public static void main(String[] args) {
		List<String> datInput = new ArrayList<>();
		final Logger LOGGER = Logger.getLogger(DataProcessorApplication.class.getName());

		try {
			WatchService watchService = FileSystems.getDefault().newWatchService();
			Path path = Paths.get(INPUT_DIR);
			path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

			WatchKey key;
			while ((key = watchService.take()) != null) {
				for (WatchEvent<?> event : key.pollEvents()) {
					try {
						datInput = new FileInputHandler(INPUT_DIR + event.context()).read();
						try {
							String outputFile = event.context().toString().substring(0,
									event.context().toString().indexOf("."));
							new FileOutputHandler(OUTPUT_DIR  + outputFile + ".json",
									DatToJsonConverter.convert(datInput)).write();
							LOGGER.info("File created");
						} catch (IOException e) {
							LOGGER.log(Level.SEVERE, "Failed while processing output file.", e);
						}
					} catch (IOException e) {
						LOGGER.log(Level.SEVERE, "Failed while reading input file.", e);
					}
				}
				key.reset();
			}

		} catch (IOException | InterruptedException e) {
			LOGGER.log(Level.SEVERE, "Failed while processing input file.", e);
		}

	}
}