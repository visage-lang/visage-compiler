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

#include <windows.h>
#include <stdlib.h>

#include "configuration.h"

Configuration::Configuration()
: javafxpath(""), classpath(""), vmargs("") {
}

Configuration::~Configuration() {
}

int Configuration::getConfiguration(int argc, char** argv) {
    int error;
    
    // set inital values
    init();
    
    // read arguments
    if ( (error =  parseArgs(--argc, ++argv)) != EXIT_SUCCESS )  {
        return error;
    }
    
    // set defaults, if neccessary
    setDefaults();
    
    return EXIT_SUCCESS;
}

void Configuration::init() {
    const char* s;
    
    // set javaCmd if given directly in _JAVACMD
    s = getenv("_JAVACMD");
    javacmd = (s != NULL)? s : "";
    
    // set fxargs if given directly in _FX_ARGS
    s = getenv("_FX_ARGS");
    fxargs = (s != NULL)? s : "";
}

int Configuration::parseArgs(int argc, char** argv) {
    const char *arg;

    while (argc-- > 0 && (arg = *argv++) != NULL) {

        if ((0 == strcmp("-cp", arg)) || (0 == strcmp("-classpath", arg))) {
            if (argc-- > 0 && (arg = *argv++) != NULL) {
                classpath = arg;
            } else {
                fprintf (stderr, "No argument for classpath found.");
                return EXIT_FAILURE;
            }

        } else if (0 == strncmp("-J", arg, 2)) {
            vmargs += " ";
            vmargs += arg+2;    // skip first two characters "-J"

        } else {
            fxargs += " ";
            fxargs += arg;
        }
    }
    return (EXIT_SUCCESS);
}

void Configuration::setDefaults() {
    char* s;
    
    // evaluate JAVA_HOME, if javacmd not set
    if (javacmd == "") {
        s = getenv("JAVA_HOME");
        if (s != NULL) {
            javacmd = s;
            javacmd += "/bin/java.exe";
            if (! fileExists(javacmd)) {
                javacmd = "java.exe";
            }

        } else {
            javacmd = "java.exe";
        }
    }
    
    // evaluate classpath if not set
    if (classpath == "") {
        classpath = ".";
        s = getenv("CLASSPATH");
        if (s != NULL) {
            classpath += ";";
            classpath += s;
        }
    }
    
    // set default javafxpath if not set
    if (javafxpath == "") {
        s = new char[MAX_PATH];
        GetModuleFileName (NULL, s, MAX_PATH);
        javafxpath = s;
        javafxpath.erase (javafxpath.rfind("\\"));
        javafxpath += "\\..\\lib";

        delete[] s;
    }
}

int Configuration::fileExists(const std::string& path) {
   WIN32_FIND_DATA ffd;
   HANDLE hFind;

   hFind = FindFirstFile(path.c_str(), &ffd);
   if (hFind == INVALID_HANDLE_VALUE) 
   {
       return FALSE;
   } 
   else 
   {
      FindClose(hFind);
      return (ffd.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY) == 0;
   }
}
