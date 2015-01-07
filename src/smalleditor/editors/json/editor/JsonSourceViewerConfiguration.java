package smalleditor.editors.json.editor;

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

public class JsonSourceViewerConfiguration extends CommonSourceViewerConfiguration {
	public JsonSourceViewerConfiguration(JsonEditor editor) {
		super(editor);
	}

	private JsonStringScanner stringScanner;
	
	
	@Override
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return new String[] { IDocument.DEFAULT_CONTENT_TYPE,
				JsonPartitionScanner.JSON_COMMENT, JsonPartitionScanner.JSON_KEYWORD,
				JsonPartitionScanner.JSON_KEYSTRING, JsonPartitionScanner.JSON_STRING };
	}
	@Override
	protected IReconcilingStrategy getReconcilingStrategy(CommonEditor editor) {
		return new JsonReconcilingStrategy(this.editor);
	}
	
	@Override
	public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
		ContentAssistant assistant = new ContentAssistant();
		IContentAssistProcessor sharedProcessor = new JsonContentAssistProcessor();
				
		assistant.setContentAssistProcessor(sharedProcessor, IDocument.DEFAULT_CONTENT_TYPE);
				
		//assistant.setEmptyMessage("Sorry, no hint for you :-/");
		assistant.enableAutoActivation(true);
		assistant.setAutoActivationDelay(250);
		
		return assistant;
	}


	protected JsonStringScanner getStringScanner() {
		if (stringScanner == null) {
			Color stringColor = colorManager.getColorFromPreferencesKey(PreferenceNames.P_STRING_COLOR);
			stringScanner = new JsonStringScanner(stringColor);
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
		reconciler.setDamager(commentRepairer, JsonPartitionScanner.JSON_COMMENT);
		reconciler.setRepairer(commentRepairer, JsonPartitionScanner.JSON_COMMENT);

		CommonNonRuleBasedDamagerRepairer stringRepairer = new CommonNonRuleBasedDamagerRepairer(
				new TextAttribute(
						colorManager.getColorFromPreferencesKey(PreferenceNames.P_STRING_COLOR)));
		reconciler.setDamager(stringRepairer, JsonPartitionScanner.JSON_STRING);
		reconciler.setRepairer(stringRepairer, JsonPartitionScanner.JSON_STRING);
		
		CommonNonRuleBasedDamagerRepairer keyStringRepairer = new CommonNonRuleBasedDamagerRepairer(
				new TextAttribute(
						colorManager.getColorFromPreferencesKey(PreferenceNames.P_DEFAULT_COLOR), null,
						SWT.BOLD));
		reconciler.setDamager(keyStringRepairer, JsonPartitionScanner.JSON_KEYSTRING);
		reconciler.setRepairer(keyStringRepairer, JsonPartitionScanner.JSON_KEYSTRING);

		CommonNonRuleBasedDamagerRepairer keywordRepairer = new CommonNonRuleBasedDamagerRepairer(
				new TextAttribute(
						colorManager.getColorFromPreferencesKey(PreferenceNames.P_KEYWORD_COLOR), null,
						SWT.BOLD));
		reconciler.setDamager(keywordRepairer, JsonPartitionScanner.JSON_KEYWORD);
		reconciler.setRepairer(keywordRepairer, JsonPartitionScanner.JSON_KEYWORD);

		return reconciler;
	}

}