/*
 * $Id: ToolBarButton.java,v 1.1.1.1 2004/09/18 17:16:18 amunoz Exp $
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

package tk.tomby.tedit.gui;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

import tk.tomby.tedit.services.ResourceManager;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1.1.1 $
 */
public class ToolBarButton extends JButton {
    //~ Constructors *******************************************************************************

    /**
     * Creates a new ToolBarButton object.
     */
    public ToolBarButton() {
        super();
    }

    //~ Methods ************************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @param action DOCUMENT ME!
     */
    public void setAction(Action action) {
        action.addPropertyChangeListener(createActionPropertyChangeListener(action));
        this.setEnabled(action.isEnabled());
        this.addActionListener(action);
    }

    /**
     * DOCUMENT ME!
     *
     * @param iconKey DOCUMENT ME!
     */
    public void setIconKey(String iconKey) {
        Icon icon = ResourceManager.getIcon(iconKey);

        if (icon != null) {
            this.setIcon(icon);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param toolTipKey DOCUMENT ME!
     */
    public void setToolTipKey(String toolTipKey) {
        this.setToolTipText(ResourceManager.getProperty(toolTipKey));
    }
}
