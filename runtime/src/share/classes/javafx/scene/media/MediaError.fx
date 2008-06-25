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

import com.sun.media.jmc.MediaUnavailableException;
import com.sun.media.jmc.MediaUnsupportedException;
import com.sun.media.jmc.MediaInaccessibleException;
import com.sun.media.jmc.MediaCorruptedException;
import com.sun.media.jmc.MediaException;
import com.sun.media.jmc.OperationUnsupportedException;

  /** 
   * Contains error information
   * 
   * @see MediaPlayer.onError
   * @platform core
   */
public class MediaError {
    /**
     * Indicates an error has occurred for an unknown reason
     * @profile core
     */
    public static attribute UNKNOWN:Integer=0;
    /**
     * Indicates an error has occurred: although the media
     * may exist, it is not accessible
     * @profile core
     */
    public static attribute MEDIA_INACCESSIBLE:Integer=1;
    /**
     * Indicates an error has occurred: the media
     * does not exist or is otherwise unavailable
     * @profile core
     */
    public static attribute MEDIA_UNAVAILABLE:Integer=2;
    /**
     * Indicates an error has occurred: the media appears to be
     * invalid or corrupted
     * @profile core
     */
    public static attribute MEDIA_CORRUPTED:Integer=3;
    /**
     * Indicates that this media is not supported by this platform
     * @profile core
     */
    public static attribute MEDIA_UNSUPPORTED:Integer=4;
    /**
     * Indicates that an operation performed on the media is not
     * supported by this platform
     * @profile core
     */
    public static attribute OPERATION_UNSUPPORTED:Integer=5;
    /**
     * Indicates that the media has not been specified
     * @profile core
     */
    public static attribute MEDIA_UNSPECIFIED=6;
    
   /**
     * reason this error
     * @profile core
     */
    public attribute cause:Integer;
    /**
     * Contains more information about this error
     * @profile core
    */
    public attribute message:String;

    /**
     * converts Java exceptions into mediaErrors
     */
    static function exceptionToError(e:java.lang.Exception):MediaError {
        var mediaErrorN;
        
        if (e instanceof MediaUnavailableException) {
            mediaErrorN = MediaError.MEDIA_UNAVAILABLE;
        } else if (e instanceof MediaInaccessibleException) {
            mediaErrorN = MediaError.MEDIA_INACCESSIBLE;
        } else if (e instanceof MediaCorruptedException) {
            mediaErrorN = MediaError.MEDIA_CORRUPTED;
        } else if (e instanceof OperationUnsupportedException) {
            mediaErrorN = MediaError.OPERATION_UNSUPPORTED;
        } else if (e instanceof MediaUnsupportedException) {
            mediaErrorN = MediaError.MEDIA_UNSUPPORTED;
        } else {
            mediaErrorN = MediaError.UNKNOWN;
        }
        return MediaError{cause: mediaErrorN, message: e.toString()}
    }
    
    private static attribute errorString = [ "unknown",
                                             "media inaccesible",
                                             "media unavailable",
                                             "media corrupted" ,
                                             "media unsupported" ,
                                             "operation unsupported",
                                             "media unspecified" ];
                                  
    function toString():String {
        return "MediaError: {errorString[cause]}:{message}";
    }
};
