package com.yomdom.ydu.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.*;

public class YduController extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    resp.getWriter().print("Hello from Caleb!\n");
  }

  public static void main(String[] args) throws Exception{
    String portString = System.getenv("PORT");
    if(portString == null || portString.length() == 0) {
      System.out.println("Port not set...defaulting to 8080");
      portString = "8080";
    }

    int port = Integer.parseInt(portString);

    Server server = new Server(port);

    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    context.setContextPath("/");
    server.setHandler(context);
    context.addServlet(new ServletHolder(new YduController()),"/*");
    server.start();
    server.join();   
  }
}
