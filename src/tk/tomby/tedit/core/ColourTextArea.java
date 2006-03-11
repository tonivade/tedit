/*
 * $Id: ColourTextArea.java,v 1.1.1.1 2004/09/18 17:16:19 amunoz Exp $
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

package tk.tomby.tedit.core;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JTextArea;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.TextUI;
import javax.swing.plaf.basic.BasicTextAreaUI;
import javax.swing.text.EditorKit;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.View;

import tk.tomby.tedit.editorkits.PlainEditorKit;

import tk.tomby.tedit.services.PreferenceManager;


/**
 * DOCUMENT ME!
 *
 * @author $Author: amunoz $
 * @version $Revision: 1.1.1.1 $
 */
public class ColourTextArea extends JTextArea {
    //~ Instance fields ****************************************************************************

    /** DOCUMENT ME! */
    private EditorKit editorKit = null;

    //~ Constructors *******************************************************************************

    /**
     * Creates a new TextArea object.
     */
    public ColourTextArea() {
        this(new PlainEditorKit());
    }

    /**
     * Creates a new ColourTextArea object.
     *
     * @param editorKit DOCUMENT ME!
     */
    public ColourTextArea(EditorKit editorKit) {
        super(null, null, 0, 0);

        setEditorKit(editorKit);

        this.ui = new ColourTextAreaUI();
        this.ui.installUI(this);
    }

    //~ Methods ************************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public String getContentType() {
        return editorKit.getContentType();
    }

    /**
     * DOCUMENT ME!
     *
     * @param editorKit DOCUMENT ME!
     */
    public void setEditorKit(EditorKit editorKit) {
        this.editorKit = editorKit;

        setDocument(editorKit.createDefaultDocument());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public TextUI getUI() {
        return (TextUI) this.ui;
    }

    /**
     * DOCUMENT ME!
     *
     * @param newUI DOCUMENT ME!
     */
    protected void setUI(ComponentUI newUI) {
        ;
    }

    //~ Inner Classes ******************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @author $Author: amunoz $
     * @version $Revision: 1.1.1.1 $
     */
    private class ColourTextAreaUI extends BasicTextAreaUI {
        /**
         * DOCUMENT ME!
         *
         * @param tc DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public EditorKit getEditorKit(JTextComponent tc) {
            return editorKit;
        }

        /**
         * DOCUMENT ME!
         *
         * @param elem DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public View create(Element elem) {
            return editorKit.getViewFactory().create(elem);
        }

        /**
         * DOCUMENT ME!
         *
         * @param g DOCUMENT ME!
         */
        protected void paintSafely(Graphics g) {
            if (PreferenceManager.getBoolean("general.appearance.antialiasing", false)) {
                ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                                                  RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_RENDERING,
                                                  RenderingHints.VALUE_RENDER_QUALITY);
            }

            super.paintSafely(g);
        }
    }
}
