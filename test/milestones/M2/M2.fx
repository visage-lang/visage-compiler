// M2.fx -- simple variable binding
// Demonstrates: unidirectional binding of local variables to expressions
//               expressions are completely reevaluated when dependencies change
//               dependencies are limited to variables named in the expression

import java.lang.*;
import java.util.Date;

var x = 17;
var y = 2;

var z = bind x + y;
var max = bind Math.max(x, y);

System.out.println("{x} + {y} == {z}");
System.out.println("{x} max {y} == {max}");

x = 4;
System.out.println("{x} + {y} == {z}");
System.out.println("{x} max {y} == {max}");

y = 100;
System.out.println("{x} + {y} == {z}");
System.out.println("{x} max {y} == {max}");

x = 50;
System.out.println("{x} + {y} == {z}");
System.out.println("{x} max {y} == {max}");

var now = new Date();
var day = now.getDay();

var date = bind new Date(now.getYear(), now.getMonth(), day);
System.out.println("now: {date}");
day = day + 1;
System.out.println("Tomorrow: {date}");
