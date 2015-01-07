package smalleditor.linters.javascript.marker;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

public class MarkerAdapter {

	private static final String TYPE_PROBLEM = "smallEditor.javascriptlintermarkerproblem";
	private final IResource resource;

	public MarkerAdapter(IResource resource) {
		this.resource = resource;
	}

	// public MarkerAdapter(IDocument resource) {
	// this.resource = (IResource) resource;
	// }

	public void removeMarkers() throws CoreException {
		resource.deleteMarkers(TYPE_PROBLEM, true, IResource.DEPTH_INFINITE);
	}

	public IMarker createMarker(int line, int start, int end, String message,
			String code) throws CoreException {
		if (message == null) {
			throw new NullPointerException("message is null");
		}
		IMarker marker = resource.createMarker(TYPE_PROBLEM);
		marker.setAttribute(IMarker.SEVERITY, new Integer(
				"ERROR".equals(code) ? IMarker.SEVERITY_ERROR
						: IMarker.SEVERITY_WARNING));
		marker.setAttribute(IMarker.MESSAGE, message);
		if (line >= 1) {
			// needed to display line number in problems view location column
			marker.setAttribute(IMarker.LINE_NUMBER, line);
		}
		if (start >= 0) {
			marker.setAttribute(IMarker.CHAR_START, new Integer(start));
			marker.setAttribute(IMarker.CHAR_END, new Integer(
					end >= start ? end : start));
		}
		return marker;
	}

}
