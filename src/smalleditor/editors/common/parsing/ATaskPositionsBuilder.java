package smalleditor.editors.common.parsing;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.text.IDocument;

import smalleditor.tokenizer.NodePosition;
import beaver.Scanner;
import beaver.Symbol;

public abstract class ATaskPositionsBuilder {

	private Scanner scanner = null;
	private IDocument document;

	public ATaskPositionsBuilder(IDocument document) {
		this.document = document;
	}
	
	public void setDocument(IDocument document) {
		this.document = document;
	}
	public IDocument getDocument() {
		return document;
	}
	public void setScanner(Scanner scanner) {
		this.scanner = scanner;
	}
	public Scanner getScanner() {
		return scanner;
	}

	public List<NodePosition> buildPositions() {
		setSource();
		
		List<NodePosition> positions = new LinkedList<NodePosition>();
		Symbol token = null;
		while((token = getNextToken()) != null) {
//			System.out.println(token);
			if (isTask(token)) {
				NodePosition position = new NodePosition(getStart(token));
				position.setLength(getEnd(token) - position.getOffset());
				positions.add(position);
			}
		}
		
		return positions;
	}
	
	protected abstract Boolean isTask(Symbol token);
	protected abstract void setSource();
	protected abstract Symbol getNextToken();
	
	private int getStart(Symbol token) {
		return token.getStart();
	}

	private int getEnd(Symbol token) {
		if (token == null) {
			return 0;
		}
		return token.getEnd();
	}
}
