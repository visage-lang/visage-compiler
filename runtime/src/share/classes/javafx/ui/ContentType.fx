/*
 *  $Id$
 *
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package javafx.ui;


public class ContentType {
    public attribute mimeType: String;
    
    public static attribute HTML       = ContentType{ mimeType: "text/html" };
    public static attribute PLAIN_TEXT = ContentType{ mimeType: "text/plain" };
    public static attribute RTF        = ContentType{ mimeType: "text/rtf" };


}
