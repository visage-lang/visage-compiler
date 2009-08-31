/**
 * JFXC-2852 - String(byte[]...) doesn't compile.
 *
 * @compilefirst jfxc2852ArrayPrinter.java
 * @test
 * @run
 */

var bytes : Byte[] = [ 65, 66, 67, 68 ]; 

// the constructor call below used to crash in 
// the back-end with not being able to find
// a constructor of signature "String(Byte[])"

var str = new String(bytes); 

println(str); 

// attempt all primitive sequence to corresponding
// Java primitive array conversions.

jfxc2852ArrayPrinter.printByteArray(bytes);

var shorts : Short[] = [ 1, 2, 3, 4 ];
jfxc2852ArrayPrinter.printShortArray(shorts);

var ints : Integer[] = [ 1, 2, 3, 4 ];
jfxc2852ArrayPrinter.printIntArray(ints);

var longs : Long[] = [ 1, 2, 3, 4 ];
jfxc2852ArrayPrinter.printLongArray(longs);

var floats : Float[] = [ 1, 2, 3, 4 ];
jfxc2852ArrayPrinter.printFloatArray(floats);

var doubles : Double[] = [ 1, 2, 3, 4 ];
jfxc2852ArrayPrinter.printDoubleArray(doubles);
