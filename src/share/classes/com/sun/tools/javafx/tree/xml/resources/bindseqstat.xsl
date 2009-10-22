<!--
 * Copyright 2009 Sun Microsystems, Inc.  All Rights Reserved.
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
 -->

<xsl:transform
     xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
     xmlns:c="http://xml.apache.org/xalan/java/com.sun.tools.javafx.tree.xml.Compiler"
     xmlns:fxt="http://xml.apache.org/xalan/java/com.sun.tools.javafx.code.JavafxTypes"
     xmlns:fx="http://openjfx.java.sun.com">

    <xsl:import href="analyze.xsl"/>    
    <xsl:strip-space elements="*"/>
    <xsl:output method="text"/>

    <xsl:template name="init-globals">
        <xsl:text>#</xsl:text>
        <xsl:value-of select="c:evalScript('inBind = new java.util.Stack(); inBind.push(false)')"/>
        <xsl:value-of select="c:putGlobal('numSeqBinds', 0)"/>
        <xsl:value-of select="c:putGlobal('numSeqBindsWithOnReplace', 0)"/>
        <xsl:value-of select="c:putGlobal('numSeqBindsWithOnInvalidate', 0)"/>
        <xsl:value-of select="c:putGlobal('numNonSeqBinds', 0)"/>
        <xsl:value-of select="c:putGlobal('numNonSeqBindsWithOnReplace', 0)"/>
        <xsl:value-of select="c:putGlobal('numNonSeqBindsWithOnInvalidate', 0)"/>
        <xsl:value-of select="c:putGlobal('numBindSeqExplicit', 0)"/>
        <xsl:value-of select="c:putGlobal('numBindSeqSlice', 0)"/>
        <xsl:value-of select="c:putGlobal('numBindSeqRange', 0)"/>
        <xsl:value-of select="c:putGlobal('numBindFor', 0)"/>
        <xsl:text>&#10;</xsl:text>
    </xsl:template>

    <xsl:template match="/">
      <xsl:call-template name="init-globals"/>
      <xsl:apply-templates select="fx:javafx-script"/>
    </xsl:template>

    <xsl:template match="fx:javafx-script">
<!-- output the source file name and write down global variable values as properties -->
        <xsl:apply-templates select="fx:file"/>
        <xsl:apply-templates select="fx:defs"/>
