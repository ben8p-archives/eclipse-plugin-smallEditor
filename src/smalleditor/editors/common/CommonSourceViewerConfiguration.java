package smalleditor.editors.common;

import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.reconciler.IReconciler;
import org.eclipse.jface.text.reconciler.IReconcilingStrategy;
import org.eclipse.jface.text.reconciler.MonoReconciler;
import org.eclipse.jface.text.source.DefaultAnnotationHover;
import org.eclipse.jface.text.source.IAnnotationHover;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

import smalleditor.preferences.ColorManager;

public class CommonSourceViewerConfiguration extends SourceViewerConfiguration {
	private CommonDoubleClickStrategy doubleClickStrategy;
	protected ColorManager colorManager;
	protected CommonEditor editor;
	protected CommonSourceConfiguration sourceConfiguration = null;
	
	
	public CommonSourceViewerConfiguration(CommonEditor editor) {
		this.colorManager = new ColorManager();
		this.editor = editor;
	}
	
	protected IReconcilingStrategy getReconcilingStrategy(CommonEditor editor) {
		return new CommonReconcilingStrategy(this.editor);
	}
	
	@Override
	public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
		ContentAssistant assistant = new ContentAssistant();
		
		String[] contentTypes = getConfiguredContentTypes(sourceViewer);

		for (String type : contentTypes) {
//			IContentAssistProcessor processor = getContentAssistProcessor(sourceViewer, type);
			IContentAssistProcessor sharedProcessor = sourceConfiguration.getContentAssistProcessor(this.editor, type);

			if (sharedProcessor != null) {
				assistant.setContentAssistProcessor(sharedProcessor, type);
			}
		}
		
		
				
//		assistant.setContentAssistProcessor(sharedProcessor, IDocument.DEFAULT_CONTENT_TYPE);

		assistant.enableAutoActivation(true);
		assistant.setAutoActivationDelay(250);
		
		return assistant;
	}
	
	@Override
	public IPresentationReconciler getPresentationReconciler(
			ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();

		sourceConfiguration.setupPresentationReconciler(reconciler, sourceViewer);
		
		return reconciler;
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
		return sourceConfiguration.getContentTypes();
	}

	public ITextDoubleClickStrategy getDoubleClickStrategy(
			ISourceViewer sourceViewer, String contentType) {
		if (doubleClickStrategy == null) {
			doubleClickStrategy = new CommonDoubleClickStrategy();
		}

		return doubleClickStrategy;
	}
	
	

}