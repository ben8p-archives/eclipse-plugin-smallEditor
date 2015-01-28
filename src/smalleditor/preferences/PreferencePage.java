package smalleditor.preferences;

import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;
import smalleditor.Activator;
import smalleditor.nls.Messages;

/**
 * This class represents a preference page that is contributed to the
 * Preferences dialog. By subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows us to create a page
 * that is small and knows how to save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They are stored in the
 * preference store that belongs to the main plug-in class. That way,
 * preferences can be accessed directly via the preference store.
 */

public class PreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage, IPreferenceNames {

	public PreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription(Messages.getString("Preferences.Description"));
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and restore itself.
	 */
	
	public void createFieldEditors() {
		addField(new ColorFieldEditor(P_COMMENT_COLOR, Messages.getString("Preferences.CommentColor"),
				getFieldEditorParent()));
		addField(new ColorFieldEditor(P_STRING_COLOR, Messages.getString("Preferences.StringColor"),
				getFieldEditorParent()));
		addField(new ColorFieldEditor(P_KEYWORD_COLOR, Messages.getString("Preferences.KeywordColor"),
				getFieldEditorParent()));
		addField(new ColorFieldEditor(P_DEFAULT_COLOR, Messages.getString("Preferences.DefaultColor"),
				getFieldEditorParent()));
		
		addField(new ColorFieldEditor(P_MATCHING_BRACKETS_COLOR, Messages.getString("Preferences.ColorMatching"),
				getFieldEditorParent()));
		
		addField(new BooleanFieldEditor(P_SHOW_MATCHING_BRACKETS, Messages.getString("Preferences.ShowMatching"), getFieldEditorParent()));
		addField(new BooleanFieldEditor(P_MARK_OCCURENCES, Messages.getString("Preferences.MarkOccurences"), getFieldEditorParent()));
		addField(new BooleanFieldEditor(P_TRAILING_SPACE, Messages.getString("Preferences.TrailingSpaces"), getFieldEditorParent()));
		
		addField(new BooleanFieldEditor(P_LINT_CODE, Messages.getString("Preferences.Linters"), getFieldEditorParent()));
		
//		addField(new ColorFieldEditor(P_MARK_OCCURENCES_COLOR, "Color for matching occurences:",
//				getFieldEditorParent()));
	}

	public void init(IWorkbench workbench) {
	}
}