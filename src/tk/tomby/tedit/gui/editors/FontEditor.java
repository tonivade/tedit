/*
 * $Id: FontEditor.java,v 1.1.1.1 2004/09/18 17:16:18 amunoz Exp $
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

import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;

import java.util.Arrays;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import tk.tomby.tedit.core.preferences.IEditor;

import tk.tomby.tedit.messages.PreferenceMessage;

import tk.tomby.tedit.services.MessageManager;
import tk.tomby.tedit.services.PreferenceManager;
import tk.tomby.tedit.services.ResourceManager;


/**
 * DOCUMENT ME!
 *
 * @author $Author: amunoz $
 * @version $Revision: 1.1.1.1 $
 */
public class FontEditor extends JPanel implements IEditor {
    //~ Instance fields ****************************************************************************

    /** DOCUMENT ME! */
    private DefaultComboBoxModel model = null;

    /** DOCUMENT ME! */
    private JComboBox combo = null;

    /** DOCUMENT ME! */
    private JLabel label = null;

    /** DOCUMENT ME! */
    private String defaultValue = "Monospaced";

    /** DOCUMENT ME! */
    private String key = null;

    //~ Constructors *******************************************************************************

    /**
     * Creates a new FontEditor object.
     */
    public FontEditor() {
        super();

        setLayout(new GridLayout(0, 2, 5, 5));

        label = new JLabel();
        label.setHorizontalAlignment(JLabel.RIGHT);
        label.setHorizontalTextPosition(JLabel.RIGHT);

        model = new DefaultComboBoxModel();
        getSystemFonts();
        combo = new JComboBox(model);
        combo.setEditable(false);

        add(label);
        add(combo);
    }

    //~ Methods ************************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @param def DOCUMENT ME!
     */
    public void setDefault(String def) {
        this.defaultValue = def;
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     */
    public void setKey(String key) {
        this.key = key;

        combo.setSelectedItem(PreferenceManager.getString(key, defaultValue));
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
        String oldValue = PreferenceManager.getString(key, defaultValue);
        String newValue = (String) combo.getSelectedItem();

        return !oldValue.equals(newValue);
    }

    /**
     * DOCUMENT ME!
     */
    public void commit() {
        String oldValue = PreferenceManager.getString(key, defaultValue);
        String newValue = (String) combo.getSelectedItem();

        if (!oldValue.equals(newValue)) {
            PreferenceMessage message = new PreferenceMessage(this, key, newValue, oldValue);

            PreferenceManager.putString(key, newValue);

            MessageManager.sendMessage(MessageManager.PREFERENCE_GROUP_NAME, message);
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void restore() {
        combo.setSelectedItem(PreferenceManager.getString(key, defaultValue));
    }

    /**
     * DOCUMENT ME!
     */
    private void getSystemFonts() {
        String[] fonts =
            GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

        Arrays.sort(fonts);

        for (int i = 0; i < fonts.length; i++) {
            model.addElement(fonts[i]);
        }
    }
}
