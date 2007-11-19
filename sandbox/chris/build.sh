#!/bin/sh
../../dist/bin/javafxc -target 1.5 -d build/classes hello/*.fx
jar cvf build/hellofx.jar -C build/classes hello
