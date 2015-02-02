package smalleditor.editors.css;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.TextPresentation;
import org.eclipse.jface.text.presentation.IPresentationRepairer;
import org.eclipse.swt.graphics.Color;

import smalleditor.editors.common.CommonNonRuleBasedDamagerRepairer;
import smalleditor.preferences.ColorManager;
import smalleditor.utils.TextUtility;

/**
 * 
 * 
 * @author $Author: agfitzp $, $Date: 2003/05/28 15:17:11 $
 * 
 * @version $Revision: 1.1 $
 */
public class CssColorDamagerRepairer extends CommonNonRuleBasedDamagerRepairer {
	private ColorManager colorManager = ColorManager.getDefault();

	/**
	 * @see IPresentationRepairer#createPresentation(TextPresentation,
	 *      ITypedRegion)
	 */
	@Override
	public void createPresentation(TextPresentation presentation,
			ITypedRegion region) {
		if(region.getLength() < 7) { return; }
		String hexa = TextUtility.EMPTY_STRING; //$NON-NLS-1$
		try {
			hexa = fDocument.get(region.getOffset(), region.getLength());
		} catch (BadLocationException e) {
			e.printStackTrace();
			return;
		}
		Color backColor = colorManager.getColor(hexa);
		Color fontColor = colorManager.getContrastColor(backColor);
		fDefaultTextAttribute = new TextAttribute(fontColor, backColor, 0);
		
		addRange(presentation, region.getOffset(), region.getLength(),
				fDefaultTextAttribute);
	}
}