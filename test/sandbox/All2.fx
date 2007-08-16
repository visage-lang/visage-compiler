import java.util.Date;
import java.lang.System;

var a = new Date;
var b = new Date(56, 9, 9, 23, 0);
System.out.println(b);

class Alpha {
  function myop(x : Integer) {
    System.out.println("Alpha={this} Value={x}");
     if x > 7 then "blither" else "be";
  }
}

class Beta extends Alpha {
  function myop(x : Integer)  {
    System.out.println("Beta={this} Value={(x)}");
    System.out.println(super.myop(x));
    if x > 7 then "blather" else {var str="bop"; str}
  }
}

var c = Beta {};
System.out.println(c.myop(3));
System.out.println(c.myop(55));
