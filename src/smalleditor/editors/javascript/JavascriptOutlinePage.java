/*
 * JavascriptContentOutlinePage.java	Created on 8 Jan 2015
 * 
 * Copyright © 2015 ING Group. All rights reserved.
 * 
 * This software is the confidential and proprietary information of 
 * ING Group ("Confidential Information"). 
 */
package smalleditor.editors.javascript;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.IActionBars;

import smalleditor.Activator;
import smalleditor.editors.common.ACommonOutlinePage;
import smalleditor.editors.common.outline.CommonOutlineElementList;
import smalleditor.editors.common.parsing.AOutlineBuilder;
import smalleditor.editors.javascript.parsing.JavascriptOutlineBuilder;
import smalleditor.nls.Messages;
import smalleditor.preferences.IPreferenceNames;
import smalleditor.utils.TextUtility;

public class JavascriptOutlinePage extends ACommonOutlinePage {
	public static final String FUNCTION = "function";
	public static final String LINE_SEPARATOR = System.getProperty("line.separator");
	private Boolean bIsAnonymousEnabled = null;
	private AOutlineBuilder outlineBuilder = new JavascriptOutlineBuilder();
	
	private class AnonymousAction extends Action {
		private static final String ICON_PATH = "res/icons/anonymous_js.gif"; //$NON-NLS-1$

		public AnonymousAction() {
			setText(Messages.getString("Outline.Anonymous"));
			setToolTipText(Messages.getString("Outline.AnonymousInfo"));
			setDescription(Messages.getString("Outline.AnonymousInfo"));
			
			ImageDescriptor sortImage = ImageDescriptor.createFromURL(
					FileLocator.find(Activator.getDefault().getBundle(),
					new Path(ICON_PATH),null));
			
			setImageDescriptor(sortImage);

			setChecked(isAnonymousEnabled());
		}

		public void run() {
			getPreferenceStore().setValue(IPreferenceNames.P_SHOW_ANONYMOUS_JS_OUTLINE,
					isChecked());
		}
	}
	
	
	private boolean isAnonymousEnabled() {
		if(bIsAnonymousEnabled == null) {
			bIsAnonymousEnabled = getPreferenceStore().getBoolean(
					IPreferenceNames.P_SHOW_ANONYMOUS_JS_OUTLINE);
		}
		return bIsAnonymousEnabled;
	}
	@Override
	protected void registerActions(IActionBars actionBars) {
		super.registerActions(actionBars);
		IToolBarManager toolBarManager = actionBars.getToolBarManager();
		if (toolBarManager != null) {
			toolBarManager.add(new AnonymousAction());
		}
	}
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		String property = event.getProperty();

		if (property
				.equals(IPreferenceNames.P_SHOW_ANONYMOUS_JS_OUTLINE)) {
			bIsAnonymousEnabled = Boolean.parseBoolean(TextUtility.getStringValue(event
					.getNewValue()));
			update();
		} else {
			super.propertyChange(event);
		}
	}

	@Override
	protected CommonOutlineElementList getNodes() {
		return outlineBuilder.buildOutline(getDocument());
	}

	
//	/**
//	 * Method getNaked.
//	 * @param funcName
//	 */
//	private String getNaked(String funcName) throws StringIndexOutOfBoundsException {
//		if (funcName == null) {
//			return null;
//		}
//
//		funcName = funcName.trim().substring(FUNCTION.length()).trim();
//		funcName = TextUtility.replace(funcName.trim(), LINE_SEPARATOR, TextUtility.EMPTY_STRING);
//
//		StringBuffer strBuf = new StringBuffer(TextUtility.EMPTY_STRING);
//		int len = funcName.length();
//		boolean wasSpace = false;
//		for (int i = 0; i < len; i++) {
//			char ch = funcName.charAt(i);
//			if (ch == ' ') {
//				wasSpace = true;
//			} else // not space
//			{
//				if (wasSpace) {
//					strBuf.append(' ');
//				}
//				strBuf.append(ch);
//				wasSpace = false;
//			}
//		}
//		return strBuf.toString();
//	}
	
}
