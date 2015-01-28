/**
 * Aptana Studio
 * Copyright (c) 2005-2011 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the GNU Public License (GPL) v3 (with exceptions).
 * Please see the license.html included with this distribution for details.
 * Any modifications to this file must keep this entire header intact.
 */
package smalleditor.editors.common;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DefaultIndentLineAutoEditStrategy;
import org.eclipse.jface.text.DocumentCommand;
import org.eclipse.jface.text.IDocument;

public class CommonAutoIndentStrategy extends DefaultIndentLineAutoEditStrategy {
	private String indent = ""; //$NON-NLS-1$

	public CommonAutoIndentStrategy(String indent) {
		super();
		this.indent = indent;
	}

	private String getTrimmedLine(IDocument d, int start, int offset) throws BadLocationException {
		String line = d.get(start, offset - start);
		return line.trim();
	}
	
	private void surround(IDocument d, DocumentCommand c) {
		if(c.length == 0) {
			return;
		}
		try {
			StringBuffer buffer = new StringBuffer(c.text);
			buffer.append(d.get(c.offset, c.length));
			
			buffer.append(getClosing(c.text));
			
			c.text = buffer.toString();
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	private void indent(IDocument d, DocumentCommand c) {
		try {
			int line = d.getLineOfOffset(c.offset);
			int start = d.getLineOffset(line);
			int end = findEndOfWhiteSpace(d, start, c.offset);
			String currentIndent = d.get(start, end - start);
			
			StringBuffer indentBuffer = new StringBuffer(c.text);
			indentBuffer.append(currentIndent);
			
			String trimmed = getTrimmedLine(d, start, c.offset);
			char pre = trimmed.length() > 0 ? trimmed.charAt(trimmed.length() - 1) : '\0'; 
			
			switch (pre) {
			case '>':
			case '[':
			case '{':
				
				indentBuffer.append(indent);
				
				
				break;
			case ']':
			case '}':
				deindent(d, c);
				return;
			}
			
			c.text = indentBuffer.toString();

		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	private void deindent(IDocument d, DocumentCommand c) {
		try {
			int line = d.getLineOfOffset(c.offset);
			int start = d.getLineOffset(line);
			int end = findEndOfWhiteSpace(d, start, c.offset);
			String currentIndent = d.get(start, end - start);
			
			StringBuffer indentBuffer = new StringBuffer(currentIndent);
			
			String trimmed = getTrimmedLine(d, start, c.offset);
			char pre = isDelimiter(d, c.text) ? trimmed.charAt(trimmed.length() - 1) : c.text.charAt(0); 
			
			int indentLength = Math.max(0, indentBuffer.length() - indent.length());
			indentBuffer.setLength(indentLength);
			switch (pre) {
			case ']':
			case '}':
				if(trimmed.length() == 0) {
					
					d.replace(start, c.offset - start, indentBuffer.toString()); // fix indent of this line
					c.offset -= indent.length();
				} else if (isDelimiter(d, c.text)){
					c.text += indentBuffer.toString();
				}
				break;
			}
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void customizeDocumentCommand(IDocument d, DocumentCommand c) {
//		System.out.println("customizeDocumentCommand");
		if(c.doit == false) { return; }
		
		//length = 0 when no selection
		if (/*c.length == 0 && */c.text != null) {
			
			if(isDelimiter(d, c.text)) {
				indent(d, c);
				return;
			}
			if(isClosingBlock(c.text)) {
				deindent(d, c);
				return;
			}
			if(isOpening(c.text)) {
				surround(d, c);
				return;
			}
		}
		super.customizeDocumentCommand(d, c);
	}

	
	private boolean isOpening(String text) {
		String[] delimiters = {"{", "(", "[", "'", "\""}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

		for (String delimiter : delimiters) {
			if (text.endsWith(delimiter)) {
				return true;
			}
		}

		return false;
	}
	private String getClosing(String text) {
		String openner = "{(['\""; //$NON-NLS-1$
		String closer = "})]'\""; //$NON-NLS-1$

		int pos = openner.indexOf(text);
		return Character.toString(closer.charAt(pos));
	}
	private boolean isClosingBlock(String text) {
		String[] delimiters = {"}", "]"}; //$NON-NLS-1$ //$NON-NLS-2$

		for (String delimiter : delimiters) {
			if (text.endsWith(delimiter)) {
				return true;
			}
		}

		return false;
	}
	
	private boolean isDelimiter(IDocument d, String text) {
		String[] delimiters = d.getLegalLineDelimiters();

		for (String delimiter : delimiters) {
			if (text.endsWith(delimiter)) {
				return true;
			}
		}

		return false;
	}
}
