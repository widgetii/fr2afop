<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" version="1.0" encoding="UTF-8" omit-xml-declaration="no" indent="yes" />

	<!-- ============ -->
	<!-- Identity Template -->
	<!-- ============ -->
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>

	<!-- ============================ -->
	<!-- template: set-variable-value -->
	<!-- ============================ -->
	<xsl:template name="set-variable-value">
		<xsl:param name="variable" />
		<xsl:param name="value" />

		<xsl:element name="Variable">
			<xsl:attribute name="Name"><xsl:value-of select="$variable/@Name" /></xsl:attribute>
			<xsl:attribute name="Type">expression</xsl:attribute>
			<xsl:attribute name="Content"><xsl:value-of select="$value" /></xsl:attribute>
			<xsl:value-of select="$variable" />
		</xsl:element>
	</xsl:template>

	<!-- ======================================= -->
	<!-- Replace MemoView to BarcodeView by font -->
	<!-- ======================================= -->
	<xsl:template match="MemoView[@FontName=('IntP36DlTt', 'C39HrP24DmTt')]">
		<xsl:element name="BarCodeView">
			<xsl:attribute name="Name"><xsl:value-of select="attribute::Name" /></xsl:attribute>
			<xsl:attribute name="Left"><xsl:value-of select="attribute::Left" /></xsl:attribute>
			<xsl:attribute name="Top"><xsl:value-of select="attribute::Top" /></xsl:attribute>
			<xsl:attribute name="Width"><xsl:value-of select="attribute::Width" /></xsl:attribute>
			<xsl:attribute name="Height"><xsl:value-of select="attribute::Height" /></xsl:attribute>
			<xsl:attribute name="FrameType"><xsl:value-of select="attribute::FrameType" /></xsl:attribute>
			<xsl:attribute name="FrameStyle"><xsl:value-of select="attribute::FrameStyle" /></xsl:attribute>
			<xsl:attribute name="FrameWidth"><xsl:value-of select="attribute::FrameWidth" /></xsl:attribute>
			<xsl:attribute name="FrameColor"><xsl:value-of select="attribute::FrameColor" /></xsl:attribute>
			<xsl:attribute name="FillColor"><xsl:value-of select="attribute::FillColor" /></xsl:attribute>
			<xsl:attribute name="Visible"><xsl:value-of select="attribute::Visible" /></xsl:attribute>

			<xsl:attribute name="CheckSum">true</xsl:attribute>
			<xsl:attribute name="ShowText">true</xsl:attribute>
			<xsl:attribute name="Angle">0</xsl:attribute>

			<xsl:choose>
				<xsl:when test="attribute::FontName='IntP36DlTt'">
					<xsl:attribute name="BarCodeType">intl2of5</xsl:attribute>
				</xsl:when>
				<xsl:when test="attribute::FontName='C39HrP24DmTt'">
					<xsl:attribute name="BarCodeType">code39</xsl:attribute>
				</xsl:when>
			</xsl:choose>

			<xsl:element name="Memo">
				<xsl:value-of select="Memo" />
			</xsl:element>
		</xsl:element>
	</xsl:template>

	<!-- ============================================== -->
	<!-- Replace Variable with name or content 'barcod' -->
	<!-- ============================================== -->
	<xsl:template match="Variable[@Name=('barkod') or @Content=('barkod')]">
		<xsl:call-template name="set-variable-value">
			<xsl:with-param name="variable" select="current()" />
			<xsl:with-param name="value" select="1234567890" />
		</xsl:call-template>
	</xsl:template>

	<!-- ============================================== -->
	<!-- Replace Variable with name or content 'barcodl' -->
	<!-- ============================================== -->
	<xsl:template match="Variable[@Name=('barcodl') or @Content=('barcodl')]">
		<xsl:call-template name="set-variable-value">
			<xsl:with-param name="variable" select="current()" />
			<xsl:with-param name="value" />
		</xsl:call-template>
	</xsl:template>
</xsl:stylesheet>
