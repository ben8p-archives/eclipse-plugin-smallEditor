package smalleditor.editors.css.editor;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.TextPresentation;
import org.eclipse.jface.text.presentation.IPresentationRepairer;
import org.eclipse.swt.graphics.Color;

import smalleditor.editors.common.editor.CommonNonRuleBasedDamagerRepairer;
import smalleditor.preference.ColorManager;

/**
 * 
 * 
 * @author $Author: agfitzp $, $Date: 2003/05/28 15:17:11 $
 * 
 * @version $Revision: 1.1 $
 */
public class CssColorDamagerRepairer extends CommonNonRuleBasedDamagerRepairer {
	private ColorManager colorManager = new ColorManager();

	/**
	 * @see IPresentationRepairer#createPresentation(TextPresentation,
	 *      ITypedRegion)
	 */
	@Override
	public void createPresentation(TextPresentation presentation,
			ITypedRegion region) {
		if(region.getLength() < 7) { return; }
		String hexa = "";
		try {
			hexa = fDocument.get(region.getOffset(), region.getLength());
		} catch (BadLocationException e) {
			return;
		}
		Color backColor = colorManager.getColor(hexa);
		Color fontColor = colorManager.getContrastColor(backColor);
		fDefaultTextAttribute = new TextAttribute(fontColor, backColor, 0);
		
		addRange(presentation, region.getOffset(), region.getLength(),
				fDefaultTextAttribute);
	}
}