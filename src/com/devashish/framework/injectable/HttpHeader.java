package com.devashish.framework.injectable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HttpHeader {
	private HashMap<String,String> httpHeaders = new HashMap<String,String>();
	public HttpHeader() {
		
	}
	public HttpHeader(Map<String,String> httpHeader) {
		final HashMap<String,String> headers = new HashMap<String,String>();
		//make all keys in lowercase
		if(httpHeader!=null) {
			httpHeader.forEach((k,v)->{
				headers.put(k.toLowerCase(), v);
			});
		}
		this.httpHeaders = headers;
	}
	public String get(String headerName) {
		return httpHeaders.get(headerName.toLowerCase());
	}
	public Set<String> getHeaderNames() {
		return this.httpHeaders.keySet();
	}
}
