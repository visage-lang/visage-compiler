/**
 * JFXC-2772 : PointerFactory missing new numerics
 *
 * @test
 * @run
 */

import com.sun.javafx.runtime.Pointer;
import com.sun.javafx.runtime.PointerFactory;

function run() {
   var pf = PointerFactory { };

   var bb : Byte = 1;
   var ss : Short = 2;
   var ii : Integer = 3;
   var ll : Long = 4;
   var ff : Float = 5.0;
   var dd : Double = 6.0;

   var bbbp = bind pf.make(bb);
   var bbp = bbbp.unwrap();
   var bbg = bbp.get();
   println(bbg);

   var bssp = bind pf.make(ss);
   var ssp = bssp.unwrap();
   var ssg = ssp.get();
   println(ssg);

   var biip = bind pf.make(ii);
   var iip = biip.unwrap();
   var iig = iip.get();
   println(iig);

   var bllp = bind pf.make(ll);
   var llp = bllp.unwrap();
   var llg = llp.get();
   println(llg);

   var bffp = bind pf.make(ff);
   var ffp = bffp.unwrap();
   var ffg = ffp.get();
   println(ffg);

   var bddp = bind pf.make(dd);
   var ddp = bddp.unwrap();
   var ddg = ddp.get();
   println(ddg);
}
