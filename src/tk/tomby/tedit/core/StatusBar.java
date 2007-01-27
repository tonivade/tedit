/*
 * $Id: StatusBar.java,v 1.1.1.1 2004/09/18 17:16:19 amunoz Exp $
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

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import tk.tomby.tedit.messages.BufferMessage;
import tk.tomby.tedit.messages.IMessageListener;
import tk.tomby.tedit.messages.StatusMessage;
import tk.tomby.tedit.messages.WorkspaceMessage;

import tk.tomby.tedit.plugins.IPlugin;

import tk.tomby.tedit.services.MessageManager;
import tk.tomby.tedit.services.ResourceManager;
import tk.tomby.tedit.services.WorkspaceManager;


/**
 * DOCUMENT ME!
 *
 * @author $Author: amunoz $
 * @version $Revision: 1.1.1.1 $
 */
public class StatusBar extends JPanel implements IStatusBar {
    //~ Instance fields ****************************************************************************

    /** DOCUMENT ME! */
    private JLabel lines = null;

    /** DOCUMENT ME! */
    private JLabel state = null;

    /** DOCUMENT ME! */
    private JLabel status = null;

    //~ Constructors *******************************************************************************

    /**
     * Creates a new MenuBar object.
     */
    public StatusBar() {
        super();

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));

        MessageManager.addMessageListener(MessageManager.STATUS_GROUP_NAME, new IMessageListener<StatusMessage>() {
        	public void receiveMessage(StatusMessage message) {
        		StatusBar.this.receiveMessage(message);
        	}
        });
        MessageManager.addMessageListener(MessageManager.BUFFER_GROUP_NAME, new IMessageListener<BufferMessage>() {
        	public void receiveMessage(BufferMessage message) {
        		StatusBar.this.receiveMessage(message);
        	}
        });
        MessageManager.addMessageListener(MessageManager.WORKSPACE_GROUP_NAME, new IMessageListener<WorkspaceMessage>() {
        	public void receiveMessage(WorkspaceMessage message) {
        		StatusBar.this.receiveMessage(message);
        	}
        });

        status = new JLabel(ResourceManager.getProperty("main.statusbar.initial"), JLabel.LEFT);
        status.setPreferredSize(new Dimension(200, 0));
        state = new JLabel();
        state.setPreferredSize(new Dimension(100, 0));
        lines = new JLabel();
        lines.setPreferredSize(new Dimension(100, 0));

        this.add(status);
        this.add(Box.createRigidArea(new Dimension(30, 0)));
        this.add(state);
        this.add(Box.createRigidArea(new Dimension(10, 0)));
        this.add(lines);
        this.add(Box.createHorizontalGlue());
    }

    //~ Methods ************************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @param plugin DOCUMENT ME!
     */
    public void addPlugin(IPlugin plugin) {
        this.add((JComponent) plugin);
    }

    /**
     * DOCUMENT ME!
     *
     * @param message DOCUMENT ME!
     */
    public void receiveMessage(final StatusMessage message) {
        SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    status.setText((String) message.getNewValue());
                }
            });
    }

    /**
     * DOCUMENT ME!
     *
     * @param message DOCUMENT ME!
     */
    public void receiveMessage(BufferMessage message) {
        SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    update();
                }
            });
    }

    /**
     * DOCUMENT ME!
     *
     * @param message DOCUMENT ME!
     */
    public void receiveMessage(WorkspaceMessage message) {
        SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    update();
                }
            });
    }

    /**
     * DOCUMENT ME!
     */
    private void update() {
        IBuffer buffer = WorkspaceManager.getCurrentBuffer();

        if (buffer != null) {
            if (buffer.isReadOnly()) {
                state.setText(ResourceManager.getProperty("main.statusbar.state.readonly"));
            } else {
                state.setText(ResourceManager.getProperty("main.statusbar.state.writable"));
            }

            lines.setText((buffer.getCurrentLine() + 1) + " : " + (buffer.getCurrentColumn() + 1));
        } else {
            state.setText(ResourceManager.getProperty("main.statusbar.state.nofiles"));
            lines.setText("0 : 0");
        }
    }
}
