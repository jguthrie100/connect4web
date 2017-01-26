package com.jguthrie.connect4web.server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jguthrie.connect4web.models.GameCollection;

public class GameServlet extends HttpServlet {
	//private final long serialVersionUID = -6154475799000019575L;
	private GameCollection gServer;
	
	public GameServlet(GameCollection gServer) {
		this.gServer = gServer;
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException,
			IOException {
		
		String htmlOut = "";
		
		if(request.getRequestURI().equals("/game")) {
			PrintWriter out = response.getWriter();
			
			htmlOut += "<h1>Welcome to Jamie's Connect 4 Game..</h1>";
			htmlOut += "<a href=\"/game/new\">Start new game</a> | ";
			htmlOut += "<a href=\"/game/join\">Join a game</a> | ";
			htmlOut += "<a href=\"/game/load\">Load a game</a>";
			
			out.println(htmlOut);
			
		} else if(request.getRequestURI().equals("/game/new")) {
			
			int gameId = gServer.newGame();
			request.setAttribute("game", gServer.getGame(gameId));
            request.getRequestDispatcher("/game/"+ gameId + "/").forward(request, response);
            
		} else if(request.getRequestURI().matches("/game/\\d+(/*?)")) {
			
			try {
				int gameId = Integer.parseInt(request.getRequestURI().split("/")[2]);
				request.setAttribute("gameid", gameId);
				request.setAttribute("game", gServer.getGame(gameId));
	            request.getRequestDispatcher("/game.jsp").forward(request, response);
			} catch (Throwable el) {
				el.printStackTrace();
			}
		
		}
	}
}
