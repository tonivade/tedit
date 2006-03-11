/*
 * $Id: PreferenceMessage.java,v 1.1.1.1 2004/09/18 17:16:20 amunoz Exp $
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
public class PreferenceMessage extends DefaultMessage {
    //~ Instance fields ****************************************************************************

    /** DOCUMENT ME! */
    private String key = null;

    //~ Constructors *******************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @param source
     * @param key DOCUMENT ME!
     * @param newValue
     * @param oldValue
     */
    public PreferenceMessage(Object source,
                             String key,
                             Object newValue,
                             Object oldValue) {
        super(source, 0, newValue, oldValue);

        this.key = key;
    }

    /**
     * DOCUMENT ME!
     *
     * @param source
     * @param key DOCUMENT ME!
     * @param newValue
     */
    public PreferenceMessage(Object source,
                             String key,
                             Object newValue) {
        this(source, key, newValue, null);
    }

    /**
     * DOCUMENT ME!
     *
     * @param source
     * @param key DOCUMENT ME!
     */
    public PreferenceMessage(Object source,
                             String key) {
        this(source, key, null, null);
    }

    //~ Methods ************************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getKey() {
        return key;
    }
}
