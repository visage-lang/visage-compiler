/* 
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved. 
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

package javafx.xml;

import java.io.Writer;

/**
 * Represents a DOM Comment
 * @author jclarke
 */

public class Comment extends Node {
    /**
     * sets the node type to COMMENT
     */
    override attribute type = NodeType.COMMENT;

    /**
     * Convert this node to an XML format based on 
     * the attributes indent, doIndent, omitXMLDeclaration, encoding and
     * standalone. Output is written to the Writer.
     * @param writer the java.io.Writer that will receive the formated xml.
     * @param depth the depth of this node in the tree being serialized
     * @see indent
     * @see doIndent
     */ 
    override function serialize(writer:Writer,  depth:Integer):Void {
        writer.write("{getIndent(depth)}<!--{value}-->\n");
        writer.flush();
    } 
}
