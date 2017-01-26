package com.jguthrie.connect4web.server;

import com.jguthrie.connect4web.models.Game;
import com.jguthrie.connect4web.models.GameCollection;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		
		// Main menu
		if(request.getRequestURI().equals("/")) {
			PrintWriter out = response.getWriter();
			
			htmlOut += "<h1>Welcome to Jamie's Connect 4 Game..</h1>";
			htmlOut += "<a href=\"/game/new\">Start new game</a> | ";
			htmlOut += "<a href=\"/game/join\">Join a game</a> | ";
			htmlOut += "<a href=\"/game/load\">Load a game</a>";
			
			out.println(htmlOut);
		
		// Handle starting a new game
		} else if(request.getRequestURI().matches("/game/new(/*?)")) {
			
			int gameId = gServer.newGame();
			request.setAttribute("game", gServer.getGame(gameId));
            request.getRequestDispatcher("/game/"+ gameId + "/").forward(request, response);
            
        // Handles redirecting to a specific game (and making moves)
		} else if(request.getRequestURI().matches("/game/\\d+(/*?)")) {
			
			try {
				int gameId = Integer.parseInt(request.getRequestURI().split("/")[2]);
				Game game = gServer.getGame(gameId);
				
				// Make move and save move to DB if valid
				if(request.getParameter("move") != null) {
					int move = Integer.parseInt(request.getParameter("move"));
					if(game.playMove(move, gServer.getGame(gameId).getNextColor())) {
						DBAccessor.saveMove(gameId, move);
					}
					
				}
				
				// Redirect to JSP page
				request.setAttribute("gameid", gameId);
				request.setAttribute("game", game);
	            request.getRequestDispatcher("/game.jsp").forward(request, response);
			} catch (Throwable el) {
				el.printStackTrace();
			}
		
		}
	}
}
