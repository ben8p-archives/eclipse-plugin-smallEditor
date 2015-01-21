package smalleditor.common;

import java.util.HashMap;
import java.util.List;

public interface IContentAssistProvider {

	public List<HashMap> suggest(String context);

}