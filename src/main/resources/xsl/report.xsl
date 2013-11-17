<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fn="http://www.w3.org/2005/xpath-functions" xmlns:fo="http://www.w3.org/1999/XSL/Format"
	xmlns:barcode="http://barcode4j.krysalis.org/ns">
	<xsl:output method="xml" version="1.0" encoding="UTF-8" cdata-section-elements="fo:block" omit-xml-declaration="no" indent="yes" />

	<!-- ============ -->
	<!-- root element -->
	<!-- ============ -->
	<xsl:template match="Pages">
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<fo:layout-master-set>
				<xsl:for-each select="Page">
					<xsl:call-template name="page-master">
						<xsl:with-param name="page" select="current()" />
						<xsl:with-param name="page-number" select="position()" />
					</xsl:call-template>
				</xsl:for-each>
			</fo:layout-master-set>

			<xsl:for-each select="Page">
				<xsl:call-template name="page-sequence">
					<xsl:with-param name="page" select="current()" />
					<xsl:with-param name="page-number" select="position()" />
				</xsl:call-template>
			</xsl:for-each>
		</fo:root>
	</xsl:template>

	<!-- ===================== -->
	<!-- template: page-master -->
	<!-- ===================== -->
	<xsl:template name="page-master">
		<xsl:param name="page" />
		<xsl:param name="page-number" />

		<xsl:variable name="page-header-height">
			<xsl:value-of select="$page/Views/BandView[@Type='page-header']/@Height" />
		</xsl:variable>

		<xsl:variable name="page-footer-height">
			<xsl:value-of select="$page/Views/BandView[@Type='page-footer']/@Height" />
		</xsl:variable>

		<fo:simple-page-master>
			<!-- Page Name -->
			<xsl:attribute name="master-name">page<xsl:value-of select="$page-number" /></xsl:attribute>

			<!-- Width/Height -->
			<xsl:attribute name="page-width"><xsl:value-of select="attribute::Width" />mm</xsl:attribute>
			<xsl:attribute name="page-height"><xsl:value-of select="attribute::Height" />mm</xsl:attribute>

			<!-- Margins -->
			<xsl:if test="attribute::UseMargins = 'true'">
				<xsl:attribute name="margin-top"><xsl:value-of select="attribute::MarginTop" />mm</xsl:attribute>
				<xsl:attribute name="margin-left"><xsl:value-of select="attribute::MarginLeft" />mm</xsl:attribute>
				<xsl:attribute name="margin-right"><xsl:value-of select="attribute::MarginRight" />mm</xsl:attribute>
				<xsl:attribute name="margin-bottom"><xsl:value-of select="attribute::MarginBottom" />mm</xsl:attribute>
			</xsl:if>

			<!-- Page body -->
			<fo:region-body>
				<xsl:if test="$page-header-height != ''">
					<xsl:attribute name="margin-top"><xsl:value-of select="$page-header-height" />%</xsl:attribute>
				</xsl:if>
				<xsl:if test="$page-footer-height != ''">
					<xsl:attribute name="margin-bottom"><xsl:value-of select="$page-footer-height" />%</xsl:attribute>
				</xsl:if>
			</fo:region-body>

			<!-- Page header -->
			<fo:region-before>
				<xsl:if test="$page-header-height != ''">
					<xsl:attribute name="extent"><xsl:value-of select="$page-header-height" />%</xsl:attribute>
				</xsl:if>
			</fo:region-before>

			<!-- Page footer -->
			<fo:region-after>
				<xsl:if test="$page-footer-height != ''">
					<xsl:attribute name="extent"><xsl:value-of select="$page-footer-height" />%</xsl:attribute>
				</xsl:if>
			</fo:region-after>
		</fo:simple-page-master>
	</xsl:template>

	<!-- ======================= -->
	<!-- template: page-sequence -->
	<!-- ======================= -->
	<xsl:template name="page-sequence">
		<xsl:param name="page" />
		<xsl:param name="page-number" />

		<!-- Calculate viewable area size -->
		<xsl:variable name="viewable-area-width">
			<xsl:choose>
				<xsl:when test="attribute::UseMargins = 'true'">
					<xsl:value-of select="attribute::Width - attribute::MarginLeft - attribute::MarginRight" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="attribute::Width" />
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>

		<xsl:variable name="viewable-area-height">
			<xsl:choose>
				<xsl:when test="attribute::UseMargins = 'true'">
					<xsl:value-of select="attribute::Height - attribute::MarginTop - attribute::MarginBottom" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="attribute::Height" />
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>

		<!-- Calculate page header and footer height -->
		<xsl:variable name="page-header-height">
			<xsl:choose>
				<xsl:when test="$page/Views/BandView[@Type='page-header']">
					<xsl:value-of select="$page/Views/BandView[@Type='page-header']/@Height * $viewable-area-height div 100" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="0" />
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>

		<xsl:variable name="page-footer-height">
			<xsl:choose>
				<xsl:when test="$page/Views/BandView[@Type='page-footer']">
					<xsl:value-of select="$page/Views/BandView[@Type='page-footer']/@Height * $viewable-area-height div 100" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="0" />
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>

		<fo:page-sequence>
			<!-- Page Name -->
			<xsl:attribute name="master-reference">page<xsl:value-of select="$page-number" /></xsl:attribute>

			<!-- Page header -->
			<xsl:call-template name="page-header">
				<xsl:with-param name="page" select="$page" />
				<xsl:with-param name="page-number" select="$page-number" />
				<xsl:with-param name="viewable-area-width" select="$viewable-area-width" />
				<xsl:with-param name="page-header-height" select="$page-header-height" />
			</xsl:call-template>

			<!-- Page footer -->
			<xsl:call-template name="page-footer">
				<xsl:with-param name="page" select="$page" />
				<xsl:with-param name="page-number" select="$page-number" />
				<xsl:with-param name="viewable-area-width" select="$viewable-area-width" />
				<xsl:with-param name="page-footer-height" select="$page-footer-height" />
			</xsl:call-template>

			<!-- Page content -->
			<xsl:call-template name="page-content">
				<xsl:with-param name="page" select="$page" />
				<xsl:with-param name="page-number" select="$page-number" />
				<xsl:with-param name="viewable-area-width" select="$viewable-area-width" />
				<xsl:with-param name="viewable-area-height" select="$viewable-area-height" />
				<xsl:with-param name="page-header-height" select="$page-header-height" />
				<xsl:with-param name="page-footer-height" select="$page-footer-height" />
			</xsl:call-template>
		</fo:page-sequence>
	</xsl:template>

	<!-- ===================== -->
	<!-- template: page-header -->
	<!-- ===================== -->
	<xsl:template name="page-header">
		<xsl:param name="page" />
		<xsl:param name="page-number" />
		<xsl:param name="viewable-area-width" />
		<xsl:param name="page-header-height" />

		<fo:static-content flow-name="xsl-region-before">
			<xsl:for-each select="$page/Views/BandView[@Type='page-header']/ChildViews/*">
				<xsl:call-template name="view-choose">
					<xsl:with-param name="viewable-area-width" select="$viewable-area-width" />
					<xsl:with-param name="viewable-area-height" select="$page-header-height" />
					<xsl:with-param name="page-number" select="$page-number" />
				</xsl:call-template>
			</xsl:for-each>

			<!-- Insert empty block just in case there are no other blocks -->
			<fo:block />
		</fo:static-content>
	</xsl:template>

	<!-- ===================== -->
	<!-- template: page-footer -->
	<!-- ===================== -->
	<xsl:template name="page-footer">
		<xsl:param name="page" />
		<xsl:param name="page-number" />
		<xsl:param name="viewable-area-width" />
		<xsl:param name="page-footer-height" />

		<fo:static-content flow-name="xsl-region-after">
			<xsl:for-each select="$page/Views/BandView[@Type='page-footer']/ChildViews/*">
				<xsl:call-template name="view-choose">
					<xsl:with-param name="viewable-area-width" select="$viewable-area-width" />
					<xsl:with-param name="viewable-area-height" select="$page-footer-height" />
					<xsl:with-param name="page-number" select="$page-number" />
				</xsl:call-template>
			</xsl:for-each>

			<!-- Insert empty block just in case there are no other blocks -->
			<fo:block />
		</fo:static-content>
	</xsl:template>

	<!-- ====================== -->
	<!-- template: page-content -->
	<!-- ====================== -->
	<xsl:template name="page-content">
		<xsl:param name="page" />
		<xsl:param name="page-number" />
		<xsl:param name="viewable-area-width" />
		<xsl:param name="viewable-area-height" />
		<xsl:param name="page-header-height" />
		<xsl:param name="page-footer-height" />

		<fo:flow flow-name="xsl-region-body">
			<!-- Report title views -->
			<xsl:call-template name="band">
				<xsl:with-param name="band-name" select="$page/Views/BandView[@Type='report-title']" />
				<xsl:with-param name="viewable-area-width" select="$viewable-area-width" />
				<xsl:with-param name="viewable-area-height" select="$viewable-area-height" />
				<xsl:with-param name="page-number" select="$page-number" />
			</xsl:call-template>

			<!-- Master data views -->
			<xsl:call-template name="band-with-dataset">
				<xsl:with-param name="band-name" select="$page/Views/BandView[@Type='master-data']" />
				<xsl:with-param name="band-header" select="$page/Views/BandView[@Type='master-header']" />
				<xsl:with-param name="band-footer" select="$page/Views/BandView[@Type='master-footer']" />
				<xsl:with-param name="column-header" select="$page/Views/BandView[@Type='column-header']" />
				<xsl:with-param name="column-footer" select="$page/Views/BandView[@Type='column-footer']" />
				<xsl:with-param name="viewable-area-width" select="$viewable-area-width" />
				<xsl:with-param name="viewable-area-height" select="$viewable-area-height" />
				<xsl:with-param name="page-number" select="$page-number" />
			</xsl:call-template>

			<!-- Report summary views -->
			<xsl:call-template name="band">
				<xsl:with-param name="band-name" select="$page/Views/BandView[@Type='report-summary']" />
				<xsl:with-param name="viewable-area-width" select="$viewable-area-width" />
				<xsl:with-param name="viewable-area-height" select="$viewable-area-height" />
				<xsl:with-param name="page-number" select="$page-number" />
			</xsl:call-template>

			<!-- Rest of views, which do not belong any band view -->
			<xsl:for-each select="$page/Views/*">
				<xsl:call-template name="view-choose">
					<xsl:with-param name="viewable-area-width" select="$viewable-area-width" />
					<xsl:with-param name="viewable-area-height" select="$viewable-area-height" />
					<xsl:with-param name="page-number" select="$page-number" />
				</xsl:call-template>
			</xsl:for-each>

			<!-- An empty block with an id at the end of the flow -->
			<fo:block>
				<xsl:attribute name="id">end-of-sequence-page<xsl:value-of select="$page-number" /></xsl:attribute>
			</fo:block>
		</fo:flow>
	</xsl:template>

	<!-- ============== -->
	<!-- template: band -->
	<!-- ============== -->
	<xsl:template name="band">
		<xsl:param name="band-name" />
		<xsl:param name="viewable-area-width" />
		<xsl:param name="viewable-area-height" />
		<xsl:param name="dataset-row" required="no" />
		<xsl:param name="dataset-row-position" required="no" />
		<xsl:param name="page-number" />

		<xsl:choose>
			<xsl:when test="$band-name">
				<xsl:variable name="band-height">
					<xsl:value-of select="$band-name/@Height * $viewable-area-height div 100" />
				</xsl:variable>

				<fo:block-container>
					<xsl:attribute name="height"><xsl:value-of select="$band-height" />mm</xsl:attribute>

					<xsl:for-each select="$band-name/ChildViews/*">
						<xsl:call-template name="view-choose">
							<xsl:with-param name="viewable-area-width" select="$viewable-area-width" />
							<xsl:with-param name="viewable-area-height" select="$band-height" />
							<xsl:with-param name="dataset-row" select="$dataset-row" />
							<xsl:with-param name="dataset-row-position" select="$dataset-row-position" />
							<xsl:with-param name="page-number" select="$page-number" />
						</xsl:call-template>
					</xsl:for-each>

					<!-- Insert empty block just in case there are no other blocks -->
					<fo:block />
				</fo:block-container>
			</xsl:when>
			<xsl:otherwise>
				<fo:block />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- =========================== -->
	<!-- template: band-with-dataset -->
	<!-- =========================== -->
	<xsl:template name="band-with-dataset">
		<xsl:param name="band-name" />
		<xsl:param name="band-header" />
		<xsl:param name="band-footer" />
		<xsl:param name="column-header" />
		<xsl:param name="column-footer" />
		<xsl:param name="viewable-area-width" />
		<xsl:param name="viewable-area-height" />
		<xsl:param name="page-number" />

		<xsl:choose>
			<xsl:when test="$band-name[@Stretched = 'true']">
				<xsl:call-template name="band-with-dataset-stretch">
					<xsl:with-param name="band-name" select="$band-name" />
					<xsl:with-param name="band-header" select="$band-header" />
					<xsl:with-param name="band-footer" select="$band-footer" />
					<xsl:with-param name="column-header" select="$column-header" />
					<xsl:with-param name="column-footer" select="$column-footer" />
					<xsl:with-param name="viewable-area-width" select="$viewable-area-width" />
					<xsl:with-param name="viewable-area-height" select="$viewable-area-height" />
					<xsl:with-param name="page-number" select="$page-number" />
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="band-with-dataset-no-stretch">
					<xsl:with-param name="band-name" select="$band-name" />
					<xsl:with-param name="band-header" select="$band-header" />
					<xsl:with-param name="band-footer" select="$band-footer" />
					<xsl:with-param name="column-header" select="$column-header" />
					<xsl:with-param name="column-footer" select="$column-footer" />
					<xsl:with-param name="viewable-area-width" select="$viewable-area-width" />
					<xsl:with-param name="viewable-area-height" select="$viewable-area-height" />
					<xsl:with-param name="page-number" select="$page-number" />
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- ====================================== -->
	<!-- template: band-with-dataset-no-stretch -->
	<!-- ====================================== -->
	<xsl:template name="band-with-dataset-no-stretch">
		<xsl:param name="band-name" />
		<xsl:param name="band-header" />
		<xsl:param name="band-footer" />
		<xsl:param name="column-header" />
		<xsl:param name="column-footer" />
		<xsl:param name="viewable-area-width" />
		<xsl:param name="viewable-area-height" />
		<xsl:param name="page-number" />

		<fo:table>
			<fo:table-header>
				<fo:table-row>
					<fo:table-cell>
						<!-- Column header views -->
						<xsl:call-template name="band">
							<xsl:with-param name="band-name" select="$column-header" />
							<xsl:with-param name="viewable-area-width" select="$viewable-area-width" />
							<xsl:with-param name="viewable-area-height" select="$viewable-area-height" />
							<xsl:with-param name="page-number" select="$page-number" />
						</xsl:call-template>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-header>

			<fo:table-footer>
				<fo:table-row>
					<fo:table-cell>
						<!-- Column footer views -->
						<xsl:call-template name="band">
							<xsl:with-param name="band-name" select="$column-footer" />
							<xsl:with-param name="viewable-area-width" select="$viewable-area-width" />
							<xsl:with-param name="viewable-area-height" select="$viewable-area-height" />
							<xsl:with-param name="page-number" select="$page-number" />
						</xsl:call-template>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-footer>

			<fo:table-body>
				<fo:table-row>
					<fo:table-cell>
						<!-- Data header views -->
						<xsl:call-template name="band">
							<xsl:with-param name="band-name" select="$band-header" />
							<xsl:with-param name="viewable-area-width" select="$viewable-area-width" />
							<xsl:with-param name="viewable-area-height" select="$viewable-area-height" />
							<xsl:with-param name="page-number" select="$page-number" />
						</xsl:call-template>
					</fo:table-cell>
				</fo:table-row>
				<xsl:for-each select="//Datasets/Dataset[@Name=$band-name/@Dataset]/Rows/*">
					<fo:table-row>
						<fo:table-cell>
							<!-- Data views -->
							<xsl:call-template name="band">
								<xsl:with-param name="band-name" select="$band-name" />
								<xsl:with-param name="viewable-area-width" select="$viewable-area-width" />
								<xsl:with-param name="viewable-area-height" select="$viewable-area-height" />
								<xsl:with-param name="dataset-row" select="current()" />
								<xsl:with-param name="dataset-row-position" select="position()" />
								<xsl:with-param name="page-number" select="$page-number" />
							</xsl:call-template>
						</fo:table-cell>
					</fo:table-row>
				</xsl:for-each>
				<fo:table-row>
					<fo:table-cell>
						<!-- Data footer views -->
						<xsl:call-template name="band">
							<xsl:with-param name="band-name" select="$band-footer" />
							<xsl:with-param name="viewable-area-width" select="$viewable-area-width" />
							<xsl:with-param name="viewable-area-height" select="$viewable-area-height" />
							<xsl:with-param name="page-number" select="$page-number" />
						</xsl:call-template>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
	</xsl:template>

	<!-- =================================== -->
	<!-- template: band-with-dataset-stretch -->
	<!-- =================================== -->
	<xsl:template name="band-with-dataset-stretch">
		<xsl:param name="band-name" />
		<xsl:param name="band-header" />
		<xsl:param name="band-footer" />
		<xsl:param name="column-header" />
		<xsl:param name="column-footer" />
		<xsl:param name="viewable-area-width" />
		<xsl:param name="viewable-area-height" />
		<xsl:param name="page-number" />

		<!-- Sort views by left position -->
		<xsl:variable name="column-views" as="node()*">
			<xsl:perform-sort select="$band-name/ChildViews/*">
				<xsl:sort select="@Left * 1.0" />
			</xsl:perform-sort>
		</xsl:variable>

		<fo:table>
			<xsl:for-each select="$column-views">
				<!-- Insert fake column as ident -->
				<xsl:if test="position() = 1">
					<fo:table-column>
						<xsl:attribute name="column-width"><xsl:value-of select="$viewable-area-width * @BoundLeft div 100" />mm</xsl:attribute>
					</fo:table-column>
				</xsl:if>

				<!-- Determine normal column width -->
				<fo:table-column>
					<xsl:attribute name="column-width"><xsl:value-of select="$viewable-area-width * @BoundWidth div 100" />mm</xsl:attribute>
				</fo:table-column>
			</xsl:for-each>

			<fo:table-header>
				<fo:table-row>
					<!-- Sort views by left position -->
					<xsl:variable name="table-header-column-views" as="node()*">
						<xsl:perform-sort select="$column-header/ChildViews/*">
							<xsl:sort select="@Left * 1.0" />
						</xsl:perform-sort>
					</xsl:variable>

					<xsl:call-template name="stretched-table-row">
						<xsl:with-param name="column-views" select="$table-header-column-views" />
						<xsl:with-param name="viewable-area-width" select="$viewable-area-width" />
						<xsl:with-param name="viewable-area-height" select="$column-header/@Height * $viewable-area-height div 100" />
						<xsl:with-param name="page-number" select="$page-number" />
					</xsl:call-template>
				</fo:table-row>
			</fo:table-header>

			<fo:table-footer>
				<fo:table-row>
					<!-- Sort views by left position -->
					<xsl:variable name="table-footer-column-views" as="node()*">
						<xsl:perform-sort select="$column-footer/ChildViews/*">
							<xsl:sort select="@Left * 1.0" />
						</xsl:perform-sort>
					</xsl:variable>

					<xsl:call-template name="stretched-table-row">
						<xsl:with-param name="column-views" select="$table-footer-column-views" />
						<xsl:with-param name="viewable-area-width" select="$viewable-area-width" />
						<xsl:with-param name="viewable-area-height" select="$column-footer/@Height * $viewable-area-height div 100" />
						<xsl:with-param name="page-number" select="$page-number" />
					</xsl:call-template>
				</fo:table-row>
			</fo:table-footer>

			<fo:table-body>
				<!-- Data header views -->
				<fo:table-row>
					<!-- Sort views by left position -->
					<xsl:variable name="band-header-column-views" as="node()*">
						<xsl:perform-sort select="$band-header/ChildViews/*">
							<xsl:sort select="@Left * 1.0" />
						</xsl:perform-sort>
					</xsl:variable>

					<xsl:call-template name="stretched-table-row">
						<xsl:with-param name="column-views" select="$band-header-column-views" />
						<xsl:with-param name="viewable-area-width" select="$viewable-area-width" />
						<xsl:with-param name="viewable-area-height" select="$band-header/@Height * $viewable-area-height div 100" />
						<xsl:with-param name="page-number" select="$page-number" />
					</xsl:call-template>
				</fo:table-row>
				<!-- Data views -->
				<xsl:for-each select="//Datasets/Dataset[@Name=$band-name/@Dataset]/Rows/*">
					<xsl:variable name="dataset-row" select="current()" />
					<xsl:variable name="dataset-row-position" select="position()" />

					<fo:table-row>
						<xsl:call-template name="stretched-table-row">
							<xsl:with-param name="column-views" select="$column-views" />
							<xsl:with-param name="viewable-area-width" select="$viewable-area-width" />
							<xsl:with-param name="viewable-area-height" select="$band-name/@Height * $viewable-area-height div 100" />
							<xsl:with-param name="dataset-row" select="$dataset-row" />
							<xsl:with-param name="dataset-row-position" select="$dataset-row-position" />
							<xsl:with-param name="page-number" select="$page-number" />
						</xsl:call-template>
					</fo:table-row>
				</xsl:for-each>
				<!-- Data footer views -->
				<fo:table-row>
					<!-- Sort views by left position -->
					<xsl:variable name="band-footer-column-views" as="node()*">
						<xsl:perform-sort select="$band-footer/ChildViews/*">
							<xsl:sort select="@Left * 1.0" />
						</xsl:perform-sort>
					</xsl:variable>

					<xsl:call-template name="stretched-table-row">
						<xsl:with-param name="column-views" select="$band-footer-column-views" />
						<xsl:with-param name="viewable-area-width" select="$viewable-area-width" />
						<xsl:with-param name="viewable-area-height" select="$band-footer/@Height * $viewable-area-height div 100" />
						<xsl:with-param name="page-number" select="$page-number" />
					</xsl:call-template>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
	</xsl:template>

	<!-- ============================= -->
	<!-- template: stretched-table-row -->
	<!-- ============================= -->
	<xsl:template name="stretched-table-row">
		<xsl:param name="column-views" />
		<xsl:param name="viewable-area-width" />
		<xsl:param name="viewable-area-height" />
		<xsl:param name="dataset-row" required="no" />
		<xsl:param name="dataset-row-position" required="no" />
		<xsl:param name="page-number" />

		<!-- Insert fake column as ident -->
		<fo:table-cell>
			<fo:block />
		</fo:table-cell>

		<xsl:for-each select="$column-views">
			<!-- Insert normal column -->
			<fo:table-cell>
				<!-- Border/Background -->
				<xsl:attribute name="background-color"><xsl:value-of select="attribute::FillColor" /></xsl:attribute>
				<xsl:attribute name="border-color"><xsl:value-of select="attribute::FrameColor" /></xsl:attribute>
				<xsl:attribute name="border-width"><xsl:value-of select="attribute::FrameWidth" />pt</xsl:attribute>
				<xsl:attribute name="border-style"><xsl:value-of select="attribute::FrameStyle" /></xsl:attribute>

				<!-- Render views -->
				<xsl:call-template name="view-choose-simple">
					<xsl:with-param name="viewable-area-width" select="$viewable-area-width" />
					<xsl:with-param name="viewable-area-height" select="$viewable-area-height" />
					<xsl:with-param name="dataset-row" select="$dataset-row" />
					<xsl:with-param name="dataset-row-position" select="$dataset-row-position" />
					<xsl:with-param name="page-number" select="$page-number" />
				</xsl:call-template>

				<!-- Insert empty block for safety -->
				<fo:block />
			</fo:table-cell>
		</xsl:for-each>
	</xsl:template>

	<!-- ===================== -->
	<!-- template: view-choose -->
	<!-- ===================== -->
	<xsl:template name="view-choose">
		<xsl:param name="viewable-area-width" />
		<xsl:param name="viewable-area-height" />
		<xsl:param name="dataset-row" required="no" />
		<xsl:param name="dataset-row-position" required="no" />
		<xsl:param name="page-number" />

		<xsl:choose>
			<xsl:when test="name() = 'MemoView'">
				<xsl:call-template name="memo-view">
					<xsl:with-param name="viewable-area-width" select="$viewable-area-width" />
					<xsl:with-param name="viewable-area-height" select="$viewable-area-height" />
					<xsl:with-param name="dataset-row" select="$dataset-row" />
					<xsl:with-param name="dataset-row-position" select="$dataset-row-position" />
					<xsl:with-param name="page-number" select="$page-number" />
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="name() = 'PictureView'">
				<xsl:call-template name="picture-view">
					<xsl:with-param name="viewable-area-width" select="$viewable-area-width" />
					<xsl:with-param name="viewable-area-height" select="$viewable-area-height" />
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="name() = 'LineView'">
			</xsl:when>
			<xsl:when test="name() = 'SubReportView'">
			</xsl:when>
			<xsl:when test="name() = 'BarCodeView'">
				<xsl:call-template name="bar-code-view">
					<xsl:with-param name="viewable-area-width" select="$viewable-area-width" />
					<xsl:with-param name="viewable-area-height" select="$viewable-area-height" />
					<xsl:with-param name="dataset-row" select="$dataset-row" />
					<xsl:with-param name="dataset-row-position" select="$dataset-row-position" />
					<xsl:with-param name="page-number" select="$page-number" />
				</xsl:call-template>
			</xsl:when>
		</xsl:choose>
	</xsl:template>

	<!-- ============================ -->
	<!-- template: view-choose-simple -->
	<!-- ============================ -->
	<xsl:template name="view-choose-simple">
		<xsl:param name="viewable-area-width" />
		<xsl:param name="viewable-area-height" />
		<xsl:param name="dataset-row" required="no" />
		<xsl:param name="dataset-row-position" required="no" />
		<xsl:param name="page-number" />

		<xsl:choose>
			<xsl:when test="name() = 'MemoView'">
				<xsl:call-template name="memo-view-simple">
					<xsl:with-param name="viewable-area-width" select="$viewable-area-width" />
					<xsl:with-param name="viewable-area-height" select="$viewable-area-height" />
					<xsl:with-param name="dataset-row" select="$dataset-row" />
					<xsl:with-param name="dataset-row-position" select="$dataset-row-position" />
					<xsl:with-param name="page-number" select="$page-number" />
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="name() = 'PictureView'">
				<xsl:call-template name="picture-view-simple">
					<xsl:with-param name="viewable-area-width" select="$viewable-area-width" />
					<xsl:with-param name="viewable-area-height" select="$viewable-area-height" />
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="name() = 'LineView'">
			</xsl:when>
			<xsl:when test="name() = 'SubReportView'">
			</xsl:when>
			<xsl:when test="name() = 'BarCodeView'">
				<xsl:call-template name="bar-code-view-simple">
					<xsl:with-param name="viewable-area-width" select="$viewable-area-width" />
					<xsl:with-param name="viewable-area-height" select="$viewable-area-height" />
					<xsl:with-param name="dataset-row" select="$dataset-row" />
					<xsl:with-param name="dataset-row-position" select="$dataset-row-position" />
					<xsl:with-param name="page-number" select="$page-number" />
				</xsl:call-template>
			</xsl:when>
		</xsl:choose>
	</xsl:template>

	<!-- ======================= -->
	<!-- template: bar-code-view -->
	<!-- ======================= -->
	<xsl:template name="bar-code-view">
		<xsl:param name="viewable-area-width" />
		<xsl:param name="viewable-area-height" />
		<xsl:param name="dataset-row" required="no" />
		<xsl:param name="dataset-row-position" required="no" />
		<xsl:param name="page-number" />

		<xsl:if test="attribute::Visible = 'true'">
			<fo:block-container absolute-position="absolute">
				<!-- Top/Left Corner -->
				<xsl:attribute name="left"><xsl:value-of select="$viewable-area-width * @Left div 100" />mm</xsl:attribute>
				<xsl:attribute name="top"><xsl:value-of select="$viewable-area-height * @Top div 100" />mm</xsl:attribute>

				<!-- Width/Height -->
				<xsl:attribute name="width"><xsl:value-of select="$viewable-area-width * @Width div 100" />mm</xsl:attribute>
				<xsl:attribute name="height"><xsl:value-of select="$viewable-area-height * @Height div 100" />mm</xsl:attribute>

				<!-- Border/Background -->
				<xsl:attribute name="background-color"><xsl:value-of select="attribute::FillColor" /></xsl:attribute>
				<xsl:attribute name="border-color"><xsl:value-of select="attribute::FrameColor" /></xsl:attribute>
				<xsl:attribute name="border-width"><xsl:value-of select="attribute::FrameWidth" />pt</xsl:attribute>

				<xsl:choose>
					<xsl:when test="attribute::FrameType = 'none'">
						<xsl:attribute name="border-style">none</xsl:attribute>
					</xsl:when>
					<xsl:when test="attribute::FrameType = 'all'">
						<xsl:attribute name="border-style"><xsl:value-of select="attribute::FrameStyle" /></xsl:attribute>
					</xsl:when>
					<xsl:otherwise>
						<xsl:attribute name="border-style">none</xsl:attribute>

						<xsl:if test="contains(attribute::FrameType, 'left')">
							<xsl:attribute name="border-left"><xsl:value-of select="attribute::FrameStyle" /></xsl:attribute>
						</xsl:if>
						<xsl:if test="contains(attribute::FrameType, 'right')">
							<xsl:attribute name="border-right"><xsl:value-of select="attribute::FrameStyle" /></xsl:attribute>
						</xsl:if>
						<xsl:if test="contains(attribute::FrameType, 'top')">
							<xsl:attribute name="border-top"><xsl:value-of select="attribute::FrameStyle" /></xsl:attribute>
						</xsl:if>
						<xsl:if test="contains(attribute::FrameType, 'bottom')">
							<xsl:attribute name="border-bottom"><xsl:value-of select="attribute::FrameStyle" /></xsl:attribute>
						</xsl:if>
					</xsl:otherwise>
				</xsl:choose>

				<xsl:call-template name="bar-code-view-simple">
					<xsl:with-param name="viewable-area-width" select="$viewable-area-width" />
					<xsl:with-param name="viewable-area-height" select="$viewable-area-height" />
					<xsl:with-param name="dataset-row" select="$dataset-row" />
					<xsl:with-param name="dataset-row-position" select="$dataset-row-position" />
					<xsl:with-param name="page-number" select="$page-number" />
				</xsl:call-template>

				<!-- Insert empty block for safety -->
				<fo:block />
			</fo:block-container>
		</xsl:if>
	</xsl:template>

	<!-- ============================== -->
	<!-- template: bar-code-view-simple -->
	<!-- ============================== -->
	<xsl:template name="bar-code-view-simple">
		<xsl:param name="viewable-area-width" />
		<xsl:param name="viewable-area-height" />
		<xsl:param name="dataset-row" required="no" />
		<xsl:param name="dataset-row-position" required="no" />
		<xsl:param name="page-number" />

		<xsl:variable name="message">
			<xsl:call-template name="text-var">
				<xsl:with-param name="text" select="Memo" />
				<xsl:with-param name="dataset-row" select="$dataset-row" />
				<xsl:with-param name="dataset-row-position" select="$dataset-row-position" />
				<xsl:with-param name="page-number" select="$page-number" />
			</xsl:call-template>
		</xsl:variable>

		<xsl:if test="attribute::Visible = 'true' and fn:normalize-space($message) != ''">
			<!-- Vertical alignment -->
			<xsl:attribute name="display-align">center</xsl:attribute>

			<!-- BarCode -->
			<fo:block font-size="0" text-align="center">
				<fo:instream-foreign-object>
					<barcode:barcode>
						<xsl:choose>
							<xsl:when test="@BarCodeType = 'datamatrix'">
								<xsl:attribute name="message">
									<xsl:text>url(data:;base64,</xsl:text>
									<xsl:value-of select="utils:base64($message, @DataMatrixEncoding)" xmlns:utils="java:ru.aplix.converters.fr2afop.utils.Utils" />
									<xsl:text>)</xsl:text>
								</xsl:attribute>
							</xsl:when>
							<xsl:otherwise>
								<xsl:attribute name="message"><xsl:value-of select="fn:normalize-space($message)" /></xsl:attribute>
							</xsl:otherwise>
						</xsl:choose>

						<xsl:attribute name="orientation"><xsl:value-of select="attribute::Angle" /></xsl:attribute>

						<xsl:element name="{concat('barcode:', @BarCodeType)}">

							<!-- BarCode height -->
							<xsl:choose>
								<xsl:when test="attribute::Angle = '90' or attribute::Angle = '270'">
									<barcode:height>
										<xsl:value-of select="concat($viewable-area-width * @Width div 100, 'mm')" />
									</barcode:height>
								</xsl:when>
								<xsl:otherwise>
									<barcode:height>
										<xsl:value-of select="concat($viewable-area-height * @Height div 100, 'mm')" />
									</barcode:height>
								</xsl:otherwise>
							</xsl:choose>

							<!-- BarCode Human-readable -->
							<xsl:if test="attribute::ShowText = 'false'">
								<barcode:human-readable>
									<barcode:placement>none</barcode:placement>
								</barcode:human-readable>
							</xsl:if>

							<!-- Checksum -->
							<xsl:choose>
								<xsl:when test="attribute::CheckSum = 'false'">
									<barcode:checksum>ignore</barcode:checksum>
								</xsl:when>
								<xsl:otherwise>
									<barcode:checksum>auto</barcode:checksum>
								</xsl:otherwise>
							</xsl:choose>

							<xsl:if test="@BarCodeType = 'datamatrix'">
								<barcode:module-width>
									<xsl:value-of select="concat(@ModuleWidth, 'mm')" />
								</barcode:module-width>
							</xsl:if>

						</xsl:element>
					</barcode:barcode>
				</fo:instream-foreign-object>
			</fo:block>
		</xsl:if>
	</xsl:template>

	<!-- ====================== -->
	<!-- template: picture-view -->
	<!-- ====================== -->
	<xsl:template name="picture-view">
		<xsl:param name="viewable-area-width" />
		<xsl:param name="viewable-area-height" />

		<xsl:if test="attribute::Visible = 'true' and boolean(*/Content) and boolean(*/@Mime)">
			<fo:block-container absolute-position="absolute">

				<!-- Top/Left Corner -->
				<xsl:attribute name="left"><xsl:value-of select="$viewable-area-width * @Left div 100" />mm</xsl:attribute>
				<xsl:attribute name="top"><xsl:value-of select="$viewable-area-height * @Top div 100" />mm</xsl:attribute>

				<!-- Width/Height -->
				<xsl:attribute name="width"><xsl:value-of select="$viewable-area-width * @Width div 100" />mm</xsl:attribute>
				<xsl:attribute name="height"><xsl:value-of select="$viewable-area-height * @Height div 100" />mm</xsl:attribute>

				<!-- Border/Background -->
				<xsl:attribute name="background-color"><xsl:value-of select="attribute::FillColor" /></xsl:attribute>
				<xsl:attribute name="border-color"><xsl:value-of select="attribute::FrameColor" /></xsl:attribute>
				<xsl:attribute name="border-width"><xsl:value-of select="attribute::FrameWidth" />pt</xsl:attribute>

				<xsl:choose>
					<xsl:when test="attribute::FrameType = 'none'">
						<xsl:attribute name="border-style">none</xsl:attribute>
					</xsl:when>
					<xsl:when test="attribute::FrameType = 'all'">
						<xsl:attribute name="border-style"><xsl:value-of select="attribute::FrameStyle" /></xsl:attribute>
					</xsl:when>
					<xsl:otherwise>
						<xsl:attribute name="border-style">none</xsl:attribute>

						<xsl:if test="contains(attribute::FrameType, 'left')">
							<xsl:attribute name="border-left"><xsl:value-of select="attribute::FrameStyle" /></xsl:attribute>
						</xsl:if>
						<xsl:if test="contains(attribute::FrameType, 'right')">
							<xsl:attribute name="border-right"><xsl:value-of select="attribute::FrameStyle" /></xsl:attribute>
						</xsl:if>
						<xsl:if test="contains(attribute::FrameType, 'top')">
							<xsl:attribute name="border-top"><xsl:value-of select="attribute::FrameStyle" /></xsl:attribute>
						</xsl:if>
						<xsl:if test="contains(attribute::FrameType, 'bottom')">
							<xsl:attribute name="border-bottom"><xsl:value-of select="attribute::FrameStyle" /></xsl:attribute>
						</xsl:if>
					</xsl:otherwise>
				</xsl:choose>

				<!-- Render Graphic -->
				<xsl:call-template name="picture-view-simple">
					<xsl:with-param name="viewable-area-width" select="$viewable-area-width" />
					<xsl:with-param name="viewable-area-height" select="$viewable-area-height" />
				</xsl:call-template>

				<!-- Insert empty block for safety -->
				<fo:block />
			</fo:block-container>
		</xsl:if>
	</xsl:template>

	<!-- ============================= -->
	<!-- template: picture-view-simple -->
	<!-- ============================= -->
	<xsl:template name="picture-view-simple">
		<xsl:param name="viewable-area-width" />
		<xsl:param name="viewable-area-height" />

		<xsl:if test="attribute::Visible = 'true' and boolean(*/Content) and boolean(*/@Mime)">
			<!-- Graphic -->
			<fo:block font-size="0">
				<xsl:if test="attribute::Centered = 'true'">
					<xsl:attribute name="text-align">center</xsl:attribute>
					<xsl:attribute name="display-align">center</xsl:attribute>
				</xsl:if>

				<fo:external-graphic>
					<xsl:choose>
						<xsl:when test="attribute::KeepRatio = 'true'">
							<xsl:attribute name="scaling">uniform</xsl:attribute>
						</xsl:when>
						<xsl:otherwise>
							<xsl:attribute name="scaling">non-uniform</xsl:attribute>
						</xsl:otherwise>
					</xsl:choose>

					<!-- Width/Height -->
					<xsl:attribute name="content-width"><xsl:value-of select="$viewable-area-width * @Width div 100" />mm</xsl:attribute>
					<xsl:attribute name="content-height"><xsl:value-of select="$viewable-area-height * @Height div 100" />mm</xsl:attribute>

					<!-- Image Source -->
					<xsl:attribute name="src">
							<xsl:text>url('data:</xsl:text>
							<xsl:value-of select="*/@Mime" /> 
							<xsl:text>;base64,</xsl:text>
							<xsl:value-of select="*/Content" />
							<xsl:text>')</xsl:text>
						</xsl:attribute>
				</fo:external-graphic>
			</fo:block>
		</xsl:if>
	</xsl:template>

	<!-- =================== -->
	<!-- template: memo-view -->
	<!-- =================== -->
	<xsl:template name="memo-view">
		<xsl:param name="viewable-area-width" />
		<xsl:param name="viewable-area-height" />
		<xsl:param name="dataset-row" required="no" />
		<xsl:param name="dataset-row-position" required="no" />
		<xsl:param name="page-number" />

		<xsl:if test="attribute::Visible = 'true'">
			<fo:block-container absolute-position="absolute">
				<!-- Top/Left Corner -->
				<xsl:attribute name="left"><xsl:value-of select="$viewable-area-width * @Left div 100" />mm</xsl:attribute>
				<xsl:attribute name="top"><xsl:value-of select="$viewable-area-height * @Top div 100" />mm</xsl:attribute>

				<!-- Rotation, Width/Height -->
				<xsl:choose>
					<xsl:when test="attribute::Rotate = 'true'">
						<xsl:attribute name="height"><xsl:value-of select="$viewable-area-width * @Width div 100" />mm</xsl:attribute>
						<xsl:attribute name="width"><xsl:value-of select="$viewable-area-height * @Height div 100" />mm</xsl:attribute>

						<xsl:attribute name="reference-orientation">90</xsl:attribute>
					</xsl:when>
					<xsl:otherwise>
						<xsl:attribute name="width"><xsl:value-of select="$viewable-area-width * @Width div 100" />mm</xsl:attribute>
						<xsl:attribute name="height"><xsl:value-of select="$viewable-area-height * @Height div 100" />mm</xsl:attribute>
					</xsl:otherwise>
				</xsl:choose>

				<!-- Border/Background -->
				<xsl:attribute name="background-color"><xsl:value-of select="attribute::FillColor" /></xsl:attribute>
				<xsl:attribute name="border-color"><xsl:value-of select="attribute::FrameColor" /></xsl:attribute>
				<xsl:attribute name="border-width"><xsl:value-of select="attribute::FrameWidth" />pt</xsl:attribute>

				<xsl:choose>
					<xsl:when test="attribute::FrameType = 'none'">
						<xsl:attribute name="border-style">none</xsl:attribute>
					</xsl:when>
					<xsl:when test="attribute::FrameType = 'all'">
						<xsl:attribute name="border-style"><xsl:value-of select="attribute::FrameStyle" /></xsl:attribute>
					</xsl:when>
					<xsl:otherwise>
						<xsl:attribute name="border-style">none</xsl:attribute>

						<xsl:if test="contains(attribute::FrameType, 'left')">
							<xsl:attribute name="border-left"><xsl:value-of select="attribute::FrameStyle" /></xsl:attribute>
						</xsl:if>
						<xsl:if test="contains(attribute::FrameType, 'right')">
							<xsl:attribute name="border-right"><xsl:value-of select="attribute::FrameStyle" /></xsl:attribute>
						</xsl:if>
						<xsl:if test="contains(attribute::FrameType, 'top')">
							<xsl:attribute name="border-top"><xsl:value-of select="attribute::FrameStyle" /></xsl:attribute>
						</xsl:if>
						<xsl:if test="contains(attribute::FrameType, 'bottom')">
							<xsl:attribute name="border-bottom"><xsl:value-of select="attribute::FrameStyle" /></xsl:attribute>
						</xsl:if>
					</xsl:otherwise>
				</xsl:choose>

				<!-- Clipping Text -->
				<xsl:if test="attribute::Rotate = 'false'">
					<xsl:attribute name="overflow">hidden</xsl:attribute>
				</xsl:if>

				<!-- Render MemoView -->
				<xsl:call-template name="memo-view-simple">
					<xsl:with-param name="viewable-area-width" select="$viewable-area-width" />
					<xsl:with-param name="viewable-area-height" select="$viewable-area-height" />
					<xsl:with-param name="dataset-row" select="$dataset-row" />
					<xsl:with-param name="dataset-row-position" select="$dataset-row-position" />
					<xsl:with-param name="page-number" select="$page-number" />
				</xsl:call-template>

				<!-- Insert empty block for safety -->
				<fo:block />
			</fo:block-container>
		</xsl:if>
	</xsl:template>

	<!-- ========================== -->
	<!-- template: memo-view-simple -->
	<!-- ========================== -->
	<xsl:template name="memo-view-simple">
		<xsl:param name="viewable-area-width" />
		<xsl:param name="viewable-area-height" />
		<xsl:param name="dataset-row" required="no" />
		<xsl:param name="dataset-row-position" required="no" />
		<xsl:param name="page-number" />

		<xsl:if test="attribute::Visible = 'true'">
			<!-- Vertical Alignment -->
			<xsl:choose>
				<xsl:when test="attribute::VerticalAlign = 'middle'">
					<xsl:attribute name="display-align">center</xsl:attribute>
				</xsl:when>
				<xsl:when test="attribute::VerticalAlign = 'bottom'">
					<xsl:attribute name="display-align">after</xsl:attribute>
				</xsl:when>
			</xsl:choose>

			<!-- Font/Text Attributes -->
			<fo:block linefeed-treatment="preserve" white-space-treatment="preserve" white-space-collapse="false">
				<xsl:attribute name="font-family"><xsl:value-of select="attribute::FontName" /></xsl:attribute>
				<xsl:attribute name="font-size"><xsl:value-of select="attribute::FontSize" /></xsl:attribute>
				<xsl:attribute name="font-weight"><xsl:value-of select="attribute::FontWeight" /></xsl:attribute>
				<xsl:attribute name="font-style"><xsl:value-of select="attribute::FontStyle" /></xsl:attribute>
				<xsl:attribute name="text-decoration"><xsl:value-of select="attribute::TextDecoration" /></xsl:attribute>
				<xsl:attribute name="color"><xsl:value-of select="attribute::FontColor" /></xsl:attribute>
				<xsl:attribute name="text-align"><xsl:value-of select="attribute::TextAlign" /></xsl:attribute>

				<xsl:call-template name="text-var">
					<xsl:with-param name="text" select="Memo" />
					<xsl:with-param name="dataset-row" select="$dataset-row" />
					<xsl:with-param name="dataset-row-position" select="$dataset-row-position" />
					<xsl:with-param name="page-number" select="$page-number" />
				</xsl:call-template>
			</fo:block>
		</xsl:if>
	</xsl:template>

	<!-- ================== -->
	<!-- template: text-var -->
	<!-- ================== -->
	<xsl:template name="text-var">
		<xsl:param name="text" />
		<xsl:param name="dataset-row" required="no" />
		<xsl:param name="dataset-row-position" required="no" />
		<xsl:param name="page-number" />

		<!-- An XPath like "/" needs a context node. -->
		<!-- Thus, when you have a context item that is not a node, -->
		<!-- you need a variable to store a reference to the document you want to access. -->
		<xsl:variable name="variables" select="//Variables" />

		<!-- Tokenize text -->
		<xsl:for-each select="utils:tokenize($text)" xmlns:utils="java:ru.aplix.converters.fr2afop.utils.Utils">
			<xsl:choose>
				<!-- If token is bracketed, then it's a variable -->
				<xsl:when test="fn:matches(current(), '\[[^\[\]]+\]')">
					<xsl:variable name="variable-name">
						<xsl:value-of select="fn:substring(current(), 2, fn:string-length() - 2)" />
					</xsl:variable>

					<!-- Retrieve variable value -->
					<xsl:call-template name="var-value">
						<xsl:with-param name="variable-node" select="$variables/Variable[@Name=$variable-name]" />
						<xsl:with-param name="dataset-row" select="$dataset-row" />
						<xsl:with-param name="dataset-row-position" select="$dataset-row-position" />
						<xsl:with-param name="page-number" select="$page-number" />
					</xsl:call-template>
				</xsl:when>
				<!-- Otherwise token is simple text -->
				<xsl:otherwise>
					<xsl:value-of select="current()" />
				</xsl:otherwise>
			</xsl:choose>
		</xsl:for-each>
	</xsl:template>

	<!-- =================== -->
	<!-- template: var-value -->
	<!-- =================== -->
	<xsl:template name="var-value">
		<xsl:param name="variable-node" />
		<xsl:param name="dataset-row" required="no" />
		<xsl:param name="dataset-row-position" required="no" />
		<xsl:param name="page-number" />

		<xsl:choose>
			<xsl:when test="boolean($dataset-row) and boolean($dataset-row/Column[@Name=$variable-node/@Name])">
				<xsl:value-of select="$dataset-row/Column[@Name=$variable-node/@Name]" />
			</xsl:when>
			<xsl:when test="($variable-node/@Type = 'function') and ($variable-node = '')">
				<xsl:choose>
					<xsl:when test="$variable-node/@Function = 'page-num'">
						<fo:page-number />
					</xsl:when>
					<xsl:when test="$variable-node/@Function = 'date'">
						<xsl:value-of select="format-date(current-date(), '[Y0001]-[M01]-[D01]', 'en', 'ISO', '')" />
					</xsl:when>
					<xsl:when test="$variable-node/@Function = 'time'">
						<xsl:value-of select="format-time(current-time(), '[H01]:[m01]', 'en', 'ISO', '')" />
					</xsl:when>
					<xsl:when test="$variable-node/@Function = 'line-num'">
						<xsl:value-of select="$dataset-row-position" />
					</xsl:when>
					<xsl:when test="$variable-node/@Function = 'line-through-num'">
						<xsl:value-of select="$dataset-row-position" />
					</xsl:when>
					<xsl:when test="$variable-node/@Function = 'current-num'">
						<xsl:value-of select="$dataset-row-position" />
					</xsl:when>
					<xsl:when test="$variable-node/@Function = 'total-pages'">
						<fo:page-number-citation-last>
							<xsl:attribute name="ref-id">end-of-sequence-page<xsl:value-of select="$page-number" /></xsl:attribute>
						</fo:page-number-citation-last>
					</xsl:when>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$variable-node" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

</xsl:stylesheet>
