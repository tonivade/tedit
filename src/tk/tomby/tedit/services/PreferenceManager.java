/*
 * $Id: PreferenceManager.java,v 1.1.1.1 2004/09/18 17:16:21 amunoz Exp $
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

import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * DOCUMENT ME!
 *
 * @author $Author: amunoz $
 * @version $Revision: 1.1.1.1 $
 */
public class PreferenceManager {
    //~ Static fields/initializers *****************************************************************

    /** DOCUMENT ME! */
    private static final String DEFAULT_PREFERENCE_ROOT = "tk/tomby/tedit/core";

    /** DOCUMENT ME! */
    private static final String MAIN_CATEGORY_NAME = "main";

    /** DOCUMENT ME! */
    private static Log log = LogFactory.getLog(PreferenceManager.class);

    /** DOCUMENT ME! */
    private static Map<String, Category> categories = new HashMap<String, Category>();

    static {
        loadCategory(MAIN_CATEGORY_NAME, DEFAULT_PREFERENCE_ROOT);
    }

    //~ Methods ************************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     * @param def DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static boolean getBoolean(String  key,
                                     boolean def) {
        return getCategory(key).getBoolean(key, def);
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     * @param def DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double getDouble(String key,
                                   double def) {
        return getCategory(key).getDouble(key, def);
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     * @param def DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static float getFloat(String key,
                                 float  def) {
        return getCategory(key).getFloat(key, def);
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     * @param def DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int getInt(String key,
                             int    def) {
        return getCategory(key).getInt(key, def);
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     * @param def DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static long getLong(String key,
                               long   def) {
        return getCategory(key).getLong(key, def);
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     * @param def DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String getString(String key,
                                   String def) {
        return getCategory(key).getString(key, def);
    }

    /**
     * DOCUMENT ME!
     *
     * @param categoryName DOCUMENT ME!
     * @param preferenceRoot DOCUMENT ME!
     */
    public static void loadCategory(String categoryName,
                                    String preferenceRoot) {
        categories.put(categoryName, new Category(preferenceRoot));

        if (log.isDebugEnabled()) {
            log.debug(categoryName + " loaded");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    public static void putBoolean(String  key,
                                  boolean value) {
        getCategory(key).putBoolean(key, value);
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    public static void putDouble(String key,
                                 double value) {
        getCategory(key).putDouble(key, value);
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    public static void putFloat(String key,
                                float  value) {
        getCategory(key).putFloat(key, value);
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    public static void putInt(String key,
                              int    value) {
        getCategory(key).putInt(key, value);
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    public static void putLong(String key,
                               long   value) {
        getCategory(key).putLong(key, value);
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    public static void putString(String key,
                                 String value) {
        getCategory(key).putString(key, value);
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static Category getCategory(String key) {
        String category = null;

        int index = key.indexOf('.');

        if (index > 0) {
            category = key.substring(0, index);
        }

        return categories.get(category);
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
        Preferences preferences = null;

        /**
         * Creates a new Category object.
         *
         * @param preferenceRoot DOCUMENT ME!
         */
        private Category(String preferenceRoot) {
            preferences = Preferences.userRoot().node(preferenceRoot);
        }

        /**
         * DOCUMENT ME!
         *
         * @param key DOCUMENT ME!
         * @param def DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        private boolean getBoolean(String  key,
                                   boolean def) {
            return preferences.getBoolean(key, def);
        }

        /**
         * DOCUMENT ME!
         *
         * @param key DOCUMENT ME!
         * @param def DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        private double getDouble(String key,
                                 double def) {
            return preferences.getDouble(key, def);
        }

        /**
         * DOCUMENT ME!
         *
         * @param key DOCUMENT ME!
         * @param def DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        private float getFloat(String key,
                               float  def) {
            return preferences.getFloat(key, def);
        }

        /**
         * DOCUMENT ME!
         *
         * @param key DOCUMENT ME!
         * @param def DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        private int getInt(String key,
                           int    def) {
            return preferences.getInt(key, def);
        }

        /**
         * DOCUMENT ME!
         *
         * @param key DOCUMENT ME!
         * @param def DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        private long getLong(String key,
                             long   def) {
            return preferences.getLong(key, def);
        }

        /**
         * DOCUMENT ME!
         *
         * @param key DOCUMENT ME!
         * @param def DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        private String getString(String key,
                                 String def) {
            return preferences.get(key, def);
        }

        /**
         * DOCUMENT ME!
         *
         * @param key DOCUMENT ME!
         * @param value DOCUMENT ME!
         */
        private void putBoolean(String  key,
                                boolean value) {
            preferences.putBoolean(key, value);
        }

        /**
         * DOCUMENT ME!
         *
         * @param key DOCUMENT ME!
         * @param value DOCUMENT ME!
         */
        private void putDouble(String key,
                               double value) {
            preferences.putDouble(key, value);
        }

        /**
         * DOCUMENT ME!
         *
         * @param key DOCUMENT ME!
         * @param value DOCUMENT ME!
         */
        private void putFloat(String key,
                              float  value) {
            preferences.putFloat(key, value);
        }

        /**
         * DOCUMENT ME!
         *
         * @param key DOCUMENT ME!
         * @param value DOCUMENT ME!
         */
        private void putInt(String key,
                            int    value) {
            preferences.putInt(key, value);
        }

        /**
         * DOCUMENT ME!
         *
         * @param key DOCUMENT ME!
         * @param value DOCUMENT ME!
         */
        private void putLong(String key,
                             long   value) {
            preferences.putLong(key, value);
        }

        /**
         * DOCUMENT ME!
         *
         * @param key DOCUMENT ME!
         * @param value DOCUMENT ME!
         */
        private void putString(String key,
                               String value) {
            preferences.put(key, value);
        }
    }
}
