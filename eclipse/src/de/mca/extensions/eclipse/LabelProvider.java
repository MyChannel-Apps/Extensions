package de.mca.extensions.eclipse;
import org.eclipse.core.internal.resources.Project;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import de.mca.extensions.eclipse.natures.KFramework;
import de.mca.extensions.eclipse.natures.Knuddels;

@SuppressWarnings("restriction")
public class LabelProvider implements ILabelProvider {
	@Override
	public void addListener(ILabelProviderListener listener) {
		/* Do Nothing */
	}

	@Override
	public void dispose() {
		/* Do Nothing */
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		/* Do Nothing */
	}

	@Override
	public Image getImage(Object element) {
		if(element instanceof Project) {
			Project project = (Project) element;
			
			try {
				if(project.hasNature(Knuddels.NATURE_ID)) {
					return Activator.getImageDescriptor("icons/project_knuddels.png").createImage();
				} else if(project.hasNature(KFramework.NATURE_ID)) {
					return Activator.getImageDescriptor("icons/project_kframework.png").createImage();				
				}
			} catch(Exception e) {
				/* Do Nothing */
			}
		}
		
		return null;
	}

	@Override
	public String getText(Object element) {
		return null;
	}
}
