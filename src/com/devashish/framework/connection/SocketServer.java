package com.devashish.framework.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

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
					String response = HttpServer.handleRequest(firstLineSplit[1]);
					responseSend = true;
					writer.println(response);
				}
			}
			System.out.println("Connection with " + clientSocket.getInetAddress() + " closed.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
