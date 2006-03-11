/*
 * $Id: ToolBar.java,v 1.1.1.1 2004/09/18 17:16:20 amunoz Exp $
 *
 * Copyright (C) 2003 Antonio G. Mu�oz Conejo <amunoz@tomby.homelinux.org>
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

package tk.tomby.tedit.core.toolbar;

import javax.swing.JToolBar;

import tk.tomby.tedit.gui.ToolBarButton;
import tk.tomby.tedit.gui.ToolBarSeparator;


/**
 * DOCUMENT ME!
 *
 * @author $Author: amunoz $
 * @version $Revision: 1.1.1.1 $
 */
public class ToolBar extends JToolBar {
    //~ Constructors *******************************************************************************

    /**
     * Creates a new MenuBar object.
     */
    public ToolBar() {
        super();
    }

    //~ Methods ************************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @param button DOCUMENT ME!
     */
    public void addButton(ToolBarButton button) {
        this.add(button);
    }

    /**
     * DOCUMENT ME!
     *
     * @param separator DOCUMENT ME!
     */
    public void addSeparator(ToolBarSeparator separator) {
        this.add(separator);
    }
}
