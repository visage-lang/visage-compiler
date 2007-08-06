import java.lang.System;
import java.lang.Double;

class Foo {
 attribute a : Integer ;
 attribute b : Integer ;
 function bleep() : Integer;
 operation gnaw() : Integer;
}

attribute Foo.a = blah();

function Foo.bleep() : Integer {
  return 5;
}

var aaa = 7;

aaa = blah() + aaa;

System.out.println(bleep() + aaa);

operation Foo.gnaw() : Integer {
  return 9;
}

function bleep() : Double {
  return 13.2;
}

operation blah() : Integer {
  return 4;
}


