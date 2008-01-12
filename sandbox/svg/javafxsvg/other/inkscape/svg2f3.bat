@echo off

REM
REM  $Id$
REM 
REM  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
REM  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
REM

set dir=C:\Alaska\root\gbtds-0.1
set JAVA_HOME=C:\Progra~1\Java\jdk1.6.0

set java=%JAVA_HOME%\bin\java

set lib=%dir%\lib
set translator=%dir%\newScripts\svgTranslator
set translatorLib=%translator%\lib

set opts=-Xss1024K -Xmx256M

%java% %opts% -classpath %translator%;%lib%\swing-layout.jar;%dir%\typeApi.jar;%dir%\typeImpl.jar;%lib%\Filters.jar;%lib%\jazz.jar;%translator%\build\svg-translator.jar;%translatorLib%\batik-awt-util.jar;%translatorLib%\batik-parser.jar;%translatorLib%\batik-util.jar;%translatorLib%\resolver.jar;%CLASSPATH% com.sun.javafx.f2d.translator.svg.Main %1 -
