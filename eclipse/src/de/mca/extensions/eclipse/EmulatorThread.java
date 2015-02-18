package de.mca.extensions.eclipse;
import java.io.File;
import java.util.Scanner;

public class EmulatorThread extends Thread {
	private String path_emulator	= "D:/MyChannel Apps/Emulator";
	private String path_app			= "";
	
	public EmulatorThread(String path_app) {
		if(path_app.endsWith("/main.js")) {
			path_app = path_app.substring(0, path_app.length() - "/main.js".length());
		}
		
		if(path_app.startsWith("file:/")) {
			path_app = path_app.substring(6);
		}
		
		this.path_app = path_app;
	}
	
	public void run() {
		try {
			File pathToExecutable	= new File(String.format("%s/Apps.bat", path_emulator));
			Console.log("[INFO] Exe Path: " + pathToExecutable.getAbsolutePath());
			ProcessBuilder builder	= new ProcessBuilder(pathToExecutable.getAbsolutePath(), path_app);
			Console.log("[INFO] DIR: " + new File(path_emulator).getAbsoluteFile());
			builder.directory(new File(path_emulator).getAbsoluteFile());
			builder.redirectErrorStream(true);
			Process process			= builder.start();
			Scanner s				= new Scanner(process.getInputStream());
			
			while(s.hasNextLine()) {
				Console.log(s.nextLine());
			}
			
			s.close();
			Console.log("Process exitValue: " + process.waitFor());
        } catch (Throwable t) {
        	t.printStackTrace();
        }
	}
}