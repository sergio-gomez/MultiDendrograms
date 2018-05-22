VER_OLD=5.0.2
VER_NEW=5.0.3

for i in  */*/*.java */*/*/*.java ; do
  echo "--------------------------------------------------------------"
  echo $i
  echo ""
  a=`pwd|sed "s/$VER_NEW/$VER_OLD/g"`
  a=$a/$i
  diff $a $i;
done
