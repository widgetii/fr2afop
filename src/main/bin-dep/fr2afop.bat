@ECHO OFF

@rem --------------------------------------------
@rem  set parameters
@rem --------------------------------------------

SETLOCAL ENABLEDELAYEDEXPANSION

set SCRIPTDIR=%~dp0
cd %SCRIPTDIR%

set JARFILE=%SCRIPTDIR%fr2afop-without-dependencies.jar
set LIBDIR=%SCRIPTDIR%\lib
set FOP_CONFIG=%SCRIPTDIR%\conf\fop.xconf
set APP_CONFIG=%SCRIPTDIR%\conf\fr2afop.xconf

for %%l in (%JARFILE% %LIBDIR%\*.jar) do set LOCALCLASSPATH=%%l;!LOCALCLASSPATH!

set JAVA_OPTS=%JAVA_OPTS% -Dorg.apache.commons.logging.Log=org.apache.commons.logging.impl.Log4JLogger

@rem --------------------------------------------
@rem  gather command line arguments
@rem --------------------------------------------

set CMD_LINE_ARGS=%1
if ""%1""=="""" goto doneStart
shift
:setupArgs
if ""%1""=="""" goto doneStart
set CMD_LINE_ARGS=%CMD_LINE_ARGS% %1
shift
goto setupArgs
:doneStart

@rem --------------------------------------------
@rem  run program
@rem --------------------------------------------

call java %JAVA_OPTS% -cp "%LOCALCLASSPATH%" ru.aplix.converters.fr2afop.app.ConsoleApp -fc "%FOP_CONFIG%" -cc "%APP_CONFIG%" %CMD_LINE_ARGS%
