#!/bin/bash

#JavaPath=/cygdrive/c/usrlocal/jdk1.6.0_22/bin
#JavaPath=/cygdrive/c/usrlocal/jdk1.8.0_201/bin
JavaPath=/cygdrive/c/usrlocal/jdk-12.0.1/bin
export PATH=$JavaPath:$PATH

echo MultiDendrograms 5.2.1
echo Copyright \(c\) 2023 Sergio Gomez, Alberto Fernandez
echo
echo This program comes with ABSOLUTELY NO WARRANTY.
echo This is free software, and you are welcome to redistribute it under certain conditions.
echo

java -version
java -jar multidendrograms.jar
