package de.mca.extensions.eclipse.wizards;
import org.eclipse.jface.viewers.ISelection;
import de.mca.extensions.eclipse.Activator;

public class KFramework extends Knuddels {
	public KFramework(ISelection selection) {
		super("New KFramework Project");
		setTitle("Knuddels: KFramework Project");
		setImageDescriptor(Activator.getImageDescriptor("icons/kframework_logo.png"));
		setDescription("Create a new App Project with latest KFramework version");
		setMessage("Create a new App Project with latest KFramework version");
	}
}