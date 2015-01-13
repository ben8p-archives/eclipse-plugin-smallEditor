package smalleditor.editors.html.editor;

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

public class HtmlSourceViewerConfiguration extends CommonSourceViewerConfiguration {
	public HtmlSourceViewerConfiguration(HtmlEditor editor) {
		super(editor);
	}

	private HtmlStringScanner stringScanner;
	
	
	@Override
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return new String[] { IDocument.DEFAULT_CONTENT_TYPE,
				HtmlPartitionScanner.HTML_COMMENT, HtmlPartitionScanner.HTML_ATTRIBUTE,
				HtmlPartitionScanner.HTML_STRING, HtmlPartitionScanner.HTML_TAG };
	}
	@Override
	protected IReconcilingStrategy getReconcilingStrategy(CommonEditor editor) {
		return new HtmlReconcilingStrategy(this.editor);
	}
	
	@Override
	public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
		ContentAssistant assistant = new ContentAssistant();
		IContentAssistProcessor sharedProcessor = new HtmlContentAssistProcessor();
				
		assistant.setContentAssistProcessor(sharedProcessor, IDocument.DEFAULT_CONTENT_TYPE);
				
		//assistant.setEmptyMessage("Sorry, no hint for you :-/");
		assistant.enableAutoActivation(true);
		assistant.setAutoActivationDelay(250);
		
		return assistant;
	}


	protected HtmlStringScanner getStringScanner() {
		if (stringScanner == null) {
			Color stringColor = colorManager.getColorFromPreferencesKey(PreferenceNames.P_STRING_COLOR);
			stringScanner = new HtmlStringScanner(stringColor);
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
		reconciler.setDamager(commentRepairer, HtmlPartitionScanner.HTML_COMMENT);
		reconciler.setRepairer(commentRepairer, HtmlPartitionScanner.HTML_COMMENT);

		CommonNonRuleBasedDamagerRepairer stringRepairer = new CommonNonRuleBasedDamagerRepairer(
				new TextAttribute(
						colorManager.getColorFromPreferencesKey(PreferenceNames.P_STRING_COLOR)));
		reconciler.setDamager(stringRepairer, HtmlPartitionScanner.HTML_STRING);
		reconciler.setRepairer(stringRepairer, HtmlPartitionScanner.HTML_STRING);
		
		CommonNonRuleBasedDamagerRepairer keyStringRepairer = new CommonNonRuleBasedDamagerRepairer(
				new TextAttribute(
						colorManager.getColorFromPreferencesKey(PreferenceNames.P_KEYWORD_COLOR), null,
						SWT.BOLD));
		reconciler.setDamager(keyStringRepairer, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(keyStringRepairer, IDocument.DEFAULT_CONTENT_TYPE);

//		CommonNonRuleBasedDamagerRepairer keywordRepairer = new CommonNonRuleBasedDamagerRepairer(
//				new TextAttribute(
//						colorManager.getColorFromPreferencesKey(PreferenceNames.P_DEFAULT_COLOR), null,
//						SWT.BOLD));
//		reconciler.setDamager(keywordRepairer, HtmlPartitionScanner.HTML_ATTRIBUTE);
//		reconciler.setRepairer(keywordRepairer, HtmlPartitionScanner.HTML_ATTRIBUTE);

		return reconciler;
	}

}