package smalleditor.editors.json.editor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import smalleditor.editors.common.editor.CommonContentAssistProvider;

public class JsonContentAssistProvider extends CommonContentAssistProvider{

	@Override
	public List<String> suggest(String context, String indentation) {
		ArrayList<String> buffer = new ArrayList<String>();
		
		String[] keywords = {
				" \"\"",
				" {}",
				" []"
		};
		
		for (String s : keywords) {
			buffer.add(context + s.replace("\n", "\n" + indentation));
		}
		Collections.sort(buffer);
		return buffer;
	}

}