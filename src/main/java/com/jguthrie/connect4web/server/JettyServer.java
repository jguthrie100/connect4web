package com.jguthrie.connect4web.server;

import java.io.File;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;

import com.jguthrie.connect4web.models.GameCollection;

public class JettyServer {

	private static GameCollection gServer;
	
	public static void main(String[] args) throws Exception {
	    Server server = new Server(8080);
	    gServer = new GameCollection();

	    WebAppContext webappcontext = new WebAppContext();
	    webappcontext.setContextPath("/");
  		webappcontext.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern",".*/[^/]*jstl.*\\.jar$");
  		org.eclipse.jetty.webapp.Configuration.ClassList classlist = org.eclipse.jetty.webapp.Configuration.ClassList.setServerDefault(server);
          classlist.addAfter("org.eclipse.jetty.webapp.FragmentConfiguration", "org.eclipse.jetty.plus.webapp.EnvConfiguration", "org.eclipse.jetty.plus.webapp.PlusConfiguration");
          classlist.addBefore("org.eclipse.jetty.webapp.JettyWebXmlConfiguration", "org.eclipse.jetty.annotations.AnnotationConfiguration");

	    File warPath = new File("/home/jguthrie100/eclipse-workspace/connect4web", "src/main/connect4web");
	    webappcontext.setWar(warPath.getAbsolutePath());
	    
	    HandlerList handlers = new HandlerList();
	    webappcontext.addServlet(new ServletHolder(new GameServlet(gServer)), "/game/*");

	    handlers.setHandlers(new Handler[] { webappcontext, new DefaultHandler() });
	    server.setHandler(handlers);
	    server.start();
	}
}
