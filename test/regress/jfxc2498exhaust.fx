/**
 * JFXC-2498 : Duration arithmetic bugs
 *
 * @test
 * @run
 */

var dur1 : Duration = 20ms;;
var dur2 : Duration = 100ms;
var f : Number = 2.0;
var i : Integer = 4;
var d : Double = 10.0;

def plus = bind dur1 + dur2;
def minus = bind dur1 - dur2;

def fMulB = bind f * dur1;
def fMulA = bind dur1 * f;
def iMulB = bind i * dur1;
def iMulA = bind dur1 * i;
def dMulB = bind d * dur1;
def dMulA = bind dur1 * d;

def durDiv = bind dur1 / dur2;

def fDiv = bind dur2 / f;
def iDiv = bind dur2 / i;
def dDiv = bind dur2 / d;

def lt = bind dur1 < dur2;
def gt = bind dur1 > dur2;

def eq = bind dur1 == dur2;
def ne = bind dur1 != dur2;

function show() {
  println("dur1 = {dur1}. dur2 = {dur2}, f = {f}, d = {d}, i = {i}");
  println("{dur1} + {dur2} = {plus}");
  println("{dur1} - {dur2} = {minus}");
  println("{f} * {dur1} = {fMulB}");
  println("{dur1} * {f} = {fMulA}");
  println("{d} * {dur1} = {dMulB}");
  println("{dur1} * {d} = {dMulA}");
  println("{i} * {dur1} = {iMulB}");
  println("{dur1} * {i} = {iMulA}");
  println("{dur1} / {dur2} = {durDiv}");
  println("{dur2} / {f} = {fDiv}");
  println("{dur2} / {d} = {dDiv}");
  println("{dur2} / {i} = {iDiv}");
  println("{dur1} < {dur2} = {lt}");
  println("{dur1} > {dur2} = {gt}");
  println("{dur1} == {dur2} = {eq}");
  println("{dur1} != {dur2} = {ne}");
}

show();

dur1 = 2s;
dur2 = 5s;
f = 0.25;
d = 0.1;
i = 50;

show();
