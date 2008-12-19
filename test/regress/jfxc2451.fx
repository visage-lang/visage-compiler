/**
 * Regression test JFXC-2451 : assignops fall over on new numerics
 *
 * @test
 * @run
 */

public var by : Byte = 0;
public var sh : Short = 1;
public var ii : Integer = 2;
public var lo : Long = 3;
public var fl : Float = 4;
public var db : Double = 5;

function run() {
  by = 0;
  sh = 1;
  ii = 2;
  lo = 3;
  fl = 4;
  db = 5;

  by += 1;
  sh += 1;
  ii += 1;
  lo += 1;
  fl += 1;
  db += 1;

  println("{by} {sh} {ii} {lo} {fl} {db}");

  by *= 2;
  sh *= 2;
  ii *= 2;
  lo *= 2;
  fl *= 2;
  db *= 2;

  println("{by} {sh} {ii} {lo} {fl} {db}");
} 

