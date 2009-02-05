/*
 * Verify that the __PROFILE, __DIR__ and __FILE__ module-level pseudo-variables work.
 * @test
 * @run
 */
import java.lang.System;
import java.net.URL;

var dir = __DIR__;
var file = __FILE__;
var source = __SOURCE_FILE__;
var profile = __PROFILE__;

var ok = dir.endsWith("/test/regress/") and
         file.endsWith("/test/regress/jfxc694.class") and
         source.endsWith("/test/regress/jfxc694.fx") and
	 profile.equals("desktop");
System.out.println(if (ok) "PASS!" else "FAIL!");
