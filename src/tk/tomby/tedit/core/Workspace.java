/*
 * $Id: Workspace.java,v 1.1.1.1 2004/09/18 17:16:19 amunoz Exp $
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
import java.awt.Dimension;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.flexdock.docking.DockingConstants;
import org.flexdock.docking.DockingManager;
import org.flexdock.docking.defaults.DefaultDockingPort;

import tk.tomby.tedit.core.preferences.IPreferencePage;
import tk.tomby.tedit.core.preferences.PreferencePageDriver;

import tk.tomby.tedit.messages.BufferMessage;
import tk.tomby.tedit.messages.IMessageListener;
import tk.tomby.tedit.messages.PreferenceMessage;
import tk.tomby.tedit.messages.WorkspaceMessage;

import tk.tomby.tedit.plugins.IDockablePlugin;
import tk.tomby.tedit.plugins.IPlugin;

import tk.tomby.tedit.services.MessageManager;
import tk.tomby.tedit.services.PreferenceManager;
import tk.tomby.tedit.services.ResourceManager;
import tk.tomby.tedit.services.TaskManager;
import tk.tomby.tedit.services.WorkspaceManager;


/**
 * DOCUMENT ME!
 *
 * @author $Author: amunoz $
 * @version $Revision: 1.1.1.1 $
 */
public class Workspace extends JPanel implements IWorkspace {
    //~ Static fields/initializers *****************************************************************

    /** DOCUMENT ME! */
    private static Log log = LogFactory.getLog(Workspace.class);

    //~ Instance fields ****************************************************************************

    /** DOCUMENT ME! */
    private JSplitPane splitPaneBottom = null;

    /** DOCUMENT ME! */
    private JSplitPane splitPaneLeft = null;

    /** DOCUMENT ME! */
    private JSplitPane splitPaneRight = null;

    /** DOCUMENT ME! */
    private JTabbedPane bufferPane = null;

    /** DOCUMENT ME! */
    private DefaultDockingPort bottomPort = null;

    /** DOCUMENT ME! */
    private DefaultDockingPort leftPort = null;

    /** DOCUMENT ME! */
    private DefaultDockingPort rightPort = null;

    //~ Constructors *******************************************************************************

    /**
     * Creates a new WorkSpace object.
     */
    public Workspace() {
        super();

        this.setLayout(new BorderLayout());

        MessageManager.addMessageListener(MessageManager.BUFFER_GROUP_NAME, new IMessageListener<BufferMessage>() {
        	public void receiveMessage(BufferMessage message) {
        		Workspace.this.receiveMessage(message);
        	}
        });
        MessageManager.addMessageListener(MessageManager.PREFERENCE_GROUP_NAME, new IMessageListener<PreferenceMessage>() {
        	public void receiveMessage(PreferenceMessage message) {
        		Workspace.this.receiveMessage(message);
        	}
        });

        bufferPane = new JTabbedPane();
        bufferPane.setMinimumSize(new Dimension(600, 400));
        bufferPane.setPreferredSize(new Dimension(800, 600));
        bufferPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        bottomPort = new DefaultDockingPort();
        bottomPort.setPreferredSize(new Dimension(0, 80));

        rightPort = new DefaultDockingPort();
        rightPort.setMinimumSize(new Dimension(0, 0));
        rightPort.setPreferredSize(new Dimension(80, 0));

        leftPort = new DefaultDockingPort();
        leftPort.setMinimumSize(new Dimension(0, 0));
        leftPort.setPreferredSize(new Dimension(80, 0));

        splitPaneLeft = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPort, bufferPane);
        splitPaneLeft.setBorder(BorderFactory.createEmptyBorder());
        splitPaneLeft.setOneTouchExpandable(true);

