/*
 * Feature test #22 - time literals
 *
 * @test
 * @run
 */

import java.lang.System;
import javafx.lang.Time;

var t = Time {millis: 50};
System.out.println("Time object literal, millis=50 = {t.toString()}");
t = 500ms;
System.out.println("500ms = {t.toString()}");
t = 5s;
System.out.println("5s = {t.toString()}");
t = 1.5s;
System.out.println("1.5s = {t.toString()}");
t = 2m;
System.out.println("2m = {t.toString()}");
t = 1h;
System.out.println("1h = {t.toString()}");
System.out.println("t.toMinutes() = {t.toMinutes()}");
System.out.println("2m.toSeconds() = {2m.toSeconds()}");
