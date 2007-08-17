import java.lang.System;

x = [1, 2, 3];
System.out.println(x);    // 1, 2, 3

System.out.println(x[1]); // 2

System.out.println(x.size);
                          // 3

insert 4 into x;
System.out.println(x);    // 1, 2, 3, 4

delete 1 from x;
System.out.println(x);    // 2, 3, 4

delete x[10];
System.out.println(x);    // 2, 3, 4

insert 1 as first into x;
System.out.println(x);    // 1, 2, 3, 4

x = [ 1..5 ];
System.out.println(x);    // 1, 2, 3, 4, 5

var y = select n*n from n in x;
                          // 1, 4, 9, 16, 25

x = [ 1..5, 10 ];
System.out.println(x);    // 1, 2, 3, 4, 5, 10

var z = x[n|n > 3];
System.out.println(x);    // 4, 5, 10

z = x[n| indexof n < 3];
System.out.println(z);    // 1, 2, 3
