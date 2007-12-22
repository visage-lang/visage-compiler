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
var range = [19..11];
var rangeStep = [0..1000 step 100];
var rangeStepE = [0..1000 step 100 exclusive];
var nRange = [12.5..18.5 step 0.5];
var nRangeE = [12.5..18.5 step 0.5 exclusive];
var toZ = [1..0 step -0.1];

var emptyStart : Integer[] = [];
insert 1234 into emptyStart;

var fractionalRange = [0.3..1.0 step .1 ];
var fractionalRangeDsc = [1.0..0.3 step -.1];

System.out.println(nums);
System.out.println(strs);
System.out.println(bars);
System.out.println(range);
System.out.println(rangeStep);
System.out.println(rangeStepE);
System.out.println(nRange);
System.out.println(nRangeE);
System.out.println(toZ);
System.out.println(emptyStart);

System.out.println(nums[3]);
System.out.println(strs[1]);
System.out.println(bars[0]);
System.out.println(range[2]);

System.out.println(sizeof fractionalRange);
System.out.println(sizeof fractionalRangeDsc);
