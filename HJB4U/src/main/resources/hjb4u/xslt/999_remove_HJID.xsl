<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

        <xsl:output omit-xml-declaration="no" indent="yes"/>

        <xsl:param name="removeElementsNamed" select="'Hjid'"/>

        <xsl:template match="node()|@*" name="identity">
            <xsl:copy>
                <xsl:apply-templates select="node()|@*"/>
            </xsl:copy>
        </xsl:template>

        <xsl:template match="node()|@*">
            <xsl:if test="not(name() = $removeElementsNamed)">
                <xsl:call-template name="identity"/>
            </xsl:if>
        </xsl:template>

</xsl:stylesheet>