/*
 * $Id: PluginDockingPort.java,v 1.1.1.1 2004/09/18 17:16:22 amunoz Exp $
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

package tk.tomby.tedit.plugins;

import java.awt.Component;

import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import net.eleritec.docking.Dockable;
import net.eleritec.docking.DockingPort;
import net.eleritec.docking.defaults.DefaultDockingPort;
import net.eleritec.docking.defaults.SubComponentProvider;

import tk.tomby.tedit.core.IWorkspace;

import tk.tomby.tedit.services.PreferenceManager;


/**
 * DOCUMENT ME!
 *
 * @author $Author: amunoz $
 * @version $Revision: 1.1.1.1 $
 */
public class PluginDockingPort extends DefaultDockingPort {
    //~ Instance fields ****************************************************************************

    /** DOCUMENT ME! */
    private int location = IWorkspace.PLUGIN_LEFT;

    //~ Constructors *******************************************************************************

    /**
     *
     */
    public PluginDockingPort() {
        super();

        setComponentProvider(new PluginComponentProvider());
    }

    /**
     * Creates a new PluginDockingPort object.
     *
     * @param location DOCUMENT ME!
     */
    public PluginDockingPort(int location) {
        this();

        this.location = location;
    }

    //~ Methods ************************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @param comp DOCUMENT ME!
     * @param desc DOCUMENT ME!
     * @param region DOCUMENT ME!
     * @param resizable DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean dock(Component comp,
                        String    desc,
                        String    region,
                        boolean   resizable) {
        IDockablePlugin plugin = (IDockablePlugin) comp;

        plugin.setDockLocation(location);

        return super.dock(comp, desc, region, resizable);
    }

    /**
     * DOCUMENT ME!
     *
     * @param dockable DOCUMENT ME!
     * @param region DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean dock(Dockable dockable,
                        String   region) {
        if (dockable == null) {
            return false;
        }

        return dock(dockable.getDockable(), dockable.getDockableDesc(), region,
                    dockable.isDockedLayoutResizable());
    }

    /**
     * DOCUMENT ME!
     *
     * @param plugin DOCUMENT ME!
     * @param region DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean dock(IDockablePlugin plugin,
                        String          region) {
        return dock(plugin.getDockable(), region);
    }

    //~ Inner Classes ******************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @author $Author: amunoz $
     * @version $Revision: 1.1.1.1 $
     */
    private class PluginComponentProvider implements SubComponentProvider {
        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public double getInitialDividerLocation() {
            return 0.5d;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public DockingPort createChildPort() {
            return new PluginDockingPort();
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public JSplitPane createSplitPane() {
            JSplitPane splitPane = new JSplitPane();

            return splitPane;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public JTabbedPane createTabbedPane() {
            JTabbedPane tabbedPane = new JTabbedPane();

            if (PreferenceManager.getInt("general.appearance.tabPosition",
                                             IWorkspace.TAB_POSITION_TOP) == IWorkspace.TAB_POSITION_TOP) {
                tabbedPane.setTabPlacement(JTabbedPane.TOP);
            } else {
                tabbedPane.setTabPlacement(JTabbedPane.BOTTOM);
            }

            return tabbedPane;
        }
    }
}
