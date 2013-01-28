package com.vbitz.MinecraftScript.scripting.javascript;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.EcmaError;
import org.mozilla.javascript.EvaluatorException;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Undefined;

import com.vbitz.MinecraftScript.MinecraftScriptClassShutter;
import com.vbitz.MinecraftScript.MinecraftScriptContextFactory;
import com.vbitz.MinecraftScript.MinecraftScriptMod;
import com.vbitz.MinecraftScript.exceptions.InternalScriptingException;
import com.vbitz.MinecraftScript.scripting.IFunction;
import com.vbitz.MinecraftScript.scripting.ScriptRunner;
import com.vbitz.MinecraftScript.scripting.ScriptingGlobals;
import com.vbitz.MinecraftScript.scripting.ScriptingManager;

public class JSScriptingManager extends ScriptingManager {

	static {
		_instance = new JSScriptingManager();
	}
	
	private static JSScriptingManager _instance;
	
	private Context mcJavascriptContext = null;
	private ScriptableObject mcJavascriptScope = null;
	
	private File _scriptsDirectory = null;
	
	private HashMap<String, FunctionObject> _calls = new HashMap<String, FunctionObject>();
	
	private ScriptRunner _runner;
	
	public void enterContext() {
		if (Context.getCurrentContext() != null) {
			Context.exit();
		}
		ContextFactory.getGlobal().enterContext(mcJavascriptContext);
	}
	
	public void exitContext() {
		if (Context.getCurrentContext() != null) {
			Context.exit();
		}
	}
	
	public boolean inContext() {
		return Context.getCurrentContext() != null;
	}
	
	@Override
	public String getName() {
		return "javascript";
	}

	@Override
	public void onServerLoad() {
		MinecraftScriptContextFactory.setup();
		mcJavascriptContext = Context.enter();
		mcJavascriptContext.setClassShutter(new MinecraftScriptClassShutter());
		loadScope(true);
		Context.exit();
	}

	@Override
	public void onServerUnload() {
		
	}
	
	@Override
	public void initScope() {
		mcJavascriptScope = mcJavascriptContext.initStandardObjects();
	}

	@Override
	public void loadAllScripts(File scriptsDir) {
		_scriptsDirectory = scriptsDir;
		
		enterContext();
		
		mcJavascriptScope.put("isPreload", mcJavascriptScope, true);
		Logger.getLogger("MinecraftScriptMod").info("Loading Startup Scripts");
		for (File f : scriptsDir.listFiles()) {
			try {
				if (!f.getName().startsWith("init_")) { // this should make including libarys easier and should pave the way for
														// a require compatable system
					continue;
				}
				MinecraftScriptMod.getLogger().info("Loading " + f.toString());
				FileReader fr = new FileReader(f);
				mcJavascriptContext.evaluateReader(mcJavascriptScope, fr, f.getName(), 0, null);
			} catch (FileNotFoundException e) {
				MinecraftScriptMod.getLogger().severe("Could not find " + f.getName());
			} catch (IOException e) {
				MinecraftScriptMod.getLogger().severe(e.toString());
			} catch (Error e) {
				MinecraftScriptMod.getLogger().severe("Error Loading " + f.getName());
			}
		}
		mcJavascriptScope.put("isPreload", mcJavascriptScope, false);
		
		exitContext();
	}

	@Override
	public void reload() {
		MinecraftScriptMod.getLogger().fine("Reloading Scripting Scope");
		loadScope(false);
		loadAllScripts(_scriptsDirectory);
	}

	@Override
	public Object runFunction(ScriptRunner runner, IFunction func, Object... args) throws InternalScriptingException {
		return func.call(mcJavascriptContext, mcJavascriptScope, mcJavascriptScope, args);
	}

	@Override
	public Object runString(String str, ScriptRunner runner) throws InternalScriptingException {
		Object ret = null;
		try {
			enterContext();
			_runner = runner;
			ret = mcJavascriptContext.compileString(str, "command", 0, null).exec(mcJavascriptContext, mcJavascriptScope);
			_runner = null;
		} catch (EcmaError e) {
			throw new InternalScriptingException(e.getMessage());
		} catch (EvaluatorException e) {
			throw new InternalScriptingException(e.getMessage());
		} catch (Error e) {
			throw new InternalScriptingException(e.getMessage());
		} finally {
			exitContext();
		}
		return ret;
	}

	@Override
	public void runFile(String filename, ScriptRunner runner) throws InternalScriptingException {
		if (!inContext()) {
			enterContext();
		}
		File fullPath = new File(_scriptsDirectory, filename);
		FileReader fr = null;
		try {
			fr = new FileReader(fullPath);
		} catch (FileNotFoundException e) {
			throw new InternalScriptingException("Could not find file");
		}
		try {
			mcJavascriptContext.evaluateReader(mcJavascriptScope, fr, fullPath.getName(), 0, null);
		} catch (IOException e) {
			throw new InternalScriptingException("Error Running file");
		}
		if (!inContext()) {
			exitContext();
		}
	}

	@Override
	public void addGlobal(String name, Object obj) {
		mcJavascriptScope.put(name, mcJavascriptScope, obj);
	}
	
	@Override
	public void addGlobal(String name, Class cls, String method,
			Class<?>... args) throws SecurityException, NoSuchMethodException {
		mcJavascriptScope.put(name, mcJavascriptScope, new FunctionObject(name, cls.getMethod(method, args), mcJavascriptScope));
	}

	@Override
	public ScriptRunner getScriptRunner() {
		return _runner;
	}

	@Override
	public IFunction getFunction(Object obj) {
		if (obj instanceof Function) {
			return new JSFunction((Function) obj);
		} else {
			return null;
		}
	}

	public static JSScriptingManager getInstance() {
		return _instance;
	}

	@Override
	public String getTidyOutput(Object obj) {
		if (obj == null) {
			return "";
		}
		if (obj instanceof Undefined) {
			return "";
		} else if (obj instanceof String) {
			return (String)obj;
		} else if (obj instanceof Integer) {
			return ((Integer)obj).toString();
		} else if (obj instanceof Boolean) {
			return ((Boolean)obj).toString();
		} else if (obj instanceof NativeArray) {
			String ret = "[";
			for (Object object : ((NativeArray) obj).getAllIds()) {
				ret += getTidyOutput(((NativeArray) obj).get(object)) + ", ";
			}
			return ret + "]";
		} else if (obj instanceof NativeJavaObject) {
			NativeJavaObject natObj = (NativeJavaObject)obj;
			if (!(natObj.unwrap() instanceof String)) {
				return "JavaObject [" + natObj.unwrap().getClass().getName() + "]";
			} else {
				return (String) natObj.unwrap();
			}
		} else {
			return obj.toString();
		}
	}
	
	@Override
	public void addExtCall(String jsName, Class<?> classObj, String methodName,
			Class<?>... methodArgs) throws SecurityException,
			NoSuchMethodException {
		_calls.put(jsName, new FunctionObject(jsName, classObj.getMethod(methodName, methodArgs), mcJavascriptScope));
	}
	
	@Override
	public Object extCall(String jsName, Object[] args) {
		return _calls.get(jsName).call(mcJavascriptContext, mcJavascriptScope, mcJavascriptScope, args);
	}

	public String getFunctionSrc(IFunction func) {
		if (func instanceof JSFunction) {
			return mcJavascriptContext.decompileFunction(((JSFunction) func).getFunction(), 1);
		} else {
			return "";
		}
	}
}
