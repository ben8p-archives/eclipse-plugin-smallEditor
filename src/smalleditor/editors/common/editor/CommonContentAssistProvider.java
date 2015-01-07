package smalleditor.editors.common.editor;

import java.util.ArrayList;
import java.util.List;

import smalleditor.util.ContentAssistProvider;

public class CommonContentAssistProvider implements ContentAssistProvider{

	@Override
	public List<String> suggest(String context, String indentation) {
		ArrayList<String> buffer = new ArrayList<String>();
		return buffer;
	}

	@Override
	public String[] concat(String[]...arrays) {
		int len = 0;
		for (final String[] array : arrays) {
			len += array.length;
		}

		final String[] result = new String[len];

		int currentPos = 0;
		for (final String[] array : arrays) {
			System.arraycopy(array, 0, result, currentPos, array.length);
			currentPos += array.length;
		}

		return result;
	}

}