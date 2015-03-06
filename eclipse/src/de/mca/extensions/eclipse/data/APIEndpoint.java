package de.mca.extensions.eclipse.data;
import java.util.HashMap;

public class APIEndpoint {
	private String version						= "";
	private String docs_url						= "";
	private String data_url						= "";
	private HashMap<String, APIClass> classes	= new HashMap<String, APIClass>();
	
	public void addClass(APIClass clazz) {
		this.classes.put(clazz.getName().toLowerCase(), clazz);
	}
	
	public APIClass getClass(String name) {
		return this.classes.get(name.toLowerCase());
	}
	
	public String getVersion() {
		return this.version;
	}
	
	public String getDocumentation() {
		return this.docs_url;
	}
	
	public String getURL() {
		return this.data_url;
	}
}
