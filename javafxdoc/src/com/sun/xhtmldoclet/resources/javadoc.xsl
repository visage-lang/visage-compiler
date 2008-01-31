<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="html"/>
    
    
    
    <xsl:template match="/">
        <html>
            <xsl:apply-templates select="/class"/>
            <xsl:apply-templates select="/abstractClass"/>
            <xsl:apply-templates select="/interface"/>
            <xsl:apply-templates select="/classList"/>
            <xsl:apply-templates select="/packageList"/>
        </html>
    </xsl:template>
    
    
    
    
    
    <xsl:template match="packageList">
        <head>
            <link href="master.css" rel="stylesheet"/>
        </head>
        <body>
            <ul id="packageList">
                <xsl:for-each select="package">
                    <li>
                        <a target='classListFrame'>
                            <xsl:attribute name="href"><xsl:value-of select="@name"/>/classes.html</xsl:attribute>
                            <xsl:value-of select="@name"/>
                        </a>
                    </li>
                </xsl:for-each>
            </ul>
        </body>
    </xsl:template>
    
    
    
    
    <xsl:template match="classList">
        <head>
            <link href="../master.css" rel="stylesheet"/>
        </head>
        <body>
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
    
    
    
    
    <xsl:template name="classOutput">
        <head>
            <link href="../master.css" rel="stylesheet"/>
        </head>
        <body>
            <h1 id="header">
                <i class="modifiers">
                    <xsl:value-of select="modifiers/@text"/>
                </i>
                <xsl:text> </xsl:text>
                <xsl:value-of select="name()"/>
                <xsl:text> </xsl:text>
                <i class="classname"><xsl:value-of select="@name"/></i>
                <xsl:if test="name() != 'interface'">
                    extends
                    <a class="superclass">
                        <xsl:attribute name="href">
                            <xsl:value-of select="superclass/@qualifiedTypeName"/>
                        </xsl:attribute>
                        <xsl:value-of select="superclass/@simpleTypeName"/>
                    </a>
                </xsl:if>
            </h1>
            
            
            <h2 id="navigation">
                <xsl:if test="attribute">
                    <a href="#toc_attributes">attributes</a>
                    <xsl:text> </xsl:text>
                </xsl:if>
                <xsl:if test="field">
                    <a href="#toc_fields">fields</a>
                    <xsl:text> </xsl:text>
                </xsl:if>
                <xsl:if test="function">
                    <a href="#toc_functions">functions</a>
                    <xsl:text> </xsl:text>
                </xsl:if>
                <xsl:if test="method">
                    <a href="#toc_methods">methods</a>
                    <xsl:text> </xsl:text>
                </xsl:if>
            </h2>
            
            <div id="class-comment" class="comment">
                <xsl:apply-templates select="docComment/commentText"/>
            </div>
            
            <div id="toc">
                
                <xsl:if test="attribute">
                    <a id="toc_attributes"><h3>Attributes</h3></a>
                    <table class="attributes">
                        <tr><th>public</th><th>name</th><th>type</th></tr>
                        <xsl:for-each select="attribute">
                            <xsl:sort select="@name" order="ascending"/>
                            <xsl:apply-templates select="." mode="toc"/>
                        </xsl:for-each>
                    </table>
                </xsl:if>
                
                <xsl:if test="field">
                    <a id="toc_fields"><h3>Fields</h3></a>
                    <table class="fields">
                        <tr><th>public</th><th>name</th><th>type</th></tr>
                        <xsl:for-each select="field">
                            <xsl:sort select="@name" order="ascending"/>
                            <xsl:apply-templates select="." mode="toc"/>
                        </xsl:for-each>
                    </table>
                </xsl:if>
                
                
                <xsl:if test="function">
                    <a id="toc_functions"><h3>Functions</h3></a>
                    <ul>
                        <xsl:for-each select="function">
                            <xsl:sort select="@name" order="ascending"/>
                            <xsl:apply-templates select="." mode="toc"/>
                        </xsl:for-each>
                    </ul>
                </xsl:if>
                
                <xsl:if test="method">
                    <a id="toc_methods"><h3>Methods</h3></a>
                    <ul>
                        <xsl:for-each select="method">
                            <xsl:sort select="@name" order="ascending"/>
                            <xsl:apply-templates select="." mode="toc"/>
                        </xsl:for-each>
                    </ul>
                </xsl:if>
                
            </div>
            
            
            
            <xsl:if test="attribute">
                <div class="attributes">
                    <h2>Attributes</h2>
                    <xsl:for-each select="attribute">
                        <xsl:sort select="@name" order="ascending"/>
                        <xsl:apply-templates select="."/>
                    </xsl:for-each>
                </div>
            </xsl:if>
            
            <xsl:if test="field">
                <div class="fields">
                    <h2>Fields</h2>
                    <xsl:for-each select="field">
                        <xsl:sort select="@name" order="ascending"/>
                        <xsl:apply-templates select="."/>
                    </xsl:for-each>
                </div>
            </xsl:if>
            
            <xsl:if test="function">
                <div class="functions">
                    <h2>Functions</h2>
                    <xsl:for-each select="function">
                        <xsl:sort select="@name" order="ascending"/>
                        <xsl:apply-templates select="."/>
                    </xsl:for-each>
                </div>
            </xsl:if>
            
            <xsl:if test="method">
                <div class="methods">
                    <h2>Methods</h2>
                    <xsl:for-each select="method">
                        <xsl:sort select="@name" order="ascending"/>
                        <xsl:apply-templates select="."/>
                    </xsl:for-each>
                </div>
            </xsl:if>
            
        </body>
    </xsl:template>
    
    
    <!-- class comments -->
    <xsl:template match="docComment/commentText">
        <p class="comment">
            <xsl:value-of select="." disable-output-escaping="yes"/>
        </p>
    </xsl:template>
    
    
    
    
    
    
    
    
    <!-- Attributes and fields -->
    <xsl:template match="attribute" mode="toc">
        <tr class="attribute">
            <td>
                <i class="modifiers"><xsl:value-of select="modifiers/@text"/></i>
            </td>
            
            <td>
                <a>
                    <xsl:attribute name="href">#attribute_<xsl:value-of select="@name"/></xsl:attribute>
                    <b class="name"><xsl:value-of select="@name"/></b>
                </a>
            </td>
            <td>
                <i class="type"><xsl:value-of select="type/@simpleTypeName"/></i>
            </td>
        </tr>
    </xsl:template>
    
    
    
    <xsl:template match="attribute">
        <div class="attribute">
            <a>
                <h4>
                    <xsl:attribute name="id">attribute_<xsl:value-of select="@name"/></xsl:attribute>
                    <i class="modifiers"><xsl:value-of select="modifiers/@text"/></i>
                    <xsl:text> </xsl:text>
                    <b class="name"><xsl:value-of select="@name"/></b>
                    <xsl:text>: </xsl:text>
                    <i class="type"><xsl:value-of select="type/@SimpleTypeName"/></i>
                </h4>
            </a>
            
            <xsl:apply-templates select="docComment/commentText"/>
        </div>
    </xsl:template>
    
    
    <xsl:template match="field" mode="toc">
        <tr class="field">
            <td>
                <i class="modifiers"><xsl:value-of select="modifiers/@text"/></i>
            </td>
            
            <td>
                <a>
                    <xsl:attribute name="href">#attribute_<xsl:value-of select="@name"/></xsl:attribute>
                    <b class="name"><xsl:value-of select="@name"/></b>
                </a>
            </td>
            <td>
                <i class="type"><xsl:value-of select="type/@simpleTypeName"/></i>
            </td>
        </tr>
    </xsl:template>
    
    
    
    <xsl:template match="field">
        <div class="field">
            <a>
                <h4>
                    <xsl:attribute name="id">attribute_<xsl:value-of select="@name"/></xsl:attribute>
                    <i class="modifiers"><xsl:value-of select="modifiers/@text"/></i>
                    <xsl:text> </xsl:text>
                    <b class="name"><xsl:value-of select="@name"/></b>
                    <xsl:text>: </xsl:text>
                    <i class="type"><xsl:value-of select="type/@SimpleTypeName"/></i>
                </h4>
            </a>
            
            <xsl:apply-templates select="docComment/commentText"/>
        </div>
    </xsl:template>
    
    
    
    
    
    
    
    
    
    
    <!--  Functions and Methods -->
    
    <xsl:template match="function" mode="toc">
        <li class="function">
            <a>
                <xsl:attribute name="href">#function_<xsl:value-of select="@name"/></xsl:attribute>
                
                <i class="modifiers"><xsl:value-of select="modifiers/@text"/></i>
                <xsl:text> </xsl:text>
                <b><xsl:value-of select="@name"/></b>
                (
                <i class="parameters">
                    <xsl:for-each select="parameters/parameter">
                        <b><xsl:value-of select="@name"/></b>:
                        <i><xsl:value-of select="type/@qualifiedTypeName"/></i>,
                    </xsl:for-each>
                </i>
                )
                :
                <i><xsl:value-of select="returns/@simpleTypeName"/></i>
            </a>
        </li>
    </xsl:template>
    
    <xsl:template match="function">
        <div class="function">
            <h4>
                <a>
                    <xsl:attribute name="id">function_<xsl:value-of select="@name"/></xsl:attribute>
                    <i class="modifiers"><xsl:value-of select="modifiers/@text"/></i>
                    <xsl:text> </xsl:text>
                    <b><xsl:value-of select="@name"/></b>
                    (
                    <i class="parameters">
                        <xsl:for-each select="parameters/parameter">
                            <b><xsl:value-of select="@name"/></b>:
                            <i><xsl:value-of select="type/@qualifiedTypeName"/></i>,
                        </xsl:for-each>
                    </i>
                    )
                    :
                    <i><xsl:value-of select="returns/@simpleTypeName"/></i>
                </a>
            </h4>
            <xsl:apply-templates select="docComment/commentText"/>
        </div>  
    </xsl:template>
    
    
    <xsl:template match="method" mode="toc">
        <li class="method">
            <a>
                <xsl:attribute name="href">#method_<xsl:value-of select="@name"/></xsl:attribute>
                
                <i class="modifiers"><xsl:value-of select="modifiers/@text"/></i>
                <xsl:text> </xsl:text>
                <i><xsl:value-of select="returns/@simpleTypeName"/></i>
                <xsl:text> </xsl:text>
                <b><xsl:value-of select="@name"/></b>
                (
                <i class="parameters">
                    <xsl:for-each select="parameters/parameter">
                        <i><xsl:value-of select="type/@simpleTypeName"/></i>
                        <xsl:text> </xsl:text>
                        <b><xsl:value-of select="@name"/></b>,
                    </xsl:for-each>
                </i>
                )
                
            </a>
        </li>
    </xsl:template>
    
    <xsl:template match="method">
        <div class="method">
            <h4>
                <a>
                    <xsl:attribute name="id">method_<xsl:value-of select="@name"/></xsl:attribute>
                    <i class="modifiers"><xsl:value-of select="modifiers/@text"/></i>
                    <xsl:text> </xsl:text>
                    <i><xsl:value-of select="returns/@simpleTypeName"/></i>
                    <xsl:text> </xsl:text>
                    <b><xsl:value-of select="@name"/></b>
                    (
                    <i class="parameters">
                        <xsl:for-each select="parameters/parameter">
                            <i><xsl:value-of select="type/@simpleTypeName"/></i>
                            <xsl:text> </xsl:text>
                            <b><xsl:value-of select="@name"/></b>,
                        </xsl:for-each>
                    </i>
                    )
                </a>
            </h4>
            <xsl:apply-templates select="docComment/commentText"/>
        </div>  
    </xsl:template>
    
    
    
    
    
</xsl:stylesheet>
