package com.austindearmond.cs121.project3;

import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Applies a Caeser Cypher to an input file, writing the results to an output
 * file.
 * <p>
 * A Caeser Cypher moves all letters forward a certain number of characters in
 * the alphabet. For example, if the number is two, "A" becomes "C," "d" becomes
 * "f," "Y" becomes "A," and so on. The final element shows that letters at the
 * end of the alphabet wrap around to the beginning.
 * 
 * @author Austin DeArmond <ajdearmond@bsu.edu>
 */

public class CaeserCypherCommandLineInterface {

	private static Scanner kb; // Only initialized when needed
	private static boolean hasKB = false;

	private static File inputFile;
	private static File outputFile;
	private static Boolean decrypt;
	private static Byte key;

	public static void main(String[] args) {
		processArguments(args);
		promptForOptions();
		try {
			cypher();
		} catch (Exception e) {
			e.printStackTrace();
		}
		closeKB();
	}

	private static void processArguments(String[] args) {
		if (isEmpty(args))
			return;
		for (int i = 0; i < args.length; i++) {
			checkArg(args, i);
		}
	}

	private static void checkArg(String[] args, int index) {
		String value = "";
		if (index < args.length - 1) {
			value = args[index + 1];
		}
		final String arg = args[index].toLowerCase();
		// TODO: Create factory
		switch (arg) {
		case "--help":
		case "-h":
			showHelp();
			System.exit(0);
			break;
		case "--input":
		case "-i":
			getInputFile(value);
			break;
		case "--output":
		case "-o":
			getOutputFile(value);
			break;
		case "--key":
		case "-k":
			setKey(value);
			break;
		case "--encrypt":
		case "-e":
			decrypt = false;
			break;
		case "--decrypt":
		case "-d":
			decrypt = true;
			break;
		}
	}

	public static void getInputFile(String path) {
		inputFile = new File(path);
		if (!(inputFile.exists() && inputFile.canRead())) {
			System.out.println("Could not open the input file specified.");
			inputFile = null;
		}
	}

	public static void getOutputFile(String path) {
		try {
			outputFile = new File(path);
			outputFile.createNewFile();
			if (outputFile.isDirectory() || !outputFile.canWrite()) {
				System.out.println("Cannot write to the output path specified.");
				outputFile = null;
			}
		} catch (IOException e) {
			System.out.println("Cannot write to the output path specified.");
			outputFile = null;
		}
	}

	public static void setKey(String value) {
		try {
			key = Byte.parseByte(value);
			if (key < 0 || key > 26) {
				System.out.println("Could not interpret the key argument.");
				key = null;
				return;
			}
			decrypt = false;
			// Encrypts by default if key is provided.
		} catch (Exception e) {
			System.out.println("Could not interpret the key argument.");
			key = 0;
		}
	}
	
	public static void showHelp() {
		System.out.println(
				"Takes an input file, applies Caeser's cypher to it with a given key, and stores the result in an output file.");
		System.out.println();
		System.out.println(
				"Caeser's cypher is an elementary way to encode a message.  It moves each alphabetical character in the file a certain number of steps in the alphabet forward, wrapping around to the beginning of the alphabet for letters twoards the end.");
		System.out.println();
		System.out.println("For example, if the key for encryption is 3, then");
		System.out.println("\tAlphabet ABCDEFGHIJKLMNOPQRSTUVWXYZ");
		System.out.println("gets translated to");
		System.out.println("\tDoskdehw DEFGHIJKLMNOPQRSTUVWXYZABC");
		System.out.println();
		System.out.println("The following arguments are accepted:");
		System.out.println("\t-i (file)\tThe path to the file to transcode");
		System.out.println("\t-o (file)\tThe file path to store the results.");
		System.out.println("\t-k (int 1-26)\tThe key to use for transcoding.");
		System.out.println("\t-d       \tDecrypt the input file.");
		System.out.println("\t-e       \tEncrypt the input file (default if key is provided).");
		System.exit(0);
	}

	private static boolean isEmpty(String[] s) {
		return s.length == 0;
	}

