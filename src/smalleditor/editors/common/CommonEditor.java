package smalleditor.editors.common;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.DefaultCharacterPairMatcher;
import org.eclipse.jface.text.source.ICharacterPairMatcher;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.projection.ProjectionAnnotation;
import org.eclipse.jface.text.source.projection.ProjectionAnnotationModel;
import org.eclipse.jface.text.source.projection.ProjectionSupport;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.ChainedPreferenceStore;
import org.eclipse.ui.texteditor.SourceViewerDecorationSupport;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

import smalleditor.Activator;
import smalleditor.preferences.PreferenceNames;

public class CommonEditor extends TextEditor implements ISelectionChangedListener {
	protected ProjectionSupport projectionSupport;
	protected ProjectionAnnotationModel annotationModel;
	protected CommonOutlinePage outlinePage;

	private ProjectionAnnotation[] oldAnnotations;
	private boolean[] annotationCollapsedState;
	protected boolean updatingContentDependentActions = false;

	public CommonEditor() {
		super();

		// merge the preferences
		IPreferenceStore[] stores = { getPreferenceStore(),
				Activator.getDefault().getPreferenceStore() };
		setPreferenceStore(new ChainedPreferenceStore(stores));

	}
	
	public void doSave(IProgressMonitor monitor) {
		super.doSave(monitor);
		if (outlinePage != null) {
			outlinePage.update();
		}
	}

	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		ProjectionViewer viewer = (ProjectionViewer) getSourceViewer();

		projectionSupport = new ProjectionSupport(viewer,
				getAnnotationAccess(), getSharedColors());
		projectionSupport.install();

		// turn projection mode on
		viewer.doOperation(ProjectionViewer.TOGGLE);

