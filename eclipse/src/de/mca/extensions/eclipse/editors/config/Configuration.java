package de.mca.extensions.eclipse.editors.config;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.Map.Entry;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.part.EditorPart;

import de.mca.extensions.eclipse.tools.StringBuilding;

public class Configuration extends EditorPart {
	private IFile file;
	private boolean is_saving	= false;
	private boolean is_dirty	= false;
	private Properties config;
	private Text app_name;
	private Text app_version;
	private Text app_nickname;
	
	IResourceChangeListener resourceChangeLister = new IResourceChangeListener() {
        public void resourceChanged(IResourceChangeEvent event) {
            IResourceDelta delta	= event.getDelta();
            IResourceDelta member	= delta.findMember(file.getFullPath());
            
            if(member != null && !is_saving) {
            	switch(member.getKind()) {
            		case IResourceDelta.CHANGED:
            			loadFile();
            			is_dirty = false;
            			firePropertyChange(IEditorPart.PROP_DIRTY);
            		break;
            		
            		// Something else deleted the file
            		case IResourceDelta.REMOVED:
            			Display.getDefault().asyncExec(new Runnable() {
            				@Override
                            public void run() {
                                getEditorSite().getPage().closeEditor(Configuration.this, false);
                            }
                        });
            		break;
                }
            }
        }
    };
	
    @Override
    public void createPartControl(Composite parent) {
    	//toolkit = new FormToolkit(parent.getDisplay());
		//form = toolkit.createScrolledForm(parent);
    	
    	parent.setBackground(new Color(null, 255, 255, 255));
    	
    	Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout(2, false));
		
		// App Name
		Label label_app_name = new Label(container, SWT.NONE);
		label_app_name.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_app_name.setText("App Name:");
		
		app_name = new Text(container, SWT.BORDER);
		app_name.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		app_name.setMessage("HelloWorld");
		
		// Version
		Label label_app_version = new Label(container, SWT.NONE);
		label_app_version.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_app_version.setText("Version:");
		
		app_version = new Text(container, SWT.BORDER);
		app_version.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		app_version.setMessage("1.0.0");
		
		// Nickname
		Label label_app_nickname = new Label(container, SWT.NONE);
		label_app_nickname.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_app_nickname.setText("Nickname:");
		
		app_nickname = new Text(container, SWT.BORDER);
		app_nickname.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		app_nickname.setMessage("James");
		
		// Load File
    	this.loadFile();
    }
    
    @Override
    public void init(IEditorSite site, IEditorInput input) {
    	setContentDescription("DESC");
    	setSite(site);
    	setInput(input);
        
    	ResourcesPlugin.getWorkspace().addResourceChangeListener(resourceChangeLister);
    }
    
    @Override
    public void dispose() {
    	super.dispose();
    	ResourcesPlugin.getWorkspace().removeResourceChangeListener(resourceChangeLister);
    }
    
    private void loadFile() {
    	IFileEditorInput input = (IFileEditorInput) this.getEditorInput();
    	
    	if(input != null) {
            this.file = ((IFileEditorInput) input).getFile();
            
            if(this.file == null) {
            	return;
            }
            
            try {
            	IPath location 	= this.file.getLocation();
            	File f 			= location.toFile();
            	InputStream in	= new FileInputStream(f);
            	this.config		= new Properties();
            	this.config.load(in);
            	in.close();
            	
            	if(this.config != null) {
	            	this.app_name.setText(this.config.getProperty("appName", ""));
	            	this.app_version.setText(this.config.getProperty("appVersion", ""));
	            	this.app_nickname.setText(this.config.getProperty("appDeveloper.knuddelsDE", ""));
            	}
            } catch(Exception e) {
            	/* Do Nothing */
            }
            
            setPartName(this.file.getName());
        }
    }
    
    public void setFocus() {
    	if(app_name != null) {
    		app_name.forceFocus();
    	}
    }
    
	@Override
	public void doSave(IProgressMonitor monitor) {
		is_saving = true;
        
        try {
        	StringBuilding buffer	= new StringBuilding();

    		for(Entry<Object, Object> entry : config.entrySet()) {
    			buffer.append(entry.getKey());
    			buffer.append(" = ");
    			buffer.append(entry.getValue());
    			buffer.appendLine();
    		}
    		
    		InputStream is = new ByteArrayInputStream(buffer.toString().getBytes());
            file.setContents(is, IResource.KEEP_HISTORY, monitor);
            is.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
            is_saving = false;
        }
        
        is_dirty = false;
        firePropertyChange(IEditorPart.PROP_DIRTY);	
	}
	
	@Override
	public void doSaveAs() {
		/* Do Nothing */
	}
	
	@Override
	public boolean isDirty() {
		return is_dirty;
	}
	
	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}
 }