/**
 * Aptana Studio
 * Copyright (c) 2005-2011 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the GNU Public License (GPL) v3 (with exceptions).
 * Please see the license.html included with this distribution for details.
 * Any modifications to this file must keep this entire header intact.
 */
package smalleditor.utils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Utility functions for set-like operations on collections
 */
public class CollectionsUtility
{
	/**
	 * Add a varargs list of items to the specified list. If the list or items array are null, then no action is
	 * performed. Note that the destination list has no requirements other than it must be a List of the source item's
	 * type. This allows the destination to be used, for example, as an accumulator.<br>
	 * <br>
	 * Note that this method is not thread safe. Users of this method will need to maintain type safety against the
	 * list.
	 * 
	 * @param list
	 *            A list to which items will be added
	 * @param items
	 *            A list of items to add
	 */
	public static final <T, U extends T> List<T> addToList(List<T> list, U... items)
	{
		if (list != null && items != null)
		{
			for (int i = 0; i < items.length; i++)
			{
				list.add(items[i]);
			}
			if (list instanceof ArrayList)
			{
				((ArrayList<T>) list).trimToSize();
			}
		}

		return list;
	}

	/**
	 * Add a varargs list of items to the specified list. If the list or items array are null, then no action is
	 * performed. Note that the destination list has no requirements other than it must be a List of the source item's
	 * type. This allows the destination to be used, for example, as an accumulator.<br>
	 * <br>
	 * Note that this method is not thread safe. Users of this method will need to maintain type safety against the
	 * list.
	 * 
	 * @param list
	 *            A list to which items will be added
	 * @param items
	 *            A list of items to add
	 */
	public static final <T, U extends T> List<T> addToList(List<T> list, List<U> items)
	{
		if (list != null && items != null)
		{
			list.addAll(items);
		}

		return list;
	}

	/**
	 * Converts a list to a new copy of array based on the start index and end index.
	 * 
	 * @param list
	 * @param startIndex
	 * @param endIndex
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "cast" })
	public static final <T> T[] toArray(List<T> list, int startIndex, int endIndex)
	{
		if (isEmpty(list))
		{
			return (T[]) list.toArray();
		}
		List<T> subList = list.subList(startIndex, endIndex);
		return (T[]) subList.toArray((T[]) java.lang.reflect.Array.newInstance(list.get(0).getClass(), subList.size()));
	}

	public static final <T> T[] toArray(List<T> list)
	{
		return toArray(list, 0, list.size());
	}

	/**
	 * Add a varargs list of items into a set. If the set or items are null then no action is performed. Note that the
	 * destination set has no requirements other than it must be a Set of the source item's type. This allows the
	 * destination to be used, for example, as an accumulator.<br>
	 * <br>
	 * Note that this method is not thread safe. Users of this method will need to maintain type safety against the set.
	 * 
	 * @param set
	 *            A set to which items will be added
	 * @param items
	 *            A list of items to add
	 */
	public static final <T, U extends T> Set<T> addToSet(Set<T> set, U... items)
	{
		if (set != null && items != null)
		{
			for (int i = 0; i < items.length; i++)
			{
				set.add(items[i]);
			}
		}

		return set;
	}

	

	/**
	 * This is a convenience method that essentially checks for a null list and returns Collections.emptyList in that
	 * case. If the list is non-null, then this is an identity function.
	 * 
	 * @param <T>
	 * @param list
	 * @return
	 */
	public static <T> List<T> getListValue(List<T> list)
	{
		if (list == null)
		{
			return Collections.emptyList();
		}

		return list;
	}

	/**
	 * This is a convenience method that essentially checks for a null list and returns Collections.emptyList in that
	 * case. If the list is non-null, then this is an identity function.
	 * 
	 * @param <T>
	 * @param <U>
	 * @param list
	 * @return
	 */
	public static <T, U> Map<T, U> getMapValue(Map<T, U> list)
	{
		if (list == null)
		{
			return Collections.emptyMap();
		}

		return list;
	}

	/**
	 * This is a convenience method that essentially checks for a null set and returns Collections.emptySet in that
	 * case. If the set is non-null, then this is an identity function.
	 * 
	 * @param <T>
	 * @param set
	 * @return
	 */
	public static <T> Set<T> getSetValue(Set<T> set)
	{
		if (set == null)
		{
			return Collections.emptySet();
		}

		return set;
	}

	/**
	 * This is a convenience method to return the first element from a list. If the list is empty, then null is
	 * returned.
	 * 
	 * @param list
	 * @return
	 */
	public static <T> T getFirstElement(List<T> list)
	{
		if (!isEmpty(list))
		{
			return list.get(0);
		}

		return null;
	}

