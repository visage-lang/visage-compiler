/*
 * Test for unconstifyHack (in type morpher) -- jfxc-20
 * @test
 * @run
 */
import java.lang.System;

var aaa = 100;
aaa = 2 + aaa;
System.out.println("Should be 102={aaa}");

