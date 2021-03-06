/*
 * $Id: SaveAsAction.java,v 1.1.1.1 2004/09/18 17:16:20 amunoz Exp $
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

package tk.tomby.tedit.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;

import tk.tomby.tedit.core.IBuffer;

import tk.tomby.tedit.messages.StatusMessage;

import tk.tomby.tedit.services.MessageManager;
import tk.tomby.tedit.services.WorkspaceManager;


/**
 * DOCUMENT ME!
 *
 * @author $Author: amunoz $
 * @version $Revision: 1.1.1.1 $
 */
public class SaveAsAction extends AbstractAction {
    //~ Constructors *******************************************************************************

    /**
     * Creates a new SaveAsAction object.
     */
    public SaveAsAction() {
        super("save-as");
    }

    //~ Methods ************************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @param evt DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent evt) {
        MessageManager.sendMessage(new StatusMessage(this, "SaveAsAction"));

        IBuffer buffer = WorkspaceManager.getCurrentBuffer();

        if (buffer != null) {
            JFileChooser chooser = new JFileChooser();
            int retVal           = chooser.showSaveDialog(WorkspaceManager.getMainFrame());

            if (retVal == JFileChooser.APPROVE_OPTION) {
                buffer.saveAs(chooser.getSelectedFile());
            }
        }
    }
}
