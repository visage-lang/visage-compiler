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

package javafx.ui.canvas;
import java.awt.geom.AffineTransform;
import java.lang.Math;

/**
 * Interface for functions that perform transformations on graphics objects.
 */

public abstract class Transform {
    protected attribute transformable :Transformable;
    protected attribute transform :AffineTransform on replace {
        if (transformable <> null)
            transformable.updateTransform();
    };

    public static function matrix(a:Number,b:Number,c:Number,
            d:Number,e:Number,f:Number):Matrix {
        //return Matrix.matrix(a,b,c,d,e,f);
        Matrix {a: a, b: b, c: c, d: d, tx: e, ty: f}
    }
    
    public static function translate(x:Number, y:Number):Translate {
        Translate.translate(x, y);
    }    

    public static function rotate(z:Number, x:Number, y:Number):Rotate {
        Rotate.rotate(z, x, y);
    }

    public static function scale(x:Number, y:Number):Scale {
        Scale.scale(x, y);
    }

    public static function scaleX(x :Number):Scale {
        Scale.scaleX(x);
    }
    public static function scaleY(y :Number):Scale {
        Scale.scaleY(y);
    }

    public static function skew(x:Number, y:Number):Skew {
        Skew.skew(x, y);
    }
    public static function skewX(x:Number) :Skew{
        Skew.skewX(x);
    }
    public static function skewY(y:Number) :Skew{
        Skew.skewY(y);
    }

    public static function translateY(y:Number):Translate {
        Translate.translateY(y);
    }
    public static function translateX(x:Number):Translate {
        Translate.translateX(x);
    }
    public static function scaleXY(s:Number):Scale {
        Scale.scaleXY(s);
    }      

}




//rotateY(angle, x, y, z, f) = rotateYRotateZ(angle, x,  y, z, f);
//rotateX(angle, x, y, z, f) = rotateXRotateZ(angle, x,  y, z, f);

public class CompositeTransform extends Transform {
    public attribute transforms :Transform[];
    private attribute txs :AffineTransform[] //TODO JXFC-152
        //= bind foreach ( t in transforms) t.transform
    on insert  [indx] (newValue) {
        updateTransform();
    }
    on delete [indx] (oldValue) {
        updateTransform();
    }
    on replace [indx] (oldValue) {
        updateTransform();
    };
    private function updateTransform() {
        var result = new AffineTransform();
        foreach (t in txs) {
            result.concatenate(t);
        }
        transform = result;
    }
}

