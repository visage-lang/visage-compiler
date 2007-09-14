import java.lang.*;

class Moo {
  public attribute a : Integer;
  public attribute b : Integer;
}

var m : Moo = Moo { };
var v : Integer = m.a;
var b : Integer = bind m.a;
m.b = 3;
