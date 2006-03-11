/*
 * $Id: MessageManager.java,v 1.1.1.1 2004/09/18 17:16:21 amunoz Exp $
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

import java.lang.reflect.InvocationTargetException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tk.tomby.tedit.messages.IMessage;
import tk.tomby.tedit.messages.IMessageListener;


/**
 * DOCUMENT ME!
 *
 * @author $Author: amunoz $
 * @version $Revision: 1.1.1.1 $
 */
public class MessageManager {
    //~ Static fields/initializers *****************************************************************

    /** DOCUMENT ME! */
    public static final String DEFAULT_GROUP_NAME = "default";

    /** DOCUMENT ME! */
    public static final String ACTIVATION_GROUP_NAME = "activation";

    /** DOCUMENT ME! */
    public static final String BUFFER_GROUP_NAME = "buffer";

    /** DOCUMENT ME! */
    public static final String STATUS_GROUP_NAME = "status";

    /** DOCUMENT ME! */
    public static final String WORKSPACE_GROUP_NAME = "workspace";
    
    /** DOCUMENT ME! */
    public static final String PREFERENCE_GROUP_NAME = "preference";

    /** DOCUMENT ME! */
    public static final String DEFAULT_METHOD_NAME = "receiveMessage";

    /** DOCUMENT ME! */
    private static Map groups = new HashMap();

    /** DOCUMENT ME! */
    private static Log log = LogFactory.getLog(MessageManager.class);

    static {
        createGroup(DEFAULT_GROUP_NAME);
        createGroup(ACTIVATION_GROUP_NAME);
        createGroup(BUFFER_GROUP_NAME);
        createGroup(STATUS_GROUP_NAME);
        createGroup(WORKSPACE_GROUP_NAME);
        createGroup(PREFERENCE_GROUP_NAME);
    }

    //~ Methods ************************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @param listener DOCUMENT ME!
     */
    public static void addMessageListener(IMessageListener listener) {
        addMessageListener(DEFAULT_GROUP_NAME, listener);
    }

    /**
     * DOCUMENT ME!
     *
     * @param groupName DOCUMENT ME!
     * @param listener DOCUMENT ME!
     */
    public static void addMessageListener(String           groupName,
                                          IMessageListener listener) {
        GroupManager group = (GroupManager) groups.get(groupName);

        if (group != null) {
            group.addMessageListener(listener);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param groupName DOCUMENT ME!
     */
    public static void createGroup(String groupName) {
        groups.put(groupName, new GroupManager());
    }

    /**
     * DOCUMENT ME!
     *
     * @param listener DOCUMENT ME!
     */
    public static void removeMessageListener(IMessageListener listener) {
        removeMessageListener(DEFAULT_GROUP_NAME, listener);
    }

    /**
     * DOCUMENT ME!
     *
     * @param groupName DOCUMENT ME!
     * @param listener DOCUMENT ME!
     */
    public static void removeMessageListener(String           groupName,
                                             IMessageListener listener) {
        GroupManager group = (GroupManager) groups.get(groupName);

        if (group != null) {
            group.removeMessageListener(listener);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param message DOCUMENT ME!
     */
    public static void sendMessage(IMessage message) {
        sendMessage(DEFAULT_GROUP_NAME, message);
    }

    /**
     * DOCUMENT ME!
     *
     * @param groupName DOCUMENT ME!
     * @param message DOCUMENT ME!
     */
    public static void sendMessage(String   groupName,
                                   IMessage message) {
        GroupManager group = (GroupManager) groups.get(groupName);

        if (group != null) {
            Dispacher dispacher = new Dispacher(group, message);

            Thread worker = new Thread(dispacher);
            
            worker.start();
        }
    }

    //~ Inner Classes ******************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @author $Author: amunoz $
     * @version $Revision: 1.1.1.1 $
     */
    public static class GroupManager {
        /** DOCUMENT ME! */
        private List listeners = new ArrayList();

        /** DOCUMENT ME! */
        private Object monitor = new Object();

        /**
         * DOCUMENT ME!
         *
         * @param listener DOCUMENT ME!
         */
        public void addMessageListener(IMessageListener listener) {
            synchronized (monitor) {
                listeners.add(listener);
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @param listener DOCUMENT ME!
         */
        public void removeMessageListener(IMessageListener listener) {
            synchronized (monitor) {
                listeners.remove(listener);
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @param message DOCUMENT ME!
         */
        public void sendMessage(IMessage message) {
            synchronized (monitor) {
                for (Iterator i = listeners.iterator(); i.hasNext();) {
                    IMessageListener listener = (IMessageListener) i.next();

                    try {
                        MethodUtils.invokeExactMethod(listener, DEFAULT_METHOD_NAME, message);
                    } catch (NoSuchMethodException e) {
                        try {
                            MethodUtils.invokeMethod(listener, DEFAULT_METHOD_NAME, message);
                        } catch (NoSuchMethodException e1) {
                            log.error(e.getMessage(), e);
                        } catch (IllegalAccessException e1) {
                            log.error(e.getMessage(), e);
                        } catch (InvocationTargetException e1) {
                            log.error(e.getMessage(), e);
                        }
                    } catch (IllegalAccessException e) {
                        log.error(e.getMessage(), e);
                    } catch (InvocationTargetException e) {
                        log.error(e.getMessage(), e);
                    }
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $Author: amunoz $
     * @version $Revision: 1.1.1.1 $
     */
    public static class Dispacher implements Runnable {
        /** DOCUMENT ME! */
        private GroupManager group = null;

        /** DOCUMENT ME! */
        private IMessage message = null;

        /**
         * Creates a new Dispacher object.
         *
         * @param group DOCUMENT ME!
         * @param message DOCUMENT ME!
         */
        public Dispacher(GroupManager group,
                         IMessage     message) {
            this.group       = group;
            this.message     = message;
        }

        /**
         * DOCUMENT ME!
         */
        public void run() {
            group.sendMessage(message);
        }
    }
}
