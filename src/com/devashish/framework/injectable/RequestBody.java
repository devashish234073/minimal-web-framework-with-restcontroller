package com.devashish.framework.injectable;

public class RequestBody<T> {
	private T body;

	public RequestBody() {
		
	}
	
	public RequestBody(T body) {
		this.body = body; 
	}
	
	public T getBody() {
		return body;
	}

	public void setBody(T body) {
		this.body = body;
	}
	
}
