/*
 * $Id: ShortedListModel.java,v 1.1.1.1 2004/09/18 17:16:22 amunoz Exp $
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

package tk.tomby.tedit.plugins.explorer;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

import javax.swing.DefaultListModel;


/**
 * DOCUMENT ME!
 *
 * @author $Author: amunoz $
 * @version $Revision: 1.1.1.1 $
 */
public class ShortedListModel extends DefaultListModel {
    //~ Instance fields ****************************************************************************

    /** DOCUMENT ME! */
    private Comparator comparator = null;

    //~ Constructors *******************************************************************************

    /**
     * Creates a new ShortedListModel object.
     *
     * @param comparator DOCUMENT ME!
     */
    public ShortedListModel(Comparator comparator) {
        super();

        this.comparator = comparator;
    }

    //~ Methods ************************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     * @param obj DOCUMENT ME!
     */
    public void add(int    i,
                    Object obj) {
        addElement(obj);
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     */
    public void addAll(Collection c) {
        for (Iterator iter = c.iterator(); iter.hasNext();) {
            Object element = (Object) iter.next();
            addElement(element);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     */
    public void addElement(Object obj) {
        int index = findIndexFor(obj);
        insertElementAt(obj, index);
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private int findIndexFor(Object obj) {
        int count = size();

        if (count == 0) {
            return 0;
        }

        if (count == 1) {
            return (comparator.compare(obj, getElementAt(0)) <= 0) ? 0 : 1;
        }

        return findIndexFor(obj, 0, count - 1);
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     * @param i1 DOCUMENT ME!
     * @param i2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private int findIndexFor(Object obj,
                             int    i1,
                             int    i2) {
        if (i1 == i2) {
            return (comparator.compare(obj, getElementAt(i1)) <= 0) ? i1 : (i1 + 1);
        }

        int half = (i1 + i2) / 2;

        if (comparator.compare(obj, getElementAt(half)) <= 0) {
            return findIndexFor(obj, i1, half);
        }

        return findIndexFor(obj, half + 1, i2);
    }
}
