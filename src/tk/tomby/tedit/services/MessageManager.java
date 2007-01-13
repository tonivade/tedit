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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tk.tomby.tedit.messages.ActivationMessage;
import tk.tomby.tedit.messages.BufferMessage;
import tk.tomby.tedit.messages.IMessage;
import tk.tomby.tedit.messages.IMessageListener;
import tk.tomby.tedit.messages.PreferenceMessage;
import tk.tomby.tedit.messages.StatusMessage;
import tk.tomby.tedit.messages.WorkspaceMessage;


/**
 * DOCUMENT ME!
 *
 * @author $Author: amunoz $
 * @version $Revision: 1.1.1.1 $
 */
public class MessageManager {
    //~ Static fields/initializers *****************************************************************
	
	/** DOCUMENT ME! */
	private static final Log log = LogFactory.getLog(MessageManager.class);

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
    private static Map<String, GroupManager> groups = new HashMap<String, GroupManager>();
    
    /** DOCUMENT ME! */
    private static BlockingQueue<IMessage> queue = new LinkedBlockingQueue<IMessage>();
    
    /** DOCUMENT ME! */
    private static Thread dispatcherThread = new Thread(new Dispacher(), "Message-Dispatcher");

    static {
        addGroup(ACTIVATION_GROUP_NAME, new GroupManager<ActivationMessage>());
        addGroup(BUFFER_GROUP_NAME, new GroupManager<BufferMessage>());
        addGroup(STATUS_GROUP_NAME, new GroupManager<StatusMessage>());
        addGroup(WORKSPACE_GROUP_NAME, new GroupManager<WorkspaceMessage>());
        addGroup(PREFERENCE_GROUP_NAME, new GroupManager<PreferenceMessage>());
        
        dispatcherThread.start();
    }

    //~ Methods ************************************************************************************
    
    /**
     * DOCUMENT ME!
     *
     * @param groupName DOCUMENT ME!
     */
    public static void addGroup(String groupName, GroupManager groupManager) {
        groups.put(groupName, groupManager);
    }

    /**
     * DOCUMENT ME!
     *
     * @param groupName DOCUMENT ME!
     * @param listener DOCUMENT ME!
     */
    public static void addMessageListener(String           groupName,
                                          IMessageListener listener) {
        GroupManager group = groups.get(groupName);

        if (group != null) {
            group.addMessageListener(listener);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param groupName DOCUMENT ME!
     * @param listener DOCUMENT ME!
     */
    public static void removeMessageListener(String           groupName,
                                             IMessageListener listener) {
        GroupManager group = groups.get(groupName);

        if (group != null) {
            group.removeMessageListener(listener);
        }
    }

    /**
     * DOCUMENT ME!
     * @param message DOCUMENT ME!
     */
    public static void sendMessage(IMessage message) {
        queue.add(message);
    }

    //~ Inner Classes ******************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @author $Author: amunoz $
     * @version $Revision: 1.1.1.1 $
     */
    public static class GroupManager<T extends IMessage> {
        /** DOCUMENT ME! */
        private List<IMessageListener<T>> listeners = Collections.synchronizedList(new ArrayList<IMessageListener<T>>());

        /**
         * DOCUMENT ME!
         *
         * @param listener DOCUMENT ME!
         */
        public void addMessageListener(IMessageListener<T> listener) {
            listeners.add(listener);
        }

        /**
         * DOCUMENT ME!
         *
         * @param listener DOCUMENT ME!
         */
        public void removeMessageListener(IMessageListener<T> listener) {
            listeners.remove(listener);
        }

        /**
         * DOCUMENT ME!
         *
         * @param message DOCUMENT ME!
         */
        public void sendMessage(T message) {
        	List<IMessageListener<T>> listenersCopy = new ArrayList<IMessageListener<T>>(listeners);
        	
            for (Iterator<IMessageListener<T>> i = listenersCopy.iterator(); i.hasNext();) {
                IMessageListener<T> listener = i.next();
                
                listener.receiveMessage(message);
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
		public void run() {
			Thread thread = Thread.currentThread();
			
			while(!thread.isInterrupted()) {
				try {
					IMessage message = queue.take();
					
					GroupManager group = groups.get(message.getGroup());
					
					if (group != null) {
						group.sendMessage(message);
					}
				} catch (InterruptedException e) {
					log.warn("error", e);
				}
			}
		}
    }
}
