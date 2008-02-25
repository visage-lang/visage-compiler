REM
REM  $Id$
REM 
REM  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
REM  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
REM

set classpath="%~dp0;%~dp0javafxpad/lib/jfxFormat.jar;%~dp0lib/javafxrt.jar;%~dp0lib/Filters.jar;%~dp0lib/swing-layout.jar;%~dp0lib/script.jar"
java -classpath %classpath% net.java.javafx.FXShell javafxpad.Main

