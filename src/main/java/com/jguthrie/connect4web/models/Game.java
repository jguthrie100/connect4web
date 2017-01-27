package com.jguthrie.connect4web.models;

import com.jguthrie.connect4web.models.Token.Color;

/**
 * A model of the Game that handles the logic around making a move and working out
 *   when there is a winner
 * @author jguthrie100
 *
 */
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
	
	public String getPlayerTag(int playerNum) {
		return this.players[playerNum-1];
	}
	
	public int getPlayerNum(String playerId) {
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
	
	/**
	 * Get next colour to be played
	 * @return
	 */
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
	
	/**
	 * Iterate through every cell to see if there is a winning move
	 * @return
	 */
	public Color determineWinner() {
		for(int row = 0; row < board.getNumRows(); row++) {
			for(int col = 0; col < board.getNumCols(); col++) {
				if(board.fourInARow(row, col)) {
					return board.getCell(row, col).getColor();
				}
			}
		}
		return null;
	}
	
	/**
	 * Only search around the specified cell, seeing if its part of a winning move
	 * @param row
	 * @param col
	 * @return
	 */
	public Color determineWinner(int row, int col) {
		board.checkRowCol(row, col);
		
		if(board.fourInARow(row, col)) {
			return board.getCell(row, col).getColor();
		} else {
			return null;
		}
	}

	/**
	 * Handle's the logic of making a move, dropping the piece into the board,
	 *  determining whether there is a winner or not, and updating which player should go next.
	 * @param col
	 * @param playerNum
	 * @return
	 */
	public boolean playMove(int col, int playerNum) {
		if(playerNum != this.nextPlayer || this.winner != null) {
			return false;
		}
		
		int[] lastMove = board.dropToken(col, new Token(getNextColor()));
		
		// If last move couldn't be made (i.e. dropping a token on a full column), return false
		if(lastMove == null) {
			return false;
		} else {
			this.lastMove = lastMove;
			this.numMoves++;
		}
		
		this.winner = determineWinner(this.lastMove[0], this.lastMove[1]);
		
		this.nextPlayer = 2-((this.nextPlayer+1)%2);
		
		return true;
		
	}

}
