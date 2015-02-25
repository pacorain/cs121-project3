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
		// Begin arguments
		// NOTE: Make sure that invalid files are set to null.
		// NOTE: Make sure that decryptArgument is TRUE if key is set.
		File inputFile = null;
		File outputFile = null;
		boolean decryptArgument = false;
		boolean decrypt = false;
		byte key = 0;
		// End arguments
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
		try {
			cypher(inputFile, outputFile, decrypt, key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		closeKB();
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
			if ("quit exit cancel stop bye ".contains(input.toLowerCase())) {
				kb.close();
				System.exit(-1);
			}
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
		String input;
		File file;
		while (true) {
			try {
				input = kb.nextLine();
				if ("quit exit cancel stop bye ".contains(input.toLowerCase())) {
					kb.close();
					System.exit(-1);
				}
				file = new File(input);
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
			if ("quit exit cancel stop bye ".contains(input.toLowerCase())) {
				kb.close();
				System.exit(-1);
			}
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
			if (null != fileReader) fileReader.close();
			if (null != fileWriter) fileWriter.close();
		}
	}
}
