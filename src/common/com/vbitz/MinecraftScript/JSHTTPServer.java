package com.vbitz.MinecraftScript;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.mozilla.javascript.EcmaError;
import org.mozilla.javascript.EvaluatorException;
import org.mozilla.javascript.Function;

public class JSHTTPServer {

	private static final int serverPort = 12524;
	
	private static HashMap<String, Function> _functions = new HashMap<String, Function>();
	
	private static ServerSocketChannel _sock;
	
	public static void addFunction(String str, Function fun) {
		_functions.put(str, fun);
	}
	
	public static void init() {
		try {
			_sock = _sock.open();
			_sock.socket().bind(new InetSocketAddress(serverPort));
			_sock.configureBlocking(false);
		} catch (IOException e) {
			MinecraftScriptMod.getLogger().severe("Could not start WebServer");
			e.printStackTrace();
			return;
		}
	}
	
	public static Object runWebpoint(String pntName, Object[] pntArgs) {
		return ScriptingManager.runFunction(_functions.get(pntName), pntArgs);
	}
	
	public static HashMap<String, String> handleReq(HashMap<String, String> headers) {
		HashMap<String, String> ret = new HashMap<String, String>();
		
		ret.put("Server", "MinecraftScript WebServer");
		ret.put("_VERSION", headers.get("_VERSION"));
		
		try {
			URL url = new URL("http://" + headers.get("Host") + headers.get("_PATH"));
			if (url.getPath() != "/") {
				String commandName = url.getPath().substring(1);
				//System.out.println(commandName);
				if (_functions.containsKey(commandName)) {
					ret.put("_STATUS", "200 OK");
					ArrayList<String> args = new ArrayList<String>();
					try {
						ScriptingManager.enterContext();
						if (args.size() > 0) {
							String out = ScriptingManager.getTidyOutput(runWebpoint(commandName, args.toArray()));
							if (!out.equals("")) { ret.put("_DATA", out); }
						} else {
							String out = ScriptingManager.getTidyOutput(runWebpoint(commandName, new Object[] { }));
							if (!out.equals("")) { ret.put("_DATA", out); }
						}
					} catch (EcmaError e) {
						ret.put("_DATA", "Error: " + e.getMessage());
						ScriptingManager.exitContext();
					} catch (EvaluatorException e) {
						ret.put("_DATA", "Error: " + e.getMessage());
						ScriptingManager.exitContext();
					}
				} else {
					ret.put("_STATUS", "404 NOT FOUND");
				}
			} else {
				ret.put("_STATUS", "404 NOT FOUND");
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		return ret;
	}
	
	public static void tryDo() {
		SocketChannel sc = null;
		try {
			sc = _sock.accept();
			if (sc == null) { return; }
			BufferedReader req = new BufferedReader(new InputStreamReader(sc.socket().getInputStream()));
			BufferedWriter res = new BufferedWriter(new OutputStreamWriter(sc.socket().getOutputStream()));
			
			HashMap<String, String> headers = new HashMap<String, String>();
			
			String[] detailLineTokens = req.readLine().split(" ");
			
			headers.put("_METHOD", detailLineTokens[0]);
			headers.put("_PATH", detailLineTokens[1]);
			headers.put("_VERSION", detailLineTokens[2]);
		
			boolean hdr = true;
			String data = "";
			
            while (req.ready()) {
            	String ln = req.readLine();
            	if (ln.length() < 5) {
            		hdr = false;
            		continue;
            	}
            	headers.put(ln.substring(0, ln.indexOf(':')), ln.substring(ln.indexOf(':') + 2));
            }
            
            HashMap<String, String> responce = handleReq(headers);
            
            if (responce.containsKey("_DATA")) {
            	responce.put("Content-Length", String.valueOf(responce.get("_DATA").length()));
            } else {
            	responce.put("Content-Length", "0");
            }
            
            res.write(responce.get("_VERSION") + " " + responce.get("_STATUS") + "\r\n");
            
            for (Entry<String, String> item : responce.entrySet()) {
				if (!(item.getKey().equals("_VERSION") || item.getKey().equals("_STATUS") || item.getKey().equals("_DATA"))) {
					res.write(item.getKey() + ": " + item.getValue() + "\r\n");
				}
			}
            
            res.write("\r\n");
            res.flush();
            
            if (responce.containsKey("_DATA")) {
            	res.write(responce.get("_DATA"));
            }
            
            sc.close();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}
	
	public static void destroy() {
		try {
			_sock.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static int getPort() {
		return serverPort;
	}
	
}
