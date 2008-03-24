/*
 * Regression test: bound sequence, esp. ranges and slices
 *
 * @test
 * @run
 */

import java.lang.System;

class jfxc505boundSeq {
  attribute a = 3;
  attribute b = 8;
  attribute s = 2;
  attribute qi = bind [a..b step s];
  attribute qe = bind [a..<b step s];

  attribute x = 0;
  attribute y = 2;
  attribute qisi = bind qi[x..y];
  attribute qise = bind qi[x..<y];
  attribute qisio = bind qi[x..];
  attribute qiseo = bind qi[x..<];

  function check(t : Integer[], expect : Integer[]) : String {
    if (t == expect)
      "pass"
    else
      "FAIL - expected: {expect} got: {t}"
  }

  function show() : Void {
    System.out.println("[{a}..{b} step {s}] == {qi}  {check(qi, [a..b step s])}");
    System.out.println("    [{x}..{y}] == {qisi}  {check(qisi, qi[x..y])}");
    System.out.println("    [{x}..<{y}] == {qise}  {check(qise, qi[x..<y])}");
    System.out.println("    [{x}..] == {qisio}  {check(qisio, qi[x..])}");
    System.out.println("    [{x}..<] == {qiseo}  {check(qiseo, qi[x..<])}");
    System.out.println("[{a}..<{b} step {s}] == {qe}  {check(qe, [a..<b step s])}");
  }

  function test() : Void {
    a = 5;
    show();
    a = 3;
    show();
    b = 9;
    show();
    s = 3;
    show();
    x = 1;
    show();
    y = 1;
    show();
    y = 99;
    show();
  }
}

jfxc505boundSeq{}.test()


