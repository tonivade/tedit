/*
 * $Id: FindReplaceWorker.java,v 1.1.1.1 2004/09/18 17:16:19 amunoz Exp $
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

package tk.tomby.tedit.core.snr;

import java.awt.Toolkit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tk.tomby.tedit.core.IBuffer;

import tk.tomby.tedit.services.PreferenceManager;


/**
 * DOCUMENT ME!
 *
 * @author $Author: amunoz $
 * @version $Revision: 1.1.1.1 $
 */
public class FindReplaceWorker {
    //~ Static fields/initializers *****************************************************************

    /** DOCUMENT ME! */
    private static Log log = LogFactory.getLog(FindReplaceWorker.class);

    /** DOCUMENT ME! */
    public static final int FORWARD = 0;

    /** DOCUMENT ME! */
    public static final int BACKWARD = 1;

    //~ Instance fields ****************************************************************************

    /** DOCUMENT ME! */
    private IBuffer buffer = null;

    /** DOCUMENT ME! */
    private Matcher matcher = null;

    //~ Constructors *******************************************************************************

    /**
     * Creates a new FindReplaceWorker object.
     *
     * @param buffer DOCUMENT ME!
     */
    public FindReplaceWorker(IBuffer buffer) {
        this.buffer = buffer;
    }

    //~ Methods ************************************************************************************

    /**
     * DOCUMENT ME!
     */
    public void next() {
        for (int count = 0, i = buffer.getCurrentLine(); i < buffer.getLineCount();) {
            Element line = init(i);

            int offset = line.getStartOffset();

            if (PreferenceManager.getInt("snr.direction", FORWARD) == FORWARD) {
                if ((buffer.getSelectionEnd() > line.getStartOffset())
                        && (buffer.getSelectionEnd() <= line.getEndOffset())) {
                    offset = buffer.getSelectionEnd();
                }
            }

            if (matcher.find()) {
                int start = offset + matcher.start();
                int end   = offset + matcher.end();

                buffer.select(start, end);

                break;
            }

            if (PreferenceManager.getBoolean("snr.warp", false)
                    && (i == (buffer.getLineCount() - 1))) {
                if (count > 1) {
                    Toolkit.getDefaultToolkit().beep();

                    break;
                }

                count++;

                i = 0;
            } else {
                i++;
            }
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void previous() {
        for (int count = 0, i = buffer.getCurrentLine(); i >= 0;) {
            Element line = init(i);

            int offset = line.getStartOffset();
            int start  = 0;
            int end    = 0;

            while (matcher.find()) {
                start     = offset + matcher.start();
                end       = offset + matcher.end();
            }

            if (start != end) {
                buffer.select(start, end);

                break;
            }

            if (PreferenceManager.getBoolean("snr.warp", false) && (i == 0)) {
                if (count > 1) {
                    Toolkit.getDefaultToolkit().beep();

                    break;
                }

                count++;

                i = buffer.getLineCount() - 1;
            } else {
                i--;
            }
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void replace() {
        String replace = PreferenceManager.getString("snr.replace", "");

        if ((replace != null) && !replace.equals("")) {
            int start = buffer.getSelectionStart();
            int end   = buffer.getSelectionEnd();

            if (start != end) {
                Document doc = buffer.getDocument();

                try {
                    doc.remove(start, end - start);
                    doc.insertString(start, replace, null);
                } catch (BadLocationException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param lineNumber DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private Element init(int lineNumber) {
        Document doc = buffer.getDocument();
        Element line = doc.getDefaultRootElement().getElement(lineNumber);

        try {
            int options = Pattern.DOTALL;

            String find = PreferenceManager.getString("snr.find", "");

            if ((find != null) && !find.equals("")) {
                if (PreferenceManager.getBoolean("snr.case", false)) {
                    find = find.toLowerCase();

                    options |= Pattern.CASE_INSENSITIVE;
                }

                if (PreferenceManager.getBoolean("snr.whole", false)) {
                    find = "\\b" + find + "\\b";
                }

                int offset = line.getStartOffset();
                int length = line.getEndOffset() - offset;

                if (PreferenceManager.getInt("snr.direction", FORWARD) == FORWARD) {
                    if ((buffer.getSelectionEnd() > line.getStartOffset())
                            && (buffer.getSelectionEnd() <= line.getEndOffset())) {
                        offset     = buffer.getSelectionEnd();
                        length     = line.getEndOffset() - offset;
                    }
                } else {
                    if ((buffer.getSelectionStart() > line.getStartOffset())
                            && (buffer.getSelectionStart() <= line.getEndOffset())) {
                        length = buffer.getSelectionStart() - offset;
                    }
                }

                String text = doc.getText(offset, length);

                Pattern pattern = Pattern.compile(find, options);

                this.matcher = pattern.matcher(text);
            }
        } catch (BadLocationException e) {
            log.error(e.getMessage(), e);
        }

        return line;
    }
}
