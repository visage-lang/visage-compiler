/*
 * Test automatic conversion of primitive datatypes
 * See also jfxc915b.fx.
 *
 * @test/fail
 */

import java.lang.*;

var i1: Integer = Long.parseLong("100000001", 16);
System.out.println("(Integer)0x100000001L = {i1}");

i1 = Long.parseLong("100000002", 16);
System.out.println("(Integer)0x100000002L = {i1}");

var i2: Integer = 3.5;
System.out.println("(Integer)3.5 = {i2}");

i2 = -3.6;
System.out.println("(Integer)-3.6 = {i2}");


