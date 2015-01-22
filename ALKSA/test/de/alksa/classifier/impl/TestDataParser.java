package de.alksa.classifier.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class TestDataParser {

	private String learned;
	private Object[] allowed;
	private Object[] disallowed;

	public Object[][] parse(Path filePath) {
		try {
			Stream<String> lines = Files.lines(filePath);
			return process(lines);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private Object[][] process(Stream<String> lines) {
		lines.forEach(line -> System.out.println(line));
		return null;
	}

}
