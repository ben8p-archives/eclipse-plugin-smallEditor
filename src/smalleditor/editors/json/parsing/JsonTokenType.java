/**
 * Aptana Studio
 * Copyright (c) 2005-2013 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the GNU Public License (GPL) v3 (with exceptions).
 * Please see the license.html included with this distribution for details.
 * Any modifications to this file must keep this entire header intact.
 */
package smalleditor.editors.json.parsing;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import smalleditor.parsing.ITypePredicate;

public enum JsonTokenType implements ITypePredicate
{
	
	STRING("STRING", JsonGrammarTerminals.STRING),
	LCURLY(JsonLanguageConstants.LCURLY, JsonGrammarTerminals.LCURLY),
	LBRACKET(JsonLanguageConstants.LBRACKET, JsonGrammarTerminals.LBRACKET),
	NUMBER("NUMBER", JsonGrammarTerminals.NUMBER),
	TRUE(JsonLanguageConstants.TRUE, JsonGrammarTerminals.TRUE),
	FALSE(JsonLanguageConstants.FALSE, JsonGrammarTerminals.FALSE),
	NULL(JsonLanguageConstants.NULL, JsonGrammarTerminals.NULL),
	RCURLY(JsonLanguageConstants.RCURLY, JsonGrammarTerminals.RCURLY),
	RBRACKET(JsonLanguageConstants.RBRACKET, JsonGrammarTerminals.RBRACKET),
	COMMA(JsonLanguageConstants.COMMA, JsonGrammarTerminals.COMMA),
	COLON(JsonLanguageConstants.COLON, JsonGrammarTerminals.COLON),
	EOF("EOF", JsonGrammarTerminals.EOF), //$NON-NLS-1$
	UNDEFINED("UNDEFINED", -1); //$NON-NLS-1$

	private static Map<String, JsonTokenType> NAME_MAP;
	private static Map<Short, JsonTokenType> ID_MAP;

	private String _name;
	private short _index;

	/**
	 * static initializer
	 */
	static {
		NAME_MAP = new HashMap<String, JsonTokenType>();
		ID_MAP = new HashMap<Short, JsonTokenType>();

		for (JsonTokenType type : EnumSet.allOf(JsonTokenType.class)) {
			NAME_MAP.put(type.getName(), type);
			ID_MAP.put(type.getIndex(), type);
		}
	}

	/**
	 * JsonTokenType
	 * 
	 * @param name
	 */
	private JsonTokenType(String name, short beaverId) {
		this._name = name;
		this._index = beaverId;
	}

	private JsonTokenType(String name, int index) {
		this(name, (short) index);
	}

	/**
	 * get
	 * 
	 * @param name
	 * @return
	 */
	public static JsonTokenType get(String name) {
		JsonTokenType result = UNDEFINED;

		if (NAME_MAP.containsKey(name)) {
			result = NAME_MAP.get(name);
		}

		return result;
	}

	public static JsonTokenType get(short id) {
		JsonTokenType result = UNDEFINED;

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
