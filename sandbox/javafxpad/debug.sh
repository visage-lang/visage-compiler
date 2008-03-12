JFX=/export/home/jclarke/src/openjfx-compiler
JFX_LIB=$JFX/dist/lib
java -agentlib:jdwp=transport=dt_socket,address=4444,server=y,suspend=y -cp dist/Guitar.jar:dist/lib/javafxrt.jar:dist/lib/Scenario-0.4.jar $*
