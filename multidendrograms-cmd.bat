@echo off

java -version 2>&1 | findstr "java.version.*1[.][6-99][.]" > nul

if errorlevel 1 (
echo Java version 1.6 or higher is required
start http://java.com/en/download/index.jsp
exit
)

echo MultiDendrograms 5.0.4
echo Copyright (c) 2018 Sergio Gomez, Alberto Fernandez
echo This program comes with ABSOLUTELY NO WARRANTY.
echo This is free software, and you are welcome to redistribute it under certain conditions.
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

pause
