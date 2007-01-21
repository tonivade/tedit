/*
 * $Id: PluginLoader.java,v 1.1.1.1 2004/09/18 17:16:22 amunoz Exp $
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
    	String directory = getBaseDir();

        File fileDir = new File(directory);
        
        if (fileDir.exists()) {
			fromXML(fileDir);
        	fromJAR(fileDir);
        } else {
        	log.warn("plugins directory not exist: " + directory);
        }
    }

	public static String getBaseDir() {
		String home = System.getProperty("tedit.home");
    	if (home == null) {
    		home = System.getProperty("user.dir");
    	}
    	
        String directory =
            PreferenceManager.getString("main.plugins.directory", home + "/plugins");
		return directory;
	}

	private static void fromXML(File fileDir) {
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

			PluginDriver.createPluginFromXML(file);
		}
	}
	
	private static void fromJAR(File fileDir) {
		File[] files =
    		fileDir.listFiles(new FileFilter() {
    			public boolean accept(File pathname) {
    				return pathname.getName().endsWith(".jar");
    			}
    		});

    	for (int i = 0; i < files.length; i++) {
    		File file = files[i];

    		if (log.isDebugEnabled()) {
    			log.debug("loading plugin: " + file.getName());
    		}

    		PluginDriver.createPluginFromJAR(file);
    	}
	}

}
