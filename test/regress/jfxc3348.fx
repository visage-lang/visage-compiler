/**
 * JFXC-3348 : An exception has occurred in the OpenJavafx compiler: java.lang.AssertionError: isSubtype 12
 *
 * @test
 * @run
 */

import java.util.HashMap;
var items = for(o in new HashMap().keySet().toArray) {1};
println(items);

