package com.vbitz.MinecraftScript.web;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class StaticFileEndpoint implements HttpHandler {

	private static HashMap<String, String> _resTypes = new HashMap<String, String>();
	
	private static final int readSize = 2 * 1024 * 1024;
	
	static {
		_resTypes.put("html", "text/html");
		_resTypes.put("js", "text/js");
	}
	
	private static String getFileExtention(String filename) {
		String[] tokens = filename.split("\\.");
		return _resTypes.get(tokens[tokens.length - 1]);
	}
	
	@Override
	public void handle(HttpExchange ex) {
		try {
			String filename = ex.getRequestURI().getPath();
			filename = filename.substring("/static/".length(), filename.length());
			if (filename.startsWith("..") || filename.endsWith("..")) {
				ex.sendResponseHeaders(403, 0);
				ex.close();
				return;
			}
			filename = "/com/vbitz/MinecraftScript/htmlsrc/" + filename;
			URL fileURL = this.getClass().getResource(filename);
			if (fileURL == null) {
				ex.sendResponseHeaders(404, 0);
				ex.close();
				return;
			}
			InputStream str = this.getClass().getResourceAsStream(filename);
			ex.sendResponseHeaders(200, 0);
			OutputStream resStr = ex.getResponseBody();
			int writenCount = 0;
			while (str.available() > 0) {
				int arrLength = readSize;
				if (str.available() < readSize) {
					arrLength = str.available();
				}
				byte[] buf = new byte[arrLength];
				writenCount += str.read(buf);
				resStr.write(buf);
			}
			resStr.close(); // this sometimes fails
			str.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
