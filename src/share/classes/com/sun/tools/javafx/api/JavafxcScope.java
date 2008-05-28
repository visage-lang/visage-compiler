/*
 * Copyright 2006 Sun Microsystems, Inc.  All Rights Reserved.
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

package com.sun.tools.javafx.api;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Env;

/**
 * Provides an implementation of Scope.
 *
 * <p><b>This is NOT part of any API supported by Sun Microsystems.
 * If you write code that depends on this, you do so at your own
 * risk.  This code and its internal interfaces are subject to change
 * or deletion without notice.</b></p>
 *
 * @author Jonathan Gibbons;
 */
public class JavafxcScope implements com.sun.source.tree.Scope {
    protected final Env<AttrContext> env;

    /** Creates a new instance of JavacScope */
    JavafxcScope(Env<AttrContext> env) {
        env.getClass(); // null-check
        this.env = env;
    }

    public JavafxcScope getEnclosingScope() {
        if (env.outer != null && env.outer != env)
            return  new JavafxcScope(env.outer);
        else {
            // synthesize an outermost "star-import" scope
            return new JavafxcScope(env) {
                @Override
                public boolean isStarImportScope() {
                    return true;
                }
                @Override
                public JavafxcScope getEnclosingScope() {
                    return null;
                }
                @Override
                public Iterable<? extends Element> getLocalElements() {
                    return env.toplevel.starImportScope.getElements();
                }
            };
        }
    }

    public TypeElement getEnclosingClass() {
        // hide the dummy class that javac uses to enclose the top level declarations
        return (env.outer == null || env.outer == env ? null : env.enclClass.sym);
    }

    public ExecutableElement getEnclosingMethod() {
        return (env.enclMethod == null ? null : env.enclMethod.sym);
    }

    public Iterable<? extends Element> getLocalElements() {
        return env.info.getLocalElements();
    }

    public Env<AttrContext> getEnv() {
        return env;
    }

    public boolean isStarImportScope() {
        return false;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof JavafxcScope) {
            JavafxcScope s = (JavafxcScope) other;
            return (env.equals(s.env)
                && isStarImportScope() == s.isStarImportScope());
        } else
            return false;
    }

    @Override
    public int hashCode() {
        return env.hashCode() + (isStarImportScope() ? 1 : 0);
    }

    @Override
    public String toString() {
        return "JavafxcScope[env=" + env + ",starImport=" + isStarImportScope() + "]";
    }
}
