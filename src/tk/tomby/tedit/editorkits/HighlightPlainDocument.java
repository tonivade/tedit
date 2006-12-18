/*
 * $Id: HighlightPlainDocument.java,v 1.1.1.1 2004/09/18 17:16:21 amunoz Exp $
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

import java.util.Stack;
import java.util.Vector;

import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.GapContent;

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
 * @version $Revision: 1.1.1.1 $
 */
public class HighlightPlainDocument extends AbstractDocument implements IMessageListener<PreferenceMessage>, ICleanable {
    //~ Static fields/initializers *****************************************************************

    /** DOCUMENT ME! */
    public static final String tabSizeAttribute = "tabSize";

    //~ Instance fields ****************************************************************************

    /** DOCUMENT ME! */
    private AbstractElement defaultRoot = null;

    /** DOCUMENT ME! */
    private HighlightPlainContext context = null;

    /** DOCUMENT ME! */
    private Tokenizer tokenizer = null;

    /** DOCUMENT ME! */
    private Vector<Element> added = new Vector<Element>();

    /** DOCUMENT ME! */
    private Vector<Element> removed = new Vector<Element>();

    //~ Constructors *******************************************************************************

    /**
     * Creates a new HackedPlainDocument object.
     *
     * @param context DOCUMENT ME!
     * @param syntax DOCUMENT ME!
     */
    public HighlightPlainDocument(HighlightPlainContext context,
                                  Syntax                syntax) {
        this(new GapContent(), context, syntax);
    }

    /**
     * Creates a new HackedPlainDocument object.
     *
     * @param content DOCUMENT ME!
     * @param context DOCUMENT ME!
     * @param syntax DOCUMENT ME!
     */
    public HighlightPlainDocument(Content               content,
                                  HighlightPlainContext context,
                                  Syntax                syntax) {
        super(content, context);

        this.context         = context;
        this.tokenizer       = new Tokenizer(syntax);
        this.defaultRoot     = createDefaultRoot();

        putProperty(tabSizeAttribute,
                    new Integer(PreferenceManager.getInt("general.editor.tabSize", 4)));

        MessageManager.addMessageListener(MessageManager.PREFERENCE_GROUP_NAME, this);
    }

