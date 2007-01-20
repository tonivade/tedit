/*
 * $Id: PluginFactory.java,v 1.1.1.1 2004/09/18 17:16:22 amunoz Exp $
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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.ObjectCreationFactory;
import org.xml.sax.Attributes;


/**
 * DOCUMENT ME!
 *
 * @author $Author: amunoz $
 * @version $Revision: 1.1.1.1 $
 */
public class PluginFactory implements ObjectCreationFactory {
    //~ Instance fields ****************************************************************************

    /** DOCUMENT ME! */
    private Digester digester = null;

    //~ Methods ************************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @param digester DOCUMENT ME!
     */
    public void setDigester(Digester digester) {
        this.digester = digester;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Digester getDigester() {
        return digester;
    }

    /**
     * DOCUMENT ME!
     *
     * @param attrs DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public Object createObject(Attributes attrs) throws Exception {
        String name = attrs.getValue("name");
        String type = attrs.getValue("type");
        String clazz = attrs.getValue("class");

        return new PluginDescriptorImpl(name, type, clazz);
    }
    
    public class PluginDescriptorImpl implements IPluginDescriptor {
    	
    	private String name;
    	private String type;
    	private String clazz;
    	
    	private String path;
    	
    	private Map<String, IPluginLibrary> libraries;
    	
    	public PluginDescriptorImpl(String name, String type, String clazz) {
    		this.name = name;
    		this.type = type;
    		this.clazz = clazz;
    		this.libraries = new HashMap<String, IPluginLibrary>();
    	}
		
		public Collection<IPluginLibrary> getLibraries() {
            return libraries.values();
		}
		
		public IPluginLibrary getLibrary(String name) {
			return libraries.get(name);
		}
		
		public void addLibrary(IPluginLibrary lib) {
			libraries.put(lib.getName(), lib);
		}

		public String getPluginClass() {
			return clazz;
		}

		public String getPluginName() {
			return name;
		}
		
		public String getPluginType() {
			return type;
		}
    	
		public void setPluginPath(String path) {
			this.path = path;
		}
		
		public String getPluginPath() {
			return path;
		}
    }
}
