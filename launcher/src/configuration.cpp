/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
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

Configuration::Configuration(const std::string& prefix)
: prefix(prefix),
        javafxpath(""), 
        classpath("."), 
        vmargs(""), 
        profile_classpath(""), 
        profile_bootclasspath(""), 
        profile_bootclasspath_prepend(""), 
        profile_bootclasspath_append(""), 
        profile_nativelibpath(""), 
        profile_filename("desktop.properties") {
}

Configuration::~Configuration() {
}

int Configuration::initConfiguration(int argc, char** argv) {
    int error;
    
    // set inital values
    init();
    
    // read arguments
    if ( (error =  parseArgs(--argc, ++argv)) != (EXIT_SUCCESS) )  {
        return error;
    }
    
    // read config-file
    if ( (error =  readConfigFile()) != (EXIT_SUCCESS) )  {
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

void Configuration::init() {
    const char* s;
    
    // set javaCmd if given directly in _JAVACMD
    s = getenv("_JAVACMD");
    javacmd = (s != NULL)? s : "";
    
    // evaluate CLASSPATH
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
    javafxpath += "\\..";
    
    // set fxargs if given directly in _FX_ARGS
    s = getenv("_FX_ARGS");
    fxargs = (s != NULL)? s : "";
}

int Configuration::readConfigFile() {
    // find file
    std::string path = javafxpath;
    path += "\\profiles\\" + profile_filename;
    std::ifstream file(path.c_str());
    if (file == NULL) {
        fprintf (stderr, "Properties-file %s not found.", profile_filename.c_str());
        return (EXIT_FAILURE);
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
            if (key.find(prefix) != 0) {
                continue;
            }
            key.erase (0, prefix.length());

            start = line.find_first_not_of(" \"\t\n\r", pos+1);
            end = line.find_last_not_of(" \"\t\n\r");
            if (start == std::string::npos || end == std::string::npos) {
                continue;
            }
            value = line.substr (start, end - start + 1);
            
            // evaluate key/value-pair
            if (key == "classpath") {
                profile_classpath = value;
            } else
            if (key == "bootclasspath") {
                profile_bootclasspath = value;
            } else
            if (key == "bootclasspath_prepend") {
                profile_bootclasspath_prepend = value;
            } else
            if (key == "bootclasspath_append") {
                profile_bootclasspath_append = value;
            } else
            if (key == "nativelibpath") {
                profile_nativelibpath = value;
            };
        }
    }
    file.close();
    return (EXIT_SUCCESS);
}

int Configuration::parseArgs(int argc, char** argv) {
    const char *arg;

    while (argc-- > 0 && (arg = *argv++) != NULL) {

        if ((0 == strcmp("-cp", arg)) || (0 == strcmp("-classpath", arg))) {
            if (argc-- > 0 && (arg = *argv++) != NULL) {
                classpath = arg;
            } else {
                fprintf (stderr, "No argument for classpath found.");
                return (EXIT_FAILURE);
            }

        } else if (0 == strcmp("-profile", arg)) {
            if (argc-- > 0 && (arg = *argv++) != NULL) {
                profile_filename = arg;
                profile_filename += ".properties";
            } else {
                fprintf (stderr, "No argument for profile found.");
                return (EXIT_FAILURE);
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
