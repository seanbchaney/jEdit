/*
 * :tabSize=4:indentSize=4:noTabs=false:
 * :folding=explicit:collapseFolds=1:
 *
 * Copyright (C) 2000, 2003 Slava Pestov
 * Portions copyright (C) 1999 Jason Ginchereau
 * Portions copyright (C) 2003 mike dillon
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

package org.gjt.sp.jedit.gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.util.ArrayList;
import java.util.List;
import org.gjt.sp.jedit.*;
import org.gjt.sp.util.GenericGUIUtilities;
import org.gjt.sp.util.Log;


/**
 * A dialog for choosing fonts.
 *
 * @author Slava Pestov
 * @version $Id: FontSelectorDialog.java 25462 2021-03-29 21:17:46Z kpouer $
 * @since jEdit 4.4pre1
 */

public class FontSelectorDialog extends EnhancedDialog
{
	//{{{ FontSelectorDialog constructor
	public FontSelectorDialog(Frame parent,
				  Font font)
	{
		super(parent,jEdit.getProperty("font-selector.title"),true);
		init(font);
	} //}}}

	//{{{ FontSelectorDialog constructor
	public FontSelectorDialog(Dialog parent,
				  Font font)
	{
		super(parent,jEdit.getProperty("font-selector.title"),true);
		init(font);
	} //}}}

	//{{{ FontSelectorDialog constructor
	FontSelectorDialog(Frame parent,
			   Font font,
			   FontSelector fontSelector)
	{
		super(parent,jEdit.getProperty("font-selector.title"),true);
		this.fontSelector = fontSelector;
		init(font);
	} //}}}

	//{{{ FontSelectorDialog constructor
	FontSelectorDialog(Dialog parent,
			   Font font,
			   FontSelector fontSelector)
	{
		super(parent,jEdit.getProperty("font-selector.title"),true);
		this.fontSelector = fontSelector;
		init(font);
	} //}}}

	//{{{ ok() method
	@Override
	public void ok()
	{
		isOK = true;
		dispose();
	} //}}}

	//{{{ cancel() method
	@Override
	public void cancel()
	{
		dispose();
	} //}}}

	//{{{ getSelectedFont() method
	public Font getSelectedFont()
	{
		if(!isOK)
			return null;

		int size;
		try
		{
			size = Integer.parseInt(sizeField.getText());
		}
		catch(Exception e)
		{
			size = 12;
		}

		return new Font(familyField.getText(),styleList
			.getSelectedIndex(),size);
	} //}}}

	//{{{ Private members

	//{{{ Instance variables
	private FontSelector fontSelector;
	private boolean isOK;
	private JTextField familyField;
	private JList<String> familyList;
	private JTextField sizeField;
	private JList<String> sizeList;
	private JTextField styleField;
	private JList<String> styleList;
	private JLabel preview;
	private JButton ok;
	private JButton cancel;
	//}}}

	/**
	 * For some reason the default Java fonts show up in the
	 * list with .bold, .bolditalic, and .italic extensions.
	 */
	private static final String[] HIDEFONTS = {
		".bold",
		".italic"
	};

