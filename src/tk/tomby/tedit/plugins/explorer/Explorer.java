/*
 * $Id: Explorer.java,v 1.4 2004/11/28 12:16:49 amunoz Exp $
 *
 * Copyright (C) 2003 Antonio G. Mu�oz Conejo <amunoz@tomby.homelinux.org>
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

package tk.tomby.tedit.plugins.explorer;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.io.File;

import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tk.tomby.tedit.core.BufferFactory;
import tk.tomby.tedit.core.IBuffer;
import tk.tomby.tedit.core.IWorkspace;

import tk.tomby.tedit.messages.IMessageListener;

import tk.tomby.tedit.plugins.AbstractDockablePlugin;

import tk.tomby.tedit.services.PreferenceManager;
import tk.tomby.tedit.services.ResourceManager;
import tk.tomby.tedit.services.WorkspaceManager;


/**
 * DOCUMENT ME!
 *
 * @author $Author: amunoz $
 * @version $Revision: 1.4 $
 */
public class Explorer extends AbstractDockablePlugin implements IMessageListener {
    //~ Static fields/initializers *****************************************************************

    /** DOCUMENT ME! */
    private static Log log = LogFactory.getLog(Explorer.class);

    static {
        ResourceManager.loadCategory("explorer", "tk/tomby/tedit/plugins/explorer");
        PreferenceManager.loadCategory("explorer", "tk/tomby/tedit/plugins/explorer");
    }

    //~ Instance fields ****************************************************************************

    /** DOCUMENT ME! */
    private JList fileList = null;

    /** DOCUMENT ME! */
    private JSplitPane splitPane = null;

    /** DOCUMENT ME! */
    private JTree directoryTree = null;

    /** DOCUMENT ME! */
    private ShortedListModel fileListModel = null;

    /** DOCUMENT ME! */
    private ShortedTreeModel directoryTreeModel = null;

    /** DOCUMENT ME! */
    private TopPanel topPanel = null;

    //~ Constructors *******************************************************************************

