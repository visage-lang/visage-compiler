/**
 * regression test: JFXC-1404 : Bound function returns null instead of empty string, if Exception is thrown.
 *
 * @test
 * @run/ignore-std-error
 */

import java.lang.System;
import com.sun.javafx.runtime.util.StringLocalization;

bound function foo(): String {
    StringLocalization.getLocalizedString(null, "hello.world", "hello.world");
}

var a = bind foo();
System.out.println(a);
