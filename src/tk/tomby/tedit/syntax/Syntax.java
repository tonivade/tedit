/*
 * $Id: Syntax.java,v 1.2 2005/01/09 13:58:37 amunoz Exp $
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

package tk.tomby.tedit.syntax;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


/**
 * DOCUMENT ME!
 *
 * @author $Author: amunoz $
 * @version $Revision: 1.2 $
 */
public class Syntax {
    //~ Static fields/initializers *****************************************************************

    /** DOCUMENT ME! */
    public static final int END = 1;
    
    /** DOCUMENT ME! */
    public static final int SCRIPTLET = 5;
    
    /** DOCUMENT ME! */
    public static final String SCRIPTLET_NAME = "scriptlet";

    /** DOCUMENT ME! */
    public static final int KEYWORD = 10;

    /** DOCUMENT ME! */
    public static final int KEYWORD1 = KEYWORD + 1;

    /** DOCUMENT ME! */
    public static final String KEYWORD1_NAME = "keyword1";

    /** DOCUMENT ME! */
    public static final int KEYWORD2 = KEYWORD + 2;

    /** DOCUMENT ME! */
    public static final String KEYWORD2_NAME = "keyword2";

    /** DOCUMENT ME! */
    public static final int KEYWORD3 = KEYWORD + 3;

    /** DOCUMENT ME! */
    public static final String KEYWORD3_NAME = "keyword3";

    /** DOCUMENT ME! */
    public static final int KEYWORD4 = KEYWORD + 4;

    /** DOCUMENT ME! */
    public static final String KEYWORD4_NAME = "keyword4";

    /** DOCUMENT ME! */
    public static final int COMMENT = 20;

    /** DOCUMENT ME! */
    public static final int COMMENT1 = COMMENT + 1;

    /** DOCUMENT ME! */
    public static final String COMMENT1_NAME = "comment1";

    /** DOCUMENT ME! */
    public static final int COMMENT2 = COMMENT + 2;

    /** DOCUMENT ME! */
    public static final String COMMENT2_NAME = "comment2";

    /** DOCUMENT ME! */
    public static final int COMMENT3 = COMMENT + 3;

    /** DOCUMENT ME! */
    public static final String COMMENT3_NAME = "comment3";

    /** DOCUMENT ME! */
    public static final int COMMENT4 = COMMENT + 4;

    /** DOCUMENT ME! */
    public static final String COMMENT4_NAME = "comment4";

    /** DOCUMENT ME! */
    public static final int LITERAL = 30;

    /** DOCUMENT ME! */
    public static final int LITERAL1 = LITERAL + 1;

    /** DOCUMENT ME! */
    public static final String LITERAL1_NAME = "literal1";

    /** DOCUMENT ME! */
    public static final int LITERAL2 = LITERAL + 2;

    /** DOCUMENT ME! */
    public static final String LITERAL2_NAME = "literal2";

    /** DOCUMENT ME! */
    public static final int LITERAL3 = LITERAL + 3;

    /** DOCUMENT ME! */
    public static final String LITERAL3_NAME = "literal3";

    /** DOCUMENT ME! */
    public static final int LITERAL4 = LITERAL + 4;

    /** DOCUMENT ME! */
    public static final String LITERAL4_NAME = "literal4";

    /** DOCUMENT ME! */
    public static final int IDENTIFIER = 40;

    /** DOCUMENT ME! */
    public static final int IDENTIFIER1 = IDENTIFIER + 1;

    /** DOCUMENT ME! */
    public static final String IDENTIFIER1_NAME = "identifier1";

    /** DOCUMENT ME! */
    public static final int IDENTIFIER2 = IDENTIFIER + 2;

    /** DOCUMENT ME! */
    public static final String IDENTIFIER2_NAME = "identifier2";

    /** DOCUMENT ME! */
    public static final int IDENTIFIER3 = IDENTIFIER + 3;

    /** DOCUMENT ME! */
    public static final String IDENTIFIER3_NAME = "identifier3";

    /** DOCUMENT ME! */
    public static final int IDENTIFIER4 = IDENTIFIER + 4;

    /** DOCUMENT ME! */
    public static final String IDENTIFIER4_NAME = "identifier4";

