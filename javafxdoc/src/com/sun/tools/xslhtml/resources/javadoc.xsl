<?xml version="1.0" encoding="UTF-8"?>
<!--
 Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
 DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.

 This code is free software; you can redistribute it and/or modify it
 under the terms of the GNU General Public License version 2 only, as
 published by the Free Software Foundation.

 This code is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 version 2 for more details (a copy is included in the LICENSE file that
 accompanied this code).

 You should have received a copy of the GNU General Public License version
 2 along with this work; if not, write to the Free Software Foundation,
 Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.

 Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 CA 95054 USA or visit www.sun.com if you need additional information or
 have any questions.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="html"/>
    
    <xsl:variable name="use-toc-tables">true</xsl:variable>
    <xsl:param name="master-css">general.css</xsl:param>
    <xsl:param name="extra-css"/>
    <xsl:param name="extra-js"/>
    <xsl:param name="extra-js2"/> <!-- josh: this is a hack -->
    <xsl:param name="target-class">javafx.ui.ToggleButton</xsl:param>
    <xsl:param name="target-profile">common</xsl:param>
    <xsl:param name="profiles-enabled">false</xsl:param>
    <xsl:param name="inline-classlist">false</xsl:param>
    <xsl:param name="inline-descriptions">false</xsl:param>
    <xsl:param name="root-path">../</xsl:param>
    <xsl:param name="std.doctitle">JavaFX API</xsl:param>
    
    
<!-- starter template -->    
    <xsl:template match="/">
        
        <xsl:if test="not (/classList) and not (/packageList)">
            <xsl:apply-templates select="//class[@qualifiedName=$target-class]"/>
            <!--
            <xsl:apply-templates select="//abstractClass[@qualifiedName=$target-class]"/>
            <xsl:apply-templates select="//interface[@qualifiedName=$target-class]"/>
            -->
        </xsl:if>
        
        <xsl:apply-templates select="/classList"/>
        <xsl:apply-templates select="/packageList"/>
        
    </xsl:template>
    
    
