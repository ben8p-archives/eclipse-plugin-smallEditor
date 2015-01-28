package smalleditor.common.tokenizer;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;

import smalleditor.editors.css.CssDocumentTokenBuilder;
import smalleditor.editors.html.HtmlDocumentTokenBuilder;
import smalleditor.editors.javascript.JavascriptDocumentTokenBuilder;
import smalleditor.editors.json.JsonDocumentTokenBuilder;

public class DocumentTokenBuilder {
	private List<DocumentNode> nodes;
//	private ArrayList<String> lines = new ArrayList<String>();
	private String[] elements = null;
	private DocumentNodeType[] elementsType = null;
	private DocumentTokenizer tokenizer;
	private String previousDocumentContent = null;
	
	private static DocumentTokenBuilder instanceLess = null;
	private static DocumentTokenBuilder instanceCss = null;
	private static DocumentTokenBuilder instanceJs = null;
	private static DocumentTokenBuilder instanceJson = null;
	private static DocumentTokenBuilder instanceHtml = null;
	
	public static DocumentTokenBuilder getDefault(DocumentType type) {
		if(type == DocumentType.CSS) {
			instanceCss = instanceCss == null ? new CssDocumentTokenBuilder() : instanceCss;
			return instanceCss;
		}
		if(type == DocumentType.LESS) {
			instanceLess = instanceLess == null ? new CssDocumentTokenBuilder() : instanceLess;
			return instanceLess;
		}
		if(type == DocumentType.JS) {
			instanceJs = instanceJs == null ? new JavascriptDocumentTokenBuilder() : instanceJs;
			return instanceJs;
		}
		if(type == DocumentType.JSON) {
			instanceJson = instanceJson == null ? new JsonDocumentTokenBuilder() : instanceJson;
			return instanceJson;
		}
		if(type == DocumentType.HTML) {
			instanceHtml = instanceHtml == null ? new HtmlDocumentTokenBuilder() : instanceHtml;
			return instanceHtml;
		}
		
		return null;
	}

	
	public DocumentTokenBuilder() {
		super();
	}
	protected void setElements(String[] newElements, DocumentNodeType[] newElementsType) {
		elements = newElements;
		elementsType = newElementsType;
	}
	
//	private String getLine(IDocument document, int line) {
//		String lineContent;
//		try {
//			lineContent = lines.get(line);
//			if(lineContent == null) {
//				lineContent = document.get(
//					document.getLineOffset(line),
//					document.getLineLength(line)
//				);
//				lines.add(line, lineContent);
//			}
//			return lineContent;
//			
//		} catch (BadLocationException e) {
//			
//		}
//		return null;
//	}
	
	public List<DocumentNode> buildNodes(IDocument document) {
		String documentContent = document.get();
		if(previousDocumentContent != null && previousDocumentContent.equals(documentContent)) {
			return nodes;
		}
		previousDocumentContent = documentContent;
		
		nodes = new LinkedList<DocumentNode>();
		
		if(elements != null) {
		
			int fromIndex = 0;
			DocumentNode node = null;
			
			tokenizer = new DocumentTokenizer(document, elements);
//			System.out.println("token counts:" + tokenizer.countTokens());
			while(tokenizer.hasMoreTokens()) {
				String token = tokenizer.nextToken();
				
				DocumentNodeType type = getNodeType(token, tokenizer.getPreviousToken(), tokenizer.getNextToken());
				DocumentNodeType nextType = getNodeType(tokenizer.getNextToken());
				DocumentNodeType previousType = getNodeType(tokenizer.getPreviousToken());
				
				if(type == null) {
					fromIndex += token.length();
					continue;
				}
				
				
				fromIndex = documentContent.indexOf(token, fromIndex);
				
				int length = token.length();
				
				if(type == DocumentNodeType.OneLineComment && nextType != DocumentNodeType.Todo && nextType != DocumentNodeType.Fixme) {
					node = createEOLNode(document, type, fromIndex, token);
				} else if(type == DocumentNodeType.OpenMultilineComment && nextType != DocumentNodeType.Todo && nextType != DocumentNodeType.Fixme) {
					node = createEOBNode(document, type, fromIndex, token, DocumentNodeType.CloseMultilineComment, true);
				} else if(type == DocumentNodeType.Todo || type == DocumentNodeType.Fixme) {
					node = createEOBNode(document, type, fromIndex, token, previousType == DocumentNodeType.OneLineComment ? DocumentNodeType.NewLine : DocumentNodeType.CloseMultilineComment, false);
				} else if(type == DocumentNodeType.String) {
					node = createEOBNode(document, type, fromIndex, token, DocumentNodeType.String, true);
				} else if(type == DocumentNodeType.SingleQuoteString) {
					node = createEOBNode(document, type, fromIndex, token, DocumentNodeType.SingleQuoteString, true);
				} else {
					node = createDefaultNode(document, type, fromIndex, length, token);
				}
				
				if (node != null) {
					nodes.add(node);
					fromIndex += node.getLength();
				} else {
					fromIndex += token.length();
				}
			}
		}
		return nodes;
	}
	
	protected DocumentNode createDefaultNode(IDocument document, DocumentNodeType type, int offset, int length, String expression) {
		return createNode(document, type, offset, length, expression);
	}
	
	protected DocumentNode createNode(IDocument document, DocumentNodeType type, int offset, int length, String expression) {
		DocumentNode node = new DocumentNode(type);
		node.setPosition(offset, length);
		try {
			node.setLine(document.getLineOfOffset(offset));
		} catch (BadLocationException e) {
			node.setLine(-1);
		}
		
		node.setValue(expression);
		return node;
	}
	
	//end of block
	protected DocumentNode createEOBNode(IDocument document, DocumentNodeType type, int offset, String expression, DocumentNodeType closeType, Boolean includeCloseToken) {
		//go til the end
		int length = 0;

		while(tokenizer.hasMoreTokens()) {
			String eobToken = tokenizer.nextToken();
			DocumentNodeType eobType = getNodeType(eobToken, tokenizer.getPreviousToken(), tokenizer.getNextToken());
			expression += " " + eobToken; //$NON-NLS-1$
			if(eobType == closeType) {
				if(includeCloseToken == false) {
					tokenizer.previousToken();
				}
				length = (document.get().indexOf(eobToken, offset + 1) - offset) + (includeCloseToken == false ? 0 : eobToken.length());
				
				break;
			}
		}
		return createNode(document, type, offset, length, expression);
	}
	
	//end of line
	protected DocumentNode createEOLNode(IDocument document, DocumentNodeType type, int offset, String expression) {
		return createEOBNode(document, type, offset, expression, DocumentNodeType.NewLine, false);
	}
	
	protected DocumentNodeType getNodeType(String token, String previousToken, String nextToken) {
		DocumentNodeType type = getNodeType(token);
		//check the previous to be sure we are in a todo or fixme
		if((type == DocumentNodeType.Todo || type == DocumentNodeType.Fixme) && getNodeType(previousToken) != DocumentNodeType.OneLineComment && getNodeType(previousToken) != DocumentNodeType.OpenMultilineComment ) {
			type = null;
		} else if((type == DocumentNodeType.String || type == DocumentNodeType.SingleQuoteString) && getNodeType(previousToken) == DocumentNodeType.EscapeChar) {
			type = null;
		}
		return type;
	}
	protected DocumentNodeType getNodeType(String token) {
		if(token == null) {
			return null;
		}
		int position = Arrays.asList(elements).indexOf(token.toLowerCase());
		if(position == -1) { return null; }
		return elementsType[position];
	}

	
}
