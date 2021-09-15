VER_OLD=5.1.0
VER_NEW=5.2.0

FN_OUT=search_diffs.txt

echo "diff "$VER_OLD" "$VER_NEW > $FN_OUT
echo "" >> $FN_OUT

for i in  */*/*.java */*/*/*.java ; do
  echo $i
  echo "--------------------------------------------------------------" >> $FN_OUT
  echo $i >> $FN_OUT
  echo "" >> $FN_OUT
  a=`pwd|sed "s/$VER_NEW/$VER_OLD/g"`
  a=$a/$i
  diff $a $i &>> $FN_OUT
done
