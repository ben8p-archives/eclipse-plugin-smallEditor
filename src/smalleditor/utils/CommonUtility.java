/**
 * Aptana Studio
 * Copyright (c) 2005-2011 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the GNU Public License (GPL) v3 (with exceptions).
 * Please see the license.html included with this distribution for details.
 * Any modifications to this file must keep this entire header intact.
 */

package smalleditor.utils;

import java.util.HashMap;

import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

/**
 * @author Max Stepanov
 */
public final class CommonUtility {

	/**
	 * 
	 */
	private static HashMap<String, Token> tokens = new HashMap<String, Token>();

	private CommonUtility() {
	}

	// TODO Ideally we generate a cache of tokens. We'd need a map with weak
	// keys and soft values. Ideally we'd also
	// have some sort of reaper to clean up unused refs over time. Perhaps just
	// use Google's Guava CacheBuilder?
	public static IToken getToken(String tokenName) {
		Token token = tokens.get(tokenName);
		if(token == null) {
			token = new Token(tokenName);
			tokens.put(tokenName, token);
		}
		return token;
	}

}
