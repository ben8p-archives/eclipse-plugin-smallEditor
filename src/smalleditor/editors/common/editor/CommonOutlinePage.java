/*
 * JavascriptContentOutlinePage.java	Created on 8 Jan 2015
 * 
 * Copyright © 2015 ING Group. All rights reserved.
 * 
 * This software is the confidential and proprietary information of 
 * ING Group ("Confidential Information"). 
 */
package smalleditor.editors.common.editor;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;
import org.eclipse.swt.widgets.Composite;

public class CommonOutlinePage extends ContentOutlinePage {
	protected IDocument document;
	protected RuleBasedScanner scanner = null;
	
	public CommonOutlinePage(IDocument document) {
		super();
		this.document = document;
		this.scanner = getScanner();
	}

	/**
	 * Creates the control and registers the popup menu for this outlinePage
	 * Menu id "org.eclipse.ui.examples.readmetool.outline"
	 * 
	 * @param parent
	 */
	public void createControl(Composite parent) {
		super.createControl(parent);

		TreeViewer viewer = getTreeViewer();
		viewer.setContentProvider(new WorkbenchContentProvider());
		viewer.setLabelProvider(new WorkbenchLabelProvider());

		viewer.setSorter(new CommonOutlineNameSorter());
	}
	
	/**
	 * Gets the content outline for a given input element. Returns the outline
	 * or null if the outline could not be generated.
	 * 
	 * @param input
	 * 
	 * @return
	 */
	private IAdaptable getContentOutline(IDocument document) {
		if(scanner == null) {
			return null;
		}
		return new CommonOutlineElementList(getSyntacticElements(document));
	}
	private List getSyntacticElements(IDocument document) {
		List elementList = new LinkedList();
		
		scanner.setRange(document, 0, document.getLength());
		IToken token = scanner.nextToken();
		while (!token.isEOF()) {
			int offset = scanner.getTokenOffset();
			int length = scanner.getTokenLength();
			String expression = getExpression(offset, length);
			
			
			Object object = processToken(token, expression, offset, length);
			if(object != null) {
				elementList.add(object);
			}
			
			token = scanner.nextToken();
		}
		return elementList;
	}

	/**
	 * Skips ahead and finds next non-whitespace token.
	 * 
	 */
	public IToken nextNonWhitespaceToken() {
		IToken aToken = scanner.nextToken();

		while (!aToken.isEOF() && aToken.isWhitespace()) {
			aToken = scanner.nextToken();
		}

		return aToken;
	}
	
	protected Object processToken(IToken token, String expression, int offset, int length) {
		return null;
	}
	
	protected RuleBasedScanner getScanner() {
		return null;
	}
	
	/**
	 * Forces the outlinePage to update its contents.
	 * 
	 */
	public void update() {
		getControl().setRedraw(false);
		TreeViewer viewer = getTreeViewer();

		Object[] expanded = viewer.getExpandedElements();
		CommonOutlineElementList currentNodes = (CommonOutlineElementList) getContentOutline(document);
		viewer.setInput(currentNodes);

		/*
		 * Is automatically expanding the tree helpful? Should this be a
		 * preference? Or should we only expand those nodes that are already
		 * expanded?
		 */
		// getTreeViewer().expandAll();

		// How about just expanding the root if it's alone?
		if (currentNodes.size() == 1) {
			viewer.expandAll();
		}

		// Attempt to determine which nodes are already expanded bearing in mind
		// that the object is not the same.
		for (int i = 0; i < expanded.length; i++) {
			CommonOutlineElement newExpandedNode = currentNodes
					.findEquivilent((CommonOutlineElement) expanded[i]);
			if (newExpandedNode != null) {
				viewer.setExpandedState(newExpandedNode, true);
			}
		}

		getControl().setRedraw(true);
	}

	protected String getExpression(int offset, int length) {
		String expression;
		try {
			expression = document.get(offset, length);// sourceBuffer.substring(offset,
															// offset + length);
		} catch (BadLocationException e) {
			expression = "";
		}
		return expression;
	}
	
	
}
