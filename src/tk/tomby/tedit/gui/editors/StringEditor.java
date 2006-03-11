/*
 * $Id: StringEditor.java,v 1.1.1.1 2004/09/18 17:16:19 amunoz Exp $
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

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

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
public class StringEditor extends JPanel implements IEditor {
    //~ Instance fields ****************************************************************************

    /** DOCUMENT ME! */
    private JLabel label = null;

    /** DOCUMENT ME! */
    private JTextField field = null;

    /** DOCUMENT ME! */
    private String defaultValue = "";

    /** DOCUMENT ME! */
    private String key = null;

    //~ Constructors *******************************************************************************

    /**
     * Creates a new StringEditor object.
     */
    public StringEditor() {
        super();

        setLayout(new GridLayout(0, 2, 5, 5));

        label = new JLabel();
        label.setHorizontalAlignment(JLabel.RIGHT);
        label.setHorizontalTextPosition(JLabel.RIGHT);
        field = new JTextField(15);
        field.setHorizontalAlignment(JTextField.LEFT);

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
        this.defaultValue = def;
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     */
    public void setKey(String key) {
        this.key = key;

        field.setText(PreferenceManager.getString(key, defaultValue));
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
        String newValue = field.getText();
        String oldValue = PreferenceManager.getString(key, defaultValue);

        return !newValue.equals(oldValue);
    }

    /**
     * DOCUMENT ME!
     */
    public void commit() {
        String newValue = field.getText();
        String oldValue = PreferenceManager.getString(key, defaultValue);

        if (!newValue.equals(oldValue)) {
            PreferenceMessage message = new PreferenceMessage(this, key, newValue, oldValue);

            PreferenceManager.putString(key, newValue);

            MessageManager.sendMessage(MessageManager.PREFERENCE_GROUP_NAME, message);
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void restore() {
        field.setText(PreferenceManager.getString(key, defaultValue));
    }
}
