/*
 * Verify that Duration instance has a non-null default within Strings.
 *
 * @test
 * @run
 */

var d: javafx.lang.Duration;
var msg = "{d}"; // throws NPE here 
