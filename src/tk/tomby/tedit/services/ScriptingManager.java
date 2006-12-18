/*
 * $Id: ScriptingManager.java,v 1.3 2005/03/25 12:01:10 amunoz Exp $
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

package tk.tomby.tedit.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Iterator;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.SwingUtilities;
import javax.swing.text.Document;
import javax.swing.text.Element;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sun.script.groovy.GroovyScriptEngineFactory;
import com.sun.script.jruby.JRubyScriptEngineFactory;

import tk.tomby.tedit.core.IBuffer;


/**
 * DOCUMENT ME!
 *
 * @author $Author: amunoz $
 * @version $Revision: 1.3 $
 */
public class ScriptingManager {
    //~ Static fields/initializers *****************************************************************

    /** DOCUMENT ME! */
    private static Log log = LogFactory.getLog(ScriptingManager.class);

    /** DOCUMENT ME! */
    public static String GROOVY_LANGUAGE = "groovy";

    /** DOCUMENT ME! */
    public static String RUBY_LANGUAGE = "ruby";

    /** DOCUMENT ME! */
    public static String DEFAULT_LANGUAGE = GROOVY_LANGUAGE;
    
    /** DOCUMENT ME! */ 
    private static ScriptEngineManager manager = new ScriptEngineManager();
    
    static {
    	register("ruby", new String[] { "rb" }, new JRubyScriptEngineFactory());
    	register("groovy", new String[] { "gy", "groovy" }, new GroovyScriptEngineFactory());
    }

    //~ Methods ************************************************************************************
    
    /**
     * DOCUMENT ME!
     *
     * @param lang DOCUMENT ME!
     * @param script DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Object eval(String lang,
                              String script) {
        return eval(lang, script, null);
    }

    /**
     * DOCUMENT ME!
     *
     * @param lang DOCUMENT ME!
     * @param script DOCUMENT ME!
     * @param buffer DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Object eval(String  lang,
                              String  script,
                              IBuffer buffer) {
        Object result = null;
        
        try {
        	ScriptEngine engine = manager.getEngineByName(lang);
        	
        	if (buffer != null) {
            	Bindings bindings = engine.createBindings();
            	bindings.put("buffer", new BufferDecorator(buffer));
            	engine.setBindings(bindings, ScriptContext.ENGINE_SCOPE);
            }

            result = engine.eval(script);
        } catch (ScriptException e) {
            log.error(e);
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param lang DOCUMENT ME!
     * @param script DOCUMENT ME!
     */
    public static Object exec(String lang,
                              InputStream script) {
        return exec(lang, script, null);
    }

    /**
     * DOCUMENT ME!
     *
     * @param lang DOCUMENT ME!
     * @param script DOCUMENT ME!
     * @param buffer DOCUMENT ME!
     */
    public static Object exec(String  lang,
                              InputStream  stream,
                              IBuffer buffer) {
    	Object result = null;
    	BufferedReader reader = null;
        
        try {
        	ScriptEngine engine = manager.getEngineByName(lang);
        	
            reader = new BufferedReader(new InputStreamReader(stream));

            if (buffer != null) {
            	Bindings bindings = engine.createBindings();
            	bindings.put("buffer", new BufferDecorator(buffer));
            	engine.setBindings(bindings, ScriptContext.ENGINE_SCOPE);
            }
            
            engine.eval(reader);

        } catch (ScriptException e) {
            log.error(e);
        } finally {
        	if (reader != null) {
        		try {
					reader.close();
				} catch (IOException e) {
					log.warn(e);
				}
        	}
        }
        
        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     * @param engine DOCUMENT ME!
     * @param exts DOCUMENT ME!
     */
    public static void register(String name,
                                String[] exts,
                                ScriptEngineFactory factory) {
        manager.registerEngineName(name, factory);
        
        for (int i = 0; i < exts.length; i++) {
			manager.registerEngineExtension(exts[i], factory);
		}
    }
    
    public static String getLanguage(String name) {
		ScriptEngine engine = manager.getEngineByExtension(getExtension(name));
		
		if (engine != null) {
			return engine.getFactory().getLanguageName();
		}
		
		return null;
	}
    
    private static String getExtension(String fileName) {
        int index = fileName.lastIndexOf('.');

        if (index > 0) {
            return fileName.substring(index + 1);
        }

        return null;
    }
    
    public static class BufferDecorator implements IBuffer {
    	
    	private IBuffer buffer = null;
    	
    	public BufferDecorator(IBuffer buffer) {
    		this.buffer = buffer;
    	}

		public boolean canRedo() {
			return buffer.canRedo();
		}

		public boolean canUndo() {
			return buffer.canUndo();
		}

		public void clean() {
			buffer.clean();
		}

		public void close() {
			buffer.close();
		}

		public void copy() {
			buffer.copy();
		}

		public void cut() {
			buffer.cut();
		}

		public void findNext() {
			buffer.findNext();
		}

		public void findPrevious() {
			buffer.findPrevious();
		}

		public int getCaretPosition() {
			return buffer.getCaretPosition();
		}

		public int getCurrentLine() {
			return buffer.getCurrentLine();
		}

		public Document getDocument() {
			return buffer.getDocument();
		}

		public File getFile() {
			return buffer.getFile();
		}

		public String getFileName() {
			return buffer.getFileName();
		}

		public int getLineCount() {
			return buffer.getLineCount();
		}

		public int getSelectionEnd() {
			return buffer.getSelectionEnd();
		}

		public int getSelectionStart() {
			return buffer.getSelectionStart();
		}

		public void gotoLine(final int line) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					buffer.gotoLine(line);
				}
			});
		}

		public boolean isModified() {
			return buffer.isModified();
		}

		public boolean isNew() {
			return buffer.isNew();
		}

		public boolean isReadOnly() {
			return buffer.isReadOnly();
		}

		public void open(File file) {
			buffer.open(file);
		}

		public void open(String file) {
			buffer.open(file);
		}

		public void open(URL url) {
			buffer.open(url);
		}

		public void paste() {
			buffer.paste();
		}

		public void redo() {
			buffer.redo();
		}

		public void replace() {
			buffer.replace();
		}

		public void save() {
			buffer.save();
		}

		public void saveAs(File file) {
			buffer.saveAs(file);
		}

		public void saveAs(String file) {
			buffer.saveAs(file);
		}

		public void saveAs(URL url) {
			buffer.saveAs(url);
		}

		public void select(int start, int end) {
			buffer.select(start, end);
		}

		public void selectAll() {
			buffer.selectAll();
		}

		public void undo() {
			buffer.undo();
		}

		public Iterator<Element> elementIterator() {
			return buffer.elementIterator();
		}

		public void insertString(final int offset, final String text) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					buffer.insertString(offset, text);
				}
			});
		}

		public Iterator<String> lineIterator() {
			return buffer.lineIterator();
		}
    }

}
