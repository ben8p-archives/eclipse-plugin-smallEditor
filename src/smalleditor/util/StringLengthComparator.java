/*
 * StringLengthComparator.java	Created on 9 Jan 2015
 * 
 * Copyright © 2015 ING Group. All rights reserved.
 * 
 * This software is the confidential and proprietary information of 
 * ING Group ("Confidential Information"). 
 */
package smalleditor.util;

import java.util.Comparator;

public class StringLengthComparator implements Comparator<String>{
	private Boolean orderASC = true;
	public StringLengthComparator(Boolean useASCOrder) {
		super();
		orderASC = useASCOrder;
	}
	@Override
	public int compare(String o1, String o2) {
		int multiplier = orderASC == true ? 1 : -1;
		if (o1.length() > o2.length()) {
			return 1 * multiplier;
		} else if (o1.length() < o2.length()) {
			return -1 * multiplier;
		}
		return o1.compareTo(o2);
	}
}