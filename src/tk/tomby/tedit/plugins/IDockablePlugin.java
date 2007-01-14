/*
 * $Id: IDockablePlugin.java,v 1.1.1.1 2004/09/18 17:16:22 amunoz Exp $
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

package tk.tomby.tedit.plugins;

import java.awt.Component;


/**
 * DOCUMENT ME!
 *
 * @author tomby
 */
public interface IDockablePlugin extends IPlugin {
    //~ Methods ************************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @param location DOCUMENT ME!
     */
    public abstract void setDockLocation(int location);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract int getDockLocation();
    
    /**
     * DOCUMENT ME!
     *
     * @param region DOCUMENT ME!
     */
    public abstract void setDockRegion(String region);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract String getDockRegion();
    
    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public abstract String getDockTitle();
    

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract Component getDockable();
}
