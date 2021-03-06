/*
 * Copyright 2008-2009 Sun Microsystems, Inc.  All Rights Reserved.
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

package org.visage.ideaplugin.parsing;

import com.intellij.psi.tree.IElementType;
import org.visage.tools.antlr.v3Parser;
import org.visage.ideaplugin.VisagePlugin;

/**
 * VisageAstNodes
 */
public enum VisageAstNodes {
    GENERIC_NODE(v3Parser.LAST_TOKEN + 1);//,
//    ERROR_NODE(v3Parser.LAST_TOKEN + 2),
//    MODULE(v3Parser.SCRIPT),
//    PACKAGE_DECL(v3Parser.PACKAGE);

    public final int tokenValue;
    public final VisageElementType elementType;

    private static VisageElementType[] tokenArray;

    static {
        int max = 0;
        for (VisageAstNodes t : VisageAstNodes.values())
            max = Math.max(max, t.tokenValue);
        tokenArray = new VisageElementType[max+1];
        for (VisageAstNodes t : VisageAstNodes.values())
            tokenArray[t.tokenValue] = t.elementType;
    }

    VisageAstNodes(int tokenValue) {
        this.tokenValue = tokenValue;
        this.elementType = new VisageElementType(name(), VisagePlugin.VISAGE_LANGUAGE, tokenValue);
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
