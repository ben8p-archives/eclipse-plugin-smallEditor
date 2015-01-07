package smalleditor.editors.javascript.editor;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.reconciler.IReconcilingStrategy;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;

import smalleditor.editors.common.editor.CommonEditor;
import smalleditor.editors.common.editor.CommonNonRuleBasedDamagerRepairer;
import smalleditor.editors.common.editor.CommonSourceViewerConfiguration;
import smalleditor.preference.PreferenceNames;

public class JavascriptSourceViewerConfiguration extends CommonSourceViewerConfiguration {
	public JavascriptSourceViewerConfiguration(JavascriptEditor editor) {
		super(editor);
	}

	private JavascriptStringScanner stringScanner;
	
	
	@Override
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return new String[] { IDocument.DEFAULT_CONTENT_TYPE,
				JavascriptPartitionScanner.JS_COMMENT, JavascriptPartitionScanner.JS_KEYWORD,
				JavascriptPartitionScanner.JS_STRING };
	}
	@Override
	protected IReconcilingStrategy getReconcilingStrategy(CommonEditor editor) {
		return new JavascriptReconcilingStrategy(this.editor);
	}
	
	@Override
	public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
		ContentAssistant assistant = new ContentAssistant();
		IContentAssistProcessor sharedProcessor = new JavascriptContentAssistProcessor();
				
		// define assist processor for each content type -> we use the same for all types here
		assistant.setContentAssistProcessor(sharedProcessor, IDocument.DEFAULT_CONTENT_TYPE);
//		assistant.setContentAssistProcessor(sharedProcessor, MarkdownTextPartitionScanner.MARKDOWN_H1);
//		assistant.setContentAssistProcessor(sharedProcessor, MarkdownTextPartitionScanner.MARKDOWN_H2);
//		assistant.setContentAssistProcessor(sharedProcessor, MarkdownTextPartitionScanner.MARKDOWN_BOLD);
//		assistant.setContentAssistProcessor(sharedProcessor, MarkdownTextPartitionScanner.MARKDOWN_ITALICS);			
				
		//assistant.setEmptyMessage("Sorry, no hint for you :-/");
		assistant.enableAutoActivation(true);
		assistant.setAutoActivationDelay(250);
		
		return assistant;
	}


	protected JavascriptStringScanner getStringScanner() {
		if (stringScanner == null) {
			Color stringColor = colorManager.getColorFromPreferencesKey(PreferenceNames.P_STRING_COLOR);
			stringScanner = new JavascriptStringScanner(stringColor);
			stringScanner.setDefaultReturnToken(new Token(new TextAttribute(
					stringColor)));
		}

		return stringScanner;
	}

	public IPresentationReconciler getPresentationReconciler(
			ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();

		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(getScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

		CommonNonRuleBasedDamagerRepairer commentRepairer = new CommonNonRuleBasedDamagerRepairer(
				new TextAttribute(
						colorManager.getColorFromPreferencesKey(PreferenceNames.P_COMMENT_COLOR)));
		reconciler.setDamager(commentRepairer, JavascriptPartitionScanner.JS_COMMENT);
		reconciler.setRepairer(commentRepairer, JavascriptPartitionScanner.JS_COMMENT);

		CommonNonRuleBasedDamagerRepairer stringRepairer = new CommonNonRuleBasedDamagerRepairer(
				new TextAttribute(
						colorManager.getColorFromPreferencesKey(PreferenceNames.P_STRING_COLOR)));
		reconciler.setDamager(stringRepairer, JavascriptPartitionScanner.JS_STRING);
		reconciler.setRepairer(stringRepairer, JavascriptPartitionScanner.JS_STRING);

		CommonNonRuleBasedDamagerRepairer keywordRepairer = new CommonNonRuleBasedDamagerRepairer(
				new TextAttribute(
						colorManager.getColorFromPreferencesKey(PreferenceNames.P_KEYWORD_COLOR), null,
						SWT.BOLD));
		reconciler.setDamager(keywordRepairer, JavascriptPartitionScanner.JS_KEYWORD);
		reconciler.setRepairer(keywordRepairer, JavascriptPartitionScanner.JS_KEYWORD);

		return reconciler;
	}

}