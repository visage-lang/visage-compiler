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

import java.lang.Exception;
import java.lang.ClassCastException;
import java.lang.NullPointerException;
import java.lang.IllegalArgumentException;


public class DummyElement {
    public attribute id: Integer;
}
public class DummyComparator extends Comparator<DummyElement> {
    public function compare(o1: DummyElement, o2: DummyElement): Integer {
        return o1.id - o2.id;
    };
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
    
    function testBinarySearchComparable() {
        var result: Integer;
        
        // search in empty sequence
        var emptyInteger: Integer[] = [];
        result = Sequences.binarySearch(emptyInteger, 1);
        assertEquals([], emptyInteger);
        assertEquals(-1, result);
        
        // single element sequence
        // TODO JFXC-833
//        var singleInteger: Integer[] = 0;
//        // successful search
//        result = Sequences.binarySearch(singleInteger, 0);
//        assertEquals(singleInteger, 0);
//        assertEquals(0, result);
//        
//        // unsuccessful search
//        result = Sequences.binarySearch(singleInteger, 1);
//        assertEquals(singleInteger, 0);
//        assertEquals(-2, result);
        
        // three elements sequence
        var sortedInteger: Integer[] = [1, 2, 3];
        // successful search
        result = Sequences.binarySearch(sortedInteger, 2);
        assertEquals([1, 2, 3], sortedInteger);
        assertEquals(1, result);
        
        // unsuccessful search
        result = Sequences.binarySearch(sortedInteger, 0);
        assertEquals([1, 2, 3], sortedInteger);
        assertEquals(-1, result);
        
        // exception when sequence is null
        try {
            Sequences.binarySearch(null, 1);
            fail("No exception thrown.");
        }
        catch (ex1: NullPointerException) {
        }
        catch (ex2: Exception) {
            fail ("Unexpected exception thrown: {ex2}");
        }
        
    }

    function testBinarySearchComparator() {
        // dummy elements
        var element: DummyElement[] = for (i in [0..7]) DummyElement{id:i};
        var comparator: DummyComparator = DummyComparator {};
        
        var result: Integer;
        
        // search in empty sequence
        var emptyElements: DummyElement[] = [];
        result = Sequences.binarySearch(emptyElements, element[1], comparator);
        assertEquals([], emptyElements);
        assertEquals(-1, result);
        
        // single element sequence
        // TODO JFXC-833
//        var singleElement: DummyElement[] = element[0];
//        // successful search
//        result = Sequences.binarySearch(singleElements, element[0], comparator);
//        assertEquals(singleElements, element[0]);
//        assertEquals(0, result);
//        
//        // unsuccessful search
//        result = Sequences.binarySearch(singleElements, element[1], comparator);
//        assertEquals(singleElements, element[0]);
//        assertEquals(-2, result);
        
        // three elements sequence
        var sortedElements: DummyElement[] = [element[1], element[2], element[3]];
        // successful search
        result = Sequences.binarySearch(sortedElements, element[2], comparator);
        assertEquals([element[1], element[2], element[3]], sortedElements);
        assertEquals(1, result);
        
        // unsuccessful search
        result = Sequences.binarySearch(sortedElements, element[0], comparator);
        assertEquals([element[1], element[2], element[3]], sortedElements);
        assertEquals(-1, result);

        // search using null-comparator
        var sortedInteger: Integer[] = [1, 2, 3];
        var resultInt: Integer = Sequences.binarySearch(sortedInteger, 2, null);
        assertEquals([1, 2, 3], sortedInteger);
        assertEquals(1, resultInt);
        
        // exception if using null-operator with non-comparable elements
        try {
            Sequences.binarySearch(sortedElements, element[2], null);
            fail("No exception thrown.");
        }
        catch (ex1: ClassCastException) {
            assertEquals([element[1], element[2], element[3]], sortedElements);
        }
        catch (ex2: Exception) {
            fail("Unexpected exception thrown: " + ex2.getMessage());
        }

        // exception when sequence is null
        try {
            Sequences.binarySearch(null, element, null);
            fail("No exception thrown.");
        }
        catch (ex3: NullPointerException) {
        }
        catch (ex4: Exception) {
            fail ("Unexpected exception thrown: {ex4}");
        }
        
    }
    
    function testSortComparable() {
        var result: Integer[];
        
        // sort empty sequence
        var emptyInteger: Integer[] = [];
        result = Sequences.sort(emptyInteger) as Integer[];
        assertEquals([], emptyInteger);
        assertEquals([], result);
        
        // sort single element
        // TODO JFXC-833
//        var singleInteger: Integer[] = 1;
//        result = Sequences.sort(singleInteger);
//        assertEquals(singleInteger, 1);
//        assertEquals(result, 1);
        
        // sort sequence
        var unsortedInteger: Integer[] = [6, 4, 5];
        result = Sequences.sort(unsortedInteger) as Integer[];
        assertEquals([6, 4, 5], unsortedInteger);
        assertEquals([4, 5, 6], result);

        // exception when sequence is null
        try {
            Sequences.sort(null);
            fail("No exception thrown.");
        }
        catch (ex1: NullPointerException) {
        }
        catch (ex2: Exception) {
            fail ("Unexpected exception thrown: {ex2}");
        }
        
    }

    function testSortComparator() {
        // dummy elements
        var element: DummyElement[] = for (i in [0..7]) DummyElement{id:i};
        var comparator: DummyComparator = DummyComparator {};
        
        var result: DummyElement[];
        
        // sort empty sequence
        var emptyElements: DummyElement[] = [];
        result = Sequences.sort(emptyElements, comparator) as DummyElement[];
        assertEquals([], emptyElements);
        assertEquals([], result);
        
        // sort single element
        // TODO JFXC-833
//        var singleElement: DummyElement[] = element[0];
//        result = Sequences.sort(singleElements, comparator);
//        assertEquals(singleElements, element[0]);
//        assertEquals(result, element[0]);
        
        // sort sequence
        var unsortedElements: DummyElement[] = [element[6], element[4], element[5]];
        result = Sequences.sort(unsortedElements, comparator) as DummyElement[];
        assertEquals([element[6], element[4], element[5]], unsortedElements);
        assertEquals([element[4], element[5], element[6]], result);
        
        // sort using null-comparator
        var unsortedInteger: Integer[] = [6, 4, 5];
        var resultInt: Integer[] = Sequences.sort(unsortedInteger, null) as Integer[];
        assertEquals([6, 4, 5], unsortedInteger);
        assertEquals([4, 5, 6], resultInt);

        // exception if using null-operator with non-comparable elements
        try {
            Sequences.sort(unsortedElements, null);
            fail("No exception thrown.");
        }
        catch (ex1: ClassCastException) {
            assertEquals([element[6], element[4], element[5]], unsortedElements);
        }
        catch (ex2: Exception) {
            fail("Unexpected exception thrown: " + ex2.getMessage());
        }

        // exception when sequence is null
        try {
            Sequences.sort(null, null);
            fail("No exception thrown.");
        }
        catch (ex3: NullPointerException) {
        }
        catch (ex4: Exception) {
            fail ("Unexpected exception thrown: {ex4}");
        }
        
    }
}
