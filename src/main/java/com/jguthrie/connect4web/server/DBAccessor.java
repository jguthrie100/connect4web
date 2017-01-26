package com.jguthrie.connect4web.server;

import java.sql.*;

public class DBAccessor {
	
	final String USERNAME = "java";
	final String PASSWORD = "javapass1";
	final String DATABASE = "connect4web";
	final String GAMES_TABLE = "games";
	final String JDBC_URL = "jdbc:mysql://localhost:3306/" + DATABASE;
	
	
	public DBAccessor() {
		
		String temp_url = "jdbc:mysql://localhost:3306/";
		
		System.out.println("Preparing database tables..");
		
		try (Connection connection = DriverManager.getConnection(temp_url, USERNAME, PASSWORD)) {
			
			System.out.println("Creating database '" + DATABASE + "' if it doesn't already exist");
			Statement stmt = connection.createStatement();
			stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS `" + DATABASE + "`");
			
			stmt.executeUpdate("USE `" + DATABASE + "`");
			
			System.out.println("Creating table '" + GAMES_TABLE + "' if it doesn't already exist");
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
	
	public int[] loadGameHistory(int gameId) {
		
		try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
			PreparedStatement stmt = connection.prepareStatement("SELECT moves FROM " + GAMES_TABLE + " WHERE id = ?");
			stmt.setInt(1, gameId);
	        ResultSet rs = stmt.executeQuery();
	        
	        while (rs.next() && rs.getBlob(1) != null) {
	        	String[] moves = rs.getString(1).split(" ");
	        	int[] output = new int[moves.length];
	        	for(int i = 0; i < moves.length; i++) {
	        		output[i] = Integer.parseInt(moves[i]);
	        	}
	        	return output;
	        }
	        
		} catch (SQLException e) {
		    throw new IllegalStateException("Cannot load game history!", e);
		}
		
		return new int[]{};
	}
	
	public boolean saveMove(int gameId, int move) {
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
}
