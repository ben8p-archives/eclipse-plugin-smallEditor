/*
 * Created on May 15, 2003
 *========================================================================
 * Modifications history
 *========================================================================
 * $Log: JSFunctionElement.java,v $
 * Revision 1.2  2003/05/30 20:53:09  agfitzp
 * 0.0.2 : Outlining is now done as the user types. Some other bug fixes.
 *
 *========================================================================
 */
package smalleditor.editors.common.outline;

import org.eclipse.jface.resource.ImageDescriptor;


/**
 * @author fitzpata
 */
public class CommonOutlineClassNameElement extends ACommonOutlineElement {

	/**
	 * @param aName
	 * @param offset
	 * @param length
	 */
	public CommonOutlineClassNameElement(String aName,
			int offset, int length) {
		super(aName, offset, length);
	}

	/**
	 * @see org.eclipse.ui.model.IWorkbenchAdapter#getImageDescriptor(Object)
	 */
	public ImageDescriptor getImageDescriptor(Object object) {
		return null;
	}

	/**
	 * Method declared on IWorkbenchAdapter
	 * 
	 * @param o
	 * 
	 * @return
	 */
	public String getLabel(Object o) {
		return name;
	}

	public int category() {
		return CLASSNAME;
	}

}
