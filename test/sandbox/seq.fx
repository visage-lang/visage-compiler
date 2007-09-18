import java.lang.System;

var nums = [5, 7, 3, 9];
var strs = ["hi", "yo"];
var res = for (a in nums) { a + 1 };
var s = for (a in nums where a < 6) {"({ a }) " };
var db = for (x in strs, y in strs)  {"{ x }{ y }-- " };
var wh = for (x in [1, 2, 3, 4, 5] where x < 5, y in [100, 200, 400]  where x*100 >= y) { y + x };
System.out.println(nums);
System.out.println(strs);
System.out.println(res);
System.out.println(s);
System.out.println(db);
System.out.println(wh);
