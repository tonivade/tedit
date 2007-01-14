/*
 * $Id: WorkspaceManager.java,v 1.1.1.1 2004/09/18 17:16:21 amunoz Exp $
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

package tk.tomby.tedit.services;

import java.awt.BorderLayout;

import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tk.tomby.tedit.core.IBuffer;
import tk.tomby.tedit.core.IStatusBar;
import tk.tomby.tedit.core.IWorkspace;
import tk.tomby.tedit.core.MainFrame;
import tk.tomby.tedit.core.StatusBar;
import tk.tomby.tedit.core.Workspace;
import tk.tomby.tedit.core.popupmenu.PopupMenu;
import tk.tomby.tedit.core.popupmenu.PopupMenuDriver;
import tk.tomby.tedit.core.preferences.IPreferences;
import tk.tomby.tedit.core.preferences.PreferencesPane;

import tk.tomby.tedit.gui.Menu;

import tk.tomby.tedit.plugins.IPlugin;


/**
 * DOCUMENT ME!
 *
 * @author $Author: amunoz $
 * @version $Revision: 1.1.1.1 $
 */
public class WorkspaceManager {
    //~ Static fields/initializers *****************************************************************

    /** DOCUMENT ME! */
    private static Log log = LogFactory.getLog(WorkspaceManager.class);

    /** DOCUMENT ME! */
    private static WorkspaceManager instance = new WorkspaceManager();

    /** DOCUMENT ME! */
    public static final int PLUGIN_WORKSPACE_POSITION = 1;

    /** DOCUMENT ME! */
    public static final int PLUGIN_STATUSBAR_POSITION = 2;

    /** DOCUMENT ME! */
    public static final int PLUGIN_MAIN_POSITION = 3;

    //~ Instance fields ****************************************************************************

    /** DOCUMENT ME! */
    private IPreferences preferences = null;

    /** DOCUMENT ME! */
    private IStatusBar statusBar = null;

    /** DOCUMENT ME! */
    private IWorkspace workspace = null;

    /** DOCUMENT ME! */
    private MainFrame main  = null;

    /** DOCUMENT ME! */
    private PopupMenu popup = null;

    //~ Methods ************************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static List<IBuffer> getBufferList() {
        return instance.workspace.getBufferList();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static IBuffer getCurrentBuffer() {
        return instance.workspace.getCurrentBuffer();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static JFrame getMainFrame() {
        return instance.main;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static PopupMenu getPopupMenu() {
        return instance.popup;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static IPreferences getPreferences() {
        return instance.preferences;
    }

    /**
     * DOCUMENT ME!
     *
     * @param buffer DOCUMENT ME!
     */
    public static void addBuffer(IBuffer buffer) {
        instance.workspace.addBuffer(buffer);
    }

    /**
     * DOCUMENT ME!
     *
     * @param menu DOCUMENT ME!
     */
    public static void addMenu(Menu menu) {
        instance.main.getJMenuBar().add(menu);
    }

    /**
     * DOCUMENT ME!
     *
     * @param location DOCUMENT ME!
     * @param plugin DOCUMENT ME!
     */
    public static void addPlugin(int     location,
                                 IPlugin plugin) {
        switch (location) {
            case PLUGIN_WORKSPACE_POSITION:
                instance.workspace.addPlugin(plugin);

                break;

            case PLUGIN_STATUSBAR_POSITION:
                instance.statusBar.addPlugin(plugin);

                break;

            case PLUGIN_MAIN_POSITION:

                //TODO: plugin
                break;

            default:
                log.warn("invalid position");
        }
    }

    /**
     * DOCUMENT ME!
     */
    public static void closeCurrentBuffer() {
        instance.workspace.closeCurrentBuffer();
    }

    /**
     * DOCUMENT ME!
     */
    public static void load() {
        instance.main            = new MainFrame();
        instance.preferences     = new PreferencesPane();
        instance.workspace       = new Workspace();
        instance.statusBar       = new StatusBar();
        instance.popup           = new PopupMenuDriver().create();

        instance.main.getContentPane().add(BorderLayout.CENTER, (JComponent) instance.workspace);
        instance.main.getContentPane().add(BorderLayout.SOUTH, (JComponent) instance.statusBar);
        
        instance.workspace.init();
    }

    /**
     * DOCUMENT ME!
     */
    public static void save() {
        instance.main.save();
        instance.workspace.save();
    }
}
