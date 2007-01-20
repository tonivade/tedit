/*
 * $Id: Buffer.java,v 1.1.1.1 2004/09/18 17:16:19 amunoz Exp $
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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.net.URL;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tk.tomby.tedit.core.snr.FindReplaceWorker;

import tk.tomby.tedit.messages.BufferMessage;
import tk.tomby.tedit.messages.IMessageListener;
import tk.tomby.tedit.messages.PreferenceMessage;

import tk.tomby.tedit.services.EditorKitManager;
import tk.tomby.tedit.services.MessageManager;
import tk.tomby.tedit.services.PreferenceManager;
import tk.tomby.tedit.services.ResourceManager;
import tk.tomby.tedit.services.WorkspaceManager;


/**
 * DOCUMENT ME!
 *
 * @author $Author: amunoz $
 * @version $Revision: 1.1.1.1 $
 */
public class Buffer extends JPanel implements IBuffer, IMessageListener<PreferenceMessage> {
    //~ Static fields/initializers *****************************************************************

    /** DOCUMENT ME! */
    private static Log log = LogFactory.getLog(Buffer.class);

    /** DOCUMENT ME! */
    private static final String DEFAULT_FILE_NAME =
        ResourceManager.getProperty("main.default.filename");

    //~ Instance fields ****************************************************************************

    /** DOCUMENT ME! */
    private ColourTextArea editor = null;

    /** DOCUMENT ME! */
    private FindReplaceWorker worker = null;

    /** DOCUMENT ME! */
    private LineNumbering lines = null;

    /** DOCUMENT ME! */
    private String fileName = null;

    /** DOCUMENT ME! */
    private UndoManager undo = null;

    /** DOCUMENT ME! */
    private UndoableEditListener undoableListener = null;

    /** DOCUMENT ME! */
    private boolean modifiedState = false;

    /** DOCUMENT ME! */
    private boolean newState = true;

    //~ Constructors *******************************************************************************

