package com.austindearmond.cs121.project3;

import java.util.Scanner;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

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
 * @version 0.2
 * @since 0.2
 */

public class CaeserCypher {

	private static Scanner kb; // Only initialized when needed
	private static boolean hasKB = false;

	public static void main(String[] args) {
		// BEGIN SECTION Arguments {
		File inputFile = null;
		File outputFile = null;
		boolean decryptArgument = false;
		boolean decrypt = false;
		byte key = 0;
		for (int i = 0; i < args.length; i++) {
			if (args[i].equalsIgnoreCase("-I")) {
				if (i+1 >= args.length) {
					System.out.println("Could not open the input file specified.");
					continue;
				}
				inputFile = new File(args[i + 1]);
				if (!(inputFile.exists() && inputFile.canRead())) {
					System.out
							.println("Could not open the input file specified.");
					inputFile = null;
				}
			} else if (args[i].equalsIgnoreCase("-O")) {
				if (i+1 >= args.length) {
					System.out.println("Cannot write to the output path specified.");
					continue;
				} else if (args[1+1].startsWith("-")){
					System.out.println("Cannot write to the output path specified.");
					continue;
				}
				try {
					outputFile = new File(args[i + 1]);
					outputFile.createNewFile();
					if (outputFile.isDirectory() || !outputFile.canWrite()) {
						System.out
								.println("Cannot write to the output path specified.");
						outputFile = null;
					}
				} catch (IOException e) {
					System.out.println("Cannot write to the output path specified.");
					outputFile = null;
				}
				
			} else if (args[i].equalsIgnoreCase("-K")) {
				try {
					key = Byte.parseByte(args[i + 1]);
					if (key < 1 || key >= 26) {
						System.out
								.println("Could not interpret the key argument.");
						key = 0;
						continue;
					}
					decrypt = false;
					decryptArgument = true;
					// Encrypts by default if key is provided.
				} catch (Exception e) {
					System.out.println("Could not interpret the key argument.");
					key = 0;
				}
			} else if (args[i].equalsIgnoreCase("-E")) {
				decrypt = false;
				decryptArgument = true;
			} else if (args[i].equalsIgnoreCase("-D")) {
				decrypt = true;
				decryptArgument = true;
			} else if (args[i].toLowerCase().contains("-h")) {
				System.out
						.println("Takes an input file, applies Caeser's cypher to it with a given key, and stores the result in an output file.");
				System.out.println();
				System.out
						.println("Caeser's cypher is an elementary way to encode a message.  It moves each alphabetical character in the file a certain number of steps in the alphabet forward, wrapping around to the beginning of the alphabet for letters twoards the end.");
				System.out.println();
				System.out
						.println("For example, if the key for encryption is 3, then");
				System.out.println("\tAlphabet ABCDEFGHIJKLMNOPQRSTUVWXYZ");
				System.out.println("gets translated to");
				System.out.println("\tDoskdehw DEFGHIJKLMNOPQRSTUVWXYZABC");
				System.out.println();
				System.out.println("The following arguments are accepted:");
				System.out
						.println("\t-i (file)\tThe path to the file to transcode");
				System.out
						.println("\t-o (file)\tThe file path to store the results.");
				System.out
						.println("\t-k (int 1-26)\tThe key to use for transcoding.");
				System.out.println("\t-d       \tDecrypt the input file.");
				System.out
						.println("\t-e       \tEncrypt the input file (default if key is provided).");
				System.exit(0);
			} else if (i == 0) {
				// Allows a file to be provided without option, but gives option
				// precedence.
				inputFile = new File(args[0]);
				if (!(inputFile.exists() && inputFile.canRead())) {
					System.out
							.println("Could not open the input file specified.");
					inputFile = null;
				}
			}
		}
		// } END SECTION
		// BEGIN SECTION Input {
		if (!decryptArgument) {
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
			inputFile = getFile(getKB(), true, false);
		}
		if (null == outputFile) {
			System.out.println("Please enter the path to your output file.");
			outputFile = getFile(getKB(), false, true);
		}
		if (key == 0) {
			if (decrypt)
				System.out.println("What key was used to encrypt the file?");
			else
				System.out
						.println("What key (between 1 and 26) would you like to use to encrypt your file?");
			key = getKey(getKB());
		}
		// } END SECTION
		// BEGIN SECTION Execution {
		try {
			cypher(inputFile, outputFile, decrypt, key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		closeKB();
		// } END SECTION
	}

	private static Scanner getKB() {
		if (!hasKB)
			kb = new Scanner(System.in);
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
			for (int i = 0; i < availableOptions.length; i++) {
				// Checks, insensitive to case, if the input matches an option.
				// Is also forgiving of extra input such as spaces or symbols,
				// or even a misunderstanding about how much should be typed.
				if (input.toUpperCase().startsWith(
						availableOptions[i].toUpperCase()))
					return i;
			}
			System.out
					.println("The input you provided was invalid.  Please enter a valid option.");
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
	private static File getFile(Scanner kb, boolean read, boolean write) {
		File file;
		while (true) {
			try {
				file = new File(kb.nextLine());
				if (write && !file.exists())
					file.createNewFile();
				// Makes sure that files for reading can be read (and therefore
				// exist)
				// and that files for writing can be written to.
				if ((read && (file.isFile() && file.canRead()))
						|| (write && file.canWrite() && !file.isDirectory()))
					return file;
				else
					System.out
							.println("The system could not open the file specified.  Please try again.");
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
			try {
				byte result = Byte.parseByte(input);
				if (result > 0 && result <= 26)
					return result;
				else
					System.out
							.println("You did not input a valid number.  Please try again.");
			} catch (NumberFormatException e) {
				System.out
						.println("You did not input a valid number. Please try again.");
			}
		}
	}

	public static void cypher(File inputFile, File outputFile, boolean decrypt,
			byte key) throws IOException {
		if (decrypt)
			key *= -1;
		FileReader fileReader = null;
		FileWriter fileWriter = null;
		try {
			fileReader = new FileReader(inputFile);
			fileWriter = new FileWriter(outputFile);
			int c;
			char cchar;
			char newchar;
			while ((c = fileReader.read()) != -1) {
				cchar = (char) c;
				if (Character.isUpperCase(cchar)) {
					newchar = (char) (cchar + key);
					if (newchar > 'Z')
						newchar = (char) (newchar - 26);
					else if (newchar < 'A')
						newchar = (char) (newchar + 26);
				} else if (Character.isLowerCase(cchar)) {
					newchar = (char) (cchar + key);
					if (newchar > 'z')
						newchar = (char) (newchar - 26);
					else if (newchar < 'a')
						newchar = (char) (newchar + 26);
				} else
					newchar = cchar;
				fileWriter.append(newchar);
			}
			System.out.println("Message transcoded!");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != fileReader)
				fileReader.close();
			if (null != fileWriter)
				fileWriter.close();
		}
	}
}
