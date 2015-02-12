/**
 * Aptana Studio
 * Copyright (c) 2005-2012 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the GNU Public License (GPL) v3 (with exceptions).
 * Please see the license.html included with this distribution for details.
 * Any modifications to this file must keep this entire header intact.
 */
package smalleditor.editors.javascript.parsing;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import smalleditor.parsing.ITypePredicate;


public enum JavascriptTokenType implements ITypePredicate {
	UNDEFINED("UNDEFINED", -1), //$NON-NLS-1$
	EOF("EOF", JavascriptGrammarTerminals.EOF), //$NON-NLS-1$
	LPAREN(JavascriptLanguageConstants.LPAREN, JavascriptGrammarTerminals.LPAREN), IDENTIFIER(
			"IDENTIFIER", JavascriptGrammarTerminals.IDENTIFIER), //$NON-NLS-1$
	LCURLY(JavascriptLanguageConstants.LCURLY, JavascriptGrammarTerminals.LCURLY), 
	LBRACKET(JavascriptLanguageConstants.LBRACKET, JavascriptGrammarTerminals.LBRACKET),
	PLUS_PLUS(JavascriptLanguageConstants.PLUS_PLUS, JavascriptGrammarTerminals.PLUS_PLUS), MINUS_MINUS(
			JavascriptLanguageConstants.MINUS_MINUS, JavascriptGrammarTerminals.MINUS_MINUS), STRING(
			"STRING", JavascriptGrammarTerminals.STRING), //$NON-NLS-1$
	NUMBER("NUMBER", JavascriptGrammarTerminals.NUMBER), //$NON-NLS-1$
	MINUS(JavascriptLanguageConstants.MINUS, JavascriptGrammarTerminals.MINUS), PLUS(
			JavascriptLanguageConstants.PLUS, JavascriptGrammarTerminals.PLUS),
	FUNCTION(JavascriptLanguageConstants.FUNCTION, JavascriptGrammarTerminals.FUNCTION), 
	THIS(
			JavascriptLanguageConstants.THIS, JavascriptGrammarTerminals.THIS), NEW(
			JavascriptLanguageConstants.NEW, JavascriptGrammarTerminals.NEW), NULL(
			JavascriptLanguageConstants.NULL, JavascriptGrammarTerminals.NULL), TRUE(
			JavascriptLanguageConstants.TRUE, JavascriptGrammarTerminals.TRUE), FALSE(
			JavascriptLanguageConstants.FALSE, JavascriptGrammarTerminals.FALSE), REGEX(
			"REGEX", JavascriptGrammarTerminals.REGEX), //$NON-NLS-1$
	DELETE(JavascriptLanguageConstants.DELETE, JavascriptGrammarTerminals.DELETE), EXCLAMATION(
			JavascriptLanguageConstants.EXCLAMATION, JavascriptGrammarTerminals.EXCLAMATION), TILDE(
			JavascriptLanguageConstants.TILDE, JavascriptGrammarTerminals.TILDE), TYPEOF(
			JavascriptLanguageConstants.TYPEOF, JavascriptGrammarTerminals.TYPEOF), VOID(
			JavascriptLanguageConstants.VOID, JavascriptGrammarTerminals.VOID), SEMICOLON(
			JavascriptLanguageConstants.SEMICOLON, JavascriptGrammarTerminals.SEMICOLON), COMMA(
			JavascriptLanguageConstants.COMMA, JavascriptGrammarTerminals.COMMA), VAR(
			JavascriptLanguageConstants.VAR, JavascriptGrammarTerminals.VAR), WHILE(
			JavascriptLanguageConstants.WHILE, JavascriptGrammarTerminals.WHILE), FOR(
			JavascriptLanguageConstants.FOR, JavascriptGrammarTerminals.FOR), DO(JavascriptLanguageConstants.DO,
			JavascriptGrammarTerminals.DO), SWITCH(JavascriptLanguageConstants.SWITCH, JavascriptGrammarTerminals.SWITCH), IF(
			JavascriptLanguageConstants.IF, JavascriptGrammarTerminals.IF), CONTINUE(
			JavascriptLanguageConstants.CONTINUE, JavascriptGrammarTerminals.CONTINUE), BREAK(
			JavascriptLanguageConstants.BREAK, JavascriptGrammarTerminals.BREAK), WITH(
			JavascriptLanguageConstants.WITH, JavascriptGrammarTerminals.WITH), RETURN(
			JavascriptLanguageConstants.RETURN, JavascriptGrammarTerminals.RETURN), THROW(
			JavascriptLanguageConstants.THROW, JavascriptGrammarTerminals.THROW), TRY(
			JavascriptLanguageConstants.TRY, JavascriptGrammarTerminals.TRY), RPAREN(
			JavascriptLanguageConstants.RPAREN, JavascriptGrammarTerminals.RPAREN), ELSE(
			JavascriptLanguageConstants.ELSE, JavascriptGrammarTerminals.ELSE), RCURLY(
			JavascriptLanguageConstants.RCURLY, JavascriptGrammarTerminals.RCURLY), COLON(
			JavascriptLanguageConstants.COLON, JavascriptGrammarTerminals.COLON), RBRACKET(
			JavascriptLanguageConstants.RBRACKET, JavascriptGrammarTerminals.RBRACKET), IN(
			JavascriptLanguageConstants.IN, JavascriptGrammarTerminals.IN), EQUAL(
			JavascriptLanguageConstants.EQUAL, JavascriptGrammarTerminals.EQUAL), CASE(
			JavascriptLanguageConstants.CASE, JavascriptGrammarTerminals.CASE), DOT(
			JavascriptLanguageConstants.DOT, JavascriptGrammarTerminals.DOT), LESS_LESS(
			JavascriptLanguageConstants.LESS_LESS, JavascriptGrammarTerminals.LESS_LESS), GREATER_GREATER(
			JavascriptLanguageConstants.GREATER_GREATER, JavascriptGrammarTerminals.GREATER_GREATER), GREATER_GREATER_GREATER(
			JavascriptLanguageConstants.GREATER_GREATER_GREATER,
			JavascriptGrammarTerminals.GREATER_GREATER_GREATER), LESS(JavascriptLanguageConstants.LESS,
			JavascriptGrammarTerminals.LESS), GREATER(JavascriptLanguageConstants.GREATER,
			JavascriptGrammarTerminals.GREATER), LESS_EQUAL(JavascriptLanguageConstants.LESS_EQUAL,
			JavascriptGrammarTerminals.LESS_EQUAL), GREATER_EQUAL(
			JavascriptLanguageConstants.GREATER_EQUAL, JavascriptGrammarTerminals.GREATER_EQUAL), INSTANCEOF(
			JavascriptLanguageConstants.INSTANCEOF, JavascriptGrammarTerminals.INSTANCEOF), EQUAL_EQUAL(
			JavascriptLanguageConstants.EQUAL_EQUAL, JavascriptGrammarTerminals.EQUAL_EQUAL), EXCLAMATION_EQUAL(
			JavascriptLanguageConstants.EXCLAMATION_EQUAL, JavascriptGrammarTerminals.EXCLAMATION_EQUAL), EQUAL_EQUAL_EQUAL(
			JavascriptLanguageConstants.EQUAL_EQUAL_EQUAL, JavascriptGrammarTerminals.EQUAL_EQUAL_EQUAL), EXCLAMATION_EQUAL_EQUAL(
			JavascriptLanguageConstants.EXCLAMATION_EQUAL_EQUAL,
			JavascriptGrammarTerminals.EXCLAMATION_EQUAL_EQUAL), AMPERSAND(
			JavascriptLanguageConstants.AMPERSAND, JavascriptGrammarTerminals.AMPERSAND), CARET(
			JavascriptLanguageConstants.CARET, JavascriptGrammarTerminals.CARET), PIPE(
			JavascriptLanguageConstants.PIPE, JavascriptGrammarTerminals.PIPE), AMPERSAND_AMPERSAND(
			JavascriptLanguageConstants.AMPERSAND_AMPERSAND,
			JavascriptGrammarTerminals.AMPERSAND_AMPERSAND), STAR_EQUAL(
			JavascriptLanguageConstants.STAR_EQUAL, JavascriptGrammarTerminals.STAR_EQUAL), FORWARD_SLASH_EQUAL(
			JavascriptLanguageConstants.FORWARD_SLASH_EQUAL,
			JavascriptGrammarTerminals.FORWARD_SLASH_EQUAL), PERCENT_EQUAL(
			JavascriptLanguageConstants.PERCENT_EQUAL, JavascriptGrammarTerminals.PERCENT_EQUAL), PLUS_EQUAL(
			JavascriptLanguageConstants.PLUS_EQUAL, JavascriptGrammarTerminals.PLUS_EQUAL), MINUS_EQUAL(
			JavascriptLanguageConstants.MINUS_EQUAL, JavascriptGrammarTerminals.MINUS_EQUAL), LESS_LESS_EQUAL(
			JavascriptLanguageConstants.LESS_LESS_EQUAL, JavascriptGrammarTerminals.LESS_LESS_EQUAL), GREATER_GREATER_EQUAL(
			JavascriptLanguageConstants.GREATER_GREATER_EQUAL,
			JavascriptGrammarTerminals.GREATER_GREATER_EQUAL), GREATER_GREATER_GREATER_EQUAL(
			JavascriptLanguageConstants.GREATER_GREATER_GREATER_EQUAL,
			JavascriptGrammarTerminals.GREATER_GREATER_GREATER_EQUAL), AMPERSAND_EQUAL(
			JavascriptLanguageConstants.AMPERSAND_EQUAL, JavascriptGrammarTerminals.AMPERSAND_EQUAL), CARET_EQUAL(
			JavascriptLanguageConstants.CARET_EQUAL, JavascriptGrammarTerminals.CARET_EQUAL), PIPE_EQUAL(
			JavascriptLanguageConstants.PIPE_EQUAL, JavascriptGrammarTerminals.PIPE_EQUAL), STAR(
			JavascriptLanguageConstants.STAR, JavascriptGrammarTerminals.STAR), FORWARD_SLASH(
			JavascriptLanguageConstants.FORWARD_SLASH, JavascriptGrammarTerminals.FORWARD_SLASH), PERCENT(
			JavascriptLanguageConstants.PERCENT, JavascriptGrammarTerminals.PERCENT), QUESTION(
			JavascriptLanguageConstants.QUESTION, JavascriptGrammarTerminals.QUESTION), PIPE_PIPE(
			JavascriptLanguageConstants.PIPE_PIPE, JavascriptGrammarTerminals.PIPE_PIPE), DEFAULT(
			JavascriptLanguageConstants.DEFAULT, JavascriptGrammarTerminals.DEFAULT), FINALLY(
			JavascriptLanguageConstants.FINALLY, JavascriptGrammarTerminals.FINALLY), CATCH(
			JavascriptLanguageConstants.CATCH, JavascriptGrammarTerminals.CATCH), DEBUGGER("debugger",
			JavascriptGrammarTerminals.DEBUGGER), CLASS("class", JavascriptGrammarTerminals.CLASS), ENUM("enum",
			JavascriptGrammarTerminals.ENUM), EXPORT("export", JavascriptGrammarTerminals.EXPORT), EXTENDS(
			"extends", JavascriptGrammarTerminals.EXTENDS), IMPORT("import", JavascriptGrammarTerminals.IMPORT), SUPER(
			"super", JavascriptGrammarTerminals.SUPER), IMPLEMENTS("implements",
			JavascriptGrammarTerminals.IMPLEMENTS), INTERFACE("interface", JavascriptGrammarTerminals.INTERFACE), LET(
			"let", JavascriptGrammarTerminals.LET), PACKAGE("package", JavascriptGrammarTerminals.PACKAGE), PRIVATE(
			"private", JavascriptGrammarTerminals.PRIVATE), PROTECTED("protected",
			JavascriptGrammarTerminals.PROTECTED), PUBLIC("public", JavascriptGrammarTerminals.PUBLIC), STATIC(
			"static", JavascriptGrammarTerminals.STATIC), YIELD("yield", JavascriptGrammarTerminals.YIELD), SET(
			JavascriptLanguageConstants.SET, JavascriptGrammarTerminals.SET), GET(
			JavascriptLanguageConstants.GET, JavascriptGrammarTerminals.GET),

