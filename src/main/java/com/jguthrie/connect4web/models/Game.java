package com.jguthrie.connect4web.models;

import com.jguthrie.connect4web.models.Token.Color;

public class Game {
	
	private int gameId;
	private int nextPlayer;
	private Color winner;
	private Board board;
	private int[] lastMove;
	private int numMoves;
	private String[] players;

	public Game(int gameId, String player1, String player2) {
		this.board = new Board(6, 7);
		this.numMoves = 0;
		this.gameId = gameId;
		this.players = new String[]{player1, player2};
		this.nextPlayer = 1;
	}
	
	public int getGameId() {
		return this.gameId;
	}
	
	public String getPlayer(int playerNum) {
		return this.players[playerNum-1];
	}
	
	public int getPlayer(String playerId) {
		for(int i = 0; i < this.players.length; i++) {
			if(playerId.equals(this.players[i])) {
				return i+1;
			}
		}
		return -1;
	}
	
	public boolean setPlayer(int playerNum, String playerId) {
		if(playerNum < 1 || playerNum > 2) {
			return false;
		} else {
			this.players[playerNum-1] = playerId;
			return true;
		}
	}
	
	public int getNextPlayer() {
		return this.nextPlayer;
	}
	
	public int getNumMoves() {
		return this.numMoves;
	}
	
	public Board getBoard() {
		return this.board;
	}
	
	public Color getNextColor() {
		if(this.nextPlayer == 1) {
			return Color.RED;
		} else {
			return Color.YELLOW;
		}
	}
	
	public Color getWinner() {
		return this.winner;
	}
	
	public boolean playMove(int col, int playerNum) {
		if(playerNum != this.nextPlayer || this.winner != null) {
			return false;
		}
		
		int[] lastMove = this.board.playMove(col, new Token(getNextColor()));
		
		if(lastMove == null) {
			return false;
		} else {
			this.lastMove = lastMove;
			this.numMoves++;
		}
		
		this.winner = board.isWinner(this.lastMove[0], this.lastMove[1]);
		
		this.nextPlayer = 2-((this.nextPlayer+1)%2);
		
		return true;
		
	}

}
