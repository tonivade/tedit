/*
 * $Id: ActionManager.java,v 1.1.1.1 2004/09/18 17:16:21 amunoz Exp $
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

import java.awt.event.ActionEvent;

import java.util.HashMap;
import java.util.Map;

import javax.swing.Action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tk.tomby.tedit.actions.ScriptletAction;


/**
 * DOCUMENT ME!
 *
 * @author $Author: amunoz $
 * @version $Revision: 1.1.1.1 $
 */
public class ActionManager {
    //~ Static fields/initializers *****************************************************************

    /** DOCUMENT ME! */
    private static Log log = LogFactory.getLog(ActionManager.class);

    /** DOCUMENT ME! */
    private static Map<String, Action> actions = new HashMap<String, Action>();

    //~ Methods ************************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Action getAction(String name) {
        return actions.get(name);
    }

    /**
     * DOCUMENT ME!
     *
     * @param action DOCUMENT ME!
     */
    public static void addAction(Action action) {
        actions.put((String) action.getValue("name"), action);
    }

    /**
     * DOCUMENT ME!
     *
     * @param actionName DOCUMENT ME!
     * @param actionClass DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Action createFromClass(String actionName,
                                         String actionClass) {
        Action action = actions.get(actionName);

        if (action == null) {
            try {
                action = (Action) Class.forName(actionClass).newInstance();

                if (log.isDebugEnabled()) {
                    log.debug("name=" + actionName);
                }

                actions.put(actionName, action);
            } catch (ClassNotFoundException e) {
                log.error(e.getMessage(), e);
            } catch (InstantiationException e) {
                log.error(e.getMessage(), e);
            } catch (IllegalAccessException e) {
                log.error(e.getMessage(), e);
            }
        }

        return action;
    }

    /**
     * DOCUMENT ME!
     *
     * @param actionName DOCUMENT ME!
     * @param scriptLang DOCUMENT ME!
     * @param scriptFile DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Action createFromScriptlet(String actionName,
                                             String scriptLang,
                                             String scriptFile) {
        Action action = actions.get(actionName);

        if (action == null) {
            action = new ScriptletAction(actionName, scriptLang, scriptFile);

            if (log.isDebugEnabled()) {
                log.debug("name=" + actionName);
            }

            actions.put(actionName, action);
        }

        return action;
    }

    /**
     * DOCUMENT ME!
     *
     * @param actionName DOCUMENT ME!
     * @param evt DOCUMENT ME!
     */
    public static void invokeAction(String      actionName,
                                    ActionEvent evt) {
        Action action = getAction(actionName);

        if (action != null) {
            action.actionPerformed(evt);
        }
    }
}
