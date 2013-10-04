cls
@echo off

@rem --------------------------------------------
@rem  set parameters
@rem --------------------------------------------

set SCRIPTDIR=%~dp0
cd %SCRIPTDIR%

set SRCDIR=%SCRIPTDIR%
set OUTDIR=%SCRIPTDIR%output\

set JAVA_OPTS=-Dfile.encoding=Cp1251

@rem --------------------------------------------
@rem  create output directory
@rem --------------------------------------------

if NOT "%SCRIPTDIR%"=="%OUTDIR%" md "%OUTDIR%"

@rem --------------------------------------------
@rem  run program, convert 3 files
@rem --------------------------------------------

set PARAMS=%PARAMS% -ifr "%SRCDIR%sample.frf" -oxml "%OUTDIR%sample.xml"
set PARAMS=%PARAMS% -ixml "%OUTDIR%sample.xml" -opdf "%OUTDIR%sample.pdf"

set PARAMS=%PARAMS% -ifr2217 "%SRCDIR%f103.frf" -oxml "%OUTDIR%f103.xml"
set PARAMS=%PARAMS% -ixml "%OUTDIR%f103.xml" -opdf "%OUTDIR%f103.pdf"

set PARAMS=%PARAMS% -ifr2217 "%SRCDIR%f113en_ls.frf" -oxml "%OUTDIR%f113en_ls.xml"
set PARAMS=%PARAMS% -ixml "%OUTDIR%f113en_ls.xml" -opdf "%OUTDIR%f113en_ls.pdf"

set PARAMS=%PARAMS% -ifr2217 "%SRCDIR%otm1.frf" -oxml "%OUTDIR%otm1.xml"
set PARAMS=%PARAMS% -ixml "%OUTDIR%otm1.xml" -opdf "%OUTDIR%otm1.pdf"

set PARAMS=%PARAMS% -ifr2217 "%SRCDIR%repE1.frf" -oxml "%OUTDIR%repE1.xml"
set PARAMS=%PARAMS% -ixml "%OUTDIR%repE1.xml" -opdf "%OUTDIR%repE1.pdf"

set PARAMS=%PARAMS% -ifr2217 "%SRCDIR%f16.frf" -oxml "%OUTDIR%f16.xml"
set PARAMS=%PARAMS% -ifr2217 "%SRCDIR%f16.frf" -opdf "%OUTDIR%f16.pdf"
set PARAMS=%PARAMS% -ifr2217 "%SRCDIR%f16.frf" -opcl "%OUTDIR%f16.pcl"
set PARAMS=%PARAMS% -ifr2217 "%SRCDIR%f16.frf" -ops "%OUTDIR%f16.ps"
set PARAMS=%PARAMS% -ifr2217 "%SRCDIR%f16.frf" -opng "%OUTDIR%f16.png"

set PARAMS=%PARAMS% -ifr2217 "%SRCDIR%f2.frf" -oxml "%OUTDIR%f2.xml"
set PARAMS=%PARAMS% -ixml "%OUTDIR%f2.xml" -opdf "%OUTDIR%f2.pdf"

set PARAMS=%PARAMS% -ifr2217 "%SRCDIR%f7a_new.frf" -oxml "%OUTDIR%f7a_new.xml"
set PARAMS=%PARAMS% -ixml "%OUTDIR%f7a_new.xml" -opdf "%OUTDIR%f7a_new.pdf"

set PARAMS=%PARAMS% -ifr2217 "%SRCDIR%f7b_new.frf" -oxml "%OUTDIR%f7b_new.xml"
set PARAMS=%PARAMS% -ixml "%OUTDIR%f7b_new.xml" -opdf "%OUTDIR%f7b_new.pdf"

set PARAMS=%PARAMS% -ifr2217 "%SRCDIR%f7l.frf" -oxml "%OUTDIR%f7l.xml"
set PARAMS=%PARAMS% -ixml "%OUTDIR%f7l.xml" -opdf "%OUTDIR%f7l.pdf"

set PARAMS=%PARAMS% -ifr2217 "%SRCDIR%f7p_new.frf" -oxml "%OUTDIR%f7p_new.xml"
set PARAMS=%PARAMS% -ixml "%OUTDIR%f7p_new.xml" -opdf "%OUTDIR%f7p_new.pdf"

set PARAMS=%PARAMS% -ifr2217 "%SRCDIR%f116_os.frf" -oxml "%OUTDIR%f116_os.xml"
set PARAMS=%PARAMS% -ixml "%OUTDIR%f116_os.xml" -opdf "%OUTDIR%f116_os.pdf"

call "%SCRIPTDIR%..\fr2afop.bat" %PARAMS%

@rem --------------------------------------------
@rem  wait user reaction
@rem --------------------------------------------

:NOTIFY
ECHO.
PAUSE