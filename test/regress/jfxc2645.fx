/**
 * JFXC-2645 : package name = file name causes javadump
 *
 * @compilefirst sub2645/sub2645.fx
 * @test
 * @run
 */

import sub2645.sub2645;

sub2645.w = 4.4;
println(sub2645.r.width);
