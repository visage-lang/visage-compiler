/**
 * Regression test JFXC-2070 : Compilation of script fails if String sequence contains null some times
 *
 * @test
 * @run
 */

var sseq:String[];
sseq = ["JavaFX",null];
println(sseq);
sseq = ["JavaFX", if (false) "BAD" else null];
println(sseq);

var bsseq:String[] = bind ["JavaFX",null];
println(bsseq);
var bsseq2:String[] = bind ["JavaFX", if (false) "BAD" else null];
println(bsseq2);

var oseq:Object[];
oseq = ["JavaFX",null];
println(oseq);
oseq = ["JavaFX",fo(null)];
println(oseq);

var boseq:Object[] = bind ["JavaFX", null];
println(boseq);
var boseq2:Object[] = bind ["JavaFX", fo(null)];
println(boseq2);

function fo(o:Object):Object {
  o
}
