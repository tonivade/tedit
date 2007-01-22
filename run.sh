#! /bin/sh

if [ "$TEDIT_HOME" = "" ] ; then
    TEDIT_HOME="`pwd`"
fi

if [ \! -d $TEDIT_HOME/log ] ; then
    mkdir $TEDIT_HOME/log
fi

java -Xmx32m -Xms8m -Dtedit.home=$TEDIT_HOME -jar boot.jar $@
