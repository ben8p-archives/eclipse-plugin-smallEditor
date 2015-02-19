package smalleditor.editors.javascript.parsing;

import java.io.IOException;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;

import smalleditor.Activator;
import smalleditor.editors.common.outline.CommonOutlineFunctionElement;
import smalleditor.editors.common.parsing.AOutlineBuilder;
import smalleditor.preferences.IPreferenceNames;
import smalleditor.utils.CharUtility;
import smalleditor.utils.TextUtility;
import beaver.Scanner.Exception;
import beaver.Symbol;

public class JavascriptOutlineBuilder extends AOutlineBuilder {
	public JavascriptOutlineBuilder(IDocument document) {
		super(document);
		setScanner(new JavascriptFlexScanner());

	}
	
	public JavascriptOutlineBuilder() {
		super();
		setScanner(new JavascriptFlexScanner());
	}

	@Override
	protected CommonOutlineFunctionElement processToken(Symbol token) {
		Symbol nextToken = getNextToken();
		int line = getLine(token);
		String functionName = TextUtility.EMPTY_STRING;
		if(nextToken.getId() == JavascriptTokenType.IDENTIFIER.getIndex()) {
			functionName = (String) nextToken.value;
		}
		String functionSignature = getSignature(nextToken);
		if(functionSignature.length() > 0) {
			functionSignature = " (" + functionSignature + ")";
		}
		
		if(functionName.equals(TextUtility.EMPTY_STRING)) {
			//try to guess the name from the line
			try {
				int lineOffset = getDocument().getLineOffset(line);
				String lineStr = getDocument().get(lineOffset, getStart(token) - lineOffset);
				String[] lineElements = lineStr.replaceAll("(\\w+)", " $1 ").replaceAll("\\s+", " ").split(" ");
				int cursor = lineElements.length;
				Boolean pickupNext = false;
				while(--cursor >= 0) {
	//				System.out.println(lineElements[cursor]);
					if(!lineElements[cursor].trim().equals(TextUtility.EMPTY_STRING)) {
						
						if(lineElements[cursor].equals(Character.toString(CharUtility.colon)) || lineElements[cursor].equals(Character.toString(CharUtility.equal))) {
							pickupNext = true;
							continue;
						}
						if(pickupNext) {
							functionName = lineElements[cursor];
							if(!functionName.matches("\\w+")) {
								functionName = TextUtility.EMPTY_STRING;
							}
							break;
						}
					}
				}
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		CommonOutlineFunctionElement aFunction = new CommonOutlineFunctionElement(functionName, functionSignature, getStart(token), 1);

		if(aFunction.getLabel(aFunction).startsWith(CommonOutlineFunctionElement.ANONYMOUS) && isAnonymousEnabled() == false) {
			aFunction = null;
		}
		
		return aFunction;
		
	}
	private boolean isAnonymousEnabled() {
		return !getPreferenceStore().getBoolean(
					IPreferenceNames.P_SHOW_ANONYMOUS_JS_OUTLINE);
		
	}
	private IPreferenceStore getPreferenceStore() {
		return Activator.getDefault().getPreferenceStore();
	}
	
	private void goTo(JavascriptTokenType type) {
		while((getNextToken()).getId() != type.getIndex()) {}
	}
	private String getSignature(Symbol token) {
		String signature = "";
		if(token.getId() != JavascriptTokenType.LPAREN.getIndex()) {
			goTo(JavascriptTokenType.LPAREN);
		}
		while((token = getNextToken()).getId() != JavascriptTokenType.RPAREN.getIndex()) {
			if(token.getId() == JavascriptTokenType.COMMA.getIndex()) {
				signature += ", ";
			} else {
				signature += (String) token.value;
			}
		}
		return signature;
	}
	
	
	@Override
	protected void setSource() {
		((JavascriptFlexScanner) getScanner()).setSource(getDocument().get());
	}
	@Override
	protected Boolean isOutlinableToken(Symbol token) {
		return isTokenType(token, JavascriptTokenType.FUNCTION);
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
