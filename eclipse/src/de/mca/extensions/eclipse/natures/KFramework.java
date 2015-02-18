package de.mca.extensions.eclipse.natures;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;

public class KFramework implements IProjectNature {
	public static final String NATURE_ID = "de.mca.extensions.eclipse.natures.KFramework";
	private IProject project;
	
	@Override
	public void configure() throws CoreException {
		if(!getProject().hasNature(NATURE_ID)) {
			IProjectDescription description	= getProject().getDescription();
			String[] prevNatures			= description.getNatureIds();
			String[] newNatures				= new String[prevNatures.length + 1];
			
			System.arraycopy(prevNatures, 0, newNatures, 0, prevNatures.length);
			
			newNatures[prevNatures.length]	= NATURE_ID;
			description.setNatureIds(newNatures);
			project.setDescription(description, null);
		}
	}

	@Override
	public void deconfigure() throws CoreException {
		/* Do Nothing */
	}

	@Override
	public IProject getProject() {
		return this.project;
	}

	@Override
	public void setProject(IProject project) {
		this.project = project;
	}
}
