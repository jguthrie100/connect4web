package com.jguthrie.connect4web.server;

import java.sql.*;

/**
 * Handles all interactions with the database.
 * 
 * @author jguthrie100
 *
 */
public class DBAccessor {
	
	final static String USERNAME = "java";
	final static String PASSWORD = "javapass1";
	final static String DATABASE = "connect4web";
	final static String GAMES_TABLE = "games";
	final static String JDBC_URL = "jdbc:mysql://localhost:3306/" + DATABASE + "?useSSL=false";
	
	/**
	 * Prepare the database by creating database and Game table if not already present.
	 */
	public static void init() {
		
		String temp_url = "jdbc:mysql://localhost:3306/?useSSL=false";
		
		try (Connection connection = DriverManager.getConnection(temp_url, USERNAME, PASSWORD)) {
			
			Statement stmt = connection.createStatement();
			stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS `" + DATABASE + "`");
			
			stmt.executeUpdate("USE `" + DATABASE + "`");
			
			String createGamesTable = "CREATE TABLE IF NOT EXISTS `" + GAMES_TABLE + "`" +
										"(" +
											"`id` INT(11) NOT NULL AUTO_INCREMENT," +
											"`password` VARCHAR(250)," +
											"`player1` VARCHAR(250)," +
											"`player2` VARCHAR(250)," +
											"`moves` TEXT," +
											"PRIMARY KEY (`id`)" +
										")";
			
			stmt.executeUpdate(createGamesTable);
	        
		} catch (SQLException e) {
		    throw new IllegalStateException("Cannot create '" + DATABASE + "' database / '" + GAMES_TABLE + "' table!", e);
		}
	}
	
	/**
	 * Creates a Game instance by creating a new row in the table containing game id and player names
	 * @param gameId
	 * @param player1
	 * @param player2
	 * @return
	 */
	public static boolean initNewGame(int gameId, String player1, String player2) {
		try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
			PreparedStatement stmt = connection.prepareStatement("INSERT INTO " + GAMES_TABLE + " (`id`, `player1`, `player2`) VALUES (?, ?, ?)");
			stmt.setInt(1, gameId);
			stmt.setString(2, player1);
			stmt.setString(3, player2);
	        stmt.executeUpdate();
	        
		} catch (SQLException e) {
		    throw new IllegalStateException("Couldn't initialize new game!", e);
		}
		
		return true;
	}
	
	/**
	 * Loads the list of moves that have been made in an existing game
	 * @param gameId
	 * @return Array of integers containing the game moves
	 */
	public static int[] loadGameMovesHistory(int gameId) {
		try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
			PreparedStatement stmt = connection.prepareStatement("SELECT moves FROM " + GAMES_TABLE + " WHERE id = ?");
			stmt.setInt(1, gameId);
	        ResultSet rs = stmt.executeQuery();
	        
	        while (rs.next()) {
	        	if(rs.getBlob(1) == null) {
	        		return new int[]{};
	        	} else {
		        	String[] moves = rs.getString(1).split(" ");
		        	int[] output = new int[moves.length];
		        	for(int i = 0; i < moves.length; i++) {
		        		output[i] = Integer.parseInt(moves[i]);
		        	}
		        	return output;
	        	}
	        }
	        
		} catch (SQLException e) {
		    throw new IllegalStateException("Cannot load game history!", e);
		}
		
		// Returns null if gameId doesn't exist (i.e. no rows were returned)
		return null;
	}
	
	/**
	 * Loads the two player 'tags' that belong to the specified game
	 * @param gameId
	 * @return Array with two cells, each one containing a player name
	 */
	public static String[] loadPlayersFromDB(int gameId) {
		try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
			PreparedStatement stmt = connection.prepareStatement("SELECT player1, player2 FROM " + GAMES_TABLE + " WHERE id = ?");
			stmt.setInt(1, gameId);
	        ResultSet rs = stmt.executeQuery();
	        
	        while (rs.next()) {
	        	String[] players = {rs.getString(1), rs.getString(2)};
	        	return players;
	        }
	        
		} catch (SQLException e) {
		    throw new IllegalStateException("Cannot load game history!", e);
		}
		
		// Returns null if gameId doesn't exist (i.e. no rows were returned)
		return null;
	}
	
	/**
	 * Appends a move to the list of existing moves for a specific Game,
	 *   building up a record of all the moves in a each game
	 * @param gameId
	 * @param move
	 * @return
	 */
	public static boolean saveMove(int gameId, int move) {
		try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
			PreparedStatement stmt = connection.prepareStatement("UPDATE " + GAMES_TABLE + " SET moves = LTRIM(concat(ifnull(moves,''), ?)) WHERE id = ?");
			stmt.setString(1, " " + move);
			stmt.setInt(2, gameId);
	        stmt.executeUpdate();
	        
		} catch (SQLException e) {
		    throw new IllegalStateException("Couldn't save game move!", e);
		}
		
		return true;
	}
	
	/**
	 * Returns the ID to be used for the next Game (ie. the next value of the game tables AUTO_INCREMENT value)
	 * @return
	 */
	public static int getNextGameId() {
		try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
			PreparedStatement stmt = connection.prepareStatement("SELECT `AUTO_INCREMENT` from INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = ? AND TABLE_NAME = ?");
			stmt.setString(1, DATABASE);
			stmt.setString(2, GAMES_TABLE);
	        ResultSet rs = stmt.executeQuery();
	        
	        while (rs.next()) {
		        return rs.getInt(1);
	        }
	        
		} catch (SQLException e) {
		    throw new IllegalStateException("Cannot load next game ID!", e);
		}
		
		return -1;
	}
}
