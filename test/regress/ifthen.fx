/*
 * Test if-then and if-then-else
 * @test
 * @run
 */

import java.lang.System;

var pass = true;
var x = 0;
var bool = false;
if bool then x = 77;
pass = (if x == 0 then pass else false);
System.out.println("{pass} Expect 0: {x}");
pass = (if (if bool then "yo") == null then pass else false);
System.out.println("{pass} Expect null: {if bool then "yo"}");
bool = true;
if bool then x = 77;
pass = (if x == 77 then pass else false);
System.out.println("{pass} Expect 77: {x}");
pass = (if (if bool then "yo").equals("yo") then pass else false);
System.out.println("{pass} Expect yo: {if bool then "yo"}");

System.out.println(if pass then "Pass" else "FAIL!");

