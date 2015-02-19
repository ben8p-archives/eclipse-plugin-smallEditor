package smalleditor.editors.json.parsing;

import java.io.IOException;
import java.util.HashMap;

import org.eclipse.jface.text.IDocument;

import smalleditor.editors.common.outline.CommonOutlineBaseElement;
import smalleditor.editors.common.parsing.AOutlineBuilder;
import beaver.Scanner.Exception;
import beaver.Symbol;

public class JsonOutlineBuilder extends AOutlineBuilder {
	private int deep = 0;
	private HashMap<Integer, CommonOutlineBaseElement> tree = null;
	private String currentString = null;
	private CommonOutlineBaseElement lastCreatedElement = null;
	private Symbol previousToken = null;
	private Symbol currentToken = null;
	
	public JsonOutlineBuilder(IDocument document) {
		super(document);
		setScanner(new JsonFlexScanner());
	}
	public JsonOutlineBuilder() {
		super();
		setScanner(new JsonFlexScanner());
	}
	@Override
	protected CommonOutlineBaseElement processToken(Symbol token) {
		String tokenValue = (String) token.value;
		CommonOutlineBaseElement parentElement = tree.get(deep);
		CommonOutlineBaseElement object = null;
		if(isTokenType(token, JsonTokenType.STRING)) {
			//wait the next token, if it is a colon we validate this string
			currentString = tokenValue;
			return null;
//			Symbol nextToken = getNextToken();
//			if(nextToken != null && isTokenType(nextToken, JsonTokenType.COLON)) {
//				currentString = tokenValue;
//			} else {
//				currentString = null;
//			}
		} else if(isTokenType(token, JsonTokenType.LCURLY, JsonTokenType.LBRACKET) && (deep == 0 || (parentElement != null && isTokenType(parentElement.getToken(), JsonTokenType.LBRACKET)))) {
			currentString = tokenValue;
		}
		
		if(currentString != null) {
			object = new CommonOutlineBaseElement(currentString, getStart(token), 1);
			object.setToken(token);
			lastCreatedElement = object;
			currentString = null;
			parentElement = tree.get(deep);
		}
		if(isTokenType(token, JsonTokenType.LCURLY, JsonTokenType.LBRACKET)) {
			deep++;
			if(lastCreatedElement != null) {
				//new object, therefore the last element is a parent
				tree.put(deep, lastCreatedElement);
				lastCreatedElement.setToken(token);
			}
		}
		if(object != null && parentElement == null) {
			//no parents, so I am the parent
			tree.put(deep, object);
		} else if(object != null) {
			//ok let's be a child of
			parentElement.addChildElement(object);
			object.setParent(parentElement);
			return null;
		}
		
		if(isTokenType(token, JsonTokenType.RCURLY, JsonTokenType.RBRACKET)) {
			tree.remove(deep);
			deep--;
		}
		return object;
		
	}
	
	
	@Override
	protected void setSource() {
		deep = 0;
		tree = new HashMap<Integer, CommonOutlineBaseElement>();
		currentString = null;
		lastCreatedElement = null;
		
		((JsonFlexScanner) getScanner()).setSource(getDocument().get());
	}
	@Override
	protected Boolean isOutlinableToken(Symbol token) {
		if(previousToken != null && isTokenType(previousToken, JsonTokenType.STRING) && !isTokenType(token, JsonTokenType.COLON)) {
			currentString = null;
		}
		return isTokenType(token, JsonTokenType.COLON, JsonTokenType.LBRACKET, JsonTokenType.RBRACKET, JsonTokenType.LCURLY, JsonTokenType.RCURLY, JsonTokenType.STRING);
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
			token = null;
		}
		
		if(currentToken != null) {
			previousToken = currentToken;
		}
		currentToken = token;
		
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
