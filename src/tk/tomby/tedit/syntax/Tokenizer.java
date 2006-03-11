/*
 * $Id: Tokenizer.java,v 1.2 2005/01/09 13:59:27 amunoz Exp $
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tk.tomby.tedit.services.SyntaxManager;


/**
 * DOCUMENT ME!
 *
 * @author $Author: amunoz $
 * @version $Revision: 1.2 $
 */
public class Tokenizer {
    //~ Static fields/initializers *****************************************************************

    /** DOCUMENT ME! */
    public static final String UNINITIALIZED = "uninitialized";

    /** DOCUMENT ME! */
    private static Log log = LogFactory.getLog(Tokenizer.class);

    //~ Instance fields ****************************************************************************

    /** DOCUMENT ME! */
    private Matcher matcher = null;

    /** DOCUMENT ME! */
    private Stack state = null;

    /** DOCUMENT ME! */
    private String text = null;

    /** DOCUMENT ME! */
    private Syntax syntax = null;

    //~ Constructors *******************************************************************************

    /**
     * Creates a new Tokenizer object.
     *
     * @param syntax DOCUMENT ME!
     */
    public Tokenizer(Syntax syntax) {
        this.syntax = syntax;

        initState();
    }

    //~ Methods ************************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public RuntimeState getCurrentState() {
        return (RuntimeState) state.peek();
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the state.
     */
    public Stack getState() {
        return (Stack) state.clone();
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the syntax.
     */
    public Syntax getSyntax() {
        return syntax;
    }

    /**
     * DOCUMENT ME!
     *
     * @param text DOCUMENT ME!
     *
     * @throws IllegalStateException DOCUMENT ME!
     */
    public void init(String text) {
        if (!getCurrentState().getName().equals(UNINITIALIZED)) {
            throw new IllegalStateException("Tokenizer initialized");
        }

        this.text = text;
        this.state.push(new RuntimeState(syntax.getMainState().getName()));

        initMatcher();
    }

    /**
     * DOCUMENT ME!
     *
     * @param offset DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalStateException DOCUMENT ME!
     */
    public Token nextToken(int offset) {
        if (getCurrentState().getName().equals(UNINITIALIZED)) {
            throw new IllegalStateException("Tokenizer uninitialized");
        }

        Token token = null;

        if (matcher.find(offset)) {
            token = getToken(matcher);

            if (token.getType() == Syntax.END) {
                state.pop();

                initMatcher();
            }
        }

        return token;
    }

    /**
     * DOCUMENT ME!
     *
     * @param text DOCUMENT ME!
     */
    public void reinit(String text) {
        initState();

        this.text = text;
        this.state.push(new RuntimeState(syntax.getMainState().getName()));

        initMatcher();
    }

    /**
     * DOCUMENT ME!
     *
     * @param text DOCUMENT ME!
     * @param state DOCUMENT ME!
     */
    public void reinit(String text,
                       Stack  state) {
        this.text      = text;
        this.state     = (Stack) state.clone();

        initMatcher();
    }

    /**
     * DOCUMENT ME!
     *
     * @param matcher DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private Token getToken(Matcher matcher) {
        Iterator groups = getCurrentState().getGroups().keySet().iterator();

        for (int i = 1; groups.hasNext(); i++) {
            Integer group = (Integer) groups.next();
            String token  = matcher.group(i);

            if (token != null) {
                IRule rule = (IRule) ((List) getCurrentState().getGroups().get(group)).get(0);

                if (rule instanceof MultiLineRule && (group.intValue() != Syntax.END)) {
                    MultiLineRule multiline = ((MultiLineRule) rule);

                    String newState = multiline.getState();

                    int index = getCurrentState().getName().indexOf(':');

                    if ((index > -1) && (newState.indexOf(':') == -1)) {
                        newState = getCurrentState().getName().substring(0, index) + ':' + newState;
                    }

                    RuntimeState runtimeState = new RuntimeState(newState, multiline);

                    state.push(runtimeState);

                    initMatcher();
                }

                int type = group.intValue();

                return new Token(matcher.start(), token.length(), type,
                                 getCurrentState().getName(), rule);
            }
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     */
    private void initMatcher() {
        this.matcher = getCurrentState().getPattern().matcher(text);
    }

    /**
     * DOCUMENT ME!
     */
    private void initState() {
        this.state = new Stack();
        this.state.push(new RuntimeState(UNINITIALIZED));
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private Syntax.State resolveState(String name) {
        int index = name.indexOf(':');

        if (index > -1) {
            return SyntaxManager.getState(name);
        }

        return syntax.getState(name);
    }

    //~ Inner Classes ******************************************************************************

    /**
     * DOCUMENT ME!
     *
     * @author $Author: amunoz $
     * @version $Revision: 1.2 $
     */
    public class RuntimeState {
        /** DOCUMENT ME! */
        private IRule rule = null;

        /** DOCUMENT ME! */
        private Map groups = null;

        /** DOCUMENT ME! */
        private Pattern pattern = null;

        /** DOCUMENT ME! */
        private String name = null;

        /**
         * Creates a new RuntimeState object.
         *
         * @param name DOCUMENT ME!
         * @param rule DOCUMENT ME!
         */
        public RuntimeState(String name,
                            IRule  rule) {
            this.name     = name;
            this.rule     = rule;
        }

        /**
         * Creates a new RuntimeState object.
         *
         * @param name DOCUMENT ME!
         */
        public RuntimeState(String name) {
            this(name, null);
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public int getDefaultToken() {
            return resolveState(name).getToken();
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public Map getGroups() {
            Syntax.State s = resolveState(name);

            if (rule == null) {
                return s.getGroups();
            }

            if (groups == null) {
                groups = new TreeMap();

                groups.putAll(s.getGroups());
                getGroup(Syntax.END).add(rule);
            }

            return groups;
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
         * @return DOCUMENT ME!
         */
        public Pattern getPattern() {
            if (pattern == null) {
                StringBuffer sb = new StringBuffer();
                for (Iterator i = getGroups().keySet().iterator(); i.hasNext();) {
                    Integer group = (Integer) i.next();
                    log.debug(group);

                    sb.append('(').append(createPattern(group.intValue())).append(')');

                    if (i.hasNext()) {
                        sb.append('|');
                    }
                }

                pattern = (sb.length() > 0) ? Pattern.compile(sb.toString(), Pattern.DOTALL) : null;
            }

            return pattern;
        }

        /**
         * DOCUMENT ME!
         *
         * @return Returns the rule.
         */
        public IRule getRule() {
            return rule;
        }

        /**
         * DOCUMENT ME!
         *
         * @param type DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        private List getGroup(int type) {
            List group = (List) getGroups().get(new Integer(type));

            if (group == null) {
                group = new ArrayList();

                groups.put(new Integer(type), group);
            }

            return group;
        }

        /**
         * DOCUMENT ME!
         *
         * @param group DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        private String createPattern(int group) {
            StringBuffer sb = new StringBuffer();

            List list = (List) getGroup(group);

            for (Iterator i = list.iterator(); i.hasNext();) {
                IRule rule = (IRule) i.next();

                if (group == Syntax.END) {
                    sb.append(((MultiLineRule) rule).getEnd());
                } else {
                    sb.append(rule.toString());
                }

                if (i.hasNext()) {
                    sb.append('|');
                }
            }

            return (sb.length() > 0) ? sb.toString() : null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $Author: amunoz $
     * @version $Revision: 1.2 $
     */
    public static class Token {
        /** DOCUMENT ME! */
        private IRule rule = null;

        /** DOCUMENT ME! */
        private String state = null;

        /** DOCUMENT ME! */
        private int length = 0;

        /** DOCUMENT ME! */
        private int position = 0;

        /** DOCUMENT ME! */
        private int type = 0;

        /**
         * Creates a new Token object.
         *
         * @param position DOCUMENT ME!
         * @param length DOCUMENT ME!
         * @param type DOCUMENT ME!
         * @param state DOCUMENT ME!
         * @param rule DOCUMENT ME!
         */
        public Token(int    position,
                     int    length,
                     int    type,
                     String state,
                     IRule  rule) {
            this.position     = position;
            this.length       = length;
            this.type         = type;
            this.state        = state;
            this.rule         = rule;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public int getLength() {
            return length;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public int getPosition() {
            return position;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public IRule getRule() {
            return rule;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public String getState() {
            return state;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public int getType() {
            return type;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public String toString() {
            StringBuffer sb = new StringBuffer();
            sb.append("[");
            sb.append("position=").append(position).append(",");
            sb.append("length=").append(length).append(",");
            sb.append("type=").append(type).append(",");
            sb.append("rule=").append(rule).append(",");
            sb.append("state=").append(state);
            sb.append("]");

            return sb.toString();
        }
    }
}
