package de.mca.extensions.eclipse.wizards;
import org.eclipse.core.resources.IProject;

public class KFrameworkWizard extends KnuddelsWizard {
	public KFrameworkWizard() {
		super();
		setWindowTitle("MyChannel-Apps");
		setNeedsProgressMonitor(true);
		setTemplate("templates/kframework");
		page = new KFramework(selection);
	}
	
	@Override
	public void updateProjectNature(IProject project) {
		addNature(project, de.mca.extensions.eclipse.natures.KFramework.NATURE_ID);
	}
}
