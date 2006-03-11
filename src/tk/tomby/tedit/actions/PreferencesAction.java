/*
 * $Id: PreferencesAction.java,v 1.1.1.1 2004/09/18 17:16:20 amunoz Exp $
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

package tk.tomby.tedit.actions;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;

import tk.tomby.tedit.core.preferences.IPreferences;

import tk.tomby.tedit.messages.StatusMessage;

import tk.tomby.tedit.services.MessageManager;
import tk.tomby.tedit.services.ResourceManager;
import tk.tomby.tedit.services.WorkspaceManager;


/**
 * DOCUMENT ME!
 *
 * @author $Author: amunoz $
 * @version $Revision: 1.1.1.1 $
 */
public class PreferencesAction extends AbstractAction {
    //~ Constructors *******************************************************************************

    /**
     * Creates a new ShowPreferencesAction object.
     */
    public PreferencesAction() {
        super("preferences");
    }

    //~ Methods ************************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        MessageManager.sendMessage(MessageManager.STATUS_GROUP_NAME,
                                   new StatusMessage(this, "PreferencesAction"));

        final JDialog dialog =
            new JDialog(WorkspaceManager.getMainFrame(),
                        ResourceManager.getProperty("preferences.title"), true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setResizable(true);

        final IPreferences preferences = WorkspaceManager.getPreferences();

        dialog.getContentPane().add((JComponent) preferences, BorderLayout.CENTER);

        JPanel buttons = new JPanel();
        JButton accept = new JButton(ResourceManager.getProperty("preferences.accept"));
        accept.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    preferences.commit();
                    dialog.setVisible(false);
                    dialog.dispose();
                }
            });

        JButton cancel = new JButton(ResourceManager.getProperty("preferences.cancel"));
        cancel.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    preferences.restore();
                    dialog.setVisible(false);
                    dialog.dispose();
                }
            });
        buttons.add(accept);
        buttons.add(cancel);

        dialog.getContentPane().add(buttons, BorderLayout.SOUTH);

        Toolkit toolkit      = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();

        dialog.setSize(new Dimension(640, 480));
        dialog.setLocation((screenSize.width / 2) - 320, (screenSize.height / 2) - 240);
        dialog.setVisible(true);
    }
}
