/*
 * Assigning compatible values to a variable of the type Long.
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

var l : Long;

public class ToLong extends FXTestCase {

    function testToLong() {
        l = bb;
        assertEquals127();
        l = 127 as Byte;
        assertEquals127();

        l = cc;
        assertEquals(65535 as Long, l);
        l = 65535 as Character;
        assertEquals(65535 as Long, l);

        l = ii;
        assertEquals(2147483647 as Long, l);
        l = 2147483647 as Integer;
        assertEquals(2147483647 as Long, l);

        l = ll;

        // Uncomment these lines when JFXC-2576 is fixed
//        assertEquals(9223372036854775807 as Long, l);
//        l = 9223372036854775807 as Long;
//        assertEquals(9223372036854775807 as Long, l);
        l = 9223372036854775807;
//        assertEquals(9223372036854775807 as Long, l);

        l = ss;
        assertEquals(32767 as Long, l);
        l = 32767 as Short;
        assertEquals(32767 as Long, l);

        l = ff;
        l = java.lang.Float.NaN;
        l = 127.0 as Float;
        assertEquals127();
        l = 127.45 as Float;
        assertEquals127();

        l = dd;
        l = java.lang.Double.NaN;
        l = 127.0 as Double;
        assertEquals127();
        l = 127.45 as Double;
        assertEquals127();

        l = nn;
        l = 127.0 as Number;
        assertEquals127();

        var seq1 = [ 125..3240 ];
        l = seq1[2];
        assertEquals127();

        /* Uncomment this block when JFXC-2582 is fixed
        var seq2 = [ 121.11, 1750.35, 127.0, 1434.9 ];
        l = seq2[2];
        assertEquals127();
        */
    }

    function assertEquals127() {
        assertEquals(127 as Long, l);
    }
}