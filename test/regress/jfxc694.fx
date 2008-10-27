/*
 * Verify that the __DIR__ and __FILE__ module-level pseudo-variables work.
 * @test
 * @run
 */
import java.lang.System;
import java.net.URL;

var dir = __DIR__;
var file = __FILE__;
var ok = dir.endsWith("/test/regress/") and
         file.endsWith("/test/regress/jfxc694.class");
System.out.println(if (ok) "PASS!" else "FAIL!");
