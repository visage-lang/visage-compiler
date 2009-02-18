/*
 * jfxc-851 - Compilation error when using for loop to iterate thru java array
 *
 * @test/fail
 * was: @test
 */

import java.awt.Frame;

for (f in Frame.getFrames()) {}