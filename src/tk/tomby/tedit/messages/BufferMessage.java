/*
 * $Id: BufferMessage.java,v 1.1.1.1 2004/09/18 17:16:20 amunoz Exp $
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
public class BufferMessage extends DefaultMessage {
    //~ Static fields/initializers *****************************************************************

    /** DOCUMENT ME! */
    public static final int UNDOABLE_EDIT_EVENT = 1;

    /** DOCUMENT ME! */
    public static final int CHANGE_FILE_EVENT = 2;

    /** DOCUMENT ME! */
    public static final int MODIFIED_FILE_EVENT = 3;
    
    /** DOCUMENT ME! */
    public static final int CARET_EVENT = 4;

    //~ Constructors *******************************************************************************

    /**
     * Creates a new BufferMessage object.
     *
     * @param source DOCUMENT ME!
     * @param type DOCUMENT ME!
     * @param newValue DOCUMENT ME!
     * @param oldValue DOCUMENT ME!
     */
    public BufferMessage(Object source,
                         int    type,
                         Object newValue,
                         Object oldValue) {
        super(MessageManager.BUFFER_GROUP_NAME, source, type, newValue, oldValue);
    }

    /**
     * Creates a new BufferMessage object.
     *
     * @param source DOCUMENT ME!
     * @param newValue DOCUMENT ME!
     * @param oldValue DOCUMENT ME!
     */
    public BufferMessage(Object source,
                         Object newValue,
                         Object oldValue) {
        super(MessageManager.BUFFER_GROUP_NAME, source, newValue, oldValue);
    }

    /**
     * Creates a new WorkSpaceMessage object.
     *
     * @param source DOCUMENT ME!
     * @param newValue DOCUMENT ME!
     */
    public BufferMessage(Object source,
                         Object newValue) {
        super(MessageManager.BUFFER_GROUP_NAME, source, newValue);
    }

    /**
     * Creates a new BufferMessage object.
     *
     * @param source DOCUMENT ME!
     */
    public BufferMessage(Object source) {
        super(MessageManager.BUFFER_GROUP_NAME, source);
    }

    /**
     * Creates a new BufferMessage object.
     *
     * @param source DOCUMENT ME!
     * @param type DOCUMENT ME!
     */
    public BufferMessage(Object source,
                         int    type) {
        super(MessageManager.BUFFER_GROUP_NAME, source, type);
    }
}
