/*
 * Verify that the __DIR__ and __FILE__ module-level pseudo-variables work.
 * @test
 * @run
 */
import java.lang.System;
import java.net.URL;

var dirURL = __DIR__.toString();
var fileURL = __FILE__.toString();
var ok = dirURL.endsWith("/test/regress/") and
         fileURL.endsWith("/test/regress/jfxc694.class");
System.out.println(if (ok) "PASS!" else "FAIL!");
