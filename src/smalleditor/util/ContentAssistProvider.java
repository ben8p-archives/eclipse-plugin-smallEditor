package smalleditor.util;

import java.util.List;

public interface ContentAssistProvider {

	public String[] concat(String[]...arrays);
	public List<String> suggest(String context, String indentation);

}