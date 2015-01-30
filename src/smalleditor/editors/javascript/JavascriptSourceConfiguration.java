/**
 * Aptana Studio
 * Copyright (c) 2005-2013 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the GNU Public License (GPL) v3 (with exceptions).
 * Please see the license.html included with this distribution for details.
 * Any modifications to this file must keep this entire header intact.
 */

package smalleditor.editors.javascript;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;

import smalleditor.common.rules.EmptyCommentRule;
import smalleditor.common.rules.ISubPartitionScanner;
import smalleditor.common.rules.SubPartitionScanner;
import smalleditor.editors.common.ACommonEditor;
import smalleditor.editors.common.ACommonSourceConfiguration;
import smalleditor.editors.common.CommonNonRuleBasedDamagerRepairer;
import smalleditor.editors.common.CommonPredicateWordRule;
import smalleditor.editors.common.CommonScanner;
import smalleditor.editors.common.CommonWordDetector;
import smalleditor.preferences.ColorManager;
import smalleditor.preferences.IPreferenceNames;
import smalleditor.utils.IConstants;

/**
 * @author Max Stepanov
 */
public class JavascriptSourceConfiguration extends ACommonSourceConfiguration {

	public final static String PREFIX = "__js_";
	public final static String JS_DEFAULT = PREFIX + IDocument.DEFAULT_CONTENT_TYPE; //PREFIX + "default";
	public final static String JS_MULTILINE_COMMENT = PREFIX + "multiline_comment";
	public final static String JS_COMMENT = PREFIX + "comment";
	public final static String JS_KEYWORD = PREFIX + "keyword";
	public final static String JS_STRING = PREFIX + "string";

	public final static String CONTENT_TYPE = IConstants.CONTENT_TYPE_JS;

	private ColorManager colorManager = ColorManager.getDefault();
	private CommonScanner scanner;

	public static final String[] CONTENT_TYPES = new String[] { 
		IDocument.DEFAULT_CONTENT_TYPE, JS_DEFAULT, JS_MULTILINE_COMMENT, JS_COMMENT, JS_KEYWORD, JS_STRING};

	private static final String[][] TOP_CONTENT_TYPES = new String[][] { { CONTENT_TYPE } };
	/**
	 * Array of keyword token strings.
	 */
	private static String[] keywordTokens = {
		"break",
		"case",
		"class",
		"catch",
		"const",
		"continue",
		"debugger",
		"default",
		"delete",
		"do",
		"else",
		"export",
		"extends",
		"finally",
		"for",
		"function",
		"if",
		"import",
		"in",
		"instanceof",
		"let",
		"new",
		"return",
		"super",
		"switch",
		"this",
		"throw",
		"try",
		"typeof",
		"var",
		"void",
		"while",
		"with",
		"yield",
		"enum",
		"await",
		"implements",
		"package",
		"protected",
		"static",
		"interface",
		"private",
		"public",
		"abstract",
		"boolean",
		"byte",
		"char",
		"double",
		"final",
		"float",
		"goto",
		"int",
		"long",
		"native",
		"short",
		"synchronized",
		"transient",
		"volatile"
	
	};

	/**
	 * Array of constant token strings.
	 */
	private static String[] constantTokens = { "this", "false", "null", "true", "undefined"};
	
	private IPredicateRule[] partitioningRules = null;

	private static JavascriptSourceConfiguration instance;

	public static JavascriptSourceConfiguration getDefault() {
		if (instance == null) {
			instance = new JavascriptSourceConfiguration();
		}
		return instance;
	}

	private JavascriptSourceConfiguration() {
		/**
		 * Array of keyword token strings.
		 * see : http://www.blooberry.com/indexdot/css/propindex/all.htm
		 */
		
		
		List rules = new ArrayList();

		rules.add(new EmptyCommentRule(getToken(JS_MULTILINE_COMMENT)));
		rules.add(new MultiLineRule("/*", "*/", getToken(JS_MULTILINE_COMMENT), (char) 0, true));
		rules.add(new EndOfLineRule("//", getToken(JS_COMMENT)));
		rules.add(new SingleLineRule("\"", "\"", getToken(JS_STRING), '\\'));
		rules.add(new SingleLineRule("/", "/", getToken(JS_STRING), '\\'));
		rules.add(new SingleLineRule("'", "'", getToken(JS_STRING), '\\'));
		
		CommonPredicateWordRule keywordRule = new CommonPredicateWordRule(
				new CommonWordDetector(), getToken(JS_DEFAULT), keywordTokens, getToken(JS_KEYWORD));
		
		keywordRule.addWords(constantTokens, getToken(JS_KEYWORD));
		rules.add(keywordRule);
		
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
		return CONTENT_TYPES;
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
		return new SubPartitionScanner(partitioningRules, CONTENT_TYPES,
				getToken(JS_DEFAULT));
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
		return null;
	}

	protected CommonScanner getScanner() {
		if (scanner == null) {
			Color defaultColor = colorManager
					.getColorFromPreferencesKey(IPreferenceNames.P_DEFAULT_COLOR);
			scanner = new CommonScanner(defaultColor);
			scanner.setDefaultReturnToken(new Token(new TextAttribute(
					defaultColor)));
		}

		return scanner;
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
		
		reconciler.setDamager(dr, JS_DEFAULT);
		reconciler.setRepairer(dr, JS_DEFAULT);
		
		CommonNonRuleBasedDamagerRepairer commentRepairer = new CommonNonRuleBasedDamagerRepairer(
				new TextAttribute(
						colorManager
								.getColorFromPreferencesKey(IPreferenceNames.P_COMMENT_COLOR)));
		reconciler.setDamager(commentRepairer, JS_MULTILINE_COMMENT);
		reconciler.setRepairer(commentRepairer, JS_MULTILINE_COMMENT);
		reconciler.setDamager(commentRepairer, JS_COMMENT);
		reconciler.setRepairer(commentRepairer, JS_COMMENT);

		CommonNonRuleBasedDamagerRepairer stringRepairer = new CommonNonRuleBasedDamagerRepairer(
				new TextAttribute(
						colorManager
								.getColorFromPreferencesKey(IPreferenceNames.P_STRING_COLOR)));
		reconciler.setDamager(stringRepairer, JS_STRING);
		reconciler.setRepairer(stringRepairer, JS_STRING);

		CommonNonRuleBasedDamagerRepairer keywordRepairer = new CommonNonRuleBasedDamagerRepairer(
				new TextAttribute(
						colorManager
								.getColorFromPreferencesKey(IPreferenceNames.P_KEYWORD_COLOR),
						null, SWT.BOLD));
		reconciler.setDamager(keywordRepairer, JS_KEYWORD);
		reconciler.setRepairer(keywordRepairer, JS_KEYWORD);
	}

	public IContentAssistProcessor getContentAssistProcessor(
			ACommonEditor editor, String contentType) {
		return new JavascriptContentAssistProcessor();
	}

//	private ITokenScanner getCommentScanner() {
//		return new CssCommentScanner(getToken(CSS_COMMENT));
//	}
//
//	private ITokenScanner getStringScanner() {
//		return new CommonStringScanner();
//	}


}
