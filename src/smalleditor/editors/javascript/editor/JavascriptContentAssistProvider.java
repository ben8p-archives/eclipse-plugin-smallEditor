package smalleditor.editors.javascript.editor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import smalleditor.editors.common.editor.CommonContentAssistProvider;

public class JavascriptContentAssistProvider extends CommonContentAssistProvider{

	private static String DOJO_LANG = "lang.";
	private static String[] DOJO_LANG_KEYWORDS = {
		"lang.hitch(this, function() {})",
		"lang.hitch(this, 'method')"
	};
	private static String DOJO_COMMENTS = "summary";
	private static String[] DOJO_COMMENTS_KEYWORDS = {
		"// summary: \n//\t\t...\n// tags://\t\tpublic"
	};
	private static String DOJO_THIS = "this.";
	private static String[] DOJO_THIS_KEYWORDS = {
		"this.inherited(arguments);"
	};
	
	@Override
	public List<String> suggest(String context, String indentation) {
		ArrayList<String> buffer = new ArrayList<String>();
		
		String[] keywords = {};
		
		if(DOJO_LANG.startsWith(context)) {
			keywords = concat(keywords, DOJO_LANG_KEYWORDS);
		}
		if(DOJO_COMMENTS.startsWith(context)) {
			keywords = concat(keywords, DOJO_COMMENTS_KEYWORDS);
		}
		
		if(DOJO_THIS.startsWith(context)) {
			keywords = concat(keywords, DOJO_THIS_KEYWORDS);
		}
		
		for (String s : keywords) {
			buffer.add(s.replace("\n", "\n" + indentation));
		}
		Collections.sort(buffer);
		return buffer;
	}

}