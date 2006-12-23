/*
 * $Id: CloseAction.java,v 1.2 2004/11/28 15:33:05 amunoz Exp $
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

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import tk.tomby.tedit.core.IBuffer;

import tk.tomby.tedit.messages.StatusMessage;
import tk.tomby.tedit.messages.WorkspaceMessage;

import tk.tomby.tedit.services.ActionManager;
import tk.tomby.tedit.services.MessageManager;
import tk.tomby.tedit.services.ResourceManager;
import tk.tomby.tedit.services.WorkspaceManager;


/**
 * DOCUMENT ME!
 *
 * @author $Author: amunoz $
 * @version $Revision: 1.2 $
 */
public class CloseAction extends AbstractAction {
    //~ Constructors *******************************************************************************

    /**
     * Creates a new CloseAction object.
     */
    public CloseAction() {
        super("close");
    }

    //~ Methods ************************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @param evt DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent evt) {
        MessageManager.sendMessage(new StatusMessage(this, "CloseAction"));

        IBuffer buffer = WorkspaceManager.getCurrentBuffer();

        if (buffer != null) {
            if (buffer.isModified()) {
                int retval =
                    JOptionPane.showConfirmDialog(WorkspaceManager.getMainFrame(),
                                                  ResourceManager.getProperty("main.savechanges.title"));

                switch (retval) {
                    case JOptionPane.YES_OPTION:
                        ActionManager.invokeAction("save", evt);
                        WorkspaceManager.closeCurrentBuffer();
                        MessageManager.sendMessage(new WorkspaceMessage(evt.getSource(),
						                        WorkspaceManager
						                        .getCurrentBuffer()));

                        break;

                    case JOptionPane.NO_OPTION:
                        WorkspaceManager.closeCurrentBuffer();
                        MessageManager.sendMessage(new WorkspaceMessage(evt.getSource(),
						                        WorkspaceManager
						                        .getCurrentBuffer()));

                        break;

                    default:
                        ;
                }
            } else {
                WorkspaceManager.closeCurrentBuffer();
                MessageManager.sendMessage(new WorkspaceMessage(evt.getSource(),
				                        WorkspaceManager.getCurrentBuffer()));
            }
        }
    }
}
