package com.devashish.framework.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SocketServer {

	private static int portNumber = 12345;

	public static void setPortNumber(int portNumber) {
		SocketServer.portNumber = portNumber;
	}

	public static void startServer() {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(portNumber);
			System.out.println("Server is listening on port " + portNumber);

			while (true) {
				Socket clientSocket = serverSocket.accept();
				System.out.println("Accepted connection from " + clientSocket.getInetAddress());
				Thread clientHandlerThread = new Thread(() -> handleClient(clientSocket));
				clientHandlerThread.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(serverSocket!=null) {
				try {
					serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static void handleClient(Socket clientSocket) {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				PrintWriter writer = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true)) {
			//String inputLine = reader.readLine();
			String firstLine = null;
			Map<String,String> headers  = new HashMap<String,String>();
			String req = "";
			char[] buffer = new char[100];
            int bytesRead;
            int contentLength = -1;
            String headerStr = null;
            String body = null;
            while ((bytesRead = reader.read(buffer)) != -1) {
                String content = new String(buffer, 0, bytesRead);
                req+=content;
                if(contentLength==-1 && req.toLowerCase().indexOf("content-length:")>-1) {
                	String conLen = req.substring(req.toLowerCase().indexOf("content-length:")+15);
                	conLen = conLen.substring(0,conLen.indexOf("\n")).trim();
                	System.out.println("Content-Length header value: "+conLen);
                	contentLength = Integer.parseInt(conLen);
                }
                if(contentLength==-1 && req.toLowerCase().indexOf("\r\n\r\n")>-1) {
                	break; 
                }
                if(contentLength!=-1 && req.toLowerCase().indexOf("\r\n\r\n")>-1) {
                	String[] reqSplit = req.split("\r\n\r\n");
                	if(reqSplit.length==2) {
                		headerStr = reqSplit[0];
                		body = reqSplit[1];
                		if(body.length()==contentLength) {
                			break;
                		}
                	}
                }
            }
			String[] reqSplit = req.split("\r\n");
			firstLine = reqSplit[0];
			if(headerStr==null || body==null) {
				String[] headerBodySplit = req.split("\r\n\r\n");
				headerStr = headerBodySplit[0];
				if(headerBodySplit.length==2) {
					body = headerBodySplit[1];	
				}
			}
			for(String header : headerStr.split("\r\n")) {
				if(header.indexOf(":")>-1) {
					String[] headerSplit = header.split(":",2);
					if(headerSplit.length==2) {
						headers.put(headerSplit[0].toLowerCase(), headerSplit[1]);
					}
				}
			}
			System.out.println("Header Start:\r\n"+headers+"\r\nHeader End...\r\n");
			System.out.println("Body Start:\r\n"+body+"\r\nBody End...\r\n");
			System.out.println("First line of request "+firstLine);
			if(firstLine!=null && (firstLine.split(" ")).length==3) {
				processRequest(firstLine,writer,headers,body);
			} else {
				writer.println("");
			}
			System.out.println("Connection with " + clientSocket.getInetAddress() + " closed.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void processRequest(String firstLine,PrintWriter writer,Map<String,String> headers,String body) {
		String[] firstLineSplit = firstLine.split(" ");
		String urlMapping = firstLineSplit[1];
		String requestMethod = "["+firstLineSplit[0]+"]";
		String queryParams = null;
		if(urlMapping.indexOf("?")>-1) {
			String[] urlMappingSplit = urlMapping.split("[?]");
			if(urlMappingSplit.length==2) {
				urlMapping = urlMappingSplit[0];
				queryParams = urlMappingSplit[1];
			}
		}
		Map<String,String> queryParamsMap = convertQueryParamsToMap(queryParams);
		String response = HttpServer.handleRequest(requestMethod+urlMapping,queryParamsMap,headers,body);
		writer.println(response);
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
