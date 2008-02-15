/**
 * Sub test case intended to be used with jfxc697.fx
 *
 * @subtest
 */
package foo.bar;
import java.lang.*;

public class FXPropTest {
    public function print(): Void {
        System.out.println(##"source original string");

        var test2 = new FXPropTest2();
        test2.print();
    }
}

public class FXPropTest2 {
    public function print(): Void {
        System.out.println(##"source original string in test 2");
    }
}
