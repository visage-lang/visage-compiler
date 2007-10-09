/*
 * Regression test: parameterized types
 * @test
 * @run
 */

import java.lang.System;

var keyMap : java.util.Map<java.lang.Integer, java.lang.String> = new  java.util.HashMap<java.lang.Integer, java.lang.String>;

keyMap.put(4, "four");
keyMap.put(8, "eight");
keyMap.put(3, "three");
keyMap.put(6, "six");

System.out.println(keyMap.get(6));
System.out.println(keyMap.get(4));
System.out.println(keyMap.get(3));

