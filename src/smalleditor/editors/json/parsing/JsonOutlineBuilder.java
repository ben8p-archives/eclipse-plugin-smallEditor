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
		CommonOutlineBaseElement parentElement = null;
		CommonOutlineBaseElement object = null;
		if(isTokenType(token, JsonTokenType.STRING)) {
			Symbol nextToken = getNextToken();
			if(nextToken != null && isTokenType(nextToken, JsonTokenType.COLON)) {
				currentString = tokenValue;
			} else {
				currentString = null;
			}
		} else if(deep == 0 && isTokenType(token, JsonTokenType.LCURLY, JsonTokenType.LBRACKET)) {
			currentString = tokenValue;
		}
		if(isTokenType(token, JsonTokenType.LCURLY, JsonTokenType.LBRACKET)) {
			deep++;
			if(lastCreatedElement != null) {
				//new object, therefore the last element is a parent
				tree.put(deep, lastCreatedElement);
			}
		}
		if(currentString != null) {
			object = new CommonOutlineBaseElement(currentString, getStart(token), 1);
			lastCreatedElement = object;
			currentString = null;
			parentElement = tree.get(deep);
			if(parentElement == null) {
				//no parents, so I am the parent
				tree.put(deep, object);
			} else {
				//ok let's be a child of
				parentElement.addChildElement(object);
				object.setParent(parentElement);
				return null;
			}
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
