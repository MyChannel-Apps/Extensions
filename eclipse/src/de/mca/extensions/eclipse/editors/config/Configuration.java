package de.mca.extensions.eclipse.editors.config;
import org.eclipse.swt.widgets.Button;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Properties;
import javax.xml.transform.TransformerFactoryConfigurationError;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;
import de.mca.extensions.eclipse.Activator;
import de.mca.extensions.eclipse.data.AppPermission;
import de.mca.extensions.eclipse.data.AppServer.Server;
import de.mca.extensions.eclipse.tools.StringBuilding;

public class Configuration extends EditorPart implements IEditorPart {
	private Text app_name;
	private Text app_version;
	private ScrolledForm form;
	private Composite permission_table;
	private Composite composite_permissions;
	private FormToolkit toolkit;
	private boolean dirty;
	private boolean ui_available = false;
	private HashMap<Server, Text> dev_server			= new HashMap<Server, Text>();
	private HashMap<Integer, AppPermission>	permissions	= new HashMap<Integer, AppPermission>();
	private Properties config = new Properties();
	private int permission_index = 0;
	private final IResourceChangeListener resourceChangeListener = new IResourceChangeListener() {
		@Override
		public void resourceChanged(final IResourceChangeEvent event) {
			if (getEditorInput() == null) {
				return;
			}
			
			final IFileEditorInput editorInput = (IFileEditorInput) getEditorInput().getAdapter(IFileEditorInput.class);
			
			if (editorInput == null) {
				return;
			}
			
			final IPath filePath = editorInput.getFile().getFullPath();
			
			if (event.getType() == IResourceChangeEvent.PRE_CLOSE) {
				if (editorInput.getFile().getProject().equals(event.getResource())) {
					Display.getDefault().asyncExec(new Runnable() {
						public void run() {
							IWorkbenchPage[] pages = getSite().getWorkbenchWindow().getPages();
							
							for (int i = 0; i < pages.length; i++) {
								pages[i].closeEditor(pages[i].findEditor(getEditorInput()), true);
							}
						}
					});
				}
			} else {
				final IResourceDelta mainDelta = event.getDelta();
				
				if (mainDelta == null) {
					return;
				}
				
				final IResourceDelta affectedElement = mainDelta.findMember(filePath);
				
				if(affectedElement == null) {
					return;
				}
				
				switch (affectedElement.getKind()) {
					case IResourceDelta.REMOVED:
						if ((IResourceDelta.MOVED_TO & affectedElement.getFlags()) != 0) {
							Display.getDefault().asyncExec(new Runnable() {
								public void run() {
									IPath path		= affectedElement.getMovedToPath();
									IFile newFile	= affectedElement.getResource() .getWorkspace().getRoot().getFile(path);
									
									if (newFile != null) {
										setInput(new FileEditorInput(newFile), false);
									}
								}
							});
						} else {
							Display.getDefault().asyncExec(new Runnable() {
								public void run() {
									IWorkbenchPage[] pages = getSite() .getWorkbenchWindow().getPages();
									
									for (int i = 0; i < pages.length; i++) {
										pages[i].closeEditor(pages[i] .findEditor(getEditorInput()), true);
									}
								}
							});
						}
					break;
					case IResourceDelta.CHANGED:
						Display.getDefault().asyncExec(new Runnable() {
							public void run() {
								if (isDirty()) {
									MessageDialog dialog = new MessageDialog( getSite().getShell(), "Resource Changed", null, "File has been changed outside of editor. Would you like to replace editors content from file.", MessageDialog.QUESTION, new String[] { IDialogConstants.YES_LABEL, IDialogConstants.NO_LABEL }, 0) {
										protected int getShellStyle() {
											return SWT.NONE | SWT.TITLE | SWT.BORDER | SWT.APPLICATION_MODAL | SWT.SHEET | getDefaultOrientation();
										}
									};
									
									if(dialog.open() != 0) {
										return;
									}
								}
								
								loadFile();
							}
						});
					break;
				}
			}
		}
	};

	public Configuration() {
		ResourcesPlugin.getWorkspace().addResourceChangeListener(resourceChangeListener);
		setFocus();
	}

