/*
 * Test if-then and if-then-else
 * @test
 * @run
 */

import java.lang.System;

var pass = true;
var x = 0;
var bool = false;
if (bool) x = 77;
pass = if (x == 0) pass else false;
System.out.println("{pass} Expect 0: {x}");
pass = if ((if (bool) "yo") == null) pass else false;
System.out.println("{pass} Expect null: {if (bool) "yo"}");
bool = true;
if (bool)  x = 77;
pass = if (x == 77) pass else false;
System.out.println("{pass} Expect 77: {x}");
pass = if ((if (bool) "yo").equals("yo")) pass else false;
System.out.println("{pass} Expect yo: {if (bool) "yo"}");

System.out.println(if (pass) "Pass" else "FAIL!");