	SINGLELINE_COMMENT("SINGLELINE_COMMENT", 1024), //$NON-NLS-1$
	MULTILINE_COMMENT("MULTILINE_COMMENT", 1025), //$NON-NLS-1$
	SDOC("SDOC", 1026), //$NON-NLS-1$
	VSDOC("VSDOC", 1027), //$NON-NLS-1$

	// Note: STRING_SINGLE and STRING_DOUBLE do not map to Terminals.STRING
	// because they should not
	// override it later on in the mappings (also, they are just needed for the
	// coloring scanner and
	// not outside of that scope).
	STRING_SINGLE("STRING_SINGLE", 1028), //$NON-NLS-1$
	STRING_DOUBLE("STRING_DOUBLE", 1029); //$NON-NLS-1$

	private static Map<String, JavascriptTokenType> NAME_MAP;
	private static Map<Short, JavascriptTokenType> ID_MAP;

	private String _name;
	private short _index;

	/**
	 * static initializer
	 */
	static {
		NAME_MAP = new HashMap<String, JavascriptTokenType>();
		ID_MAP = new HashMap<Short, JavascriptTokenType>();

		for (JavascriptTokenType type : EnumSet.allOf(JavascriptTokenType.class)) {
			NAME_MAP.put(type.getName(), type);
			ID_MAP.put(type.getIndex(), type);
		}
	}

	/**
	 * JavascriptTokenType
	 * 
	 * @param name
	 */
	private JavascriptTokenType(String name, short beaverId) {
		this._name = name;
		this._index = beaverId;
	}

	private JavascriptTokenType(String name, int index) {
		this(name, (short) index);
	}

	/**
	 * get
	 * 
	 * @param name
	 * @return
	 */
	public static JavascriptTokenType get(String name) {
		JavascriptTokenType result = UNDEFINED;

		if (NAME_MAP.containsKey(name)) {
			result = NAME_MAP.get(name);
		}

		return result;
	}

	public static JavascriptTokenType get(short id) {
		JavascriptTokenType result = UNDEFINED;

		if (ID_MAP.containsKey(id)) {
			result = ID_MAP.get(id);
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.aptana.parsing.lexer.ITypePredicate#getIndex()
	 */
	public short getIndex() {
		return this._index;
	}

	/**
	 * getName
	 * 
	 * @return
	 */
	public String getName() {
		return this._name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.aptana.editor.css.parsing.lexer.ITypePredicate#isDefined()
	 */
	public boolean isDefined() {
		return this != UNDEFINED;
	}

	/**
	 * toString
	 */
	public String toString() {
		return this.getName();
	}
}