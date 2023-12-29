package com.devashish.main;

import com.devashish.framework.connection.HttpServer;
import com.devashish.framework.connection.SocketServer;

public class MainClass {
	public static void main(String[] args) {
    	HttpServer.scanAndInitializeControllers("com.devashish.controller");
    	
    	SocketServer.setPortNumber(8080);
    	SocketServer.startServer();
	}
}
