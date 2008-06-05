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

package com.sun.javafx.ideaplugin.parsing;

import com.intellij.psi.tree.IElementType;
import com.sun.tools.javafx.antlr.v3Parser;
import com.sun.javafx.ideaplugin.FxPlugin;

/**
 * FxAstNodes
 */
public enum FxAstNodes {
    GENERIC_NODE(v3Parser.LAST_TOKEN + 1),
    ERROR_NODE(v3Parser.LAST_TOKEN + 2),
    MODULE(v3Parser.MODULE),
    PACKAGE_DECL(v3Parser.PACKAGE);

    public final int tokenValue;
    public final FxElementType elementType;

    private static FxElementType[] tokenArray;

    static {
        int max = 0;
        for (FxAstNodes t : FxAstNodes.values())
            max = Math.max(max, t.tokenValue);
        tokenArray = new FxElementType[max+1];
        for (FxAstNodes t : FxAstNodes.values())
            tokenArray[t.tokenValue] = t.elementType;
    }

    FxAstNodes(int tokenValue) {
        this.tokenValue = tokenValue;
        this.elementType = new FxElementType(name(), FxPlugin.FX_LANGUAGE, tokenValue);
    }

    public IElementType asElementType() {
        return elementType;
    }

    public static IElementType getElement(int tokenType) {
        if (tokenType < 0)
            return null; // avoid AIOOBE
        else
            return tokenArray[tokenType];
    }
}
