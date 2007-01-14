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
import java.io.PrintStream;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;

import org.flexdock.docking.DockingConstants;

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
public class Console extends AbstractDockablePlugin implements IMessageListener<IMessage> {
    //~ Instance fields ****************************************************************************

    /** DOCUMENT ME! */
    private JTabbedPane container = null;
    
    private JTextArea messages = null;
    private JTextArea console = null;
    
    private JTextAreaOutputStream output = null;
    
    private ConsoleInterceptor out;
    private ConsoleInterceptor err;
    
    private PrintStream oldOut;
    private PrintStream oldErr;

    //~ Constructors *******************************************************************************

    /**
     * Creates a new Console object.
     */
    public Console() {
        super();

        setLayout(new BorderLayout());

        PreferenceManager.loadCategory("console", "tk/tomby/tedit/plugins/console");

        title.setText("Console");
        location = PreferenceManager.getInt("console.location", IWorkspace.PLUGIN_BOTTOM);
        region = PreferenceManager.getString("console.region", DockingConstants.CENTER_REGION);
        
        container = new JTabbedPane();
        
        add(title, BorderLayout.NORTH);
        add(container, BorderLayout.CENTER);
        
        messages = createArea();
        console = createArea();
        
        container.addTab("Messages", createConsole(messages));
        container.addTab("Console", createConsole(console));
        
        output = new JTextAreaOutputStream(console);
        
        out = new ConsoleInterceptor(System.out, output);
        err = new ConsoleInterceptor(System.err, output);

        MessageManager.addMessageListener(MessageManager.ACTIVATION_GROUP_NAME, this);
        MessageManager.addMessageListener(MessageManager.BUFFER_GROUP_NAME, this);
        MessageManager.addMessageListener(MessageManager.STATUS_GROUP_NAME, this);
        MessageManager.addMessageListener(MessageManager.WORKSPACE_GROUP_NAME, this);
        MessageManager.addMessageListener(MessageManager.PREFERENCE_GROUP_NAME, this);
    }

    //~ Methods ************************************************************************************
    
    private JTextArea createArea() {
    	JTextArea area = new JTextArea();
    	area.setEditable(false);
        area.setFont(new Font("monospaced", Font.PLAIN, 12));
        return area;
    }
    
    private JScrollPane createConsole(JTextArea area) {
        JScrollPane scroll = new JScrollPane(area);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.getViewport().setScrollMode(JViewport.BLIT_SCROLL_MODE);        
        return scroll;
    }

    /**
     * DOCUMENT ME!
     */
    public void init() {
        oldOut = System.out;
        System.setOut(out);
        
        oldErr = System.err;
        System.setErr(err);
        
        WorkspaceManager.addPlugin(WorkspaceManager.PLUGIN_WORKSPACE_POSITION, this);
    }
    
    /**
     * DOCUMENT ME!
     * 
     * @param title DOCUMENT ME!
     * @param component DOCUMENT ME!
     */
    public void addConsole(String title, JScrollPane component) {
    	container.addTab(title, component);
    }

    /**
     * DOCUMENT ME!
     *
     * @param message DOCUMENT ME!
     */
    public void receiveMessage(final IMessage message) {
        SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    messages.append(message.getClass().getName());
                    messages.append(":");
                    messages.append(message.toString());
                    messages.append("\n");
                }
            });
    }

    /**
     * DOCUMENT ME!
     */
    public void save() {
        PreferenceManager.putInt("console.location", location);
        PreferenceManager.putString("console.region", region);
        
        System.setOut(oldOut);
        System.setErr(oldErr);
    }
}
