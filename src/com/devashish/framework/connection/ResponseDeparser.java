package com.devashish.framework.connection;

import com.devashish.framework.annotations.ResponseType.TYPE;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class ResponseDeparser {
	private String body;
	public ResponseDeparser(Object obj,TYPE type) {
		if(type==TYPE.XML) {
			XmlMapper xmlMapper = new XmlMapper();
			try {
				this.body = xmlMapper.writeValueAsString(obj);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		} else if(type==TYPE.JSON) {
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				this.body = objectMapper.writeValueAsString(obj);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
}
