package com.jguthrie.connect4web.server;

import com.jguthrie.connect4web.models.Game;
import com.jguthrie.connect4web.models.GameCollection;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * The GameServlet handles directing the client when starting a new game or making a move.
 * 
 * It either responds with a 'menu' screen, starts a new Game, or displays an existing game (and 
 *   makes the requested move if a move is passed in the parameter list)
 * @author jguthrie100
 *
 */
public class GameServlet extends HttpServlet {
	private static final long serialVersionUID = -6154475799000019575L;
	
	private GameCollection gServer;
	
	public GameServlet(GameCollection gServer) {
		this.gServer = gServer;
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException,
			IOException {
		
		String htmlOut = "";
		String pid = request.getParameter("pid");
		
		// Main menu
		if(request.getRequestURI().equals("/")) {
			PrintWriter out = response.getWriter();
			
			htmlOut += "<h1>Welcome to Jamie's Connect 4 Game..</h1>";
			htmlOut += "<a href=\"/game/new/player1/player2\">Start new game</a> | ";
			htmlOut += "<a href=\"/game/join\">Join a game</a> | ";
			htmlOut += "<a href=\"/game/load\">Load a game</a>";
			
			out.println(htmlOut);
		
		// Handle starting a new game
		} else if(request.getRequestURI().matches("/game/new/(.+?)/(.+?)(/*?)")) {
			
			String player1 = request.getRequestURI().split("/")[3];
			String player2 = request.getRequestURI().split("/")[4];
			
			if(player1.equals(player2)) {
				PrintWriter out = response.getWriter();
				out.println("Players must have different names: " + player1 + " & " + player2 + " are the same..");
				out.println("<a href=\"/\">Return to home screen</a>");
			
			} else {
				int gameId = gServer.newGame(player1, player2);
	            request.getRequestDispatcher("/game/"+ gameId + "/?pid=" + player1).forward(request, response);
			}
            
        // Handles redirecting to a specific game (and making moves)
		} else if(request.getRequestURI().matches("/game/\\d+(/*?)")) {
			
			try {
				int gameId = Integer.parseInt(request.getRequestURI().split("/")[2]);
				Game game = gServer.getGame(gameId);
				
				if(game != null) {
					// Make move and save move to DB if valid
					if(request.getParameter("move") != null) {
						int move = Integer.parseInt(request.getParameter("move"));
						if(game.playMove(move, gServer.getGame(gameId).getPlayerNum(pid))) {
							DBAccessor.saveMove(gameId, move);
						}
					}
					
					// Redirect to JSP page
					request.setAttribute("gameid", gameId);
					request.setAttribute("game", game);
					request.getRequestDispatcher("/game.jsp").forward(request, response);
				
				} else {
					PrintWriter out = response.getWriter();
					out.println("GameID: " + gameId + " does not exist. Please either start a new game or join an existing one :)");
					out.println("<a href=\"/\">Return to home screen</a>");
				}
			} catch (Throwable el) {
				el.printStackTrace();
			}
		
		} else {
			request.getRequestDispatcher("/").forward(request, response);
		}
	}
}
