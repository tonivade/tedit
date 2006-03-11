/*
 * $Id: MainFrame.java,v 1.4 2004/11/28 15:33:11 amunoz Exp $
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
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import tk.tomby.tedit.Main;

import tk.tomby.tedit.core.menubar.MenuBarDriver;
import tk.tomby.tedit.core.toolbar.ToolBarDriver;

import tk.tomby.tedit.messages.ActivationMessage;
import tk.tomby.tedit.messages.IMessageListener;
import tk.tomby.tedit.messages.WorkspaceMessage;

import tk.tomby.tedit.services.ActionManager;
import tk.tomby.tedit.services.MessageManager;
import tk.tomby.tedit.services.PreferenceManager;


/**
 * DOCUMENT ME!
 *
 * @author $Author: amunoz $
 * @version $Revision: 1.4 $
 */
public class MainFrame extends JFrame implements IMessageListener {
    //~ Constructors *******************************************************************************

    /**
     * Creates a new Main object.
     */
    public MainFrame() {
        super();

        MessageManager.addMessageListener(MessageManager.ACTIVATION_GROUP_NAME, this);
        MessageManager.addMessageListener(MessageManager.WORKSPACE_GROUP_NAME, this);

        this.setTitle(Main.NAME + " - " + Main.VERSION);

        this.setJMenuBar(new MenuBarDriver().create());

        this.getContentPane().add(BorderLayout.NORTH, new ToolBarDriver().create());

        Toolkit toolkit = Toolkit.getDefaultToolkit();

        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        this.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent evt) {
                    ActionManager.invokeAction("exit", null);
                }
            });

        Dimension screenSize = toolkit.getScreenSize();

        this.setSize(PreferenceManager.getInt("main.mainframe.size.width", screenSize.width),
                     PreferenceManager.getInt("main.mainframe.size.height", screenSize.height));
        this.setLocation(PreferenceManager.getInt("main.mainframe.location.width", 0),
                         PreferenceManager.getInt("main.mainframe.location.height", 0));
    }

    //~ Methods ************************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @param message DOCUMENT ME!
     */
    public void receiveMessage(ActivationMessage message) {
        SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    setVisible(true);
                }
            });
    }

    /**
     * DOCUMENT ME!
     *
     * @param message DOCUMENT ME!
     */
    public void receiveMessage(WorkspaceMessage message) {
        final IBuffer buffer = (IBuffer) message.getNewValue();

        if (buffer != null) {
            SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        setTitle(Main.NAME + " - " + Main.VERSION + " - " + buffer.getFileName());
                    }
                });
        } else {
            SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        setTitle(Main.NAME + " - " + Main.VERSION);
                    }
                });
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void save() {
        PreferenceManager.putInt("main.mainframe.size.width", getSize().width);
        PreferenceManager.putInt("main.mainframe.size.height", getSize().height);
        PreferenceManager.putInt("main.mainframe.location.width", getLocation().x);
        PreferenceManager.putInt("main.mainframe.location.height", getLocation().y);
    }
}
