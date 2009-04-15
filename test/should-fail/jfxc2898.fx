/*
 * Regression test: JFXC-2898: Same variable name in nested for loops internal compiler error
 *
 * @test/compile-error
 */

function f1() {
var x;
{var x;}
}

function f2() {
{var x;}
var x;
}

function f3() {
var x;
var x;
}

function f4(x) {
var x;
}

function f5(x) {
{var x;}
}

function f6() {
for (x in [0..1]) {
  for (x in [0..1]) {}
}
}

function f7(x) {
for (x in [0..1]) {}
}

function f8() {
var x;
{var x;}
true;
}

function f9() {
{var x;}
var x;
true;
}

function f10() {
var x;
var x;
true;
}

function f11(x) {
var x;
true;
}

function f12(x) {
{var x;}
true;
}

function f13() {
for (x in [0..1]) {
  for (x in [0..1]) {}
}
}

function f14(x) {
for (x in [0..1]) {}
true;
}

var x;
var x;
