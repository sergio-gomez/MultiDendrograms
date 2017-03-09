for i in  */*/*.java */*/*/*.java */*.l */*.ini */*.mf ; do
  echo $i
  grep -i "Versatile" $i
  echo "--------"
done
