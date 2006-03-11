/*
 * $Id: StringEditorFactory.java,v 1.1.1.1 2004/09/18 17:16:19 amunoz Exp $
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

package tk.tomby.tedit.gui.editors;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.ObjectCreationFactory;

import org.xml.sax.Attributes;

import tk.tomby.tedit.core.preferences.IEditor;


/**
 * DOCUMENT ME!
 *
 * @author $Author: amunoz $
 * @version $Revision: 1.1.1.1 $
 */
public class StringEditorFactory implements ObjectCreationFactory {
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
    public Object createObject(Attributes attrs)
                        throws Exception {
        String label = attrs.getValue("label");
        String def = attrs.getValue("default");
        String key   = attrs.getValue("key");

        IEditor editor = new StringEditor();
        editor.setLabel(label);
        editor.setDefault(def);
        editor.setKey(key);

        return editor;
    }
}
