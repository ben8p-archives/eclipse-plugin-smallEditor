package smalleditor.editors.javascript.parsing;

import java.io.IOException;

import org.eclipse.jface.text.IDocument;

import smalleditor.editors.common.parsing.AFoldingPositionsBuilder;
import smalleditor.tokenizer.DocumentNodeType;
import beaver.Scanner.Exception;
import beaver.Symbol;

public class JavascriptFoldingPositionsBuilder extends AFoldingPositionsBuilder {
	private DocumentNodeType lastAdded = null;
	public JavascriptFoldingPositionsBuilder(IDocument document) {
		super(document);
		setFoldOnOneLine(true);
		setScanner(new JavascriptFlexScanner());
	}
	
	@Override
	protected String setSource() {
		lastAdded = null;
		String source = getDocument().get();
		((JavascriptFlexScanner) getScanner()).setSource(source);
		return source;
	}
	@Override
	protected Boolean isOpenToken(Symbol token) {
		Boolean isTypeOk = isTokenType(token, JavascriptTokenType.LBRACKET, JavascriptTokenType.LCURLY, JavascriptTokenType.FUNCTION);
		
		if(isTypeOk == true && lastAdded != null && lastAdded == DocumentNodeType.OpenFunction && getNodeType(token) == DocumentNodeType.OpenObject) {
			//this is the { of the function, so we ignore it
			lastAdded = null;
			isTypeOk = false;
		}
		
		if(isTypeOk == true) {
			lastAdded = getNodeType(token);
		}
		
		return isTypeOk;
	}
	@Override
	protected Boolean isCloseToken(Symbol token) {
		Boolean isTypeOk = isTokenType(token, JavascriptTokenType.RBRACKET, JavascriptTokenType.RCURLY);
		if(isTypeOk == true) {
			lastAdded = null;
		}
		return isTypeOk;
	}
	
	@Override
	protected DocumentNodeType getNodeType(Symbol token) {
		if(token.getId() == JavascriptTokenType.FUNCTION.getIndex()) {
			return DocumentNodeType.OpenFunction;
		}
		if(token.getId() == JavascriptTokenType.LBRACKET.getIndex()) {
			return DocumentNodeType.OpenArray;
		}
		if(token.getId() == JavascriptTokenType.LCURLY.getIndex()) {
			return DocumentNodeType.OpenObject;
		}
		if(token.getId() == JavascriptTokenType.RBRACKET.getIndex()) {
			return DocumentNodeType.CloseArray;
		}
		if(token.getId() == JavascriptTokenType.RCURLY.getIndex()) {
			return DocumentNodeType.CloseObject;
		}
		return null;
	}

	@Override
	protected Symbol getNextToken() {
		Symbol token = null;
		try {
			token = getScanner().nextToken();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(token == null || token.getId() == JavascriptTokenType.EOF.getIndex()) {
			return null;
		}
		return token;
	}
	@Override
	protected boolean isTokenType(Symbol token, Object ... types) {
		for (Object type : types) {
			JavascriptTokenType javascriptType = (JavascriptTokenType) type;
			if (token.getId() == javascriptType.getIndex()) {
				return true;
			}
		}

		return false;
	}
}
