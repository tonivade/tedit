/*
 * $Id: AbstractDockablePlugin.java,v 1.1.1.1 2004/09/18 17:16:22 amunoz Exp $
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

package tk.tomby.tedit.plugins;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

import org.flexdock.docking.DockingConstants;

import tk.tomby.tedit.core.IWorkspace;
import tk.tomby.tedit.services.PreferenceManager;


/**
 * DOCUMENT ME!
 *
 * @author $Author: amunoz $
 * @version $Revision: 1.1.1.1 $
 */
public abstract class AbstractDockablePlugin extends JPanel implements IDockablePlugin {
    //~ Instance fields ****************************************************************************
	
	/** DOCUMENT ME! */
	private String name = null;

    /** DOCUMENT ME! */
    private JLabel title = null;

    /** DOCUMENT ME! */
    private int location = IWorkspace.PLUGIN_LEFT;
    
    /** DOCUMENT ME! */
    private String region = DockingConstants.CENTER_REGION;

    //~ Constructors *******************************************************************************

    /**
     *
     */
    public AbstractDockablePlugin(String name) {
        super();
        
        setLayout(new BorderLayout());
        setBorder(new ShadowBorder());
        
        this.name = name;

        title = new JLabel(name);
        title.setBorder(createTitleBorder());
        add(title, BorderLayout.NORTH);
        
        location = PreferenceManager.getInt(name.toLowerCase() + ".location", IWorkspace.PLUGIN_LEFT);
		region = PreferenceManager.getString(name.toLowerCase() + ".region", DockingConstants.CENTER_REGION);
    }

	private CompoundBorder createTitleBorder() {
		Border b1 = BorderFactory.createMatteBorder(0, 0, 1, 0, UIManager.getColor("Button.borderColor"));
		Border b2 = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		return BorderFactory.createCompoundBorder(b1, b2);
	}

    //~ Methods ************************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @param location DOCUMENT ME!
     */
    public void setDockLocation(int location) {
        this.location = location;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDockLocation() {
        return location;
    }
    
    /**
     * DOCUMENT ME!
     *
     * @param region DOCUMENT ME!
     */
    public void setDockRegion(String region) {
    	this.region = region;
    }
    
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getDockRegion() {
    	return region;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Component getDockable() {
        return this;
    }
    
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getDockTitle() {
    	return name;
    }
    
    /**
     * DOCUMENT ME!
     * 
     * @param content DOCUMENT ME!
     */
    protected void setContent(Component content) {
    	add(content, BorderLayout.CENTER);
    }
    
    public void init() {
    	
    }
    
    public void save() {
    	PreferenceManager.putInt(name.toLowerCase() + ".location", location);
		PreferenceManager.putString(name.toLowerCase() + ".region", region);
    }
    
    private static class ShadowBorder extends AbstractBorder {

        private static final Insets INSETS = new Insets(1, 1, 3, 3);

        public Insets getBorderInsets(Component c) {
        	return INSETS;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
                
            Color shadow = UIManager.getColor("controlShadow");
            
            if (shadow == null) {
                shadow = Color.GRAY;
            }
            
            Color lightShadow   = new Color(shadow.getRed(), 
                                            shadow.getGreen(), 
                                            shadow.getBlue(), 
                                            170);
            
            Color lighterShadow = new Color(shadow.getRed(),
                                            shadow.getGreen(),
                                            shadow.getBlue(),
                                            70);
            g.translate(x, y);
            
            g.setColor(shadow);
            g.fillRect(0, 0, w - 3, 1);
            g.fillRect(0, 0, 1, h - 3);
            g.fillRect(w - 3, 1, 1, h - 3);
            g.fillRect(1, h - 3, w - 3, 1);
            // Shadow line 1
            g.setColor(lightShadow);
            g.fillRect(w - 3, 0, 1, 1);
            g.fillRect(0, h - 3, 1, 1);
            g.fillRect(w - 2, 1, 1, h - 3);
            g.fillRect(1, h - 2, w - 3, 1);
            // Shadow line2
            g.setColor(lighterShadow);
            g.fillRect(w - 2, 0, 1, 1);
            g.fillRect(0, h - 2, 1, 1);
            g.fillRect(w-2, h-2, 1, 1);
            g.fillRect(w - 1, 1, 1, h - 2);
            g.fillRect(1, h - 1, w - 2, 1);
            g.translate(-x, -y);
        }
    }
    
}
