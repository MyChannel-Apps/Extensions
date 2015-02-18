package de.mca.extensions.eclipse.handlers;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import de.mca.extensions.eclipse.EmulatorThread;

public class Emulator extends AbstractHandler {
	private EmulatorThread emulator;
    private IWorkbenchWindow window;
    private IWorkbenchPage activePage;
    private IProject project;
    
    public Object execute(ExecutionEvent event) throws ExecutionException {
        if(!extractProjectAndFileFromInitiatingEvent(event)) {
            return null;
        }
        
        try {
			this.window.getActivePage().showView("de.mca.extensions.eclipse.Console");
		} catch (PartInitException e) {
			/* Do Nothing */
		}
        
		emulator = new EmulatorThread(project.getFile("/main.js").getLocationURI().toString());
        emulator.start();
        
        return null;
    }

    private boolean extractProjectAndFileFromInitiatingEvent(ExecutionEvent event) {
        this.window				= HandlerUtil.getActiveWorkbenchWindow(event);
        this.activePage			= this.window.getActivePage();
        ISelection selection	= this.activePage.getSelection();
        
        if(selection instanceof ITreeSelection) {
            TreeSelection treeSelection	= (TreeSelection) selection;
            TreePath[] treePaths		= treeSelection.getPaths();
            
            if(treePaths.length == 0) {
            	noProjectSelected();
            	return false;
            }
            
            TreePath treePath			= treePaths[0];
            Object firstSegmentObj		= treePath.getFirstSegment();
            this.project				= (IProject) ((IAdaptable) firstSegmentObj).getAdapter(IProject.class);
            
            if(this.project == null) {
            	noProjectSelected();
                return false;
            }
            
            return true;
        } else {
        	IEditorPart editor	= this.activePage.getActiveEditor();
        	
        	if(editor == null) {
        		noProjectSelected();
        		return false;
        	}
        	
        	IFileEditorInput input 	= (IFileEditorInput) editor.getEditorInput();
            IFile file				= input.getFile();
            this.project			= file.getProject();
            
            if(this.project == null) {
            	noProjectSelected();
                return false;
            }
            
            return true;
        }
    }
    
    private void noProjectSelected() {
    	MessageDialog.openError(this.window.getShell(), "Information", "The App cannot be started. Make sure that the selected file is a valid App-Project.");
    }
}
