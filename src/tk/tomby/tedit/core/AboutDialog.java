/*
 * $Id: AboutDialog.java,v 1.1.1.1 2004/09/18 17:16:19 amunoz Exp $
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

package tk.tomby.tedit.core;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

import tk.tomby.tedit.Main;

import tk.tomby.tedit.services.ResourceManager;
import tk.tomby.tedit.services.WorkspaceManager;


/**
 * DOCUMENT ME!
 *
 * @author $Author: amunoz $
 * @version $Revision: 1.1.1.1 $
 */
public class AboutDialog extends JDialog {
    //~ Constructors *******************************************************************************

    /**
     * Creates a new SaveChangesDialog object.
     */
    public AboutDialog() {
        super(WorkspaceManager.getMainFrame(), ResourceManager.getProperty("main.about.title"), true);

        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        Toolkit toolkit     = Toolkit.getDefaultToolkit();
        Container container = getContentPane();

        JLabel image = new JLabel(ResourceManager.getIcon("main.splash.image"));
        image.setOpaque(true);
        image.setBackground(Color.BLACK);
        image.setMinimumSize(new Dimension(240, 280));

        JTextArea text = new JTextArea();
        text.append(Main.NAME);
        text.append(" ");
        text.append(Main.VERSION);
        text.append("\n");
        text.append("Aug, 2004\n");
        text.append("\n");
        text.append("tEdit was written by Antonio G. Muñoz Conejo, amunoz AT tomby.homelinux.org.\n");
        text.append("\n");
        text.append("tEdit is free software; you can redistribute it and/or modify it");
        text.append(" under the of the GNU General Public License as published by the");
        text.append(" Free Software Foundation. either version 2 of the License, or");
        text.append(" (at your option) any later version.");

        text.setEditable(false);
        text.setWrapStyleWord(true);
        text.setLineWrap(true);

        text.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, image, text);
        splitPane.setDividerSize(0);

        container.add(BorderLayout.CENTER, splitPane);

        Dimension screenSize = toolkit.getScreenSize();

        this.setSize(480, 480);
        this.setLocation((screenSize.width / 2) - 240, (screenSize.height / 2) - 240);

        this.setVisible(true);
    }
}
