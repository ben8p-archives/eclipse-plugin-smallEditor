package smalleditor.editors.common.editor;

import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.reconciler.IReconciler;
import org.eclipse.jface.text.reconciler.IReconcilingStrategy;
import org.eclipse.jface.text.reconciler.MonoReconciler;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.DefaultAnnotationHover;
import org.eclipse.jface.text.source.IAnnotationHover;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.graphics.Color;

import smalleditor.preference.ColorManager;
import smalleditor.preference.PreferenceNames;

public class CommonSourceViewerConfiguration extends SourceViewerConfiguration {
	private CommonDoubleClickStrategy doubleClickStrategy;
	protected ColorManager colorManager;
	protected CommonEditor editor;
	private CommonScanner scanner;
	
	public CommonSourceViewerConfiguration(CommonEditor editor) {
		this.colorManager = new ColorManager();
		this.editor = editor;
	}
	
	protected IReconcilingStrategy getReconcilingStrategy(CommonEditor editor) {
		return new CommonReconcilingStrategy(this.editor);
	}
	
	@Override
	public IReconciler getReconciler(ISourceViewer sourceViewer){
		MonoReconciler reconciler = new MonoReconciler(getReconcilingStrategy(this.editor), false);
		return reconciler;
	}
	@Override
	public IAnnotationHover getAnnotationHover(ISourceViewer sourceViewer) {
	    return new DefaultAnnotationHover();
	}

	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return new String[] { };
	}

	public ITextDoubleClickStrategy getDoubleClickStrategy(
			ISourceViewer sourceViewer, String contentType) {
		if (doubleClickStrategy == null) {
			doubleClickStrategy = new CommonDoubleClickStrategy();
		}

		return doubleClickStrategy;
	}
	
	protected CommonScanner getScanner() {
		if (scanner == null) {
			Color defaultColor = colorManager.getColorFromPreferencesKey(PreferenceNames.P_DEFAULT_COLOR);
			scanner = new CommonScanner(defaultColor);
			scanner.setDefaultReturnToken(new Token(new TextAttribute(
					defaultColor)));
		}

		return scanner;
	}

}