    /**
     * Creates a new Explorer object.
     */
    public Explorer() {
        super();

        setLayout(new BorderLayout());

        location = PreferenceManager.getInt("explorer.location", IWorkspace.PLUGIN_LEFT);

        File rootDir =
            new File(PreferenceManager.getString("explorer.directory",
                                                 System.getProperty("user.home")));

        title.setText("Explorer");

        JPanel internalPanel = new JPanel();
        internalPanel.setLayout(new BorderLayout());

        topPanel = new TopPanel(rootDir.getAbsolutePath());
        topPanel.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    final JComboBox combo = (JComboBox) evt.getSource();

                    Thread worker =
                                   new Thread(new Runnable() {
                                public void run() {
                                    final String dirName = (String) combo.getSelectedItem();
                                    final File dir       = new File(dirName);

                                    if (dir.exists()) {
                                        final DefaultMutableTreeNode root =
                                              new DefaultMutableTreeNode(dir);
                                        directoryTreeModel =
                                              new ShortedTreeModel(root, new FileComparator());

                                        final List files = getFiles(dir);
                                        final List dirs  = openDirectory(dir);

                                        SwingUtilities.invokeLater(new Runnable() {
                                                public void run() {
                                                    directoryTreeModel.insertAllInto(dirs, root);
                                                    fileListModel.removeAllElements();
                                                    fileListModel.addAll(files);
                                                    directoryTree.setModel(directoryTreeModel);

                                                    directoryTree.expandRow(0);
                                                }
                                            });
                                    }
                                }
                            });

                    worker.start();
                }
            });

        internalPanel.add(BorderLayout.NORTH, topPanel);

        DefaultMutableTreeNode directoryRoot = new DefaultMutableTreeNode(rootDir);

        directoryTreeModel     = new ShortedTreeModel(directoryRoot, new FileComparator());
        directoryTree          = new JTree(directoryTreeModel);
        directoryTree.setCellRenderer(new DirectoryCellRenderer());
        directoryTree.setRootVisible(true);
        directoryTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        directoryTree.setEnabled(true);
        directoryTree.setEditable(false);
        directoryTree.setShowsRootHandles(true);
        directoryTree.addTreeSelectionListener(new TreeSelectionListener() {
                public void valueChanged(TreeSelectionEvent evt) {
                    final TreePath path     = evt.getPath();
                    final TreePath leadPath = evt.getNewLeadSelectionPath();

                    if ((path != null) && (leadPath != null)) {
                        Thread worker = new Thread(new RefreshThread(leadPath, false));

                        worker.start();
                    }
                }
            });
        directoryTree.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent evt) {
                    if (evt.getClickCount() == 2) {
                        DefaultMutableTreeNode node =
                                       (DefaultMutableTreeNode) directoryTree
                                       .getLastSelectedPathComponent();

                        if (!node.isRoot()) {
                            File dir = (File) node.getUserObject();

                            topPanel.setDirectory(dir.getAbsolutePath());
                        }
                    }
                }
            });

        JScrollPane directoryScroll = new JScrollPane(directoryTree);
        directoryScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        directoryScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        directoryScroll.getViewport().setScrollMode(JViewport.BLIT_SCROLL_MODE);

        fileListModel     = new ShortedListModel(new FileComparator());
        fileList          = new JList(fileListModel);
        fileList.setDragEnabled(true);
        fileList.setCellRenderer(new FileCellRenderer());
        fileList.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent evt) {
                    if (evt.getClickCount() == 2) {
                        final int index = fileList.locationToIndex(evt.getPoint());

                        Thread worker =
                                  new Thread(new Runnable() {
                                    public void run() {
                                        File file = (File) fileListModel.getElementAt(index);

                                        if (log.isDebugEnabled()) {
                                            log.debug(file);
                                        }

                                        IBuffer buffer = new BufferFactory().createBuffer();
                                        buffer.open(file);

                                        WorkspaceManager.addBuffer(buffer);
                                    }
                                });

                        worker.start();
                    }
                }
            });

        JScrollPane filesScroll = new JScrollPane(fileList);
        filesScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        filesScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        filesScroll.getViewport().setScrollMode(JViewport.BLIT_SCROLL_MODE);

        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, directoryScroll, filesScroll);
        splitPane.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
        splitPane.setDividerLocation(PreferenceManager.getInt("explorer.divider", 0));

        List dirs = openDirectory(rootDir);
        directoryTreeModel.insertAllInto(dirs, directoryRoot);

        List files = getFiles(rootDir);
        fileListModel.addAll(files);

        directoryTree.expandRow(0);

        internalPanel.add(splitPane, BorderLayout.CENTER);

        add(title, BorderLayout.NORTH);
        add(internalPanel, BorderLayout.CENTER);
    }

    //~ Methods ************************************************************************************

    /**
     * DOCUMENT ME!
     */
    public void init() {
        WorkspaceManager.addPlugin(WorkspaceManager.PLUGIN_WORKSPACE_POSITION, this);
    }

    /**
     * DOCUMENT ME!
     */
    public void refresh() {
        TreePath leadPath = directoryTree.getLeadSelectionPath();

        if (leadPath != null) {
            Thread worker = new Thread(new RefreshThread(leadPath, true));
            worker.start();
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void save() {
        PreferenceManager.putInt("explorer.location", location);
        PreferenceManager.putInt("explorer.divider", splitPane.getDividerLocation());
        PreferenceManager.putString("explorer.directory", topPanel.getDirectory());
    }

    /**
     * DOCUMENT ME!
     *
     * @param dir DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private List getFiles(File dir) {
        List result = new ArrayList();

        if (dir.exists() && dir.canRead()) {
            File[] files = dir.listFiles();

            for (int i = 0; i < files.length; i++) {
                if (!files[i].isHidden() && files[i].isFile()) {
                    result.add(files[i]);
                }
            }
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param dir DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private List openDirectory(File dir) {
        List result = new ArrayList();

        if (dir.exists() && dir.canRead()) {
            File[] files = dir.listFiles();

            for (int i = 0; i < files.length; i++) {
                if (!files[i].isHidden() && files[i].isDirectory()) {
                    result.add(new DefaultMutableTreeNode(files[i]));
                }
            }
        }

        return result;
    }

    //~ Inner Classes ******************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @author $Author: amunoz $
     * @version $Revision: 1.4 $
     */
    private class RefreshThread implements Runnable {
        /** DOCUMENT ME! */
        private TreePath leadPath = null;

        /** DOCUMENT ME! */
        private boolean refreshDir = true;

        /**
         * Creates a new RefreshThread object.
         *
         * @param leadPath DOCUMENT ME!
         * @param refreshDir DOCUMENT ME!
         */
        public RefreshThread(TreePath leadPath,
                             boolean  refreshDir) {
            this.leadPath       = leadPath;
            this.refreshDir     = refreshDir;
        }

        /**
         * DOCUMENT ME!
         */
        public void run() {
            final DefaultMutableTreeNode selected =
                (DefaultMutableTreeNode) leadPath.getLastPathComponent();

            final File dir   = (File) selected.getUserObject();
            final List files = getFiles(dir);
            final List dirs  = (refreshDir || selected.isLeaf()) ? openDirectory(dir) : null;

            SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        if (dir.exists()) {
                            if (refreshDir) {
                                selected.removeAllChildren();
                                directoryTreeModel.insertAllInto(dirs, selected);
                                directoryTreeModel.nodeStructureChanged(selected);
                            } else if (selected.isLeaf()) {
                                directoryTreeModel.insertAllInto(dirs, selected);
                            }

                            fileListModel.removeAllElements();
                            fileListModel.addAll(files);
                        }
                    }
                });
        }
    }
}
