var x = [1,2,3];
var y = 1;
var z = bind x[y];
println(z); // prints 2
z = 3;
println(x[y]); // prints 3
y = 0;
println(z); // prints 1
delete x[0];
println(z); // prints 3
insert 99 as first into x;
println(z); //prints 99
x[y] = 10;
println(z); // prints 10 
