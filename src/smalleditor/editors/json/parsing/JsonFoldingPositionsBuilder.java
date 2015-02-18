package smalleditor.editors.json.parsing;

import java.io.IOException;

import org.eclipse.jface.text.IDocument;

import smalleditor.editors.common.parsing.AFoldingPositionsBuilder;
import smalleditor.tokenizer.DocumentNodeType;
import beaver.Scanner.Exception;
import beaver.Symbol;

public class JsonFoldingPositionsBuilder extends AFoldingPositionsBuilder {
	public JsonFoldingPositionsBuilder(IDocument document) {
		super(document);
		setFoldOnOneLine(true);
		setScanner(new JsonFlexScanner());
	}
	
	@Override
	protected String setSource() {
		String source = getDocument().get();
		((JsonFlexScanner) getScanner()).setSource(source);
		return source;
	}
	@Override
	protected Boolean isOpenToken(Symbol token) {
		return isTokenType(token, JsonTokenType.LBRACKET, JsonTokenType.LCURLY);
	}
	@Override
	protected Boolean isCloseToken(Symbol token) {
		return isTokenType(token, JsonTokenType.RBRACKET, JsonTokenType.RCURLY);
	}
	
	@Override
	protected DocumentNodeType getNodeType(Symbol token) {
		if(token.getId() == JsonTokenType.LBRACKET.getIndex()) {
			return DocumentNodeType.OpenArray;
		}
		if(token.getId() == JsonTokenType.LCURLY.getIndex()) {
			return DocumentNodeType.OpenObject;
		}
		if(token.getId() == JsonTokenType.RBRACKET.getIndex()) {
			return DocumentNodeType.CloseArray;
		}
		if(token.getId() == JsonTokenType.RCURLY.getIndex()) {
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
		if(token == null || token.getId() == JsonTokenType.EOF.getIndex()) {
			return null;
		}
		return token;
	}
	@Override
	protected boolean isTokenType(Symbol token, Object ... types) {
		for (Object type : types) {
			JsonTokenType javascriptType = (JsonTokenType) type;
			if (token.getId() == javascriptType.getIndex()) {
				return true;
			}
		}

		return false;
	}
}
