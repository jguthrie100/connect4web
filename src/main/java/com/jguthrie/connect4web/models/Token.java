package com.jguthrie.connect4web.models;

/**
 * A basic class modelling a Game Token (i.e. the red and yellow pieces that are dropped into the game board)
 * @author jguthrie100
 *
 */
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
