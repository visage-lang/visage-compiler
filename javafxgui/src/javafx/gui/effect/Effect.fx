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

package javafx.gui.effect;

import com.sun.javafx.gui.AccessHelper;
import com.sun.javafx.gui.AccessHelper.EffectAccessor;
import java.lang.Object;
import java.awt.GraphicsConfiguration;

/**
 * The abstract base class for all effect implementations.
 */
public abstract class Effect {

    private static attribute accessor = EffectAccessorImpl.create();

    abstract function getImpl():com.sun.scenario.effect.Effect;

    /**
     * Returns a {@code String} representing the type of hardware
     * acceleration, if any, that is used when applying this {@code Effect}
     * on the given {@code GraphicsConfiguration}.  This method is
     * intended for informational or debugging purposes only.
     */
    public function getAccelType(config:GraphicsConfiguration):String {
        return getImpl().getAccelType(config).toString();
    }
}

/**
 * Package-private class that gives javafx.gui.Node access to implementation
 * details from this package.
 */
class EffectAccessorImpl extends EffectAccessor {

    static function create() : EffectAccessor {
        var accessor:EffectAccessor = EffectAccessorImpl {};
        AccessHelper.setEffectAccessor(accessor);
        return accessor;
    }

    public function getImpl(fxEffect:Object):com.sun.scenario.effect.Effect {
        return (fxEffect as Effect).getImpl();
    }
}
