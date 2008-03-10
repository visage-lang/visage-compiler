/**
 * Regression test JFXC-837 : funcion/method call within bind
 * @test
 */
import java.lang.System; 

var enableBindingOverhaul;

var x = bind 1234 .toString();
System.out.println(x);
