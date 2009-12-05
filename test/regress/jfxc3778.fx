/**
 * JFXC-3778 : compiled-bind: new accessibility error since last work on bind object literals
 *
 * @compile jfxc3778sub/jfxc3778sub.fx
 * @test
 */

import jfxc3778sub.*;

var x = bind jfxc3778sub { x:1 }
