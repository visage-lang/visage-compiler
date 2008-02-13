/*
 * Test basic slice trigger behavior; removed insert and delete triggers.
 * I'll work on more functional triggers(triggers that do something) later.
 * TODO: not complete but need to put this back
 *
 * @test
 * @compilefirst TestUtils.fx
 * @run
 */

import java.lang.System;


class FooSlice extends TestUtils {

/** seq1, 0 - 50 x 5's */
attribute seq1 = [0,5,10,15,20,25,30,35,40,50]
on replace oldValue[indx  .. lastIndex]=newElements {	insert "{oldValue}" into replacements;		};

/** seq2, 2^0 - 2^8 */
attribute seq2 = [0,1,2,3,4]
on replace oldValue[indx  .. lastIndex]=newElements
  { insert "seq_2p: replace {newElements} at {indx}\n" into replacements;  };

attribute seq_2p : Integer[]
on replace oldValue[indx  .. lastIndex]=newElements
  { insert "seq_2p: replaced {String.valueOf(oldValue)}[{indx}..{lastIndex}] by {String.valueOf(newElements)}\n" into replacements;  };

/** seq3 10 element descending sequence 10 - 1 */
attribute seq3 = [10..1 step -1]
on replace oldValue[indx  .. lastIndex]=newElements
	{
		if(sizeof oldValue  < sizeof newElements) 			{
			if(sizeof oldValue > 0)
			System.out.println("replacement({sizeof newElements}) is larger than existing({sizeof oldValue})");
		    }
   };

/** seq4 4 element descending sequence 101 - 98 */
attribute done: Boolean = false;
attribute seq4 = [101..98 step -1]
on replace oldValue[indx  .. lastIndex]=newElements {	
	System.out.println("insert {newElements} or {oldValue} into {seq4}");
  check("Size of seq4: {sizeof seq4}");
};

/** seq5, 11 element sequence 100 - 110 */
attribute seq5 = [100..110]
	on replace oldValue[indx  .. lastIndex]=newElements {	insert "{oldValue}" into replacements;		};

/**
 * Tests on seq1
 */
function test1() {
   System.out.println("-test1-");
   //this does not trigger a trigger.
	seq1[3] = seq1[3]/5;
   //insert seq1[3]/5 at seq1[3];  //presumably this replacement syntax will.
   printSequence(seq1);
   PrintPassFail( "check insertion into seq1", Success( seq1[3],3) );
}

function pow(b:Integer, e:Integer):Integer {
	var res = 1;
	if(e==0) {return res;}
	if(e<0) { return 0; }
	for(i in [ 1..e]) res = res*b;
	return res;
}

/**
 * Tests on seq1
 */
function test2() {
 System.out.println("-test2-");
 PrintPassFail( "check size of seq2", Success( seq2.size(),5) );
 printSequence(seq2);
  for ( n in seq2 ) 	  {
	  insert pow(2,n) into seq_2p;
  }
  PrintPassFail( "seq_2p: check new sequence", Success((seq_2p == [1,2,4,8,16]), true));
  printSequence(seq_2p);
  insert 7 into seq2;
  insert 10 into seq2;
  insert 8 into seq2;
  PrintPassFail( "seq2: check altered sequence", Success((seq2 == [0,1,2,3,4,7,10,8]), true));
  System.out.println("trying to trigger replace trigger");
  seq2[5..6] = [5,6,7];
  PrintPassFail( "seq2: check new sequence after replace", Success((seq2 == [0,1,2,3,4,5,6,7,8]), true));

  for ( n in seq2[5..] ) 	  {
	  insert pow(2,n) into seq_2p;
  }
  printSequence(seq_2p);
  PrintPassFail( "seq_2p: check new sequence", Success((seq_2p == [1,2,4,8,16,32,64,128,256]), true));
  PrintPassFail("check for ascending sequence", Success( checkAscendingSequence(seq2,true), true ));
 }

function test3() {
  System.out.println("-test3-replacement is larger than the sequence");
  PrintPassFail( "check size of seq3: {sizeof seq3}", Success( sizeof seq3,10) );
  var larger_seq3 = [ 20..1 step -1 ];
  seq3[0..10] = larger_seq3;
  PrintPassFail( "check size of larger seq3: { sizeof seq3}", Success( sizeof seq3,20) );
  PrintPassFail("check for descending sequence of seq3", Success( checkAscendingSequence(seq3,false), true ));

  var larger_seq3b = [ 26..100 ];
  seq3[0..20] = larger_seq3b;
  PrintPassFail( "check size of largerer  seq3: { sizeof seq3}", Success( sizeof seq3,75) );
  check("contents of seq3: {seq3}");
  PrintPassFail("check for ascending sequence of seq3", Success( checkAscendingSequence(seq3,true), true ));
}

/** var seq4 = [101..98] */
function test4() {
  System.out.println("-test4-empty-");
  done=false;
  insert 99 into seq4;
  check("contents of seq4 with 99 inserted: {seq4}");
  PrintPassFail("check for descending sequence of seq4", Success( checkAscendingSequence(seq4,false), false ));
}

/**
 * Tests on seq5
 */
 function test5() {
    System.out.println("-test5-");
    printSequence(seq5);
    seq5[3] = 88;
    insert 77 into seq5;
    delete 109 from seq5;
    delete seq5[6];
    seq5[4..8] = seq5[5..7];
    PrintPassFail( "seq5-check end sequence", Success((seq5 == [ 100, 101, 102, 88, 105, 107, 108, 77 ]), true));
    delete seq5;
    PrintPassFail("seq5-check for deleted sequence", Success( seq5==[], true ));
 }
};


var fooslice = new FooSlice;
fooslice.test1();
fooslice.test2();
fooslice.test3();
fooslice.test4();
fooslice.test5();
fooslice.Replacements();
fooslice.report();

