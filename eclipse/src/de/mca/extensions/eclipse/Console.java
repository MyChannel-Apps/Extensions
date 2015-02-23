package de.mca.extensions.eclipse;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.LineBackgroundEvent;
import org.eclipse.swt.custom.LineBackgroundListener;
import org.eclipse.swt.custom.LineStyleEvent;
import org.eclipse.swt.custom.LineStyleListener;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

public class Console extends ViewPart {
	public static final String ID	= "de.mca.extensions.eclipse.Console"; //$NON-NLS-1$
	public static Console instance	= null;
	private StyledText output;
	private Text input;
	
	public Console() {
		if(instance == null) {
			instance = this;
		}
	}
	
	public static Console get() {
		return instance;
	}
	
	@Override
	public void createPartControl(Composite parent) {
		IToolBarManager toolbar = getViewSite().getActionBars().getToolBarManager();
		toolbar.add(new Action("Emulator", Activator.getImageDescriptor("icons/start.gif")) {
			@Override
			public void run() {
				// TODO mach etwas
			}
		});
		
		GridLayout gl_parent = new GridLayout(1, false);
		gl_parent.verticalSpacing = 0;
		gl_parent.marginWidth = 0;
		gl_parent.marginHeight = 0;
		gl_parent.horizontalSpacing = 0;
		parent.setLayout(gl_parent);
		
        Font font	= new Font(null, "Arial", 9, SWT.NORMAL);
		output		= new StyledText(parent, SWT.MULTI | SWT.READ_ONLY | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL);
		output.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		output.setFont(font);
		output.addListener(SWT.Modify, new Listener(){
		    public void handleEvent(Event e){
		        output.setTopIndex(output.getLineCount() - 1);
		    }
		});

		output.addLineBackgroundListener(new LineBackgroundListener() {
			@Override
			public void lineGetBackground(LineBackgroundEvent event) {
				String message			= event.lineText;
				Color background		= new Color(null, 255, 255, 255);

				 if(message.startsWith("[INFO] ")) {
					background = new Color(null, 234, 247, 255);
				} else if(message.startsWith("[ERROR] ")) {
					background = new Color(null, 246, 210, 206);
				} else if(message.startsWith("[FATAL] ") || message.startsWith("[PANIC] ")) {
					background = new Color(null, 150, 0, 0);
				} else if(message.startsWith("[DEBUG] ") || message.startsWith("[WARN] ")) {
					background = new Color(null, 255, 251, 204);
				}

				event.lineBackground = background;
			}
		});
		
		output.addLineStyleListener(new LineStyleListener() {
			public void lineGetStyle(LineStyleEvent event) {
				List<StyleRange> styles	= new ArrayList<StyleRange>();
				String message			= event.lineText;
				Color foreground		= new Color(null, 0, 0, 0);

				if(message.startsWith("[INFO] ")) {
					foreground = new Color(null, 76, 116, 165);
				} else if(message.startsWith("[ERROR] ")) {
					foreground = new Color(null, 169, 0, 31);
				} else if(message.startsWith("[FATAL] ") || message.startsWith("[PANIC] ")) {
					foreground = new Color(null, 255, 255, 255);
				} else if(message.startsWith("[DEBUG] ")) {
					foreground = new Color(null, 232, 119, 44);
				}
				
				StyleRange style = new StyleRange(event.lineOffset, event.lineOffset + message.length(), foreground, null);
				
				if(message.startsWith("[PANIC] ")) {
					style.fontStyle = SWT.BOLD;
				}
				
				styles.add(style);
				event.styles = (StyleRange[]) styles.toArray(new StyleRange[0]);
			}
		});
        input		= new Text(parent, SWT.BORDER);
        input.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, false, false, 1, 1));
        input.setMessage("Command Line...");
	}
	
	private void insert(String message) {
		if(this.output == null) {
			return;
		}
		
		Display.getDefault().syncExec(new Runnable() {
            public void run() {
            	if(!output.isDisposed()) {
	            	output.append(message);
	            	output.append("\r\n");
            	}
            }
		});
	}
	
	public static void log(String message) {
		Console.get().insert(message);
	}
	
	@Override
	public void setFocus() {
		// Set the focus
	}
}