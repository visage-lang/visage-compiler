/*
 * Copyright (c) 2010-2011, Visage Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name Visage nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.sun.tools.javafx.tree;

import com.sun.javafx.api.tree.*;
import com.sun.javafx.api.tree.Tree.JavaFXKind;
import javafx.lang.AngleUnit;

/**
 * Tree node for angle literals, such as "100deg" or "3rad".
 * @author Stephen Chin <steveonjava@gmail.com>
 */
public class JFXAngleLiteral extends JFXExpression implements AngleLiteralTree {
    public JFXLiteral value;
    public AngleUnit units;
    
   protected JFXAngleLiteral(){
        this.value = null;
        this.units = null;
    }

    protected JFXAngleLiteral(JFXLiteral value, AngleUnit units) {
        this.value = value;
        this.units = units;
    }

    @Override
    public JavafxTag getFXTag() {
        return JavafxTag.ANGLE_LITERAL;
    }

    @Override
    public void accept(JavafxVisitor v) {
        v.visitAngleLiteral(this);
    }

    @Override
    public JavaFXKind getJavaFXKind() {
        return JavaFXKind.ANGLE_LITERAL;
    }

    @Override
    public <R, D> R accept(JavaFXTreeVisitor<R, D> visitor, D data) {
        return visitor.visitAngleLiteral(this, data);
    }

    @Override
    public AngleUnit getUnits() {
        return units;
    }

    @Override
    public JFXLiteral getValue() {
        return value;
    }
}
