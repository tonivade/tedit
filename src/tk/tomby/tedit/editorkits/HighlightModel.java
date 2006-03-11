/*
 * $Id: HighlightModel.java,v 1.1.1.1 2004/09/18 17:16:21 amunoz Exp $
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

package tk.tomby.tedit.editorkits;

import java.awt.Color;
import java.awt.Font;

import tk.tomby.tedit.services.PreferenceManager;


/**
 * DOCUMENT ME!
 *
 * @author $Author: amunoz $
 * @version $Revision: 1.1.1.1 $
 */
public class HighlightModel {
    //~ Instance fields ****************************************************************************

    /** DOCUMENT ME! */
    private String fontName = "Monospaced";

    /** DOCUMENT ME! */
    private boolean bold = false;

    /** DOCUMENT ME! */
    private boolean italic = false;

    /** DOCUMENT ME! */
    private boolean modified = false;

    /** DOCUMENT ME! */
    private int blue = 0;

    /** DOCUMENT ME! */
    private int fontSize = 12;

    /** DOCUMENT ME! */
    private int green = 0;

    /** DOCUMENT ME! */
    private int red = 0;

    //~ Constructors *******************************************************************************

    /**
     * Creates a new HighlightModel object.
     *
     * @param key DOCUMENT ME!
     */
    public HighlightModel(String key) {
        // Font name and size from general editor preferences
        fontName     = PreferenceManager.getString("general.editor.font", "Monospaced");
        fontSize     = PreferenceManager.getInt("general.editor.fontSize", 12);

        // otherwise from specific values
        red        = PreferenceManager.getInt(key + ".color.red", 0);
        green      = PreferenceManager.getInt(key + ".color.green", 0);
        blue       = PreferenceManager.getInt(key + ".color.blue", 0);
        bold       = PreferenceManager.getBoolean(key + ".font.bold", false);
        italic     = PreferenceManager.getBoolean(key + ".font.italic", false);
    }

    //~ Methods ************************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @param blue DOCUMENT ME!
     */
    public void setBlue(int blue) {
        this.blue         = blue;
        this.modified     = true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getBlue() {
        return blue;
    }

    /**
     * DOCUMENT ME!
     *
     * @param bold DOCUMENT ME!
     */
    public void setBold(boolean bold) {
        this.bold         = bold;
        this.modified     = true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isBold() {
        return bold;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Color getColor() {
        return new Color(red, green, blue);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Font getFont() {
        int boldAttr   = (bold) ? Font.BOLD : Font.PLAIN;
        int italicAttr = (italic) ? Font.ITALIC : Font.PLAIN;

        return new Font(fontName, boldAttr | italicAttr, fontSize);
    }

    /**
     * DOCUMENT ME!
     *
     * @param fontName DOCUMENT ME!
     */
    public void setFontName(String fontName) {
        this.fontName     = fontName;
        this.modified     = true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getFontName() {
        return fontName;
    }

    /**
     * DOCUMENT ME!
     *
     * @param fontSize The fontSize to set.
     */
    public void setFontSize(int fontSize) {
        this.fontSize     = fontSize;
        this.modified     = true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the fontSize.
     */
    public int getFontSize() {
        return fontSize;
    }

    /**
     * DOCUMENT ME!
     *
     * @param green DOCUMENT ME!
     */
    public void setGreen(int green) {
        this.green        = green;
        this.modified     = true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getGreen() {
        return green;
    }

    /**
     * DOCUMENT ME!
     *
     * @param italic DOCUMENT ME!
     */
    public void setItalic(boolean italic) {
        this.italic       = italic;
        this.modified     = true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isItalic() {
        return italic;
    }

    /**
     * DOCUMENT ME!
     *
     * @param modified DOCUMENT ME!
     */
    public void setModified(boolean modified) {
        this.modified = modified;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isModified() {
        return modified;
    }

    /**
     * DOCUMENT ME!
     *
     * @param red DOCUMENT ME!
     */
    public void setRed(int red) {
        this.red          = red;
        this.modified     = true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getRed() {
        return red;
    }
}
