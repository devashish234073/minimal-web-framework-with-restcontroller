package com.devashish.main;

import com.devashish.rawspringboot.framework.HttpServer;
import com.devashish.rawspringboot.framework.SocketServer;

public class MainClass {
	public static void main(String[] args) {
    	HttpServer.scanAndInitializeControllers("com.devashish.controller");
    	
    	SocketServer.setPortNumber(8080);
    	SocketServer.startServer();
	}
}
