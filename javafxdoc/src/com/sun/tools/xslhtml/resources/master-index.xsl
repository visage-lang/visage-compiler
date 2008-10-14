<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : master-index.xsl
    Created on : September 11, 2008, 1:06 PM
    Author     : joshua.marinacci@sun.com
    Description: generate a master index of every variable and function in the entire api
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="html"/>
    <xsl:import href="sdk.xsl"/>
    <xsl:template match="/">
        <html>
            <head>
                <link href="{$root-path}{$master-css}" rel="stylesheet"/>
                <xsl:if test="$extra-css">
                    <link href="{$root-path}{$extra-css}" rel="stylesheet"/>
                </xsl:if>
                <xsl:if test="$extra-js">
                    <script src="{$root-path}{$extra-js}"></script>
                </xsl:if>
                <xsl:call-template name="head-post"/>
            </head>
            <body>
                <xsl:call-template name="header-pre"/>
                <ul id="master-list"><!-- |//class | //function-->
                    <xsl:for-each select="//attribute | //function | //class">
                        <xsl:sort select="@name"/>
                        <li>
                            <xsl:attribute name="class">
                                <xsl:call-template name="profile-class"/>    
                                <xsl:if test="docComment/tags/treatasprivate">
                                    <xsl:text>private</xsl:text>
                                </xsl:if>
                            </xsl:attribute>
                            <xsl:apply-templates select="."/>
                        </li>
                    </xsl:for-each>
                </ul>
            </body>
        </html>
    </xsl:template>
    
    
    <xsl:template match="class">
        <a>
            <xsl:apply-templates select="." mode="href"/>
            <xsl:value-of select="@name"/>
        </a>
        - class in package <b><xsl:value-of select="@packageName"/></b>
    </xsl:template>
    <xsl:template match="attribute">
        <a>
            <xsl:apply-templates select="." mode="href"/>
            <xsl:value-of select="@name"/>
        </a>
        - variable in class 
        <a>
            <xsl:apply-templates select=".." mode="href"/>
            <xsl:value-of select="../@qualifiedName"/>
        </a>
    </xsl:template>
    <xsl:template match="function">
        <a>
            <xsl:apply-templates select="." mode="href"/>
            <xsl:value-of select="@name"/>
        </a>
        - function in class
        <a>
            <xsl:apply-templates select=".." mode="href"/>
            <xsl:value-of select="../@qualifiedName"/>
        </a>
    </xsl:template>
    <xsl:template match="*"></xsl:template>

</xsl:stylesheet>
