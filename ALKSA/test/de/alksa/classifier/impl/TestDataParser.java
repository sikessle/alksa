package de.alksa.classifier.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

public class TestDataParser {

	private static class TestSet {

		public String learned;
		public List<String> allowed;
		public List<String> disallowed;

		public TestSet(String learned, List<String> allowed,
				List<String> disallowed) {
			this.learned = learned;
			this.allowed = new LinkedList<>(allowed);
			this.disallowed = new LinkedList<>(disallowed);
		}

	}

	private List<TestSet> total;
	private String learned;
	private List<String> allowed;
	private List<String> disallowed;
	private boolean inQuery = false;
	private boolean inLearned = false;
	private boolean inAllowed = false;
	private boolean inDisallowed = false;

	public Object[][] parse(Path filePath) {
		try {
			Stream<String> lines = Files.lines(filePath);
			process(lines);
			return getData();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private Object[][] getData() {
		Object[][] data = new Object[total.size()][3];
		TestSet testSet;

		for (int i = 0; i < total.size(); i++) {
			testSet = total.get(i);

			data[i][0] = testSet.learned;
			data[i][1] = new LinkedList<String>(testSet.allowed);
			data[i][2] = new LinkedList<String>(testSet.disallowed);
		}

		return data;
	}

	private void process(Stream<String> lines) {
		allowed = new LinkedList<>();
		disallowed = new LinkedList<>();
		total = new LinkedList<>();

		lines.forEach((l) -> {
			if (l.isEmpty()) {
				return;
			}
			if (l.startsWith("#~QUERY~#")) {
				inQuery = true;
				inLearned = false;
				inAllowed = false;
				inDisallowed = false;
				return;
			}
			if (learned != null && l.startsWith("#~ENDQUERY~#")) {
				total.add(new TestSet(learned, allowed, disallowed));
				learned = null;
				allowed.clear();
				disallowed.clear();
				return;
			}
			if (inQuery && l.startsWith("#~learned~#")) {
				inLearned = true;
				inAllowed = false;
				inDisallowed = false;
				return;
			}
			if (inQuery && l.startsWith("#~allowed~#")) {
				inLearned = false;
				inAllowed = true;
				inDisallowed = false;
				return;
			}
			if (inQuery && l.startsWith("#~disallowed~#")) {
				inLearned = false;
				inAllowed = false;
				inDisallowed = true;
				return;
			}
			if (inLearned) {
				learned = l;
				inLearned = false; // only accept one learned
			}
			if (inAllowed) {
				allowed.add(l.trim());
			}
			if (inDisallowed) {
				disallowed.add(l.trim());
			}
		});
	}
}
