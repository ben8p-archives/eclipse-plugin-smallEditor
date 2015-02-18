package smalleditor.editors.css.parsing;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.text.IDocument;

import smalleditor.editors.common.parsing.ATaskPositionsBuilder;
import beaver.Scanner.Exception;
import beaver.Symbol;

public class CssTaskPositionsBuilder extends ATaskPositionsBuilder {
	private Iterator<Symbol> iterator = null;
	public CssTaskPositionsBuilder(IDocument document) {
		super(document);
		setScanner(new CssFlexScanner());
	}
	
	@Override
	protected void setSource() {
		CssFlexScanner scanner = (CssFlexScanner) getScanner();
		scanner.setSource(getDocument().get());
		
		//parse all token to fill the array of comments;
		try {
			while(scanner.nextToken().getId() != CssTokenType.EOF.getIndex()) {}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		List<Symbol> comments = scanner.getComments();
		
		iterator = comments.iterator();
	}

	@Override
	protected Symbol getNextToken() {
		if(iterator.hasNext()) { 
			return iterator.next();
		}
		return null;
	}
}
