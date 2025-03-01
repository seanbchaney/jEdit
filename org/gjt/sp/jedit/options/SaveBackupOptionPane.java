/*
 * AutosaveBackupOptionPane.java - Autosave & backup options
 * :tabSize=4:indentSize=4:noTabs=false:
 * :folding=explicit:collapseFolds=1:
 *
 * Copyright (C) 2003 Slava Pestov
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package org.gjt.sp.jedit.options;

//{{{ Imports
import javax.swing.*;
import java.awt.*;
import org.gjt.sp.jedit.*;
import org.gjt.sp.jedit.gui.NumericTextField;
import org.gjt.sp.jedit.browser.VFSBrowser;
import org.gjt.sp.jedit.manager.BufferManager;
//}}}

/**
 * The Save and Backup option panel.
 *
 * @author Slava Pestov
 * @author $Id: SaveBackupOptionPane.java 25702 2023-11-17 20:21:41Z vampire0 $
 */
public class SaveBackupOptionPane extends AbstractOptionPane
{
	//{{{ SaveBackupOptionPane constructor
	public SaveBackupOptionPane()
	{
		super("save-back");
	} //}}}

	//{{{ _init() method
	@Override
	protected void _init()
	{
		/* Save-As Uses FSB */

		saveAsUsesFSB = new JCheckBox(jEdit.getProperty(
			"options.save-back.saveAsUsesFSB"));
		saveAsUsesFSB.setSelected(jEdit.getBooleanProperty(
			"saveAsUsesFSB"));
		saveAsUsesFSB.setToolTipText(jEdit.getProperty(
			"options.save-back.saveAsUsesFSB.tooltip"));
		addComponent(saveAsUsesFSB);

		/* Two-stage save */
		twoStageSave = new JCheckBox(jEdit.getProperty(
			"options.save-back.twoStageSave"));
		twoStageSave.setSelected(jEdit.getBooleanProperty(
			"twoStageSave"));
		twoStageSave.setToolTipText(jEdit.getProperty(

			"options.save-back.twoStageSave.tooltip"));
		addComponent(twoStageSave);

		/* Confirm save all */
		confirmSaveAll = new JCheckBox(jEdit.getProperty(
			"options.save-back.confirmSaveAll"));
		confirmSaveAll.setSelected(jEdit.getBooleanProperty(
			"confirmSaveAll"));
		addComponent(confirmSaveAll);

		useMD5forDirtyCalculation = new JCheckBox(jEdit.getProperty(
			"options.save-back.useMD5forDirtyCalculation"));
		useMD5forDirtyCalculation.setToolTipText(jEdit.getProperty(
			"options.save-back.useMD5forDirtyCalculation.tooltip"));
		useMD5forDirtyCalculation.setSelected(
			jEdit.getBooleanProperty("useMD5forDirtyCalculation"));
		addComponent(useMD5forDirtyCalculation);


		/* Close Dirty Untitled Buffers without confirm */
		suppressNotSavedConfirmUntitled = new JCheckBox(jEdit.getProperty(
			"options.save-back.suppressNotSavedConfirmUntitled"));

		suppressNotSavedConfirmUntitled.setToolTipText(jEdit.getProperty("options.save-back.suppressNotSavedConfirmUntitled.tooltip"));
		suppressNotSavedConfirmUntitled.setSelected(jEdit.getBooleanProperty("suppressNotSavedConfirmUntitled"));
		addComponent(suppressNotSavedConfirmUntitled);

		addSeparator("options.autosave");

		/* Autosave Directory */

		autosaveDirectory = new JTextField(jEdit.getProperty(
			"autosave.directory"));
		autosaveDirectory.setToolTipText(
			jEdit.getProperty("options.save-back.backupDirectory.tooltip"));

		JButton browseAutosaveDirectory = new JButton("...");
		browseAutosaveDirectory.addActionListener(e ->
		{
			String[] choosenFolder =
				GUIUtilities.showVFSFileDialog(null, autosaveDirectory.getText(),
					VFSBrowser.CHOOSE_DIRECTORY_DIALOG, false);
			if (choosenFolder.length > 0)
				autosaveDirectory.setText(choosenFolder[0]);
		});
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(autosaveDirectory);
		panel.add(browseAutosaveDirectory, BorderLayout.EAST);
		addComponent(jEdit.getProperty("options.save-back.autosaveDirectory"),
			panel);


		/* Autosave untitled buffers */
		autosaveUntitled = new JCheckBox(jEdit.getProperty(
			"options.save-back.autosaveUntitled"));
		autosaveUntitled.setToolTipText(jEdit.getProperty("options.save-back.autosaveUntitled.tooltip"));

		autosaveUntitled.setSelected(jEdit.getBooleanProperty("autosaveUntitled"));
		addComponent(autosaveUntitled);

		/* Autosave interval */
		autosave = new NumericTextField(jEdit.getProperty("autosave"), true);
		autosave.setToolTipText(jEdit.getProperty("options.save-back.autosave.tooltip"));
		addComponent(jEdit.getProperty("options.save-back.autosave"),autosave);

		addSeparator("options.backup");

		/* Backup directory */
		backupDirectory = new JTextField(jEdit.getProperty(
			"backup.directory"));
		backupDirectory.setToolTipText(
			jEdit.getProperty("options.save-back.backupDirectory.tooltip"));

		JButton browseBackupDirectory = new JButton("...");
		browseBackupDirectory.addActionListener(e ->
		{
			String[] choosenFolder =
				GUIUtilities.showVFSFileDialog(null, backupDirectory.getText(),
					VFSBrowser.CHOOSE_DIRECTORY_DIALOG, false);
			if (choosenFolder.length > 0)
				backupDirectory.setText(choosenFolder[0]);
		});
		panel = new JPanel(new BorderLayout());
		panel.add(backupDirectory);
		panel.add(browseBackupDirectory, BorderLayout.EAST);
		addComponent(jEdit.getProperty("options.save-back.backupDirectory"),
			panel);


		/* Backup count */
		backups = new NumericTextField(jEdit.getProperty("backups"), true);
		backups.setToolTipText(jEdit.getProperty("options.save-back.backups.tooltip"));
		addComponent(jEdit.getProperty("options.save-back.backups"),backups);


		/* Backup filename prefix */
		backupPrefix = new JTextField(jEdit.getProperty("backup.prefix"));
		addComponent(jEdit.getProperty("options.save-back.backupPrefix"),
			backupPrefix);

		/* Backup suffix */
		backupSuffix = new JTextField(jEdit.getProperty(
			"backup.suffix"));
		addComponent(jEdit.getProperty("options.save-back.backupSuffix"),
			backupSuffix);

		/* Backup on every save */
		backupEverySave = new JCheckBox(jEdit.getProperty(
			"options.save-back.backupEverySave"));
		backupEverySave.setSelected(jEdit.getBooleanProperty("backupEverySave"));
		addComponent(backupEverySave);
	} //}}}

