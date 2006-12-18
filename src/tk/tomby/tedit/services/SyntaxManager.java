/*
 * $Id: SyntaxManager.java,v 1.1.1.1 2004/09/18 17:16:21 amunoz Exp $
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

import tk.tomby.tedit.syntax.Syntax;
import tk.tomby.tedit.syntax.SyntaxDriver;


/**
 * DOCUMENT ME!
 *
 * @author $Author: amunoz $
 * @version $Revision: 1.1.1.1 $
 */
public class SyntaxManager {
    //~ Static fields/initializers *****************************************************************

    /** DOCUMENT ME! */
    private static Map<String, Syntax> syntaxPool = new HashMap<String, Syntax>();

    //~ Methods ************************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @param stateName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Syntax.State getState(String stateName) {
        int index = stateName.indexOf(':');

        Syntax syntax = getSyntax(stateName.substring(0, index));

        return syntax.getState(stateName.substring(index + 1));
    }

    /**
     * DOCUMENT ME!
     *
     * @param syntaxName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Syntax getSyntax(String syntaxName) {
        Syntax syntax = syntaxPool.get(syntaxName);

        if (syntax == null) {
            syntax = createSyntax(syntaxName);
        }

        return syntax;
    }

    /**
     * DOCUMENT ME!
     *
     * @param syntaxName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Syntax createSyntax(String syntaxName) {
        Syntax syntax = new SyntaxDriver().create(syntaxName);

        if (syntax != null) {
            syntaxPool.put(syntaxName, syntax);
        }

        return syntax;
    }
}
