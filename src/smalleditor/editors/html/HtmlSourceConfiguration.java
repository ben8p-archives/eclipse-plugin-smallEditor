/**
 * Aptana Studio
 * Copyright (c) 2005-2013 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the GNU Public License (GPL) v3 (with exceptions).
 * Please see the license.html included with this distribution for details.
 * Any modifications to this file must keep this entire header intact.
 */

package smalleditor.editors.html;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.graphics.Color;

import smalleditor.common.rules.CaseInsensitiveMultiLineRule;
import smalleditor.common.rules.ExtendedToken;
import smalleditor.common.rules.FixedMultiLineRule;
import smalleditor.common.rules.ISubPartitionScanner;
import smalleditor.common.rules.PartitionerSwitchingIgnoreRule;
import smalleditor.common.rules.TagRule;
import smalleditor.editors.common.CommonEditor;
import smalleditor.editors.common.CommonNonRuleBasedDamagerRepairer;
import smalleditor.editors.common.CommonScanner;
import smalleditor.editors.common.CommonSourceConfiguration;
import smalleditor.editors.css.CssSourceConfiguration;
import smalleditor.editors.html.rules.HtmlDocTypeRule;
import smalleditor.editors.javascript.JavascriptSourceConfiguration;
import smalleditor.preferences.ColorManager;
import smalleditor.preferences.PreferenceNames;
import smalleditor.utils.Constants;
import smalleditor.utils.TextUtility;

/**
 * @author Max Stepanov
 */
public class HtmlSourceConfiguration extends CommonSourceConfiguration{

	public final static String PREFIX = "__html_";
	public final static String HTML_DEFAULT = PREFIX + IDocument.DEFAULT_CONTENT_TYPE; //PREFIX + "default";
	public final static String HTML_COMMENT = PREFIX + "comment";
	public final static String HTML_TAG = PREFIX + "tag";
	public final static String HTML_TAG_CLOSE = PREFIX + "tag_close"; //$NON-NLS-1$
	public final static String HTML_STRING = PREFIX + "string";
	public final static String HTML_ATTRIBUTE = PREFIX + "attribute";
	public final static String HTML_SCRIPT = PREFIX + "script"; //$NON-NLS-1$
	public final static String HTML_STYLE = PREFIX + "style"; //$NON-NLS-1$
	public final static String HTML_SVG = PREFIX + "svg"; //$NON-NLS-1$
	public final static String HTML_CDATA = PREFIX + "cdata"; //$NON-NLS-1$
	public final static String HTML_DOCTYPE = PREFIX + "doctype"; //$NON-NLS-1$

	public final static String CONTENT_TYPE = Constants.CONTENT_TYPE_HTML;

	private ColorManager colorManager = ColorManager.getDefault();
	private CommonScanner scanner;

	public static final String[] CONTENT_TYPES = new String[] { /*IDocument.DEFAULT_CONTENT_TYPE,*/ HTML_DEFAULT,
		HTML_COMMENT, HTML_TAG, HTML_STRING, HTML_ATTRIBUTE, HTML_SCRIPT, HTML_STYLE, HTML_SVG, HTML_TAG_CLOSE, HTML_CDATA, HTML_DOCTYPE};

	private static final String[][] TOP_CONTENT_TYPES = new String[][] { { CONTENT_TYPE },
		{ CONTENT_TYPE, Constants.CONTENT_TYPE_JS },
		{ CONTENT_TYPE, Constants.CONTENT_TYPE_CSS }/*,
		{ CONTENT_TYPE, Constants.CONTENT_TYPE_SVG }*/ };
	
	
	private IPredicateRule[] partitioningRules = null;

	private static HtmlSourceConfiguration instance;

	public static HtmlSourceConfiguration getDefault() {
		if (instance == null) {
			instance = new HtmlSourceConfiguration();
		}
		return instance;
	}

