for i in  */*/*.java */*/*/*.java */*.l */*.ini */*.mf ; do
  echo $i
  grep -i "openFrameCount" $i
  echo "--------"
done
