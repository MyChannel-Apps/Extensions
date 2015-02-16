package de.mca.extensions.eclipse.wizards;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import de.mca.extensions.eclipse.Activator;

public class Knuddels extends WizardPage {
	private Text app_name;
	private Text app_version;
	private Text app_nickname;
	
	public Knuddels(String title) {
		super(title);
		setTitle("Knuddels: App Project");
		setImageDescriptor(Activator.getImageDescriptor("icons/knuddels_logo.png"));
		setDescription("Create a new App Project");
		setMessage("Create a new App Project");
	}
	
	public Knuddels(ISelection selection) {
		super("New Knuddels Project");
		setTitle("Knuddels: App Project");
		setImageDescriptor(Activator.getImageDescriptor("icons/knuddels_logo.png"));
		setDescription("Create a new App Project with latest KFramework version");
		setMessage("Create a new App Project with latest KFramework version");
	}

	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		container.setLayout(new GridLayout(2, false));
		
		// App Name
		Label label_app_name = new Label(container, SWT.NONE);
		label_app_name.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_app_name.setText("App Name:");
		
		app_name = new Text(container, SWT.BORDER);
		app_name.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		app_name.setMessage("HelloWorld");
		app_name.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		
		// Version
		Label label_app_version = new Label(container, SWT.NONE);
		label_app_version.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_app_version.setText("Version:");
		
		app_version = new Text(container, SWT.BORDER);
		app_version.setText("1.0.0");
		app_version.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		app_version.setMessage("1.0.0");
		app_version.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		
		// Nickname
		Label label_app_nickname = new Label(container, SWT.NONE);
		label_app_nickname.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_app_nickname.setText("Nickname:");
		
		app_nickname = new Text(container, SWT.BORDER);
		app_nickname.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		app_nickname.setMessage("James");
		app_nickname.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		
		dialogChanged();
		setControl(container);
	}

	private void dialogChanged() {
		/* Name */
		if(getAppName().length() == 0) {
			updateStatus("Please enter a project name.");
			return;
		}
		
		/* Version */
		if(getAppVersion().length() == 0) {
			updateStatus("Please enter a versions number.");
			return;
		}
		
		/* Nickname */
		if(getAppNickname().length() == 0) {
			updateStatus("Please enter a nickname");
			return;
		}
		
		updateStatus(null);
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	public String getAppName() {
		return app_name.getText();
	}
	
	public String getAppNickname() {
		return app_nickname.getText();
	}
	
	public String getAppVersion() {
		return app_version.getText();
	}
}