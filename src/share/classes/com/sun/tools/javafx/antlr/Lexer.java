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

package com.sun.tools.javafx.antlr;

import com.sun.tools.javafx.tree.JavafxTreeMaker;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.*;

import com.sun.tools.javac.code.*;
import com.sun.tools.javac.util.*;
import static com.sun.tools.javac.util.ListBuffer.lb;
import com.sun.tools.javac.util.Position;

import org.antlr.runtime.*;

/**
 * Base class for ANTLR generated parsers 
 * 
 * @author Zhiqun Chen
 */
public abstract class Lexer extends org.antlr.runtime.Lexer {
    
    /** The log to be used for error diagnostics.
     */
    protected Log log;
    
    /** The Source language setting. */
    protected Source source;
    
    protected JCErroneous errorTree;
      
    protected Lexer(){};
    
    protected Lexer (CharStream input) {
        super(input);
    }
    
    protected Lexer (CharStream input, RecognizerSharedState state) {
        super(input, state);
    }
    
    public String getErrorMessage(RecognitionException e, String[] tokenNames) {
        
        StringBuffer mb = new StringBuffer();
        if (e instanceof NoViableAltException) {
            NoViableAltException nvae = (NoViableAltException) e;
            mb.append(getCharErrorDisplay(e.c));          
            mb.append(" is not supported in JavaFX");
        } else {
            mb.append( super.getErrorMessage(e, tokenNames) );
        }
      
        return  mb.toString();
    }
    

    @Override
    public void displayRecognitionError(String[] tokenNames, RecognitionException e) {
   
        String msg = getErrorMessage(e, tokenNames);
//        log.error(Position.NOPOS, "javafx.generalerror", msg);
        log.error(getCharIndex(), "javafx.generalerror", msg);
    }
}
