package com.jguthrie.connect4web.models;

import java.util.HashMap;

import com.jguthrie.connect4web.server.DBAccessor;

public class GameCollection {
	
	private HashMap<Integer, Game> games;
	private int nextGameId;
	
	public GameCollection() {
		games = new HashMap<Integer, Game>();
		this.nextGameId = DBAccessor.getNextGameId();
	}
	
	public int newGame(String player1, String player2) {
		if(getGame(this.nextGameId) != null) {
			this.nextGameId++;
			return newGame(player1, player2);
		}
		
		Game g = new Game(this.nextGameId, player1, player2);
		games.put(this.nextGameId, g);
		DBAccessor.initNewGame(this.nextGameId, player1, player2);
		
		return this.nextGameId++;
	}
	
	public Game getGame(int gameId) {
		if(games.containsKey(gameId)) {
			return games.get(gameId);
		} else {
			return loadGameFromDB(gameId);
		}
	}
	
	public Game loadGameFromDB(int gameId) {
		String[] players = DBAccessor.loadPlayersFromDB(gameId);
		int[] moves = DBAccessor.loadGameMovesHistory(gameId);
		
		if(moves == null) {
			return null;
		}
		
		Game g = new Game(gameId, players[0], players[1]);
		
		for(int i = 0; i < moves.length; i++) {
			g.playMove(moves[i], g.getNextPlayer());
		}
		
		games.put(gameId, g);
		
		return g;
	}

}
