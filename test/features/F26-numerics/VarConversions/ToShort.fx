/*
 * Assigning compatible values to a variable of the type Short.
 * The code is expected to compile and run successfully. Warnings
 * are not checked.
 *
 * @test/fxunit
 * @run
 */


import java.util.Date;
import javafx.fxunit.FXTestCase;

/* --------------------------------------------------------------
 *            This block could live in a common fx file
 *            if (at)compilefirst worked with (at)test/fxunit
 * -------------------------------------------------------------- */
public def bb : Byte = 127;
public def cc : Character = 65535;
public def ii : Integer = 2147483647;
public def ll : Long = 9223372036854775807;
public def ss : Short = 32767;
public def ff : Float = 3.4028234663852886E38;
public def dd : Double = 1.7976931348623157E308;
public def nn : Number = 3.1415926535;
public def dduu : Duration = 600s;
public def bboo : Boolean = true;
public def sstt : String = "Hello, Java FX!";
public def jj : Date = new Date();
public def nul = null;
public def iSeq : Integer[] = [ 1, 2, 3 ];
public def fSeq : Float[] = [ 1.11, 2.22, 3.33 ];
/* ------------------------------------------------------------ */

var s : Character;

public class ToShort extends FXTestCase {

    function testToShort() {
        s = bb;
        assertEquals(127 as Short, s as Short);
        s = 127 as Byte;
        assertEquals(127 as Short, s as Short);

        s = cc;
        s = 32767 as Character;
        assertEquals32767();

        s = ii;
        s = 32767 as Integer;
        assertEquals32767();

        s = ll;
        s = 32767 as Long;
        assertEquals32767();

        s = ss;
        assertEquals32767();
        s = 32767 as Short;
        assertEquals32767();
        s = 32767;
        assertEquals32767();

        s = ff;
        s = java.lang.Float.NaN;
        s = 32767.0 as Float;
        assertEquals32767();
        s = 32767.45 as Float;
        assertEquals32767();

        s = dd;
        s = java.lang.Double.NaN;
        s = 32767.0 as Double;
        assertEquals32767();
        s = 32767.45 as Double;
        assertEquals32767();

        s = nn;
        s = 32767.0 as Number;
        assertEquals32767();

        /* Each of the following lines produces a compile-time error
        s = dduu;
        s = bboo;
        s = sstt;
        s = jj;
        s = nul;
        s = iSeq;
        s = fSeq;
        */

        /* Uncomment this block when JFXC-2582 is fixed
        var seq1 = [ 32765..33000 ];
        s = seq1[2];
        assertEquals32767();

        var seq2 = [ 121.11, 1750.35, 32767.0, 1434.9 ];
        s = seq2[2];
        assertEquals32767();
        */
    }

    function assertEquals32767() {
        assertEquals(32767 as Short, s as Short);
    }

}