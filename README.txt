========================================================================

						FreeReport to Apache FOP Converter

========================================================================

'fr2afop' is a Java application that reads a FreeReport file and turns it
into a XML document, populating with data, and finally renders the report
as PDF document or certain other output formats. Apache FOP is used as 
rendering engine.

Converter execution cycle consists of several steps and looks as follows:

1) Convert Free Report file to XML.
  At this stage converter takes .frf file and produces .xml stream. XML file
  contains all pages, views and their positions on page, list of variables
  and other information belonging to a report. The acquired .xml file can be 
  used later as input for converter. When a report is being read from .xml
  file, the same report instance is restored as it would read from .frf file.
   
2) Correct XML output using XSL replacement template.
  This step is optional. It will be executed only if ReplacementFile is 
  specified in converter configuration. At this stage XML output can be modified
  by applying any rules that you may define. The rules are specified as XSL 
  templates. For example, you may want to replace a MemoView to BarcodeView 
  sometimes or assign defined value to not-assigned variable.

3) Fetch data from database, place data in XML.
  This step is optional. It will be applied only if the data sources listed in 
  a report will be defined in convert configuration. FreeReport file does not 
  contain data source definition (like connection settings or SQL query or 
  list of fields). The datasource is defined in Delphi application and we can't
  get an access to it. So, we define our own datasources in convert configuration
  file. For details about configuring a converter, see the comments in corresponding 
  configuration files of ./conf directory.

4) Convert XML to XSL-FO.
  This step precedes rendering. At this stage .xml report file is being converted 
  to XSL-FO format using build-in XSL template. After that the report will be 
  rendered as output, but if you specify XSL-FO transformation (-ofo) instead of
  rendering, then converter will stop here (you will get .fo file as the result).

5) Render XSL-FO as PDF/PCL/PS output.
  This is the final stage. The output file will be acquired after that. Converter
  will go to the next command or will finish process if it was the last one.

Converter can be invoked twice: firstly you start it in order to convert .frf 
file to .xml, then make any changes in .xml file or populating the data manually,
finally you launch converter to render .xml file as PDF output or certain other 
output formats.

More details about Free Report engine can be found at
http://freereport.sourceforge.net

Note that not all parts of the FreeReport functionality have yet been
implemented. Read release notes in order to understand, what features are not 
ready.

Apache FOP is part of Apache's XML Graphics project. The homepage of Apache 
FOP is http://xmlgraphics.apache.org/fop/.


BUILDING THE APPLICATION
------------------------

1. Requirements

In order to build application you will need:
	- Maven 3.0.x or later
	- JDK 1.6 or later

Also optional additional software would make it easy for you to work 
with the sources of the application:

    - Spring Tool Suite IDE (to explore and run the application 
      in IDE). You're obviously free to use any other IDE you wish. 

2. Build

To build the project you need to navigate to the project root folder and 
run:

	mvn clean package
	
Maven will read project specification in pom.xml file, will download all 
dependencies, will compile the project source code and build .jar file. 

3. Deploy

By default .jar file is compiled without dependencies. In order to use it 
as console application, you need to add dependencies and configuration files.
This was done for you by Maven during packaging. The complete and final
distribution was placed in /target/dist/ folder as .zip archive.

4. Sources

During packaging Maven places all sources in /target/src/ folder as .zip 
archive.


RUNNING THE APPLICATION
-----------------------

Converter is a console application. Simply run it by typing 'fr2afop' in 
the command-line. Without any parameters you will receive a screen with 
instruction on how to call converter from the command-line.

The converter executes commands in pairs: it takes some input and produces
some output. Such input and output parameters are named 'command pair'.
Command line can consist of as many command pairs as you want, converter
will executes them one by one sequentially. 

General usage of converter is as follows:
  
	fr2afop CommandPair1 CommandPair2 CommandPair3 ...  

, where CommandPairX is actually two commands:
  
	-i[fr|fr2217|xml|ttf] inputFile -o[xml|pdf|pcl|ps|png|fo] outputFile

The input and output formats can belong to any type from listed below:

Input formats:
 ifr     - reads FreeReport file and converts it to xml or renders it
 ifr2217 - reads FreeReport file in old format (version 2217)
 ixml    - reads XML-file acquired after conversion
 ittf    - reads the TTF file and generates an appropriate font metrics xml-file for it

Output formats:
 oxml    - specifies XML file conversion
 opdf    - specifies PDF rendering
 opcl    - specifies PCL rendering
 ops     - specifies PostScript rendering
 opng    - specifies PNG rendering
 ofo     - specifies XSL-FO transformation

If not specified explicitly configuration files are taken from ./conf directory. For 
additional details how to specify paths to configuration files, look into contents of
'fr2afop' batch script.

For details about configuring a converter, see the comments in corresponding configuration
files of ./conf directory.

The reports sometimes use different fonts. A new font must be described in converter
configuration, otherwise it will be replaced to existing font. In order to add a new 
font, you should:
1) create XML font-metrics file (use -ittf and -oxml as command pair);
2) copy created XML file and TTF font to /fonts folder; 
3) describe a font in Apache FOP configuration file (see the details in /conf/fop.xconf).


RELEASE NOTES
-------------

Version 1.0.

This is first production grade release of "FreeReport to Apache FOP Converter".

The following features of FreeReport engine have NOT been implemented:
	Page can't be splitted in columns.
	The following bands are not supported: Detail, SubDetail, Overlay, Group, VarColumn.
	Bands can't be stretched.
	LineView and SubReportView are not rendered.
	MemoView flags (stretched, word-wrap) are not supported.
	MemoView 'format' flags are not used.
	MemoView highlight field is not used.
	
