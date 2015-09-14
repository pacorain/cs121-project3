package com.austindearmond.cs121.project3;

abstract class Cypher<E> {
	protected E key;
	
	Cypher(E key) {
		this.key = key;
	}
	
	abstract public String encode(String s);
	abstract public String decode(String s);
}
