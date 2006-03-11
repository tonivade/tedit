/*
 * $Id: Console.java,v 1.1.1.1 2004/09/18 17:16:22 amunoz Exp $
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

package tk.tomby.tedit.plugins.console;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;

import tk.tomby.tedit.core.IWorkspace;

import tk.tomby.tedit.messages.IMessage;
import tk.tomby.tedit.messages.IMessageListener;

import tk.tomby.tedit.plugins.AbstractDockablePlugin;

import tk.tomby.tedit.services.MessageManager;
import tk.tomby.tedit.services.PreferenceManager;
import tk.tomby.tedit.services.WorkspaceManager;


/**
 * DOCUMENT ME!
 *
 * @author tomby
 */
public class Console extends AbstractDockablePlugin implements IMessageListener {
    //~ Instance fields ****************************************************************************

    /** DOCUMENT ME! */
    private JTextArea console = null;

    //~ Constructors *******************************************************************************

    /**
     * Creates a new Console object.
     */
    public Console() {
        super();

        setLayout(new BorderLayout());

        PreferenceManager.loadCategory("console", "tk/tomby/tedit/plugins/console");

        location = PreferenceManager.getInt("console.location", IWorkspace.PLUGIN_BOTTOM);

        title.setText("Console");

        console = new JTextArea();

        console.setEditable(false);
        console.setFont(new Font("monospaced", Font.PLAIN, 12));

        JScrollPane scroll = new JScrollPane(console);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.getViewport().setScrollMode(JViewport.BLIT_SCROLL_MODE);

        add(title, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        MessageManager.addMessageListener(MessageManager.ACTIVATION_GROUP_NAME, this);
        MessageManager.addMessageListener(MessageManager.BUFFER_GROUP_NAME, this);
        MessageManager.addMessageListener(MessageManager.DEFAULT_GROUP_NAME, this);
        MessageManager.addMessageListener(MessageManager.STATUS_GROUP_NAME, this);
        MessageManager.addMessageListener(MessageManager.WORKSPACE_GROUP_NAME, this);
        MessageManager.addMessageListener(MessageManager.PREFERENCE_GROUP_NAME, this);
    }

    //~ Methods ************************************************************************************

    /**
     * DOCUMENT ME!
     */
    public void init() {
        WorkspaceManager.addPlugin(WorkspaceManager.PLUGIN_WORKSPACE_POSITION, this);
    }

    /**
     * DOCUMENT ME!
     *
     * @param message DOCUMENT ME!
     */
    public void receiveMessage(final IMessage message) {
        SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    console.append(message.getClass().getName());
                    console.append(":");
                    console.append(message.toString());
                    console.append("\n");
                }
            });
    }

    /**
     * DOCUMENT ME!
     */
    public void save() {
        PreferenceManager.putInt("console.location", location);
    }
}
