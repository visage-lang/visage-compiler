/*
 * jfxc-2063 - Behaviour of invoking a trigger attached to a sequence needs to be clarified.
 * @test
 *
 * test/run with .expected file; when fixed, the output can be determined.
 */
class strig {
   var seq1b:Integer[] = [1,2,3,4];
   var seq1:Integer[] = bind seq1b[ x | x > 2 ]
     on replace oldValue[lo .. hi]=newVals {
       println("seq1 old: {oldValue} with new: {newVals}")
     }

   var seq2b:Integer[] = [1,2,3,4];
   var seq2:Integer[] = bind seq2b[ x | x > 2 ]
     on replace oldValue[lo .. hi]=newVals {
       println("seq2 old: {oldValue} with new: {newVals}")
      }

function test1() {
  println("-------------test1----------");
  seq1b = [2,5,6,7];
}

function test2() {
  println("----------test2----------");
  seq2b = [2,4,5,6,7];
}
}

function run(args:String[]) {
  println("---------create object------");
 var st:strig = new strig();
 st.test1();
 st.test2();
}

/*
It is not clear which way the resolution of this bug will go, whether sequence
trigger will be call for each value a la test1 or once a la test2.
The current output is:

---------create object------
seq1 old:  with new: 34
seq2 old:  with new: 34
-------------test1----------
seq1 old: 34 with new:
seq1 old: 34 with new: 5
seq1 old: 534 with new: 5
seq1 old: 534 with new: 6
seq1 old: 564 with new: 6
seq1 old: 564 with new: 7
seq1 old: 567 with new: 7
----------test2----------
seq2 old: 34 with new: 4567

*/
