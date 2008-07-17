/**
 * regression test: JFXC-1224 : Compiler error - indexing a sequence slice
 *
 * @test
 * @run
 */

import java.lang.System;

var nums = [100,400,800,900];
var slice = nums[1..<5] [0];
var slice2 = nums[1..<] [0]; //Compilation error
System.out.println("Slice {slice}");
System.out.println("Slice {slice2}");
