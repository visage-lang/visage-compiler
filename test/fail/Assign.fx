// Fails unless unconstifyHack (in type morpher) is used -- Issue 20
import java.lang.System;

var aaa = 100;
aaa = 2 + aaa;
System.out.println("Should be 102={aaa}");

