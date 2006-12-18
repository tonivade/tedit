/*
 * $Id: ExitAction.java,v 1.1.1.1 2004/09/18 17:16:20 amunoz Exp $
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

package tk.tomby.tedit.actions;

import java.awt.event.ActionEvent;

import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tk.tomby.tedit.core.IBuffer;

import tk.tomby.tedit.services.PluginManager;
import tk.tomby.tedit.services.ResourceManager;
import tk.tomby.tedit.services.WorkspaceManager;


/**
 * DOCUMENT ME!
 *
 * @author $Author: amunoz $
 * @version $Revision: 1.1.1.1 $
 */
public class ExitAction extends AbstractAction {
    //~ Static fields/initializers *****************************************************************

    /** DOCUMENT ME! */
    private static Log log = LogFactory.getLog(ExitAction.class);

    //~ Constructors *******************************************************************************

    /**
     * Creates a new ExitAction object.
     */
    public ExitAction() {
        super("exit");
    }

    //~ Methods ************************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @param evt DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent evt) {
        List<IBuffer> buffers = WorkspaceManager.getBufferList();

        for (Iterator<IBuffer> iter = buffers.iterator(); iter.hasNext();) {
            IBuffer buffer = iter.next();

            if (buffer.isModified()) {
                if (log.isDebugEnabled()) {
                    log.debug(buffer.getFileName() + "was modified");
                }

                int retval =
                    JOptionPane.showConfirmDialog(WorkspaceManager.getMainFrame(),
                                                  ResourceManager.getProperty("main.savechanges.title"),
                                                  buffer.getFileName(),
                                                  JOptionPane.YES_NO_CANCEL_OPTION);

                switch (retval) {
                    case JOptionPane.YES_OPTION:

                        if (!buffer.isNew()) {
                            buffer.save();
                        } else {
                            JFileChooser chooser = new JFileChooser();

                            if (chooser.showSaveDialog(WorkspaceManager.getMainFrame()) == JFileChooser.APPROVE_OPTION) {
                                buffer.saveAs(chooser.getSelectedFile());
                            }
                        }

                        break;

                    case JOptionPane.NO_OPTION:
                        break;

                    default:
                        return;
                }
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("saving workspace");
        }

        WorkspaceManager.save();
        PluginManager.save();

        if (log.isDebugEnabled()) {
            log.debug("exit from application");
        }

        System.exit(0);
    }
}