	//{{{ init() method
	private void init(Font font)
	{
		JPanel content = new JPanel(new BorderLayout());
		content.setBorder(new EmptyBorder(12,12,12,12));
		setContentPane(content);

		JPanel listPanel = new JPanel(new GridLayout(1,3,6,6));

		String[] fonts;
		try
		{
			fonts = getFontList();
		}
		catch(Exception e)
		{
			Log.log(Log.ERROR,this,"Broken Java implementation!");
			/* Log.log(Log.ERROR,this,"Using deprecated Toolkit.getFontList()"); */
			Log.log(Log.ERROR,this,e);

			/* fonts = getToolkit().getFontList(); */
			fonts = new String[] { "Broken Java implementation!" };
		}

		JPanel familyPanel = createTextFieldAndListPanel(
			"font-selector.family",
			familyField = new JTextField(),
			familyList = new JList<String>(fonts));
		listPanel.add(familyPanel);

		String[] sizes = { "9", "10", "12", "14", "16", "18", "24", "30", "36", "42" };
		JPanel sizePanel = createTextFieldAndListPanel(
			"font-selector.size",
			sizeField = new JTextField(),
			sizeList = new JList<String>(sizes));
		listPanel.add(sizePanel);

		String[] styles = {
			jEdit.getProperty("font-selector.plain"),
			jEdit.getProperty("font-selector.bold"),
			jEdit.getProperty("font-selector.italic"),
			jEdit.getProperty("font-selector.bolditalic")
		};

		JPanel stylePanel = createTextFieldAndListPanel(
			"font-selector.style",
			styleField = new JTextField(),
			styleList = new JList<String>(styles));
		styleField.setEditable(false);
		listPanel.add(stylePanel);

		if (font != null)
		{
			familyList.setSelectedValue(font.getFamily(),true);
			familyField.setText(font.getFamily());
			sizeList.setSelectedValue(String.valueOf(font.getSize()),true);
			sizeField.setText(String.valueOf(font.getSize()));
			styleList.setSelectedIndex(font.getStyle());
		}
		else
		{
			sizeList.setSelectedValue("12", true);
			styleList.setSelectedIndex(Font.PLAIN);
		}

		styleField.setText(styleList.getSelectedValue());

		ListSelectionListener listHandler = new ListHandler();
		familyList.addListSelectionListener(listHandler);
		sizeList.addListSelectionListener(listHandler);
		styleList.addListSelectionListener(listHandler);

		content.add(BorderLayout.NORTH,listPanel);

		preview = new JLabel(jEdit.getProperty("font-selector.long-text"))
		{
			@Override
			public void paintComponent(Graphics g)
			{
				if(fontSelector != null)
					fontSelector.setAntiAliasEnabled(g);
				super.paintComponent(g);
			}
		};
		preview.setBorder(new TitledBorder(jEdit.getProperty(
			"font-selector.preview")));

		updatePreview();

		Dimension prefSize = preview.getPreferredSize();
		prefSize.height = 100;
		preview.setPreferredSize(prefSize);

		content.add(BorderLayout.CENTER,preview);

		JPanel buttons = new JPanel();
		buttons.setLayout(new BoxLayout(buttons,BoxLayout.X_AXIS));
		buttons.setBorder(new EmptyBorder(17, 0, 0, 0));

		ok = new JButton(jEdit.getProperty("common.ok"));
		ok.addActionListener(e -> ok());
		getRootPane().setDefaultButton(ok);

		cancel = new JButton(jEdit.getProperty("common.cancel"));
		cancel.addActionListener(e -> cancel());
		
		GenericGUIUtilities.makeSameSize(ok, cancel);

		buttons.add(Box.createGlue());
		buttons.add(ok);
		buttons.add(Box.createHorizontalStrut(6));
		buttons.add(cancel);
		
		content.add(BorderLayout.SOUTH,buttons);

		pack();
		setLocationRelativeTo(getParent());
		setVisible(true);
	} //}}}

	//{{{ getFontList() method
	private static String[] getFontList()
	{
		String[] nameArray = GraphicsEnvironment
			.getLocalGraphicsEnvironment()
			.getAvailableFontFamilyNames();
		List<String> nameVector = new ArrayList<>(nameArray.length);

		for(int i = 0; i < nameArray.length; i++)
		{
			int j;
			for(j = 0; j < HIDEFONTS.length; j++)
			{
				if(nameArray[i].contains(HIDEFONTS[j]))
					break;
			}

			if(j == HIDEFONTS.length)
				nameVector.add(nameArray[i]);
		}

		String[] _array = new String[nameVector.size()];
		return nameVector.toArray(_array);
	} //}}}

	//{{{ createTextFieldAndListPanel() method
	private static JPanel createTextFieldAndListPanel(String label,
		JTextField textField, JList list)
	{
		GridBagLayout layout = new GridBagLayout();
		JPanel panel = new JPanel(layout);

		GridBagConstraints cons = new GridBagConstraints();
		cons.gridx = cons.gridy = 0;
		cons.gridwidth = cons.gridheight = 1;
		cons.fill = GridBagConstraints.BOTH;
		cons.weightx = 1.0f;

		JLabel _label = new JLabel(jEdit.getProperty(label));
		layout.setConstraints(_label,cons);
		panel.add(_label);

		cons.gridy = 1;
		Component vs = Box.createVerticalStrut(6);
		layout.setConstraints(vs,cons);
		panel.add(vs);

		cons.gridy = 2;
		layout.setConstraints(textField,cons);
		panel.add(textField);

		cons.gridy = 3;
		vs = Box.createVerticalStrut(6);
		layout.setConstraints(vs,cons);
		panel.add(vs);

		cons.gridy = 4;
		cons.gridheight = GridBagConstraints.REMAINDER;
		cons.weighty = 1.0f;
		JScrollPane scroller = new JScrollPane(list);
		layout.setConstraints(scroller,cons);
		panel.add(scroller);

		return panel;
	} //}}}

	//{{{ updatePreview() method
	private void updatePreview()
	{
		String family = familyField.getText();
		int size;
		try
		{
			size = Integer.parseInt(sizeField.getText());
		}
		catch(Exception e)
		{
			size = 12;
		}
		int style = styleList.getSelectedIndex();

		preview.setFont(new Font(family,style,size));
	} //}}}

	//}}}

	//{{{ ListHandler class
	private class ListHandler implements ListSelectionListener
	{
		@Override
		public void valueChanged(ListSelectionEvent evt)
		{
			Object source = evt.getSource();
			if(source == familyList)
			{
				String family = familyList.getSelectedValue();
				if(family != null)
					familyField.setText(family);
			}
			else if(source == sizeList)
			{
				String size = sizeList.getSelectedValue();
				if(size != null)
					sizeField.setText(size);
			}
			else if(source == styleList)
			{
				String style = styleList.getSelectedValue();
				if(style != null)
					styleField.setText(style);
			}

			updatePreview();
		}
	} //}}}
}