	private HtmlSourceConfiguration() {
		
		List rules = new ArrayList();

//		rules.add(new MultiLineRule("<!--", "-->", getToken(HTML_COMMENT)));
//		rules.add(new SingleLineRule("\"", "\"", getToken(HTML_STRING)));
//
//		CommonPredicateWordRule keywordRule = new CommonPredicateWordRule(
//				new CommonWordDetector(), getToken(HTML_DEFAULT), new String[] {}, getToken(HTML_DEFAULT));
//		rules.add(keywordRule);
		
		rules.add(new CaseInsensitiveMultiLineRule(
				"<!DOCTYPE ", ">", getToken(HTML_DOCTYPE))); //$NON-NLS-1$ //$NON-NLS-2$
		rules.add(new HtmlDocTypeRule(getToken(HTML_CDATA)));
		rules.add(new PartitionerSwitchingIgnoreRule(new FixedMultiLineRule(
				"<!--", "-->", getToken(HTML_COMMENT), (char) 0, true))); //$NON-NLS-1$ //$NON-NLS-2$
		rules.add(new TagRule(
				"script", new ExtendedToken(getToken(HTML_SCRIPT)), true)); //$NON-NLS-1$
		rules.add(new TagRule(
				"style", new ExtendedToken(getToken(HTML_STYLE)), true)); //$NON-NLS-1$
		rules.add(new TagRule(
				"svg", new ExtendedToken(getToken(HTML_SVG)), true)); //$NON-NLS-1$
		rules.add(new TagRule("/", getToken(HTML_TAG_CLOSE))); //$NON-NLS-1$
		rules.add(new TagRule(new ExtendedToken(getToken(HTML_TAG))));

		partitioningRules = new IPredicateRule[rules.size()];
		rules.toArray(partitioningRules);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.aptana.editor.common.IPartitioningConfiguration#getContentTypes()
	 */
	public String[] getContentTypes() {
		return TextUtility.combine(new String[][] { CONTENT_TYPES, JavascriptSourceConfiguration.CONTENT_TYPES,
				CssSourceConfiguration.CONTENT_TYPES });
	}
	

	public String[][] getTopContentTypes() {
		return TOP_CONTENT_TYPES;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.aptana.editor.common.IPartitioningConfiguration#getPartitioningRules
	 * ()
	 */
	public IPredicateRule[] getPartitioningRules() {
		return partitioningRules;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.aptana.editor.common.IPartitioningConfiguration#createSubPartitionScanner
	 * ()
	 */
	public ISubPartitionScanner createSubPartitionScanner() {
		return new HtmlSubPartitionScanner();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.aptana.editor.common.IPartitioningConfiguration#getDocumentContentType
	 * (java.lang.String)
	 */
	public String getDocumentContentType(String contentType) {
		if (contentType.startsWith(PREFIX)) {
			return CONTENT_TYPE;
		}
		String result = JavascriptSourceConfiguration.getDefault()
				.getDocumentContentType(contentType);
		if (result != null) {
			return result;
		}
		result = CssSourceConfiguration.getDefault().getDocumentContentType(
				contentType);
		if (result != null) {
			return result;
		}

		return null;
	}

	protected CommonScanner getScanner() {
		if (scanner == null) {
			Color defaultColor = colorManager
					.getColorFromPreferencesKey(PreferenceNames.P_DEFAULT_COLOR);
			scanner = new CommonScanner(defaultColor);
			scanner.setDefaultReturnToken(new Token(new TextAttribute(
					defaultColor)));
		}

		return scanner;
	}
	private ITokenScanner getHtmlTagScanner() {
		return new HtmlTagScanner();
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.aptana.editor.common.ISourceViewerConfiguration#
	 * setupPresentationReconciler(org.eclipse.jface.text.presentation
	 * .PresentationReconciler, org.eclipse.jface.text.source.ISourceViewer)
	 */
	public void setupPresentationReconciler(PresentationReconciler reconciler,
			ISourceViewer sourceViewer) {
		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(getScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);
		
		reconciler.setDamager(dr, HTML_DEFAULT);
		reconciler.setRepairer(dr, HTML_DEFAULT);
		
		JavascriptSourceConfiguration.getDefault().setupPresentationReconciler(reconciler, sourceViewer);
		CssSourceConfiguration.getDefault().setupPresentationReconciler(reconciler, sourceViewer);
		
		dr = new DefaultDamagerRepairer(getHtmlTagScanner());
		reconciler.setDamager(dr, HTML_SCRIPT);
		reconciler.setRepairer(dr, HTML_SCRIPT);

		reconciler.setDamager(dr, HTML_STYLE);
		reconciler.setRepairer(dr, HTML_STYLE);

		reconciler.setDamager(dr, HTML_SVG);
		reconciler.setRepairer(dr, HTML_SVG);

		reconciler.setDamager(dr, HTML_TAG);
		reconciler.setRepairer(dr, HTML_TAG);

		reconciler.setDamager(dr, HTML_TAG_CLOSE);
		reconciler.setRepairer(dr, HTML_TAG_CLOSE);
		
		dr = new DefaultDamagerRepairer(getDoctypeScanner());
		reconciler.setDamager(dr, HTML_DOCTYPE);
		reconciler.setRepairer(dr, HTML_DOCTYPE);
		
		CommonNonRuleBasedDamagerRepairer commentRepairer = new CommonNonRuleBasedDamagerRepairer(
				new TextAttribute(
						colorManager.getColorFromPreferencesKey(PreferenceNames.P_COMMENT_COLOR)));
		reconciler.setDamager(commentRepairer, HTML_COMMENT);
		reconciler.setRepairer(commentRepairer, HTML_COMMENT);

		CommonNonRuleBasedDamagerRepairer stringRepairer = new CommonNonRuleBasedDamagerRepairer(
				new TextAttribute(
						colorManager.getColorFromPreferencesKey(PreferenceNames.P_STRING_COLOR)));
		reconciler.setDamager(stringRepairer, HTML_STRING);
		reconciler.setRepairer(stringRepairer, HTML_STRING);
		
//		CommonNonRuleBasedDamagerRepairer keyStringRepairer = new CommonNonRuleBasedDamagerRepairer(
//				new TextAttribute(
//						colorManager.getColorFromPreferencesKey(PreferenceNames.P_KEYWORD_COLOR), null,
//						SWT.BOLD));
//		reconciler.setDamager(keyStringRepairer, HTML_DEFAULT);
//		reconciler.setRepairer(keyStringRepairer, HTML_DEFAULT);

//		CommonNonRuleBasedDamagerRepairer keywordRepairer = new CommonNonRuleBasedDamagerRepairer(
//				new TextAttribute(
//						colorManager.getColorFromPreferencesKey(PreferenceNames.P_DEFAULT_COLOR), null,
//						SWT.BOLD));
//		reconciler.setDamager(keywordRepairer, HTML_ATTRIBUTE);
//		reconciler.setRepairer(keywordRepairer, HTML_ATTRIBUTE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.aptana.editor.common.ISourceViewerConfiguration#getContentAssistProcessor
	 * (com.aptana.editor.common. AbstractThemeableEditor, java.lang.String)
	 */
	@Override
	public IContentAssistProcessor getContentAssistProcessor(
			CommonEditor editor, String contentType) {

		if (contentType.startsWith(JavascriptSourceConfiguration.PREFIX)) {
			return JavascriptSourceConfiguration.getDefault()
					.getContentAssistProcessor(editor, contentType);
		}
		if (contentType.startsWith(CssSourceConfiguration.PREFIX)) {
			return CssSourceConfiguration.getDefault()
					.getContentAssistProcessor(editor, contentType);
		}

		return new HtmlContentAssistProcessor();
	}
	
	private ITokenScanner getDoctypeScanner() {
		return new HtmlDoctypeScanner();
	}

//	private ITokenScanner getCommentScanner() {
//		return new CssCommentScanner(getToken(CSS_COMMENT));
//	}
//
//	private ITokenScanner getStringScanner() {
//		return new CommonStringScanner();
//	}

}
