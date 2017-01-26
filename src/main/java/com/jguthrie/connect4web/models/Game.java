package com.jguthrie.connect4web.models;

import com.jguthrie.connect4web.models.Token.Color;

public class Game {
	
	private Color nextColor;
	private Color winner;
	private Board board;
	private int[] lastMove;
	private int numMoves;

	public Game() {
		this.board = new Board(6, 7);
		this.nextColor = Color.RED;
		this.numMoves = 0;
	}
	
	public int getNumMoves() {
		return this.numMoves;
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
		if(c != this.nextColor || this.winner != null) {
			return false;
		}
		
		int[] lastMove = this.board.playMove(col, new Token(c));
		
		if(lastMove == null) {
			return false;
		} else {
			this.lastMove = lastMove;
			this.numMoves++;
		}
		
		this.winner = board.isWinner(this.lastMove[0], this.lastMove[1]);
		
		if(c == Color.RED) {
			this.nextColor = Color.YELLOW;
		} else {
			this.nextColor = Color.RED;
		}
		
		return true;
		
	}

}
