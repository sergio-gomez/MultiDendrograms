#!/bin/bash

echo MultiDendrograms 5.1.0
echo Copyright \(c\) 2019 Sergio Gomez, Alberto Fernandez
echo
echo This program comes with ABSOLUTELY NO WARRANTY.
echo This is free software, and you are welcome to redistribute it under certain conditions.
echo

java -version
echo ---------
java -jar multidendrograms.jar  -help
echo ---------
java -jar multidendrograms.jar  -direct sample_files/data.txt distances 2 Ward
echo ---------
java -jar multidendrograms.jar  -direct sample_files/data.txt similarities 2 Complete_Linkage
echo ---------
java -jar multidendrograms.jar  -direct sample_files/air_distances.txt D AL
echo ---------
java -jar multidendrograms.jar  -direct sample_files/air_distances.txt D VL 0.1
echo ---------
java -jar multidendrograms.jar  -direct sample_files/air_distances.txt D WD weighted
echo ---------
