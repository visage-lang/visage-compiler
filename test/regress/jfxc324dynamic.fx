/*
 * Regression test: dynamic dependencies on x.y
 *
 * @test
 * @run
 */

import java.lang.System;

class Node {
  attribute bounds : Integer;
}

class Foo {
   attribute focusBounds: Integer = bind focusedNode.bounds;
   attribute focusedNode : Node; 
}

var n1 = Node { bounds: 9 }
var n2 = Node { bounds: 1 }
var f = Foo {focusedNode: n1 }

System.out.println(f.focusBounds);
n1.bounds = 99;
System.out.println(f.focusBounds);
n1.bounds = 999;
System.out.println(f.focusBounds);
f.focusedNode = n2;
System.out.println(f.focusBounds);
n2.bounds = 100;
System.out.println(f.focusBounds);
