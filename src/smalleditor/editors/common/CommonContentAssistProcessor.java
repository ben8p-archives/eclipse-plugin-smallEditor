package smalleditor.editors.common;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;

import smalleditor.common.IContentAssistProvider;
import smalleditor.utils.CharUtility;

public class CommonContentAssistProcessor implements IContentAssistProcessor {
	protected IContentAssistProvider provider;
	public CommonContentAssistProcessor() {
	}
	
	@Override
	public ICompletionProposal[] computeCompletionProposals(ITextViewer textViewer,
			int documentOffset) {
		
		ICompletionProposal[] proposals = null;
		try {
			IDocument document = textViewer.getDocument();
			int currOffset = documentOffset - 1; //cursor place in the text
			String currWord = ""; //$NON-NLS-1$
			String currToken = ""; //$NON-NLS-1$
			char currChar;
			char indentChar;
			String indentPath = ""; //$NON-NLS-1$
			
			String completionProposalAutoActivationCharacters = new String(getCompletionProposalAutoActivationCharacters());
			//calculate the indentation
			IRegion lineInformation = document.getLineInformationOfOffset(currOffset);
			int indentOffset = lineInformation.getOffset();
			indentChar = document.get(indentOffset, 1).charAt(0);
			while(CharUtility.isWhiteSpace(indentChar)) {
				indentPath += indentChar;
				indentOffset++;
				indentChar = document.get(indentOffset, 1).charAt(0);
			}
			
			// build the world until we found a separator (anything else than a a-z0-9_)
			while (currOffset > 0) {
				currChar = document.getChar(currOffset);
				if(CharUtility.minus != currChar && !CharUtility.isAlphaNumeric(currChar) && !completionProposalAutoActivationCharacters.contains(String.valueOf(currChar))) { break; }
				if(!completionProposalAutoActivationCharacters.contains(String.valueOf(currChar))) {
					currWord = currChar + currWord;
				}
				currToken = currChar + currToken;
				currOffset--;
			}
			
		
//			System.out.println("currWord for content assist:" + currWord + " -token:" + currToken);
			// compute proposal
			List suggestions = provider.suggest(currWord);
			
			if (suggestions.size() > 0) {
				proposals = buildProposals(suggestions, currToken, documentOffset, indentPath);
			}
		} catch (Exception e) {}
		
		return proposals;
	}

	private ICompletionProposal[] buildProposals(List<String> suggestions,
			String replacedWord, int offset, String indentPath) throws Exception {
		
		ICompletionProposal[] proposals = new ICompletionProposal[suggestions.size()];
		int index = 0;

		Iterator it = suggestions.iterator();
		while (it.hasNext()) {
			HashMap item = (HashMap) it.next();
			
			Boolean appendValue = (Boolean) item.get("append-value"); //$NON-NLS-1$
			String stringValue = (String) item.get("value"); //$NON-NLS-1$
			String currSuggestion = (appendValue == true ? replacedWord + stringValue : stringValue).replace("\n", "\n" + indentPath); //$NON-NLS-1$ //$NON-NLS-2$
			int documentOffset = offset - replacedWord.length();
			int length = replacedWord.length();
			
			CompletionProposal cp = new CompletionProposal(currSuggestion,
					documentOffset, length, currSuggestion.length(),
					null, stringValue, null, null);
			proposals[index] = cp;
			index++;
			
		}
//		for (Iterator<String> i = suggestions.iterator(); i.hasNext();) {
//			String currSuggestion = (String) i.next();
//			
//			CompletionProposal cp = new CompletionProposal(currSuggestion,
//					offset, replacedWord.length(), currSuggestion.length(),
//					null, currSuggestion, null, null);
//			proposals[index] = cp;
//			index++;
//		}
		return proposals;
	}

	@Override
	public IContextInformation[] computeContextInformation(ITextViewer arg0,
			int arg1) {
		return null;
	}

	@Override
	public char[] getCompletionProposalAutoActivationCharacters() {
		return null;
	}

	@Override
	public char[] getContextInformationAutoActivationCharacters() {
		return null;
	}

	@Override
	public IContextInformationValidator getContextInformationValidator() {
		return null;
	}

	@Override
	public String getErrorMessage() {
		return null;
	}
		
}