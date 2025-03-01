/*
 * VFSUpdate.java - A path has changed
 * Copyright (C) 2000 Slava Pestov
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

import org.gjt.sp.jedit.*;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * Message sent when a file or directory changes.
 * @author Slava Pestov
 * @version $Id: VFSUpdate.java 25698 2023-11-17 01:31:50Z vampire0 $
 *
 * @since jEdit 2.6pre4
 */
public class VFSUpdate extends EBMessage
{
	/**
	 * Creates a VFS update message.
	 * @param path The path in question
	 */
	public VFSUpdate(@Nonnull String path)
	{
		super(null);
		Objects.requireNonNull(path);
		this.path = path;
	}

	/**
	 * Returns the path that changed.
	 */
	public String getPath()
	{
		return path;
	}

	@Override
	public String paramString()
	{
		return "path=" + path + "," + super.paramString();
	}

	// private members
	private final String path;
}
