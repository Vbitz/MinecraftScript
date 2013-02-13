package com.vbitz.MinecraftScript;

import org.mozilla.javascript.Callable;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.Scriptable;

public class MinecraftScriptContextFactory extends ContextFactory {
	private static class WatchedContext extends Context {
		public long startTime;
		
		@Override
		public boolean hasFeature(int featureIndex) {
			if (featureIndex == FEATURE_ENHANCED_JAVA_ACCESS && MinecraftScriptMod.getUnsafeEnabled()) {
				return true;
			}
			return super.hasFeature(featureIndex);
		}
	}
	
	public static void setup() {
		ContextFactory.initGlobal(new MinecraftScriptContextFactory());
	}
	
    protected Context makeContext()
    {
    	WatchedContext cx = new WatchedContext();
        // Make Rhino runtime to call observeInstructionCount
        // each 2000 bytecode instructions
        cx.setInstructionObserverThreshold(2000);
        cx.setWrapFactory(new MinecraftScriptWrapFactory());
        return cx;
    }
    
    protected void observeInstructionCount(Context cx, int instructionCount)
    {
    	WatchedContext mcx = (WatchedContext)cx;
        long currentTime = System.currentTimeMillis();
        if (currentTime - mcx.startTime > 1000) {
            throw new Error("Script ran for too long");
        }
    }

    protected Object doTopCall(Callable callable,
                               Context cx, Scriptable scope,
                               Scriptable thisObj, Object[] args)
    {
    	WatchedContext mcx = (WatchedContext)cx;
        mcx.startTime = System.currentTimeMillis();

        return super.doTopCall(callable, cx, scope, thisObj, args);
    }
    
    
}
