/*
 * Test sequences casting 
 *
 * @test/fxunit
 * @
 */

import javafx.fxunit.FXTestCase;
 
public class SeqCastTest extends FXTestCase {
    var c1: Character = 5 as Character;
    var vCharacter : Character[] = [c1, c1+1, c1+2];
    var vByte : Byte[] = [1..7];
    var vInteger : Integer[] = [256..259];
    var vLong : Long[] = [1000000..1000003];
    var vShort : Short[] = [16000..16004];
    var vFloat : Float[] = [5.5, 6.5,7.25];
    var vDouble : Double[] = [6.125..7.875];
    var vNumber: Number[]=[35120.5..35125.5];
    var res=this; 
    var gen: Boolean = false;

    var xByte: Byte[] = vByte;  
    function testByteSeqCastToByte() { 
       if (gen) javafx.lang.FX.println("\nvar resByteSeqCastToByte1:Byte[]=");
        xByte=vByte; 
       // xByte=[]; 
       if (gen)javafx.lang.FX.println(xByte);
       if (gen)javafx.lang.FX.print("; //");
        assertEquals(res.resByteSeqCastToByte1, xByte);
    }    

    function testByteSeqUnionWithByte() { 
       if (gen)javafx.lang.FX.println("\nvar resByteSeqUnionWithByte1:Byte[]=");
        xByte=vByte;
       // xByte=[]; 
        assertEquals(res.resByteSeqUnionWithByte1, xByte);
       if (gen)javafx.lang.FX.println(xByte);
       if (gen)javafx.lang.FX.println("; var resByteSeqUnionWithByte2:Byte[]=");
        xByte=[vByte, vByte];
       // xByte=[]; 
        assertEquals(res.resByteSeqUnionWithByte2, xByte);
       if (gen)javafx.lang.FX.println(xByte);
       if (gen)javafx.lang.FX.println("; var resByteSeqUnionWithByte3:Byte[]=");
        xByte=[vByte, vByte];
       // xByte=[]; 
        assertEquals(res.resByteSeqUnionWithByte3, xByte);
       if (gen)javafx.lang.FX.println(xByte);
       if (gen)javafx.lang.FX.print("; //");
    }    
    function testShortSeqCastToByte() { 
       if (gen) javafx.lang.FX.println("\nvar resShortSeqCastToByte1:Byte[]=");
        xByte=vShort; 
       // xByte=[]; 
       if (gen)javafx.lang.FX.println(xByte);
       if (gen)javafx.lang.FX.print("; //");
        assertEquals(res.resShortSeqCastToByte1, xByte);
    }    

    function testShortSeqUnionWithByte() { 
       if (gen)javafx.lang.FX.println("\nvar resShortSeqUnionWithByte1:Byte[]=");
        xByte=vByte;
       // xByte=[]; 
        assertEquals(res.resShortSeqUnionWithByte1, xByte);
       if (gen)javafx.lang.FX.println(xByte);
       if (gen)javafx.lang.FX.println("; var resShortSeqUnionWithByte2:Byte[]=");
        xByte=[vByte, vShort];
       // xByte=[]; 
        assertEquals(res.resShortSeqUnionWithByte2, xByte);
       if (gen)javafx.lang.FX.println(xByte);
       if (gen)javafx.lang.FX.println("; var resShortSeqUnionWithByte3:Byte[]=");
        xByte=[vShort, vByte];
       // xByte=[]; 
        assertEquals(res.resShortSeqUnionWithByte3, xByte);
       if (gen)javafx.lang.FX.println(xByte);
       if (gen)javafx.lang.FX.print("; //");
    }    
    function testCharacterSeqCastToByte() { 
       if (gen) javafx.lang.FX.println("\nvar resCharacterSeqCastToByte1:Byte[]=");
       //  xByte=vCharacter; 
        xByte=[]; 
       if (gen)javafx.lang.FX.println(xByte);
       if (gen)javafx.lang.FX.print("; //");
       //  assertEquals(res.resCharacterSeqCastToByte1, xByte);
    }    

    function testCharacterSeqUnionWithByte() { 
       if (gen)javafx.lang.FX.println("\nvar resCharacterSeqUnionWithByte1:Byte[]=");
       //  xByte=vByte;
        xByte=[]; 
       //  assertEquals(res.resCharacterSeqUnionWithByte1, xByte);
       if (gen)javafx.lang.FX.println(xByte);
       if (gen)javafx.lang.FX.println("; var resCharacterSeqUnionWithByte2:Byte[]=");
       //  xByte=[vByte, vCharacter];
        xByte=[]; 
       //  assertEquals(res.resCharacterSeqUnionWithByte2, xByte);
       if (gen)javafx.lang.FX.println(xByte);
       if (gen)javafx.lang.FX.println("; var resCharacterSeqUnionWithByte3:Byte[]=");
       //  xByte=[vCharacter, , vByte];
        xByte=[]; 
       //  assertEquals(res.resCharacterSeqUnionWithByte3, xByte);
       if (gen)javafx.lang.FX.println(xByte);
       if (gen)javafx.lang.FX.print("; //");
    }    
    function testIntegerSeqCastToByte() { 
       if (gen) javafx.lang.FX.println("\nvar resIntegerSeqCastToByte1:Byte[]=");
        xByte=vInteger; 
       // xByte=[]; 
       if (gen)javafx.lang.FX.println(xByte);
       if (gen)javafx.lang.FX.print("; //");
        assertEquals(res.resIntegerSeqCastToByte1, xByte);
    }    

    function testIntegerSeqUnionWithByte() { 
       if (gen)javafx.lang.FX.println("\nvar resIntegerSeqUnionWithByte1:Byte[]=");
        xByte=vByte;
       // xByte=[]; 
        assertEquals(res.resIntegerSeqUnionWithByte1, xByte);
       if (gen)javafx.lang.FX.println(xByte);
       if (gen)javafx.lang.FX.println("; var resIntegerSeqUnionWithByte2:Byte[]=");
        xByte=[vByte, vInteger];
       // xByte=[]; 
        assertEquals(res.resIntegerSeqUnionWithByte2, xByte);
       if (gen)javafx.lang.FX.println(xByte);
       if (gen)javafx.lang.FX.println("; var resIntegerSeqUnionWithByte3:Byte[]=");
        xByte=[vInteger, vByte];
       // xByte=[]; 
        assertEquals(res.resIntegerSeqUnionWithByte3, xByte);
       if (gen)javafx.lang.FX.println(xByte);
       if (gen)javafx.lang.FX.print("; //");
    }    
    function testLongSeqCastToByte() { 
       if (gen) javafx.lang.FX.println("\nvar resLongSeqCastToByte1:Byte[]=");
        xByte=vLong; 
       // xByte=[]; 
       if (gen)javafx.lang.FX.println(xByte);
       if (gen)javafx.lang.FX.print("; //");
        assertEquals(res.resLongSeqCastToByte1, xByte);
    }    

    function testLongSeqUnionWithByte() { 
       if (gen)javafx.lang.FX.println("\nvar resLongSeqUnionWithByte1:Byte[]=");
        xByte=vByte;
       // xByte=[]; 
        assertEquals(res.resLongSeqUnionWithByte1, xByte);
       if (gen)javafx.lang.FX.println(xByte);
       if (gen)javafx.lang.FX.println("; var resLongSeqUnionWithByte2:Byte[]=");
        xByte=[vByte, vLong];
       // xByte=[]; 
        assertEquals(res.resLongSeqUnionWithByte2, xByte);
       if (gen)javafx.lang.FX.println(xByte);
       if (gen)javafx.lang.FX.println("; var resLongSeqUnionWithByte3:Byte[]=");
        xByte=[vLong, vByte];
       // xByte=[]; 
        assertEquals(res.resLongSeqUnionWithByte3, xByte);
       if (gen)javafx.lang.FX.println(xByte);
       if (gen)javafx.lang.FX.print("; //");
    }    
    function testFloatSeqCastToByte() { 
       if (gen) javafx.lang.FX.println("\nvar resFloatSeqCastToByte1:Byte[]=");
        xByte=vFloat; 
       // xByte=[]; 
       if (gen)javafx.lang.FX.println(xByte);
       if (gen)javafx.lang.FX.print("; //");
        assertEquals(res.resFloatSeqCastToByte1, xByte);
    }    

    function testFloatSeqUnionWithByte() { 
       if (gen)javafx.lang.FX.println("\nvar resFloatSeqUnionWithByte1:Byte[]=");
        xByte=vByte;
       // xByte=[]; 
        assertEquals(res.resFloatSeqUnionWithByte1, xByte);
       if (gen)javafx.lang.FX.println(xByte);
       if (gen)javafx.lang.FX.println("; var resFloatSeqUnionWithByte2:Byte[]=");
        xByte=[vByte, vFloat];
       // xByte=[]; 
        assertEquals(res.resFloatSeqUnionWithByte2, xByte);
       if (gen)javafx.lang.FX.println(xByte);
       if (gen)javafx.lang.FX.println("; var resFloatSeqUnionWithByte3:Byte[]=");
        xByte=[vFloat, vByte];
       // xByte=[]; 
        assertEquals(res.resFloatSeqUnionWithByte3, xByte);
       if (gen)javafx.lang.FX.println(xByte);
       if (gen)javafx.lang.FX.print("; //");
    }    
    function testDoubleSeqCastToByte() { 
       if (gen) javafx.lang.FX.println("\nvar resDoubleSeqCastToByte1:Byte[]=");
        xByte=vDouble; 
       // xByte=[]; 
       if (gen)javafx.lang.FX.println(xByte);
       if (gen)javafx.lang.FX.print("; //");
        assertEquals(res.resDoubleSeqCastToByte1, xByte);
    }    

    function testDoubleSeqUnionWithByte() { 
       if (gen)javafx.lang.FX.println("\nvar resDoubleSeqUnionWithByte1:Byte[]=");
        xByte=vByte;
       // xByte=[]; 
        assertEquals(res.resDoubleSeqUnionWithByte1, xByte);
       if (gen)javafx.lang.FX.println(xByte);
       if (gen)javafx.lang.FX.println("; var resDoubleSeqUnionWithByte2:Byte[]=");
        xByte=[vByte, vDouble];
       // xByte=[]; 
        assertEquals(res.resDoubleSeqUnionWithByte2, xByte);
       if (gen)javafx.lang.FX.println(xByte);
       if (gen)javafx.lang.FX.println("; var resDoubleSeqUnionWithByte3:Byte[]=");
        xByte=[vDouble, vByte];
       // xByte=[]; 
        assertEquals(res.resDoubleSeqUnionWithByte3, xByte);
       if (gen)javafx.lang.FX.println(xByte);
       if (gen)javafx.lang.FX.print("; //");
    }    
    function testNumberSeqCastToByte() { 
       if (gen) javafx.lang.FX.println("\nvar resNumberSeqCastToByte1:Byte[]=");
        xByte=vNumber; 
       // xByte=[]; 
       if (gen)javafx.lang.FX.println(xByte);
       if (gen)javafx.lang.FX.print("; //");
        assertEquals(res.resNumberSeqCastToByte1, xByte);
    }    

    function testNumberSeqUnionWithByte() { 
       if (gen)javafx.lang.FX.println("\nvar resNumberSeqUnionWithByte1:Byte[]=");
        xByte=vByte;
       // xByte=[]; 
        assertEquals(res.resNumberSeqUnionWithByte1, xByte);
       if (gen)javafx.lang.FX.println(xByte);
       if (gen)javafx.lang.FX.println("; var resNumberSeqUnionWithByte2:Byte[]=");
        xByte=[vByte, vNumber];
       // xByte=[]; 
        assertEquals(res.resNumberSeqUnionWithByte2, xByte);
       if (gen)javafx.lang.FX.println(xByte);
       if (gen)javafx.lang.FX.println("; var resNumberSeqUnionWithByte3:Byte[]=");
        xByte=[vNumber, vByte];
       // xByte=[]; 
        assertEquals(res.resNumberSeqUnionWithByte3, xByte);
       if (gen)javafx.lang.FX.println(xByte);
       if (gen)javafx.lang.FX.print("; //");
    }    
    var xShort: Short[] = vShort;  
    function testByteSeqCastToShort() { 
       if (gen) javafx.lang.FX.println("\nvar resByteSeqCastToShort1:Short[]=");
        xShort=vByte; 
       // xShort=[]; 
       if (gen)javafx.lang.FX.println(xShort);
       if (gen)javafx.lang.FX.print("; //");
        assertEquals(res.resByteSeqCastToShort1, xShort);
    }    

    function testByteSeqUnionWithShort() { 
       if (gen)javafx.lang.FX.println("\nvar resByteSeqUnionWithShort1:Short[]=");
        xShort=vShort;
       // xShort=[]; 
        assertEquals(res.resByteSeqUnionWithShort1, xShort);
       if (gen)javafx.lang.FX.println(xShort);
       if (gen)javafx.lang.FX.println("; var resByteSeqUnionWithShort2:Short[]=");
        xShort=[vShort, vByte];
       // xShort=[]; 
        assertEquals(res.resByteSeqUnionWithShort2, xShort);
       if (gen)javafx.lang.FX.println(xShort);
       if (gen)javafx.lang.FX.println("; var resByteSeqUnionWithShort3:Short[]=");
        xShort=[vByte, vShort];
       // xShort=[]; 
        assertEquals(res.resByteSeqUnionWithShort3, xShort);
       if (gen)javafx.lang.FX.println(xShort);
       if (gen)javafx.lang.FX.print("; //");
    }    
    function testShortSeqCastToShort() { 
       if (gen) javafx.lang.FX.println("\nvar resShortSeqCastToShort1:Short[]=");
        xShort=vShort; 
       // xShort=[]; 
       if (gen)javafx.lang.FX.println(xShort);
       if (gen)javafx.lang.FX.print("; //");
        assertEquals(res.resShortSeqCastToShort1, xShort);
    }    

    function testShortSeqUnionWithShort() { 
       if (gen)javafx.lang.FX.println("\nvar resShortSeqUnionWithShort1:Short[]=");
        xShort=vShort;
       // xShort=[]; 
        assertEquals(res.resShortSeqUnionWithShort1, xShort);
       if (gen)javafx.lang.FX.println(xShort);
       if (gen)javafx.lang.FX.println("; var resShortSeqUnionWithShort2:Short[]=");
        xShort=[vShort, vShort];
       // xShort=[]; 
        assertEquals(res.resShortSeqUnionWithShort2, xShort);
       if (gen)javafx.lang.FX.println(xShort);
       if (gen)javafx.lang.FX.println("; var resShortSeqUnionWithShort3:Short[]=");
        xShort=[vShort, vShort];
       // xShort=[]; 
        assertEquals(res.resShortSeqUnionWithShort3, xShort);
       if (gen)javafx.lang.FX.println(xShort);
       if (gen)javafx.lang.FX.print("; //");
    }    
    function testCharacterSeqCastToShort() { 
       if (gen) javafx.lang.FX.println("\nvar resCharacterSeqCastToShort1:Short[]=");
       //  xShort=vCharacter; 
        xShort=[]; 
       if (gen)javafx.lang.FX.println(xShort);
       if (gen)javafx.lang.FX.print("; //");
       //  assertEquals(res.resCharacterSeqCastToShort1, xShort);
    }    