		annotationModel = viewer.getProjectionAnnotationModel();
	}

	protected ISourceViewer createSourceViewer(Composite parent,
			IVerticalRuler ruler, int styles) {
		ISourceViewer viewer = new ProjectionViewer(parent, ruler,
				getOverviewRuler(), isOverviewRulerVisible(), styles);

		// ensure decoration support has been created and configured.
		getSourceViewerDecorationSupport(viewer);

		return viewer;
	}
	
	public Object getAdapter(Class key) {
		if (key.equals(IContentOutlinePage.class)) {
			IDocument document = getDocumentProvider().getDocument(getEditorInput());

			outlinePage = getOutlinePage(document);
			if(outlinePage != null) {
				outlinePage.addSelectionChangedListener(this);
				return outlinePage;
			}
		}

		return super.getAdapter(key);
	}
	
	protected CommonOutlinePage getOutlinePage(IDocument document) {
		return null;
	}
	/**
	 * Updates all content dependent actions.
	 * 
	 * This might be a hack: We're trapping this update to ensure that the 
	 * outline is always up to date.
	 */
	protected void updateContentDependentActions() {
		super.updateContentDependentActions();

		if (!updatingContentDependentActions && outlinePage != null) {
				updatingContentDependentActions = true;
				outlinePage.update();
				updatingContentDependentActions = false;
		}
	}
	
	@Override
	protected void configureSourceViewerDecorationSupport(
			SourceViewerDecorationSupport support) {
		super.configureSourceViewerDecorationSupport(support);

		char[] matchChars = getMatchingBrackets(); // which brackets to match
		if(matchChars != null) {
			ICharacterPairMatcher matcher = new DefaultCharacterPairMatcher(
					matchChars, IDocumentExtension3.DEFAULT_PARTITIONING);
			support.setCharacterPairMatcher(matcher);
			support.setMatchingCharacterPainterPreferenceKeys(
					PreferenceNames.P_SHOW_MATCHING_BRACKETS,
					PreferenceNames.P_MATCHING_BRACKETS_COLOR);
		}

	}
	

	protected void handleCursorPositionChanged() {
		super.handleCursorPositionChanged();

		markOccurrences();
	}

	protected char[] getMatchingBrackets() {
		// return the brckets to match
		char[] empty = {};
		return empty;
	}

	public void updateFoldingStructure(List<Position> positions) {
		ProjectionAnnotation[] annotations = new ProjectionAnnotation[positions
				.size()];

		// this will hold the new annotations along
		// with their corresponding positions
		HashMap<ProjectionAnnotation, Position> newAnnotations = new HashMap<ProjectionAnnotation, Position>();

		for (int i = 0; i < positions.size(); i++) {
			ProjectionAnnotation annotation = new ProjectionAnnotation();
			newAnnotations.put(annotation, positions.get(i));
			annotations[i] = annotation;
			if (annotationCollapsedState != null
					&& annotationCollapsedState.length > i
					&& annotationCollapsedState[i]) {
				annotation.markCollapsed();
			}
		}

		annotationModel.modifyAnnotations(oldAnnotations, newAnnotations, null);

		oldAnnotations = annotations;
	}

	public void updateTask(List<Position> positions) {
		IDocument document = this.getDocumentProvider().getDocument(
				this.getEditorInput());
		IFile file = ((FileEditorInput) this.getEditorInput()).getFile();

		IMarker marker;

		try {
			file.deleteMarkers(IMarker.TASK, true, IResource.DEPTH_INFINITE);

			for (int i = 0; i < positions.size(); i++) {
				Position position = positions.get(i);

				marker = file.createMarker(IMarker.TASK);
				marker.setAttribute(IMarker.LINE_NUMBER,
						document.getLineOfOffset(position.getOffset()) + 1);
				marker.setAttribute(IMarker.MESSAGE, document.get(
						position.getOffset(), position.getLength()));

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void markOccurrences() {
		IDocument document = this.getDocumentProvider().getDocument(
				this.getEditorInput());
		IFile file = ((FileEditorInput) this.getEditorInput()).getFile();
		ISelection selection = getSelectionProvider().getSelection();
		String markerType = "slicemarker";

		// cleanup old annotations
		try {
			file.deleteMarkers(markerType, true, IResource.DEPTH_INFINITE);
		} catch (CoreException e1) {
		}

		// if it is disabled, we get out
		Boolean markOccurences = getPreferenceStore().getBoolean(
				PreferenceNames.P_MARK_OCCURENCES);
		if (!markOccurences || !(selection instanceof ITextSelection)) {
			return;
		}

		ITextSelection textSelection = (ITextSelection) selection;
		// only react when cursor move, not when selected
		if (textSelection.getLength() > 0) {
			return;
		}

		try {
			int lineOffset = document.getLineOffset(textSelection
					.getStartLine());
			int caretOffset = textSelection.getOffset() - lineOffset;
			String line = document.get(lineOffset,
					document.getLineLength(textSelection.getStartLine()));
			String[] words = line.split("[^\\w]");
			int offset = 0;
			String word = null;
			List<Position> positions = new LinkedList<Position>();

			// get word under caret
			for (int i = 0; i < words.length; i++) {
				offset += words[i].length() + 1; // 1 for the splittted
													// char
				if (offset > caretOffset) {
					// System.out.println("word under carret is:" + words[i]);
					word = words[i];
					break;
				}
			}
			if (word != null && !word.isEmpty()) {
				// get all the positions of that word

				Pattern p = Pattern.compile("\\b" + word + "\\b");
				Matcher m = p.matcher(document.get());
				while (m.find()) {
					Position position = new Position(m.start());
					position.setLength(word.length());
					positions.add(position);
				}
			}
			if (positions.size() > 0) {
				// create markers
				IMarker marker;

				for (int index = 0; index < positions.size(); index++) {
					Position position = positions.get(index);
					marker = file.createMarker(markerType);
					marker.setAttribute(IMarker.CHAR_START,
							position.getOffset());
					marker.setAttribute(IMarker.CHAR_END, position.getOffset()
							+ position.getLength());

				}
			}
		} catch (Exception e) {
		}
	}

	/**
	 * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(SelectionChangedEvent)
	 */
	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		if (null != event) {
			if (event.getSelection() instanceof IStructuredSelection) {
				IStructuredSelection sel = (IStructuredSelection) event.getSelection();
				if (null != sel) {
					CommonOutlineElement fe = (CommonOutlineElement) sel.getFirstElement();
					if (null != fe) {
						selectAndReveal(fe.getStart(), fe.getLength());
					}
				}
			}
		}
	}
}
