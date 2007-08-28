/* Feature test #5 -- sequences
 * Demonstrates simple manipulation of sequences -- creation, extraction, insertion, deletion
 *
 * @test
 * @run
 */

import java.lang.System;

var x = [ 1..4 ];
System.out.println(x);    // 1, 2, 3, 4

x = [1, 2, 3];
System.out.println(x);    // 1, 2, 3

System.out.println(x[1]); // 2

System.out.println(x.size);
                          // 3

System.out.println(x[100]); // 0

insert 4 into x;
System.out.println(x);    // 1, 2, 3, 4

delete 2 from x;
System.out.println(x);    // 1, 3, 4

delete x[2];
System.out.println(x);    // 2, 3

x = [ 2..4 ];

delete x[10];
System.out.println(x);    // 2, 3, 4

insert 1 as first into x;
System.out.println(x);    // 1, 2, 3, 4

x = [ 1..5 ];
System.out.println(x);    // 1, 2, 3, 4, 5

x = [ 1..5, 10 ];
System.out.println(x);    // 1, 2, 3, 4, 5, 10

var z = x[n|n > 3];
System.out.println(x);    // 4, 5, 10

z = x[n| indexof n < 3];
System.out.println(z);    // 1, 2, 3

var y = select n*n from n in x;
                          // 1, 4, 9, 16, 25
System.out.println(y);

y = foreach n in x { n + 1 };
                          // 2, 3, 4, 5, 6
System.out.println(y);

