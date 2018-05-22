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
		echo MultiDendrograms 5.0.3
		echo Copyright \(c\) 2018 Sergio Gomez, Alberto Fernandez
		echo This program comes with ABSOLUTELY NO WARRANTY.
		echo This is free software, and you are welcome to redistribute it under certain conditions.
		echo ---------
		java -jar multidendrograms.jar  -help
		echo ---------
		java -jar multidendrograms.jar  -direct sample_files/data.txt distances 2 Complete_Linkage
		echo ---------
		java -jar multidendrograms.jar  -direct sample_files/air_distances.txt D AL
		echo ---------
		java -jar multidendrograms.jar  -direct sample_files/air_distances.txt D VL 0.1
		echo ---------
		java -jar multidendrograms.jar  -direct sample_files/air_distances.txt D WD weighted
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
