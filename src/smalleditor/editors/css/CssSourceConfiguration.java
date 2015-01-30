/**
 * Aptana Studio
 * Copyright (c) 2005-2013 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the GNU Public License (GPL) v3 (with exceptions).
 * Please see the license.html included with this distribution for details.
 * Any modifications to this file must keep this entire header intact.
 */

package smalleditor.editors.css;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;

import smalleditor.common.rules.AExtendedWordRule;
import smalleditor.common.rules.ISubPartitionScanner;
import smalleditor.common.rules.SubPartitionScanner;
import smalleditor.editors.common.ACommonEditor;
import smalleditor.editors.common.CommonHexaColorDetector;
import smalleditor.editors.common.CommonNonRuleBasedDamagerRepairer;
import smalleditor.editors.common.CommonPredicateWordRule;
import smalleditor.editors.common.CommonScanner;
import smalleditor.editors.common.ACommonSourceConfiguration;
import smalleditor.preferences.ColorManager;
import smalleditor.preferences.IPreferenceNames;
import smalleditor.utils.IConstants;

/**
 * @author Max Stepanov
 */
public class CssSourceConfiguration extends ACommonSourceConfiguration {

	public final static String PREFIX = "__css_";
	public final static String CSS_DEFAULT = PREFIX + IDocument.DEFAULT_CONTENT_TYPE; //PREFIX + "default";
	public final static String CSS_COMMENT = PREFIX + "comment";
	public final static String CSS_KEYWORD = PREFIX + "keyword";
	public final static String CSS_CLASSNAME = PREFIX + "classname";
	public final static String CSS_STRING = PREFIX + "string";
	public final static String CSS_COLOR = PREFIX + "color";

	public final static String CONTENT_TYPE = IConstants.CONTENT_TYPE_CSS;

	private ColorManager colorManager = ColorManager.getDefault();
	private CommonScanner scanner;

	public static final String[] CONTENT_TYPES = new String[] { IDocument.DEFAULT_CONTENT_TYPE, CSS_DEFAULT,
			CSS_COMMENT, CSS_KEYWORD, CSS_CLASSNAME, CSS_STRING, CSS_COLOR };

	private static final String[][] TOP_CONTENT_TYPES = new String[][] { { CONTENT_TYPE } };
	private static String[] keywordTokens = {
		"-replace",
		"-set-link-source",
		"-use-link-source",
		"accelerator",
		"azimuth",
		"background",
		"background-attachment",
		"background-color",
		"background-image",
		"background-position",
		"background-position-x",
		"background-position-y",
		"background-repeat",
		"behavior",
		"binding",
		"border",
		"border-bottom",
		"border-bottom-color",
		"border-bottom-colors",
		"border-bottom-left-radius",
		"border-bottom-right-radius",
		"border-bottom-style",
		"border-bottom-width",
		"border-collapse",
		"border-color",
		"border-left",
		"border-left-color",
		"border-left-colors",
		"border-left-style",
		"border-left-width",
		"border-radius",
		"border-radius-bottomleft",
		"border-radius-bottomright",
		"border-radius-topleft",
		"border-radius-topright",
		"border-right",
		"border-right-color",
		"border-right-colors",
		"border-right-style",
		"border-right-width",
		"border-spacing",
		"border-style",
		"border-top",
		"border-top-color",
		"border-top-colors",
		"border-top-left-radius",
		"border-top-right-radius",
		"border-top-style",
		"border-top-width",
		"border-width",
		"bottom",
		"caption-side",
		"clear",
		"clip",
		"color",
		"content",
		"counter-increment",
		"counter-reset",
		"cue",
		"cue-after",
		"cue-before",
		"cursor",
		"direction",
		"display",
		"elevation",
		"empty-cells",
		"fill",
		"filter",
		"float",
		"font",
		"font-family",
		"font-size",
		"font-size-adjust",
		"font-stretch",
		"font-style",
		"font-variant",
		"font-weight",
		"height",
		"ime-mode",
		"include-source",
		"layer-background-color",
		"layer-background-image",
		"layout-flow",
		"layout-grid",
		"layout-grid-char",
		"layout-grid-char-spacing",
		"layout-grid-line",
		"layout-grid-mode",
		"layout-grid-type",
		"left",
		"letter-spacing",
		"line-break",
		"line-height",
		"list-style",
		"list-style-image",
		"list-style-position",
		"list-style-type",
		"margin",
		"margin-bottom",
		"margin-left",
		"margin-right",
		"margin-top",
		"marker-offset",
		"marks",
		"max-height",
		"max-width",
		"min-height",
		"min-width",
		"opacity",
		"orphans",
		"outline",
		"outline-color",
		"outline-style",
		"outline-width",
		"overflow",
		"overflow-X",
		"overflow-Y",
		"padding",
		"padding-bottom",
		"padding-left",
		"padding-right",
		"padding-top",
		"page",
		"page-break-after",
		"page-break-before",
		"page-break-inside",
		"pause",
		"pause-after",
		"pause-before",
		"pitch",
		"pitch-range",
		"play-during",
		"position",
		"quotes",
		"richness",
		"right",
		"ruby-align",
		"ruby-overhang",
		"ruby-position",
		"scrollbar-3d-light-color",
		"scrollbar-arrow-color",
		"scrollbar-base-color",
		"scrollbar-dark-shadow-color",
		"scrollbar-face-color",
		"scrollbar-highlight-color",
		"scrollbar-shadow-color",
		"scrollbar-track-color",
		"size",
		"speak",
		"speak-header",
		"speak-numeral",
		"speak-punctuation",
		"speech-rate",
		"stress",
		"stroke",
		"table-layout",
		"text-align",
		"text-align-last",
		"text-autospace",
		"text-decoration",
		"text-indent",
		"text-justify",
		"text-kashida-space",
		"text-overflow",
		"text-shadow",
		"text-transform",
		"text-underline-position",
		"top",
		"touch-callout",
		"unicode-bidi",
		"user-focus",
		"user-input",
		"user-modify",
		"user-select",
		"vertical-align",
		"visibility",
		"voice-family",
		"volume",
		"white-space",
		"widows",
		"width",
		"word-break",
		"word-spacing",
		"word-wrap",
		"writing-mode",
		"z-index",
		"zoom"
	};
	private IPredicateRule[] partitioningRules = null;

