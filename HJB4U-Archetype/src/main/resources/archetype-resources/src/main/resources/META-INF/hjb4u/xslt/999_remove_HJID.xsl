<!--
  ~ HJB4U is toolchain for creating a HyperJAXB front end for database users.
  ~ Copyright (C) 2010  NigelB
  ~
  ~ This program is free software; you can redistribute it and/or
  ~ modify it under the terms of the GNU General Public License
  ~ as published by the Free Software Foundation; either version 2
  ~ of the License, or (at your option) any later version.
  ~
  ~ This program is distributed in the hope that it will be useful,
  ~ but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~ MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~ GNU General Public License for more details.
  ~
  ~ You should have received a copy of the GNU General Public License
  ~ along with this program; if not, write to the Free Software
  ~ Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
  -->

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