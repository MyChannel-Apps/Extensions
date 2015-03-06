package de.mca.extensions.eclipse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.mca.extensions.eclipse.data.APIClass;
import de.mca.extensions.eclipse.interfaces.APIEntry;

public class Autocomplete {
	private String identifier			= "";
	private String version				= "";
	private String url					= "";
	private HashMap<String, APIClass> classes;
	
	public Autocomplete(String identifier) {
		this.identifier	= identifier;
		this.classes	= new HashMap<String, APIClass>();
		this.load();
	}

	public String getID() {
		return this.identifier;
	}

	public String getVersion() {
		return this.version;
	}
	
	public String getURL() {
		return this.url;
	}
	
	public HashMap<String, APIClass> getClasses() {
		return this.classes;
	}
	
	private void load() {
		try {
			InputStream stream	= getClass().getClassLoader().getResourceAsStream("/de/mca/extensions/eclipse/" + this.identifier + ".json");
			BufferedReader in	= new BufferedReader(new InputStreamReader(stream));
	        StringBuffer buffer = new StringBuffer();
	        String line			= "";
	        
	        while((line = in.readLine()) != null) {
	            buffer.append(line);
	        }
	        
	        parse(new JSONObject(buffer.toString()));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	private void parse(JSONObject main) throws JSONException {
		if(main.has("version")) {
			this.version = main.getString("version");
		}
		
		if(main.has("url")) {
			this.url = main.getString("url");
		}
		
		if(main.has("classes")) {
			this.parseClasses(main.getJSONArray("classes"));
		}
	}
	
	private void parseClasses(JSONArray classes) throws JSONException {
		for(int index = 0; index < classes.length(); ++index) {
			JSONObject entry	= classes.getJSONObject(index);
			
			if(entry.has("name")) {
				String name			= entry.getString("name");
				APIClass clazz		= new APIClass(name);
				
				if(entry.has("static")) {
					clazz.setStatic(entry.getBoolean("static"));
				}
				
				if(entry.has("source")) {
					clazz.setSource(entry.getString("source"));
				}
				
				if(entry.has("methods")) {
					this.parseMethods(entry.getJSONArray("methods"));
				}
				
				this.classes.put(clazz.getName(), clazz);
			}
		}
	}
	
	private void parseMethods(JSONArray methods) throws JSONException {
		for(int index = 0; index < methods.length(); ++index) {
			JSONObject entry	= methods.getJSONObject(index);
		}
	}

	public List<APIEntry> find(String input) {
		List<APIEntry> matches = new LinkedList<APIEntry>();
		
		if((input == null) || ("".equals(input))) {
			return matches;
		}
		
		for(Entry<String, APIClass> entry: classes.entrySet()) {
			if(entry.getKey().startsWith(input)){
				matches.add(entry.getValue());
			}
		}
		
		return matches;
	}
}
