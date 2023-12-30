package com.devashish.framework.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class SocketServer {

	private static int portNumber = 12345;

	public static void setPortNumber(int portNumber) {
		SocketServer.portNumber = portNumber;
	}

	public static void startServer() {

		try {
			ServerSocket serverSocket = new ServerSocket(portNumber);
			System.out.println("Server is listening on port " + portNumber);

			while (true) {
				Socket clientSocket = serverSocket.accept();
				System.out.println("Accepted connection from " + clientSocket.getInetAddress());
				Thread clientHandlerThread = new Thread(() -> handleClient(clientSocket));
				clientHandlerThread.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void handleClient(Socket clientSocket) {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				PrintWriter writer = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true)) {
			String inputLine;
			boolean responseSend = false;
			while ((inputLine = reader.readLine()) != null) {
				System.out.println("Received from " + clientSocket.getInetAddress() + ": " + inputLine);
				if(!responseSend) {
					String[] firstLineSplit = inputLine.split(" ");
					String urlMapping = firstLineSplit[1];
					String queryParams = null;
					if(urlMapping.indexOf("?")>-1) {
						String[] urlMappingSplit = urlMapping.split("[?]");
						if(urlMappingSplit.length==2) {
							urlMapping = urlMappingSplit[0];
							queryParams = urlMappingSplit[1];
						}
					}
					Map<String,String> queryParamsMap = convertQueryParamsToMap(queryParams);
					String response = HttpServer.handleRequest(urlMapping,queryParamsMap);
					responseSend = true;
					writer.println(response);
				}
			}
			System.out.println("Connection with " + clientSocket.getInetAddress() + " closed.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static Map<String,String> convertQueryParamsToMap(String queryParams) {
		if(queryParams==null) {
			return null;
		}
		Map<String,String> queryParamsMap = new HashMap<String,String>();
		String[] queryParamsSplit = queryParams.split("&");
		for(String queryParam : queryParamsSplit) {
			String[] kv = queryParam.split("=");
			if(kv.length==2) {
				queryParamsMap.put(kv[0].trim(), kv[1].trim());
			}
		}
		return queryParamsMap;
	}
}
