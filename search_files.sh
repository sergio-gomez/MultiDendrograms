for i in  */*/*.java */*/*/*.java */*/*/*/*.java */*.l */*.ini */*.mf ; do
  echo $i
  grep -i "main[ ]*(" $i
  echo "--------"
done
