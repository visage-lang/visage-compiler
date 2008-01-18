/*
 * Feature test #22 - time literals
 *
 * @test
 * @run
 */

import java.lang.System;
import javafx.lang.Time;

var t = Time {millis: 50};
System.out.println("Time object literal, millis=50 = {t}");
t = 500ms;
System.out.println("500ms = {t}");
t = 5s;
System.out.println("5s = {t}");
t = 1.5s;
System.out.println("1.5s = {t}");
t = 2m;
System.out.println("2m = {t}");
t = 1h;
System.out.println("1h = {t}");
System.out.println("t.toMinutes() = {t.toMinutes()}");
System.out.println("2m.toSeconds() = {2m.toSeconds()}");
