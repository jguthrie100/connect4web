package com.jguthrie.connect4web.server;

import com.jguthrie.connect4web.models.GameCollection;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This servlet waiting/looping and checking to see if the Game object has had any moves made in it.
 *  If a move has been made (i.e. by the other player), then it sends a message to the client and exits.
 * @author jguthrie100
 *
 */
public class UpdatesServlet extends HttpServlet {
	private static final long serialVersionUID = -6154475799000019575L;
	private GameCollection gServer;
	
	public UpdatesServlet(GameCollection gServer) {
		this.gServer = gServer;
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException,
			IOException {
		
        response.setContentType("text/event-stream");
        response.setHeader("Cache-Control", "no-cache");
        response.setCharacterEncoding("UTF-8");
        
        int gameId = Integer.parseInt(request.getRequestURI().split("/")[3]);
        
        int numMoves = gServer.getGame(gameId).getNumMoves();
        
        while(true) {
        	if(gServer.getGame(gameId).getNumMoves() != numMoves) {
        		PrintWriter writer = response.getWriter();
        		writer.write("data: " + "refreshpage" + "\r\n");
        		writer.flush();
                writer.close();
                break;
        	} else {
        		try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
        	}
        } 
	}
}
