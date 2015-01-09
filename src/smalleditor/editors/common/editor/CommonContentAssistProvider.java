package smalleditor.editors.common.editor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import smalleditor.util.ContentAssistProvider;

public class CommonContentAssistProvider implements ContentAssistProvider{
	protected HashMap elementsList = null;
	
	@Override
	public List<HashMap> suggest(String context) {
		ArrayList<HashMap> buffer = new ArrayList<HashMap>();
		
		
		if(elementsList != null) {
			Iterator it = elementsList.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pairs = (Map.Entry)it.next();

				if(pairs.getKey().toString().startsWith(context) || pairs.getKey().toString().equals("all")) {
					HashMap value = (HashMap) pairs.getValue();
					ArrayList<HashMap> fixedValue = fixupList((ArrayList) value.get("values"), (Boolean) value.get("append-value"));
					buffer.addAll(fixedValue);
				}
				//it.remove();
			}
	
			//Collections.sort(buffer);
		}
		return buffer;
	}
	
	protected ArrayList<HashMap> fixupList(ArrayList<String> list, Boolean appendValue) {
		ArrayList<HashMap> fixedList = new ArrayList<HashMap>();
		for (String s : list) {
			HashMap hash = new HashMap();
			hash.put("append-value", appendValue);
			hash.put("value", s);
			fixedList.add(hash);
		}
		
		return fixedList;
	}

}