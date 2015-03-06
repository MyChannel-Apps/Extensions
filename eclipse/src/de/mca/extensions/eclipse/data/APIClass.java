package de.mca.extensions.eclipse.data;
import java.util.List;
import de.mca.extensions.eclipse.interfaces.APIEntry;

public class APIClass implements APIEntry {
	private String name					= "";
	private String source				= "";
	private String description			= "";
	private boolean is_static			= false;
	
	public APIClass(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setStatic(boolean state) {
		this.is_static = state;
	}
	
	public boolean isStatic() {
		return this.is_static;
	}
	
	public void setSource(String source) {
		this.source = source;
	}
	
	public String getSource() {
		return this.source;
	}

	@Override
	public List<APIEntry> find(String input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription() {
		return this.description;
	}
}
