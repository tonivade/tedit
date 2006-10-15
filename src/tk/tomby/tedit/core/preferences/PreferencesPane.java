/*
 * $Id: Preferences.java,v 1.1.1.1 2004/09/18 17:16:20 amunoz Exp $
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

package tk.tomby.tedit.core.preferences;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Map;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import tk.tomby.tedit.services.ResourceManager;
import tk.tomby.tedit.services.WorkspaceManager;


/**
 * DOCUMENT ME!
 *
 * @author $Author: amunoz $
 * @version $Revision: 1.1.1.1 $
 */
public class PreferencesPane extends JPanel implements IPreferences {
    //~ Static fields/initializers *****************************************************************

    /** DOCUMENT ME! */
    private static final String RESOURCE_ROOT = "tk/tomby/tedit/core/preferences";

    /** DOCUMENT ME! */
    private static final String CATEGORY_NAME = "preferences";

    //~ Instance fields ****************************************************************************

    /** DOCUMENT ME! */
    private Box buttons = null;

    /** DOCUMENT ME! */
    private DefaultTreeModel preferencesTreeModel = null;

    /** DOCUMENT ME! */
    private JPanel preferencesPane = null;

    /** DOCUMENT ME! */
    private JTree preferencesTree = null;

    /** DOCUMENT ME! */
    private Map preferences = null;

    //~ Constructors *******************************************************************************

    /**
     * Creates a new Preferences object.
     */
    public PreferencesPane() {
        super();

        ResourceManager.loadCategory(CATEGORY_NAME, RESOURCE_ROOT);

        preferences = new TreeMap();

        setLayout(new BorderLayout());

        buttons = new Box(BoxLayout.X_AXIS);
        buttons.setVisible(false);

        JButton apply = new JButton(ResourceManager.getProperty("preferences.apply"));
        apply.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Thread worker =
                                new Thread(new Runnable() {
                                public void run() {
                                    commit();
                                }
                            });

                    worker.start();
                }
            });

        JButton restore = new JButton(ResourceManager.getProperty("preferences.restore"));
        restore.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Thread worker =
                        new Thread(new Runnable() {
                        public void run() {
                            restore();
                        }
                    });

            worker.start();
                }
            });
        buttons.add(Box.createHorizontalGlue());
        buttons.add(apply);
        buttons.add(Box.createRigidArea(new Dimension(10, 0)));
        buttons.add(restore);

        preferencesPane = new JPanel();
        preferencesPane.setLayout(new BorderLayout());
        preferencesPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JPanel internalPreferencesPane = new JPanel();
        internalPreferencesPane.setLayout(new BorderLayout());
        internalPreferencesPane.setBorder(createBorderPanel());
        internalPreferencesPane.add(preferencesPane, BorderLayout.CENTER);
        internalPreferencesPane.add(buttons, BorderLayout.SOUTH);

        DefaultMutableTreeNode preferencesRoot = new DefaultMutableTreeNode("root");

        preferencesTreeModel     = new DefaultTreeModel(preferencesRoot);
        preferencesTree          = new JTree(preferencesTreeModel);
        preferencesTree.setRootVisible(false);
        preferencesTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        preferencesTree.setEnabled(true);
        preferencesTree.setEditable(false);
        preferencesTree.setShowsRootHandles(true);
        preferencesTree.addTreeSelectionListener(new TreeSelectionListener() {
                public void valueChanged(TreeSelectionEvent evt) {
                    TreePath path     = evt.getPath();
                    TreePath leadPath = evt.getNewLeadSelectionPath();

                    if ((path != null) && (leadPath != null)) {
                        DefaultMutableTreeNode selected =
                                                 (DefaultMutableTreeNode) leadPath
                                                 .getLastPathComponent();

                        IPreferencePage preferencePage =
                                                 (IPreferencePage) preferences.get(selected
                                                                                   .getUserObject());

                        preferencesPane.removeAll();
                        preferencesPane.add((JComponent) preferencePage, BorderLayout.CENTER);
                        buttons.setVisible(true);
                        SwingUtilities.updateComponentTreeUI(preferencesPane);
                    }
                }
            });
        preferencesTree.setSelectionRow(1);

        JPanel preferencesTreePane = new JPanel();
        preferencesTreePane.setLayout(new BorderLayout());
        preferencesTreePane.add(preferencesTree, BorderLayout.CENTER);
        preferencesTreePane.setBorder(createBorderTree());

        add(preferencesTreePane, BorderLayout.WEST);
        add(internalPreferencesPane, BorderLayout.CENTER);
    }

    //~ Methods ************************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @param preferencePage DOCUMENT ME!
     */
    public void addPage(IPreferencePage preferencePage) {
        preferences.put(preferencePage.getLabel(), preferencePage);

        DefaultMutableTreeNode root = (DefaultMutableTreeNode) preferencesTreeModel.getRoot();
        DefaultMutableTreeNode page = new DefaultMutableTreeNode(preferencePage.getLabel());

        preferencesTreeModel.insertNodeInto(page, root, root.getChildCount());

        preferencesTree.scrollPathToVisible(new TreePath(page.getPath()));
    }

    /**
     * DOCUMENT ME!
     */
    public void commit() {
        if (preferencesPane.getComponentCount() > 0) {
            IPreferencePage preferencePage = (IPreferencePage) preferencesPane.getComponent(0);

            preferencePage.commitAll();
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void restore() {
        if (preferencesPane.getComponentCount() > 0) {
            IPreferencePage preferencePage = (IPreferencePage) preferencesPane.getComponent(0);

            preferencePage.restoreAll();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private Border createBorderPanel() {
        Border emptyBorder1   = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        Border emptyBorder2   = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        Border etchedBorder   = BorderFactory.createEtchedBorder();
        Border compoundBorder = BorderFactory.createCompoundBorder(emptyBorder1, etchedBorder);

        return BorderFactory.createCompoundBorder(compoundBorder, emptyBorder2);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private Border createBorderTree() {
        Border emptyBorder  = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        Border etchedBorder = BorderFactory.createEtchedBorder();

        return BorderFactory.createCompoundBorder(emptyBorder, etchedBorder);
    }
    
    /**
     * DOCUMENT ME!
     */
    public static void showDialog() {
    	final JDialog dialog =
            new JDialog(WorkspaceManager.getMainFrame(),
                        ResourceManager.getProperty("preferences.title"), true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setResizable(true);

        final IPreferences preferences = WorkspaceManager.getPreferences();

        dialog.getContentPane().add((JComponent) preferences, BorderLayout.CENTER);

        JPanel buttons = new JPanel();
        JButton accept = new JButton(ResourceManager.getProperty("preferences.accept"));
        accept.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    preferences.commit();
                    dialog.setVisible(false);
                    dialog.dispose();
                }
            });

        JButton cancel = new JButton(ResourceManager.getProperty("preferences.cancel"));
        cancel.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    preferences.restore();
                    dialog.setVisible(false);
                    dialog.dispose();
                }
            });
        buttons.add(accept);
        buttons.add(cancel);

        dialog.getContentPane().add(buttons, BorderLayout.SOUTH);

        Toolkit toolkit      = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();

        dialog.setSize(new Dimension(640, 480));
        dialog.setLocation((screenSize.width / 2) - 320, (screenSize.height / 2) - 240);
        
        SwingUtilities.invokeLater(new Runnable() {
        	public void run() {
        		dialog.setVisible(true);
        	}
        });
    }
}
