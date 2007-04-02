#!/bin/sh
# jve.sh
# Thiago Nóbrega (thiagonobrega@gmail.com ) -  01-03-2007

batch_file=$1

if [ $# -eq 0 ]
then
	echo "Starting jve!"
	java -Dcom.sun.management.jmxremote -Dawt.toolkit=sun.awt.HeadlessToolkit -cp lib/jmf.jar:lib/fobs4jmf.jar:lib/log4j-1.2.14.jar:bin/ engine.ui.text.KeyboardReader

fi

echo $batch_file
if [ $# -lt 1 ]
then
	echo "Usage jve batch_script.jves"
	else
	java -Dcom.sun.management.jmxremote -Dawt.toolkit=sun.awt.HeadlessToolkit -cp lib/jmf.jar:lib/fobs4jmf.jar:lib/log4j-1.2.14.jar:bin/ engine.ui.text.KeyboardReader < $batch_file
fi