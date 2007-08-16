import java.lang.System;

System.out.println(bleep());


class Foo {
  attribute a : Integer  = blah();
  attribute b : Integer ;
  function bleep() : Integer {
      return 5;
  }
  function gnaw() : Integer{
      return 9;
  }
}


var aaa = 7;

aaa = 2 + aaa; //blah() + aaa;

System.out.println(bleep() + aaa);

function bleep() : Number {
  return 13.2;
}

function blah() : Integer {
  return 4;
}


