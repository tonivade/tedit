/*
 * $Id: ResourceManager.java,v 1.1.1.1 2004/09/18 17:16:21 amunoz Exp $
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

import java.net.URL;

import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1.1.1 $
 */
public class ResourceManager {
    //~ Static fields/initializers *****************************************************************

    /** DOCUMENT ME! */
    private static final String DEFAULT_RESOURCE_ROOT = "tk/tomby/tedit/core";

    /** DOCUMENT ME! */
    private static final String MAIN_CATEGORY_NAME = "main";

    /** DOCUMENT ME! */
    private static Log log = LogFactory.getLog(ResourceManager.class);

    /** DOCUMENT ME! */
    private static Map categories = new HashMap();

    static {
        loadCategory(MAIN_CATEGORY_NAME, DEFAULT_RESOURCE_ROOT);
    }

    //~ Methods ************************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static boolean getBooleanProperty(String key) {
        return getBooleanProperty(getCategory(key), key);
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Icon getIcon(String key) {
        return getIcon(getCategory(key), key);
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int getIntProperty(String key) {
        return getIntProperty(getCategory(key), key);
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String getProperty(String key) {
        return getProperty(getCategory(key), key);
    }

    /**
     * DOCUMENT ME!
     *
     * @param categoryName DOCUMENT ME!
     * @param resourceRoot DOCUMENT ME!
     */
    public static void loadCategory(String categoryName,
                                    String resourceRoot) {
        categories.put(categoryName, new Category(resourceRoot));
    }

    /**
     * DOCUMENT ME!
     *
     * @param categoryName DOCUMENT ME!
     * @param key DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static boolean getBooleanProperty(String categoryName,
                                              String key) {
        boolean value = false;

        value = Boolean.valueOf(getProperty(categoryName, key)).booleanValue();

        return value;
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static String getCategory(String key) {
        String category = null;

        int index = key.indexOf('.');

        if (index > 0) {
            category = key.substring(0, index);
        }

        return category;
    }

    /**
     * DOCUMENT ME!
     *
     * @param categoryName DOCUMENT ME!
     * @param iconKey DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static Icon getIcon(String categoryName,
                                String iconKey) {
        Icon icon = null;

        Category category = (Category) categories.get(categoryName);

        if (category != null) {
            icon = category.getIcon(iconKey);
        }

        return icon;
    }

    /**
     * DOCUMENT ME!
     *
     * @param categoryName DOCUMENT ME!
     * @param key DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static int getIntProperty(String categoryName,
                                      String key) {
        int value = 0;

        try {
            String temp = getProperty(categoryName, key);

            value = Integer.parseInt(temp);
        } catch (NumberFormatException e) {
            ;
        }

        return value;
    }

    /**
     * DOCUMENT ME!
     *
     * @param categoryName DOCUMENT ME!
     * @param key DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static String getProperty(String categoryName,
                                      String key) {
        String property = null;

        Category category = (Category) categories.get(categoryName);

        if (category != null) {
            property = category.getProperty(key);
        }

        return property;
    }

    //~ Inner Classes ******************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @author $Author: amunoz $
     * @version $Revision: 1.1.1.1 $
     */
    private static class Category {
        /** DOCUMENT ME! */
        private ResourceBundle resources = null;

        /** DOCUMENT ME! */
        private String resourceRoot = null;

        /**
         * Creates a new Category object.
         *
         * @param resourceRoot DOCUMENT ME!
         */
        private Category(String resourceRoot) {
            this.resourceRoot     = resourceRoot;
            this.resources        = ResourceBundle.getBundle(this.resourceRoot + "/resources");
        }

        /**
         * DOCUMENT ME!
         *
         * @param iconName DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        private Icon getIcon(String iconName) {
            Icon icon = null;

            ClassLoader loader = ResourceManager.class.getClassLoader();

            String property = getProperty(iconName);

            if (log.isDebugEnabled()) {
                log.debug(property);
            }

            if (property != null) {
                URL url = loader.getResource(property);

                log.debug(url);

                icon = new ImageIcon(url);
            }

            return icon;
        }

        /**
         * DOCUMENT ME!
         *
         * @param key DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        private String getProperty(String key) {
            String property = null;

            try {
                property = resources.getString(key);
            } catch (MissingResourceException e) {
                log.warn(e);
            }

            return property;
        }
    }
}
