/*
 * Feature test #16 - sequence insert and delete
* 
 * @test
 * @run
 */

import java.lang.System;

var names = ["Bob", "Carol", "Ted", "Alice"];
delete names[1];
System.out.println(names);
delete "Alice" from names;
System.out.println(names);
delete "Berty" from names;
System.out.println(names);
insert "James" into names;
System.out.println(names);
insert "Max" into names;
System.out.println(names);
delete names;
insert "Self" into names;
insert "Self" into names;
System.out.println(names);

var nums = [0..10];
insert 97 into nums;
insert 98 into nums;
insert 99 into nums;
System.out.println(nums);
delete nums[10];
System.out.println(nums);
delete nums[8];
System.out.println(nums);
delete nums[6];
System.out.println(nums);
delete nums[4];
System.out.println(nums);
delete nums[2];
System.out.println(nums);
delete nums[0];
System.out.println(nums);
delete nums;
System.out.println(nums);
for (a in [1..20]) { insert a*a into nums; };
System.out.println(nums);
for (a in [1..200]) { delete a*2 from nums; };
System.out.println(nums);

nums = [0..10];
delete nums[3..7];
System.out.println(nums);
nums = [0..10];
delete nums[3..<7];
System.out.println(nums);
nums = [0..10];
delete nums[5..];
System.out.println(nums);
nums = [0..10];
delete nums[5..<];
System.out.println(nums);
nums = [0..10];
delete nums[9..2];
System.out.println(nums);

