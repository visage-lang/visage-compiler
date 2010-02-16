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
               xmlns:c="http://xml.apache.org/xalan/java/com.sun.tools.javafx.tree.xml.Compiler"
               xmlns:fx="http://openjfx.java.sun.com">
    
    <xsl:strip-space elements="*"/>
    <xsl:template match="@*|node()"/>

    <xsl:template match="/">
      <xsl:apply-templates select="fx:javafx-script"/>
    </xsl:template>

    <xsl:template match="fx:javafx-script">
        <xsl:apply-templates select="fx:file"/>
        <xsl:apply-templates select="fx:package"/>
        <xsl:apply-templates select="fx:defs"/>
    </xsl:template>

    <xsl:template match="fx:file"/>
    <xsl:template match="fx:package"/>
   
    <xsl:template match="fx:defs">
        <xsl:apply-templates/>
    </xsl:template>
    
    <xsl:template match="fx:import"/>
    <xsl:template match="fx:bind-status"/>
    
    <xsl:template match="fx:var">
        <xsl:apply-templates select="fx:init-value/*"/>
        <xsl:apply-templates select="fx:on-replace"/>
        <xsl:apply-templates select="fx:on-invalidate"/>
    </xsl:template>
    <xsl:template match="fx:def">
        <xsl:apply-templates select="fx:init-value/*"/>
        <xsl:apply-templates select="fx:on-replace"/>
        <xsl:apply-templates select="fx:on-invalidate"/>
    </xsl:template>

    <xsl:template match="fx:empty"/>
    
    <xsl:template match="fx:while">
        <xsl:apply-templates select="fx:test/*"/>
        <xsl:apply-templates select="fx:stmt/*"/>
    </xsl:template>
   
    <xsl:template match="fx:try">
        <xsl:apply-templates select="fx:block"/>
        <xsl:apply-templates select="fx:catches/fx:catch"/>
        <xsl:if test="fx:finally">
            <xsl:apply-templates select="fx:finally/fx:block"/>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="fx:catch">
        <xsl:apply-templates select="fx:block"/>
    </xsl:template>
    
    <xsl:template match="fx:if">
        <xsl:apply-templates select="fx:test/*"/>
        <xsl:apply-templates select="fx:then/*"/>
        <xsl:if test="fx:else">
            <xsl:apply-templates select="fx:else/*"/>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="fx:break"/>
    <xsl:template match="fx:continue"/>
    <xsl:template match="fx:return">
        <xsl:apply-templates select="*"/>
    </xsl:template>
    
    <xsl:template match="fx:throw">
        <xsl:apply-templates select="*"/>
    </xsl:template>
    
    <xsl:template match="fx:invoke">
        <xsl:apply-templates select="fx:method/*"/>
        <xsl:apply-templates select="fx:args/*"/>
    </xsl:template>
    
    <xsl:template match="fx:paren">
        <xsl:apply-templates select="*[1]"/>
    </xsl:template>
    
    <xsl:template name="handle-binary-expr">
        <xsl:param name="operator"/>
        <xsl:apply-templates select="fx:left/*"/>
        <xsl:apply-templates select="fx:right/*"/>
    </xsl:template>
    
    <xsl:template match="fx:assignment">
        <xsl:call-template name="handle-binary-expr">
            <xsl:with-param name="operator"> = </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <!-- compound assignments -->
    <xsl:template match="fx:multiply-assignment">
        <xsl:call-template name="handle-binary-expr">
            <xsl:with-param name="operator"> *= </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="fx:divide-assignment">
        <xsl:call-template name="handle-binary-expr">
            <xsl:with-param name="operator"> /= </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="fx:remainder-assignment">
        <xsl:call-template name="handle-binary-expr">
            <xsl:with-param name="operator"> %= </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="fx:plus-assignment">
        <xsl:call-template name="handle-binary-expr">
            <xsl:with-param name="operator"> += </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="fx:minus-assignment">
        <xsl:call-template name="handle-binary-expr">
            <xsl:with-param name="operator"> -= </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="fx:left-shift-assignment">
        <xsl:call-template name="handle-binary-expr">
            <xsl:with-param name="operator"> &lt;&lt;= </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="fx:right-shift-assignment">
        <xsl:call-template name="handle-binary-expr">
            <xsl:with-param name="operator"> &gt;&gt;= </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="fx:unsigned-right-shift-assignment">
        <xsl:call-template name="handle-binary-expr">
            <xsl:with-param name="operator"> &gt;&gt;&gt;= </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="fx:and-assignment">
        <xsl:call-template name="handle-binary-expr">
            <xsl:with-param name="operator"> &amp;= </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="fx:xor-assignment">
        <xsl:call-template name="handle-binary-expr">
            <xsl:with-param name="operator"> ^= </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="fx:or-assignment">
        <xsl:call-template name="handle-binary-expr">
            <xsl:with-param name="operator"> |= </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <!-- unary operators -->
    <xsl:template match="fx:sizeof">
        <xsl:apply-templates/>
    </xsl:template>
    
    <xsl:template name="handle-unary-expr">
        <xsl:param name="operator"/>
        <xsl:apply-templates select="*[1]"/>
    </xsl:template>
    
    <xsl:template match="fx:postfix-increment">
        <xsl:apply-templates select="*[1]"/>
    </xsl:template>
    
    <xsl:template match="fx:prefix-increment">
        <xsl:call-template name="handle-unary-expr">
            <xsl:with-param name="operator">++</xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="fx:postfix-decrement">
        <xsl:apply-templates select="*[1]"/>
    </xsl:template>
    
    <xsl:template match="fx:prefix-decrement">
        <xsl:call-template name="handle-unary-expr">
            <xsl:with-param name="operator">--</xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="fx:unary-plus">
        <xsl:call-template name="handle-unary-expr">
            <xsl:with-param name="operator">+</xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="fx:unary-minus">
        <xsl:call-template name="handle-unary-expr">
            <xsl:with-param name="operator">-</xsl:with-param>
        </xsl:call-template>
    </xsl:template>

    <xsl:template match="fx:logical-complement">
        <xsl:call-template name="handle-unary-expr">
            <xsl:with-param name="operator">not </xsl:with-param>
        </xsl:call-template>
    </xsl:template>

    <!-- binary operators -->
    <xsl:template match="fx:multiply">
        <xsl:call-template name="handle-binary-expr">
            <xsl:with-param name="operator"> * </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="fx:divide">
        <xsl:call-template name="handle-binary-expr">
            <xsl:with-param name="operator"> / </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="fx:remainder">
        <xsl:call-template name="handle-binary-expr">
            <xsl:with-param name="operator"> mod </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="fx:plus">
        <xsl:call-template name="handle-binary-expr">
            <xsl:with-param name="operator"> + </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="fx:minus">
        <xsl:call-template name="handle-binary-expr">
            <xsl:with-param name="operator"> - </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="fx:left-shift">
        <xsl:call-template name="handle-binary-expr">
            <xsl:with-param name="operator"> &lt;&lt; </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="fx:right-shift">
        <xsl:call-template name="handle-binary-expr">
            <xsl:with-param name="operator"> &gt;&gt; </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="fx:unsigned-right-shift">
        <xsl:call-template name="handle-binary-expr">
            <xsl:with-param name="operator"> &gt;&gt;&gt; </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="fx:less-than">
        <xsl:call-template name="handle-binary-expr">
            <xsl:with-param name="operator"> &lt; </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="fx:greater-than">
        <xsl:call-template name="handle-binary-expr">
            <xsl:with-param name="operator"> &gt; </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="fx:less-than-equal">
        <xsl:call-template name="handle-binary-expr">
            <xsl:with-param name="operator"> &lt;= </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="fx:greater-than-equal">
        <xsl:call-template name="handle-binary-expr">
            <xsl:with-param name="operator"> &gt;= </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="fx:equal-to">
        <xsl:call-template name="handle-binary-expr">
            <xsl:with-param name="operator"> == </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="fx:not-equal-to">
        <xsl:call-template name="handle-binary-expr">
            <xsl:with-param name="operator"> != </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="fx:and">
        <xsl:call-template name="handle-binary-expr">
            <xsl:with-param name="operator"> &amp; </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="fx:xor">
        <xsl:call-template name="handle-binary-expr">
            <xsl:with-param name="operator"> ^ </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="fx:or">
        <xsl:call-template name="handle-binary-expr">
            <xsl:with-param name="operator"> | </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="fx:conditional-and">
        <xsl:call-template name="handle-binary-expr">
            <xsl:with-param name="operator"> and </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="fx:conditional-or">
        <xsl:call-template name="handle-binary-expr">
            <xsl:with-param name="operator"> or </xsl:with-param>
        </xsl:call-template>
    </xsl:template>

    <xsl:template match="fx:cast">
        <xsl:apply-templates select="fx:expr/*"/>
        <xsl:apply-templates select="fx:type/*" mode="no-colon"/>
    </xsl:template>
    
    <xsl:template match="fx:instanceof">
        <xsl:apply-templates select="fx:expr/*"/>
        <xsl:apply-templates select="fx:type/*"/>
    </xsl:template>
    
    <xsl:template match="fx:select">
        <xsl:apply-templates select="fx:expr/*"/>
        <xsl:apply-templates select="fx:member/*"/>
    </xsl:template>
    
    <xsl:template match="fx:ident"/>
    
    <!-- literals -->
    <xsl:template match="fx:int-literal"/>
    <xsl:template match="fx:long-literal"/>
    <xsl:template match="fx:float-literal"/>
    <xsl:template match="fx:double-literal"/>
    <xsl:template match="fx:true"/>
    <xsl:template match="fx:false"/>
    <xsl:template match="fx:string-literal"/>
    <xsl:template match="fx:null"/>
    
    <!-- modifiers -->
    <xsl:template match="fx:modifiers"/>
    
    <xsl:template name="handle-list">
        <xsl:param name="parent"/>
        <xsl:for-each select="$parent/*">
            <xsl:apply-templates select="."/>
        </xsl:for-each>
    </xsl:template>
    
    <xsl:template match="fx:value">
        <xsl:apply-templates/>
    </xsl:template>
    
    <xsl:template match="fx:block">
        <xsl:call-template name="handle-list">
            <xsl:with-param name="parent" select="fx:stmts"/>
        </xsl:call-template>
        <xsl:apply-templates select="fx:value"/>
    </xsl:template>
    
    <!-- importing stylesheet may override these -->
    <xsl:template name="class-body-begin"/>
    <xsl:template name="class-body-end"/>
    
    <xsl:template name="handle-class-body">
        <xsl:call-template name="class-body-begin"/>
        <xsl:call-template name="handle-list">
            <xsl:with-param name="parent" select="fx:members"/>
        </xsl:call-template>
        <xsl:call-template name="class-body-end"/>
    </xsl:template>
    
    <xsl:template match="fx:class">
        <xsl:apply-templates select="fx:modifiers"/>
        
        <!-- class body -->
        <xsl:call-template name="handle-class-body"/>
    </xsl:template>
    
    <xsl:template match="fx:for">
        <xsl:for-each select="fx:in/*">
            <xsl:apply-templates select="fx:var"/>
            <xsl:apply-templates select="fx:seq/*"/>
            <xsl:if test="fx:where">
                <xsl:apply-templates select="fx:where/*"/>
            </xsl:if>
        </xsl:for-each>
        <xsl:apply-templates select="fx:body/*"/>
    </xsl:template>
    
    <xsl:template match="fx:indexof">
        <xsl:apply-templates/>
    </xsl:template>
    
    <xsl:template match="fx:init">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="fx:postinit">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="fx:new">
        <xsl:apply-templates select="fx:class/*"/>
        <xsl:call-template name="handle-list">
            <xsl:with-param name="parent" select="fx:args"/>
        </xsl:call-template>
    </xsl:template>
            
    <xsl:template name="object-literal-begin"/>
    <xsl:template name="object-literal-end"/>
    <xsl:template match="fx:object-literal">
        <xsl:apply-templates select="fx:class/*"/>
        <xsl:call-template name="object-literal-begin"/>
        <xsl:for-each select="fx:defs/*">
            <xsl:apply-templates select="."/>
        </xsl:for-each>
        <xsl:call-template name="object-literal-end"/>
    </xsl:template>
    
    <xsl:template match="fx:object-literal-init">
        <xsl:apply-templates select="fx:bind-status"/>
        <xsl:apply-templates select="fx:expr/*"/>
    </xsl:template>
   
   <xsl:template match="fx:override-var">
        <xsl:apply-templates select="fx:expr/*"/>
        <xsl:apply-templates select="fx:on-replace"/>
        <xsl:apply-templates select="fx:on-invalidate"/>
    </xsl:template>

    <xsl:template name="handle-on-replace-clause">
        <xsl:apply-templates select="fx:old-value/*"/>
        <xsl:apply-templates select="fx:first-index/*"/>
        <xsl:apply-templates select="fx:last-index/*"/>
        <xsl:apply-templates select="fx:new-elements/*"/>
        <xsl:apply-templates select="fx:block"/>
    </xsl:template>

    <xsl:template match="fx:on-replace">
        <xsl:call-template name="handle-on-replace-clause"/>
    </xsl:template>

    <xsl:template match="fx:on-invalidate">
        <xsl:call-template name="handle-on-replace-clause"/>
    </xsl:template>

    <!-- functions -->
    <xsl:template name="function-body-begin"/>
    <xsl:template name="function-body-end"/>
    <xsl:template name="handle-function-body">
        <xsl:apply-templates select="fx:params/*"/>
        <xsl:apply-templates select="fx:return-type/*"/> 
        <xsl:choose>
            <xsl:when test="fx:block">
                <xsl:call-template name="function-body-begin"/>
                <xsl:call-template name="handle-list">
                    <xsl:with-param name="parent" select="fx:block/fx:stmts"/>
                </xsl:call-template>
                <xsl:apply-templates select="fx:block/fx:value"/>
                <xsl:call-template name="function-body-end"/>
            </xsl:when>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template match="fx:function">
        <xsl:variable name="name" select="fx:name"/>
        <xsl:apply-templates select="fx:modifiers"/>
        <xsl:call-template name="handle-function-body"/>
    </xsl:template>
    
    <xsl:template match="fx:anon-function">
        <xsl:call-template name="handle-function-body"/>
    </xsl:template>
    
    <xsl:template match="fx:seq-delete">
        <xsl:apply-templates select="fx:elem/*"/>
        <xsl:apply-templates select="fx:seq/*"/>
    </xsl:template>
    
    <xsl:template match="fx:seq-empty"/>
    
    <xsl:template match="fx:seq-explicit">
        <xsl:call-template name="handle-list">
            <xsl:with-param name="parent" select="fx:items"/>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="fx:seq-indexed">
        <xsl:apply-templates select="fx:seq/*"/>
        <xsl:apply-templates select="fx:index/*"/>
    </xsl:template>
    
    <xsl:template match="fx:seq-slice">
        <xsl:apply-templates select="fx:seq/*"/>
        <xsl:apply-templates select="fx:first/*"/>
        <xsl:apply-templates select="fx:last/*"/>
    </xsl:template>
    
    <xsl:template match="fx:seq-insert">
        <xsl:apply-templates select="fx:elem/*"/>
        <xsl:apply-templates select="fx:seq/*"/>
    </xsl:template>
    
    <xsl:template match="fx:seq-range">
        <xsl:apply-templates select="fx:lower/*"/>
        <xsl:apply-templates select="fx:upper/*"/>
        <xsl:apply-templates select="fx:step/*"/>
    </xsl:template>

    <xsl:template match="fx:invalidate">
        <xsl:apply-templates select="fx:var/*"/>
    </xsl:template>
    
    <xsl:template match="fx:string-expr">
        <xsl:for-each select="fx:part">
            <xsl:choose>
                <xsl:when test="fx:string-literal">
                    <xsl:apply-templates/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:apply-templates select="fx:expr/*"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:for-each>
    </xsl:template>
    
    <xsl:template match="fx:time-literal"/>

    <xsl:template match="fx:interpolate-value">
       <xsl:if test="fx:attribute">
           <xsl:for-each select="fx:attribute/*">
               <xsl:apply-templates select="."/>
           </xsl:for-each>
       </xsl:if>
       <xsl:for-each select="fx:value/*">
           <xsl:apply-templates select="."/>
       </xsl:for-each>
       <xsl:if test="fx:interpolation">
           <xsl:apply-templates select="fx:interpolation/*"/>
       </xsl:if>
    </xsl:template>
    
    <xsl:template match="fx:keyframe-literal">
        <xsl:apply-templates select="fx:start-dur/*"/>
        <xsl:for-each select="fx:interpolation-values/*">
            <xsl:apply-templates select="."/>
        </xsl:for-each>
        <xsl:if test="fx:trigger">
            <xsl:apply-templates select="./*"/>
        </xsl:if> 
    </xsl:template>

</xsl:transform>
