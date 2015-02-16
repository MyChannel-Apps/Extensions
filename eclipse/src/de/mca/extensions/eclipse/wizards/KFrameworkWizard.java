package de.mca.extensions.eclipse.wizards;

public class KFrameworkWizard extends KnuddelsWizard {
	public KFrameworkWizard() {
		super();
		setWindowTitle("MyChannel-Apps");
		setNeedsProgressMonitor(true);
		
		page = new KFramework(selection);
	}
}
