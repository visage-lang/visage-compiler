/*
 * Check for passing null to array type - don't convert to sequence.
 * (Note that the test program in JFXC-2348 is buggy in itself;
 * see JFXC-2355.)
 * @test
 */
import java.awt.Color;
var c : Color = null;
c.getRGBComponents(null)
