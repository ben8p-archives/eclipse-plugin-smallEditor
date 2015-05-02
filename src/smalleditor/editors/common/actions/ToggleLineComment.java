package smalleditor.editors.common.actions;

import java.util.regex.Pattern;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.TextUtilities;
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

public class ToggleLineComment extends AbstractHandler {

	public Object execute(ExecutionEvent event) {
		IEditorPart editor = HandlerUtil.getActiveEditor(event);
		if (editor instanceof JavascriptEditor) {
			run((ACommonEditor) editor);
		} else if (editor instanceof CssEditor || editor instanceof LessEditor || editor instanceof HtmlEditor) {
			ToggleBlockComment block = new ToggleBlockComment();
			block.execute(event);
		}
		return null;
	}
	
	private void run(ACommonEditor editor) {
		IDocumentProvider docProvider = editor.getDocumentProvider();
		IEditorInput input = editor.getEditorInput();
		if (docProvider == null || input == null){ return; }
		
		ITextSelection selection = getCurrentSelection(editor);
		if (selection == null){ return; }
		
		IDocument document = docProvider.getDocument(input);
		if (document == null){ return; }
		
		comment(document, selection);
	}
	
	private void comment(IDocument d, ITextSelection selection) {
		try {
			int lineOffset = d.getLineOffset(selection.getStartLine());
			int endOffset = (selection.getOffset() - lineOffset) + selection.getLength();
			String text = d.get(lineOffset, endOffset);
			if(text.startsWith("//")) {
				text = text.substring(2).replaceAll("(\r\n*|\n\r*)//", "$1");
			} else {
				text = "//" + text.replaceAll("(\r\n*|\n\r*)", "$1//");
			}
			d.replace(lineOffset, endOffset, text);
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