    function testCharacterSeqUnionWithShort() { 
       if (gen)javafx.lang.FX.println("\nvar resCharacterSeqUnionWithShort1:Short[]=");
       //  xShort=vShort;
        xShort=[]; 
       //  assertEquals(res.resCharacterSeqUnionWithShort1, xShort);
       if (gen)javafx.lang.FX.println(xShort);
       if (gen)javafx.lang.FX.println("; var resCharacterSeqUnionWithShort2:Short[]=");
       //  xShort=[vShort, vCharacter];
        xShort=[]; 
       //  assertEquals(res.resCharacterSeqUnionWithShort2, xShort);
       if (gen)javafx.lang.FX.println(xShort);
       if (gen)javafx.lang.FX.println("; var resCharacterSeqUnionWithShort3:Short[]=");
       //  xShort=[vCharacter, vShort];
        xShort=[]; 
       //  assertEquals(res.resCharacterSeqUnionWithShort3, xShort);
       if (gen)javafx.lang.FX.println(xShort);
       if (gen)javafx.lang.FX.print("; //");
    }    
    function testIntegerSeqCastToShort() { 
       if (gen) javafx.lang.FX.println("\nvar resIntegerSeqCastToShort1:Short[]=");
        xShort=vInteger; 
       // xShort=[]; 
       if (gen)javafx.lang.FX.println(xShort);
       if (gen)javafx.lang.FX.print("; //");
        assertEquals(res.resIntegerSeqCastToShort1, xShort);
    }    

    function testIntegerSeqUnionWithShort() { 
       if (gen)javafx.lang.FX.println("\nvar resIntegerSeqUnionWithShort1:Short[]=");
        xShort=vShort;
       // xShort=[]; 
        assertEquals(res.resIntegerSeqUnionWithShort1, xShort);
       if (gen)javafx.lang.FX.println(xShort);
       if (gen)javafx.lang.FX.println("; var resIntegerSeqUnionWithShort2:Short[]=");
        xShort=[vShort, vInteger];
       // xShort=[]; 
        assertEquals(res.resIntegerSeqUnionWithShort2, xShort);
       if (gen)javafx.lang.FX.println(xShort);
       if (gen)javafx.lang.FX.println("; var resIntegerSeqUnionWithShort3:Short[]=");
        xShort=[vInteger, vShort];
       // xShort=[]; 
        assertEquals(res.resIntegerSeqUnionWithShort3, xShort);
       if (gen)javafx.lang.FX.println(xShort);
       if (gen)javafx.lang.FX.print("; //");
    }    
    function testLongSeqCastToShort() { 
       if (gen) javafx.lang.FX.println("\nvar resLongSeqCastToShort1:Short[]=");
        xShort=vLong; 
       // xShort=[]; 
       if (gen)javafx.lang.FX.println(xShort);
       if (gen)javafx.lang.FX.print("; //");
        assertEquals(res.resLongSeqCastToShort1, xShort);
    }    

    function testLongSeqUnionWithShort() { 
       if (gen)javafx.lang.FX.println("\nvar resLongSeqUnionWithShort1:Short[]=");
        xShort=vShort;
       // xShort=[]; 
        assertEquals(res.resLongSeqUnionWithShort1, xShort);
       if (gen)javafx.lang.FX.println(xShort);
       if (gen)javafx.lang.FX.println("; var resLongSeqUnionWithShort2:Short[]=");
        xShort=[vShort, vLong];
       // xShort=[]; 
        assertEquals(res.resLongSeqUnionWithShort2, xShort);
       if (gen)javafx.lang.FX.println(xShort);
       if (gen)javafx.lang.FX.println("; var resLongSeqUnionWithShort3:Short[]=");
        xShort=[vLong, vShort];
       // xShort=[]; 
        assertEquals(res.resLongSeqUnionWithShort3, xShort);
       if (gen)javafx.lang.FX.println(xShort);
       if (gen)javafx.lang.FX.print("; //");
    }    
    function testFloatSeqCastToShort() { 
       if (gen) javafx.lang.FX.println("\nvar resFloatSeqCastToShort1:Short[]=");
        xShort=vFloat; 
       // xShort=[]; 
       if (gen)javafx.lang.FX.println(xShort);
       if (gen)javafx.lang.FX.print("; //");
        assertEquals(res.resFloatSeqCastToShort1, xShort);
    }    

    function testFloatSeqUnionWithShort() { 
       if (gen)javafx.lang.FX.println("\nvar resFloatSeqUnionWithShort1:Short[]=");
        xShort=vShort;
       // xShort=[]; 
        assertEquals(res.resFloatSeqUnionWithShort1, xShort);
       if (gen)javafx.lang.FX.println(xShort);
       if (gen)javafx.lang.FX.println("; var resFloatSeqUnionWithShort2:Short[]=");
        xShort=[vShort, vFloat];
       // xShort=[]; 
        assertEquals(res.resFloatSeqUnionWithShort2, xShort);
       if (gen)javafx.lang.FX.println(xShort);
       if (gen)javafx.lang.FX.println("; var resFloatSeqUnionWithShort3:Short[]=");
        xShort=[vFloat, vShort];
       // xShort=[]; 
        assertEquals(res.resFloatSeqUnionWithShort3, xShort);
       if (gen)javafx.lang.FX.println(xShort);
       if (gen)javafx.lang.FX.print("; //");
    }    
    function testDoubleSeqCastToShort() { 
       if (gen) javafx.lang.FX.println("\nvar resDoubleSeqCastToShort1:Short[]=");
        xShort=vDouble; 
       // xShort=[]; 
       if (gen)javafx.lang.FX.println(xShort);
       if (gen)javafx.lang.FX.print("; //");
        assertEquals(res.resDoubleSeqCastToShort1, xShort);
    }    

    function testDoubleSeqUnionWithShort() { 
       if (gen)javafx.lang.FX.println("\nvar resDoubleSeqUnionWithShort1:Short[]=");
        xShort=vShort;
       // xShort=[]; 
        assertEquals(res.resDoubleSeqUnionWithShort1, xShort);
       if (gen)javafx.lang.FX.println(xShort);
       if (gen)javafx.lang.FX.println("; var resDoubleSeqUnionWithShort2:Short[]=");
        xShort=[vShort, vDouble];
       // xShort=[]; 
        assertEquals(res.resDoubleSeqUnionWithShort2, xShort);
       if (gen)javafx.lang.FX.println(xShort);
       if (gen)javafx.lang.FX.println("; var resDoubleSeqUnionWithShort3:Short[]=");
        xShort=[vDouble, vShort];
       // xShort=[]; 
        assertEquals(res.resDoubleSeqUnionWithShort3, xShort);
       if (gen)javafx.lang.FX.println(xShort);
       if (gen)javafx.lang.FX.print("; //");
    }    
    function testNumberSeqCastToShort() { 
       if (gen) javafx.lang.FX.println("\nvar resNumberSeqCastToShort1:Short[]=");
        xShort=vNumber; 
       // xShort=[]; 
       if (gen)javafx.lang.FX.println(xShort);
       if (gen)javafx.lang.FX.print("; //");
        assertEquals(res.resNumberSeqCastToShort1, xShort);
    }    

    function testNumberSeqUnionWithShort() { 
       if (gen)javafx.lang.FX.println("\nvar resNumberSeqUnionWithShort1:Short[]=");
        xShort=vShort;
       // xShort=[]; 
        assertEquals(res.resNumberSeqUnionWithShort1, xShort);
       if (gen)javafx.lang.FX.println(xShort);
       if (gen)javafx.lang.FX.println("; var resNumberSeqUnionWithShort2:Short[]=");
        xShort=[vShort, vNumber];
       // xShort=[]; 
        assertEquals(res.resNumberSeqUnionWithShort2, xShort);
       if (gen)javafx.lang.FX.println(xShort);
       if (gen)javafx.lang.FX.println("; var resNumberSeqUnionWithShort3:Short[]=");
        xShort=[vNumber, vShort];
       // xShort=[]; 
        assertEquals(res.resNumberSeqUnionWithShort3, xShort);
       if (gen)javafx.lang.FX.println(xShort);
       if (gen)javafx.lang.FX.print("; //");
    }    
    var xCharacter: Character[] = vCharacter;  
    function testByteSeqCastToCharacter() { 
       if (gen) javafx.lang.FX.println("\nvar resByteSeqCastToCharacter1:Character[]=");
       //  xCharacter=vByte; 
        xCharacter=[]; 
       if (gen)javafx.lang.FX.println(xCharacter);
       if (gen)javafx.lang.FX.print("; //");
       //  assertEquals(res.resByteSeqCastToCharacter1, xCharacter);
    }    

    function testByteSeqUnionWithCharacter() { 
       if (gen)javafx.lang.FX.println("\nvar resByteSeqUnionWithCharacter1:Character[]=");
       //  xCharacter=vCharacter;
        xCharacter=[]; 
       //  assertEquals(res.resByteSeqUnionWithCharacter1, xCharacter);
       if (gen)javafx.lang.FX.println(xCharacter);
       if (gen)javafx.lang.FX.println("; var resByteSeqUnionWithCharacter2:Character[]=");
       //  xCharacter=[vCharacter,  vByte];
        xCharacter=[]; 
       //  assertEquals(res.resByteSeqUnionWithCharacter2, xCharacter);
       if (gen)javafx.lang.FX.println(xCharacter);
       if (gen)javafx.lang.FX.println("; var resByteSeqUnionWithCharacter3:Character[]=");
       //  xCharacter=[vByte, vCharacter];
        xCharacter=[]; 
       //  assertEquals(res.resByteSeqUnionWithCharacter3, xCharacter);
       if (gen)javafx.lang.FX.println(xCharacter);
       if (gen)javafx.lang.FX.print("; //");
    }    
    function testShortSeqCastToCharacter() { 
       if (gen) javafx.lang.FX.println("\nvar resShortSeqCastToCharacter1:Character[]=");
       //  xCharacter=vShort; 
        xCharacter=[]; 
       if (gen)javafx.lang.FX.println(xCharacter);
       if (gen)javafx.lang.FX.print("; //");
       //  assertEquals(res.resShortSeqCastToCharacter1, xCharacter);
    }    

    function testShortSeqUnionWithCharacter() { 
       if (gen)javafx.lang.FX.println("\nvar resShortSeqUnionWithCharacter1:Character[]=");
       //  xCharacter=vCharacter;
        xCharacter=[]; 
       //  assertEquals(res.resShortSeqUnionWithCharacter1, xCharacter);
       if (gen)javafx.lang.FX.println(xCharacter);
       if (gen)javafx.lang.FX.println("; var resShortSeqUnionWithCharacter2:Character[]=");
       //  xCharacter=[vCharacter, vShort];
        xCharacter=[]; 
       //  assertEquals(res.resShortSeqUnionWithCharacter2, xCharacter);
       if (gen)javafx.lang.FX.println(xCharacter);
       if (gen)javafx.lang.FX.println("; var resShortSeqUnionWithCharacter3:Character[]=");
       //  xCharacter=[vShort, vCharacter];
        xCharacter=[]; 
       //  assertEquals(res.resShortSeqUnionWithCharacter3, xCharacter);
       if (gen)javafx.lang.FX.println(xCharacter);
       if (gen)javafx.lang.FX.print("; //");
    }    
    function testCharacterSeqCastToCharacter() { 
       if (gen) javafx.lang.FX.println("\nvar resCharacterSeqCastToCharacter1:Character[]=");
       //  xCharacter=vCharacter; 
        xCharacter=[]; 
       if (gen)javafx.lang.FX.println(xCharacter);
       if (gen)javafx.lang.FX.print("; //");
       //  assertEquals(res.resCharacterSeqCastToCharacter1, xCharacter);
    }    

    function testCharacterSeqUnionWithCharacter() { 
       if (gen)javafx.lang.FX.println("\nvar resCharacterSeqUnionWithCharacter1:Character[]=");
       //  xCharacter=vCharacter;
        xCharacter=[]; 
       //  assertEquals(res.resCharacterSeqUnionWithCharacter1, xCharacter);
       if (gen)javafx.lang.FX.println(xCharacter);
       if (gen)javafx.lang.FX.println("; var resCharacterSeqUnionWithCharacter2:Character[]=");
       //  xCharacter=[vCharacter, vCharacter];
        xCharacter=[]; 
       //  assertEquals(res.resCharacterSeqUnionWithCharacter2, xCharacter);
       if (gen)javafx.lang.FX.println(xCharacter);
       if (gen)javafx.lang.FX.println("; var resCharacterSeqUnionWithCharacter3:Character[]=");
       //  xCharacter=[vCharacter, vCharacter];
        xCharacter=[]; 
       //  assertEquals(res.resCharacterSeqUnionWithCharacter3, xCharacter);
       if (gen)javafx.lang.FX.println(xCharacter);
       if (gen)javafx.lang.FX.print("; //");
    }    
    function testIntegerSeqCastToCharacter() { 
       if (gen) javafx.lang.FX.println("\nvar resIntegerSeqCastToCharacter1:Character[]=");
       //  xCharacter=vInteger; 
        xCharacter=[]; 
       if (gen)javafx.lang.FX.println(xCharacter);
       if (gen)javafx.lang.FX.print("; //");
       //  assertEquals(res.resIntegerSeqCastToCharacter1, xCharacter);
    }    

    function testIntegerSeqUnionWithCharacter() { 
       if (gen)javafx.lang.FX.println("\nvar resIntegerSeqUnionWithCharacter1:Character[]=");
       //  xCharacter=vCharacter;
        xCharacter=[]; 
       //  assertEquals(res.resIntegerSeqUnionWithCharacter1, xCharacter);
       if (gen)javafx.lang.FX.println(xCharacter);
       if (gen)javafx.lang.FX.println("; var resIntegerSeqUnionWithCharacter2:Character[]=");
       //  xCharacter=[vCharacter, vInteger];
        xCharacter=[]; 
       //  assertEquals(res.resIntegerSeqUnionWithCharacter2, xCharacter);
       if (gen)javafx.lang.FX.println(xCharacter);
       if (gen)javafx.lang.FX.println("; var resIntegerSeqUnionWithCharacter3:Character[]=");
       //  xCharacter=[vInteger, vCharacter];
        xCharacter=[]; 
       //  assertEquals(res.resIntegerSeqUnionWithCharacter3, xCharacter);
       if (gen)javafx.lang.FX.println(xCharacter);
       if (gen)javafx.lang.FX.print("; //");
    }    
    function testLongSeqCastToCharacter() { 
       if (gen) javafx.lang.FX.println("\nvar resLongSeqCastToCharacter1:Character[]=");
       //  xCharacter=vLong; 
        xCharacter=[]; 
       if (gen)javafx.lang.FX.println(xCharacter);
       if (gen)javafx.lang.FX.print("; //");
       //  assertEquals(res.resLongSeqCastToCharacter1, xCharacter);
    }    

    function testLongSeqUnionWithCharacter() { 
       if (gen)javafx.lang.FX.println("\nvar resLongSeqUnionWithCharacter1:Character[]=");
       //  xCharacter=vCharacter;
        xCharacter=[]; 
       //  assertEquals(res.resLongSeqUnionWithCharacter1, xCharacter);
       if (gen)javafx.lang.FX.println(xCharacter);
       if (gen)javafx.lang.FX.println("; var resLongSeqUnionWithCharacter2:Character[]=");
       //  xCharacter=[vCharacter, vLong];
        xCharacter=[]; 
       //  assertEquals(res.resLongSeqUnionWithCharacter2, xCharacter);
       if (gen)javafx.lang.FX.println(xCharacter);
       if (gen)javafx.lang.FX.println("; var resLongSeqUnionWithCharacter3:Character[]=");
       //  xCharacter=[vLong, vCharacter];
        xCharacter=[]; 
       //  assertEquals(res.resLongSeqUnionWithCharacter3, xCharacter);
       if (gen)javafx.lang.FX.println(xCharacter);
       if (gen)javafx.lang.FX.print("; //");
    }    
    function testFloatSeqCastToCharacter() { 
       if (gen) javafx.lang.FX.println("\nvar resFloatSeqCastToCharacter1:Character[]=");
       //  xCharacter=vFloat; 
        xCharacter=[]; 
       if (gen)javafx.lang.FX.println(xCharacter);
       if (gen)javafx.lang.FX.print("; //");
       //  assertEquals(res.resFloatSeqCastToCharacter1, xCharacter);
    }    

