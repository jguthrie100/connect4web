package com.jguthrie.connect4web.models;

import java.util.HashMap;

import com.jguthrie.connect4web.server.DBAccessor;

/**
 * Stores a collection of all the games and handles creating new games / loading existing games etc
 * @author jguthrie100
 *
 */
public class GameCollection {
	
	private HashMap<Integer, Game> games;
	private int nextGameId;
	
	public GameCollection() {
		games = new HashMap<Integer, Game>();
		this.nextGameId = DBAccessor.getNextGameId();
	}
	
	/**
	 * Creates a new Game object
	 * @param player1
	 * @param player2
	 * @return Id number of the newly created game
	 */
	public int newGame(String player1, String player2) {
		
		// If game with same id doesn't already exist, then create game
		if(getGame(this.nextGameId) == null) {
			Game g = new Game(this.nextGameId, player1, player2);
			games.put(this.nextGameId, g);
			DBAccessor.initNewGame(this.nextGameId, player1, player2);
			
			return this.nextGameId++;
		
		} else {
			this.nextGameId++;
			return newGame(player1, player2);
		}
	}
	
	/**
	 * Retrieves an existing game either from the HashMap collection or the database
	 * @param gameId
	 * @return
	 */
	public Game getGame(int gameId) {
		if(games.containsKey(gameId)) {
			return games.get(gameId);
		} else {
			return loadGameFromDB(gameId);
		}
	}
	
	/**
	 * Loads a game from the database
	 *  - Loads all the old moves and then runs them through the new game object
	 * @param gameId
	 * @return Game object containing all the existing moves
	 */
	public Game loadGameFromDB(int gameId) {
		String[] players = DBAccessor.loadPlayersFromDB(gameId);
		int[] moves = DBAccessor.loadGameMovesHistory(gameId);
		
		if(moves == null) {
			return null;
		}
		
		Game g = new Game(gameId, players[0], players[1]);
		
		// Run through old moves and build up the game board
		for(int i = 0; i < moves.length; i++) {
			g.playMove(moves[i], g.getNextPlayer());
		}
		
		games.put(gameId, g);
		
		return g;
	}

}
