#!/bin/sh
#javac -g CalcJSR223.java
javac -classpath "../../lib/script.jar:/../lib/swing-layout.jar:../../lib/Filters.jar" -g CalcJSR223.java
java -classpath ".:..:../../javafxrt.jar:../../lib/script.jar:../../lib/swing-layout.jar:../../lib/Filters.jar" CalcJSR223

