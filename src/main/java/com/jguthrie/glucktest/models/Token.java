package com.jguthrie.glucktest.models;

public class Token {
	
	public enum Color {
		RED, YELLOW
	}

	private Color color;
	
	public Token(Color color) {
		this.color = color;
	}
	
	public Color getColor() {
		return this.color;
	}
}
