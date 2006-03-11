/*
 * $Id: HighlightPlainContext.java,v 1.3 2005/01/09 15:26:28 amunoz Exp $
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

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.PlainView;
import javax.swing.text.Segment;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.Utilities;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tk.tomby.tedit.core.ICleanable;

import tk.tomby.tedit.messages.IMessageListener;
import tk.tomby.tedit.messages.PreferenceMessage;

import tk.tomby.tedit.services.MessageManager;
import tk.tomby.tedit.services.PreferenceManager;

import tk.tomby.tedit.syntax.Syntax;
import tk.tomby.tedit.syntax.Tokenizer;


/**
 * DOCUMENT ME!
 *
 * @author $Author: amunoz $
 * @version $Revision: 1.3 $
 */
public class HighlightPlainContext extends StyleContext implements ViewFactory, IMessageListener,
                                                                   ICleanable {
    //~ Static fields/initializers *****************************************************************

    /** DOCUMENT ME! */
    private static Log log = LogFactory.getLog(HighlightPlainContext.class);

    //~ Instance fields ****************************************************************************

    /** DOCUMENT ME! */
    private Tokenizer tokenizer = null;

    //~ Constructors *******************************************************************************

    /**
     * Creates a new HighlightContext object.
     *
     * @param syntax DOCUMENT ME!
     */
    public HighlightPlainContext(Syntax syntax) {
        super();

        tokenizer = new Tokenizer(syntax);

        Style style = addStyle(Syntax.UNKNOWN_NAME, getStyle(DEFAULT_STYLE));

        int red   = PreferenceManager.getInt("general.editor.foreground.red", 0);
        int green = PreferenceManager.getInt("general.editor.foreground.green", 0);
        int blue  = PreferenceManager.getInt("general.editor.foreground.blue", 0);
        style.addAttribute(StyleConstants.Foreground, new Color(red, green, blue));

        String font = PreferenceManager.getString("general.editor.font", "Monospaced");
        int size    = PreferenceManager.getInt("general.editor.fontSize", 12);
        style.addAttribute(StyleConstants.FontFamily, new Font(font, Font.PLAIN, size));

        createStyle(Syntax.KEYWORD1_NAME, "highlight.keyword1");
        createStyle(Syntax.KEYWORD2_NAME, "highlight.keyword2");
        createStyle(Syntax.KEYWORD3_NAME, "highlight.keyword3");
        createStyle(Syntax.KEYWORD4_NAME, "highlight.keyword4");

        createStyle(Syntax.COMMENT1_NAME, "highlight.comment1");
        createStyle(Syntax.COMMENT2_NAME, "highlight.comment2");
        createStyle(Syntax.COMMENT3_NAME, "highlight.comment3");
        createStyle(Syntax.COMMENT4_NAME, "highlight.comment3");

        createStyle(Syntax.LITERAL1_NAME, "highlight.literal1");
        createStyle(Syntax.LITERAL2_NAME, "highlight.literal2");
        createStyle(Syntax.LITERAL3_NAME, "highlight.literal3");
        createStyle(Syntax.LITERAL4_NAME, "highlight.literal4");

        createStyle(Syntax.IDENTIFIER1_NAME, "highlight.identifier1");
        createStyle(Syntax.IDENTIFIER2_NAME, "highlight.identifier2");
        createStyle(Syntax.IDENTIFIER3_NAME, "highlight.identifier3");
        createStyle(Syntax.IDENTIFIER4_NAME, "highlight.identifier4");

        createStyle(Syntax.OPERATOR_NAME, "highlight.operator");
        createStyle(Syntax.SEPARATOR_NAME, "highlight.separator");
        createStyle(Syntax.LABEL_NAME, "highlight.label");
        createStyle(Syntax.MARKUP_NAME, "highlight.markup");
        createStyle(Syntax.DIGIT_NAME, "highlight.digit");
        createStyle(Syntax.SCRIPTLET_NAME, "highlight.scriptlet");

        MessageManager.addMessageListener(MessageManager.PREFERENCE_GROUP_NAME, this);
    }

    //~ Methods ************************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @param type
     *
     * @return
     */
    public Style getTokenStyle(int type) {
        String styleName = Syntax.getTokenName(type);

        return getStyle(styleName);
    }

    /**
     * DOCUMENT ME!
     */
    public void cleanup() {
        MessageManager.removeMessageListener(MessageManager.PREFERENCE_GROUP_NAME, this);
    }

    /**
     * DOCUMENT ME!
     *
     * @param element DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public View create(Element element) {
        if (log.isDebugEnabled()) {
            log.debug(element.getName());
        }

        return new HighlightView(element);
    }

    /**
     * DOCUMENT ME!
     *
     * @param message DOCUMENT ME!
     */
    public void receiveMessage(PreferenceMessage message) {
        if (message.getKey().startsWith("highlight")) {
            int index        = message.getKey().indexOf('.');
            String styleName = message.getKey().substring(index + 1);

            log.debug("styleName=" + styleName);

            HighlightModel model = (HighlightModel) message.getNewValue();

            Style style = getStyle(styleName);
            style.addAttribute(StyleConstants.Foreground, model.getColor());
            style.addAttribute(StyleConstants.FontFamily, model.getFont());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     * @param key DOCUMENT ME!
     */
    private void createStyle(String name,
                             String key) {
        HighlightModel model = new HighlightModel(key);

        Style style = addStyle(name, getStyle(DEFAULT_STYLE));
        style.addAttribute(StyleConstants.Foreground, model.getColor());
        style.addAttribute(StyleConstants.FontFamily, model.getFont());
    }

    //~ Inner Classes ******************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @author $Author: amunoz $
     * @version $Revision: 1.3 $
     */
    public class HighlightView extends PlainView {
        /** DOCUMENT ME! */
        private Element element = null;

        /**
         * Creates a new HighlightView object.
         *
         * @param element DOCUMENT ME!
         */
        public HighlightView(Element element) {
            super(element);

            this.element = element;
        }

        /**
         * DOCUMENT ME!
         *
         * @param lineIndex DOCUMENT ME!
         * @param g DOCUMENT ME!
         * @param x DOCUMENT ME!
         * @param y DOCUMENT ME!
         */
        protected void drawLine(int      lineIndex,
                                Graphics g,
                                int      x,
                                int      y) {
            Element line = element.getElement(lineIndex);

            try {
                if (line.isLeaf()) {
                    drawElement(lineIndex, line, g, x, y);
                } else {
                    for (int i = 0; i < line.getElementCount(); i++) {
                        Element elem = line.getElement(i);
                        x = drawElement(lineIndex, elem, g, x, y);
                    }
                }
            } catch (BadLocationException e) {
                log.error(e.getMessage(), e);
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @param line DOCUMENT ME!
         * @param previous DOCUMENT ME!
         * @param g DOCUMENT ME!
         * @param x DOCUMENT ME!
         * @param y DOCUMENT ME!
         * @param p0 DOCUMENT ME!
         * @param p1 DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         *
         * @throws BadLocationException DOCUMENT ME!
         */
        protected int drawUnselectedText(HighlightPlainDocument.LineElement line,
                                         HighlightPlainDocument.LineElement previous,
                                         Graphics                           g,
                                         int                                x,
                                         int                                y,
                                         int                                p0,
                                         int                                p1)
                                  throws BadLocationException {
            Document doc = getDocument();

            String text = doc.getText(p0, p1 - p0);

            if (previous == null) {
                tokenizer.reinit(text);
            } else {
                tokenizer.reinit(text, previous.getLastState());
            }

            Tokenizer.Token token = null;

            int lastPosition                 = 0;
            Tokenizer.RuntimeState lastState = tokenizer.getCurrentState();

            do {
                token = tokenizer.nextToken(lastPosition);

                if (token != null) {
                    if (lastPosition < token.getPosition()) {
                        int type = lastState.getDefaultToken();

                        x = draw(type, x, y, g, p0 + lastPosition, p0 + token.getPosition());
                        lastPosition += (token.getPosition() - lastPosition);
                    }

                    int type = token.getType();

                    if (type == Syntax.END) {
                        type = lastState.getDefaultToken();
                    }

                    x = draw(type, x, y, g, p0 + token.getPosition(),
                             p0 + token.getPosition() + token.getLength());

                    lastPosition += token.getLength();
                    lastState = tokenizer.getCurrentState();
                }
            } while (token != null);

            if ((p0 + lastPosition) < p1) {
                int type = lastState.getDefaultToken();

                x = draw(type, x, y, g, p0 + lastPosition, p1);
            }

            return x;
        }

        /**
         * DOCUMENT ME!
         *
         * @param type DOCUMENT ME!
         * @param x DOCUMENT ME!
         * @param y DOCUMENT ME!
         * @param g DOCUMENT ME!
         * @param p0 DOCUMENT ME!
         * @param p1 DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         *
         * @throws BadLocationException DOCUMENT ME!
         */
        private int draw(int      type,
                         int      x,
                         int      y,
                         Graphics g,
                         int      p0,
                         int      p1)
                  throws BadLocationException {
            Style style = getTokenStyle(type);

            if (style != null) {
	            Color color = (Color) style.getAttribute(StyleConstants.Foreground);
	            Font font   = (Font) style.getAttribute(StyleConstants.FontFamily);
	
	            g.setColor(color);
	            g.setFont(font);
            }

            Document doc = getDocument();

            Segment segment = getLineBuffer();

            doc.getText(p0, p1 - p0, segment);

            return Utilities.drawTabbedText(segment, x, y, g, this, p0);
        }

        /**
         * DOCUMENT ME!
         *
         * @param lineIndex DOCUMENT ME!
         * @param line DOCUMENT ME!
         * @param g DOCUMENT ME!
         * @param x DOCUMENT ME!
         * @param y DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         *
         * @throws BadLocationException DOCUMENT ME!
         */
        private int drawElement(int      lineIndex,
                                Element  line,
                                Graphics g,
                                int      x,
                                int      y)
                         throws BadLocationException {
            Element previous = null;

            if (lineIndex > 0) {
                previous = element.getElement(lineIndex - 1);
            }

            int p0 = line.getStartOffset();
            int p1 = line.getEndOffset();

            p1     = Math.min(getDocument().getLength(), p1);

            if (lineIndex == 0) {
                x += 0;
            }

            return drawUnselectedText((HighlightPlainDocument.LineElement) line,
                                      (HighlightPlainDocument.LineElement) previous, g, x, y, p0, p1);
        }
    }
}
