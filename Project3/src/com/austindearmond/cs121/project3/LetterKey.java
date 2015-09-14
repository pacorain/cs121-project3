package com.austindearmond.cs121.project3;

public class LetterKey {
	
	private byte key;
	
	public LetterKey(byte key) {
		if (key < 0 || key > 26) {
			throw new IllegalStateException("Value must be between 0 and 26.");
		}
		this.key = key;
	}
	
	public LetterKey(char key) {
		if (Character.isUpperCase(key)) {
			this.key = (byte) (key + 1 - 'A');
		} else if (Character.isLowerCase(key)) {
			this.key = (byte) (key + 1 - 'a');
		} else {
			throw new IllegalStateException("Value must be an alphanumeric character.");
		}
	}
	
	public byte toByte() {
		return key;
	}
	
	
}
