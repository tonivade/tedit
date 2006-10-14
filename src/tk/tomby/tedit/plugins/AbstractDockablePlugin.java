/*
 * $Id: AbstractDockablePlugin.java,v 1.1.1.1 2004/09/18 17:16:22 amunoz Exp $
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

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import tk.tomby.tedit.core.IWorkspace;


/**
 * DOCUMENT ME!
 *
 * @author $Author: amunoz $
 * @version $Revision: 1.1.1.1 $
 */
public abstract class AbstractDockablePlugin extends JPanel implements IDockablePlugin {
    //~ Instance fields ****************************************************************************

    /** DOCUMENT ME! */
    protected JLabel title = null;

    /** DOCUMENT ME! */
    protected int location = IWorkspace.PLUGIN_LEFT;

    //~ Constructors *******************************************************************************

    /**
     *
     */
    public AbstractDockablePlugin() {
        super();

        setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(),
                                                     BorderFactory.createEtchedBorder()));

        title = new JLabel();
        title.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 1,
                                                                                           0,
                                                                                           UIManager
                                                                                           .getColor("Button.borderColor")),
                                                           BorderFactory.createEmptyBorder(5, 5, 5,
                                                                                           5)));
    }

    //~ Methods ************************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @param location DOCUMENT ME!
     */
    public void setDockLocation(int location) {
        this.location = location;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDockLocation() {
        return location;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Component getDockable() {
        return this;
    }
    
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getDockTitle() {
    	return title.getText();
    }
    
}
