class Base {
  attribute n : Integer;

  function foo(a : Integer) : Integer { a+n+1 }
  function moo(a : Integer) : Integer { a+n+2 }
  function bar(a : Integer) : Integer { a+n+3 }
}

class OtherBase {
  function bork(a : Integer) : Integer { a+4 }
}

class Dispatch extends Base, OtherBase {
  // override foo
  function foo(a : Integer) : Integer { a+n+5 }
}