<!-- ====================== -->    
<!-- indexes and overviews -->
<!-- ====================== -->    
    <xsl:template match="packageList[@mode='overview-frame']">
        <html>
            <head>
                <link href="{$master-css}" rel="stylesheet"/>
                <xsl:if test="$extra-css">
                    <link href="{$extra-css}" rel="stylesheet"/>
                </xsl:if>
                <xsl:if test="$extra-js">
                    <script src="{$extra-js}"></script>
                </xsl:if>
            </head>
            <body>
                <ul id="packageList">
                    <xsl:for-each select="package">
                        <li>
                            <a target='classListFrame'>
                                <xsl:attribute name="href"><xsl:value-of select="@name"/>/package-frame.html</xsl:attribute>
                                <xsl:value-of select="@name"/>
                            </a>
                        </li>
                    </xsl:for-each>
                </ul>
            </body>
        </html>
    </xsl:template>
    
    <xsl:template match="packageList[@mode='overview-summary']">
        <html>
            <head>
                <link href="{$root-path}{$master-css}" rel="stylesheet"/>
                <xsl:if test="$extra-css">
                    <link href="{$root-path}{$extra-css}" rel="stylesheet"/>
                </xsl:if>
                <xsl:if test="$extra-js">
                    <script src="{$root-path}{$extra-js}"></script>
                </xsl:if>
            </head>
            <body>
                <h3>JavaFX API</h3>
                <table>
                    <tr><th></th></tr>
                    <xsl:for-each select="package">
                        <tr>
                            <td>
                                <a target='classFrame'>
                                    <xsl:attribute name="href"><xsl:value-of select="@name"/>/package-summary.html</xsl:attribute>
                                    <xsl:value-of select="@name"/>
                                </a>
                            </td>
                            <td>
                                <xsl:apply-templates select="docComment/firstSentenceTags"/>
                            </td>
                        </tr>
                    </xsl:for-each>
                </table>
            </body>
        </html>
    </xsl:template>
    
    
    
    
    <xsl:template match="classList[@mode='overview-frame']">
        <html>
            <head>
                <link href="${root-path}{$master-css}" rel="stylesheet"/>
                <xsl:if test="$extra-css">
                    <link href="${root-path}{$extra-css}" rel="stylesheet"/>
                </xsl:if>
                <xsl:if test="$extra-js">
                    <script src="${root-path}{$extra-js}"></script>
                </xsl:if>
            </head>
            <body>
                <p><b>
                    <a href="package-summary.html" target="classFrame"><xsl:value-of select="@packageName"/></a>
                </b></p>
                <ul id="classList">
                    <xsl:for-each select="class">
                        <li>
                            <a target='classFrame'>
                                <xsl:attribute name="href"><xsl:value-of select="@qualifiedName"/>.html</xsl:attribute>
                                <xsl:attribute name="class">
                                    <xsl:for-each select="tags/cssclass">
                                        <xsl:value-of select="text()"/>
                                        <xsl:text> </xsl:text>
                                    </xsl:for-each>
                                    <xsl:text>profile-<xsl:value-of select="docComment/tags/profile/text()"/></xsl:text>
                                </xsl:attribute>
                                <xsl:value-of select="@name"/>
                            </a>
                        </li>
                    </xsl:for-each>
                </ul>
            </body>
        </html>
    </xsl:template>
        
    <xsl:template match="classList[@mode='overview-summary']">
        <html>
            <head>
                <link href="${root-path}{$master-css}" rel="stylesheet"/>
                <xsl:if test="$extra-css">
                    <link href="${root-path}{$extra-css}" rel="stylesheet"/>
                </xsl:if>
                <xsl:if test="$extra-js">
                    <script src="${root-path}{$extra-js}"></script>
                </xsl:if>
            </head>
            <body>
                <p><b><xsl:value-of select="@packageName"/></b></p>
                <table id="classList">
                    <tr><th>This is a summary</th></tr>
                    <xsl:for-each select="class">
                        <tr>
                            <td>
                                <a target='classFrame'>
                                    <xsl:attribute name="href"><xsl:value-of select="@qualifiedName"/>.html</xsl:attribute>
                                    <xsl:value-of select="@name"/>
                                </a>
                            </td>
                            <td>
                                <xsl:value-of select="firstSentenceTags"/>
                            </td>
                         </tr>
                     </xsl:for-each>
                </table>
            </body>
        </html>
    </xsl:template>
    
    
    <xsl:template name="inline-classlist">
        <ul id="classes-toc">
            <xsl:for-each select="/javadoc/package">
                <xsl:sort select="@name"/>
                <li>
                    <h4 class='header'><a href="#"><xsl:value-of select="@name"/></a></h4>
                    <ul class='content'>
                        <xsl:for-each select="class">
                            <xsl:sort select="@name"/>
                            <li>
                                <xsl:attribute name="class">
                                    <xsl:text>profile-<xsl:value-of select="docComment/tags/profile/text()"/></xsl:text>
                                    <xsl:if test="@qualifiedName = $target-class">
                                        <xsl:text> selected-class</xsl:text>
                                    </xsl:if>
                                </xsl:attribute>
                                <a>
                                <xsl:attribute name="href">
                                    <xsl:text>../</xsl:text>
                                    <xsl:value-of select="@packageName"/>
                                    <xsl:text>/</xsl:text>
                                    <xsl:value-of select="@qualifiedName"/>
                                    <xsl:text>.html</xsl:text>
                                </xsl:attribute>
                                <xsl:value-of select="@name"/>
                                </a></li>
                        </xsl:for-each>
                    </ul>
                </li>
            </xsl:for-each>
        </ul>
    </xsl:template>
    
    
    
    <xsl:template match="class">
        <xsl:call-template name="classOutput"/>
    </xsl:template>
    <xsl:template match="abstractClass">
        <xsl:call-template name="classOutput"/>
    </xsl:template>
    <xsl:template match="interface">
        <xsl:call-template name="classOutput"/>
    </xsl:template>

    
    
    
    
    
    
