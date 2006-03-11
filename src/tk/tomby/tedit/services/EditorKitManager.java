/*
 * $Id: EditorKitManager.java,v 1.1.1.1 2004/09/18 17:16:21 amunoz Exp $
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

import javax.swing.text.EditorKit;

import tk.tomby.tedit.editorkits.HighlightPlainEditorKit;
import tk.tomby.tedit.editorkits.PlainEditorKit;

import tk.tomby.tedit.syntax.Syntax;
import tk.tomby.tedit.syntax.catalog.Catalog;
import tk.tomby.tedit.syntax.catalog.CatalogDriver;


/**
 * DOCUMENT ME!
 *
 * @author $Author: amunoz $
 * @version $Revision: 1.1.1.1 $
 */
public class EditorKitManager {
    //~ Static fields/initializers *****************************************************************

    /** DOCUMENT ME! */
    private static Catalog catalog = new CatalogDriver().create();

    //~ Methods ************************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @param extension DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static EditorKit createEditorKit(String extension) {
        String syntaxName = catalog.getSyntaxName(extension);

        Syntax syntax = SyntaxManager.getSyntax(syntaxName);

        EditorKit editorKit = null;

        if (syntax != null) {
            editorKit = new HighlightPlainEditorKit(syntax);
        }

        return (editorKit != null) ? editorKit : new PlainEditorKit();
    }
}
