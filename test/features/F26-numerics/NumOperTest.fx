/*
 * Test sequences casting
 *
 * @test/fxunit
 * @
 */

import javafx.fxunit.FXTestCase;
 
public class NumOperTest extends FXTestCase {

    var vByte : Byte = 120 as Byte ;
    var vShort : Short = 30000 as Short ;
    var vCharacter : Character = 64 as Character ;
    var vInteger : Integer = 1000000 as Integer ;
    var vLong : Long = 1000000000 as Long ;
    var vFloat : Float = 10.5 as Float ;
    var vDouble : Double = 1.25e4 as Double ;
    var vNumber : Number = 100 as Number ;

     function foo(x: Byte): String {"Byte"}
     function foo(x: Short): String {"Short"}
     function foo(x: Character): String {"Character"}
     function foo(x: Integer): String {"Integer"}
     function foo(x: Long): String {"Long"}
     function foo(x: Float): String {"Float"}
     function foo(x: Double): String {"Double"}
    // function foo(x: Number): String {"Number"}

    var res=this; 
    var gen: Boolean = false;

    var xByte: Byte[] = vByte;  
    function testOperByteWithByte() { 
       var radd: Number = vByte + vByte ;
       if (gen)javafx.lang.FX.print("\nvar resaddByteWithByte: Number={radd};");       
       assertEquals("{res.resaddByteWithByte}", "{radd}");

       var fadd: String = foo(vByte + vByte) ;
       if (gen)javafx.lang.FX.print("\nvar resaddByteWithByte_f: String=\"{fadd}\";");       
       assertEquals("{res.resaddByteWithByte_f}", "{fadd}");
       var rsub: Number = vByte - vByte ;
       if (gen)javafx.lang.FX.print("\nvar ressubByteWithByte: Number={rsub};");       
       assertEquals("{res.ressubByteWithByte}", "{rsub}");

       var fsub: String = foo(vByte - vByte) ;
       if (gen)javafx.lang.FX.print("\nvar ressubByteWithByte_f: String=\"{fsub}\";");       
       assertEquals("{res.ressubByteWithByte_f}", "{fsub}");
       var rmul: Number = vByte * vByte ;
       if (gen)javafx.lang.FX.print("\nvar resmulByteWithByte: Number={rmul};");       
       assertEquals("{res.resmulByteWithByte}", "{rmul}");

       var fmul: String = foo(vByte * vByte) ;
       if (gen)javafx.lang.FX.print("\nvar resmulByteWithByte_f: String=\"{fmul}\";");       
       assertEquals("{res.resmulByteWithByte_f}", "{fmul}");
       var rdiv: Number = vByte / vByte ;
       if (gen)javafx.lang.FX.print("\nvar resdivByteWithByte: Number={rdiv};");       
       assertEquals("{res.resdivByteWithByte}", "{rdiv}");

       var fdiv: String = foo(vByte / vByte) ;
       if (gen)javafx.lang.FX.print("\nvar resdivByteWithByte_f: String=\"{fdiv}\";");       
       assertEquals("{res.resdivByteWithByte_f}", "{fdiv}");
       var rmod: Number = vByte mod vByte ;
       if (gen)javafx.lang.FX.print("\nvar resmodByteWithByte: Number={rmod};");       
       assertEquals("{res.resmodByteWithByte}", "{rmod}");

       var fmod: String = foo(vByte mod vByte) ;
       if (gen)javafx.lang.FX.print("\nvar resmodByteWithByte_f: String=\"{fmod}\";");       
       assertEquals("{res.resmodByteWithByte_f}", "{fmod}");
       var req: Boolean = vByte == vByte ;
       if (gen)javafx.lang.FX.print("\nvar reseqByteWithByte: Boolean={req};");       
       assertEquals("{res.reseqByteWithByte}", "{req}");

       var rneq: Boolean = vByte != vByte ;
       if (gen)javafx.lang.FX.print("\nvar resneqByteWithByte: Boolean={rneq};");       
       assertEquals("{res.resneqByteWithByte}", "{rneq}");

       var rlt: Boolean = vByte < vByte ;
       if (gen)javafx.lang.FX.print("\nvar resltByteWithByte: Boolean={rlt};");       
       assertEquals("{res.resltByteWithByte}", "{rlt}");

       var rlte: Boolean = vByte <= vByte ;
       if (gen)javafx.lang.FX.print("\nvar reslteByteWithByte: Boolean={rlte};");       
       assertEquals("{res.reslteByteWithByte}", "{rlte}");

       var rgt: Boolean = vByte > vByte ;
       if (gen)javafx.lang.FX.print("\nvar resgtByteWithByte: Boolean={rgt};");       
       assertEquals("{res.resgtByteWithByte}", "{rgt}");

       var rgte: Boolean = vByte >= vByte ;
       if (gen)javafx.lang.FX.print("\nvar resgteByteWithByte: Boolean={rgte};");       
       assertEquals("{res.resgteByteWithByte}", "{rgte}");

       if (gen)javafx.lang.FX.print("\n//");
}
    function testOperByteWithShort() { 
       var radd: Number = vByte + vShort ;
       if (gen)javafx.lang.FX.print("\nvar resaddByteWithShort: Number={radd};");       
       assertEquals("{res.resaddByteWithShort}", "{radd}");

       var fadd: String = foo(vByte + vShort) ;
       if (gen)javafx.lang.FX.print("\nvar resaddByteWithShort_f: String=\"{fadd}\";");       
       assertEquals("{res.resaddByteWithShort_f}", "{fadd}");
       var rsub: Number = vByte - vShort ;
       if (gen)javafx.lang.FX.print("\nvar ressubByteWithShort: Number={rsub};");       
       assertEquals("{res.ressubByteWithShort}", "{rsub}");

       var fsub: String = foo(vByte - vShort) ;
       if (gen)javafx.lang.FX.print("\nvar ressubByteWithShort_f: String=\"{fsub}\";");       
       assertEquals("{res.ressubByteWithShort_f}", "{fsub}");
       var rmul: Number = vByte * vShort ;
       if (gen)javafx.lang.FX.print("\nvar resmulByteWithShort: Number={rmul};");       
       assertEquals("{res.resmulByteWithShort}", "{rmul}");

       var fmul: String = foo(vByte * vShort) ;
       if (gen)javafx.lang.FX.print("\nvar resmulByteWithShort_f: String=\"{fmul}\";");       
       assertEquals("{res.resmulByteWithShort_f}", "{fmul}");
       var rdiv: Number = vByte / vShort ;
       if (gen)javafx.lang.FX.print("\nvar resdivByteWithShort: Number={rdiv};");       
       assertEquals("{res.resdivByteWithShort}", "{rdiv}");

       var fdiv: String = foo(vByte / vShort) ;
       if (gen)javafx.lang.FX.print("\nvar resdivByteWithShort_f: String=\"{fdiv}\";");       
       assertEquals("{res.resdivByteWithShort_f}", "{fdiv}");
       var rmod: Number = vByte mod vShort ;
       if (gen)javafx.lang.FX.print("\nvar resmodByteWithShort: Number={rmod};");       
       assertEquals("{res.resmodByteWithShort}", "{rmod}");

       var fmod: String = foo(vByte mod vShort) ;
       if (gen)javafx.lang.FX.print("\nvar resmodByteWithShort_f: String=\"{fmod}\";");       
       assertEquals("{res.resmodByteWithShort_f}", "{fmod}");
       var req: Boolean = vByte == vShort ;
       if (gen)javafx.lang.FX.print("\nvar reseqByteWithShort: Boolean={req};");       
       assertEquals("{res.reseqByteWithShort}", "{req}");

       var rneq: Boolean = vByte != vShort ;
       if (gen)javafx.lang.FX.print("\nvar resneqByteWithShort: Boolean={rneq};");       
       assertEquals("{res.resneqByteWithShort}", "{rneq}");

       var rlt: Boolean = vByte < vShort ;
       if (gen)javafx.lang.FX.print("\nvar resltByteWithShort: Boolean={rlt};");       
       assertEquals("{res.resltByteWithShort}", "{rlt}");

       var rlte: Boolean = vByte <= vShort ;
       if (gen)javafx.lang.FX.print("\nvar reslteByteWithShort: Boolean={rlte};");       
       assertEquals("{res.reslteByteWithShort}", "{rlte}");

       var rgt: Boolean = vByte > vShort ;
       if (gen)javafx.lang.FX.print("\nvar resgtByteWithShort: Boolean={rgt};");       
       assertEquals("{res.resgtByteWithShort}", "{rgt}");

       var rgte: Boolean = vByte >= vShort ;
       if (gen)javafx.lang.FX.print("\nvar resgteByteWithShort: Boolean={rgte};");       
       assertEquals("{res.resgteByteWithShort}", "{rgte}");

       if (gen)javafx.lang.FX.print("\n//");
}
    function testOperByteWithCharacter() { 
       var radd: Number = vByte + vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar resaddByteWithCharacter: Number={radd};");       
       assertEquals("{res.resaddByteWithCharacter}", "{radd}");

       var fadd: String = foo(vByte + vCharacter) ;
       if (gen)javafx.lang.FX.print("\nvar resaddByteWithCharacter_f: String=\"{fadd}\";");       
       assertEquals("{res.resaddByteWithCharacter_f}", "{fadd}");
       var rsub: Number = vByte - vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar ressubByteWithCharacter: Number={rsub};");       
       assertEquals("{res.ressubByteWithCharacter}", "{rsub}");

       var fsub: String = foo(vByte - vCharacter) ;
       if (gen)javafx.lang.FX.print("\nvar ressubByteWithCharacter_f: String=\"{fsub}\";");       
       assertEquals("{res.ressubByteWithCharacter_f}", "{fsub}");
       var rmul: Number = vByte * vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar resmulByteWithCharacter: Number={rmul};");       
       assertEquals("{res.resmulByteWithCharacter}", "{rmul}");

       var fmul: String = foo(vByte * vCharacter) ;
       if (gen)javafx.lang.FX.print("\nvar resmulByteWithCharacter_f: String=\"{fmul}\";");       
       assertEquals("{res.resmulByteWithCharacter_f}", "{fmul}");
       var rdiv: Number = vByte / vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar resdivByteWithCharacter: Number={rdiv};");       
       assertEquals("{res.resdivByteWithCharacter}", "{rdiv}");

       var fdiv: String = foo(vByte / vCharacter) ;
       if (gen)javafx.lang.FX.print("\nvar resdivByteWithCharacter_f: String=\"{fdiv}\";");       
       assertEquals("{res.resdivByteWithCharacter_f}", "{fdiv}");
       var rmod: Number = vByte mod vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar resmodByteWithCharacter: Number={rmod};");       
       assertEquals("{res.resmodByteWithCharacter}", "{rmod}");

       var fmod: String = foo(vByte mod vCharacter) ;
       if (gen)javafx.lang.FX.print("\nvar resmodByteWithCharacter_f: String=\"{fmod}\";");       
       assertEquals("{res.resmodByteWithCharacter_f}", "{fmod}");
       var req: Boolean = vByte == vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar reseqByteWithCharacter: Boolean={req};");       
       assertEquals("{res.reseqByteWithCharacter}", "{req}");

       var rneq: Boolean = vByte != vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar resneqByteWithCharacter: Boolean={rneq};");       
       assertEquals("{res.resneqByteWithCharacter}", "{rneq}");

       var rlt: Boolean = vByte < vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar resltByteWithCharacter: Boolean={rlt};");       
       assertEquals("{res.resltByteWithCharacter}", "{rlt}");

       var rlte: Boolean = vByte <= vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar reslteByteWithCharacter: Boolean={rlte};");       
       assertEquals("{res.reslteByteWithCharacter}", "{rlte}");

       var rgt: Boolean = vByte > vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar resgtByteWithCharacter: Boolean={rgt};");       
       assertEquals("{res.resgtByteWithCharacter}", "{rgt}");

       var rgte: Boolean = vByte >= vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar resgteByteWithCharacter: Boolean={rgte};");       
       assertEquals("{res.resgteByteWithCharacter}", "{rgte}");

       if (gen)javafx.lang.FX.print("\n//");
}
    function testOperByteWithInteger() { 
       var radd: Number = vByte + vInteger ;
       if (gen)javafx.lang.FX.print("\nvar resaddByteWithInteger: Number={radd};");       
       assertEquals("{res.resaddByteWithInteger}", "{radd}");

       var fadd: String = foo(vByte + vInteger) ;
       if (gen)javafx.lang.FX.print("\nvar resaddByteWithInteger_f: String=\"{fadd}\";");       
       assertEquals("{res.resaddByteWithInteger_f}", "{fadd}");
       var rsub: Number = vByte - vInteger ;
       if (gen)javafx.lang.FX.print("\nvar ressubByteWithInteger: Number={rsub};");       
       assertEquals("{res.ressubByteWithInteger}", "{rsub}");

       var fsub: String = foo(vByte - vInteger) ;
       if (gen)javafx.lang.FX.print("\nvar ressubByteWithInteger_f: String=\"{fsub}\";");       
       assertEquals("{res.ressubByteWithInteger_f}", "{fsub}");
       var rmul: Number = vByte * vInteger ;
       if (gen)javafx.lang.FX.print("\nvar resmulByteWithInteger: Number={rmul};");       
       assertEquals("{res.resmulByteWithInteger}", "{rmul}");

       var fmul: String = foo(vByte * vInteger) ;
       if (gen)javafx.lang.FX.print("\nvar resmulByteWithInteger_f: String=\"{fmul}\";");       
       assertEquals("{res.resmulByteWithInteger_f}", "{fmul}");
       var rdiv: Number = vByte / vInteger ;
       if (gen)javafx.lang.FX.print("\nvar resdivByteWithInteger: Number={rdiv};");       
       assertEquals("{res.resdivByteWithInteger}", "{rdiv}");

       var fdiv: String = foo(vByte / vInteger) ;
       if (gen)javafx.lang.FX.print("\nvar resdivByteWithInteger_f: String=\"{fdiv}\";");       
       assertEquals("{res.resdivByteWithInteger_f}", "{fdiv}");
       var rmod: Number = vByte mod vInteger ;
       if (gen)javafx.lang.FX.print("\nvar resmodByteWithInteger: Number={rmod};");       
       assertEquals("{res.resmodByteWithInteger}", "{rmod}");

       var fmod: String = foo(vByte mod vInteger) ;
       if (gen)javafx.lang.FX.print("\nvar resmodByteWithInteger_f: String=\"{fmod}\";");       
       assertEquals("{res.resmodByteWithInteger_f}", "{fmod}");
       var req: Boolean = vByte == vInteger ;
       if (gen)javafx.lang.FX.print("\nvar reseqByteWithInteger: Boolean={req};");       
       assertEquals("{res.reseqByteWithInteger}", "{req}");

       var rneq: Boolean = vByte != vInteger ;
       if (gen)javafx.lang.FX.print("\nvar resneqByteWithInteger: Boolean={rneq};");       
       assertEquals("{res.resneqByteWithInteger}", "{rneq}");

       var rlt: Boolean = vByte < vInteger ;
       if (gen)javafx.lang.FX.print("\nvar resltByteWithInteger: Boolean={rlt};");       
       assertEquals("{res.resltByteWithInteger}", "{rlt}");

       var rlte: Boolean = vByte <= vInteger ;
       if (gen)javafx.lang.FX.print("\nvar reslteByteWithInteger: Boolean={rlte};");       
       assertEquals("{res.reslteByteWithInteger}", "{rlte}");

       var rgt: Boolean = vByte > vInteger ;
       if (gen)javafx.lang.FX.print("\nvar resgtByteWithInteger: Boolean={rgt};");       
       assertEquals("{res.resgtByteWithInteger}", "{rgt}");

       var rgte: Boolean = vByte >= vInteger ;
       if (gen)javafx.lang.FX.print("\nvar resgteByteWithInteger: Boolean={rgte};");       
       assertEquals("{res.resgteByteWithInteger}", "{rgte}");

       if (gen)javafx.lang.FX.print("\n//");
}
    function testOperByteWithLong() { 
       var radd: Number = vByte + vLong ;
       if (gen)javafx.lang.FX.print("\nvar resaddByteWithLong: Number={radd};");       
       assertEquals("{res.resaddByteWithLong}", "{radd}");

       var fadd: String = foo(vByte + vLong) ;
       if (gen)javafx.lang.FX.print("\nvar resaddByteWithLong_f: String=\"{fadd}\";");       
       assertEquals("{res.resaddByteWithLong_f}", "{fadd}");
       var rsub: Number = vByte - vLong ;
       if (gen)javafx.lang.FX.print("\nvar ressubByteWithLong: Number={rsub};");       
       assertEquals("{res.ressubByteWithLong}", "{rsub}");

       var fsub: String = foo(vByte - vLong) ;
       if (gen)javafx.lang.FX.print("\nvar ressubByteWithLong_f: String=\"{fsub}\";");       
       assertEquals("{res.ressubByteWithLong_f}", "{fsub}");
       var rmul: Number = vByte * vLong ;
       if (gen)javafx.lang.FX.print("\nvar resmulByteWithLong: Number={rmul};");       
       assertEquals("{res.resmulByteWithLong}", "{rmul}");

       var fmul: String = foo(vByte * vLong) ;
       if (gen)javafx.lang.FX.print("\nvar resmulByteWithLong_f: String=\"{fmul}\";");       
       assertEquals("{res.resmulByteWithLong_f}", "{fmul}");
       var rdiv: Number = vByte / vLong ;
       if (gen)javafx.lang.FX.print("\nvar resdivByteWithLong: Number={rdiv};");       
       assertEquals("{res.resdivByteWithLong}", "{rdiv}");

       var fdiv: String = foo(vByte / vLong) ;
       if (gen)javafx.lang.FX.print("\nvar resdivByteWithLong_f: String=\"{fdiv}\";");       
       assertEquals("{res.resdivByteWithLong_f}", "{fdiv}");
       var rmod: Number = vByte mod vLong ;
       if (gen)javafx.lang.FX.print("\nvar resmodByteWithLong: Number={rmod};");       
       assertEquals("{res.resmodByteWithLong}", "{rmod}");

       var fmod: String = foo(vByte mod vLong) ;
       if (gen)javafx.lang.FX.print("\nvar resmodByteWithLong_f: String=\"{fmod}\";");       
       assertEquals("{res.resmodByteWithLong_f}", "{fmod}");
       var req: Boolean = vByte == vLong ;
       if (gen)javafx.lang.FX.print("\nvar reseqByteWithLong: Boolean={req};");       
       assertEquals("{res.reseqByteWithLong}", "{req}");

       var rneq: Boolean = vByte != vLong ;
       if (gen)javafx.lang.FX.print("\nvar resneqByteWithLong: Boolean={rneq};");       
       assertEquals("{res.resneqByteWithLong}", "{rneq}");

       var rlt: Boolean = vByte < vLong ;
       if (gen)javafx.lang.FX.print("\nvar resltByteWithLong: Boolean={rlt};");       
       assertEquals("{res.resltByteWithLong}", "{rlt}");

       var rlte: Boolean = vByte <= vLong ;
       if (gen)javafx.lang.FX.print("\nvar reslteByteWithLong: Boolean={rlte};");       
       assertEquals("{res.reslteByteWithLong}", "{rlte}");

       var rgt: Boolean = vByte > vLong ;
       if (gen)javafx.lang.FX.print("\nvar resgtByteWithLong: Boolean={rgt};");       
       assertEquals("{res.resgtByteWithLong}", "{rgt}");

       var rgte: Boolean = vByte >= vLong ;
       if (gen)javafx.lang.FX.print("\nvar resgteByteWithLong: Boolean={rgte};");       
       assertEquals("{res.resgteByteWithLong}", "{rgte}");

       if (gen)javafx.lang.FX.print("\n//");
}
    function testOperByteWithFloat() { 
       var radd: Number = vByte + vFloat ;
       if (gen)javafx.lang.FX.print("\nvar resaddByteWithFloat: Number={radd};");       
       assertEquals("{res.resaddByteWithFloat}", "{radd}");

       var fadd: String = foo(vByte + vFloat) ;
       if (gen)javafx.lang.FX.print("\nvar resaddByteWithFloat_f: String=\"{fadd}\";");       
       assertEquals("{res.resaddByteWithFloat_f}", "{fadd}");
       var rsub: Number = vByte - vFloat ;
       if (gen)javafx.lang.FX.print("\nvar ressubByteWithFloat: Number={rsub};");       
       assertEquals("{res.ressubByteWithFloat}", "{rsub}");

       var fsub: String = foo(vByte - vFloat) ;
       if (gen)javafx.lang.FX.print("\nvar ressubByteWithFloat_f: String=\"{fsub}\";");       
       assertEquals("{res.ressubByteWithFloat_f}", "{fsub}");
       var rmul: Number = vByte * vFloat ;
       if (gen)javafx.lang.FX.print("\nvar resmulByteWithFloat: Number={rmul};");       
       assertEquals("{res.resmulByteWithFloat}", "{rmul}");

       var fmul: String = foo(vByte * vFloat) ;
       if (gen)javafx.lang.FX.print("\nvar resmulByteWithFloat_f: String=\"{fmul}\";");       
       assertEquals("{res.resmulByteWithFloat_f}", "{fmul}");
       var rdiv: Number = vByte / vFloat ;
       if (gen)javafx.lang.FX.print("\nvar resdivByteWithFloat: Number={rdiv};");       
       assertEquals("{res.resdivByteWithFloat}", "{rdiv}");

       var fdiv: String = foo(vByte / vFloat) ;
       if (gen)javafx.lang.FX.print("\nvar resdivByteWithFloat_f: String=\"{fdiv}\";");       
       assertEquals("{res.resdivByteWithFloat_f}", "{fdiv}");
       var rmod: Number = vByte mod vFloat ;
       if (gen)javafx.lang.FX.print("\nvar resmodByteWithFloat: Number={rmod};");       
       assertEquals("{res.resmodByteWithFloat}", "{rmod}");

       var fmod: String = foo(vByte mod vFloat) ;
       if (gen)javafx.lang.FX.print("\nvar resmodByteWithFloat_f: String=\"{fmod}\";");       
       assertEquals("{res.resmodByteWithFloat_f}", "{fmod}");
       var req: Boolean = vByte == vFloat ;
       if (gen)javafx.lang.FX.print("\nvar reseqByteWithFloat: Boolean={req};");       
       assertEquals("{res.reseqByteWithFloat}", "{req}");

       var rneq: Boolean = vByte != vFloat ;
       if (gen)javafx.lang.FX.print("\nvar resneqByteWithFloat: Boolean={rneq};");       
       assertEquals("{res.resneqByteWithFloat}", "{rneq}");

       var rlt: Boolean = vByte < vFloat ;
       if (gen)javafx.lang.FX.print("\nvar resltByteWithFloat: Boolean={rlt};");       
       assertEquals("{res.resltByteWithFloat}", "{rlt}");

       var rlte: Boolean = vByte <= vFloat ;
       if (gen)javafx.lang.FX.print("\nvar reslteByteWithFloat: Boolean={rlte};");       
       assertEquals("{res.reslteByteWithFloat}", "{rlte}");

       var rgt: Boolean = vByte > vFloat ;
       if (gen)javafx.lang.FX.print("\nvar resgtByteWithFloat: Boolean={rgt};");       
       assertEquals("{res.resgtByteWithFloat}", "{rgt}");

       var rgte: Boolean = vByte >= vFloat ;
       if (gen)javafx.lang.FX.print("\nvar resgteByteWithFloat: Boolean={rgte};");       
       assertEquals("{res.resgteByteWithFloat}", "{rgte}");

       if (gen)javafx.lang.FX.print("\n//");
}
    function testOperByteWithDouble() { 
       var radd: Number = vByte + vDouble ;
       if (gen)javafx.lang.FX.print("\nvar resaddByteWithDouble: Number={radd};");       
       assertEquals("{res.resaddByteWithDouble}", "{radd}");

       var fadd: String = foo(vByte + vDouble) ;
       if (gen)javafx.lang.FX.print("\nvar resaddByteWithDouble_f: String=\"{fadd}\";");       
       assertEquals("{res.resaddByteWithDouble_f}", "{fadd}");
       var rsub: Number = vByte - vDouble ;
       if (gen)javafx.lang.FX.print("\nvar ressubByteWithDouble: Number={rsub};");       
       assertEquals("{res.ressubByteWithDouble}", "{rsub}");

       var fsub: String = foo(vByte - vDouble) ;
       if (gen)javafx.lang.FX.print("\nvar ressubByteWithDouble_f: String=\"{fsub}\";");       
       assertEquals("{res.ressubByteWithDouble_f}", "{fsub}");
       var rmul: Number = vByte * vDouble ;
       if (gen)javafx.lang.FX.print("\nvar resmulByteWithDouble: Number={rmul};");       
       assertEquals("{res.resmulByteWithDouble}", "{rmul}");

       var fmul: String = foo(vByte * vDouble) ;
       if (gen)javafx.lang.FX.print("\nvar resmulByteWithDouble_f: String=\"{fmul}\";");       
       assertEquals("{res.resmulByteWithDouble_f}", "{fmul}");
       var rdiv: Number = vByte / vDouble ;
       if (gen)javafx.lang.FX.print("\nvar resdivByteWithDouble: Number={rdiv};");       
       assertEquals("{res.resdivByteWithDouble}", "{rdiv}");

       var fdiv: String = foo(vByte / vDouble) ;
       if (gen)javafx.lang.FX.print("\nvar resdivByteWithDouble_f: String=\"{fdiv}\";");       
       assertEquals("{res.resdivByteWithDouble_f}", "{fdiv}");
       var rmod: Number = vByte mod vDouble ;
       if (gen)javafx.lang.FX.print("\nvar resmodByteWithDouble: Number={rmod};");       
       assertEquals("{res.resmodByteWithDouble}", "{rmod}");

       var fmod: String = foo(vByte mod vDouble) ;
       if (gen)javafx.lang.FX.print("\nvar resmodByteWithDouble_f: String=\"{fmod}\";");       
       assertEquals("{res.resmodByteWithDouble_f}", "{fmod}");
       var req: Boolean = vByte == vDouble ;
       if (gen)javafx.lang.FX.print("\nvar reseqByteWithDouble: Boolean={req};");       
       assertEquals("{res.reseqByteWithDouble}", "{req}");

       var rneq: Boolean = vByte != vDouble ;
       if (gen)javafx.lang.FX.print("\nvar resneqByteWithDouble: Boolean={rneq};");       
       assertEquals("{res.resneqByteWithDouble}", "{rneq}");

       var rlt: Boolean = vByte < vDouble ;
       if (gen)javafx.lang.FX.print("\nvar resltByteWithDouble: Boolean={rlt};");       
       assertEquals("{res.resltByteWithDouble}", "{rlt}");

       var rlte: Boolean = vByte <= vDouble ;
       if (gen)javafx.lang.FX.print("\nvar reslteByteWithDouble: Boolean={rlte};");       
       assertEquals("{res.reslteByteWithDouble}", "{rlte}");

       var rgt: Boolean = vByte > vDouble ;
       if (gen)javafx.lang.FX.print("\nvar resgtByteWithDouble: Boolean={rgt};");       
       assertEquals("{res.resgtByteWithDouble}", "{rgt}");

       var rgte: Boolean = vByte >= vDouble ;
       if (gen)javafx.lang.FX.print("\nvar resgteByteWithDouble: Boolean={rgte};");       
       assertEquals("{res.resgteByteWithDouble}", "{rgte}");

       if (gen)javafx.lang.FX.print("\n//");
}
    function testOperByteWithNumber() { 
       var radd: Number = vByte + vNumber ;
       if (gen)javafx.lang.FX.print("\nvar resaddByteWithNumber: Number={radd};");       
       assertEquals("{res.resaddByteWithNumber}", "{radd}");

       var fadd: String = foo(vByte + vNumber) ;
       if (gen)javafx.lang.FX.print("\nvar resaddByteWithNumber_f: String=\"{fadd}\";");       
       assertEquals("{res.resaddByteWithNumber_f}", "{fadd}");
       var rsub: Number = vByte - vNumber ;
       if (gen)javafx.lang.FX.print("\nvar ressubByteWithNumber: Number={rsub};");       
       assertEquals("{res.ressubByteWithNumber}", "{rsub}");

       var fsub: String = foo(vByte - vNumber) ;
       if (gen)javafx.lang.FX.print("\nvar ressubByteWithNumber_f: String=\"{fsub}\";");       
       assertEquals("{res.ressubByteWithNumber_f}", "{fsub}");
       var rmul: Number = vByte * vNumber ;
       if (gen)javafx.lang.FX.print("\nvar resmulByteWithNumber: Number={rmul};");       
       assertEquals("{res.resmulByteWithNumber}", "{rmul}");

       var fmul: String = foo(vByte * vNumber) ;
       if (gen)javafx.lang.FX.print("\nvar resmulByteWithNumber_f: String=\"{fmul}\";");       
       assertEquals("{res.resmulByteWithNumber_f}", "{fmul}");
       var rdiv: Number = vByte / vNumber ;
       if (gen)javafx.lang.FX.print("\nvar resdivByteWithNumber: Number={rdiv};");       
       assertEquals("{res.resdivByteWithNumber}", "{rdiv}");

       var fdiv: String = foo(vByte / vNumber) ;
       if (gen)javafx.lang.FX.print("\nvar resdivByteWithNumber_f: String=\"{fdiv}\";");       
       assertEquals("{res.resdivByteWithNumber_f}", "{fdiv}");
       var rmod: Number = vByte mod vNumber ;
       if (gen)javafx.lang.FX.print("\nvar resmodByteWithNumber: Number={rmod};");       
       assertEquals("{res.resmodByteWithNumber}", "{rmod}");

       var fmod: String = foo(vByte mod vNumber) ;
       if (gen)javafx.lang.FX.print("\nvar resmodByteWithNumber_f: String=\"{fmod}\";");       
       assertEquals("{res.resmodByteWithNumber_f}", "{fmod}");
       var req: Boolean = vByte == vNumber ;
       if (gen)javafx.lang.FX.print("\nvar reseqByteWithNumber: Boolean={req};");       
       assertEquals("{res.reseqByteWithNumber}", "{req}");

       var rneq: Boolean = vByte != vNumber ;
       if (gen)javafx.lang.FX.print("\nvar resneqByteWithNumber: Boolean={rneq};");       
       assertEquals("{res.resneqByteWithNumber}", "{rneq}");

       var rlt: Boolean = vByte < vNumber ;
       if (gen)javafx.lang.FX.print("\nvar resltByteWithNumber: Boolean={rlt};");       
       assertEquals("{res.resltByteWithNumber}", "{rlt}");

       var rlte: Boolean = vByte <= vNumber ;
       if (gen)javafx.lang.FX.print("\nvar reslteByteWithNumber: Boolean={rlte};");       
       assertEquals("{res.reslteByteWithNumber}", "{rlte}");

       var rgt: Boolean = vByte > vNumber ;
       if (gen)javafx.lang.FX.print("\nvar resgtByteWithNumber: Boolean={rgt};");       
       assertEquals("{res.resgtByteWithNumber}", "{rgt}");

       var rgte: Boolean = vByte >= vNumber ;
       if (gen)javafx.lang.FX.print("\nvar resgteByteWithNumber: Boolean={rgte};");       
       assertEquals("{res.resgteByteWithNumber}", "{rgte}");

       if (gen)javafx.lang.FX.print("\n//");
}
    var xShort: Short[] = vShort;  
    function testOperShortWithByte() { 
       var radd: Number = vShort + vByte ;
       if (gen)javafx.lang.FX.print("\nvar resaddShortWithByte: Number={radd};");       
       assertEquals("{res.resaddShortWithByte}", "{radd}");

       var fadd: String = foo(vShort + vByte) ;
       if (gen)javafx.lang.FX.print("\nvar resaddShortWithByte_f: String=\"{fadd}\";");       
       assertEquals("{res.resaddShortWithByte_f}", "{fadd}");
       var rsub: Number = vShort - vByte ;
       if (gen)javafx.lang.FX.print("\nvar ressubShortWithByte: Number={rsub};");       
       assertEquals("{res.ressubShortWithByte}", "{rsub}");

       var fsub: String = foo(vShort - vByte) ;
       if (gen)javafx.lang.FX.print("\nvar ressubShortWithByte_f: String=\"{fsub}\";");       
       assertEquals("{res.ressubShortWithByte_f}", "{fsub}");
       var rmul: Number = vShort * vByte ;
       if (gen)javafx.lang.FX.print("\nvar resmulShortWithByte: Number={rmul};");       
       assertEquals("{res.resmulShortWithByte}", "{rmul}");

       var fmul: String = foo(vShort * vByte) ;
       if (gen)javafx.lang.FX.print("\nvar resmulShortWithByte_f: String=\"{fmul}\";");       
       assertEquals("{res.resmulShortWithByte_f}", "{fmul}");
       var rdiv: Number = vShort / vByte ;
       if (gen)javafx.lang.FX.print("\nvar resdivShortWithByte: Number={rdiv};");       
       assertEquals("{res.resdivShortWithByte}", "{rdiv}");

       var fdiv: String = foo(vShort / vByte) ;
       if (gen)javafx.lang.FX.print("\nvar resdivShortWithByte_f: String=\"{fdiv}\";");       
       assertEquals("{res.resdivShortWithByte_f}", "{fdiv}");
       var rmod: Number = vShort mod vByte ;
       if (gen)javafx.lang.FX.print("\nvar resmodShortWithByte: Number={rmod};");       
       assertEquals("{res.resmodShortWithByte}", "{rmod}");

       var fmod: String = foo(vShort mod vByte) ;
       if (gen)javafx.lang.FX.print("\nvar resmodShortWithByte_f: String=\"{fmod}\";");       
       assertEquals("{res.resmodShortWithByte_f}", "{fmod}");
       var req: Boolean = vShort == vByte ;
       if (gen)javafx.lang.FX.print("\nvar reseqShortWithByte: Boolean={req};");       
       assertEquals("{res.reseqShortWithByte}", "{req}");

       var rneq: Boolean = vShort != vByte ;
       if (gen)javafx.lang.FX.print("\nvar resneqShortWithByte: Boolean={rneq};");       
       assertEquals("{res.resneqShortWithByte}", "{rneq}");

       var rlt: Boolean = vShort < vByte ;
       if (gen)javafx.lang.FX.print("\nvar resltShortWithByte: Boolean={rlt};");       
       assertEquals("{res.resltShortWithByte}", "{rlt}");

       var rlte: Boolean = vShort <= vByte ;
       if (gen)javafx.lang.FX.print("\nvar reslteShortWithByte: Boolean={rlte};");       
       assertEquals("{res.reslteShortWithByte}", "{rlte}");

       var rgt: Boolean = vShort > vByte ;
       if (gen)javafx.lang.FX.print("\nvar resgtShortWithByte: Boolean={rgt};");       
       assertEquals("{res.resgtShortWithByte}", "{rgt}");

       var rgte: Boolean = vShort >= vByte ;
       if (gen)javafx.lang.FX.print("\nvar resgteShortWithByte: Boolean={rgte};");       
       assertEquals("{res.resgteShortWithByte}", "{rgte}");

       if (gen)javafx.lang.FX.print("\n//");
}
    function testOperShortWithShort() { 
       var radd: Number = vShort + vShort ;
       if (gen)javafx.lang.FX.print("\nvar resaddShortWithShort: Number={radd};");       
       assertEquals("{res.resaddShortWithShort}", "{radd}");

       var fadd: String = foo(vShort + vShort) ;
       if (gen)javafx.lang.FX.print("\nvar resaddShortWithShort_f: String=\"{fadd}\";");       
       assertEquals("{res.resaddShortWithShort_f}", "{fadd}");
       var rsub: Number = vShort - vShort ;
       if (gen)javafx.lang.FX.print("\nvar ressubShortWithShort: Number={rsub};");       
       assertEquals("{res.ressubShortWithShort}", "{rsub}");

       var fsub: String = foo(vShort - vShort) ;
       if (gen)javafx.lang.FX.print("\nvar ressubShortWithShort_f: String=\"{fsub}\";");       
       assertEquals("{res.ressubShortWithShort_f}", "{fsub}");
       var rmul: Number = vShort * vShort ;
       if (gen)javafx.lang.FX.print("\nvar resmulShortWithShort: Number={rmul};");       
       assertEquals("{res.resmulShortWithShort}", "{rmul}");

       var fmul: String = foo(vShort * vShort) ;
       if (gen)javafx.lang.FX.print("\nvar resmulShortWithShort_f: String=\"{fmul}\";");       
       assertEquals("{res.resmulShortWithShort_f}", "{fmul}");
       var rdiv: Number = vShort / vShort ;
       if (gen)javafx.lang.FX.print("\nvar resdivShortWithShort: Number={rdiv};");       
       assertEquals("{res.resdivShortWithShort}", "{rdiv}");

       var fdiv: String = foo(vShort / vShort) ;
       if (gen)javafx.lang.FX.print("\nvar resdivShortWithShort_f: String=\"{fdiv}\";");       
       assertEquals("{res.resdivShortWithShort_f}", "{fdiv}");
       var rmod: Number = vShort mod vShort ;
       if (gen)javafx.lang.FX.print("\nvar resmodShortWithShort: Number={rmod};");       
       assertEquals("{res.resmodShortWithShort}", "{rmod}");

       var fmod: String = foo(vShort mod vShort) ;
       if (gen)javafx.lang.FX.print("\nvar resmodShortWithShort_f: String=\"{fmod}\";");       
       assertEquals("{res.resmodShortWithShort_f}", "{fmod}");
       var req: Boolean = vShort == vShort ;
       if (gen)javafx.lang.FX.print("\nvar reseqShortWithShort: Boolean={req};");       
       assertEquals("{res.reseqShortWithShort}", "{req}");

       var rneq: Boolean = vShort != vShort ;
       if (gen)javafx.lang.FX.print("\nvar resneqShortWithShort: Boolean={rneq};");       
       assertEquals("{res.resneqShortWithShort}", "{rneq}");

       var rlt: Boolean = vShort < vShort ;
       if (gen)javafx.lang.FX.print("\nvar resltShortWithShort: Boolean={rlt};");       
       assertEquals("{res.resltShortWithShort}", "{rlt}");

       var rlte: Boolean = vShort <= vShort ;
       if (gen)javafx.lang.FX.print("\nvar reslteShortWithShort: Boolean={rlte};");       
       assertEquals("{res.reslteShortWithShort}", "{rlte}");

       var rgt: Boolean = vShort > vShort ;
       if (gen)javafx.lang.FX.print("\nvar resgtShortWithShort: Boolean={rgt};");       
       assertEquals("{res.resgtShortWithShort}", "{rgt}");

       var rgte: Boolean = vShort >= vShort ;
       if (gen)javafx.lang.FX.print("\nvar resgteShortWithShort: Boolean={rgte};");       
       assertEquals("{res.resgteShortWithShort}", "{rgte}");

       if (gen)javafx.lang.FX.print("\n//");
}
    function testOperShortWithCharacter() { 
       var radd: Number = vShort + vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar resaddShortWithCharacter: Number={radd};");       
       assertEquals("{res.resaddShortWithCharacter}", "{radd}");

       var fadd: String = foo(vShort + vCharacter) ;
       if (gen)javafx.lang.FX.print("\nvar resaddShortWithCharacter_f: String=\"{fadd}\";");       
       assertEquals("{res.resaddShortWithCharacter_f}", "{fadd}");
       var rsub: Number = vShort - vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar ressubShortWithCharacter: Number={rsub};");       
       assertEquals("{res.ressubShortWithCharacter}", "{rsub}");

       var fsub: String = foo(vShort - vCharacter) ;
       if (gen)javafx.lang.FX.print("\nvar ressubShortWithCharacter_f: String=\"{fsub}\";");       
       assertEquals("{res.ressubShortWithCharacter_f}", "{fsub}");
       var rmul: Number = vShort * vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar resmulShortWithCharacter: Number={rmul};");       
       assertEquals("{res.resmulShortWithCharacter}", "{rmul}");

       var fmul: String = foo(vShort * vCharacter) ;
       if (gen)javafx.lang.FX.print("\nvar resmulShortWithCharacter_f: String=\"{fmul}\";");       
       assertEquals("{res.resmulShortWithCharacter_f}", "{fmul}");
       var rdiv: Number = vShort / vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar resdivShortWithCharacter: Number={rdiv};");       
       assertEquals("{res.resdivShortWithCharacter}", "{rdiv}");

       var fdiv: String = foo(vShort / vCharacter) ;
       if (gen)javafx.lang.FX.print("\nvar resdivShortWithCharacter_f: String=\"{fdiv}\";");       
       assertEquals("{res.resdivShortWithCharacter_f}", "{fdiv}");
       var rmod: Number = vShort mod vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar resmodShortWithCharacter: Number={rmod};");       
       assertEquals("{res.resmodShortWithCharacter}", "{rmod}");

       var fmod: String = foo(vShort mod vCharacter) ;
       if (gen)javafx.lang.FX.print("\nvar resmodShortWithCharacter_f: String=\"{fmod}\";");       
       assertEquals("{res.resmodShortWithCharacter_f}", "{fmod}");
       var req: Boolean = vShort == vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar reseqShortWithCharacter: Boolean={req};");       
       assertEquals("{res.reseqShortWithCharacter}", "{req}");

       var rneq: Boolean = vShort != vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar resneqShortWithCharacter: Boolean={rneq};");       
       assertEquals("{res.resneqShortWithCharacter}", "{rneq}");

       var rlt: Boolean = vShort < vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar resltShortWithCharacter: Boolean={rlt};");       
       assertEquals("{res.resltShortWithCharacter}", "{rlt}");

       var rlte: Boolean = vShort <= vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar reslteShortWithCharacter: Boolean={rlte};");       
       assertEquals("{res.reslteShortWithCharacter}", "{rlte}");

       var rgt: Boolean = vShort > vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar resgtShortWithCharacter: Boolean={rgt};");       
       assertEquals("{res.resgtShortWithCharacter}", "{rgt}");

       var rgte: Boolean = vShort >= vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar resgteShortWithCharacter: Boolean={rgte};");       
       assertEquals("{res.resgteShortWithCharacter}", "{rgte}");

       if (gen)javafx.lang.FX.print("\n//");
}
    function testOperShortWithInteger() { 
       var radd: Number = vShort + vInteger ;
       if (gen)javafx.lang.FX.print("\nvar resaddShortWithInteger: Number={radd};");       
       assertEquals("{res.resaddShortWithInteger}", "{radd}");

       var fadd: String = foo(vShort + vInteger) ;
       if (gen)javafx.lang.FX.print("\nvar resaddShortWithInteger_f: String=\"{fadd}\";");       
       assertEquals("{res.resaddShortWithInteger_f}", "{fadd}");
       var rsub: Number = vShort - vInteger ;
       if (gen)javafx.lang.FX.print("\nvar ressubShortWithInteger: Number={rsub};");       
       assertEquals("{res.ressubShortWithInteger}", "{rsub}");

       var fsub: String = foo(vShort - vInteger) ;
       if (gen)javafx.lang.FX.print("\nvar ressubShortWithInteger_f: String=\"{fsub}\";");       
       assertEquals("{res.ressubShortWithInteger_f}", "{fsub}");
       var rmul: Number = vShort * vInteger ;
       if (gen)javafx.lang.FX.print("\nvar resmulShortWithInteger: Number={rmul};");       
       assertEquals("{res.resmulShortWithInteger}", "{rmul}");

       var fmul: String = foo(vShort * vInteger) ;
       if (gen)javafx.lang.FX.print("\nvar resmulShortWithInteger_f: String=\"{fmul}\";");       
       assertEquals("{res.resmulShortWithInteger_f}", "{fmul}");
       var rdiv: Number = vShort / vInteger ;
       if (gen)javafx.lang.FX.print("\nvar resdivShortWithInteger: Number={rdiv};");       
       assertEquals("{res.resdivShortWithInteger}", "{rdiv}");

       var fdiv: String = foo(vShort / vInteger) ;
       if (gen)javafx.lang.FX.print("\nvar resdivShortWithInteger_f: String=\"{fdiv}\";");       
       assertEquals("{res.resdivShortWithInteger_f}", "{fdiv}");
       var rmod: Number = vShort mod vInteger ;
       if (gen)javafx.lang.FX.print("\nvar resmodShortWithInteger: Number={rmod};");       
       assertEquals("{res.resmodShortWithInteger}", "{rmod}");

       var fmod: String = foo(vShort mod vInteger) ;
       if (gen)javafx.lang.FX.print("\nvar resmodShortWithInteger_f: String=\"{fmod}\";");       
       assertEquals("{res.resmodShortWithInteger_f}", "{fmod}");
       var req: Boolean = vShort == vInteger ;
       if (gen)javafx.lang.FX.print("\nvar reseqShortWithInteger: Boolean={req};");       
       assertEquals("{res.reseqShortWithInteger}", "{req}");

       var rneq: Boolean = vShort != vInteger ;
       if (gen)javafx.lang.FX.print("\nvar resneqShortWithInteger: Boolean={rneq};");       
       assertEquals("{res.resneqShortWithInteger}", "{rneq}");

       var rlt: Boolean = vShort < vInteger ;
       if (gen)javafx.lang.FX.print("\nvar resltShortWithInteger: Boolean={rlt};");       
       assertEquals("{res.resltShortWithInteger}", "{rlt}");

       var rlte: Boolean = vShort <= vInteger ;
       if (gen)javafx.lang.FX.print("\nvar reslteShortWithInteger: Boolean={rlte};");       
       assertEquals("{res.reslteShortWithInteger}", "{rlte}");

       var rgt: Boolean = vShort > vInteger ;
       if (gen)javafx.lang.FX.print("\nvar resgtShortWithInteger: Boolean={rgt};");       
       assertEquals("{res.resgtShortWithInteger}", "{rgt}");

       var rgte: Boolean = vShort >= vInteger ;
       if (gen)javafx.lang.FX.print("\nvar resgteShortWithInteger: Boolean={rgte};");       
       assertEquals("{res.resgteShortWithInteger}", "{rgte}");

       if (gen)javafx.lang.FX.print("\n//");
}
    function testOperShortWithLong() { 
       var radd: Number = vShort + vLong ;
       if (gen)javafx.lang.FX.print("\nvar resaddShortWithLong: Number={radd};");       
       assertEquals("{res.resaddShortWithLong}", "{radd}");

       var fadd: String = foo(vShort + vLong) ;
       if (gen)javafx.lang.FX.print("\nvar resaddShortWithLong_f: String=\"{fadd}\";");       
       assertEquals("{res.resaddShortWithLong_f}", "{fadd}");
       var rsub: Number = vShort - vLong ;
       if (gen)javafx.lang.FX.print("\nvar ressubShortWithLong: Number={rsub};");       
       assertEquals("{res.ressubShortWithLong}", "{rsub}");

       var fsub: String = foo(vShort - vLong) ;
       if (gen)javafx.lang.FX.print("\nvar ressubShortWithLong_f: String=\"{fsub}\";");       
       assertEquals("{res.ressubShortWithLong_f}", "{fsub}");
       var rmul: Number = vShort * vLong ;
       if (gen)javafx.lang.FX.print("\nvar resmulShortWithLong: Number={rmul};");       
       assertEquals("{res.resmulShortWithLong}", "{rmul}");

       var fmul: String = foo(vShort * vLong) ;
       if (gen)javafx.lang.FX.print("\nvar resmulShortWithLong_f: String=\"{fmul}\";");       
       assertEquals("{res.resmulShortWithLong_f}", "{fmul}");
       var rdiv: Number = vShort / vLong ;
       if (gen)javafx.lang.FX.print("\nvar resdivShortWithLong: Number={rdiv};");       
       assertEquals("{res.resdivShortWithLong}", "{rdiv}");

       var fdiv: String = foo(vShort / vLong) ;
       if (gen)javafx.lang.FX.print("\nvar resdivShortWithLong_f: String=\"{fdiv}\";");       
       assertEquals("{res.resdivShortWithLong_f}", "{fdiv}");
       var rmod: Number = vShort mod vLong ;
       if (gen)javafx.lang.FX.print("\nvar resmodShortWithLong: Number={rmod};");       
       assertEquals("{res.resmodShortWithLong}", "{rmod}");

       var fmod: String = foo(vShort mod vLong) ;
       if (gen)javafx.lang.FX.print("\nvar resmodShortWithLong_f: String=\"{fmod}\";");       
       assertEquals("{res.resmodShortWithLong_f}", "{fmod}");
       var req: Boolean = vShort == vLong ;
       if (gen)javafx.lang.FX.print("\nvar reseqShortWithLong: Boolean={req};");       
       assertEquals("{res.reseqShortWithLong}", "{req}");

       var rneq: Boolean = vShort != vLong ;
       if (gen)javafx.lang.FX.print("\nvar resneqShortWithLong: Boolean={rneq};");       
       assertEquals("{res.resneqShortWithLong}", "{rneq}");

       var rlt: Boolean = vShort < vLong ;
       if (gen)javafx.lang.FX.print("\nvar resltShortWithLong: Boolean={rlt};");       
       assertEquals("{res.resltShortWithLong}", "{rlt}");

       var rlte: Boolean = vShort <= vLong ;
       if (gen)javafx.lang.FX.print("\nvar reslteShortWithLong: Boolean={rlte};");       
       assertEquals("{res.reslteShortWithLong}", "{rlte}");

       var rgt: Boolean = vShort > vLong ;
       if (gen)javafx.lang.FX.print("\nvar resgtShortWithLong: Boolean={rgt};");       
       assertEquals("{res.resgtShortWithLong}", "{rgt}");

       var rgte: Boolean = vShort >= vLong ;
       if (gen)javafx.lang.FX.print("\nvar resgteShortWithLong: Boolean={rgte};");       
       assertEquals("{res.resgteShortWithLong}", "{rgte}");

       if (gen)javafx.lang.FX.print("\n//");
}
    function testOperShortWithFloat() { 
       var radd: Number = vShort + vFloat ;
       if (gen)javafx.lang.FX.print("\nvar resaddShortWithFloat: Number={radd};");       
       assertEquals("{res.resaddShortWithFloat}", "{radd}");

       var fadd: String = foo(vShort + vFloat) ;
       if (gen)javafx.lang.FX.print("\nvar resaddShortWithFloat_f: String=\"{fadd}\";");       
       assertEquals("{res.resaddShortWithFloat_f}", "{fadd}");
       var rsub: Number = vShort - vFloat ;
       if (gen)javafx.lang.FX.print("\nvar ressubShortWithFloat: Number={rsub};");       
       assertEquals("{res.ressubShortWithFloat}", "{rsub}");

       var fsub: String = foo(vShort - vFloat) ;
       if (gen)javafx.lang.FX.print("\nvar ressubShortWithFloat_f: String=\"{fsub}\";");       
       assertEquals("{res.ressubShortWithFloat_f}", "{fsub}");
       var rmul: Number = vShort * vFloat ;
       if (gen)javafx.lang.FX.print("\nvar resmulShortWithFloat: Number={rmul};");       
       assertEquals("{res.resmulShortWithFloat}", "{rmul}");

       var fmul: String = foo(vShort * vFloat) ;
       if (gen)javafx.lang.FX.print("\nvar resmulShortWithFloat_f: String=\"{fmul}\";");       
       assertEquals("{res.resmulShortWithFloat_f}", "{fmul}");
       var rdiv: Number = vShort / vFloat ;
       if (gen)javafx.lang.FX.print("\nvar resdivShortWithFloat: Number={rdiv};");       
       assertEquals("{res.resdivShortWithFloat}", "{rdiv}");

       var fdiv: String = foo(vShort / vFloat) ;
       if (gen)javafx.lang.FX.print("\nvar resdivShortWithFloat_f: String=\"{fdiv}\";");       
       assertEquals("{res.resdivShortWithFloat_f}", "{fdiv}");
       var rmod: Number = vShort mod vFloat ;
       if (gen)javafx.lang.FX.print("\nvar resmodShortWithFloat: Number={rmod};");       
       assertEquals("{res.resmodShortWithFloat}", "{rmod}");

       var fmod: String = foo(vShort mod vFloat) ;
       if (gen)javafx.lang.FX.print("\nvar resmodShortWithFloat_f: String=\"{fmod}\";");       
       assertEquals("{res.resmodShortWithFloat_f}", "{fmod}");
       var req: Boolean = vShort == vFloat ;
       if (gen)javafx.lang.FX.print("\nvar reseqShortWithFloat: Boolean={req};");       
       assertEquals("{res.reseqShortWithFloat}", "{req}");

       var rneq: Boolean = vShort != vFloat ;
       if (gen)javafx.lang.FX.print("\nvar resneqShortWithFloat: Boolean={rneq};");       
       assertEquals("{res.resneqShortWithFloat}", "{rneq}");

       var rlt: Boolean = vShort < vFloat ;
       if (gen)javafx.lang.FX.print("\nvar resltShortWithFloat: Boolean={rlt};");       
       assertEquals("{res.resltShortWithFloat}", "{rlt}");

       var rlte: Boolean = vShort <= vFloat ;
       if (gen)javafx.lang.FX.print("\nvar reslteShortWithFloat: Boolean={rlte};");       
       assertEquals("{res.reslteShortWithFloat}", "{rlte}");

       var rgt: Boolean = vShort > vFloat ;
       if (gen)javafx.lang.FX.print("\nvar resgtShortWithFloat: Boolean={rgt};");       
       assertEquals("{res.resgtShortWithFloat}", "{rgt}");

       var rgte: Boolean = vShort >= vFloat ;
       if (gen)javafx.lang.FX.print("\nvar resgteShortWithFloat: Boolean={rgte};");       
       assertEquals("{res.resgteShortWithFloat}", "{rgte}");

       if (gen)javafx.lang.FX.print("\n//");
}
    function testOperShortWithDouble() { 
       var radd: Number = vShort + vDouble ;
       if (gen)javafx.lang.FX.print("\nvar resaddShortWithDouble: Number={radd};");       
       assertEquals("{res.resaddShortWithDouble}", "{radd}");

       var fadd: String = foo(vShort + vDouble) ;
       if (gen)javafx.lang.FX.print("\nvar resaddShortWithDouble_f: String=\"{fadd}\";");       
       assertEquals("{res.resaddShortWithDouble_f}", "{fadd}");
       var rsub: Number = vShort - vDouble ;
       if (gen)javafx.lang.FX.print("\nvar ressubShortWithDouble: Number={rsub};");       
       assertEquals("{res.ressubShortWithDouble}", "{rsub}");

       var fsub: String = foo(vShort - vDouble) ;
       if (gen)javafx.lang.FX.print("\nvar ressubShortWithDouble_f: String=\"{fsub}\";");       
       assertEquals("{res.ressubShortWithDouble_f}", "{fsub}");
       var rmul: Number = vShort * vDouble ;
       if (gen)javafx.lang.FX.print("\nvar resmulShortWithDouble: Number={rmul};");       
       assertEquals("{res.resmulShortWithDouble}", "{rmul}");

       var fmul: String = foo(vShort * vDouble) ;
       if (gen)javafx.lang.FX.print("\nvar resmulShortWithDouble_f: String=\"{fmul}\";");       
       assertEquals("{res.resmulShortWithDouble_f}", "{fmul}");
       var rdiv: Number = vShort / vDouble ;
       if (gen)javafx.lang.FX.print("\nvar resdivShortWithDouble: Number={rdiv};");       
       assertEquals("{res.resdivShortWithDouble}", "{rdiv}");

       var fdiv: String = foo(vShort / vDouble) ;
       if (gen)javafx.lang.FX.print("\nvar resdivShortWithDouble_f: String=\"{fdiv}\";");       
       assertEquals("{res.resdivShortWithDouble_f}", "{fdiv}");
       var rmod: Number = vShort mod vDouble ;
       if (gen)javafx.lang.FX.print("\nvar resmodShortWithDouble: Number={rmod};");       
       assertEquals("{res.resmodShortWithDouble}", "{rmod}");

       var fmod: String = foo(vShort mod vDouble) ;
       if (gen)javafx.lang.FX.print("\nvar resmodShortWithDouble_f: String=\"{fmod}\";");       
       assertEquals("{res.resmodShortWithDouble_f}", "{fmod}");
       var req: Boolean = vShort == vDouble ;
       if (gen)javafx.lang.FX.print("\nvar reseqShortWithDouble: Boolean={req};");       
       assertEquals("{res.reseqShortWithDouble}", "{req}");

       var rneq: Boolean = vShort != vDouble ;
       if (gen)javafx.lang.FX.print("\nvar resneqShortWithDouble: Boolean={rneq};");       
       assertEquals("{res.resneqShortWithDouble}", "{rneq}");

       var rlt: Boolean = vShort < vDouble ;
       if (gen)javafx.lang.FX.print("\nvar resltShortWithDouble: Boolean={rlt};");       
       assertEquals("{res.resltShortWithDouble}", "{rlt}");

       var rlte: Boolean = vShort <= vDouble ;
       if (gen)javafx.lang.FX.print("\nvar reslteShortWithDouble: Boolean={rlte};");       
       assertEquals("{res.reslteShortWithDouble}", "{rlte}");

       var rgt: Boolean = vShort > vDouble ;
       if (gen)javafx.lang.FX.print("\nvar resgtShortWithDouble: Boolean={rgt};");       
       assertEquals("{res.resgtShortWithDouble}", "{rgt}");

       var rgte: Boolean = vShort >= vDouble ;
       if (gen)javafx.lang.FX.print("\nvar resgteShortWithDouble: Boolean={rgte};");       
       assertEquals("{res.resgteShortWithDouble}", "{rgte}");

       if (gen)javafx.lang.FX.print("\n//");
}
    function testOperShortWithNumber() { 
       var radd: Number = vShort + vNumber ;
       if (gen)javafx.lang.FX.print("\nvar resaddShortWithNumber: Number={radd};");       
       assertEquals("{res.resaddShortWithNumber}", "{radd}");

       var fadd: String = foo(vShort + vNumber) ;
       if (gen)javafx.lang.FX.print("\nvar resaddShortWithNumber_f: String=\"{fadd}\";");       
       assertEquals("{res.resaddShortWithNumber_f}", "{fadd}");
       var rsub: Number = vShort - vNumber ;
       if (gen)javafx.lang.FX.print("\nvar ressubShortWithNumber: Number={rsub};");       
       assertEquals("{res.ressubShortWithNumber}", "{rsub}");

       var fsub: String = foo(vShort - vNumber) ;
       if (gen)javafx.lang.FX.print("\nvar ressubShortWithNumber_f: String=\"{fsub}\";");       
       assertEquals("{res.ressubShortWithNumber_f}", "{fsub}");
       var rmul: Number = vShort * vNumber ;
       if (gen)javafx.lang.FX.print("\nvar resmulShortWithNumber: Number={rmul};");       
       assertEquals("{res.resmulShortWithNumber}", "{rmul}");

       var fmul: String = foo(vShort * vNumber) ;
       if (gen)javafx.lang.FX.print("\nvar resmulShortWithNumber_f: String=\"{fmul}\";");       
       assertEquals("{res.resmulShortWithNumber_f}", "{fmul}");
       var rdiv: Number = vShort / vNumber ;
       if (gen)javafx.lang.FX.print("\nvar resdivShortWithNumber: Number={rdiv};");       
       assertEquals("{res.resdivShortWithNumber}", "{rdiv}");

       var fdiv: String = foo(vShort / vNumber) ;
       if (gen)javafx.lang.FX.print("\nvar resdivShortWithNumber_f: String=\"{fdiv}\";");       
       assertEquals("{res.resdivShortWithNumber_f}", "{fdiv}");
       var rmod: Number = vShort mod vNumber ;
       if (gen)javafx.lang.FX.print("\nvar resmodShortWithNumber: Number={rmod};");       
       assertEquals("{res.resmodShortWithNumber}", "{rmod}");

       var fmod: String = foo(vShort mod vNumber) ;
       if (gen)javafx.lang.FX.print("\nvar resmodShortWithNumber_f: String=\"{fmod}\";");       
       assertEquals("{res.resmodShortWithNumber_f}", "{fmod}");
       var req: Boolean = vShort == vNumber ;
       if (gen)javafx.lang.FX.print("\nvar reseqShortWithNumber: Boolean={req};");       
       assertEquals("{res.reseqShortWithNumber}", "{req}");

       var rneq: Boolean = vShort != vNumber ;
       if (gen)javafx.lang.FX.print("\nvar resneqShortWithNumber: Boolean={rneq};");       
       assertEquals("{res.resneqShortWithNumber}", "{rneq}");

       var rlt: Boolean = vShort < vNumber ;
       if (gen)javafx.lang.FX.print("\nvar resltShortWithNumber: Boolean={rlt};");       
       assertEquals("{res.resltShortWithNumber}", "{rlt}");

       var rlte: Boolean = vShort <= vNumber ;
       if (gen)javafx.lang.FX.print("\nvar reslteShortWithNumber: Boolean={rlte};");       
       assertEquals("{res.reslteShortWithNumber}", "{rlte}");

       var rgt: Boolean = vShort > vNumber ;
       if (gen)javafx.lang.FX.print("\nvar resgtShortWithNumber: Boolean={rgt};");       
       assertEquals("{res.resgtShortWithNumber}", "{rgt}");

       var rgte: Boolean = vShort >= vNumber ;
       if (gen)javafx.lang.FX.print("\nvar resgteShortWithNumber: Boolean={rgte};");       
       assertEquals("{res.resgteShortWithNumber}", "{rgte}");

       if (gen)javafx.lang.FX.print("\n//");
}
    var xCharacter: Character[] = vCharacter;  
    function testOperCharacterWithByte() { 
       var radd: Number = vCharacter + vByte ;
       if (gen)javafx.lang.FX.print("\nvar resaddCharacterWithByte: Number={radd};");       
       assertEquals("{res.resaddCharacterWithByte}", "{radd}");

       var fadd: String = foo(vCharacter + vByte) ;
       if (gen)javafx.lang.FX.print("\nvar resaddCharacterWithByte_f: String=\"{fadd}\";");       
       assertEquals("{res.resaddCharacterWithByte_f}", "{fadd}");
       var rsub: Number = vCharacter - vByte ;
       if (gen)javafx.lang.FX.print("\nvar ressubCharacterWithByte: Number={rsub};");       
       assertEquals("{res.ressubCharacterWithByte}", "{rsub}");

       var fsub: String = foo(vCharacter - vByte) ;
       if (gen)javafx.lang.FX.print("\nvar ressubCharacterWithByte_f: String=\"{fsub}\";");       
       assertEquals("{res.ressubCharacterWithByte_f}", "{fsub}");
       var rmul: Number = vCharacter * vByte ;
       if (gen)javafx.lang.FX.print("\nvar resmulCharacterWithByte: Number={rmul};");       
       assertEquals("{res.resmulCharacterWithByte}", "{rmul}");

       var fmul: String = foo(vCharacter * vByte) ;
       if (gen)javafx.lang.FX.print("\nvar resmulCharacterWithByte_f: String=\"{fmul}\";");       
       assertEquals("{res.resmulCharacterWithByte_f}", "{fmul}");
       var rdiv: Number = vCharacter / vByte ;
       if (gen)javafx.lang.FX.print("\nvar resdivCharacterWithByte: Number={rdiv};");       
       assertEquals("{res.resdivCharacterWithByte}", "{rdiv}");

       var fdiv: String = foo(vCharacter / vByte) ;
       if (gen)javafx.lang.FX.print("\nvar resdivCharacterWithByte_f: String=\"{fdiv}\";");       
       assertEquals("{res.resdivCharacterWithByte_f}", "{fdiv}");
       var rmod: Number = vCharacter mod vByte ;
       if (gen)javafx.lang.FX.print("\nvar resmodCharacterWithByte: Number={rmod};");       
       assertEquals("{res.resmodCharacterWithByte}", "{rmod}");

       var fmod: String = foo(vCharacter mod vByte) ;
       if (gen)javafx.lang.FX.print("\nvar resmodCharacterWithByte_f: String=\"{fmod}\";");       
       assertEquals("{res.resmodCharacterWithByte_f}", "{fmod}");
       var req: Boolean = vCharacter == vByte ;
       if (gen)javafx.lang.FX.print("\nvar reseqCharacterWithByte: Boolean={req};");       
       assertEquals("{res.reseqCharacterWithByte}", "{req}");

       var rneq: Boolean = vCharacter != vByte ;
       if (gen)javafx.lang.FX.print("\nvar resneqCharacterWithByte: Boolean={rneq};");       
       assertEquals("{res.resneqCharacterWithByte}", "{rneq}");

       var rlt: Boolean = vCharacter < vByte ;
       if (gen)javafx.lang.FX.print("\nvar resltCharacterWithByte: Boolean={rlt};");       
       assertEquals("{res.resltCharacterWithByte}", "{rlt}");

       var rlte: Boolean = vCharacter <= vByte ;
       if (gen)javafx.lang.FX.print("\nvar reslteCharacterWithByte: Boolean={rlte};");       
       assertEquals("{res.reslteCharacterWithByte}", "{rlte}");

       var rgt: Boolean = vCharacter > vByte ;
       if (gen)javafx.lang.FX.print("\nvar resgtCharacterWithByte: Boolean={rgt};");       
       assertEquals("{res.resgtCharacterWithByte}", "{rgt}");

       var rgte: Boolean = vCharacter >= vByte ;
       if (gen)javafx.lang.FX.print("\nvar resgteCharacterWithByte: Boolean={rgte};");       
       assertEquals("{res.resgteCharacterWithByte}", "{rgte}");

       if (gen)javafx.lang.FX.print("\n//");
}
    function testOperCharacterWithShort() { 
       var radd: Number = vCharacter + vShort ;
       if (gen)javafx.lang.FX.print("\nvar resaddCharacterWithShort: Number={radd};");       
       assertEquals("{res.resaddCharacterWithShort}", "{radd}");

       var fadd: String = foo(vCharacter + vShort) ;
       if (gen)javafx.lang.FX.print("\nvar resaddCharacterWithShort_f: String=\"{fadd}\";");       
       assertEquals("{res.resaddCharacterWithShort_f}", "{fadd}");
       var rsub: Number = vCharacter - vShort ;
       if (gen)javafx.lang.FX.print("\nvar ressubCharacterWithShort: Number={rsub};");       
       assertEquals("{res.ressubCharacterWithShort}", "{rsub}");

       var fsub: String = foo(vCharacter - vShort) ;
       if (gen)javafx.lang.FX.print("\nvar ressubCharacterWithShort_f: String=\"{fsub}\";");       
       assertEquals("{res.ressubCharacterWithShort_f}", "{fsub}");
       var rmul: Number = vCharacter * vShort ;
       if (gen)javafx.lang.FX.print("\nvar resmulCharacterWithShort: Number={rmul};");       
       assertEquals("{res.resmulCharacterWithShort}", "{rmul}");

       var fmul: String = foo(vCharacter * vShort) ;
       if (gen)javafx.lang.FX.print("\nvar resmulCharacterWithShort_f: String=\"{fmul}\";");       
       assertEquals("{res.resmulCharacterWithShort_f}", "{fmul}");
       var rdiv: Number = vCharacter / vShort ;
       if (gen)javafx.lang.FX.print("\nvar resdivCharacterWithShort: Number={rdiv};");       
       assertEquals("{res.resdivCharacterWithShort}", "{rdiv}");

       var fdiv: String = foo(vCharacter / vShort) ;
       if (gen)javafx.lang.FX.print("\nvar resdivCharacterWithShort_f: String=\"{fdiv}\";");       
       assertEquals("{res.resdivCharacterWithShort_f}", "{fdiv}");
       var rmod: Number = vCharacter mod vShort ;
       if (gen)javafx.lang.FX.print("\nvar resmodCharacterWithShort: Number={rmod};");       
       assertEquals("{res.resmodCharacterWithShort}", "{rmod}");

       var fmod: String = foo(vCharacter mod vShort) ;
       if (gen)javafx.lang.FX.print("\nvar resmodCharacterWithShort_f: String=\"{fmod}\";");       
       assertEquals("{res.resmodCharacterWithShort_f}", "{fmod}");
       var req: Boolean = vCharacter == vShort ;
       if (gen)javafx.lang.FX.print("\nvar reseqCharacterWithShort: Boolean={req};");       
       assertEquals("{res.reseqCharacterWithShort}", "{req}");

       var rneq: Boolean = vCharacter != vShort ;
       if (gen)javafx.lang.FX.print("\nvar resneqCharacterWithShort: Boolean={rneq};");       
       assertEquals("{res.resneqCharacterWithShort}", "{rneq}");

       var rlt: Boolean = vCharacter < vShort ;
       if (gen)javafx.lang.FX.print("\nvar resltCharacterWithShort: Boolean={rlt};");       
       assertEquals("{res.resltCharacterWithShort}", "{rlt}");

       var rlte: Boolean = vCharacter <= vShort ;
       if (gen)javafx.lang.FX.print("\nvar reslteCharacterWithShort: Boolean={rlte};");       
       assertEquals("{res.reslteCharacterWithShort}", "{rlte}");

       var rgt: Boolean = vCharacter > vShort ;
       if (gen)javafx.lang.FX.print("\nvar resgtCharacterWithShort: Boolean={rgt};");       
       assertEquals("{res.resgtCharacterWithShort}", "{rgt}");

       var rgte: Boolean = vCharacter >= vShort ;
       if (gen)javafx.lang.FX.print("\nvar resgteCharacterWithShort: Boolean={rgte};");       
       assertEquals("{res.resgteCharacterWithShort}", "{rgte}");

       if (gen)javafx.lang.FX.print("\n//");
}
    function testOperCharacterWithCharacter() { 
       var radd: Number = vCharacter + vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar resaddCharacterWithCharacter: Number={radd};");       
       assertEquals("{res.resaddCharacterWithCharacter}", "{radd}");

       var fadd: String = foo(vCharacter + vCharacter) ;
       if (gen)javafx.lang.FX.print("\nvar resaddCharacterWithCharacter_f: String=\"{fadd}\";");       
       assertEquals("{res.resaddCharacterWithCharacter_f}", "{fadd}");
       var rsub: Number = vCharacter - vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar ressubCharacterWithCharacter: Number={rsub};");       
       assertEquals("{res.ressubCharacterWithCharacter}", "{rsub}");

       var fsub: String = foo(vCharacter - vCharacter) ;
       if (gen)javafx.lang.FX.print("\nvar ressubCharacterWithCharacter_f: String=\"{fsub}\";");       
       assertEquals("{res.ressubCharacterWithCharacter_f}", "{fsub}");
       var rmul: Number = vCharacter * vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar resmulCharacterWithCharacter: Number={rmul};");       
       assertEquals("{res.resmulCharacterWithCharacter}", "{rmul}");

       var fmul: String = foo(vCharacter * vCharacter) ;
       if (gen)javafx.lang.FX.print("\nvar resmulCharacterWithCharacter_f: String=\"{fmul}\";");       
       assertEquals("{res.resmulCharacterWithCharacter_f}", "{fmul}");
       var rdiv: Number = vCharacter / vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar resdivCharacterWithCharacter: Number={rdiv};");       
       assertEquals("{res.resdivCharacterWithCharacter}", "{rdiv}");

       var fdiv: String = foo(vCharacter / vCharacter) ;
       if (gen)javafx.lang.FX.print("\nvar resdivCharacterWithCharacter_f: String=\"{fdiv}\";");       
       assertEquals("{res.resdivCharacterWithCharacter_f}", "{fdiv}");
       var rmod: Number = vCharacter mod vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar resmodCharacterWithCharacter: Number={rmod};");       
       assertEquals("{res.resmodCharacterWithCharacter}", "{rmod}");

       var fmod: String = foo(vCharacter mod vCharacter) ;
       if (gen)javafx.lang.FX.print("\nvar resmodCharacterWithCharacter_f: String=\"{fmod}\";");       
       assertEquals("{res.resmodCharacterWithCharacter_f}", "{fmod}");
       var req: Boolean = vCharacter == vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar reseqCharacterWithCharacter: Boolean={req};");       
       assertEquals("{res.reseqCharacterWithCharacter}", "{req}");

       var rneq: Boolean = vCharacter != vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar resneqCharacterWithCharacter: Boolean={rneq};");       
       assertEquals("{res.resneqCharacterWithCharacter}", "{rneq}");

       var rlt: Boolean = vCharacter < vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar resltCharacterWithCharacter: Boolean={rlt};");       
       assertEquals("{res.resltCharacterWithCharacter}", "{rlt}");

       var rlte: Boolean = vCharacter <= vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar reslteCharacterWithCharacter: Boolean={rlte};");       
       assertEquals("{res.reslteCharacterWithCharacter}", "{rlte}");

       var rgt: Boolean = vCharacter > vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar resgtCharacterWithCharacter: Boolean={rgt};");       
       assertEquals("{res.resgtCharacterWithCharacter}", "{rgt}");

       var rgte: Boolean = vCharacter >= vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar resgteCharacterWithCharacter: Boolean={rgte};");       
       assertEquals("{res.resgteCharacterWithCharacter}", "{rgte}");

       if (gen)javafx.lang.FX.print("\n//");
}
    function testOperCharacterWithInteger() { 
       var radd: Number = vCharacter + vInteger ;
       if (gen)javafx.lang.FX.print("\nvar resaddCharacterWithInteger: Number={radd};");       
       assertEquals("{res.resaddCharacterWithInteger}", "{radd}");

       var fadd: String = foo(vCharacter + vInteger) ;
       if (gen)javafx.lang.FX.print("\nvar resaddCharacterWithInteger_f: String=\"{fadd}\";");       
       assertEquals("{res.resaddCharacterWithInteger_f}", "{fadd}");
       var rsub: Number = vCharacter - vInteger ;
       if (gen)javafx.lang.FX.print("\nvar ressubCharacterWithInteger: Number={rsub};");       
       assertEquals("{res.ressubCharacterWithInteger}", "{rsub}");

       var fsub: String = foo(vCharacter - vInteger) ;
       if (gen)javafx.lang.FX.print("\nvar ressubCharacterWithInteger_f: String=\"{fsub}\";");       
       assertEquals("{res.ressubCharacterWithInteger_f}", "{fsub}");
       var rmul: Number = vCharacter * vInteger ;
       if (gen)javafx.lang.FX.print("\nvar resmulCharacterWithInteger: Number={rmul};");       
       assertEquals("{res.resmulCharacterWithInteger}", "{rmul}");

       var fmul: String = foo(vCharacter * vInteger) ;
       if (gen)javafx.lang.FX.print("\nvar resmulCharacterWithInteger_f: String=\"{fmul}\";");       
       assertEquals("{res.resmulCharacterWithInteger_f}", "{fmul}");
       var rdiv: Number = vCharacter / vInteger ;
       if (gen)javafx.lang.FX.print("\nvar resdivCharacterWithInteger: Number={rdiv};");       
       assertEquals("{res.resdivCharacterWithInteger}", "{rdiv}");

       var fdiv: String = foo(vCharacter / vInteger) ;
       if (gen)javafx.lang.FX.print("\nvar resdivCharacterWithInteger_f: String=\"{fdiv}\";");       
       assertEquals("{res.resdivCharacterWithInteger_f}", "{fdiv}");
       var rmod: Number = vCharacter mod vInteger ;
       if (gen)javafx.lang.FX.print("\nvar resmodCharacterWithInteger: Number={rmod};");       
       assertEquals("{res.resmodCharacterWithInteger}", "{rmod}");

       var fmod: String = foo(vCharacter mod vInteger) ;
       if (gen)javafx.lang.FX.print("\nvar resmodCharacterWithInteger_f: String=\"{fmod}\";");       
       assertEquals("{res.resmodCharacterWithInteger_f}", "{fmod}");
       var req: Boolean = vCharacter == vInteger ;
       if (gen)javafx.lang.FX.print("\nvar reseqCharacterWithInteger: Boolean={req};");       
       assertEquals("{res.reseqCharacterWithInteger}", "{req}");

       var rneq: Boolean = vCharacter != vInteger ;
       if (gen)javafx.lang.FX.print("\nvar resneqCharacterWithInteger: Boolean={rneq};");       
       assertEquals("{res.resneqCharacterWithInteger}", "{rneq}");

       var rlt: Boolean = vCharacter < vInteger ;
       if (gen)javafx.lang.FX.print("\nvar resltCharacterWithInteger: Boolean={rlt};");       
       assertEquals("{res.resltCharacterWithInteger}", "{rlt}");

       var rlte: Boolean = vCharacter <= vInteger ;
       if (gen)javafx.lang.FX.print("\nvar reslteCharacterWithInteger: Boolean={rlte};");       
       assertEquals("{res.reslteCharacterWithInteger}", "{rlte}");

       var rgt: Boolean = vCharacter > vInteger ;
       if (gen)javafx.lang.FX.print("\nvar resgtCharacterWithInteger: Boolean={rgt};");       
       assertEquals("{res.resgtCharacterWithInteger}", "{rgt}");

       var rgte: Boolean = vCharacter >= vInteger ;
       if (gen)javafx.lang.FX.print("\nvar resgteCharacterWithInteger: Boolean={rgte};");       
       assertEquals("{res.resgteCharacterWithInteger}", "{rgte}");

       if (gen)javafx.lang.FX.print("\n//");
}
    function testOperCharacterWithLong() { 
       var radd: Number = vCharacter + vLong ;
       if (gen)javafx.lang.FX.print("\nvar resaddCharacterWithLong: Number={radd};");       
       assertEquals("{res.resaddCharacterWithLong}", "{radd}");

       var fadd: String = foo(vCharacter + vLong) ;
       if (gen)javafx.lang.FX.print("\nvar resaddCharacterWithLong_f: String=\"{fadd}\";");       
       assertEquals("{res.resaddCharacterWithLong_f}", "{fadd}");
       var rsub: Number = vCharacter - vLong ;
       if (gen)javafx.lang.FX.print("\nvar ressubCharacterWithLong: Number={rsub};");       
       assertEquals("{res.ressubCharacterWithLong}", "{rsub}");

       var fsub: String = foo(vCharacter - vLong) ;
       if (gen)javafx.lang.FX.print("\nvar ressubCharacterWithLong_f: String=\"{fsub}\";");       
       assertEquals("{res.ressubCharacterWithLong_f}", "{fsub}");
       var rmul: Number = vCharacter * vLong ;
       if (gen)javafx.lang.FX.print("\nvar resmulCharacterWithLong: Number={rmul};");       
       assertEquals("{res.resmulCharacterWithLong}", "{rmul}");

       var fmul: String = foo(vCharacter * vLong) ;
       if (gen)javafx.lang.FX.print("\nvar resmulCharacterWithLong_f: String=\"{fmul}\";");       
       assertEquals("{res.resmulCharacterWithLong_f}", "{fmul}");
       var rdiv: Number = vCharacter / vLong ;
       if (gen)javafx.lang.FX.print("\nvar resdivCharacterWithLong: Number={rdiv};");       
       assertEquals("{res.resdivCharacterWithLong}", "{rdiv}");

       var fdiv: String = foo(vCharacter / vLong) ;
       if (gen)javafx.lang.FX.print("\nvar resdivCharacterWithLong_f: String=\"{fdiv}\";");       
       assertEquals("{res.resdivCharacterWithLong_f}", "{fdiv}");
       var rmod: Number = vCharacter mod vLong ;
       if (gen)javafx.lang.FX.print("\nvar resmodCharacterWithLong: Number={rmod};");       
       assertEquals("{res.resmodCharacterWithLong}", "{rmod}");

       var fmod: String = foo(vCharacter mod vLong) ;
       if (gen)javafx.lang.FX.print("\nvar resmodCharacterWithLong_f: String=\"{fmod}\";");       
       assertEquals("{res.resmodCharacterWithLong_f}", "{fmod}");
       var req: Boolean = vCharacter == vLong ;
       if (gen)javafx.lang.FX.print("\nvar reseqCharacterWithLong: Boolean={req};");       
       assertEquals("{res.reseqCharacterWithLong}", "{req}");

       var rneq: Boolean = vCharacter != vLong ;
       if (gen)javafx.lang.FX.print("\nvar resneqCharacterWithLong: Boolean={rneq};");       
       assertEquals("{res.resneqCharacterWithLong}", "{rneq}");

       var rlt: Boolean = vCharacter < vLong ;
       if (gen)javafx.lang.FX.print("\nvar resltCharacterWithLong: Boolean={rlt};");       
       assertEquals("{res.resltCharacterWithLong}", "{rlt}");

       var rlte: Boolean = vCharacter <= vLong ;
       if (gen)javafx.lang.FX.print("\nvar reslteCharacterWithLong: Boolean={rlte};");       
       assertEquals("{res.reslteCharacterWithLong}", "{rlte}");

       var rgt: Boolean = vCharacter > vLong ;
       if (gen)javafx.lang.FX.print("\nvar resgtCharacterWithLong: Boolean={rgt};");       
       assertEquals("{res.resgtCharacterWithLong}", "{rgt}");

       var rgte: Boolean = vCharacter >= vLong ;
       if (gen)javafx.lang.FX.print("\nvar resgteCharacterWithLong: Boolean={rgte};");       
       assertEquals("{res.resgteCharacterWithLong}", "{rgte}");

       if (gen)javafx.lang.FX.print("\n//");
}
    function testOperCharacterWithFloat() { 
       var radd: Number = vCharacter + vFloat ;
       if (gen)javafx.lang.FX.print("\nvar resaddCharacterWithFloat: Number={radd};");       
       assertEquals("{res.resaddCharacterWithFloat}", "{radd}");

       var fadd: String = foo(vCharacter + vFloat) ;
       if (gen)javafx.lang.FX.print("\nvar resaddCharacterWithFloat_f: String=\"{fadd}\";");       
       assertEquals("{res.resaddCharacterWithFloat_f}", "{fadd}");
       var rsub: Number = vCharacter - vFloat ;
       if (gen)javafx.lang.FX.print("\nvar ressubCharacterWithFloat: Number={rsub};");       
       assertEquals("{res.ressubCharacterWithFloat}", "{rsub}");

       var fsub: String = foo(vCharacter - vFloat) ;
       if (gen)javafx.lang.FX.print("\nvar ressubCharacterWithFloat_f: String=\"{fsub}\";");       
       assertEquals("{res.ressubCharacterWithFloat_f}", "{fsub}");
       var rmul: Number = vCharacter * vFloat ;
       if (gen)javafx.lang.FX.print("\nvar resmulCharacterWithFloat: Number={rmul};");       
       assertEquals("{res.resmulCharacterWithFloat}", "{rmul}");

       var fmul: String = foo(vCharacter * vFloat) ;
       if (gen)javafx.lang.FX.print("\nvar resmulCharacterWithFloat_f: String=\"{fmul}\";");       
       assertEquals("{res.resmulCharacterWithFloat_f}", "{fmul}");
       var rdiv: Number = vCharacter / vFloat ;
       if (gen)javafx.lang.FX.print("\nvar resdivCharacterWithFloat: Number={rdiv};");       
       assertEquals("{res.resdivCharacterWithFloat}", "{rdiv}");

       var fdiv: String = foo(vCharacter / vFloat) ;
       if (gen)javafx.lang.FX.print("\nvar resdivCharacterWithFloat_f: String=\"{fdiv}\";");       
       assertEquals("{res.resdivCharacterWithFloat_f}", "{fdiv}");
       var rmod: Number = vCharacter mod vFloat ;
       if (gen)javafx.lang.FX.print("\nvar resmodCharacterWithFloat: Number={rmod};");       
       assertEquals("{res.resmodCharacterWithFloat}", "{rmod}");

       var fmod: String = foo(vCharacter mod vFloat) ;
       if (gen)javafx.lang.FX.print("\nvar resmodCharacterWithFloat_f: String=\"{fmod}\";");       
       assertEquals("{res.resmodCharacterWithFloat_f}", "{fmod}");
       var req: Boolean = vCharacter == vFloat ;
       if (gen)javafx.lang.FX.print("\nvar reseqCharacterWithFloat: Boolean={req};");       
       assertEquals("{res.reseqCharacterWithFloat}", "{req}");

       var rneq: Boolean = vCharacter != vFloat ;
       if (gen)javafx.lang.FX.print("\nvar resneqCharacterWithFloat: Boolean={rneq};");       
       assertEquals("{res.resneqCharacterWithFloat}", "{rneq}");

       var rlt: Boolean = vCharacter < vFloat ;
       if (gen)javafx.lang.FX.print("\nvar resltCharacterWithFloat: Boolean={rlt};");       
       assertEquals("{res.resltCharacterWithFloat}", "{rlt}");

       var rlte: Boolean = vCharacter <= vFloat ;
       if (gen)javafx.lang.FX.print("\nvar reslteCharacterWithFloat: Boolean={rlte};");       
       assertEquals("{res.reslteCharacterWithFloat}", "{rlte}");

       var rgt: Boolean = vCharacter > vFloat ;
       if (gen)javafx.lang.FX.print("\nvar resgtCharacterWithFloat: Boolean={rgt};");       
       assertEquals("{res.resgtCharacterWithFloat}", "{rgt}");

       var rgte: Boolean = vCharacter >= vFloat ;
       if (gen)javafx.lang.FX.print("\nvar resgteCharacterWithFloat: Boolean={rgte};");       
       assertEquals("{res.resgteCharacterWithFloat}", "{rgte}");

       if (gen)javafx.lang.FX.print("\n//");
}
    function testOperCharacterWithDouble() { 
       var radd: Number = vCharacter + vDouble ;
       if (gen)javafx.lang.FX.print("\nvar resaddCharacterWithDouble: Number={radd};");       
       assertEquals("{res.resaddCharacterWithDouble}", "{radd}");

       var fadd: String = foo(vCharacter + vDouble) ;
       if (gen)javafx.lang.FX.print("\nvar resaddCharacterWithDouble_f: String=\"{fadd}\";");       
       assertEquals("{res.resaddCharacterWithDouble_f}", "{fadd}");
       var rsub: Number = vCharacter - vDouble ;
       if (gen)javafx.lang.FX.print("\nvar ressubCharacterWithDouble: Number={rsub};");       
       assertEquals("{res.ressubCharacterWithDouble}", "{rsub}");

       var fsub: String = foo(vCharacter - vDouble) ;
       if (gen)javafx.lang.FX.print("\nvar ressubCharacterWithDouble_f: String=\"{fsub}\";");       
       assertEquals("{res.ressubCharacterWithDouble_f}", "{fsub}");
       var rmul: Number = vCharacter * vDouble ;
       if (gen)javafx.lang.FX.print("\nvar resmulCharacterWithDouble: Number={rmul};");       
       assertEquals("{res.resmulCharacterWithDouble}", "{rmul}");

       var fmul: String = foo(vCharacter * vDouble) ;
       if (gen)javafx.lang.FX.print("\nvar resmulCharacterWithDouble_f: String=\"{fmul}\";");       
       assertEquals("{res.resmulCharacterWithDouble_f}", "{fmul}");
       var rdiv: Number = vCharacter / vDouble ;
       if (gen)javafx.lang.FX.print("\nvar resdivCharacterWithDouble: Number={rdiv};");       
       assertEquals("{res.resdivCharacterWithDouble}", "{rdiv}");

       var fdiv: String = foo(vCharacter / vDouble) ;
       if (gen)javafx.lang.FX.print("\nvar resdivCharacterWithDouble_f: String=\"{fdiv}\";");       
       assertEquals("{res.resdivCharacterWithDouble_f}", "{fdiv}");
       var rmod: Number = vCharacter mod vDouble ;
       if (gen)javafx.lang.FX.print("\nvar resmodCharacterWithDouble: Number={rmod};");       
       assertEquals("{res.resmodCharacterWithDouble}", "{rmod}");

       var fmod: String = foo(vCharacter mod vDouble) ;
       if (gen)javafx.lang.FX.print("\nvar resmodCharacterWithDouble_f: String=\"{fmod}\";");       
       assertEquals("{res.resmodCharacterWithDouble_f}", "{fmod}");
       var req: Boolean = vCharacter == vDouble ;
       if (gen)javafx.lang.FX.print("\nvar reseqCharacterWithDouble: Boolean={req};");       
       assertEquals("{res.reseqCharacterWithDouble}", "{req}");

       var rneq: Boolean = vCharacter != vDouble ;
       if (gen)javafx.lang.FX.print("\nvar resneqCharacterWithDouble: Boolean={rneq};");       
       assertEquals("{res.resneqCharacterWithDouble}", "{rneq}");

       var rlt: Boolean = vCharacter < vDouble ;
       if (gen)javafx.lang.FX.print("\nvar resltCharacterWithDouble: Boolean={rlt};");       
       assertEquals("{res.resltCharacterWithDouble}", "{rlt}");

       var rlte: Boolean = vCharacter <= vDouble ;
       if (gen)javafx.lang.FX.print("\nvar reslteCharacterWithDouble: Boolean={rlte};");       
       assertEquals("{res.reslteCharacterWithDouble}", "{rlte}");

       var rgt: Boolean = vCharacter > vDouble ;
       if (gen)javafx.lang.FX.print("\nvar resgtCharacterWithDouble: Boolean={rgt};");       
       assertEquals("{res.resgtCharacterWithDouble}", "{rgt}");

       var rgte: Boolean = vCharacter >= vDouble ;
       if (gen)javafx.lang.FX.print("\nvar resgteCharacterWithDouble: Boolean={rgte};");       
       assertEquals("{res.resgteCharacterWithDouble}", "{rgte}");

       if (gen)javafx.lang.FX.print("\n//");
}
    function testOperCharacterWithNumber() { 
       var radd: Number = vCharacter + vNumber ;
       if (gen)javafx.lang.FX.print("\nvar resaddCharacterWithNumber: Number={radd};");       
       assertEquals("{res.resaddCharacterWithNumber}", "{radd}");

       var fadd: String = foo(vCharacter + vNumber) ;
       if (gen)javafx.lang.FX.print("\nvar resaddCharacterWithNumber_f: String=\"{fadd}\";");       
       assertEquals("{res.resaddCharacterWithNumber_f}", "{fadd}");
       var rsub: Number = vCharacter - vNumber ;
       if (gen)javafx.lang.FX.print("\nvar ressubCharacterWithNumber: Number={rsub};");       
       assertEquals("{res.ressubCharacterWithNumber}", "{rsub}");

       var fsub: String = foo(vCharacter - vNumber) ;
       if (gen)javafx.lang.FX.print("\nvar ressubCharacterWithNumber_f: String=\"{fsub}\";");       
       assertEquals("{res.ressubCharacterWithNumber_f}", "{fsub}");
       var rmul: Number = vCharacter * vNumber ;
       if (gen)javafx.lang.FX.print("\nvar resmulCharacterWithNumber: Number={rmul};");       
       assertEquals("{res.resmulCharacterWithNumber}", "{rmul}");

       var fmul: String = foo(vCharacter * vNumber) ;
       if (gen)javafx.lang.FX.print("\nvar resmulCharacterWithNumber_f: String=\"{fmul}\";");       
       assertEquals("{res.resmulCharacterWithNumber_f}", "{fmul}");
       var rdiv: Number = vCharacter / vNumber ;
       if (gen)javafx.lang.FX.print("\nvar resdivCharacterWithNumber: Number={rdiv};");       
       assertEquals("{res.resdivCharacterWithNumber}", "{rdiv}");

       var fdiv: String = foo(vCharacter / vNumber) ;
       if (gen)javafx.lang.FX.print("\nvar resdivCharacterWithNumber_f: String=\"{fdiv}\";");       
       assertEquals("{res.resdivCharacterWithNumber_f}", "{fdiv}");
       var rmod: Number = vCharacter mod vNumber ;
       if (gen)javafx.lang.FX.print("\nvar resmodCharacterWithNumber: Number={rmod};");       
       assertEquals("{res.resmodCharacterWithNumber}", "{rmod}");

       var fmod: String = foo(vCharacter mod vNumber) ;
       if (gen)javafx.lang.FX.print("\nvar resmodCharacterWithNumber_f: String=\"{fmod}\";");       
       assertEquals("{res.resmodCharacterWithNumber_f}", "{fmod}");
       var req: Boolean = vCharacter == vNumber ;
       if (gen)javafx.lang.FX.print("\nvar reseqCharacterWithNumber: Boolean={req};");       
       assertEquals("{res.reseqCharacterWithNumber}", "{req}");

       var rneq: Boolean = vCharacter != vNumber ;
       if (gen)javafx.lang.FX.print("\nvar resneqCharacterWithNumber: Boolean={rneq};");       
       assertEquals("{res.resneqCharacterWithNumber}", "{rneq}");

       var rlt: Boolean = vCharacter < vNumber ;
       if (gen)javafx.lang.FX.print("\nvar resltCharacterWithNumber: Boolean={rlt};");       
       assertEquals("{res.resltCharacterWithNumber}", "{rlt}");

       var rlte: Boolean = vCharacter <= vNumber ;
       if (gen)javafx.lang.FX.print("\nvar reslteCharacterWithNumber: Boolean={rlte};");       
       assertEquals("{res.reslteCharacterWithNumber}", "{rlte}");

       var rgt: Boolean = vCharacter > vNumber ;
       if (gen)javafx.lang.FX.print("\nvar resgtCharacterWithNumber: Boolean={rgt};");       
       assertEquals("{res.resgtCharacterWithNumber}", "{rgt}");

       var rgte: Boolean = vCharacter >= vNumber ;
       if (gen)javafx.lang.FX.print("\nvar resgteCharacterWithNumber: Boolean={rgte};");       
       assertEquals("{res.resgteCharacterWithNumber}", "{rgte}");

       if (gen)javafx.lang.FX.print("\n//");
}
    var xInteger: Integer[] = vInteger;  
    function testOperIntegerWithByte() { 
       var radd: Number = vInteger + vByte ;
       if (gen)javafx.lang.FX.print("\nvar resaddIntegerWithByte: Number={radd};");       
       assertEquals("{res.resaddIntegerWithByte}", "{radd}");

       var fadd: String = foo(vInteger + vByte) ;
       if (gen)javafx.lang.FX.print("\nvar resaddIntegerWithByte_f: String=\"{fadd}\";");       
       assertEquals("{res.resaddIntegerWithByte_f}", "{fadd}");
       var rsub: Number = vInteger - vByte ;
       if (gen)javafx.lang.FX.print("\nvar ressubIntegerWithByte: Number={rsub};");       
       assertEquals("{res.ressubIntegerWithByte}", "{rsub}");

       var fsub: String = foo(vInteger - vByte) ;
       if (gen)javafx.lang.FX.print("\nvar ressubIntegerWithByte_f: String=\"{fsub}\";");       
       assertEquals("{res.ressubIntegerWithByte_f}", "{fsub}");
       var rmul: Number = vInteger * vByte ;
       if (gen)javafx.lang.FX.print("\nvar resmulIntegerWithByte: Number={rmul};");       
       assertEquals("{res.resmulIntegerWithByte}", "{rmul}");

       var fmul: String = foo(vInteger * vByte) ;
       if (gen)javafx.lang.FX.print("\nvar resmulIntegerWithByte_f: String=\"{fmul}\";");       
       assertEquals("{res.resmulIntegerWithByte_f}", "{fmul}");
       var rdiv: Number = vInteger / vByte ;
       if (gen)javafx.lang.FX.print("\nvar resdivIntegerWithByte: Number={rdiv};");       
       assertEquals("{res.resdivIntegerWithByte}", "{rdiv}");

       var fdiv: String = foo(vInteger / vByte) ;
       if (gen)javafx.lang.FX.print("\nvar resdivIntegerWithByte_f: String=\"{fdiv}\";");       
       assertEquals("{res.resdivIntegerWithByte_f}", "{fdiv}");
       var rmod: Number = vInteger mod vByte ;
       if (gen)javafx.lang.FX.print("\nvar resmodIntegerWithByte: Number={rmod};");       
       assertEquals("{res.resmodIntegerWithByte}", "{rmod}");

       var fmod: String = foo(vInteger mod vByte) ;
       if (gen)javafx.lang.FX.print("\nvar resmodIntegerWithByte_f: String=\"{fmod}\";");       
       assertEquals("{res.resmodIntegerWithByte_f}", "{fmod}");
       var req: Boolean = vInteger == vByte ;
       if (gen)javafx.lang.FX.print("\nvar reseqIntegerWithByte: Boolean={req};");       
       assertEquals("{res.reseqIntegerWithByte}", "{req}");

       var rneq: Boolean = vInteger != vByte ;
       if (gen)javafx.lang.FX.print("\nvar resneqIntegerWithByte: Boolean={rneq};");       
       assertEquals("{res.resneqIntegerWithByte}", "{rneq}");

       var rlt: Boolean = vInteger < vByte ;
       if (gen)javafx.lang.FX.print("\nvar resltIntegerWithByte: Boolean={rlt};");       
       assertEquals("{res.resltIntegerWithByte}", "{rlt}");

       var rlte: Boolean = vInteger <= vByte ;
       if (gen)javafx.lang.FX.print("\nvar reslteIntegerWithByte: Boolean={rlte};");       
       assertEquals("{res.reslteIntegerWithByte}", "{rlte}");

       var rgt: Boolean = vInteger > vByte ;
       if (gen)javafx.lang.FX.print("\nvar resgtIntegerWithByte: Boolean={rgt};");       
       assertEquals("{res.resgtIntegerWithByte}", "{rgt}");

       var rgte: Boolean = vInteger >= vByte ;
       if (gen)javafx.lang.FX.print("\nvar resgteIntegerWithByte: Boolean={rgte};");       
       assertEquals("{res.resgteIntegerWithByte}", "{rgte}");

       if (gen)javafx.lang.FX.print("\n//");
}
    function testOperIntegerWithShort() { 
       var radd: Number = vInteger + vShort ;
       if (gen)javafx.lang.FX.print("\nvar resaddIntegerWithShort: Number={radd};");       
       assertEquals("{res.resaddIntegerWithShort}", "{radd}");

       var fadd: String = foo(vInteger + vShort) ;
       if (gen)javafx.lang.FX.print("\nvar resaddIntegerWithShort_f: String=\"{fadd}\";");       
       assertEquals("{res.resaddIntegerWithShort_f}", "{fadd}");
       var rsub: Number = vInteger - vShort ;
       if (gen)javafx.lang.FX.print("\nvar ressubIntegerWithShort: Number={rsub};");       
       assertEquals("{res.ressubIntegerWithShort}", "{rsub}");

       var fsub: String = foo(vInteger - vShort) ;
       if (gen)javafx.lang.FX.print("\nvar ressubIntegerWithShort_f: String=\"{fsub}\";");       
       assertEquals("{res.ressubIntegerWithShort_f}", "{fsub}");
       var rmul: Number = vInteger * vShort ;
       if (gen)javafx.lang.FX.print("\nvar resmulIntegerWithShort: Number={rmul};");       
       assertEquals("{res.resmulIntegerWithShort}", "{rmul}");

       var fmul: String = foo(vInteger * vShort) ;
       if (gen)javafx.lang.FX.print("\nvar resmulIntegerWithShort_f: String=\"{fmul}\";");       
       assertEquals("{res.resmulIntegerWithShort_f}", "{fmul}");
       var rdiv: Number = vInteger / vShort ;
       if (gen)javafx.lang.FX.print("\nvar resdivIntegerWithShort: Number={rdiv};");       
       assertEquals("{res.resdivIntegerWithShort}", "{rdiv}");

       var fdiv: String = foo(vInteger / vShort) ;
       if (gen)javafx.lang.FX.print("\nvar resdivIntegerWithShort_f: String=\"{fdiv}\";");       
       assertEquals("{res.resdivIntegerWithShort_f}", "{fdiv}");
       var rmod: Number = vInteger mod vShort ;
       if (gen)javafx.lang.FX.print("\nvar resmodIntegerWithShort: Number={rmod};");       
       assertEquals("{res.resmodIntegerWithShort}", "{rmod}");

       var fmod: String = foo(vInteger mod vShort) ;
       if (gen)javafx.lang.FX.print("\nvar resmodIntegerWithShort_f: String=\"{fmod}\";");       
       assertEquals("{res.resmodIntegerWithShort_f}", "{fmod}");
       var req: Boolean = vInteger == vShort ;
       if (gen)javafx.lang.FX.print("\nvar reseqIntegerWithShort: Boolean={req};");       
       assertEquals("{res.reseqIntegerWithShort}", "{req}");

       var rneq: Boolean = vInteger != vShort ;
       if (gen)javafx.lang.FX.print("\nvar resneqIntegerWithShort: Boolean={rneq};");       
       assertEquals("{res.resneqIntegerWithShort}", "{rneq}");

       var rlt: Boolean = vInteger < vShort ;
       if (gen)javafx.lang.FX.print("\nvar resltIntegerWithShort: Boolean={rlt};");       
       assertEquals("{res.resltIntegerWithShort}", "{rlt}");

       var rlte: Boolean = vInteger <= vShort ;
       if (gen)javafx.lang.FX.print("\nvar reslteIntegerWithShort: Boolean={rlte};");       
       assertEquals("{res.reslteIntegerWithShort}", "{rlte}");

       var rgt: Boolean = vInteger > vShort ;
       if (gen)javafx.lang.FX.print("\nvar resgtIntegerWithShort: Boolean={rgt};");       
       assertEquals("{res.resgtIntegerWithShort}", "{rgt}");

       var rgte: Boolean = vInteger >= vShort ;
       if (gen)javafx.lang.FX.print("\nvar resgteIntegerWithShort: Boolean={rgte};");       
       assertEquals("{res.resgteIntegerWithShort}", "{rgte}");

       if (gen)javafx.lang.FX.print("\n//");
}
    function testOperIntegerWithCharacter() { 
       var radd: Number = vInteger + vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar resaddIntegerWithCharacter: Number={radd};");       
       assertEquals("{res.resaddIntegerWithCharacter}", "{radd}");

       var fadd: String = foo(vInteger + vCharacter) ;
       if (gen)javafx.lang.FX.print("\nvar resaddIntegerWithCharacter_f: String=\"{fadd}\";");       
       assertEquals("{res.resaddIntegerWithCharacter_f}", "{fadd}");
       var rsub: Number = vInteger - vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar ressubIntegerWithCharacter: Number={rsub};");       
       assertEquals("{res.ressubIntegerWithCharacter}", "{rsub}");

       var fsub: String = foo(vInteger - vCharacter) ;
       if (gen)javafx.lang.FX.print("\nvar ressubIntegerWithCharacter_f: String=\"{fsub}\";");       
       assertEquals("{res.ressubIntegerWithCharacter_f}", "{fsub}");
       var rmul: Number = vInteger * vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar resmulIntegerWithCharacter: Number={rmul};");       
       assertEquals("{res.resmulIntegerWithCharacter}", "{rmul}");

       var fmul: String = foo(vInteger * vCharacter) ;
       if (gen)javafx.lang.FX.print("\nvar resmulIntegerWithCharacter_f: String=\"{fmul}\";");       
       assertEquals("{res.resmulIntegerWithCharacter_f}", "{fmul}");
       var rdiv: Number = vInteger / vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar resdivIntegerWithCharacter: Number={rdiv};");       
       assertEquals("{res.resdivIntegerWithCharacter}", "{rdiv}");

       var fdiv: String = foo(vInteger / vCharacter) ;
       if (gen)javafx.lang.FX.print("\nvar resdivIntegerWithCharacter_f: String=\"{fdiv}\";");       
       assertEquals("{res.resdivIntegerWithCharacter_f}", "{fdiv}");
       var rmod: Number = vInteger mod vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar resmodIntegerWithCharacter: Number={rmod};");       
       assertEquals("{res.resmodIntegerWithCharacter}", "{rmod}");

       var fmod: String = foo(vInteger mod vCharacter) ;
       if (gen)javafx.lang.FX.print("\nvar resmodIntegerWithCharacter_f: String=\"{fmod}\";");       
       assertEquals("{res.resmodIntegerWithCharacter_f}", "{fmod}");
       var req: Boolean = vInteger == vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar reseqIntegerWithCharacter: Boolean={req};");       
       assertEquals("{res.reseqIntegerWithCharacter}", "{req}");

       var rneq: Boolean = vInteger != vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar resneqIntegerWithCharacter: Boolean={rneq};");       
       assertEquals("{res.resneqIntegerWithCharacter}", "{rneq}");

       var rlt: Boolean = vInteger < vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar resltIntegerWithCharacter: Boolean={rlt};");       
       assertEquals("{res.resltIntegerWithCharacter}", "{rlt}");

       var rlte: Boolean = vInteger <= vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar reslteIntegerWithCharacter: Boolean={rlte};");       
       assertEquals("{res.reslteIntegerWithCharacter}", "{rlte}");

       var rgt: Boolean = vInteger > vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar resgtIntegerWithCharacter: Boolean={rgt};");       
       assertEquals("{res.resgtIntegerWithCharacter}", "{rgt}");

       var rgte: Boolean = vInteger >= vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar resgteIntegerWithCharacter: Boolean={rgte};");       
       assertEquals("{res.resgteIntegerWithCharacter}", "{rgte}");

       if (gen)javafx.lang.FX.print("\n//");
}
    function testOperIntegerWithInteger() { 
       var radd: Number = vInteger + vInteger ;
       if (gen)javafx.lang.FX.print("\nvar resaddIntegerWithInteger: Number={radd};");       
       assertEquals("{res.resaddIntegerWithInteger}", "{radd}");

       var fadd: String = foo(vInteger + vInteger) ;
       if (gen)javafx.lang.FX.print("\nvar resaddIntegerWithInteger_f: String=\"{fadd}\";");       
       assertEquals("{res.resaddIntegerWithInteger_f}", "{fadd}");
       var rsub: Number = vInteger - vInteger ;
       if (gen)javafx.lang.FX.print("\nvar ressubIntegerWithInteger: Number={rsub};");       
       assertEquals("{res.ressubIntegerWithInteger}", "{rsub}");

       var fsub: String = foo(vInteger - vInteger) ;
       if (gen)javafx.lang.FX.print("\nvar ressubIntegerWithInteger_f: String=\"{fsub}\";");       
       assertEquals("{res.ressubIntegerWithInteger_f}", "{fsub}");
       var rmul: Number = vInteger * vInteger ;
       if (gen)javafx.lang.FX.print("\nvar resmulIntegerWithInteger: Number={rmul};");       
       assertEquals("{res.resmulIntegerWithInteger}", "{rmul}");

       var fmul: String = foo(vInteger * vInteger) ;
       if (gen)javafx.lang.FX.print("\nvar resmulIntegerWithInteger_f: String=\"{fmul}\";");       
       assertEquals("{res.resmulIntegerWithInteger_f}", "{fmul}");
       var rdiv: Number = vInteger / vInteger ;
       if (gen)javafx.lang.FX.print("\nvar resdivIntegerWithInteger: Number={rdiv};");       
       assertEquals("{res.resdivIntegerWithInteger}", "{rdiv}");

       var fdiv: String = foo(vInteger / vInteger) ;
       if (gen)javafx.lang.FX.print("\nvar resdivIntegerWithInteger_f: String=\"{fdiv}\";");       
       assertEquals("{res.resdivIntegerWithInteger_f}", "{fdiv}");
       var rmod: Number = vInteger mod vInteger ;
       if (gen)javafx.lang.FX.print("\nvar resmodIntegerWithInteger: Number={rmod};");       
       assertEquals("{res.resmodIntegerWithInteger}", "{rmod}");

       var fmod: String = foo(vInteger mod vInteger) ;
       if (gen)javafx.lang.FX.print("\nvar resmodIntegerWithInteger_f: String=\"{fmod}\";");       
       assertEquals("{res.resmodIntegerWithInteger_f}", "{fmod}");
       var req: Boolean = vInteger == vInteger ;
       if (gen)javafx.lang.FX.print("\nvar reseqIntegerWithInteger: Boolean={req};");       
       assertEquals("{res.reseqIntegerWithInteger}", "{req}");

       var rneq: Boolean = vInteger != vInteger ;
       if (gen)javafx.lang.FX.print("\nvar resneqIntegerWithInteger: Boolean={rneq};");       
       assertEquals("{res.resneqIntegerWithInteger}", "{rneq}");

       var rlt: Boolean = vInteger < vInteger ;
       if (gen)javafx.lang.FX.print("\nvar resltIntegerWithInteger: Boolean={rlt};");       
       assertEquals("{res.resltIntegerWithInteger}", "{rlt}");

       var rlte: Boolean = vInteger <= vInteger ;
       if (gen)javafx.lang.FX.print("\nvar reslteIntegerWithInteger: Boolean={rlte};");       
       assertEquals("{res.reslteIntegerWithInteger}", "{rlte}");

       var rgt: Boolean = vInteger > vInteger ;
       if (gen)javafx.lang.FX.print("\nvar resgtIntegerWithInteger: Boolean={rgt};");       
       assertEquals("{res.resgtIntegerWithInteger}", "{rgt}");

       var rgte: Boolean = vInteger >= vInteger ;
       if (gen)javafx.lang.FX.print("\nvar resgteIntegerWithInteger: Boolean={rgte};");       
       assertEquals("{res.resgteIntegerWithInteger}", "{rgte}");

       if (gen)javafx.lang.FX.print("\n//");
}
    function testOperIntegerWithLong() { 
       var radd: Number = vInteger + vLong ;
       if (gen)javafx.lang.FX.print("\nvar resaddIntegerWithLong: Number={radd};");       
       assertEquals("{res.resaddIntegerWithLong}", "{radd}");

       var fadd: String = foo(vInteger + vLong) ;
       if (gen)javafx.lang.FX.print("\nvar resaddIntegerWithLong_f: String=\"{fadd}\";");       
       assertEquals("{res.resaddIntegerWithLong_f}", "{fadd}");
       var rsub: Number = vInteger - vLong ;
       if (gen)javafx.lang.FX.print("\nvar ressubIntegerWithLong: Number={rsub};");       
       assertEquals("{res.ressubIntegerWithLong}", "{rsub}");

       var fsub: String = foo(vInteger - vLong) ;
       if (gen)javafx.lang.FX.print("\nvar ressubIntegerWithLong_f: String=\"{fsub}\";");       
       assertEquals("{res.ressubIntegerWithLong_f}", "{fsub}");
       var rmul: Number = vInteger * vLong ;
       if (gen)javafx.lang.FX.print("\nvar resmulIntegerWithLong: Number={rmul};");       
       assertEquals("{res.resmulIntegerWithLong}", "{rmul}");

       var fmul: String = foo(vInteger * vLong) ;
       if (gen)javafx.lang.FX.print("\nvar resmulIntegerWithLong_f: String=\"{fmul}\";");       
       assertEquals("{res.resmulIntegerWithLong_f}", "{fmul}");
       var rdiv: Number = vInteger / vLong ;
       if (gen)javafx.lang.FX.print("\nvar resdivIntegerWithLong: Number={rdiv};");       
       assertEquals("{res.resdivIntegerWithLong}", "{rdiv}");

       var fdiv: String = foo(vInteger / vLong) ;
       if (gen)javafx.lang.FX.print("\nvar resdivIntegerWithLong_f: String=\"{fdiv}\";");       
       assertEquals("{res.resdivIntegerWithLong_f}", "{fdiv}");
       var rmod: Number = vInteger mod vLong ;
       if (gen)javafx.lang.FX.print("\nvar resmodIntegerWithLong: Number={rmod};");       
       assertEquals("{res.resmodIntegerWithLong}", "{rmod}");

       var fmod: String = foo(vInteger mod vLong) ;
       if (gen)javafx.lang.FX.print("\nvar resmodIntegerWithLong_f: String=\"{fmod}\";");       
       assertEquals("{res.resmodIntegerWithLong_f}", "{fmod}");
       var req: Boolean = vInteger == vLong ;
       if (gen)javafx.lang.FX.print("\nvar reseqIntegerWithLong: Boolean={req};");       
       assertEquals("{res.reseqIntegerWithLong}", "{req}");

       var rneq: Boolean = vInteger != vLong ;
       if (gen)javafx.lang.FX.print("\nvar resneqIntegerWithLong: Boolean={rneq};");       
       assertEquals("{res.resneqIntegerWithLong}", "{rneq}");

       var rlt: Boolean = vInteger < vLong ;
       if (gen)javafx.lang.FX.print("\nvar resltIntegerWithLong: Boolean={rlt};");       
       assertEquals("{res.resltIntegerWithLong}", "{rlt}");

       var rlte: Boolean = vInteger <= vLong ;
       if (gen)javafx.lang.FX.print("\nvar reslteIntegerWithLong: Boolean={rlte};");       
       assertEquals("{res.reslteIntegerWithLong}", "{rlte}");

       var rgt: Boolean = vInteger > vLong ;
       if (gen)javafx.lang.FX.print("\nvar resgtIntegerWithLong: Boolean={rgt};");       
       assertEquals("{res.resgtIntegerWithLong}", "{rgt}");

       var rgte: Boolean = vInteger >= vLong ;
       if (gen)javafx.lang.FX.print("\nvar resgteIntegerWithLong: Boolean={rgte};");       
       assertEquals("{res.resgteIntegerWithLong}", "{rgte}");

       if (gen)javafx.lang.FX.print("\n//");
}
    function testOperIntegerWithFloat() { 
       var radd: Number = vInteger + vFloat ;
       if (gen)javafx.lang.FX.print("\nvar resaddIntegerWithFloat: Number={radd};");       
       assertEquals("{res.resaddIntegerWithFloat}", "{radd}");

       var fadd: String = foo(vInteger + vFloat) ;
       if (gen)javafx.lang.FX.print("\nvar resaddIntegerWithFloat_f: String=\"{fadd}\";");       
       assertEquals("{res.resaddIntegerWithFloat_f}", "{fadd}");
       var rsub: Number = vInteger - vFloat ;
       if (gen)javafx.lang.FX.print("\nvar ressubIntegerWithFloat: Number={rsub};");       
       assertEquals("{res.ressubIntegerWithFloat}", "{rsub}");

       var fsub: String = foo(vInteger - vFloat) ;
       if (gen)javafx.lang.FX.print("\nvar ressubIntegerWithFloat_f: String=\"{fsub}\";");       
       assertEquals("{res.ressubIntegerWithFloat_f}", "{fsub}");
       var rmul: Number = vInteger * vFloat ;
       if (gen)javafx.lang.FX.print("\nvar resmulIntegerWithFloat: Number={rmul};");       
       assertEquals("{res.resmulIntegerWithFloat}", "{rmul}");

       var fmul: String = foo(vInteger * vFloat) ;
       if (gen)javafx.lang.FX.print("\nvar resmulIntegerWithFloat_f: String=\"{fmul}\";");       
       assertEquals("{res.resmulIntegerWithFloat_f}", "{fmul}");
       var rdiv: Number = vInteger / vFloat ;
       if (gen)javafx.lang.FX.print("\nvar resdivIntegerWithFloat: Number={rdiv};");       
       assertEquals("{res.resdivIntegerWithFloat}", "{rdiv}");

       var fdiv: String = foo(vInteger / vFloat) ;
       if (gen)javafx.lang.FX.print("\nvar resdivIntegerWithFloat_f: String=\"{fdiv}\";");       
       assertEquals("{res.resdivIntegerWithFloat_f}", "{fdiv}");
       var rmod: Number = vInteger mod vFloat ;
       if (gen)javafx.lang.FX.print("\nvar resmodIntegerWithFloat: Number={rmod};");       
       assertEquals("{res.resmodIntegerWithFloat}", "{rmod}");

       var fmod: String = foo(vInteger mod vFloat) ;
       if (gen)javafx.lang.FX.print("\nvar resmodIntegerWithFloat_f: String=\"{fmod}\";");       
       assertEquals("{res.resmodIntegerWithFloat_f}", "{fmod}");
       var req: Boolean = vInteger == vFloat ;
       if (gen)javafx.lang.FX.print("\nvar reseqIntegerWithFloat: Boolean={req};");       
       assertEquals("{res.reseqIntegerWithFloat}", "{req}");

       var rneq: Boolean = vInteger != vFloat ;
       if (gen)javafx.lang.FX.print("\nvar resneqIntegerWithFloat: Boolean={rneq};");       
       assertEquals("{res.resneqIntegerWithFloat}", "{rneq}");

       var rlt: Boolean = vInteger < vFloat ;
       if (gen)javafx.lang.FX.print("\nvar resltIntegerWithFloat: Boolean={rlt};");       
       assertEquals("{res.resltIntegerWithFloat}", "{rlt}");

       var rlte: Boolean = vInteger <= vFloat ;
       if (gen)javafx.lang.FX.print("\nvar reslteIntegerWithFloat: Boolean={rlte};");       
       assertEquals("{res.reslteIntegerWithFloat}", "{rlte}");

       var rgt: Boolean = vInteger > vFloat ;
       if (gen)javafx.lang.FX.print("\nvar resgtIntegerWithFloat: Boolean={rgt};");       
       assertEquals("{res.resgtIntegerWithFloat}", "{rgt}");

       var rgte: Boolean = vInteger >= vFloat ;
       if (gen)javafx.lang.FX.print("\nvar resgteIntegerWithFloat: Boolean={rgte};");       
       assertEquals("{res.resgteIntegerWithFloat}", "{rgte}");

       if (gen)javafx.lang.FX.print("\n//");
}
    function testOperIntegerWithDouble() { 
       var radd: Number = vInteger + vDouble ;
       if (gen)javafx.lang.FX.print("\nvar resaddIntegerWithDouble: Number={radd};");       
       assertEquals("{res.resaddIntegerWithDouble}", "{radd}");

       var fadd: String = foo(vInteger + vDouble) ;
       if (gen)javafx.lang.FX.print("\nvar resaddIntegerWithDouble_f: String=\"{fadd}\";");       
       assertEquals("{res.resaddIntegerWithDouble_f}", "{fadd}");
       var rsub: Number = vInteger - vDouble ;
       if (gen)javafx.lang.FX.print("\nvar ressubIntegerWithDouble: Number={rsub};");       
       assertEquals("{res.ressubIntegerWithDouble}", "{rsub}");

       var fsub: String = foo(vInteger - vDouble) ;
       if (gen)javafx.lang.FX.print("\nvar ressubIntegerWithDouble_f: String=\"{fsub}\";");       
       assertEquals("{res.ressubIntegerWithDouble_f}", "{fsub}");
       var rmul: Number = vInteger * vDouble ;
       if (gen)javafx.lang.FX.print("\nvar resmulIntegerWithDouble: Number={rmul};");       
       assertEquals("{res.resmulIntegerWithDouble}", "{rmul}");

       var fmul: String = foo(vInteger * vDouble) ;
       if (gen)javafx.lang.FX.print("\nvar resmulIntegerWithDouble_f: String=\"{fmul}\";");       
       assertEquals("{res.resmulIntegerWithDouble_f}", "{fmul}");
       var rdiv: Number = vInteger / vDouble ;
       if (gen)javafx.lang.FX.print("\nvar resdivIntegerWithDouble: Number={rdiv};");       
       assertEquals("{res.resdivIntegerWithDouble}", "{rdiv}");

       var fdiv: String = foo(vInteger / vDouble) ;
       if (gen)javafx.lang.FX.print("\nvar resdivIntegerWithDouble_f: String=\"{fdiv}\";");       
       assertEquals("{res.resdivIntegerWithDouble_f}", "{fdiv}");
       var rmod: Number = vInteger mod vDouble ;
       if (gen)javafx.lang.FX.print("\nvar resmodIntegerWithDouble: Number={rmod};");       
       assertEquals("{res.resmodIntegerWithDouble}", "{rmod}");

       var fmod: String = foo(vInteger mod vDouble) ;
       if (gen)javafx.lang.FX.print("\nvar resmodIntegerWithDouble_f: String=\"{fmod}\";");       
       assertEquals("{res.resmodIntegerWithDouble_f}", "{fmod}");
       var req: Boolean = vInteger == vDouble ;
       if (gen)javafx.lang.FX.print("\nvar reseqIntegerWithDouble: Boolean={req};");       
       assertEquals("{res.reseqIntegerWithDouble}", "{req}");

       var rneq: Boolean = vInteger != vDouble ;
       if (gen)javafx.lang.FX.print("\nvar resneqIntegerWithDouble: Boolean={rneq};");       
       assertEquals("{res.resneqIntegerWithDouble}", "{rneq}");

       var rlt: Boolean = vInteger < vDouble ;
       if (gen)javafx.lang.FX.print("\nvar resltIntegerWithDouble: Boolean={rlt};");       
       assertEquals("{res.resltIntegerWithDouble}", "{rlt}");

       var rlte: Boolean = vInteger <= vDouble ;
       if (gen)javafx.lang.FX.print("\nvar reslteIntegerWithDouble: Boolean={rlte};");       
       assertEquals("{res.reslteIntegerWithDouble}", "{rlte}");

       var rgt: Boolean = vInteger > vDouble ;
       if (gen)javafx.lang.FX.print("\nvar resgtIntegerWithDouble: Boolean={rgt};");       
       assertEquals("{res.resgtIntegerWithDouble}", "{rgt}");

       var rgte: Boolean = vInteger >= vDouble ;
       if (gen)javafx.lang.FX.print("\nvar resgteIntegerWithDouble: Boolean={rgte};");       
       assertEquals("{res.resgteIntegerWithDouble}", "{rgte}");

       if (gen)javafx.lang.FX.print("\n//");
}
    function testOperIntegerWithNumber() { 
       var radd: Number = vInteger + vNumber ;
       if (gen)javafx.lang.FX.print("\nvar resaddIntegerWithNumber: Number={radd};");       
       assertEquals("{res.resaddIntegerWithNumber}", "{radd}");

       var fadd: String = foo(vInteger + vNumber) ;
       if (gen)javafx.lang.FX.print("\nvar resaddIntegerWithNumber_f: String=\"{fadd}\";");       
       assertEquals("{res.resaddIntegerWithNumber_f}", "{fadd}");
       var rsub: Number = vInteger - vNumber ;
       if (gen)javafx.lang.FX.print("\nvar ressubIntegerWithNumber: Number={rsub};");       
       assertEquals("{res.ressubIntegerWithNumber}", "{rsub}");

       var fsub: String = foo(vInteger - vNumber) ;
       if (gen)javafx.lang.FX.print("\nvar ressubIntegerWithNumber_f: String=\"{fsub}\";");       
       assertEquals("{res.ressubIntegerWithNumber_f}", "{fsub}");
       var rmul: Number = vInteger * vNumber ;
       if (gen)javafx.lang.FX.print("\nvar resmulIntegerWithNumber: Number={rmul};");       
       assertEquals("{res.resmulIntegerWithNumber}", "{rmul}");

       var fmul: String = foo(vInteger * vNumber) ;
       if (gen)javafx.lang.FX.print("\nvar resmulIntegerWithNumber_f: String=\"{fmul}\";");       
       assertEquals("{res.resmulIntegerWithNumber_f}", "{fmul}");
       var rdiv: Number = vInteger / vNumber ;
       if (gen)javafx.lang.FX.print("\nvar resdivIntegerWithNumber: Number={rdiv};");       
       assertEquals("{res.resdivIntegerWithNumber}", "{rdiv}");

       var fdiv: String = foo(vInteger / vNumber) ;
       if (gen)javafx.lang.FX.print("\nvar resdivIntegerWithNumber_f: String=\"{fdiv}\";");       
       assertEquals("{res.resdivIntegerWithNumber_f}", "{fdiv}");
       var rmod: Number = vInteger mod vNumber ;
       if (gen)javafx.lang.FX.print("\nvar resmodIntegerWithNumber: Number={rmod};");       
       assertEquals("{res.resmodIntegerWithNumber}", "{rmod}");

       var fmod: String = foo(vInteger mod vNumber) ;
       if (gen)javafx.lang.FX.print("\nvar resmodIntegerWithNumber_f: String=\"{fmod}\";");       
       assertEquals("{res.resmodIntegerWithNumber_f}", "{fmod}");
       var req: Boolean = vInteger == vNumber ;
       if (gen)javafx.lang.FX.print("\nvar reseqIntegerWithNumber: Boolean={req};");       
       assertEquals("{res.reseqIntegerWithNumber}", "{req}");

       var rneq: Boolean = vInteger != vNumber ;
       if (gen)javafx.lang.FX.print("\nvar resneqIntegerWithNumber: Boolean={rneq};");       
       assertEquals("{res.resneqIntegerWithNumber}", "{rneq}");

       var rlt: Boolean = vInteger < vNumber ;
       if (gen)javafx.lang.FX.print("\nvar resltIntegerWithNumber: Boolean={rlt};");       
       assertEquals("{res.resltIntegerWithNumber}", "{rlt}");

       var rlte: Boolean = vInteger <= vNumber ;
       if (gen)javafx.lang.FX.print("\nvar reslteIntegerWithNumber: Boolean={rlte};");       
       assertEquals("{res.reslteIntegerWithNumber}", "{rlte}");

       var rgt: Boolean = vInteger > vNumber ;
       if (gen)javafx.lang.FX.print("\nvar resgtIntegerWithNumber: Boolean={rgt};");       
       assertEquals("{res.resgtIntegerWithNumber}", "{rgt}");

       var rgte: Boolean = vInteger >= vNumber ;
       if (gen)javafx.lang.FX.print("\nvar resgteIntegerWithNumber: Boolean={rgte};");       
       assertEquals("{res.resgteIntegerWithNumber}", "{rgte}");

       if (gen)javafx.lang.FX.print("\n//");
}
    var xLong: Long[] = vLong;  
    function testOperLongWithByte() { 
       var radd: Number = vLong + vByte ;
       if (gen)javafx.lang.FX.print("\nvar resaddLongWithByte: Number={radd};");       
       assertEquals("{res.resaddLongWithByte}", "{radd}");

       var fadd: String = foo(vLong + vByte) ;
       if (gen)javafx.lang.FX.print("\nvar resaddLongWithByte_f: String=\"{fadd}\";");       
       assertEquals("{res.resaddLongWithByte_f}", "{fadd}");
       var rsub: Number = vLong - vByte ;
       if (gen)javafx.lang.FX.print("\nvar ressubLongWithByte: Number={rsub};");       
       assertEquals("{res.ressubLongWithByte}", "{rsub}");

       var fsub: String = foo(vLong - vByte) ;
       if (gen)javafx.lang.FX.print("\nvar ressubLongWithByte_f: String=\"{fsub}\";");       
       assertEquals("{res.ressubLongWithByte_f}", "{fsub}");
       var rmul: Number = vLong * vByte ;
       if (gen)javafx.lang.FX.print("\nvar resmulLongWithByte: Number={rmul};");       
       assertEquals("{res.resmulLongWithByte}", "{rmul}");

       var fmul: String = foo(vLong * vByte) ;
       if (gen)javafx.lang.FX.print("\nvar resmulLongWithByte_f: String=\"{fmul}\";");       
       assertEquals("{res.resmulLongWithByte_f}", "{fmul}");
       var rdiv: Number = vLong / vByte ;
       if (gen)javafx.lang.FX.print("\nvar resdivLongWithByte: Number={rdiv};");       
       assertEquals("{res.resdivLongWithByte}", "{rdiv}");

       var fdiv: String = foo(vLong / vByte) ;
       if (gen)javafx.lang.FX.print("\nvar resdivLongWithByte_f: String=\"{fdiv}\";");       
       assertEquals("{res.resdivLongWithByte_f}", "{fdiv}");
       var rmod: Number = vLong mod vByte ;
       if (gen)javafx.lang.FX.print("\nvar resmodLongWithByte: Number={rmod};");       
       assertEquals("{res.resmodLongWithByte}", "{rmod}");

       var fmod: String = foo(vLong mod vByte) ;
       if (gen)javafx.lang.FX.print("\nvar resmodLongWithByte_f: String=\"{fmod}\";");       
       assertEquals("{res.resmodLongWithByte_f}", "{fmod}");
       var req: Boolean = vLong == vByte ;
       if (gen)javafx.lang.FX.print("\nvar reseqLongWithByte: Boolean={req};");       
       assertEquals("{res.reseqLongWithByte}", "{req}");

       var rneq: Boolean = vLong != vByte ;
       if (gen)javafx.lang.FX.print("\nvar resneqLongWithByte: Boolean={rneq};");       
       assertEquals("{res.resneqLongWithByte}", "{rneq}");

       var rlt: Boolean = vLong < vByte ;
       if (gen)javafx.lang.FX.print("\nvar resltLongWithByte: Boolean={rlt};");       
       assertEquals("{res.resltLongWithByte}", "{rlt}");

       var rlte: Boolean = vLong <= vByte ;
       if (gen)javafx.lang.FX.print("\nvar reslteLongWithByte: Boolean={rlte};");       
       assertEquals("{res.reslteLongWithByte}", "{rlte}");

       var rgt: Boolean = vLong > vByte ;
       if (gen)javafx.lang.FX.print("\nvar resgtLongWithByte: Boolean={rgt};");       
       assertEquals("{res.resgtLongWithByte}", "{rgt}");

       var rgte: Boolean = vLong >= vByte ;
       if (gen)javafx.lang.FX.print("\nvar resgteLongWithByte: Boolean={rgte};");       
       assertEquals("{res.resgteLongWithByte}", "{rgte}");

       if (gen)javafx.lang.FX.print("\n//");
}
    function testOperLongWithShort() { 
       var radd: Number = vLong + vShort ;
       if (gen)javafx.lang.FX.print("\nvar resaddLongWithShort: Number={radd};");       
       assertEquals("{res.resaddLongWithShort}", "{radd}");

       var fadd: String = foo(vLong + vShort) ;
       if (gen)javafx.lang.FX.print("\nvar resaddLongWithShort_f: String=\"{fadd}\";");       
       assertEquals("{res.resaddLongWithShort_f}", "{fadd}");
       var rsub: Number = vLong - vShort ;
       if (gen)javafx.lang.FX.print("\nvar ressubLongWithShort: Number={rsub};");       
       assertEquals("{res.ressubLongWithShort}", "{rsub}");

       var fsub: String = foo(vLong - vShort) ;
       if (gen)javafx.lang.FX.print("\nvar ressubLongWithShort_f: String=\"{fsub}\";");       
       assertEquals("{res.ressubLongWithShort_f}", "{fsub}");
       var rmul: Number = vLong * vShort ;
       if (gen)javafx.lang.FX.print("\nvar resmulLongWithShort: Number={rmul};");       
       assertEquals("{res.resmulLongWithShort}", "{rmul}");

       var fmul: String = foo(vLong * vShort) ;
       if (gen)javafx.lang.FX.print("\nvar resmulLongWithShort_f: String=\"{fmul}\";");       
       assertEquals("{res.resmulLongWithShort_f}", "{fmul}");
       var rdiv: Number = vLong / vShort ;
       if (gen)javafx.lang.FX.print("\nvar resdivLongWithShort: Number={rdiv};");       
       assertEquals("{res.resdivLongWithShort}", "{rdiv}");

       var fdiv: String = foo(vLong / vShort) ;
       if (gen)javafx.lang.FX.print("\nvar resdivLongWithShort_f: String=\"{fdiv}\";");       
       assertEquals("{res.resdivLongWithShort_f}", "{fdiv}");
       var rmod: Number = vLong mod vShort ;
       if (gen)javafx.lang.FX.print("\nvar resmodLongWithShort: Number={rmod};");       
       assertEquals("{res.resmodLongWithShort}", "{rmod}");

       var fmod: String = foo(vLong mod vShort) ;
       if (gen)javafx.lang.FX.print("\nvar resmodLongWithShort_f: String=\"{fmod}\";");       
       assertEquals("{res.resmodLongWithShort_f}", "{fmod}");
       var req: Boolean = vLong == vShort ;
       if (gen)javafx.lang.FX.print("\nvar reseqLongWithShort: Boolean={req};");       
       assertEquals("{res.reseqLongWithShort}", "{req}");

       var rneq: Boolean = vLong != vShort ;
       if (gen)javafx.lang.FX.print("\nvar resneqLongWithShort: Boolean={rneq};");       
       assertEquals("{res.resneqLongWithShort}", "{rneq}");

       var rlt: Boolean = vLong < vShort ;
       if (gen)javafx.lang.FX.print("\nvar resltLongWithShort: Boolean={rlt};");       
       assertEquals("{res.resltLongWithShort}", "{rlt}");

       var rlte: Boolean = vLong <= vShort ;
       if (gen)javafx.lang.FX.print("\nvar reslteLongWithShort: Boolean={rlte};");       
       assertEquals("{res.reslteLongWithShort}", "{rlte}");

       var rgt: Boolean = vLong > vShort ;
       if (gen)javafx.lang.FX.print("\nvar resgtLongWithShort: Boolean={rgt};");       
       assertEquals("{res.resgtLongWithShort}", "{rgt}");

       var rgte: Boolean = vLong >= vShort ;
       if (gen)javafx.lang.FX.print("\nvar resgteLongWithShort: Boolean={rgte};");       
       assertEquals("{res.resgteLongWithShort}", "{rgte}");

       if (gen)javafx.lang.FX.print("\n//");
}
    function testOperLongWithCharacter() { 
       var radd: Number = vLong + vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar resaddLongWithCharacter: Number={radd};");       
       assertEquals("{res.resaddLongWithCharacter}", "{radd}");

       var fadd: String = foo(vLong + vCharacter) ;
       if (gen)javafx.lang.FX.print("\nvar resaddLongWithCharacter_f: String=\"{fadd}\";");       
       assertEquals("{res.resaddLongWithCharacter_f}", "{fadd}");
       var rsub: Number = vLong - vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar ressubLongWithCharacter: Number={rsub};");       
       assertEquals("{res.ressubLongWithCharacter}", "{rsub}");

       var fsub: String = foo(vLong - vCharacter) ;
       if (gen)javafx.lang.FX.print("\nvar ressubLongWithCharacter_f: String=\"{fsub}\";");       
       assertEquals("{res.ressubLongWithCharacter_f}", "{fsub}");
       var rmul: Number = vLong * vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar resmulLongWithCharacter: Number={rmul};");       
       assertEquals("{res.resmulLongWithCharacter}", "{rmul}");

       var fmul: String = foo(vLong * vCharacter) ;
       if (gen)javafx.lang.FX.print("\nvar resmulLongWithCharacter_f: String=\"{fmul}\";");       
       assertEquals("{res.resmulLongWithCharacter_f}", "{fmul}");
       var rdiv: Number = vLong / vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar resdivLongWithCharacter: Number={rdiv};");       
       assertEquals("{res.resdivLongWithCharacter}", "{rdiv}");

       var fdiv: String = foo(vLong / vCharacter) ;
       if (gen)javafx.lang.FX.print("\nvar resdivLongWithCharacter_f: String=\"{fdiv}\";");       
       assertEquals("{res.resdivLongWithCharacter_f}", "{fdiv}");
       var rmod: Number = vLong mod vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar resmodLongWithCharacter: Number={rmod};");       
       assertEquals("{res.resmodLongWithCharacter}", "{rmod}");

       var fmod: String = foo(vLong mod vCharacter) ;
       if (gen)javafx.lang.FX.print("\nvar resmodLongWithCharacter_f: String=\"{fmod}\";");       
       assertEquals("{res.resmodLongWithCharacter_f}", "{fmod}");
       var req: Boolean = vLong == vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar reseqLongWithCharacter: Boolean={req};");       
       assertEquals("{res.reseqLongWithCharacter}", "{req}");

       var rneq: Boolean = vLong != vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar resneqLongWithCharacter: Boolean={rneq};");       
       assertEquals("{res.resneqLongWithCharacter}", "{rneq}");

       var rlt: Boolean = vLong < vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar resltLongWithCharacter: Boolean={rlt};");       
       assertEquals("{res.resltLongWithCharacter}", "{rlt}");

       var rlte: Boolean = vLong <= vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar reslteLongWithCharacter: Boolean={rlte};");       
       assertEquals("{res.reslteLongWithCharacter}", "{rlte}");

       var rgt: Boolean = vLong > vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar resgtLongWithCharacter: Boolean={rgt};");       
       assertEquals("{res.resgtLongWithCharacter}", "{rgt}");

       var rgte: Boolean = vLong >= vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar resgteLongWithCharacter: Boolean={rgte};");       
       assertEquals("{res.resgteLongWithCharacter}", "{rgte}");

       if (gen)javafx.lang.FX.print("\n//");
}
    function testOperLongWithInteger() { 
       var radd: Number = vLong + vInteger ;
       if (gen)javafx.lang.FX.print("\nvar resaddLongWithInteger: Number={radd};");       
       assertEquals("{res.resaddLongWithInteger}", "{radd}");

       var fadd: String = foo(vLong + vInteger) ;
       if (gen)javafx.lang.FX.print("\nvar resaddLongWithInteger_f: String=\"{fadd}\";");       
       assertEquals("{res.resaddLongWithInteger_f}", "{fadd}");
       var rsub: Number = vLong - vInteger ;
       if (gen)javafx.lang.FX.print("\nvar ressubLongWithInteger: Number={rsub};");       
       assertEquals("{res.ressubLongWithInteger}", "{rsub}");

       var fsub: String = foo(vLong - vInteger) ;
       if (gen)javafx.lang.FX.print("\nvar ressubLongWithInteger_f: String=\"{fsub}\";");       
       assertEquals("{res.ressubLongWithInteger_f}", "{fsub}");
       var rmul: Number = vLong * vInteger ;
       if (gen)javafx.lang.FX.print("\nvar resmulLongWithInteger: Number={rmul};");       
       assertEquals("{res.resmulLongWithInteger}", "{rmul}");

       var fmul: String = foo(vLong * vInteger) ;
       if (gen)javafx.lang.FX.print("\nvar resmulLongWithInteger_f: String=\"{fmul}\";");       
       assertEquals("{res.resmulLongWithInteger_f}", "{fmul}");
       var rdiv: Number = vLong / vInteger ;
       if (gen)javafx.lang.FX.print("\nvar resdivLongWithInteger: Number={rdiv};");       
       assertEquals("{res.resdivLongWithInteger}", "{rdiv}");

       var fdiv: String = foo(vLong / vInteger) ;
       if (gen)javafx.lang.FX.print("\nvar resdivLongWithInteger_f: String=\"{fdiv}\";");       
       assertEquals("{res.resdivLongWithInteger_f}", "{fdiv}");
       var rmod: Number = vLong mod vInteger ;
       if (gen)javafx.lang.FX.print("\nvar resmodLongWithInteger: Number={rmod};");       
       assertEquals("{res.resmodLongWithInteger}", "{rmod}");

       var fmod: String = foo(vLong mod vInteger) ;
       if (gen)javafx.lang.FX.print("\nvar resmodLongWithInteger_f: String=\"{fmod}\";");       
       assertEquals("{res.resmodLongWithInteger_f}", "{fmod}");
       var req: Boolean = vLong == vInteger ;
       if (gen)javafx.lang.FX.print("\nvar reseqLongWithInteger: Boolean={req};");       
       assertEquals("{res.reseqLongWithInteger}", "{req}");

       var rneq: Boolean = vLong != vInteger ;
       if (gen)javafx.lang.FX.print("\nvar resneqLongWithInteger: Boolean={rneq};");       
       assertEquals("{res.resneqLongWithInteger}", "{rneq}");

       var rlt: Boolean = vLong < vInteger ;
       if (gen)javafx.lang.FX.print("\nvar resltLongWithInteger: Boolean={rlt};");       
       assertEquals("{res.resltLongWithInteger}", "{rlt}");

       var rlte: Boolean = vLong <= vInteger ;
       if (gen)javafx.lang.FX.print("\nvar reslteLongWithInteger: Boolean={rlte};");       
       assertEquals("{res.reslteLongWithInteger}", "{rlte}");

       var rgt: Boolean = vLong > vInteger ;
       if (gen)javafx.lang.FX.print("\nvar resgtLongWithInteger: Boolean={rgt};");       
       assertEquals("{res.resgtLongWithInteger}", "{rgt}");

       var rgte: Boolean = vLong >= vInteger ;
       if (gen)javafx.lang.FX.print("\nvar resgteLongWithInteger: Boolean={rgte};");       
       assertEquals("{res.resgteLongWithInteger}", "{rgte}");

       if (gen)javafx.lang.FX.print("\n//");
}
    function testOperLongWithLong() { 
       var radd: Number = vLong + vLong ;
       if (gen)javafx.lang.FX.print("\nvar resaddLongWithLong: Number={radd};");       
       assertEquals("{res.resaddLongWithLong}", "{radd}");

       var fadd: String = foo(vLong + vLong) ;
       if (gen)javafx.lang.FX.print("\nvar resaddLongWithLong_f: String=\"{fadd}\";");       
       assertEquals("{res.resaddLongWithLong_f}", "{fadd}");
       var rsub: Number = vLong - vLong ;
       if (gen)javafx.lang.FX.print("\nvar ressubLongWithLong: Number={rsub};");       
       assertEquals("{res.ressubLongWithLong}", "{rsub}");

       var fsub: String = foo(vLong - vLong) ;
       if (gen)javafx.lang.FX.print("\nvar ressubLongWithLong_f: String=\"{fsub}\";");       
       assertEquals("{res.ressubLongWithLong_f}", "{fsub}");
       var rmul: Number = vLong * vLong ;
       if (gen)javafx.lang.FX.print("\nvar resmulLongWithLong: Number={rmul};");       
       assertEquals("{res.resmulLongWithLong}", "{rmul}");

       var fmul: String = foo(vLong * vLong) ;
       if (gen)javafx.lang.FX.print("\nvar resmulLongWithLong_f: String=\"{fmul}\";");       
       assertEquals("{res.resmulLongWithLong_f}", "{fmul}");
       var rdiv: Number = vLong / vLong ;
       if (gen)javafx.lang.FX.print("\nvar resdivLongWithLong: Number={rdiv};");       
       assertEquals("{res.resdivLongWithLong}", "{rdiv}");

       var fdiv: String = foo(vLong / vLong) ;
       if (gen)javafx.lang.FX.print("\nvar resdivLongWithLong_f: String=\"{fdiv}\";");       
       assertEquals("{res.resdivLongWithLong_f}", "{fdiv}");
       var rmod: Number = vLong mod vLong ;
       if (gen)javafx.lang.FX.print("\nvar resmodLongWithLong: Number={rmod};");       
       assertEquals("{res.resmodLongWithLong}", "{rmod}");

       var fmod: String = foo(vLong mod vLong) ;
       if (gen)javafx.lang.FX.print("\nvar resmodLongWithLong_f: String=\"{fmod}\";");       
       assertEquals("{res.resmodLongWithLong_f}", "{fmod}");
       var req: Boolean = vLong == vLong ;
       if (gen)javafx.lang.FX.print("\nvar reseqLongWithLong: Boolean={req};");       
       assertEquals("{res.reseqLongWithLong}", "{req}");

       var rneq: Boolean = vLong != vLong ;
       if (gen)javafx.lang.FX.print("\nvar resneqLongWithLong: Boolean={rneq};");       
       assertEquals("{res.resneqLongWithLong}", "{rneq}");

       var rlt: Boolean = vLong < vLong ;
       if (gen)javafx.lang.FX.print("\nvar resltLongWithLong: Boolean={rlt};");       
       assertEquals("{res.resltLongWithLong}", "{rlt}");

       var rlte: Boolean = vLong <= vLong ;
       if (gen)javafx.lang.FX.print("\nvar reslteLongWithLong: Boolean={rlte};");       
       assertEquals("{res.reslteLongWithLong}", "{rlte}");

       var rgt: Boolean = vLong > vLong ;
       if (gen)javafx.lang.FX.print("\nvar resgtLongWithLong: Boolean={rgt};");       
       assertEquals("{res.resgtLongWithLong}", "{rgt}");

       var rgte: Boolean = vLong >= vLong ;
       if (gen)javafx.lang.FX.print("\nvar resgteLongWithLong: Boolean={rgte};");       
       assertEquals("{res.resgteLongWithLong}", "{rgte}");

       if (gen)javafx.lang.FX.print("\n//");
}
    function testOperLongWithFloat() { 
       var radd: Number = vLong + vFloat ;
       if (gen)javafx.lang.FX.print("\nvar resaddLongWithFloat: Number={radd};");       
       assertEquals("{res.resaddLongWithFloat}", "{radd}");

       var fadd: String = foo(vLong + vFloat) ;
       if (gen)javafx.lang.FX.print("\nvar resaddLongWithFloat_f: String=\"{fadd}\";");       
       assertEquals("{res.resaddLongWithFloat_f}", "{fadd}");
       var rsub: Number = vLong - vFloat ;
       if (gen)javafx.lang.FX.print("\nvar ressubLongWithFloat: Number={rsub};");       
       assertEquals("{res.ressubLongWithFloat}", "{rsub}");

       var fsub: String = foo(vLong - vFloat) ;
       if (gen)javafx.lang.FX.print("\nvar ressubLongWithFloat_f: String=\"{fsub}\";");       
       assertEquals("{res.ressubLongWithFloat_f}", "{fsub}");
       var rmul: Number = vLong * vFloat ;
       if (gen)javafx.lang.FX.print("\nvar resmulLongWithFloat: Number={rmul};");       
       assertEquals("{res.resmulLongWithFloat}", "{rmul}");

       var fmul: String = foo(vLong * vFloat) ;
       if (gen)javafx.lang.FX.print("\nvar resmulLongWithFloat_f: String=\"{fmul}\";");       
       assertEquals("{res.resmulLongWithFloat_f}", "{fmul}");
       var rdiv: Number = vLong / vFloat ;
       if (gen)javafx.lang.FX.print("\nvar resdivLongWithFloat: Number={rdiv};");       
       assertEquals("{res.resdivLongWithFloat}", "{rdiv}");

       var fdiv: String = foo(vLong / vFloat) ;
       if (gen)javafx.lang.FX.print("\nvar resdivLongWithFloat_f: String=\"{fdiv}\";");       
       assertEquals("{res.resdivLongWithFloat_f}", "{fdiv}");
       var rmod: Number = vLong mod vFloat ;
       if (gen)javafx.lang.FX.print("\nvar resmodLongWithFloat: Number={rmod};");       
       assertEquals("{res.resmodLongWithFloat}", "{rmod}");

       var fmod: String = foo(vLong mod vFloat) ;
       if (gen)javafx.lang.FX.print("\nvar resmodLongWithFloat_f: String=\"{fmod}\";");       
       assertEquals("{res.resmodLongWithFloat_f}", "{fmod}");
       var req: Boolean = vLong == vFloat ;
       if (gen)javafx.lang.FX.print("\nvar reseqLongWithFloat: Boolean={req};");       
       assertEquals("{res.reseqLongWithFloat}", "{req}");

       var rneq: Boolean = vLong != vFloat ;
       if (gen)javafx.lang.FX.print("\nvar resneqLongWithFloat: Boolean={rneq};");       
       assertEquals("{res.resneqLongWithFloat}", "{rneq}");

       var rlt: Boolean = vLong < vFloat ;
       if (gen)javafx.lang.FX.print("\nvar resltLongWithFloat: Boolean={rlt};");       
       assertEquals("{res.resltLongWithFloat}", "{rlt}");

       var rlte: Boolean = vLong <= vFloat ;
       if (gen)javafx.lang.FX.print("\nvar reslteLongWithFloat: Boolean={rlte};");       
       assertEquals("{res.reslteLongWithFloat}", "{rlte}");

       var rgt: Boolean = vLong > vFloat ;
       if (gen)javafx.lang.FX.print("\nvar resgtLongWithFloat: Boolean={rgt};");       
       assertEquals("{res.resgtLongWithFloat}", "{rgt}");

       var rgte: Boolean = vLong >= vFloat ;
       if (gen)javafx.lang.FX.print("\nvar resgteLongWithFloat: Boolean={rgte};");       
       assertEquals("{res.resgteLongWithFloat}", "{rgte}");

       if (gen)javafx.lang.FX.print("\n//");
}
    function testOperLongWithDouble() { 
       var radd: Number = vLong + vDouble ;
       if (gen)javafx.lang.FX.print("\nvar resaddLongWithDouble: Number={radd};");       
       assertEquals("{res.resaddLongWithDouble}", "{radd}");

       var fadd: String = foo(vLong + vDouble) ;
       if (gen)javafx.lang.FX.print("\nvar resaddLongWithDouble_f: String=\"{fadd}\";");       
       assertEquals("{res.resaddLongWithDouble_f}", "{fadd}");
       var rsub: Number = vLong - vDouble ;
       if (gen)javafx.lang.FX.print("\nvar ressubLongWithDouble: Number={rsub};");       
       assertEquals("{res.ressubLongWithDouble}", "{rsub}");

       var fsub: String = foo(vLong - vDouble) ;
       if (gen)javafx.lang.FX.print("\nvar ressubLongWithDouble_f: String=\"{fsub}\";");       
       assertEquals("{res.ressubLongWithDouble_f}", "{fsub}");
       var rmul: Number = vLong * vDouble ;
       if (gen)javafx.lang.FX.print("\nvar resmulLongWithDouble: Number={rmul};");       
       assertEquals("{res.resmulLongWithDouble}", "{rmul}");

       var fmul: String = foo(vLong * vDouble) ;
       if (gen)javafx.lang.FX.print("\nvar resmulLongWithDouble_f: String=\"{fmul}\";");       
       assertEquals("{res.resmulLongWithDouble_f}", "{fmul}");
       var rdiv: Number = vLong / vDouble ;
       if (gen)javafx.lang.FX.print("\nvar resdivLongWithDouble: Number={rdiv};");       
       assertEquals("{res.resdivLongWithDouble}", "{rdiv}");

       var fdiv: String = foo(vLong / vDouble) ;
       if (gen)javafx.lang.FX.print("\nvar resdivLongWithDouble_f: String=\"{fdiv}\";");       
       assertEquals("{res.resdivLongWithDouble_f}", "{fdiv}");
       var rmod: Number = vLong mod vDouble ;
       if (gen)javafx.lang.FX.print("\nvar resmodLongWithDouble: Number={rmod};");       
       assertEquals("{res.resmodLongWithDouble}", "{rmod}");

       var fmod: String = foo(vLong mod vDouble) ;
       if (gen)javafx.lang.FX.print("\nvar resmodLongWithDouble_f: String=\"{fmod}\";");       
       assertEquals("{res.resmodLongWithDouble_f}", "{fmod}");
       var req: Boolean = vLong == vDouble ;
       if (gen)javafx.lang.FX.print("\nvar reseqLongWithDouble: Boolean={req};");       
       assertEquals("{res.reseqLongWithDouble}", "{req}");

       var rneq: Boolean = vLong != vDouble ;
       if (gen)javafx.lang.FX.print("\nvar resneqLongWithDouble: Boolean={rneq};");       
       assertEquals("{res.resneqLongWithDouble}", "{rneq}");

       var rlt: Boolean = vLong < vDouble ;
       if (gen)javafx.lang.FX.print("\nvar resltLongWithDouble: Boolean={rlt};");       
       assertEquals("{res.resltLongWithDouble}", "{rlt}");

       var rlte: Boolean = vLong <= vDouble ;
       if (gen)javafx.lang.FX.print("\nvar reslteLongWithDouble: Boolean={rlte};");       
       assertEquals("{res.reslteLongWithDouble}", "{rlte}");

       var rgt: Boolean = vLong > vDouble ;
       if (gen)javafx.lang.FX.print("\nvar resgtLongWithDouble: Boolean={rgt};");       
       assertEquals("{res.resgtLongWithDouble}", "{rgt}");

       var rgte: Boolean = vLong >= vDouble ;
       if (gen)javafx.lang.FX.print("\nvar resgteLongWithDouble: Boolean={rgte};");       
       assertEquals("{res.resgteLongWithDouble}", "{rgte}");

       if (gen)javafx.lang.FX.print("\n//");
}
    function testOperLongWithNumber() { 
       var radd: Number = vLong + vNumber ;
       if (gen)javafx.lang.FX.print("\nvar resaddLongWithNumber: Number={radd};");       
       assertEquals("{res.resaddLongWithNumber}", "{radd}");

       var fadd: String = foo(vLong + vNumber) ;
       if (gen)javafx.lang.FX.print("\nvar resaddLongWithNumber_f: String=\"{fadd}\";");       
       assertEquals("{res.resaddLongWithNumber_f}", "{fadd}");
       var rsub: Number = vLong - vNumber ;
       if (gen)javafx.lang.FX.print("\nvar ressubLongWithNumber: Number={rsub};");       
       assertEquals("{res.ressubLongWithNumber}", "{rsub}");

       var fsub: String = foo(vLong - vNumber) ;
       if (gen)javafx.lang.FX.print("\nvar ressubLongWithNumber_f: String=\"{fsub}\";");       
       assertEquals("{res.ressubLongWithNumber_f}", "{fsub}");
       var rmul: Number = vLong * vNumber ;
       if (gen)javafx.lang.FX.print("\nvar resmulLongWithNumber: Number={rmul};");       
       assertEquals("{res.resmulLongWithNumber}", "{rmul}");

       var fmul: String = foo(vLong * vNumber) ;
       if (gen)javafx.lang.FX.print("\nvar resmulLongWithNumber_f: String=\"{fmul}\";");       
       assertEquals("{res.resmulLongWithNumber_f}", "{fmul}");
       var rdiv: Number = vLong / vNumber ;
       if (gen)javafx.lang.FX.print("\nvar resdivLongWithNumber: Number={rdiv};");       
       assertEquals("{res.resdivLongWithNumber}", "{rdiv}");

       var fdiv: String = foo(vLong / vNumber) ;
       if (gen)javafx.lang.FX.print("\nvar resdivLongWithNumber_f: String=\"{fdiv}\";");       
       assertEquals("{res.resdivLongWithNumber_f}", "{fdiv}");
       var rmod: Number = vLong mod vNumber ;
       if (gen)javafx.lang.FX.print("\nvar resmodLongWithNumber: Number={rmod};");       
       assertEquals("{res.resmodLongWithNumber}", "{rmod}");

       var fmod: String = foo(vLong mod vNumber) ;
       if (gen)javafx.lang.FX.print("\nvar resmodLongWithNumber_f: String=\"{fmod}\";");       
       assertEquals("{res.resmodLongWithNumber_f}", "{fmod}");
       var req: Boolean = vLong == vNumber ;
       if (gen)javafx.lang.FX.print("\nvar reseqLongWithNumber: Boolean={req};");       
       assertEquals("{res.reseqLongWithNumber}", "{req}");

       var rneq: Boolean = vLong != vNumber ;
       if (gen)javafx.lang.FX.print("\nvar resneqLongWithNumber: Boolean={rneq};");       
       assertEquals("{res.resneqLongWithNumber}", "{rneq}");

       var rlt: Boolean = vLong < vNumber ;
       if (gen)javafx.lang.FX.print("\nvar resltLongWithNumber: Boolean={rlt};");       
       assertEquals("{res.resltLongWithNumber}", "{rlt}");

       var rlte: Boolean = vLong <= vNumber ;
       if (gen)javafx.lang.FX.print("\nvar reslteLongWithNumber: Boolean={rlte};");       
       assertEquals("{res.reslteLongWithNumber}", "{rlte}");

       var rgt: Boolean = vLong > vNumber ;
       if (gen)javafx.lang.FX.print("\nvar resgtLongWithNumber: Boolean={rgt};");       
       assertEquals("{res.resgtLongWithNumber}", "{rgt}");

       var rgte: Boolean = vLong >= vNumber ;
       if (gen)javafx.lang.FX.print("\nvar resgteLongWithNumber: Boolean={rgte};");       
       assertEquals("{res.resgteLongWithNumber}", "{rgte}");

       if (gen)javafx.lang.FX.print("\n//");
}
    var xFloat: Float[] = vFloat;  
    function testOperFloatWithByte() { 
       var radd: Number = vFloat + vByte ;
       if (gen)javafx.lang.FX.print("\nvar resaddFloatWithByte: Number={radd};");       
       assertEquals("{res.resaddFloatWithByte}", "{radd}");

       var fadd: String = foo(vFloat + vByte) ;
       if (gen)javafx.lang.FX.print("\nvar resaddFloatWithByte_f: String=\"{fadd}\";");       
       assertEquals("{res.resaddFloatWithByte_f}", "{fadd}");
       var rsub: Number = vFloat - vByte ;
       if (gen)javafx.lang.FX.print("\nvar ressubFloatWithByte: Number={rsub};");       
       assertEquals("{res.ressubFloatWithByte}", "{rsub}");

       var fsub: String = foo(vFloat - vByte) ;
       if (gen)javafx.lang.FX.print("\nvar ressubFloatWithByte_f: String=\"{fsub}\";");       
       assertEquals("{res.ressubFloatWithByte_f}", "{fsub}");
       var rmul: Number = vFloat * vByte ;
       if (gen)javafx.lang.FX.print("\nvar resmulFloatWithByte: Number={rmul};");       
       assertEquals("{res.resmulFloatWithByte}", "{rmul}");

       var fmul: String = foo(vFloat * vByte) ;
       if (gen)javafx.lang.FX.print("\nvar resmulFloatWithByte_f: String=\"{fmul}\";");       
       assertEquals("{res.resmulFloatWithByte_f}", "{fmul}");
       var rdiv: Number = vFloat / vByte ;
       if (gen)javafx.lang.FX.print("\nvar resdivFloatWithByte: Number={rdiv};");       
       assertEquals("{res.resdivFloatWithByte}", "{rdiv}");

       var fdiv: String = foo(vFloat / vByte) ;
       if (gen)javafx.lang.FX.print("\nvar resdivFloatWithByte_f: String=\"{fdiv}\";");       
       assertEquals("{res.resdivFloatWithByte_f}", "{fdiv}");
       var rmod: Number = vFloat mod vByte ;
       if (gen)javafx.lang.FX.print("\nvar resmodFloatWithByte: Number={rmod};");       
       assertEquals("{res.resmodFloatWithByte}", "{rmod}");

       var fmod: String = foo(vFloat mod vByte) ;
       if (gen)javafx.lang.FX.print("\nvar resmodFloatWithByte_f: String=\"{fmod}\";");       
       assertEquals("{res.resmodFloatWithByte_f}", "{fmod}");
       var req: Boolean = vFloat == vByte ;
       if (gen)javafx.lang.FX.print("\nvar reseqFloatWithByte: Boolean={req};");       
       assertEquals("{res.reseqFloatWithByte}", "{req}");

       var rneq: Boolean = vFloat != vByte ;
       if (gen)javafx.lang.FX.print("\nvar resneqFloatWithByte: Boolean={rneq};");       
       assertEquals("{res.resneqFloatWithByte}", "{rneq}");

       var rlt: Boolean = vFloat < vByte ;
       if (gen)javafx.lang.FX.print("\nvar resltFloatWithByte: Boolean={rlt};");       
       assertEquals("{res.resltFloatWithByte}", "{rlt}");

       var rlte: Boolean = vFloat <= vByte ;
       if (gen)javafx.lang.FX.print("\nvar reslteFloatWithByte: Boolean={rlte};");       
       assertEquals("{res.reslteFloatWithByte}", "{rlte}");

       var rgt: Boolean = vFloat > vByte ;
       if (gen)javafx.lang.FX.print("\nvar resgtFloatWithByte: Boolean={rgt};");       
       assertEquals("{res.resgtFloatWithByte}", "{rgt}");

       var rgte: Boolean = vFloat >= vByte ;
       if (gen)javafx.lang.FX.print("\nvar resgteFloatWithByte: Boolean={rgte};");       
       assertEquals("{res.resgteFloatWithByte}", "{rgte}");

       if (gen)javafx.lang.FX.print("\n//");
}
    function testOperFloatWithShort() { 
       var radd: Number = vFloat + vShort ;
       if (gen)javafx.lang.FX.print("\nvar resaddFloatWithShort: Number={radd};");       
       assertEquals("{res.resaddFloatWithShort}", "{radd}");

       var fadd: String = foo(vFloat + vShort) ;
       if (gen)javafx.lang.FX.print("\nvar resaddFloatWithShort_f: String=\"{fadd}\";");       
       assertEquals("{res.resaddFloatWithShort_f}", "{fadd}");
       var rsub: Number = vFloat - vShort ;
       if (gen)javafx.lang.FX.print("\nvar ressubFloatWithShort: Number={rsub};");       
       assertEquals("{res.ressubFloatWithShort}", "{rsub}");

       var fsub: String = foo(vFloat - vShort) ;
       if (gen)javafx.lang.FX.print("\nvar ressubFloatWithShort_f: String=\"{fsub}\";");       
       assertEquals("{res.ressubFloatWithShort_f}", "{fsub}");
       var rmul: Number = vFloat * vShort ;
       if (gen)javafx.lang.FX.print("\nvar resmulFloatWithShort: Number={rmul};");       
       assertEquals("{res.resmulFloatWithShort}", "{rmul}");

       var fmul: String = foo(vFloat * vShort) ;
       if (gen)javafx.lang.FX.print("\nvar resmulFloatWithShort_f: String=\"{fmul}\";");       
       assertEquals("{res.resmulFloatWithShort_f}", "{fmul}");
       var rdiv: Number = vFloat / vShort ;
       if (gen)javafx.lang.FX.print("\nvar resdivFloatWithShort: Number={rdiv};");       
       assertEquals("{res.resdivFloatWithShort}", "{rdiv}");

       var fdiv: String = foo(vFloat / vShort) ;
       if (gen)javafx.lang.FX.print("\nvar resdivFloatWithShort_f: String=\"{fdiv}\";");       
       assertEquals("{res.resdivFloatWithShort_f}", "{fdiv}");
       var rmod: Number = vFloat mod vShort ;
       if (gen)javafx.lang.FX.print("\nvar resmodFloatWithShort: Number={rmod};");       
       assertEquals("{res.resmodFloatWithShort}", "{rmod}");

       var fmod: String = foo(vFloat mod vShort) ;
       if (gen)javafx.lang.FX.print("\nvar resmodFloatWithShort_f: String=\"{fmod}\";");       
       assertEquals("{res.resmodFloatWithShort_f}", "{fmod}");
       var req: Boolean = vFloat == vShort ;
       if (gen)javafx.lang.FX.print("\nvar reseqFloatWithShort: Boolean={req};");       
       assertEquals("{res.reseqFloatWithShort}", "{req}");

       var rneq: Boolean = vFloat != vShort ;
       if (gen)javafx.lang.FX.print("\nvar resneqFloatWithShort: Boolean={rneq};");       
       assertEquals("{res.resneqFloatWithShort}", "{rneq}");

       var rlt: Boolean = vFloat < vShort ;
       if (gen)javafx.lang.FX.print("\nvar resltFloatWithShort: Boolean={rlt};");       
       assertEquals("{res.resltFloatWithShort}", "{rlt}");

       var rlte: Boolean = vFloat <= vShort ;
       if (gen)javafx.lang.FX.print("\nvar reslteFloatWithShort: Boolean={rlte};");       
       assertEquals("{res.reslteFloatWithShort}", "{rlte}");

       var rgt: Boolean = vFloat > vShort ;
       if (gen)javafx.lang.FX.print("\nvar resgtFloatWithShort: Boolean={rgt};");       
       assertEquals("{res.resgtFloatWithShort}", "{rgt}");

       var rgte: Boolean = vFloat >= vShort ;
       if (gen)javafx.lang.FX.print("\nvar resgteFloatWithShort: Boolean={rgte};");       
       assertEquals("{res.resgteFloatWithShort}", "{rgte}");

       if (gen)javafx.lang.FX.print("\n//");
}
    function testOperFloatWithCharacter() { 
       var radd: Number = vFloat + vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar resaddFloatWithCharacter: Number={radd};");       
       assertEquals("{res.resaddFloatWithCharacter}", "{radd}");

       var fadd: String = foo(vFloat + vCharacter) ;
       if (gen)javafx.lang.FX.print("\nvar resaddFloatWithCharacter_f: String=\"{fadd}\";");       
       assertEquals("{res.resaddFloatWithCharacter_f}", "{fadd}");
       var rsub: Number = vFloat - vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar ressubFloatWithCharacter: Number={rsub};");       
       assertEquals("{res.ressubFloatWithCharacter}", "{rsub}");

       var fsub: String = foo(vFloat - vCharacter) ;
       if (gen)javafx.lang.FX.print("\nvar ressubFloatWithCharacter_f: String=\"{fsub}\";");       
       assertEquals("{res.ressubFloatWithCharacter_f}", "{fsub}");
       var rmul: Number = vFloat * vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar resmulFloatWithCharacter: Number={rmul};");       
       assertEquals("{res.resmulFloatWithCharacter}", "{rmul}");

       var fmul: String = foo(vFloat * vCharacter) ;
       if (gen)javafx.lang.FX.print("\nvar resmulFloatWithCharacter_f: String=\"{fmul}\";");       
       assertEquals("{res.resmulFloatWithCharacter_f}", "{fmul}");
       var rdiv: Number = vFloat / vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar resdivFloatWithCharacter: Number={rdiv};");       
       assertEquals("{res.resdivFloatWithCharacter}", "{rdiv}");

       var fdiv: String = foo(vFloat / vCharacter) ;
       if (gen)javafx.lang.FX.print("\nvar resdivFloatWithCharacter_f: String=\"{fdiv}\";");       
       assertEquals("{res.resdivFloatWithCharacter_f}", "{fdiv}");
       var rmod: Number = vFloat mod vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar resmodFloatWithCharacter: Number={rmod};");       
       assertEquals("{res.resmodFloatWithCharacter}", "{rmod}");

       var fmod: String = foo(vFloat mod vCharacter) ;
       if (gen)javafx.lang.FX.print("\nvar resmodFloatWithCharacter_f: String=\"{fmod}\";");       
       assertEquals("{res.resmodFloatWithCharacter_f}", "{fmod}");
       var req: Boolean = vFloat == vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar reseqFloatWithCharacter: Boolean={req};");       
       assertEquals("{res.reseqFloatWithCharacter}", "{req}");

       var rneq: Boolean = vFloat != vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar resneqFloatWithCharacter: Boolean={rneq};");       
       assertEquals("{res.resneqFloatWithCharacter}", "{rneq}");

       var rlt: Boolean = vFloat < vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar resltFloatWithCharacter: Boolean={rlt};");       
       assertEquals("{res.resltFloatWithCharacter}", "{rlt}");

       var rlte: Boolean = vFloat <= vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar reslteFloatWithCharacter: Boolean={rlte};");       
       assertEquals("{res.reslteFloatWithCharacter}", "{rlte}");

       var rgt: Boolean = vFloat > vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar resgtFloatWithCharacter: Boolean={rgt};");       
       assertEquals("{res.resgtFloatWithCharacter}", "{rgt}");

       var rgte: Boolean = vFloat >= vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar resgteFloatWithCharacter: Boolean={rgte};");       
       assertEquals("{res.resgteFloatWithCharacter}", "{rgte}");

       if (gen)javafx.lang.FX.print("\n//");
}
    function testOperFloatWithInteger() { 
       var radd: Number = vFloat + vInteger ;
       if (gen)javafx.lang.FX.print("\nvar resaddFloatWithInteger: Number={radd};");       
       assertEquals("{res.resaddFloatWithInteger}", "{radd}");

       var fadd: String = foo(vFloat + vInteger) ;
       if (gen)javafx.lang.FX.print("\nvar resaddFloatWithInteger_f: String=\"{fadd}\";");       
       assertEquals("{res.resaddFloatWithInteger_f}", "{fadd}");
       var rsub: Number = vFloat - vInteger ;
       if (gen)javafx.lang.FX.print("\nvar ressubFloatWithInteger: Number={rsub};");       
       assertEquals("{res.ressubFloatWithInteger}", "{rsub}");

       var fsub: String = foo(vFloat - vInteger) ;
       if (gen)javafx.lang.FX.print("\nvar ressubFloatWithInteger_f: String=\"{fsub}\";");       
       assertEquals("{res.ressubFloatWithInteger_f}", "{fsub}");
       var rmul: Number = vFloat * vInteger ;
       if (gen)javafx.lang.FX.print("\nvar resmulFloatWithInteger: Number={rmul};");       
       assertEquals("{res.resmulFloatWithInteger}", "{rmul}");

       var fmul: String = foo(vFloat * vInteger) ;
       if (gen)javafx.lang.FX.print("\nvar resmulFloatWithInteger_f: String=\"{fmul}\";");       
       assertEquals("{res.resmulFloatWithInteger_f}", "{fmul}");
       var rdiv: Number = vFloat / vInteger ;
       if (gen)javafx.lang.FX.print("\nvar resdivFloatWithInteger: Number={rdiv};");       
       assertEquals("{res.resdivFloatWithInteger}", "{rdiv}");

       var fdiv: String = foo(vFloat / vInteger) ;
       if (gen)javafx.lang.FX.print("\nvar resdivFloatWithInteger_f: String=\"{fdiv}\";");       
       assertEquals("{res.resdivFloatWithInteger_f}", "{fdiv}");
       var rmod: Number = vFloat mod vInteger ;
       if (gen)javafx.lang.FX.print("\nvar resmodFloatWithInteger: Number={rmod};");       
       assertEquals("{res.resmodFloatWithInteger}", "{rmod}");

       var fmod: String = foo(vFloat mod vInteger) ;
       if (gen)javafx.lang.FX.print("\nvar resmodFloatWithInteger_f: String=\"{fmod}\";");       
       assertEquals("{res.resmodFloatWithInteger_f}", "{fmod}");
       var req: Boolean = vFloat == vInteger ;
       if (gen)javafx.lang.FX.print("\nvar reseqFloatWithInteger: Boolean={req};");       
       assertEquals("{res.reseqFloatWithInteger}", "{req}");

       var rneq: Boolean = vFloat != vInteger ;
       if (gen)javafx.lang.FX.print("\nvar resneqFloatWithInteger: Boolean={rneq};");       
       assertEquals("{res.resneqFloatWithInteger}", "{rneq}");

       var rlt: Boolean = vFloat < vInteger ;
       if (gen)javafx.lang.FX.print("\nvar resltFloatWithInteger: Boolean={rlt};");       
       assertEquals("{res.resltFloatWithInteger}", "{rlt}");

       var rlte: Boolean = vFloat <= vInteger ;
       if (gen)javafx.lang.FX.print("\nvar reslteFloatWithInteger: Boolean={rlte};");       
       assertEquals("{res.reslteFloatWithInteger}", "{rlte}");

       var rgt: Boolean = vFloat > vInteger ;
       if (gen)javafx.lang.FX.print("\nvar resgtFloatWithInteger: Boolean={rgt};");       
       assertEquals("{res.resgtFloatWithInteger}", "{rgt}");

       var rgte: Boolean = vFloat >= vInteger ;
       if (gen)javafx.lang.FX.print("\nvar resgteFloatWithInteger: Boolean={rgte};");       
       assertEquals("{res.resgteFloatWithInteger}", "{rgte}");

       if (gen)javafx.lang.FX.print("\n//");
}
    function testOperFloatWithLong() { 
       var radd: Number = vFloat + vLong ;
       if (gen)javafx.lang.FX.print("\nvar resaddFloatWithLong: Number={radd};");       
       assertEquals("{res.resaddFloatWithLong}", "{radd}");

       var fadd: String = foo(vFloat + vLong) ;
       if (gen)javafx.lang.FX.print("\nvar resaddFloatWithLong_f: String=\"{fadd}\";");       
       assertEquals("{res.resaddFloatWithLong_f}", "{fadd}");
       var rsub: Number = vFloat - vLong ;
       if (gen)javafx.lang.FX.print("\nvar ressubFloatWithLong: Number={rsub};");       
       assertEquals("{res.ressubFloatWithLong}", "{rsub}");

       var fsub: String = foo(vFloat - vLong) ;
       if (gen)javafx.lang.FX.print("\nvar ressubFloatWithLong_f: String=\"{fsub}\";");       
       assertEquals("{res.ressubFloatWithLong_f}", "{fsub}");
       var rmul: Number = vFloat * vLong ;
       if (gen)javafx.lang.FX.print("\nvar resmulFloatWithLong: Number={rmul};");       
       assertEquals("{res.resmulFloatWithLong}", "{rmul}");

       var fmul: String = foo(vFloat * vLong) ;
       if (gen)javafx.lang.FX.print("\nvar resmulFloatWithLong_f: String=\"{fmul}\";");       
       assertEquals("{res.resmulFloatWithLong_f}", "{fmul}");
       var rdiv: Number = vFloat / vLong ;
       if (gen)javafx.lang.FX.print("\nvar resdivFloatWithLong: Number={rdiv};");       
       assertEquals("{res.resdivFloatWithLong}", "{rdiv}");

       var fdiv: String = foo(vFloat / vLong) ;
       if (gen)javafx.lang.FX.print("\nvar resdivFloatWithLong_f: String=\"{fdiv}\";");       
       assertEquals("{res.resdivFloatWithLong_f}", "{fdiv}");
       var rmod: Number = vFloat mod vLong ;
       if (gen)javafx.lang.FX.print("\nvar resmodFloatWithLong: Number={rmod};");       
       assertEquals("{res.resmodFloatWithLong}", "{rmod}");

       var fmod: String = foo(vFloat mod vLong) ;
       if (gen)javafx.lang.FX.print("\nvar resmodFloatWithLong_f: String=\"{fmod}\";");       
       assertEquals("{res.resmodFloatWithLong_f}", "{fmod}");
       var req: Boolean = vFloat == vLong ;
       if (gen)javafx.lang.FX.print("\nvar reseqFloatWithLong: Boolean={req};");       
       assertEquals("{res.reseqFloatWithLong}", "{req}");

       var rneq: Boolean = vFloat != vLong ;
       if (gen)javafx.lang.FX.print("\nvar resneqFloatWithLong: Boolean={rneq};");       
       assertEquals("{res.resneqFloatWithLong}", "{rneq}");

       var rlt: Boolean = vFloat < vLong ;
       if (gen)javafx.lang.FX.print("\nvar resltFloatWithLong: Boolean={rlt};");       
       assertEquals("{res.resltFloatWithLong}", "{rlt}");

       var rlte: Boolean = vFloat <= vLong ;
       if (gen)javafx.lang.FX.print("\nvar reslteFloatWithLong: Boolean={rlte};");       
       assertEquals("{res.reslteFloatWithLong}", "{rlte}");

       var rgt: Boolean = vFloat > vLong ;
       if (gen)javafx.lang.FX.print("\nvar resgtFloatWithLong: Boolean={rgt};");       
       assertEquals("{res.resgtFloatWithLong}", "{rgt}");

       var rgte: Boolean = vFloat >= vLong ;
       if (gen)javafx.lang.FX.print("\nvar resgteFloatWithLong: Boolean={rgte};");       
       assertEquals("{res.resgteFloatWithLong}", "{rgte}");

       if (gen)javafx.lang.FX.print("\n//");
}
    function testOperFloatWithFloat() { 
       var radd: Number = vFloat + vFloat ;
       if (gen)javafx.lang.FX.print("\nvar resaddFloatWithFloat: Number={radd};");       
       assertEquals("{res.resaddFloatWithFloat}", "{radd}");

       var fadd: String = foo(vFloat + vFloat) ;
       if (gen)javafx.lang.FX.print("\nvar resaddFloatWithFloat_f: String=\"{fadd}\";");       
       assertEquals("{res.resaddFloatWithFloat_f}", "{fadd}");
       var rsub: Number = vFloat - vFloat ;
       if (gen)javafx.lang.FX.print("\nvar ressubFloatWithFloat: Number={rsub};");       
       assertEquals("{res.ressubFloatWithFloat}", "{rsub}");

       var fsub: String = foo(vFloat - vFloat) ;
       if (gen)javafx.lang.FX.print("\nvar ressubFloatWithFloat_f: String=\"{fsub}\";");       
       assertEquals("{res.ressubFloatWithFloat_f}", "{fsub}");
       var rmul: Number = vFloat * vFloat ;
       if (gen)javafx.lang.FX.print("\nvar resmulFloatWithFloat: Number={rmul};");       
       assertEquals("{res.resmulFloatWithFloat}", "{rmul}");

       var fmul: String = foo(vFloat * vFloat) ;
       if (gen)javafx.lang.FX.print("\nvar resmulFloatWithFloat_f: String=\"{fmul}\";");       
       assertEquals("{res.resmulFloatWithFloat_f}", "{fmul}");
       var rdiv: Number = vFloat / vFloat ;
       if (gen)javafx.lang.FX.print("\nvar resdivFloatWithFloat: Number={rdiv};");       
       assertEquals("{res.resdivFloatWithFloat}", "{rdiv}");

       var fdiv: String = foo(vFloat / vFloat) ;
       if (gen)javafx.lang.FX.print("\nvar resdivFloatWithFloat_f: String=\"{fdiv}\";");       
       assertEquals("{res.resdivFloatWithFloat_f}", "{fdiv}");
       var rmod: Number = vFloat mod vFloat ;
       if (gen)javafx.lang.FX.print("\nvar resmodFloatWithFloat: Number={rmod};");       
       assertEquals("{res.resmodFloatWithFloat}", "{rmod}");

       var fmod: String = foo(vFloat mod vFloat) ;
       if (gen)javafx.lang.FX.print("\nvar resmodFloatWithFloat_f: String=\"{fmod}\";");       
       assertEquals("{res.resmodFloatWithFloat_f}", "{fmod}");
       var req: Boolean = vFloat == vFloat ;
       if (gen)javafx.lang.FX.print("\nvar reseqFloatWithFloat: Boolean={req};");       
       assertEquals("{res.reseqFloatWithFloat}", "{req}");

       var rneq: Boolean = vFloat != vFloat ;
       if (gen)javafx.lang.FX.print("\nvar resneqFloatWithFloat: Boolean={rneq};");       
       assertEquals("{res.resneqFloatWithFloat}", "{rneq}");

       var rlt: Boolean = vFloat < vFloat ;
       if (gen)javafx.lang.FX.print("\nvar resltFloatWithFloat: Boolean={rlt};");       
       assertEquals("{res.resltFloatWithFloat}", "{rlt}");

       var rlte: Boolean = vFloat <= vFloat ;
       if (gen)javafx.lang.FX.print("\nvar reslteFloatWithFloat: Boolean={rlte};");       
       assertEquals("{res.reslteFloatWithFloat}", "{rlte}");

       var rgt: Boolean = vFloat > vFloat ;
       if (gen)javafx.lang.FX.print("\nvar resgtFloatWithFloat: Boolean={rgt};");       
       assertEquals("{res.resgtFloatWithFloat}", "{rgt}");

       var rgte: Boolean = vFloat >= vFloat ;
       if (gen)javafx.lang.FX.print("\nvar resgteFloatWithFloat: Boolean={rgte};");       
       assertEquals("{res.resgteFloatWithFloat}", "{rgte}");

       if (gen)javafx.lang.FX.print("\n//");
}
    function testOperFloatWithDouble() { 
       var radd: Number = vFloat + vDouble ;
       if (gen)javafx.lang.FX.print("\nvar resaddFloatWithDouble: Number={radd};");       
       assertEquals("{res.resaddFloatWithDouble}", "{radd}");

       var fadd: String = foo(vFloat + vDouble) ;
       if (gen)javafx.lang.FX.print("\nvar resaddFloatWithDouble_f: String=\"{fadd}\";");       
       assertEquals("{res.resaddFloatWithDouble_f}", "{fadd}");
       var rsub: Number = vFloat - vDouble ;
       if (gen)javafx.lang.FX.print("\nvar ressubFloatWithDouble: Number={rsub};");       
       assertEquals("{res.ressubFloatWithDouble}", "{rsub}");

       var fsub: String = foo(vFloat - vDouble) ;
       if (gen)javafx.lang.FX.print("\nvar ressubFloatWithDouble_f: String=\"{fsub}\";");       
       assertEquals("{res.ressubFloatWithDouble_f}", "{fsub}");
       var rmul: Number = vFloat * vDouble ;
       if (gen)javafx.lang.FX.print("\nvar resmulFloatWithDouble: Number={rmul};");       
       assertEquals("{res.resmulFloatWithDouble}", "{rmul}");

       var fmul: String = foo(vFloat * vDouble) ;
       if (gen)javafx.lang.FX.print("\nvar resmulFloatWithDouble_f: String=\"{fmul}\";");       
       assertEquals("{res.resmulFloatWithDouble_f}", "{fmul}");
       var rdiv: Number = vFloat / vDouble ;
       if (gen)javafx.lang.FX.print("\nvar resdivFloatWithDouble: Number={rdiv};");       
       assertEquals("{res.resdivFloatWithDouble}", "{rdiv}");

       var fdiv: String = foo(vFloat / vDouble) ;
       if (gen)javafx.lang.FX.print("\nvar resdivFloatWithDouble_f: String=\"{fdiv}\";");       
       assertEquals("{res.resdivFloatWithDouble_f}", "{fdiv}");
       var rmod: Number = vFloat mod vDouble ;
       if (gen)javafx.lang.FX.print("\nvar resmodFloatWithDouble: Number={rmod};");       
       assertEquals("{res.resmodFloatWithDouble}", "{rmod}");

       var fmod: String = foo(vFloat mod vDouble) ;
       if (gen)javafx.lang.FX.print("\nvar resmodFloatWithDouble_f: String=\"{fmod}\";");       
       assertEquals("{res.resmodFloatWithDouble_f}", "{fmod}");
       var req: Boolean = vFloat == vDouble ;
       if (gen)javafx.lang.FX.print("\nvar reseqFloatWithDouble: Boolean={req};");       
       assertEquals("{res.reseqFloatWithDouble}", "{req}");

       var rneq: Boolean = vFloat != vDouble ;
       if (gen)javafx.lang.FX.print("\nvar resneqFloatWithDouble: Boolean={rneq};");       
       assertEquals("{res.resneqFloatWithDouble}", "{rneq}");

       var rlt: Boolean = vFloat < vDouble ;
       if (gen)javafx.lang.FX.print("\nvar resltFloatWithDouble: Boolean={rlt};");       
       assertEquals("{res.resltFloatWithDouble}", "{rlt}");

       var rlte: Boolean = vFloat <= vDouble ;
       if (gen)javafx.lang.FX.print("\nvar reslteFloatWithDouble: Boolean={rlte};");       
       assertEquals("{res.reslteFloatWithDouble}", "{rlte}");

       var rgt: Boolean = vFloat > vDouble ;
       if (gen)javafx.lang.FX.print("\nvar resgtFloatWithDouble: Boolean={rgt};");       
       assertEquals("{res.resgtFloatWithDouble}", "{rgt}");

       var rgte: Boolean = vFloat >= vDouble ;
       if (gen)javafx.lang.FX.print("\nvar resgteFloatWithDouble: Boolean={rgte};");       
       assertEquals("{res.resgteFloatWithDouble}", "{rgte}");

       if (gen)javafx.lang.FX.print("\n//");
}
    function testOperFloatWithNumber() { 
       var radd: Number = vFloat + vNumber ;
       if (gen)javafx.lang.FX.print("\nvar resaddFloatWithNumber: Number={radd};");       
       assertEquals("{res.resaddFloatWithNumber}", "{radd}");

       var fadd: String = foo(vFloat + vNumber) ;
       if (gen)javafx.lang.FX.print("\nvar resaddFloatWithNumber_f: String=\"{fadd}\";");       
       assertEquals("{res.resaddFloatWithNumber_f}", "{fadd}");
       var rsub: Number = vFloat - vNumber ;
       if (gen)javafx.lang.FX.print("\nvar ressubFloatWithNumber: Number={rsub};");       
       assertEquals("{res.ressubFloatWithNumber}", "{rsub}");

       var fsub: String = foo(vFloat - vNumber) ;
       if (gen)javafx.lang.FX.print("\nvar ressubFloatWithNumber_f: String=\"{fsub}\";");       
       assertEquals("{res.ressubFloatWithNumber_f}", "{fsub}");
       var rmul: Number = vFloat * vNumber ;
       if (gen)javafx.lang.FX.print("\nvar resmulFloatWithNumber: Number={rmul};");       
       assertEquals("{res.resmulFloatWithNumber}", "{rmul}");

       var fmul: String = foo(vFloat * vNumber) ;
       if (gen)javafx.lang.FX.print("\nvar resmulFloatWithNumber_f: String=\"{fmul}\";");       
       assertEquals("{res.resmulFloatWithNumber_f}", "{fmul}");
       var rdiv: Number = vFloat / vNumber ;
       if (gen)javafx.lang.FX.print("\nvar resdivFloatWithNumber: Number={rdiv};");       
       assertEquals("{res.resdivFloatWithNumber}", "{rdiv}");

       var fdiv: String = foo(vFloat / vNumber) ;
       if (gen)javafx.lang.FX.print("\nvar resdivFloatWithNumber_f: String=\"{fdiv}\";");       
       assertEquals("{res.resdivFloatWithNumber_f}", "{fdiv}");
       var rmod: Number = vFloat mod vNumber ;
       if (gen)javafx.lang.FX.print("\nvar resmodFloatWithNumber: Number={rmod};");       
       assertEquals("{res.resmodFloatWithNumber}", "{rmod}");

       var fmod: String = foo(vFloat mod vNumber) ;
       if (gen)javafx.lang.FX.print("\nvar resmodFloatWithNumber_f: String=\"{fmod}\";");       
       assertEquals("{res.resmodFloatWithNumber_f}", "{fmod}");
       var req: Boolean = vFloat == vNumber ;
       if (gen)javafx.lang.FX.print("\nvar reseqFloatWithNumber: Boolean={req};");       
       assertEquals("{res.reseqFloatWithNumber}", "{req}");

       var rneq: Boolean = vFloat != vNumber ;
       if (gen)javafx.lang.FX.print("\nvar resneqFloatWithNumber: Boolean={rneq};");       
       assertEquals("{res.resneqFloatWithNumber}", "{rneq}");

       var rlt: Boolean = vFloat < vNumber ;
       if (gen)javafx.lang.FX.print("\nvar resltFloatWithNumber: Boolean={rlt};");       
       assertEquals("{res.resltFloatWithNumber}", "{rlt}");

       var rlte: Boolean = vFloat <= vNumber ;
       if (gen)javafx.lang.FX.print("\nvar reslteFloatWithNumber: Boolean={rlte};");       
       assertEquals("{res.reslteFloatWithNumber}", "{rlte}");

       var rgt: Boolean = vFloat > vNumber ;
       if (gen)javafx.lang.FX.print("\nvar resgtFloatWithNumber: Boolean={rgt};");       
       assertEquals("{res.resgtFloatWithNumber}", "{rgt}");

       var rgte: Boolean = vFloat >= vNumber ;
       if (gen)javafx.lang.FX.print("\nvar resgteFloatWithNumber: Boolean={rgte};");       
       assertEquals("{res.resgteFloatWithNumber}", "{rgte}");

       if (gen)javafx.lang.FX.print("\n//");
}
    var xDouble: Double[] = vDouble;  
    function testOperDoubleWithByte() { 
       var radd: Number = vDouble + vByte ;
       if (gen)javafx.lang.FX.print("\nvar resaddDoubleWithByte: Number={radd};");       
       assertEquals("{res.resaddDoubleWithByte}", "{radd}");

       var fadd: String = foo(vDouble + vByte) ;
       if (gen)javafx.lang.FX.print("\nvar resaddDoubleWithByte_f: String=\"{fadd}\";");       
       assertEquals("{res.resaddDoubleWithByte_f}", "{fadd}");
       var rsub: Number = vDouble - vByte ;
       if (gen)javafx.lang.FX.print("\nvar ressubDoubleWithByte: Number={rsub};");       
       assertEquals("{res.ressubDoubleWithByte}", "{rsub}");

       var fsub: String = foo(vDouble - vByte) ;
       if (gen)javafx.lang.FX.print("\nvar ressubDoubleWithByte_f: String=\"{fsub}\";");       
       assertEquals("{res.ressubDoubleWithByte_f}", "{fsub}");
       var rmul: Number = vDouble * vByte ;
       if (gen)javafx.lang.FX.print("\nvar resmulDoubleWithByte: Number={rmul};");       
       assertEquals("{res.resmulDoubleWithByte}", "{rmul}");

       var fmul: String = foo(vDouble * vByte) ;
       if (gen)javafx.lang.FX.print("\nvar resmulDoubleWithByte_f: String=\"{fmul}\";");       
       assertEquals("{res.resmulDoubleWithByte_f}", "{fmul}");
       var rdiv: Number = vDouble / vByte ;
       if (gen)javafx.lang.FX.print("\nvar resdivDoubleWithByte: Number={rdiv};");       
       assertEquals("{res.resdivDoubleWithByte}", "{rdiv}");

       var fdiv: String = foo(vDouble / vByte) ;
       if (gen)javafx.lang.FX.print("\nvar resdivDoubleWithByte_f: String=\"{fdiv}\";");       
       assertEquals("{res.resdivDoubleWithByte_f}", "{fdiv}");
       var rmod: Number = vDouble mod vByte ;
       if (gen)javafx.lang.FX.print("\nvar resmodDoubleWithByte: Number={rmod};");       
       assertEquals("{res.resmodDoubleWithByte}", "{rmod}");

       var fmod: String = foo(vDouble mod vByte) ;
       if (gen)javafx.lang.FX.print("\nvar resmodDoubleWithByte_f: String=\"{fmod}\";");       
       assertEquals("{res.resmodDoubleWithByte_f}", "{fmod}");
       var req: Boolean = vDouble == vByte ;
       if (gen)javafx.lang.FX.print("\nvar reseqDoubleWithByte: Boolean={req};");       
       assertEquals("{res.reseqDoubleWithByte}", "{req}");

       var rneq: Boolean = vDouble != vByte ;
       if (gen)javafx.lang.FX.print("\nvar resneqDoubleWithByte: Boolean={rneq};");       
       assertEquals("{res.resneqDoubleWithByte}", "{rneq}");

       var rlt: Boolean = vDouble < vByte ;
       if (gen)javafx.lang.FX.print("\nvar resltDoubleWithByte: Boolean={rlt};");       
       assertEquals("{res.resltDoubleWithByte}", "{rlt}");

       var rlte: Boolean = vDouble <= vByte ;
       if (gen)javafx.lang.FX.print("\nvar reslteDoubleWithByte: Boolean={rlte};");       
       assertEquals("{res.reslteDoubleWithByte}", "{rlte}");

       var rgt: Boolean = vDouble > vByte ;
       if (gen)javafx.lang.FX.print("\nvar resgtDoubleWithByte: Boolean={rgt};");       
       assertEquals("{res.resgtDoubleWithByte}", "{rgt}");

       var rgte: Boolean = vDouble >= vByte ;
       if (gen)javafx.lang.FX.print("\nvar resgteDoubleWithByte: Boolean={rgte};");       
       assertEquals("{res.resgteDoubleWithByte}", "{rgte}");

       if (gen)javafx.lang.FX.print("\n//");
}
    function testOperDoubleWithShort() { 
       var radd: Number = vDouble + vShort ;
       if (gen)javafx.lang.FX.print("\nvar resaddDoubleWithShort: Number={radd};");       
       assertEquals("{res.resaddDoubleWithShort}", "{radd}");

       var fadd: String = foo(vDouble + vShort) ;
       if (gen)javafx.lang.FX.print("\nvar resaddDoubleWithShort_f: String=\"{fadd}\";");       
       assertEquals("{res.resaddDoubleWithShort_f}", "{fadd}");
       var rsub: Number = vDouble - vShort ;
       if (gen)javafx.lang.FX.print("\nvar ressubDoubleWithShort: Number={rsub};");       
       assertEquals("{res.ressubDoubleWithShort}", "{rsub}");

       var fsub: String = foo(vDouble - vShort) ;
       if (gen)javafx.lang.FX.print("\nvar ressubDoubleWithShort_f: String=\"{fsub}\";");       
       assertEquals("{res.ressubDoubleWithShort_f}", "{fsub}");
       var rmul: Number = vDouble * vShort ;
       if (gen)javafx.lang.FX.print("\nvar resmulDoubleWithShort: Number={rmul};");       
       assertEquals("{res.resmulDoubleWithShort}", "{rmul}");

       var fmul: String = foo(vDouble * vShort) ;
       if (gen)javafx.lang.FX.print("\nvar resmulDoubleWithShort_f: String=\"{fmul}\";");       
       assertEquals("{res.resmulDoubleWithShort_f}", "{fmul}");
       var rdiv: Number = vDouble / vShort ;
       if (gen)javafx.lang.FX.print("\nvar resdivDoubleWithShort: Number={rdiv};");       
       assertEquals("{res.resdivDoubleWithShort}", "{rdiv}");

       var fdiv: String = foo(vDouble / vShort) ;
       if (gen)javafx.lang.FX.print("\nvar resdivDoubleWithShort_f: String=\"{fdiv}\";");       
       assertEquals("{res.resdivDoubleWithShort_f}", "{fdiv}");
       var rmod: Number = vDouble mod vShort ;
       if (gen)javafx.lang.FX.print("\nvar resmodDoubleWithShort: Number={rmod};");       
       assertEquals("{res.resmodDoubleWithShort}", "{rmod}");

       var fmod: String = foo(vDouble mod vShort) ;
       if (gen)javafx.lang.FX.print("\nvar resmodDoubleWithShort_f: String=\"{fmod}\";");       
       assertEquals("{res.resmodDoubleWithShort_f}", "{fmod}");
       var req: Boolean = vDouble == vShort ;
       if (gen)javafx.lang.FX.print("\nvar reseqDoubleWithShort: Boolean={req};");       
       assertEquals("{res.reseqDoubleWithShort}", "{req}");

       var rneq: Boolean = vDouble != vShort ;
       if (gen)javafx.lang.FX.print("\nvar resneqDoubleWithShort: Boolean={rneq};");       
       assertEquals("{res.resneqDoubleWithShort}", "{rneq}");

       var rlt: Boolean = vDouble < vShort ;
       if (gen)javafx.lang.FX.print("\nvar resltDoubleWithShort: Boolean={rlt};");       
       assertEquals("{res.resltDoubleWithShort}", "{rlt}");

       var rlte: Boolean = vDouble <= vShort ;
       if (gen)javafx.lang.FX.print("\nvar reslteDoubleWithShort: Boolean={rlte};");       
       assertEquals("{res.reslteDoubleWithShort}", "{rlte}");

       var rgt: Boolean = vDouble > vShort ;
       if (gen)javafx.lang.FX.print("\nvar resgtDoubleWithShort: Boolean={rgt};");       
       assertEquals("{res.resgtDoubleWithShort}", "{rgt}");

       var rgte: Boolean = vDouble >= vShort ;
       if (gen)javafx.lang.FX.print("\nvar resgteDoubleWithShort: Boolean={rgte};");       
       assertEquals("{res.resgteDoubleWithShort}", "{rgte}");

       if (gen)javafx.lang.FX.print("\n//");
}
    function testOperDoubleWithCharacter() { 
       var radd: Number = vDouble + vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar resaddDoubleWithCharacter: Number={radd};");       
       assertEquals("{res.resaddDoubleWithCharacter}", "{radd}");

       var fadd: String = foo(vDouble + vCharacter) ;
       if (gen)javafx.lang.FX.print("\nvar resaddDoubleWithCharacter_f: String=\"{fadd}\";");       
       assertEquals("{res.resaddDoubleWithCharacter_f}", "{fadd}");
       var rsub: Number = vDouble - vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar ressubDoubleWithCharacter: Number={rsub};");       
       assertEquals("{res.ressubDoubleWithCharacter}", "{rsub}");

       var fsub: String = foo(vDouble - vCharacter) ;
       if (gen)javafx.lang.FX.print("\nvar ressubDoubleWithCharacter_f: String=\"{fsub}\";");       
       assertEquals("{res.ressubDoubleWithCharacter_f}", "{fsub}");
       var rmul: Number = vDouble * vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar resmulDoubleWithCharacter: Number={rmul};");       
       assertEquals("{res.resmulDoubleWithCharacter}", "{rmul}");

       var fmul: String = foo(vDouble * vCharacter) ;
       if (gen)javafx.lang.FX.print("\nvar resmulDoubleWithCharacter_f: String=\"{fmul}\";");       
       assertEquals("{res.resmulDoubleWithCharacter_f}", "{fmul}");
       var rdiv: Number = vDouble / vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar resdivDoubleWithCharacter: Number={rdiv};");       
       assertEquals("{res.resdivDoubleWithCharacter}", "{rdiv}");

       var fdiv: String = foo(vDouble / vCharacter) ;
       if (gen)javafx.lang.FX.print("\nvar resdivDoubleWithCharacter_f: String=\"{fdiv}\";");       
       assertEquals("{res.resdivDoubleWithCharacter_f}", "{fdiv}");
       var rmod: Number = vDouble mod vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar resmodDoubleWithCharacter: Number={rmod};");       
       assertEquals("{res.resmodDoubleWithCharacter}", "{rmod}");

       var fmod: String = foo(vDouble mod vCharacter) ;
       if (gen)javafx.lang.FX.print("\nvar resmodDoubleWithCharacter_f: String=\"{fmod}\";");       
       assertEquals("{res.resmodDoubleWithCharacter_f}", "{fmod}");
       var req: Boolean = vDouble == vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar reseqDoubleWithCharacter: Boolean={req};");       
       assertEquals("{res.reseqDoubleWithCharacter}", "{req}");

       var rneq: Boolean = vDouble != vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar resneqDoubleWithCharacter: Boolean={rneq};");       
       assertEquals("{res.resneqDoubleWithCharacter}", "{rneq}");

       var rlt: Boolean = vDouble < vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar resltDoubleWithCharacter: Boolean={rlt};");       
       assertEquals("{res.resltDoubleWithCharacter}", "{rlt}");

       var rlte: Boolean = vDouble <= vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar reslteDoubleWithCharacter: Boolean={rlte};");       
       assertEquals("{res.reslteDoubleWithCharacter}", "{rlte}");

       var rgt: Boolean = vDouble > vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar resgtDoubleWithCharacter: Boolean={rgt};");       
       assertEquals("{res.resgtDoubleWithCharacter}", "{rgt}");

       var rgte: Boolean = vDouble >= vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar resgteDoubleWithCharacter: Boolean={rgte};");       
       assertEquals("{res.resgteDoubleWithCharacter}", "{rgte}");

       if (gen)javafx.lang.FX.print("\n//");
}
    function testOperDoubleWithInteger() { 
       var radd: Number = vDouble + vInteger ;
       if (gen)javafx.lang.FX.print("\nvar resaddDoubleWithInteger: Number={radd};");       
       assertEquals("{res.resaddDoubleWithInteger}", "{radd}");

       var fadd: String = foo(vDouble + vInteger) ;
       if (gen)javafx.lang.FX.print("\nvar resaddDoubleWithInteger_f: String=\"{fadd}\";");       
       assertEquals("{res.resaddDoubleWithInteger_f}", "{fadd}");
       var rsub: Number = vDouble - vInteger ;
       if (gen)javafx.lang.FX.print("\nvar ressubDoubleWithInteger: Number={rsub};");       
       assertEquals("{res.ressubDoubleWithInteger}", "{rsub}");

       var fsub: String = foo(vDouble - vInteger) ;
       if (gen)javafx.lang.FX.print("\nvar ressubDoubleWithInteger_f: String=\"{fsub}\";");       
       assertEquals("{res.ressubDoubleWithInteger_f}", "{fsub}");
       var rmul: Number = vDouble * vInteger ;
       if (gen)javafx.lang.FX.print("\nvar resmulDoubleWithInteger: Number={rmul};");       
       assertEquals("{res.resmulDoubleWithInteger}", "{rmul}");

       var fmul: String = foo(vDouble * vInteger) ;
       if (gen)javafx.lang.FX.print("\nvar resmulDoubleWithInteger_f: String=\"{fmul}\";");       
       assertEquals("{res.resmulDoubleWithInteger_f}", "{fmul}");
       var rdiv: Number = vDouble / vInteger ;
       if (gen)javafx.lang.FX.print("\nvar resdivDoubleWithInteger: Number={rdiv};");       
       assertEquals("{res.resdivDoubleWithInteger}", "{rdiv}");

       var fdiv: String = foo(vDouble / vInteger) ;
       if (gen)javafx.lang.FX.print("\nvar resdivDoubleWithInteger_f: String=\"{fdiv}\";");       
       assertEquals("{res.resdivDoubleWithInteger_f}", "{fdiv}");
       var rmod: Number = vDouble mod vInteger ;
       if (gen)javafx.lang.FX.print("\nvar resmodDoubleWithInteger: Number={rmod};");       
       assertEquals("{res.resmodDoubleWithInteger}", "{rmod}");

       var fmod: String = foo(vDouble mod vInteger) ;
       if (gen)javafx.lang.FX.print("\nvar resmodDoubleWithInteger_f: String=\"{fmod}\";");       
       assertEquals("{res.resmodDoubleWithInteger_f}", "{fmod}");
       var req: Boolean = vDouble == vInteger ;
       if (gen)javafx.lang.FX.print("\nvar reseqDoubleWithInteger: Boolean={req};");       
       assertEquals("{res.reseqDoubleWithInteger}", "{req}");

       var rneq: Boolean = vDouble != vInteger ;
       if (gen)javafx.lang.FX.print("\nvar resneqDoubleWithInteger: Boolean={rneq};");       
       assertEquals("{res.resneqDoubleWithInteger}", "{rneq}");

       var rlt: Boolean = vDouble < vInteger ;
       if (gen)javafx.lang.FX.print("\nvar resltDoubleWithInteger: Boolean={rlt};");       
       assertEquals("{res.resltDoubleWithInteger}", "{rlt}");

       var rlte: Boolean = vDouble <= vInteger ;
       if (gen)javafx.lang.FX.print("\nvar reslteDoubleWithInteger: Boolean={rlte};");       
       assertEquals("{res.reslteDoubleWithInteger}", "{rlte}");

       var rgt: Boolean = vDouble > vInteger ;
       if (gen)javafx.lang.FX.print("\nvar resgtDoubleWithInteger: Boolean={rgt};");       
       assertEquals("{res.resgtDoubleWithInteger}", "{rgt}");

       var rgte: Boolean = vDouble >= vInteger ;
       if (gen)javafx.lang.FX.print("\nvar resgteDoubleWithInteger: Boolean={rgte};");       
       assertEquals("{res.resgteDoubleWithInteger}", "{rgte}");

       if (gen)javafx.lang.FX.print("\n//");
}
    function testOperDoubleWithLong() { 
       var radd: Number = vDouble + vLong ;
       if (gen)javafx.lang.FX.print("\nvar resaddDoubleWithLong: Number={radd};");       
       assertEquals("{res.resaddDoubleWithLong}", "{radd}");

       var fadd: String = foo(vDouble + vLong) ;
       if (gen)javafx.lang.FX.print("\nvar resaddDoubleWithLong_f: String=\"{fadd}\";");       
       assertEquals("{res.resaddDoubleWithLong_f}", "{fadd}");
       var rsub: Number = vDouble - vLong ;
       if (gen)javafx.lang.FX.print("\nvar ressubDoubleWithLong: Number={rsub};");       
       assertEquals("{res.ressubDoubleWithLong}", "{rsub}");

       var fsub: String = foo(vDouble - vLong) ;
       if (gen)javafx.lang.FX.print("\nvar ressubDoubleWithLong_f: String=\"{fsub}\";");       
       assertEquals("{res.ressubDoubleWithLong_f}", "{fsub}");
       var rmul: Number = vDouble * vLong ;
       if (gen)javafx.lang.FX.print("\nvar resmulDoubleWithLong: Number={rmul};");       
       assertEquals("{res.resmulDoubleWithLong}", "{rmul}");

       var fmul: String = foo(vDouble * vLong) ;
       if (gen)javafx.lang.FX.print("\nvar resmulDoubleWithLong_f: String=\"{fmul}\";");       
       assertEquals("{res.resmulDoubleWithLong_f}", "{fmul}");
       var rdiv: Number = vDouble / vLong ;
       if (gen)javafx.lang.FX.print("\nvar resdivDoubleWithLong: Number={rdiv};");       
       assertEquals("{res.resdivDoubleWithLong}", "{rdiv}");

       var fdiv: String = foo(vDouble / vLong) ;
       if (gen)javafx.lang.FX.print("\nvar resdivDoubleWithLong_f: String=\"{fdiv}\";");       
       assertEquals("{res.resdivDoubleWithLong_f}", "{fdiv}");
       var rmod: Number = vDouble mod vLong ;
       if (gen)javafx.lang.FX.print("\nvar resmodDoubleWithLong: Number={rmod};");       
       assertEquals("{res.resmodDoubleWithLong}", "{rmod}");

       var fmod: String = foo(vDouble mod vLong) ;
       if (gen)javafx.lang.FX.print("\nvar resmodDoubleWithLong_f: String=\"{fmod}\";");       
       assertEquals("{res.resmodDoubleWithLong_f}", "{fmod}");
       var req: Boolean = vDouble == vLong ;
       if (gen)javafx.lang.FX.print("\nvar reseqDoubleWithLong: Boolean={req};");       
       assertEquals("{res.reseqDoubleWithLong}", "{req}");

       var rneq: Boolean = vDouble != vLong ;
       if (gen)javafx.lang.FX.print("\nvar resneqDoubleWithLong: Boolean={rneq};");       
       assertEquals("{res.resneqDoubleWithLong}", "{rneq}");

       var rlt: Boolean = vDouble < vLong ;
       if (gen)javafx.lang.FX.print("\nvar resltDoubleWithLong: Boolean={rlt};");       
       assertEquals("{res.resltDoubleWithLong}", "{rlt}");

       var rlte: Boolean = vDouble <= vLong ;
       if (gen)javafx.lang.FX.print("\nvar reslteDoubleWithLong: Boolean={rlte};");       
       assertEquals("{res.reslteDoubleWithLong}", "{rlte}");

       var rgt: Boolean = vDouble > vLong ;
       if (gen)javafx.lang.FX.print("\nvar resgtDoubleWithLong: Boolean={rgt};");       
       assertEquals("{res.resgtDoubleWithLong}", "{rgt}");

       var rgte: Boolean = vDouble >= vLong ;
       if (gen)javafx.lang.FX.print("\nvar resgteDoubleWithLong: Boolean={rgte};");       
       assertEquals("{res.resgteDoubleWithLong}", "{rgte}");

       if (gen)javafx.lang.FX.print("\n//");
}
    function testOperDoubleWithFloat() { 
       var radd: Number = vDouble + vFloat ;
       if (gen)javafx.lang.FX.print("\nvar resaddDoubleWithFloat: Number={radd};");       
       assertEquals("{res.resaddDoubleWithFloat}", "{radd}");

       var fadd: String = foo(vDouble + vFloat) ;
       if (gen)javafx.lang.FX.print("\nvar resaddDoubleWithFloat_f: String=\"{fadd}\";");       
       assertEquals("{res.resaddDoubleWithFloat_f}", "{fadd}");
       var rsub: Number = vDouble - vFloat ;
       if (gen)javafx.lang.FX.print("\nvar ressubDoubleWithFloat: Number={rsub};");       
       assertEquals("{res.ressubDoubleWithFloat}", "{rsub}");

       var fsub: String = foo(vDouble - vFloat) ;
       if (gen)javafx.lang.FX.print("\nvar ressubDoubleWithFloat_f: String=\"{fsub}\";");       
       assertEquals("{res.ressubDoubleWithFloat_f}", "{fsub}");
       var rmul: Number = vDouble * vFloat ;
       if (gen)javafx.lang.FX.print("\nvar resmulDoubleWithFloat: Number={rmul};");       
       assertEquals("{res.resmulDoubleWithFloat}", "{rmul}");

       var fmul: String = foo(vDouble * vFloat) ;
       if (gen)javafx.lang.FX.print("\nvar resmulDoubleWithFloat_f: String=\"{fmul}\";");       
       assertEquals("{res.resmulDoubleWithFloat_f}", "{fmul}");
       var rdiv: Number = vDouble / vFloat ;
       if (gen)javafx.lang.FX.print("\nvar resdivDoubleWithFloat: Number={rdiv};");       
       assertEquals("{res.resdivDoubleWithFloat}", "{rdiv}");

       var fdiv: String = foo(vDouble / vFloat) ;
       if (gen)javafx.lang.FX.print("\nvar resdivDoubleWithFloat_f: String=\"{fdiv}\";");       
       assertEquals("{res.resdivDoubleWithFloat_f}", "{fdiv}");
       var rmod: Number = vDouble mod vFloat ;
       if (gen)javafx.lang.FX.print("\nvar resmodDoubleWithFloat: Number={rmod};");       
       assertEquals("{res.resmodDoubleWithFloat}", "{rmod}");

       var fmod: String = foo(vDouble mod vFloat) ;
       if (gen)javafx.lang.FX.print("\nvar resmodDoubleWithFloat_f: String=\"{fmod}\";");       
       assertEquals("{res.resmodDoubleWithFloat_f}", "{fmod}");
       var req: Boolean = vDouble == vFloat ;
       if (gen)javafx.lang.FX.print("\nvar reseqDoubleWithFloat: Boolean={req};");       
       assertEquals("{res.reseqDoubleWithFloat}", "{req}");

       var rneq: Boolean = vDouble != vFloat ;
       if (gen)javafx.lang.FX.print("\nvar resneqDoubleWithFloat: Boolean={rneq};");       
       assertEquals("{res.resneqDoubleWithFloat}", "{rneq}");

       var rlt: Boolean = vDouble < vFloat ;
       if (gen)javafx.lang.FX.print("\nvar resltDoubleWithFloat: Boolean={rlt};");       
       assertEquals("{res.resltDoubleWithFloat}", "{rlt}");

       var rlte: Boolean = vDouble <= vFloat ;
       if (gen)javafx.lang.FX.print("\nvar reslteDoubleWithFloat: Boolean={rlte};");       
       assertEquals("{res.reslteDoubleWithFloat}", "{rlte}");

       var rgt: Boolean = vDouble > vFloat ;
       if (gen)javafx.lang.FX.print("\nvar resgtDoubleWithFloat: Boolean={rgt};");       
       assertEquals("{res.resgtDoubleWithFloat}", "{rgt}");

       var rgte: Boolean = vDouble >= vFloat ;
       if (gen)javafx.lang.FX.print("\nvar resgteDoubleWithFloat: Boolean={rgte};");       
       assertEquals("{res.resgteDoubleWithFloat}", "{rgte}");

       if (gen)javafx.lang.FX.print("\n//");
}
    function testOperDoubleWithDouble() { 
       var radd: Number = vDouble + vDouble ;
       if (gen)javafx.lang.FX.print("\nvar resaddDoubleWithDouble: Number={radd};");       
       assertEquals("{res.resaddDoubleWithDouble}", "{radd}");

       var fadd: String = foo(vDouble + vDouble) ;
       if (gen)javafx.lang.FX.print("\nvar resaddDoubleWithDouble_f: String=\"{fadd}\";");       
       assertEquals("{res.resaddDoubleWithDouble_f}", "{fadd}");
       var rsub: Number = vDouble - vDouble ;
       if (gen)javafx.lang.FX.print("\nvar ressubDoubleWithDouble: Number={rsub};");       
       assertEquals("{res.ressubDoubleWithDouble}", "{rsub}");

       var fsub: String = foo(vDouble - vDouble) ;
       if (gen)javafx.lang.FX.print("\nvar ressubDoubleWithDouble_f: String=\"{fsub}\";");       
       assertEquals("{res.ressubDoubleWithDouble_f}", "{fsub}");
       var rmul: Number = vDouble * vDouble ;
       if (gen)javafx.lang.FX.print("\nvar resmulDoubleWithDouble: Number={rmul};");       
       assertEquals("{res.resmulDoubleWithDouble}", "{rmul}");

       var fmul: String = foo(vDouble * vDouble) ;
       if (gen)javafx.lang.FX.print("\nvar resmulDoubleWithDouble_f: String=\"{fmul}\";");       
       assertEquals("{res.resmulDoubleWithDouble_f}", "{fmul}");
       var rdiv: Number = vDouble / vDouble ;
       if (gen)javafx.lang.FX.print("\nvar resdivDoubleWithDouble: Number={rdiv};");       
       assertEquals("{res.resdivDoubleWithDouble}", "{rdiv}");

       var fdiv: String = foo(vDouble / vDouble) ;
       if (gen)javafx.lang.FX.print("\nvar resdivDoubleWithDouble_f: String=\"{fdiv}\";");       
       assertEquals("{res.resdivDoubleWithDouble_f}", "{fdiv}");
       var rmod: Number = vDouble mod vDouble ;
       if (gen)javafx.lang.FX.print("\nvar resmodDoubleWithDouble: Number={rmod};");       
       assertEquals("{res.resmodDoubleWithDouble}", "{rmod}");

       var fmod: String = foo(vDouble mod vDouble) ;
       if (gen)javafx.lang.FX.print("\nvar resmodDoubleWithDouble_f: String=\"{fmod}\";");       
       assertEquals("{res.resmodDoubleWithDouble_f}", "{fmod}");
       var req: Boolean = vDouble == vDouble ;
       if (gen)javafx.lang.FX.print("\nvar reseqDoubleWithDouble: Boolean={req};");       
       assertEquals("{res.reseqDoubleWithDouble}", "{req}");

       var rneq: Boolean = vDouble != vDouble ;
       if (gen)javafx.lang.FX.print("\nvar resneqDoubleWithDouble: Boolean={rneq};");       
       assertEquals("{res.resneqDoubleWithDouble}", "{rneq}");

       var rlt: Boolean = vDouble < vDouble ;
       if (gen)javafx.lang.FX.print("\nvar resltDoubleWithDouble: Boolean={rlt};");       
       assertEquals("{res.resltDoubleWithDouble}", "{rlt}");

       var rlte: Boolean = vDouble <= vDouble ;
       if (gen)javafx.lang.FX.print("\nvar reslteDoubleWithDouble: Boolean={rlte};");       
       assertEquals("{res.reslteDoubleWithDouble}", "{rlte}");

       var rgt: Boolean = vDouble > vDouble ;
       if (gen)javafx.lang.FX.print("\nvar resgtDoubleWithDouble: Boolean={rgt};");       
       assertEquals("{res.resgtDoubleWithDouble}", "{rgt}");

       var rgte: Boolean = vDouble >= vDouble ;
       if (gen)javafx.lang.FX.print("\nvar resgteDoubleWithDouble: Boolean={rgte};");       
       assertEquals("{res.resgteDoubleWithDouble}", "{rgte}");

       if (gen)javafx.lang.FX.print("\n//");
}
    function testOperDoubleWithNumber() { 
       var radd: Number = vDouble + vNumber ;
       if (gen)javafx.lang.FX.print("\nvar resaddDoubleWithNumber: Number={radd};");       
       assertEquals("{res.resaddDoubleWithNumber}", "{radd}");

       var fadd: String = foo(vDouble + vNumber) ;
       if (gen)javafx.lang.FX.print("\nvar resaddDoubleWithNumber_f: String=\"{fadd}\";");       
       assertEquals("{res.resaddDoubleWithNumber_f}", "{fadd}");
       var rsub: Number = vDouble - vNumber ;
       if (gen)javafx.lang.FX.print("\nvar ressubDoubleWithNumber: Number={rsub};");       
       assertEquals("{res.ressubDoubleWithNumber}", "{rsub}");

       var fsub: String = foo(vDouble - vNumber) ;
       if (gen)javafx.lang.FX.print("\nvar ressubDoubleWithNumber_f: String=\"{fsub}\";");       
       assertEquals("{res.ressubDoubleWithNumber_f}", "{fsub}");
       var rmul: Number = vDouble * vNumber ;
       if (gen)javafx.lang.FX.print("\nvar resmulDoubleWithNumber: Number={rmul};");       
       assertEquals("{res.resmulDoubleWithNumber}", "{rmul}");

       var fmul: String = foo(vDouble * vNumber) ;
       if (gen)javafx.lang.FX.print("\nvar resmulDoubleWithNumber_f: String=\"{fmul}\";");       
       assertEquals("{res.resmulDoubleWithNumber_f}", "{fmul}");
       var rdiv: Number = vDouble / vNumber ;
       if (gen)javafx.lang.FX.print("\nvar resdivDoubleWithNumber: Number={rdiv};");       
       assertEquals("{res.resdivDoubleWithNumber}", "{rdiv}");

       var fdiv: String = foo(vDouble / vNumber) ;
       if (gen)javafx.lang.FX.print("\nvar resdivDoubleWithNumber_f: String=\"{fdiv}\";");       
       assertEquals("{res.resdivDoubleWithNumber_f}", "{fdiv}");
       var rmod: Number = vDouble mod vNumber ;
       if (gen)javafx.lang.FX.print("\nvar resmodDoubleWithNumber: Number={rmod};");       
       assertEquals("{res.resmodDoubleWithNumber}", "{rmod}");

       var fmod: String = foo(vDouble mod vNumber) ;
       if (gen)javafx.lang.FX.print("\nvar resmodDoubleWithNumber_f: String=\"{fmod}\";");       
       assertEquals("{res.resmodDoubleWithNumber_f}", "{fmod}");
       var req: Boolean = vDouble == vNumber ;
       if (gen)javafx.lang.FX.print("\nvar reseqDoubleWithNumber: Boolean={req};");       
       assertEquals("{res.reseqDoubleWithNumber}", "{req}");

       var rneq: Boolean = vDouble != vNumber ;
       if (gen)javafx.lang.FX.print("\nvar resneqDoubleWithNumber: Boolean={rneq};");       
       assertEquals("{res.resneqDoubleWithNumber}", "{rneq}");

       var rlt: Boolean = vDouble < vNumber ;
       if (gen)javafx.lang.FX.print("\nvar resltDoubleWithNumber: Boolean={rlt};");       
       assertEquals("{res.resltDoubleWithNumber}", "{rlt}");

       var rlte: Boolean = vDouble <= vNumber ;
       if (gen)javafx.lang.FX.print("\nvar reslteDoubleWithNumber: Boolean={rlte};");       
       assertEquals("{res.reslteDoubleWithNumber}", "{rlte}");

       var rgt: Boolean = vDouble > vNumber ;
       if (gen)javafx.lang.FX.print("\nvar resgtDoubleWithNumber: Boolean={rgt};");       
       assertEquals("{res.resgtDoubleWithNumber}", "{rgt}");

       var rgte: Boolean = vDouble >= vNumber ;
       if (gen)javafx.lang.FX.print("\nvar resgteDoubleWithNumber: Boolean={rgte};");       
       assertEquals("{res.resgteDoubleWithNumber}", "{rgte}");

       if (gen)javafx.lang.FX.print("\n//");
}
    var xNumber: Number[] = vNumber;  
    function testOperNumberWithByte() { 
       var radd: Number = vNumber + vByte ;
       if (gen)javafx.lang.FX.print("\nvar resaddNumberWithByte: Number={radd};");       
       assertEquals("{res.resaddNumberWithByte}", "{radd}");

       var fadd: String = foo(vNumber + vByte) ;
       if (gen)javafx.lang.FX.print("\nvar resaddNumberWithByte_f: String=\"{fadd}\";");       
       assertEquals("{res.resaddNumberWithByte_f}", "{fadd}");
       var rsub: Number = vNumber - vByte ;
       if (gen)javafx.lang.FX.print("\nvar ressubNumberWithByte: Number={rsub};");       
       assertEquals("{res.ressubNumberWithByte}", "{rsub}");

       var fsub: String = foo(vNumber - vByte) ;
       if (gen)javafx.lang.FX.print("\nvar ressubNumberWithByte_f: String=\"{fsub}\";");       
       assertEquals("{res.ressubNumberWithByte_f}", "{fsub}");
       var rmul: Number = vNumber * vByte ;
       if (gen)javafx.lang.FX.print("\nvar resmulNumberWithByte: Number={rmul};");       
       assertEquals("{res.resmulNumberWithByte}", "{rmul}");

       var fmul: String = foo(vNumber * vByte) ;
       if (gen)javafx.lang.FX.print("\nvar resmulNumberWithByte_f: String=\"{fmul}\";");       
       assertEquals("{res.resmulNumberWithByte_f}", "{fmul}");
       var rdiv: Number = vNumber / vByte ;
       if (gen)javafx.lang.FX.print("\nvar resdivNumberWithByte: Number={rdiv};");       
       assertEquals("{res.resdivNumberWithByte}", "{rdiv}");

       var fdiv: String = foo(vNumber / vByte) ;
       if (gen)javafx.lang.FX.print("\nvar resdivNumberWithByte_f: String=\"{fdiv}\";");       
       assertEquals("{res.resdivNumberWithByte_f}", "{fdiv}");
       var rmod: Number = vNumber mod vByte ;
       if (gen)javafx.lang.FX.print("\nvar resmodNumberWithByte: Number={rmod};");       
       assertEquals("{res.resmodNumberWithByte}", "{rmod}");

       var fmod: String = foo(vNumber mod vByte) ;
       if (gen)javafx.lang.FX.print("\nvar resmodNumberWithByte_f: String=\"{fmod}\";");       
       assertEquals("{res.resmodNumberWithByte_f}", "{fmod}");
       var req: Boolean = vNumber == vByte ;
       if (gen)javafx.lang.FX.print("\nvar reseqNumberWithByte: Boolean={req};");       
       assertEquals("{res.reseqNumberWithByte}", "{req}");

       var rneq: Boolean = vNumber != vByte ;
       if (gen)javafx.lang.FX.print("\nvar resneqNumberWithByte: Boolean={rneq};");       
       assertEquals("{res.resneqNumberWithByte}", "{rneq}");

       var rlt: Boolean = vNumber < vByte ;
       if (gen)javafx.lang.FX.print("\nvar resltNumberWithByte: Boolean={rlt};");       
       assertEquals("{res.resltNumberWithByte}", "{rlt}");

       var rlte: Boolean = vNumber <= vByte ;
       if (gen)javafx.lang.FX.print("\nvar reslteNumberWithByte: Boolean={rlte};");       
       assertEquals("{res.reslteNumberWithByte}", "{rlte}");

       var rgt: Boolean = vNumber > vByte ;
       if (gen)javafx.lang.FX.print("\nvar resgtNumberWithByte: Boolean={rgt};");       
       assertEquals("{res.resgtNumberWithByte}", "{rgt}");

       var rgte: Boolean = vNumber >= vByte ;
       if (gen)javafx.lang.FX.print("\nvar resgteNumberWithByte: Boolean={rgte};");       
       assertEquals("{res.resgteNumberWithByte}", "{rgte}");

       if (gen)javafx.lang.FX.print("\n//");
}
    function testOperNumberWithShort() { 
       var radd: Number = vNumber + vShort ;
       if (gen)javafx.lang.FX.print("\nvar resaddNumberWithShort: Number={radd};");       
       assertEquals("{res.resaddNumberWithShort}", "{radd}");

       var fadd: String = foo(vNumber + vShort) ;
       if (gen)javafx.lang.FX.print("\nvar resaddNumberWithShort_f: String=\"{fadd}\";");       
       assertEquals("{res.resaddNumberWithShort_f}", "{fadd}");
       var rsub: Number = vNumber - vShort ;
       if (gen)javafx.lang.FX.print("\nvar ressubNumberWithShort: Number={rsub};");       
       assertEquals("{res.ressubNumberWithShort}", "{rsub}");

       var fsub: String = foo(vNumber - vShort) ;
       if (gen)javafx.lang.FX.print("\nvar ressubNumberWithShort_f: String=\"{fsub}\";");       
       assertEquals("{res.ressubNumberWithShort_f}", "{fsub}");
       var rmul: Number = vNumber * vShort ;
       if (gen)javafx.lang.FX.print("\nvar resmulNumberWithShort: Number={rmul};");       
       assertEquals("{res.resmulNumberWithShort}", "{rmul}");

       var fmul: String = foo(vNumber * vShort) ;
       if (gen)javafx.lang.FX.print("\nvar resmulNumberWithShort_f: String=\"{fmul}\";");       
       assertEquals("{res.resmulNumberWithShort_f}", "{fmul}");
       var rdiv: Number = vNumber / vShort ;
       if (gen)javafx.lang.FX.print("\nvar resdivNumberWithShort: Number={rdiv};");       
       assertEquals("{res.resdivNumberWithShort}", "{rdiv}");

       var fdiv: String = foo(vNumber / vShort) ;
       if (gen)javafx.lang.FX.print("\nvar resdivNumberWithShort_f: String=\"{fdiv}\";");       
       assertEquals("{res.resdivNumberWithShort_f}", "{fdiv}");
       var rmod: Number = vNumber mod vShort ;
       if (gen)javafx.lang.FX.print("\nvar resmodNumberWithShort: Number={rmod};");       
       assertEquals("{res.resmodNumberWithShort}", "{rmod}");

       var fmod: String = foo(vNumber mod vShort) ;
       if (gen)javafx.lang.FX.print("\nvar resmodNumberWithShort_f: String=\"{fmod}\";");       
       assertEquals("{res.resmodNumberWithShort_f}", "{fmod}");
       var req: Boolean = vNumber == vShort ;
       if (gen)javafx.lang.FX.print("\nvar reseqNumberWithShort: Boolean={req};");       
       assertEquals("{res.reseqNumberWithShort}", "{req}");

       var rneq: Boolean = vNumber != vShort ;
       if (gen)javafx.lang.FX.print("\nvar resneqNumberWithShort: Boolean={rneq};");       
       assertEquals("{res.resneqNumberWithShort}", "{rneq}");

       var rlt: Boolean = vNumber < vShort ;
       if (gen)javafx.lang.FX.print("\nvar resltNumberWithShort: Boolean={rlt};");       
       assertEquals("{res.resltNumberWithShort}", "{rlt}");

       var rlte: Boolean = vNumber <= vShort ;
       if (gen)javafx.lang.FX.print("\nvar reslteNumberWithShort: Boolean={rlte};");       
       assertEquals("{res.reslteNumberWithShort}", "{rlte}");

       var rgt: Boolean = vNumber > vShort ;
       if (gen)javafx.lang.FX.print("\nvar resgtNumberWithShort: Boolean={rgt};");       
       assertEquals("{res.resgtNumberWithShort}", "{rgt}");

       var rgte: Boolean = vNumber >= vShort ;
       if (gen)javafx.lang.FX.print("\nvar resgteNumberWithShort: Boolean={rgte};");       
       assertEquals("{res.resgteNumberWithShort}", "{rgte}");

       if (gen)javafx.lang.FX.print("\n//");
}
    function testOperNumberWithCharacter() { 
       var radd: Number = vNumber + vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar resaddNumberWithCharacter: Number={radd};");       
       assertEquals("{res.resaddNumberWithCharacter}", "{radd}");

       var fadd: String = foo(vNumber + vCharacter) ;
       if (gen)javafx.lang.FX.print("\nvar resaddNumberWithCharacter_f: String=\"{fadd}\";");       
       assertEquals("{res.resaddNumberWithCharacter_f}", "{fadd}");
       var rsub: Number = vNumber - vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar ressubNumberWithCharacter: Number={rsub};");       
       assertEquals("{res.ressubNumberWithCharacter}", "{rsub}");

       var fsub: String = foo(vNumber - vCharacter) ;
       if (gen)javafx.lang.FX.print("\nvar ressubNumberWithCharacter_f: String=\"{fsub}\";");       
       assertEquals("{res.ressubNumberWithCharacter_f}", "{fsub}");
       var rmul: Number = vNumber * vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar resmulNumberWithCharacter: Number={rmul};");       
       assertEquals("{res.resmulNumberWithCharacter}", "{rmul}");

       var fmul: String = foo(vNumber * vCharacter) ;
       if (gen)javafx.lang.FX.print("\nvar resmulNumberWithCharacter_f: String=\"{fmul}\";");       
       assertEquals("{res.resmulNumberWithCharacter_f}", "{fmul}");
       var rdiv: Number = vNumber / vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar resdivNumberWithCharacter: Number={rdiv};");       
       assertEquals("{res.resdivNumberWithCharacter}", "{rdiv}");

       var fdiv: String = foo(vNumber / vCharacter) ;
       if (gen)javafx.lang.FX.print("\nvar resdivNumberWithCharacter_f: String=\"{fdiv}\";");       
       assertEquals("{res.resdivNumberWithCharacter_f}", "{fdiv}");
       var rmod: Number = vNumber mod vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar resmodNumberWithCharacter: Number={rmod};");       
       assertEquals("{res.resmodNumberWithCharacter}", "{rmod}");

       var fmod: String = foo(vNumber mod vCharacter) ;
       if (gen)javafx.lang.FX.print("\nvar resmodNumberWithCharacter_f: String=\"{fmod}\";");       
       assertEquals("{res.resmodNumberWithCharacter_f}", "{fmod}");
       var req: Boolean = vNumber == vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar reseqNumberWithCharacter: Boolean={req};");       
       assertEquals("{res.reseqNumberWithCharacter}", "{req}");

       var rneq: Boolean = vNumber != vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar resneqNumberWithCharacter: Boolean={rneq};");       
       assertEquals("{res.resneqNumberWithCharacter}", "{rneq}");

       var rlt: Boolean = vNumber < vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar resltNumberWithCharacter: Boolean={rlt};");       
       assertEquals("{res.resltNumberWithCharacter}", "{rlt}");

       var rlte: Boolean = vNumber <= vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar reslteNumberWithCharacter: Boolean={rlte};");       
       assertEquals("{res.reslteNumberWithCharacter}", "{rlte}");

       var rgt: Boolean = vNumber > vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar resgtNumberWithCharacter: Boolean={rgt};");       
       assertEquals("{res.resgtNumberWithCharacter}", "{rgt}");

       var rgte: Boolean = vNumber >= vCharacter ;
       if (gen)javafx.lang.FX.print("\nvar resgteNumberWithCharacter: Boolean={rgte};");       
       assertEquals("{res.resgteNumberWithCharacter}", "{rgte}");

       if (gen)javafx.lang.FX.print("\n//");
}
    function testOperNumberWithInteger() { 
       var radd: Number = vNumber + vInteger ;
       if (gen)javafx.lang.FX.print("\nvar resaddNumberWithInteger: Number={radd};");       
       assertEquals("{res.resaddNumberWithInteger}", "{radd}");

       var fadd: String = foo(vNumber + vInteger) ;
       if (gen)javafx.lang.FX.print("\nvar resaddNumberWithInteger_f: String=\"{fadd}\";");       
       assertEquals("{res.resaddNumberWithInteger_f}", "{fadd}");
       var rsub: Number = vNumber - vInteger ;
       if (gen)javafx.lang.FX.print("\nvar ressubNumberWithInteger: Number={rsub};");       
       assertEquals("{res.ressubNumberWithInteger}", "{rsub}");

       var fsub: String = foo(vNumber - vInteger) ;
       if (gen)javafx.lang.FX.print("\nvar ressubNumberWithInteger_f: String=\"{fsub}\";");       
       assertEquals("{res.ressubNumberWithInteger_f}", "{fsub}");
       var rmul: Number = vNumber * vInteger ;
       if (gen)javafx.lang.FX.print("\nvar resmulNumberWithInteger: Number={rmul};");       
       assertEquals("{res.resmulNumberWithInteger}", "{rmul}");

       var fmul: String = foo(vNumber * vInteger) ;
       if (gen)javafx.lang.FX.print("\nvar resmulNumberWithInteger_f: String=\"{fmul}\";");       
       assertEquals("{res.resmulNumberWithInteger_f}", "{fmul}");
       var rdiv: Number = vNumber / vInteger ;
       if (gen)javafx.lang.FX.print("\nvar resdivNumberWithInteger: Number={rdiv};");       
       assertEquals("{res.resdivNumberWithInteger}", "{rdiv}");

       var fdiv: String = foo(vNumber / vInteger) ;
       if (gen)javafx.lang.FX.print("\nvar resdivNumberWithInteger_f: String=\"{fdiv}\";");       
       assertEquals("{res.resdivNumberWithInteger_f}", "{fdiv}");
       var rmod: Number = vNumber mod vInteger ;
       if (gen)javafx.lang.FX.print("\nvar resmodNumberWithInteger: Number={rmod};");       
       assertEquals("{res.resmodNumberWithInteger}", "{rmod}");

       var fmod: String = foo(vNumber mod vInteger) ;
       if (gen)javafx.lang.FX.print("\nvar resmodNumberWithInteger_f: String=\"{fmod}\";");       
       assertEquals("{res.resmodNumberWithInteger_f}", "{fmod}");
       var req: Boolean = vNumber == vInteger ;
       if (gen)javafx.lang.FX.print("\nvar reseqNumberWithInteger: Boolean={req};");       
       assertEquals("{res.reseqNumberWithInteger}", "{req}");

       var rneq: Boolean = vNumber != vInteger ;
       if (gen)javafx.lang.FX.print("\nvar resneqNumberWithInteger: Boolean={rneq};");       
       assertEquals("{res.resneqNumberWithInteger}", "{rneq}");

       var rlt: Boolean = vNumber < vInteger ;
       if (gen)javafx.lang.FX.print("\nvar resltNumberWithInteger: Boolean={rlt};");       
       assertEquals("{res.resltNumberWithInteger}", "{rlt}");

       var rlte: Boolean = vNumber <= vInteger ;
       if (gen)javafx.lang.FX.print("\nvar reslteNumberWithInteger: Boolean={rlte};");       
       assertEquals("{res.reslteNumberWithInteger}", "{rlte}");

       var rgt: Boolean = vNumber > vInteger ;
       if (gen)javafx.lang.FX.print("\nvar resgtNumberWithInteger: Boolean={rgt};");       
       assertEquals("{res.resgtNumberWithInteger}", "{rgt}");

       var rgte: Boolean = vNumber >= vInteger ;
       if (gen)javafx.lang.FX.print("\nvar resgteNumberWithInteger: Boolean={rgte};");       
       assertEquals("{res.resgteNumberWithInteger}", "{rgte}");

       if (gen)javafx.lang.FX.print("\n//");
}
    function testOperNumberWithLong() { 
       var radd: Number = vNumber + vLong ;
       if (gen)javafx.lang.FX.print("\nvar resaddNumberWithLong: Number={radd};");       
       assertEquals("{res.resaddNumberWithLong}", "{radd}");

       var fadd: String = foo(vNumber + vLong) ;
       if (gen)javafx.lang.FX.print("\nvar resaddNumberWithLong_f: String=\"{fadd}\";");       
       assertEquals("{res.resaddNumberWithLong_f}", "{fadd}");
       var rsub: Number = vNumber - vLong ;
       if (gen)javafx.lang.FX.print("\nvar ressubNumberWithLong: Number={rsub};");       
       assertEquals("{res.ressubNumberWithLong}", "{rsub}");

       var fsub: String = foo(vNumber - vLong) ;
       if (gen)javafx.lang.FX.print("\nvar ressubNumberWithLong_f: String=\"{fsub}\";");       
       assertEquals("{res.ressubNumberWithLong_f}", "{fsub}");
       var rmul: Number = vNumber * vLong ;
       if (gen)javafx.lang.FX.print("\nvar resmulNumberWithLong: Number={rmul};");       
       assertEquals("{res.resmulNumberWithLong}", "{rmul}");

       var fmul: String = foo(vNumber * vLong) ;
       if (gen)javafx.lang.FX.print("\nvar resmulNumberWithLong_f: String=\"{fmul}\";");       
       assertEquals("{res.resmulNumberWithLong_f}", "{fmul}");
       var rdiv: Number = vNumber / vLong ;
       if (gen)javafx.lang.FX.print("\nvar resdivNumberWithLong: Number={rdiv};");       
       assertEquals("{res.resdivNumberWithLong}", "{rdiv}");

       var fdiv: String = foo(vNumber / vLong) ;
       if (gen)javafx.lang.FX.print("\nvar resdivNumberWithLong_f: String=\"{fdiv}\";");       
       assertEquals("{res.resdivNumberWithLong_f}", "{fdiv}");
       var rmod: Number = vNumber mod vLong ;
       if (gen)javafx.lang.FX.print("\nvar resmodNumberWithLong: Number={rmod};");       
       assertEquals("{res.resmodNumberWithLong}", "{rmod}");

       var fmod: String = foo(vNumber mod vLong) ;
       if (gen)javafx.lang.FX.print("\nvar resmodNumberWithLong_f: String=\"{fmod}\";");       
       assertEquals("{res.resmodNumberWithLong_f}", "{fmod}");
       var req: Boolean = vNumber == vLong ;
       if (gen)javafx.lang.FX.print("\nvar reseqNumberWithLong: Boolean={req};");       
       assertEquals("{res.reseqNumberWithLong}", "{req}");

       var rneq: Boolean = vNumber != vLong ;
       if (gen)javafx.lang.FX.print("\nvar resneqNumberWithLong: Boolean={rneq};");       
       assertEquals("{res.resneqNumberWithLong}", "{rneq}");

       var rlt: Boolean = vNumber < vLong ;
       if (gen)javafx.lang.FX.print("\nvar resltNumberWithLong: Boolean={rlt};");       
       assertEquals("{res.resltNumberWithLong}", "{rlt}");

       var rlte: Boolean = vNumber <= vLong ;
       if (gen)javafx.lang.FX.print("\nvar reslteNumberWithLong: Boolean={rlte};");       
       assertEquals("{res.reslteNumberWithLong}", "{rlte}");

       var rgt: Boolean = vNumber > vLong ;
       if (gen)javafx.lang.FX.print("\nvar resgtNumberWithLong: Boolean={rgt};");       
       assertEquals("{res.resgtNumberWithLong}", "{rgt}");

       var rgte: Boolean = vNumber >= vLong ;
       if (gen)javafx.lang.FX.print("\nvar resgteNumberWithLong: Boolean={rgte};");       
       assertEquals("{res.resgteNumberWithLong}", "{rgte}");

       if (gen)javafx.lang.FX.print("\n//");
}
    function testOperNumberWithFloat() { 
       var radd: Number = vNumber + vFloat ;
       if (gen)javafx.lang.FX.print("\nvar resaddNumberWithFloat: Number={radd};");       
       assertEquals("{res.resaddNumberWithFloat}", "{radd}");

       var fadd: String = foo(vNumber + vFloat) ;
       if (gen)javafx.lang.FX.print("\nvar resaddNumberWithFloat_f: String=\"{fadd}\";");       
       assertEquals("{res.resaddNumberWithFloat_f}", "{fadd}");
       var rsub: Number = vNumber - vFloat ;
       if (gen)javafx.lang.FX.print("\nvar ressubNumberWithFloat: Number={rsub};");       
       assertEquals("{res.ressubNumberWithFloat}", "{rsub}");

       var fsub: String = foo(vNumber - vFloat) ;
       if (gen)javafx.lang.FX.print("\nvar ressubNumberWithFloat_f: String=\"{fsub}\";");       
       assertEquals("{res.ressubNumberWithFloat_f}", "{fsub}");
       var rmul: Number = vNumber * vFloat ;
       if (gen)javafx.lang.FX.print("\nvar resmulNumberWithFloat: Number={rmul};");       
       assertEquals("{res.resmulNumberWithFloat}", "{rmul}");

       var fmul: String = foo(vNumber * vFloat) ;
       if (gen)javafx.lang.FX.print("\nvar resmulNumberWithFloat_f: String=\"{fmul}\";");       
       assertEquals("{res.resmulNumberWithFloat_f}", "{fmul}");
       var rdiv: Number = vNumber / vFloat ;
       if (gen)javafx.lang.FX.print("\nvar resdivNumberWithFloat: Number={rdiv};");       
       assertEquals("{res.resdivNumberWithFloat}", "{rdiv}");

       var fdiv: String = foo(vNumber / vFloat) ;
       if (gen)javafx.lang.FX.print("\nvar resdivNumberWithFloat_f: String=\"{fdiv}\";");       
       assertEquals("{res.resdivNumberWithFloat_f}", "{fdiv}");
       var rmod: Number = vNumber mod vFloat ;
       if (gen)javafx.lang.FX.print("\nvar resmodNumberWithFloat: Number={rmod};");       
       assertEquals("{res.resmodNumberWithFloat}", "{rmod}");

       var fmod: String = foo(vNumber mod vFloat) ;
       if (gen)javafx.lang.FX.print("\nvar resmodNumberWithFloat_f: String=\"{fmod}\";");       
       assertEquals("{res.resmodNumberWithFloat_f}", "{fmod}");
       var req: Boolean = vNumber == vFloat ;
       if (gen)javafx.lang.FX.print("\nvar reseqNumberWithFloat: Boolean={req};");       
       assertEquals("{res.reseqNumberWithFloat}", "{req}");

       var rneq: Boolean = vNumber != vFloat ;
       if (gen)javafx.lang.FX.print("\nvar resneqNumberWithFloat: Boolean={rneq};");       
       assertEquals("{res.resneqNumberWithFloat}", "{rneq}");

       var rlt: Boolean = vNumber < vFloat ;
       if (gen)javafx.lang.FX.print("\nvar resltNumberWithFloat: Boolean={rlt};");       
       assertEquals("{res.resltNumberWithFloat}", "{rlt}");

       var rlte: Boolean = vNumber <= vFloat ;
       if (gen)javafx.lang.FX.print("\nvar reslteNumberWithFloat: Boolean={rlte};");       
       assertEquals("{res.reslteNumberWithFloat}", "{rlte}");

       var rgt: Boolean = vNumber > vFloat ;
       if (gen)javafx.lang.FX.print("\nvar resgtNumberWithFloat: Boolean={rgt};");       
       assertEquals("{res.resgtNumberWithFloat}", "{rgt}");

       var rgte: Boolean = vNumber >= vFloat ;
       if (gen)javafx.lang.FX.print("\nvar resgteNumberWithFloat: Boolean={rgte};");       
       assertEquals("{res.resgteNumberWithFloat}", "{rgte}");

       if (gen)javafx.lang.FX.print("\n//");
}
    function testOperNumberWithDouble() { 
       var radd: Number = vNumber + vDouble ;
       if (gen)javafx.lang.FX.print("\nvar resaddNumberWithDouble: Number={radd};");       
       assertEquals("{res.resaddNumberWithDouble}", "{radd}");

       var fadd: String = foo(vNumber + vDouble) ;
       if (gen)javafx.lang.FX.print("\nvar resaddNumberWithDouble_f: String=\"{fadd}\";");       
       assertEquals("{res.resaddNumberWithDouble_f}", "{fadd}");
       var rsub: Number = vNumber - vDouble ;
       if (gen)javafx.lang.FX.print("\nvar ressubNumberWithDouble: Number={rsub};");       
       assertEquals("{res.ressubNumberWithDouble}", "{rsub}");

       var fsub: String = foo(vNumber - vDouble) ;
       if (gen)javafx.lang.FX.print("\nvar ressubNumberWithDouble_f: String=\"{fsub}\";");       
       assertEquals("{res.ressubNumberWithDouble_f}", "{fsub}");
       var rmul: Number = vNumber * vDouble ;
       if (gen)javafx.lang.FX.print("\nvar resmulNumberWithDouble: Number={rmul};");       
       assertEquals("{res.resmulNumberWithDouble}", "{rmul}");

       var fmul: String = foo(vNumber * vDouble) ;
       if (gen)javafx.lang.FX.print("\nvar resmulNumberWithDouble_f: String=\"{fmul}\";");       
       assertEquals("{res.resmulNumberWithDouble_f}", "{fmul}");
       var rdiv: Number = vNumber / vDouble ;
       if (gen)javafx.lang.FX.print("\nvar resdivNumberWithDouble: Number={rdiv};");       
       assertEquals("{res.resdivNumberWithDouble}", "{rdiv}");

       var fdiv: String = foo(vNumber / vDouble) ;
       if (gen)javafx.lang.FX.print("\nvar resdivNumberWithDouble_f: String=\"{fdiv}\";");       
       assertEquals("{res.resdivNumberWithDouble_f}", "{fdiv}");
       var rmod: Number = vNumber mod vDouble ;
       if (gen)javafx.lang.FX.print("\nvar resmodNumberWithDouble: Number={rmod};");       
       assertEquals("{res.resmodNumberWithDouble}", "{rmod}");

       var fmod: String = foo(vNumber mod vDouble) ;
       if (gen)javafx.lang.FX.print("\nvar resmodNumberWithDouble_f: String=\"{fmod}\";");       
       assertEquals("{res.resmodNumberWithDouble_f}", "{fmod}");
       var req: Boolean = vNumber == vDouble ;
       if (gen)javafx.lang.FX.print("\nvar reseqNumberWithDouble: Boolean={req};");       
       assertEquals("{res.reseqNumberWithDouble}", "{req}");

       var rneq: Boolean = vNumber != vDouble ;
       if (gen)javafx.lang.FX.print("\nvar resneqNumberWithDouble: Boolean={rneq};");       
       assertEquals("{res.resneqNumberWithDouble}", "{rneq}");

       var rlt: Boolean = vNumber < vDouble ;
       if (gen)javafx.lang.FX.print("\nvar resltNumberWithDouble: Boolean={rlt};");       
       assertEquals("{res.resltNumberWithDouble}", "{rlt}");

       var rlte: Boolean = vNumber <= vDouble ;
       if (gen)javafx.lang.FX.print("\nvar reslteNumberWithDouble: Boolean={rlte};");       
       assertEquals("{res.reslteNumberWithDouble}", "{rlte}");

       var rgt: Boolean = vNumber > vDouble ;
       if (gen)javafx.lang.FX.print("\nvar resgtNumberWithDouble: Boolean={rgt};");       
       assertEquals("{res.resgtNumberWithDouble}", "{rgt}");

       var rgte: Boolean = vNumber >= vDouble ;
       if (gen)javafx.lang.FX.print("\nvar resgteNumberWithDouble: Boolean={rgte};");       
       assertEquals("{res.resgteNumberWithDouble}", "{rgte}");

       if (gen)javafx.lang.FX.print("\n//");
}
    function testOperNumberWithNumber() { 
       var radd: Number = vNumber + vNumber ;
       if (gen)javafx.lang.FX.print("\nvar resaddNumberWithNumber: Number={radd};");       
       assertEquals("{res.resaddNumberWithNumber}", "{radd}");

       var fadd: String = foo(vNumber + vNumber) ;
       if (gen)javafx.lang.FX.print("\nvar resaddNumberWithNumber_f: String=\"{fadd}\";");       
       assertEquals("{res.resaddNumberWithNumber_f}", "{fadd}");
       var rsub: Number = vNumber - vNumber ;
       if (gen)javafx.lang.FX.print("\nvar ressubNumberWithNumber: Number={rsub};");       
       assertEquals("{res.ressubNumberWithNumber}", "{rsub}");

       var fsub: String = foo(vNumber - vNumber) ;
       if (gen)javafx.lang.FX.print("\nvar ressubNumberWithNumber_f: String=\"{fsub}\";");       
       assertEquals("{res.ressubNumberWithNumber_f}", "{fsub}");
       var rmul: Number = vNumber * vNumber ;
       if (gen)javafx.lang.FX.print("\nvar resmulNumberWithNumber: Number={rmul};");       
       assertEquals("{res.resmulNumberWithNumber}", "{rmul}");

       var fmul: String = foo(vNumber * vNumber) ;
       if (gen)javafx.lang.FX.print("\nvar resmulNumberWithNumber_f: String=\"{fmul}\";");       
       assertEquals("{res.resmulNumberWithNumber_f}", "{fmul}");
       var rdiv: Number = vNumber / vNumber ;
       if (gen)javafx.lang.FX.print("\nvar resdivNumberWithNumber: Number={rdiv};");       
       assertEquals("{res.resdivNumberWithNumber}", "{rdiv}");

       var fdiv: String = foo(vNumber / vNumber) ;
       if (gen)javafx.lang.FX.print("\nvar resdivNumberWithNumber_f: String=\"{fdiv}\";");       
       assertEquals("{res.resdivNumberWithNumber_f}", "{fdiv}");
       var rmod: Number = vNumber mod vNumber ;
       if (gen)javafx.lang.FX.print("\nvar resmodNumberWithNumber: Number={rmod};");       
       assertEquals("{res.resmodNumberWithNumber}", "{rmod}");

       var fmod: String = foo(vNumber mod vNumber) ;
       if (gen)javafx.lang.FX.print("\nvar resmodNumberWithNumber_f: String=\"{fmod}\";");       
       assertEquals("{res.resmodNumberWithNumber_f}", "{fmod}");
       var req: Boolean = vNumber == vNumber ;
       if (gen)javafx.lang.FX.print("\nvar reseqNumberWithNumber: Boolean={req};");       
       assertEquals("{res.reseqNumberWithNumber}", "{req}");

       var rneq: Boolean = vNumber != vNumber ;
       if (gen)javafx.lang.FX.print("\nvar resneqNumberWithNumber: Boolean={rneq};");       
       assertEquals("{res.resneqNumberWithNumber}", "{rneq}");

       var rlt: Boolean = vNumber < vNumber ;
       if (gen)javafx.lang.FX.print("\nvar resltNumberWithNumber: Boolean={rlt};");       
       assertEquals("{res.resltNumberWithNumber}", "{rlt}");

       var rlte: Boolean = vNumber <= vNumber ;
       if (gen)javafx.lang.FX.print("\nvar reslteNumberWithNumber: Boolean={rlte};");       
       assertEquals("{res.reslteNumberWithNumber}", "{rlte}");

       var rgt: Boolean = vNumber > vNumber ;
       if (gen)javafx.lang.FX.print("\nvar resgtNumberWithNumber: Boolean={rgt};");       
       assertEquals("{res.resgtNumberWithNumber}", "{rgt}");

       var rgte: Boolean = vNumber >= vNumber ;
       if (gen)javafx.lang.FX.print("\nvar resgteNumberWithNumber: Boolean={rgte};");       
       assertEquals("{res.resgteNumberWithNumber}", "{rgte}");

       if (gen)javafx.lang.FX.print("\n//");
}

var resaddByteWithByte: Number=240.0;
var resaddByteWithByte_f: String="Integer";
var ressubByteWithByte: Number=0.0;
var ressubByteWithByte_f: String="Integer";
var resmulByteWithByte: Number=14400.0;
var resmulByteWithByte_f: String="Integer";
var resdivByteWithByte: Number=1.0;
var resdivByteWithByte_f: String="Integer";
var resmodByteWithByte: Number=0.0;
var resmodByteWithByte_f: String="Integer";
var reseqByteWithByte: Boolean=true;
var resneqByteWithByte: Boolean=false;
var resltByteWithByte: Boolean=false;
var reslteByteWithByte: Boolean=true;
var resgtByteWithByte: Boolean=false;
var resgteByteWithByte: Boolean=true;
//.
var resaddByteWithShort: Number=30120.0;
var resaddByteWithShort_f: String="Integer";
var ressubByteWithShort: Number=-29880.0;
var ressubByteWithShort_f: String="Integer";
var resmulByteWithShort: Number=3600000.0;
var resmulByteWithShort_f: String="Integer";
var resdivByteWithShort: Number=0.0;
var resdivByteWithShort_f: String="Integer";
var resmodByteWithShort: Number=120.0;
var resmodByteWithShort_f: String="Integer";
var reseqByteWithShort: Boolean=false;
var resneqByteWithShort: Boolean=true;
var resltByteWithShort: Boolean=true;
var reslteByteWithShort: Boolean=true;
var resgtByteWithShort: Boolean=false;
var resgteByteWithShort: Boolean=false;
//.
var resaddByteWithCharacter: Number=184.0;
var resaddByteWithCharacter_f: String="Integer";
var ressubByteWithCharacter: Number=56.0;
var ressubByteWithCharacter_f: String="Integer";
var resmulByteWithCharacter: Number=7680.0;
var resmulByteWithCharacter_f: String="Integer";
var resdivByteWithCharacter: Number=1.0;
var resdivByteWithCharacter_f: String="Integer";
var resmodByteWithCharacter: Number=56.0;
var resmodByteWithCharacter_f: String="Integer";
var reseqByteWithCharacter: Boolean=false;
var resneqByteWithCharacter: Boolean=true;
var resltByteWithCharacter: Boolean=false;
var reslteByteWithCharacter: Boolean=false;
var resgtByteWithCharacter: Boolean=true;
var resgteByteWithCharacter: Boolean=true;
//.
var resaddByteWithInteger: Number=1000120.0;
var resaddByteWithInteger_f: String="Integer";
var ressubByteWithInteger: Number=-999880.0;
var ressubByteWithInteger_f: String="Integer";
var resmulByteWithInteger: Number=1.2E8;
var resmulByteWithInteger_f: String="Integer";
var resdivByteWithInteger: Number=0.0;
var resdivByteWithInteger_f: String="Integer";
var resmodByteWithInteger: Number=120.0;
var resmodByteWithInteger_f: String="Integer";
var reseqByteWithInteger: Boolean=false;
var resneqByteWithInteger: Boolean=true;
var resltByteWithInteger: Boolean=true;
var reslteByteWithInteger: Boolean=true;
var resgtByteWithInteger: Boolean=false;
var resgteByteWithInteger: Boolean=false;
//.
var resaddByteWithLong: Number=1.00000013E9;
var resaddByteWithLong_f: String="Long";
var ressubByteWithLong: Number=-9.9999987E8;
var ressubByteWithLong_f: String="Long";
var resmulByteWithLong: Number=1.20000004E11;
var resmulByteWithLong_f: String="Long";
var resdivByteWithLong: Number=0.0;
var resdivByteWithLong_f: String="Long";
var resmodByteWithLong: Number=120.0;
var resmodByteWithLong_f: String="Long";
var reseqByteWithLong: Boolean=false;
var resneqByteWithLong: Boolean=true;
var resltByteWithLong: Boolean=true;
var reslteByteWithLong: Boolean=true;
var resgtByteWithLong: Boolean=false;
var resgteByteWithLong: Boolean=false;
//.
var resaddByteWithFloat: Number=130.5;
var resaddByteWithFloat_f: String="Float";
var ressubByteWithFloat: Number=109.5;
var ressubByteWithFloat_f: String="Float";
var resmulByteWithFloat: Number=1260.0;
var resmulByteWithFloat_f: String="Float";
var resdivByteWithFloat: Number=11.428572;
var resdivByteWithFloat_f: String="Float";
var resmodByteWithFloat: Number=4.5;
var resmodByteWithFloat_f: String="Float";
var reseqByteWithFloat: Boolean=false;
var resneqByteWithFloat: Boolean=true;
var resltByteWithFloat: Boolean=false;
var reslteByteWithFloat: Boolean=false;
var resgtByteWithFloat: Boolean=true;
var resgteByteWithFloat: Boolean=true;
//.
var resaddByteWithDouble: Number=12620.0;
var resaddByteWithDouble_f: String="Double";
var ressubByteWithDouble: Number=-12380.0;
var ressubByteWithDouble_f: String="Double";
var resmulByteWithDouble: Number=1500000.0;
var resmulByteWithDouble_f: String="Double";
var resdivByteWithDouble: Number=0.0096;
var resdivByteWithDouble_f: String="Double";
var resmodByteWithDouble: Number=120.0;
var resmodByteWithDouble_f: String="Double";
var reseqByteWithDouble: Boolean=false;
var resneqByteWithDouble: Boolean=true;
var resltByteWithDouble: Boolean=true;
var reslteByteWithDouble: Boolean=true;
var resgtByteWithDouble: Boolean=false;
var resgteByteWithDouble: Boolean=false;
//.
var resaddByteWithNumber: Number=220.0;
var resaddByteWithNumber_f: String="Float";
var ressubByteWithNumber: Number=20.0;
var ressubByteWithNumber_f: String="Float";
var resmulByteWithNumber: Number=12000.0;
var resmulByteWithNumber_f: String="Float";
var resdivByteWithNumber: Number=1.2;
var resdivByteWithNumber_f: String="Float";
var resmodByteWithNumber: Number=20.0;
var resmodByteWithNumber_f: String="Float";
var reseqByteWithNumber: Boolean=false;
var resneqByteWithNumber: Boolean=true;
var resltByteWithNumber: Boolean=false;
var reslteByteWithNumber: Boolean=false;
var resgtByteWithNumber: Boolean=true;
var resgteByteWithNumber: Boolean=true;
//.
var resaddShortWithByte: Number=30120.0;
var resaddShortWithByte_f: String="Integer";
var ressubShortWithByte: Number=29880.0;
var ressubShortWithByte_f: String="Integer";
var resmulShortWithByte: Number=3600000.0;
var resmulShortWithByte_f: String="Integer";
var resdivShortWithByte: Number=250.0;
var resdivShortWithByte_f: String="Integer";
var resmodShortWithByte: Number=0.0;
var resmodShortWithByte_f: String="Integer";
var reseqShortWithByte: Boolean=false;
var resneqShortWithByte: Boolean=true;
var resltShortWithByte: Boolean=false;
var reslteShortWithByte: Boolean=false;
var resgtShortWithByte: Boolean=true;
var resgteShortWithByte: Boolean=true;
//.
var resaddShortWithShort: Number=60000.0;
var resaddShortWithShort_f: String="Integer";
var ressubShortWithShort: Number=0.0;
var ressubShortWithShort_f: String="Integer";
var resmulShortWithShort: Number=9.0E8;
var resmulShortWithShort_f: String="Integer";
var resdivShortWithShort: Number=1.0;
var resdivShortWithShort_f: String="Integer";
var resmodShortWithShort: Number=0.0;
var resmodShortWithShort_f: String="Integer";
var reseqShortWithShort: Boolean=true;
var resneqShortWithShort: Boolean=false;
var resltShortWithShort: Boolean=false;
var reslteShortWithShort: Boolean=true;
var resgtShortWithShort: Boolean=false;
var resgteShortWithShort: Boolean=true;
//.
var resaddShortWithCharacter: Number=30064.0;
var resaddShortWithCharacter_f: String="Integer";
var ressubShortWithCharacter: Number=29936.0;
var ressubShortWithCharacter_f: String="Integer";
var resmulShortWithCharacter: Number=1920000.0;
var resmulShortWithCharacter_f: String="Integer";
var resdivShortWithCharacter: Number=468.0;
var resdivShortWithCharacter_f: String="Integer";
var resmodShortWithCharacter: Number=48.0;
var resmodShortWithCharacter_f: String="Integer";
var reseqShortWithCharacter: Boolean=false;
var resneqShortWithCharacter: Boolean=true;
var resltShortWithCharacter: Boolean=false;
var reslteShortWithCharacter: Boolean=false;
var resgtShortWithCharacter: Boolean=true;
var resgteShortWithCharacter: Boolean=true;
//.
var resaddShortWithInteger: Number=1030000.0;
var resaddShortWithInteger_f: String="Integer";
var ressubShortWithInteger: Number=-970000.0;
var ressubShortWithInteger_f: String="Integer";
var resmulShortWithInteger: Number=-6.4771072E7;
var resmulShortWithInteger_f: String="Integer";
var resdivShortWithInteger: Number=0.0;
var resdivShortWithInteger_f: String="Integer";
var resmodShortWithInteger: Number=30000.0;
var resmodShortWithInteger_f: String="Integer";
var reseqShortWithInteger: Boolean=false;
var resneqShortWithInteger: Boolean=true;
var resltShortWithInteger: Boolean=true;
var reslteShortWithInteger: Boolean=true;
var resgtShortWithInteger: Boolean=false;
var resgteShortWithInteger: Boolean=false;
//.
var resaddShortWithLong: Number=1.00003002E9;
var resaddShortWithLong_f: String="Long";
var ressubShortWithLong: Number=-9.9996998E8;
var ressubShortWithLong_f: String="Long";
var resmulShortWithLong: Number=3.00000005E13;
var resmulShortWithLong_f: String="Long";
var resdivShortWithLong: Number=0.0;
var resdivShortWithLong_f: String="Long";
var resmodShortWithLong: Number=30000.0;
var resmodShortWithLong_f: String="Long";
var reseqShortWithLong: Boolean=false;
var resneqShortWithLong: Boolean=true;
var resltShortWithLong: Boolean=true;
var reslteShortWithLong: Boolean=true;
var resgtShortWithLong: Boolean=false;
var resgteShortWithLong: Boolean=false;
//.
var resaddShortWithFloat: Number=30010.5;
var resaddShortWithFloat_f: String="Float";
var ressubShortWithFloat: Number=29989.5;
var ressubShortWithFloat_f: String="Float";
var resmulShortWithFloat: Number=315000.0;
var resmulShortWithFloat_f: String="Float";
var resdivShortWithFloat: Number=2857.1428;
var resdivShortWithFloat_f: String="Float";
var resmodShortWithFloat: Number=1.5;
var resmodShortWithFloat_f: String="Float";
var reseqShortWithFloat: Boolean=false;
var resneqShortWithFloat: Boolean=true;
var resltShortWithFloat: Boolean=false;
var reslteShortWithFloat: Boolean=false;
var resgtShortWithFloat: Boolean=true;
var resgteShortWithFloat: Boolean=true;
//.
var resaddShortWithDouble: Number=42500.0;
var resaddShortWithDouble_f: String="Double";
var ressubShortWithDouble: Number=17500.0;
var ressubShortWithDouble_f: String="Double";
var resmulShortWithDouble: Number=3.75E8;
var resmulShortWithDouble_f: String="Double";
var resdivShortWithDouble: Number=2.4;
var resdivShortWithDouble_f: String="Double";
var resmodShortWithDouble: Number=5000.0;
var resmodShortWithDouble_f: String="Double";
var reseqShortWithDouble: Boolean=false;
var resneqShortWithDouble: Boolean=true;
var resltShortWithDouble: Boolean=false;
var reslteShortWithDouble: Boolean=false;
var resgtShortWithDouble: Boolean=true;
var resgteShortWithDouble: Boolean=true;
//.
var resaddShortWithNumber: Number=30100.0;
var resaddShortWithNumber_f: String="Float";
var ressubShortWithNumber: Number=29900.0;
var ressubShortWithNumber_f: String="Float";
var resmulShortWithNumber: Number=3000000.0;
var resmulShortWithNumber_f: String="Float";
var resdivShortWithNumber: Number=300.0;
var resdivShortWithNumber_f: String="Float";
var resmodShortWithNumber: Number=0.0;
var resmodShortWithNumber_f: String="Float";
var reseqShortWithNumber: Boolean=false;
var resneqShortWithNumber: Boolean=true;
var resltShortWithNumber: Boolean=false;
var reslteShortWithNumber: Boolean=false;
var resgtShortWithNumber: Boolean=true;
var resgteShortWithNumber: Boolean=true;
//.
var resaddCharacterWithByte: Number=184.0;
var resaddCharacterWithByte_f: String="Integer";
var ressubCharacterWithByte: Number=-56.0;
var ressubCharacterWithByte_f: String="Integer";
var resmulCharacterWithByte: Number=7680.0;
var resmulCharacterWithByte_f: String="Integer";
var resdivCharacterWithByte: Number=0.0;
var resdivCharacterWithByte_f: String="Integer";
var resmodCharacterWithByte: Number=64.0;
var resmodCharacterWithByte_f: String="Integer";
var reseqCharacterWithByte: Boolean=false;
var resneqCharacterWithByte: Boolean=true;
var resltCharacterWithByte: Boolean=true;
var reslteCharacterWithByte: Boolean=true;
var resgtCharacterWithByte: Boolean=false;
var resgteCharacterWithByte: Boolean=false;
//.
var resaddCharacterWithShort: Number=30064.0;
var resaddCharacterWithShort_f: String="Integer";
var ressubCharacterWithShort: Number=-29936.0;
var ressubCharacterWithShort_f: String="Integer";
var resmulCharacterWithShort: Number=1920000.0;
var resmulCharacterWithShort_f: String="Integer";
var resdivCharacterWithShort: Number=0.0;
var resdivCharacterWithShort_f: String="Integer";
var resmodCharacterWithShort: Number=64.0;
var resmodCharacterWithShort_f: String="Integer";
var reseqCharacterWithShort: Boolean=false;
var resneqCharacterWithShort: Boolean=true;
var resltCharacterWithShort: Boolean=true;
var reslteCharacterWithShort: Boolean=true;
var resgtCharacterWithShort: Boolean=false;
var resgteCharacterWithShort: Boolean=false;
//.
var resaddCharacterWithCharacter: Number=128.0;
var resaddCharacterWithCharacter_f: String="Integer";
var ressubCharacterWithCharacter: Number=0.0;
var ressubCharacterWithCharacter_f: String="Integer";
var resmulCharacterWithCharacter: Number=4096.0;
var resmulCharacterWithCharacter_f: String="Integer";
var resdivCharacterWithCharacter: Number=1.0;
var resdivCharacterWithCharacter_f: String="Integer";
var resmodCharacterWithCharacter: Number=0.0;
var resmodCharacterWithCharacter_f: String="Integer";
var reseqCharacterWithCharacter: Boolean=true;
var resneqCharacterWithCharacter: Boolean=false;
var resltCharacterWithCharacter: Boolean=false;
var reslteCharacterWithCharacter: Boolean=true;
var resgtCharacterWithCharacter: Boolean=false;
var resgteCharacterWithCharacter: Boolean=true;
//.
var resaddCharacterWithInteger: Number=1000064.0;
var resaddCharacterWithInteger_f: String="Integer";
var ressubCharacterWithInteger: Number=-999936.0;
var ressubCharacterWithInteger_f: String="Integer";
var resmulCharacterWithInteger: Number=6.4E7;
var resmulCharacterWithInteger_f: String="Integer";
var resdivCharacterWithInteger: Number=0.0;
var resdivCharacterWithInteger_f: String="Integer";
var resmodCharacterWithInteger: Number=64.0;
var resmodCharacterWithInteger_f: String="Integer";
var reseqCharacterWithInteger: Boolean=false;
var resneqCharacterWithInteger: Boolean=true;
var resltCharacterWithInteger: Boolean=true;
var reslteCharacterWithInteger: Boolean=true;
var resgtCharacterWithInteger: Boolean=false;
var resgteCharacterWithInteger: Boolean=false;
//.
var resaddCharacterWithLong: Number=1.00000006E9;
var resaddCharacterWithLong_f: String="Long";
var ressubCharacterWithLong: Number=-9.9999994E8;
var ressubCharacterWithLong_f: String="Long";
var resmulCharacterWithLong: Number=6.4E10;
var resmulCharacterWithLong_f: String="Long";
var resdivCharacterWithLong: Number=0.0;
var resdivCharacterWithLong_f: String="Long";
var resmodCharacterWithLong: Number=64.0;
var resmodCharacterWithLong_f: String="Long";
var reseqCharacterWithLong: Boolean=false;
var resneqCharacterWithLong: Boolean=true;
var resltCharacterWithLong: Boolean=true;
var reslteCharacterWithLong: Boolean=true;
var resgtCharacterWithLong: Boolean=false;
var resgteCharacterWithLong: Boolean=false;
//.
var resaddCharacterWithFloat: Number=74.5;
var resaddCharacterWithFloat_f: String="Float";
var ressubCharacterWithFloat: Number=53.5;
var ressubCharacterWithFloat_f: String="Float";
var resmulCharacterWithFloat: Number=672.0;
var resmulCharacterWithFloat_f: String="Float";
var resdivCharacterWithFloat: Number=6.095238;
var resdivCharacterWithFloat_f: String="Float";
var resmodCharacterWithFloat: Number=1.0;
var resmodCharacterWithFloat_f: String="Float";
var reseqCharacterWithFloat: Boolean=false;
var resneqCharacterWithFloat: Boolean=true;
var resltCharacterWithFloat: Boolean=false;
var reslteCharacterWithFloat: Boolean=false;
var resgtCharacterWithFloat: Boolean=true;
var resgteCharacterWithFloat: Boolean=true;
//.
var resaddCharacterWithDouble: Number=12564.0;
var resaddCharacterWithDouble_f: String="Double";
var ressubCharacterWithDouble: Number=-12436.0;
var ressubCharacterWithDouble_f: String="Double";
var resmulCharacterWithDouble: Number=800000.0;
var resmulCharacterWithDouble_f: String="Double";
var resdivCharacterWithDouble: Number=0.00512;
var resdivCharacterWithDouble_f: String="Double";
var resmodCharacterWithDouble: Number=64.0;
var resmodCharacterWithDouble_f: String="Double";
var reseqCharacterWithDouble: Boolean=false;
var resneqCharacterWithDouble: Boolean=true;
var resltCharacterWithDouble: Boolean=true;
var reslteCharacterWithDouble: Boolean=true;
var resgtCharacterWithDouble: Boolean=false;
var resgteCharacterWithDouble: Boolean=false;
//.
var resaddCharacterWithNumber: Number=164.0;
var resaddCharacterWithNumber_f: String="Float";
var ressubCharacterWithNumber: Number=-36.0;
var ressubCharacterWithNumber_f: String="Float";
var resmulCharacterWithNumber: Number=6400.0;
var resmulCharacterWithNumber_f: String="Float";
var resdivCharacterWithNumber: Number=0.64;
var resdivCharacterWithNumber_f: String="Float";
var resmodCharacterWithNumber: Number=64.0;
var resmodCharacterWithNumber_f: String="Float";
var reseqCharacterWithNumber: Boolean=false;
var resneqCharacterWithNumber: Boolean=true;
var resltCharacterWithNumber: Boolean=true;
var reslteCharacterWithNumber: Boolean=true;
var resgtCharacterWithNumber: Boolean=false;
var resgteCharacterWithNumber: Boolean=false;
//.
var resaddIntegerWithByte: Number=1000120.0;
var resaddIntegerWithByte_f: String="Integer";
var ressubIntegerWithByte: Number=999880.0;
var ressubIntegerWithByte_f: String="Integer";
var resmulIntegerWithByte: Number=1.2E8;
var resmulIntegerWithByte_f: String="Integer";
var resdivIntegerWithByte: Number=8333.0;
var resdivIntegerWithByte_f: String="Integer";
var resmodIntegerWithByte: Number=40.0;
var resmodIntegerWithByte_f: String="Integer";
var reseqIntegerWithByte: Boolean=false;
var resneqIntegerWithByte: Boolean=true;
var resltIntegerWithByte: Boolean=false;
var reslteIntegerWithByte: Boolean=false;
var resgtIntegerWithByte: Boolean=true;
var resgteIntegerWithByte: Boolean=true;
//.
var resaddIntegerWithShort: Number=1030000.0;
var resaddIntegerWithShort_f: String="Integer";
var ressubIntegerWithShort: Number=970000.0;
var ressubIntegerWithShort_f: String="Integer";
var resmulIntegerWithShort: Number=-6.4771072E7;
var resmulIntegerWithShort_f: String="Integer";
var resdivIntegerWithShort: Number=33.0;
var resdivIntegerWithShort_f: String="Integer";
var resmodIntegerWithShort: Number=10000.0;
var resmodIntegerWithShort_f: String="Integer";
var reseqIntegerWithShort: Boolean=false;
var resneqIntegerWithShort: Boolean=true;
var resltIntegerWithShort: Boolean=false;
var reslteIntegerWithShort: Boolean=false;
var resgtIntegerWithShort: Boolean=true;
var resgteIntegerWithShort: Boolean=true;
//.
var resaddIntegerWithCharacter: Number=1000064.0;
var resaddIntegerWithCharacter_f: String="Integer";
var ressubIntegerWithCharacter: Number=999936.0;
var ressubIntegerWithCharacter_f: String="Integer";
var resmulIntegerWithCharacter: Number=6.4E7;
var resmulIntegerWithCharacter_f: String="Integer";
var resdivIntegerWithCharacter: Number=15625.0;
var resdivIntegerWithCharacter_f: String="Integer";
var resmodIntegerWithCharacter: Number=0.0;
var resmodIntegerWithCharacter_f: String="Integer";
var reseqIntegerWithCharacter: Boolean=false;
var resneqIntegerWithCharacter: Boolean=true;
var resltIntegerWithCharacter: Boolean=false;
var reslteIntegerWithCharacter: Boolean=false;
var resgtIntegerWithCharacter: Boolean=true;
var resgteIntegerWithCharacter: Boolean=true;
//.
var resaddIntegerWithInteger: Number=2000000.0;
var resaddIntegerWithInteger_f: String="Integer";
var ressubIntegerWithInteger: Number=0.0;
var ressubIntegerWithInteger_f: String="Integer";
var resmulIntegerWithInteger: Number=-7.2737997E8;
var resmulIntegerWithInteger_f: String="Integer";
var resdivIntegerWithInteger: Number=1.0;
var resdivIntegerWithInteger_f: String="Integer";
var resmodIntegerWithInteger: Number=0.0;
var resmodIntegerWithInteger_f: String="Integer";
var reseqIntegerWithInteger: Boolean=true;
var resneqIntegerWithInteger: Boolean=false;
var resltIntegerWithInteger: Boolean=false;
var reslteIntegerWithInteger: Boolean=true;
var resgtIntegerWithInteger: Boolean=false;
var resgteIntegerWithInteger: Boolean=true;
//.
var resaddIntegerWithLong: Number=1.001E9;
var resaddIntegerWithLong_f: String="Long";
var ressubIntegerWithLong: Number=-9.99E8;
var ressubIntegerWithLong_f: String="Long";
var resmulIntegerWithLong: Number=9.9999999E14;
var resmulIntegerWithLong_f: String="Long";
var resdivIntegerWithLong: Number=0.0;
var resdivIntegerWithLong_f: String="Long";
var resmodIntegerWithLong: Number=1000000.0;
var resmodIntegerWithLong_f: String="Long";
var reseqIntegerWithLong: Boolean=false;
var resneqIntegerWithLong: Boolean=true;
var resltIntegerWithLong: Boolean=true;
var reslteIntegerWithLong: Boolean=true;
var resgtIntegerWithLong: Boolean=false;
var resgteIntegerWithLong: Boolean=false;
//.
var resaddIntegerWithFloat: Number=1000010.5;
var resaddIntegerWithFloat_f: String="Float";
var ressubIntegerWithFloat: Number=999989.5;
var ressubIntegerWithFloat_f: String="Float";
var resmulIntegerWithFloat: Number=1.05E7;
var resmulIntegerWithFloat_f: String="Float";
var resdivIntegerWithFloat: Number=95238.09;
var resdivIntegerWithFloat_f: String="Float";
var resmodIntegerWithFloat: Number=1.0;
var resmodIntegerWithFloat_f: String="Float";
var reseqIntegerWithFloat: Boolean=false;
var resneqIntegerWithFloat: Boolean=true;
var resltIntegerWithFloat: Boolean=false;
var reslteIntegerWithFloat: Boolean=false;
var resgtIntegerWithFloat: Boolean=true;
var resgteIntegerWithFloat: Boolean=true;
//.
var resaddIntegerWithDouble: Number=1012500.0;
var resaddIntegerWithDouble_f: String="Double";
var ressubIntegerWithDouble: Number=987500.0;
var ressubIntegerWithDouble_f: String="Double";
var resmulIntegerWithDouble: Number=1.24999997E10;
var resmulIntegerWithDouble_f: String="Double";
var resdivIntegerWithDouble: Number=80.0;
var resdivIntegerWithDouble_f: String="Double";
var resmodIntegerWithDouble: Number=0.0;
var resmodIntegerWithDouble_f: String="Double";
var reseqIntegerWithDouble: Boolean=false;
var resneqIntegerWithDouble: Boolean=true;
var resltIntegerWithDouble: Boolean=false;
var reslteIntegerWithDouble: Boolean=false;
var resgtIntegerWithDouble: Boolean=true;
var resgteIntegerWithDouble: Boolean=true;
//.
var resaddIntegerWithNumber: Number=1000100.0;
var resaddIntegerWithNumber_f: String="Float";
var ressubIntegerWithNumber: Number=999900.0;
var ressubIntegerWithNumber_f: String="Float";
var resmulIntegerWithNumber: Number=1.0E8;
var resmulIntegerWithNumber_f: String="Float";
var resdivIntegerWithNumber: Number=10000.0;
var resdivIntegerWithNumber_f: String="Float";
var resmodIntegerWithNumber: Number=0.0;
var resmodIntegerWithNumber_f: String="Float";
var reseqIntegerWithNumber: Boolean=false;
var resneqIntegerWithNumber: Boolean=true;
var resltIntegerWithNumber: Boolean=false;
var reslteIntegerWithNumber: Boolean=false;
var resgtIntegerWithNumber: Boolean=true;
var resgteIntegerWithNumber: Boolean=true;
//.
var resaddLongWithByte: Number=1.00000013E9;
var resaddLongWithByte_f: String="Long";
var ressubLongWithByte: Number=9.9999987E8;
var ressubLongWithByte_f: String="Long";
var resmulLongWithByte: Number=1.20000004E11;
var resmulLongWithByte_f: String="Long";
var resdivLongWithByte: Number=8333333.0;
var resdivLongWithByte_f: String="Long";
var resmodLongWithByte: Number=40.0;
var resmodLongWithByte_f: String="Long";
var reseqLongWithByte: Boolean=false;
var resneqLongWithByte: Boolean=true;
var resltLongWithByte: Boolean=false;
var reslteLongWithByte: Boolean=false;
var resgtLongWithByte: Boolean=true;
var resgteLongWithByte: Boolean=true;
//.
var resaddLongWithShort: Number=1.00003002E9;
var resaddLongWithShort_f: String="Long";
var ressubLongWithShort: Number=9.9996998E8;
var ressubLongWithShort_f: String="Long";
var resmulLongWithShort: Number=3.00000005E13;
var resmulLongWithShort_f: String="Long";
var resdivLongWithShort: Number=33333.0;
var resdivLongWithShort_f: String="Long";
var resmodLongWithShort: Number=10000.0;
var resmodLongWithShort_f: String="Long";
var reseqLongWithShort: Boolean=false;
var resneqLongWithShort: Boolean=true;
var resltLongWithShort: Boolean=false;
var reslteLongWithShort: Boolean=false;
var resgtLongWithShort: Boolean=true;
var resgteLongWithShort: Boolean=true;
//.
var resaddLongWithCharacter: Number=1.00000006E9;
var resaddLongWithCharacter_f: String="Long";
var ressubLongWithCharacter: Number=9.9999994E8;
var ressubLongWithCharacter_f: String="Long";
var resmulLongWithCharacter: Number=6.4E10;
var resmulLongWithCharacter_f: String="Long";
var resdivLongWithCharacter: Number=1.5625E7;
var resdivLongWithCharacter_f: String="Long";
var resmodLongWithCharacter: Number=0.0;
var resmodLongWithCharacter_f: String="Long";
var reseqLongWithCharacter: Boolean=false;
var resneqLongWithCharacter: Boolean=true;
var resltLongWithCharacter: Boolean=false;
var reslteLongWithCharacter: Boolean=false;
var resgtLongWithCharacter: Boolean=true;
var resgteLongWithCharacter: Boolean=true;
//.
var resaddLongWithInteger: Number=1.001E9;
var resaddLongWithInteger_f: String="Long";
var ressubLongWithInteger: Number=9.99E8;
var ressubLongWithInteger_f: String="Long";
var resmulLongWithInteger: Number=9.9999999E14;
var resmulLongWithInteger_f: String="Long";
var resdivLongWithInteger: Number=1000.0;
var resdivLongWithInteger_f: String="Long";
var resmodLongWithInteger: Number=0.0;
var resmodLongWithInteger_f: String="Long";
var reseqLongWithInteger: Boolean=false;
var resneqLongWithInteger: Boolean=true;
var resltLongWithInteger: Boolean=false;
var reslteLongWithInteger: Boolean=false;
var resgtLongWithInteger: Boolean=true;
var resgteLongWithInteger: Boolean=true;
//.
var resaddLongWithLong: Number=2.0E9;
var resaddLongWithLong_f: String="Long";
var ressubLongWithLong: Number=0.0;
var ressubLongWithLong_f: String="Long";
var resmulLongWithLong: Number=9.9999998E17;
var resmulLongWithLong_f: String="Long";
var resdivLongWithLong: Number=1.0;
var resdivLongWithLong_f: String="Long";
var resmodLongWithLong: Number=0.0;
var resmodLongWithLong_f: String="Long";
var reseqLongWithLong: Boolean=true;
var resneqLongWithLong: Boolean=false;
var resltLongWithLong: Boolean=false;
var reslteLongWithLong: Boolean=true;
var resgtLongWithLong: Boolean=false;
var resgteLongWithLong: Boolean=true;
//.
var resaddLongWithFloat: Number=1.0E9;
var resaddLongWithFloat_f: String="Float";
var ressubLongWithFloat: Number=1.0E9;
var ressubLongWithFloat_f: String="Float";
var resmulLongWithFloat: Number=1.04999997E10;
var resmulLongWithFloat_f: String="Float";
var resdivLongWithFloat: Number=9.5238096E7;
var resdivLongWithFloat_f: String="Float";
var resmodLongWithFloat: Number=2.5;
var resmodLongWithFloat_f: String="Float";
var reseqLongWithFloat: Boolean=false;
var resneqLongWithFloat: Boolean=true;
var resltLongWithFloat: Boolean=false;
var reslteLongWithFloat: Boolean=false;
var resgtLongWithFloat: Boolean=true;
var resgteLongWithFloat: Boolean=true;
//.
var resaddLongWithDouble: Number=1.00001248E9;
var resaddLongWithDouble_f: String="Double";
var ressubLongWithDouble: Number=9.9998752E8;
var ressubLongWithDouble_f: String="Double";
var resmulLongWithDouble: Number=1.25E13;
var resmulLongWithDouble_f: String="Double";
var resdivLongWithDouble: Number=80000.0;
var resdivLongWithDouble_f: String="Double";
var resmodLongWithDouble: Number=0.0;
var resmodLongWithDouble_f: String="Double";
var reseqLongWithDouble: Boolean=false;
var resneqLongWithDouble: Boolean=true;
var resltLongWithDouble: Boolean=false;
var reslteLongWithDouble: Boolean=false;
var resgtLongWithDouble: Boolean=true;
var resgteLongWithDouble: Boolean=true;
//.
var resaddLongWithNumber: Number=1.00000013E9;
var resaddLongWithNumber_f: String="Float";
var ressubLongWithNumber: Number=9.9999987E8;
var ressubLongWithNumber_f: String="Float";
var resmulLongWithNumber: Number=9.9999998E10;
var resmulLongWithNumber_f: String="Float";
var resdivLongWithNumber: Number=1.0E7;
var resdivLongWithNumber_f: String="Float";
var resmodLongWithNumber: Number=0.0;
var resmodLongWithNumber_f: String="Float";
var reseqLongWithNumber: Boolean=false;
var resneqLongWithNumber: Boolean=true;
var resltLongWithNumber: Boolean=false;
var reslteLongWithNumber: Boolean=false;
var resgtLongWithNumber: Boolean=true;
var resgteLongWithNumber: Boolean=true;
//.
var resaddFloatWithByte: Number=130.5;
var resaddFloatWithByte_f: String="Float";
var ressubFloatWithByte: Number=-109.5;
var ressubFloatWithByte_f: String="Float";
var resmulFloatWithByte: Number=1260.0;
var resmulFloatWithByte_f: String="Float";
var resdivFloatWithByte: Number=0.0875;
var resdivFloatWithByte_f: String="Float";
var resmodFloatWithByte: Number=10.5;
var resmodFloatWithByte_f: String="Float";
var reseqFloatWithByte: Boolean=false;
var resneqFloatWithByte: Boolean=true;
var resltFloatWithByte: Boolean=true;
var reslteFloatWithByte: Boolean=true;
var resgtFloatWithByte: Boolean=false;
var resgteFloatWithByte: Boolean=false;
//.
var resaddFloatWithShort: Number=30010.5;
var resaddFloatWithShort_f: String="Float";
var ressubFloatWithShort: Number=-29989.5;
var ressubFloatWithShort_f: String="Float";
var resmulFloatWithShort: Number=315000.0;
var resmulFloatWithShort_f: String="Float";
var resdivFloatWithShort: Number=3.5E-4;
var resdivFloatWithShort_f: String="Float";
var resmodFloatWithShort: Number=10.5;
var resmodFloatWithShort_f: String="Float";
var reseqFloatWithShort: Boolean=false;
var resneqFloatWithShort: Boolean=true;
var resltFloatWithShort: Boolean=true;
var reslteFloatWithShort: Boolean=true;
var resgtFloatWithShort: Boolean=false;
var resgteFloatWithShort: Boolean=false;
//.
var resaddFloatWithCharacter: Number=74.5;
var resaddFloatWithCharacter_f: String="Float";
var ressubFloatWithCharacter: Number=-53.5;
var ressubFloatWithCharacter_f: String="Float";
var resmulFloatWithCharacter: Number=672.0;
var resmulFloatWithCharacter_f: String="Float";
var resdivFloatWithCharacter: Number=0.1640625;
var resdivFloatWithCharacter_f: String="Float";
var resmodFloatWithCharacter: Number=10.5;
var resmodFloatWithCharacter_f: String="Float";
var reseqFloatWithCharacter: Boolean=false;
var resneqFloatWithCharacter: Boolean=true;
var resltFloatWithCharacter: Boolean=true;
var reslteFloatWithCharacter: Boolean=true;
var resgtFloatWithCharacter: Boolean=false;
var resgteFloatWithCharacter: Boolean=false;
//.
var resaddFloatWithInteger: Number=1000010.5;
var resaddFloatWithInteger_f: String="Float";
var ressubFloatWithInteger: Number=-999989.5;
var ressubFloatWithInteger_f: String="Float";
var resmulFloatWithInteger: Number=1.05E7;
var resmulFloatWithInteger_f: String="Float";
var resdivFloatWithInteger: Number=1.05E-5;
var resdivFloatWithInteger_f: String="Float";
var resmodFloatWithInteger: Number=10.5;
var resmodFloatWithInteger_f: String="Float";
var reseqFloatWithInteger: Boolean=false;
var resneqFloatWithInteger: Boolean=true;
var resltFloatWithInteger: Boolean=true;
var reslteFloatWithInteger: Boolean=true;
var resgtFloatWithInteger: Boolean=false;
var resgteFloatWithInteger: Boolean=false;
//.
var resaddFloatWithLong: Number=1.0E9;
var resaddFloatWithLong_f: String="Float";
var ressubFloatWithLong: Number=-1.0E9;
var ressubFloatWithLong_f: String="Float";
var resmulFloatWithLong: Number=1.04999997E10;
var resmulFloatWithLong_f: String="Float";
var resdivFloatWithLong: Number=1.05E-8;
var resdivFloatWithLong_f: String="Float";
var resmodFloatWithLong: Number=10.5;
var resmodFloatWithLong_f: String="Float";
var reseqFloatWithLong: Boolean=false;
var resneqFloatWithLong: Boolean=true;
var resltFloatWithLong: Boolean=true;
var reslteFloatWithLong: Boolean=true;
var resgtFloatWithLong: Boolean=false;
var resgteFloatWithLong: Boolean=false;
//.
var resaddFloatWithFloat: Number=21.0;
var resaddFloatWithFloat_f: String="Float";
var ressubFloatWithFloat: Number=0.0;
var ressubFloatWithFloat_f: String="Float";
var resmulFloatWithFloat: Number=110.25;
var resmulFloatWithFloat_f: String="Float";
var resdivFloatWithFloat: Number=1.0;
var resdivFloatWithFloat_f: String="Float";
var resmodFloatWithFloat: Number=0.0;
var resmodFloatWithFloat_f: String="Float";
var reseqFloatWithFloat: Boolean=true;
var resneqFloatWithFloat: Boolean=false;
var resltFloatWithFloat: Boolean=false;
var reslteFloatWithFloat: Boolean=true;
var resgtFloatWithFloat: Boolean=false;
var resgteFloatWithFloat: Boolean=true;
//.
var resaddFloatWithDouble: Number=12510.5;
var resaddFloatWithDouble_f: String="Double";
var ressubFloatWithDouble: Number=-12489.5;
var ressubFloatWithDouble_f: String="Double";
var resmulFloatWithDouble: Number=131250.0;
var resmulFloatWithDouble_f: String="Double";
var resdivFloatWithDouble: Number=8.4E-4;
var resdivFloatWithDouble_f: String="Double";
var resmodFloatWithDouble: Number=10.5;
var resmodFloatWithDouble_f: String="Double";
var reseqFloatWithDouble: Boolean=false;
var resneqFloatWithDouble: Boolean=true;
var resltFloatWithDouble: Boolean=true;
var reslteFloatWithDouble: Boolean=true;
var resgtFloatWithDouble: Boolean=false;
var resgteFloatWithDouble: Boolean=false;
//.
var resaddFloatWithNumber: Number=110.5;
var resaddFloatWithNumber_f: String="Float";
var ressubFloatWithNumber: Number=-89.5;
var ressubFloatWithNumber_f: String="Float";
var resmulFloatWithNumber: Number=1050.0;
var resmulFloatWithNumber_f: String="Float";
var resdivFloatWithNumber: Number=0.105;
var resdivFloatWithNumber_f: String="Float";
var resmodFloatWithNumber: Number=10.5;
var resmodFloatWithNumber_f: String="Float";
var reseqFloatWithNumber: Boolean=false;
var resneqFloatWithNumber: Boolean=true;
var resltFloatWithNumber: Boolean=true;
var reslteFloatWithNumber: Boolean=true;
var resgtFloatWithNumber: Boolean=false;
var resgteFloatWithNumber: Boolean=false;
//.
var resaddDoubleWithByte: Number=12620.0;
var resaddDoubleWithByte_f: String="Double";
var ressubDoubleWithByte: Number=12380.0;
var ressubDoubleWithByte_f: String="Double";
var resmulDoubleWithByte: Number=1500000.0;
var resmulDoubleWithByte_f: String="Double";
var resdivDoubleWithByte: Number=104.166664;
var resdivDoubleWithByte_f: String="Double";
var resmodDoubleWithByte: Number=20.0;
var resmodDoubleWithByte_f: String="Double";
var reseqDoubleWithByte: Boolean=false;
var resneqDoubleWithByte: Boolean=true;
var resltDoubleWithByte: Boolean=false;
var reslteDoubleWithByte: Boolean=false;
var resgtDoubleWithByte: Boolean=true;
var resgteDoubleWithByte: Boolean=true;
//.
var resaddDoubleWithShort: Number=42500.0;
var resaddDoubleWithShort_f: String="Double";
var ressubDoubleWithShort: Number=-17500.0;
var ressubDoubleWithShort_f: String="Double";
var resmulDoubleWithShort: Number=3.75E8;
var resmulDoubleWithShort_f: String="Double";
var resdivDoubleWithShort: Number=0.41666666;
var resdivDoubleWithShort_f: String="Double";
var resmodDoubleWithShort: Number=12500.0;
var resmodDoubleWithShort_f: String="Double";
var reseqDoubleWithShort: Boolean=false;
var resneqDoubleWithShort: Boolean=true;
var resltDoubleWithShort: Boolean=true;
var reslteDoubleWithShort: Boolean=true;
var resgtDoubleWithShort: Boolean=false;
var resgteDoubleWithShort: Boolean=false;
//.
var resaddDoubleWithCharacter: Number=12564.0;
var resaddDoubleWithCharacter_f: String="Double";
var ressubDoubleWithCharacter: Number=12436.0;
var ressubDoubleWithCharacter_f: String="Double";
var resmulDoubleWithCharacter: Number=800000.0;
var resmulDoubleWithCharacter_f: String="Double";
var resdivDoubleWithCharacter: Number=195.3125;
var resdivDoubleWithCharacter_f: String="Double";
var resmodDoubleWithCharacter: Number=20.0;
var resmodDoubleWithCharacter_f: String="Double";
var reseqDoubleWithCharacter: Boolean=false;
var resneqDoubleWithCharacter: Boolean=true;
var resltDoubleWithCharacter: Boolean=false;
var reslteDoubleWithCharacter: Boolean=false;
var resgtDoubleWithCharacter: Boolean=true;
var resgteDoubleWithCharacter: Boolean=true;
//.
var resaddDoubleWithInteger: Number=1012500.0;
var resaddDoubleWithInteger_f: String="Double";
var ressubDoubleWithInteger: Number=-987500.0;
var ressubDoubleWithInteger_f: String="Double";
var resmulDoubleWithInteger: Number=1.24999997E10;
var resmulDoubleWithInteger_f: String="Double";
var resdivDoubleWithInteger: Number=0.0125;
var resdivDoubleWithInteger_f: String="Double";
var resmodDoubleWithInteger: Number=12500.0;
var resmodDoubleWithInteger_f: String="Double";
var reseqDoubleWithInteger: Boolean=false;
var resneqDoubleWithInteger: Boolean=true;
var resltDoubleWithInteger: Boolean=true;
var reslteDoubleWithInteger: Boolean=true;
var resgtDoubleWithInteger: Boolean=false;
var resgteDoubleWithInteger: Boolean=false;
//.
var resaddDoubleWithLong: Number=1.00001248E9;
var resaddDoubleWithLong_f: String="Double";
var ressubDoubleWithLong: Number=-9.9998752E8;
var ressubDoubleWithLong_f: String="Double";
var resmulDoubleWithLong: Number=1.25E13;
var resmulDoubleWithLong_f: String="Double";
var resdivDoubleWithLong: Number=1.25E-5;
var resdivDoubleWithLong_f: String="Double";
var resmodDoubleWithLong: Number=12500.0;
var resmodDoubleWithLong_f: String="Double";
var reseqDoubleWithLong: Boolean=false;
var resneqDoubleWithLong: Boolean=true;
var resltDoubleWithLong: Boolean=true;
var reslteDoubleWithLong: Boolean=true;
var resgtDoubleWithLong: Boolean=false;
var resgteDoubleWithLong: Boolean=false;
//.
var resaddDoubleWithFloat: Number=12510.5;
var resaddDoubleWithFloat_f: String="Double";
var ressubDoubleWithFloat: Number=12489.5;
var ressubDoubleWithFloat_f: String="Double";
var resmulDoubleWithFloat: Number=131250.0;
var resmulDoubleWithFloat_f: String="Double";
var resdivDoubleWithFloat: Number=1190.4762;
var resdivDoubleWithFloat_f: String="Double";
var resmodDoubleWithFloat: Number=5.0;
var resmodDoubleWithFloat_f: String="Double";
var reseqDoubleWithFloat: Boolean=false;
var resneqDoubleWithFloat: Boolean=true;
var resltDoubleWithFloat: Boolean=false;
var reslteDoubleWithFloat: Boolean=false;
var resgtDoubleWithFloat: Boolean=true;
var resgteDoubleWithFloat: Boolean=true;
//.
var resaddDoubleWithDouble: Number=25000.0;
var resaddDoubleWithDouble_f: String="Double";
var ressubDoubleWithDouble: Number=0.0;
var ressubDoubleWithDouble_f: String="Double";
var resmulDoubleWithDouble: Number=1.5625E8;
var resmulDoubleWithDouble_f: String="Double";
var resdivDoubleWithDouble: Number=1.0;
var resdivDoubleWithDouble_f: String="Double";
var resmodDoubleWithDouble: Number=0.0;
var resmodDoubleWithDouble_f: String="Double";
var reseqDoubleWithDouble: Boolean=true;
var resneqDoubleWithDouble: Boolean=false;
var resltDoubleWithDouble: Boolean=false;
var reslteDoubleWithDouble: Boolean=true;
var resgtDoubleWithDouble: Boolean=false;
var resgteDoubleWithDouble: Boolean=true;
//.
var resaddDoubleWithNumber: Number=12600.0;
var resaddDoubleWithNumber_f: String="Double";
var ressubDoubleWithNumber: Number=12400.0;
var ressubDoubleWithNumber_f: String="Double";
var resmulDoubleWithNumber: Number=1250000.0;
var resmulDoubleWithNumber_f: String="Double";
var resdivDoubleWithNumber: Number=125.0;
var resdivDoubleWithNumber_f: String="Double";
var resmodDoubleWithNumber: Number=0.0;
var resmodDoubleWithNumber_f: String="Double";
var reseqDoubleWithNumber: Boolean=false;
var resneqDoubleWithNumber: Boolean=true;
var resltDoubleWithNumber: Boolean=false;
var reslteDoubleWithNumber: Boolean=false;
var resgtDoubleWithNumber: Boolean=true;
var resgteDoubleWithNumber: Boolean=true;
//.
var resaddNumberWithByte: Number=220.0;
var resaddNumberWithByte_f: String="Float";
var ressubNumberWithByte: Number=-20.0;
var ressubNumberWithByte_f: String="Float";
var resmulNumberWithByte: Number=12000.0;
var resmulNumberWithByte_f: String="Float";
var resdivNumberWithByte: Number=0.8333333;
var resdivNumberWithByte_f: String="Float";
var resmodNumberWithByte: Number=100.0;
var resmodNumberWithByte_f: String="Float";
var reseqNumberWithByte: Boolean=false;
var resneqNumberWithByte: Boolean=true;
var resltNumberWithByte: Boolean=true;
var reslteNumberWithByte: Boolean=true;
var resgtNumberWithByte: Boolean=false;
var resgteNumberWithByte: Boolean=false;
//.
var resaddNumberWithShort: Number=30100.0;
var resaddNumberWithShort_f: String="Float";
var ressubNumberWithShort: Number=-29900.0;
var ressubNumberWithShort_f: String="Float";
var resmulNumberWithShort: Number=3000000.0;
var resmulNumberWithShort_f: String="Float";
var resdivNumberWithShort: Number=0.0033333334;
var resdivNumberWithShort_f: String="Float";
var resmodNumberWithShort: Number=100.0;
var resmodNumberWithShort_f: String="Float";
var reseqNumberWithShort: Boolean=false;
var resneqNumberWithShort: Boolean=true;
var resltNumberWithShort: Boolean=true;
var reslteNumberWithShort: Boolean=true;
var resgtNumberWithShort: Boolean=false;
var resgteNumberWithShort: Boolean=false;
//.
var resaddNumberWithCharacter: Number=164.0;
var resaddNumberWithCharacter_f: String="Float";
var ressubNumberWithCharacter: Number=36.0;
var ressubNumberWithCharacter_f: String="Float";
var resmulNumberWithCharacter: Number=6400.0;
var resmulNumberWithCharacter_f: String="Float";
var resdivNumberWithCharacter: Number=1.5625;
var resdivNumberWithCharacter_f: String="Float";
var resmodNumberWithCharacter: Number=36.0;
var resmodNumberWithCharacter_f: String="Float";
var reseqNumberWithCharacter: Boolean=false;
var resneqNumberWithCharacter: Boolean=true;
var resltNumberWithCharacter: Boolean=false;
var reslteNumberWithCharacter: Boolean=false;
var resgtNumberWithCharacter: Boolean=true;
var resgteNumberWithCharacter: Boolean=true;
//.
var resaddNumberWithInteger: Number=1000100.0;
var resaddNumberWithInteger_f: String="Float";
var ressubNumberWithInteger: Number=-999900.0;
var ressubNumberWithInteger_f: String="Float";
var resmulNumberWithInteger: Number=1.0E8;
var resmulNumberWithInteger_f: String="Float";
var resdivNumberWithInteger: Number=1.0E-4;
var resdivNumberWithInteger_f: String="Float";
var resmodNumberWithInteger: Number=100.0;
var resmodNumberWithInteger_f: String="Float";
var reseqNumberWithInteger: Boolean=false;
var resneqNumberWithInteger: Boolean=true;
var resltNumberWithInteger: Boolean=true;
var reslteNumberWithInteger: Boolean=true;
var resgtNumberWithInteger: Boolean=false;
var resgteNumberWithInteger: Boolean=false;
//.
var resaddNumberWithLong: Number=1.00000013E9;
var resaddNumberWithLong_f: String="Float";
var ressubNumberWithLong: Number=-9.9999987E8;
var ressubNumberWithLong_f: String="Float";
var resmulNumberWithLong: Number=9.9999998E10;
var resmulNumberWithLong_f: String="Float";
var resdivNumberWithLong: Number=1.0E-7;
var resdivNumberWithLong_f: String="Float";
var resmodNumberWithLong: Number=100.0;
var resmodNumberWithLong_f: String="Float";
var reseqNumberWithLong: Boolean=false;
var resneqNumberWithLong: Boolean=true;
var resltNumberWithLong: Boolean=true;
var reslteNumberWithLong: Boolean=true;
var resgtNumberWithLong: Boolean=false;
var resgteNumberWithLong: Boolean=false;
//.
var resaddNumberWithFloat: Number=110.5;
var resaddNumberWithFloat_f: String="Float";
var ressubNumberWithFloat: Number=89.5;
var ressubNumberWithFloat_f: String="Float";
var resmulNumberWithFloat: Number=1050.0;
var resmulNumberWithFloat_f: String="Float";
var resdivNumberWithFloat: Number=9.523809;
var resdivNumberWithFloat_f: String="Float";
var resmodNumberWithFloat: Number=5.5;
var resmodNumberWithFloat_f: String="Float";
var reseqNumberWithFloat: Boolean=false;
var resneqNumberWithFloat: Boolean=true;
var resltNumberWithFloat: Boolean=false;
var reslteNumberWithFloat: Boolean=false;
var resgtNumberWithFloat: Boolean=true;
var resgteNumberWithFloat: Boolean=true;
//.
var resaddNumberWithDouble: Number=12600.0;
var resaddNumberWithDouble_f: String="Double";
var ressubNumberWithDouble: Number=-12400.0;
var ressubNumberWithDouble_f: String="Double";
var resmulNumberWithDouble: Number=1250000.0;
var resmulNumberWithDouble_f: String="Double";
var resdivNumberWithDouble: Number=0.0080;
var resdivNumberWithDouble_f: String="Double";
var resmodNumberWithDouble: Number=100.0;
var resmodNumberWithDouble_f: String="Double";
var reseqNumberWithDouble: Boolean=false;
var resneqNumberWithDouble: Boolean=true;
var resltNumberWithDouble: Boolean=true;
var reslteNumberWithDouble: Boolean=true;
var resgtNumberWithDouble: Boolean=false;
var resgteNumberWithDouble: Boolean=false;
//.
var resaddNumberWithNumber: Number=200.0;
var resaddNumberWithNumber_f: String="Float";
var ressubNumberWithNumber: Number=0.0;
var ressubNumberWithNumber_f: String="Float";
var resmulNumberWithNumber: Number=10000.0;
var resmulNumberWithNumber_f: String="Float";
var resdivNumberWithNumber: Number=1.0;
var resdivNumberWithNumber_f: String="Float";
var resmodNumberWithNumber: Number=0.0;
var resmodNumberWithNumber_f: String="Float";
var reseqNumberWithNumber: Boolean=true;
var resneqNumberWithNumber: Boolean=false;
var resltNumberWithNumber: Boolean=false;
var reslteNumberWithNumber: Boolean=true;
var resgtNumberWithNumber: Boolean=false;
var resgteNumberWithNumber: Boolean=true;

}


