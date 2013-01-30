package com.vbitz.MinecraftScript.docs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.sound.sampled.Line;

public class KeyValueStore {

	private static HashMap<String, String> _items = new HashMap<String, String>();
	
	private static File _filename = null;
	
	public static void load(File filename) {
		_filename = filename;
		if (!filename.exists()) {
			return;
		}
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String line = "";
			while ((line = reader.readLine()) != null) {
				_items.put(line.substring(0, line.indexOf(':')), line.substring(line.indexOf(':') + 1));
			}
			reader.close();
		} catch (FileNotFoundException e) {
			return;
		} catch (IOException e) {
			return;
		}
	}
	
	public static void save() {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(_filename));
			for (String key : _items.keySet()) {
				writer.write(key + ":" + _items.get(key) + "\n");
			}
			writer.close();
		} catch (IOException e) {
			return;
		}
	}
	
	public static String getItem(String string) {
		if (!_items.containsKey(string)) {
			return HelpRegistry.getHelp(string);
		} else {
			return _items.get(string);
		}
	}
	
	public static void setItem(String key, String value) {
		_items.put(key, value);
		save();
	}
	
	public static List getAll() {
		ArrayList<String> ret = new ArrayList<String>();
		for (String string : HelpRegistry.helpStuff.keySet()) {
			ret.add(string);
		}
		for (String string : _items.keySet()) {
			ret.add(string);
		}
		return ret;
	}

}
