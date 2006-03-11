/*
 * $Id: PreferencePageDriver.java,v 1.1.1.1 2004/09/18 17:16:20 amunoz Exp $
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

package tk.tomby.tedit.core.preferences;

import tk.tomby.tedit.core.GenericDriver;

import tk.tomby.tedit.services.PreferenceManager;
import tk.tomby.tedit.services.ResourceManager;


/**
 * DOCUMENT ME!
 *
 * @author $Author: amunoz $
 * @version $Revision: 1.1.1.1 $
 */
public class PreferencePageDriver extends GenericDriver {
    //~ Static fields/initializers *****************************************************************

    /** DOCUMENT ME! */
    private static final String RULES_FILE =
        "tk/tomby/tedit/core/preferences/preferencepage-rules.xml";

    //~ Instance fields ****************************************************************************

    /** DOCUMENT ME! */
    private String resourceRoot = null;

    //~ Constructors *******************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @param categoryName DOCUMENT ME!
     * @param resourceRoot DOCUMENT ME!
     */
    public PreferencePageDriver(String categoryName,
                                String resourceRoot) {
        super(RULES_FILE);

        this.resourceRoot = resourceRoot;

        ResourceManager.loadCategory(categoryName, resourceRoot);
        PreferenceManager.loadCategory(categoryName, resourceRoot);
    }

    //~ Methods ************************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public IPreferencePage create() {
        return (IPreferencePage) createObject(resourceRoot + "/preferencepage.xml");
    }
}
