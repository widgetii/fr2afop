#!/bin/sh

#--------------------------------------------
#  set parameters
#--------------------------------------------

# Absolute path to this script, e.g. /home/user/bin/foo.sh
SCRIPT=$(readlink -f $0)
# Absolute path this script is in, thus /home/user/bin
SCRIPTDIR=$(dirname $SCRIPT)
# Change dir to script dir
cd $SCRIPTDIR

SRCDIR=$SCRIPTDIR
OUTDIR=$SCRIPTDIR/output

export JAVA_OPTS="$JAVA_OPTS -Dfile.encoding=Cp1251"

#--------------------------------------------
#  create output directory
#--------------------------------------------

if [ ! -d "$OUTDIR" ]; then 
 mkdir "$OUTDIR"
fi

#--------------------------------------------
#  convert 3 files
#--------------------------------------------

PARAMS=
PARAMS="$PARAMS -ifr \"$SRCDIR/sample.frf\" -oxml \"$OUTDIR/sample.xml\""
PARAMS="$PARAMS -ixml \"$OUTDIR/sample.xml\" -opdf \"$OUTDIR/sample.pdf\""

PARAMS="$PARAMS -ifr2217 \"$SRCDIR/f103.frf\" -oxml \"$OUTDIR/f103.xml\""
PARAMS="$PARAMS -ixml \"$OUTDIR/f103.xml\" -opdf \"$OUTDIR/f103.pdf\""

PARAMS="$PARAMS -ifr2217 \"$SRCDIR/f113en_ls.frf\" -oxml \"$OUTDIR/f113en_ls.xml\""
PARAMS="$PARAMS -ixml \"$OUTDIR/f113en_ls.xml\" -opdf \"$OUTDIR/f113en_ls.pdf\""

PARAMS="$PARAMS -ifr2217 \"$SRCDIR/otm1.frf\" -oxml \"$OUTDIR/otm1.xml\""
PARAMS="$PARAMS -ixml \"$OUTDIR/otm1.xml\" -opdf \"$OUTDIR/otm1.pdf\""

PARAMS="$PARAMS -ifr2217 \"$SRCDIR/repE1.frf\" -oxml \"$OUTDIR/repE1.xml\""
PARAMS="$PARAMS -ixml \"$OUTDIR/repE1.xml\" -opdf \"$OUTDIR/repE1.pdf\""

PARAMS="$PARAMS -ifr2217 \"$SRCDIR/f16.frf\" -oxml \"$OUTDIR/f16.xml\""
PARAMS="$PARAMS -ifr2217 \"$SRCDIR/f16.frf\" -opdf \"$OUTDIR/f16.pdf\""
PARAMS="$PARAMS -ifr2217 \"$SRCDIR/f16.frf\" -opcl \"$OUTDIR/f16.pcl\""
PARAMS="$PARAMS -ifr2217 \"$SRCDIR/f16.frf\" -ops \"$OUTDIR/f16.ps\""
PARAMS="$PARAMS -ifr2217 \"$SRCDIR/f16.frf\" -opng \"$OUTDIR/f16.png\""

#--------------------------------------------
#  run program
#--------------------------------------------

exec_command="\"$SCRIPTDIR/../fr2afop.sh\" $PARAMS"
eval $exec_command
