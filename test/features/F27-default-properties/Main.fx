/* Feature test #27 -- default properties
 * Demonstrates use of default properties to simplify object literal syntax
 *
 * @test
 * @run
 */

class WithDefault {
    public default var contents:String[];
}

def explicit = WithDefault {
    contents: ["a", "b", "c"]
    override function toString() {contents.toString()}
}

println(explicit);
