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

#--------------------------------------------
#  gather command line arguments
#--------------------------------------------

CMD_LINE_ARGS=
for ARG in "$@" ; do
  CMD_LINE_ARGS="$CMD_LINE_ARGS \"$ARG\""
done 

#--------------------------------------------
#  run program
#--------------------------------------------

exec_command="java $JAVA_OPTS -jar \"$SCRIPTDIR/fr2afop.jar\" $CMD_LINE_ARGS"
eval $exec_command
