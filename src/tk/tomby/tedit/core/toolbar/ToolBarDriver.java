/*
 * $Id: ToolBarDriver.java,v 1.1.1.1 2004/09/18 17:16:20 amunoz Exp $
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

package tk.tomby.tedit.core.toolbar;

import tk.tomby.tedit.core.GenericDriver;

import tk.tomby.tedit.services.ResourceManager;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1.1.1 $
 */
public class ToolBarDriver extends GenericDriver {
    //~ Static fields/initializers *****************************************************************

    /** DOCUMENT ME! */
    private static final String RESOURCE_ROOT = "tk/tomby/tedit/core/toolbar";

    /** DOCUMENT ME! */
    private static final String CATEGORY_NAME = "toolbar";

    //~ Constructors *******************************************************************************

    /**
     * DOCUMENT ME!
     */
    public ToolBarDriver() {
        super(RESOURCE_ROOT + "/toolbar-rules.xml");

        ResourceManager.loadCategory(CATEGORY_NAME, RESOURCE_ROOT);
    }

    //~ Methods ************************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ToolBar create() {
        return (ToolBar) createObject(RESOURCE_ROOT + "/toolbar.xml");
    }
}
