/**
 * Aptana Studio
 * Copyright (c) 2005-2013 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the GNU Public License (GPL) v3 (with exceptions).
 * Please see the license.html included with this distribution for details.
 * Any modifications to this file must keep this entire header intact.
 */
package smalleditor.editors.json.parsing;

import beaver.Symbol;

/**
 * Used in the jflex scanner so that we have faster mappings (i.e.: the symbol has the type directly, so, we don't need
 * to do a long switch in the scanner for the mappings).
 */
public class JsonTokenTypeSymbol extends Symbol
{

	public final JsonTokenType token;

	public JsonTokenTypeSymbol(JsonTokenType id, int left, int right, Object value)
	{
		super(id.getIndex(), left, right, value);
		this.token = id;
	}

}
