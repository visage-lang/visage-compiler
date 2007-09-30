import java.lang.System;
import java.util.*;

var nums  = [5, 7, 3, 9];
var numsb = bind nums;
var strs = ["hi", "yo"];
var res = foreach (a in nums) { a + 1 };
var s = foreach (a in nums where a < 6) {"({ a }) " };
var db = foreach (x in strs, y in strs)  {"{ x }{ y }-- " };
var wh = foreach (x in [1..20] where x < 12, y in [100, 200, 400]  where x*100 >= y) { y + x };
foreach (i in [100..110]) { System.out.println("...{ i }"); };
System.out.println(nums);
System.out.println(strs);
System.out.println(res);
System.out.println(s);
System.out.println(db);
System.out.println(wh);
System.out.println([1..30]);

System.out.println(db[0]);
System.out.println(nums[1]);
nums[1] = 999;
System.out.println(nums[1]);
System.out.println(nums);
System.out.println(numsb);