    function testFloatSeqUnionWithCharacter() { 
       if (gen)javafx.lang.FX.println("\nvar resFloatSeqUnionWithCharacter1:Character[]=");
       //  xCharacter=vCharacter;
        xCharacter=[]; 
       //  assertEquals(res.resFloatSeqUnionWithCharacter1, xCharacter);
       if (gen)javafx.lang.FX.println(xCharacter);
       if (gen)javafx.lang.FX.println("; var resFloatSeqUnionWithCharacter2:Character[]=");
       //  xCharacter=[vCharacter, vFloat];
        xCharacter=[]; 
       //  assertEquals(res.resFloatSeqUnionWithCharacter2, xCharacter);
       if (gen)javafx.lang.FX.println(xCharacter);
       if (gen)javafx.lang.FX.println("; var resFloatSeqUnionWithCharacter3:Character[]=");
       //  xCharacter=[vFloat, vCharacter];
        xCharacter=[]; 
       //  assertEquals(res.resFloatSeqUnionWithCharacter3, xCharacter);
       if (gen)javafx.lang.FX.println(xCharacter);
       if (gen)javafx.lang.FX.print("; //");
    }    
    function testDoubleSeqCastToCharacter() { 
       if (gen) javafx.lang.FX.println("\nvar resDoubleSeqCastToCharacter1:Character[]=");
       //  xCharacter=vDouble; 
        xCharacter=[]; 
       if (gen)javafx.lang.FX.println(xCharacter);
       if (gen)javafx.lang.FX.print("; //");
       //  assertEquals(res.resDoubleSeqCastToCharacter1, xCharacter);
    }    

    function testDoubleSeqUnionWithCharacter() { 
       if (gen)javafx.lang.FX.println("\nvar resDoubleSeqUnionWithCharacter1:Character[]=");
       //  xCharacter=vCharacter;
        xCharacter=[]; 
       //  assertEquals(res.resDoubleSeqUnionWithCharacter1, xCharacter);
       if (gen)javafx.lang.FX.println(xCharacter);
       if (gen)javafx.lang.FX.println("; var resDoubleSeqUnionWithCharacter2:Character[]=");
       //  xCharacter=[vCharacter, vDouble];
        xCharacter=[]; 
       //  assertEquals(res.resDoubleSeqUnionWithCharacter2, xCharacter);
       if (gen)javafx.lang.FX.println(xCharacter);
       if (gen)javafx.lang.FX.println("; var resDoubleSeqUnionWithCharacter3:Character[]=");
       //  xCharacter=[vDouble, vCharacter];
        xCharacter=[]; 
       //  assertEquals(res.resDoubleSeqUnionWithCharacter3, xCharacter);
       if (gen)javafx.lang.FX.println(xCharacter);
       if (gen)javafx.lang.FX.print("; //");
    }    
    function testNumberSeqCastToCharacter() { 
       if (gen) javafx.lang.FX.println("\nvar resNumberSeqCastToCharacter1:Character[]=");
       //  xCharacter=vNumber; 
        xCharacter=[]; 
       if (gen)javafx.lang.FX.println(xCharacter);
       if (gen)javafx.lang.FX.print("; //");
       //  assertEquals(res.resNumberSeqCastToCharacter1, xCharacter);
    }    

    function testNumberSeqUnionWithCharacter() { 
       if (gen)javafx.lang.FX.println("\nvar resNumberSeqUnionWithCharacter1:Character[]=");
       //  xCharacter=vCharacter;
        xCharacter=[]; 
       //  assertEquals(res.resNumberSeqUnionWithCharacter1, xCharacter);
       if (gen)javafx.lang.FX.println(xCharacter);
       if (gen)javafx.lang.FX.println("; var resNumberSeqUnionWithCharacter2:Character[]=");
       //  xCharacter=[vCharacter, vNumber];
        xCharacter=[]; 
       //  assertEquals(res.resNumberSeqUnionWithCharacter2, xCharacter);
       if (gen)javafx.lang.FX.println(xCharacter);
       if (gen)javafx.lang.FX.println("; var resNumberSeqUnionWithCharacter3:Character[]=");
       //  xCharacter=[vNumber, vCharacter];
        xCharacter=[]; 
       //  assertEquals(res.resNumberSeqUnionWithCharacter3, xCharacter);
       if (gen)javafx.lang.FX.println(xCharacter);
       if (gen)javafx.lang.FX.print("; //");
    }    
    var xInteger: Integer[] = vInteger;  
    function testByteSeqCastToInteger() { 
       if (gen) javafx.lang.FX.println("\nvar resByteSeqCastToInteger1:Integer[]=");
        xInteger=vByte; 
       // xInteger=[]; 
       if (gen)javafx.lang.FX.println(xInteger);
       if (gen)javafx.lang.FX.print("; //");
        assertEquals(res.resByteSeqCastToInteger1, xInteger);
    }    

    function testByteSeqUnionWithInteger() { 
       if (gen)javafx.lang.FX.println("\nvar resByteSeqUnionWithInteger1:Integer[]=");
        xInteger=vInteger;
       // xInteger=[]; 
        assertEquals(res.resByteSeqUnionWithInteger1, xInteger);
       if (gen)javafx.lang.FX.println(xInteger);
       if (gen)javafx.lang.FX.println("; var resByteSeqUnionWithInteger2:Integer[]=");
        xInteger=[vInteger, vByte];
       // xInteger=[]; 
        assertEquals(res.resByteSeqUnionWithInteger2, xInteger);
       if (gen)javafx.lang.FX.println(xInteger);
       if (gen)javafx.lang.FX.println("; var resByteSeqUnionWithInteger3:Integer[]=");
        xInteger=[vByte, vInteger];
       // xInteger=[]; 
        assertEquals(res.resByteSeqUnionWithInteger3, xInteger);
       if (gen)javafx.lang.FX.println(xInteger);
       if (gen)javafx.lang.FX.print("; //");
    }    
    function testShortSeqCastToInteger() { 
       if (gen) javafx.lang.FX.println("\nvar resShortSeqCastToInteger1:Integer[]=");
        xInteger=vShort; 
       // xInteger=[]; 
       if (gen)javafx.lang.FX.println(xInteger);
       if (gen)javafx.lang.FX.print("; //");
        assertEquals(res.resShortSeqCastToInteger1, xInteger);
    }    

    function testShortSeqUnionWithInteger() { 
       if (gen)javafx.lang.FX.println("\nvar resShortSeqUnionWithInteger1:Integer[]=");
        xInteger=vInteger;
       // xInteger=[]; 
        assertEquals(res.resShortSeqUnionWithInteger1, xInteger);
       if (gen)javafx.lang.FX.println(xInteger);
       if (gen)javafx.lang.FX.println("; var resShortSeqUnionWithInteger2:Integer[]=");
        xInteger=[vInteger, vShort];
       // xInteger=[]; 
        assertEquals(res.resShortSeqUnionWithInteger2, xInteger);
       if (gen)javafx.lang.FX.println(xInteger);
       if (gen)javafx.lang.FX.println("; var resShortSeqUnionWithInteger3:Integer[]=");
        xInteger=[vShort, vInteger];
       // xInteger=[]; 
        assertEquals(res.resShortSeqUnionWithInteger3, xInteger);
       if (gen)javafx.lang.FX.println(xInteger);
       if (gen)javafx.lang.FX.print("; //");
    }    
    function testCharacterSeqCastToInteger() { 
       if (gen) javafx.lang.FX.println("\nvar resCharacterSeqCastToInteger1:Integer[]=");
       //  xInteger=vCharacter; 
        xInteger=[]; 
       if (gen)javafx.lang.FX.println(xInteger);
       if (gen)javafx.lang.FX.print("; //");
       //  assertEquals(res.resCharacterSeqCastToInteger1, xInteger);
    }    

    function testCharacterSeqUnionWithInteger() { 
       if (gen)javafx.lang.FX.println("\nvar resCharacterSeqUnionWithInteger1:Integer[]=");
       //  xInteger=vInteger;
        xInteger=[]; 
       //  assertEquals(res.resCharacterSeqUnionWithInteger1, xInteger);
       if (gen)javafx.lang.FX.println(xInteger);
       if (gen)javafx.lang.FX.println("; var resCharacterSeqUnionWithInteger2:Integer[]=");
       //  xInteger=[vInteger, vCharacter];
        xInteger=[]; 
       //  assertEquals(res.resCharacterSeqUnionWithInteger2, xInteger);
       if (gen)javafx.lang.FX.println(xInteger);
       if (gen)javafx.lang.FX.println("; var resCharacterSeqUnionWithInteger3:Integer[]=");
       //  xInteger=[vCharacter, vInteger];
        xInteger=[]; 
       //  assertEquals(res.resCharacterSeqUnionWithInteger3, xInteger);
       if (gen)javafx.lang.FX.println(xInteger);
       if (gen)javafx.lang.FX.print("; //");
    }    
    function testIntegerSeqCastToInteger() { 
       if (gen) javafx.lang.FX.println("\nvar resIntegerSeqCastToInteger1:Integer[]=");
        xInteger=vInteger; 
       // xInteger=[]; 
       if (gen)javafx.lang.FX.println(xInteger);
       if (gen)javafx.lang.FX.print("; //");
        assertEquals(res.resIntegerSeqCastToInteger1, xInteger);
    }    

    function testIntegerSeqUnionWithInteger() { 
       if (gen)javafx.lang.FX.println("\nvar resIntegerSeqUnionWithInteger1:Integer[]=");
        xInteger=vInteger;
       // xInteger=[]; 
        assertEquals(res.resIntegerSeqUnionWithInteger1, xInteger);
       if (gen)javafx.lang.FX.println(xInteger);
       if (gen)javafx.lang.FX.println("; var resIntegerSeqUnionWithInteger2:Integer[]=");
        xInteger=[vInteger, vInteger];
       // xInteger=[]; 
        assertEquals(res.resIntegerSeqUnionWithInteger2, xInteger);
       if (gen)javafx.lang.FX.println(xInteger);
       if (gen)javafx.lang.FX.println("; var resIntegerSeqUnionWithInteger3:Integer[]=");
        xInteger=[vInteger, vInteger];
       // xInteger=[]; 
        assertEquals(res.resIntegerSeqUnionWithInteger3, xInteger);
       if (gen)javafx.lang.FX.println(xInteger);
       if (gen)javafx.lang.FX.print("; //");
    }    
    function testLongSeqCastToInteger() { 
       if (gen) javafx.lang.FX.println("\nvar resLongSeqCastToInteger1:Integer[]=");
        xInteger=vLong; 
       // xInteger=[]; 
       if (gen)javafx.lang.FX.println(xInteger);
       if (gen)javafx.lang.FX.print("; //");
        assertEquals(res.resLongSeqCastToInteger1, xInteger);
    }    

    function testLongSeqUnionWithInteger() { 
       if (gen)javafx.lang.FX.println("\nvar resLongSeqUnionWithInteger1:Integer[]=");
        xInteger=vInteger;
       // xInteger=[]; 
        assertEquals(res.resLongSeqUnionWithInteger1, xInteger);
       if (gen)javafx.lang.FX.println(xInteger);
       if (gen)javafx.lang.FX.println("; var resLongSeqUnionWithInteger2:Integer[]=");
        xInteger=[vInteger, vLong];
       // xInteger=[]; 
        assertEquals(res.resLongSeqUnionWithInteger2, xInteger);
       if (gen)javafx.lang.FX.println(xInteger);
       if (gen)javafx.lang.FX.println("; var resLongSeqUnionWithInteger3:Integer[]=");
        xInteger=[vLong, vInteger];
       // xInteger=[]; 
        assertEquals(res.resLongSeqUnionWithInteger3, xInteger);
       if (gen)javafx.lang.FX.println(xInteger);
       if (gen)javafx.lang.FX.print("; //");
    }    
    function testFloatSeqCastToInteger() { 
       if (gen) javafx.lang.FX.println("\nvar resFloatSeqCastToInteger1:Integer[]=");
        xInteger=vFloat; 
       // xInteger=[]; 
       if (gen)javafx.lang.FX.println(xInteger);
       if (gen)javafx.lang.FX.print("; //");
        assertEquals(res.resFloatSeqCastToInteger1, xInteger);
    }    

    function testFloatSeqUnionWithInteger() { 
       if (gen)javafx.lang.FX.println("\nvar resFloatSeqUnionWithInteger1:Integer[]=");
        xInteger=vInteger;
       // xInteger=[]; 
        assertEquals(res.resFloatSeqUnionWithInteger1, xInteger);
       if (gen)javafx.lang.FX.println(xInteger);
       if (gen)javafx.lang.FX.println("; var resFloatSeqUnionWithInteger2:Integer[]=");
        xInteger=[vInteger, vFloat];
       // xInteger=[]; 
        assertEquals(res.resFloatSeqUnionWithInteger2, xInteger);
       if (gen)javafx.lang.FX.println(xInteger);
       if (gen)javafx.lang.FX.println("; var resFloatSeqUnionWithInteger3:Integer[]=");
        xInteger=[vFloat, vInteger];
       // xInteger=[]; 
        assertEquals(res.resFloatSeqUnionWithInteger3, xInteger);
       if (gen)javafx.lang.FX.println(xInteger);
       if (gen)javafx.lang.FX.print("; //");
    }    
    function testDoubleSeqCastToInteger() { 
       if (gen) javafx.lang.FX.println("\nvar resDoubleSeqCastToInteger1:Integer[]=");
        xInteger=vDouble; 
       // xInteger=[]; 
       if (gen)javafx.lang.FX.println(xInteger);
       if (gen)javafx.lang.FX.print("; //");
        assertEquals(res.resDoubleSeqCastToInteger1, xInteger);
    }    

    function testDoubleSeqUnionWithInteger() { 
       if (gen)javafx.lang.FX.println("\nvar resDoubleSeqUnionWithInteger1:Integer[]=");
        xInteger=vInteger;
       // xInteger=[]; 
        assertEquals(res.resDoubleSeqUnionWithInteger1, xInteger);
       if (gen)javafx.lang.FX.println(xInteger);
       if (gen)javafx.lang.FX.println("; var resDoubleSeqUnionWithInteger2:Integer[]=");
        xInteger=[vInteger, vDouble];
       // xInteger=[]; 
        assertEquals(res.resDoubleSeqUnionWithInteger2, xInteger);
       if (gen)javafx.lang.FX.println(xInteger);
       if (gen)javafx.lang.FX.println("; var resDoubleSeqUnionWithInteger3:Integer[]=");
        xInteger=[vDouble, vInteger];
       // xInteger=[]; 
        assertEquals(res.resDoubleSeqUnionWithInteger3, xInteger);
       if (gen)javafx.lang.FX.println(xInteger);
       if (gen)javafx.lang.FX.print("; //");
    }    
    function testNumberSeqCastToInteger() { 
       if (gen) javafx.lang.FX.println("\nvar resNumberSeqCastToInteger1:Integer[]=");
        xInteger=vNumber; 
       // xInteger=[]; 
       if (gen)javafx.lang.FX.println(xInteger);
       if (gen)javafx.lang.FX.print("; //");
        assertEquals(res.resNumberSeqCastToInteger1, xInteger);
    }    

    function testNumberSeqUnionWithInteger() { 
       if (gen)javafx.lang.FX.println("\nvar resNumberSeqUnionWithInteger1:Integer[]=");
        xInteger=vInteger;
       // xInteger=[]; 
        assertEquals(res.resNumberSeqUnionWithInteger1, xInteger);
       if (gen)javafx.lang.FX.println(xInteger);
       if (gen)javafx.lang.FX.println("; var resNumberSeqUnionWithInteger2:Integer[]=");
        xInteger=[vInteger, vNumber];
       // xInteger=[]; 
        assertEquals(res.resNumberSeqUnionWithInteger2, xInteger);
       if (gen)javafx.lang.FX.println(xInteger);
       if (gen)javafx.lang.FX.println("; var resNumberSeqUnionWithInteger3:Integer[]=");
        xInteger=[vNumber, vInteger];
       // xInteger=[]; 
        assertEquals(res.resNumberSeqUnionWithInteger3, xInteger);
       if (gen)javafx.lang.FX.println(xInteger);
       if (gen)javafx.lang.FX.print("; //");
    }    
    var xLong: Long[] = vLong;  
    function testByteSeqCastToLong() { 
       if (gen) javafx.lang.FX.println("\nvar resByteSeqCastToLong1:Long[]=");
        xLong=vByte; 
       // xLong=[]; 
       if (gen)javafx.lang.FX.println(xLong);
       if (gen)javafx.lang.FX.print("; //");
        assertEquals(res.resByteSeqCastToLong1, xLong);
    }    

