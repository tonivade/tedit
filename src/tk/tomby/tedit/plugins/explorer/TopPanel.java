/*
 * $Id: TopPanel.java,v 1.4 2004/11/28 13:00:55 amunoz Exp $
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

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tk.tomby.tedit.gui.ToolBarButton;

import tk.tomby.tedit.plugins.explorer.actions.RefreshAction;
import tk.tomby.tedit.plugins.explorer.actions.UpDirectoryAction;


/**
 * DOCUMENT ME!
 *
 * @author $Author: amunoz $
 * @version $Revision: 1.4 $
 */
public class TopPanel extends JPanel {
    //~ Static fields/initializers *****************************************************************

    /** DOCUMENT ME! */
    private static Log log = LogFactory.getLog(TopPanel.class);

    //~ Instance fields ****************************************************************************

    /** DOCUMENT ME! */
    private JComboBox combo = null;

    /** DOCUMENT ME! */
    private JToolBar buttons = null;

    /** DOCUMENT ME! */
    private ShortedComboBoxModel comboModel = null;

    //~ Constructors *******************************************************************************

    /**
     * Creates a new TopPanel object.
     *
     * @param rootDirectory DOCUMENT ME!
     */
    public TopPanel(String rootDirectory) {
        super();

        setLayout(new BorderLayout());

        comboModel = new ShortedComboBoxModel(new ToStringComparator());
        comboModel.addElement(rootDirectory);
        comboModel.setSelectedItem(rootDirectory);

        combo = new JComboBox(comboModel);
        combo.setEditable(true);
        combo.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String directory = (String) combo.getSelectedItem();
                    comboModel.addElement(directory);
                }
            });

        ToolBarButton refresh = new ToolBarButton();
        refresh.setIconKey("explorer.top.refresh.icon");
        refresh.setToolTipKey("explorer.top.refresh.tooltip");
        refresh.setAction(new RefreshAction());

        ToolBarButton updir = new ToolBarButton();
        updir.setIconKey("explorer.top.updir.icon");
        updir.setToolTipKey("explorer.top.updir.tooltip");
        updir.setAction(new UpDirectoryAction(this));

        buttons = new JToolBar();
        buttons.setFloatable(false);
        buttons.add(updir);
        buttons.add(refresh);

        this.add(BorderLayout.NORTH, buttons);
        this.add(BorderLayout.CENTER, combo);
    }

    //~ Methods ************************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @param directory The directory to set.
     */
    public void setDirectory(String directory) {
        if (log.isDebugEnabled()) {
            log.debug("Setting directory to " + directory);
        }

        comboModel.addElement(directory);
        comboModel.setSelectedItem(directory);
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the directory.
     */
    public String getDirectory() {
        return (String) comboModel.getSelectedItem();
    }

    /**
     * DOCUMENT ME!
     *
     * @param listener DOCUMENT ME!
     */
    public void addActionListener(ActionListener listener) {
        combo.addActionListener(listener);
    }
}