	//{{{ _save() method
	@Override
	protected void _save()
	{
		jEdit.setBooleanProperty("saveAsUsesFSB", saveAsUsesFSB.isSelected());
		jEdit.setBooleanProperty("twoStageSave",twoStageSave.isSelected());
		jEdit.setBooleanProperty("confirmSaveAll",confirmSaveAll.isSelected());
		jEdit.setProperty("autosave", this.autosave.getText());
		jEdit.setProperty("backups",backups.getText());

		jEdit.setProperty("backup.directory",backupDirectory.getText());
		String autosaveDirectoryOriginal = jEdit.getProperty("autosave.directory");
		jEdit.setProperty("autosave.directory", autosaveDirectory.getText());
		jEdit.setProperty("backup.prefix",backupPrefix.getText());
		jEdit.setProperty("backup.suffix",backupSuffix.getText());
		jEdit.setBooleanProperty("backupEverySave", backupEverySave.isSelected());
		boolean newAutosave = autosaveUntitled.isSelected();
		boolean oldAutosave = jEdit.getBooleanProperty("autosaveUntitled");
		jEdit.setBooleanProperty("autosaveUntitled", newAutosave);
		jEdit.setBooleanProperty("suppressNotSavedConfirmUntitled", suppressNotSavedConfirmUntitled.isSelected());

		jEdit.setBooleanProperty("useMD5forDirtyCalculation",
				useMD5forDirtyCalculation.isSelected());
		BufferManager bufferManager = jEdit.getBufferManager();
		if ((!newAutosave || jEdit.getIntegerProperty("autosave",0) == 0) && oldAutosave)
		{
			bufferManager.getUntitledBuffers().forEach(Buffer::removeAutosaveFile);
		}

		// if autosave dir changed, we should issue to perform an autosave for all dirty and all untitled buffers
		// to have the autosaves at the new location
		if (!autosaveDirectory.getText().equals(autosaveDirectoryOriginal))
		{
			bufferManager.getDirtyBuffers().forEach(buffer -> buffer.autosave(true));
		}
	} //}}}

	//{{{ Private members
	private JCheckBox saveAsUsesFSB;
	private JCheckBox twoStageSave;
	private JCheckBox confirmSaveAll;
	private JTextField autosave;
	private JCheckBox autosaveUntitled;
	private JCheckBox suppressNotSavedConfirmUntitled;

	private JCheckBox useMD5forDirtyCalculation;
	private JTextField backups;
	private JTextField autosaveDirectory;
	private JTextField backupDirectory;
	private JTextField backupPrefix;
	private JTextField backupSuffix;
	private JCheckBox backupEverySave;
	//}}}
}
