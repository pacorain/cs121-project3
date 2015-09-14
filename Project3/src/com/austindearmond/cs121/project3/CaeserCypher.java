package com.austindearmond.cs121.project3;

/**
 * A cypher class that uses the number of a letter of the alphabet to shift
 * letters in a string. Punctuation is left unaffected.
 */
public class CaeserCypher extends Cypher<LetterKey> {

	private CaeserCypher(LetterKey key) {
		super(key);
	}

	/**
	 * Get a CeaserCypher from a byte value.
	 * 
	 * @param b
	 *            The value to use when shifting the letters.
	 */
	public static CaeserCypher fromByte(byte b) {
		return new CaeserCypher(new LetterKey(b));
	}

	/**
	 * Get a CeaserCypher from a letter value.
	 * 
	 * @param c
	 *            The letter to use to shift the letters in the text over.
	 */
	public static CaeserCypher fromChar(char c) {
		return new CaeserCypher(new LetterKey(c));
	}

	@Override
	public String encrypt(String s) {
		String outputString = "";
		for (char oldChar : s.toCharArray()) {
			outputString += processUp(oldChar);
		}
		return outputString;
	}

	@Override
	public String decrypt(String s) {
		String outputString = "";
		for (char oldChar : s.toCharArray()) {
			outputString += processDown(oldChar);
		}
		return outputString;
	}

	private char processUp(char oldChar) {
		return process(oldChar, key.toByte());
	}

	private char processDown(char oldChar) {
		return process(oldChar, (byte) (key.toByte() * -1));
	}

	private char process(char oldChar, byte amount) {
		char newChar;
		if (Character.isUpperCase(oldChar)) {
			newChar = (char) (oldChar + amount);
			if (newChar > 'Z')
				newChar = (char) (newChar - 26);
			else if (newChar < 'A')
				newChar = (char) (newChar + 26);
		} else if (Character.isLowerCase(oldChar)) {
			newChar = (char) (oldChar + amount);
			if (newChar > 'z')
				newChar = (char) (newChar - 26);
			else if (newChar < 'a')
				newChar = (char) (newChar + 26);
		} else
			newChar = oldChar;
		return newChar;
	}

}
