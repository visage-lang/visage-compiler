JFX=/export/home/jclarke/src/openjfx-compiler
JFX_LIB=$JFX/dist/lib
java -jar -agentlib:jdwp=transport=dt_socket,address=4444,server=y,suspend=y dist/JavaFXPad.jar
