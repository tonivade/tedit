/*
 * $Id: Menu.java,v 1.1.1.1 2004/09/18 17:16:18 amunoz Exp $
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

package tk.tomby.tedit.gui;

import javax.swing.JMenu;

import tk.tomby.tedit.services.ResourceManager;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1.1.1 $
 */
public class Menu extends JMenu {
    //~ Constructors *******************************************************************************

    /**
     * Creates a new Menu object.
     */
    public Menu() {
        super();
    }

    //~ Methods ************************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @param mnemonicKey DOCUMENT ME!
     */
    public void setMnemonicKey(String mnemonicKey) {
        String mnemonic = ResourceManager.getProperty(mnemonicKey);

        if (mnemonic != null) {
            this.setMnemonic(mnemonic.charAt(0));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param textKey DOCUMENT ME!
     */
    public void setTextKey(String textKey) {
        this.setText(ResourceManager.getProperty(textKey));
    }

    /**
     * DOCUMENT ME!
     *
     * @param item DOCUMENT ME!
     */
    public void addMenuItem(MenuItem item) {
        this.add(item);
    }

    /**
     * DOCUMENT ME!
     *
     * @param separator DOCUMENT ME!
     */
    public void addMenuSeparator(MenuSeparator separator) {
        this.add(separator);
    }
}
