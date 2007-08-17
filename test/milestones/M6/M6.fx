import java.lang.System;

var x = [1, 2, 3];
var y = 1;
var z = bind x[y];

System.out.println(z);        // 2

System.out.println(x[y]);     // 3

x[1] = 5;
System.out.println(x[y]);     // 5

y = 0;
System.out.println(z);        // 1

delete x[0];
System.out.println(z);        // 2

insert 99 as first into x;
System.out.println(z);        // 99

x[y] = 10;
System.out.println(z);        // 10

// bidirectional binding
// z = 3;
