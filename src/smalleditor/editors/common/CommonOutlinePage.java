package smalleditor.editors.common;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.PatternFilter;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.progress.WorkbenchJob;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;


import smalleditor.common.tokenizer.DocumentNode;
import smalleditor.common.tokenizer.DocumentTokenBuilder;
import smalleditor.nls.Messages;
import smalleditor.utils.TextUtility;

public class CommonOutlinePage extends ContentOutlinePage {
	protected IDocument document;
	protected DocumentTokenBuilder scanner = null;
	protected Boolean sort = true;
	
	private List<DocumentNode> previousNodes = null;
	private List elementList = null;
	
	private Composite fMainControl;
	private Text fSearchBox;
	private TreeViewer fTreeViewer;
	private PatternFilter fFilter;
	private static final int FILTER_REFRESH_DELAY = 200;
	private WorkbenchJob fFilterRefreshJob;
	
	private static final String INITIAL_FILTER_TEXT = smalleditor.nls.Messages.getString("Outline.InitialFilterText");
	
	private ModifyListener fSearchModifyListener = new ModifyListener() {

		public void modifyText(ModifyEvent e) {
			String text = fSearchBox.getText();

			if (INITIAL_FILTER_TEXT.equals(text)) {
				fFilter.setPattern(null);
			} else {
				fFilter.setPattern(text);
			}
			// refresh the content on a delay
			fFilterRefreshJob.cancel();
			fFilterRefreshJob.schedule(FILTER_REFRESH_DELAY);
		}
	};
	
	protected void setSort(Boolean s) {
		sort = s;
	}
	
	public CommonOutlinePage(IDocument document) {
		super();
		this.document = document;
		this.scanner = getScanner();
	}

	/**
	 * Creates the control and registers the popup menu for this outlinePage
	 * Menu id "org.eclipse.ui.examples.readmetool.outline"
	 * 
	 * @param parent
	 */
	@Override
	public void createControl(Composite parent) {
		//super.createControl(parent);
		fMainControl = new Composite(parent, SWT.NONE);
		fMainControl.setLayout(GridLayoutFactory.fillDefaults().spacing(0, 2).create());
		fMainControl.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());

