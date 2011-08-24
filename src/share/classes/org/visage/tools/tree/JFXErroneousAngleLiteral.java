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
package org.visage.tools.tree;

import org.visage.api.tree.*;
import org.visage.api.tree.Tree.VisageKind;

import com.sun.tools.mjavac.util.List;

/**
 * Specialized tree that can indicate to the walker that it was manufactured
 * in place of an angle value that should have been there in the source code but
 * was erroneously not there (or perhaps the IDE is using this tree and the user
 * has not typed that in yet).
 *
 * @author Stephen Chin <steveonjava@gmail.com>
 */
public class VisageErroneousAngleLiteral extends VisageAngleLiteral  {

    /**
     * This class is just an Erroneous node masquerading as
     * a Block so that we can create it in the tree. So it
     * stores a local erroneous block and uses this for the
     * visitor pattern etc.
     */
    private VisageErroneous errNode;

    protected VisageErroneousAngleLiteral(List<? extends VisageTree> errs) {
        errNode = new VisageErroneous(errs);
    }

    @Override
    public JavafxTag getFXTag() {
        return JavafxTag.ERRONEOUS;
    }

    @Override
    public void accept(JavafxVisitor v) {
        v.visitErroneous(errNode);
    }

    @Override
    public <R, D> R accept(VisageTreeVisitor<R, D> v, D d) {
        return v.visitErroneous(errNode, d);
    }

    public List<? extends VisageTree> getErrorTrees() {
        return errNode.getErrorTrees();
    }
    
    @Override
    public VisageKind getJavaFXKind() {
        return VisageKind.ERRONEOUS;
    }
    
}
