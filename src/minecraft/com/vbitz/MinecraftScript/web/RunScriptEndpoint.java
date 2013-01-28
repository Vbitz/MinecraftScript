package com.vbitz.MinecraftScript.web;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

import org.mozilla.javascript.EcmaError;
import org.mozilla.javascript.EvaluatorException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.vbitz.MinecraftScript.exceptions.InternalScriptingException;
import com.vbitz.MinecraftScript.scripting.ScriptRunnerPlayer;
import com.vbitz.MinecraftScript.scripting.ScriptingManager;
import com.vbitz.MinecraftScript.scripting.javascript.JSScriptingManager;

public class RunScriptEndpoint implements HttpHandler {

	@Override
	public void handle(HttpExchange ex) throws IOException {
		InputStream request = ex.getRequestBody();
		String _requestBody = "";
		while (request.available() > 0) {
			int arrLength = 2048;
			if (request.available() < 2048) {
				arrLength = request.available();
			}
			byte[] buf = new byte[arrLength];
			if (request.read(buf) != arrLength) {
				break;
			}
			_requestBody += new String(buf);
		}
		final HttpExchange finalEX = ex;
		final String requestBody = _requestBody;
		Map<String, String> params = MinecraftScriptHTTPServer.getQueryMap(ex.getRequestURI().getQuery());
		if (!params.containsKey("apiKey")) {
			ex.sendResponseHeaders(404, 0);
			ex.close();
			return;
		}
		final String requestKey = params.get("apiKey");
		MinecraftScriptHTTPServer.getInstance().addRunnable(new Runnable() {
			// from here I'm running on a different thread
			@Override
			public void run() {
				if (MinecraftScriptAPIKey.validateKey(requestKey)) {
					String outData = "";
					EntityPlayer ply = MinecraftScriptAPIKey.getPlayer(requestKey);
					try {
						outData = JSScriptingManager.getInstance().getTidyOutput(
								JSScriptingManager.getInstance().runString(requestBody, new ScriptRunnerPlayer(ply)));
					} catch (InternalScriptingException e) {
						outData = e.getMessage();
					}
					try {
						finalEX.sendResponseHeaders(200, outData.length());
						finalEX.getResponseBody().write(outData.getBytes());
						finalEX.close();
					} catch (IOException ex) {}
				} else {
					try {
						finalEX.sendResponseHeaders(404, 0);
						finalEX.close();
					} catch (IOException ex) {}
				}
			}
		});
	}

}
