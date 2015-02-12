package smalleditor.editors.javascript.parsing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.text.IDocument;

import smalleditor.editors.common.parsing.ATaskPositionsBuilder;
import beaver.Symbol;
import beaver.Scanner.Exception;

public class JavascriptTaskPositionsBuilder extends ATaskPositionsBuilder {
	private Iterator<Symbol> iterator = null;
	public JavascriptTaskPositionsBuilder(IDocument document) {
		super(document);
		setScanner(new JavascriptFlexScanner());
	}
	
	@Override
	protected void setSource() {
		JavascriptFlexScanner scanner = (JavascriptFlexScanner) getScanner();
		scanner.setSource(getDocument().get());
		
		//parse all token to fill the array of comments;
		try {
			while(scanner.nextToken().getId() != JavascriptTokenType.EOF.getIndex()) {}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		List<Symbol> multiLine = scanner.getMultiLineComments();
		List<Symbol> singleLine = scanner.getSingleLineComments();
		
		List<Symbol> comments = new ArrayList<Symbol>();
		comments.addAll(singleLine);
		comments.addAll(multiLine);
		
		iterator = comments.iterator();
	}
	@Override
	protected Boolean isTask(Symbol token) {
		String value = (String) token.value;
		value = value.toLowerCase();
		return (value.contains("todo") || value.contains("FIXME"));
	}


	@Override
	protected Symbol getNextToken() {
		if(iterator.hasNext()) { 
			return iterator.next();
		}
		return null;
	}
}