count.of.non.sequence.binds=<xsl:value-of select="c:getGlobal('numNonSeqBinds')"/>
count.of.non.sequence.binds.with.on.replace=<xsl:value-of select="c:getGlobal('numNonSeqBindsWithOnReplace')"/>
count.of.non.sequence.binds.with.on.invalidate=<xsl:value-of select="c:getGlobal('numNonSeqBindsWithOnInvalidate')"/>
count.of.sequence.binds=<xsl:value-of select="c:getGlobal('numSeqBinds')"/>
count.of.sequence.binds.with.on.replace=<xsl:value-of select="c:getGlobal('numSeqBindsWithOnReplace')"/>
count.of.sequence.binds.with.on.invalidate=<xsl:value-of select="c:getGlobal('numSeqBindsWithOnInvalidate')"/>
count.of.bound.explicit.sequences=<xsl:value-of select="c:getGlobal('numBindSeqExplicit')"/>
count.of.bound.slice.sequences=<xsl:value-of select="c:getGlobal('numBindSeqSlice')"/>
count.of.bound.range.sequences=<xsl:value-of select="c:getGlobal('numBindSeqRange')"/>
count.of.bound.for.expressions=<xsl:value-of select="c:getGlobal('numBindFor')"/>
    </xsl:template>

    <xsl:template match="fx:file">
        <xsl:text># compiled from </xsl:text> <xsl:value-of select="."/>
    </xsl:template>

     <xsl:template name="handle-var" >
        <xsl:for-each select="fx:bind-status">
             <xsl:choose>
                 <xsl:when test=". != 'unbound'">
                     <xsl:if test="not(c:evalScript('inBind.push(true)'))"/>
                 </xsl:when>
                 <xsl:otherwise>
                     <xsl:if test="not(c:evalScript('inBind.push(false)'))"/>
                 </xsl:otherwise>
             </xsl:choose>
        </xsl:for-each>
        <xsl:if test="c:evalScript('inBind.peek()')">
            <xsl:choose>
            <xsl:when test="fxt:isSequence(c:types(), c:type(@typeref))">
                <xsl:if test="not(c:putGlobal('numSeqBinds', c:getGlobal('numSeqBinds') + 1))"/>
                <xsl:if test="fx:on-replace">
                    <xsl:if test="not(c:putGlobal('numSeqBindsWithOnReplace', c:getGlobal('numSeqBindsWithOnReplace') + 1))"/>
                </xsl:if>
                <xsl:if test="fx:on-invalidate">
                    <xsl:if test="not(c:putGlobal('numSeqBindsWithOnInvalidate', c:getGlobal('numSeqBindsWithOnInvalidate') + 1))"/>
                </xsl:if>
            </xsl:when>
            <xsl:otherwise>
                <xsl:if test="not(c:putGlobal('numNonSeqBinds', c:getGlobal('numNonSeqBinds') + 1))"/>
                <xsl:if test="fx:on-replace">
                    <xsl:if test="not(c:putGlobal('numNonSeqBindsWithOnReplace', c:getGlobal('numNonSeqBindsWithOnReplace') + 1))"/>
                </xsl:if>
                <xsl:if test="fx:on-invalidate">
                    <xsl:if test="not(c:putGlobal('numNonSeqBindsWithOnInvalidate', c:getGlobal('numNonSeqBindsWithOnInvalidate') + 1))"/>
                </xsl:if>
             </xsl:otherwise>
            </xsl:choose>
        </xsl:if>
        <xsl:apply-imports/>
        <xsl:if test="not(c:evalScript('inBind.pop()'))"/>
    </xsl:template>

    <xsl:template match="fx:class/fx:members/fx:var|fx:class/fx:members/fx:def">
        <xsl:call-template name="handle-var"/>
    </xsl:template>

    <xsl:template match="fx:javafx-script/fx:defs/fx:var|fx:javafx-script/fx:defs/fx:def">
        <xsl:call-template name="handle-var"/>
    </xsl:template>

    <!-- a local var or def -->
    <xsl:template match="fx:var|fx:def">
        <xsl:call-template name="handle-var"/>
    </xsl:template>
    
    <xsl:template match="fx:object-literal-init">
        <xsl:for-each select="fx:bind-status">
             <xsl:choose>
                 <xsl:when test=". != 'unbound'">
                     <xsl:if test="not(c:evalScript('inBind.push(true)'))"/>
                 </xsl:when>
                 <xsl:otherwise>
                     <xsl:if test="not(c:evalScript('inBind.push(false)'))"/>
                 </xsl:otherwise>
             </xsl:choose>
        </xsl:for-each>
        <xsl:apply-templates select="fx:expr/*"/>
        <xsl:if test="c:evalScript('inBind.peek()')">
            <xsl:choose>
            <xsl:when test="fxt:isSequence(c:types(), c:type(@typeref))">
                <xsl:if test="not(c:putGlobal('numSeqBinds', c:getGlobal('numSeqBinds') + 1))"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:if test="not(c:putGlobal('numNonSeqBinds', c:getGlobal('numNonSeqBinds') + 1))"/>
             </xsl:otherwise>
            </xsl:choose>
        </xsl:if>
        <xsl:if test="not(c:evalScript('inBind.pop()'))"/>
    </xsl:template>

    <xsl:template match="fx:seq-explicit">
        <xsl:if test="c:evalScript('inBind.peek()')">
            <xsl:if test="not(c:putGlobal('numBindSeqExplicit', c:getGlobal('numBindSeqExplicit') + 1))"/>
        </xsl:if>
        <xsl:apply-imports/>
    </xsl:template>
   
    <xsl:template match="fx:seq-slice">
        <xsl:if test="c:evalScript('inBind.peek()')">
            <xsl:if test="not(c:putGlobal('numBindSeqSlice', c:getGlobal('numBindSeqSlice') + 1))"/>
        </xsl:if>
        <xsl:apply-imports/>
    </xsl:template>

    <xsl:template match="fx:seq-range">
        <xsl:if test="c:evalScript('inBind.peek()')">
            <xsl:if test="not(c:putGlobal('numBindSeqRange', c:getGlobal('numBindSeqRange') + 1))"/>
        </xsl:if>
        <xsl:apply-imports/>
    </xsl:template>

    <xsl:template match="fx:for">
        <xsl:if test="c:evalScript('inBind.peek()')">
            <xsl:if test="not(c:putGlobal('numBindFor', c:getGlobal('numBindFor') + 1))"/>
        </xsl:if>
        <xsl:apply-imports/>
    </xsl:template>

</xsl:transform>
