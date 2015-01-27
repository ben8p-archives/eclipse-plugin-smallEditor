/*
 * ResourceReader.java	Created on 9 Jan 2015
 * 
 * Copyright © 2015 ING Group. All rights reserved.
 * 
 * This software is the confidential and proprietary information of 
 * ING Group ("Confidential Information"). 
 */
package smalleditor.util.xml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ResourceReader {
	public static HashMap read(InputStream file) {
		HashMap elementMap = new HashMap();
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(true);
		factory.setIgnoringElementContentWhitespace(true);
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			
			Document doc = builder.parse(file);
			
			NodeList elements = doc.getElementsByTagName("element");
			for (int i = 0; i < elements.getLength(); ++i) {
				Element element = (Element) elements.item(i);
				String elementName = element.getAttribute("name");
				String appendValue = element.getAttribute("append-value");
				Boolean doAppend = false;
				if(appendValue != null) {
					doAppend = appendValue.toLowerCase().equals("true");
				}

				HashMap elementValueMap = new HashMap();
				elementValueMap.put("name", elementName);
				elementValueMap.put("append-value", doAppend);
				
				NodeList valueList = element.getElementsByTagName("value");
				List<String> values = new ArrayList<String>();
				for (int j = 0; j < valueList.getLength(); ++j) {
					Element value = (Element) valueList.item(j);
					String valueString = value.getFirstChild().getNodeValue();
					valueString = valueString.replaceAll("\\\\n", "\n").replaceAll("\\\\r", "\r").replaceAll("\\\\t", "\t");
					values.add(valueString);
				}
				elementValueMap.put("values", values);
				
				elementMap.put(elementName, elementValueMap);
			}
			
			return elementMap;
		} catch (Exception e) {
		}
		return null;
	}
}