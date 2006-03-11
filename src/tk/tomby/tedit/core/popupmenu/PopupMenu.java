/*
 * $Id: PopupMenu.java,v 1.1.1.1 2004/09/18 17:16:20 amunoz Exp $
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

package tk.tomby.tedit.core.popupmenu;

import javax.swing.JPopupMenu;

import tk.tomby.tedit.gui.MenuItem;
import tk.tomby.tedit.gui.MenuSeparator;


/**
 * DOCUMENT ME!
 *
 * @author $Author: amunoz $
 * @version $Revision: 1.1.1.1 $
 */
public class PopupMenu extends JPopupMenu {
    //~ Constructors *******************************************************************************

    /**
     * Creates a new MenuBar object.
     */
    public PopupMenu() {
        super();
    }

    //~ Methods ************************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @param item DOCUMENT ME!
     */
    public void addMenuItem(MenuItem item) {
        this.add(item);
    }

    /**
     * DOCUMENT ME!
     *
     * @param separator DOCUMENT ME!
     */
    public void addMenuSeparator(MenuSeparator separator) {
        this.add(separator);
    }
}
