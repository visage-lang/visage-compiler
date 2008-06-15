/*
 * Copyright 1999-2007 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */


#ifdef PROJECT_JAVAFXDOC

#include "configuration.h"
#include <string>

int main(int argc, char** argv) {
    Configuration config;
    int error;
    
    if ( (error =  config.getConfiguration(argc, argv)) != (EXIT_SUCCESS) )  {
        return error;
    }
    
    // construct command
    // "%_JAVACMD%" %_JVM_ARGS% "-Xbootclasspath/p:%_JAVAFXDOC_HOME%\javafxc.jar;%_JAVAFXDOC_HOME%\javafxdoc.jar" com.sun.tools.javafxdoc.Main -classpath "%_CLASSPATH%" %_FX_ARGS%
    std::string cmd = "\"" + config.javacmd + "\" ";

    if (config.vmargs != "") {
        cmd += config.vmargs + " ";
    }

    cmd += "\"-Xbootclasspath/p:";
    cmd += config.javafxpath + "\\javafxc.jar;";
    cmd += config.javafxpath + "\\javafxdoc.jar\" ";

    cmd += "com.sun.tools.javafxdoc.Main ";
    
    if (config.classpath != "") {
        cmd += "-classpath \"" + config.classpath + "\" ";
    }

    cmd += config.fxargs;
    
    // debug
//    printf (cmd.c_str());
    system (cmd.c_str());

    return EXIT_SUCCESS;
}

#endif  /* PROJECT_JAVAFXDOC */