	@Override
	public void createPartControl(Composite parent) {
		ui_available		= true;
		toolkit				= new FormToolkit(parent.getDisplay());
		form				= toolkit.createScrolledForm(parent);
		form.setText("App Configuration");
		form.getBody().setLayout(new TableWrapLayout());
		form.getBody().setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		
		// Global Informations
		Section section_globals = toolkit.createSection(form.getBody(), SWT.FILL | Section.TREE_NODE | Section.EXPANDED);
		section_globals.setText("Globals");
		section_globals.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		toolkit.createCompositeSeparator(section_globals);
		
		Composite composite_globals = toolkit.createComposite(section_globals, SWT.FILL);
		composite_globals.setLayout(new GridLayout(2, false));
		section_globals.setClient(composite_globals);
		
		FormText description_globals = toolkit.createFormText(composite_globals, false);
		description_globals.setText("This informations are required to be able to install the app.", false, false);
		description_globals.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		// >> App Name
		Label label_app_name = toolkit.createLabel(composite_globals, "App Name:", SWT.NO_BACKGROUND);
		label_app_name.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		label_app_name.setFont(JFaceResources.getFontRegistry().getBold(""));
		
		app_name = toolkit.createText(composite_globals, "", SWT.BORDER);
		app_name.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		app_name.setMessage("HelloWorld");
		app_name.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				config.setProperty("appName", app_name.getText());
				setDirty(true);
		    }
		});
		
		// >> Version
		Label label_app_version = toolkit.createLabel(composite_globals, "Version:", SWT.BOLD | SWT.NO_BACKGROUND);
		label_app_version.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		label_app_version.setFont(JFaceResources.getFontRegistry().getBold(""));
		
		app_version = toolkit.createText(composite_globals, "", SWT.BORDER);
		app_version.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		app_version.setMessage("1.0.0");
		app_version.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				config.setProperty("appVersion", app_version.getText());
				setDirty(true);
		    }
		});
	    
		// Developers
		Section section_developers = toolkit.createSection(form.getBody(), Section.TREE_NODE | Section.EXPANDED);
		section_developers.setText("Developers");
		section_developers.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		toolkit.createCompositeSeparator(section_developers);
		
		Composite composite_developers = toolkit.createComposite(section_developers, SWT.FILL);
		composite_developers.setLayout(new GridLayout(2, false));
		section_developers.setClient(composite_developers);
		
		FormText description_developers = toolkit.createFormText(composite_developers, false);
		description_developers.setText("This information indicates at what systems the name of your user.", false, false);
		description_developers.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		for(Server server : Server.values()) {
			Label label_server = toolkit.createLabel(composite_developers, server.getValue() + ":", SWT.NO_BACKGROUND);
			label_server.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
			label_server.setFont(JFaceResources.getFontRegistry().getBold(""));
			
			Text entry = toolkit.createText(composite_developers, "", SWT.BORDER);
			entry.setData("Server", server);
			entry.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			entry.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent event) {
					Text element	= ((Text) event.getSource());
					Server server	= (Server) element.getData("Server");
					config.setProperty(String.format("appDeveloper.%s", server), element.getText());
					setDirty(true);
				}
			});
			dev_server.put(server, entry);
		}
		
		// Permissions
		Section section_permissions = toolkit.createSection(form.getBody(), Section.TREE_NODE | Section.EXPANDED);
		section_permissions.setText("Installation Permissions");
		section_permissions.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		toolkit.createCompositeSeparator(section_permissions);
		
		composite_permissions = toolkit.createComposite(section_permissions, SWT.FILL);
		composite_permissions.setLayout(new GridLayout(2, false));
		section_permissions.setClient(composite_permissions);
		
		FormText description_permissions = toolkit.createFormText(composite_permissions, false);
		description_permissions.setText("You can specify which users can install the app on what system. By default, the app developer has always been a privilege.", false, false);
		description_permissions.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		Button create = toolkit.createButton(composite_permissions, "Add Entry", SWT.NONE);
		create.setLayoutData(new GridData(SWT.NORMAL, SWT.CENTER, true, false, 4, 1));
		create.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent event) {
				int index					= ++permission_index;
				String value 				= "*.knuddelsDEV";
    			AppPermission permission	= new AppPermission(index, value);
    			
    			addPermssionEntry(permission);
    			
    			// Storage
    			permissions.put(index, permission);
    			permission.update(config);
    			
				updateUI();
				setDirty(true);
			}
			
			public void widgetSelected(SelectionEvent event) {
				widgetDefaultSelected(event);
			}
		});
		
		permission_table = toolkit.createComposite(composite_permissions, SWT.FILL);
		permission_table.setLayout(new GridLayout(4, true));
		updateUI();
	}

	@Override
	public void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(resourceChangeListener);
		super.dispose();
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		monitor.beginTask("Saving", 2);
		try {
			IFileEditorInput editorInput = (IFileEditorInput) getEditorInput().getAdapter(IFileEditorInput.class);
			IFile file = editorInput.getFile();
			StringBuilding buffer	= new StringBuilding();

    		for(Entry<Object, Object> entry : config.entrySet()) {
    			buffer.append(entry.getKey());
    			buffer.append(" = ");
    			buffer.append(entry.getValue());
    			buffer.appendLine();
    		}
			
			new SubProgressMonitor(monitor, 1).worked(1);
			byte[] bytes = String.valueOf(buffer.toString()).getBytes();
			file.setContents(new ByteArrayInputStream(bytes), IFile.KEEP_HISTORY, new SubProgressMonitor(monitor, 1));
			setDirty(false);
		} catch (CoreException e) {
			logAndShow(e);
		} catch (TransformerFactoryConfigurationError e) {
			logAndShow(e);
		} finally {
			monitor.done();
		}
	}

	@Override
	public void doSaveAs() {
		Shell shell = getSite().getWorkbenchWindow().getShell();
		SaveAsDialog dialog = new SaveAsDialog(shell);
		IFileEditorInput editorInput = (IFileEditorInput) getEditorInput().getAdapter(IFileEditorInput.class);
		dialog.setOriginalFile(editorInput.getFile());
		dialog.open();
		IPath path = dialog.getResult();
		
		if(path == null) {
			return;
		}
		
		final IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
		try {
			new ProgressMonitorDialog(shell).run(false, false, new WorkspaceModifyOperation() {
				public void execute(final IProgressMonitor monitor) throws CoreException, InvocationTargetException, InterruptedException {
					try {
						StringBuilding buffer	= new StringBuilding();

			    		for(Entry<Object, Object> entry : config.entrySet()) {
			    			buffer.append(entry.getKey());
			    			buffer.append(" = ");
			    			buffer.append(entry.getValue());
			    			buffer.appendLine();
			    		}
			    		
						monitor.worked(1);
						
						byte[] bytes = String.valueOf(buffer.toString()).getBytes();
						
						if(file.exists()) {
							file.setContents(new ByteArrayInputStream(bytes), IFile.KEEP_HISTORY, new SubProgressMonitor(monitor, 1));
						} else {
							file.create(new ByteArrayInputStream(bytes), false, monitor);
						}
						
						setDirty(false);
					} catch (TransformerFactoryConfigurationError e) {
						throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
					}
				}
			});
			
			setInput(new FileEditorInput(file), false);
		} catch (InterruptedException e) {
			logAndShow(e);
		} catch (InvocationTargetException e) {
			logAndShow(e);
		}
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input, true);
		setFocus();
	}

	@Override
	public boolean isDirty() {
		return dirty;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return true;
	}

	private void loadFile() {
		try {
			IFileEditorInput editorInput = (IFileEditorInput) getEditorInput().getAdapter(IFileEditorInput.class);
			IFile file = (IFile) editorInput.getFile();
		
			IPath location 	= file.getLocation();
        	File f 			= location.toFile();
        	InputStream in	= new FileInputStream(f);
        	config = new Properties();
        	config.load(in);
			in.close();
			updateUI();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void updateUI() {
		if(config != null && ui_available == true) {
			app_name.setText(config.getProperty("appName", ""));
        	app_version.setText(config.getProperty("appVersion", ""));
        	
        	for(Server server : Server.values()) {
    			Text entry		= dev_server.get(server);
    			String value	= config.getProperty(String.format("appDeveloper.%s", server), "");
    			
    			if(entry == null || value == null) {
    				continue;
    			}
    			
    			entry.setText(value);
    		}
    		
        	permission_table.dispose();
        	permission_table = toolkit.createComposite(composite_permissions, SWT.FILL);
    		permission_table.setLayout(new GridLayout(4, true));
        	permissions.clear();
        	
    		for(Object keys : config.keySet()) {
    			String value	= keys.toString();
    			String search	= "mayBeInstalledBy.";
    			
    			if(!value.startsWith(search)) {
    				continue;
    			}
    			
    			int key						= Integer.parseInt(value.substring(search.length()));
    			if(key > permission_index) {
    				permission_index = key;
    			}
    			
    			String pair					= this.config.getProperty(value, "");
    			AppPermission permission	= new AppPermission(key, pair);
    			
    			addPermssionEntry(permission);
    			
    			// Storage
    			permissions.put(key, permission);
    		}
    		
        	composite_permissions.layout();
		}
		
		setDirty(false);
		setFocus();
	}
	
	private void addPermssionEntry(AppPermission permission) {
		// Label
		Label label_app_name = toolkit.createLabel(permission_table, "" + permission.getIndex(), SWT.NO_BACKGROUND);
		label_app_name.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		label_app_name.setFont(JFaceResources.getFontRegistry().getBold(""));
		label_app_name.setData("AppPermission", permission);
		
		// Nickname
		Text nickname = toolkit.createText(permission_table, permission.getNickname(), SWT.BORDER);
		nickname.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		nickname.setMessage("*");
		nickname.setData("AppPermission", permission);
		nickname.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				Text element				= (Text) event.getSource();
				AppPermission permission	= (AppPermission) element.getData("AppPermission");
				permission.setNickname(element.getText());
				permission.update(config);
				setDirty(true);
		    }
		});
		
		// Server
		CCombo server = new CCombo(permission_table, SWT.BORDER);
		server.setText(permission.getServer().getServer().getValue());
		server.setItems(Server.stringValues());
		server.setData("AppPermission", permission);
		server.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				CCombo element				= (CCombo) event.getSource();
				AppPermission permission	= (AppPermission) element.getData("AppPermission");
				permission.setServer(element.getText());
				permission.update(config);
				setDirty(true);
			}
			
			public void widgetDefaultSelected(SelectionEvent event) {
				CCombo element				= (CCombo) event.getSource();
				AppPermission permission	= (AppPermission) element.getData("AppPermission");
				permission.setServer(element.getText());
				permission.update(config);
				setDirty(true);
			}
		});
		
		// Delete
		Button delete = toolkit.createButton(permission_table, "Delete", SWT.NONE);
		delete.setData("AppPermission", permission);
		delete.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent event) {
				Button element				= (Button) event.getSource();
				AppPermission permission	= (AppPermission) element.getData("AppPermission");
				permission.setAsDeleted(true);
				permission.remove(config);
				updateUI();
				setDirty(true);
			}
			
			public void widgetSelected(SelectionEvent event) {
				widgetDefaultSelected(event);
			}
		});
	}

	private void logAndShow(Throwable e) {
		final IStatus status;
		
		if (e instanceof CoreException) {
			status = ((CoreException) e).getStatus();
		} else {
			status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e);
		}
		
		Activator.getDefault().getLog().log(status);
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				ErrorDialog.openError(getSite().getShell(), "Error", "Error", status);
			}
		};
		
		if (getSite().getShell().getDisplay().getThread() == Thread.currentThread()) {
			runnable.run();
		} else {
			getSite().getShell().getDisplay().syncExec(runnable);
		}
	}

	private void setDirty(boolean dirty) {
		if (this.dirty != dirty) {
			this.dirty = dirty;
			firePropertyChange(IEditorPart.PROP_DIRTY);
		}
	}

	@Override
	protected void setInput(IEditorInput input) {
		setInput(input, true);
		setFocus();
	}

	protected void setInput(IEditorInput input, boolean loadFile) {
		super.setInput(input);
		
		IFileEditorInput fileEditorInput = (IFileEditorInput) getEditorInput().getAdapter(IFileEditorInput.class);
		
		setPartName(fileEditorInput.getName());
		
		if(loadFile) {
			loadFile();
		}
		
		setFocus();
	}

	@Override
	public void setFocus() {
		if(form != null) {
			form.setFocus();
		}
	}
}