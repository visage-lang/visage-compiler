/*
 * @test
 * @run
 */

class sha {
  var seq1 = ['a', 'b', 'c', 'd', 'e', 'f', 'g'];
  var mir1 = seq1;

  function doit() {
    insert "@" before seq1[0];
    println(mir1);
    delete seq1[0..4];
    println(mir1);
    println(seq1);
  }
}

sha{}.doit();

// Let's throw in the equivalent test for script-level:

var seq2 = ['A', 'B', 'C', 'D', 'E', 'F', 'G'];
var mir2 = seq2;
insert "@" before seq2[0];
println(mir2);
delete seq2[0..4];
println(mir2);
println(seq2);

// and for local-block-level:

function f3 () {
    var seq3 = ['1', '2', '3', '4', '5', '6', '7'];
    var mir3 = seq3;
    insert "@" before seq3[0];
    println(mir3);
    delete seq3[0..4];
    println(mir3);
    println(seq3);
}
f3();
