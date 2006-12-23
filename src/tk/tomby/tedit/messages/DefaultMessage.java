/*
 * $Id: DefaultMessage.java,v 1.1.1.1 2004/09/18 17:16:20 amunoz Exp $
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

/**
 * DOCUMENT ME!
 *
 * @author $Author: amunoz $
 * @version $Revision: 1.1.1.1 $
 */
public class DefaultMessage implements IMessage {
    //~ Instance fields ****************************************************************************

	/** DOCUMENT ME! */
	private String group = null;
	
    /** DOCUMENT ME! */
    private Object newValue = null;

    /** DOCUMENT ME! */
    private Object oldValue = null;

    /** DOCUMENT ME! */
    private Object source = null;

    /** DOCUMENT ME! */
    private int type = 0;

    //~ Constructors *******************************************************************************

    /**
     * Creates a new DefaultEvent object.
     *
     * @param source DOCUMENT ME!
     * @param type DOCUMENT ME!
     * @param newValue DOCUMENT ME!
     * @param oldValue DOCUMENT ME!
     */
    public DefaultMessage(String group,
    					  Object source,
                          int    type,
                          Object newValue,
                          Object oldValue) {
    	this.group 	  = group;
        this.source       = source;
        this.type         = type;
        this.newValue     = newValue;
        this.oldValue     = oldValue;
    }

    /**
     * Creates a new DefaultEvent object.
     *
     * @param source DOCUMENT ME!
     * @param newValue DOCUMENT ME!
     * @param oldValue DOCUMENT ME!
     */
    public DefaultMessage(String group,
			  			  Object source,
                          Object newValue,
                          Object oldValue) {
        this(group, source, 0, newValue, oldValue);
    }

    /**
     * Creates a new DefaultEvent object.
     *
     * @param source DOCUMENT ME!
     * @param newValue DOCUMENT ME!
     */
    public DefaultMessage(String group,
			  			  Object source,
                          Object newValue) {
        this(group, source, 0, newValue, null);
    }

    /**
     * Creates a new DefaultEvent object.
     *
     * @param source DOCUMENT ME!
     */
    public DefaultMessage(String group,
			  			  Object source) {
        this(group, source, null, null);
    }

    /**
     * Creates a new DefaultEvent object.
     *
     * @param source DOCUMENT ME!
     * @param type DOCUMENT ME!
     */
    public DefaultMessage(String group,
			  			  Object source,
                          int    type) {
        this(group, source, type, null, null);
    }

    //~ Methods ************************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getNewValue() {
        return newValue;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getOldValue() {
        return oldValue;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getSource() {
        return source;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getType() {
        return type;
    }
    
    public String getGroup() {
    	return group;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append("[").append("type=").append(type).append(",");
        sb.append("group=").append(group).append(",");
        sb.append("source=").append(source).append(",");
        sb.append("oldValue=").append(oldValue).append(",");
        sb.append("newValue=").append(newValue).append("]");

        return sb.toString();
    }
}
