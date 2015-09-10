for i in lib/*.jar dist/*.jar
do
export CLASSPATH=$CLASSPATH:$i
done
export CLASSPATH=$CLASSPATH:$1
shift
java -classpath $CLASSPATH org.me4se.MIDletRunner $*
