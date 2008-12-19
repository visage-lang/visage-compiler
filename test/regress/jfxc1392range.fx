/**
 * Regression test JFXC-1392 : Mixed Integer and Number in bound range causes back-end failure
 *
 * @test
 * @run
 */

import java.lang.System;

class Hold {
    var held : Number;

    function show() {
        System.out.print("{%4.1f held}, ");
    }
}

class RangeTest {

    bound function numOfIntervals0(): Hold[] {
        for(i in [0 .. 2 step 1])
            Hold { held: i }
    }

    bound function numOfIntervals1(): Hold[] {
        for(i in [0 .. 2 step 0.1])
            Hold { held: i }
    }

    bound function numOfIntervals2(): Hold[] {
        for(i in [0 .. 1.5 step 1])
            Hold { held: i }
    }

    bound function numOfIntervals3(): Hold[] {
        for(i in [0 .. 1.5 step 0.1])
            Hold { held: i }
    }

    bound function numOfIntervals4(): Hold[] {
        for(i in [0.1 .. 2 step 1])
            Hold { held: i }
    }

    bound function numOfIntervals5(): Hold[] {
        for(i in [0.1 .. 2 step 0.1])
            Hold { held: i }
    }

    bound function numOfIntervals6(): Hold[] {
        for(i in [0.1 .. 1.5 step 1])
            Hold { held: i }
    }

    bound function numOfIntervals7(): Hold[] {
        for(i in [0.1 .. 1.5 step 0.1])
            Hold { held: i }
    }

    bound function numOfIntervalsA(): Hold[] {
        for(i in [0 .. 1.5])
            Hold { held: i }
    }

    bound function numOfIntervalsB(): Hold[] {
        for(i in [0.1 .. 2])
            Hold { held: i }
    }

    bound function numOfIntervalsC(): Hold[] {
        for(i in [0.1 .. 1.5])
            Hold { held: i }
    }

    bound function numOfIntervals0e(): Hold[] {
        for(i in [0 ..< 2 step 1])
            Hold { held: i }
    }

    bound function numOfIntervals1e(): Hold[] {
        for(i in [0 ..< 2 step 0.1])
            Hold { held: i }
    }

    bound function numOfIntervals2e(): Hold[] {
        for(i in [0 ..< 1.5 step 1])
            Hold { held: i }
    }

    bound function numOfIntervals3e(): Hold[] {
        for(i in [0 ..< 1.5 step 0.1])
            Hold { held: i }
    }

    bound function numOfIntervals4e(): Hold[] {
        for(i in [0.1 ..< 2 step 1])
            Hold { held: i }
    }

    bound function numOfIntervals5e(): Hold[] {
        for(i in [0.1 ..< 2 step 0.1])
            Hold { held: i }
    }

    bound function numOfIntervals6e(): Hold[] {
        for(i in [0.1 ..< 1.5 step 1])
            Hold { held: i }
    }

    bound function numOfIntervals7e(): Hold[] {
        for(i in [0.1 ..< 1.5 step 0.1])
            Hold { held: i }
    }

    bound function numOfIntervalsAe(): Hold[] {
        for(i in [0 ..< 1.5])
            Hold { held: i }
    }

    bound function numOfIntervalsBe(): Hold[] {
        for(i in [0.1 ..< 2])
            Hold { held: i }
    }

    bound function numOfIntervalsCe(): Hold[] {
        for(i in [0.1 ..< 1.5])
            Hold { held: i }
    }
}

function myprint(foos : Hold[]) {
    for (oo in foos) {
        oo.show();
    }
    System.out.println();
}

var rt = RangeTest {};
myprint( rt.numOfIntervals0() );
myprint( rt.numOfIntervals0e() );
myprint( rt.numOfIntervals1() );
myprint( rt.numOfIntervals1e() );
myprint( rt.numOfIntervals2() );
myprint( rt.numOfIntervals2e() );
myprint( rt.numOfIntervals3() );
myprint( rt.numOfIntervals3e() );
myprint( rt.numOfIntervals4() );
myprint( rt.numOfIntervals4e() );
myprint( rt.numOfIntervals5() );
myprint( rt.numOfIntervals5e() );
myprint( rt.numOfIntervals6() );
myprint( rt.numOfIntervals6e() );
myprint( rt.numOfIntervals7() );
myprint( rt.numOfIntervals7e() );
myprint( rt.numOfIntervalsA() );
myprint( rt.numOfIntervalsAe() );
myprint( rt.numOfIntervalsB() );
myprint( rt.numOfIntervalsBe() );
myprint( rt.numOfIntervalsC() );
myprint( rt.numOfIntervalsCe() );