    /** DOCUMENT ME! */
    public static final int OPERATOR = 50;

    /** DOCUMENT ME! */
    public static final String OPERATOR_NAME = "operator";

    /** DOCUMENT ME! */
    public static final int SEPARATOR = 60;

    /** DOCUMENT ME! */
    public static final String SEPARATOR_NAME = "separator";

    /** DOCUMENT ME! */
    public static final int LABEL = 70;

    /** DOCUMENT ME! */
    public static final String LABEL_NAME = "label";

    /** DOCUMENT ME! */
    public static final int MARKUP = 80;

    /** DOCUMENT ME! */
    public static final String MARKUP_NAME = "markup";

    /** DOCUMENT ME! */
    public static final int DIGIT = 90;

    /** DOCUMENT ME! */
    public static final String DIGIT_NAME = "digit";
    
    /** DOCUMENT ME! */
    public static final int UNKNOWN = 99;

    /** DOCUMENT ME! */
    public static final String UNKNOWN_NAME = "unknown";

    /** DOCUMENT ME! */
    public static final Map NAME_INDEX = new HashMap();

    /** DOCUMENT ME! */
    public static final Map VALUE_INDEX = new HashMap();

    static {
        NAME_INDEX.put(KEYWORD1_NAME, new Integer(KEYWORD1));
        NAME_INDEX.put(KEYWORD2_NAME, new Integer(KEYWORD2));
        NAME_INDEX.put(KEYWORD3_NAME, new Integer(KEYWORD3));
        NAME_INDEX.put(KEYWORD4_NAME, new Integer(KEYWORD4));

        NAME_INDEX.put(COMMENT1_NAME, new Integer(COMMENT1));
        NAME_INDEX.put(COMMENT2_NAME, new Integer(COMMENT2));
        NAME_INDEX.put(COMMENT3_NAME, new Integer(COMMENT3));
        NAME_INDEX.put(COMMENT4_NAME, new Integer(COMMENT4));

        NAME_INDEX.put(LITERAL1_NAME, new Integer(LITERAL1));
        NAME_INDEX.put(LITERAL2_NAME, new Integer(LITERAL2));
        NAME_INDEX.put(LITERAL3_NAME, new Integer(LITERAL3));
        NAME_INDEX.put(LITERAL4_NAME, new Integer(LITERAL4));

        NAME_INDEX.put(IDENTIFIER1_NAME, new Integer(IDENTIFIER1));
        NAME_INDEX.put(IDENTIFIER2_NAME, new Integer(IDENTIFIER2));
        NAME_INDEX.put(IDENTIFIER3_NAME, new Integer(IDENTIFIER3));
        NAME_INDEX.put(IDENTIFIER4_NAME, new Integer(IDENTIFIER4));

        NAME_INDEX.put(OPERATOR_NAME, new Integer(OPERATOR));
        NAME_INDEX.put(SEPARATOR_NAME, new Integer(SEPARATOR));
        NAME_INDEX.put(LABEL_NAME, new Integer(LABEL));
        NAME_INDEX.put(MARKUP_NAME, new Integer(MARKUP));
        NAME_INDEX.put(DIGIT_NAME, new Integer(DIGIT));
        NAME_INDEX.put(SCRIPTLET_NAME, new Integer(SCRIPTLET));

        VALUE_INDEX.put(new Integer(KEYWORD1), KEYWORD1_NAME);
        VALUE_INDEX.put(new Integer(KEYWORD2), KEYWORD2_NAME);
        VALUE_INDEX.put(new Integer(KEYWORD3), KEYWORD3_NAME);
        VALUE_INDEX.put(new Integer(KEYWORD4), KEYWORD4_NAME);

        VALUE_INDEX.put(new Integer(COMMENT1), COMMENT1_NAME);
        VALUE_INDEX.put(new Integer(COMMENT2), COMMENT2_NAME);
        VALUE_INDEX.put(new Integer(COMMENT3), COMMENT3_NAME);
        VALUE_INDEX.put(new Integer(COMMENT4), COMMENT4_NAME);

        VALUE_INDEX.put(new Integer(LITERAL1), LITERAL1_NAME);
        VALUE_INDEX.put(new Integer(LITERAL2), LITERAL2_NAME);
        VALUE_INDEX.put(new Integer(LITERAL3), LITERAL3_NAME);
        VALUE_INDEX.put(new Integer(LITERAL4), LITERAL4_NAME);

        VALUE_INDEX.put(new Integer(IDENTIFIER1), IDENTIFIER1_NAME);
        VALUE_INDEX.put(new Integer(IDENTIFIER2), IDENTIFIER2_NAME);
        VALUE_INDEX.put(new Integer(IDENTIFIER3), IDENTIFIER3_NAME);
        VALUE_INDEX.put(new Integer(IDENTIFIER4), IDENTIFIER4_NAME);

        VALUE_INDEX.put(new Integer(OPERATOR), OPERATOR_NAME);
        VALUE_INDEX.put(new Integer(SEPARATOR), SEPARATOR_NAME);
        VALUE_INDEX.put(new Integer(LABEL), LABEL_NAME);
        VALUE_INDEX.put(new Integer(MARKUP), MARKUP_NAME);
        VALUE_INDEX.put(new Integer(DIGIT), DIGIT_NAME);
        VALUE_INDEX.put(new Integer(SCRIPTLET), SCRIPTLET_NAME);
    }