<!-- ====================== -->    
<!-- the actual class -->
<!-- ====================== -->    
    
    
    <xsl:template name="classOutput">
        <html>
            <head>
                <link href="../{$master-css}" rel="stylesheet"/>
                <style type="text/css"></style>
                <xsl:if test="$extra-css"><link href="../{$extra-css}" rel="stylesheet"/></xsl:if>
                <xsl:if test="$extra-js"><script src="../{$extra-js}"></script></xsl:if>
                <xsl:if test="$extra-js2"><script src="../{$extra-js2}"></script></xsl:if>
                <xsl:call-template name="head-post"/>
            </head>
            <body>
                <xsl:call-template name="header-pre"/>
                <xsl:if test="$inline-classlist='true'">
                    <xsl:call-template name="inline-classlist"/>
                </xsl:if>
                <div id="content">
                    <xsl:call-template name="header"/>
                    <a id="overview"><h3>Overview</h3></a>
                    <div class="overview">
                        <xsl:apply-templates select="docComment/inlineTags"/>
                        <xsl:apply-templates select="docComment/tags/example"/>
                        <xsl:apply-templates select="docComment/seeTags"/>
                        <xsl:apply-templates select="docComment/tags/profile"/>
                        <xsl:apply-templates select="docComment/tags/needsreview"/>
                    </div>
                    <xsl:call-template name="toc"/>
                    <xsl:call-template name="inherited"/>
                    <xsl:if test="not($inline-descriptions='true')">
                        <xsl:call-template name="members"/>
                    </xsl:if>
                </div>
            </body>
        </html>
    </xsl:template>
    
    
    
    
    
    <!-- =========== comments =========== -->
    <xsl:template match="docComment/commentText">
        <p class="comment">
            <xsl:value-of select="." disable-output-escaping="yes"/>
        </p>
    </xsl:template>
    
    <xsl:template match="docComment/inlineTags">
        <p class="comment">
            <xsl:for-each select="*"><xsl:apply-templates select="."/></xsl:for-each>
        </p>
    </xsl:template>
    
    <xsl:template match="docComment/tags/return">
        <xsl:apply-templates/>
    </xsl:template>
    <xsl:template match="inlineTags">
        <xsl:apply-templates/>
    </xsl:template>
    <xsl:template match="tags/example">
        <xsl:value-of select="." disable-output-escaping="yes"/>
    </xsl:template>
    <xsl:template match="docComment/tags/param">
        <xsl:apply-templates/>
    </xsl:template>
    
    <xsl:template match="docComment/firstSentenceTags">
        <p class="comment">
            <xsl:for-each select="*"><xsl:apply-templates select="."/></xsl:for-each>
        </p>
    </xsl:template>
    <xsl:template match="docComment/tags/profile">
        <p class="profile">Profile: <b><xsl:value-of select="."/></b></p>
    </xsl:template>
    
    <xsl:template match="docComment/tags/needsreview">
        <p class="needsreview">This comment needs review.</p>
    </xsl:template>

    
    <xsl:template match="Text"><xsl:value-of select="." disable-output-escaping="yes"/></xsl:template>
    <xsl:template match="seeTags">
        <p><b>See Also:</b><br/>
            <xsl:apply-templates select="see"/>
        </p>
    </xsl:template>
    
    <xsl:template match="seeTags/see">
        <a>
            <xsl:attribute name="href"><xsl:value-of select="@href"/></xsl:attribute>
            <xsl:choose>
                <xsl:when test="@label">
                    <xsl:text><xsl:value-of select="@label"/></xsl:text>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:text><xsl:value-of select="text()"/></xsl:text>
                </xsl:otherwise>
            </xsl:choose>
        </a>,
    </xsl:template>
    
    <xsl:template match="code"><code><xsl:value-of select="." disable-output-escaping="yes"/></code></xsl:template>
    <xsl:template match="inheritDoc">
        <xsl:variable name="override-name" select="../../../overrides/@name"/>
        <xsl:apply-templates select="//function[@name=$override-name]/docComment"/>
    </xsl:template>
    
    
    
    
<!-- ====================== -->    
<!--    header    -->
<!-- ====================== -->    
    <xsl:template name="header">

        <div id="nav">
            <!-- name of the class -->
            <h1 class="classname">
                <xsl:apply-templates select="modifiers"/>
                class
                <a class="classname">
                    <strong><xsl:value-of select="@packageName"/>.</strong>
                    <b><xsl:value-of select="@name"/></b>
                </a>
            </h1>
            
            
            <!-- inheritance hierarchy -->
            <h2><span class="descriptive">Inherits from:</span>
                <xsl:apply-templates select="." mode="super"/>
                <xsl:apply-templates select="." mode="interface"/>
            </h2>
            
            <!-- navigation header -->
            <xsl:if test="@language='javafx'">
                <ul id="tabs">
                    <li>Jump to Section:</li>
                    <li><a href="#overview">overview</a></li><li><a href="#fields-summary">attributes</a></li><li><a href="#methods-summary">functions</a></li>
                </ul>
            </xsl:if>
            
            <xsl:if test="@language='java'">
                <ul id="tabs">
                    <li>Jump to Section:</li>
                    <li><a href="#overview">overview</a></li><li><a href="#fields-summary">fields</a></li><li><a href="#constructors-summary">constructors</a></li><li><a href="#methods-summary">methods</a></li>
                </ul>
            </xsl:if>
            
        </div>
    </xsl:template>
    
    
    
    
    <xsl:template match="class[@language='javafx']" mode="interface">
        <xsl:for-each select="hierarchy/super">
            <a>
                <xsl:attribute name="title"><xsl:value-of select="@packageName"/>.<xsl:value-of select="@typeName"/></xsl:attribute>
                <xsl:attribute name="href">../<xsl:value-of select="@packageName"/>/<xsl:value-of select="@packageName"/>.<xsl:value-of select="@typeName"/>.html</xsl:attribute>
                <strong><xsl:value-of select="@packageName"/>.</strong>
                <b><xsl:value-of select="@typeName"/></b>
            </a>
            <xsl:text>, </xsl:text>
        </xsl:for-each>        
    </xsl:template>
    
    <xsl:template match="class" mode="interface">
        <xsl:if test="interfaces/interface">implements </xsl:if>
        <xsl:for-each select="interfaces/interface">
            <a>
                <xsl:attribute name="title"><xsl:value-of select="@packageName"/>.<xsl:value-of select="@typeName"/></xsl:attribute>
                <xsl:attribute name="href">../<xsl:value-of select="@packageName"/>/<xsl:value-of select="@packageName"/>.<xsl:value-of select="@typeName"/>.html</xsl:attribute>
                <strong><xsl:value-of select="@packageName"/>.</strong>
                <b><xsl:value-of select="@typeName"/></b>
            </a>
            <xsl:text>, </xsl:text>
        </xsl:for-each>
    </xsl:template>
    
    
    <xsl:template match="class" mode="super">        
        <!-- only do stuff if super exists at all -->
        <xsl:variable name="super" select="superclass/@qualifiedTypeName"/>
        <xsl:variable name="super-package" select="superclass/@packageName"/>
        <xsl:variable name="super-name" select="superclass/@simpleTypeName"/>
        <xsl:if test="$super">
            <!-- if super can't be found -->
            <xsl:if test="not(/javadoc/package[@name=$super-package]/class[@name=$super-name])">
                <!-- be sure to skip java.lang.Object -->
                <xsl:if test="not($super='java.lang.Object')">
                    <a>
                        <xsl:attribute name="title"><xsl:value-of select="superclass/@packageName"/>.<xsl:value-of select="superclass/@typeName"/></xsl:attribute>
                        <strong><xsl:value-of select="superclass/@packageName"/>.</strong>
                        <b><xsl:value-of select="superclass/@typeName"/></b>
                    </a>
                </xsl:if>
            </xsl:if>

            <!-- if super can be found -->
            <xsl:apply-templates select="/javadoc/package[@name=$super-package]/class[@name=$super-name]" mode="super"/>
            &gt;
            <a>
                <xsl:attribute name="title"><xsl:value-of select="@packageName"/>.<xsl:value-of select="@name"/></xsl:attribute>
                <xsl:attribute name="href">../<xsl:value-of select="@packageName"/>/<xsl:value-of select="@packageName"/>.<xsl:value-of select="@name"/>.html</xsl:attribute>
                <strong><xsl:value-of select="@packageName"/>.</strong>
                <b><xsl:value-of select="@name"/></b>
            </a>
        
        </xsl:if>
        
    </xsl:template>
    

    
    
    
    
    
    
    
