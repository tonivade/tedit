/*
 * $Id: ScriptletAction.java,v 1.2 2004/09/26 20:29:10 amunoz Exp $
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

package tk.tomby.tedit.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import tk.tomby.tedit.services.ScriptingManager;
import tk.tomby.tedit.services.ThreadManager;
import tk.tomby.tedit.services.WorkspaceManager;


/**
 * DOCUMENT ME!
 *
 * @author $Author: amunoz $
 * @version $Revision: 1.2 $
 */
public class ScriptletAction extends AbstractAction {
    //~ Instance fields ****************************************************************************

    /** DOCUMENT ME! */
    private String file = null;

    /** DOCUMENT ME! */
    private String lang = ScriptingManager.DEFAULT_LANGUAGE;

    //~ Constructors *******************************************************************************

    /**
     * Creates a new ScriptletAction object.
     *
     * @param name DOCUMENT ME!
     */
    public ScriptletAction(String name) {
        super(name);
    }

    /**
     * Creates a new ScriptletAction object.
     *
     * @param name DOCUMENT ME!
     * @param lang DOCUMENT ME!
     * @param file DOCUMENT ME!
     */
    public ScriptletAction(String name,
                           String lang,
                           String file) {
        this(name);

        this.lang     = lang;
        this.file     = file;
    }

    //~ Methods ************************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @param file DOCUMENT ME!
     */
    public void setFile(String file) {
        this.file = file;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getFile() {
        return file;
    }

    /**
     * DOCUMENT ME!
     *
     * @param lang DOCUMENT ME!
     */
    public void setLang(String lang) {
        this.lang = lang;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getLang() {
        return lang;
    }

    /**
     * DOCUMENT ME!
     *
     * @param evt DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent evt) {
        ThreadManager.execute(new Runnable() {
	            public void run() {
	                ScriptingManager.exec(lang, file, WorkspaceManager.getCurrentBuffer());
	            }
	        });
    }
}
