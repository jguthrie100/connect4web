package com.jguthrie.glucktest.models;

import com.jguthrie.glucktest.models.Token.Color;

public class Game {
	
	private Color nextColor;
	private Color winner;
	private Board board;
	private int[] lastMove;

	public Game() {
		this.board = new Board(6, 7);
		this.nextColor = Color.RED;
	}
	
	public Board getBoard() {
		return this.board;
	}
	
	public Color getNextColor() {
		return this.nextColor;
	}
	
	public Color getWinner() {
		return this.winner;
	}
	
	public boolean playMove(int col, Color c) {
		if(c != this.nextColor) {
			return false;
		}
		
		int[] last = this.board.playMove(col, new Token(c));
		
		if(last == null) {
			return false;
		} else {
			this.lastMove = last;
		}
		
		if(this.winner == null) {
			this.winner = board.isWinner(this.lastMove[0], this.lastMove[1]);
		}
		
		if(c == Color.RED) {
			this.nextColor = Color.YELLOW;
		} else {
			this.nextColor = Color.RED;
		}
		
		return true;
		
	}

}
