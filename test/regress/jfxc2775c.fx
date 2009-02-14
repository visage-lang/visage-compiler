/* JFXC-2775 : Collapse bound member select accesses into the single per-scipt BindingExpression
 *
 * @test
 * @run
 */

class Foo {
  var q = ["Gezz"];
}

function run() {
  var p : Foo;
  var bq = bind p.q;
  println(bq);
  p = Foo{};
  println(bq);
  insert "Jaw" into p.q;
  println(bq);
  p.q = "Nah";
  println(bq);
}
