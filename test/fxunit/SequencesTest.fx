/*
 * @test/fxunit
 * @run
 */

// TODO JFXC-833
// The tests for using a single element as input are deactivated until JFXC-833
// is resolved.
 
import javafx.lang.Sequences;

import com.sun.javafx.runtime.JavaFXTestCase;
import java.util.Comparator;
import java.lang.Object;
import java.lang.System;


public class DummyElement {
    public attribute id: Integer;
}
//public class DummyComparator extends Comparator<DummyElement> {
//    public function compare(o1: DummyElement, o2: DummyElement): Integer {
//        return o1.id - o2.id;
//    };
//    public function equals(o: Object): Boolean {
//        return this == o;
//    }
//}
public class DummyComparator extends Comparator {
    public function compare(o1: Object, o2: Object): Integer {
        var de1 = o1 as DummyElement;
        var de2 = o2 as DummyElement;
        return de1.id - de2.id;
    };
//    public function compare(o1: DummyElement, o2: DummyElement): Integer {
//        return o1.id - o2.id;
//    };
    public function equals(o: Object): Boolean {
        return this == o;
    }
}
public class SequencesTest extends javafx.fxunit.FXTestCase {

    // TODO: For some reason the setUp-function is not called.
    //       Needs further investigation.
//    attribute emptyInteger:    Integer[];
//    attribute singleInteger:   Integer[];
//    attribute sortedInteger:   Integer[];
//    attribute unsortedInteger: Integer[];
//    attribute emptyElements: DummyElement[];
//    attribute singleElements: DummyElement[];
//    attribute sortedElements: DummyElement[];
//    attribute unsortedElements: DummyElement[];
//    
//    attribute element: DummyElement[];
//    attribute comparator: DummyComparator;
//    
//    protected function setUp(): Void {
//        // Integer-sequences
//        emptyInteger    = [];
//        // TODO JFXC-833
////        singleInteger   = 1;
//        sortedInteger   = [2, 3, 4];
//        unsortedInteger = [7, 5, 6];
//        
//        // 7 Dummyelements
//        element = for (i in [0..7]) DummyElement{id:i};
//        
//        // DummyElement-sequences
//        emptyElements    = [];
//        // TODO JFXC-833
////        singleElements   = element[0];
//        sortedElements   = [element[1], element[2], element[3]];
//        unsortedElements = [element[6], element[4], element[5]];
//
//        // Comparator
//        comparator = DummyComparator {};
//    }
    
    function testSortComparable() {
        // sort empty sequence
        var emptyInteger: Integer[] = [];
        var result = Sequences.sort(emptyInteger);
        assertEquals([], emptyInteger);
        assertEquals([], result);
        
        // sort single element
        // TODO JFXC-833
//        var singleInteger: Integer[] = 1;
//        result = Sequences.sort(singleInteger);
//        assertEquals(singleInteger, 1);
//        assertEquals(result, 1);
        
        // sort sorted sequence
        var sortedInteger: Integer[] = [2, 3, 4];
        result = Sequences.sort(sortedInteger);
        assertEquals([2, 3, 4], sortedInteger);
        assertEquals([2, 3, 4], result);
        
        // sort unsorted sequence
        var unsortedInteger: Integer[] = [7, 5, 6];
        result = Sequences.sort(unsortedInteger);
        assertEquals([7, 5, 6], unsortedInteger);
        assertEquals([5, 6, 7], result);
    }

    function testSortComparator() {
        // 7 dummy elements
        var element: DummyElement[] = for (i in [0..7]) DummyElement{id:i};
        
        // dummy comparator
        var comparator: DummyComparator = DummyComparator {};
        
        // sort empty sequence
        var emptyElements: DummyElement[] = [];
        var result = Sequences.sort(emptyElements, comparator);
        assertEquals([], emptyElements);
        assertEquals([], result);
        
        // sort single element
        // TODO JFXC-833
//        var singleElement: DummyElement[] = element[0];
//        result = Sequences.sort(singleElements, comparator);
//        assertEquals(singleElements, element[0]);
//        assertEquals(result, element[0]);
        
        // sort sorted sequence
        var sortedElements: DummyElement[] = [element[1], element[2], element[3]];
        result = Sequences.sort(sortedElements, comparator);
        assertEquals([element[1], element[2], element[3]], sortedElements);
        assertEquals([element[1], element[2], element[3]], result);
        
        // sort unsorted sequence
        var unsortedElements: DummyElement[] = [element[6], element[4], element[5]];
        result = Sequences.sort(unsortedElements, comparator);
        assertEquals([element[6], element[4], element[5]], unsortedElements);
        assertEquals([element[4], element[5], element[6]], result);
    }
}
