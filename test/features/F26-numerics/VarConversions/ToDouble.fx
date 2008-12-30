/*
 * Assigning compatible values to a variable of the type Double.
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

var d : Double;

public class ToDouble extends FXTestCase {

    function testToDouble() {
        d = bb;
        assertEquals127();
        d = 127 as Byte;
        assertEquals127();

        d = cc;
        assertEquals(65535.0 as Double, d, 0);
        d = 127 as Character;
        assertEquals127();

        d = ii;
        assertEquals(2147483647.0 as Double, d, 0);
        d = 127 as Integer;
        assertEquals127();

        d = ll;
        assertEquals(9223372036854775807.0 as Double, d, 0);
        d = 127 as Long;
        assertEquals127();

        d = ss;
        assertEquals(32767.0 as Double, d, 0);
        d = 127 as Short;
        assertEquals127();

        d = ff;
        assertEquals(3.4028234663852886E38, d, 0);
        d = 3.4028234663852886E38 as Float;
        assertEquals(3.4028234663852886E38, d, 0);

        d = dd;
        assertEquals(1.7976931348623157E308, d, 0);
        d = 1.7976931348623157E308 as Double;
        assertEquals(1.7976931348623157E308, d, 0);
        d = 1.7976931348623157E308;
        assertEquals(1.7976931348623157E308, d, 0);

        d = nn;
        assertEquals(3.1415926535 as Double, d, 0);
        d = 127.0 as Number;
        assertEquals127();

        /* Each of the following lines produces a compile-time error
        d = dduu;
        d = bboo;
        d = sstt;
        d = jj;
        d = nul;
        d = iSeq;
        d = fSeq;
        */

        d = java.lang.Double.NaN;
        assertTrue(java.lang.Double.isNaN(d));

        var seq1 = [ 125..200 ];
        d = seq1[2];
        assertEquals127();

        var seq2 = [ 121.11, 1750.35, 127.0, 1434.9 ];
        d = seq2[2];
        assertEquals127();
    }

    function assertEquals127() {
        assertEquals(127.0 as Double, d, 0);
    }
}