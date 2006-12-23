/*
 * $Id: ColorEditor.java,v 1.1.1.1 2004/09/18 17:16:19 amunoz Exp $
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

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import tk.tomby.tedit.core.preferences.IEditor;

import tk.tomby.tedit.messages.PreferenceMessage;

import tk.tomby.tedit.services.MessageManager;
import tk.tomby.tedit.services.PreferenceManager;
import tk.tomby.tedit.services.ResourceManager;
import tk.tomby.tedit.services.WorkspaceManager;


/**
 * DOCUMENT ME!
 *
 * @author $Author: amunoz $
 * @version $Revision: 1.1.1.1 $
 */
public class ColorEditor extends JPanel implements IEditor {
    //~ Instance fields ****************************************************************************

    /** DOCUMENT ME! */
    private Color defaultValue = Color.BLACK;

    /** DOCUMENT ME! */
    private JLabel label = null;

    /** DOCUMENT ME! */
    private JTextField field = null;

    /** DOCUMENT ME! */
    private String key = null;

    /** DOCUMENT ME! */
    private boolean modified = false;

    //~ Constructors *******************************************************************************

    /**
     * Creates a new ColorEditor object.
     */
    public ColorEditor() {
        super();

        setLayout(new GridLayout(0, 2, 5, 5));

        label = new JLabel();
        label.setHorizontalAlignment(JLabel.RIGHT);
        label.setHorizontalTextPosition(JLabel.RIGHT);

        field = new JTextField(15);
        field.setOpaque(true);
        field.setEditable(false);
        field.setHorizontalAlignment(JTextField.LEFT);
        field.setEditable(false);
        field.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    Color color =
                               JColorChooser.showDialog(WorkspaceManager.getMainFrame(),
                                                        ResourceManager.getProperty("preferences.color"),
                                                        field.getBackground());

                    if (color != null) {
                        field.setBackground(color);

                        modified = true;
                    }
                }
            });

        add(label);
        add(field);
    }

    //~ Methods ************************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @param def DOCUMENT ME!
     */
    public void setDefault(String def) {
        defaultValue = parseColor(def);
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     */
    public void setKey(String key) {
        this.key = key;

        int red   = PreferenceManager.getInt(this.key + ".red", defaultValue.getRed());
        int green = PreferenceManager.getInt(this.key + ".green", defaultValue.getGreen());
        int blue  = PreferenceManager.getInt(this.key + ".blue", defaultValue.getBlue());

        field.setBackground(new Color(red, green, blue));
    }

    /**
     * DOCUMENT ME!
     *
     * @param label DOCUMENT ME!
     */
    public void setLabel(String label) {
        this.label.setText(ResourceManager.getProperty(label));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isModified() {
        return modified;
    }

    /**
     * DOCUMENT ME!
     */
    public void commit() {
        if (modified) {
            Color background = field.getBackground();

            PreferenceManager.putInt(key + ".red", background.getRed());
            PreferenceManager.putInt(key + ".green", background.getGreen());
            PreferenceManager.putInt(key + ".blue", background.getBlue());

            PreferenceMessage message = new PreferenceMessage(this, key, background);
            MessageManager.sendMessage(message);

            modified = false;
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void restore() {
        int red   = PreferenceManager.getInt(this.key + ".red", 0);
        int green = PreferenceManager.getInt(this.key + ".green", 0);
        int blue  = PreferenceManager.getInt(this.key + ".blue", 0);

        field.setBackground(new Color(red, green, blue));

        modified = false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param def
     *
     * @return
     */
    private Color parseColor(String def) {
        int red   = Integer.parseInt(def.substring(1, 3), 16);
        int green = Integer.parseInt(def.substring(3, 5), 16);
        int blue  = Integer.parseInt(def.substring(5, 7), 16);

        return new Color(red, green, blue);
    }
}
