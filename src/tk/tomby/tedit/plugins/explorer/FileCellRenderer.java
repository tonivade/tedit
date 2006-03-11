/*
 * $Id: FileCellRenderer.java,v 1.1.1.1 2004/09/18 17:16:22 amunoz Exp $
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

import java.awt.Component;

import java.io.File;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;


/**
 * DOCUMENT ME!
 *
 * @author $Author: amunoz $
 * @version $Revision: 1.1.1.1 $
 */
public class FileCellRenderer extends JLabel implements ListCellRenderer {
    //~ Constructors *******************************************************************************

    /**
     * Creates a new FileCellRenderer object.
     */
    public FileCellRenderer() {
        setOpaque(true);
    }

    //~ Methods ************************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @param list DOCUMENT ME!
     * @param value DOCUMENT ME!
     * @param index DOCUMENT ME!
     * @param selected DOCUMENT ME!
     * @param cellHasFocus DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Component getListCellRendererComponent(JList   list,
                                                  Object  value,
                                                  int     index,
                                                  boolean selected,
                                                  boolean cellHasFocus) {
        File file = (File) value;

        setText(file.getName());
        setIcon(UIManager.getIcon("Tree.leafIcon"));

        if (selected) {
            setOpaque(true);
            setBackground(UIManager.getColor("List.selectionBackground"));
            setForeground(UIManager.getColor("List.selectionForeground"));
        } else {
            setOpaque(false);
            setBackground(UIManager.getColor("List.background"));
            setForeground(UIManager.getColor("List.foreground"));
        }

        return this;
    }
}
