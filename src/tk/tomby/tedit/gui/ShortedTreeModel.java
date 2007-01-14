/*
 * $Id: ShortedTreeModel.java,v 1.1.1.1 2004/09/18 17:16:22 amunoz Exp $
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

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;


/**
 * DOCUMENT ME!
 *
 * @author $Author: amunoz $
 * @version $Revision: 1.1.1.1 $
 */
public class ShortedTreeModel extends DefaultTreeModel {
    //~ Instance fields ****************************************************************************

    /** DOCUMENT ME! */
    private Comparator<Object> comparator = null;

    //~ Constructors *******************************************************************************

    /**
     * Creates a new ShortedTreeModel object.
     *
     * @param node DOCUMENT ME!
     * @param comparator DOCUMENT ME!
     */
    public ShortedTreeModel(TreeNode   node,
                            Comparator<Object> comparator) {
        super(node);

        this.comparator = comparator;
    }

    /**
     * Creates a new ShortedTreeModel object.
     *
     * @param node DOCUMENT ME!
     * @param askAllowsChildren DOCUMENT ME!
     * @param comparator DOCUMENT ME!
     */
    public ShortedTreeModel(TreeNode   node,
                            boolean    askAllowsChildren,
                            Comparator<Object> comparator) {
        super(node, askAllowsChildren);

        this.comparator = comparator;
    }

    //~ Methods ************************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     * @param parent DOCUMENT ME!
     */
    public void insertAllInto(Collection<MutableTreeNode> c,
                              MutableTreeNode parent) {
        for (Iterator<MutableTreeNode> iter = c.iterator(); iter.hasNext();) {
            MutableTreeNode element = iter.next();
            insertNodeInto(element, parent);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param child DOCUMENT ME!
     * @param parent DOCUMENT ME!
     */
    public void insertNodeInto(MutableTreeNode child,
                               MutableTreeNode parent) {
        int index = findIndexFor(child, parent);
        super.insertNodeInto(child, parent, index);
    }

    /**
     * DOCUMENT ME!
     *
     * @param child DOCUMENT ME!
     * @param parent DOCUMENT ME!
     * @param i DOCUMENT ME!
     */
    public void insertNodeInto(MutableTreeNode child,
                               MutableTreeNode parent,
                               int             i) {
        insertNodeInto(child, parent);
    }

    /**
     * DOCUMENT ME!
     *
     * @param child DOCUMENT ME!
     * @param parent DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private int findIndexFor(MutableTreeNode child,
                             MutableTreeNode parent) {
        int count = parent.getChildCount();

        if (count == 0) {
            return 0;
        }

        if (count == 1) {
            return (comparator.compare(child, parent.getChildAt(0)) <= 0) ? 0 : 1;
        }

        return findIndexFor(child, parent, 0, count - 1);
    }

    /**
     * DOCUMENT ME!
     *
     * @param child DOCUMENT ME!
     * @param parent DOCUMENT ME!
     * @param i1 DOCUMENT ME!
     * @param i2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private int findIndexFor(MutableTreeNode child,
                             MutableTreeNode parent,
                             int             i1,
                             int             i2) {
        if (i1 == i2) {
            return (comparator.compare(child, parent.getChildAt(i1)) <= 0) ? i1 : (i1 + 1);
        }

        int half = (i1 + i2) / 2;

        if (comparator.compare(child, parent.getChildAt(half)) <= 0) {
            return findIndexFor(child, parent, i1, half);
        }

        return findIndexFor(child, parent, half + 1, i2);
    }
}
