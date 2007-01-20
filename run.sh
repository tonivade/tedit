#! /bin/sh

if [ "$TEDIT_HOME" = "" ] ; then
    TEDIT_HOME="`pwd`"
fi

if [ \! -d $TEDIT_HOME/log ] ; then
    mkdir $TEDIT_HOME/log
fi

CLASSPATH=$JAVA_HOME/lib/tools.jar:$TEDIT_HOME/config

for i in `find $TEDIT_HOME/main -type f \( -name '*.jar' -or -name '*.zip' \) -print`; do
        CLASSPATH=$CLASSPATH:$i
done

for i in `find $TEDIT_HOME/lib -type f \( -name '*.jar' -or -name '*.zip' \) -print`; do
        CLASSPATH=$CLASSPATH:$i
done

exec java -Xmx32m -Xms8m -classpath $CLASSPATH -Dtedit.home=$TEDIT_HOME tk.tomby.tedit.Main $@
