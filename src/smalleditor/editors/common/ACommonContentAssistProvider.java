package smalleditor.editors.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import smalleditor.common.IContentAssistProvider;

public abstract class ACommonContentAssistProvider implements IContentAssistProvider{
	protected HashMap elementsList = null;
	
	@Override
	public List<HashMap> suggest(String context) {
		ArrayList<HashMap> buffer = new ArrayList<HashMap>();
		
		
		if(elementsList != null) {
			Iterator it = elementsList.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pairs = (Map.Entry)it.next();
				String key = pairs.getKey().toString();

				if((context.isEmpty() == false && key.startsWith(context)) || key.isEmpty() == true) { 
					HashMap value = (HashMap) pairs.getValue();
					ArrayList<HashMap> fixedValue;
					fixedValue = fixupList(context, (ArrayList) value.get("values"), (Boolean) value.get("append-value")); //$NON-NLS-1$ //$NON-NLS-2$
					buffer.addAll(fixedValue);
				}
				//it.remove();
			}
	
			//Collections.sort(buffer);
		}
		return buffer;
	}
	
	protected ArrayList<HashMap> fixupList(String context, ArrayList<String> list, Boolean appendValue) {
		ArrayList<HashMap> fixedList = new ArrayList<HashMap>();
		for (String s : list) {
			if(s.startsWith(context) == false) { continue; }
			HashMap hash = new HashMap();
			hash.put("append-value", appendValue); //$NON-NLS-1$
			hash.put("value", s); //$NON-NLS-1$
			fixedList.add(hash);
		}
		
		return fixedList;
	}

}