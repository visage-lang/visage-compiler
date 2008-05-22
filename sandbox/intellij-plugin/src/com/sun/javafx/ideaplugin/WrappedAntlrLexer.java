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

package com.sun.javafx.ideaplugin;

import com.sun.tools.javafx.antlr.v3Lexer;
import com.sun.tools.javac.util.Context;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.RecognitionException;

/**
 * WrappedAntlrLexer
*
* @author Brian Goetz
*/
class WrappedAntlrLexer extends v3Lexer {
    public static final int SYNTHETIC_SEMI = -100;
    public final boolean useSytheticSemi;

    public WrappedAntlrLexer(ANTLRStringStream stringStream, boolean useSyntheticSemi) {
        super(new Context(), stringStream);
        this.useSytheticSemi = useSyntheticSemi;
    }

    // Workaround IAE exception in creating diagnostic
    public void displayRecognitionError(String[] strings, RecognitionException recognitionException) {
        // Blechh!!  But if we don't do this, we loop forever.
        throw new RecognitionExceptionSignal(recognitionException);
    }

    /* Override this so we can distinguish between real and synthetic semicolon in lexing */
    protected int getSyntheticSemiType() {
        return useSytheticSemi ? SYNTHETIC_SEMI : v3Lexer.SEMI;
    }

    public int getState() {
        return getLexicalState();
    }
}
