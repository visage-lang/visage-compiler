/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
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

package javafx.gui;

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
    public static attribute MEDIA_UNAVAILILABLE:Integer=2;
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
     * Contains more information about this error
     * @profile core
    */
    public attribute message:String;
};
