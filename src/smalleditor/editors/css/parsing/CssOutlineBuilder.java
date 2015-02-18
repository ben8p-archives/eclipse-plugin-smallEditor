package smalleditor.editors.css.parsing;

import java.io.IOException;

import org.eclipse.jface.text.IDocument;

import smalleditor.editors.common.outline.CommonOutlineClassNameElement;
import smalleditor.editors.common.parsing.AOutlineBuilder;
import beaver.Scanner.Exception;
import beaver.Symbol;

public class CssOutlineBuilder extends AOutlineBuilder {
	private String currentCssSelector = null;
	private Integer currentCssSelectorTokenEnd = 0;
	
	public CssOutlineBuilder(IDocument document) {
		super(document);
		setScanner(new CssFlexScanner());

	}
	
	public CssOutlineBuilder() {
		super();
		setScanner(new CssFlexScanner());
	}
	
	protected String getCurrentCssSelector() {
		return currentCssSelector;
	}
	protected void setCurrentCssSelector(String currentCssSelector) {
		this.currentCssSelector = currentCssSelector;
	}
	protected Integer getCurrentCssSelectorTokenEnd() {
		return currentCssSelectorTokenEnd;
	}
	protected void setCurrentCssSelectorTokenEnd(Integer currentCssSelectorTokenEnd) {
		this.currentCssSelectorTokenEnd = currentCssSelectorTokenEnd;
	}

	@Override
	protected CommonOutlineClassNameElement processToken(Symbol token) {
		if(isTokenType(token, CssTokenType.LCURLY, CssTokenType.COMMA) && getCurrentCssSelector() != null) {
			CommonOutlineClassNameElement element = new CommonOutlineClassNameElement(getCurrentCssSelector(), getStart(token), 1);
			setCurrentCssSelector(null);
			return element;
		}
		return null;
	}
	
	
	@Override
	protected void setSource() {
		setCurrentCssSelector(null);
		((CssFlexScanner) getScanner()).setSource(getDocument().get());
	}
	@Override
	protected Boolean isOutlinableToken(Symbol token) {
		String selectorValue = null;
		if(getCurrentCssSelector() == null && isTokenType(token, CssTokenType.ID, CssTokenType.ELEMENT, CssTokenType.STAR, CssTokenType.CLASS)) {
			//initialize the selector
			selectorValue = (String) token.value;
			setCurrentCssSelectorTokenEnd(getEnd(token));
		} else if (getCurrentCssSelector() != null && !isTokenType(token, CssTokenType.LCURLY, CssTokenType.COMMA)) {
			//continue the selector
			selectorValue = getCurrentCssSelector();
			if(getCurrentCssSelectorTokenEnd() + 1 != getStart(token)) {
				selectorValue += " ";
			}
			selectorValue += (String) token.value;
			setCurrentCssSelectorTokenEnd(getEnd(token));
		}
		if(selectorValue != null) {
			setCurrentCssSelector(selectorValue);
		}
		return isTokenType(token, CssTokenType.LCURLY, CssTokenType.COMMA);
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
