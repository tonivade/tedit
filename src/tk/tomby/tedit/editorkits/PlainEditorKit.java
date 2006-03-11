/*
 * $Id: PlainEditorKit.java,v 1.1.1.1 2004/09/18 17:16:21 amunoz Exp $
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

package tk.tomby.tedit.editorkits;

import java.io.IOException;
import java.io.Reader;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.PlainView;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;


/**
 * DOCUMENT ME!
 *
 * @author $Author: amunoz $
 * @version $Revision: 1.1.1.1 $
 */
public class PlainEditorKit extends DefaultEditorKit implements ViewFactory {
    //~ Constructors *******************************************************************************

    /**
     * Creates a new PlainEditorKit object.
     */
    public PlainEditorKit() {
        super();
    }

    //~ Methods ************************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ViewFactory getViewFactory() {
        return this;
    }

    /**
     * DOCUMENT ME!
     *
     * @param element DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public View create(Element element) {
        return new PlainView(element);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Document createDefaultDocument() {
        return new PlainDocument();
    }

    /**
     * DOCUMENT ME!
     *
     * @param in DOCUMENT ME!
     * @param doc DOCUMENT ME!
     * @param pos DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws BadLocationException DOCUMENT ME!
     */
    public void read(Reader   in,
                     Document doc,
                     int      pos)
              throws IOException, BadLocationException {
        char[] buffer = new char[4096];
        int count     = 0;
        int lastPos   = pos;

        while ((count = in.read(buffer, 0, buffer.length)) != -1) {
            doc.insertString(lastPos, new String(buffer, 0, count), null);

            lastPos = doc.getLength() - pos;
        }
    }
}
