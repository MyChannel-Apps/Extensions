package de.mca.extensions.eclipse;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.LineStyleEvent;
import org.eclipse.swt.custom.LineStyleListener;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.part.ViewPart;

public class Console extends ViewPart {
	public static final String ID	= "de.mca.extensions.eclipse.Console"; //$NON-NLS-1$
	public static Console instance	= null;
	private StyledText output;
	
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
		parent.setLayout(new FillLayout());
		
        Font font	= new Font(null, "Arial", 10, SWT.NORMAL);
		output		= new StyledText(parent, SWT.MULTI | SWT.READ_ONLY | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL);
		output.setFont(font);
		output.addListener(SWT.Modify, new Listener(){
		    public void handleEvent(Event e){
		        output.setTopIndex(output.getLineCount() - 1);
		    }
		});

		output.addLineStyleListener(new LineStyleListener() {
			public void lineGetStyle(LineStyleEvent event) {
				List<StyleRange> styles	= new ArrayList<StyleRange>();
				String message			= event.lineText;
				Color foreground		= new Color(null, 0, 0, 0);
				Color background		= new Color(null, 255, 255, 255);
				
				if(message.startsWith("[INFO] ")) {
					background = new Color(null, 234, 247, 255);
					foreground = new Color(null, 76, 116, 165);
				} else if(message.startsWith("[ERROR] ")) {
					background = new Color(null, 246, 210, 206);
					foreground = new Color(null, 169, 0, 31);
				} else if(message.startsWith("[FATAL] ")) {
					background = new Color(null, 246, 210, 206);
					foreground = new Color(null, 255, 0, 0);
				}

				styles.add(new StyleRange(event.lineOffset, event.lineOffset + message.length(), foreground, background));
				event.styles = (StyleRange[]) styles.toArray(new StyleRange[0]);
			}
		});
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