	/**
	 * Given two collections of elements of type <T>, return a collection with the items which only appear in one
	 * collection or the other
	 * 
	 * @param <T>
	 *            Type
	 * @param collection1
	 *            Collection #1
	 * @param collection2
	 *            Collection #2
	 * @return Collection with items unique to each list
	 */
	public static <T> Collection<T> getNonOverlapping(Collection<T> collection1, Collection<T> collection2)
	{
		Collection<T> result = union(collection1, collection2);

		if (!isEmpty(result))
		{
			result.removeAll(intersect(collection1, collection2));
			if (result instanceof ArrayList)
			{
				((ArrayList<T>) result).trimToSize();
			}
		}

		return result;
	}

	/**
	 * Given two collections of elements of type <T>, return a collection with the items which only appear in both lists
	 * 
	 * @param <T>
	 *            Type
	 * @param collection1
	 *            Collection #1
	 * @param collection2
	 *            Collection #2
	 * @return Collection with items common to both lists
	 */
	public static <T> Collection<T> intersect(Collection<T> collection1, Collection<T> collection2)
	{
		if (isEmpty(collection1) || isEmpty(collection2))
		{
			return Collections.emptyList();
		}

		Set<T> intersection = new HashSet<T>(collection1);

		intersection.retainAll(collection2);

		return intersection;
	}

	/**
	 * This is a convenience method that returns true if the specified collection is null or empty
	 * 
	 * @param <T>
	 *            Any type of object
	 * @param collection
	 * @return
	 */
	public static <T> boolean isEmpty(Collection<T> collection)
	{
		return collection == null || collection.isEmpty();
	}

	/**
	 * This is a convenience method that returns true if the specified map is null or empty
	 * 
	 * @param <T>
	 *            any type of key
	 * @param <U>
	 *            any type of value
	 * @param map
	 * @return
	 */
	public static <T, U> boolean isEmpty(Map<T, U> map)
	{
		return map == null || map.isEmpty();
	}

	

	/**
	 * Convert a varargs list of items into a Set while preserving order. An empty set is returned if items is null.
	 * 
	 * @param <T>
	 *            Any type of object
	 * @param items
	 *            A variable length list of items of type T
	 * @return Returns a new LinkedHashSet<T> or an empty set
	 */
	public static final <T> Set<T> newInOrderSet(T... items)
	{
		return addToSet(new LinkedHashSet<T>(items != null ? items.length : 0), items);
	}

	/**
	 * Convert a vararg list of items into a List. An empty list is returned if items is null
	 * 
	 * @param <T>
	 *            Any type of object
	 * @param items
	 *            A variable length list of items of type T
	 * @return Returns a new ArrayList<T> or an empty list
	 */
	public static final <T> List<T> newList(T... items)
	{
		return addToList(new ArrayList<T>(items != null ? items.length : 0), items);
	}

	/**
	 * Convert a list of items into a Set. An empty set is returned if items is null
	 * 
	 * @param <T>
	 *            Any type of object
	 * @param items
	 *            A variable length list of items of type T
	 * @return Returns a new HashSet<T> or an empty set
	 */
	public static final <T> Set<T> newSet(T... items)
	{
		return addToSet(new HashSet<T>(items != null ? items.length : 0), items);
	}

	/**
	 * Convert a list of items into a Set. An empty set is returned if items is null
	 * 
	 * @param <T>
	 *            Any type of object
	 * @param items
	 *            A variable length list of items of type T
	 * @return Returns a new HashSet<T> or an empty set
	 */
	public static final <T> Map<T, T> newMap(T... items)
	{
		return addToMap(new HashMap<T, T>(items != null ? items.length / 2 : 0), items);
	}

	/**
	 * Convert a list of items into a Set. An empty set is returned if items is null
	 * 
	 * @param <T>
	 *            Any type of object
	 * @param items
	 *            A variable length list of items of type T
	 * @return Returns a new HashSet<T> or an empty set
	 */
	public static final <T> Map<T, T> newInOrderMap(T... items)
	{
		return addToMap(new LinkedHashMap<T, T>(items != null ? items.length / 2 : 0), items);
	}

