package de.mca.extensions.eclipse.assists.knuddels;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.wst.jsdt.ui.text.java.ContentAssistInvocationContext;
import org.eclipse.wst.jsdt.ui.text.java.IJavaCompletionProposalComputer;

public class Completion implements IJavaCompletionProposalComputer {
	@Override
	public List<ICompletionProposal> computeCompletionProposals(ContentAssistInvocationContext context, IProgressMonitor monitor) {
		List<ICompletionProposal> list	= new ArrayList<ICompletionProposal>();
		ITextViewer viewer				= context.getViewer();
		int offset						= context.getInvocationOffset();
        IDocument document				= viewer.getDocument();
        String input					= getInputString(document, offset);

        // Calculate actual class
        if(input.equals("") || input.startsWith("this.")) {
        	System.err.println("Can't find actual functions context!");
        } else {
        	//int length					= input.length();
        	
        	/*
            Model model					= ContentFromSources.getDefaultInstances().model;
            
            if(model == null){
            	/ Do Nothing /
            }	
            
            for(Entry entry: model.findMatchingEntries(input)){
            	String trigger	= entry.trigger;
            	String desc		= entry.desc;
            	//Image image	= (entry.type == EntryType.clazz) ? CLASS : METHOD;
            	Image image		= null;
            	
            	switch(entry.type) {
            		case module:	image = MODULE; break;
            		case method:	image = METHOD; break;
            		case clazz:		image = CLASS; break;        		
            		case property:	image = PROPERTY; break;	
            	}
            	
    			list.add(new CompletionProposal(trigger, offset - length, length, trigger.length(), image, trigger, null, desc)); 
            }*/
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
		/* Do Nothing */
	}

	@Override
	public void sessionStarted() {
		/* Do Nothing */
		// Check for update
		// Load JSON
		System.err.println("Session started.");
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