    function testByteSeqUnionWithLong() { 
       if (gen)javafx.lang.FX.println("\nvar resByteSeqUnionWithLong1:Long[]=");
        xLong=vLong;
       // xLong=[]; 
        assertEquals(res.resByteSeqUnionWithLong1, xLong);
       if (gen)javafx.lang.FX.println(xLong);
       if (gen)javafx.lang.FX.println("; var resByteSeqUnionWithLong2:Long[]=");
        xLong=[vLong, vByte];
       // xLong=[]; 
        assertEquals(res.resByteSeqUnionWithLong2, xLong);
       if (gen)javafx.lang.FX.println(xLong);
       if (gen)javafx.lang.FX.println("; var resByteSeqUnionWithLong3:Long[]=");
        xLong=[vByte, vLong];
       // xLong=[]; 
        assertEquals(res.resByteSeqUnionWithLong3, xLong);
       if (gen)javafx.lang.FX.println(xLong);
       if (gen)javafx.lang.FX.print("; //");
    }    
    function testShortSeqCastToLong() { 
       if (gen) javafx.lang.FX.println("\nvar resShortSeqCastToLong1:Long[]=");
        xLong=vShort; 
       // xLong=[]; 
       if (gen)javafx.lang.FX.println(xLong);
       if (gen)javafx.lang.FX.print("; //");
        assertEquals(res.resShortSeqCastToLong1, xLong);
    }    

    function testShortSeqUnionWithLong() { 
       if (gen)javafx.lang.FX.println("\nvar resShortSeqUnionWithLong1:Long[]=");
        xLong=vLong;
       // xLong=[]; 
        assertEquals(res.resShortSeqUnionWithLong1, xLong);
       if (gen)javafx.lang.FX.println(xLong);
       if (gen)javafx.lang.FX.println("; var resShortSeqUnionWithLong2:Long[]=");
        xLong=[vLong, vShort];
       // xLong=[]; 
        assertEquals(res.resShortSeqUnionWithLong2, xLong);
       if (gen)javafx.lang.FX.println(xLong);
       if (gen)javafx.lang.FX.println("; var resShortSeqUnionWithLong3:Long[]=");
        xLong=[vShort, vLong];
       // xLong=[]; 
        assertEquals(res.resShortSeqUnionWithLong3, xLong);
       if (gen)javafx.lang.FX.println(xLong);
       if (gen)javafx.lang.FX.print("; //");
    }    
    function testCharacterSeqCastToLong() { 
       if (gen) javafx.lang.FX.println("\nvar resCharacterSeqCastToLong1:Long[]=");
       //  xLong=vCharacter; 
        xLong=[]; 
       if (gen)javafx.lang.FX.println(xLong);
       if (gen)javafx.lang.FX.print("; //");
       //  assertEquals(res.resCharacterSeqCastToLong1, xLong);
    }    

    function testCharacterSeqUnionWithLong() { 
       if (gen)javafx.lang.FX.println("\nvar resCharacterSeqUnionWithLong1:Long[]=");
       //  xLong=vLong;
        xLong=[]; 
       //  assertEquals(res.resCharacterSeqUnionWithLong1, xLong);
       if (gen)javafx.lang.FX.println(xLong);
       if (gen)javafx.lang.FX.println("; var resCharacterSeqUnionWithLong2:Long[]=");
       //  xLong=[vLong, vCharacter];
        xLong=[]; 
       //  assertEquals(res.resCharacterSeqUnionWithLong2, xLong);
       if (gen)javafx.lang.FX.println(xLong);
       if (gen)javafx.lang.FX.println("; var resCharacterSeqUnionWithLong3:Long[]=");
       //  xLong=[vCharacter, vLong];
        xLong=[]; 
       //  assertEquals(res.resCharacterSeqUnionWithLong3, xLong);
       if (gen)javafx.lang.FX.println(xLong);
       if (gen)javafx.lang.FX.print("; //");
    }    
    function testIntegerSeqCastToLong() { 
       if (gen) javafx.lang.FX.println("\nvar resIntegerSeqCastToLong1:Long[]=");
        xLong=vInteger; 
       // xLong=[]; 
       if (gen)javafx.lang.FX.println(xLong);
       if (gen)javafx.lang.FX.print("; //");
        assertEquals(res.resIntegerSeqCastToLong1, xLong);
    }    

    function testIntegerSeqUnionWithLong() { 
       if (gen)javafx.lang.FX.println("\nvar resIntegerSeqUnionWithLong1:Long[]=");
        xLong=vLong;
       // xLong=[]; 
        assertEquals(res.resIntegerSeqUnionWithLong1, xLong);
       if (gen)javafx.lang.FX.println(xLong);
       if (gen)javafx.lang.FX.println("; var resIntegerSeqUnionWithLong2:Long[]=");
        xLong=[vLong, vInteger];
       // xLong=[]; 
        assertEquals(res.resIntegerSeqUnionWithLong2, xLong);
       if (gen)javafx.lang.FX.println(xLong);
       if (gen)javafx.lang.FX.println("; var resIntegerSeqUnionWithLong3:Long[]=");
        xLong=[vInteger, vLong];
       // xLong=[]; 
        assertEquals(res.resIntegerSeqUnionWithLong3, xLong);
       if (gen)javafx.lang.FX.println(xLong);
       if (gen)javafx.lang.FX.print("; //");
    }    
    function testLongSeqCastToLong() { 
       if (gen) javafx.lang.FX.println("\nvar resLongSeqCastToLong1:Long[]=");
        xLong=vLong; 
       // xLong=[]; 
       if (gen)javafx.lang.FX.println(xLong);
       if (gen)javafx.lang.FX.print("; //");
        assertEquals(res.resLongSeqCastToLong1, xLong);
    }    

    function testLongSeqUnionWithLong() { 
       if (gen)javafx.lang.FX.println("\nvar resLongSeqUnionWithLong1:Long[]=");
        xLong=vLong;
       // xLong=[]; 
        assertEquals(res.resLongSeqUnionWithLong1, xLong);
       if (gen)javafx.lang.FX.println(xLong);
       if (gen)javafx.lang.FX.println("; var resLongSeqUnionWithLong2:Long[]=");
        xLong=[vLong, vLong];
       // xLong=[]; 
        assertEquals(res.resLongSeqUnionWithLong2, xLong);
       if (gen)javafx.lang.FX.println(xLong);
       if (gen)javafx.lang.FX.println("; var resLongSeqUnionWithLong3:Long[]=");
        xLong=[vLong, vLong];
       // xLong=[]; 
        assertEquals(res.resLongSeqUnionWithLong3, xLong);
       if (gen)javafx.lang.FX.println(xLong);
       if (gen)javafx.lang.FX.print("; //");
    }    
    function testFloatSeqCastToLong() { 
       if (gen) javafx.lang.FX.println("\nvar resFloatSeqCastToLong1:Long[]=");
        xLong=vFloat; 
       // xLong=[]; 
       if (gen)javafx.lang.FX.println(xLong);
       if (gen)javafx.lang.FX.print("; //");
        assertEquals(res.resFloatSeqCastToLong1, xLong);
    }    

    function testFloatSeqUnionWithLong() { 
       if (gen)javafx.lang.FX.println("\nvar resFloatSeqUnionWithLong1:Long[]=");
        xLong=vLong;
       // xLong=[]; 
        assertEquals(res.resFloatSeqUnionWithLong1, xLong);
       if (gen)javafx.lang.FX.println(xLong);
       if (gen)javafx.lang.FX.println("; var resFloatSeqUnionWithLong2:Long[]=");
        xLong=[vLong, vFloat];
       // xLong=[]; 
        assertEquals(res.resFloatSeqUnionWithLong2, xLong);
       if (gen)javafx.lang.FX.println(xLong);
       if (gen)javafx.lang.FX.println("; var resFloatSeqUnionWithLong3:Long[]=");
        xLong=[vFloat, vLong];
       // xLong=[]; 
        assertEquals(res.resFloatSeqUnionWithLong3, xLong);
       if (gen)javafx.lang.FX.println(xLong);
       if (gen)javafx.lang.FX.print("; //");
    }    
    function testDoubleSeqCastToLong() { 
       if (gen) javafx.lang.FX.println("\nvar resDoubleSeqCastToLong1:Long[]=");
        xLong=vDouble; 
       // xLong=[]; 
       if (gen)javafx.lang.FX.println(xLong);
       if (gen)javafx.lang.FX.print("; //");
        assertEquals(res.resDoubleSeqCastToLong1, xLong);
    }    

    function testDoubleSeqUnionWithLong() { 
       if (gen)javafx.lang.FX.println("\nvar resDoubleSeqUnionWithLong1:Long[]=");
        xLong=vLong;
       // xLong=[]; 
        assertEquals(res.resDoubleSeqUnionWithLong1, xLong);
       if (gen)javafx.lang.FX.println(xLong);
       if (gen)javafx.lang.FX.println("; var resDoubleSeqUnionWithLong2:Long[]=");
        xLong=[vLong, vDouble];
       // xLong=[]; 
        assertEquals(res.resDoubleSeqUnionWithLong2, xLong);
       if (gen)javafx.lang.FX.println(xLong);
       if (gen)javafx.lang.FX.println("; var resDoubleSeqUnionWithLong3:Long[]=");
        xLong=[vDouble, vLong];
       // xLong=[]; 
        assertEquals(res.resDoubleSeqUnionWithLong3, xLong);
       if (gen)javafx.lang.FX.println(xLong);
       if (gen)javafx.lang.FX.print("; //");
    }    
    function testNumberSeqCastToLong() { 
       if (gen) javafx.lang.FX.println("\nvar resNumberSeqCastToLong1:Long[]=");
        xLong=vNumber; 
       // xLong=[]; 
       if (gen)javafx.lang.FX.println(xLong);
       if (gen)javafx.lang.FX.print("; //");
        assertEquals(res.resNumberSeqCastToLong1, xLong);
    }    

    function testNumberSeqUnionWithLong() { 
       if (gen)javafx.lang.FX.println("\nvar resNumberSeqUnionWithLong1:Long[]=");
        xLong=vLong;
       // xLong=[]; 
        assertEquals(res.resNumberSeqUnionWithLong1, xLong);
       if (gen)javafx.lang.FX.println(xLong);
       if (gen)javafx.lang.FX.println("; var resNumberSeqUnionWithLong2:Long[]=");
        xLong=[vLong, vNumber];
       // xLong=[]; 
        assertEquals(res.resNumberSeqUnionWithLong2, xLong);
       if (gen)javafx.lang.FX.println(xLong);
       if (gen)javafx.lang.FX.println("; var resNumberSeqUnionWithLong3:Long[]=");
        xLong=[vNumber, vLong];
       // xLong=[]; 
        assertEquals(res.resNumberSeqUnionWithLong3, xLong);
       if (gen)javafx.lang.FX.println(xLong);
       if (gen)javafx.lang.FX.print("; //");
    }    
    var xFloat: Float[] = vFloat;  
    function testByteSeqCastToFloat() { 
       if (gen) javafx.lang.FX.println("\nvar resByteSeqCastToFloat1:Float[]=");
        xFloat=vByte; 
       // xFloat=[]; 
       if (gen)javafx.lang.FX.println(xFloat);
       if (gen)javafx.lang.FX.print("; //");
        assertEquals(res.resByteSeqCastToFloat1, xFloat);
    }    

    function testByteSeqUnionWithFloat() { 
       if (gen)javafx.lang.FX.println("\nvar resByteSeqUnionWithFloat1:Float[]=");
        xFloat=vFloat;
       // xFloat=[]; 
        assertEquals(res.resByteSeqUnionWithFloat1, xFloat);
       if (gen)javafx.lang.FX.println(xFloat);
       if (gen)javafx.lang.FX.println("; var resByteSeqUnionWithFloat2:Float[]=");
        xFloat=[vFloat, vByte];
       // xFloat=[]; 
        assertEquals(res.resByteSeqUnionWithFloat2, xFloat);
       if (gen)javafx.lang.FX.println(xFloat);
       if (gen)javafx.lang.FX.println("; var resByteSeqUnionWithFloat3:Float[]=");
        xFloat=[vByte, vFloat];
       // xFloat=[]; 
        assertEquals(res.resByteSeqUnionWithFloat3, xFloat);
       if (gen)javafx.lang.FX.println(xFloat);
       if (gen)javafx.lang.FX.print("; //");
    }    
    function testShortSeqCastToFloat() { 
       if (gen) javafx.lang.FX.println("\nvar resShortSeqCastToFloat1:Float[]=");
        xFloat=vShort; 
       // xFloat=[]; 
       if (gen)javafx.lang.FX.println(xFloat);
       if (gen)javafx.lang.FX.print("; //");
        assertEquals(res.resShortSeqCastToFloat1, xFloat);
    }    

    function testShortSeqUnionWithFloat() { 
       if (gen)javafx.lang.FX.println("\nvar resShortSeqUnionWithFloat1:Float[]=");
        xFloat=vFloat;
       // xFloat=[]; 
        assertEquals(res.resShortSeqUnionWithFloat1, xFloat);
       if (gen)javafx.lang.FX.println(xFloat);
       if (gen)javafx.lang.FX.println("; var resShortSeqUnionWithFloat2:Float[]=");
        xFloat=[vFloat, vShort];
       // xFloat=[]; 
        assertEquals(res.resShortSeqUnionWithFloat2, xFloat);
       if (gen)javafx.lang.FX.println(xFloat);
       if (gen)javafx.lang.FX.println("; var resShortSeqUnionWithFloat3:Float[]=");
        xFloat=[vShort, vFloat];
       // xFloat=[]; 
        assertEquals(res.resShortSeqUnionWithFloat3, xFloat);
       if (gen)javafx.lang.FX.println(xFloat);
       if (gen)javafx.lang.FX.print("; //");
    }    
    function testCharacterSeqCastToFloat() { 
       if (gen) javafx.lang.FX.println("\nvar resCharacterSeqCastToFloat1:Float[]=");
       //  xFloat=vCharacter; 
        xFloat=[]; 
       if (gen)javafx.lang.FX.println(xFloat);
       if (gen)javafx.lang.FX.print("; //");
       //  assertEquals(res.resCharacterSeqCastToFloat1, xFloat);
    }    

    function testCharacterSeqUnionWithFloat() { 
       if (gen)javafx.lang.FX.println("\nvar resCharacterSeqUnionWithFloat1:Float[]=");
       //  xFloat=vFloat;
        xFloat=[]; 
       //  assertEquals(res.resCharacterSeqUnionWithFloat1, xFloat);
       if (gen)javafx.lang.FX.println(xFloat);
       if (gen)javafx.lang.FX.println("; var resCharacterSeqUnionWithFloat2:Float[]=");
       //  xFloat=[vFloat, vCharacter];
        xFloat=[]; 
       //  assertEquals(res.resCharacterSeqUnionWithFloat2, xFloat);
       if (gen)javafx.lang.FX.println(xFloat);
       if (gen)javafx.lang.FX.println("; var resCharacterSeqUnionWithFloat3:Float[]=");
       //  xFloat=[vCharacter, vFloat];
        xFloat=[]; 
       //  assertEquals(res.resCharacterSeqUnionWithFloat3, xFloat);
       if (gen)javafx.lang.FX.println(xFloat);
       if (gen)javafx.lang.FX.print("; //");
    }    
    function testIntegerSeqCastToFloat() { 
       if (gen) javafx.lang.FX.println("\nvar resIntegerSeqCastToFloat1:Float[]=");
        xFloat=vInteger; 
       // xFloat=[]; 
       if (gen)javafx.lang.FX.println(xFloat);
       if (gen)javafx.lang.FX.print("; //");
        assertEquals(res.resIntegerSeqCastToFloat1, xFloat);
    }    

