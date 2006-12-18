/*
 * $Id: GenericDriver.java,v 1.1.1.1 2004/09/18 17:16:19 amunoz Exp $
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
 * @author $Author: amunoz $
 * @version $Revision: 1.1.1.1 $
 */
public abstract class GenericDriver {
    //~ Static fields/initializers *****************************************************************

    /** DOCUMENT ME! */
    private static Log log = LogFactory.getLog(GenericDriver.class);

    //~ Instance fields ****************************************************************************

    /** DOCUMENT ME! */
    private String rulesFile = null;

    //~ Constructors *******************************************************************************

    /**
     * Creates a new GenericDriver object.
     *
     * @param rulesFile DOCUMENT ME!
     */
    public GenericDriver(String rulesFile) {
        this.rulesFile = rulesFile;
    }

    //~ Methods ************************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @param resourceFile DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected Object createObject(String resourceFile) {
        Object object = null;

        try {
            ClassLoader loader = this.getClass().getClassLoader();

            URL rules         = loader.getResource(rulesFile);
            InputStream input = loader.getResourceAsStream(resourceFile);

            if (input != null) {
                Digester digester = DigesterLoader.createDigester(rules);

                object = digester.parse(input);
            }
        } catch (SAXException e) {
            log.error("error in object creation", e);
        } catch (IOException e) {
            log.error("error in object creation", e);
        }
        
        return object;
    }
}
