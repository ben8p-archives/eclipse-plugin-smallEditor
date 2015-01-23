/*
 * JavascriptContentOutlinePage.java	Created on 8 Jan 2015
 * 
 * Copyright © 2015 ING Group. All rights reserved.
 * 
 * This software is the confidential and proprietary information of 
 * ING Group ("Confidential Information"). 
 */
package smalleditor.editors.common;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.PatternFilter;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

import smalleditor.common.tokenizer.DocumentNode;
import smalleditor.common.tokenizer.DocumentTokenBuilder;

public class CommonOutlinePage extends ContentOutlinePage {
	protected IDocument document;
	protected DocumentTokenBuilder scanner = null;
	protected Boolean sort = true;
	
	private List<DocumentNode> previousNodes = null;
	private List elementList = null;
	
	protected void setSort(Boolean s) {
		sort = s;
	}
	
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

		// apply tree filters
		PatternFilter filter = new PatternFilter() {};
		filter.setIncludeLeadingWildcard(true);
		
		TreeViewer viewer = getTreeViewer();
		viewer.setUseHashlookup(true);
		viewer.setContentProvider(new WorkbenchContentProvider());
		viewer.setLabelProvider(new WorkbenchLabelProvider());
		//viewer.setInput(ResourcesPlugin.getWorkspace().getRoot());
		if(this.sort) {
			viewer.setSorter(new CommonOutlineNameSorter());
		}
		viewer.addFilter(filter);
		
	}
	
	/**
	 * Gets the content outline for a given input element. Returns the outline
	 * or null if the outline could not be generated.
	 * 
	 * @param input
	 * 
	 * @return
	 */
	private IAdaptable getContentOutline(List<DocumentNode> nodes) {
		if(scanner == null) {
			return null;
		}
		
		return new CommonOutlineElementList(getSyntacticElements(nodes));
	}
	private IAdaptable getContentOutline() {
		if(scanner == null) {
			return null;
		}
		List<DocumentNode> nodes = scanner.buildNodes(document);
		return getContentOutline(nodes);
	}
	protected List getSyntacticElements(List<DocumentNode> nodes) {
		
		if(previousNodes != null && previousNodes == nodes) {
			return elementList;
		}
		previousNodes = nodes;
		
		elementList = new LinkedList();
		
		DocumentNode previousItem = null;
		
		Iterator it = nodes.iterator();
		while (it.hasNext()) {
			DocumentNode item = (DocumentNode) it.next();
			
			int offset = item.getStart();
			int length = item.getLength();
			String expression = getExpression(offset, length);
			
			Object object = processToken(item, previousItem, expression, offset, length);
			if(object != null) {
				elementList.add(object);
			}
			
			//System.out.println(item);
			previousItem = item;
		}
		

		return elementList;
	}


	protected Object processToken(DocumentNode node, DocumentNode previousNode, String expression, int offset, int length) {
		return null;
	}
	
	protected DocumentTokenBuilder getScanner() {
		return null;
	}
	
	/**
	 * Forces the outlinePage to update its contents.
	 * 
	 */
	public void update(List<DocumentNode> nodes) {
		CommonOutlineElementList currentNodes = (CommonOutlineElementList) getContentOutline(nodes);
		update(currentNodes);
	}
	public void update() {
		CommonOutlineElementList currentNodes = (CommonOutlineElementList) getContentOutline();
		update(currentNodes);
	}
	
	public void update(CommonOutlineElementList currentNodes) {
		if(getControl() == null) {
			return; 
		}
		getControl().setRedraw(false);
		TreeViewer viewer = getTreeViewer();

		Object[] expanded = viewer.getExpandedElements();
		
		viewer.setInput(currentNodes);

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

		if(getControl() != null) {
			getControl().setRedraw(true);
		}
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
