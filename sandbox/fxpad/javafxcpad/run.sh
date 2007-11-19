#!/bin/sh

#
#  $Id$
# 
#  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
#  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
#
FX_HOME=`dirname "$0"`/../..
# Make the path absolute
FX_HOME=`cd "$FX_HOME" && pwd`
FX_BOOTCLASSPATH=${FX_HOME}/demos/javafxcpad/lib/javac.jar:${FX_HOME}/demos/javafxcpad/lib/javafxc.jar
export FX_BOOTCLASSPATH
CLASSPATH=${FX_HOME}/demos/javafxcpad/lib/jfxFormat.jar
export CLASSPATH
dp0="${FX_HOME}/"
opts="-Djava.library.path=$FX_LIBRARY_PATH"
# opts="-Djava.library.path=$FX_LIBRARY_PATH -XX:+UseParallelGC"
# -XX:+PrintGCApplicationStoppedTime 
app="JavaFX"

CLASSPATH="${dp0}javafxrt/src/javafx:${dp0}lib/swing-layout.jar:${dp0}lib/javafxrt.jar:${dp0}lib/Filters.jar:${dp0}demos:${CLASSPATH}"
ARGS=javafxcpad.Main
if [ `uname -s` = "Darwin" ]
then 
     java -Xbootclasspath/p:$FX_BOOTCLASSPATH -Xmx256M -Xss1024K -Xdock:name=$app $opts $debug $opengl -classpath "${CLASSPATH}" net.java.javafx.FXShell $ARGS
elif [ `uname -s` = "CYGWIN_NT-5.1" ]
then
     CP=`cygpath --path --windows "$CLASSPATH"`
     java -Xmx256M -Xss1024K $opts $debug $opengl -classpath "$CP" net.java.javafx.FXShell $ARGS
else
     java -Xmx256M -Xss1024K $opts $debug $opengl -classpath "${CLASSPATH}" net.java.javafx.FXShell $ARGS
fi


