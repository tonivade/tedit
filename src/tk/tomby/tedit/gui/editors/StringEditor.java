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
import javax.swing.JTextField;

import tk.tomby.tedit.services.ResourceManager;


/**
 * DOCUMENT ME!
 *
 * @author $Author: amunoz $
 * @version $Revision: 1.1.1.1 $
 */
public class StringEditor extends AbstractEditor {
    //~ Instance fields ****************************************************************************

    /** DOCUMENT ME! */
    private JLabel label = null;

    /** DOCUMENT ME! */
    private JTextField field = null;

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

    public void setLabel(String label) {
    	this.label.setText(ResourceManager.getProperty(label));
    }

    @Override
    protected void setValue(String value) {
    	field.setText(value);
    }
    
    @Override
    protected String getValue() {
    	return field.getText();
    }
}
