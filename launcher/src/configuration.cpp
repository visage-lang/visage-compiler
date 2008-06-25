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
#include <fstream>

#include "configuration.h"

Configuration::Configuration()
: javafxpath(""), classpath(""), vmargs("") {
}

Configuration::~Configuration() {
}

int Configuration::initConfiguration(int argc, char** argv) {
    int error;
    
    // set inital values
    init();
    
    // read config-file
    readConfigFile();
    
    // read arguments
    if ( (error =  parseArgs(--argc, ++argv)) != EXIT_SUCCESS )  {
        return error;
    }
    
    // evaluate JAVA_HOME, if javacmd not set
    if (javacmd.empty()) {
        const char* s = getenv("JAVA_HOME");
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
    
    return EXIT_SUCCESS;
}

std::string Configuration::evaluatePath (std::string& libs) {
    std::string result = "";
    std::string::size_type start=0, end;
    while ((end = libs.find(";", start)) != std::string::npos) {
        ++end; // include semicolon
        result += javafxpath + "\\" + libs.substr (start, end-start);
        start = end;
    }
    result += javafxpath + "\\" + libs.substr (start);
    return result;
}

void Configuration::init() {
    const char* s;
    
    // set javaCmd if given directly in _JAVACMD
    s = getenv("_JAVACMD");
    javacmd = (s != NULL)? s : "";
    
    // evaluate CLASSPATH
    classpath = ".";
    s = getenv("CLASSPATH");
    if (s != NULL) {
        classpath += ";";
        classpath += s;
    }
    
    // set default javafxpath
    char buf[MAX_PATH];
    GetModuleFileName (NULL, buf, MAX_PATH);
    javafxpath = buf;
    javafxpath.erase (javafxpath.rfind("\\"));
    javafxpath += "\\..\\lib";
    
    // set fxargs if given directly in _FX_ARGS
    s = getenv("_FX_ARGS");
    fxargs = (s != NULL)? s : "";

    // set default classpath-libraries for javafx
    if (javafx_classpath_libs.empty()) {
        javafx_classpath_libs = "javafxrt.jar;Scenario.jar;Decora-HW.jar;Decora-D3D.jar;jmc.jar";
    }
    
    // set default bootclass-libraries for javafxc
    if (javafxc_bootclasspath_libs.empty()) {
        javafxc_bootclasspath_libs = "javafxc.jar;javafxrt.jar";
    }
    
    // set default classpath-libraries for javafxc
    if (javafxc_classpath_libs.empty()) {
        javafxc_classpath_libs = "Scenario.jar;jmc.jar";
    }
    
    // set default bootclass-libraries for javafxdoc
    if (javafxdoc_bootclasspath_libs.empty()) {
        javafxdoc_bootclasspath_libs = "javafxc.jar;javafxdoc.jar";
    }
}

void Configuration::readConfigFile() {
    // find file
    std::string path = javafxpath;
    path += "\\javafx.properties";
    std::ifstream file(path.c_str());
    if (file == NULL) {
        return;
    }
    
    // prepare regular expression
    std::string line, key, value;
    std::string::size_type pos, start, end;
    
    while (getline (file, line)) {
        // remove comment
        if ((pos = line.find('#')) != std::string::npos) {
            line.erase (pos);
        }
        
        // parse line
        pos = line.find_first_of("=;");
        if (pos > 0 && pos != std::string::npos) {
            start = line.find_first_not_of(" \t\n\r");
            end = line.find_last_not_of(" \t\n\r", pos-1);
            if (start == std::string::npos || end == std::string::npos) {
                continue;
            }
            key   = line.substr (start, end - start + 1);

            start = line.find_first_not_of(" \t\n\r", pos+1);
            end = line.find_last_not_of(" \t\n\r");
            if (start == std::string::npos || end == std::string::npos) {
                continue;
            }
            value = line.substr (start, end - start + 1);
            
            // evaluate key/value-pair
            if (key == "javafx_classpath_libs") {
                javafx_classpath_libs = value;
            } else
            if (key == "javafxc_bootclasspath_libs") {
                javafxc_bootclasspath_libs = value;
            } else
            if (key == "javafxc_classpath_libs") {
                javafxc_classpath_libs = value;
            } else
            if (key == "javafxdoc_bootclasspath_libs") {
                javafxdoc_bootclasspath_libs = value;
            };
        }
    }
    file.close();
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
