package smalleditor.editors.less.parsing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import smalleditor.editors.common.outline.CommonOutlineClassNameElement;
import smalleditor.editors.css.parsing.CssOutlineBuilder;
import smalleditor.editors.css.parsing.CssTokenType;
import smalleditor.utils.CharUtility;
import beaver.Symbol;

public class LessOutlineBuilder extends CssOutlineBuilder {
	private int deep = 0;
	private HashMap tree = null;
	private Boolean canGoDeeper = true;
	private String originalCssSelector = null;
	
	@Override
	protected void setSource() {
		deep = 0;
		canGoDeeper = true;
		tree = new HashMap();
		
		super.setSource();
	}
	@Override
	protected void setCurrentCssSelector(String currentCssSelector) {
		if(currentCssSelector == null) {
			originalCssSelector = null;
		}
		if(getCurrentCssSelector() == null && currentCssSelector != null) {
			originalCssSelector = currentCssSelector;
		}
		super.setCurrentCssSelector(currentCssSelector);
	}
	
	@Override
	protected Boolean isOutlinableToken(Symbol token) {
		Boolean value = super.isOutlinableToken(token);
		
		String tokenValue = (String) token.value;
		if(value == false) {
			if(getCurrentCssSelector() == null && tokenValue.equals(Character.toString(CharUtility.ampersand))) {
				//initialize the selector
				setCurrentCssSelector(tokenValue);
				setCurrentCssSelectorTokenEnd(getEnd(token));
			}
			if(getCurrentCssSelector() != null && originalCssSelector != null && originalCssSelector.equals(getCurrentCssSelector().replaceAll(Pattern.quote(tokenValue), "").trim()) && isTokenType(token, CssTokenType.LPAREN)) {
				//this is a less mixin we don't want it
				setCurrentCssSelector(null);
				setCurrentCssSelectorTokenEnd(0);
			}
			
			value = isTokenType(token, CssTokenType.RCURLY);
		}
		return value;
	}
	
	@Override
	protected CommonOutlineClassNameElement processToken(Symbol token) {
		List<CommonOutlineClassNameElement> elements = null;
		if(isTokenType(token, CssTokenType.COMMA) && canGoDeeper == true) {
			deep++;
			canGoDeeper = false;
		}
		if(isTokenType(token, CssTokenType.LCURLY)) {
			if(canGoDeeper == true) {
				deep++;
			}
			canGoDeeper = true;
		}
		CommonOutlineClassNameElement object = super.processToken(token);
		if(object != null) {
			elements = (List) tree.get(deep);
			if(elements == null) {
				elements = new ArrayList<CommonOutlineClassNameElement>();
				tree.put(deep, elements);
			}
			elements.add(object);
		}
		
		if(isTokenType(token, CssTokenType.RCURLY)) {
			List<CommonOutlineClassNameElement> parents = (List) tree.get(deep - 1);
			elements = (List) tree.get(deep);
			
			if(parents != null && elements != null) {
				for(CommonOutlineClassNameElement element: elements) {
					for(CommonOutlineClassNameElement parent: parents) {
						parent.addChildElement(element);
						element.setParent(parent);
					}
				}
			}
			tree.remove(deep);
			deep--;
		}
		if(deep > 1) {
			return null;
		}
		
		return object;
	}

}
