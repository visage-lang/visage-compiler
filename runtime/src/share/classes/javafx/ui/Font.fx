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

package javafx.ui;

import com.sun.javafx.api.ui.UIContext;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.lang.*;
import java.net.URL;
import java.util.Map;
import java.util.WeakHashMap;

import javafx.ui.FontStyle;

public class Font {
    public attribute face: FontFace;
    public attribute faceName: String;
    public attribute size: Integer;
    public attribute style: FontStyle[];
    private attribute styleStr: String[];
    public bound function getFont(): java.awt.Font{
       return awtFont;
    }
    
    public static function Font(faceName:String, styleStr:String[], size:Integer){
        var style: FontStyle[] = [];
        for (i in styleStr) {
            if (i == "PLAIN")  {
                insert FontStyle.PLAIN into style;
            } else if (i == "ITALIC")  {
                insert FontStyle.ITALIC into style;
            } else if (i == "BOLD") {
                insert FontStyle.BOLD into style;
            } else {
                throw new java.lang.Throwable("Bad font style {i}: expected PLAIN, BOLD, or ITALIC");
            };
        };
        Font {faceName:faceName, style:style, size:size};
    }
    public bound function bigger(): Font {
        Font{faceName:faceName, style:style, size:size+1};
    }
    public bound function smaller(): Font {
        Font{faceName:faceName, style:style, size:size-1};
    }
    public bound function plain(): Font {
        Font{faceName:faceName, style:[FontStyle.PLAIN], size:size};
    }
    public bound function bold(): Font{
        Font{faceName:faceName, style:[FontStyle.BOLD], size:size};
    }
    public bound function italic(): Font {
        Font{faceName:faceName, style:[FontStyle.ITALIC], size:size};
    }
    private attribute awtFont: java.awt.Font
    /*TODO: NPE in initialization 
     =  bind
                 if(face <> null and face.url <> null  ) {
                    getCachedFont(face.url, computeStyle(style), size) 
                 } else if (face <> null) {
                     new java.awt.Font(face.id, computeStyle(style), size);
                 } else if (faceName <> null)  {
                     new java.awt.Font(faceName, computeStyle(style), size);
                 } else {
                    new java.awt.Font("Dialog", computeStyle(style), size);
                 }
     */
        on replace {
            if (awtFont <> null) {
                var layout = new TextLayout(" ", awtFont, HIGH_QUALITY_FONT_CONTEXT);
                ascent = layout.getAscent();
                descent = layout.getDescent();
                leading = layout.getLeading();
            }
        }; 
    //TODO private functions
    private function computeStyle(n:FontStyle[]): Integer {
        var bits = 0;
        
        for (i in n) {
            var id = i.id;
            bits = UIElement.context.setBit(bits, id);
        };
        return bits;
    }
    
    private static function getCachedFont(url: String, style: Integer, size: Integer): 
            java.awt.Font {
        var f: java.awt.Font = fontCache.get(url) as java.awt.Font;
        if (f == null) {
            try {
                var urlInstance = new URL(url);
                f = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT,
                                             urlInstance.openStream());
                fontCache.put(url, f);
            } catch (e: Exception) {
                throw new RuntimeException(e);
            }
        }
        return f.deriveFont(style, size);
    }
    private static attribute fontCache: Map = new WeakHashMap();

    public attribute ascent: Number;
    public attribute descent: Number;
    public attribute leading: Number;	
    
    public static attribute HIGH_QUALITY_FONT_CONTEXT:FontRenderContext = 
        new FontRenderContext(null, true, true);

    //TODO: workaround for awtFont initialization NPE
    init {
        awtFont = {
             if (face <> null and face.url <> null and face.url.length() <> 0) {
                getCachedFont(face.url, computeStyle(style), size);
             } else if (face <> null) {
                 new java.awt.Font(face.id, computeStyle(style), size);
             } else if (faceName <> null)  {
                 new java.awt.Font(faceName, computeStyle(style), size);
             } else {
                new java.awt.Font("Dialog", computeStyle(style), size);
             }
        };
    }
}

