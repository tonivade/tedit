/*
 * $Id: FindReplacePane.java,v 1.1.1.1 2004/09/18 17:16:20 amunoz Exp $
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

package tk.tomby.tedit.core.snr;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import tk.tomby.tedit.core.IBuffer;

import tk.tomby.tedit.gui.editors.BooleanEditor;
import tk.tomby.tedit.gui.editors.MultiEditor;
import tk.tomby.tedit.gui.editors.StringEditor;

import tk.tomby.tedit.services.PreferenceManager;
import tk.tomby.tedit.services.ResourceManager;
import tk.tomby.tedit.services.WorkspaceManager;


/**
 * DOCUMENT ME!
 *
 * @author $Author: amunoz $
 * @version $Revision: 1.1.1.1 $
 */
public class FindReplacePane extends JPanel {
    //~ Static fields/initializers *****************************************************************

    /** DOCUMENT ME! */
    public static final int FIND = 0;

    /** DOCUMENT ME! */
    public static final int REPLACE = 1;

    //~ Instance fields ****************************************************************************

    /** DOCUMENT ME! */
    private BooleanEditor caseEditor = null;

    /** DOCUMENT ME! */
    private BooleanEditor completeEditor = null;

    /** DOCUMENT ME! */
    private BooleanEditor warpEditor = null;

    /** DOCUMENT ME! */
    private MultiEditor directionEditor = null;

    /** DOCUMENT ME! */
    private MultiEditor scopeEditor = null;

    /** DOCUMENT ME! */
    private StringEditor replaceEditor = null;

    /** DOCUMENT ME! */
    private StringEditor wordEditor = null;

    /** DOCUMENT ME! */
    private int mode = FIND;

    //~ Constructors *******************************************************************************

    /**
     * Creates a new FindReplacePane object.
     */
    public FindReplacePane() {
        this(FIND);
    }

    /**
     * Creates a new SearchPane object.
     *
     * @param mode DOCUMENT ME!
     */
    public FindReplacePane(int mode) {
        super();

        this.mode = mode;

        JPanel internalPanel = new JPanel();
        internalPanel.setLayout(new GridLayout(0, 1, 5, 5));

        wordEditor = new StringEditor();
        wordEditor.setLabel("snr.find");
        wordEditor.setKey("snr.find");

        if (mode == REPLACE) {
            replaceEditor = new StringEditor();
            replaceEditor.setLabel("snr.replace");
            replaceEditor.setKey("snr.replace");
        }

        directionEditor = new MultiEditor();
        directionEditor.setLabel("snr.direction");
        directionEditor.setValues("snr.direction.forward snr.direction.backward");
        directionEditor.setKey("snr.direction");

        scopeEditor = new MultiEditor();
        scopeEditor.setLabel("snr.scope");
        scopeEditor.setValues("snr.scope.all snr.scope.selection");
        scopeEditor.setKey("snr.scope");

        caseEditor = new BooleanEditor();
        caseEditor.setLabel("snr.case");
        caseEditor.setKey("snr.case");

        completeEditor = new BooleanEditor();
        completeEditor.setLabel("snr.whole");
        completeEditor.setKey("snr.whole");

        warpEditor = new BooleanEditor();
        warpEditor.setLabel("snr.warp");
        warpEditor.setKey("snr.warp");

        internalPanel.add(wordEditor);

        if (mode == REPLACE) {
            internalPanel.add(replaceEditor);
        }

        internalPanel.add(directionEditor);
        internalPanel.add(scopeEditor);
        internalPanel.add(caseEditor);
        internalPanel.add(completeEditor);
        internalPanel.add(warpEditor);

        add(internalPanel);
    }

    //~ Methods ************************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getMode() {
        return mode;
    }

    /**
     * DOCUMENT ME!
     */
    public void commitAll() {
        wordEditor.commit();

        if (mode == REPLACE) {
            replaceEditor.commit();
        }

        directionEditor.commit();
        scopeEditor.commit();
        caseEditor.commit();
        completeEditor.commit();
        warpEditor.commit();
    }

    /**
     * DOCUMENT ME!
     *
     * @param mode DOCUMENT ME!
     */
    public static void showDialog(int mode) {
        final JDialog dialog =
            new JDialog(WorkspaceManager.getMainFrame(),
                        (mode == FIND) ? ResourceManager.getProperty("snr.find")
                                       : ResourceManager.getProperty("snr.replace"), false);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setResizable(true);

        final FindReplacePane pane = new FindReplacePane(mode);

        dialog.getContentPane().add(pane, BorderLayout.CENTER);

        JPanel buttons = new JPanel();
        JButton find   = new JButton(ResourceManager.getProperty("snr.find"));
        find.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    pane.commitAll();

                    IBuffer buffer = WorkspaceManager.getCurrentBuffer();

                    if (buffer != null) {
                        if (PreferenceManager.getInt("snr.direction", FindReplaceWorker.FORWARD) == FindReplaceWorker.FORWARD) {
                            buffer.findNext();
                        } else {
                            buffer.findPrevious();
                        }
                    }
                }
            });
        buttons.add(find);

        if (mode == REPLACE) {
            JButton replace = new JButton(ResourceManager.getProperty("snr.replace"));
            replace.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        pane.commitAll();

                        IBuffer buffer = WorkspaceManager.getCurrentBuffer();

                        if (buffer != null) {
                            buffer.replace();
                        }
                    }
                });
            buttons.add(replace);

            JButton replaceAll = new JButton(ResourceManager.getProperty("snr.replaceAll"));
            replaceAll.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        pane.commitAll();
                    }
                });
            buttons.add(replaceAll);
        }

        JButton cancel = new JButton(ResourceManager.getProperty("snr.cancel"));
        cancel.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dialog.setVisible(false);
                    dialog.dispose();
                }
            });
        buttons.add(cancel);

        dialog.getContentPane().add(buttons, BorderLayout.SOUTH);

        Toolkit toolkit      = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();

        if (mode == FIND) {
            dialog.setSize(new Dimension(360, 250));
            dialog.setLocation((screenSize.width / 2) - 180, (screenSize.height / 2) - 125);
        } else {
            dialog.setSize(new Dimension(440, 250));
            dialog.setLocation((screenSize.width / 2) - 220, (screenSize.height / 2) - 125);
        }

        SwingUtilities.invokeLater(new Runnable() {
        	public void run() {
        		dialog.setVisible(true);
        	}
        });
        
    }
}
