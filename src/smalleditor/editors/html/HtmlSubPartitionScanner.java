/**
 * Aptana Studio
 * Copyright (c) 2005-2011 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the GNU Public License (GPL) v3 (with exceptions).
 * Please see the license.html included with this distribution for details.
 * Any modifications to this file must keep this entire header intact.
 */

package smalleditor.editors.html;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

import smalleditor.common.ACompositeSubPartitionScanner;
import smalleditor.common.IPartitionScannerSwitchStrategy;
import smalleditor.common.PartitionScannerSwitchStrategy;
import smalleditor.common.rules.ExtendedToken;
import smalleditor.common.rules.ISubPartitionScanner;
import smalleditor.common.rules.SubPartitionScanner;
import smalleditor.editors.css.CssSourceConfiguration;
import smalleditor.editors.javascript.JavascriptSourceConfiguration;
import smalleditor.utils.HtmlUtililty;

/**
 * @author Max Stepanov
 */
public class HtmlSubPartitionScanner extends ACompositeSubPartitionScanner {

	private static final int TYPE_JS = 1;
	private static final int TYPE_CSS = 2;
	// private static final int TYPE_SVG = 3;

	private static final String[] JS_SWITCH_SEQUENCES = new String[] { "</script>" }; //$NON-NLS-1$
	private static final String[] CSS_SWITCH_SEQUENCES = new String[] { "</style>" }; //$NON-NLS-1$

	//	private static final String[] SVG_SWITCH_SEQUENCES = new String[] { "</svg>" }; //$NON-NLS-1$

	/**
	 * HTMLSubPartitionScanner
	 */
	public HtmlSubPartitionScanner() {
		super(
				new ISubPartitionScanner[] {
						new SubPartitionScanner(HtmlSourceConfiguration
								.getDefault().getPartitioningRules(),
								HtmlSourceConfiguration.CONTENT_TYPES,
								new Token(HtmlSourceConfiguration.HTML_DEFAULT)),
						JavascriptSourceConfiguration.getDefault()
								.createSubPartitionScanner(),
						CssSourceConfiguration.getDefault()
								.createSubPartitionScanner() // ,
				// SVGSourceConfiguration.getDefault().createSubPartitionScanner()
				},
				new IPartitionScannerSwitchStrategy[] {
						new PartitionScannerSwitchStrategy(JS_SWITCH_SEQUENCES),
						new PartitionScannerSwitchStrategy(CSS_SWITCH_SEQUENCES) // ,
				// new PartitionScannerSwitchStrategy(SVG_SWITCH_SEQUENCES)
				});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.aptana.editor.common.CompositeSubPartitionScanner#setLastToken(org
	 * .eclipse.jface.text.rules.IToken)
	 */
	@Override
	public void setLastToken(IToken token) {
		super.setLastToken(token);
		if (token == null) {
			return;
		}

		Object data = token.getData();
		if (!(data instanceof String)) {
			current = TYPE_DEFAULT;
			return;
		}

		String contentType = (String) data;

		if (HtmlSourceConfiguration.HTML_SCRIPT.equals(contentType)
		/* || SVGSourceConfiguration.SCRIPT.equals(contentType) */) {
			if (!(token instanceof ExtendedToken && ((HtmlUtililty
					.isTagSelfClosing(((ExtendedToken) token).getContents())) || !HtmlUtililty
					.isJavaScriptTag(((ExtendedToken) token).getContents())))) {
				current = TYPE_JS;
				super.setLastToken(null);
			}
		} else if (HtmlSourceConfiguration.HTML_STYLE.equals(contentType)
		/* || SVGSourceConfiguration.STYLE.equals(contentType) */) {
			if (!(token instanceof ExtendedToken && (HtmlUtililty
					.isTagSelfClosing(((ExtendedToken) token).getContents()) || !HtmlUtililty
					.isTagComplete(((ExtendedToken) token).getContents())))) {
				current = TYPE_CSS;
				super.setLastToken(null);
			}
		}
		// else if (HTMLSourceConfiguration.HTML_SVG.equals(contentType))
		// {
		// if (!(token instanceof ExtendedToken &&
		// HTMLUtils.isTagSelfClosing(((ExtendedToken) token).getContents()) ||
		// !HTMLUtils
		// .isTagComplete(((ExtendedToken) token).getContents())))
		// {
		// current = TYPE_SVG;
		// super.setLastToken(null);
		// }
		// }
		else if (HtmlSourceConfiguration.HTML_DEFAULT.equals(contentType)
				|| IDocument.DEFAULT_CONTENT_TYPE.equals(contentType)) {
			current = TYPE_DEFAULT;
		} else {
			for (int i = 0; i < subPartitionScanners.length; ++i) {
				if (subPartitionScanners[i].hasContentType(contentType)) {
					current = i;
					break;
				}
			}
		}
	}
}
