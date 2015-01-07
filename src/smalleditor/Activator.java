package smalleditor;

import org.eclipse.ui.plugin.*;
//import org.eclipse.core.runtime.*;
import org.eclipse.core.resources.*;
import org.eclipse.jface.preference.IPreferenceStore;

import smalleditor.preference.PreferenceNames;

import java.util.*;

public class Activator extends AbstractUIPlugin {
	// The shared instance.
	private static Activator plugin;

	// Resource bundle.
//	private ResourceBundle resourceBundle;

	private boolean defaultsInitialized = false;

	private List currentFunctions = new LinkedList();

	public Activator() {
		super();
		plugin = this;

//		try {
//			resourceBundle = ResourceBundle
//					.getBundle("smalleditor.editors.javascript.EditorPluginResources");
//		} catch (MissingResourceException x) {
//			System.out.println("Activator MissingResourceException");
//			x.printStackTrace();
//			resourceBundle = null;
//		}
	}

	public static Activator getDefault() {
		return plugin;
	}

	public static IWorkspace getWorkspace() {
		return ResourcesPlugin.getWorkspace();
	}

//	public static String getResourceString(String key) {
//		ResourceBundle bundle = Activator.getDefault().getResourceBundle();
//
//		try {
//			return (bundle != null) ? bundle.getString(key) : key;
//		} catch (MissingResourceException e) {
//			System.out.println("getResourceString MissingResourceException");
//			e.printStackTrace();
//			return key;
//		}
//	}

//	public ResourceBundle getResourceBundle() {
//		return resourceBundle;
//	}

	public List getCurrentFunctions() {
		return currentFunctions;
	}

	public void setCurrentFunctions(List currentFunctions) {
		this.currentFunctions = currentFunctions;
	}

	public IPreferenceStore getPreferenceStore() {
		IPreferenceStore store = super.getPreferenceStore();

		if (!defaultsInitialized) {
			initializeDefaults(store);
		}
		return store;
	}

	private void initializeDefaults(IPreferenceStore store) {
		store.setDefault(PreferenceNames.P_COMMENT_COLOR, "63,127,95");
		store.setDefault(PreferenceNames.P_STRING_COLOR, "42,0,255");
		store.setDefault(PreferenceNames.P_KEYWORD_COLOR, "127,0,85");
		store.setDefault(PreferenceNames.P_DEFAULT_COLOR, "0,0,0");
		store.setDefault(PreferenceNames.P_SHOW_MATCHING_BRACKETS, true);
		store.setDefault(PreferenceNames.P_MATCHING_BRACKETS_COLOR, "128,128,128");
		
		store.setDefault(PreferenceNames.P_MARK_OCCURENCES, true);
//		store.setDefault(PreferenceNames.P_MARK_OCCURENCES_COLOR, "128,128,128");

		this.defaultsInitialized = true;
	}
}