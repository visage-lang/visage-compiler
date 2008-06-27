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

#ifndef _CONFIGURATION_H
#define	_CONFIGURATION_H

#include <string>

class Configuration {
public:
    std::string javacmd;
    std::string javafxpath;
    std::string classpath;
    std::string vmargs;
    std::string fxargs;
    
    std::string javafx_classpath_libs;
    std::string javafxc_bootclasspath_libs;
    std::string javafxc_classpath_libs;
    std::string javafxdoc_bootclasspath_libs;
    
    Configuration();
    ~Configuration();
    
    int initConfiguration (int argc, char** argv);
    
private:
    void init();
    void readConfigFile();
    int parseArgs(int argc, char** argv);
    int fileExists(const std::string& path);
};

#endif	/* _CONFIGURATION_H */

