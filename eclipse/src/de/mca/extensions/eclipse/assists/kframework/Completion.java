package de.mca.extensions.eclipse.assists.kframework;
import de.mca.extensions.eclipse.Activator;

public class Completion extends de.mca.extensions.eclipse.assists.knuddels.Completion {
	public Completion() {
		super();
	}
	
	@Override
	public void sessionStarted() {
		this.autocomplete = Activator.getAutocompletement("kframework");
	}
}
