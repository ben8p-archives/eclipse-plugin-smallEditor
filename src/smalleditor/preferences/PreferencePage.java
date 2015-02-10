package smalleditor.preferences;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.preference.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
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
	
	public static Composite createGroup(Composite appearanceComposite,
			String string) {
		Group group = new Group(appearanceComposite, SWT.NONE);
		group.setFont(appearanceComposite.getFont());
		group.setText(string);

		group.setLayout(GridLayoutFactory.fillDefaults().margins(5, 5)
				.numColumns(2).create());
		group.setLayoutData(GridDataFactory.fillDefaults().span(2, 0)
				.grab(true, false).create());

		Composite c = new Composite(group, SWT.NONE);
		c.setLayout(GridLayoutFactory.fillDefaults().numColumns(2).create());

		return c;
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and restore itself.
	 */
	
	public void createFieldEditors() {
		Composite appearanceComposite = getFieldEditorParent();
		
		Composite group = createGroup(appearanceComposite, Messages.getString("Preferences.CodeColors"));
		addField(new ColorFieldEditor(P_COMMENT_COLOR, Messages.getString("Preferences.CommentColor"),
				group));
		addField(new ColorFieldEditor(P_STRING_COLOR, Messages.getString("Preferences.StringColor"),
				group));
		addField(new ColorFieldEditor(P_KEYWORD_COLOR, Messages.getString("Preferences.KeywordColor"),
				group));
		addField(new ColorFieldEditor(P_DEFAULT_COLOR, Messages.getString("Preferences.DefaultColor"),
				group));
		
		group = createGroup(appearanceComposite, Messages.getString("Preferences.Brackets"));
		addField(new BooleanFieldEditor(P_SHOW_MATCHING_BRACKETS, Messages.getString("Preferences.ShowMatching"), group));
		addField(new ColorFieldEditor(P_MATCHING_BRACKETS_COLOR, Messages.getString("Preferences.ColorMatching"), group));
		
		group = createGroup(appearanceComposite, Messages.getString("Preferences.Folding"));
		addField(new ComboFieldEditor(
				P_INITIAL_FOLDING,
				Messages.getString("Preferences.StartFolded"),
				new String[][] {
						{ Messages.getString("Preferences.StartFoldedNone"), P_FOLDING_STATUS_NONE },
						{ Messages.getString("Preferences.StartFoldedAll"), P_FOLDING_STATUS_ALL },
						{ Messages.getString("Preferences.StartFoldedFunction"), P_FOLDING_STATUS_FUNCTION },
						{ Messages.getString("Preferences.StartFoldedLevel1"), P_FOLDING_STATUS_LEVEL1 },
						{ Messages.getString("Preferences.StartFoldedLevel2"), P_FOLDING_STATUS_LEVEL2 },
						{ Messages.getString("Preferences.StartFoldedLevel3"), P_FOLDING_STATUS_LEVEL3 },
						{ Messages.getString("Preferences.StartFoldedLevel4"), P_FOLDING_STATUS_LEVEL4 },
						{ Messages.getString("Preferences.StartFoldedLevel5"), P_FOLDING_STATUS_LEVEL5 },
						{ Messages.getString("Preferences.StartFoldedLevel6"), P_FOLDING_STATUS_LEVEL6 },
					}, group));

		group = createGroup(appearanceComposite, Messages.getString("Preferences.Misc"));
		addField(new BooleanFieldEditor(P_MARK_OCCURENCES, Messages.getString("Preferences.MarkOccurences"), group));
		Label listLabel = new Label(group, SWT.NONE);
		listLabel.setText(Messages.getString("Preferences.MarkOccurencesColor"));
		addField(new BooleanFieldEditor(P_TRAILING_SPACE, Messages.getString("Preferences.TrailingSpaces"), group));
		addField(new BooleanFieldEditor(P_LINT_CODE, Messages.getString("Preferences.Linters"), group));
		
	}

	public void init(IWorkbench workbench) {
	}
}