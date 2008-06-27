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

#include "util.h"

Util::Util() {
}

Util::~Util() {
}

std::string Util::evaluatePath (std::string& javafxpath, std::string& libs) {
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

int Util::createProcess(std::string& cmd) {
    STARTUPINFO start;
    PROCESS_INFORMATION pi;

    memset (&start, 0, sizeof (start));
    start.cb = sizeof (start);

#ifdef DEBUG 
    printf("Cmdline: %s\n", cmd.c_str());
#endif
    if (!CreateProcess (NULL, (char*)cmd.c_str(),
                        NULL, NULL, TRUE, NORMAL_PRIORITY_CLASS,
                        NULL, 
                        NULL, // lpCurrentDirectory
                        &start,
                        &pi)) {
        fprintf (stderr, "Cannot start java.exe.");
        return EXIT_FAILURE;
    } else {
        // Wait until child process exits.
        WaitForSingleObject( pi.hProcess, INFINITE );

        // Close process and thread handles. 
        CloseHandle( pi.hProcess );
        CloseHandle( pi.hThread );        
        exit(0);
    }
    
}
