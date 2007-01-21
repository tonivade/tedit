/*
 * $Id: ThreadSafeRepaintManager.java,v 1.1.1.1 2004/09/18 17:16:19 amunoz Exp $
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

import javax.swing.JComponent;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * DOCUMENT ME!
 *
 * @author $Author: amunoz $
 * @version $Revision: 1.1.1.1 $
 */
public class ThreadSafeRepaintManager extends RepaintManager {
	
	private static final Log log = LogFactory.getLog(ThreadSafeRepaintManager.class);
	
    //~ Methods ************************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param w DOCUMENT ME!
     * @param h DOCUMENT ME!
     */
    public synchronized void addDirtyRegion(JComponent c,
                                            int        x,
                                            int        y,
                                            int        w,
                                            int        h) {
        checkThread(c);
        super.addDirtyRegion(c, x, y, w, h);
    }

    /**
     * DOCUMENT ME!
     *
     * @param invalidComponent DOCUMENT ME!
     */
    public synchronized void addInvalidComponent(JComponent invalidComponent) {
        checkThread(invalidComponent);
        super.addInvalidComponent(invalidComponent);
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private boolean isRootShowing(JComponent c) {
        return (c.getRootPane() != null) && c.getRootPane().isShowing();
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private boolean isShowing(JComponent c) {
        return c.isShowing();
    }
    
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
	private boolean isThreadValid() {
		return (SwingUtilities.isEventDispatchThread() || Thread.currentThread().getName().equals("SyntheticImageGenerator"));
	}

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     *
     * @throws Error DOCUMENT ME!
     */
    private void checkThread(JComponent c) {
        if (!isThreadValid() && (isShowing(c) || isRootShowing(c))) {
        	boolean threadSafeMethod = false;
            boolean fromSwing = false;
            StackTraceElement[] stackTrace = new Exception().getStackTrace();
            for (int i = 0; i < stackTrace.length; i++) {
                if (threadSafeMethod && stackTrace[i].getClassName().startsWith("javax.swing.")) {
                    fromSwing = true;
                }
                if ("repaint".equals(stackTrace[i].getMethodName())) {
                    threadSafeMethod = true;
                    fromSwing = false;
                }
            }

            if (threadSafeMethod && !fromSwing) {
                // Calling a thread safe method; no problem
                return;
            }

            log.warn("display error", new Error("Invalid thread access to display"));
        }
    }
}
