#!/bin/bash
REQUIRED_VERSION=1.6
# Transform the required version string into a number that can be used in comparisons
REQUIRED_VERSION_NUM=`echo $REQUIRED_VERSION | sed -e 's;\.;0;g'`

java -version 2> ./tmp.ver
VERSION=`cat ./tmp.ver | grep "java version" | awk '{ print substr($3, 2, length($3)-2); }'`
rm ./tmp.ver

VERSION=`echo $VERSION | awk '{ print substr($1, 1, 3); }' | sed -e 's;\.;0;g'`
if [ $VERSION ]
then
	if [ $VERSION -ge $REQUIRED_VERSION_NUM ]
	then
		echo MultiDendrograms 3.2.0
		echo Copyright \(c\) 2014 Sergio Gomez, Alberto Fernandez, Justo Montiel, David Torres
		echo This program comes with ABSOLUTELY NO WARRANTY.
		echo This is free software, and you are welcome to redistribute it under certain conditions.
		echo ---------
		java -jar multidendrograms.jar  -direct sample_files/data.txt distances Complete_Linkage 2
		echo ---------
		java -jar multidendrograms.jar  -direct sample_files/air_distances.txt D UA
		echo ---------
	else
		echo Java version $REQUIRED_VERSION or higher is required
		echo
		echo Please download the latest Java version from:
		echo "http://www.java.com/download/index.jsp"
		echo Then set the Java environment path and run the program again
		echo
	fi
fi
