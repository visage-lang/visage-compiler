/**
 * Regression test JFXC-1241 : Allow iterating over java arrays, collections, or other Iterables
 *
 * @test
 * @run
 */

import java.util.ArrayList;

var list = new ArrayList;
list.add("Stones");
list.add("from");
list.add("the");
list.add("River");
var book = for (w in list) "_{w}_";
var arr = list.toArray();
for (v in arr) {
  println(v);
}
for (v in book) {
  println(v);
}
