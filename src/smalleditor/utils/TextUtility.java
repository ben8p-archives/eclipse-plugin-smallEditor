/**
 * Aptana Studio
 * Copyright (c) 2005-2011 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the GNU Public License (GPL) v3 (with exceptions).
 * Please see the license.html included with this distribution for details.
 * Any modifications to this file must keep this entire header intact.
 */

package smalleditor.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.text.rules.ICharacterScanner;

/**
 * @author Max Stepanov
 */
public final class TextUtility {

	// TODO Move to util plugin

	/**
	 * 
	 */
	private TextUtility() {
	}

	public static String getStringValue(Object object) {
		return (object != null) ? object.toString() : "";
	}
	
	/**
	 * Combines by flattening the string arrays into a single string array. Does
	 * not add duplicate strings!
	 * 
	 * @param arrays
	 * @return
	 */
	public static String[] combine(String[][] arrays) {
		List<String> list = new ArrayList<String>();
		for (String[] array : arrays) {
			for (String i : array) {
				if (!list.contains(i)) {
					list.add(i);
				}
			}
		}
		return list.toArray(new String[list.size()]);
	}

	/**
	 * Flattens each 2d String array into a single 2D array containing them all.
	 * {{"1", "2"}, {"3", "4"}} and {{"5", "6"}} becomes {"1", "2"}, {"3", "4"},
	 * {"5", "6"}}. Duplicates are retained.
	 * 
	 * @param arraysArray
	 * @return
	 */
	public static String[][] combineArrays(String[][]... arraysArray) {
		List<String[]> list = new ArrayList<String[]>();
		for (String[][] arrays : arraysArray) {
			for (String[] array : arrays) {
				list.add(array);
			}
		}
		String[][] arrays = new String[list.size()][1];
		for (int i = 0; i < list.size(); i++) {
			arrays[i] = list.get(i);
		}

		return arrays;
	}

	public static boolean isEmpty(String text) {
		return text == null || text.trim().length() == 0;
	}

	public static char[][] removeDuplicates(char[][] arrays) {
		List<char[]> list = new ArrayList<char[]>();
		Set<String> strings = new HashSet<String>();
		for (char[] i : arrays) {
			String string = String.valueOf(i);
			if (!strings.contains(string)) {
				list.add(i);
				strings.add(string);
			}
		}
		return list.toArray(new char[list.size()][]);
	}

	public static char[][] replace(char[][] arrays, char character,
			char[][] replacements) {
		List<char[]> list = new ArrayList<char[]>();
		for (char[] array : arrays) {
			String string = String.valueOf(array);
			if (string.indexOf(character) >= 0) {
				for (char[] replacement : replacements) {
					list.add(string.replaceAll(String.valueOf(character),
							String.valueOf(replacement)).toCharArray());
				}
			} else {
				list.add(array);
			}
		}
		return list.toArray(new char[list.size()][]);
	}

	/**
	 * replace in a string a string sequence with another string sequence
	 */
	public static String replace(String source, String whatBefore,
			String whatAfter) {
		if (null == source || source.length() == 0) {
			return source;
		}
		int beforeLen = whatBefore.length();
		if (beforeLen == 0) {
			return source;
		}
		StringBuffer result = new StringBuffer("");
		int lastIndex = 0;
		int index = source.indexOf(whatBefore, lastIndex);
		while (index >= 0) {
			result.append(source.substring(lastIndex, index));
			result.append(whatAfter);
			lastIndex = index + beforeLen;

			// get next
			index = source.indexOf(whatBefore, lastIndex);
		}
		result.append(source.substring(lastIndex));
		return result.toString();
	}

	public static char[][] rsort(char[][] arrays) {
		arrays = arrays.clone();
		Arrays.sort(arrays, new Comparator<char[]>() {
			public int compare(char[] o1, char[] o2) {
				return o1.length - o2.length;
			}
		});
		return arrays;
	}

	public static boolean sequenceDetected(ICharacterScanner characterScanner,
			char[] sequence, boolean ignoreCase) {
		for (int i = 1; i < sequence.length; ++i) {
			int c = characterScanner.read();
			if ((ignoreCase && Character.toLowerCase(c) != Character
					.toLowerCase(sequence[i]))
					|| (!ignoreCase && c != sequence[i])) {
				// Non-matching character detected, rewind the scanner back to
				// the start.
				// Do not unread the first character.
				characterScanner.unread();
				for (int j = i - 1; j > 0; --j) {
					characterScanner.unread();
				}
				return false;
			}
		}
		for (int j = sequence.length - 1; j > 0; --j) {
			characterScanner.unread();
		}
		return true;
	}

