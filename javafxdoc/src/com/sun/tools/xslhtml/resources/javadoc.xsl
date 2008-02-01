<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="html"/>
    <xsl:template match="/">
        <html>
            <head>
                <link href="../demo.css" rel="stylesheet"/>
            </head>
            <body>
                <xsl:apply-templates select="/class"/>
                <xsl:apply-templates select="/abstractClass"/>
                <xsl:apply-templates select="/interface"/>
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
    
    
    <xsl:template name="classOutput">
        <div class="class">
            <h1 class="classname">
                <i class="modifiers">
                    <xsl:value-of select="modifiers/@text"/>
                </i>
                class
                <i class="classname"><xsl:value-of select="@name"/></i>
                extends
                <i class="superclass">
                    <xsl:value-of select="superclass/@toString"/>
                </i>
                <!--
                implements
                -->
            </h1>
            
            <div class="class-comment">
                <xsl:apply-templates select="docComment/commentText"/>
            </div>
            
            <div class="toc">
                
                <xsl:if test="count(attribute) > 0">
                    <h3>Attributes</h3>
                    <table class="attributes">
                        <tr><th>public</th><th>name</th><th>type</th></tr>
                        <xsl:for-each select="attribute">
                            <xsl:sort select="@name" order="ascending"/>
                            <xsl:apply-templates select="." mode="toc"/>
                        </xsl:for-each>
                    </table>
                </xsl:if>
                                
                <xsl:if test="count(field) > 0">
                    <h3>Fields</h3>
                    <table class="fields">
                        <tr><th>public</th><th>name</th><th>type</th></tr>
                        <xsl:for-each select="field">
                            <xsl:sort select="@name" order="ascending"/>
                            <xsl:apply-templates select="." mode="toc"/>
                        </xsl:for-each>
                    </table>
                </xsl:if>

                <xsl:if test="count(function) > 0">
                    <h3>Functions</h3>
                    <ul>
                        <xsl:for-each select="function">
                            <xsl:sort select="@name" order="ascending"/>
                            <xsl:apply-templates select="." mode="toc"/>
                        </xsl:for-each>
                    </ul>
                </xsl:if>
                
                <xsl:if test="count(method) > 0">
                    <h3>Methods</h3>
                    <ul>
                        <xsl:for-each select="method">
                            <xsl:sort select="@name" order="ascending"/>
                            <xsl:apply-templates select="." mode="toc"/>
                        </xsl:for-each>
                    </ul>
                </xsl:if>
            </div>
            
            <xsl:if test="count(attribute) > 0">
                <div class="attributes">
                    <h2>Attributes</h2>
                    <xsl:for-each select="attribute">
                        <xsl:sort select="@name" order="ascending"/>
                        <xsl:apply-templates select="."/>
                    </xsl:for-each>
                </div>
            </xsl:if>
            
            <xsl:if test="count(field) > 0">
                <div class="fields">
                    <h2>Fields</h2>
                    <xsl:for-each select="field">
                        <xsl:sort select="@name" order="ascending"/>
                        <xsl:apply-templates select="."/>
                    </xsl:for-each>
                </div>
            </xsl:if>

            <xsl:if test="count(function) > 0">
                <div class="functions">
                    <h2>functions</h2>
                    <xsl:for-each select="function">
                        <xsl:sort select="@name" order="ascending"/>
                        <xsl:apply-templates select="."/>
                    </xsl:for-each>
                </div>
            </xsl:if>
            
            <xsl:if test="count(method) > 0">
                <div class="methods">
                    <h2>Methods</h2>
                    <xsl:for-each select="method">
                        <xsl:sort select="@name" order="ascending"/>
                        <xsl:apply-templates select="."/>
                    </xsl:for-each>
                </div>
            </xsl:if>
            
        </div>
    </xsl:template>

    <xsl:template match="docComment/commentText">
        <p class="comment">
            <xsl:value-of select="." disable-output-escaping="yes"/>
        </p>
    </xsl:template>
    
    
    
    
    
    <!-- Attributes -->
    
    <xsl:template match="attribute" mode="toc">
        <div class="attribute">
            <tr>
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
                    <i class="type"><xsl:value-of select="type/@simpleTypeName"/>
                                    <xsl:value-of select="type/@dimension"/></i>
                </td>
            </tr>
         </div>
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
                    <i class="type"><xsl:value-of select="type/@SimpleTypeName"/>
                                    <xsl:value-of select="type/@dimension"/></i>
            </h4>
                </a>
            
            <xsl:apply-templates select="docComment/commentText"/>
         </div>
    </xsl:template>
    
    
    
    
    
    
    <!--  Functions -->
    
    <xsl:template match="function" mode="toc">
        <div class="function">
            <li>
                <a>
                <xsl:attribute name="href">#function_<xsl:value-of select="@name"/></xsl:attribute>

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
                    <i><xsl:value-of select="type/@toString"/></i>,
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
    
    
    
    <!--  Functions -->
    
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
</xsl:stylesheet>
