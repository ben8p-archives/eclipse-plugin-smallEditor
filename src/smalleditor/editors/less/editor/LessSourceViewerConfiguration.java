package smalleditor.editors.less.editor;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.reconciler.IReconcilingStrategy;
import org.eclipse.jface.text.source.ISourceViewer;

import smalleditor.editors.common.editor.CommonEditor;
import smalleditor.editors.css.editor.CssSourceViewerConfiguration;

public class LessSourceViewerConfiguration extends CssSourceViewerConfiguration {
	public LessSourceViewerConfiguration(LessEditor editor) {
		super(editor);
	}

	@Override
	protected IReconcilingStrategy getReconcilingStrategy(CommonEditor editor) {
		return new LessReconcilingStrategy(this.editor);
	}
	
	@Override
	public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
		ContentAssistant assistant = new ContentAssistant();
		IContentAssistProcessor sharedProcessor = new LessContentAssistProcessor();
				
		assistant.setContentAssistProcessor(sharedProcessor, IDocument.DEFAULT_CONTENT_TYPE);

		assistant.enableAutoActivation(true);
		assistant.setAutoActivationDelay(250);
		
		return assistant;
	}

}