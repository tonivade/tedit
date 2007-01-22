/*
 * $Id: Main.java,v 1.1.1.1 2004/09/18 17:16:18 amunoz Exp $
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

package tk.tomby.tedit;

import java.io.File;

import javax.swing.RepaintManager;

import tk.tomby.tedit.core.BufferFactory;
import tk.tomby.tedit.core.IBuffer;
import tk.tomby.tedit.core.SplashWindow;
import tk.tomby.tedit.core.ThreadSafeRepaintManager;

import tk.tomby.tedit.messages.ActivationMessage;

import tk.tomby.tedit.plugins.PluginLoader;

import tk.tomby.tedit.services.MessageManager;
import tk.tomby.tedit.services.WorkspaceManager;


/**
 * DOCUMENT ME!
 *
 * @author $Author: amunoz $
 * @version $Revision: 1.1.1.1 $
 */
public class Main {
    //~ Static fields/initializers *****************************************************************

    /** DOCUMENT ME! */
    public static final String NAME = "tEdit";

    /** DOCUMENT ME! */
    public static final String VERSION = "0.1.2";

    //~ Methods ************************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        RepaintManager.setCurrentManager(new ThreadSafeRepaintManager());

        new SplashWindow();

        WorkspaceManager.load();

        PluginLoader.load();

        BufferFactory factory = new BufferFactory();

        //basic CLI interface
        if (args.length == 0) {
            WorkspaceManager.addBuffer(factory.createBuffer());
        } else {
            for (int i = 0; i < args.length; i++) {
                IBuffer buffer = factory.createBuffer();

                buffer.open(new File(args[i]));

                WorkspaceManager.addBuffer(buffer);
            }
        }
        
        MessageManager.sendMessage(new ActivationMessage(WorkspaceManager.getMainFrame()));
    }
}
