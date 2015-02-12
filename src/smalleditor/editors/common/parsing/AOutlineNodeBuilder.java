package smalleditor.editors.common.parsing;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;

import smalleditor.editors.common.outline.CommonOutlineElementList;
import smalleditor.tokenizer.DocumentNode;
import smalleditor.tokenizer.DocumentTokenBuilder;
import smalleditor.utils.TextUtility;

public abstract class AOutlineNodeBuilder {
	private DocumentTokenBuilder scanner = null;
	
	private List<DocumentNode> previousNodes = null;
	private List elementList = null;
	private Boolean doForceSyntacticElementsUpdate = false;
	
	public AOutlineNodeBuilder() {
		
	}
	public void setScanner(DocumentTokenBuilder scanner) {
		this.scanner = scanner;
	}
	public CommonOutlineElementList buildOutline(IDocument document) {
		List<DocumentNode> nodes = scanner.buildNodes(document);
		return getContentOutline(document, nodes);
	}
	
	/**
	 * Gets the content outline for a given input element. Returns the outline
	 * or null if the outline could not be generated.
	 * 
	 * @param input
	 * 
	 * @return
	 */
	private CommonOutlineElementList getContentOutline(IDocument document, List<DocumentNode> nodes) {
		return new CommonOutlineElementList(getSyntacticElements(document, nodes));
	}
	
	private Boolean forceSyntacticElementsUpdate(List<DocumentNode> nodes) {
		if(doForceSyntacticElementsUpdate == true) {
			return true;
		}
		if(previousNodes != null && previousNodes == nodes) {
			return false;
		}
		return true;
	}
	
	protected List getSyntacticElements(IDocument document, List<DocumentNode> nodes) {
		if(forceSyntacticElementsUpdate(nodes) == false) {
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
			String expression = getExpression(document, offset, length);
			
			Object object = processToken(document, item, previousItem, expression, offset, length);
			if(object != null) {
				elementList.add(object);
			}
			
			//System.out.println(item);
			previousItem = item;
		}
		

		return elementList;
	}


	protected abstract Object processToken(IDocument document, DocumentNode node, DocumentNode previousNode, String expression, int offset, int length);
	
	
	private String getExpression(IDocument document, int offset, int length) {
		String expression;
		try {
			expression = document.get(offset, length);// sourceBuffer.substring(offset,
															// offset + length);
		} catch (BadLocationException e) {
			e.printStackTrace();
//			System.out.println(document.get());
			expression = TextUtility.EMPTY_STRING; //$NON-NLS-1$
		}
		return expression;
	}
	
	
}