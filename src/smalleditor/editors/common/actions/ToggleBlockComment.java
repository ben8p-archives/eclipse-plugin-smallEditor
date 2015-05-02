package smalleditor.editors.common.actions;

import java.util.regex.Pattern;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.texteditor.IDocumentProvider;

import smalleditor.editors.common.ACommonEditor;
import smalleditor.editors.css.CssEditor;
import smalleditor.editors.html.HtmlEditor;
import smalleditor.editors.javascript.JavascriptEditor;
import smalleditor.editors.less.LessEditor;

public class ToggleBlockComment extends AbstractHandler {

	public Object execute(ExecutionEvent event) {
		IEditorPart editor = HandlerUtil.getActiveEditor(event);
		if (editor instanceof JavascriptEditor || editor instanceof CssEditor || editor instanceof LessEditor) {
			run((ACommonEditor) editor, "/*", "*/");
		} else if (editor instanceof HtmlEditor) {
			run((ACommonEditor) editor, "<!--", "-->");
		}
		return null;
	}
	
	private void run(ACommonEditor editor, String open, String close) {
		IDocumentProvider docProvider = editor.getDocumentProvider();
		IEditorInput input = editor.getEditorInput();
		if (docProvider == null || input == null){ return; }
		
		ITextSelection selection = getCurrentSelection(editor);
		if (selection == null){ return; }
		
		IDocument document = docProvider.getDocument(input);
		if (document == null){ return; }
		
		String searchRange = "";
		int offset = Math.max(0, selection.getOffset() - open.length());
		try {
			searchRange = document.get(
					offset, 
					Math.min(document.getLength(), selection.getLength() + open.length() + close.length())
			);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		if(searchRange.contains(open) && searchRange.contains(close)) {
			unsurround(document, searchRange, offset, open, close);
		} else {
			surround(document, selection, open, close);
		}
		

	}
	
	private void unsurround(IDocument d, String selection, int offset, String open, String close) {
		try {
			int length = selection.length();
			String newText = selection.replaceAll(Pattern.quote(open), "").replaceAll(Pattern.quote(close), "");
			d.replace(offset, length, newText);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		
	}
	
	private void surround(IDocument d, ITextSelection selection, String open, String close) {
	
		try {
			String newText = open + selection.getText() + close;
			d.replace(selection.getOffset(), selection.getLength(), newText);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		
	}
	
	private ITextSelection getCurrentSelection(ACommonEditor editor) {
		if (editor != null) {
			ISelectionProvider provider = editor.getSelectionProvider();
			if (provider != null) {
				ISelection selection = provider.getSelection();
				if (selection instanceof ITextSelection)
					return (ITextSelection) selection;
			}
		}
		return null;
	}

}
