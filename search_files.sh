for i in  */*/*.java */*/*/*.java */*.l */*.ini */*.mf *.sh *.bat *.txt ; do
  echo $i
  grep -i "5.1" $i
  echo "--------"
done