        splitPaneRight = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, splitPaneLeft, rightPort);
        splitPaneRight.setBorder(BorderFactory.createEmptyBorder());
        splitPaneRight.setOneTouchExpandable(true);

        splitPaneBottom = new JSplitPane(JSplitPane.VERTICAL_SPLIT, splitPaneRight, bottomPort);
        splitPaneBottom.setBorder(BorderFactory.createEmptyBorder());
        splitPaneBottom.setOneTouchExpandable(true);

        bufferPane.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent evt) {
                    MessageManager.sendMessage(MessageManager.WORKSPACE_GROUP_NAME,
                                               new WorkspaceMessage(evt.getSource(),
                                                                    bufferPane.getSelectedComponent()));
                }
            });

        bufferPane.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent evt) {
                    if (evt.getClickCount() == 2) {
                        splitPaneLeft.setDividerLocation(0.0d);
                        splitPaneBottom.setDividerLocation(1.0d);
                        splitPaneRight.setDividerLocation(1.0d);
                    }
                }
            });

        DropTarget dropTarget =
            new DropTarget(bufferPane,
                           new DropTargetAdapter() {
                    public void drop(DropTargetDropEvent dtde) {
                        if (log.isDebugEnabled()) {
                            log.debug("drop start");
                        }

                        try {
                            if (log.isDebugEnabled()) {
                                log.debug(dtde.getSource());
                            }

                            Transferable tr      = dtde.getTransferable();
                            DataFlavor[] flavors = tr.getTransferDataFlavors();

                            for (int i = 0; i < flavors.length; i++) {
                                DataFlavor flavor = flavors[i];

                                if (log.isDebugEnabled()) {
                                    log.debug("mime-type:" + flavor.getMimeType());
                                }

                                if (flavor.isMimeTypeEqual("text/plain")) {
                                    final Object obj = tr.getTransferData(flavor);

                                    if (log.isDebugEnabled()) {
                                        log.debug(obj);
                                    }

                                    if (obj instanceof String) {
                                        TaskManager.execute(new Runnable() {
                                        	public void run() {
                                                BufferFactory factory = new BufferFactory();
                                                IBuffer buffer        = factory.createBuffer();
                                                buffer.open((String) obj);

                                                addBuffer(buffer);
                                            }
                                        });
                                    }

                                    dtde.dropComplete(true);

                                    return;
                                }
                            }
                        } catch (UnsupportedFlavorException e) {
                            log.warn(e.getMessage(), e);
                        } catch (IOException e) {
                            log.warn(e.getMessage(), e);
                        }

                        dtde.rejectDrop();

                        if (log.isDebugEnabled()) {
                            log.debug("drop end");
                        }
                    }
                });

        this.setDropTarget(dropTarget);

        this.add(BorderLayout.CENTER, splitPaneBottom);
    }

    //~ Methods ************************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public IBuffer getBuffer(int i) {
        return (IBuffer) bufferPane.getComponentAt(i);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public List<IBuffer> getBufferList() {
        List<IBuffer> buffers = new ArrayList<IBuffer>();

        for (int i = 0; i < bufferPane.getTabCount(); i++) {
            buffers.add((IBuffer) bufferPane.getComponentAt(i));
        }

        return buffers;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public IBuffer getCurrentBuffer() {
        return (IBuffer) bufferPane.getSelectedComponent();
    }

    /**
     * DOCUMENT ME!
     *
     * @param buffer DOCUMENT ME!
     */
    public void addBuffer(final IBuffer buffer) {
        SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    bufferPane.addTab(buffer.getFile().getName(), null, (JComponent) buffer,
                                      buffer.getFileName());
                    bufferPane.setSelectedComponent((JComponent) buffer);
                }
            });
    }

    /**
     * DOCUMENT ME!
     *
     * @param plugin DOCUMENT ME!
     */
    public void addPlugin(IPlugin plugin) {
        IDockablePlugin dock = (IDockablePlugin) plugin;

        DockingManager.registerDockable(dock.getDockable(), dock.getDockTitle());

        switch (dock.getDockLocation()) {
            case PLUGIN_BOTTOM:
                bottomPort.dock(dock.getDockable(), DockingConstants.CENTER_REGION);

                break;

            case PLUGIN_LEFT:
                leftPort.dock(dock.getDockable(), DockingConstants.CENTER_REGION);

                break;

            case PLUGIN_RIGHT:
                rightPort.dock(dock.getDockable(), DockingConstants.CENTER_REGION);

                break;

            default:
                log.warn("invalid location");
        }

        SwingUtilities.updateComponentTreeUI(this);
    }

    /**
     * DOCUMENT ME!
     */
    public void closeCurrentBuffer() {
        IBuffer buffer = getCurrentBuffer();

        if (buffer != null) {
            buffer.close();

            bufferPane.remove((JComponent) buffer);
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void init() {
        IPreferencePage generalPage =
            new PreferencePageDriver("general", "tk/tomby/tedit/core/preferences/general").create();

        IPreferencePage highlightPage =
            new PreferencePageDriver("highlight", "tk/tomby/tedit/core/preferences/highlight")
            .create();

        WorkspaceManager.getPreferences().addPage(generalPage);
        WorkspaceManager.getPreferences().addPage(highlightPage);

        if (PreferenceManager.getInt("general.appearance.tabPosition", TAB_POSITION_TOP) == TAB_POSITION_TOP) {
            bufferPane.setTabPlacement(JTabbedPane.TOP);
        } else {
            bufferPane.setTabPlacement(JTabbedPane.BOTTOM);
        }

        ResourceManager.loadCategory("snr", "tk/tomby/tedit/core/snr");
        PreferenceManager.loadCategory("snr", "tk/tomby/tedit/core/snr");

        splitPaneBottom.setDividerLocation(PreferenceManager.getInt("main.workspace.bottom.divider",
                                                                    0));
        splitPaneLeft.setDividerLocation(PreferenceManager.getInt("main.workspace.left.divider", 0));
        splitPaneRight.setDividerLocation(PreferenceManager.getInt("main.workspace.right.divider", 0));
    }

    /**
     * DOCUMENT ME!
     *
     * @param message DOCUMENT ME!
     */
    public void receiveMessage(BufferMessage message) {
        IBuffer buffer = getCurrentBuffer();

        if (buffer != null) {
            final int index = bufferPane.indexOfComponent((JComponent) buffer);
            final File file = buffer.getFile();

            Runnable update = null;

            if (message.getType() == BufferMessage.CHANGE_FILE_EVENT) {
                update =
                    new Runnable() {
                            public void run() {
                                bufferPane.setTitleAt(index, file.getName());
                                bufferPane.setToolTipTextAt(index, file.getAbsolutePath());
                            }
                        };
            } else if ((message.getType() == BufferMessage.MODIFIED_FILE_EVENT)) {
                if (buffer.isModified()) {
                    update =
                        new Runnable() {
                                public void run() {
                                    bufferPane.setTitleAt(index, file.getName() + "*");
                                }
                            };
                } else {
                    update =
                        new Runnable() {
                                public void run() {
                                    bufferPane.setTitleAt(index, file.getName());
                                }
                            };
                }
            }

            if (update != null) {
                SwingUtilities.invokeLater(update);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param message DOCUMENT ME!
     */
    public void receiveMessage(PreferenceMessage message) {
        if (message.getKey().equals("general.appearance.tabPosition")) {
            Runnable update = null;

            if (((Integer) message.getNewValue()).intValue() == TAB_POSITION_TOP) {
                update =
                    new Runnable() {
                            public void run() {
                                bufferPane.setTabPlacement(JTabbedPane.TOP);
                            }
                        };
            } else {
                update =
                    new Runnable() {
                            public void run() {
                                bufferPane.setTabPlacement(JTabbedPane.BOTTOM);
                            }
                        };
            }

            if (update != null) {
                SwingUtilities.invokeLater(update);
            }
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void save() {
        PreferenceManager.putInt("main.workspace.bottom.divider",
                                 splitPaneBottom.getDividerLocation());
        PreferenceManager.putInt("main.workspace.left.divider", splitPaneLeft.getDividerLocation());
        PreferenceManager.putInt("main.workspace.right.divider", splitPaneRight.getDividerLocation());
    }
}
