#JOGL_HOME=../../../jogl/release/jogl-1.1.1-rc8-solaris-i586
if [ "" = "$JOGL_HOME" ]
then
	echo please set the JOGL_HOME directory
	exit
fi
java -Djava.library.path=$JOGL_HOME/lib -Dsun.java2d.opengl=false -jar dist/FX_WorldWind.jar
