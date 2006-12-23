/*
 * $Id: HighlightEditor.java,v 1.1.1.1 2004/09/18 17:16:19 amunoz Exp $
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

package tk.tomby.tedit.gui.editors;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import tk.tomby.tedit.core.preferences.IEditor;

import tk.tomby.tedit.editorkits.HighlightModel;

import tk.tomby.tedit.messages.PreferenceMessage;

import tk.tomby.tedit.services.MessageManager;
import tk.tomby.tedit.services.PreferenceManager;
import tk.tomby.tedit.services.ResourceManager;
import tk.tomby.tedit.services.WorkspaceManager;


/**
 * DOCUMENT ME!
 *
 * @author $Author: amunoz $
 * @version $Revision: 1.1.1.1 $
 */
public class HighlightEditor extends JPanel implements IEditor {
    //~ Instance fields ****************************************************************************

    /** DOCUMENT ME! */
    private HighlightModel model = null;

    /** DOCUMENT ME! */
    private JLabel label = null;

    /** DOCUMENT ME! */
    private JTextField field = null;

    /** DOCUMENT ME! */
    private String key = null;

    //~ Constructors *******************************************************************************

    /**
     * Creates a new HighlightEditor object.
     */
    public HighlightEditor() {
        super();

        setLayout(new GridLayout(0, 2, 5, 5));

        label = new JLabel();
        label.setHorizontalAlignment(JLabel.RIGHT);
        label.setHorizontalTextPosition(JLabel.RIGHT);
        field = new JTextField(15);
        field.setHorizontalAlignment(JTextField.LEFT);
        field.setEditable(false);
        field.setOpaque(true);
        field.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    new HighlightEditorDialog(model).setVisible(true);

                    field.setFont(model.getFont());
                    field.setForeground(model.getColor());

                    SwingUtilities.updateComponentTreeUI(field);
                }
            });

        add(label);
        add(field);
    }

    //~ Methods ************************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @param def DOCUMENT ME!
     */
    public void setDefault(String def) {
        ;
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     */
    public void setKey(String key) {
        this.key     = key;
        model        = new HighlightModel(key);

        field.setFont(model.getFont());
        field.setForeground(model.getColor());

        int red   = PreferenceManager.getInt("general.editor.background.red", 0);
        int green = PreferenceManager.getInt("general.editor.background.green", 0);
        int blue  = PreferenceManager.getInt("general.editor.background.blue", 0);
        field.setBackground(new Color(red, green, blue));
    }

    /**
     * DOCUMENT ME!
     *
     * @param label DOCUMENT ME!
     */
    public void setLabel(String label) {
        this.label.setText(ResourceManager.getProperty(label));
        this.field.setText(ResourceManager.getProperty(label));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isModified() {
        return model.isModified();
    }

    /**
     * DOCUMENT ME!
     */
    public void commit() {
        if (model.isModified()) {
            PreferenceManager.putInt(key + ".color.red", model.getRed());
            PreferenceManager.putInt(key + ".color.green", model.getGreen());
            PreferenceManager.putInt(key + ".color.blue", model.getBlue());
            PreferenceManager.putBoolean(key + ".font.bold", model.isBold());
            PreferenceManager.putBoolean(key + ".font.italic", model.isItalic());

            PreferenceMessage message = new PreferenceMessage(this, key, model);
            MessageManager.sendMessage(message);

            model.setModified(false);
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void restore() {
        model = new HighlightModel(key);

        field.setFont(model.getFont());
        field.setForeground(model.getColor());

        int red   = PreferenceManager.getInt("general.editor.background.red", 0);
        int green = PreferenceManager.getInt("general.editor.background.green", 0);
        int blue  = PreferenceManager.getInt("general.editor.background.blue", 0);
        field.setBackground(new Color(red, green, blue));
    }

    //~ Inner Classes ******************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @author $Author: amunoz $
     * @version $Revision: 1.1.1.1 $
     */
    private static class HighlightEditorDialog extends JDialog {
        /** DOCUMENT ME! */
        private HighlightModel model = null;

        /**
         * Creates a new HighlightEditorDialog object.
         *
         * @param m DOCUMENT ME!
         */
        private HighlightEditorDialog(HighlightModel m) {
            super(WorkspaceManager.getMainFrame(), true);

            this.model = m;

            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(0, 2, 5, 5));

            JLabel boldLabel = new JLabel(ResourceManager.getProperty("preferences.bold"));
            boldLabel.setHorizontalAlignment(JLabel.RIGHT);
            boldLabel.setHorizontalTextPosition(JLabel.RIGHT);
            panel.add(boldLabel);

            final JCheckBox boldBox = new JCheckBox();
            boldBox.setSelected(model.isBold());
            boldBox.setHorizontalAlignment(JCheckBox.LEFT);
            panel.add(boldBox);

            JLabel italicLabel = new JLabel(ResourceManager.getProperty("preferences.italic"));
            italicLabel.setHorizontalAlignment(JLabel.RIGHT);
            italicLabel.setHorizontalTextPosition(JLabel.RIGHT);
            panel.add(italicLabel);

            final JCheckBox italicBox = new JCheckBox();
            italicBox.setSelected(model.isItalic());
            italicBox.setHorizontalAlignment(JCheckBox.LEFT);
            panel.add(italicBox);

            JLabel colorLabel = new JLabel(ResourceManager.getProperty("preferences.color"));
            colorLabel.setHorizontalAlignment(JLabel.RIGHT);
            colorLabel.setHorizontalTextPosition(JLabel.RIGHT);
            panel.add(colorLabel);

            final JTextField colorField = new JTextField(15);
            colorField.setOpaque(true);
            colorField.setEditable(false);
            colorField.setBackground(model.getColor());
            colorField.setHorizontalAlignment(JTextField.LEFT);
            colorField.setEditable(false);
            colorField.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        Color color =
                                        JColorChooser.showDialog(WorkspaceManager.getMainFrame(),
                                                                 ResourceManager.getProperty("preferences.color"),
                                                                 colorField.getForeground());

                        if (color != null) {
                            colorField.setBackground(color);
                        }
                    }
                });
            panel.add(colorField);

            JPanel buttons = new JPanel();
            JButton accept = new JButton(ResourceManager.getProperty("preferences.accept"));
            accept.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        model.setBold(boldBox.isSelected());
                        model.setItalic(italicBox.isSelected());

                        Color color = colorField.getBackground();
                        model.setBlue(color.getBlue());
                        model.setGreen(color.getGreen());
                        model.setRed(color.getRed());

                        setVisible(false);
                        dispose();
                    }
                });
            buttons.add(accept);

            JButton cancel = new JButton(ResourceManager.getProperty("preferences.cancel"));
            cancel.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        setVisible(false);
                        dispose();
                    }
                });
            buttons.add(cancel);

            JPanel internalPanel = new JPanel();
            internalPanel.add(panel);

            getContentPane().add(internalPanel, BorderLayout.CENTER);
            getContentPane().add(buttons, BorderLayout.SOUTH);

            Toolkit toolkit      = Toolkit.getDefaultToolkit();
            Dimension screenSize = toolkit.getScreenSize();

            setSize(new Dimension(360, 160));
            setLocation((screenSize.width / 2) - 180, (screenSize.height / 2) - 80);
        }
    }
}
