JFX=../../visage-compiler
JFX_LIB=$JFX/dist/lib
java -jar -Xbootclasspath/p:dist/lib/visagec.jar:dist/lib/visagert.jar:dist/lib/Scenario.jar -agentlib:jdwp=transport=dt_socket,address=4444,server=y,suspend=y dist/JavaFXPad.jar