	/**
	 * Add a varargs list of items into a map. It is expected that items be in "key, value, key2, value2, etc.."
	 * ordering. If the map or items are null then no action is performed. Note that the destination map has no
	 * requirements other than it must be a Map of the source item's type. This allows the destination to be used, for
	 * example, as an accumulator.<br>
	 * <br>
	 * Note that this method is not thread safe. Users of this method will need to maintain type safety against the map.
	 * 
	 * @param keyType
	 *            The key type in the resulting map
	 * @param valueType
	 *            The value type in the resulting collection
	 * @param map
	 *            A map to which items will be added
	 * @param items
	 *            An interleaved list of keys and values
	 */
	public static final <T, U> void addToMap(Class<T> keyType, Class<U> valueType, Map<T, U> map, Object... items)
	{
		if (keyType != null && valueType != null && map != null && !ArrayUtility.isEmpty(items))
		{
			if (items.length % 2 != 0)
			{
				throw new IllegalArgumentException("Length of list of items must be multiple of 2"); //$NON-NLS-1$
			}

			for (int i = 0; i < items.length; i += 2)
			{
				Object keyObject = items[i];
				T key;
				if (keyType.isAssignableFrom(keyObject.getClass()))
				{
					key = keyType.cast(keyObject);
				}
				else
				{
					// @formatter:off
					String message = MessageFormat.format(
						"Key {0} was not of the expected type: {1}", //$NON-NLS-1$
						i,
						keyType
					);
					// @formatter:on
					throw new IllegalArgumentException(message);
				}

				Object valueObject = items[i + 1];
				U value;
				if (valueObject == null)
				{
					value = null;
					map.put(key, value);
				}
				else if (valueType.isAssignableFrom(valueObject.getClass()))
				{
					value = valueType.cast(valueObject);

					map.put(key, value);
				}
				else
				{
					// @formatter:off
					String message = MessageFormat.format(
						"Value {0} was not of the expected type: {1}", //$NON-NLS-1$
						i + 1,
						valueType
					);
					// @formatter:on
					throw new IllegalArgumentException(message);
				}
			}
		}
	}

	/**
	 * Convert a list of items into a Map where the key and value types are specified. An empty map is returned if
	 * keyType, valueType, or items is null
	 * 
	 * @param keyType
	 *            The key type in the resulting map
	 * @param valueType
	 *            The value type in the resulting collection
	 * @param items
	 *            An interleaved list of keys and values
	 * @return Returns a new HashMap<T, U> or an empty map
	 */
	public static final <T, U> Map<T, U> newTypedMap(Class<T> keyType, Class<U> valueType, Object... items)
	{
		Map<T, U> result;

		if (keyType != null && valueType != null && !ArrayUtility.isEmpty(items))
		{
			result = new HashMap<T, U>();
			addToMap(keyType, valueType, result, items);
		}
		else
		{
			result = Collections.emptyMap();
		}

		return result;
	}

	/**
	 * Add a varargs list of items into a map. It is expected that items be in "key, value, key2, value2, etc.."
	 * ordering. If the map or items are null then no action is performed. Note that the destination map has no
	 * requirements other than it must be a Map of the source item's type. This allows the destination to be used, for
	 * example, as an accumulator.<br>
	 * <br>
	 * Note that this method is not thread safe. Users of this method will need to maintain type safety against the map.
	 * 
	 * @param map
	 *            A map to which items will be added
	 * @param items
	 *            A list of items to add
	 */
	public static final <T, U extends T> Map<T, T> addToMap(Map<T, T> map, U... items)
	{
		if (map != null && items != null)
		{
			if (items.length % 2 != 0)
			{
				throw new IllegalArgumentException("Length of list of items must be multiple of 2"); //$NON-NLS-1$
			}
			for (int i = 0; i < items.length; i += 2)
			{
				map.put(items[i], items[i + 1]);
			}
		}

		return map;
	}

	/**
	 * Given a list of elements of type <T>, remove the duplicates from the list in place
	 * 
	 * @param <T>
	 * @param list
	 */
	public static <T> void removeDuplicates(List<T> list)
	{
		// uses LinkedHashSet to keep the order
		Set<T> set = new LinkedHashSet<T>(list);

		list.clear();
		list.addAll(set);
		if (list instanceof ArrayList)
		{
			((ArrayList<T>) list).trimToSize();
		}
	}

	/**
	 * Given two collections of elements of type <T>, return a collection containing the items from both lists
	 * 
	 * @param <T>
	 *            Type
	 * @param collection1
	 *            Collection #1
	 * @param collection2
	 *            Collection #2
	 * @return Collection with items from both lists
	 */
	public static <T> Collection<T> union(Collection<? extends T> collection1, Collection<? extends T> collection2)
	{
		if (isEmpty(collection1))
		{
			if (isEmpty(collection2))
			{
				// if both are empty, return empty list
				return Collections.emptyList();
			}
			// if just 1 is empty, return 2
			return new ArrayList<T>(collection2);
		}
		// at this point when know 1 is not empty
		if (isEmpty(collection2))
		{
			// so if 2 is, return 1.
			return new ArrayList<T>(collection1);
		}

		// we know both 1 and 2 aren't empty
		Set<T> union = new HashSet<T>(collection1.size() + collection2.size());

		union.addAll(collection1);
		union.addAll(collection2);

		return new ArrayList<T>(union);
	}

	private CollectionsUtility()
	{
	}

	

	public static int size(Collection<? extends Object> collection)
	{
		if (collection == null)
		{
			return 0;
		}
		return collection.size();
	}
}
