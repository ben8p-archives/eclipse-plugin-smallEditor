package smalleditor.editors.common.parsing;

import java.util.LinkedList;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;

import smalleditor.editors.common.outline.ACommonOutlineElement;
import smalleditor.editors.common.outline.CommonOutlineElementList;
import beaver.Scanner;
import beaver.Symbol;

public abstract class AOutlineBuilder {

	private Scanner scanner = null;
	private IDocument document = null;

	public AOutlineBuilder(IDocument document) {
		setDocument(document);
	}
	public AOutlineBuilder() {
	}
	
	protected abstract Symbol getNextToken();
	protected abstract Boolean isOutlinableToken(Symbol token);
	protected abstract void setSource();
	protected abstract boolean isTokenType(Symbol token, Object ... types);
	protected abstract ACommonOutlineElement processToken(Symbol token);
	
	protected void setDocument(IDocument document) {
		this.document = document;
	}
	protected IDocument getDocument() {
		return document;
	}
	protected void setScanner(Scanner scanner) {
		this.scanner = scanner;
	}
	protected Scanner getScanner() {
		return scanner;
	}
	public CommonOutlineElementList buildOutline(IDocument document) {
		setDocument(document);
		return buildOutline();
	}
	public CommonOutlineElementList buildOutline() {
		setSource();
		LinkedList elementList = new LinkedList();
		Symbol token = null;
		while((token = getNextToken()) != null) {
//			System.out.println(token);
			if (isOutlinableToken(token)) {
				ACommonOutlineElement element = processToken(token);
				if(element != null) {
					elementList.add(element);
				}
			}
		}
		
		return new CommonOutlineElementList(elementList);
	}
	
	protected int getLine(Symbol token) {
		try {
			return document.getLineOfOffset(getStart(token));
		} catch (BadLocationException e) {
			System.err.println(token);
			e.printStackTrace();
		}
		return 0;
	}
	protected int getStart(Symbol token) {
		return token.getStart();
	}
	protected int getEnd(Symbol token) {
		return token.getEnd();
	}
}
