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
	
	public int newGame() {
		
		if(getGame(this.nextGameId) != null) {
			this.nextGameId++;
			return newGame();
		} else {
			Game g = new Game();
			games.put(this.nextGameId, g);
			DBAccessor.initNewGame(this.nextGameId);
		}
		
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
		Game g = new Game();
		
		int[] moves = DBAccessor.loadGameMovesHistory(gameId);
		
		if(moves == null) {
			return null;
		}
		
		for(int i = 0; i < moves.length; i++) {
			g.playMove(moves[i], g.getNextColor());
		}
		
		games.put(gameId, g);
		
		return g;
	}

}
