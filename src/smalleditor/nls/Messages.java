/**
 * Aptana Studio
 * Copyright (c) 2005-2011 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the GNU Public License (GPL) v3 (with exceptions).
 * Please see the license.html included with this distribution for details.
 * Any modifications to this file must keep this entire header intact.
 */
package smalleditor.nls;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import smalleditor.utils.IConstants;

public class Messages {

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(IConstants.BUNDLE_NAME);

	private Messages() {
	}

	public static ResourceBundle getResourceBundle() {
		return RESOURCE_BUNDLE;
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			e.printStackTrace();
			return '!' + key + '!';
		}
	}
}
