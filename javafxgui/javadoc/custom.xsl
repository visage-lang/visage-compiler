<?xml version="1.0" encoding="UTF-8"?>

<!--
    Author     : joshua.marinacci@sun.com
    Description: customize the output with special doctags for use only
    by the JavaFX GUI project.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="html"/>
    <xsl:import href="javadoc.xsl"/>
    
    
    <xsl:template match="docComment/tags/treatasprivate">
        <p class="needsreview">This attribute should be treated as private</p>
    </xsl:template>
    <xsl:template match="docComment/tags/readonly">
        <p class="needsreview">This attribute is readonly. It's value may change, but developers should not try to set it.</p>
    </xsl:template>
    
    <xsl:template match="docComment/tags/setonce">
        <p class="setonce">Note: this attribute can only be set once. Any changes after
        the constructor is called will be ignored.</p>
    </xsl:template>
    
    <xsl:template match="docComment/tags/defaultvalue">
        <p class="defaultvalue">
            <span>Default value:</span> 
            <xsl:text> </xsl:text>
            <b><xsl:value-of select="."/></b>
        </p>
    </xsl:template>

    <xsl:template name="extra-attribute">
        <xsl:if test="docComment/tags/treatasprivate">
            <xsl:text>private</xsl:text>
        </xsl:if>
    </xsl:template>
    <xsl:template name="extra-method">
        <xsl:if test="docComment/tags/treatasprivate">
            <xsl:text>private</xsl:text>
        </xsl:if>
    </xsl:template>
</xsl:stylesheet>