<!-- ====================== -->    
<!-- The Table of Contents  -->
<!-- ====================== -->    
    
    <xsl:template name="toc">
        <div id="toc">
            
            <xsl:if test="count(attribute) > 0">
                <a id="fields-summary"><h3>Attribute Summary</h3></a>
                <table class="fields-summary fields">
                    <tr><th class="name">name</th><th class="type">type</th>
                        <xsl:call-template name="extra-attribute-column-header"/>
                        <th class="description">description</th></tr>
                    <tr><th class="header">
                        <xsl:attribute name="colspan"><xsl:call-template name="attribute-table-width"/></xsl:attribute>
                        <xsl:text>Public</xsl:text>
                        </th></tr>
                    <xsl:for-each select="attribute[modifiers/public]">
                        <xsl:sort select="@name" order="ascending"/>
                        <xsl:apply-templates select="." mode="toc"/>
                    </xsl:for-each>
                    <!-- do all protected attributes -->
                    <xsl:if test="attribute[modifiers/protected]">
                        <tr><th colspan="3" class="header">Protected</th></tr>
                        <xsl:for-each select="attribute[modifiers/protected]">
                            <xsl:sort select="@name" order="ascending"/>
                            <xsl:apply-templates select="." mode="toc"/>
                        </xsl:for-each>
                    </xsl:if>
                </table>
            </xsl:if>
            
            <xsl:if test="count(field) > 0">
                <a id="fields-summary"><h3>Field Summary</h3></a>
                <table class="fields-summary">
                    <tr><th>public</th><th>name</th><th>type</th></tr>
                    <xsl:for-each select="field">
                        <xsl:sort select="@name" order="ascending"/>
                        <xsl:apply-templates select="." mode="toc"/>
                    </xsl:for-each>
                </table>
            </xsl:if>
            
            
            <!-- inherited attributes -->
            <h3>Inherited Attributes</h3>
            <xsl:for-each select="hierarchy/super">
                <xsl:variable name="super-package" select="@packageName"/>
                <xsl:variable name="super-name" select="@simpleTypeName"/>
                <xsl:apply-templates select="/javadoc/package[@name=$super-package]/class[@name=$super-name]" mode="inherited-field"/>
            </xsl:for-each>

            
            
            
            <!-- constructors -->
            
            <xsl:if test="count(constructor) > 0">
                <a id="constructors-summary"><h3>Constructor Summary</h3></a>
                <dl>
                    <xsl:for-each select="constructor">
                        <xsl:sort select="@name" order="ascending"/>
                        <xsl:call-template name="method-like-toc"/>
                    </xsl:for-each>
                </dl>
            </xsl:if>
            
            
            
            
            
            <!-- methods and functions -->
            
            <!-- functions -->
            <xsl:if test="count(function) > 0">
                <a id="methods-summary"><h3>Function Summary</h3></a>
                <dl class="methods-summary">
                    <xsl:for-each select="function">
                        <xsl:sort select="@name" order="ascending"/>
                        <xsl:call-template name="method-like-toc"/>
                    </xsl:for-each>
                </dl>
            </xsl:if>
            
            
            <!-- methods -->
            <xsl:if test="count(method) > 0">
                <a id="methods-summary"><h3>Method Summary</h3></a>
                <dl class="methods-summary">
                    <xsl:for-each select="method">
                        <xsl:sort select="@name" order="ascending"/>
                        <xsl:call-template name="method-like-toc"/>
                    </xsl:for-each>
                </dl>
            </xsl:if>
            
            <!-- inherited -->
            <h3>Inherited Functions</h3>
            <xsl:for-each select="hierarchy/super">
                <xsl:variable name="super-package" select="@packageName"/>
                <xsl:variable name="super-name" select="@simpleTypeName"/>
                <xsl:apply-templates select="/javadoc/package[@name=$super-package]/class[@name=$super-name]" mode="inherited-method"/>
            </xsl:for-each>
        </div>
        
    </xsl:template>
    
    
    <!--  ==== Member details: attributes, fields, functions, methods === -->
    
    <xsl:template name="members">
        <xsl:if test="count(attribute) > 0">
            <div id="attributes">
                <h3>Attributes</h3>
                <xsl:for-each select="attribute">
                    <xsl:sort select="@name" order="ascending"/>
                    <xsl:apply-templates select="."/>
                </xsl:for-each>
            </div>
        </xsl:if>
        
        <xsl:if test="count(field) > 0">
            <div id="fields">
                <h3>Fields</h3>
                <xsl:for-each select="field">
                    <xsl:sort select="@name" order="ascending"/>
                    <xsl:apply-templates select="."/>
                </xsl:for-each>
            </div>
        </xsl:if>
        
        <xsl:if test="count(constructor) > 0">
            <div id="constructors">
                <h3>Constructors</h3>
                <xsl:for-each select="constructor">
                    <xsl:sort select="@name" order="ascending"/>
                    <xsl:call-template name="method-like"/>
                </xsl:for-each>
            </div>
        </xsl:if>
        
        <xsl:if test="count(function) > 0">
            <div id="functions">
                <h3>Functions</h3>
                <xsl:for-each select="function">
                    <xsl:sort select="@name" order="ascending"/>
                    <xsl:call-template name="method-like"/>
                </xsl:for-each>
            </div>
        </xsl:if>
        
        <xsl:if test="count(method) > 0">
            <div id="methods">
                <h3>Methods</h3>
                <xsl:for-each select="method">
                    <xsl:sort select="@name" order="ascending"/>
                    <xsl:call-template name="method-like"/>
                </xsl:for-each>
            </div>
        </xsl:if>
    </xsl:template>
    
    
    <xsl:template name="inherited">
    </xsl:template>
    
    <xsl:template match="class" mode="inherited-field">
        <xsl:if test="count(attribute) > 0">
            <h4><xsl:value-of select="@qualifiedName"/></h4>
            <table class="inherited-field fields">
                <tr><th class="name">name</th><th class="type">type</th>
                    <xsl:call-template name="extra-attribute-column-header"/>
                    <th class="description">description</th></tr>
                <xsl:for-each select="attribute">
                    <xsl:sort select="@name" order="ascending"/>
                    <xsl:apply-templates select="." mode="toc"/>
                </xsl:for-each>
            </table>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="class" mode="inherited-method">
        <xsl:if test="count(function) > 0">
            <h4><xsl:value-of select="@qualifiedName"/></h4>
                
            <dl class="inherited-method">
                <xsl:for-each select="function">
                    <xsl:sort select="@name" order="ascending"/>
                    <xsl:call-template name="method-like-toc"/>
                </xsl:for-each>
            </dl>
                
        </xsl:if>
    </xsl:template>
    
    
    