	private static CssSourceConfiguration instance;

	// static
	// {
	// if (CommonEditorPlugin.getDefault() != null)
	// {
	// IContentTypeTranslator c =
	// CommonEditorPlugin.getDefault().getContentTypeTranslator();
	// c.addTranslation(new
	// QualifiedContentType(ICSSConstants.CONTENT_TYPE_CSS), new
	// QualifiedContentType(
	// ICSSConstants.CSS_SCOPE));
	// c.addTranslation(new QualifiedContentType(MULTILINE_COMMENT), new
	// QualifiedContentType(
	// ICSSConstants.CSS_COMMENT_BLOCK_SCOPE));
	// c.addTranslation(new QualifiedContentType(STRING_DOUBLE), new
	// QualifiedContentType(
	// ICSSConstants.CSS_STRING_SCOPE));
	// c.addTranslation(new QualifiedContentType(STRING_SINGLE), new
	// QualifiedContentType(
	// ICSSConstants.CSS_STRING_SCOPE));
	// }
	// }

	public static CssSourceConfiguration getDefault() {
		if (instance == null) {
			instance = new CssSourceConfiguration();
		}
		return instance;
	}

	private CssSourceConfiguration() {
		/**
		 * Array of keyword token strings.
		 * see : http://www.blooberry.com/indexdot/css/propindex/all.htm
		 */
		
		
		List<IPredicateRule> rules = new ArrayList<IPredicateRule>();

		rules.add(new MultiLineRule("/*", "*/", getToken(CSS_COMMENT)));
		rules.add(new EndOfLineRule("//", getToken(CSS_COMMENT)));
		
		rules.add(new SingleLineRule("\"", "\"", getToken(CSS_STRING)));
		rules.add(new SingleLineRule("\'", "\'", getToken(CSS_STRING)));
		
//		rules.add(new SingleLineRule("@","import", getToken(CSS_KEYWORD)));
//		rules.add(new SingleLineRule("@","media", getToken(CSS_KEYWORD)));
//		rules.add(new SingleLineRule("@", ";", getToken(CSS_KEYWORD)));
		
		WordRule atWordRule = new AExtendedWordRule(
				new CssAtWordDetector(),
				getToken(CSS_KEYWORD), true) {
			@Override
			protected IToken getWordToken(String word) {
				return null;
			}
		};
		rules.add((IPredicateRule) atWordRule);
		

		rules.add(new SingleLineRule("(", ")", getToken(CSS_STRING)));
		rules.add(new SingleLineRule("!" ,"important", getToken(CSS_KEYWORD)));
		
		WordRule hexaWordRule = new AExtendedWordRule(
				new CommonHexaColorDetector(),
				getToken(CSS_COLOR), true) {
			@Override
			protected IToken getWordToken(String word) {
				return null;
			}
		};
		rules.add((IPredicateRule) hexaWordRule);
		
		
//		rules.add(new WordPatternRule(new CommonHexaColorDetector(), "#", "0", getToken(CSS_COLOR)));
//		rules.add(new WordPatternRule(new CommonHexaColorDetector(), "#", "1", getToken(CSS_COLOR)));
//		rules.add(new WordPatternRule(new CommonHexaColorDetector(), "#", "2", getToken(CSS_COLOR)));
//		rules.add(new WordPatternRule(new CommonHexaColorDetector(), "#", "3", getToken(CSS_COLOR)));
//		rules.add(new WordPatternRule(new CommonHexaColorDetector(), "#", "4", getToken(CSS_COLOR)));
//		rules.add(new WordPatternRule(new CommonHexaColorDetector(), "#", "5", getToken(CSS_COLOR)));
//		rules.add(new WordPatternRule(new CommonHexaColorDetector(), "#", "6", getToken(CSS_COLOR)));
//		rules.add(new WordPatternRule(new CommonHexaColorDetector(), "#", "7", getToken(CSS_COLOR)));
//		rules.add(new WordPatternRule(new CommonHexaColorDetector(), "#", "8", getToken(CSS_COLOR)));
//		rules.add(new WordPatternRule(new CommonHexaColorDetector(), "#", "9", getToken(CSS_COLOR)));
//		rules.add(new WordPatternRule(new CommonHexaColorDetector(), "#", "a", getToken(CSS_COLOR)));
//		rules.add(new WordPatternRule(new CommonHexaColorDetector(), "#", "b", getToken(CSS_COLOR)));
//		rules.add(new WordPatternRule(new CommonHexaColorDetector(), "#", "c", getToken(CSS_COLOR)));
//		rules.add(new WordPatternRule(new CommonHexaColorDetector(), "#", "d", getToken(CSS_COLOR)));
//		rules.add(new WordPatternRule(new CommonHexaColorDetector(), "#", "e", getToken(CSS_COLOR)));
//		rules.add(new WordPatternRule(new CommonHexaColorDetector(), "#", "f", getToken(CSS_COLOR)));
//		rules.add(new WordPatternRule(new CommonHexaColorDetector(), "#", "A", getToken(CSS_COLOR)));
//		rules.add(new WordPatternRule(new CommonHexaColorDetector(), "#", "B", getToken(CSS_COLOR)));
//		rules.add(new WordPatternRule(new CommonHexaColorDetector(), "#", "C", getToken(CSS_COLOR)));
//		rules.add(new WordPatternRule(new CommonHexaColorDetector(), "#", "D", getToken(CSS_COLOR)));
//		rules.add(new WordPatternRule(new CommonHexaColorDetector(), "#", "E", getToken(CSS_COLOR)));
//		rules.add(new WordPatternRule(new CommonHexaColorDetector(), "#", "F", getToken(CSS_COLOR)));
		
		List<String> allKeywords = new ArrayList<String>();
		for (String keywordToken : keywordTokens) {
			allKeywords.add(keywordToken);
			allKeywords.add("-webkit-" + keywordToken);
			allKeywords.add("-moz-" + keywordToken);
			allKeywords.add("-o-" + keywordToken);
			allKeywords.add("-ms-" + keywordToken);
			allKeywords.add("-khtml-" + keywordToken);
		}
		String[] simpleArray = new String[ allKeywords.size() ];
		allKeywords.toArray( simpleArray );
		
		CommonPredicateWordRule keywordRule = new CommonPredicateWordRule(
				new CssWordDetector(), getToken(CSS_DEFAULT), simpleArray, getToken(CSS_KEYWORD));
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
				getToken(CSS_DEFAULT));
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
		