    //~ Instance fields ****************************************************************************

    /** DOCUMENT ME! */
    private Map states = new HashMap();

    /** DOCUMENT ME! */
    private String contentType = null;

    /** DOCUMENT ME! */
    private String name = null;

    //~ Constructors *******************************************************************************

    /**
     * Creates a new Syntax object.
     */
    public Syntax() {
        super();
    }

    //~ Methods ************************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String getTokenName(int value) {
        String name = (String) VALUE_INDEX.get(new Integer(value));

        return (name != null) ? name : UNKNOWN_NAME;
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int getTokenValue(String name) {
        Integer value = (Integer) NAME_INDEX.get(name);

        return (value != null) ? value.intValue() : UNKNOWN;
    }

    /**
     * DOCUMENT ME!
     *
     * @param contentType DOCUMENT ME!
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the mainState.
     */
    public State getMainState() {
        return (State) states.get(State.MAIN);
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return name;
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public State getState(String name) {
        return (State) states.get(name);
    }

    /**
     * DOCUMENT ME!
     *
     * @param state DOCUMENT ME!
     */
    public void addState(State state) {
        states.put(state.getName(), state);
    }

    //~ Inner Classes ******************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @author $Author: amunoz $
     * @version $Revision: 1.2 $
     */
    public static class State {
        /** DOCUMENT ME! */
        private static final String MAIN = "main";

        /** DOCUMENT ME! */
        private Map groups = new TreeMap();

        /** DOCUMENT ME! */
        private String name = MAIN;

        /** DOCUMENT ME! */
        private int token = Syntax.UNKNOWN;

        /**
         * Creates a new State object.
         */
        public State() {
            super();
        }

        /**
         * DOCUMENT ME!
         *
         * @param defaultToken The defaultToken to set.
         */
        public void setDefaultToken(String defaultToken) {
            this.token = getTokenValue(defaultToken);
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public Map getGroups() {
            return groups;
        }

        /**
         * DOCUMENT ME!
         *
         * @param name The name to set.
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * DOCUMENT ME!
         *
         * @return Returns the name.
         */
        public String getName() {
            return name;
        }

        /**
         * DOCUMENT ME!
         *
         * @return Returns the defaultToken.
         */
        public int getToken() {
            return token;
        }

        /**
         * DOCUMENT ME!
         *
         * @param comment DOCUMENT ME!
         */
        public void addComment1(IRule comment) {
            getGroup(COMMENT1).add(comment);
        }

        /**
         * DOCUMENT ME!
         *
         * @param comment DOCUMENT ME!
         */
        public void addComment2(IRule comment) {
            getGroup(COMMENT2).add(comment);
        }

        /**
         * DOCUMENT ME!
         *
         * @param comment DOCUMENT ME!
         */
        public void addComment3(IRule comment) {
            getGroup(COMMENT3).add(comment);
        }

        /**
         * DOCUMENT ME!
         *
         * @param comment DOCUMENT ME!
         */
        public void addComment4(IRule comment) {
            getGroup(COMMENT4).add(comment);
        }

        /**
         * DOCUMENT ME!
         *
         * @param digit DOCUMENT ME!
         */
        public void addDigit(IRule digit) {
            getGroup(DIGIT).add(digit);
        }

        /**
         * DOCUMENT ME!
         *
         * @param identifier DOCUMENT ME!
         */
        public void addIdentifier1(IRule identifier) {
            getGroup(IDENTIFIER1).add(identifier);
        }

        /**
         * DOCUMENT ME!
         *
         * @param identifier DOCUMENT ME!
         */
        public void addIdentifier2(IRule identifier) {
            getGroup(IDENTIFIER2).add(identifier);
        }

        /**
         * DOCUMENT ME!
         *
         * @param identifier DOCUMENT ME!
         */
        public void addIdentifier3(IRule identifier) {
            getGroup(IDENTIFIER3).add(identifier);
        }

        /**
         * DOCUMENT ME!
         *
         * @param identifier DOCUMENT ME!
         */
        public void addIdentifier4(IRule identifier) {
            getGroup(IDENTIFIER4).add(identifier);
        }

        /**
         * DOCUMENT ME!
         *
         * @param keyword DOCUMENT ME!
         */
        public void addKeyword1(IRule keyword) {
            getGroup(KEYWORD1).add(keyword);
        }

        /**
         * DOCUMENT ME!
         *
         * @param keyword DOCUMENT ME!
         */
        public void addKeyword2(IRule keyword) {
            getGroup(KEYWORD2).add(keyword);
        }

        /**
         * DOCUMENT ME!
         *
         * @param keyword DOCUMENT ME!
         */
        public void addKeyword3(IRule keyword) {
            getGroup(KEYWORD3).add(keyword);
        }

        /**
         * DOCUMENT ME!
         *
         * @param keyword DOCUMENT ME!
         */
        public void addKeyword4(IRule keyword) {
            getGroup(KEYWORD4).add(keyword);
        }

        /**
         * DOCUMENT ME!
         *
         * @param label DOCUMENT ME!
         */
        public void addLabel(IRule label) {
            getGroup(LABEL).add(label);
        }

        /**
         * DOCUMENT ME!
         *
         * @param literal DOCUMENT ME!
         */
        public void addLiteral1(IRule literal) {
            getGroup(LITERAL1).add(literal);
        }

        /**
         * DOCUMENT ME!
         *
         * @param literal DOCUMENT ME!
         */
        public void addLiteral2(IRule literal) {
            getGroup(LITERAL2).add(literal);
        }

        /**
         * DOCUMENT ME!
         *
         * @param literal DOCUMENT ME!
         */
        public void addLiteral3(IRule literal) {
            getGroup(LITERAL3).add(literal);
        }

        /**
         * DOCUMENT ME!
         *
         * @param literal DOCUMENT ME!
         */
        public void addLiteral4(IRule literal) {
            getGroup(LITERAL4).add(literal);
        }

        /**
         * DOCUMENT ME!
         *
         * @param markup DOCUMENT ME!
         */
        public void addMarkup(IRule markup) {
            getGroup(MARKUP).add(markup);
        }

        /**
         * DOCUMENT ME!
         *
         * @param operator DOCUMENT ME!
         */
        public void addOperator(IRule operator) {
            getGroup(OPERATOR).add(operator);
        }

        /**
         * DOCUMENT ME!
         *
         * @param separator DOCUMENT ME!
         */
        public void addSeparator(IRule separator) {
            getGroup(SEPARATOR).add(separator);
        }
        
        /**
         * DOCUMENT ME!
         *
         * @param comment DOCUMENT ME!
         */
        public void addScriptlet(IRule scriptlet) {
            getGroup(SCRIPTLET).add(scriptlet);
        }

        /**
         * DOCUMENT ME!
         *
         * @param type DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        private List getGroup(int type) {
            List group = (List) groups.get(new Integer(type));

            if (group == null) {
                group = new ArrayList();

                groups.put(new Integer(type), group);
            }

            return group;
        }
    }
}
