/**
 * Aptana Studio
 * Copyright (c) 2005-2013 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the GNU Public License (GPL) v3 (with exceptions).
 * Please see the license.html included with this distribution for details.
 * Any modifications to this file must keep this entire header intact.
 */

package smalleditor.editors.common;

import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.source.ISourceViewer;

import smalleditor.common.IPartitioningConfiguration;
import smalleditor.common.ISourceViewerConfiguration;
import smalleditor.common.rules.ISubPartitionScanner;
import smalleditor.utils.CommonUtility;

/**
 * @author Max Stepanov
 */
public class CommonSourceConfiguration implements IPartitioningConfiguration,
		ISourceViewerConfiguration {

	public static CommonSourceConfiguration getDefault() {
		return null;
	}
	protected CommonSourceConfiguration() {
	}
	
	public String[] getContentTypes() {
		return null;
	}
	public String[][] getTopContentTypes() {
		return null;
	}
	public IPredicateRule[] getPartitioningRules() {
		return null;
	}
	public ISubPartitionScanner createSubPartitionScanner() {
		return null;
	}
	public String getDocumentContentType(String contentType) {
		return null;
	}
	protected CommonScanner getScanner() {
		return null;
	}
	public void setupPresentationReconciler(PresentationReconciler reconciler,
			ISourceViewer sourceViewer) {
	}
	public IContentAssistProcessor getContentAssistProcessor(
			CommonEditor editor, String contentType) {
		return null;
	}
	protected static IToken getToken(String tokenName) {
		return CommonUtility.getToken(tokenName);
	}
}
