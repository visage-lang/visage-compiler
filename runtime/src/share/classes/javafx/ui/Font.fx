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

import javafx.ui.FontStyle;

public class Font {
    public attribute face: FontFace;
    public attribute faceName: String;
    public attribute size: Integer;
    public attribute style: FontStyle[];
    private attribute styleStr: String[];
    public function getFont(): java.awt.Font{
       return awtFont;
    }
    
    public function Font(faceName:String, style:String[], size:Integer){
        this.faceName = faceName;
        
        foreach (i in style) {
            if (i == "PLAIN")  {
                insert FontStyle.PLAIN into this.style;
            } else if (i == "ITALIC")  {
                insert FontStyle.ITALIC into this.style;
            } else if (i == "BOLD") {
                insert FontStyle.BOLD into this.style;
            } else {
                throw new java.lang.Throwable("Bad font style {i}: expected PLAIN, BOLD, or ITALIC");
            };
        };
        this.styleStr = style;
        this.size = size;
    }
    public function bigger(): Font {
        Font{faceName:faceName, style:style, size:size+1};
    }
    public function smaller(): Font {
        Font{faceName:faceName, style:style, size:size-1};
    }
    public function plain(): Font {
        Font{faceName:faceName, style:[FontStyle.PLAIN], size:size};
    }
    public function bold(): Font{
        Font{faceName:faceName, style:[FontStyle.BOLD], size:size};
    }
    public function italic(): Font {
        Font{faceName:faceName, style:[FontStyle.ITALIC], size:size};
    }
    private attribute awtFont: java.awt.Font =  bind
                 if(face <> null and face.url <> null  ) {
                    UIElement.context.getFont(face.url, computeStyle(style), size) 
                 } else if (face <> null) {
                     new java.awt.Font(face.id, computeStyle(style), size);
                 } else if (faceName <> null)  {
                     new java.awt.Font(faceName, computeStyle(style), size);
                 } else {
                    new java.awt.Font("Dialog", computeStyle(style), size);
                 }
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
        
        foreach (i in n) {
            bits = UIElement.context.setBit(bits, i.id);
        };
        return bits;
    }
    public attribute ascent: Number;
    public attribute descent: Number;
    public attribute leading: Number;	
    
    public static attribute HIGH_QUALITY_FONT_CONTEXT:FontRenderContext = 
                        new FontRenderContext(null, true, true);
}