		reconciler.setDamager(dr, CSS_DEFAULT);
		reconciler.setRepairer(dr, CSS_DEFAULT);

		CommonNonRuleBasedDamagerRepairer commentRepairer = new CommonNonRuleBasedDamagerRepairer(
				new TextAttribute(
						colorManager
								.getColorFromPreferencesKey(IPreferenceNames.P_COMMENT_COLOR)));
		reconciler.setDamager(commentRepairer, CSS_COMMENT);
		reconciler
				.setRepairer(commentRepairer, CSS_COMMENT);

		CommonNonRuleBasedDamagerRepairer stringRepairer = new CommonNonRuleBasedDamagerRepairer(
				new TextAttribute(
						colorManager
								.getColorFromPreferencesKey(IPreferenceNames.P_STRING_COLOR)));
		reconciler.setDamager(stringRepairer, CSS_STRING);
		reconciler.setRepairer(stringRepairer, CSS_STRING);

		CommonNonRuleBasedDamagerRepairer keywordRepairer = new CommonNonRuleBasedDamagerRepairer(
				new TextAttribute(
						colorManager
								.getColorFromPreferencesKey(IPreferenceNames.P_KEYWORD_COLOR),
						null, SWT.BOLD));
		reconciler.setDamager(keywordRepairer, CSS_KEYWORD);
		reconciler
				.setRepairer(keywordRepairer, CSS_KEYWORD);

		CssColorDamagerRepairer colorRepairer = new CssColorDamagerRepairer();
		reconciler.setDamager(colorRepairer, CSS_COLOR);
		reconciler.setRepairer(colorRepairer, CSS_COLOR);

		CommonNonRuleBasedDamagerRepairer classRepairer = new CommonNonRuleBasedDamagerRepairer(
				new TextAttribute(
						colorManager
								.getColorFromPreferencesKey(IPreferenceNames.P_DEFAULT_COLOR),
						null, SWT.BOLD));
		reconciler.setDamager(classRepairer, CSS_CLASSNAME);
		reconciler
				.setRepairer(classRepairer, CSS_CLASSNAME);
	}

	public IContentAssistProcessor getContentAssistProcessor(
			ACommonEditor editor, String contentType) {
		return new CssContentAssistProcessor();
	}

//	private ITokenScanner getCommentScanner() {
//		return new CssCommentScanner(getToken(CSS_COMMENT));
//	}
//
//	private ITokenScanner getStringScanner() {
//		return new CommonStringScanner();
//	}

}
