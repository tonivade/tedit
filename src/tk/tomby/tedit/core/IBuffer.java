/*
 * $Id: IBuffer.java,v 1.1.1.1 2004/09/18 17:16:19 amunoz Exp $
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

import java.io.File;

import java.net.URL;
import java.util.Iterator;

import javax.swing.text.Document;
import javax.swing.text.Element;


/**
 * DOCUMENT ME!
 *
 * @author $Author: amunoz $
 * @version $Revision: 1.1.1.1 $
 */
public interface IBuffer {
    //~ Methods ************************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract int getCaretPosition();
    
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract int getCurrentColumn();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract int getCurrentLine();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract Document getDocument();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract File getFile();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract String getFileName();
    
    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public abstract String getText();
    
    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public abstract String getText(int offset, int length);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract int getLineCount();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract boolean isModified();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract boolean isNew();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract boolean isReadOnly();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract int getSelectionEnd();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract int getSelectionStart();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract boolean canRedo();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract boolean canUndo();

    /**
     * DOCUMENT ME!
     */
    public abstract void clean();

    /**
     * DOCUMENT ME!
     */
    public abstract void close();

    /**
     * DOCUMENT ME!
     */
    public abstract void copy();

    /**
     * DOCUMENT ME!
     */
    public abstract void cut();

    /**
     * DOCUMENT ME!
     */
    public abstract void findNext();

    /**
     * DOCUMENT ME!
     */
    public abstract void findPrevious();

    /**
     * DOCUMENT ME!
     *
     * @param line DOCUMENT ME!
     */
    public abstract void gotoLine(int line);

    /**
     * DOCUMENT ME!
     *
     * @param url DOCUMENT ME!
     */
    public abstract void open(URL url);

    /**
     * DOCUMENT ME!
     *
     * @param file DOCUMENT ME!
     */
    public abstract void open(String file);

    /**
     * DOCUMENT ME!
     *
     * @param file DOCUMENT ME!
     */
    public abstract void open(File file);

    /**
     * DOCUMENT ME!
     */
    public abstract void paste();

    /**
     * DOCUMENT ME!
     */
    public abstract void redo();

    /**
     * DOCUMENT ME!
     */
    public abstract void replace();

    /**
     * DOCUMENT ME!
     */
    public abstract void save();

    /**
     * DOCUMENT ME!
     *
     * @param url DOCUMENT ME!
     */
    public abstract void saveAs(URL url);

    /**
     * DOCUMENT ME!
     *
     * @param file DOCUMENT ME!
     */
    public abstract void saveAs(String file);

    /**
     * DOCUMENT ME!
     *
     * @param file DOCUMENT ME!
     */
    public abstract void saveAs(File file);

    /**
     * DOCUMENT ME!
     *
     * @param start DOCUMENT ME!
     * @param end DOCUMENT ME!
     */
    public abstract void select(int start,
                                int end);

    /**
     * DOCUMENT ME!
     */
    public abstract void selectAll();

    /**
     * DOCUMENT ME!
     */
    public abstract void undo();
    
    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public abstract Iterator<Element> elementIterator();
    
    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public abstract Iterator<String> lineIterator();
    
    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public abstract void insertString(int offset, String text);
    
    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public abstract void replaceString(int offset, int length, String text);
    
    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public abstract void removeString(int offset, int length);
}
