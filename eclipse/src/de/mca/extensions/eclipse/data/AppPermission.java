package de.mca.extensions.eclipse.data;

import java.util.Properties;

import de.mca.extensions.eclipse.data.AppServer.Server;

public class AppPermission {
	private int index			= 1;
	private String nickname		= "*";
	private AppServer server	= null;
	private boolean is_deleted	=	false;
	
	public AppPermission(int key, String pair) {
		this.index = key;
		
		if(pair.contains(".")) {
			String[] split	= pair.split("\\.");
			
			if(split.length != 2) {
				return;
			}
			
			this.nickname	= split[0];
			this.server		= new AppServer(split[1]);
		}
	}
	
	public int getIndex() {
		return this.index;
	}
	
	public String getNickname() {
		return this.nickname;
	}
	
	public AppServer getServer() {
		return this.server;
	}
	
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("AppPermission [ ");
		buffer.append("Index=");
		buffer.append(this.index);
		buffer.append(", Nickname=");
		buffer.append(this.nickname);
		buffer.append(", Server= { ");
		buffer.append(this.server);
		buffer.append(" } ]");
		
		return buffer.toString();
	}

	public void setServer(String text) {
		for(Server server : Server.values()) {
			if(server.getValue().equals(text)) {
				this.server = new AppServer(server);
				break;
			}
		}
	}

	public void update(Properties config) {
		config.setProperty(String.format("mayBeInstalledBy.%s", this.index), String.format("%s.%s", this.nickname, this.server.getServer()));
	}

	public void remove(Properties config) {
		config.remove(String.format("mayBeInstalledBy.%s", this.index));
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public void setAsDeleted(boolean state) {
		this.is_deleted = state;
	}
	
	public boolean isDeleted() {
		return this.is_deleted;
	}
}
