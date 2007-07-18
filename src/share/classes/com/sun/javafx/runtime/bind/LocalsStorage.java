package com.sun.javafx.runtime.bind;

/**
 * LocalsStorage
*
* @author Brian Goetz
*/
public class LocalsStorage {
    private static final long[] zeroPrimitives = new long[0];
    private static final Object [] zeroReferences = new Object[0];

    public final long[] primitives;
    public final Object[] references;

    public LocalsStorage(int numPrimitives, int numReferences) {
        primitives = numPrimitives == 0 ? zeroPrimitives : new long[numPrimitives];
        references = numReferences == 0 ? zeroReferences : new Object[numReferences];
    }
}
