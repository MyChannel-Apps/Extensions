package de.mca.extensions.eclipse.wizards;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import de.mca.extensions.eclipse.interfaces.WizardNature;
import de.mca.extensions.eclipse.tools.StringBuilding;

public class KnuddelsWizard extends org.eclipse.jface.wizard.Wizard implements INewWizard, WizardNature {
	protected ISelection selection;
	protected WizardPage page;
	private String app_name			= "";
	private String app_version		= "";
	private String app_nickname		= "";
	private String app_template		= "templates/knuddels";
	
	public KnuddelsWizard() {
		super();
		setWindowTitle("MyChannel-Apps");
		setNeedsProgressMonitor(true);
		
		page = new Knuddels(selection);
	}

	public void addPages() {
		addPage(page);
	}
	
	public void setTemplate(String path) {
		this.app_template = path;
	}

	public boolean performFinish() {
		if(page == null) {
			return false;
		}
		
		if(page instanceof KFramework) {
			app_name			= ((KFramework) page).getAppName();
			app_version			= ((KFramework) page).getAppVersion();
			app_nickname		= ((KFramework) page).getAppNickname();
		} else if(page instanceof Knuddels) {
			app_name			= ((Knuddels) page).getAppName();
			app_version			= ((Knuddels) page).getAppVersion();
			app_nickname		= ((Knuddels) page).getAppNickname();
		}

		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				final IProgressMonitor m = monitor;
				
				getShell().getDisplay().asyncExec(new Runnable() {
					public void run() {
						try {
							doFinish(m);
						} catch(Exception e) {
							e.printStackTrace();
						} finally {
							m.done();
						}
					}
				});
			}
		};
		
		try {
			getContainer().run(true, false, op);
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			MessageDialog.openError(getShell(), "Error", e.getTargetException().getMessage());
			return false;
		}
		
		return true;
	}
	
	private void doFinish(IProgressMonitor monitor) throws CoreException {
		monitor.beginTask("Check Workspace", 2);
		IWorkspaceRoot workspace	= ResourcesPlugin.getWorkspace().getRoot();
		IProject app				= workspace.getProject(app_name);
		monitor.worked(1);
		
		if(app.exists()) {
			throwCoreException(String.format("The Project \"%s\" already exists.", app_name));
		} else {
			monitor.setTaskName("Create Project");
			createProject(app);
		}
		
		try {	
			if(!monitor.isCanceled()) {
				monitor.setTaskName("Show Project");
				monitor.worked(2);
			}
		
			if(!getShell().isDisposed()) {
				getShell().getDisplay().asyncExec(new Runnable() {
					public void run() {
						try {
							IWorkbench workbench	= PlatformUI.getWorkbench();
							String file_name		= "main.js";
							String editor_id		= "org.eclipse.ui.DefaultTextEditor";
							IFile file				= app.getFile(file_name);
							IEditorDescriptor desc	= workbench.getEditorRegistry().getDefaultEditor(file_name);
							
							if(desc != null) {
								editor_id			= desc.getId();
							}
							
							IDE.openEditor(workbench.getActiveWorkbenchWindow().getActivePage(), file, editor_id);
						} catch (Exception e) {
							/* Do Nothing */
						}
					}
				});
			}
				
			if(!monitor.isCanceled()) {
				monitor.done();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void updateProjectNature(IProject project) {
		addNature(project, de.mca.extensions.eclipse.natures.Knuddels.NATURE_ID);
	}
	
	public void addNature(IProject project, String nature_id) {
		if(project.isOpen() == false) {
			return;
		}
		
		try {
			IProjectDescription pd	= project.getDescription();
			IProjectNature nature	= project.getNature(nature_id);
			
			if(nature == null) {
				List<String> newNatures	= new ArrayList<String>();
			    newNatures.addAll(Arrays.asList(pd.getNatureIds()));
			    newNatures.add(nature_id);
			    pd.setNatureIds(newNatures.toArray(new String[newNatures.size()]));
			    project.setDescription(pd, null);
			}
		} catch(Exception e) {
			/* Do Nothing */
		}
	}
	
	private void createProject(IProject project) throws CoreException {
		project.create(null);
		project.open(null);
		project.setDefaultCharset("UTF-8", null);
		updateProjectNature(project);
		IScopeContext context			= new ProjectScope(project);
		System.err.println(context.getName() + ", " + context.getLocation());
		/* projectPreferences.put(key, value); projectPreferences.flush();*/
		
		Properties config = new Properties();
		config.setProperty("appName", app_name);
		config.setProperty("appVersion", app_version);
		config.setProperty("appDeveloper.knuddelsDEV", app_nickname);
		config.setProperty("appDeveloper.knuddelsDE", app_nickname);
		config.setProperty("mayBeInstalledBy.1", "*.knuddelsDE");
		saveProperties(config, project.getFile("app.config"));
		
		loadTemplate(project);
	}

	private void loadTemplate(IProject project) throws CoreException {
		File projDir = new File(project.getLocationURI().getPath());
		
		try {
			File template = new File(FileLocator.toFileURL(Platform.getBundle("de.mca.extensions.eclipse").getEntry(this.app_template)).getFile());
			copyDirectory(template, projDir);
			project.refreshLocal(IProject.DEPTH_INFINITE, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void copyDirectory(File srcPath, File dstPath) throws IOException {
		if(srcPath.isDirectory()) {
			if(!dstPath.exists()) {
				dstPath.mkdir();
			}

			String files[] = srcPath.list();
			
			for(int i = 0; i < files.length; i++) {
				copyDirectory(new File(srcPath, files[i]), new File(dstPath, files[i]));
			}
		} else {
			if(!srcPath.exists()) {
				System.out.println("File or directory does not exist.");
			} else {
				InputStream in = new FileInputStream(srcPath);
		        OutputStream out = new FileOutputStream(dstPath);
    
				// Transfer bytes from in to out
		        byte[] buf = new byte[1024];
				int len;
				
		        while((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
		        
				in.close();
		        out.close();
			}
		}
		
		System.out.println("Directory copied.");
	}
	
	private void createContent(String input, IFile file) {
		try {
			file.create(new ByteArrayInputStream(input.getBytes()), true, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
	
	private void saveProperties(Properties config, IFile file) {
		StringBuilding buffer	= new StringBuilding();

		for(Entry<Object, Object> entry : config.entrySet()) {
			buffer.append(entry.getKey());
			buffer.append(" = ");
			buffer.append(entry.getValue());
			buffer.appendLine();
		}
		
		createContent(buffer.toString(), file);
	}
	
	private void throwCoreException(String message) throws CoreException {
		throw new CoreException(new Status(IStatus.ERROR, "com.jemgengine.errors", IStatus.OK, message, null));
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}
}