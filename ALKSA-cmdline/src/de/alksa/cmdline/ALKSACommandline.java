package de.alksa.cmdline;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import de.alksa.ALKSA;

public final class ALKSACommandline {

	private static final String PARM_DB_PATH = "s";
	private static final String PARM_PRODUCTIVE = "p";
	private static final String PARM_LEARN = "l";
	private static final String PARM_USAGE = "h";
	private static final String PARM_FILE = "f";

	private static boolean learn = false;
	private static String storagePath = null;
	private static String filePath = null;

	public static void main(String[] args) {

		parseArguments(args);

		try {
			classify();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void classify() throws IOException {
		final ALKSA alksa = new ALKSA(storagePath);
		alksa.setLearning(learn);

		Files.lines(new File(filePath).toPath()).forEach(line -> {
			if (line.isEmpty()) {
				return;
			}
			try {
				if (alksa.accept(line, "db", "user")) {
					System.out.println("ACCEPTED: " + line);
				} else {
					System.out.println("REJECTED: " + line);
				}
			} catch (Exception e) {
				System.out.println("ERROR: " + line);
			}
		});

		alksa.shutdown();
	}

	private static void parseArguments(String[] args) {
		Options options = setupOptions();
		CommandLineParser parser = new GnuParser();

		if (args.length == 0) {
			printUsage(options);
			System.exit(0);
		}

		try {
			CommandLine cmd = parser.parse(options, args);
			if (cmd.hasOption(PARM_USAGE)) {
				printUsage(options);
				System.exit(0);
			}
			if (cmd.hasOption(PARM_PRODUCTIVE)) {
				learn = false;
			} else if (cmd.hasOption(PARM_LEARN)) {
				learn = true;
			}

			storagePath = cmd.getOptionValue(PARM_DB_PATH);
			filePath = cmd.getOptionValue(PARM_FILE);

		} catch (ParseException e) {
			printUsage(options);
			e.printStackTrace();
			System.exit(1);
		}

		if (storagePath == null || filePath == null) {
			System.err.println("no storage path or file path");
			printUsage(options);
			System.exit(1);
		}
	}

	private static void printUsage(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("alksa-commandline", options);
	}

	private static Options setupOptions() {
		Options options = new Options();
		options.addOption(PARM_LEARN, false, "process input in learning mode");
		options.addOption(PARM_PRODUCTIVE, false,
				"process input in produtive mode");
		options.addOption(PARM_DB_PATH, true,
				"path where ALKSA may store its results");
		options.addOption(PARM_FILE, true, "path for query file");
		return options;
	}

}
