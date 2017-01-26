package com.jguthrie.connect4web.models;

import com.jguthrie.connect4web.models.Token.Color;

public class Player {
	
	private int playerNum;
	private String playerId;
	private Color color;
	
	public Player(int playerNum, String playerId) {
		if(playerNum < 1 || playerNum > 2) {
			throw new IndexOutOfBoundsException("Player num must be either 1 or 2!");
		}
		
		this.playerNum = playerNum;
		this.playerId = playerId;
		
		if(playerNum == 1) {
			this.color = Color.RED;
		} else {
			this.color = Color.YELLOW;
		}
	}

	public int getPlayerNum() {
		return this.playerNum;
	}
	
	public String getPlayerId() {
		return this.playerId;
	}
	
	public Color getColor() {
		return this.color;
	}
}
