/*
 * $Id: MultiLineRule.java,v 1.1.1.1 2004/09/18 17:16:21 amunoz Exp $
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

package tk.tomby.tedit.syntax;

/**
 * DOCUMENT ME!
 *
 * @author $Author: amunoz $
 * @version $Revision: 1.1.1.1 $
 */
public class MultiLineRule implements IRule {
    //~ Instance fields ****************************************************************************

    /** DOCUMENT ME! */
    private String end = null;

    /** DOCUMENT ME! */
    private String start = null;

    /** DOCUMENT ME! */
    private String state = null;

    //~ Methods ************************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @param end The end to set.
     */
    public void setEnd(String end) {
        this.end = end;
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the end.
     */
    public String getEnd() {
        return end;
    }

    /**
     * DOCUMENT ME!
     *
     * @param start The start to set.
     */
    public void setStart(String start) {
        this.start = start;
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the start.
     */
    public String getStart() {
        return start;
    }

    /**
     * DOCUMENT ME!
     *
     * @param state The state to set.
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the state.
     */
    public String getState() {
        return state;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return start;
    }
}
