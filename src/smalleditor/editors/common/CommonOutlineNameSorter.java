package smalleditor.editors.common;

import org.eclipse.jface.viewers.ViewerSorter;

/**
 * Name sorter
 * 
 * @author $Author: agfitzp $, $Date: 2003/05/28 15:17:11 $
 * 
 * @version $Revision: 1.1 $
 */
class CommonOutlineNameSorter extends ViewerSorter {

	/**
	 * Returns the category of the given element. The category is a number used
	 * to allocate elements to bins; the bins are arranged in ascending numeric
	 * order. The elements within a bin are arranged via a second level sort
	 * criterion.
	 * 
	 * @param element
	 *            the element
	 * @return the category
	 */
	public int category(Object element) {
		return ((CommonOutlineElement) element).category();
	}
}