package smalleditor.editors.common.outline;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.model.IWorkbenchAdapter;

import beaver.Symbol;


public abstract class ACommonOutlineElement implements IWorkbenchAdapter,
		IAdaptable, ICommonOutlineElementCategories {
	protected String name;
	protected int offset;
	protected int numberOfLines;
	protected int length;
	
	private Symbol token;

	protected ACommonOutlineElement parent;
	protected List children;
	protected HashMap childrenByName;

	/**
	 * Creates a new JSElement and stores parent element and location in the
	 * text.
	 * 
	 * @param aName
	 *            text corresponding to the func
	 * @param offset
	 *            the offset into the Readme text
	 * @param length
	 *            the length of the element
	 */
	public ACommonOutlineElement(String aName, int offset, int length) {
		this.name = aName;
		this.offset = offset;
		this.length = length;
		this.children = new LinkedList();
		this.childrenByName = new HashMap();
	}
	public void setToken(Symbol token) {
		this.token = token;
	}
	public Symbol getToken() {
		return token;
	}
	/**
	 * Method declared on IAdaptable
	 * 
	 * @param adapter
	 * 
	 * @return
	 */
	public Object getAdapter(Class adapter) {
		if (adapter == IWorkbenchAdapter.class) {
			return this;
		}

		return null;
	}
	
	public void addChildElement(ACommonOutlineElement anElement) {
		String elementName = anElement.getName();
		//if (!childrenByName.containsKey(elementName)) {
		this.children.add(anElement);
		this.childrenByName.put(elementName, anElement);
		anElement.setParent(this);
		//}
	}
	public void removeLastChildElement() {
		ACommonOutlineElement anElement = (ACommonOutlineElement) this.children.get(this.children.size() - 1);
		this.children.remove(anElement);
		this.childrenByName.remove(anElement.name);

		//}
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

	/**
	 * Returns the number of characters in this section.
	 * 
	 * @return
	 */
	public int getLength() {
		return length;
	}

	/**
	 * Returns the number of lines in the element.
	 * 
	 * @return the number of lines in the element
	 */
	public int getNumberOfLines() {
		return numberOfLines;
	}

	/**
	 * Returns the offset of this section in the file.
	 * 
	 * @return
	 */
	public int getStart() {
		return offset;
	}

	/**
	 * Sets the number of lines in the element
	 * 
	 * @param newNumberOfLines
	 *            the number of lines in the element
	 */
	public void setNumberOfLines(int newNumberOfLines) {
		numberOfLines = newNumberOfLines;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return getLabel(this);
	}

	/**
	 * @see org.eclipse.ui.model.IWorkbenchAdapter#getChildren(Object)
	 */
	public Object[] getChildren(Object o) {
		Object[] result = new Object[children.size()];
		return children.toArray(result);
	}

	/**
	 * @see org.eclipse.ui.model.IWorkbenchAdapter#getParent(Object)
	 */
	public Object getParent(Object o) {
		return null;
	}

	/**
	 * 
	 * @return A category enumeration for sub-types.
	 */
	abstract public int category();

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * @return
	 */
	public ACommonOutlineElement getParent() {
		return parent;
	}

	/**
	 * @param element
	 */
	public void setParent(ACommonOutlineElement element) {
		parent = element;
	}

	public boolean sharesParentWith(ACommonOutlineElement anElement) {
		if(anElement == null) { return false; }
		if (parent == null) {
			return anElement.getParent() == null;
		}

		return parent.equals(anElement.getParent());
	}

	public boolean equals(ACommonOutlineElement anElement) {
		return sharesParentWith(anElement) && name.equals(anElement.getName());
	}

}