package com.devashish.controller;

import com.devashish.beans.Employee;
import com.devashish.beans.Person;
import com.devashish.framework.annotations.GetMapping;
import com.devashish.framework.annotations.PostMapping;
import com.devashish.framework.annotations.RequestBody;
import com.devashish.framework.annotations.RequestParam;
import com.devashish.framework.annotations.ResponseType;
import com.devashish.framework.annotations.ResponseType.TYPE;
import com.devashish.framework.annotations.RestController;
import com.devashish.framework.injectable.HttpHeader;

@RestController
public class ExamplesController {
	@GetMapping("/hello1")
	public String sayHello1() {
		return "<h1>name is ${name} and id is ${id}</h1>, <input type='text'>";
	}

	@GetMapping("/hello2")
	public String sayHello2(@RequestParam("name") String name, HttpHeader httpHeaders) {
		return "Hello2, <input type='text' value='" + name.toUpperCase() 
		        + "'><p>" + httpHeaders.get("user-agent")
				+ "</p><p>" + httpHeaders.get("Host") + "</p>";
	}
	
	@GetMapping("/xmlapi")
	@ResponseType(TYPE.XML)
	public String callXmlAPi() {
		return "<html><head></head><body><p>Hello</p></body></html>";
	}
	
	@GetMapping("/jsonapi")
	@ResponseType(TYPE.JSON)
	public String callJsonAPi() {
		return "{'name':'Devashish','id':123}";
	}
	
	@PostMapping("/xmlpostapireqjson")
	@ResponseType(TYPE.XML)
	public Employee callXmlPostAPiReqJson(@RequestBody(TYPE.JSON) Person body) {
		Employee emp = new Employee();
		emp.setPerson(body);
		emp.setEmployeeId("123");
		return emp;
	}
	
	@PostMapping("/xmlpostapireqxml")
	@ResponseType(TYPE.JSON)
	public Employee callXmlPostAPiReqXml(@RequestBody(TYPE.XML) Person body) {
		Employee emp = new Employee();
		emp.setPerson(body);
		emp.setEmployeeId("123");
		return emp;
	}
	
	@PostMapping("/xmlpostapi")
	@ResponseType(TYPE.XML)
	public String callXmlPostAPi(@RequestBody(TYPE.JSON) String body) {
		return "<html><head></head><body><p>Hello</p>"+
	           "<script>let a = "+body+"</script>"+
			   "</body></html>";
	}
}
