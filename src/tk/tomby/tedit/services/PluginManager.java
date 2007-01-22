/*
 * $Id: PluginManager.java,v 1.1.1.1 2004/09/18 17:16:21 amunoz Exp $
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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tk.tomby.tedit.plugins.IPlugin;
import tk.tomby.tedit.plugins.IPluginDescriptor;
import tk.tomby.tedit.plugins.PluginClassLoader;


/**
 * DOCUMENT ME!
 *
 * @author $Author: amunoz $
 * @version $Revision: 1.1.1.1 $
 */
public class PluginManager {
    //~ Static fields/initializers *****************************************************************

    /** DOCUMENT ME! */
    private static Log log = LogFactory.getLog(PluginManager.class);

    /** DOCUMENT ME! */
    private static Map<String, IPlugin> plugins = new HashMap<String, IPlugin>();
    
    /** DOCUMENT ME! */
    private static Map<String, IPluginDescriptor> descriptors = new HashMap<String, IPluginDescriptor>();
    
    /** DOCUMENT ME! */
    private static Map<String, ClassLoader> loaders = new HashMap<String, ClassLoader>();

    //~ Methods ************************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static IPlugin getPlugin(String name) {
        return plugins.get(name);
    }
    
    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static IPluginDescriptor getDescriptor(String name) {
        return descriptors.get(name);
    }
    
    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static ClassLoader getLoader(String name) {
        return loaders.get(name);
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     * @param clazz DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static IPlugin createPlugin(IPluginDescriptor descriptor) {
        IPlugin plugin = null;

        try {
        	PluginClassLoader loader = new PluginClassLoader(descriptor, PluginManager.class.getClassLoader());
        	
        	if (descriptor.getPreferences() != null) {
        		PreferenceManager.loadCategory(descriptor.getPluginName(), descriptor.getPreferences());
        	}
        	
        	if (descriptor.getResources() != null) {
        		ResourceManager.loadCategory(descriptor.getPluginName(), descriptor.getResources(), loader);
        	}
        	
            plugin = loadPlugin(descriptor, loader);
            plugin.init();
            
            if (descriptor.getPluginType().equals(IPluginDescriptor.PLUGIN_WORKSPACE_TYPE)) {
            	WorkspaceManager.addPlugin(WorkspaceManager.PLUGIN_WORKSPACE_POSITION, plugin);
            } else if (descriptor.getPluginType().equals(IPluginDescriptor.PLUGIN_STATUS_TYPE)) {
            	WorkspaceManager.addPlugin(WorkspaceManager.PLUGIN_STATUSBAR_POSITION, plugin);
            }

            plugins.put(descriptor.getPluginName(), plugin);
            descriptors.put(descriptor.getPluginName(), descriptor);
            loaders.put(descriptor.getPluginName(), loader);
        } catch (InstantiationException e) {
            log.error(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            log.error(e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage(), e);
        }

        return plugin;
    }

	private static IPlugin loadPlugin(IPluginDescriptor descriptor, PluginClassLoader loader) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		Class clazz = loader.loadClass(descriptor.getPluginClass());
		return (IPlugin) clazz.newInstance();
	}

    /**
     * DOCUMENT ME!
     */
    public static void save() {
        for (IPlugin plugin : plugins.values()) {
            plugin.save();
        }
    }
}
