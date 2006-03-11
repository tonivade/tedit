/*
 * $Id: PlainDocument.java,v 1.1.1.1 2004/09/18 17:16:21 amunoz Exp $
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

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.GapContent;

import tk.tomby.tedit.core.ICleanable;

import tk.tomby.tedit.messages.IMessageListener;
import tk.tomby.tedit.messages.PreferenceMessage;

import tk.tomby.tedit.services.MessageManager;
import tk.tomby.tedit.services.PreferenceManager;


/**
 * DOCUMENT ME!
 *
 * @author $Author: amunoz $
 * @version $Revision: 1.1.1.1 $
 */
public class PlainDocument extends javax.swing.text.PlainDocument implements IMessageListener,
                                                                             ICleanable {
    //~ Constructors *******************************************************************************

    /**
     * Creates a new PlainDocument object.
     */
    public PlainDocument() {
        this(new GapContent());
    }

    /**
     * Creates a new PlainDocument object.
     *
     * @param c DOCUMENT ME!
     */
    public PlainDocument(Content c) {
        super(c);

        putProperty(tabSizeAttribute,
                    new Integer(PreferenceManager.getInt("general.editor.tabSize", 4)));

        MessageManager.addMessageListener(MessageManager.PREFERENCE_GROUP_NAME, this);
    }

    //~ Methods ************************************************************************************

    /**
     * DOCUMENT ME!
     */
    public void cleanup() {
        MessageManager.removeMessageListener(MessageManager.PREFERENCE_GROUP_NAME, this);
    }

    /**
     * DOCUMENT ME!
     *
     * @param offs DOCUMENT ME!
     * @param str DOCUMENT ME!
     * @param a DOCUMENT ME!
     *
     * @throws BadLocationException DOCUMENT ME!
     */
    public void insertString(int          offs,
                             String       str,
                             AttributeSet a)
                      throws BadLocationException {
        // CR filtering
        if (str.indexOf('\r') > -1) {
            StringBuffer buffer = new StringBuffer(str);

            for (int i = 0; i < buffer.length(); i++) {
                if (buffer.charAt(i) == '\r') {
                    if (buffer.length() > (i + 1)) {
                        if (buffer.charAt(i + 1) == '\n') {
                            buffer.replace(i, i + 1, "");
                        } else {
                            buffer.replace(i, i + 1, "\n");
                        }
                    } else {
                        buffer.replace(i, i + 1, "\n");
                    }
                }
            }

            str = buffer.toString();
        }

        // Tab filtering
        if (PreferenceManager.getBoolean("general.editor.replaceTabs", true)) {
            StringBuffer buffer = new StringBuffer(str);
            int tabSize         = PreferenceManager.getInt("general.editor.tabSize", 4);

            if (str.indexOf('\t') > -1) {
                StringBuffer spaces = new StringBuffer();

                for (int i = 0; i < tabSize; i++) {
                    spaces.append(' ');
                }

                for (int i = 0; i < buffer.length(); i++) {
                    if (buffer.charAt(i) == '\t') {
                        buffer.replace(i, i + 1, spaces.toString());
                    }
                }
            }

            str = buffer.toString();
        }

        super.insertString(offs, str, a);
    }

    /**
     * DOCUMENT ME!
     *
     * @param message DOCUMENT ME!
     */
    public void receiveMessage(PreferenceMessage message) {
        if (message.getKey().equals("general.editor.tabSize")) {
            putProperty(tabSizeAttribute, message.getNewValue());
        }
    }
}
