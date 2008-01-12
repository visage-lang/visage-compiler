#!/bin/sh

#
#  $Id$
# 
#  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
#  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
#

DIR="c:/Alaska/root/gbtds-0.1"
JAVA_HOME='C:/Program Files/Java/jdk1.6.0'

java="$JAVA_HOME/bin/java"

LIB="$dir/lib"
TRANSLATOR="$DIR/newScripts/svgTranslator"
TRANSLATOR_LIB="$TRANSLATOR/lib"

case `uname` in
        CYGWIN*) PS=";";;
        *) PS=":";;
esac

OPTS="-Xss1024K -Xmx256M"

"$java" \
        $opts \
        -classpath "$TRANSLATOR${PS}$LIB/swing-layout.jar${PS}$DIR/typeApi.jar${PS}$DIR/typeImpl.jar${PS}$LIB/Filters.jar${PS}$LIB/jazz.jar${PS}$TRANSLATOR/build/svg-translator.jar${PS}$TRANSLATOR_LIB/batik-awt-util.jar${PS}$TRANSLATOR_LIB/batik-parser.jar${PS}$TRANSLATOR_LIB/batik-util.jar${PS}$TRANSLATOR_LIB/resolver.jar${PS}$CLASSPATH" \
        com.sun.javafx.f2d.translator.svg.Main \
        "$1" -