    /**
     * Creates a new Buffer object.
     */
    public Buffer() {
        super();

        setLayout(new BorderLayout());

        JPanel internalPanel = new JPanel();
        internalPanel.setLayout(new BorderLayout());

        editor = new ColourTextArea();

        int red   = PreferenceManager.getInt("general.editor.background.red", 0);
        int green = PreferenceManager.getInt("general.editor.background.green", 0);
        int blue  = PreferenceManager.getInt("general.editor.background.blue", 0);
        editor.setBackground(new Color(red, green, blue));

        red       = PreferenceManager.getInt("general.editor.foreground.red", 0);
        green     = PreferenceManager.getInt("general.editor.foreground.green", 0);
        blue      = PreferenceManager.getInt("general.editor.foreground.blue", 0);
        editor.setForeground(new Color(red, green, blue));

        red       = PreferenceManager.getInt("general.editor.selection.red", 0);
        green     = PreferenceManager.getInt("general.editor.selection.green", 0);
        blue      = PreferenceManager.getInt("general.editor.selection.blue", 0);
        editor.setSelectionColor(new Color(red, green, blue));

        String font = PreferenceManager.getString("general.editor.font", "Monospaced");
        int size    = PreferenceManager.getInt("general.editor.fontSize", 12);
        editor.setFont(new Font(font, Font.PLAIN, size));

        editor.setEditable(true);
        editor.setDragEnabled(true);
        editor.setEditorKit(EditorKitManager.createEditorKit(getExtension(DEFAULT_FILE_NAME)));

        editor.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));

        InputMap map = editor.getInputMap(JComponent.WHEN_FOCUSED);

        for (InputMap imap = map; imap != null; imap = imap.getParent()) {
            imap.remove(KeyStroke.getKeyStroke('V', InputEvent.CTRL_MASK, false));
            imap.remove(KeyStroke.getKeyStroke('C', InputEvent.CTRL_MASK, false));
            imap.remove(KeyStroke.getKeyStroke('X', InputEvent.CTRL_MASK, false));
            imap.remove(KeyStroke.getKeyStroke('A', InputEvent.CTRL_MASK, false));
        }

        editor.setInputMap(JComponent.WHEN_FOCUSED, map);

        editor.addCaretListener(new CaretListener() {
                public void caretUpdate(CaretEvent evt) {
                    MessageManager.sendMessage(new BufferMessage(evt.getSource(),
					                     BufferMessage.CARET_EVENT));
                }
            });

        internalPanel.add(BorderLayout.CENTER, editor);

        if (PreferenceManager.getBoolean("general.editor.lineNumbers", false)) {
            lines = new LineNumbering();
            lines.setPreferredSize(new Dimension(50, 0));
            lines.setFont(new Font(font, Font.PLAIN, size));
            lines.setFocusable(false);
            lines.setDocument(editor.getDocument());

            internalPanel.add(BorderLayout.WEST, lines);
        }

        JScrollPane scroll = new JScrollPane(internalPanel);
        scroll.getVerticalScrollBar().setUnitIncrement(10);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.getViewport().setScrollMode(JViewport.BLIT_SCROLL_MODE);

        add(BorderLayout.CENTER, scroll);

        editor.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent evt) {
                    if (evt.isPopupTrigger()) {
                        WorkspaceManager.getPopupMenu().show(evt.getComponent(), evt.getX(),
                                                             evt.getY());
                    }
                }

                public void mouseReleased(MouseEvent evt) {
                    if (evt.isPopupTrigger()) {
                        WorkspaceManager.getPopupMenu().show(evt.getComponent(), evt.getX(),
                                                             evt.getY());
                    }
                }
            });

        undo     = new UndoManager();

        undoableListener =
            new UndoableEditListener() {
                    public void undoableEditHappened(UndoableEditEvent evt) {
                        undo.addEdit(evt.getEdit());

                        MessageManager.sendMessage(new BufferMessage(evt.getSource(),
						                     BufferMessage.UNDOABLE_EDIT_EVENT));

                        if (!modifiedState) {
                            setModifiedState(true);
                        }
                    }
                };

        editor.getDocument().addUndoableEditListener(undoableListener);

        MessageManager.addMessageListener(MessageManager.PREFERENCE_GROUP_NAME, this);

        fileName = DEFAULT_FILE_NAME;
    }

    //~ Methods ************************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getCaretPosition() {
        return editor.getCaretPosition();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getCurrentLine() {
        int position = getCaretPosition();

        return getDocument().getDefaultRootElement().getElementIndex(position);
    }
    
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getCurrentColumn() {
        int position = getCaretPosition();
        int line = getCurrentLine();
        
        Element element = getDocument().getDefaultRootElement().getElement(line);

        return (element != null)? position - element.getStartOffset() : position;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Document getDocument() {
        return editor.getDocument();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public File getFile() {
        return new File(this.fileName);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getFileName() {
        return this.fileName;
    }
    
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getText() {
    	int length = getDocument().getLength();
    	try {
			return getDocument().getText(0, length);
		} catch (BadLocationException e) {
			log.error("error getting buffer text", e);
		}
		return null;
    }
    
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getText(int offset, int length) {
    	try {
			return getDocument().getText(offset, length);
		} catch (BadLocationException e) {
			log.error("error getting buffer text", e);
		}
		return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getLineCount() {
        return getDocument().getDefaultRootElement().getElementCount();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isModified() {
        return modifiedState;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isNew() {
        return newState;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isReadOnly() {
        return !editor.isEditable();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getSelectionEnd() {
        return editor.getSelectionEnd();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getSelectionStart() {
        return editor.getSelectionStart();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean canRedo() {
        return undo.canRedo();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean canUndo() {
        return undo.canUndo();
    }

    /**
     * DOCUMENT ME!
     */
    public void clean() {
        editor.setText("");
        editor.setEditorKit(EditorKitManager.createEditorKit(getExtension(DEFAULT_FILE_NAME)));

        if (lines != null) {
            lines.setDocument(editor.getDocument());
        }

        resetDocument();

        setFileName(DEFAULT_FILE_NAME);

        setNewState(true);
        setModifiedState(false);
    }

    /**
     * DOCUMENT ME!
     */
    public void close() {
        MessageManager.removeMessageListener(MessageManager.PREFERENCE_GROUP_NAME, this);

        Document doc = editor.getDocument();

        ((ICleanable) doc).cleanup();
    }

    /**
     * DOCUMENT ME!
     */
    public void copy() {
        editor.copy();
    }

    /**
     * DOCUMENT ME!
     */
    public void cut() {
        editor.cut();
    }

    /**
     * DOCUMENT ME!
     */
    public void findNext() {
        if (worker == null) {
            worker = new FindReplaceWorker(this);
        }

        worker.next();
    }

    /**
     * DOCUMENT ME!
     */
    public void findPrevious() {
        if (worker == null) {
            worker = new FindReplaceWorker(this);
        }

        worker.previous();
    }

    /**
     * DOCUMENT ME!
     *
     * @param line DOCUMENT ME!
     */
    public void gotoLine(int line) {
        Element root = editor.getDocument().getDefaultRootElement();

        if ((line < root.getElementCount()) && (line > 0)) {
            Element element = root.getElement(line - 1);
            editor.setCaretPosition(element.getStartOffset());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param url DOCUMENT ME!
     */
    public void open(URL url) {
        open(url.getFile());
    }

    /**
     * DOCUMENT ME!
     *
     * @param file DOCUMENT ME!
     */
    public void open(String file) {
        open(new File(file));
    }

    /**
     * DOCUMENT ME!
     *
     * @param file DOCUMENT ME!
     */
    public void open(File file) {
        try {
            editor.setEditorKit(EditorKitManager.createEditorKit(getExtension(file.getName())));
            editor.getDocument().removeUndoableEditListener(undoableListener);

            FileReader in = new FileReader(file);
            editor.read(in, null);

            if (lines != null) {
                lines.setDocument(editor.getDocument());
            }

            editor.setEditable(file.canWrite());

            resetDocument();

            setFileName(file.getAbsolutePath());

            setNewState(false);
            setModifiedState(false);

            if (log.isDebugEnabled()) {
                log.debug("contentType=" + editor.getContentType());
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void paste() {
        editor.paste();
    }

    /**
     * DOCUMENT ME!
     *
     * @param message DOCUMENT ME!
     */
    public void receiveMessage(PreferenceMessage message) {
        if (message.getKey().equals("general.editor.background")) {
            final Color background = (Color) message.getNewValue();
            SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        editor.setBackground(background);
                    }
                });
        } else if (message.getKey().equals("general.editor.foreground")) {
            final Color foreground = (Color) message.getNewValue();
            SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        editor.setForeground(foreground);
                    }
                });
        } else if (message.getKey().equals("general.editor.selection")) {
            final Color selection = (Color) message.getNewValue();
            SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        editor.setSelectionColor(selection);
                    }
                });
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void redo() {
        try {
            undo.redo();
            MessageManager.sendMessage(new BufferMessage(this, BufferMessage.UNDOABLE_EDIT_EVENT));

            if (!modifiedState) {
                setModifiedState(true);
            }
        } catch (CannotRedoException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void replace() {
        if (worker == null) {
            worker = new FindReplaceWorker(this);
        }

        worker.replace();
    }

    /**
     * DOCUMENT ME!
     */
    public void save() {
        try {
            if (editor.isEditable()) {
                FileWriter out = new FileWriter(this.fileName);

                editor.write(out);

                setModifiedState(false);
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param url DOCUMENT ME!
     */
    public void saveAs(URL url) {
        saveAs(url.getFile());
    }

    /**
     * DOCUMENT ME!
     *
     * @param file DOCUMENT ME!
     */
    public void saveAs(String file) {
        saveAs(new File(file));
    }

    /**
     * DOCUMENT ME!
     *
     * @param file DOCUMENT ME!
     */
    public void saveAs(File file) {
        try {
            FileWriter out = new FileWriter(file);

            editor.write(out);

            open(file);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param start DOCUMENT ME!
     * @param end DOCUMENT ME!
     */
    public void select(int start,
                       int end) {
        editor.select(start, end);
    }

    /**
     * DOCUMENT ME!
     */
    public void selectAll() {
        editor.selectAll();
    }

    /**
     * DOCUMENT ME!
     */
    public void undo() {
        try {
            undo.undo();
            MessageManager.sendMessage(new BufferMessage(this, BufferMessage.UNDOABLE_EDIT_EVENT));

            if (!modifiedState) {
                setModifiedState(true);
            }
        } catch (CannotUndoException e) {
            log.error(e.getMessage(), e);
        }
    }
    
    public void insertString(int offset, String text) {
    	try {
			getDocument().insertString(offset, text, null);
		} catch (BadLocationException e) {
			log.error(e.getMessage(), e);
		}
    }
    
    public void replaceString(int offset, int length, String text) {
    	try {
    		getDocument().remove(offset, length);
			getDocument().insertString(offset, text, null);
		} catch (BadLocationException e) {
			log.error(e.getMessage(), e);
		}
    }
    
    public void removeString(int offset, int length) {
    	try {
    		getDocument().remove(offset, length);
		} catch (BadLocationException e) {
			log.error(e.getMessage(), e);
		}
    }
    
    public Iterator<Element> elementIterator() {
    	return new ElementIterator(getDocument());
    }
    
    public Iterator<String> lineIterator() {
    	return new LineIterator(getDocument());
    }

    /**
     * DOCUMENT ME!
     *
     * @param fileName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private String getExtension(String fileName) {
        int index = fileName.lastIndexOf('.');

        if (index > 0) {
            return fileName.substring(index + 1);
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param fileName DOCUMENT ME!
     */
    private void setFileName(String fileName) {
        this.fileName = fileName;

        MessageManager.sendMessage(new BufferMessage(this, BufferMessage.CHANGE_FILE_EVENT));
    }

    /**
     * DOCUMENT ME!
     *
     * @param modifiedState DOCUMENT ME!
     */
    private void setModifiedState(boolean modifiedState) {
        this.modifiedState = modifiedState;

        MessageManager.sendMessage(new BufferMessage(this, BufferMessage.MODIFIED_FILE_EVENT));
    }

    /**
     * DOCUMENT ME!
     *
     * @param newState DOCUMENT ME!
     */
    private void setNewState(boolean newState) {
        this.newState = newState;
    }

    /**
     * DOCUMENT ME!
     */
    private void resetDocument() {
        editor.getDocument().addUndoableEditListener(undoableListener);

        undo.discardAllEdits();

        MessageManager.sendMessage(new BufferMessage(this, BufferMessage.UNDOABLE_EDIT_EVENT));
    }
    
    class ElementIterator implements Iterator<Element> {
    	
    	Document doc;
    	int count;
    	
    	public ElementIterator(Document doc) {
			this.doc = doc;
		}

		public boolean hasNext() {
			return doc.getDefaultRootElement().getElementCount() > count;
		}

		public Element next() {
			return doc.getDefaultRootElement().getElement(count++);
		}

		public void remove() {
			throw new RuntimeException("not implemented");
		}
    }
    
    class LineIterator implements Iterator<String> {
    	
    	Document doc;
    	int count;
    	
    	public LineIterator(Document doc) {
			this.doc = doc;
		}

		public boolean hasNext() {
			return doc.getDefaultRootElement().getElementCount() > count;
		}

		public String next() {
			Element line = doc.getDefaultRootElement().getElement(count++);
			try {
				return doc.getText(line.getStartOffset(), line.getEndOffset() - line.getStartOffset());
			} catch (BadLocationException e) {
				log.error(e.getMessage(), e);
			}
			return null;
		}

		public void remove() {
			throw new RuntimeException("not implemented");
		}
    }
}
