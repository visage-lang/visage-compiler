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
    This stylesheet converts XML representation of Visage AST (abstract
    syntax tree) to Visage source code form.
-->
<xsl:transform version="1.0"
               xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
               xmlns:c="http://xml.apache.org/xalan/java/org.visage.tools.tree.xml.Compiler"
               xmlns:visage="http://visage.org">
    
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
      <xsl:apply-templates select="visage:visage"/>
    </xsl:template>

    <xsl:template match="visage:visage">
        <xsl:call-template name="init-tabs"/>
        <xsl:apply-templates select="visage:file"/>
        <xsl:apply-templates select="visage:package"/>
        <xsl:apply-templates select="visage:defs"/>
    </xsl:template>

    <xsl:template match="visage:file">
        <xsl:text>// compiled from </xsl:text>
        <xsl:value-of select="."/>
        <xsl:call-template name="println"/>
    </xsl:template>
    
    <xsl:template match="visage:package">
        <xsl:text>package </xsl:text> 
        <xsl:apply-templates/>
        <xsl:text>;</xsl:text>
        <xsl:call-template name="println"/>
        <xsl:call-template name="println"/>
    </xsl:template>
   
    <xsl:template match="visage:defs">
        <xsl:for-each select="*">
            <xsl:apply-templates select="."/>
            <xsl:call-template name="println"/>
        </xsl:for-each>
    </xsl:template>
    
    <xsl:template match="visage:import">
        <xsl:text>import </xsl:text>
        <xsl:apply-templates/>
        <xsl:text>;</xsl:text>
    </xsl:template>
    
    <xsl:template match="visage:bind-status">
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
    
    <xsl:template match="visage:bind-status" mode="suffix">
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
        <xsl:if test="visage:name">
            <xsl:value-of select="visage:name"/>
        </xsl:if>
        
        <!-- print type -->
        <xsl:if test="visage:type">
            <xsl:apply-templates select="visage:type/*"/>
        </xsl:if>
    </xsl:template>
    
    <xsl:template name="print-var" >
        <xsl:apply-templates select="visage:modifiers"/>
        <xsl:choose>
            <xsl:when test="name(.) = 'visage:var'">
                <xsl:text>var </xsl:text>
            </xsl:when>
            <xsl:when test="name(.) = 'visage:def'">
                <xsl:text>def </xsl:text>
            </xsl:when>
        </xsl:choose>
            
        
        <!-- name of the variable/attribute -->
        <xsl:if test="visage:name">
            <xsl:value-of select="visage:name"/>
        </xsl:if>
        
        <!-- print type -->
        <xsl:if test="visage:type">
            <xsl:apply-templates select="visage:type/*"/>
        </xsl:if>
        
        <!-- optional initializer -->
        <xsl:if test="visage:init-value">
            <xsl:text> = </xsl:text>
            <xsl:apply-templates select="visage:bind-status"/>
            <xsl:apply-templates select="visage:init-value/*"/>
            <xsl:apply-templates select="visage:bind-status" mode="suffix"/>
        </xsl:if>
        
        <xsl:apply-templates select="visage:on-replace"/>
        <xsl:apply-templates select="visage:on-invalidate"/>
    </xsl:template>
    
    <xsl:template match="visage:var">
        <xsl:call-template name="print-var"/>
        <xsl:text>;</xsl:text>
    </xsl:template>
    
    <xsl:template match="visage:def">
        <xsl:call-template name="print-var"/>
        <xsl:text>;</xsl:text>
    </xsl:template>
    
    <xsl:template match="visage:empty">
        <xsl:text>;</xsl:text>
    </xsl:template>
    
    <xsl:template match="visage:while">
        <xsl:text>while (</xsl:text>
        <xsl:apply-templates select="visage:test/*"/>
        <xsl:text>) </xsl:text>
        <xsl:apply-templates select="visage:stmt/*"/>
    </xsl:template>
   
    <xsl:template match="visage:try">
        <xsl:text>try </xsl:text>
        <xsl:apply-templates select="visage:block"/>
        <xsl:apply-templates select="visage:catches/visage:catch"/>
        <xsl:if test="visage:finally">
            <xsl:text> finally </xsl:text>
            <xsl:apply-templates select="visage:finally/visage:block"/>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="visage:catch">
        <xsl:text> catch (</xsl:text>
        <xsl:for-each select="visage:var">
            <xsl:call-template name="print-param"/>
        </xsl:for-each>
        <xsl:text>) </xsl:text>
        <xsl:apply-templates select="visage:block"/>
    </xsl:template>
    
    <xsl:template match="visage:if">
        <xsl:text>if (</xsl:text>
        <xsl:apply-templates select="visage:test/*"/>
        <xsl:text>) </xsl:text>
        <xsl:apply-templates select="visage:then/*"/>
        <xsl:if test="visage:else">
            <xsl:text> else </xsl:text>
            <xsl:apply-templates select="visage:else/*"/>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="visage:break">
        <xsl:text>break</xsl:text>
        <xsl:if test="visage:label">
            <xsl:text> </xsl:text>
            <xsl:value-of select="visage:label"/>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="visage:continue">
        <xsl:text>continue</xsl:text>
        <xsl:if test="visage:label">
            <xsl:text> </xsl:text>
            <xsl:value-of select="visage:label"/>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="visage:return">
        <xsl:text>return</xsl:text>
        <xsl:if test="*">
            <xsl:text> </xsl:text>
            <xsl:apply-templates select="*"/>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="visage:throw">
        <xsl:text>throw</xsl:text>
        <xsl:text> </xsl:text>
        <xsl:apply-templates select="*"/>
    </xsl:template>
    
    <xsl:template match="visage:invoke">
        <xsl:apply-templates select="visage:method/*"/>
        <xsl:text>(</xsl:text>
        <xsl:call-template name="print-comma-list">
            <xsl:with-param name="parent" select="visage:args"/>
        </xsl:call-template>
        <xsl:text>)</xsl:text>
    </xsl:template>
    
    <xsl:template match="visage:paren">
        <xsl:text>(</xsl:text>
        <xsl:apply-templates select="*[1]"/>
        <xsl:text>)</xsl:text>
    </xsl:template>
    
    <xsl:template name="print-binary-expr">
        <xsl:param name="operator"/>
        <xsl:apply-templates select="visage:left/*"/>
        <xsl:value-of select="$operator"/>
        <xsl:apply-templates select="visage:right/*"/>
    </xsl:template>
    
    <xsl:template match="visage:assignment">
        <xsl:call-template name="print-binary-expr">
            <xsl:with-param name="operator"> = </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <!-- compound assignments -->
    <xsl:template match="visage:multiply-assignment">
        <xsl:call-template name="print-binary-expr">
            <xsl:with-param name="operator"> *= </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="visage:divide-assignment">
        <xsl:call-template name="print-binary-expr">
            <xsl:with-param name="operator"> /= </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="visage:remainder-assignment">
        <xsl:call-template name="print-binary-expr">
            <xsl:with-param name="operator"> %= </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="visage:plus-assignment">
        <xsl:call-template name="print-binary-expr">
            <xsl:with-param name="operator"> += </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="visage:minus-assignment">
        <xsl:call-template name="print-binary-expr">
            <xsl:with-param name="operator"> -= </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="visage:left-shift-assignment">
        <xsl:call-template name="print-binary-expr">
            <xsl:with-param name="operator"> &lt;&lt;= </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="visage:right-shift-assignment">
        <xsl:call-template name="print-binary-expr">
            <xsl:with-param name="operator"> &gt;&gt;= </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="visage:unsigned-right-shift-assignment">
        <xsl:call-template name="print-binary-expr">
            <xsl:with-param name="operator"> &gt;&gt;&gt;= </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="visage:and-assignment">
        <xsl:call-template name="print-binary-expr">
            <xsl:with-param name="operator"> &amp;= </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="visage:xor-assignment">
        <xsl:call-template name="print-binary-expr">
            <xsl:with-param name="operator"> ^= </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="visage:or-assignment">
        <xsl:call-template name="print-binary-expr">
            <xsl:with-param name="operator"> |= </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <!-- unary operators -->
    <xsl:template match="visage:sizeof">
        <xsl:text>sizeof </xsl:text>
        <xsl:apply-templates/>
        <xsl:text></xsl:text>
    </xsl:template>
    
    <xsl:template name="print-unary-expr">
        <xsl:param name="operator"/>
        <xsl:value-of select="$operator"/>
        <xsl:apply-templates select="*[1]"/>
    </xsl:template>
    
    <xsl:template match="visage:postfix-increment">
        <xsl:apply-templates select="*[1]"/>
        <xsl:text>++</xsl:text>
    </xsl:template>
    
    <xsl:template match="visage:prefix-increment">
        <xsl:call-template name="print-unary-expr">
            <xsl:with-param name="operator">++</xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="visage:postfix-decrement">
        <xsl:apply-templates select="*[1]"/>
        <xsl:text>--</xsl:text>
    </xsl:template>
    
    <xsl:template match="visage:prefix-decrement">
        <xsl:call-template name="print-unary-expr">
            <xsl:with-param name="operator">--</xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="visage:unary-plus">
        <xsl:call-template name="print-unary-expr">
            <xsl:with-param name="operator">+</xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="visage:unary-minus">
        <xsl:call-template name="print-unary-expr">
            <xsl:with-param name="operator">-</xsl:with-param>
        </xsl:call-template>
    </xsl:template>

    <xsl:template match="visage:logical-complement">
        <xsl:call-template name="print-unary-expr">
            <xsl:with-param name="operator">not </xsl:with-param>
        </xsl:call-template>
    </xsl:template>

    <!-- binary operators -->
    <xsl:template match="visage:multiply">
        <xsl:call-template name="print-binary-expr">
            <xsl:with-param name="operator"> * </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="visage:divide">
        <xsl:call-template name="print-binary-expr">
            <xsl:with-param name="operator"> / </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="visage:remainder">
        <xsl:call-template name="print-binary-expr">
            <xsl:with-param name="operator"> mod </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="visage:plus">
        <xsl:call-template name="print-binary-expr">
            <xsl:with-param name="operator"> + </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="visage:minus">
        <xsl:call-template name="print-binary-expr">
            <xsl:with-param name="operator"> - </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="visage:left-shift">
        <xsl:call-template name="print-binary-expr">
            <xsl:with-param name="operator"> &lt;&lt; </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="visage:right-shift">
        <xsl:call-template name="print-binary-expr">
            <xsl:with-param name="operator"> &gt;&gt; </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="visage:unsigned-right-shift">
        <xsl:call-template name="print-binary-expr">
            <xsl:with-param name="operator"> &gt;&gt;&gt; </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="visage:less-than">
        <xsl:call-template name="print-binary-expr">
            <xsl:with-param name="operator"> &lt; </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="visage:greater-than">
        <xsl:call-template name="print-binary-expr">
            <xsl:with-param name="operator"> &gt; </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="visage:less-than-equal">
        <xsl:call-template name="print-binary-expr">
            <xsl:with-param name="operator"> &lt;= </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="visage:greater-than-equal">
        <xsl:call-template name="print-binary-expr">
            <xsl:with-param name="operator"> &gt;= </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="visage:equal-to">
        <xsl:call-template name="print-binary-expr">
            <xsl:with-param name="operator"> == </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="visage:not-equal-to">
        <xsl:call-template name="print-binary-expr">
            <xsl:with-param name="operator"> != </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="visage:and">
        <xsl:call-template name="print-binary-expr">
            <xsl:with-param name="operator"> &amp; </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="visage:xor">
        <xsl:call-template name="print-binary-expr">
            <xsl:with-param name="operator"> ^ </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="visage:or">
        <xsl:call-template name="print-binary-expr">
            <xsl:with-param name="operator"> | </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="visage:conditional-and">
        <xsl:call-template name="print-binary-expr">
            <xsl:with-param name="operator"> and </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="visage:conditional-or">
        <xsl:call-template name="print-binary-expr">
            <xsl:with-param name="operator"> or </xsl:with-param>
        </xsl:call-template>
    </xsl:template>

    <xsl:template match="visage:cast">
        <xsl:text>(</xsl:text>
        <xsl:apply-templates select="visage:expr/*"/>
        <xsl:text> as </xsl:text>
        <xsl:apply-templates select="visage:type/*" mode="no-colon"/>
        <xsl:text>)</xsl:text>
    </xsl:template>
    
    <xsl:template match="visage:instanceof">
        <xsl:apply-templates select="visage:expr/*"/>
        <xsl:text> instanceof </xsl:text>
        <xsl:apply-templates select="visage:type/*"/>
    </xsl:template>
    
    <xsl:template match="visage:select">
        <xsl:apply-templates select="visage:expr/*"/>
        <xsl:text>.</xsl:text>
        <xsl:value-of select="visage:member"/>
    </xsl:template>
    
    <xsl:template match="visage:ident">
        <xsl:value-of select="."/>
    </xsl:template>
    
    <!-- literals -->
    
    <xsl:template match="visage:int-literal">
        <xsl:value-of select="."/>
    </xsl:template>

    <xsl:template match="visage:long-literal">
        <xsl:value-of select="."/>
    </xsl:template>
    
    <xsl:template match="visage:float-literal">
        <xsl:value-of select="."/>
    </xsl:template>
    
    <xsl:template match="visage:double-literal">
        <xsl:value-of select="."/>
    </xsl:template>
    
    <xsl:template match="visage:true">
        <xsl:text>true</xsl:text>
    </xsl:template>
    
    <xsl:template match="visage:false">
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
    
    <xsl:template match="visage:string-literal">
        <xsl:text>"</xsl:text>
        <xsl:value-of select="."/>
        <!--
        <xsl:call-template name="print-quoted-string">
            <xsl:with-param name="str" select="."/>
        </xsl:call-template>
        -->
        <xsl:text>"</xsl:text>
    </xsl:template>
    
    <xsl:template match="visage:null">
        <xsl:text>null</xsl:text>
    </xsl:template>
    
    <!-- modifiers -->
    <xsl:template match="visage:modifiers">
        <xsl:for-each select="visage:li">
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
    
    <xsl:template match="visage:value">
        <xsl:call-template name="align"/>
        <xsl:apply-templates/>
        <xsl:call-template name="println"/>
    </xsl:template>
    
    <!-- treat file-level blocks as special, don't include braces around -->
    <xsl:template match="visage:visage/visage:defs/visage:block">
        <xsl:call-template name="print-stmts">
        <xsl:with-param name="parent" select="visage:stmts"/>
        </xsl:call-template>
        <xsl:apply-templates select="visage:value"/>
    </xsl:template>

    <xsl:template match="visage:block">
        <xsl:text>{</xsl:text>
        <xsl:call-template name="println"/>
        <xsl:call-template name="indent"/>
        <xsl:call-template name="print-stmts">
            <xsl:with-param name="parent" select="visage:stmts"/>
        </xsl:call-template>
        <xsl:apply-templates select="visage:value"/>
        <xsl:call-template name="undent"/>
        <xsl:call-template name="align"/>
        <xsl:text>}</xsl:text>
    </xsl:template>
    
    <xsl:template name="print-super-types">
        <xsl:if test="visage:extends">
            <xsl:text> extends </xsl:text>
            <xsl:call-template name="print-comma-list">
                <xsl:with-param name="parent" select="visage:extends"/>
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
            <xsl:with-param name="parent" select="visage:members"/>
            <xsl:with-param name="semicolon" select="false"/>
        </xsl:call-template>
        <xsl:call-template name="undent"/>
        <xsl:call-template name="align"/>
        
        <xsl:call-template name="class-body-end"/>
        <xsl:text>}</xsl:text>
    </xsl:template>
    
    <xsl:template match="visage:class">
        <xsl:call-template name="println"/>
        <xsl:call-template name="align"/>
        <xsl:apply-templates select="visage:modifiers"/>
        <xsl:text>class </xsl:text>
        <xsl:value-of select="visage:name"/>
        
        <xsl:call-template name="print-super-types"/>
        
        <!-- class body -->
        <xsl:text> </xsl:text>
        <xsl:call-template name="print-class-body"/>
    </xsl:template>
    
    <xsl:template match="visage:for">
        <xsl:text>for (</xsl:text>
        <xsl:for-each select="visage:in/*">
            <xsl:if test="not(position() = 1)">
                <xsl:text>, </xsl:text>
            </xsl:if>
            <xsl:value-of select="visage:var/visage:name"/>
            <xsl:apply-templates select="visage:var/visage:type/*"/>
            <xsl:text> in </xsl:text>
            <xsl:apply-templates select="visage:seq/*"/>
            <xsl:if test="visage:where">
                <xsl:text> where </xsl:text>
                <xsl:apply-templates select="visage:where/*"/>
            </xsl:if>
        </xsl:for-each>
        <xsl:text>) </xsl:text>
        <xsl:apply-templates select="visage:body/*"/>
    </xsl:template>
    
    <xsl:template match="visage:indexof">
        <xsl:text>indexof </xsl:text>
        <xsl:apply-templates/>
    </xsl:template>
    
    <xsl:template match="visage:init">
        <xsl:call-template name="println"/>
        <xsl:call-template name="align"/>
        <xsl:text>init </xsl:text>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="visage:postinit">
        <xsl:call-template name="println"/>
        <xsl:call-template name="align"/>
        <xsl:text>postinit </xsl:text>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="visage:new">
        <xsl:text>new </xsl:text>
        <xsl:apply-templates select="visage:class/*"/>
        <xsl:text>(</xsl:text>
            <xsl:call-template name="print-comma-list">
                <xsl:with-param name="parent" select="visage:args"/>
            </xsl:call-template>
        <xsl:text>)</xsl:text>
    </xsl:template>
            
    <xsl:template name="object-literal-begin"/>
    <xsl:template name="object-literal-end"/>
    <xsl:template match="visage:object-literal">
        <xsl:apply-templates select="visage:class/*"/>
        <xsl:text> {</xsl:text>
        <xsl:call-template name="object-literal-begin"/>
        <xsl:call-template name="indent"/>
        <xsl:for-each select="visage:defs/*">
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
    
    <xsl:template match="visage:object-literal-init">
        <xsl:value-of select="visage:name"/>
        <xsl:text> : </xsl:text>
        <xsl:apply-templates select="visage:bind-status"/>
        <xsl:apply-templates select="visage:expr/*"/>
        <xsl:apply-templates select="visage:bind-status" mode="suffix"/>
    </xsl:template>
   
   <xsl:template match="visage:override-var">
        <xsl:text>override var </xsl:text>
        <xsl:value-of select="./visage:name"/>
        <xsl:apply-templates select="visage:expr/*"/>
        <xsl:text> </xsl:text>
        <xsl:apply-templates select="visage:on-replace"/>
        <xsl:apply-templates select="visage:on-invalidate"/>
    </xsl:template>

    <xsl:template name="handle-on-replace-clause">
        <xsl:if test="visage:old-value">
            <xsl:text>  </xsl:text>
            <xsl:value-of select="visage:old-value/visage:var/visage:name"/>
        </xsl:if>
        <xsl:if test="visage:first-index">
            <xsl:text> [ </xsl:text>
            <xsl:value-of select="visage:first-index/visage:var/visage:name"/>
            <xsl:if test="visage:last-index">
                <xsl:text> .. </xsl:text>
                <xsl:value-of select="visage:last-index/visage:var/visage:name"/>
            </xsl:if>
            <xsl:text> ] </xsl:text>
        </xsl:if>
        <xsl:if test="visage:new-elements">
            <xsl:value-of select="visage:new-elements/visage:var/visage:name"/>
        </xsl:if>
        <xsl:apply-templates select="visage:block"/>
    </xsl:template>

    <xsl:template match="visage:on-replace">
        <xsl:text> on replace </xsl:text>
        <xsl:call-template name="handle-on-replace-clause"/>
    </xsl:template>

    <xsl:template match="visage:on-invalidate">
        <xsl:text> on invalidate </xsl:text>
        <xsl:call-template name="handle-on-replace-clause"/>
    </xsl:template>

    <!-- functions -->
    <xsl:template name="function-body-begin"/>
    <xsl:template name="function-body-end"/>
    <xsl:template name="print-function-body">
        <!-- print the comma separated parameters -->
        <xsl:text>(</xsl:text>
        <xsl:for-each select="visage:params/*">
            <xsl:call-template name="print-param"/>
            <xsl:if test="not(position()=last())">
                <xsl:text>, </xsl:text>
            </xsl:if>
        </xsl:for-each>
        <xsl:text>)</xsl:text>
        
        <!-- return type, if any -->
        <xsl:if test="visage:return-type">
            <xsl:apply-templates select="visage:return-type/*"/> 
        </xsl:if>
        
        <xsl:choose>
            <!-- print the code block, if any -->
            <xsl:when test="visage:block">
                <xsl:text> </xsl:text>
                <xsl:text>{</xsl:text>
                <xsl:call-template name="function-body-begin"/>
                <xsl:call-template name="println"/>
                <xsl:call-template name="indent"/>
                <xsl:call-template name="print-stmts">
                    <xsl:with-param name="parent" select="visage:block/visage:stmts"/>
                </xsl:call-template>
                <xsl:apply-templates select="visage:block/visage:value"/>
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
    
    <xsl:template match="visage:function">
        <xsl:variable name="name" select="visage:name"/>
        <xsl:call-template name="println"/>
        <xsl:call-template name="align"/>
            
        <xsl:apply-templates select="visage:modifiers"/>
        
        <xsl:text>function </xsl:text>
        <!-- method name -->
        <xsl:value-of select="$name"/>
            
        <xsl:call-template name="print-function-body"/>
    </xsl:template>
    
    <xsl:template match="visage:anon-function">
        <xsl:call-template name="println"/>
        <xsl:call-template name="align"/>
        
        <xsl:text>function </xsl:text>
        <xsl:call-template name="print-function-body"/>
    </xsl:template>
    
    <xsl:template match="visage:seq-delete">
        <xsl:text>delete </xsl:text>
        <xsl:if test="visage:elem">
            <xsl:apply-templates select="visage:elem/*"/>
            <xsl:text> from </xsl:text>
        </xsl:if>
        <xsl:apply-templates select="visage:seq/*"/>
    </xsl:template>
    
    <xsl:template match="visage:seq-empty">
        <xsl:text>[]</xsl:text>
    </xsl:template>
    
    <xsl:template match="visage:seq-explicit">
        <xsl:text>[</xsl:text>
        <xsl:call-template name="print-comma-list">
            <xsl:with-param name="parent" select="visage:items"/>
        </xsl:call-template>
        <xsl:text>]</xsl:text>
    </xsl:template>
    
    <xsl:template match="visage:seq-indexed">
        <xsl:apply-templates select="visage:seq/*"/>
        <xsl:text>[</xsl:text>
        <xsl:apply-templates select="visage:index/*"/>
        <xsl:text>]</xsl:text>
    </xsl:template>
    
    <xsl:template match="visage:seq-slice">
        <xsl:apply-templates select="visage:seq/*"/>
        <xsl:text>[</xsl:text>
        <xsl:apply-templates select="visage:first/*"/>
        <xsl:text>..</xsl:text>
        <xsl:if test="visage:slice-end-kind = 'exclusive'">
            <xsl:text>&lt;</xsl:text>
        </xsl:if>
        <xsl:apply-templates select="visage:last/*"/>
        <xsl:text>]</xsl:text>
    </xsl:template>
    
    <xsl:template match="visage:seq-insert">
        <xsl:text>insert </xsl:text>
        <xsl:apply-templates select="visage:elem/*"/>
        <xsl:text> into </xsl:text>
        <xsl:apply-templates select="visage:seq/*"/>
    </xsl:template>
    
    <xsl:template match="visage:seq-range">
        <xsl:text>[</xsl:text>
        <xsl:apply-templates select="visage:lower/*"/>
        <xsl:text>..</xsl:text>
        <xsl:if test="visage:exclusive = 'true'">
            <xsl:text> &lt;</xsl:text>
        </xsl:if>
        <xsl:apply-templates select="visage:upper/*"/>
        <xsl:if test="visage:step">
            <xsl:text> step </xsl:text>
            <xsl:apply-templates select="visage:step/*"/>
        </xsl:if>
        <xsl:text>]</xsl:text>
    </xsl:template>

    <xsl:template match="visage:invalidate">
        <xsl:text>invalidate </xsl:text>
        <xsl:apply-templates select="visage:var/*"/>
    </xsl:template>
    
    <xsl:template match="visage:string-expr">
        <xsl:text>"</xsl:text>
        <xsl:for-each select="visage:part">
            <xsl:choose>
                <xsl:when test="visage:string-literal">
                    <xsl:value-of select="."/>
                    <!--
                    <xsl:call-template name="print-quoted-string">
                        <xsl:with-param name="str" select="."/>
                    </xsl:call-template>
                    -->
                </xsl:when>
                <xsl:otherwise>
                    <xsl:text>{</xsl:text>
                    <xsl:if test="not(visage:format = '')">
                        <xsl:value-of select="visage:format"/>
                        <xsl:text> </xsl:text>
                    </xsl:if>
                    <xsl:apply-templates select="visage:expr/*"/>
                    <xsl:text>}</xsl:text>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:for-each>
        <xsl:text>"</xsl:text>
    </xsl:template>
    
    <!-- types -->
    <xsl:template match="visage:type-any">
        <xsl:text> : *</xsl:text>
        <xsl:apply-templates select="visage:cardinality"/>
    </xsl:template>
    
    <xsl:template match="visage:type-class">
        <xsl:text> : </xsl:text>
        <xsl:apply-templates select="visage:class/*" mode="no-colon"/>
        <xsl:apply-templates select="visage:cardinality"/>
    </xsl:template>
    
    <xsl:template match="visage:type-functional">
        <xsl:text> : function (</xsl:text>
        <xsl:for-each select="visage:params/*">
            <xsl:apply-templates select="."/>
            <xsl:if test="not(position()=last())">
                <xsl:text>, </xsl:text>
            </xsl:if>
        </xsl:for-each>
        <xsl:text>)</xsl:text>
        <xsl:apply-templates select="visage:return-type/*"/>
        <xsl:apply-templates select="visage:cardinality"/>
    </xsl:template>

    <xsl:template match="visage:type-array">
        <xsl:text> : nativearray of </xsl:text>
        <xsl:apply-templates select="*" mode="no-colon"/>
    </xsl:template>
    
    <xsl:template match="visage:type-any" mode="no-colon">
        <xsl:text> *</xsl:text>
        <xsl:apply-templates select="visage:cardinality"/>
    </xsl:template>
    
    <xsl:template match="visage:type-class" mode="no-colon">
        <xsl:text> </xsl:text>
        <xsl:apply-templates select="visage:class/*"/>
        <xsl:apply-templates select="visage:cardinality"/>
    </xsl:template>
    
    <xsl:template match="visage:type-functional" mode="no-colon">
        <xsl:text> function (</xsl:text>
        <xsl:for-each select="visage:params/*">
            <xsl:apply-templates select="."/>
            <xsl:if test="not(position()=last())">
                <xsl:text>, </xsl:text>
            </xsl:if>
        </xsl:for-each>
        <xsl:text>)</xsl:text>
        <xsl:apply-templates select="visage:return-type/*"/>
        <xsl:apply-templates select="visage:cardinality"/>
    </xsl:template>

    <xsl:template match="visage:type-array" mode="no-colon">
        <xsl:text> nativearray of</xsl:text>
        <xsl:apply-templates select="*" mode="no-colon"/>
    </xsl:template>

    <xsl:template match="visage:type-unknown">
        <xsl:apply-templates select="visage:cardinality"/>
    </xsl:template>
    
    <xsl:template match="visage:cardinality">
        <xsl:choose>
            <xsl:when test=". = 'singleton'"/>
            <xsl:when test=". = 'unknown'"/>
            <xsl:when test=". = 'any'">
                <xsl:text>[]</xsl:text>
            </xsl:when>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template match="visage:time-literal">
        <xsl:value-of select="."/>
        <xsl:text>ms</xsl:text>
    </xsl:template>

    <xsl:template match="visage:interpolate-value">
       <xsl:if test="visage:attribute">
           <xsl:for-each select="visage:attribute/*">
               <xsl:apply-templates select="."/>
           </xsl:for-each>
       </xsl:if>
       <xsl:text> =&gt; </xsl:text>
       <xsl:for-each select="visage:value/*">
           <xsl:apply-templates select="."/>
       </xsl:for-each>
       <xsl:if test="visage:interpolation">
           <xsl:text> tween </xsl:text>
           <xsl:apply-templates select="visage:interpolation/*"/>
       </xsl:if>
    </xsl:template>
    
    <xsl:template match="visage:keyframe-literal">
        <xsl:text>at (</xsl:text>
        <xsl:apply-templates select="visage:start-dur/*"/>
        <xsl:text>) {</xsl:text>
        <xsl:call-template name="println"/>
        <xsl:for-each select="visage:interpolation-values/*">
            <xsl:apply-templates select="."/>
            <xsl:text>;</xsl:text>
            <xsl:call-template name="println"/>
        </xsl:for-each>
        <xsl:if test="visage:trigger">
            <xsl:text> trigger </xsl:text>
            <xsl:apply-templates select="./*"/>
        </xsl:if> 
        <xsl:text>}</xsl:text>
        <xsl:call-template name="println"/>
    </xsl:template>

</xsl:transform>
