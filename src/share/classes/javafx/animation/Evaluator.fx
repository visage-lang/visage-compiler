/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
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

package javafx.animation;

import com.sun.javafx.runtime.Pointer;
import java.lang.Object;

// TODO: this class will go away once we fully migrate to the
// Interpolatable system...
abstract class Evaluator {
    private static attribute NUMBER:Evaluator = NumberEvaluator {}
    private static attribute INTEGER:Evaluator = IntegerEvaluator {}

    static function forPointer(p:Pointer) : Evaluator {
        var o = p.get();

        if (o instanceof java.lang.Double) {
            //java.lang.System.err.println("Eval: Double");
            return NUMBER;
        } else if (o instanceof java.lang.Integer) {
            //java.lang.System.err.println("Eval: Integer");
            return INTEGER;
        } else {
            //java.lang.System.err.println("Eval: NULL");
            return null;
        }
    }

    abstract function evaluate(pole1:Object, pole2:Object, t:Number) : Object;
}

class NumberEvaluator extends Evaluator {
    function evaluate(pole1:Object, pole2:Object, t:Number) : Object {
        var v1 = pole1 as Number;
        var v2 = pole2 as Number;
        return (v1 + (v2 - v1) * t);
    }
}

class IntegerEvaluator extends Evaluator {
    function evaluate(pole1:Object, pole2:Object, t:Number) : Object {
        var v1 = pole1 as Integer;
        var v2 = pole2 as Integer;
        return (v1.intValue() + (v2.intValue() - v1.intValue()) * t);
    }
}
