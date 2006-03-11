/*
 * $Id: SplashWindow.java,v 1.1.1.1 2004/09/18 17:16:19 amunoz Exp $
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
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JLabel;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;

import tk.tomby.tedit.messages.ActivationMessage;
import tk.tomby.tedit.messages.IMessageListener;

import tk.tomby.tedit.services.MessageManager;
import tk.tomby.tedit.services.ResourceManager;


/**
 * DOCUMENT ME!
 *
 * @author $Author: amunoz $
 * @version $Revision: 1.1.1.1 $
 */
public class SplashWindow extends JWindow implements IMessageListener {
    //~ Constructors *******************************************************************************

    /**
     * Creates a new Splash object.
     */
    public SplashWindow() {
        super();

        MessageManager.addMessageListener(MessageManager.ACTIVATION_GROUP_NAME, this);

        Toolkit toolkit     = Toolkit.getDefaultToolkit();
        Container container = getContentPane();

        container.add(BorderLayout.CENTER, new JLabel(ResourceManager.getIcon("main.splash.image")));

        Dimension screenSize = toolkit.getScreenSize();

        this.setSize(253, 236);
        this.setLocation((screenSize.width / 2) - 127, (screenSize.height / 2) - 118);

        this.setVisible(true);
    }

    //~ Methods ************************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @param message DOCUMENT ME!
     */
    public void receiveMessage(ActivationMessage message) {
        MessageManager.removeMessageListener(this);
        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                setVisible(false);
                dispose();
            }
        });
        
    }
}
