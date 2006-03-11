/*
 * $Id: UpDirectoryAction.java,v 1.1 2004/11/28 12:17:00 amunoz Exp $
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

package tk.tomby.tedit.plugins.explorer.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tk.tomby.tedit.plugins.explorer.TopPanel;


/**
 * DOCUMENT ME!
 *
 * @author $Author: amunoz $
 * @version $Revision: 1.1 $
 */
public class UpDirectoryAction extends AbstractAction {
    //~ Static fields/initializers *****************************************************************

    /** DOCUMENT ME! */
    private static Log log = LogFactory.getLog(UpDirectoryAction.class);

    //~ Instance fields ****************************************************************************

    /** DOCUMENT ME! */
    private TopPanel topPanel = null;

    //~ Constructors *******************************************************************************

    /**
     * Creates a new UpDirectoryAction object.
     *
     * @param topPanel DOCUMENT ME!
     */
    public UpDirectoryAction(TopPanel topPanel) {
        super("up-dir");

        this.topPanel = topPanel;
    }

    //~ Methods ************************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        if (log.isDebugEnabled()) {
            log.debug("Up directory");
        }

        String directory = topPanel.getDirectory();
        topPanel.setDirectory(removeOneLevel(directory));
    }

    /**
     * DOCUMENT ME!
     *
     * @param directory
     *
     * @return new directory
     */
    private String removeOneLevel(String directory) {
        if (log.isDebugEnabled()) {
            log.debug("Directory is " + directory);
        }

        String newDirectory = directory;

        int indexOfSlash = directory.lastIndexOf('/');

        if (indexOfSlash > -1) {
            newDirectory = directory.substring(0, indexOfSlash);
        }

        if (newDirectory.length() == 0) {
            newDirectory = "/";
        }

        if (log.isDebugEnabled()) {
            log.debug("New directory is " + newDirectory);
        }

        return newDirectory;
    }
}
