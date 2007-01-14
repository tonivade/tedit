/*
 * $Id: ShortedComboBoxModel.java,v 1.1 2004/11/28 12:16:49 amunoz Exp $
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

package tk.tomby.tedit.gui;

import java.util.Comparator;

import javax.swing.DefaultComboBoxModel;


/**
 * DOCUMENT ME!
 *
 * @author $Author: amunoz $
 * @version $Revision: 1.1 $
 */
public class ShortedComboBoxModel extends DefaultComboBoxModel {
    //~ Instance fields ****************************************************************************

    /** DOCUMENT ME! */
    private Comparator<Object> comparator = null;

    //~ Constructors *******************************************************************************

    /**
     * Creates a new ShortedComboModel object.
     *
     * @param comparator DOCUMENT ME!
     */
    public ShortedComboBoxModel(Comparator<Object> comparator) {
        super();

        this.comparator = comparator;
    }

    //~ Methods ************************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     *
     * @see javax.swing.DefaultComboBoxModel#addElement(java.lang.Object)
     */
    public void addElement(Object obj) {
        int index = findIndexFor(obj);

        if (index > -1) {
            insertElementAt(obj, index);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private int findIndexFor(Object obj) {
        int count = getSize();

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
            if (comparator.compare(obj, getElementAt(i1)) < 0) {
                return i1;
            } else if (comparator.compare(obj, getElementAt(i1)) > 0) {
                return i1 + 1;
            }

            return -1;
        }

        int half = (i1 + i2) / 2;

        if (comparator.compare(obj, getElementAt(half)) <= 0) {
            return findIndexFor(obj, i1, half);
        }

        return findIndexFor(obj, half + 1, i2);
    }
}
