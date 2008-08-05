/**
 * Regression test for JFXC-494 : Script importing
 *
 * @compilefirst sub494/jfxc494sub.fx
 * @test
 * @run
 */

import java.lang.System;
import sub494.jfxc494sub.scriptDef;
import sub494.jfxc494sub.scriptClass;

System.out.println(scriptDef);
var x = new scriptClass;
System.out.println(x.thing);

