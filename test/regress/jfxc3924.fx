/*
 * Regression test
 * JFXC-3924 : Unexpected type assertion in JavafxTreeMaker.java
 *
 * @test
 */

if (true)  [] else null;
if (true)  null else [];
if (true)  [null] else [];
if (true)  [] else [null];
if (true)  [null] else null;
if (true)  null else [null];
