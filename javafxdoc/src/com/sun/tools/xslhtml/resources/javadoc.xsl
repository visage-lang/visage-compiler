<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="html"/>
    
    <xsl:variable name="use-toc-tables">true</xsl:variable>
    <xsl:param name="master-css">master.css</xsl:param>
    <xsl:param name="target-class">javafx.ui.ToggleButton</xsl:param>

    
    
    <xsl:template match="/">
        
        <xsl:if test="not (/classList)">
            <xsl:apply-templates select="//class[@qualifiedName=$target-class]"/>
            <!--
            <xsl:apply-templates select="//abstractClass[@qualifiedName=$target-class]"/>
            <xsl:apply-templates select="//interface[@qualifiedName=$target-class]"/>
            -->
        </xsl:if>
        
        <xsl:apply-templates select="/classList"/>
        <xsl:apply-templates select="/packageList"/>
        
    </xsl:template>
    
    
    
    <!-- indexes -->
    
    <xsl:template match="packageList">
        <html>
            <head>
                <link href="{$master-css}" rel="stylesheet"/>
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
    
    
    
    
    <xsl:template match="classList">
        <html>
            <head>
                <link href="../{$master-css}" rel="stylesheet"/>
            </head>
            <body>
                <p><b><xsl:value-of select="@packageName"/></b></p>
                <ul id="classList">
                    <xsl:for-each select="class">
                        <li>
                            <a target='classFrame'>
                                <xsl:attribute name="href"><xsl:value-of select="@qualifiedName"/>.html</xsl:attribute>
                                <xsl:value-of select="@name"/>
                            </a>
                        </li>
                    </xsl:for-each>
                </ul>
            </body>
        </html>
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
    
    
    <!-- the actual class -->
    
    <xsl:template name="classOutput">
        <html>
            <head>
                <link href="../{$master-css}" rel="stylesheet"/>
                <style type="text/css">
                </style>
            </head>
            <body>
                <xsl:call-template name="header"/>
                <div id="content">
                    <a id="overview"><h3>Overview</h3></a>
                    <div class="overview">
                        <xsl:apply-templates select="docComment/commentText"/>
                    </div>
                    <xsl:call-template name="toc"/>
                    <xsl:call-template name="inherited"/>
                    <xsl:call-template name="members"/>
                </div>
            </body>
        </html>
    </xsl:template>
    
    <xsl:template match="docComment/commentText">
        <p class="comment">
            <xsl:value-of select="." disable-output-escaping="yes"/>
        </p>
    </xsl:template>
    
    <xsl:template match="class" mode="super">
        <xsl:variable name="super" select="superclass/@qualifiedTypeName"/>
        <xsl:apply-templates select="//class[@qualifiedName=$super]" mode="super"/>
        &gt;
        <a>
            <xsl:attribute name="href">../<xsl:value-of select="@packageName"/>/<xsl:value-of select="@packageName"/>.<xsl:value-of select="@name"/>.html</xsl:attribute>
            <strong><xsl:value-of select="@packageName"/>.</strong>
            <b><xsl:value-of select="@name"/></b>
        </a>
    </xsl:template>
    
    
    <!-- header -->
    <xsl:template name="header">
        <div id="nav">
            <h1 class="classname">
                <i class="modifiers">
                    <xsl:value-of select="modifiers/@text"/>
                </i>
                class
                <a class="classname">
                    <strong><xsl:value-of select="@packageName"/>.</strong>
                    <b><xsl:value-of select="@name"/></b>
                </a>
            </h1>
            
            
            <h2>
                <xsl:variable name="blah" select="superclass/@qualifiedTypeName"/>
                <xsl:apply-templates select="//class[@qualifiedName=$blah]" mode="super"/>
                &gt;
                <a href="#">
                    <strong><xsl:value-of select="@packageName"/>.</strong>
                    <b><xsl:value-of select="@name"/></b>
                </a>
            </h2>
            
            <xsl:if test="@language='javafx'">
                <ul>
                    <li><a href="#overview">overview</a></li><li><a href="#fields-summary">attributes</a></li><li><a href="#methods-summary">functions</a></li>
                </ul>
            </xsl:if>
            <xsl:if test="@language='java'">
                <ul>
                    <li><a href="#overview">overview</a></li><li><a href="#fields-summary">fields</a></li><li><a href="#constructors-summary">constructors</a></li><li><a href="#methods-summary">methods</a></li>
                </ul>
            </xsl:if>
            
        </div>
    </xsl:template>
    
    
    
    <!--   ====  The Table of Contents ====  -->
    
    <xsl:template name="toc">
        <div id="toc">
            
            <xsl:if test="count(attribute) > 0">
                <a id="fields-summary"><h3>Attribute Summary</h3></a>
                <table>
                    <tr><th>name</th><th>type</th><th>description</th></tr>
                    <tr><th colspan="3">Public</th></tr>
                    <xsl:for-each select="attribute[modifiers/public]">
                        <xsl:sort select="@name" order="ascending"/>
                        <xsl:apply-templates select="." mode="toc"/>
                    </xsl:for-each>
                    <tr><th colspan="3">Protected</th></tr>
                    <xsl:for-each select="attribute[modifiers/protected]">
                        <xsl:sort select="@name" order="ascending"/>
                        <xsl:apply-templates select="." mode="toc"/>
                    </xsl:for-each>
                </table>
            </xsl:if>
            
            <xsl:if test="count(field) > 0">
                <a id="fields-summary"><h3>Field Summary</h3></a>
                <table class="fields">
                    <tr><th>public</th><th>name</th><th>type</th></tr>
                    <xsl:for-each select="field">
                        <xsl:sort select="@name" order="ascending"/>
                        <xsl:apply-templates select="." mode="toc"/>
                    </xsl:for-each>
                </table>
            </xsl:if>
            
            <xsl:if test="count(function) > 0">
                <a id="methods-summary"><h3>Function Summary</h3></a>
                <dl>
                    <xsl:for-each select="function">
                        <xsl:sort select="@name" order="ascending"/>
                        <xsl:call-template name="method-like-toc"/>
                    </xsl:for-each>
                </dl>
            </xsl:if>
            
            <xsl:if test="count(method) > 0">
                <a id="methods-summary"><h3>Method Summary</h3></a>
                <ul>
                    <xsl:for-each select="method">
                        <xsl:sort select="@name" order="ascending"/>
                        <xsl:call-template name="method-like-toc"/>
                    </xsl:for-each>
                </ul>
            </xsl:if>
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
    
    
    <xsl:template name="inherited"/>
    
    
    <!-- Attributes -->
    
    <xsl:template match="attribute" mode="toc">
        <tr class="attribute">
            <td>
                <a>
                    <xsl:attribute name="href">#attribute_<xsl:value-of select="@name"/></xsl:attribute>
                    <b class="name"><xsl:value-of select="@name"/></b>
                </a>
            </td>
            <td>
                <i class="type"><xsl:value-of select="type/@simpleTypeName"/></i>
            </td>
            <td>
                <xsl:value-of select="docComment/firstSentenceTags/Text"
                              disable-output-escaping="yes"/>
            </td>
        </tr>
    </xsl:template>
    
    
    
    <xsl:template match="attribute">
        <div class="attribute member">
            <a>
                <h4>
                    <xsl:attribute name="id">attribute_<xsl:value-of select="@name"/></xsl:attribute>
                    <i class="modifiers"><xsl:value-of select="modifiers/@text"/></i>
                    <xsl:text> </xsl:text>
                    <b class="name"><xsl:value-of select="@name"/></b>
                    <xsl:text>: </xsl:text>
                    <i class="type"><xsl:value-of select="type/@simpleTypeName"/></i>
                </h4>
            </a>
            
            <xsl:apply-templates select="docComment/commentText"/>
        </div>
    </xsl:template>
    
    
    
    
    
    
    <!--  Functions -->
    
    <xsl:template name="method-like-toc">
        <dt>
            <a>
                <xsl:attribute name="href">#method_<xsl:value-of select="@name"/></xsl:attribute>
                
                <i class="modifiers"><xsl:value-of select="modifiers/@text"/></i>
                <xsl:text> </xsl:text>
                <b><xsl:value-of select="@name"/></b>
                (
                <i class="parameters">
                    <xsl:for-each select="parameters/parameter">
                        <b><xsl:value-of select="@name"/></b>:
                        <i><xsl:value-of select="type/@toString"/></i>,
                    </xsl:for-each>
                </i>
                )
                :
                <i><xsl:value-of select="returns/@simpleTypeName"/>
                <xsl:value-of select="type/@dimension"/></i>
            </a>
        </dt>
        <dd>
            <xsl:value-of select="docComment/firstSentenceTags/Text"
                          disable-output-escaping="yes"/>
        </dd>
    </xsl:template>
    
    <xsl:template name="method-like">
        <div class="method member">
            <a>
                <xsl:attribute name="id">method_<xsl:value-of select="@name"/></xsl:attribute>
                <h4>
                    <i class="modifiers"><xsl:value-of select="modifiers/@text"/></i>
                    <xsl:text> </xsl:text>
                    <b><xsl:value-of select="@name"/></b>
                    (
                    <i class="parameters">
                        <xsl:for-each select="parameters/parameter">
                            <b><xsl:value-of select="@name"/></b>:
                            <i><xsl:value-of select="type/@toString"/></i>,
                        </xsl:for-each>
                    </i>
                    )
                    :
                    <i><xsl:value-of select="returns/@simpleTypeName"/>
                    <xsl:value-of select="type/@dimension"/></i>
                </h4>
            </a>
            
            
            <xsl:if test="parameters/parameter">
                <dl class="parameters">
                    Parameters
                    <xsl:for-each select="parameters/parameter">
                        <dt><xsl:value-of select="@name"/></dt>
                        <dd><xsl:value-of select="type/@toString"/></dd>
                    </xsl:for-each>
                </dl>
            </xsl:if>
            
            <dl class="returns">
                Returns
                <dt><xsl:value-of select="returns/@simpleTypeName"/></dt>
                <dd><xsl:value-of select="type/@dimension"/></dd>
            </dl>
            
            <xsl:apply-templates select="docComment/commentText"/>
            
        </div>  
    </xsl:template>
    
    
    
    <!--  Functions -->
    <!--    
    <xsl:template match="method" mode="toc">
        <div class="function">
            <li>
                <a>
                    <xsl:attribute name="href">#method_<xsl:value-of select="@name"/></xsl:attribute>
                    
                    <i class="modifiers"><xsl:value-of select="modifiers/@text"/></i>
                    <xsl:text> </xsl:text>
                    <b><xsl:value-of select="@name"/></b>
                    (
                    <i class="parameters">
                        <xsl:for-each select="parameters/parameter">
                            <b><xsl:value-of select="@name"/></b>:
                            <i><xsl:value-of select="type/@toString"/></i>,
                        </xsl:for-each>
                    </i>
                    )
                    :
                    <i><xsl:value-of select="returns/@simpleTypeName"/>
                    <xsl:value-of select="type/@dimension"/></i>
                </a>
            </li>
        </div>  
    </xsl:template>
    
    <xsl:template match="method">
        <div class="function">
            <h4>
                <a>
                    <xsl:attribute name="id">method_<xsl:value-of select="@name"/></xsl:attribute>
                    <i class="modifiers"><xsl:value-of select="modifiers/@text"/></i>
                    <xsl:text> </xsl:text>
                    <b><xsl:value-of select="@name"/></b>
                    (
                    <i class="parameters">
                        <xsl:for-each select="parameters/parameter">
                            <i><xsl:value-of select="type/@toString"/></i>
                            <b><xsl:value-of select="@name"/></b>,
                        </xsl:for-each>
                    </i>
                    )
                    :
                    <i><xsl:value-of select="returns/@simpleTypeName"/>
                    <xsl:value-of select="type/@dimension"/></i>
                </a>
            </h4>
        </div>  
    </xsl:template>
    
    -->
</xsl:stylesheet>