<!-- ====================== -->    
<!-- Attributes and Fields  -->
<!-- ====================== -->    
    
    <!-- summary line -->
<!--    <xsl:template match="$foo and attribute[]" mode="toc"><tr><td>skipping because it's a common one</td></tr></xsl:template>-->
    <xsl:template match="attribute" mode="toc">
        <xsl:if test="$profiles-enabled='false' or docComment/tags/profile/text()=$target-profile">
            <tr>
                <xsl:attribute name="class">
                    <xsl:text>attribute </xsl:text>
                    <xsl:for-each select="docComment/tags/cssclass">
                        <xsl:value-of select="text()"/>
                        <xsl:text> </xsl:text>
                    </xsl:for-each>
                    <xsl:text>profile-<xsl:value-of select="docComment/tags/profile/text()"/></xsl:text>
                    <xsl:call-template name="extra-attribute"/>
                    <xsl:call-template name="extra-attribute-toc"/>
                </xsl:attribute>
                <td class="name">
                    <a>
                        <xsl:apply-templates select="." mode="href"/>
                        <b class="name"><xsl:value-of select="@name"/></b>
                    </a>
                </td>
                <td class="type">
                    <a>
                        <xsl:apply-templates select="type" mode="href"/>
                        <i class="type"><xsl:value-of select="type/@simpleTypeName"/><xsl:value-of select="type/@dimension"/></i>
                    </a>
                </td>
                <xsl:call-template name="extra-attribute-column-data"/>
                <td class="description">
                    <xsl:apply-templates select="docComment/firstSentenceTags"/>
                    <xsl:if test="$inline-descriptions='true'">
                        <xsl:if test="docComment/extraNotes[@multipleSentences='true']">
                            <a href="#" class="long-desc-open"><img src="../images/JFX_arrow_right.png"/></a>
                            <div class="long-desc">
                                <xsl:call-template name="attribute-full-description"/>
                                &amp;nbsp;
                            </div>
                        </xsl:if>
                    </xsl:if>
                </td>
            </tr>
                
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="attribute/type | parameter/type" mode="href">
        <!--<xsl:variable name="atype" select="@qualifiedTypeName"/>-->
        <xsl:variable name="type-package" select="@packageName"/>
        <xsl:variable name="type-name" select="@simpleTypeName"/>
        <xsl:if test="/javadoc/package[@name=$type-package]/class[@name=$type-name]">
            <xsl:attribute name="href">
                <xsl:text>../</xsl:text>
                <xsl:value-of select="@packageName"/>
                <xsl:text>/</xsl:text>
                <xsl:value-of select="@qualifiedTypeName"/>
                <xsl:text>.html</xsl:text>
            </xsl:attribute>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="attribute/type | parameter/type" mode="linkname">
        <xsl:variable name="type-package" select="@packageName"/>
        <xsl:variable name="type-name" select="@simpleTypeName"/>
        <xsl:variable name="atype" select="@qualifiedTypeName"/>
        <xsl:choose>
            <xsl:when test="/javadoc/package[@name=$type-package]/class[@name=$type-name]">
                <xsl:value-of select="@simpleTypeName"/>    
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="@qualifiedTypeName"/>    
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template match="attribute" mode="href">
        <xsl:attribute name="href">
            <xsl:text>../</xsl:text>
            <xsl:value-of select="../@packageName"/>
            <xsl:text>/</xsl:text>
            <xsl:value-of select="../@qualifiedName"/>
            <xsl:text>.html#</xsl:text>
            <xsl:value-of select="@name"/>
        </xsl:attribute>
    </xsl:template>
    
    <!-- full description -->
    <xsl:template match="attribute">
        <xsl:if test="$profiles-enabled='false' or docComment/tags/profile/text()=$target-profile">
        <div>
            
            <!-- class attribute of div -->
            <xsl:attribute name="class">
                <xsl:text>attribute member </xsl:text>
                <xsl:for-each select="docComment/tags/cssclass">
                    <xsl:value-of select="text()"/>
                    <xsl:text> </xsl:text>
                </xsl:for-each>
                <xsl:text>profile-<xsl:value-of select="docComment/tags/profile/text()"/></xsl:text>
                <xsl:call-template name="extra-attribute"/>
                <xsl:call-template name="extra-attribute-full"/>
            </xsl:attribute>
            
            <!-- signature line -->
            <a>
                <h4>
                    <xsl:attribute name="id"><xsl:value-of select="@name"/></xsl:attribute>
                    <xsl:apply-templates select="modifiers"/>
                    <xsl:text> </xsl:text>
                    <b class="name"><xsl:value-of select="@name"/></b>
                    <xsl:text>: </xsl:text>
                    <a>
                        <xsl:apply-templates select="type" mode="href"/>
                        <i class="type"><xsl:value-of select="type/@simpleTypeName"/><xsl:value-of select="type/@dimension"/></i>
                    </a>
                </h4>
            </a>
            
            <!-- all of the docs -->
            <xsl:call-template name="attribute-full-description"/>
            
        </div>
        </xsl:if>
    </xsl:template>
    

    <xsl:template name="attribute-full-description">
        <xsl:apply-templates select="docComment/inlineTags"/>
        <xsl:apply-templates select="docComment/tags/defaultvalue"/>
        <xsl:apply-templates select="docComment/tags/setonce"/>
        <xsl:apply-templates select="docComment/tags/readonly"/>
        <xsl:apply-templates select="docComment/seeTags"/>
        <xsl:apply-templates select="docComment/tags/profile"/>
        <xsl:apply-templates select="docComment/tags/treatasprivate"/>
        <xsl:apply-templates select="docComment/tags/needsreview"/>
    </xsl:template>
    
    
    
    
