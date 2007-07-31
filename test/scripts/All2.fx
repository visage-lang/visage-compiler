/*
 * Coverage. continuing with primaryExpression
 */
import java.util.Date;
import java.lang.System;

var a = new Date;
var b = new Date(56, 10, 9, 23, 0);

class Alpha {
  operation myop(x);
}
  operation Alpha.myop(x) {
    System.out.println("Alpha={this} Value={x}");
  }

class Beta extends Alpha {
  operation myop(x);
}
  operation Beta.myop(x) {
    System.out.println("Beta={this} Value={(x)}");
    super.myop(x);
  }

var c = Beta {};
c.myop(55);
