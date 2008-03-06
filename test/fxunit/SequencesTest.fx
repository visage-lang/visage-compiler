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
        return o instanceof DummyComparator;
    }
}

public class SequencesTest extends javafx.fxunit.FXTestCase {

    attribute emptyInteger:     Integer[];
    attribute singleInteger:    Integer[];
    attribute sortedInteger:    Integer[];
    attribute unsortedInteger:  Integer[];
    attribute emptyElements:    DummyElement[];
    attribute singleElements:   DummyElement[];
    attribute sortedElements:   DummyElement[];
    attribute unsortedElements: DummyElement[];
    attribute longSequence:     DummyElement[];
    
    attribute element: DummyElement[];
    attribute comparator: DummyComparator;
    
    protected function setUp(): Void {
        // Integer-sequences
        emptyInteger    = [];
        // TODO JFXC-833
//        singleInteger   = 0;
        sortedInteger   = [1, 2, 3];
        unsortedInteger = [3, 1, 2];
        
        // 7 Dummyelements
        element = for (i in [0..4]) DummyElement{id:i};
        
        // DummyElement-sequences
        emptyElements    = [];
        // TODO JFXC-833
//        singleElements   = element[0];
        sortedElements   = [element[1], element[2], element[3]];
        unsortedElements = [element[3], element[1], element[2]];
        longSequence     = [element[0], element[1], element[2], element[1], element[3]];

        // Comparator
        comparator = DummyComparator {};
    }
    
    function testBinarySearchComparable() {
        var result: Integer;
        
        // search in empty sequence
        result = Sequences.binarySearch(emptyInteger, 1);
        assertEquals([], emptyInteger);
        assertEquals(-1, result);
        
        // single element sequence
        // TODO JFXC-833
//        // successful search
//        result = Sequences.binarySearch(singleInteger, 0);
//        assertEquals(singleInteger, 0);
//        assertEquals(0, result);
//        
//        // unsuccessful search
//        result = Sequences.binarySearch(singleInteger, 1);
//        assertEquals(0, singleInteger);
//        assertEquals(-2, result);
        
        // three elements sequence
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
        var result: Integer;
        
        // search in empty sequence
        result = Sequences.binarySearch(emptyElements, element[1], comparator);
        assertEquals([], emptyElements);
        assertEquals(-1, result);
        
        // single element sequence
        // TODO JFXC-833
//        // successful search
//        result = Sequences.binarySearch(singleElements, element[0], comparator);
//        assertEquals(element[0], singleElements);
//        assertEquals(0, result);
//        
//        // unsuccessful search
//        result = Sequences.binarySearch(singleElements, element[1], comparator);
//        assertEquals(element[0], singleElements);
//        assertEquals(-2, result);
        
        // three elements sequence
        // successful search
        result = Sequences.binarySearch(sortedElements, element[2], comparator);
        assertEquals([element[1], element[2], element[3]], sortedElements);
        assertEquals(1, result);
        
        // unsuccessful search
        result = Sequences.binarySearch(sortedElements, element[0], comparator);
        assertEquals([element[1], element[2], element[3]], sortedElements);
        assertEquals(-1, result);

        // search using null-comparator
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
    
    function testIndexOf() {
        var result: Integer;
        
        // search in empty sequence
        result = Sequences.indexOf(emptyElements, element[1]);
        assertEquals([], emptyElements);
        assertEquals(-1, result);
        
        // single element sequence
        // TODO JFXC-833
//        // successful search
//        result = Sequences.indexOf(singleElements, element[0]);
//        assertEquals(element[0], singleElements);
//        assertEquals(0, result);
//        
//        // unsuccessful search
//        result = Sequences.indexOf(singleElements, element[1]);
//        assertEquals(element[0], singleElements);
//        assertEquals(-1, result);
        
        // three elements sequence
        // successful search for first element
        result = Sequences.indexOf(unsortedElements, element[3]);
        assertEquals([element[3], element[1], element[2]], unsortedElements);
        assertEquals(0, result);
        
        // successful search for middle element
        result = Sequences.indexOf(unsortedElements, element[1]);
        assertEquals([element[3], element[1], element[2]], unsortedElements);
        assertEquals(1, result);
        
        // successful search for last element
        result = Sequences.indexOf(unsortedElements, element[2]);
        assertEquals([element[3], element[1], element[2]], unsortedElements);
        assertEquals(2, result);
        
        // make sure first element is returned
        result = Sequences.indexOf(longSequence, element[1]);
        assertEquals([element[0], element[1], element[2], element[1], element[3]], longSequence);
        assertEquals(1, result);
        
        // unsuccessful search
        result = Sequences.indexOf(unsortedElements, element[0]);
        assertEquals([element[3], element[1], element[2]], unsortedElements);
        assertEquals(-1, result);

        // exception when sequence is null
        try {
            Sequences.indexOf(null, 1);
            fail("No exception thrown.");
        }
        catch (ex1: NullPointerException) {
        }
        catch (ex2: Exception) {
            fail ("Unexpected exception thrown: " + ex2.getMessage());
        }

        // exception when sequence is null
        try {
            Sequences.indexOf(unsortedElements, null);
            fail("No exception thrown.");
        }
        catch (ex3: NullPointerException) {
        }
        catch (ex4: Exception) {
            fail ("Unexpected exception thrown: " + ex4.getMessage());
        }
    }
    
    function testSortComparable() {
        var result: Integer[];
        
        // sort empty sequence
        result = Sequences.sort(emptyInteger) as Integer[];
        assertEquals([], emptyInteger);
        assertEquals([], result);
        
        // sort single element
        // TODO JFXC-833
//        result = Sequences.sort(singleInteger);
//        assertEquals(1, singleInteger);
//        assertEquals(1, result);
        
        // sort sequence
        result = Sequences.sort(unsortedInteger) as Integer[];
        assertEquals([3, 1, 2], unsortedInteger);
        assertEquals([1, 2, 3], result);

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
        var result: DummyElement[];
        
        // sort empty sequence
        result = Sequences.sort(emptyElements, comparator) as DummyElement[];
        assertEquals([], emptyElements);
        assertEquals([], result);
        
        // sort single element
        // TODO JFXC-833
//        result = Sequences.sort(singleElements, comparator);
//        assertEquals(element[0], singleElements);
//        assertEquals(element[0], result);
        
        // sort sequence
        result = Sequences.sort(unsortedElements, comparator) as DummyElement[];
        assertEquals([element[3], element[1], element[2]], unsortedElements);
        assertEquals([element[1], element[2], element[3]], result);
        
        // sort using null-comparator
        var resultInt: Integer[] = Sequences.sort(unsortedInteger, null) as Integer[];
        assertEquals([3, 1, 2], unsortedInteger);
        assertEquals([1, 2, 3], resultInt);

        // exception if using null-operator with non-comparable elements
        try {
            Sequences.sort(unsortedElements, null);
            fail("No exception thrown.");
        }
        catch (ex1: ClassCastException) {
            assertEquals([element[3], element[1], element[2]], unsortedElements);
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
