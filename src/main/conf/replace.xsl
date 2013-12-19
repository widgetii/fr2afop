<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fn="http://www.w3.org/2005/xpath-functions"
	xmlns:functx="http://www.functx.com">
	<xsl:output method="xml" version="1.0" encoding="UTF-8" omit-xml-declaration="no" indent="yes" />

	<!-- ================= -->
	<!-- Identity Template -->
	<!-- ================= -->
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>

	<!-- =============================================================== -->
	<!-- Function: provides default values in case a data item is absent -->
	<!-- =============================================================== -->
	<xsl:function name="functx:if-absent" as="item()*">
		<xsl:param name="arg" as="item()*" />
		<xsl:param name="value" as="item()*" />
		<xsl:sequence select="if (exists($arg)) then $arg else $value" />
	</xsl:function>

	<!-- ============================================================ -->
	<!-- Function: performs multiple calls to the fn:replace function -->
	<!-- ============================================================ -->
	<xsl:function name="functx:replace-multi" as="xs:string?">
		<xsl:param name="arg" as="xs:string?" />
		<xsl:param name="changeFrom" as="xs:string*" />
		<xsl:param name="changeTo" as="xs:string*" />
		<xsl:sequence
			select="if (count($changeFrom) > 0) then 
						functx:replace-multi(
							replace($arg, $changeFrom[1], functx:if-absent($changeTo[1],'')), 
							$changeFrom[position() > 1], 
							$changeTo[position() > 1]) 
					else 
						$arg" />
	</xsl:function>

	<!-- ================================ -->
	<!-- template: set-exp-variable-value -->
	<!-- ================================ -->
	<xsl:template name="set-exp-variable-value">
		<xsl:param name="variable" />
		<xsl:param name="value" />

		<xsl:element name="Variable">
			<xsl:attribute name="Name"><xsl:value-of select="$variable/@Name" /></xsl:attribute>
			<xsl:attribute name="Type">expression</xsl:attribute>
			<xsl:attribute name="Content"><xsl:value-of select="$value" /></xsl:attribute>
			<xsl:value-of select="$variable" />
		</xsl:element>
	</xsl:template>

	<!-- =============================== -->
	<!-- template: set-db-variable-value -->
	<!-- =============================== -->
	<xsl:template name="set-db-variable-value">
		<xsl:param name="variable" />
		<xsl:param name="dataset" />
		<xsl:param name="content" />

		<xsl:element name="Variable">
			<xsl:attribute name="Name"><xsl:value-of select="$variable/@Name" /></xsl:attribute>
			<xsl:attribute name="Type">db-field</xsl:attribute>
			<xsl:attribute name="Dataset"><xsl:value-of select="$dataset" /></xsl:attribute>
			<xsl:attribute name="Content"><xsl:value-of select="$content" /></xsl:attribute>
		</xsl:element>
	</xsl:template>

	<!-- ======================================= -->
	<!-- Replace MemoView to BarcodeView by font -->
	<!-- ======================================= -->
	<xsl:template match="MemoView[@FontName=('IntP36DlTt', 'C39HrP24DmTt')]">
		<xsl:element name="BarCodeView">
			<xsl:apply-templates select="@*" />

			<xsl:attribute name="CheckSum">true</xsl:attribute>
			<xsl:attribute name="ShowText">false</xsl:attribute>
			<xsl:attribute name="Angle">0</xsl:attribute>

			<xsl:choose>
				<xsl:when test="attribute::FontName='IntP36DlTt'">
					<xsl:attribute name="BarCodeType">intl2of5</xsl:attribute>
				</xsl:when>
				<xsl:when test="attribute::FontName='C39HrP24DmTt'">
					<xsl:attribute name="BarCodeType">code39</xsl:attribute>
				</xsl:when>
			</xsl:choose>
			
			<xsl:apply-templates select="node()" />
		</xsl:element>
	</xsl:template>

	<!-- ======================== -->
	<!-- Replace BandView dataset -->
	<!-- ======================== -->
	<xsl:template match="BandView[@Dataset='frdbs_Ems_Detail']">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
			<xsl:attribute name="Dataset">frdbs_Ems</xsl:attribute>
		</xsl:copy>
	</xsl:template>

	<!-- ============================ -->
	<!-- Replace MemoView expressions -->
	<!-- ============================ -->
	<xsl:template match="MemoView">
		<xsl:copy>
			<xsl:apply-templates select="@*" />
			<xsl:element name="Memo">
				<xsl:variable name="fr"
					select="(
					'\[Sum\(IBQuery1.&quot;VALUE_&quot;\)\]',
					'\[Sum\(IBQuery1.&quot;PAYMENT&quot;\)\]',
					'\[SumUvedMark\]\s*\(\s*\[SumUvedMarkText\]\s*\)',
					'Sum\(\[\[IBQuery1.&quot;MASSRATE&quot;\s*\+\s*IBQuery1.&quot;AIRRATE&quot;\]/1.18\]\)',
					'Sum\(\[IBQuery1.&quot;MASSRATE&quot;\s*\+\s*IBQuery1.&quot;AIRRATE&quot;\]\)',
					'\[IBQuery1.&quot;MASSRATE&quot;\s*\+\s*IBQuery1.&quot;AIRRATE&quot;\]/1.18',
					'\[IBQuery1.&quot;MASSRATE&quot;\s*\+\s*IBQuery1.&quot;AIRRATE&quot;\]',
					'\[IBQuery1.&quot;VALUE_&quot;\]',
					'\[IBQuery1.&quot;PAYMENT&quot;\]',
					'\[IBQuery1.&quot;MASS&quot;\]',
					'TextBarCode',
					'comment77',
					'without_nds',
					'paysubtype',
					'marks',
					'SumPeresilkiStr',
					'SumPeresilki',
					'NdsSperesulkiStr',
					'NdsSperesulki',
					'NDSotSumOCRateStr',
					'NDSotSumOCRate',
					'SumOCRateStr',
					'SumOCRate',
					'ItogoZaPeresulkuStr',
					'ItogoZaPeresulku',
					'NDSotSumUvedMarkStr',
					'NDSotSumUvedMark',
					'NDSotItogoKoplateStr',
					'NDSotItogoKoplate',
					'ItogoKoplateStr',
					'ItogoKoplate',
					
					'\[index\]',
					'\[adres\]',
					'\[mainindex\]',
					'\[pre_rep.IBQuery8.&quot;MASS&quot;\]',
					'\[pre_rep.IBQuery8.&quot;AIRRATE&quot;\]\s*\+\s*\[pre_rep.IBQuery8.&quot;MASSRATE&quot;\]\s*\+\s*\[pre_rep.IBQuery8.&quot;INSRRATE&quot;\]',
					'\[F16ParcelNumber1\]',
					'\[F16SumValue1\]',
					'\[F16ParcelNumber2\]',
					'\[F16SumValue2\]',
					
					'\[pre_rep.IBQuery2.&quot;MASSRATE&quot;\]\s*\+\s*\[pre_rep.IBQuery2.&quot;INSRRATE&quot;\]\s*\+\s*\[pre_rep.IBQuery2.&quot;AIRRATE&quot;\]'
				)" />
				<xsl:variable name="to"
					select="(
					'[Str(Sum(IBQuery1.&quot;VALUE_&quot;), 2)]',
					'[Str(Sum(IBQuery1.&quot;PAYMENT&quot;), 2)]',
					'[Str([SumUvedMark], 2)] ([SumUvedMarkText])',
					'Str(Sum(IBQuery1.&quot;PERESULKASNDS&quot;)/1.18, 2)',
					'Str(Sum(IBQuery1.&quot;PERESULKASNDS&quot;), 2)',
					'Str([IBQuery1.&quot;PERESULKASNDS&quot;]/1.18, 2)',
					'[Str([IBQuery1.&quot;PERESULKASNDS&quot;], 2)]',
					'[Str([IBQuery1.&quot;VALUE_&quot;], 2)]',
					'[Str([IBQuery1.&quot;PAYMENT&quot;], 2)]',
					'[Str([IBQuery1.&quot;MASS&quot;], 3)]',
					'Copy(IBQuery1.&quot;BARCODE&quot;, 1, 6)] [Copy(IBQuery1.&quot;BARCODE&quot;, 7, 2)] [Copy(IBQuery1.&quot;BARCODE&quot;, 9, 5)] [Copy(IBQuery1.&quot;BARCODE&quot;, 14, 1)',
					'IBQuery1.&quot;COMMENT77&quot;',
					'IBQuery1.&quot;WITHOUTNDS&quot;',
					'IBQuery1.&quot;PAYSUBTYPE&quot;',
					'IBQuery103.&quot;MARKS&quot;',
					'ToRoublesRU([Sum(IBQuery1.&quot;PERESULKASNDS&quot;)])',
					'Str(Sum(IBQuery1.&quot;PERESULKASNDS&quot;), 2)',
					'ToRoublesRU(Round(([Sum(IBQuery1.&quot;PERESULKASNDS&quot;)] - [Sum(IBQuery1.&quot;PERESULKASNDS&quot;)]/1.18) * 100.0) / 100.0)',
					'Str(Round(([Sum(IBQuery1.&quot;PERESULKASNDS&quot;)] - [Sum(IBQuery1.&quot;PERESULKASNDS&quot;)]/1.18) * 100.0) / 100.0, 2)',
					'ToRoublesRU(Round([Sum(IBQuery1.&quot;INSRRATE&quot;)] * (1 - 1/1.18) * 100.0) / 100.0)',
					'Str(Round([Sum(IBQuery1.&quot;INSRRATE&quot;)] * (1 - 1/1.18) * 100.0) / 100.0, 2)',
					'ToRoublesRU(Sum(IBQuery1.&quot;INSRRATE&quot;))',
					'Str(Sum(IBQuery1.&quot;INSRRATE&quot;), 2)',
					'ToRoublesRU(Sum(IBQuery1.&quot;ITOGOZAPERESULKU&quot;))',
					'Str(Sum(IBQuery1.&quot;ITOGOZAPERESULKU&quot;), 2)',
					'ToRoublesRU(Round([sumUvedMark] * (1 - 1/1.18) * 100.0) / 100.0)',
					'Str(Round([sumUvedMark] * (1 - 1/1.18) * 100.0) / 100.0, 2)',
					'ToRoublesRU([Sum(IBQuery1.&quot;ITOGOKOPLATE&quot;)])',
					'Str(Sum(IBQuery1.&quot;ITOGOKOPLATE&quot;), 2)',
					'ToRoublesRU(Round([Sum(IBQuery1.&quot;ITOGOKOPLATE&quot;)] * (1 - 1/1.18) * 100.0) / 100.0)',
					'Str(Round([Sum(IBQuery1.&quot;ITOGOKOPLATE&quot;)] * (1 - 1/1.18) * 100.0) / 100.0, 2)',
					
					'[pre_rep.IBTable1.&quot;ZIP_ADR1&quot;]',
					'[pre_rep.IBTable1.&quot;ADRES1&quot;]',
					'[pre_rep.IBQuery8.&quot;MAININDEX&quot;]',
					'[Str([pre_rep.IBQuery8.&quot;MASS&quot;], 3)]',
					'Str([pre_rep.IBQuery8.&quot;ITOGOZAPERESULKU&quot;], 2)',
					'[Copy([F16ParcelNumber1], 1, 6)] [Copy([F16ParcelNumber1], 7, 2)] [Copy([F16ParcelNumber1], 9, 5)] [Copy([F16ParcelNumber1], 14, 1)]',
					'[Str([F16SumValue1], 2)]',
					'[Copy([F16ParcelNumber2], 1, 6)] [Copy([F16ParcelNumber2], 7, 2)] [Copy([F16ParcelNumber2], 9, 5)] [Copy([F16ParcelNumber2], 14, 1)]',
					'[Str([F16SumValue2], 2)]',

					'[pre_rep.IBQuery2.&quot;ITOGOZAPERESULKU&quot;]'
				)" />

				<xsl:value-of select="functx:replace-multi(Memo, $fr, $to)" />
			</xsl:element>
		</xsl:copy>
	</xsl:template>

	<!-- ============================================== -->
	<!-- Replace Variable with name or content 'barcod' -->
	<!-- ============================================== -->
	<xsl:template match="Variable[@Name=('barkod') or @Content=('barkod')]">
		<xsl:call-template name="set-exp-variable-value">
			<xsl:with-param name="variable" select="current()" />
			<xsl:with-param name="value" select="1234567890" />
		</xsl:call-template>
	</xsl:template>

	<!-- =============================================== -->
	<!-- Replace Variable with name or content 'barcodl' -->
	<!-- =============================================== -->
	<xsl:template match="Variable[@Name=('barcodl') or @Content=('barcodl')]">
		<xsl:call-template name="set-exp-variable-value">
			<xsl:with-param name="variable" select="current()" />
			<xsl:with-param name="value" />
		</xsl:call-template>
	</xsl:template>

	<!-- ================================================== -->
	<!-- Replace Variable with name or content 'barkod_ems' -->
	<!-- ================================================== -->
	<xsl:template match="Variable[@Name=('barkod_ems') or @Content=('barkod_ems')]">
		<xsl:call-template name="set-db-variable-value">
			<xsl:with-param name="variable" select="current()" />
			<xsl:with-param name="dataset" select="'pre_rep.IBQuery8'" />
			<xsl:with-param name="content" select="'BARCODE'" />
		</xsl:call-template>
	</xsl:template>

	<!-- ======================================== -->
	<!-- Replace Variable with name 'SumUvedMark' -->
	<!-- ======================================== -->
	<xsl:template match="Variable[@Name=('SumUvedMark')]">
		<xsl:call-template name="set-exp-variable-value">
			<xsl:with-param name="variable" select="current()" />
			<xsl:with-param name="value" select="'Sum(IBQuery1.&quot;UVEDRATE_VAL&quot;)'" />
		</xsl:call-template>
	</xsl:template>

	<!-- ============================================ -->
	<!-- Replace Variable with name 'SumUvedMarkText' -->
	<!-- ============================================ -->
	<xsl:template match="Variable[@Name=('SumUvedMarkText')]">
		<xsl:call-template name="set-exp-variable-value">
			<xsl:with-param name="variable" select="current()" />
			<xsl:with-param name="value" select="'ToRoublesRU([sumUvedMark])'" />
		</xsl:call-template>
	</xsl:template>

	<!-- ===================================== -->
	<!-- Replace Variable with name 'CountRec' -->
	<!-- ===================================== -->
	<xsl:template match="Variable[@Name=('CountRec')]">
		<xsl:choose>
			<xsl:when test="//BandView[@Type='master-data' and @Dataset='f_16']">
				<xsl:call-template name="set-db-variable-value">
					<xsl:with-param name="variable" select="current()" />
					<xsl:with-param name="dataset" select="'f_16_Ext'" />
					<xsl:with-param name="content" select="'COUNT_REC'" />
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:copy>
					<xsl:apply-templates select="@*|node()" />
				</xsl:copy>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- ======================================== -->
	<!-- Replace Variable with name 'CountRecStr' -->
	<!-- ======================================== -->
	<xsl:template match="Variable[@Name=('CountRecStr')]">
		<xsl:choose>
			<xsl:when test="//BandView[@Type='master-data' and @Dataset='f_16']">
				<xsl:call-template name="set-exp-variable-value">
					<xsl:with-param name="variable" select="current()" />
					<xsl:with-param name="value" select="'ToWordsRU([CountRec])'" />
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="set-exp-variable-value">
					<xsl:with-param name="variable" select="current()" />
					<xsl:with-param name="value" select="'ToWordsRU(Count(IBQuery1))'" />
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- ======================================== -->
	<!-- Replace Variable with name 'SumValueStr' -->
	<!-- ======================================== -->
	<xsl:template match="Variable[@Name=('SumValueStr')]">
		<xsl:call-template name="set-exp-variable-value">
			<xsl:with-param name="variable" select="current()" />
			<xsl:with-param name="value" select="'ToRoublesRU(Round(Sum(IBQuery1.&quot;VALUE_&quot;) * 100.0) / 100.0)'" />
		</xsl:call-template>
	</xsl:template>

	<!-- ========================================= -->
	<!-- Replace Variable with name 'type_otpravl' -->
	<!-- ========================================= -->
	<xsl:template match="Variable[@Name=('type_otpravl')]">
		<xsl:choose>
			<xsl:when test="//BandView[@Type='master-data' and @Dataset='f_16']">
				<xsl:call-template name="set-db-variable-value">
					<xsl:with-param name="variable" select="current()" />
					<xsl:with-param name="dataset" select="'f_16_Ext'" />
					<xsl:with-param name="content" select="'TYPE_OTPRAVL'" />
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="set-db-variable-value">
					<xsl:with-param name="variable" select="current()" />
					<xsl:with-param name="dataset" select="'IBQuery103'" />
					<xsl:with-param name="content" select="'TYPE_OTPRAVL'" />
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- ========================================== -->
	<!-- Replace Variable with name 'kateg_otpravl' -->
	<!-- ========================================== -->
	<xsl:template match="Variable[@Name=('kateg_otpravl')]">
		<xsl:choose>
			<xsl:when test="//BandView[@Type='master-data' and @Dataset='f_16']">
				<xsl:call-template name="set-db-variable-value">
					<xsl:with-param name="variable" select="current()" />
					<xsl:with-param name="dataset" select="'f_16_Ext'" />
					<xsl:with-param name="content" select="'KATEG_OTPRAVL'" />
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="set-db-variable-value">
					<xsl:with-param name="variable" select="current()" />
					<xsl:with-param name="dataset" select="'IBQuery103'" />
					<xsl:with-param name="content" select="'KATEG_OTPRAVL'" />
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- ====================================== -->
	<!-- Replace Variable with name 'razr_otpr' -->
	<!-- ====================================== -->
	<xsl:template match="Variable[@Name=('razr_otpr')]">
		<xsl:call-template name="set-db-variable-value">
			<xsl:with-param name="variable" select="current()" />
			<xsl:with-param name="dataset" select="'IBQuery103'" />
			<xsl:with-param name="content" select="'RAZR_OTPR'" />
		</xsl:call-template>
	</xsl:template>

	<!-- ======================================= -->
	<!-- Replace Variable with name 'otpravitel' -->
	<!-- ======================================= -->
	<xsl:template match="Variable[@Name=('otpravitel')]">
		<xsl:call-template name="set-db-variable-value">
			<xsl:with-param name="variable" select="current()" />
			<xsl:with-param name="dataset" select="'IBQuery103'" />
			<xsl:with-param name="content" select="'OTPRAVITEL'" />
		</xsl:call-template>
	</xsl:template>

	<!-- ====================================== -->
	<!-- Replace Variable with name 'indexfrom' -->
	<!-- ====================================== -->
	<xsl:template match="Variable[@Name=('indexfrom')]">
		<xsl:call-template name="set-db-variable-value">
			<xsl:with-param name="variable" select="current()" />
			<xsl:with-param name="dataset" select="'IBQuery103'" />
			<xsl:with-param name="content" select="'INDEXFROM'" />
		</xsl:call-template>
	</xsl:template>

	<!-- ================================ -->
	<!-- Replace Variable with name 'pps' -->
	<!-- ================================ -->
	<xsl:template match="Variable[@Name=('pps')]">
		<xsl:choose>
			<xsl:when test="//BandView[@Type='master-data' and @Dataset='f_16']">
				<xsl:call-template name="set-db-variable-value">
					<xsl:with-param name="variable" select="current()" />
					<xsl:with-param name="dataset" select="'f_16_Ext'" />
					<xsl:with-param name="content" select="'PPS'" />
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="set-db-variable-value">
					<xsl:with-param name="variable" select="current()" />
					<xsl:with-param name="dataset" select="'IBQuery103'" />
					<xsl:with-param name="content" select="'PPS'" />
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- =================================== -->
	<!-- Replace Variable with name 'To_F16' -->
	<!-- =================================== -->
	<xsl:template match="Variable[@Name=('To_F16')]">
		<xsl:call-template name="set-db-variable-value">
			<xsl:with-param name="variable" select="current()" />
			<xsl:with-param name="dataset" select="'f_16_Ext'" />
			<xsl:with-param name="content" select="'OTPRAVITEL'" />
		</xsl:call-template>
	</xsl:template>

	<!-- ===================================== -->
	<!-- Replace Variable with name 'data_rep' -->
	<!-- ===================================== -->
	<xsl:template match="Variable[@Name=('data_rep')]">
		<xsl:call-template name="set-db-variable-value">
			<xsl:with-param name="variable" select="current()" />
			<xsl:with-param name="dataset" select="'f_16_Ext'" />
			<xsl:with-param name="content" select="'DATA_REP'" />
		</xsl:call-template>
	</xsl:template>
	
	<!-- =========================================== -->
	<!-- Replace Variable with name 'F16ParcelName1' -->
	<!-- =========================================== -->
	<xsl:template match="Variable[@Name=('F16ParcelName1')]">
		<xsl:call-template name="set-db-variable-value">
			<xsl:with-param name="variable" select="current()" />
			<xsl:with-param name="dataset" select="'IBQueryF16'" />
			<xsl:with-param name="content" select="'TYPE_OTPRAVL_1'" />
		</xsl:call-template>
	</xsl:template>
	
	<!-- ============================================= -->
	<!-- Replace Variable with name 'F16ParcelNumber1' -->
	<!-- ============================================= -->
	<xsl:template match="Variable[@Name=('F16ParcelNumber1')]">
		<xsl:call-template name="set-db-variable-value">
			<xsl:with-param name="variable" select="current()" />
			<xsl:with-param name="dataset" select="'IBQueryF16'" />
			<xsl:with-param name="content" select="'BARCODE_1'" />
		</xsl:call-template>
	</xsl:template>
	
	<!-- ========================================= -->
	<!-- Replace Variable with name 'F16SumValue1' -->
	<!-- ========================================= -->
	<xsl:template match="Variable[@Name=('F16SumValue1')]">
		<xsl:call-template name="set-db-variable-value">
			<xsl:with-param name="variable" select="current()" />
			<xsl:with-param name="dataset" select="'IBQueryF16'" />
			<xsl:with-param name="content" select="'VALUE_1'" />
		</xsl:call-template>
	</xsl:template>
	
	<!-- ========================================= -->
	<!-- Replace Variable with name 'F16Postmark1' -->
	<!-- ========================================= -->
	<xsl:template match="Variable[@Name=('F16Postmark1')]">
		<xsl:call-template name="set-db-variable-value">
			<xsl:with-param name="variable" select="current()" />
			<xsl:with-param name="dataset" select="'IBQueryF16'" />
			<xsl:with-param name="content" select="'POSTMARK_1'" />
		</xsl:call-template>
	</xsl:template>
	
	<!-- =========================================== -->
	<!-- Replace Variable with name 'F16ParcelName2' -->
	<!-- =========================================== -->
	<xsl:template match="Variable[@Name=('F16ParcelName2')]">
		<xsl:call-template name="set-db-variable-value">
			<xsl:with-param name="variable" select="current()" />
			<xsl:with-param name="dataset" select="'IBQueryF16'" />
			<xsl:with-param name="content" select="'TYPE_OTPRAVL_2'" />
		</xsl:call-template>
	</xsl:template>
	
	<!-- ============================================= -->
	<!-- Replace Variable with name 'F16ParcelNumber2' -->
	<!-- ============================================= -->
	<xsl:template match="Variable[@Name=('F16ParcelNumber2')]">
		<xsl:call-template name="set-db-variable-value">
			<xsl:with-param name="variable" select="current()" />
			<xsl:with-param name="dataset" select="'IBQueryF16'" />
			<xsl:with-param name="content" select="'BARCODE_2'" />
		</xsl:call-template>
	</xsl:template>
	
	<!-- ========================================= -->
	<!-- Replace Variable with name 'F16SumValue2' -->
	<!-- ========================================= -->
	<xsl:template match="Variable[@Name=('F16SumValue2')]">
		<xsl:call-template name="set-db-variable-value">
			<xsl:with-param name="variable" select="current()" />
			<xsl:with-param name="dataset" select="'IBQueryF16'" />
			<xsl:with-param name="content" select="'VALUE_2'" />
		</xsl:call-template>
	</xsl:template>
	
	<!-- ========================================= -->
	<!-- Replace Variable with name 'F16Postmark2' -->
	<!-- ========================================= -->
	<xsl:template match="Variable[@Name=('F16Postmark2')]">
		<xsl:call-template name="set-db-variable-value">
			<xsl:with-param name="variable" select="current()" />
			<xsl:with-param name="dataset" select="'IBQueryF16'" />
			<xsl:with-param name="content" select="'POSTMARK_2'" />
		</xsl:call-template>
	</xsl:template>

</xsl:stylesheet>
