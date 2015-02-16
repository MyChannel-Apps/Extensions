package de.mca.extensions.eclipse.wizards;
import java.io.ByteArrayInputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Map.Entry;
import java.util.Properties;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import de.mca.extensions.eclipse.tools.StringBuilding;

public class KnuddelsWizard extends org.eclipse.jface.wizard.Wizard implements INewWizard {
	protected ISelection selection;
	protected WizardPage page;
	private String app_name			= "";
	private String app_version		= "";
	private String app_nickname		= "";
	
	public KnuddelsWizard() {
		super();
		setWindowTitle("MyChannel-Apps");
		setNeedsProgressMonitor(true);
		
		page = new Knuddels(selection);
	}

	public void addPages() {
		addPage(page);
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
			//e.printStackTrace();
			return false;
		} catch (InvocationTargetException e) {
			//e.printStackTrace();
			Throwable realException = e.getTargetException();
			MessageDialog.openError(getShell(), "Error", realException.getMessage());
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
		
		if(!monitor.isCanceled()) {
			monitor.setTaskName("Show Project");
			monitor.worked(1);
		}
		
		if(!getShell().isDisposed()) {
			getShell().getDisplay().asyncExec(new Runnable() {
				public void run() {
					try {
						IDE.openEditor(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage(), app.getFile("main.js"), "main.js");
					} catch (PartInitException e) {
						e.printStackTrace();
					}
				}
			});
		}
		
		if(!monitor.isCanceled()) {
			monitor.done();
		}
	}
	
	private void createProject(IProject project) throws CoreException {
		project.create(null);
		project.open(null);
		project.setDefaultCharset("UTF-8", null);
		
		Properties config = new Properties();
		config.setProperty("appName", app_name);
		config.setProperty("appVersion", app_version);
		config.setProperty("appDeveloper.knuddelsDEV", app_nickname);
		config.setProperty("appDeveloper.knuddelsDE", app_nickname);
		config.setProperty("mayBeInstalledBy.1", "*.knuddelsDE");
		saveProperties(config, project.getFile("app.config"));
		
		createMain(project);
	}
	
	private void createMain(IProject project) throws CoreException {
		StringBuilding content = new StringBuilding();
		
		// Start
		content.append("var App = (new function() {");
		content.appendLine();
		
		// onAppStart
		content.append("this.onAppStart = function() {", 1);
		content.appendLine();
		content.append("/* @ToDo auto-generated */", 2);
		content.appendLine();
		content.append("};", 1);
		content.appendLine();
		content.appendLine();

		// onPrepareShutdown
		content.append("this.onPrepareShutdown = function(secondsTillShutdown) {", 1);
		content.appendLine();
		content.append("/* @ToDo auto-generated */", 2);
		content.appendLine();
		content.append("};", 1);
		content.appendLine();
		content.appendLine();

		// onShutdown
		content.append("this.onShutdown = function() {", 1);
		content.appendLine();
		content.append("/* @ToDo auto-generated */", 2);
		content.appendLine();
		content.append("};", 1);
		content.appendLine();
		content.appendLine();
		
		// onUserJoined
		content.append("this.onUserJoined = function(user) {", 1);
		content.appendLine();
		content.append("/* @ToDo auto-generated */", 2);
		content.appendLine();
		content.append("};", 1);
		content.appendLine();
		content.appendLine();
		
		// onUserLeft
		content.append("this.onUserLeft = function(user) {", 1);
		content.appendLine();
		content.append("/* @ToDo auto-generated */", 2);
		content.appendLine();
		content.append("};", 1);
		content.appendLine();
		content.appendLine();
		
		// mayJoinChannel
		content.append("this.mayJoinChannel = function(user) {", 1);
		content.appendLine();
		content.append("/* @ToDo auto-generated */", 2);
		content.appendLine();
		content.append("};", 1);
		content.appendLine();
		content.appendLine();
		
		// maySendPublicMessage
		content.append("this.maySendPublicMessage = function(publicMessage) {", 1);
		content.appendLine();
		content.append("/* @ToDo auto-generated */", 2);
		content.appendLine();
		content.append("};", 1);
		content.appendLine();
		content.appendLine();
		
		// onPrivateMessage
		content.append("this.onPrivateMessage = function(privateMessage) {", 1);
		content.appendLine();
		content.append("/* @ToDo auto-generated */", 2);
		content.appendLine();
		content.append("};", 1);
		content.appendLine();
		content.appendLine();
		
		// onPublicMessage
		content.append("this.onPublicMessage = function(publicMessage) {", 1);
		content.appendLine();
		content.append("/* @ToDo auto-generated */", 2);
		content.appendLine();
		content.append("};", 1);
		content.appendLine();
		content.appendLine();
		
		// onKnuddelReceived
		content.append("this.onKnuddelReceived = function(sender, receiver, knuddelAmount) {", 1);
		content.appendLine();
		content.append("/* @ToDo auto-generated */", 2);
		content.appendLine();
		content.append("};", 1);
		content.appendLine();
		content.appendLine();
		
		// onUserDiced
		content.append("this.onUserDiced = function(diceEvent) {", 1);
		content.appendLine();
		content.append("/* @ToDo auto-generated */", 2);
		content.appendLine();
		content.append("};", 1);
		content.appendLine();
		content.appendLine();
		
		// chatCommands
		content.append("this.chatCommands = {", 1);
		content.appendLine();
		content.append("/* @ToDo auto-generated */", 2);
		content.appendLine();
		content.append("};", 1);
		content.appendLine();

		// End
		content.append("}());");
		
		createContent(content.toString(), project.getFile("main.js"));		
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