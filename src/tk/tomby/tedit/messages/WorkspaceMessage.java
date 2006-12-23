/*
 * $Id: WorkspaceMessage.java,v 1.1.1.1 2004/09/18 17:16:20 amunoz Exp $
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

package tk.tomby.tedit.messages;

import tk.tomby.tedit.services.MessageManager;

/**
 * DOCUMENT ME!
 *
 * @author $Author: amunoz $
 * @version $Revision: 1.1.1.1 $
 */
public class WorkspaceMessage extends DefaultMessage {
    //~ Constructors *******************************************************************************

    /**
     * Creates a new WorkspaceMessage object.
     *
     * @param source DOCUMENT ME!
     * @param type DOCUMENT ME!
     * @param newValue DOCUMENT ME!
     * @param oldValue DOCUMENT ME!
     */
    public WorkspaceMessage(Object source,
                            int    type,
                            Object newValue,
                            Object oldValue) {
        super(MessageManager.WORKSPACE_GROUP_NAME, source, type, newValue, oldValue);
    }

    /**
     * Creates a new WorkspaceMessage object.
     *
     * @param source DOCUMENT ME!
     * @param newValue DOCUMENT ME!
     * @param oldValue DOCUMENT ME!
     */
    public WorkspaceMessage(Object source,
                            Object newValue,
                            Object oldValue) {
        super(MessageManager.WORKSPACE_GROUP_NAME, source, newValue, oldValue);
    }

    /**
     * Creates a new WorkSpaceMessage object.
     *
     * @param source DOCUMENT ME!
     * @param newValue DOCUMENT ME!
     */
    public WorkspaceMessage(Object source,
                            Object newValue) {
        super(MessageManager.WORKSPACE_GROUP_NAME, source, newValue);
    }

    /**
     * Creates a new WorkspaceMessage object.
     *
     * @param source DOCUMENT ME!
     */
    public WorkspaceMessage(Object source) {
        super(MessageManager.WORKSPACE_GROUP_NAME, source);
    }

    /**
     * Creates a new WorkspaceMessage object.
     *
     * @param source DOCUMENT ME!
     * @param type DOCUMENT ME!
     */
    public WorkspaceMessage(Object source,
                            int    type) {
        super(MessageManager.WORKSPACE_GROUP_NAME, source, type);
    }
}