    function testIntegerSeqUnionWithFloat() { 
       if (gen)javafx.lang.FX.println("\nvar resIntegerSeqUnionWithFloat1:Float[]=");
        xFloat=vFloat;
       // xFloat=[]; 
        assertEquals(res.resIntegerSeqUnionWithFloat1, xFloat);
       if (gen)javafx.lang.FX.println(xFloat);
       if (gen)javafx.lang.FX.println("; var resIntegerSeqUnionWithFloat2:Float[]=");
        xFloat=[vFloat, vInteger];
       // xFloat=[]; 
        assertEquals(res.resIntegerSeqUnionWithFloat2, xFloat);
       if (gen)javafx.lang.FX.println(xFloat);
       if (gen)javafx.lang.FX.println("; var resIntegerSeqUnionWithFloat3:Float[]=");
        xFloat=[vInteger, vFloat];
       // xFloat=[]; 
        assertEquals(res.resIntegerSeqUnionWithFloat3, xFloat);
       if (gen)javafx.lang.FX.println(xFloat);
       if (gen)javafx.lang.FX.print("; //");
    }    
    function testLongSeqCastToFloat() { 
       if (gen) javafx.lang.FX.println("\nvar resLongSeqCastToFloat1:Float[]=");
        xFloat=vLong; 
       // xFloat=[]; 
       if (gen)javafx.lang.FX.println(xFloat);
       if (gen)javafx.lang.FX.print("; //");
        assertEquals(res.resLongSeqCastToFloat1, xFloat);
    }    

    function testLongSeqUnionWithFloat() { 
       if (gen)javafx.lang.FX.println("\nvar resLongSeqUnionWithFloat1:Float[]=");
        xFloat=vFloat;
       // xFloat=[]; 
        assertEquals(res.resLongSeqUnionWithFloat1, xFloat);
       if (gen)javafx.lang.FX.println(xFloat);
       if (gen)javafx.lang.FX.println("; var resLongSeqUnionWithFloat2:Float[]=");
        xFloat=[vFloat, vLong];
       // xFloat=[]; 
        assertEquals(res.resLongSeqUnionWithFloat2, xFloat);
       if (gen)javafx.lang.FX.println(xFloat);
       if (gen)javafx.lang.FX.println("; var resLongSeqUnionWithFloat3:Float[]=");
        xFloat=[vLong, vFloat];
       // xFloat=[]; 
        assertEquals(res.resLongSeqUnionWithFloat3, xFloat);
       if (gen)javafx.lang.FX.println(xFloat);
       if (gen)javafx.lang.FX.print("; //");
    }    
    function testFloatSeqCastToFloat() { 
       if (gen) javafx.lang.FX.println("\nvar resFloatSeqCastToFloat1:Float[]=");
        xFloat=vFloat; 
       // xFloat=[]; 
       if (gen)javafx.lang.FX.println(xFloat);
       if (gen)javafx.lang.FX.print("; //");
        assertEquals(res.resFloatSeqCastToFloat1, xFloat);
    }    

    function testFloatSeqUnionWithFloat() { 
       if (gen)javafx.lang.FX.println("\nvar resFloatSeqUnionWithFloat1:Float[]=");
        xFloat=vFloat;
       // xFloat=[]; 
        assertEquals(res.resFloatSeqUnionWithFloat1, xFloat);
       if (gen)javafx.lang.FX.println(xFloat);
       if (gen)javafx.lang.FX.println("; var resFloatSeqUnionWithFloat2:Float[]=");
        xFloat=[vFloat, vFloat];
       // xFloat=[]; 
        assertEquals(res.resFloatSeqUnionWithFloat2, xFloat);
       if (gen)javafx.lang.FX.println(xFloat);
       if (gen)javafx.lang.FX.println("; var resFloatSeqUnionWithFloat3:Float[]=");
        xFloat=[vFloat, vFloat];
       // xFloat=[]; 
        assertEquals(res.resFloatSeqUnionWithFloat3, xFloat);
       if (gen)javafx.lang.FX.println(xFloat);
       if (gen)javafx.lang.FX.print("; //");
    }    
    function testDoubleSeqCastToFloat() { 
       if (gen) javafx.lang.FX.println("\nvar resDoubleSeqCastToFloat1:Float[]=");
        xFloat=vDouble; 
       // xFloat=[]; 
       if (gen)javafx.lang.FX.println(xFloat);
       if (gen)javafx.lang.FX.print("; //");
        assertEquals(res.resDoubleSeqCastToFloat1, xFloat);
    }    

    function testDoubleSeqUnionWithFloat() { 
       if (gen)javafx.lang.FX.println("\nvar resDoubleSeqUnionWithFloat1:Float[]=");
        xFloat=vFloat;
       // xFloat=[]; 
        assertEquals(res.resDoubleSeqUnionWithFloat1, xFloat);
       if (gen)javafx.lang.FX.println(xFloat);
       if (gen)javafx.lang.FX.println("; var resDoubleSeqUnionWithFloat2:Float[]=");
        xFloat=[vFloat, vDouble];
       // xFloat=[]; 
        assertEquals(res.resDoubleSeqUnionWithFloat2, xFloat);
       if (gen)javafx.lang.FX.println(xFloat);
       if (gen)javafx.lang.FX.println("; var resDoubleSeqUnionWithFloat3:Float[]=");
        xFloat=[vDouble, vFloat];
       // xFloat=[]; 
        assertEquals(res.resDoubleSeqUnionWithFloat3, xFloat);
       if (gen)javafx.lang.FX.println(xFloat);
       if (gen)javafx.lang.FX.print("; //");
    }    
    function testNumberSeqCastToFloat() { 
       if (gen) javafx.lang.FX.println("\nvar resNumberSeqCastToFloat1:Float[]=");
        xFloat=vNumber; 
       // xFloat=[]; 
       if (gen)javafx.lang.FX.println(xFloat);
       if (gen)javafx.lang.FX.print("; //");
        assertEquals(res.resNumberSeqCastToFloat1, xFloat);
    }    

    function testNumberSeqUnionWithFloat() { 
       if (gen)javafx.lang.FX.println("\nvar resNumberSeqUnionWithFloat1:Float[]=");
        xFloat=vFloat;
       // xFloat=[]; 
        assertEquals(res.resNumberSeqUnionWithFloat1, xFloat);
       if (gen)javafx.lang.FX.println(xFloat);
       if (gen)javafx.lang.FX.println("; var resNumberSeqUnionWithFloat2:Float[]=");
        xFloat=[vFloat, vNumber];
       // xFloat=[]; 
        assertEquals(res.resNumberSeqUnionWithFloat2, xFloat);
       if (gen)javafx.lang.FX.println(xFloat);
       if (gen)javafx.lang.FX.println("; var resNumberSeqUnionWithFloat3:Float[]=");
        xFloat=[vNumber, vFloat];
       // xFloat=[]; 
        assertEquals(res.resNumberSeqUnionWithFloat3, xFloat);
       if (gen)javafx.lang.FX.println(xFloat);
       if (gen)javafx.lang.FX.print("; //");
    }    
    var xDouble: Double[] = vDouble;  
    function testByteSeqCastToDouble() { 
       if (gen) javafx.lang.FX.println("\nvar resByteSeqCastToDouble1:Double[]=");
        xDouble=vByte; 
       // xDouble=[]; 
       if (gen)javafx.lang.FX.println(xDouble);
       if (gen)javafx.lang.FX.print("; //");
        assertEquals(res.resByteSeqCastToDouble1, xDouble);
    }    

    function testByteSeqUnionWithDouble() { 
       if (gen)javafx.lang.FX.println("\nvar resByteSeqUnionWithDouble1:Double[]=");
        xDouble=vDouble;
       // xDouble=[]; 
        assertEquals(res.resByteSeqUnionWithDouble1, xDouble);
       if (gen)javafx.lang.FX.println(xDouble);
       if (gen)javafx.lang.FX.println("; var resByteSeqUnionWithDouble2:Double[]=");
        xDouble=[vDouble, vByte];
       // xDouble=[]; 
        assertEquals(res.resByteSeqUnionWithDouble2, xDouble);
       if (gen)javafx.lang.FX.println(xDouble);
       if (gen)javafx.lang.FX.println("; var resByteSeqUnionWithDouble3:Double[]=");
        xDouble=[vByte, vDouble];
       // xDouble=[]; 
        assertEquals(res.resByteSeqUnionWithDouble3, xDouble);
       if (gen)javafx.lang.FX.println(xDouble);
       if (gen)javafx.lang.FX.print("; //");
    }    
    function testShortSeqCastToDouble() { 
       if (gen) javafx.lang.FX.println("\nvar resShortSeqCastToDouble1:Double[]=");
        xDouble=vShort; 
       // xDouble=[]; 
       if (gen)javafx.lang.FX.println(xDouble);
       if (gen)javafx.lang.FX.print("; //");
        assertEquals(res.resShortSeqCastToDouble1, xDouble);
    }    

    function testShortSeqUnionWithDouble() { 
       if (gen)javafx.lang.FX.println("\nvar resShortSeqUnionWithDouble1:Double[]=");
        xDouble=vDouble;
       // xDouble=[]; 
        assertEquals(res.resShortSeqUnionWithDouble1, xDouble);
       if (gen)javafx.lang.FX.println(xDouble);
       if (gen)javafx.lang.FX.println("; var resShortSeqUnionWithDouble2:Double[]=");
        xDouble=[vDouble, vShort];
       // xDouble=[]; 
        assertEquals(res.resShortSeqUnionWithDouble2, xDouble);
       if (gen)javafx.lang.FX.println(xDouble);
       if (gen)javafx.lang.FX.println("; var resShortSeqUnionWithDouble3:Double[]=");
        xDouble=[vShort, vDouble];
       // xDouble=[]; 
        assertEquals(res.resShortSeqUnionWithDouble3, xDouble);
       if (gen)javafx.lang.FX.println(xDouble);
       if (gen)javafx.lang.FX.print("; //");
    }    
    function testCharacterSeqCastToDouble() { 
       if (gen) javafx.lang.FX.println("\nvar resCharacterSeqCastToDouble1:Double[]=");
       //  xDouble=vCharacter; 
        xDouble=[]; 
       if (gen)javafx.lang.FX.println(xDouble);
       if (gen)javafx.lang.FX.print("; //");
       //  assertEquals(res.resCharacterSeqCastToDouble1, xDouble);
    }    

    function testCharacterSeqUnionWithDouble() { 
       if (gen)javafx.lang.FX.println("\nvar resCharacterSeqUnionWithDouble1:Double[]=");
       //  xDouble=vDouble;
        xDouble=[]; 
       //  assertEquals(res.resCharacterSeqUnionWithDouble1, xDouble);
       if (gen)javafx.lang.FX.println(xDouble);
       if (gen)javafx.lang.FX.println("; var resCharacterSeqUnionWithDouble2:Double[]=");
       //  xDouble=[vDouble, vCharacter];
        xDouble=[]; 
       //  assertEquals(res.resCharacterSeqUnionWithDouble2, xDouble);
       if (gen)javafx.lang.FX.println(xDouble);
       if (gen)javafx.lang.FX.println("; var resCharacterSeqUnionWithDouble3:Double[]=");
       //  xDouble=[vCharacter,  vDouble];
        xDouble=[]; 
       //  assertEquals(res.resCharacterSeqUnionWithDouble3, xDouble);
       if (gen)javafx.lang.FX.println(xDouble);
       if (gen)javafx.lang.FX.print("; //");
    }    
    function testIntegerSeqCastToDouble() { 
       if (gen) javafx.lang.FX.println("\nvar resIntegerSeqCastToDouble1:Double[]=");
        xDouble=vInteger; 
       // xDouble=[]; 
       if (gen)javafx.lang.FX.println(xDouble);
       if (gen)javafx.lang.FX.print("; //");
        assertEquals(res.resIntegerSeqCastToDouble1, xDouble);
    }    

    function testIntegerSeqUnionWithDouble() { 
       if (gen)javafx.lang.FX.println("\nvar resIntegerSeqUnionWithDouble1:Double[]=");
        xDouble=vDouble;
       // xDouble=[]; 
        assertEquals(res.resIntegerSeqUnionWithDouble1, xDouble);
       if (gen)javafx.lang.FX.println(xDouble);
       if (gen)javafx.lang.FX.println("; var resIntegerSeqUnionWithDouble2:Double[]=");
        xDouble=[vDouble, vInteger];
       // xDouble=[]; 
        assertEquals(res.resIntegerSeqUnionWithDouble2, xDouble);
       if (gen)javafx.lang.FX.println(xDouble);
       if (gen)javafx.lang.FX.println("; var resIntegerSeqUnionWithDouble3:Double[]=");
        xDouble=[vInteger, vDouble];
       // xDouble=[]; 
        assertEquals(res.resIntegerSeqUnionWithDouble3, xDouble);
       if (gen)javafx.lang.FX.println(xDouble);
       if (gen)javafx.lang.FX.print("; //");
    }    
    function testLongSeqCastToDouble() { 
       if (gen) javafx.lang.FX.println("\nvar resLongSeqCastToDouble1:Double[]=");
        xDouble=vLong; 
       // xDouble=[]; 
       if (gen)javafx.lang.FX.println(xDouble);
       if (gen)javafx.lang.FX.print("; //");
        assertEquals(res.resLongSeqCastToDouble1, xDouble);
    }    

    function testLongSeqUnionWithDouble() { 
       if (gen)javafx.lang.FX.println("\nvar resLongSeqUnionWithDouble1:Double[]=");
        xDouble=vDouble;
       // xDouble=[]; 
        assertEquals(res.resLongSeqUnionWithDouble1, xDouble);
       if (gen)javafx.lang.FX.println(xDouble);
       if (gen)javafx.lang.FX.println("; var resLongSeqUnionWithDouble2:Double[]=");
        xDouble=[vDouble, vLong];
       // xDouble=[]; 
        assertEquals(res.resLongSeqUnionWithDouble2, xDouble);
       if (gen)javafx.lang.FX.println(xDouble);
       if (gen)javafx.lang.FX.println("; var resLongSeqUnionWithDouble3:Double[]=");
        xDouble=[vLong,vDouble];
       // xDouble=[]; 
        assertEquals(res.resLongSeqUnionWithDouble3, xDouble);
       if (gen)javafx.lang.FX.println(xDouble);
       if (gen)javafx.lang.FX.print("; //");
    }    
    function testFloatSeqCastToDouble() { 
       if (gen) javafx.lang.FX.println("\nvar resFloatSeqCastToDouble1:Double[]=");
        xDouble=vFloat; 
       // xDouble=[]; 
       if (gen)javafx.lang.FX.println(xDouble);
       if (gen)javafx.lang.FX.print("; //");
        assertEquals(res.resFloatSeqCastToDouble1, xDouble);
    }    

    function testFloatSeqUnionWithDouble() { 
       if (gen)javafx.lang.FX.println("\nvar resFloatSeqUnionWithDouble1:Double[]=");
        xDouble=vDouble;
       // xDouble=[]; 
        assertEquals(res.resFloatSeqUnionWithDouble1, xDouble);
       if (gen)javafx.lang.FX.println(xDouble);
       if (gen)javafx.lang.FX.println("; var resFloatSeqUnionWithDouble2:Double[]=");
        xDouble=[vDouble, vFloat];
       // xDouble=[]; 
        assertEquals(res.resFloatSeqUnionWithDouble2, xDouble);
       if (gen)javafx.lang.FX.println(xDouble);
       if (gen)javafx.lang.FX.println("; var resFloatSeqUnionWithDouble3:Double[]=");
        xDouble=[vFloat, vDouble];
       // xDouble=[]; 
        assertEquals(res.resFloatSeqUnionWithDouble3, xDouble);
       if (gen)javafx.lang.FX.println(xDouble);
       if (gen)javafx.lang.FX.print("; //");
    }    
    function testDoubleSeqCastToDouble() { 
       if (gen) javafx.lang.FX.println("\nvar resDoubleSeqCastToDouble1:Double[]=");
        xDouble=vDouble; 
       // xDouble=[]; 
       if (gen)javafx.lang.FX.println(xDouble);
       if (gen)javafx.lang.FX.print("; //");
        assertEquals(res.resDoubleSeqCastToDouble1, xDouble);
    }    

