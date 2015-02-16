package de.mca.extensions.eclipse.tools;

public class StringBuilding {
	private StringBuffer buffer = new StringBuffer();
	
	public void append(String string) {
		buffer.append(string);
	}

	public void append(String string, int indent) {
		for(int i = 0; i < indent; i++) {
			buffer.append("\t");
		}
		
		buffer.append(string);
	}
	
	public void appendLine() {
		buffer.append("\r\n");
	}
	
	public String toString() {
		return buffer.toString();
	}

	public void append(Object object) {
		buffer.append(object);
	}
}
