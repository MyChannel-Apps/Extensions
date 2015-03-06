package de.mca.extensions.eclipse.assists.knuddels;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.jsdt.ui.text.java.ContentAssistInvocationContext;
import org.eclipse.wst.jsdt.ui.text.java.IJavaCompletionProposalComputer;

import de.mca.extensions.eclipse.Activator;
import de.mca.extensions.eclipse.Autocomplete;
import de.mca.extensions.eclipse.data.APIClass;
import de.mca.extensions.eclipse.data.APIMethod;
import de.mca.extensions.eclipse.interfaces.APIEntry;

public class Completion implements IJavaCompletionProposalComputer {
	protected Autocomplete autocomplete;
	
	@Override
	public List<ICompletionProposal> computeCompletionProposals(ContentAssistInvocationContext context, IProgressMonitor monitor) {
		List<ICompletionProposal> list	= new ArrayList<ICompletionProposal>();
		ITextViewer viewer				= context.getViewer();
		int offset						= context.getInvocationOffset();
        IDocument document				= viewer.getDocument();
        String input					= getInputString(document, offset);
    	int length						= input.length();
    	boolean is_new					= getInputString(document, offset - 1).equalsIgnoreCase("new");
    	
        // Calculate actual class
        if(input.equals("") || input.startsWith("this.")) {
        	System.err.println("Can't find actual functions context!");
        } else {
        	if(this.autocomplete != null) {
        		List<APIEntry> result = this.autocomplete.find(input);
        		
        		for(APIEntry entry : result) {
        			String trigger	= entry.getName();
        			String desc		= entry.getDescription();
        			Image image		= null;
        			
        			if(entry instanceof APIClass) {
        				image		= Activator.getImageDescriptor("icons/class.gif").createImage();
        			} else if(entry instanceof APIMethod) {
        				image		= Activator.getImageDescriptor("icons/method.gif").createImage();        				
        			}
        			
        			list.add(new CompletionProposal(trigger, offset - length, length, trigger.length(), image, trigger, null, desc));
        		}
        	}
        }
        
        System.err.println("Compute: " + input);
        
		return list;
	}

	@Override
	public List<IContextInformation> computeContextInformation(ContentAssistInvocationContext context, IProgressMonitor monitor) {
		return new ArrayList<IContextInformation>();
	}

	@Override
	public String getErrorMessage() {
		return null;
	}

	@Override
	public void sessionEnded() {
		this.autocomplete = null;
	}

	@Override
	public void sessionStarted() {
		this.autocomplete = Activator.getAutocompletement("knuddels");
	}
	
	public String getInputString(IDocument document, int offset) {
		StringBuffer buffer = new StringBuffer();
		
		while(true) {
			try {
				char charOffset = document.getChar(--offset);
				
				if(Character.isWhitespace(charOffset)) {
                    break;
				}
				
                buffer.append(charOffset);
            } catch (BadLocationException e) {
                break;
            }
        }
		
        return buffer.reverse().toString();
    }
}
