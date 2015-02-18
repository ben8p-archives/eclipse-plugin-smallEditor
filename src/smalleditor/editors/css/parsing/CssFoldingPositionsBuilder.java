package smalleditor.editors.css.parsing;

import java.io.IOException;

import org.eclipse.jface.text.IDocument;

import smalleditor.editors.common.parsing.AFoldingPositionsBuilder;
import smalleditor.tokenizer.DocumentNodeType;
import beaver.Scanner.Exception;
import beaver.Symbol;

public class CssFoldingPositionsBuilder extends AFoldingPositionsBuilder {
	public CssFoldingPositionsBuilder(IDocument document) {
		super(document);
		setFoldOnOneLine(true);
		setScanner(new CssFlexScanner());
	}
	
	@Override
	protected String setSource() {
		String source = getDocument().get();
		((CssFlexScanner) getScanner()).setSource(source);
		return source;
	}
	@Override
	protected Boolean isOpenToken(Symbol token) {
		return isTokenType(token, CssTokenType.LCURLY);
	}
	@Override
	protected Boolean isCloseToken(Symbol token) {
		return isTokenType(token, CssTokenType.RCURLY);
	}
	
	@Override
	protected DocumentNodeType getNodeType(Symbol token) {
		if(token.getId() == CssTokenType.LCURLY.getIndex()) {
			return DocumentNodeType.OpenObject;
		}
		if(token.getId() == CssTokenType.RCURLY.getIndex()) {
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
		if(token == null || token.getId() == CssTokenType.EOF.getIndex()) {
			return null;
		}
		return token;
	}
	@Override
	protected boolean isTokenType(Symbol token, Object ... types) {
		for (Object type : types) {
			CssTokenType javascriptType = (CssTokenType) type;
			if (token.getId() == javascriptType.getIndex()) {
				return true;
			}
		}

		return false;
	}
}
