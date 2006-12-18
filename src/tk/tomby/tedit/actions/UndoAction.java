/*
 * $Id: UndoAction.java,v 1.1.1.1 2004/09/18 17:16:20 amunoz Exp $
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
import javax.swing.SwingUtilities;

import tk.tomby.tedit.core.IBuffer;

import tk.tomby.tedit.messages.BufferMessage;
import tk.tomby.tedit.messages.IMessageListener;
import tk.tomby.tedit.messages.StatusMessage;
import tk.tomby.tedit.messages.WorkspaceMessage;

import tk.tomby.tedit.services.MessageManager;
import tk.tomby.tedit.services.WorkspaceManager;


/**
 * DOCUMENT ME!
 *
 * @author $Author: amunoz $
 * @version $Revision: 1.1.1.1 $
 */
public class UndoAction extends AbstractAction {
    //~ Constructors *******************************************************************************

    /**
     * Creates a new UndoAction object.
     */
    public UndoAction() {
        super("undo");
        this.setEnabled(false);

        MessageManager.addMessageListener(MessageManager.BUFFER_GROUP_NAME, new IMessageListener<BufferMessage>() {
        	public void receiveMessage(BufferMessage message) {
        		UndoAction.this.receiveMessage(message);
        	}
        });
        MessageManager.addMessageListener(MessageManager.WORKSPACE_GROUP_NAME, new IMessageListener<WorkspaceMessage>() {
        	public void receiveMessage(WorkspaceMessage message) {
        		UndoAction.this.receiveMessage(message);
        	}
        });
    }

    //~ Methods ************************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @param evt DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent evt) {
        MessageManager.sendMessage(MessageManager.STATUS_GROUP_NAME,
                                   new StatusMessage(this, "UndoAction"));

        IBuffer buffer = WorkspaceManager.getCurrentBuffer();

        if (buffer != null) {
            if (buffer.canUndo()) {
                buffer.undo();
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param message DOCUMENT ME!
     */
    public void receiveMessage(BufferMessage message) {
        if (message.getType() == BufferMessage.UNDOABLE_EDIT_EVENT) {
            SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        IBuffer buffer = WorkspaceManager.getCurrentBuffer();

                        if (buffer != null) {
                            setEnabled(buffer.canUndo());
                        } else {
                            setEnabled(false);
                        }
                    }
                });
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param message DOCUMENT ME!
     */
    public void receiveMessage(WorkspaceMessage message) {
        SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    IBuffer buffer = WorkspaceManager.getCurrentBuffer();

                    if (buffer != null) {
                        setEnabled(buffer.canUndo());
                    } else {
                        setEnabled(false);
                    }
                }
            });
    }
}
