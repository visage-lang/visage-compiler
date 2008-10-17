/**
 * Regression test JFXC-2200 : varargs constructor args not handled -- Compiler crash java.lang.ProcessBuilder constructor
 *
 * @test
 * @run
 */

import java.lang.ProcessBuilder;

var pb:ProcessBuilder = new ProcessBuilder("javafx", "print");
var cmd = pb.command();
println(cmd.get(0));
println(cmd.get(1));

pb = new ProcessBuilder("javafx");
cmd = pb.command();
println(cmd.get(0));
