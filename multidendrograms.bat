@echo off

java -version 2>&1 | findstr "java.version.*1[.][6-99][.]" > nul

if errorlevel 1 (
echo Java version 1.6 or higher is required
start http://java.com/en/download/index.jsp
exit
)

echo MultiDendrograms 5.0.2
echo Copyright (c) 2018 Sergio Gomez, Alberto Fernandez
echo This program comes with ABSOLUTELY NO WARRANTY.
echo This is free software, and you are welcome to redistribute it under certain conditions.
echo ---
java -jar multidendrograms.jar
