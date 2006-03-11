#! /bin/sh

if [ "$1" = "--laf" ]; then
    case "$2" in
        "win")
            LAF="com.sun.java.swing.plaf.windows.WindowsLookAndFeel"
        ;;
        "gtk")
            LAF="com.sun.java.swing.plaf.gtk.GTKLookAndFeel"
        ;;
        "motif")
            LAF="com.sun.java.swing.plaf.motif.MotifLookAndFeel"
        ;;
        *)
            LAF=""
    esac
    shift 2
fi

if [ "$TEDIT_HOME" = "" ] ; then
    TEDIT_HOME="$HOME/tedit"
fi

if [ \! -d $TEDIT_HOME/log ] ; then
    mkdir $TEDIT_HOME/log
fi

CLASSPATH=$TEDIT_HOME/classes:$TEDIT_HOME/config:$TEDIT_HOME/resources:$TEDIT_HOME/scripts

for i in `find $TEDIT_HOME/lib -type f \( -name '*.jar' -or -name '*.zip' \) -print`; do
        CLASSPATH=$CLASSPATH:$i
done

if [ "$LAF" = "" ] ; then
    exec java -Xmx32m -Xms8m -classpath $CLASSPATH -Dtedit.home=$TEDIT_HOME tk.tomby.tedit.Main $@
else
    exec java -Xmx32m -Xms8m -classpath $CLASSPATH -Dtedit.home=$TEDIT_HOME -Dswing.defaultlaf=$LAF tk.tomby.tedit.Main $@
fi
