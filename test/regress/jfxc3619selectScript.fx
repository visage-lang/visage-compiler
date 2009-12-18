/**
 * Regression test: JFXC-3619 : Compiled bind: dependent state/mode -- non-bound initalizers of bound object literal
 *
 * Select reference in bound object literal from non-bound initializer (from script-level)
 *
 * @test
 * @run
 */

class A {
  var a : Integer
}


  var ref = A {a: 10 };
  def obla = bind A { a: ref.a }

    println(obla.a);
    var hash1 = java.lang.System.identityHashCode(obla);
    --ref.a;
    if (hash1 == java.lang.System.identityHashCode(obla)) println("Error: No new object for --ref.a");
    println(obla.a);
    --ref.a;
    println(obla.a);
    --ref.a;
    println(obla.a);

    hash1 = java.lang.System.identityHashCode(obla);
    ref.a = ref.a;
    if (hash1 != java.lang.System.identityHashCode(obla)) println("Error: New object for ref.a = ref.a");
    println(obla.a);

    hash1 = java.lang.System.identityHashCode(obla);
    ref = ref;
    if (hash1 != java.lang.System.identityHashCode(obla)) println("Error: New object for ref = ref");
    println(obla.a);

    hash1 = java.lang.System.identityHashCode(obla);
    ref = A {a: 99 };
    if (hash1 == java.lang.System.identityHashCode(obla)) println("Error: No new object for ref = A\{a: 99\}");
    println(obla.a);


    ref = A{};
    hash1 = java.lang.System.identityHashCode(obla);
    ref = A{};
    if (hash1 != java.lang.System.identityHashCode(obla)) println("Error: New object for ref = A\{\}");
    println(obla.a);

    ref = A{a: 22};
    hash1 = java.lang.System.identityHashCode(obla);
    ref = null;
    if (hash1 == java.lang.System.identityHashCode(obla)) println("Error: No new object for ref = null");
    println(obla.a);