<!-- ====================== -->    
<!--  Functions and Methods -->
<!-- ====================== -->    

    <!-- summary line -->
    <xsl:template name="method-like-toc">
        <xsl:if test="$profiles-enabled='false' or docComment/tags/profile/text()=$target-profile">
        <dt>
            <xsl:attribute name="class">
                <xsl:text>method </xsl:text>
                <xsl:if test="docComment/tags/advanced">
                    <xsl:text>advanced</xsl:text>
                </xsl:if>
                <xsl:text>profile-<xsl:value-of select="docComment/tags/profile/text()"/></xsl:text>
                <xsl:call-template name="extra-method"/>
                <xsl:call-template name="extra-method-toc"/>
            </xsl:attribute>
             <xsl:apply-templates select="." mode="toc-signature"/>
        </dt>
        <dd>
            <xsl:attribute name="class">
                <xsl:text>method </xsl:text>
                <xsl:if test="docComment/tags/advanced">
                    <xsl:text>advanced</xsl:text>
                </xsl:if>
                <xsl:text>profile-<xsl:value-of select="docComment/tags/profile/text()"/></xsl:text>
                <xsl:call-template name="extra-method"/>
                <xsl:call-template name="extra-method-toc"/>
            </xsl:attribute>
            <div>
            <xsl:apply-templates select="docComment/firstSentenceTags"/>
                <xsl:if test="$inline-descriptions='true'">
                    <!-- josh: we must always show the more content for parameters
                    and return values, even if there is only a one sentence description.
                    <xsl:if test="docComment/extraNotes[@multipleSentences='true']">
                    -->
                        <a href="#" class="long-desc-open"><img src="../images/JFX_arrow_right.png"/></a>
                        <div class="long-desc">
                            <xsl:call-template name="method-like-full-description"/>
                            &amp;nbsp;
                        </div>
                        <!--
                    </xsl:if>
                    -->
                </xsl:if>
            </div>
        </dd>
        </xsl:if>
    </xsl:template>

    
    <!-- full description -->
    <xsl:template name="method-like">
        <xsl:if test="$profiles-enabled='false' or docComment/tags/profile/text()=$target-profile">
        <div>
            <!-- div's class attribute -->
            <xsl:attribute name="class">
                <xsl:text>method member </xsl:text>
                <xsl:for-each select="docComment/tags/cssclass">
                    <xsl:value-of select="text()"/>
                    <xsl:text> </xsl:text>
                </xsl:for-each>
                <xsl:text>profile-<xsl:value-of select="docComment/tags/profile/text()"/></xsl:text>
                <xsl:call-template name="extra-method"/>
                <xsl:call-template name="extra-method-full"/>
            </xsl:attribute>
            <!-- signature -->
            <a>
                <xsl:attribute name="id"><xsl:apply-templates select="." mode="anchor-signature"/></xsl:attribute>
                <h4><xsl:apply-templates select="." mode="detail-signature"/></h4>
            </a>
            
            <xsl:call-template name="method-like-full-description"/>
        </div>  
        </xsl:if>
    </xsl:template>
    
    <!-- the full description of a method, minus the signature itself -->
    <xsl:template name="method-like-full-description">

            <!-- the rest of the docs -->
            <xsl:apply-templates select="docComment/inlineTags"/>
            <xsl:apply-templates select="docComment/seeTags"/>
            <xsl:apply-templates select="docComment/tags/needsreview"/>
            
            <!-- full parameters desc -->
            <xsl:if test="parameters/parameter">
                <dl class="parameters">
                    Parameters
                    <xsl:for-each select="parameters/parameter">
                        <dt><xsl:value-of select="@name"/></dt>
                        <dd><xsl:apply-templates select="docComment"/></dd>
                    </xsl:for-each>
                </dl>
            </xsl:if>
            
            <!-- full returns desc -->
            <xsl:if test="not(returns/@simpleTypeName='void' or returns/@simpleTypeName='Void')">
                <dl class="returns">
                    Returns
                    <dt><xsl:value-of select="returns/@simpleTypeName"/>
                    <xsl:value-of select="returns/@dimension"/></dt>
                    <dd><xsl:apply-templates select="docComment/tags/return"/></dd>
                </dl>
            </xsl:if>
            
            
            <!-- profile comment -->
            <xsl:apply-templates select="docComment/tags/profile"/>
            
    </xsl:template>
    
    <xsl:template name="extra-method"></xsl:template>
    <xsl:template name="extra-method-full"></xsl:template>
    <xsl:template name="extra-method-toc"></xsl:template>
    
    
    <!-- =================== -->
    <!-- signature stuff -->
    <!-- =================== -->

    <xsl:template match="function | method | constructor" mode="anchor-signature">
        <xsl:value-of select="@name"/>
        <xsl:text>(</xsl:text>
        <xsl:for-each select="parameters/parameter">
            <xsl:value-of select="@name"/>
            <xsl:text>:</xsl:text>
            <xsl:value-of select="type/@toString"/>
            <xsl:text>,</xsl:text>
        </xsl:for-each>
        <xsl:text>)</xsl:text>
    </xsl:template>
    
    <xsl:template match="modifiers">
        <i class="modifiers"><xsl:value-of select="@text"/></i>
    </xsl:template>
    
    <xsl:template match="parameters" mode="signature">
        <xsl:text>(</xsl:text>
        <i class="parameters">
            <xsl:for-each select="parameter">
                <xsl:if test="../../../@language='javafx'">
                    <b><xsl:value-of select="@name"/></b>:
                    <!-- build parameter type link, if appropriate -->
                    <a>
                        <xsl:apply-templates select="type" mode="href"/>
                        <i><xsl:apply-templates select="type" mode="linkname"/></i>
                    </a><xsl:value-of select="type/@dimension"/>
                </xsl:if>
                <xsl:if test="../../../@language='java'">
                    <i><xsl:value-of select="type/@toString"/></i>
                    <xsl:text> </xsl:text>
                    <b><xsl:value-of select="@name"/></b>
                </xsl:if>
                
                <!-- calc last comma -->
                <xsl:variable name="pos" select="position()"/>
                <xsl:variable name="lst" select="last()"/>
                <xsl:if test="not($pos=$lst)">
                    <xsl:text>, </xsl:text>
                </xsl:if>
            </xsl:for-each>
        </i>
        <xsl:text>)</xsl:text>
    </xsl:template>

    <xsl:template match="returns" mode="signature">
        <a>
           <xsl:apply-templates select="." mode="href"/>
           <i><xsl:apply-templates select="." mode="linkname"/>
           <xsl:value-of select="@dimension"/></i>
        </a>
    </xsl:template>
    <xsl:template match="returns[@simpleTypeName='void' or @simpleTypeName='Void']" mode="signature">
        <i><xsl:apply-templates select="." mode="linkname"/>
        <xsl:value-of select="@dimension"/></i>
    </xsl:template>
    
    <xsl:template match="returns" mode="href">
        <xsl:variable name="type-package" select="@packageName"/>
        <xsl:variable name="type-name" select="@simpleTypeName"/>
        <xsl:if test="/javadoc/package[@name=$type-package]/class[@name=$type-name]">
            <xsl:attribute name="href">../<xsl:value-of select="@packageName"/>/<xsl:value-of select="@qualifiedTypeName"/>.html</xsl:attribute>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="returns" mode="linkname">
        <xsl:variable name="type-package" select="@packageName"/>
        <xsl:variable name="type-name" select="@simpleTypeName"/>
        <xsl:choose>
            <xsl:when test="/javadoc/package[@name=$type-package]/class[@name=$type-name]">
                <xsl:value-of select="@simpleTypeName"/>    
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="@qualifiedTypeName"/>    
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="function | method | constructor" mode="toc-signature">
        <xsl:apply-templates select="modifiers"/>
        <xsl:text> </xsl:text>
        
        <!-- fx -->
        <xsl:if test="not(../@language='java')">
            <a>
                <xsl:attribute name="href">
                    <xsl:text>../</xsl:text>
                    <xsl:value-of select="../@packageName"/>
                    <xsl:text>/</xsl:text>
                    <xsl:value-of select="../@qualifiedName"/>
                    <xsl:text>.html</xsl:text>
                    <xsl:text>#</xsl:text>
                    <xsl:apply-templates select="." mode="anchor-signature"/>
                </xsl:attribute>
                <b><xsl:value-of select="@name"/></b>
            </a>
            <xsl:apply-templates select="parameters" mode="signature"/>
            :
            <!-- build return type link, if appropriate -->
            <xsl:apply-templates select="returns" mode="signature"/>
            <xsl:value-of select="type/@dimension"/>
        </xsl:if>
        
        <!-- java -->
        <xsl:if test="../@language='java'">
            <xsl:apply-templates select="returns" mode="signature"/>
            <xsl:text> </xsl:text>
            <b><xsl:value-of select="@name"/></b>
            <xsl:apply-templates select="parameters" mode="signature"/>
        </xsl:if>
            
    </xsl:template>
    
    <xsl:template match="function | method | constructor" mode="detail-signature">
        <xsl:apply-templates select="modifiers"/>
        <xsl:text> </xsl:text>
        
        <!-- fx -->
        <xsl:if test="not(../@language='java')">
            <b><xsl:value-of select="@name"/></b>
            <xsl:apply-templates select="parameters" mode="signature"/>
            <xsl:text>:</xsl:text>
            <!-- build return type link, if appropriate -->
            <xsl:apply-templates select="returns" mode="signature"/>
            <xsl:value-of select="type/@dimension"/>
        </xsl:if>
        
        <!-- java -->
        <xsl:if test="../@language='java'">
            <xsl:apply-templates select="returns" mode="signature"/>
            <xsl:text> </xsl:text>
            <b><xsl:value-of select="@name"/></b>
            <xsl:apply-templates select="parameters" mode="signature"/>
        </xsl:if>
            
    </xsl:template>






    <!-- extension templates for custom XSLTs to override -->
    <xsl:template name="extra-attribute"></xsl:template>
    <xsl:template name="extra-attribute-full"></xsl:template>
    <xsl:template name="extra-attribute-toc"></xsl:template>
    <xsl:template name="head-post"></xsl:template>
    <xsl:template name="header-pre"></xsl:template>
    <xsl:template name="extra-attribute-column-header"></xsl:template>
    <xsl:template name="extra-attribute-column-data"></xsl:template>
    <xsl:template name="attribute-table-width">3</xsl:template>

</xsl:stylesheet>
                
