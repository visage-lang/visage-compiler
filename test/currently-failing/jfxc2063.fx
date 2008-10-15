/*
 * jfxc-2063 - Behaviour of invoking a trigger attached to a sequence needs to be clarified.
 * @test
 *
 * test/run with .expected file; when fixed, the output can be determined.
 */


var seq:Integer[] = [1,2,3,4];
var seq1:Integer[] = bind seq[ x | x > 2 ]
  on replace oldValue[lo .. hi]=newVals {
    println("replaced old: {oldValue} with new: {newVals} low idx:{lo} ; hi idx: {hi}")
  }
seq = [2,5,6,7];


/*
Current output, which seems to be incorrect:

replaced old:  with new: 34 low idx:0 ; hi idx: -1
replaced old: 34 with new:  low idx:0 ; hi idx: -1
replaced old: 34 with new: 5 low idx:0 ; hi idx: -1
replaced old: 534 with new: 5 low idx:0 ; hi idx: 0
replaced old: 534 with new: 6 low idx:1 ; hi idx: 1
replaced old: 564 with new: 6 low idx:1 ; hi idx: 1
replaced old: 564 with new: 7 low idx:2 ; hi idx: 2
replaced old: 567 with new: 7 low idx:2 ; hi idx: 2

*/
