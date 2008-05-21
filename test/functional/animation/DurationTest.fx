/**
  * Checks Duration literals.
  * @test
  * @run
  */


import javafx.animation.*;
import java.lang.*;
import java.util.concurrent.TimeUnit;
import javafx.lang.Duration; 

var t1: Duration = 1m + 12s;
var t2: Duration = 1.2m;
var t3: Duration = 72000ms;

if(t1 == t2) {
    System.out.println("test1 - pass");
} else {
    System.out.println("test1 - fail");
}

if(t2 == t3) {
    System.out.println("test2 - pass");
} else {
    System.out.println("test2 - fail");
}

if(t1 == t3) {
    System.out.println("test3 - pass");
} else {
    System.out.println("test3 - fail");
}
