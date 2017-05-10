package com.web.javsterisk.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.util.resource.ResourceCollection;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;

import com.web.javsterisk.controller.CdrController;

public class MainServer {
	
	private static final Logger log = LogManager.getLogger(MainServer.class);
	
//	private final Server server;
	public static final String SERVER_REFERENCE = "jettyInMemory";
	
	public static void main(String[] args) throws Exception {

		Server server = new Server();

        Connector connector = new SelectChannelConnector();
        connector.setPort(8080);
        connector.setHost("127.0.0.1");
        server.addConnector(connector);

        WebAppContext wac = new WebAppContext();
        
//        wac.setcont  .setcon setContextPath("/javsterisk");
        
//        wac.setBaseResource(
//            new ResourceCollection(
//                new String[] {"./src/main/webapp", "./target"}));
//        
//        wac.setResourceAlias("/WEB-INF/classes/", "/classes/");

//        server.setHandler(wac);
        server.setStopAtShutdown(true);
        server.start();
        server.join();
	}
	
}
