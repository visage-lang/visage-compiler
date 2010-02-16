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
    This stylesheet converts XML representation of JavaFX AST (abstract
    syntax tree) to JavaFX source code form.
-->
<xsl:transform version="1.0"
               xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
               xmlns:c="http://xml.apache.org/xalan/java/com.sun.tools.javafx.tree.xml.Compiler"
               xmlns:fx="http://openjfx.java.sun.com">
    
    <xsl:strip-space elements="*"/>
    <xsl:output method="text"/>
    
    <!-- prints 4 spaces for each 'tab' -->
    <xsl:template name="print-spaces">
        <xsl:param name="tabs"/>
        <xsl:text>    </xsl:text>
        <xsl:if test="not($tabs = 1)">
            <xsl:call-template name="print-spaces">
                <xsl:with-param name="tabs">
                    <xsl:value-of select="$tabs - 1"/>
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>
    </xsl:template>
    
    <!-- initialize (current) number of tabs for alignment -->
    <xsl:template name="init-tabs">
        <xsl:value-of select="c:putGlobal('tabs', 0)"/>
    </xsl:template>
    
    <!-- increment number of tabs -->
    <xsl:template name="indent">
        <!-- hack to have only side-effect and ignore the result -->
        <xsl:if test="not(c:putGlobal('tabs', c:getGlobal('tabs') + 1))"/>
    </xsl:template>
    
    <!-- decrement number of tabs -->
    <xsl:template name="undent">
        <!-- hack to have only side-effect and ignore the result -->
        <xsl:if test="not(c:putGlobal('tabs', c:getGlobal('tabs') - 1))"/>
    </xsl:template>
    
    <!-- align prints spaces as per current tab count -->
    <xsl:template name="align">
        <xsl:if test="not(c:getGlobal('tabs') = 0)">
            <xsl:call-template name="print-spaces">
                <xsl:with-param name="tabs" select="c:getGlobal('tabs')"/>
            </xsl:call-template>
        </xsl:if>
    </xsl:template>
    
    <!-- print a new line in output -->
    <xsl:template name="println">
        <xsl:text>&#xa;</xsl:text>
    </xsl:template>
    
    <!-- print a list of items separated by given separator -->
    <xsl:template name="print-separator-list">
        <xsl:param name="parent"/>
        <xsl:param name="sep"/>
        <xsl:for-each select="$parent/*">
            <xsl:apply-templates select="."/>
            <xsl:if test="not(position()=last())">
                <xsl:value-of select="$sep"/>
            </xsl:if>
        </xsl:for-each>
    </xsl:template>
    
    <!-- print a comma separated list of items -->
    <xsl:template name="print-comma-list">
        <xsl:param name="parent"/>
        <xsl:call-template name="print-separator-list">
            <xsl:with-param name="parent" select="$parent"/>
            <xsl:with-param name="sep">, </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="@*|node()"/>

    <xsl:template match="/">
      <xsl:apply-templates select="fx:javafx-script"/>
    </xsl:template>

    <xsl:template match="fx:javafx-script">
        <xsl:call-template name="init-tabs"/>
        <xsl:apply-templates select="fx:file"/>
        <xsl:apply-templates select="fx:package"/>
        <xsl:apply-templates select="fx:defs"/>
    </xsl:template>

    <xsl:template match="fx:file">
        <xsl:text>// compiled from </xsl:text>
        <xsl:value-of select="."/>
        <xsl:call-template name="println"/>
    </xsl:template>
    
    <xsl:template match="fx:package">
        <xsl:text>package </xsl:text> 
        <xsl:apply-templates/>
        <xsl:text>;</xsl:text>
        <xsl:call-template name="println"/>
        <xsl:call-template name="println"/>
    </xsl:template>
   
    <xsl:template match="fx:defs">
        <xsl:for-each select="*">
            <xsl:apply-templates select="."/>
            <xsl:call-template name="println"/>
        </xsl:for-each>
    </xsl:template>
    
    <xsl:template match="fx:import">
        <xsl:text>import </xsl:text>
        <xsl:apply-templates/>
        <xsl:text>;</xsl:text>
    </xsl:template>
    
    <xsl:template match="fx:bind-status">
        <xsl:choose>
            <xsl:when test=". = 'bind'">
                <xsl:text> bind </xsl:text>
            </xsl:when>
            <xsl:when test=". = 'bind-with-inverse'">
                <xsl:text> bind </xsl:text>
            </xsl:when>
            <xsl:when test=". = 'bind-lazy'">
                <xsl:text> bind lazy </xsl:text>
            </xsl:when> 
            <xsl:when test=". = 'bind-lazy-with-inverse'">
                <xsl:text> bind lazy </xsl:text>
            </xsl:when>
            <xsl:when test=". = 'unbound-lazy'">
                <xsl:text> lazy </xsl:text>
            </xsl:when>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template match="fx:bind-status" mode="suffix">
        <xsl:choose>
            <xsl:when test=". = 'bind-with-inverse'">
                <xsl:text> with inverse</xsl:text>
            </xsl:when>
            <xsl:when test=". = 'bind-lazy-with-inverse'">
                <xsl:text> with inverse</xsl:text>
            </xsl:when>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template name="print-param">
        <!-- name of the param -->
        <xsl:if test="fx:name">
            <xsl:value-of select="fx:name"/>
        </xsl:if>
        
        <!-- print type -->
        <xsl:if test="fx:type">
            <xsl:apply-templates select="fx:type/*"/>
        </xsl:if>
    </xsl:template>
    
    <xsl:template name="print-var" >
        <xsl:apply-templates select="fx:modifiers"/>
        <xsl:choose>
            <xsl:when test="name(.) = 'fx:var'">
                <xsl:text>var </xsl:text>
            </xsl:when>
            <xsl:when test="name(.) = 'fx:def'">
                <xsl:text>def </xsl:text>
            </xsl:when>
        </xsl:choose>
            
        
        <!-- name of the variable/attribute -->
        <xsl:if test="fx:name">
            <xsl:value-of select="fx:name"/>
        </xsl:if>
        
        <!-- print type -->
        <xsl:if test="fx:type">
            <xsl:apply-templates select="fx:type/*"/>
        </xsl:if>
        
        <!-- optional initializer -->
        <xsl:if test="fx:init-value">
            <xsl:text> = </xsl:text>
            <xsl:apply-templates select="fx:bind-status"/>
            <xsl:apply-templates select="fx:init-value/*"/>
            <xsl:apply-templates select="fx:bind-status" mode="suffix"/>
        </xsl:if>
        
        <xsl:apply-templates select="fx:on-replace"/>
        <xsl:apply-templates select="fx:on-invalidate"/>
    </xsl:template>
    
    <xsl:template match="fx:var">
        <xsl:call-template name="print-var"/>
        <xsl:text>;</xsl:text>
    </xsl:template>
    
    <xsl:template match="fx:def">
        <xsl:call-template name="print-var"/>
        <xsl:text>;</xsl:text>
    </xsl:template>
    
    <xsl:template match="fx:empty">
        <xsl:text>;</xsl:text>
    </xsl:template>
    
    <xsl:template match="fx:while">
        <xsl:text>while (</xsl:text>
        <xsl:apply-templates select="fx:test/*"/>
        <xsl:text>) </xsl:text>
        <xsl:apply-templates select="fx:stmt/*"/>
    </xsl:template>
   
    <xsl:template match="fx:try">
        <xsl:text>try </xsl:text>
        <xsl:apply-templates select="fx:block"/>
        <xsl:apply-templates select="fx:catches/fx:catch"/>
        <xsl:if test="fx:finally">
            <xsl:text> finally </xsl:text>
            <xsl:apply-templates select="fx:finally/fx:block"/>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="fx:catch">
        <xsl:text> catch (</xsl:text>
        <xsl:for-each select="fx:var">
            <xsl:call-template name="print-param"/>
        </xsl:for-each>
        <xsl:text>) </xsl:text>
        <xsl:apply-templates select="fx:block"/>
    </xsl:template>
    
    <xsl:template match="fx:if">
        <xsl:text>if (</xsl:text>
        <xsl:apply-templates select="fx:test/*"/>
        <xsl:text>) </xsl:text>
        <xsl:apply-templates select="fx:then/*"/>
        <xsl:if test="fx:else">
            <xsl:text> else </xsl:text>
            <xsl:apply-templates select="fx:else/*"/>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="fx:break">
        <xsl:text>break</xsl:text>
        <xsl:if test="fx:label">
            <xsl:text> </xsl:text>
            <xsl:value-of select="fx:label"/>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="fx:continue">
        <xsl:text>continue</xsl:text>
        <xsl:if test="fx:label">
            <xsl:text> </xsl:text>
            <xsl:value-of select="fx:label"/>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="fx:return">
        <xsl:text>return</xsl:text>
        <xsl:if test="*">
            <xsl:text> </xsl:text>
            <xsl:apply-templates select="*"/>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="fx:throw">
        <xsl:text>throw</xsl:text>
        <xsl:text> </xsl:text>
        <xsl:apply-templates select="*"/>
    </xsl:template>
    
    <xsl:template match="fx:invoke">
        <xsl:apply-templates select="fx:method/*"/>
        <xsl:text>(</xsl:text>
        <xsl:call-template name="print-comma-list">
            <xsl:with-param name="parent" select="fx:args"/>
        </xsl:call-template>
        <xsl:text>)</xsl:text>
    </xsl:template>
    
    <xsl:template match="fx:paren">
        <xsl:text>(</xsl:text>
        <xsl:apply-templates select="*[1]"/>
        <xsl:text>)</xsl:text>
    </xsl:template>
    
    <xsl:template name="print-binary-expr">
        <xsl:param name="operator"/>
        <xsl:apply-templates select="fx:left/*"/>
        <xsl:value-of select="$operator"/>
        <xsl:apply-templates select="fx:right/*"/>
    </xsl:template>
    
    <xsl:template match="fx:assignment">
        <xsl:call-template name="print-binary-expr">
            <xsl:with-param name="operator"> = </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <!-- compound assignments -->
    <xsl:template match="fx:multiply-assignment">
        <xsl:call-template name="print-binary-expr">
            <xsl:with-param name="operator"> *= </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="fx:divide-assignment">
        <xsl:call-template name="print-binary-expr">
            <xsl:with-param name="operator"> /= </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="fx:remainder-assignment">
        <xsl:call-template name="print-binary-expr">
            <xsl:with-param name="operator"> %= </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="fx:plus-assignment">
        <xsl:call-template name="print-binary-expr">
            <xsl:with-param name="operator"> += </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="fx:minus-assignment">
        <xsl:call-template name="print-binary-expr">
            <xsl:with-param name="operator"> -= </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="fx:left-shift-assignment">
        <xsl:call-template name="print-binary-expr">
            <xsl:with-param name="operator"> &lt;&lt;= </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="fx:right-shift-assignment">
        <xsl:call-template name="print-binary-expr">
            <xsl:with-param name="operator"> &gt;&gt;= </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="fx:unsigned-right-shift-assignment">
        <xsl:call-template name="print-binary-expr">
            <xsl:with-param name="operator"> &gt;&gt;&gt;= </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="fx:and-assignment">
        <xsl:call-template name="print-binary-expr">
            <xsl:with-param name="operator"> &amp;= </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="fx:xor-assignment">
        <xsl:call-template name="print-binary-expr">
            <xsl:with-param name="operator"> ^= </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="fx:or-assignment">
        <xsl:call-template name="print-binary-expr">
            <xsl:with-param name="operator"> |= </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <!-- unary operators -->
    <xsl:template match="fx:sizeof">
        <xsl:text>sizeof </xsl:text>
        <xsl:apply-templates/>
        <xsl:text></xsl:text>
    </xsl:template>
    
    <xsl:template name="print-unary-expr">
        <xsl:param name="operator"/>
        <xsl:value-of select="$operator"/>
        <xsl:apply-templates select="*[1]"/>
    </xsl:template>
    
    <xsl:template match="fx:postfix-increment">
        <xsl:apply-templates select="*[1]"/>
        <xsl:text>++</xsl:text>
    </xsl:template>
    
    <xsl:template match="fx:prefix-increment">
        <xsl:call-template name="print-unary-expr">
            <xsl:with-param name="operator">++</xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="fx:postfix-decrement">
        <xsl:apply-templates select="*[1]"/>
        <xsl:text>--</xsl:text>
    </xsl:template>
    
    <xsl:template match="fx:prefix-decrement">
        <xsl:call-template name="print-unary-expr">
            <xsl:with-param name="operator">--</xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="fx:unary-plus">
        <xsl:call-template name="print-unary-expr">
            <xsl:with-param name="operator">+</xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="fx:unary-minus">
        <xsl:call-template name="print-unary-expr">
            <xsl:with-param name="operator">-</xsl:with-param>
        </xsl:call-template>
    </xsl:template>

    <xsl:template match="fx:logical-complement">
        <xsl:call-template name="print-unary-expr">
            <xsl:with-param name="operator">not </xsl:with-param>
        </xsl:call-template>
    </xsl:template>

    <!-- binary operators -->
    <xsl:template match="fx:multiply">
        <xsl:call-template name="print-binary-expr">
            <xsl:with-param name="operator"> * </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="fx:divide">
        <xsl:call-template name="print-binary-expr">
            <xsl:with-param name="operator"> / </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="fx:remainder">
        <xsl:call-template name="print-binary-expr">
            <xsl:with-param name="operator"> mod </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="fx:plus">
        <xsl:call-template name="print-binary-expr">
            <xsl:with-param name="operator"> + </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="fx:minus">
        <xsl:call-template name="print-binary-expr">
            <xsl:with-param name="operator"> - </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="fx:left-shift">
        <xsl:call-template name="print-binary-expr">
            <xsl:with-param name="operator"> &lt;&lt; </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="fx:right-shift">
        <xsl:call-template name="print-binary-expr">
            <xsl:with-param name="operator"> &gt;&gt; </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="fx:unsigned-right-shift">
        <xsl:call-template name="print-binary-expr">
            <xsl:with-param name="operator"> &gt;&gt;&gt; </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="fx:less-than">
        <xsl:call-template name="print-binary-expr">
            <xsl:with-param name="operator"> &lt; </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="fx:greater-than">
        <xsl:call-template name="print-binary-expr">
            <xsl:with-param name="operator"> &gt; </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="fx:less-than-equal">
        <xsl:call-template name="print-binary-expr">
            <xsl:with-param name="operator"> &lt;= </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="fx:greater-than-equal">
        <xsl:call-template name="print-binary-expr">
            <xsl:with-param name="operator"> &gt;= </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="fx:equal-to">
        <xsl:call-template name="print-binary-expr">
            <xsl:with-param name="operator"> == </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="fx:not-equal-to">
        <xsl:call-template name="print-binary-expr">
            <xsl:with-param name="operator"> != </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="fx:and">
        <xsl:call-template name="print-binary-expr">
            <xsl:with-param name="operator"> &amp; </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="fx:xor">
        <xsl:call-template name="print-binary-expr">
            <xsl:with-param name="operator"> ^ </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="fx:or">
        <xsl:call-template name="print-binary-expr">
            <xsl:with-param name="operator"> | </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="fx:conditional-and">
        <xsl:call-template name="print-binary-expr">
            <xsl:with-param name="operator"> and </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="fx:conditional-or">
        <xsl:call-template name="print-binary-expr">
            <xsl:with-param name="operator"> or </xsl:with-param>
        </xsl:call-template>
    </xsl:template>

    <xsl:template match="fx:cast">
        <xsl:text>(</xsl:text>
        <xsl:apply-templates select="fx:expr/*"/>
        <xsl:text> as </xsl:text>
        <xsl:apply-templates select="fx:type/*" mode="no-colon"/>
        <xsl:text>)</xsl:text>
    </xsl:template>
    
    <xsl:template match="fx:instanceof">
        <xsl:apply-templates select="fx:expr/*"/>
        <xsl:text> instanceof </xsl:text>
        <xsl:apply-templates select="fx:type/*"/>
    </xsl:template>
    
    <xsl:template match="fx:select">
        <xsl:apply-templates select="fx:expr/*"/>
        <xsl:text>.</xsl:text>
        <xsl:value-of select="fx:member"/>
    </xsl:template>
    
    <xsl:template match="fx:ident">
        <xsl:value-of select="."/>
    </xsl:template>
    
    <!-- literals -->
    
    <xsl:template match="fx:int-literal">
        <xsl:value-of select="."/>
    </xsl:template>

    <xsl:template match="fx:long-literal">
        <xsl:value-of select="."/>
    </xsl:template>
    
    <xsl:template match="fx:float-literal">
        <xsl:value-of select="."/>
    </xsl:template>
    
    <xsl:template match="fx:double-literal">
        <xsl:value-of select="."/>
    </xsl:template>
    
    <xsl:template match="fx:true">
        <xsl:text>true</xsl:text>
    </xsl:template>
    
    <xsl:template match="fx:false">
        <xsl:text>false</xsl:text>
    </xsl:template>
    
    <!-- set flag to tell if we have q:quoteChar function -->
    <xsl:variable name="quote-char-exists" select="function-available('c:quoteChar')"/>
    
    <!-- set flag to tell if we have q:quoteString function -->
    <xsl:variable name="quote-string-exists" select="function-available('c:quoteString')"/>
    
    <xsl:template name="print-quoted-string">
        <xsl:param name="str"/>
        <xsl:choose>
            <xsl:when test="$quote-string-exists">
                <xsl:value-of select="c:quoteString($str)"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:message>WARNING : c:quoteString() is not available!</xsl:message>
                <xsl:value-of select="$str"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template match="fx:string-literal">
        <xsl:text>"</xsl:text>
        <xsl:value-of select="."/>
        <!--
        <xsl:call-template name="print-quoted-string">
            <xsl:with-param name="str" select="."/>
        </xsl:call-template>
        -->
        <xsl:text>"</xsl:text>
    </xsl:template>
    
    <xsl:template match="fx:null">
        <xsl:text>null</xsl:text>
    </xsl:template>
    
    <!-- modifiers -->
    <xsl:template match="fx:modifiers">
        <xsl:for-each select="fx:li">
            <xsl:variable name="flag">
                <xsl:value-of select="."/>
            </xsl:variable>
            <xsl:choose>
            <!-- script-private is "default" access -->
            <xsl:when test="$flag = 'script-private'">
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$flag"/>
                <xsl:text> </xsl:text>
            </xsl:otherwise>
            </xsl:choose>
        </xsl:for-each>
    </xsl:template>
    
    <xsl:template name="print-stmts">
        <xsl:param name="parent"/>
        <xsl:param name="semicolon">true</xsl:param>
        <xsl:for-each select="$parent/*">
            <xsl:call-template name="align"/>
            <xsl:apply-templates select="."/>
            <xsl:if test="$semicolon">
                <xsl:text>;</xsl:text>
            </xsl:if>
            <xsl:call-template name="println"/>
        </xsl:for-each>
    </xsl:template>
    
    <xsl:template match="fx:value">
        <xsl:call-template name="align"/>
        <xsl:apply-templates/>
        <xsl:call-template name="println"/>
    </xsl:template>
    
    <!-- treat file-level blocks as special, don't include braces around -->
    <xsl:template match="fx:javafx-script/fx:defs/fx:block">
        <xsl:call-template name="print-stmts">
        <xsl:with-param name="parent" select="fx:stmts"/>
        </xsl:call-template>
        <xsl:apply-templates select="fx:value"/>
    </xsl:template>

    <xsl:template match="fx:block">
        <xsl:text>{</xsl:text>
        <xsl:call-template name="println"/>
        <xsl:call-template name="indent"/>
        <xsl:call-template name="print-stmts">
            <xsl:with-param name="parent" select="fx:stmts"/>
        </xsl:call-template>
        <xsl:apply-templates select="fx:value"/>
        <xsl:call-template name="undent"/>
        <xsl:call-template name="align"/>
        <xsl:text>}</xsl:text>
    </xsl:template>
    
    <xsl:template name="print-super-types">
        <xsl:if test="fx:extends">
            <xsl:text> extends </xsl:text>
            <xsl:call-template name="print-comma-list">
                <xsl:with-param name="parent" select="fx:extends"/>
            </xsl:call-template>
            <xsl:text> </xsl:text>
        </xsl:if>
    </xsl:template>
    
    <!-- importing stylesheet may override these -->
    <xsl:template name="class-body-begin"/>
    <xsl:template name="class-body-end"/>
    
    <xsl:template name="print-class-body">
        <xsl:text>{</xsl:text>
        <xsl:call-template name="class-body-begin"/>
        
        <xsl:call-template name="println"/>
        <xsl:call-template name="indent"/>
        <xsl:call-template name="print-stmts">
            <xsl:with-param name="parent" select="fx:members"/>
            <xsl:with-param name="semicolon" select="false"/>
        </xsl:call-template>
        <xsl:call-template name="undent"/>
        <xsl:call-template name="align"/>
        
        <xsl:call-template name="class-body-end"/>
        <xsl:text>}</xsl:text>
    </xsl:template>
    
    <xsl:template match="fx:class">
        <xsl:call-template name="println"/>
        <xsl:call-template name="align"/>
        <xsl:apply-templates select="fx:modifiers"/>
        <xsl:text>class </xsl:text>
        <xsl:value-of select="fx:name"/>
        
        <xsl:call-template name="print-super-types"/>
        
        <!-- class body -->
        <xsl:text> </xsl:text>
        <xsl:call-template name="print-class-body"/>
    </xsl:template>
    
    <xsl:template match="fx:for">
        <xsl:text>for (</xsl:text>
        <xsl:for-each select="fx:in/*">
            <xsl:if test="not(position() = 1)">
                <xsl:text>, </xsl:text>
            </xsl:if>
            <xsl:value-of select="fx:var/fx:name"/>
            <xsl:apply-templates select="fx:var/fx:type/*"/>
            <xsl:text> in </xsl:text>
            <xsl:apply-templates select="fx:seq/*"/>
            <xsl:if test="fx:where">
                <xsl:text> where </xsl:text>
                <xsl:apply-templates select="fx:where/*"/>
            </xsl:if>
        </xsl:for-each>
        <xsl:text>) </xsl:text>
        <xsl:apply-templates select="fx:body/*"/>
    </xsl:template>
    
    <xsl:template match="fx:indexof">
        <xsl:text>indexof </xsl:text>
        <xsl:apply-templates/>
    </xsl:template>
    
    <xsl:template match="fx:init">
        <xsl:call-template name="println"/>
        <xsl:call-template name="align"/>
        <xsl:text>init </xsl:text>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="fx:postinit">
        <xsl:call-template name="println"/>
        <xsl:call-template name="align"/>
        <xsl:text>postinit </xsl:text>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="fx:new">
        <xsl:text>new </xsl:text>
        <xsl:apply-templates select="fx:class/*"/>
        <xsl:text>(</xsl:text>
            <xsl:call-template name="print-comma-list">
                <xsl:with-param name="parent" select="fx:args"/>
            </xsl:call-template>
        <xsl:text>)</xsl:text>
    </xsl:template>
            
    <xsl:template name="object-literal-begin"/>
    <xsl:template name="object-literal-end"/>
    <xsl:template match="fx:object-literal">
        <xsl:apply-templates select="fx:class/*"/>
        <xsl:text> {</xsl:text>
        <xsl:call-template name="object-literal-begin"/>
        <xsl:call-template name="indent"/>
        <xsl:for-each select="fx:defs/*">
            <xsl:call-template name="println"/>
            <xsl:call-template name="align"/>
            <xsl:apply-templates select="."/>
        </xsl:for-each>
        <xsl:call-template name="undent"/>
        <xsl:call-template name="println"/>
        <xsl:call-template name="align"/>
        <xsl:call-template name="object-literal-end"/>
        <xsl:text>}</xsl:text>
        <xsl:call-template name="println"/>
        <xsl:call-template name="align"/>
    </xsl:template>
    
    <xsl:template match="fx:object-literal-init">
        <xsl:value-of select="fx:name"/>
        <xsl:text> : </xsl:text>
        <xsl:apply-templates select="fx:bind-status"/>
        <xsl:apply-templates select="fx:expr/*"/>
        <xsl:apply-templates select="fx:bind-status" mode="suffix"/>
    </xsl:template>
   
   <xsl:template match="fx:override-var">
        <xsl:text>override var </xsl:text>
        <xsl:value-of select="./fx:name"/>
        <xsl:apply-templates select="fx:expr/*"/>
        <xsl:text> </xsl:text>
        <xsl:apply-templates select="fx:on-replace"/>
        <xsl:apply-templates select="fx:on-invalidate"/>
    </xsl:template>

    <xsl:template name="handle-on-replace-clause">
        <xsl:if test="fx:old-value">
            <xsl:text>  </xsl:text>
            <xsl:value-of select="fx:old-value/fx:var/fx:name"/>
        </xsl:if>
        <xsl:if test="fx:first-index">
            <xsl:text> [ </xsl:text>
            <xsl:value-of select="fx:first-index/fx:var/fx:name"/>
            <xsl:if test="fx:last-index">
                <xsl:text> .. </xsl:text>
                <xsl:value-of select="fx:last-index/fx:var/fx:name"/>
            </xsl:if>
            <xsl:text> ] </xsl:text>
        </xsl:if>
        <xsl:if test="fx:new-elements">
            <xsl:value-of select="fx:new-elements/fx:var/fx:name"/>
        </xsl:if>
        <xsl:apply-templates select="fx:block"/>
    </xsl:template>

    <xsl:template match="fx:on-replace">
        <xsl:text> on replace </xsl:text>
        <xsl:call-template name="handle-on-replace-clause"/>
    </xsl:template>

    <xsl:template match="fx:on-invalidate">
        <xsl:text> on invalidate </xsl:text>
        <xsl:call-template name="handle-on-replace-clause"/>
    </xsl:template>

    <!-- functions -->
    <xsl:template name="function-body-begin"/>
    <xsl:template name="function-body-end"/>
    <xsl:template name="print-function-body">
        <!-- print the comma separated parameters -->
        <xsl:text>(</xsl:text>
        <xsl:for-each select="fx:params/*">
            <xsl:call-template name="print-param"/>
            <xsl:if test="not(position()=last())">
                <xsl:text>, </xsl:text>
            </xsl:if>
        </xsl:for-each>
        <xsl:text>)</xsl:text>
        
        <!-- return type, if any -->
        <xsl:if test="fx:return-type">
            <xsl:apply-templates select="fx:return-type/*"/> 
        </xsl:if>
        
        <xsl:choose>
            <!-- print the code block, if any -->
            <xsl:when test="fx:block">
                <xsl:text> </xsl:text>
                <xsl:text>{</xsl:text>
                <xsl:call-template name="function-body-begin"/>
                <xsl:call-template name="println"/>
                <xsl:call-template name="indent"/>
                <xsl:call-template name="print-stmts">
                    <xsl:with-param name="parent" select="fx:block/fx:stmts"/>
                </xsl:call-template>
                <xsl:apply-templates select="fx:block/fx:value"/>
                <xsl:call-template name="undent"/>
                <xsl:call-template name="align"/>
                <xsl:call-template name="function-body-end"/>
                <xsl:text>}</xsl:text>
            </xsl:when>
            <!-- abstract/native methods have no body -->
            <xsl:otherwise>
                <xsl:text>;</xsl:text>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template match="fx:function">
        <xsl:variable name="name" select="fx:name"/>
        <xsl:call-template name="println"/>
        <xsl:call-template name="align"/>
            
        <xsl:apply-templates select="fx:modifiers"/>
        
        <xsl:text>function </xsl:text>
        <!-- method name -->
        <xsl:value-of select="$name"/>
            
        <xsl:call-template name="print-function-body"/>
    </xsl:template>
    
    <xsl:template match="fx:anon-function">
        <xsl:call-template name="println"/>
        <xsl:call-template name="align"/>
        
        <xsl:text>function </xsl:text>
        <xsl:call-template name="print-function-body"/>
    </xsl:template>
    
    <xsl:template match="fx:seq-delete">
        <xsl:text>delete </xsl:text>
        <xsl:if test="fx:elem">
            <xsl:apply-templates select="fx:elem/*"/>
            <xsl:text> from </xsl:text>
        </xsl:if>
        <xsl:apply-templates select="fx:seq/*"/>
    </xsl:template>
    
    <xsl:template match="fx:seq-empty">
        <xsl:text>[]</xsl:text>
    </xsl:template>
    
    <xsl:template match="fx:seq-explicit">
        <xsl:text>[</xsl:text>
        <xsl:call-template name="print-comma-list">
            <xsl:with-param name="parent" select="fx:items"/>
        </xsl:call-template>
        <xsl:text>]</xsl:text>
    </xsl:template>
    
    <xsl:template match="fx:seq-indexed">
        <xsl:apply-templates select="fx:seq/*"/>
        <xsl:text>[</xsl:text>
        <xsl:apply-templates select="fx:index/*"/>
        <xsl:text>]</xsl:text>
    </xsl:template>
    
    <xsl:template match="fx:seq-slice">
        <xsl:apply-templates select="fx:seq/*"/>
        <xsl:text>[</xsl:text>
        <xsl:apply-templates select="fx:first/*"/>
        <xsl:text>..</xsl:text>
        <xsl:if test="fx:slice-end-kind = 'exclusive'">
            <xsl:text>&lt;</xsl:text>
        </xsl:if>
        <xsl:apply-templates select="fx:last/*"/>
        <xsl:text>]</xsl:text>
    </xsl:template>
    
    <xsl:template match="fx:seq-insert">
        <xsl:text>insert </xsl:text>
        <xsl:apply-templates select="fx:elem/*"/>
        <xsl:text> into </xsl:text>
        <xsl:apply-templates select="fx:seq/*"/>
    </xsl:template>
    
    <xsl:template match="fx:seq-range">
        <xsl:text>[</xsl:text>
        <xsl:apply-templates select="fx:lower/*"/>
        <xsl:text>..</xsl:text>
        <xsl:if test="fx:exclusive = 'true'">
            <xsl:text> &lt;</xsl:text>
        </xsl:if>
        <xsl:apply-templates select="fx:upper/*"/>
        <xsl:if test="fx:step">
            <xsl:text> step </xsl:text>
            <xsl:apply-templates select="fx:step/*"/>
        </xsl:if>
        <xsl:text>]</xsl:text>
    </xsl:template>

    <xsl:template match="fx:invalidate">
        <xsl:text>invalidate </xsl:text>
        <xsl:apply-templates select="fx:var/*"/>
    </xsl:template>
    
    <xsl:template match="fx:string-expr">
        <xsl:text>"</xsl:text>
        <xsl:for-each select="fx:part">
            <xsl:choose>
                <xsl:when test="fx:string-literal">
                    <xsl:value-of select="."/>
                    <!--
                    <xsl:call-template name="print-quoted-string">
                        <xsl:with-param name="str" select="."/>
                    </xsl:call-template>
                    -->
                </xsl:when>
                <xsl:otherwise>
                    <xsl:text>{</xsl:text>
                    <xsl:if test="not(fx:format = '')">
                        <xsl:value-of select="fx:format"/>
                        <xsl:text> </xsl:text>
                    </xsl:if>
                    <xsl:apply-templates select="fx:expr/*"/>
                    <xsl:text>}</xsl:text>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:for-each>
        <xsl:text>"</xsl:text>
    </xsl:template>
    
    <!-- types -->
    <xsl:template match="fx:type-any">
        <xsl:text> : *</xsl:text>
        <xsl:apply-templates select="fx:cardinality"/>
    </xsl:template>
    
    <xsl:template match="fx:type-class">
        <xsl:text> : </xsl:text>
        <xsl:apply-templates select="fx:class/*" mode="no-colon"/>
        <xsl:apply-templates select="fx:cardinality"/>
    </xsl:template>
    
    <xsl:template match="fx:type-functional">
        <xsl:text> : function (</xsl:text>
        <xsl:for-each select="fx:params/*">
            <xsl:apply-templates select="."/>
            <xsl:if test="not(position()=last())">
                <xsl:text>, </xsl:text>
            </xsl:if>
        </xsl:for-each>
        <xsl:text>)</xsl:text>
        <xsl:apply-templates select="fx:return-type/*"/>
        <xsl:apply-templates select="fx:cardinality"/>
    </xsl:template>

    <xsl:template match="fx:type-array">
        <xsl:text> : nativearray of </xsl:text>
        <xsl:apply-templates select="*" mode="no-colon"/>
    </xsl:template>
    
    <xsl:template match="fx:type-any" mode="no-colon">
        <xsl:text> *</xsl:text>
        <xsl:apply-templates select="fx:cardinality"/>
    </xsl:template>
    
    <xsl:template match="fx:type-class" mode="no-colon">
        <xsl:text> </xsl:text>
        <xsl:apply-templates select="fx:class/*"/>
        <xsl:apply-templates select="fx:cardinality"/>
    </xsl:template>
    
    <xsl:template match="fx:type-functional" mode="no-colon">
        <xsl:text> function (</xsl:text>
        <xsl:for-each select="fx:params/*">
            <xsl:apply-templates select="."/>
            <xsl:if test="not(position()=last())">
                <xsl:text>, </xsl:text>
            </xsl:if>
        </xsl:for-each>
        <xsl:text>)</xsl:text>
        <xsl:apply-templates select="fx:return-type/*"/>
        <xsl:apply-templates select="fx:cardinality"/>
    </xsl:template>

    <xsl:template match="fx:type-array" mode="no-colon">
        <xsl:text> nativearray of</xsl:text>
        <xsl:apply-templates select="*" mode="no-colon"/>
    </xsl:template>

    <xsl:template match="fx:type-unknown">
        <xsl:apply-templates select="fx:cardinality"/>
    </xsl:template>
    
    <xsl:template match="fx:cardinality">
        <xsl:choose>
            <xsl:when test=". = 'singleton'"/>
            <xsl:when test=". = 'unknown'"/>
            <xsl:when test=". = 'any'">
                <xsl:text>[]</xsl:text>
            </xsl:when>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template match="fx:time-literal">
        <xsl:value-of select="."/>
        <xsl:text>ms</xsl:text>
    </xsl:template>

    <xsl:template match="fx:interpolate-value">
       <xsl:if test="fx:attribute">
           <xsl:for-each select="fx:attribute/*">
               <xsl:apply-templates select="."/>
           </xsl:for-each>
       </xsl:if>
       <xsl:text> =&gt; </xsl:text>
       <xsl:for-each select="fx:value/*">
           <xsl:apply-templates select="."/>
       </xsl:for-each>
       <xsl:if test="fx:interpolation">
           <xsl:text> tween </xsl:text>
           <xsl:apply-templates select="fx:interpolation/*"/>
       </xsl:if>
    </xsl:template>
    
    <xsl:template match="fx:keyframe-literal">
        <xsl:text>at (</xsl:text>
        <xsl:apply-templates select="fx:start-dur/*"/>
        <xsl:text>) {</xsl:text>
        <xsl:call-template name="println"/>
        <xsl:for-each select="fx:interpolation-values/*">
            <xsl:apply-templates select="."/>
            <xsl:text>;</xsl:text>
            <xsl:call-template name="println"/>
        </xsl:for-each>
        <xsl:if test="fx:trigger">
            <xsl:text> trigger </xsl:text>
            <xsl:apply-templates select="./*"/>
        </xsl:if> 
        <xsl:text>}</xsl:text>
        <xsl:call-template name="println"/>
    </xsl:template>

</xsl:transform>
