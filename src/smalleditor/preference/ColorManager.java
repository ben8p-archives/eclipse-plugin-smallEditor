package smalleditor.preference;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import smalleditor.Activator;

public class ColorManager {
	protected Map fColorTable = new HashMap(10);

	public Color getContrastColor(Color color) {
		
		int red = color.getRed();
		int green = color.getGreen();
		int blue = color.getBlue();
		int yiq = ((red * 299) + (green * 587) + (blue * 114)) / 1000;
		if(yiq >= 128) {
			return new Color(Display.getCurrent(), 0, 0, 0);
		}
		return new Color(Display.getCurrent(), 255, 255, 255);
	}
	
	public Color getColor(RGB rgb) {
		Color color = (Color) fColorTable.get(rgb);

		if (color == null) {
			color = new Color(Display.getCurrent(), rgb);
			fColorTable.put(rgb, color);
		}

		return color;
	}
	
	public Color getColor(String hexa) {
		RGB rgb = new RGB(Integer.valueOf( hexa.substring( 1, 3 ), 16 ),
							Integer.valueOf( hexa.substring( 3, 5 ), 16 ),
							Integer.valueOf( hexa.substring( 5, 7 ), 16 ));
		Color color = getColor(rgb);
		return color;
	}
	
	public Color getColorFromPreferencesKey(String categoryColor) {
		IPreferenceStore preferences = Activator.getDefault().getPreferenceStore();
		
		String rgbString = preferences.getString(categoryColor);

		if (rgbString.length() <= 0) {
			rgbString = preferences.getDefaultString(categoryColor);
			if (rgbString.length() <= 0) {
				rgbString = "0,0,0";
			}
		}
		return getColor(StringConverter.asRGB(rgbString));
	}
}