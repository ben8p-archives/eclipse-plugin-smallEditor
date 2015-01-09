package smalleditor.util;

import java.util.HashMap;
import java.util.List;

public interface ContentAssistProvider {

	public List<HashMap> suggest(String context);

}