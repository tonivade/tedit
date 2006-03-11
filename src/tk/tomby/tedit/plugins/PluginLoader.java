/*
 * $Id: PluginLoader.java,v 1.1.1.1 2004/09/18 17:16:22 amunoz Exp $
 *
 * Copyright (C) 2003 Antonio G. Mu�oz Conejo <amunoz@tomby.homelinux.org>
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

import java.io.File;
import java.io.FileFilter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tk.tomby.tedit.services.PreferenceManager;


/**
 * DOCUMENT ME!
 *
 * @author $Author: amunoz $
 * @version $Revision: 1.1.1.1 $
 */
public class PluginLoader {
    //~ Static fields/initializers *****************************************************************

    /** DOCUMENT ME! */
    private static Log log = LogFactory.getLog(PluginLoader.class);

    //~ Methods ************************************************************************************

    /**
     * DOCUMENT ME!
     */
    public static void load() {
        String directory =
            PreferenceManager.getString("main.plugins.directory",
                                        System.getProperty("tedit.home") + "/plugins");

        File fileDir = new File(directory);

        File[] files =
            fileDir.listFiles(new FileFilter() {
                    public boolean accept(File pathname) {
                        return pathname.getName().endsWith(".xml");
                    }
                });

        for (int i = 0; i < files.length; i++) {
            File file = files[i];

            if (log.isDebugEnabled()) {
                log.debug("loading plugin: " + file.getName());
            }

            IPlugin plugin = PluginDriver.createPlugin(file);

            plugin.init();
        }
    }
}
