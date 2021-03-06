package de.mca.extensions.eclipse;
import java.util.HashMap;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class Activator extends AbstractUIPlugin {
	public static final String PLUGIN_ID = "de.mca.extensions.eclipse";
	private static Activator plugin;
	public static HashMap<String, Autocomplete> autocomplete;
	
	public Activator() {
		/* Do Nothing */
	}
	
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin			= this;
		autocomplete	= new HashMap<String, Autocomplete>();
		createAutocompletement("knuddels");
		createAutocompletement("kframework");
	}
	
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}
	
	public static Activator getDefault() {
		return plugin;
	}
	
	public static void createAutocompletement(String name) {
		Autocomplete data = new Autocomplete(name);
		autocomplete.put(data.getID(), data);
	}
	
	public static Autocomplete getAutocompletement(String name) {
		return autocomplete.get(name);
	}
	
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	public static void warn(String message, Throwable exception){
		Activator.getDefault().getLog().log(new Status(Status.WARNING, PLUGIN_ID, Status.OK, message, exception));
	}
	
	public static void error(String message, Throwable exception){
		Activator.getDefault().getLog().log(new Status(Status.ERROR, PLUGIN_ID, Status.OK, message,  exception));
	}
}
