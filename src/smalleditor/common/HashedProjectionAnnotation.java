/*
 * HashedProjectionAnnotiation.java	Created on 13 Feb 2015
 * 
 * Copyright © 2015 ING Group. All rights reserved.
 * 
 * This software is the confidential and proprietary information of 
 * ING Group ("Confidential Information"). 
 */
package smalleditor.common;

import org.eclipse.jface.text.source.projection.ProjectionAnnotation;


public class HashedProjectionAnnotation extends ProjectionAnnotation {
	private int hashCode = 0;
	public void setHashCode(int hashCode) {
		this.hashCode = hashCode;
	}
	public int getHashCode() {
		return hashCode;
	}
}
