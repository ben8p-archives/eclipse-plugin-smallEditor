/**
 * Aptana Studio
 * Copyright (c) 2005-2013 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the GNU Public License (GPL) v3 (with exceptions).
 * Please see the license.html included with this distribution for details.
 * Any modifications to this file must keep this entire header intact.
 */

package smalleditor.editors.json;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;

import smalleditor.common.rules.ISubPartitionScanner;
import smalleditor.common.rules.SubPartitionScanner;
import smalleditor.editors.common.CommonEditor;
import smalleditor.editors.common.CommonNonRuleBasedDamagerRepairer;
import smalleditor.editors.common.CommonPredicateWordRule;
import smalleditor.editors.common.CommonScanner;
import smalleditor.editors.common.CommonSourceConfiguration;
import smalleditor.editors.common.CommonWordDetector;
import smalleditor.preferences.ColorManager;
import smalleditor.preferences.PreferenceNames;
import smalleditor.utils.Constants;

/**
 * @author Max Stepanov
 */
public class JsonSourceConfiguration extends CommonSourceConfiguration {

	public final static String PREFIX = "__json_";
	public final static String JSON_DEFAULT = PREFIX + IDocument.DEFAULT_CONTENT_TYPE; //PREFIX + "default";
	public final static String JSON_COMMENT = PREFIX + "comment";
	public final static String JSON_KEYWORD = PREFIX + "keyword";
	public final static String JSON_STRING = PREFIX + "string";
	public final static String JSON_KEYSTRING = PREFIX + "keystring";

	public final static String CONTENT_TYPE = Constants.CONTENT_TYPE_JSON;

	private ColorManager colorManager = new ColorManager();
	private CommonScanner scanner;

	public static final String[] CONTENT_TYPES = new String[] { JSON_DEFAULT,
		JSON_COMMENT, JSON_KEYWORD, JSON_STRING, JSON_KEYSTRING};

	private static final String[][] TOP_CONTENT_TYPES = new String[][] { { CONTENT_TYPE } };
	/**
	 * Array of keyword token strings.
	 */
	private static String[] keywordTokens = {
		"false", "null", "true", "undefined"
	};
	
	private IPredicateRule[] partitioningRules = null;

	private static JsonSourceConfiguration instance;

	public static JsonSourceConfiguration getDefault() {
		if (instance == null) {
			instance = new JsonSourceConfiguration();
		}
		return instance;
	}

	protected JsonSourceConfiguration() {
		/**
		 * Array of keyword token strings.
		 * see : http://www.blooberry.com/indexdot/css/propindex/all.htm
		 */
		
		
		List rules = new ArrayList();

		rules.add(new MultiLineRule("/*", "*/", getToken(JSON_COMMENT)));
		
		rules.add(new SingleLineRule(":\"", "\"", getToken(JSON_STRING), '\\'));
		rules.add(new SingleLineRule(": \"", "\"", getToken(JSON_STRING), '\\'));
		rules.add(new SingleLineRule(":	\"", "\"", getToken(JSON_STRING), '\\'));
		rules.add(new SingleLineRule(",\"", "\"", getToken(JSON_STRING), '\\'));
		rules.add(new SingleLineRule(", \"", "\"", getToken(JSON_STRING), '\\'));
		rules.add(new SingleLineRule(",	\"", "\"", getToken(JSON_STRING), '\\'));
		rules.add(new SingleLineRule("[\"", "\"", getToken(JSON_STRING), '\\'));
		rules.add(new SingleLineRule("[ \"", "\"", getToken(JSON_STRING), '\\'));
		rules.add(new SingleLineRule("[	\"", "\"", getToken(JSON_STRING), '\\'));
		
		rules.add(new SingleLineRule("\"", "\"", getToken(JSON_KEYSTRING), '\\'));
		
		CommonPredicateWordRule keywordRule = new CommonPredicateWordRule(
				new CommonWordDetector(), getToken(JSON_DEFAULT), keywordTokens, getToken(JSON_KEYWORD));
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
				getToken(JSON_DEFAULT));
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
					.getColorFromPreferencesKey(PreferenceNames.P_DEFAULT_COLOR);
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
		
		reconciler.setDamager(dr, JSON_DEFAULT);
		reconciler.setRepairer(dr, JSON_DEFAULT);

		CommonNonRuleBasedDamagerRepairer commentRepairer = new CommonNonRuleBasedDamagerRepairer(
				new TextAttribute(
						colorManager.getColorFromPreferencesKey(PreferenceNames.P_COMMENT_COLOR)));
		reconciler.setDamager(commentRepairer, JSON_COMMENT);
		reconciler.setRepairer(commentRepairer, JSON_COMMENT);

		CommonNonRuleBasedDamagerRepairer stringRepairer = new CommonNonRuleBasedDamagerRepairer(
				new TextAttribute(
						colorManager.getColorFromPreferencesKey(PreferenceNames.P_STRING_COLOR)));
		reconciler.setDamager(stringRepairer, JSON_STRING);
		reconciler.setRepairer(stringRepairer, JSON_STRING);
		
		CommonNonRuleBasedDamagerRepairer keyStringRepairer = new CommonNonRuleBasedDamagerRepairer(
				new TextAttribute(
						colorManager.getColorFromPreferencesKey(PreferenceNames.P_DEFAULT_COLOR), null,
						SWT.BOLD));
		reconciler.setDamager(keyStringRepairer, JSON_KEYSTRING);
		reconciler.setRepairer(keyStringRepairer, JSON_KEYSTRING);

		CommonNonRuleBasedDamagerRepairer keywordRepairer = new CommonNonRuleBasedDamagerRepairer(
				new TextAttribute(
						colorManager.getColorFromPreferencesKey(PreferenceNames.P_KEYWORD_COLOR), null,
						SWT.BOLD));
		reconciler.setDamager(keywordRepairer, JSON_KEYWORD);
		reconciler.setRepairer(keywordRepairer, JSON_KEYWORD);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.aptana.editor.common.ISourceViewerConfiguration#getContentAssistProcessor
	 * (com.aptana.editor.common. AbstractThemeableEditor, java.lang.String)
	 */
	public IContentAssistProcessor getContentAssistProcessor(
			CommonEditor editor, String contentType) {
		return new JsonContentAssistProcessor();
	}

//	private ITokenScanner getCommentScanner() {
//		return new CssCommentScanner(getToken(CSS_COMMENT));
//	}
//
//	private ITokenScanner getStringScanner() {
//		return new CommonStringScanner();
//	}


}
