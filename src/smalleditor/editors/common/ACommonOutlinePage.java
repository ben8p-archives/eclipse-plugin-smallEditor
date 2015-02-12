package smalleditor.editors.common;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
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
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.dialogs.PatternFilter;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.progress.WorkbenchJob;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

import smalleditor.Activator;
import smalleditor.editors.common.outline.ACommonOutlineElement;
import smalleditor.editors.common.outline.CommonOutlineElementList;
import smalleditor.editors.common.outline.CommonOutlineNameSorter;
import smalleditor.nls.Messages;
import smalleditor.preferences.IPreferenceNames;
import smalleditor.utils.TextUtility;

public abstract class ACommonOutlinePage extends ContentOutlinePage implements IPropertyChangeListener {
	private Composite fMainControl;
	private Text fSearchBox;
	private TreeViewer fTreeViewer;
	private PatternFilter fFilter;
	private static final int FILTER_REFRESH_DELAY = 200;
	private WorkbenchJob fFilterRefreshJob;
	
	private IPreferenceStore fPref = null;
	
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
	
	private class SortingAction extends Action {
		private static final String ICON_PATH = "res/icons/sort_alphab.gif"; //$NON-NLS-1$

		public SortingAction() {
			setText(Messages.getString("Outline.Sort"));
			setToolTipText(Messages.getString("Outline.SortInfo"));
			setDescription(Messages.getString("Outline.SortInfo"));
			
			ImageDescriptor sortImage = ImageDescriptor.createFromURL(
					FileLocator.find(Activator.getDefault().getBundle(),
					new Path(ICON_PATH),null));
			
			setImageDescriptor(sortImage);

			setChecked(isSortingEnabled());
		}

		public void run() {
			getPreferenceStore().setValue(IPreferenceNames.P_SORT_OUTLINE,
					isChecked());
		}
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
					fSearchBox.setText(TextUtility.EMPTY_STRING);
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
		viewer.setSorter(isSortingEnabled() ? new CommonOutlineNameSorter() : null);

		viewer.addFilter(fFilter);
		
		IActionBars actionBars = getSite().getActionBars();
		registerActions(actionBars);
		actionBars.updateActionBars();
		
		getPreferenceStore().addPropertyChangeListener(this);

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

	@Override
	public void dispose() {
		getPreferenceStore().removePropertyChangeListener(this);
		super.dispose();
	}
	
	public void propertyChange(PropertyChangeEvent event) {
		String property = event.getProperty();

		if (property
				.equals(IPreferenceNames.P_SORT_OUTLINE)) {
			boolean sort = Boolean.parseBoolean(TextUtility.getStringValue(event
					.getNewValue()));
			getTreeViewer().setComparator(sort ? new CommonOutlineNameSorter() : null);
		}
	}
	
	protected void registerActions(IActionBars actionBars) {
		IToolBarManager toolBarManager = actionBars.getToolBarManager();
		if (toolBarManager != null) {
			toolBarManager.add(new SortingAction());
		}
	}
	
	protected IPreferenceStore getPreferenceStore() {
		if(fPref == null) {
			fPref = Activator.getDefault().getPreferenceStore();
		}
		return fPref;
	}
	
	private boolean isSortingEnabled() {
		return getPreferenceStore().getBoolean(
				IPreferenceNames.P_SORT_OUTLINE);
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
		if(fTreeViewer == null) {
			System.err.println("getTreeViewer is null ??");
		}
		return fTreeViewer;
	}
	public void setRedraw(Boolean state) {
		if(getControl() != null) {
			getControl().setRedraw(state);
		}
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
	
	
	
	
	
	
	
	
	private IDocument document = null;
	public void setDocument(IDocument document) {
		this.document = document;
	}
	public IDocument getDocument() {
		return document;
	}
	protected abstract CommonOutlineElementList getNodes();
	
	public void update() {
		setRedraw(false);
		TreeViewer viewer = getTreeViewer();
		if(viewer == null) {
			return;
		}

		Object[] expanded = viewer.getExpandedElements();
		
		CommonOutlineElementList currentNodes = getNodes();
		if(currentNodes != null) {
			viewer.setInput(currentNodes);
	
			// How about just expanding the root if it's alone?
			if (currentNodes.size() == 1) {
				viewer.expandAll();
			}
	
			// Attempt to determine which nodes are already expanded bearing in mind
			// that the object is not the same.
			for (int i = 0; i < expanded.length; i++) {
				ACommonOutlineElement newExpandedNode = currentNodes
						.findEquivilent((ACommonOutlineElement) expanded[i]);
				if (newExpandedNode != null) {
					viewer.setExpandedState(newExpandedNode, true);
				}
			}
		}
		setRedraw(true);
	}

}
