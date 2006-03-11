/*
 * $Id: PluginDriver.java,v 1.1.1.1 2004/09/18 17:16:22 amunoz Exp $
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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.net.URL;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.xmlrules.DigesterLoader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.xml.sax.SAXException;


/**
 * DOCUMENT ME!
 *
 * @author tomby
 */
public class PluginDriver {
    //~ Static fields/initializers *****************************************************************

    /** DOCUMENT ME! */
    private static final String RESOURCE_ROOT = "tk/tomby/tedit/plugins";

    /** DOCUMENT ME! */
    private static Log log = LogFactory.getLog(PluginDriver.class);

    //~ Methods ************************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @param pluginFile DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static IPlugin createPlugin(File pluginFile) {
        IPlugin temp = null;

        try {
            ClassLoader loader = PluginDriver.class.getClassLoader();

            URL rules         = loader.getResource(RESOURCE_ROOT + "/plugins-rules.xml");
            InputStream input = new FileInputStream(pluginFile);

            if (input != null) {
                Digester digester = DigesterLoader.createDigester(rules);

                temp = (IPlugin) digester.parse(input);
            }
        } catch (SAXException e) {
            log.error(e.getMessage());
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return temp;
    }
}
