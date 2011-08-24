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

<!--
    This stylesheet serves as "base" template which other sheets can import.
-->
<xsl:transform version="1.0"
               xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
               xmlns:c="http://xml.apache.org/xalan/java/org.visage.tools.tree.xml.Compiler"
               xmlns:visage="http://visage.org">
    
    <xsl:strip-space elements="*"/>
    <xsl:template match="@*|node()"/>

    <xsl:template match="/">
      <xsl:apply-templates select="visage:visage"/>
    </xsl:template>

    <xsl:template match="visage:visage">
        <xsl:apply-templates select="visage:file"/>
        <xsl:apply-templates select="visage:package"/>
        <xsl:apply-templates select="visage:defs"/>
    </xsl:template>

    <xsl:template match="visage:file"/>
    <xsl:template match="visage:package"/>
   
    <xsl:template match="visage:defs">
        <xsl:apply-templates/>
    </xsl:template>
    
    <xsl:template match="visage:import"/>
    <xsl:template match="visage:bind-status"/>
    
    <xsl:template match="visage:var">
        <xsl:apply-templates select="visage:init-value/*"/>
        <xsl:apply-templates select="visage:on-replace"/>
        <xsl:apply-templates select="visage:on-invalidate"/>
    </xsl:template>
    <xsl:template match="visage:def">
        <xsl:apply-templates select="visage:init-value/*"/>
        <xsl:apply-templates select="visage:on-replace"/>
        <xsl:apply-templates select="visage:on-invalidate"/>
    </xsl:template>

    <xsl:template match="visage:empty"/>
    
    <xsl:template match="visage:while">
        <xsl:apply-templates select="visage:test/*"/>
        <xsl:apply-templates select="visage:stmt/*"/>
    </xsl:template>
   
    <xsl:template match="visage:try">
        <xsl:apply-templates select="visage:block"/>
        <xsl:apply-templates select="visage:catches/visage:catch"/>
        <xsl:if test="visage:finally">
            <xsl:apply-templates select="visage:finally/visage:block"/>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="visage:catch">
        <xsl:apply-templates select="visage:block"/>
    </xsl:template>
    
    <xsl:template match="visage:if">
        <xsl:apply-templates select="visage:test/*"/>
        <xsl:apply-templates select="visage:then/*"/>
        <xsl:if test="visage:else">
            <xsl:apply-templates select="visage:else/*"/>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="visage:break"/>
    <xsl:template match="visage:continue"/>
    <xsl:template match="visage:return">
        <xsl:apply-templates select="*"/>
    </xsl:template>
    
    <xsl:template match="visage:throw">
        <xsl:apply-templates select="*"/>
    </xsl:template>
    
    <xsl:template match="visage:invoke">
        <xsl:apply-templates select="visage:method/*"/>
        <xsl:apply-templates select="visage:args/*"/>
    </xsl:template>
    
    <xsl:template match="visage:paren">
        <xsl:apply-templates select="*[1]"/>
    </xsl:template>
    
    <xsl:template name="handle-binary-expr">
        <xsl:param name="operator"/>
        <xsl:apply-templates select="visage:left/*"/>
        <xsl:apply-templates select="visage:right/*"/>
    </xsl:template>
    
    <xsl:template match="visage:assignment">
        <xsl:call-template name="handle-binary-expr">
            <xsl:with-param name="operator"> = </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <!-- compound assignments -->
    <xsl:template match="visage:multiply-assignment">
        <xsl:call-template name="handle-binary-expr">
            <xsl:with-param name="operator"> *= </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="visage:divide-assignment">
        <xsl:call-template name="handle-binary-expr">
            <xsl:with-param name="operator"> /= </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="visage:remainder-assignment">
        <xsl:call-template name="handle-binary-expr">
            <xsl:with-param name="operator"> %= </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="visage:plus-assignment">
        <xsl:call-template name="handle-binary-expr">
            <xsl:with-param name="operator"> += </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="visage:minus-assignment">
        <xsl:call-template name="handle-binary-expr">
            <xsl:with-param name="operator"> -= </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="visage:left-shift-assignment">
        <xsl:call-template name="handle-binary-expr">
            <xsl:with-param name="operator"> &lt;&lt;= </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="visage:right-shift-assignment">
        <xsl:call-template name="handle-binary-expr">
            <xsl:with-param name="operator"> &gt;&gt;= </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="visage:unsigned-right-shift-assignment">
        <xsl:call-template name="handle-binary-expr">
            <xsl:with-param name="operator"> &gt;&gt;&gt;= </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="visage:and-assignment">
        <xsl:call-template name="handle-binary-expr">
            <xsl:with-param name="operator"> &amp;= </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="visage:xor-assignment">
        <xsl:call-template name="handle-binary-expr">
            <xsl:with-param name="operator"> ^= </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="visage:or-assignment">
        <xsl:call-template name="handle-binary-expr">
            <xsl:with-param name="operator"> |= </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <!-- unary operators -->
    <xsl:template match="visage:sizeof">
        <xsl:apply-templates/>
    </xsl:template>
    
    <xsl:template name="handle-unary-expr">
        <xsl:param name="operator"/>
        <xsl:apply-templates select="*[1]"/>
    </xsl:template>
    
    <xsl:template match="visage:postfix-increment">
        <xsl:apply-templates select="*[1]"/>
    </xsl:template>
    
    <xsl:template match="visage:prefix-increment">
        <xsl:call-template name="handle-unary-expr">
            <xsl:with-param name="operator">++</xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="visage:postfix-decrement">
        <xsl:apply-templates select="*[1]"/>
    </xsl:template>
    
    <xsl:template match="visage:prefix-decrement">
        <xsl:call-template name="handle-unary-expr">
            <xsl:with-param name="operator">--</xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="visage:unary-plus">
        <xsl:call-template name="handle-unary-expr">
            <xsl:with-param name="operator">+</xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="visage:unary-minus">
        <xsl:call-template name="handle-unary-expr">
            <xsl:with-param name="operator">-</xsl:with-param>
        </xsl:call-template>
    </xsl:template>

    <xsl:template match="visage:logical-complement">
        <xsl:call-template name="handle-unary-expr">
            <xsl:with-param name="operator">not </xsl:with-param>
        </xsl:call-template>
    </xsl:template>

    <!-- binary operators -->
    <xsl:template match="visage:multiply">
        <xsl:call-template name="handle-binary-expr">
            <xsl:with-param name="operator"> * </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="visage:divide">
        <xsl:call-template name="handle-binary-expr">
            <xsl:with-param name="operator"> / </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="visage:remainder">
        <xsl:call-template name="handle-binary-expr">
            <xsl:with-param name="operator"> mod </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="visage:plus">
        <xsl:call-template name="handle-binary-expr">
            <xsl:with-param name="operator"> + </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="visage:minus">
        <xsl:call-template name="handle-binary-expr">
            <xsl:with-param name="operator"> - </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="visage:left-shift">
        <xsl:call-template name="handle-binary-expr">
            <xsl:with-param name="operator"> &lt;&lt; </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="visage:right-shift">
        <xsl:call-template name="handle-binary-expr">
            <xsl:with-param name="operator"> &gt;&gt; </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="visage:unsigned-right-shift">
        <xsl:call-template name="handle-binary-expr">
            <xsl:with-param name="operator"> &gt;&gt;&gt; </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="visage:less-than">
        <xsl:call-template name="handle-binary-expr">
            <xsl:with-param name="operator"> &lt; </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="visage:greater-than">
        <xsl:call-template name="handle-binary-expr">
            <xsl:with-param name="operator"> &gt; </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="visage:less-than-equal">
        <xsl:call-template name="handle-binary-expr">
            <xsl:with-param name="operator"> &lt;= </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="visage:greater-than-equal">
        <xsl:call-template name="handle-binary-expr">
            <xsl:with-param name="operator"> &gt;= </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="visage:equal-to">
        <xsl:call-template name="handle-binary-expr">
            <xsl:with-param name="operator"> == </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="visage:not-equal-to">
        <xsl:call-template name="handle-binary-expr">
            <xsl:with-param name="operator"> != </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="visage:and">
        <xsl:call-template name="handle-binary-expr">
            <xsl:with-param name="operator"> &amp; </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="visage:xor">
        <xsl:call-template name="handle-binary-expr">
            <xsl:with-param name="operator"> ^ </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="visage:or">
        <xsl:call-template name="handle-binary-expr">
            <xsl:with-param name="operator"> | </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="visage:conditional-and">
        <xsl:call-template name="handle-binary-expr">
            <xsl:with-param name="operator"> and </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="visage:conditional-or">
        <xsl:call-template name="handle-binary-expr">
            <xsl:with-param name="operator"> or </xsl:with-param>
        </xsl:call-template>
    </xsl:template>

    <xsl:template match="visage:cast">
        <xsl:apply-templates select="visage:expr/*"/>
        <xsl:apply-templates select="visage:type/*" mode="no-colon"/>
    </xsl:template>
    
    <xsl:template match="visage:instanceof">
        <xsl:apply-templates select="visage:expr/*"/>
        <xsl:apply-templates select="visage:type/*"/>
    </xsl:template>
    
    <xsl:template match="visage:select">
        <xsl:apply-templates select="visage:expr/*"/>
        <xsl:apply-templates select="visage:member/*"/>
    </xsl:template>
    
    <xsl:template match="visage:ident"/>
    
    <!-- literals -->
    <xsl:template match="visage:int-literal"/>
    <xsl:template match="visage:long-literal"/>
    <xsl:template match="visage:float-literal"/>
    <xsl:template match="visage:double-literal"/>
    <xsl:template match="visage:true"/>
    <xsl:template match="visage:false"/>
    <xsl:template match="visage:string-literal"/>
    <xsl:template match="visage:null"/>
    
    <!-- modifiers -->
    <xsl:template match="visage:modifiers"/>
    
    <xsl:template name="handle-list">
        <xsl:param name="parent"/>
        <xsl:for-each select="$parent/*">
            <xsl:apply-templates select="."/>
        </xsl:for-each>
    </xsl:template>
    
    <xsl:template match="visage:value">
        <xsl:apply-templates/>
    </xsl:template>
    
    <xsl:template match="visage:block">
        <xsl:call-template name="handle-list">
            <xsl:with-param name="parent" select="visage:stmts"/>
        </xsl:call-template>
        <xsl:apply-templates select="visage:value"/>
    </xsl:template>
    
    <!-- importing stylesheet may override these -->
    <xsl:template name="class-body-begin"/>
    <xsl:template name="class-body-end"/>
    
    <xsl:template name="handle-class-body">
        <xsl:call-template name="class-body-begin"/>
        <xsl:call-template name="handle-list">
            <xsl:with-param name="parent" select="visage:members"/>
        </xsl:call-template>
        <xsl:call-template name="class-body-end"/>
    </xsl:template>
    
    <xsl:template match="visage:class">
        <xsl:apply-templates select="visage:modifiers"/>
        
        <!-- class body -->
        <xsl:call-template name="handle-class-body"/>
    </xsl:template>
    
    <xsl:template match="visage:for">
        <xsl:for-each select="visage:in/*">
            <xsl:apply-templates select="visage:var"/>
            <xsl:apply-templates select="visage:seq/*"/>
            <xsl:if test="visage:where">
                <xsl:apply-templates select="visage:where/*"/>
            </xsl:if>
        </xsl:for-each>
        <xsl:apply-templates select="visage:body/*"/>
    </xsl:template>
    
    <xsl:template match="visage:indexof">
        <xsl:apply-templates/>
    </xsl:template>
    
    <xsl:template match="visage:init">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="visage:postinit">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="visage:new">
        <xsl:apply-templates select="visage:class/*"/>
        <xsl:call-template name="handle-list">
            <xsl:with-param name="parent" select="visage:args"/>
        </xsl:call-template>
    </xsl:template>
            
    <xsl:template name="object-literal-begin"/>
    <xsl:template name="object-literal-end"/>
    <xsl:template match="visage:object-literal">
        <xsl:apply-templates select="visage:class/*"/>
        <xsl:call-template name="object-literal-begin"/>
        <xsl:for-each select="visage:defs/*">
            <xsl:apply-templates select="."/>
        </xsl:for-each>
        <xsl:call-template name="object-literal-end"/>
    </xsl:template>
    
    <xsl:template match="visage:object-literal-init">
        <xsl:apply-templates select="visage:bind-status"/>
        <xsl:apply-templates select="visage:expr/*"/>
    </xsl:template>
   
   <xsl:template match="visage:override-var">
        <xsl:apply-templates select="visage:expr/*"/>
        <xsl:apply-templates select="visage:on-replace"/>
        <xsl:apply-templates select="visage:on-invalidate"/>
    </xsl:template>

    <xsl:template name="handle-on-replace-clause">
        <xsl:apply-templates select="visage:old-value/*"/>
        <xsl:apply-templates select="visage:first-index/*"/>
        <xsl:apply-templates select="visage:last-index/*"/>
        <xsl:apply-templates select="visage:new-elements/*"/>
        <xsl:apply-templates select="visage:block"/>
    </xsl:template>

    <xsl:template match="visage:on-replace">
        <xsl:call-template name="handle-on-replace-clause"/>
    </xsl:template>

    <xsl:template match="visage:on-invalidate">
        <xsl:call-template name="handle-on-replace-clause"/>
    </xsl:template>

    <!-- functions -->
    <xsl:template name="function-body-begin"/>
    <xsl:template name="function-body-end"/>
    <xsl:template name="handle-function-body">
        <xsl:apply-templates select="visage:params/*"/>
        <xsl:apply-templates select="visage:return-type/*"/> 
        <xsl:choose>
            <xsl:when test="visage:block">
                <xsl:call-template name="function-body-begin"/>
                <xsl:call-template name="handle-list">
                    <xsl:with-param name="parent" select="visage:block/visage:stmts"/>
                </xsl:call-template>
                <xsl:apply-templates select="visage:block/visage:value"/>
                <xsl:call-template name="function-body-end"/>
            </xsl:when>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template match="visage:function">
        <xsl:variable name="name" select="visage:name"/>
        <xsl:apply-templates select="visage:modifiers"/>
        <xsl:call-template name="handle-function-body"/>
    </xsl:template>
    
    <xsl:template match="visage:anon-function">
        <xsl:call-template name="handle-function-body"/>
    </xsl:template>
    
    <xsl:template match="visage:seq-delete">
        <xsl:apply-templates select="visage:elem/*"/>
        <xsl:apply-templates select="visage:seq/*"/>
    </xsl:template>
    
    <xsl:template match="visage:seq-empty"/>
    
    <xsl:template match="visage:seq-explicit">
        <xsl:call-template name="handle-list">
            <xsl:with-param name="parent" select="visage:items"/>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="visage:seq-indexed">
        <xsl:apply-templates select="visage:seq/*"/>
        <xsl:apply-templates select="visage:index/*"/>
    </xsl:template>
    
    <xsl:template match="visage:seq-slice">
        <xsl:apply-templates select="visage:seq/*"/>
        <xsl:apply-templates select="visage:first/*"/>
        <xsl:apply-templates select="visage:last/*"/>
    </xsl:template>
    
    <xsl:template match="visage:seq-insert">
        <xsl:apply-templates select="visage:elem/*"/>
        <xsl:apply-templates select="visage:seq/*"/>
    </xsl:template>
    
    <xsl:template match="visage:seq-range">
        <xsl:apply-templates select="visage:lower/*"/>
        <xsl:apply-templates select="visage:upper/*"/>
        <xsl:apply-templates select="visage:step/*"/>
    </xsl:template>

    <xsl:template match="visage:invalidate">
        <xsl:apply-templates select="visage:var/*"/>
    </xsl:template>
    
    <xsl:template match="visage:string-expr">
        <xsl:for-each select="visage:part">
            <xsl:choose>
                <xsl:when test="visage:string-literal">
                    <xsl:apply-templates/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:apply-templates select="visage:expr/*"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:for-each>
    </xsl:template>
    
    <xsl:template match="visage:time-literal"/>

    <xsl:template match="visage:interpolate-value">
       <xsl:if test="visage:attribute">
           <xsl:for-each select="visage:attribute/*">
               <xsl:apply-templates select="."/>
           </xsl:for-each>
       </xsl:if>
       <xsl:for-each select="visage:value/*">
           <xsl:apply-templates select="."/>
       </xsl:for-each>
       <xsl:if test="visage:interpolation">
           <xsl:apply-templates select="visage:interpolation/*"/>
       </xsl:if>
    </xsl:template>
    
    <xsl:template match="visage:keyframe-literal">
        <xsl:apply-templates select="visage:start-dur/*"/>
        <xsl:for-each select="visage:interpolation-values/*">
            <xsl:apply-templates select="."/>
        </xsl:for-each>
        <xsl:if test="visage:trigger">
            <xsl:apply-templates select="./*"/>
        </xsl:if> 
    </xsl:template>

</xsl:transform>
