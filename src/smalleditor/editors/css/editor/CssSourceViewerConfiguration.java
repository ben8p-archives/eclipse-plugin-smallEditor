package smalleditor.editors.css.editor;

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

public class CssSourceViewerConfiguration extends CommonSourceViewerConfiguration {
	public CssSourceViewerConfiguration(CssEditor editor) {
		super(editor);
	}

	private CssStringScanner stringScanner;
	
	
	@Override
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return new String[] { IDocument.DEFAULT_CONTENT_TYPE,
				CssPartitionScanner.CSS_COMMENT, CssPartitionScanner.CSS_COLOR, CssPartitionScanner.CSS_KEYWORD,
				CssPartitionScanner.CSS_CLASSNAME, CssPartitionScanner.CSS_STRING };
	}
	@Override
	protected IReconcilingStrategy getReconcilingStrategy(CommonEditor editor) {
		return new CssReconcilingStrategy(this.editor);
	}
	
	@Override
	public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
		ContentAssistant assistant = new ContentAssistant();
		IContentAssistProcessor sharedProcessor = new CssContentAssistProcessor();
				
		assistant.setContentAssistProcessor(sharedProcessor, IDocument.DEFAULT_CONTENT_TYPE);

		assistant.enableAutoActivation(true);
		assistant.setAutoActivationDelay(250);
		
		return assistant;
	}


	protected CssStringScanner getStringScanner() {
		if (stringScanner == null) {
			Color stringColor = colorManager.getColorFromPreferencesKey(PreferenceNames.P_STRING_COLOR);
			stringScanner = new CssStringScanner(stringColor);
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
		reconciler.setDamager(commentRepairer, CssPartitionScanner.CSS_COMMENT);
		reconciler.setRepairer(commentRepairer, CssPartitionScanner.CSS_COMMENT);

		CommonNonRuleBasedDamagerRepairer stringRepairer = new CommonNonRuleBasedDamagerRepairer(
				new TextAttribute(
						colorManager.getColorFromPreferencesKey(PreferenceNames.P_STRING_COLOR)));
		reconciler.setDamager(stringRepairer, CssPartitionScanner.CSS_STRING);
		reconciler.setRepairer(stringRepairer, CssPartitionScanner.CSS_STRING);

		CommonNonRuleBasedDamagerRepairer keywordRepairer = new CommonNonRuleBasedDamagerRepairer(
				new TextAttribute(
						colorManager.getColorFromPreferencesKey(PreferenceNames.P_KEYWORD_COLOR), null,
						SWT.BOLD));
		reconciler.setDamager(keywordRepairer, CssPartitionScanner.CSS_KEYWORD);
		reconciler.setRepairer(keywordRepairer, CssPartitionScanner.CSS_KEYWORD);
		
		CssColorDamagerRepairer colorRepairer = new CssColorDamagerRepairer();
		reconciler.setDamager(colorRepairer, CssPartitionScanner.CSS_COLOR);
		reconciler.setRepairer(colorRepairer, CssPartitionScanner.CSS_COLOR);
		
		CommonNonRuleBasedDamagerRepairer classRepairer = new CommonNonRuleBasedDamagerRepairer(
				new TextAttribute(
						colorManager.getColorFromPreferencesKey(PreferenceNames.P_DEFAULT_COLOR), null,
						SWT.BOLD));
		reconciler.setDamager(classRepairer, CssPartitionScanner.CSS_CLASSNAME);
		reconciler.setRepairer(classRepairer, CssPartitionScanner.CSS_CLASSNAME);

		return reconciler;
	}

}