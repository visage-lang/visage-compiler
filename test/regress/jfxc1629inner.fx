/*
 * Regression test JFXC-1629 : Enforce public-readable modifier
 * Inner access case.
 *
 * @test
 */

class Foo {}

class reada {

    public-readable var running = false;

    private function createAdapter():Foo {
        Foo {
            function begin() : Void {
                running = true;
            }
        }
    }
}