	private static void promptForOptions() {
		if (decrypt == null) {
			System.out.println("Would you like to encrypt (E) or decrypt (D)?");
			int inputOption = getOption(getKB(), new String[] { "e", "d" });
			// Returns 0 if encrypt chosen or 1 if decrypt chosen.
			if (0 == inputOption)
				decrypt = false;
			else
				decrypt = true;
		}
		if (null == inputFile) {
			System.out.println("Please enter the path to your input file.");
			inputFile = getFile(true, false);
		}
		if (null == outputFile) {
			System.out.println("Please enter the path to your output file.");
			outputFile = getFile(false, true);
		}
		if (key == null) {
			if (decrypt)
				System.out.println("What key was used to encrypt the file?");
			else
				System.out.println("What key (between 1 and 26) would you like to use to encrypt your file?");
			key = getKey(getKB());
		}
	}

	private static Scanner getKB() {
		if (!hasKB) {
			kb = new Scanner(System.in);
			System.out.println("Type QUIT at any time to cancel the operation.");
		}
		return kb;
	}

	private static void closeKB() {
		if (hasKB)
			kb.close();
	}

	/**
	 * Takes input, validating it with a set of given options.
	 * 
	 * @param kb
	 *            The {@link Scanner} to use to get input.
	 * @param availableOptions
	 *            A {@link String} array of options to use to validate the
	 *            input.
	 * @return Returns the integer corresponding to the option chosen.
	 */
	private static int getOption(Scanner kb, String[] availableOptions) {
		while (true) {
			String input = kb.nextLine();
			if (input.toLowerCase().contains("quit")) {
				kb.close();
				System.exit(-1);
			}
			for (int i = 0; i < availableOptions.length; i++) {
				// Checks, insensitive to case, if the input matches an option.
				// Is also forgiving of extra input such as spaces or symbols,
				// or even a misunderstanding about how much should be typed.
				if (input.toUpperCase().startsWith(availableOptions[i].toUpperCase()))
					return i;
			}
			System.out.println("The input you provided was invalid.  Please enter a valid option.");
		}
	}

	/**
	 * Takes input, validating it as a file path.
	 * 
	 * @param kb
	 *            The {@link Scanner} to use to get input.
	 * @param read
	 *            Determines if the file path should be an existing readable
	 *            file.
	 * @param write
	 *            Determines if the file path should be a writable file (does
	 *            not need to exist).
	 * @return Returns a {@link File} with the input path.
	 */
	private static File getFile(boolean read, boolean write) {
		String input;
		File file;
		while (true) {
			try {
				input = getKB().nextLine();
				if (input.toLowerCase().contains("quit")) {
					closeKB();
					System.exit(-1);
				}
				file = new File(input);
				if (write && !file.exists())
					file.createNewFile();
				// Makes sure that files for reading can be read (and therefore
				// exist)
				// and that files for writing can be written to.
				if ((read && (file.isFile() && file.canRead())) || (write && file.canWrite() && !file.isDirectory()))
					return file;
				else
					System.out.println("The system could not open the file specified.  Please try again.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Takes input, validating it as an encryption key for the cypher.
	 * 
	 * @param kb
	 *            The {@link Scanner} to use to get input.
	 * @return The input key.
	 */
	public static byte getKey(Scanner kb) {
		while (true) {
			String input = kb.nextLine();
			if (input.toLowerCase().contains("quit")) {
				closeKB();
				System.exit(-1);
			}
			try {
				byte result = Byte.parseByte(input);
				if (result >= 0 && result <= 26)
					return result;
				else
					System.out.println("You did not input a valid number.  Please try again.");
			} catch (NumberFormatException e) {
				System.out.println("You did not input a valid number. Please try again.");
			}
		}
	}

	public static void cypher() {
		String inputString = readFile();
		CaeserCypher cypher = CaeserCypher.fromByte(key);
		String outputString = decrypt ? cypher.decrypt(inputString) : cypher.encrypt(inputString);
		writeFile(outputString);
		System.out.println("Successfully wrote file!");
	}
	
	private static String readFile() {
		try {
			byte[] encoded = Files.readAllBytes(inputFile.toPath());
			return new String(encoded);
		}
		catch (Exception e) {
			System.out.println("Could not read the input file.");
			closeKB();
			System.exit(-1);
			return null;
		}
	}
	
	private static void writeFile(String contents) {
		try {
			Files.write(outputFile.toPath(), contents.getBytes());
		} catch (IOException e) {
			System.out.println("Could not write the file.");
			closeKB();
			System.exit(-1);
		}
	}
}
