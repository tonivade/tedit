/*
 * $Id: Syntax.java,v 1.1.1.1 2004/09/18 17:16:21 amunoz Exp $
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

package tk.tomby.tedit.syntax.catalog;

import java.util.ArrayList;
import java.util.List;


/**
 * DOCUMENT ME!
 *
 * @author $Author: amunoz $
 * @version $Revision: 1.1.1.1 $
 */
public class Syntax {
    //~ Instance fields ****************************************************************************

    /** DOCUMENT ME! */
    private List<Extension> extensions = new ArrayList<Extension>();

    /** DOCUMENT ME! */
    private String file = "";

    /** DOCUMENT ME! */
    private String name = "";

    //~ Methods ************************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public List<Extension> getExtensions() {
        return extensions;
    }

    /**
     * DOCUMENT ME!
     *
     * @param file The file to set.
     */
    public void setFile(String file) {
        this.file = file;
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the file.
     */
    public String getFile() {
        return file;
    }

    /**
     * DOCUMENT ME!
     *
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * DOCUMENT ME!
     *
     * @param extension DOCUMENT ME!
     */
    public void addExtension(Extension extension) {
        extensions.add(extension);
    }
}
