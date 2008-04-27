JFX=../../openjfx-compiler
JFX_LIB=$JFX/dist/lib
java -jar -Xbootclasspath/p:dist/lib/javafxc.jar:dist/lib/javafxrt.jar:dist/lib/Scenario.jar -agentlib:jdwp=transport=dt_socket,address=4444,server=y,suspend=y dist/JavaFXPad.jar
