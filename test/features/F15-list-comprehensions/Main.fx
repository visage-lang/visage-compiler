/*
 * Feature test #15 - list comprehensions
* 
 * @test
 * @run
 */

import java.lang.System;

var nums = [5, 7, 3, 9];
var numsb = bind nums;
var strs = ["hi", "yo"];
var res = for (a in nums) { a + 1 };
var s = for (a in nums where a < 6) {"({ a }) " };
var db = for (x in strs, y in strs)  {"{indexof x}.{indexof y}:{ x }{ y }-- " };
var wh = for (x in [1..20] where x < 12, y in [100, 200, 400]  where x*100 >= y) { y + x };
for (i in [100..110]) { System.out.println("...{ i }"); };
System.out.println(nums);
System.out.println(strs);
System.out.println(res);
System.out.println(s);
System.out.println(db);
System.out.println(wh);

function print(display:String, result:String) : String { System.out.println(display); result }
var xs = ["a","b","c"];
var ys = bind for (x in xs) { print("body {indexof x}->{x}", "<{x}>") };
System.out.println("ys:{for (y in ys) " {y}"}");
xs[1]="w";
System.out.println("ys:{for (y in ys) " {y}"}");
//insert "v" before xs[1];
//System.out.println("ys:{for (y in ys) " {y}"}");


import java.lang.System;
var xis = [3,4,5,6];
var yis = bind for (x in xis) { print("body ->{x}", "<{x}>") };
System.out.println("yis:{for (y in yis) " {y}"}");
xis[2]=9;
System.out.println("yis:{for (y in yis) " {y}"}");
insert 7 before xis[1];
System.out.println("yis:{for (y in yis) " {y}"}");
