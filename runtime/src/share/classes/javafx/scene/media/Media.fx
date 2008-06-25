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
 
package javafx.scene.media;

import javafx.lang.Duration;
//import com.sun.media.jmc.Media;
import java.net.URI;
import com.sun.javafx.scene.MediaHelper;
import java.lang.System;
import java.lang.Exception;

/**
 * The {@code Media} class represents a media resource.
 * It contains information about the media, such
 * as its source, resolution, and metadata.
 * 
 * @profile common
 */
public class Media {
    private attribute jmcMediaInfo:com.sun.media.jmc.Media = null;
    
    /**
     * Defines the {@code String} which specifies the URI of the media;
     * It may be an absolute URI, such as "file://media.mov", or
     * relative, such as "./media.mov"
     * 
     * @profile common
     */
    public attribute source:String on replace {
        if ((source == null)) {
            jmcMediaInfo = null; // avoid errors while initializing
        } else {
            try {
                jmcMediaInfo = new com.sun.media.jmc.Media(new URI(source));
            } catch (e:Exception) {
                handleException(e);
            }
        }
    }
    
    /**
     * The width resolution of the source media
     * 
     * @profile common
     */
    public attribute resolutionX:Number;
    /**
     * The height resolution of the source media
     * 
     * @profile common
     */
    public attribute resolutionY:Number;
    /**
     * The duration of the source media
     * 
     * @profile common
     */
    public attribute duration:Duration;
    
    /**
     * Returns the metadata stored in the source media for the specified key
     * 
     * @profile common
     */
    public function getMetadata(key:String) : String { // Need generic returns
        if (jmcMediaInfo <> null) {
            return MediaHelper.getStringMetadata(jmcMediaInfo, key);
        } else {
            return null;
        }
    }
    
    
     /**
     * The function to be invoked when an error occurs on this {@code Media} object
     * 
     * @profile common
     */
    public attribute onError: function(e:MediaError);
    
    private function handleException(e: Exception) {
        if (onError <> null) {
            onError(MediaError.exceptionToError(e));
        } else {
            System.out.println("FX Media Object caught Exception {e}");
            System.out.println("    source ='{source}'");
        }
    }
}
