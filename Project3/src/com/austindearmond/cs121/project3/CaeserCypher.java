package com.austindearmond.cs121.project3;

public class CaeserCypher extends Cypher<LetterKey> {
	
	private CaeserCypher(LetterKey key) {
		super(key);
	}

	public static CaeserCypher fromByte(byte b) {
		return new CaeserCypher(new LetterKey(b));
	}
	
	public static CaeserCypher fromChar(char c) {
		return new CaeserCypher(new LetterKey(c));
	}

	@Override
	public String encode(String s) {
		String outputString = "";
		for (char oldChar : s.toCharArray()) {
			outputString += processUp(oldChar);
		}
		return outputString;
	}

	@Override
	public String decode(String s) {
		String outputString = "";
		for (char oldChar : s.toCharArray()) {
			outputString += processDown(oldChar);
		}
		return outputString;
	}
	
	private char processUp(char oldChar) {
		char newChar;
		if (Character.isUpperCase(oldChar)) {
			newChar = (char) (oldChar + key.toByte());
			if (newChar > 'Z')
				newChar = (char) (newChar - 26);
		} else if (Character.isLowerCase(oldChar)) {
			newChar = (char) (oldChar + key.toByte());
			if (newChar > 'z')
				newChar = (char) (newChar - 26);
		} else
			newChar = oldChar;
		return newChar;
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
