/*
 * $Id: IWorkspace.java,v 1.1.1.1 2004/09/18 17:16:19 amunoz Exp $
 *
 * Copyright (C) 2003 Antonio G. Muñoz Conejo <amunoz@tomby.homelinux.org>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package tk.tomby.tedit.core;

import java.util.List;

import tk.tomby.tedit.plugins.IPlugin;


/**
 * DOCUMENT ME!
 *
 * @author $Author: amunoz $
 * @version $Revision: 1.1.1.1 $
 */
public interface IWorkspace {
    //~ Static fields/initializers *****************************************************************

    /** DOCUMENT ME! */
    public static final int PLUGIN_BOTTOM = 1;

    /** DOCUMENT ME! */
    public static final int PLUGIN_LEFT = 2;

    /** DOCUMENT ME! */
    public static final int PLUGIN_RIGHT = 3;

    /** DOCUMENT ME! */
    public static final int TAB_POSITION_TOP = 0;

    /** DOCUMENT ME! */
    public static final int TAB_POSITION_BOTTOM = 1;

    //~ Methods ************************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract IBuffer getBuffer(int i);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract List getBufferList();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract IBuffer getCurrentBuffer();

    /**
     * DOCUMENT ME!
     *
     * @param buffer DOCUMENT ME!
     */
    public abstract void addBuffer(IBuffer buffer);

    /**
     * DOCUMENT ME!
     *
     * @param plugin DOCUMENT ME!
     */
    public abstract void addPlugin(IPlugin plugin);

    /**
     * DOCUMENT ME!
     */
    public abstract void closeCurrentBuffer();

    /**
     * DOCUMENT ME!
     */
    public abstract void init();

    /**
     * DOCUMENT ME!
     */
    public abstract void save();
}
