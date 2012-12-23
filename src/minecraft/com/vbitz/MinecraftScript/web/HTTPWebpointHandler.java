package com.vbitz.MinecraftScript.web;

import java.io.IOException;

import org.mozilla.javascript.Function;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class HTTPWebpointHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange ex) throws IOException {
		byte[] htmlBody = "<html><head></head><body>Hello World</body></html>".getBytes();
		ex.getResponseHeaders().add("Content-Type", "text/html");
		ex.sendResponseHeaders(200, htmlBody.length);
		ex.getResponseBody().write(htmlBody);
		ex.getRequestBody().close();
	}

	public void addFunction(String name, Function func) {
		// TODO Auto-generated method stub
		
	}

}
