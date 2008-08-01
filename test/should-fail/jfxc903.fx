/*
 * Should complain that:
 * "non-static method bar() cannot be referenced from a static context"
 *
 * @test/fail
 */

var notbug = jfxc903{ value:123 }
var bug = jfxc903{ value:bar() }
class jfxc903 {
    var value;
    function bar() { 123 }
}
