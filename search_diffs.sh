VER_OLD=5.0.3
VER_NEW=5.0.4

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
