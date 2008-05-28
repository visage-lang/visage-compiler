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

package javafx.ui.canvas;
import java.awt.geom.AffineTransform;

public class Matrix extends Transform {
    public attribute a: Number on replace {
        transform = new AffineTransform(a, b, c, d, tx, ty);
    }
    public attribute b: Number on replace {
        transform = new AffineTransform(a, b, c, d, tx, ty);
    };
    public attribute c: Number on replace {
        transform = new AffineTransform(a, b, c, d, tx, ty);
    };
    public attribute d: Number on replace {
        transform = new AffineTransform(a, b, c, d, tx, ty);
    };
    public attribute tx: Number on replace {
        transform = new AffineTransform(a, b, c, d, tx, ty);
    };
    public attribute ty: Number on replace {
        transform = new AffineTransform(a, b, c, d, tx, ty);
    };
    

}

    public static function matrix(a:Number,b:Number,c:Number,
            d:Number,tx:Number,ty:Number):Matrix {
        Matrix {a: a, b: b, c: c,
               d: d, tx: tx, ty: ty};
    }

