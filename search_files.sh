for i in  */*/*.java */*/*/*.java */*.l */*.ini */*.mf *.sh *.bat *.txt ; do
  echo $i
  grep -i "Ward" $i
  echo "--------"
done
