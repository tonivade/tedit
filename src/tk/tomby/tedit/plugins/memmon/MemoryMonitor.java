/*
 * $Id: MemoryMonitor.java,v 1.2 2004/09/26 20:29:19 amunoz Exp $
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

package tk.tomby.tedit.plugins.memmon;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.Timer;

import tk.tomby.tedit.plugins.IPlugin;
import tk.tomby.tedit.services.ResourceManager;
import tk.tomby.tedit.services.TaskManager;
import tk.tomby.tedit.services.WorkspaceManager;


/**
 * DOCUMENT ME!
 *
 * @author $Author: amunoz $
 * @version $Revision: 1.2 $
 */
public class MemoryMonitor extends JLabel implements IPlugin {
    //~ Static fields/initializers *****************************************************************

    static {
        ResourceManager.loadCategory("memmon", "tk/tomby/tedit/plugins/memmon");
    }

    //~ Instance fields ****************************************************************************

    /** DOCUMENT ME! */
    private Timer timer = null;

    //~ Constructors *******************************************************************************

    /**
     * Creates a new MemoryMonitor object.
     */
    public MemoryMonitor() {
        super("", JLabel.RIGHT);

        addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent evt) {
                    if (evt.getClickCount() == 2) {
                        TaskManager.execute(new Runnable() {
                                    public void run() {
                                        Runtime.getRuntime().gc();
                                    }
                                });
                    }
                }
            });

        timer =
            new Timer(1000,
                      new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        Runtime runtime = Runtime.getRuntime();

                        long freeMemory  = runtime.freeMemory() / 1000;
                        long totalMemory = runtime.totalMemory() / 1000;
                        long usedMemory  = totalMemory - freeMemory;

                        setText(usedMemory + "/" + freeMemory + "/" + totalMemory);
                    }
                });

        timer.start();

        setToolTipText(ResourceManager.getProperty("memmon.tooltip.used") + "/"
                       + ResourceManager.getProperty("memmon.tooltip.free") + "/"
                       + ResourceManager.getProperty("memmon.tooltip.total"));
    }

    //~ Methods ************************************************************************************

    /**
     * DOCUMENT ME!
     */
    public void init() {
        WorkspaceManager.addPlugin(WorkspaceManager.PLUGIN_STATUSBAR_POSITION, this);
    }

    /**
     * DOCUMENT ME!
     */
    public void save() {
    }
}
