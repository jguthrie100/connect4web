package com.jguthrie.glucktest.models;

import java.util.HashMap;

import com.jguthrie.glucktest.server.DBAccessor;

public class GameCollection {
	
	private DBAccessor dba;
	private HashMap<Integer, Game> games;

	private int nextGame = 0;
	
	public GameCollection() {
		games = new HashMap<Integer, Game>();
		dba = new DBAccessor();
	}
	
	public Game getGame(int gameId) {
		return games.get(gameId);
	}
	
	public int newGame() {
		Game g = new Game();
		games.put(this.nextGame++, g);
		
		return this.nextGame-1;
	}
	
	public Game loadGame(int gameId) {
		Game g = new Game();
		
		int[] moves = dba.loadGameHistory(gameId);
		
		for(int i = 0; i < moves.length; i++) {
			g.playMove(moves[i], g.getNextColor());
		}
		
		games.put(gameId, g);
		
		return g;
	}

}