		fSearchBox = new Text(fMainControl, SWT.SINGLE | SWT.BORDER | SWT.SEARCH);
		fSearchBox.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).indent(0, 3).create());
		fSearchBox.setText(INITIAL_FILTER_TEXT);
		fSearchBox.setForeground(fSearchBox.getDisplay().getSystemColor(SWT.COLOR_TITLE_INACTIVE_FOREGROUND));
		fSearchBox.addFocusListener(new FocusListener() {

			public void focusLost(FocusEvent e) {
				if (fSearchBox.getText().length() == 0) {
					fSearchBox.removeModifyListener(fSearchModifyListener);
					fSearchBox.setText(INITIAL_FILTER_TEXT);
					fSearchBox.addModifyListener(fSearchModifyListener);
				}
				fSearchBox.setForeground(fSearchBox.getDisplay()
						.getSystemColor(SWT.COLOR_TITLE_INACTIVE_FOREGROUND));
			}

			public void focusGained(FocusEvent e) {
				if (fSearchBox.getText().equals(INITIAL_FILTER_TEXT)) {
					fSearchBox.removeModifyListener(fSearchModifyListener);
					fSearchBox.setText("");
					fSearchBox.addModifyListener(fSearchModifyListener);
				}
				fSearchBox.setForeground(null);
			}
		});
		
		fTreeViewer = new TreeViewer(fMainControl, SWT.VIRTUAL | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		fTreeViewer.addSelectionChangedListener(this);
		fTreeViewer.getControl().setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
		
		// apply tree filters
		fFilter = new PatternFilter() {};
		fFilter.setIncludeLeadingWildcard(true);
		
		final TreeViewer viewer = getTreeViewer();
		
		viewer.setUseHashlookup(true);
		viewer.setContentProvider(new WorkbenchContentProvider());
		viewer.setLabelProvider(new WorkbenchLabelProvider());
		//viewer.setInput(ResourcesPlugin.getWorkspace().getRoot());
		if(this.sort) {
			viewer.setSorter(new CommonOutlineNameSorter());
		}
		viewer.addFilter(fFilter);
		
		update();
		
		fFilterRefreshJob = new WorkbenchJob(
				Messages.getString("Outline.RefreshFilter")) //$NON-NLS-1$
		{

			@Override
			public IStatus runInUIThread(IProgressMonitor monitor) {
				if (isDisposed()) {
					return Status.CANCEL_STATUS;
				}

				fTreeViewer.refresh();
				String text = fSearchBox.getText();
				if (!TextUtility.isEmpty(text)
						&& !INITIAL_FILTER_TEXT.equals(text)) {
					fTreeViewer.expandAll();
				}
				return Status.OK_STATUS;
			}
		};
		fFilterRefreshJob.setSystem(true);
		
	}

	private boolean isDisposed() {
		Control control = getControl();
		return control == null || control.isDisposed();
	}
	@Override
	public Control getControl() {
		if (fMainControl == null) {
			return null;
		}
		return fMainControl;
	}

	@Override
	public ISelection getSelection() {
		if (fTreeViewer == null) {
			return StructuredSelection.EMPTY;
		}
		return fTreeViewer.getSelection();
	}

	@Override
	public TreeViewer getTreeViewer() {
		return fTreeViewer;
	}
	
	@Override
	public void setSelection(ISelection selection) {
		if (fTreeViewer != null) {
			fTreeViewer.setSelection(selection);
		}
	}

	@Override
	public void setFocus() {
		getControl().setFocus();
	}
	
	/**
	 * Gets the content outline for a given input element. Returns the outline
	 * or null if the outline could not be generated.
	 * 
	 * @param input
	 * 
	 * @return
	 */
	private IAdaptable getContentOutline(List<DocumentNode> nodes) {
		if(scanner == null) {
			return null;
		}
		
		return new CommonOutlineElementList(getSyntacticElements(nodes));
	}
	private IAdaptable getContentOutline() {
		if(scanner == null) {
			return null;
		}
		List<DocumentNode> nodes = scanner.buildNodes(document);
		return getContentOutline(nodes);
	}
	protected List getSyntacticElements(List<DocumentNode> nodes) {
		
		if(previousNodes != null && previousNodes == nodes) {
			return elementList;
		}
		previousNodes = nodes;
		
		elementList = new LinkedList();
		
		DocumentNode previousItem = null;
		
		Iterator it = nodes.iterator();
		while (it.hasNext()) {
			DocumentNode item = (DocumentNode) it.next();
			
			int offset = item.getStart();
			int length = item.getLength();
			String expression = getExpression(offset, length);
			
			Object object = processToken(item, previousItem, expression, offset, length);
			if(object != null) {
				elementList.add(object);
			}
			
			//System.out.println(item);
			previousItem = item;
		}
		

		return elementList;
	}


	protected Object processToken(DocumentNode node, DocumentNode previousNode, String expression, int offset, int length) {
		return null;
	}
	
	protected DocumentTokenBuilder getScanner() {
		return null;
	}
	
	/**
	 * Forces the outlinePage to update its contents.
	 * 
	 */
	public void update(List<DocumentNode> nodes) {
		CommonOutlineElementList currentNodes = (CommonOutlineElementList) getContentOutline(nodes);
		update(currentNodes);
	}
	public void update() {
		CommonOutlineElementList currentNodes = (CommonOutlineElementList) getContentOutline();
		update(currentNodes);
	}
	
	public void update(CommonOutlineElementList currentNodes) {
		if(getControl() != null) {
			getControl().setRedraw(false);
		}
		TreeViewer viewer = getTreeViewer();

		Object[] expanded = viewer.getExpandedElements();
		
		viewer.setInput(currentNodes);

		// How about just expanding the root if it's alone?
		if (currentNodes.size() == 1) {
			viewer.expandAll();
		}

		// Attempt to determine which nodes are already expanded bearing in mind
		// that the object is not the same.
		for (int i = 0; i < expanded.length; i++) {
			CommonOutlineElement newExpandedNode = currentNodes
					.findEquivilent((CommonOutlineElement) expanded[i]);
			if (newExpandedNode != null) {
				viewer.setExpandedState(newExpandedNode, true);
			}
		}

		if(getControl() != null) {
			getControl().setRedraw(true);
		}
	}

	protected String getExpression(int offset, int length) {
		String expression;
		try {
			expression = document.get(offset, length);// sourceBuffer.substring(offset,
															// offset + length);
		} catch (BadLocationException e) {
			expression = ""; //$NON-NLS-1$
		}
		return expression;
	}
	
	
}