	/**
	 * Pads the beginning of a string with a given character.
	 * 
	 * @param string
	 * @param desiredLength
	 * @param padChar
	 * @return
	 */
	public static String pad(String string, int desiredLength, char padChar) {
		if (string == null) {
			string = "";
		}

		int diff = desiredLength - string.length();
		if (diff > 0) {
			string = repeat(padChar, diff) + string;
		}
		return string;
	}

	/**
	 * Repeats the given char n times and returns it as a new string.
	 */
	public static String repeat(char c, int times) {
		char[] buf = new char[times];
		for (int i = 0; i < times; i++) {
			buf[i] = c;
		}
		return new String(buf);
	}

	/**
	 * Create a string by concatenating the elements of a collection using a
	 * delimiter between each item
	 * 
	 * @param delimiter
	 *            The text to place between each element in the array
	 * @param items
	 *            The collection of items to join
	 * @return The resulting string
	 */
	public static String join(String delimiter, Collection<String> items) {
		return (items != null) ? join(delimiter,
				items.toArray(new String[items.size()])) : null;
	}

	/**
	 * Create a string by concatenating the elements of a collection using a
	 * delimiter between each item
	 * 
	 * @param delimiter
	 *            The text to place between each element in the array
	 * @param items
	 *            The array of items
	 * @return The resulting string
	 */
	public static String join(String delimiter, Object... items) {
		String[] s = new String[items.length];
		for (int i = 0; i < items.length; i++) {
			Object item = items[i];
			if (item == null) {
				s[i] = "null"; //$NON-NLS-1$

			} else {
				s[i] = item.toString();
			}
		}
		return join(delimiter, s);
	}

	/**
	 * Create a string by concatenating the elements of a collection using a
	 * delimiter between each item
	 * 
	 * @param delimiter
	 *            The text to place between each element in the array
	 * @param items
	 *            The array of chars
	 * @return The resulting string
	 */
	public static String join(String delimiter, char... items) {
		String[] strings = new String[items.length];
		for (int i = 0; i < items.length; i++) {
			strings[i] = new String(items, i, 1);
		}
		return join(delimiter, strings);
	}

	/**
	 * Create a string by concatenating the elements of a string array using a
	 * delimiter between each item
	 * 
	 * @param delimiter
	 *            The text to place between each element in the array
	 * @param items
	 *            The array of items to join
	 * @return The resulting string
	 */
	public static String join(String delimiter, String... items) {
		String result = null;

		if (items != null) {
			switch (items.length) {
			case 0: {
				result = "";
				break;
			}

			case 1: {
				result = items[0];
				break;
			}

				// NOTE: consider adding additional cases here, probably for at
				// least 2, by unrolling the loop from the
				// default section below

			default: {
				int lastIndex = items.length - 1;

				// determine length of the delimiter
				int delimiterLength = (delimiter != null) ? delimiter.length()
						: 0;

				// determine the length of the resulting string, starting with
				// the length of all delimiters
				int targetLength = (lastIndex) * delimiterLength;

				// now add in the length of each item in our list of items
				for (int i = 0; i <= lastIndex; i++) {
					targetLength += items[i].length();
				}

				// build the resulting character array
				int offset = 0;
				char[] accumulator = new char[targetLength];

				// NOTE: We test for delimiter length here to avoid having a
				// conditional within the for-loops in the
				// true/false blocks. Moving the conditional inside the for-loop
				// barely improved the performance of
				// this implementation from the StringBuilder version we had
				// before
				if (delimiterLength != 0) {
					// copy all items (except last) and all delimiters
					for (int i = 0; i < lastIndex; i++) {
						String item = items[i];

						// cache current item's length
						int length = item.length();

						// copy the item into the accumulator
						item.getChars(0, length, accumulator, offset);
						offset += length;

						// copy in the delimiter
						delimiter.getChars(0, delimiterLength, accumulator,
								offset);
						offset += delimiterLength;
					}

					String item = items[lastIndex];
					item.getChars(0, item.length(), accumulator, offset);
				} else {
					// NOTE: use classic iteration to avoid the overhead of an
					// iterator
					for (int i = 0; i <= lastIndex; i++) {
						String item = items[i];

						// cache current item's length
						int length = item.length();

						// copy the item into the accumulator
						item.getChars(0, length, accumulator, offset);
						offset += length;
					}
				}

				// convert the result to a String and return that value
				result = new String(accumulator);
			}
			}
		}

		return result;
	}
}
