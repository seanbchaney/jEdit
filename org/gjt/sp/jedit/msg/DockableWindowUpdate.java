/*
 * DockableWindowUpdate.java - Dockable window update message
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

package org.gjt.sp.jedit.msg;

import org.gjt.sp.jedit.gui.DockableWindowManager;
import org.gjt.sp.jedit.*;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * Message sent when dockable window state changes.
 * @author Slava Pestov
 * @version $Id: DockableWindowUpdate.java 25698 2023-11-17 01:31:50Z vampire0 $
 *
 * @since jEdit 4.2pre1
 */
public class DockableWindowUpdate extends EBMessage
{
	//{{{ Message types
	/**
	 * Properties changed. Fired instead of global
	 * <code>PropertiesChanged</code> for improved performance.
	 * @since jEdit 4.2pre1
	 */
	public static final Object PROPERTIES_CHANGED = "PROPERTIES_CHANGED";

	/**
	 * Dockable activated. This is sent when the dockable is made visible.
	 * @since jEdit 4.2pre1
	 */
	public static final Object ACTIVATED = "ACTIVATED";

	/**
	 * Dockable deactivated. This is sent when the dockable is hidden.
	 * @since jEdit 4.2pre1
	 */
	public static final Object DEACTIVATED = "DEACTIVATED";
	//}}}

	//{{{ DockableWindowUpdate constructor
	/**
	 * Creates a new dockable window update message.
	 * @param wm The dockable window manager
	 * @param what What happened
	 * @param dockable The dockable window in question
	 */
	public DockableWindowUpdate(DockableWindowManager wm, @Nonnull Object what, String dockable)
	{
		super(wm);
		Objects.requireNonNull(what);
		this.what = what;
		this.dockable = dockable;
	} //}}}

	//{{{ getWhat() method
	/**
	 * Returns what caused this dockable update.
	 */
	public Object getWhat()
	{
		return what;
	} //}}}

	//{{{ getDockable() method
	/**
	 * Returns the dockable in question, or null if the message type is
	 * <code>PROPERTIES_CHANGED</code>.
	 */
	public String getDockable()
	{
		return dockable;
	} //}}}

	//{{{ paramString() method
	@Override
	public String paramString()
	{
		return "what=" + what + ",dockable=" + dockable + "," + super.paramString();
	} //}}}

	//{{{ Private members
	private final Object what;
	private final String dockable;
	//}}}
}