    //~ Methods ************************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Element getDefaultRootElement() {
        return defaultRoot;
    }

    /**
     * DOCUMENT ME!
     *
     * @param pos DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Element getParagraphElement(int pos) {
        Element root = getDefaultRootElement();

        int index    = root.getElementIndex(pos);
        Element line = root.getElement(index);

        return line;
    }

    /**
     * DOCUMENT ME!
     */
    public void cleanup() {
        MessageManager.removeMessageListener(MessageManager.PREFERENCE_GROUP_NAME, this);

        context.cleanup();
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

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected AbstractElement createDefaultRoot() {
        BranchElement root = (BranchElement) createBranchElement(null, null);

        Element line = createLeafElement(root, null, 0, 1);

        root.replace(0, 0, new Element[] { line });

        return root;
    }

    /**
     * DOCUMENT ME!
     *
     * @param parent DOCUMENT ME!
     * @param a DOCUMENT ME!
     * @param p0 DOCUMENT ME!
     * @param p1 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected Element createLeafElement(Element      parent,
                                        AttributeSet a,
                                        int          p0,
                                        int          p1) {
        return new LineElement(parent, a, p0, p1);
    }

    /**
     * DOCUMENT ME!
     *
     * @param chng DOCUMENT ME!
     * @param attr DOCUMENT ME!
     *
     * @throws Error DOCUMENT ME!
     */
    protected void insertUpdate(DefaultDocumentEvent chng,
                                AttributeSet         attr) {
        removed.removeAllElements();
        added.removeAllElements();

        BranchElement root = (BranchElement) getDefaultRootElement();

        int offset = chng.getOffset();
        int length = chng.getLength();

        if (offset > 0) {
            offset -= 1;
            length += 1;
        }

        int lineIndex    = root.getElementIndex(offset);
        LineElement line = (LineElement) root.getElement(lineIndex);

        LineElement previousLine = null;

        if (lineIndex > 0) {
            previousLine = (LineElement) root.getElement(lineIndex - 1);
        }

        int lineStartOffset = line.getStartOffset();
        int lineEndOffset   = line.getEndOffset();

        int lastOffset = lineStartOffset;

        try {
            String str        = getText(offset, length);
            boolean hasBreaks = false;

            for (int i = 0; i < length; i++) {
                char c = str.charAt(i);

                if (c == '\n') {
                    int breakOffset     = offset + i + 1;
                    LineElement newLine =
                        (LineElement) createLeafElement(root, null, lastOffset, breakOffset);
                    added.addElement(newLine);

                    parseLine(previousLine, newLine);

                    lastOffset       = breakOffset;
                    hasBreaks        = true;
                    previousLine     = newLine;
                }
            }

            if (hasBreaks) {
                removed.addElement(line);

                if (((offset + length) == lineEndOffset) && (lastOffset != lineEndOffset)
                        && ((lineIndex + 1) < root.getElementCount())) {
                    Element e = root.getElement(lineIndex + 1);
                    removed.addElement(e);

                    lineEndOffset = e.getEndOffset();
                }

                if (lastOffset < lineEndOffset) {
                    LineElement newLine =
                        (LineElement) createLeafElement(root, null, lastOffset, lineEndOffset);

                    added.addElement(newLine);

                    parseLine(previousLine, newLine);
                }

                Element[] addedElements = new Element[added.size()];
                added.copyInto(addedElements);

                Element[] removedElements = new Element[removed.size()];
                removed.copyInto(removedElements);

                ElementEdit edit = new ElementEdit(root, lineIndex, removedElements, addedElements);
                chng.addEdit(edit);

                root.replace(lineIndex, removedElements.length, addedElements);
            } else {
                parseLine(previousLine, line);
            }
        } catch (BadLocationException e) {
            throw new Error("Internal error: " + e.toString());
        }

        super.insertUpdate(chng, attr);
    }

    /**
     * DOCUMENT ME!
     *
     * @param chng DOCUMENT ME!
     *
     * @throws Error DOCUMENT ME!
     */
    protected void postRemoveUpdate(DefaultDocumentEvent chng) {
        BranchElement root = (BranchElement) getDefaultRootElement();

        int offset = chng.getOffset();

        int lineIndex = root.getElementIndex(offset);

        LineElement previousLine = null;

        if (lineIndex > 0) {
            previousLine = (LineElement) root.getElement(lineIndex - 1);
        }

        LineElement line = (LineElement) root.getElement(lineIndex);

        try {
            parseLine(previousLine, line);
        } catch (BadLocationException e) {
            throw new Error("Internal error: " + e.toString());
        }

        super.postRemoveUpdate(chng);
    }

    /**
     * DOCUMENT ME!
     *
     * @param chng DOCUMENT ME!
     */
    protected void removeUpdate(DefaultDocumentEvent chng) {
        removed.removeAllElements();

        BranchElement root = (BranchElement) getDefaultRootElement();

        int offset = chng.getOffset();
        int length = chng.getLength();

        int lineIndex0 = root.getElementIndex(offset);
        int lineIndex1 = root.getElementIndex(offset + length);

        if (lineIndex0 != lineIndex1) {
            for (int i = lineIndex0; i <= lineIndex1; i++) {
                removed.addElement(root.getElement(i));
            }

            int p0 = root.getElement(lineIndex0).getStartOffset();
            int p1 = root.getElement(lineIndex1).getEndOffset();

            LineElement newline = (LineElement) createLeafElement(root, null, p0, p1);

            Element[] removedElements = new Element[removed.size()];
            removed.copyInto(removedElements);

            ElementEdit edit =
                new ElementEdit(root, lineIndex0, removedElements, new Element[] { newline });
            chng.addEdit(edit);

            root.replace(lineIndex0, removedElements.length, new Element[] { newline });
        }

        super.removeUpdate(chng);
    }

    /**
     * DOCUMENT ME!
     *
     * @param previous DOCUMENT ME!
     * @param line DOCUMENT ME!
     *
     * @throws BadLocationException
     */
    private void parseLine(LineElement previous,
                           LineElement line)
                    throws BadLocationException {
        int lineOffset    = line.getStartOffset();
        int lineEndOffset = line.getEndOffset();

        int lineLength = lineEndOffset - lineOffset;

        String text = getText(lineOffset, lineLength);

        if (previous == null) {
            tokenizer.reinit(text);
        } else {
            tokenizer.reinit(text, previous.getLastState());
        }

        Tokenizer.Token token = null;

        int lastPosition = 0;

        do {
            token = tokenizer.nextToken(lastPosition);

            if (token != null) {
                if (lastPosition < token.getPosition()) {
                    lastPosition += (token.getPosition() - lastPosition);
                }

                lastPosition += token.getLength();
            }
        } while (token != null);

        line.setLastState(tokenizer.getState());
    }

    //~ Inner Classes ******************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @author $Author: amunoz $
     * @version $Revision: 1.1.1.1 $
     */
    public class LineElement extends LeafElement {
        /** DOCUMENT ME! */
        private Stack lastState = null;

        /**
         * DOCUMENT ME!
         *
         * @param parent
         * @param attrs
         * @param p0
         * @param p1
         */
        public LineElement(Element      parent,
                           AttributeSet attrs,
                           int          p0,
                           int          p1) {
            super(parent, attrs, p0, p1);
        }

        /**
         * DOCUMENT ME!
         *
         * @param state The state to set.
         */
        public void setLastState(Stack state) {
            this.lastState = state;
        }

        /**
         * DOCUMENT ME!
         *
         * @return Returns the state.
         */
        public Stack getLastState() {
            return lastState;
        }
    }
}
