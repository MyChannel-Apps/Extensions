package de.mca.extensions.eclipse.data;

public class AppServer {
	public enum Server {
		knuddelsDEV("Testserver"),
		knuddelsDE("Knuddels.de");
		// knuddelsAT("Knuddels.at"),
		// knuddelsCH("Knuddels.ch"),
		// knuddelsCOM("Knuddels.com"),
		// knuddelsMFC("Mainfranken-Chat.de");

		Server(String name) {
			this.name = name;
		}

		private final String name;

		public String getValue() {
			return this.name;
		}

		public static String[] stringValues() {
			String[] output = new String[values().length];
			int index		= 0;
			
			for(Server server : values()) {
				output[index++] = server.getValue();
			}
			
			return output;
		}
	}

	private Server server;
	
	public AppServer(Server server) {
		this.server = server;
	}
	
	public AppServer(String string) {
		this(Server.valueOf(string));
	}
	
	public Server getServer() {
		return this.server;
	}
	
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("AppServer [ ");
		buffer.append("Server=");
		buffer.append(this.server);
		buffer.append(", Name=");
		buffer.append(this.server.getValue());
		buffer.append(" ]");
		
		return buffer.toString();
	}
}
