package de.mca.extensions.eclipse.data;

public class APIClass {
	private String name = "";
	private boolean is_static = false;
	
	public APIClass(String name) {
		this.name = name;
	}
	
	public String getClassName() {
		return this.name;
	}
	
	public void setStatic(boolean state) {
		this.is_static = state;
	}
	
	public boolean isStatic() {
		return this.is_static;
	}
}
