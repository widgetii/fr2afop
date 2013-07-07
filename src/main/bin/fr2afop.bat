@ECHO OFF

@rem --------------------------------------------
@rem  set parameters
@rem --------------------------------------------

set SCRIPTDIR=%~dp0
cd %SCRIPTDIR%

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

call java %JAVA_OPTS% -jar "%SCRIPTDIR%fr2afop.jar" %CMD_LINE_ARGS%
