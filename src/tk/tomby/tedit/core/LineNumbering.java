/*
 * $Id: LineNumbering.java,v 1.1.1.1 2004/09/18 17:16:19 amunoz Exp $
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

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JComponent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.text.Document;

import tk.tomby.tedit.services.PreferenceManager;


/**
 * DOCUMENT ME!
 *
 * @author $Author: amunoz $
 * @version $Revision: 1.1.1.1 $
 */
public class LineNumbering extends JComponent implements DocumentListener {
    //~ Instance fields ****************************************************************************

    /** DOCUMENT ME! */
    private Document document = null;

    //~ Constructors *******************************************************************************

    /**
     * Creates a new TextArea object.
     */
    public LineNumbering() {
        super();

        this.ui = new LineNumberingUI();
    }

    //~ Methods ************************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @param document DOCUMENT ME!
     */
    public void setDocument(Document document) {
        if (this.document != null) {
            this.document.removeDocumentListener(this);
        }

        this.document = document;

        document.addDocumentListener(this);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Document getDocument() {
        return document;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void changedUpdate(DocumentEvent e) {
        repaint();
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void insertUpdate(DocumentEvent e) {
        repaint();
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void removeUpdate(DocumentEvent e) {
        repaint();
    }

    /**
     * DOCUMENT ME!
     *
     * @param newUI DOCUMENT ME!
     */
    protected void setUI(ComponentUI newUI) {
        ;
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    //~ Inner Classes ******************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @author $Author: amunoz $
     * @version $Revision: 1.1.1.1 $
     */
    private static class LineNumberingUI extends ComponentUI {
        /**
         * DOCUMENT ME!
         *
         * @param g DOCUMENT ME!
         * @param c DOCUMENT ME!
         */
        public void paint(Graphics   g,
                          JComponent c) {
            if (c instanceof LineNumbering) {
                LineNumbering n = (LineNumbering) c;

                if (PreferenceManager.getBoolean("general.appearance.antialiasing", false)) {
                    ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                                                      RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_RENDERING,
                                                      RenderingHints.VALUE_RENDER_QUALITY);
                }

                FontMetrics metrics = n.getFontMetrics(n.getFont());
                int lineHeight      = metrics.getHeight();

                int firstLine = n.getY() / lineHeight;
                int lastLine  = ((n.getY() + n.getHeight()) - 1) / lineHeight;
                int y         = (n.getY() - (n.getY() % lineHeight));

                g.setColor(n.getForeground());
                g.setFont(n.getFont());

                int endX = n.getX() + n.getPreferredSize().width;
                int endY = n.getY() + (lastLine * lineHeight) + lineHeight;

                g.drawLine(endX - 1, n.getY(), endX - 1, endY);

                int maxLines = n.getDocument().getDefaultRootElement().getElementCount();

                for (int line = firstLine; (line <= lastLine) && (line <= maxLines);
                         line++, y += lineHeight) {
                    String text = Integer.toString(line);

                    int width = metrics.stringWidth(text);

                    int x = endX - width - 3;

                    g.drawString(text, x, y);
                }
            }
        }
    }
}
