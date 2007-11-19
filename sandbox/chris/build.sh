#!/bin/sh
mkdir -p build/classes
../../dist/bin/javafxc -target 1.5 -d build/classes hello/*.fx

