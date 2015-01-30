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
package smalleditor.editors.common;

import org.eclipse.jface.resource.ImageDescriptor;

/**
 * @author fitzpata
 */
public class CommonOutlineFunctionElement extends ACommonOutlineElement {

	protected String arguments;

	/**
	 * @param aName
	 * @param offset
	 * @param length
	 */
	public CommonOutlineFunctionElement(String aName, String argumentString,
			int offset, int length) {
		super(aName, offset, length);
		arguments = argumentString;
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
		String firstPart = name;
		if (firstPart.length() <= 0) {
			firstPart = "<anonymous>"; //$NON-NLS-1$
		}

		return firstPart + arguments;
	}

	public int category() {
		return FUNCTION;
	}

	/**
	 * @return
	 */
	public String getArguments() {
		return arguments;
	}

}
