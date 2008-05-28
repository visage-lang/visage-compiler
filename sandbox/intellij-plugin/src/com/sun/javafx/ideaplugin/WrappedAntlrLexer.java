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
    public final int syntheticSemi;
    public final boolean signalOnError;

    public WrappedAntlrLexer(ANTLRStringStream stringStream, boolean useSyntheticSemi, boolean signalOnError) {
        super(new Context(), stringStream);
        syntheticSemi = useSyntheticSemi ? SYNTHETIC_SEMI : v3Lexer.SEMI;
        this.signalOnError = signalOnError;
    }

    public WrappedAntlrLexer(ANTLRStringStream stringStream, boolean useSyntheticSemi) {
        this(stringStream, useSyntheticSemi, true);
    }

    // Workaround IAE exception in creating diagnostic
    public void displayRecognitionError(String[] strings, RecognitionException recognitionException) {
        // Blechh!!  But if we don't do this, we loop forever.
        if (signalOnError)
            throw new RecognitionExceptionSignal(recognitionException);
    }

    /* Override this so we can distinguish between real and synthetic semicolon in lexing */
    protected int getSyntheticSemiType() {
        return syntheticSemi;
    }

    public int getState() {
        return getLexicalState();
    }
}