    function testDoubleSeqUnionWithDouble() { 
       if (gen)javafx.lang.FX.println("\nvar resDoubleSeqUnionWithDouble1:Double[]=");
        xDouble=vDouble;
       // xDouble=[]; 
        assertEquals(res.resDoubleSeqUnionWithDouble1, xDouble);
       if (gen)javafx.lang.FX.println(xDouble);
       if (gen)javafx.lang.FX.println("; var resDoubleSeqUnionWithDouble2:Double[]=");
        xDouble=[vDouble, vDouble];
       // xDouble=[]; 
        assertEquals(res.resDoubleSeqUnionWithDouble2, xDouble);
       if (gen)javafx.lang.FX.println(xDouble);
       if (gen)javafx.lang.FX.println("; var resDoubleSeqUnionWithDouble3:Double[]=");
        xDouble=[vDouble, vDouble];
       // xDouble=[]; 
        assertEquals(res.resDoubleSeqUnionWithDouble3, xDouble);
       if (gen)javafx.lang.FX.println(xDouble);
       if (gen)javafx.lang.FX.print("; //");
    }    
    function testNumberSeqCastToDouble() { 
       if (gen) javafx.lang.FX.println("\nvar resNumberSeqCastToDouble1:Double[]=");
        xDouble=vNumber; 
       // xDouble=[]; 
       if (gen)javafx.lang.FX.println(xDouble);
       if (gen)javafx.lang.FX.print("; //");
        assertEquals(res.resNumberSeqCastToDouble1, xDouble);
    }    

    function testNumberSeqUnionWithDouble() { 
       if (gen)javafx.lang.FX.println("\nvar resNumberSeqUnionWithDouble1:Double[]=");
        xDouble=vDouble;
       // xDouble=[]; 
        assertEquals(res.resNumberSeqUnionWithDouble1, xDouble);
       if (gen)javafx.lang.FX.println(xDouble);
       if (gen)javafx.lang.FX.println("; var resNumberSeqUnionWithDouble2:Double[]=");
        xDouble=[vDouble, vNumber];
       // xDouble=[]; 
        assertEquals(res.resNumberSeqUnionWithDouble2, xDouble);
       if (gen)javafx.lang.FX.println(xDouble);
       if (gen)javafx.lang.FX.println("; var resNumberSeqUnionWithDouble3:Double[]=");
        xDouble=[vNumber, vDouble];
       // xDouble=[]; 
        assertEquals(res.resNumberSeqUnionWithDouble3, xDouble);
       if (gen)javafx.lang.FX.println(xDouble);
       if (gen)javafx.lang.FX.print("; //");
    }    
    var xNumber: Number[] = vNumber;  
    function testByteSeqCastToNumber() { 
       if (gen) javafx.lang.FX.println("\nvar resByteSeqCastToNumber1:Number[]=");
        xNumber=vByte; 
       // xNumber=[]; 
       if (gen)javafx.lang.FX.println(xNumber);
       if (gen)javafx.lang.FX.print("; //");
        assertEquals(res.resByteSeqCastToNumber1, xNumber);
    }    

    function testByteSeqUnionWithNumber() { 
       if (gen)javafx.lang.FX.println("\nvar resByteSeqUnionWithNumber1:Number[]=");
        xNumber=vNumber;
       // xNumber=[]; 
        assertEquals(res.resByteSeqUnionWithNumber1, xNumber);
       if (gen)javafx.lang.FX.println(xNumber);
       if (gen)javafx.lang.FX.println("; var resByteSeqUnionWithNumber2:Number[]=");
        xNumber=[vNumber, vByte];
       // xNumber=[]; 
        assertEquals(res.resByteSeqUnionWithNumber2, xNumber);
       if (gen)javafx.lang.FX.println(xNumber);
       if (gen)javafx.lang.FX.println("; var resByteSeqUnionWithNumber3:Number[]=");
        xNumber=[vByte, vNumber];
       // xNumber=[]; 
        assertEquals(res.resByteSeqUnionWithNumber3, xNumber);
       if (gen)javafx.lang.FX.println(xNumber);
       if (gen)javafx.lang.FX.print("; //");
    }    
    function testShortSeqCastToNumber() { 
       if (gen) javafx.lang.FX.println("\nvar resShortSeqCastToNumber1:Number[]=");
        xNumber=vShort; 
       // xNumber=[]; 
       if (gen)javafx.lang.FX.println(xNumber);
       if (gen)javafx.lang.FX.print("; //");
        assertEquals(res.resShortSeqCastToNumber1, xNumber);
    }    

    function testShortSeqUnionWithNumber() { 
       if (gen)javafx.lang.FX.println("\nvar resShortSeqUnionWithNumber1:Number[]=");
        xNumber=vNumber;
       // xNumber=[]; 
        assertEquals(res.resShortSeqUnionWithNumber1, xNumber);
       if (gen)javafx.lang.FX.println(xNumber);
       if (gen)javafx.lang.FX.println("; var resShortSeqUnionWithNumber2:Number[]=");
        xNumber=[vNumber, vShort];
       // xNumber=[]; 
        assertEquals(res.resShortSeqUnionWithNumber2, xNumber);
       if (gen)javafx.lang.FX.println(xNumber);
       if (gen)javafx.lang.FX.println("; var resShortSeqUnionWithNumber3:Number[]=");
        xNumber=[vShort, vNumber];
       // xNumber=[]; 
        assertEquals(res.resShortSeqUnionWithNumber3, xNumber);
       if (gen)javafx.lang.FX.println(xNumber);
       if (gen)javafx.lang.FX.print("; //");
    }    
    function testCharacterSeqCastToNumber() { 
       if (gen) javafx.lang.FX.println("\nvar resCharacterSeqCastToNumber1:Number[]=");
       //  xNumber=vCharacter; 
        xNumber=[]; 
       if (gen)javafx.lang.FX.println(xNumber);
       if (gen)javafx.lang.FX.print("; //");
       //  assertEquals(res.resCharacterSeqCastToNumber1, xNumber);
    }    

    function testCharacterSeqUnionWithNumber() { 
       if (gen)javafx.lang.FX.println("\nvar resCharacterSeqUnionWithNumber1:Number[]=");
       //  xNumber=vNumber;
        xNumber=[]; 
       //  assertEquals(res.resCharacterSeqUnionWithNumber1, xNumber);
       if (gen)javafx.lang.FX.println(xNumber);
       if (gen)javafx.lang.FX.println("; var resCharacterSeqUnionWithNumber2:Number[]=");
       //  xNumber=[vNumber, vCharacter];
        xNumber=[]; 
       //  assertEquals(res.resCharacterSeqUnionWithNumber2, xNumber);
       if (gen)javafx.lang.FX.println(xNumber);
       if (gen)javafx.lang.FX.println("; var resCharacterSeqUnionWithNumber3:Number[]=");
       //  xNumber=[vCharacter,  vNumber];
        xNumber=[]; 
       //  assertEquals(res.resCharacterSeqUnionWithNumber3, xNumber);
       if (gen)javafx.lang.FX.println(xNumber);
       if (gen)javafx.lang.FX.print("; //");
    }    
    function testIntegerSeqCastToNumber() { 
       if (gen) javafx.lang.FX.println("\nvar resIntegerSeqCastToNumber1:Number[]=");
        xNumber=vInteger; 
       // xNumber=[]; 
       if (gen)javafx.lang.FX.println(xNumber);
       if (gen)javafx.lang.FX.print("; //");
        assertEquals(res.resIntegerSeqCastToNumber1, xNumber);
    }    

    function testIntegerSeqUnionWithNumber() { 
       if (gen)javafx.lang.FX.println("\nvar resIntegerSeqUnionWithNumber1:Number[]=");
        xNumber=vNumber;
       // xNumber=[]; 
        assertEquals(res.resIntegerSeqUnionWithNumber1, xNumber);
       if (gen)javafx.lang.FX.println(xNumber);
       if (gen)javafx.lang.FX.println("; var resIntegerSeqUnionWithNumber2:Number[]=");
        xNumber=[vNumber, vInteger];
       // xNumber=[]; 
        assertEquals(res.resIntegerSeqUnionWithNumber2, xNumber);
       if (gen)javafx.lang.FX.println(xNumber);
       if (gen)javafx.lang.FX.println("; var resIntegerSeqUnionWithNumber3:Number[]=");
        xNumber=[vInteger, vNumber];
       // xNumber=[]; 
        assertEquals(res.resIntegerSeqUnionWithNumber3, xNumber);
       if (gen)javafx.lang.FX.println(xNumber);
       if (gen)javafx.lang.FX.print("; //");
    }    
    function testLongSeqCastToNumber() { 
       if (gen) javafx.lang.FX.println("\nvar resLongSeqCastToNumber1:Number[]=");
        xNumber=vLong; 
       // xNumber=[]; 
       if (gen)javafx.lang.FX.println(xNumber);
       if (gen)javafx.lang.FX.print("; //");
        assertEquals(res.resLongSeqCastToNumber1, xNumber);
    }    

    function testLongSeqUnionWithNumber() { 
       if (gen)javafx.lang.FX.println("\nvar resLongSeqUnionWithNumber1:Number[]=");
        xNumber=vNumber;
       // xNumber=[]; 
        assertEquals(res.resLongSeqUnionWithNumber1, xNumber);
       if (gen)javafx.lang.FX.println(xNumber);
       if (gen)javafx.lang.FX.println("; var resLongSeqUnionWithNumber2:Number[]=");
        xNumber=[vNumber, vLong];
       // xNumber=[]; 
        assertEquals(res.resLongSeqUnionWithNumber2, xNumber);
       if (gen)javafx.lang.FX.println(xNumber);
       if (gen)javafx.lang.FX.println("; var resLongSeqUnionWithNumber3:Number[]=");
        xNumber=[vLong,vNumber];
       // xNumber=[]; 
        assertEquals(res.resLongSeqUnionWithNumber3, xNumber);
       if (gen)javafx.lang.FX.println(xNumber);
       if (gen)javafx.lang.FX.print("; //");
    }    
    function testFloatSeqCastToNumber() { 
       if (gen) javafx.lang.FX.println("\nvar resFloatSeqCastToNumber1:Number[]=");
        xNumber=vFloat; 
       // xNumber=[]; 
       if (gen)javafx.lang.FX.println(xNumber);
       if (gen)javafx.lang.FX.print("; //");
        assertEquals(res.resFloatSeqCastToNumber1, xNumber);
    }    

    function testFloatSeqUnionWithNumber() { 
       if (gen)javafx.lang.FX.println("\nvar resFloatSeqUnionWithNumber1:Number[]=");
        xNumber=vNumber;
       // xNumber=[]; 
        assertEquals(res.resFloatSeqUnionWithNumber1, xNumber);
       if (gen)javafx.lang.FX.println(xNumber);
       if (gen)javafx.lang.FX.println("; var resFloatSeqUnionWithNumber2:Number[]=");
        xNumber=[vNumber, vFloat];
       // xNumber=[]; 
        assertEquals(res.resFloatSeqUnionWithNumber2, xNumber);
       if (gen)javafx.lang.FX.println(xNumber);
       if (gen)javafx.lang.FX.println("; var resFloatSeqUnionWithNumber3:Number[]=");
        xNumber=[vFloat, vNumber];
       // xNumber=[]; 
        assertEquals(res.resFloatSeqUnionWithNumber3, xNumber);
       if (gen)javafx.lang.FX.println(xNumber);
       if (gen)javafx.lang.FX.print("; //");
    }    
    function testDoubleSeqCastToNumber() { 
       if (gen) javafx.lang.FX.println("\nvar resDoubleSeqCastToNumber1:Number[]=");
        xNumber=vDouble; 
       // xNumber=[]; 
       if (gen)javafx.lang.FX.println(xNumber);
       if (gen)javafx.lang.FX.print("; //");
        assertEquals(res.resDoubleSeqCastToNumber1, xNumber);
    }    

    function testDoubleSeqUnionWithNumber() { 
       if (gen)javafx.lang.FX.println("\nvar resDoubleSeqUnionWithNumber1:Number[]=");
        xNumber=vNumber;
       // xNumber=[]; 
        assertEquals(res.resDoubleSeqUnionWithNumber1, xNumber);
       if (gen)javafx.lang.FX.println(xNumber);
       if (gen)javafx.lang.FX.println("; var resDoubleSeqUnionWithNumber2:Number[]=");
        xNumber=[vNumber, vDouble];
       // xNumber=[]; 
        assertEquals(res.resDoubleSeqUnionWithNumber2, xNumber);
       if (gen)javafx.lang.FX.println(xNumber);
       if (gen)javafx.lang.FX.println("; var resDoubleSeqUnionWithNumber3:Number[]=");
        xNumber=[vDouble, vNumber];
       // xNumber=[]; 
        assertEquals(res.resDoubleSeqUnionWithNumber3, xNumber);
       if (gen)javafx.lang.FX.println(xNumber);
       if (gen)javafx.lang.FX.print("; //");
    }    
    function testNumberSeqCastToNumber() { 
       if (gen) javafx.lang.FX.println("\nvar resNumberSeqCastToNumber1:Number[]=");
        xNumber=vNumber; 
       // xNumber=[]; 
       if (gen)javafx.lang.FX.println(xNumber);
       if (gen)javafx.lang.FX.print("; //");
        assertEquals(res.resNumberSeqCastToNumber1, xNumber);
    }    

