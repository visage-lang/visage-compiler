/*
 * Test automatic conversion of primitive datatypes
 * See also jfxc915b.fx.
 *
 * @test
 * @test/compile-error
 */

import java.lang.*;

var i: Integer = Long.parseLong("100000001", 16);
