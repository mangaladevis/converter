package com.bubbles.learning.jms;

import java.io.BufferedReader;
import java.io.FileReader;
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
import javax.jms.JMSException;

public class JmsClient {

	private static final String INPUT_DIR = System.getProperty("input-dir", "data/input/");
	private static String INPUT_DESTINATION = "DatQueue";
	private static String OUTPUT_DESTINATION = "JsonQueue";
	private static final Logger LOGGER = Logger.getLogger(JmsClient.class.getName());

	public static void main(String[] args) {

		MessageConsumerRunner messageConsumerRunner = new MessageConsumerRunner(OUTPUT_DESTINATION);
		Thread messageConsumer = new Thread(messageConsumerRunner);
		messageConsumer.start();

		try {
			WatchService watchService = FileSystems.getDefault().newWatchService();
			Path path = Paths.get(INPUT_DIR);
			path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

			WatchKey key;
			while ((key = watchService.take()) != null) {
				for (WatchEvent<?> event : key.pollEvents()) {
					try {
						BufferedReader bufferedReader = new BufferedReader(new FileReader(INPUT_DIR + event.context()));
						String inputLine;
						List<String> datInputs = new ArrayList<>();
						while ((inputLine = bufferedReader.readLine()) != null) {
							datInputs.add(inputLine);
						}
						String datInput = String.join("|", datInputs);
						bufferedReader.close();
						MessageSender messageSender = new MessageSender();
						messageSender.send(INPUT_DESTINATION, datInput);
					} catch (IOException e) {
						LOGGER.log(Level.SEVERE, "Failed to read input file.", e);
					} catch (JMSException e) {
						LOGGER.log(Level.SEVERE, "Failed to send message.", e);
						throw new RuntimeException(e);
					}
				}
				key.reset();
			}

		} catch (IOException | InterruptedException e) {
			LOGGER.log(Level.SEVERE, "Failed to process input file.", e);
		}
	}
}