    function testNumberSeqUnionWithNumber() { 
       if (gen)javafx.lang.FX.println("\nvar resNumberSeqUnionWithNumber1:Number[]=");
        xNumber=vNumber;
       // xNumber=[]; 
        assertEquals(res.resNumberSeqUnionWithNumber1, xNumber);
       if (gen)javafx.lang.FX.println(xNumber);
       if (gen)javafx.lang.FX.println("; var resNumberSeqUnionWithNumber2:Number[]=");
        xNumber=[vNumber, vNumber];
       // xNumber=[]; 
        assertEquals(res.resNumberSeqUnionWithNumber2, xNumber);
       if (gen)javafx.lang.FX.println(xNumber);
       if (gen)javafx.lang.FX.println("; var resNumberSeqUnionWithNumber3:Number[]=");
        xNumber=[vNumber, vNumber];
       // xNumber=[]; 
        assertEquals(res.resNumberSeqUnionWithNumber3, xNumber);
       if (gen)javafx.lang.FX.println(xNumber);
       if (gen)javafx.lang.FX.print("; //");
    }    

var resByteSeqCastToByte1:Byte[]=
[ 1, 2, 3, 4, 5, 6, 7 ]
; //.
var resByteSeqUnionWithByte1:Byte[]=
[ 1, 2, 3, 4, 5, 6, 7 ]
; var resByteSeqUnionWithByte2:Byte[]=
[ 1, 2, 3, 4, 5, 6, 7, 1, 2, 3, 4, 5, 6, 7 ]
; var resByteSeqUnionWithByte3:Byte[]=
[ 1, 2, 3, 4, 5, 6, 7, 1, 2, 3, 4, 5, 6, 7 ]
; //.
var resShortSeqCastToByte1:Byte[]=
[ -128, -127, -126, -125, -124 ]
; //.
var resShortSeqUnionWithByte1:Byte[]=
[ 1, 2, 3, 4, 5, 6, 7 ]
; var resShortSeqUnionWithByte2:Byte[]=
[ 1, 2, 3, 4, 5, 6, 7, -128, -127, -126, -125, -124 ]
; var resShortSeqUnionWithByte3:Byte[]=
[ -128, -127, -126, -125, -124, 1, 2, 3, 4, 5, 6, 7 ]
; //.
var resCharacterSeqCastToByte1:Byte[]=
[ ]
; //.
var resCharacterSeqUnionWithByte1:Byte[]=
[ ]
; var resCharacterSeqUnionWithByte2:Byte[]=
[ ]
; var resCharacterSeqUnionWithByte3:Byte[]=
[ ]
; //.
var resIntegerSeqCastToByte1:Byte[]=
[ 0, 1, 2, 3 ]
; //.
var resIntegerSeqUnionWithByte1:Byte[]=
[ 1, 2, 3, 4, 5, 6, 7 ]
; var resIntegerSeqUnionWithByte2:Byte[]=
[ 1, 2, 3, 4, 5, 6, 7, 0, 1, 2, 3 ]
; var resIntegerSeqUnionWithByte3:Byte[]=
[ 0, 1, 2, 3, 1, 2, 3, 4, 5, 6, 7 ]
; //.
var resLongSeqCastToByte1:Byte[]=
[ 64, 65, 66, 67 ]
; //.
var resLongSeqUnionWithByte1:Byte[]=
[ 1, 2, 3, 4, 5, 6, 7 ]
; var resLongSeqUnionWithByte2:Byte[]=
[ 1, 2, 3, 4, 5, 6, 7, 64, 65, 66, 67 ]
; var resLongSeqUnionWithByte3:Byte[]=
[ 64, 65, 66, 67, 1, 2, 3, 4, 5, 6, 7 ]
; //.
var resFloatSeqCastToByte1:Byte[]=
[ 5, 6, 7 ]
; //.
var resFloatSeqUnionWithByte1:Byte[]=
[ 1, 2, 3, 4, 5, 6, 7 ]
; var resFloatSeqUnionWithByte2:Byte[]=
[ 1, 2, 3, 4, 5, 6, 7, 5, 6, 7 ]
; var resFloatSeqUnionWithByte3:Byte[]=
[ 5, 6, 7, 1, 2, 3, 4, 5, 6, 7 ]
; //.
var resDoubleSeqCastToByte1:Byte[]=
[ 6, 7 ]
; //.
var resDoubleSeqUnionWithByte1:Byte[]=
[ 1, 2, 3, 4, 5, 6, 7 ]
; var resDoubleSeqUnionWithByte2:Byte[]=
[ 1, 2, 3, 4, 5, 6, 7, 6, 7 ]
; var resDoubleSeqUnionWithByte3:Byte[]=
[ 6, 7, 1, 2, 3, 4, 5, 6, 7 ]
; //.
var resNumberSeqCastToByte1:Byte[]=
[ 48, 49, 50, 51, 52, 53 ]
; //.
var resNumberSeqUnionWithByte1:Byte[]=
[ 1, 2, 3, 4, 5, 6, 7 ]
; var resNumberSeqUnionWithByte2:Byte[]=
[ 1, 2, 3, 4, 5, 6, 7, 48, 49, 50, 51, 52, 53 ]
; var resNumberSeqUnionWithByte3:Byte[]=
[ 48, 49, 50, 51, 52, 53, 1, 2, 3, 4, 5, 6, 7 ]
; //.
var resByteSeqCastToShort1:Short[]=
[ 1, 2, 3, 4, 5, 6, 7 ]
; //.
var resByteSeqUnionWithShort1:Short[]=
[ 16000, 16001, 16002, 16003, 16004 ]
; var resByteSeqUnionWithShort2:Short[]=
[ 16000, 16001, 16002, 16003, 16004, 1, 2, 3, 4, 5, 6, 7 ]
; var resByteSeqUnionWithShort3:Short[]=
[ 1, 2, 3, 4, 5, 6, 7, 16000, 16001, 16002, 16003, 16004 ]
; //.
var resShortSeqCastToShort1:Short[]=
[ 16000, 16001, 16002, 16003, 16004 ]
; //.
var resShortSeqUnionWithShort1:Short[]=
[ 16000, 16001, 16002, 16003, 16004 ]
; var resShortSeqUnionWithShort2:Short[]=
[ 16000, 16001, 16002, 16003, 16004, 16000, 16001, 16002, 16003, 16004 ]
; var resShortSeqUnionWithShort3:Short[]=
[ 16000, 16001, 16002, 16003, 16004, 16000, 16001, 16002, 16003, 16004 ]
; //.
var resCharacterSeqCastToShort1:Short[]=
[ ]
; //.
var resCharacterSeqUnionWithShort1:Short[]=
[ ]
; var resCharacterSeqUnionWithShort2:Short[]=
[ ]
; var resCharacterSeqUnionWithShort3:Short[]=
[ ]
; //.
var resIntegerSeqCastToShort1:Short[]=
[ 256, 257, 258, 259 ]
; //.
var resIntegerSeqUnionWithShort1:Short[]=
[ 16000, 16001, 16002, 16003, 16004 ]
; var resIntegerSeqUnionWithShort2:Short[]=
[ 16000, 16001, 16002, 16003, 16004, 256, 257, 258, 259 ]
; var resIntegerSeqUnionWithShort3:Short[]=
[ 256, 257, 258, 259, 16000, 16001, 16002, 16003, 16004 ]
; //.
var resLongSeqCastToShort1:Short[]=
[ 16960, 16961, 16962, 16963 ]
; //.
var resLongSeqUnionWithShort1:Short[]=
[ 16000, 16001, 16002, 16003, 16004 ]
; var resLongSeqUnionWithShort2:Short[]=
[ 16000, 16001, 16002, 16003, 16004, 16960, 16961, 16962, 16963 ]
; var resLongSeqUnionWithShort3:Short[]=
[ 16960, 16961, 16962, 16963, 16000, 16001, 16002, 16003, 16004 ]
; //.
var resFloatSeqCastToShort1:Short[]=
[ 5, 6, 7 ]
; //.
var resFloatSeqUnionWithShort1:Short[]=
[ 16000, 16001, 16002, 16003, 16004 ]
; var resFloatSeqUnionWithShort2:Short[]=
[ 16000, 16001, 16002, 16003, 16004, 5, 6, 7 ]
; var resFloatSeqUnionWithShort3:Short[]=
[ 5, 6, 7, 16000, 16001, 16002, 16003, 16004 ]
; //.
var resDoubleSeqCastToShort1:Short[]=
[ 6, 7 ]
; //.
var resDoubleSeqUnionWithShort1:Short[]=
[ 16000, 16001, 16002, 16003, 16004 ]
; var resDoubleSeqUnionWithShort2:Short[]=
[ 16000, 16001, 16002, 16003, 16004, 6, 7 ]
; var resDoubleSeqUnionWithShort3:Short[]=
[ 6, 7, 16000, 16001, 16002, 16003, 16004 ]
; //.
var resNumberSeqCastToShort1:Short[]=
[ -30416, -30415, -30414, -30413, -30412, -30411 ]
; //.
var resNumberSeqUnionWithShort1:Short[]=
[ 16000, 16001, 16002, 16003, 16004 ]
; var resNumberSeqUnionWithShort2:Short[]=
[ 16000, 16001, 16002, 16003, 16004, -30416, -30415, -30414, -30413, -30412, -30411 ]
; var resNumberSeqUnionWithShort3:Short[]=
[ -30416, -30415, -30414, -30413, -30412, -30411, 16000, 16001, 16002, 16003, 16004 ]
; //.
var resByteSeqCastToCharacter1:Character[]=
[ ]
; //.
var resByteSeqUnionWithCharacter1:Character[]=
[ ]
; var resByteSeqUnionWithCharacter2:Character[]=
[ ]
; var resByteSeqUnionWithCharacter3:Character[]=
[ ]
; //.
var resShortSeqCastToCharacter1:Character[]=
[ ]
; //.
var resShortSeqUnionWithCharacter1:Character[]=
[ ]
; var resShortSeqUnionWithCharacter2:Character[]=
[ ]
; var resShortSeqUnionWithCharacter3:Character[]=
[ ]
; //.
var resCharacterSeqCastToCharacter1:Character[]=
[ ]
; //.
var resCharacterSeqUnionWithCharacter1:Character[]=
[ ]
; var resCharacterSeqUnionWithCharacter2:Character[]=
[ ]
; var resCharacterSeqUnionWithCharacter3:Character[]=
[ ]
; //.
var resIntegerSeqCastToCharacter1:Character[]=
[ ]
; //.
var resIntegerSeqUnionWithCharacter1:Character[]=
[ ]
; var resIntegerSeqUnionWithCharacter2:Character[]=
[ ]
; var resIntegerSeqUnionWithCharacter3:Character[]=
[ ]
; //.
var resLongSeqCastToCharacter1:Character[]=
[ ]
; //.
var resLongSeqUnionWithCharacter1:Character[]=
[ ]
; var resLongSeqUnionWithCharacter2:Character[]=
[ ]
; var resLongSeqUnionWithCharacter3:Character[]=
[ ]
; //.
var resFloatSeqCastToCharacter1:Character[]=
[ ]
; //.
var resFloatSeqUnionWithCharacter1:Character[]=
[ ]
; var resFloatSeqUnionWithCharacter2:Character[]=
[ ]
; var resFloatSeqUnionWithCharacter3:Character[]=
[ ]
; //.
var resDoubleSeqCastToCharacter1:Character[]=
[ ]
; //.
var resDoubleSeqUnionWithCharacter1:Character[]=
[ ]
; var resDoubleSeqUnionWithCharacter2:Character[]=
[ ]
; var resDoubleSeqUnionWithCharacter3:Character[]=
[ ]
; //.
var resNumberSeqCastToCharacter1:Character[]=
[ ]
; //.
var resNumberSeqUnionWithCharacter1:Character[]=
[ ]
; var resNumberSeqUnionWithCharacter2:Character[]=
[ ]
; var resNumberSeqUnionWithCharacter3:Character[]=
[ ]
; //.
var resByteSeqCastToInteger1:Integer[]=
[ 1, 2, 3, 4, 5, 6, 7 ]
; //.
var resByteSeqUnionWithInteger1:Integer[]=
[ 256, 257, 258, 259 ]
; var resByteSeqUnionWithInteger2:Integer[]=
[ 256, 257, 258, 259, 1, 2, 3, 4, 5, 6, 7 ]
; var resByteSeqUnionWithInteger3:Integer[]=
[ 1, 2, 3, 4, 5, 6, 7, 256, 257, 258, 259 ]
; //.
var resShortSeqCastToInteger1:Integer[]=
[ 16000, 16001, 16002, 16003, 16004 ]
; //.
var resShortSeqUnionWithInteger1:Integer[]=
[ 256, 257, 258, 259 ]
; var resShortSeqUnionWithInteger2:Integer[]=
[ 256, 257, 258, 259, 16000, 16001, 16002, 16003, 16004 ]
; var resShortSeqUnionWithInteger3:Integer[]=
[ 16000, 16001, 16002, 16003, 16004, 256, 257, 258, 259 ]
; //.
var resCharacterSeqCastToInteger1:Integer[]=
[ ]
; //.
var resCharacterSeqUnionWithInteger1:Integer[]=
[ ]
; var resCharacterSeqUnionWithInteger2:Integer[]=
[ ]
; var resCharacterSeqUnionWithInteger3:Integer[]=
[ ]
; //.
var resIntegerSeqCastToInteger1:Integer[]=
[ 256, 257, 258, 259 ]
; //.
var resIntegerSeqUnionWithInteger1:Integer[]=
[ 256, 257, 258, 259 ]
; var resIntegerSeqUnionWithInteger2:Integer[]=
[ 256, 257, 258, 259, 256, 257, 258, 259 ]
; var resIntegerSeqUnionWithInteger3:Integer[]=
[ 256, 257, 258, 259, 256, 257, 258, 259 ]
; //.
var resLongSeqCastToInteger1:Integer[]=
[ 1000000, 1000001, 1000002, 1000003 ]
; //.
var resLongSeqUnionWithInteger1:Integer[]=
[ 256, 257, 258, 259 ]
; var resLongSeqUnionWithInteger2:Integer[]=
[ 256, 257, 258, 259, 1000000, 1000001, 1000002, 1000003 ]
; var resLongSeqUnionWithInteger3:Integer[]=
[ 1000000, 1000001, 1000002, 1000003, 256, 257, 258, 259 ]
; //.
var resFloatSeqCastToInteger1:Integer[]=
[ 5, 6, 7 ]
; //.
var resFloatSeqUnionWithInteger1:Integer[]=
[ 256, 257, 258, 259 ]
; var resFloatSeqUnionWithInteger2:Integer[]=
[ 256, 257, 258, 259, 5, 6, 7 ]
; var resFloatSeqUnionWithInteger3:Integer[]=
[ 5, 6, 7, 256, 257, 258, 259 ]
; //.
var resDoubleSeqCastToInteger1:Integer[]=
[ 6, 7 ]
; //.
var resDoubleSeqUnionWithInteger1:Integer[]=
[ 256, 257, 258, 259 ]
; var resDoubleSeqUnionWithInteger2:Integer[]=
[ 256, 257, 258, 259, 6, 7 ]
; var resDoubleSeqUnionWithInteger3:Integer[]=
[ 6, 7, 256, 257, 258, 259 ]
; //.
var resNumberSeqCastToInteger1:Integer[]=
[ 35120, 35121, 35122, 35123, 35124, 35125 ]
; //.
var resNumberSeqUnionWithInteger1:Integer[]=
[ 256, 257, 258, 259 ]
; var resNumberSeqUnionWithInteger2:Integer[]=
[ 256, 257, 258, 259, 35120, 35121, 35122, 35123, 35124, 35125 ]
; var resNumberSeqUnionWithInteger3:Integer[]=
[ 35120, 35121, 35122, 35123, 35124, 35125, 256, 257, 258, 259 ]
; //.
var resByteSeqCastToLong1:Long[]=
[ 1, 2, 3, 4, 5, 6, 7 ]
; //.
var resByteSeqUnionWithLong1:Long[]=
[ 1000000, 1000001, 1000002, 1000003 ]
; var resByteSeqUnionWithLong2:Long[]=
[ 1000000, 1000001, 1000002, 1000003, 1, 2, 3, 4, 5, 6, 7 ]
; var resByteSeqUnionWithLong3:Long[]=
[ 1, 2, 3, 4, 5, 6, 7, 1000000, 1000001, 1000002, 1000003 ]
; //.
var resShortSeqCastToLong1:Long[]=
[ 16000, 16001, 16002, 16003, 16004 ]
; //.
var resShortSeqUnionWithLong1:Long[]=
[ 1000000, 1000001, 1000002, 1000003 ]
; var resShortSeqUnionWithLong2:Long[]=
[ 1000000, 1000001, 1000002, 1000003, 16000, 16001, 16002, 16003, 16004 ]
; var resShortSeqUnionWithLong3:Long[]=
[ 16000, 16001, 16002, 16003, 16004, 1000000, 1000001, 1000002, 1000003 ]
; //.
var resCharacterSeqCastToLong1:Long[]=
[ ]
; //.
var resCharacterSeqUnionWithLong1:Long[]=
[ ]
; var resCharacterSeqUnionWithLong2:Long[]=
[ ]
; var resCharacterSeqUnionWithLong3:Long[]=
[ ]
; //.
var resIntegerSeqCastToLong1:Long[]=
[ 256, 257, 258, 259 ]
; //.
var resIntegerSeqUnionWithLong1:Long[]=
[ 1000000, 1000001, 1000002, 1000003 ]
; var resIntegerSeqUnionWithLong2:Long[]=
[ 1000000, 1000001, 1000002, 1000003, 256, 257, 258, 259 ]
; var resIntegerSeqUnionWithLong3:Long[]=
[ 256, 257, 258, 259, 1000000, 1000001, 1000002, 1000003 ]
; //.
var resLongSeqCastToLong1:Long[]=
[ 1000000, 1000001, 1000002, 1000003 ]
; //.
var resLongSeqUnionWithLong1:Long[]=
[ 1000000, 1000001, 1000002, 1000003 ]
; var resLongSeqUnionWithLong2:Long[]=
[ 1000000, 1000001, 1000002, 1000003, 1000000, 1000001, 1000002, 1000003 ]
; var resLongSeqUnionWithLong3:Long[]=
[ 1000000, 1000001, 1000002, 1000003, 1000000, 1000001, 1000002, 1000003 ]
; //.
var resFloatSeqCastToLong1:Long[]=
[ 5, 6, 7 ]
; //.
var resFloatSeqUnionWithLong1:Long[]=
[ 1000000, 1000001, 1000002, 1000003 ]
; var resFloatSeqUnionWithLong2:Long[]=
[ 1000000, 1000001, 1000002, 1000003, 5, 6, 7 ]
; var resFloatSeqUnionWithLong3:Long[]=
[ 5, 6, 7, 1000000, 1000001, 1000002, 1000003 ]
; //.
var resDoubleSeqCastToLong1:Long[]=
[ 6, 7 ]
; //.
var resDoubleSeqUnionWithLong1:Long[]=
[ 1000000, 1000001, 1000002, 1000003 ]
; var resDoubleSeqUnionWithLong2:Long[]=
[ 1000000, 1000001, 1000002, 1000003, 6, 7 ]
; var resDoubleSeqUnionWithLong3:Long[]=
[ 6, 7, 1000000, 1000001, 1000002, 1000003 ]
; //.
var resNumberSeqCastToLong1:Long[]=
[ 35120, 35121, 35122, 35123, 35124, 35125 ]
; //.
var resNumberSeqUnionWithLong1:Long[]=
[ 1000000, 1000001, 1000002, 1000003 ]
; var resNumberSeqUnionWithLong2:Long[]=
[ 1000000, 1000001, 1000002, 1000003, 35120, 35121, 35122, 35123, 35124, 35125 ]
; var resNumberSeqUnionWithLong3:Long[]=
[ 35120, 35121, 35122, 35123, 35124, 35125, 1000000, 1000001, 1000002, 1000003 ]
; //.
var resByteSeqCastToFloat1:Float[]=
[ 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0 ]
; //.
var resByteSeqUnionWithFloat1:Float[]=
[ 5.5, 6.5, 7.25 ]
; var resByteSeqUnionWithFloat2:Float[]=
[ 5.5, 6.5, 7.25, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0 ]
; var resByteSeqUnionWithFloat3:Float[]=
[ 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 5.5, 6.5, 7.25 ]
; //.
var resShortSeqCastToFloat1:Float[]=
[ 16000.0, 16001.0, 16002.0, 16003.0, 16004.0 ]
; //.
var resShortSeqUnionWithFloat1:Float[]=
[ 5.5, 6.5, 7.25 ]
; var resShortSeqUnionWithFloat2:Float[]=
[ 5.5, 6.5, 7.25, 16000.0, 16001.0, 16002.0, 16003.0, 16004.0 ]
; var resShortSeqUnionWithFloat3:Float[]=
[ 16000.0, 16001.0, 16002.0, 16003.0, 16004.0, 5.5, 6.5, 7.25 ]
; //.
var resCharacterSeqCastToFloat1:Float[]=
[ ]
; //.
var resCharacterSeqUnionWithFloat1:Float[]=
[ ]
; var resCharacterSeqUnionWithFloat2:Float[]=
[ ]
; var resCharacterSeqUnionWithFloat3:Float[]=
[ ]
; //.
var resIntegerSeqCastToFloat1:Float[]=
[ 256.0, 257.0, 258.0, 259.0 ]
; //.
var resIntegerSeqUnionWithFloat1:Float[]=
[ 5.5, 6.5, 7.25 ]
; var resIntegerSeqUnionWithFloat2:Float[]=
[ 5.5, 6.5, 7.25, 256.0, 257.0, 258.0, 259.0 ]
; var resIntegerSeqUnionWithFloat3:Float[]=
[ 256.0, 257.0, 258.0, 259.0, 5.5, 6.5, 7.25 ]
; //.
var resLongSeqCastToFloat1:Float[]=
[ 1000000.0, 1000001.0, 1000002.0, 1000003.0 ]
; //.
var resLongSeqUnionWithFloat1:Float[]=
[ 5.5, 6.5, 7.25 ]
; var resLongSeqUnionWithFloat2:Float[]=
[ 5.5, 6.5, 7.25, 1000000.0, 1000001.0, 1000002.0, 1000003.0 ]
; var resLongSeqUnionWithFloat3:Float[]=
[ 1000000.0, 1000001.0, 1000002.0, 1000003.0, 5.5, 6.5, 7.25 ]
; //.
var resFloatSeqCastToFloat1:Float[]=
[ 5.5, 6.5, 7.25 ]
; //.
var resFloatSeqUnionWithFloat1:Float[]=
[ 5.5, 6.5, 7.25 ]
; var resFloatSeqUnionWithFloat2:Float[]=
[ 5.5, 6.5, 7.25, 5.5, 6.5, 7.25 ]
; var resFloatSeqUnionWithFloat3:Float[]=
[ 5.5, 6.5, 7.25, 5.5, 6.5, 7.25 ]
; //.
var resDoubleSeqCastToFloat1:Float[]=
[ 6.125, 7.125 ]
; //.
var resDoubleSeqUnionWithFloat1:Float[]=
[ 5.5, 6.5, 7.25 ]
; var resDoubleSeqUnionWithFloat2:Float[]=
[ 5.5, 6.5, 7.25, 6.125, 7.125 ]
; var resDoubleSeqUnionWithFloat3:Float[]=
[ 6.125, 7.125, 5.5, 6.5, 7.25 ]
; //.
var resNumberSeqCastToFloat1:Float[]=
[ 35120.5, 35121.5, 35122.5, 35123.5, 35124.5, 35125.5 ]
; //.
var resNumberSeqUnionWithFloat1:Float[]=
[ 5.5, 6.5, 7.25 ]
; var resNumberSeqUnionWithFloat2:Float[]=
[ 5.5, 6.5, 7.25, 35120.5, 35121.5, 35122.5, 35123.5, 35124.5, 35125.5 ]
; var resNumberSeqUnionWithFloat3:Float[]=
[ 35120.5, 35121.5, 35122.5, 35123.5, 35124.5, 35125.5, 5.5, 6.5, 7.25 ]
; //.
var resByteSeqCastToDouble1:Double[]=
[ 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0 ]
; //.
var resByteSeqUnionWithDouble1:Double[]=
[ 6.125, 7.125 ]
; var resByteSeqUnionWithDouble2:Double[]=
[ 6.125, 7.125, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0 ]
; var resByteSeqUnionWithDouble3:Double[]=
[ 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 6.125, 7.125 ]
; //.
var resShortSeqCastToDouble1:Double[]=
[ 16000.0, 16001.0, 16002.0, 16003.0, 16004.0 ]
; //.
var resShortSeqUnionWithDouble1:Double[]=
[ 6.125, 7.125 ]
; var resShortSeqUnionWithDouble2:Double[]=
[ 6.125, 7.125, 16000.0, 16001.0, 16002.0, 16003.0, 16004.0 ]
; var resShortSeqUnionWithDouble3:Double[]=
[ 16000.0, 16001.0, 16002.0, 16003.0, 16004.0, 6.125, 7.125 ]
; //.
var resCharacterSeqCastToDouble1:Double[]=
[ ]
; //.
var resCharacterSeqUnionWithDouble1:Double[]=
[ ]
; var resCharacterSeqUnionWithDouble2:Double[]=
[ ]
; var resCharacterSeqUnionWithDouble3:Double[]=
[ ]
; //.
var resIntegerSeqCastToDouble1:Double[]=
[ 256.0, 257.0, 258.0, 259.0 ]
; //.
var resIntegerSeqUnionWithDouble1:Double[]=
[ 6.125, 7.125 ]
; var resIntegerSeqUnionWithDouble2:Double[]=
[ 6.125, 7.125, 256.0, 257.0, 258.0, 259.0 ]
; var resIntegerSeqUnionWithDouble3:Double[]=
[ 256.0, 257.0, 258.0, 259.0, 6.125, 7.125 ]
; //.
var resLongSeqCastToDouble1:Double[]=
[ 1000000.0, 1000001.0, 1000002.0, 1000003.0 ]
; //.
var resLongSeqUnionWithDouble1:Double[]=
[ 6.125, 7.125 ]
; var resLongSeqUnionWithDouble2:Double[]=
[ 6.125, 7.125, 1000000.0, 1000001.0, 1000002.0, 1000003.0 ]
; var resLongSeqUnionWithDouble3:Double[]=
[ 1000000.0, 1000001.0, 1000002.0, 1000003.0, 6.125, 7.125 ]
; //.
var resFloatSeqCastToDouble1:Double[]=
[ 5.5, 6.5, 7.25 ]
; //.
var resFloatSeqUnionWithDouble1:Double[]=
[ 6.125, 7.125 ]
; var resFloatSeqUnionWithDouble2:Double[]=
[ 6.125, 7.125, 5.5, 6.5, 7.25 ]
; var resFloatSeqUnionWithDouble3:Double[]=
[ 5.5, 6.5, 7.25, 6.125, 7.125 ]
; //.
var resDoubleSeqCastToDouble1:Double[]=
[ 6.125, 7.125 ]
; //.
var resDoubleSeqUnionWithDouble1:Double[]=
[ 6.125, 7.125 ]
; var resDoubleSeqUnionWithDouble2:Double[]=
[ 6.125, 7.125, 6.125, 7.125 ]
; var resDoubleSeqUnionWithDouble3:Double[]=
[ 6.125, 7.125, 6.125, 7.125 ]
; //.
var resNumberSeqCastToDouble1:Double[]=
[ 35120.5, 35121.5, 35122.5, 35123.5, 35124.5, 35125.5 ]
; //.
var resNumberSeqUnionWithDouble1:Double[]=
[ 6.125, 7.125 ]
; var resNumberSeqUnionWithDouble2:Double[]=
[ 6.125, 7.125, 35120.5, 35121.5, 35122.5, 35123.5, 35124.5, 35125.5 ]
; var resNumberSeqUnionWithDouble3:Double[]=
[ 35120.5, 35121.5, 35122.5, 35123.5, 35124.5, 35125.5, 6.125, 7.125 ]
; //.
var resByteSeqCastToNumber1:Number[]=
[ 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0 ]
; //.
var resByteSeqUnionWithNumber1:Number[]=
[ 35120.5, 35121.5, 35122.5, 35123.5, 35124.5, 35125.5 ]
; var resByteSeqUnionWithNumber2:Number[]=
[ 35120.5, 35121.5, 35122.5, 35123.5, 35124.5, 35125.5, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0 ]
; var resByteSeqUnionWithNumber3:Number[]=
[ 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 35120.5, 35121.5, 35122.5, 35123.5, 35124.5, 35125.5 ]
; //.
var resShortSeqCastToNumber1:Number[]=
[ 16000.0, 16001.0, 16002.0, 16003.0, 16004.0 ]
; //.
var resShortSeqUnionWithNumber1:Number[]=
[ 35120.5, 35121.5, 35122.5, 35123.5, 35124.5, 35125.5 ]
; var resShortSeqUnionWithNumber2:Number[]=
[ 35120.5, 35121.5, 35122.5, 35123.5, 35124.5, 35125.5, 16000.0, 16001.0, 16002.0, 16003.0, 16004.0 ]
; var resShortSeqUnionWithNumber3:Number[]=
[ 16000.0, 16001.0, 16002.0, 16003.0, 16004.0, 35120.5, 35121.5, 35122.5, 35123.5, 35124.5, 35125.5 ]
; //.
var resCharacterSeqCastToNumber1:Number[]=
[ ]
; //.
var resCharacterSeqUnionWithNumber1:Number[]=
[ ]
; var resCharacterSeqUnionWithNumber2:Number[]=
[ ]
; var resCharacterSeqUnionWithNumber3:Number[]=
[ ]
; //.
var resIntegerSeqCastToNumber1:Number[]=
[ 256.0, 257.0, 258.0, 259.0 ]
; //.
var resIntegerSeqUnionWithNumber1:Number[]=
[ 35120.5, 35121.5, 35122.5, 35123.5, 35124.5, 35125.5 ]
; var resIntegerSeqUnionWithNumber2:Number[]=
[ 35120.5, 35121.5, 35122.5, 35123.5, 35124.5, 35125.5, 256.0, 257.0, 258.0, 259.0 ]
; var resIntegerSeqUnionWithNumber3:Number[]=
[ 256.0, 257.0, 258.0, 259.0, 35120.5, 35121.5, 35122.5, 35123.5, 35124.5, 35125.5 ]
; //.
var resLongSeqCastToNumber1:Number[]=
[ 1000000.0, 1000001.0, 1000002.0, 1000003.0 ]
; //.
var resLongSeqUnionWithNumber1:Number[]=
[ 35120.5, 35121.5, 35122.5, 35123.5, 35124.5, 35125.5 ]
; var resLongSeqUnionWithNumber2:Number[]=
[ 35120.5, 35121.5, 35122.5, 35123.5, 35124.5, 35125.5, 1000000.0, 1000001.0, 1000002.0, 1000003.0 ]
; var resLongSeqUnionWithNumber3:Number[]=
[ 1000000.0, 1000001.0, 1000002.0, 1000003.0, 35120.5, 35121.5, 35122.5, 35123.5, 35124.5, 35125.5 ]
; //.
var resFloatSeqCastToNumber1:Number[]=
[ 5.5, 6.5, 7.25 ]
; //.
var resFloatSeqUnionWithNumber1:Number[]=
[ 35120.5, 35121.5, 35122.5, 35123.5, 35124.5, 35125.5 ]
; var resFloatSeqUnionWithNumber2:Number[]=
[ 35120.5, 35121.5, 35122.5, 35123.5, 35124.5, 35125.5, 5.5, 6.5, 7.25 ]
; var resFloatSeqUnionWithNumber3:Number[]=
[ 5.5, 6.5, 7.25, 35120.5, 35121.5, 35122.5, 35123.5, 35124.5, 35125.5 ]
; //.
var resDoubleSeqCastToNumber1:Number[]=
[ 6.125, 7.125 ]
; //.
var resDoubleSeqUnionWithNumber1:Number[]=
[ 35120.5, 35121.5, 35122.5, 35123.5, 35124.5, 35125.5 ]
; var resDoubleSeqUnionWithNumber2:Number[]=
[ 35120.5, 35121.5, 35122.5, 35123.5, 35124.5, 35125.5, 6.125, 7.125 ]
; var resDoubleSeqUnionWithNumber3:Number[]=
[ 6.125, 7.125, 35120.5, 35121.5, 35122.5, 35123.5, 35124.5, 35125.5 ]
; //.
var resNumberSeqCastToNumber1:Number[]=
[ 35120.5, 35121.5, 35122.5, 35123.5, 35124.5, 35125.5 ]
; //.
var resNumberSeqUnionWithNumber1:Number[]=
[ 35120.5, 35121.5, 35122.5, 35123.5, 35124.5, 35125.5 ]
; var resNumberSeqUnionWithNumber2:Number[]=
[ 35120.5, 35121.5, 35122.5, 35123.5, 35124.5, 35125.5, 35120.5, 35121.5, 35122.5, 35123.5, 35124.5, 35125.5 ]
; var resNumberSeqUnionWithNumber3:Number[]=
[ 35120.5, 35121.5, 35122.5, 35123.5, 35124.5, 35125.5, 35120.5, 35121.5, 35122.5, 35123.5, 35124.5, 35125.5 ]
; //

}


