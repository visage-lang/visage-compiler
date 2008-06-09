/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
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
 
package demo.mediatest;
import javafx.gui.*;

import java.lang.System;

/**
 * a media object which tries multiple extensions
 * looking for a format that the platform can handle
 */
class MxMedia extends Media {
    attribute onExtension = 0;
    static attribute extensions= ["mp4", "wmv"];

    attribute fileName: String on replace {
        onError = myError; // catch errors try different extesions
        source = "{__DIR__}{fileName}.{extensions[0]}";
    }
    
    function myError(mediaError:MediaError) {
        if (onExtension++ < sizeof(extensions)) {
                
                System.out.println("error = {mediaError}. Retrying...");
                source = "{__DIR__}{fileName}.{extensions[onExtension]}";
        } else {
            System.out.println("Cannot Open Media");
        }
    }
};
