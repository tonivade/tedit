/*
 * $Id: MultitabPreferencePage.java,v 1.1.1.1 2004/09/18 17:16:20 amunoz Exp $
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

package tk.tomby.tedit.core.preferences;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import tk.tomby.tedit.services.ResourceManager;


/**
 * DOCUMENT ME!
 *
 * @author $Author: amunoz $
 * @version $Revision: 1.1.1.1 $
 */
public class MultitabPreferencePage extends JPanel implements IMultitabPreferencePage {
    //~ Instance fields ****************************************************************************

    /** DOCUMENT ME! */
    private JTabbedPane tabs = null;

    /** DOCUMENT ME! */
    private String label = null;

    //~ Constructors *******************************************************************************

    /**
     * Creates a new MultitabPreferencePage object.
     */
    public MultitabPreferencePage() {
        super();

        setLayout(new BorderLayout());

        tabs = new JTabbedPane();

        add(tabs, BorderLayout.CENTER);
    }

    //~ Methods ************************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @param label DOCUMENT ME!
     */
    public void setLabel(String label) {
        this.label = ResourceManager.getProperty(label);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getLabel() {
        return label;
    }

    /**
     * DOCUMENT ME!
     *
     * @param page DOCUMENT ME!
     */
    public void addPage(IPreferencePage page) {
        tabs.addTab(page.getLabel(), (JComponent) page);
    }

    /**
     * DOCUMENT ME!
     */
    public void commitAll() {
        for (int i = 0; i < tabs.getTabCount(); i++) {
            IPreferencePage page = (IPreferencePage) tabs.getComponentAt(i);

            page.commitAll();
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void restoreAll() {
        for (int i = 0; i < tabs.getTabCount(); i++) {
            IPreferencePage page = (IPreferencePage) tabs.getComponentAt(i);

            page.restoreAll();
        }
    }
}
