/*
 * $Id: PreferencePage.java,v 1.1.1.1 2004/09/18 17:16:20 amunoz Exp $
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
import java.awt.GridLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;

import tk.tomby.tedit.services.ResourceManager;


/**
 * DOCUMENT ME!
 *
 * @author $Author: amunoz $
 * @version $Revision: 1.1.1.1 $
 */
public class PreferencePage extends JPanel implements ISinglePreferencePage {
    //~ Instance fields ****************************************************************************

    /** DOCUMENT ME! */
    private JPanel panel = null;

    /** DOCUMENT ME! */
    private String label = null;

    //~ Constructors *******************************************************************************

    /**
     * Creates a new PreferencePage object.
     */
    public PreferencePage() {
        super();

        setLayout(new BorderLayout());

        panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1, 5, 5));

        JPanel internalPane = new JPanel();
        internalPane.add(panel);

        JScrollPane scrollPane = new JScrollPane(internalPane);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getViewport().setScrollMode(JViewport.BLIT_SCROLL_MODE);

        add(scrollPane, BorderLayout.CENTER);
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
     * @param editor DOCUMENT ME!
     */
    public void addEditor(IEditor editor) {
        panel.add((JComponent) editor);
    }

    /**
     * DOCUMENT ME!
     */
    public void commitAll() {
        for (int i = 0; i < panel.getComponentCount(); i++) {
            IEditor editor = (IEditor) panel.getComponent(i);

            editor.commit();
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void restoreAll() {
        for (int i = 0; i < panel.getComponentCount(); i++) {
            final IEditor editor = (IEditor) panel.getComponent(i);

            SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        editor.restore();
                    }
                });
        }
    }
}
