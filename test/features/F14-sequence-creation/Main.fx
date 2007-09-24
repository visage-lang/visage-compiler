/*
 * Feature test #14 -- sequence creation and access
 * 
 * @test
 * @run
 */

import java.lang.System;

class Bar { 
    attribute name : String;
    public function toString() : String { name }
}

var nums = [5, 7, 3, 9];
var strs = ["hi", "yo"];
var bars = [ Bar { name: "James" }, Bar { name: "Nancy" }, Bar { name: "Raymond" } ];
var range = [11..19];

System.out.println(nums);
System.out.println(strs);
System.out.println(bars);
System.out.println(range);

System.out.println(nums[3]);
System.out.println(strs[1]);
System.out.println(bars[0]);
System.out.println(range[2]);
