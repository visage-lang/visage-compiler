/*
 * Lazy/eager binding for class variables and variables in a class function
 *
 * @test/fxunit
 * @run
 */

import javafx.fxunit.FXTestCase;

public class LzClassVar extends FXTestCase {
    function checkFlag (expected : Boolean, current : Boolean, msg : String) {
        if (expected != current) println("FAILED {msg}");
        assertEquals("{expected as Boolean}", "{current as Boolean}");
    }

/********** Lazy/eager Binding for class variables **********/

    /***** Byte *****/
    var flagByte : Boolean = false as Boolean;
    var flagBdByte : Boolean = false as Boolean;

    function fByte(x : Byte) : Byte{
        flagByte = true;
        return 122;
    }

    var valueByte : Byte = 120 as Byte;
    def bindedByte7 : Byte = bind lazy valueByte;
    var bindedByte6 : Byte = bind bindedByte7;
    def bindedByte5 : Byte = bind lazy bindedByte6;
    var bindedByte4 : Byte = bind bindedByte5;
    def bindedByte3 : Byte = bind lazy fByte(bindedByte4);
    var bindedByte2 : Byte = bind lazy bindedByte3;
    def bindedByte : Byte = bind lazy bindedByte2;

    function testLazyByte() {
        checkFlag(false, flagByte, "Byte 1");
        valueByte = 121 as Byte;
        checkFlag(false, flagByte, "Byte 2");
        var trashByte : Byte = bindedByte;
        checkFlag(true, flagByte, "Byte 3");
        if (trashByte != 122) println("FAILED Byte 4");
        assertEquals("{trashByte as Byte}", "{122 as Byte}");
        flagByte = false; // reset flag
    }

    function fBdByte(x : Byte) : Byte{
        flagBdByte = true;
        return 122;
    }

    var valueBdByte : Byte = 120 as Byte;
    def BindedBdByte7 : Byte = bind valueBdByte;
    var BindedBdByte6 : Byte = bind BindedBdByte7;
    def BindedBdByte5 : Byte = bind BindedBdByte6;
    var BindedBdByte4 : Byte = bind BindedBdByte5;
    def BindedBdByte3 : Byte = bind fBdByte(BindedBdByte4);
    var BindedBdByte2 : Byte = bind BindedBdByte3;
    def BindedBdByte : Byte = bind BindedBdByte2;

    function testBindByte() {
        checkFlag(true, flagBdByte, "Byte 1B");
        flagBdByte = false; // reset flag
        valueBdByte = 121 as Byte;
        checkFlag(true, flagBdByte, "Byte 2B");
        flagBdByte = false; // reset flag
        var trashBdByte : Byte = BindedBdByte;
        checkFlag(false, flagBdByte, "Byte 3B");
        if (trashBdByte != 122) println("FAILED Byte 4B");
        assertEquals("{trashBdByte as Byte}", "{122 as Byte}");
        flagBdByte = false; // reset flag
    }

    /***** Short *****/
    var flagShort : Boolean = false as Boolean;
    var flagBdShort : Boolean = false as Boolean;

    function fShort(x : Short) : Short{
        flagShort = true;
        return 30002;
    }

    var valueShort : Short = 30000 as Short;
    def bindedShort7 : Short = bind lazy valueShort;
    var bindedShort6 : Short = bind bindedShort7;
    def bindedShort5 : Short = bind lazy bindedShort6;
    var bindedShort4 : Short = bind bindedShort5;
    def bindedShort3 : Short = bind lazy fShort(bindedShort4);
    var bindedShort2 : Short = bind lazy bindedShort3;
    def bindedShort : Short = bind lazy bindedShort2;

    function testLazyShort() {
        checkFlag(false, flagShort, "Short 1");
        valueShort = 30001 as Short;
        checkFlag(false, flagShort, "Short 2");
        var trashShort : Short = bindedShort;
        checkFlag(true, flagShort, "Short 3");
        if (trashShort != 30002) println("FAILED Short 4");
        assertEquals("{trashShort as Short}", "{30002 as Short}");
        flagShort = false; // reset flag
    }

    function fBdShort(x : Short) : Short{
        flagBdShort = true;
        return 30002;
    }

    var valueBdShort : Short = 30000 as Short;
    def BindedBdShort7 : Short = bind valueBdShort;
    var BindedBdShort6 : Short = bind BindedBdShort7;
    def BindedBdShort5 : Short = bind BindedBdShort6;
    var BindedBdShort4 : Short = bind BindedBdShort5;
    def BindedBdShort3 : Short = bind fBdShort(BindedBdShort4);
    var BindedBdShort2 : Short = bind BindedBdShort3;
    def BindedBdShort : Short = bind BindedBdShort2;

    function testBindShort() {
        checkFlag(true, flagBdShort, "Short 1B");
        flagBdShort = false; // reset flag
        valueBdShort = 30001 as Short;
        checkFlag(true, flagBdShort, "Short 2B");
        flagBdShort = false; // reset flag
        var trashBdShort : Short = BindedBdShort;
        checkFlag(false, flagBdShort, "Short 3B");
        if (trashBdShort != 30002) println("FAILED Short 4B");
        assertEquals("{trashBdShort as Short}", "{30002 as Short}");
        flagBdShort = false; // reset flag
    }

    /***** Character *****/
    var flagCharacter : Boolean = false as Boolean;
    var flagBdCharacter : Boolean = false as Boolean;

    function fCharacter(x : Character) : Character{
        flagCharacter = true;
        return 62;
    }

    var valueCharacter : Character = 64 as Character;
    def bindedCharacter7 : Character = bind lazy valueCharacter;
    var bindedCharacter6 : Character = bind bindedCharacter7;
    def bindedCharacter5 : Character = bind lazy bindedCharacter6;
    var bindedCharacter4 : Character = bind bindedCharacter5;
    def bindedCharacter3 : Character = bind lazy fCharacter(bindedCharacter4);
    var bindedCharacter2 : Character = bind lazy bindedCharacter3;
    def bindedCharacter : Character = bind lazy bindedCharacter2;

    function testLazyCharacter() {
        checkFlag(false, flagCharacter, "Character 1");
        valueCharacter = 61 as Character;
        checkFlag(false, flagCharacter, "Character 2");
        var trashCharacter : Character = bindedCharacter;
        checkFlag(true, flagCharacter, "Character 3");
        if (trashCharacter != 62) println("FAILED Character 4");
        assertEquals("{trashCharacter as Character}", "{62 as Character}");
        flagCharacter = false; // reset flag
    }

    function fBdCharacter(x : Character) : Character{
        flagBdCharacter = true;
        return 62;
    }

    var valueBdCharacter : Character = 64 as Character;
    def BindedBdCharacter7 : Character = bind valueBdCharacter;
    var BindedBdCharacter6 : Character = bind BindedBdCharacter7;
    def BindedBdCharacter5 : Character = bind BindedBdCharacter6;
    var BindedBdCharacter4 : Character = bind BindedBdCharacter5;
    def BindedBdCharacter3 : Character = bind fBdCharacter(BindedBdCharacter4);
    var BindedBdCharacter2 : Character = bind BindedBdCharacter3;
    def BindedBdCharacter : Character = bind BindedBdCharacter2;

    function testBindCharacter() {
        checkFlag(true, flagBdCharacter, "Character 1B");
        flagBdCharacter = false; // reset flag
        valueBdCharacter = 61 as Character;
        checkFlag(true, flagBdCharacter, "Character 2B");
        flagBdCharacter = false; // reset flag
        var trashBdCharacter : Character = BindedBdCharacter;
        checkFlag(false, flagBdCharacter, "Character 3B");
        if (trashBdCharacter != 62) println("FAILED Character 4B");
        assertEquals("{trashBdCharacter as Character}", "{62 as Character}");
        flagBdCharacter = false; // reset flag
    }

    /***** Integer *****/
    var flagInteger : Boolean = false as Boolean;
    var flagBdInteger : Boolean = false as Boolean;

    function fInteger(x : Integer) : Integer{
        flagInteger = true;
        return 1000002;
    }

    var valueInteger : Integer = 1000000 as Integer;
    def bindedInteger7 : Integer = bind lazy valueInteger;
    var bindedInteger6 : Integer = bind bindedInteger7;
    def bindedInteger5 : Integer = bind lazy bindedInteger6;
    var bindedInteger4 : Integer = bind bindedInteger5;
    def bindedInteger3 : Integer = bind lazy fInteger(bindedInteger4);
    var bindedInteger2 : Integer = bind lazy bindedInteger3;
    def bindedInteger : Integer = bind lazy bindedInteger2;

    function testLazyInteger() {
        checkFlag(false, flagInteger, "Integer 1");
        valueInteger = 1000001 as Integer;
        checkFlag(false, flagInteger, "Integer 2");
        var trashInteger : Integer = bindedInteger;
        checkFlag(true, flagInteger, "Integer 3");
        if (trashInteger != 1000002) println("FAILED Integer 4");
        assertEquals("{trashInteger as Integer}", "{1000002 as Integer}");
        flagInteger = false; // reset flag
    }

    function fBdInteger(x : Integer) : Integer{
        flagBdInteger = true;
        return 1000002;
    }

    var valueBdInteger : Integer = 1000000 as Integer;
    def BindedBdInteger7 : Integer = bind valueBdInteger;
    var BindedBdInteger6 : Integer = bind BindedBdInteger7;
    def BindedBdInteger5 : Integer = bind BindedBdInteger6;
    var BindedBdInteger4 : Integer = bind BindedBdInteger5;
    def BindedBdInteger3 : Integer = bind fBdInteger(BindedBdInteger4);
    var BindedBdInteger2 : Integer = bind BindedBdInteger3;
    def BindedBdInteger : Integer = bind BindedBdInteger2;

    function testBindInteger() {
        checkFlag(true, flagBdInteger, "Integer 1B");
        flagBdInteger = false; // reset flag
        valueBdInteger = 1000001 as Integer;
        checkFlag(true, flagBdInteger, "Integer 2B");
        flagBdInteger = false; // reset flag
        var trashBdInteger : Integer = BindedBdInteger;
        checkFlag(false, flagBdInteger, "Integer 3B");
        if (trashBdInteger != 1000002) println("FAILED Integer 4B");
        assertEquals("{trashBdInteger as Integer}", "{1000002 as Integer}");
        flagBdInteger = false; // reset flag
    }

    /***** Long *****/
    var flagLong : Boolean = false as Boolean;
    var flagBdLong : Boolean = false as Boolean;

    function fLong(x : Long) : Long{
        flagLong = true;
        return 1000000002;
    }

    var valueLong : Long = 1000000000 as Long;
    def bindedLong7 : Long = bind lazy valueLong;
    var bindedLong6 : Long = bind bindedLong7;
    def bindedLong5 : Long = bind lazy bindedLong6;
    var bindedLong4 : Long = bind bindedLong5;
    def bindedLong3 : Long = bind lazy fLong(bindedLong4);
    var bindedLong2 : Long = bind lazy bindedLong3;
    def bindedLong : Long = bind lazy bindedLong2;

    function testLazyLong() {
        checkFlag(false, flagLong, "Long 1");
        valueLong = 1000000001 as Long;
        checkFlag(false, flagLong, "Long 2");
        var trashLong : Long = bindedLong;
        checkFlag(true, flagLong, "Long 3");
        if (trashLong != 1000000002) println("FAILED Long 4");
        assertEquals("{trashLong as Long}", "{1000000002 as Long}");
        flagLong = false; // reset flag
    }

    function fBdLong(x : Long) : Long{
        flagBdLong = true;
        return 1000000002;
    }

    var valueBdLong : Long = 1000000000 as Long;
    def BindedBdLong7 : Long = bind valueBdLong;
    var BindedBdLong6 : Long = bind BindedBdLong7;
    def BindedBdLong5 : Long = bind BindedBdLong6;
    var BindedBdLong4 : Long = bind BindedBdLong5;
    def BindedBdLong3 : Long = bind fBdLong(BindedBdLong4);
    var BindedBdLong2 : Long = bind BindedBdLong3;
    def BindedBdLong : Long = bind BindedBdLong2;

    function testBindLong() {
        checkFlag(true, flagBdLong, "Long 1B");
        flagBdLong = false; // reset flag
        valueBdLong = 1000000001 as Long;
        checkFlag(true, flagBdLong, "Long 2B");
        flagBdLong = false; // reset flag
        var trashBdLong : Long = BindedBdLong;
        checkFlag(false, flagBdLong, "Long 3B");
        if (trashBdLong != 1000000002) println("FAILED Long 4B");
        assertEquals("{trashBdLong as Long}", "{1000000002 as Long}");
        flagBdLong = false; // reset flag
    }

    /***** Float *****/
    var flagFloat : Boolean = false as Boolean;
    var flagBdFloat : Boolean = false as Boolean;

    function fFloat(x : Float) : Float{
        flagFloat = true;
        return 12.5;
    }

    var valueFloat : Float = 10.5 as Float;
    def bindedFloat7 : Float = bind lazy valueFloat;
    var bindedFloat6 : Float = bind bindedFloat7;
    def bindedFloat5 : Float = bind lazy bindedFloat6;
    var bindedFloat4 : Float = bind bindedFloat5;
    def bindedFloat3 : Float = bind lazy fFloat(bindedFloat4);
    var bindedFloat2 : Float = bind lazy bindedFloat3;
    def bindedFloat : Float = bind lazy bindedFloat2;

    function testLazyFloat() {
        checkFlag(false, flagFloat, "Float 1");
        valueFloat = 11.5 as Float;
        checkFlag(false, flagFloat, "Float 2");
        var trashFloat : Float = bindedFloat;
        checkFlag(true, flagFloat, "Float 3");
        if (trashFloat != 12.5) println("FAILED Float 4");
        assertEquals("{trashFloat as Float}", "{12.5 as Float}");
        flagFloat = false; // reset flag
    }

    function fBdFloat(x : Float) : Float{
        flagBdFloat = true;
        return 12.5;
    }

    var valueBdFloat : Float = 10.5 as Float;
    def BindedBdFloat7 : Float = bind valueBdFloat;
    var BindedBdFloat6 : Float = bind BindedBdFloat7;
    def BindedBdFloat5 : Float = bind BindedBdFloat6;
    var BindedBdFloat4 : Float = bind BindedBdFloat5;
    def BindedBdFloat3 : Float = bind fBdFloat(BindedBdFloat4);
    var BindedBdFloat2 : Float = bind BindedBdFloat3;
    def BindedBdFloat : Float = bind BindedBdFloat2;

    function testBindFloat() {
        checkFlag(true, flagBdFloat, "Float 1B");
        flagBdFloat = false; // reset flag
        valueBdFloat = 11.5 as Float;
        checkFlag(true, flagBdFloat, "Float 2B");
        flagBdFloat = false; // reset flag
        var trashBdFloat : Float = BindedBdFloat;
        checkFlag(false, flagBdFloat, "Float 3B");
        if (trashBdFloat != 12.5) println("FAILED Float 4B");
        assertEquals("{trashBdFloat as Float}", "{12.5 as Float}");
        flagBdFloat = false; // reset flag
    }

    /***** Double *****/
    var flagDouble : Boolean = false as Boolean;
    var flagBdDouble : Boolean = false as Boolean;

    function fDouble(x : Double) : Double{
        flagDouble = true;
        return 1.22e4;
    }

    var valueDouble : Double = 1.25e4 as Double;
    def bindedDouble7 : Double = bind lazy valueDouble;
    var bindedDouble6 : Double = bind bindedDouble7;
    def bindedDouble5 : Double = bind lazy bindedDouble6;
    var bindedDouble4 : Double = bind bindedDouble5;
    def bindedDouble3 : Double = bind lazy fDouble(bindedDouble4);
    var bindedDouble2 : Double = bind lazy bindedDouble3;
    def bindedDouble : Double = bind lazy bindedDouble2;

    function testLazyDouble() {
        checkFlag(false, flagDouble, "Double 1");
        valueDouble = 1.21e4 as Double;
        checkFlag(false, flagDouble, "Double 2");
        var trashDouble : Double = bindedDouble;
        checkFlag(true, flagDouble, "Double 3");
        if (trashDouble != 1.22e4) println("FAILED Double 4");
        assertEquals("{trashDouble as Double}", "{1.22e4 as Double}");
        flagDouble = false; // reset flag
    }

    function fBdDouble(x : Double) : Double{
        flagBdDouble = true;
        return 1.22e4;
    }

    var valueBdDouble : Double = 1.25e4 as Double;
    def BindedBdDouble7 : Double = bind valueBdDouble;
    var BindedBdDouble6 : Double = bind BindedBdDouble7;
    def BindedBdDouble5 : Double = bind BindedBdDouble6;
    var BindedBdDouble4 : Double = bind BindedBdDouble5;
    def BindedBdDouble3 : Double = bind fBdDouble(BindedBdDouble4);
    var BindedBdDouble2 : Double = bind BindedBdDouble3;
    def BindedBdDouble : Double = bind BindedBdDouble2;

    function testBindDouble() {
        checkFlag(true, flagBdDouble, "Double 1B");
        flagBdDouble = false; // reset flag
        valueBdDouble = 1.21e4 as Double;
        checkFlag(true, flagBdDouble, "Double 2B");
        flagBdDouble = false; // reset flag
        var trashBdDouble : Double = BindedBdDouble;
        checkFlag(false, flagBdDouble, "Double 3B");
        if (trashBdDouble != 1.22e4) println("FAILED Double 4B");
        assertEquals("{trashBdDouble as Double}", "{1.22e4 as Double}");
        flagBdDouble = false; // reset flag
    }

    /***** Number *****/
    var flagNumber : Boolean = false as Boolean;
    var flagBdNumber : Boolean = false as Boolean;

    function fNumber(x : Number) : Number{
        flagNumber = true;
        return 102;
    }

    var valueNumber : Number = 100 as Number;
    def bindedNumber7 : Number = bind lazy valueNumber;
    var bindedNumber6 : Number = bind bindedNumber7;
    def bindedNumber5 : Number = bind lazy bindedNumber6;
    var bindedNumber4 : Number = bind bindedNumber5;
    def bindedNumber3 : Number = bind lazy fNumber(bindedNumber4);
    var bindedNumber2 : Number = bind lazy bindedNumber3;
    def bindedNumber : Number = bind lazy bindedNumber2;

    function testLazyNumber() {
        checkFlag(false, flagNumber, "Number 1");
        valueNumber = 101 as Number;
        checkFlag(false, flagNumber, "Number 2");
        var trashNumber : Number = bindedNumber;
        checkFlag(true, flagNumber, "Number 3");
        if (trashNumber != 102) println("FAILED Number 4");
        assertEquals("{trashNumber as Number}", "{102 as Number}");
        flagNumber = false; // reset flag
    }

    function fBdNumber(x : Number) : Number{
        flagBdNumber = true;
        return 102;
    }

    var valueBdNumber : Number = 100 as Number;
    def BindedBdNumber7 : Number = bind valueBdNumber;
    var BindedBdNumber6 : Number = bind BindedBdNumber7;
    def BindedBdNumber5 : Number = bind BindedBdNumber6;
    var BindedBdNumber4 : Number = bind BindedBdNumber5;
    def BindedBdNumber3 : Number = bind fBdNumber(BindedBdNumber4);
    var BindedBdNumber2 : Number = bind BindedBdNumber3;
    def BindedBdNumber : Number = bind BindedBdNumber2;

    function testBindNumber() {
        checkFlag(true, flagBdNumber, "Number 1B");
        flagBdNumber = false; // reset flag
        valueBdNumber = 101 as Number;
        checkFlag(true, flagBdNumber, "Number 2B");
        flagBdNumber = false; // reset flag
        var trashBdNumber : Number = BindedBdNumber;
        checkFlag(false, flagBdNumber, "Number 3B");
        if (trashBdNumber != 102) println("FAILED Number 4B");
        assertEquals("{trashBdNumber as Number}", "{102 as Number}");
        flagBdNumber = false; // reset flag
    }

    /***** String *****/
    var flagString : Boolean = false as Boolean;
    var flagBdString : Boolean = false as Boolean;

    function fString(x : String) : String{
        flagString = true;
        return "-52";
    }

    var valueString : String = "-50" as String;
    def bindedString7 : String = bind lazy valueString;
    var bindedString6 : String = bind bindedString7;
    def bindedString5 : String = bind lazy bindedString6;
    var bindedString4 : String = bind bindedString5;
    def bindedString3 : String = bind lazy fString(bindedString4);
    var bindedString2 : String = bind lazy bindedString3;
    def bindedString : String = bind lazy bindedString2;

    function testLazyString() {
        checkFlag(false, flagString, "String 1");
        valueString = "-51" as String;
        checkFlag(false, flagString, "String 2");
        var trashString : String = bindedString;
        checkFlag(true, flagString, "String 3");
        if (trashString != "-52") println("FAILED String 4");
        assertEquals("{trashString as String}", "-52");
        flagString = false; // reset flag
    }

    function fBdString(x : String) : String{
        flagBdString = true;
        return "-52";
    }

    var valueBdString : String = "-50" as String;
    def BindedBdString7 : String = bind valueBdString;
    var BindedBdString6 : String = bind BindedBdString7;
    def BindedBdString5 : String = bind BindedBdString6;
    var BindedBdString4 : String = bind BindedBdString5;
    def BindedBdString3 : String = bind fBdString(BindedBdString4);
    var BindedBdString2 : String = bind BindedBdString3;
    def BindedBdString : String = bind BindedBdString2;

    function testBindString() {
        checkFlag(true, flagBdString, "String 1B");
        flagBdString = false; // reset flag
        valueBdString = "-51" as String;
        checkFlag(true, flagBdString, "String 2B");
        flagBdString = false; // reset flag
        var trashBdString : String = BindedBdString;
        checkFlag(false, flagBdString, "String 3B");
        if (trashBdString != "-52") println("FAILED String 4B");
        assertEquals("{trashBdString as String}", "-52");
        flagBdString = false; // reset flag
    }

    /***** Boolean *****/
    var flagBoolean : Boolean = false as Boolean;
    var flagBdBoolean : Boolean = false as Boolean;

    function fBoolean(x : Boolean) : Boolean{
        flagBoolean = true;
        return true;
    }

    var valueBoolean : Boolean = true as Boolean;
    def bindedBoolean7 : Boolean = bind lazy valueBoolean;
    var bindedBoolean6 : Boolean = bind bindedBoolean7;
    def bindedBoolean5 : Boolean = bind lazy bindedBoolean6;
    var bindedBoolean4 : Boolean = bind bindedBoolean5;
    def bindedBoolean3 : Boolean = bind lazy fBoolean(bindedBoolean4);
    var bindedBoolean2 : Boolean = bind lazy bindedBoolean3;
    def bindedBoolean : Boolean = bind lazy bindedBoolean2;

    function testLazyBoolean() {
        checkFlag(false, flagBoolean, "Boolean 1");
        valueBoolean = false as Boolean;
        checkFlag(false, flagBoolean, "Boolean 2");
        var trashBoolean : Boolean = bindedBoolean;
        checkFlag(true, flagBoolean, "Boolean 3");
        if (trashBoolean != true) println("FAILED Boolean 4");
        assertEquals("{trashBoolean as Boolean}", "{true as Boolean}");
        flagBoolean = false; // reset flag
    }

    function fBdBoolean(x : Boolean) : Boolean{
        flagBdBoolean = true;
        return true;
    }

    var valueBdBoolean : Boolean = true as Boolean;
    def BindedBdBoolean7 : Boolean = bind valueBdBoolean;
    var BindedBdBoolean6 : Boolean = bind BindedBdBoolean7;
    def BindedBdBoolean5 : Boolean = bind BindedBdBoolean6;
    var BindedBdBoolean4 : Boolean = bind BindedBdBoolean5;
    def BindedBdBoolean3 : Boolean = bind fBdBoolean(BindedBdBoolean4);
    var BindedBdBoolean2 : Boolean = bind BindedBdBoolean3;
    def BindedBdBoolean : Boolean = bind BindedBdBoolean2;

    function testBindBoolean() {
        checkFlag(true, flagBdBoolean, "Boolean 1B");
        flagBdBoolean = false; // reset flag
        valueBdBoolean = false as Boolean;
        checkFlag(true, flagBdBoolean, "Boolean 2B");
        flagBdBoolean = false; // reset flag
        var trashBdBoolean : Boolean = BindedBdBoolean;
        checkFlag(false, flagBdBoolean, "Boolean 3B");
        if (trashBdBoolean != true) println("FAILED Boolean 4B");
        assertEquals("{trashBdBoolean as Boolean}", "{true as Boolean}");
        flagBdBoolean = false; // reset flag
    }

    /***** Duration *****/
    var flagDuration : Boolean = false as Boolean;
    var flagBdDuration : Boolean = false as Boolean;

    function fDuration(x : Duration) : Duration{
        flagDuration = true;
        return 12s;
    }

    var valueDuration : Duration = 10s as Duration;
    def bindedDuration7 : Duration = bind lazy valueDuration;
    var bindedDuration6 : Duration = bind bindedDuration7;
    def bindedDuration5 : Duration = bind lazy bindedDuration6;
    var bindedDuration4 : Duration = bind bindedDuration5;
    def bindedDuration3 : Duration = bind lazy fDuration(bindedDuration4);
    var bindedDuration2 : Duration = bind lazy bindedDuration3;
    def bindedDuration : Duration = bind lazy bindedDuration2;

    function testLazyDuration() {
        checkFlag(false, flagDuration, "Duration 1");
        valueDuration = 11s as Duration;
        checkFlag(false, flagDuration, "Duration 2");
        var trashDuration : Duration = bindedDuration;
        checkFlag(true, flagDuration, "Duration 3");
        if (trashDuration != 12s) println("FAILED Duration 4");
        assertEquals("{trashDuration as Duration}", "{12s as Duration}");
        flagDuration = false; // reset flag
    }

    function fBdDuration(x : Duration) : Duration{
        flagBdDuration = true;
        return 12s;
    }

    var valueBdDuration : Duration = 10s as Duration;
    def BindedBdDuration7 : Duration = bind valueBdDuration;
    var BindedBdDuration6 : Duration = bind BindedBdDuration7;
    def BindedBdDuration5 : Duration = bind BindedBdDuration6;
    var BindedBdDuration4 : Duration = bind BindedBdDuration5;
    def BindedBdDuration3 : Duration = bind fBdDuration(BindedBdDuration4);
    var BindedBdDuration2 : Duration = bind BindedBdDuration3;
    def BindedBdDuration : Duration = bind BindedBdDuration2;

    function testBindDuration() {
        checkFlag(true, flagBdDuration, "Duration 1B");
        flagBdDuration = false; // reset flag
        valueBdDuration = 11s as Duration;
        checkFlag(true, flagBdDuration, "Duration 2B");
        flagBdDuration = false; // reset flag
        var trashBdDuration : Duration = BindedBdDuration;
        checkFlag(false, flagBdDuration, "Duration 3B");
        if (trashBdDuration != 12s) println("FAILED Duration 4B");
        assertEquals("{trashBdDuration as Duration}", "{12s as Duration}");
        flagBdDuration = false; // reset flag
    }

/********** End: Lazy/eager Binding for class variables **********/

/********** Lazy/eager Binding for variables in a class function **********/
    function testClassFunc() {
        flagByte = false; // reset flag
        var valueFuncByte : Byte = 120 as Byte;
        def bindedFuncByte7 : Byte = bind lazy valueFuncByte;
        var bindedFuncByte6 : Byte = bind bindedFuncByte7;
        def bindedFuncByte5 : Byte = bind lazy bindedFuncByte6;
        var bindedFuncByte4 : Byte = bind bindedFuncByte5;
        def bindedFuncByte3 : Byte = bind lazy fByte(bindedFuncByte4);
        var bindedFuncByte2 : Byte = bind lazy bindedFuncByte3;
        def bindedFuncByte : Byte = bind lazy bindedFuncByte2;
        checkFlag(false, flagByte, "Byte 1f");
        valueFuncByte = 121 as Byte;
        checkFlag(false, flagByte, "Byte 2f");
        var trashFuncByte : Byte = bindedFuncByte;
        checkFlag(true, flagByte, "Byte 3f");
        if (trashFuncByte != 122) println("FAILED Byte 4f");
        assertEquals("{trashFuncByte as Byte}", "{122 as Byte}");
        flagByte = false; // reset flag

        var valueFuncBdByte : Byte = 120 as Byte;
        def bindedFuncBdByte7 : Byte = bind valueFuncBdByte;
        var bindedFuncBdByte6 : Byte = bind bindedFuncBdByte7;
        def bindedFuncBdByte5 : Byte = bind bindedFuncBdByte6;
        var bindedFuncBdByte4 : Byte = bind bindedFuncBdByte5;
        def bindedFuncBdByte3 : Byte = bind fByte(bindedFuncBdByte4);
        var bindedFuncBdByte2 : Byte = bind bindedFuncBdByte3;
        def bindedFuncBdByte : Byte = bind bindedFuncBdByte2;
        checkFlag(true, flagByte, "Byte 1Bf");
        flagByte = false; // reset flag
        valueFuncBdByte = 121 as Byte;
        checkFlag(true, flagByte, "Byte 2Bf");
        flagByte = false; // reset flag
        var trashFuncBdByte : Byte = bindedFuncBdByte;
        checkFlag(false, flagByte, "Byte 3Bf");
        if (trashFuncBdByte != 122) println("FAILED Byte 4Bf");
        assertEquals("{trashFuncBdByte as Byte}", "{122 as Byte}");
        flagByte = false; // reset flag

        flagShort = false; // reset flag
        var valueFuncShort : Short = 30000 as Short;
        def bindedFuncShort7 : Short = bind lazy valueFuncShort;
        var bindedFuncShort6 : Short = bind bindedFuncShort7;
        def bindedFuncShort5 : Short = bind lazy bindedFuncShort6;
        var bindedFuncShort4 : Short = bind bindedFuncShort5;
        def bindedFuncShort3 : Short = bind lazy fShort(bindedFuncShort4);
        var bindedFuncShort2 : Short = bind lazy bindedFuncShort3;
        def bindedFuncShort : Short = bind lazy bindedFuncShort2;
        checkFlag(false, flagShort, "Short 1f");
        valueFuncShort = 30001 as Short;
        checkFlag(false, flagShort, "Short 2f");
        var trashFuncShort : Short = bindedFuncShort;
        checkFlag(true, flagShort, "Short 3f");
        if (trashFuncShort != 30002) println("FAILED Short 4f");
        assertEquals("{trashFuncShort as Short}", "{30002 as Short}");
        flagShort = false; // reset flag

        var valueFuncBdShort : Short = 30000 as Short;
        def bindedFuncBdShort7 : Short = bind valueFuncBdShort;
        var bindedFuncBdShort6 : Short = bind bindedFuncBdShort7;
        def bindedFuncBdShort5 : Short = bind bindedFuncBdShort6;
        var bindedFuncBdShort4 : Short = bind bindedFuncBdShort5;
        def bindedFuncBdShort3 : Short = bind fShort(bindedFuncBdShort4);
        var bindedFuncBdShort2 : Short = bind bindedFuncBdShort3;
        def bindedFuncBdShort : Short = bind bindedFuncBdShort2;
        checkFlag(true, flagShort, "Short 1Bf");
        flagShort = false; // reset flag
        valueFuncBdShort = 30001 as Short;
        checkFlag(true, flagShort, "Short 2Bf");
        flagShort = false; // reset flag
        var trashFuncBdShort : Short = bindedFuncBdShort;
        checkFlag(false, flagShort, "Short 3Bf");
        if (trashFuncBdShort != 30002) println("FAILED Short 4Bf");
        assertEquals("{trashFuncBdShort as Short}", "{30002 as Short}");
        flagShort = false; // reset flag

        flagCharacter = false; // reset flag
        var valueFuncCharacter : Character = 64 as Character;
        def bindedFuncCharacter7 : Character = bind lazy valueFuncCharacter;
        var bindedFuncCharacter6 : Character = bind bindedFuncCharacter7;
        def bindedFuncCharacter5 : Character = bind lazy bindedFuncCharacter6;
        var bindedFuncCharacter4 : Character = bind bindedFuncCharacter5;
        def bindedFuncCharacter3 : Character = bind lazy fCharacter(bindedFuncCharacter4);
        var bindedFuncCharacter2 : Character = bind lazy bindedFuncCharacter3;
        def bindedFuncCharacter : Character = bind lazy bindedFuncCharacter2;
        checkFlag(false, flagCharacter, "Character 1f");
        valueFuncCharacter = 61 as Character;
        checkFlag(false, flagCharacter, "Character 2f");
        var trashFuncCharacter : Character = bindedFuncCharacter;
        checkFlag(true, flagCharacter, "Character 3f");
        if (trashFuncCharacter != 62) println("FAILED Character 4f");
        assertEquals("{trashFuncCharacter as Character}", "{62 as Character}");
        flagCharacter = false; // reset flag

        var valueFuncBdCharacter : Character = 64 as Character;
        def bindedFuncBdCharacter7 : Character = bind valueFuncBdCharacter;
        var bindedFuncBdCharacter6 : Character = bind bindedFuncBdCharacter7;
        def bindedFuncBdCharacter5 : Character = bind bindedFuncBdCharacter6;
        var bindedFuncBdCharacter4 : Character = bind bindedFuncBdCharacter5;
        def bindedFuncBdCharacter3 : Character = bind fCharacter(bindedFuncBdCharacter4);
        var bindedFuncBdCharacter2 : Character = bind bindedFuncBdCharacter3;
        def bindedFuncBdCharacter : Character = bind bindedFuncBdCharacter2;
        checkFlag(true, flagCharacter, "Character 1Bf");
        flagCharacter = false; // reset flag
        valueFuncBdCharacter = 61 as Character;
        checkFlag(true, flagCharacter, "Character 2Bf");
        flagCharacter = false; // reset flag
        var trashFuncBdCharacter : Character = bindedFuncBdCharacter;
        checkFlag(false, flagCharacter, "Character 3Bf");
        if (trashFuncBdCharacter != 62) println("FAILED Character 4Bf");
        assertEquals("{trashFuncBdCharacter as Character}", "{62 as Character}");
        flagCharacter = false; // reset flag

        flagInteger = false; // reset flag
        var valueFuncInteger : Integer = 1000000 as Integer;
        def bindedFuncInteger7 : Integer = bind lazy valueFuncInteger;
        var bindedFuncInteger6 : Integer = bind bindedFuncInteger7;
        def bindedFuncInteger5 : Integer = bind lazy bindedFuncInteger6;
        var bindedFuncInteger4 : Integer = bind bindedFuncInteger5;
        def bindedFuncInteger3 : Integer = bind lazy fInteger(bindedFuncInteger4);
        var bindedFuncInteger2 : Integer = bind lazy bindedFuncInteger3;
        def bindedFuncInteger : Integer = bind lazy bindedFuncInteger2;
        checkFlag(false, flagInteger, "Integer 1f");
        valueFuncInteger = 1000001 as Integer;
        checkFlag(false, flagInteger, "Integer 2f");
        var trashFuncInteger : Integer = bindedFuncInteger;
        checkFlag(true, flagInteger, "Integer 3f");
        if (trashFuncInteger != 1000002) println("FAILED Integer 4f");
        assertEquals("{trashFuncInteger as Integer}", "{1000002 as Integer}");
        flagInteger = false; // reset flag

        var valueFuncBdInteger : Integer = 1000000 as Integer;
        def bindedFuncBdInteger7 : Integer = bind valueFuncBdInteger;
        var bindedFuncBdInteger6 : Integer = bind bindedFuncBdInteger7;
        def bindedFuncBdInteger5 : Integer = bind bindedFuncBdInteger6;
        var bindedFuncBdInteger4 : Integer = bind bindedFuncBdInteger5;
        def bindedFuncBdInteger3 : Integer = bind fInteger(bindedFuncBdInteger4);
        var bindedFuncBdInteger2 : Integer = bind bindedFuncBdInteger3;
        def bindedFuncBdInteger : Integer = bind bindedFuncBdInteger2;
        checkFlag(true, flagInteger, "Integer 1Bf");
        flagInteger = false; // reset flag
        valueFuncBdInteger = 1000001 as Integer;
        checkFlag(true, flagInteger, "Integer 2Bf");
        flagInteger = false; // reset flag
        var trashFuncBdInteger : Integer = bindedFuncBdInteger;
        checkFlag(false, flagInteger, "Integer 3Bf");
        if (trashFuncBdInteger != 1000002) println("FAILED Integer 4Bf");
        assertEquals("{trashFuncBdInteger as Integer}", "{1000002 as Integer}");
        flagInteger = false; // reset flag

        flagLong = false; // reset flag
        var valueFuncLong : Long = 1000000000 as Long;
        def bindedFuncLong7 : Long = bind lazy valueFuncLong;
        var bindedFuncLong6 : Long = bind bindedFuncLong7;
        def bindedFuncLong5 : Long = bind lazy bindedFuncLong6;
        var bindedFuncLong4 : Long = bind bindedFuncLong5;
        def bindedFuncLong3 : Long = bind lazy fLong(bindedFuncLong4);
        var bindedFuncLong2 : Long = bind lazy bindedFuncLong3;
        def bindedFuncLong : Long = bind lazy bindedFuncLong2;
        checkFlag(false, flagLong, "Long 1f");
        valueFuncLong = 1000000001 as Long;
        checkFlag(false, flagLong, "Long 2f");
        var trashFuncLong : Long = bindedFuncLong;
        checkFlag(true, flagLong, "Long 3f");
        if (trashFuncLong != 1000000002) println("FAILED Long 4f");
        assertEquals("{trashFuncLong as Long}", "{1000000002 as Long}");
        flagLong = false; // reset flag

        var valueFuncBdLong : Long = 1000000000 as Long;
        def bindedFuncBdLong7 : Long = bind valueFuncBdLong;
        var bindedFuncBdLong6 : Long = bind bindedFuncBdLong7;
        def bindedFuncBdLong5 : Long = bind bindedFuncBdLong6;
        var bindedFuncBdLong4 : Long = bind bindedFuncBdLong5;
        def bindedFuncBdLong3 : Long = bind fLong(bindedFuncBdLong4);
        var bindedFuncBdLong2 : Long = bind bindedFuncBdLong3;
        def bindedFuncBdLong : Long = bind bindedFuncBdLong2;
        checkFlag(true, flagLong, "Long 1Bf");
        flagLong = false; // reset flag
        valueFuncBdLong = 1000000001 as Long;
        checkFlag(true, flagLong, "Long 2Bf");
        flagLong = false; // reset flag
        var trashFuncBdLong : Long = bindedFuncBdLong;
        checkFlag(false, flagLong, "Long 3Bf");
        if (trashFuncBdLong != 1000000002) println("FAILED Long 4Bf");
        assertEquals("{trashFuncBdLong as Long}", "{1000000002 as Long}");
        flagLong = false; // reset flag

        flagFloat = false; // reset flag
        var valueFuncFloat : Float = 10.5 as Float;
        def bindedFuncFloat7 : Float = bind lazy valueFuncFloat;
        var bindedFuncFloat6 : Float = bind bindedFuncFloat7;
        def bindedFuncFloat5 : Float = bind lazy bindedFuncFloat6;
        var bindedFuncFloat4 : Float = bind bindedFuncFloat5;
        def bindedFuncFloat3 : Float = bind lazy fFloat(bindedFuncFloat4);
        var bindedFuncFloat2 : Float = bind lazy bindedFuncFloat3;
        def bindedFuncFloat : Float = bind lazy bindedFuncFloat2;
        checkFlag(false, flagFloat, "Float 1f");
        valueFuncFloat = 11.5 as Float;
        checkFlag(false, flagFloat, "Float 2f");
        var trashFuncFloat : Float = bindedFuncFloat;
        checkFlag(true, flagFloat, "Float 3f");
        if (trashFuncFloat != 12.5) println("FAILED Float 4f");
        assertEquals("{trashFuncFloat as Float}", "{12.5 as Float}");
        flagFloat = false; // reset flag

        var valueFuncBdFloat : Float = 10.5 as Float;
        def bindedFuncBdFloat7 : Float = bind valueFuncBdFloat;
        var bindedFuncBdFloat6 : Float = bind bindedFuncBdFloat7;
        def bindedFuncBdFloat5 : Float = bind bindedFuncBdFloat6;
        var bindedFuncBdFloat4 : Float = bind bindedFuncBdFloat5;
        def bindedFuncBdFloat3 : Float = bind fFloat(bindedFuncBdFloat4);
        var bindedFuncBdFloat2 : Float = bind bindedFuncBdFloat3;
        def bindedFuncBdFloat : Float = bind bindedFuncBdFloat2;
        checkFlag(true, flagFloat, "Float 1Bf");
        flagFloat = false; // reset flag
        valueFuncBdFloat = 11.5 as Float;
        checkFlag(true, flagFloat, "Float 2Bf");
        flagFloat = false; // reset flag
        var trashFuncBdFloat : Float = bindedFuncBdFloat;
        checkFlag(false, flagFloat, "Float 3Bf");
        if (trashFuncBdFloat != 12.5) println("FAILED Float 4Bf");
        assertEquals("{trashFuncBdFloat as Float}", "{12.5 as Float}");
        flagFloat = false; // reset flag

        flagDouble = false; // reset flag
        var valueFuncDouble : Double = 1.25e4 as Double;
        def bindedFuncDouble7 : Double = bind lazy valueFuncDouble;
        var bindedFuncDouble6 : Double = bind bindedFuncDouble7;
        def bindedFuncDouble5 : Double = bind lazy bindedFuncDouble6;
        var bindedFuncDouble4 : Double = bind bindedFuncDouble5;
        def bindedFuncDouble3 : Double = bind lazy fDouble(bindedFuncDouble4);
        var bindedFuncDouble2 : Double = bind lazy bindedFuncDouble3;
        def bindedFuncDouble : Double = bind lazy bindedFuncDouble2;
        checkFlag(false, flagDouble, "Double 1f");
        valueFuncDouble = 1.21e4 as Double;
        checkFlag(false, flagDouble, "Double 2f");
        var trashFuncDouble : Double = bindedFuncDouble;
        checkFlag(true, flagDouble, "Double 3f");
        if (trashFuncDouble != 1.22e4) println("FAILED Double 4f");
        assertEquals("{trashFuncDouble as Double}", "{1.22e4 as Double}");
        flagDouble = false; // reset flag

        var valueFuncBdDouble : Double = 1.25e4 as Double;
        def bindedFuncBdDouble7 : Double = bind valueFuncBdDouble;
        var bindedFuncBdDouble6 : Double = bind bindedFuncBdDouble7;
        def bindedFuncBdDouble5 : Double = bind bindedFuncBdDouble6;
        var bindedFuncBdDouble4 : Double = bind bindedFuncBdDouble5;
        def bindedFuncBdDouble3 : Double = bind fDouble(bindedFuncBdDouble4);
        var bindedFuncBdDouble2 : Double = bind bindedFuncBdDouble3;
        def bindedFuncBdDouble : Double = bind bindedFuncBdDouble2;
        checkFlag(true, flagDouble, "Double 1Bf");
        flagDouble = false; // reset flag
        valueFuncBdDouble = 1.21e4 as Double;
        checkFlag(true, flagDouble, "Double 2Bf");
        flagDouble = false; // reset flag
        var trashFuncBdDouble : Double = bindedFuncBdDouble;
        checkFlag(false, flagDouble, "Double 3Bf");
        if (trashFuncBdDouble != 1.22e4) println("FAILED Double 4Bf");
        assertEquals("{trashFuncBdDouble as Double}", "{1.22e4 as Double}");
        flagDouble = false; // reset flag

        flagNumber = false; // reset flag
        var valueFuncNumber : Number = 100 as Number;
        def bindedFuncNumber7 : Number = bind lazy valueFuncNumber;
        var bindedFuncNumber6 : Number = bind bindedFuncNumber7;
        def bindedFuncNumber5 : Number = bind lazy bindedFuncNumber6;
        var bindedFuncNumber4 : Number = bind bindedFuncNumber5;
        def bindedFuncNumber3 : Number = bind lazy fNumber(bindedFuncNumber4);
        var bindedFuncNumber2 : Number = bind lazy bindedFuncNumber3;
        def bindedFuncNumber : Number = bind lazy bindedFuncNumber2;
        checkFlag(false, flagNumber, "Number 1f");
        valueFuncNumber = 101 as Number;
        checkFlag(false, flagNumber, "Number 2f");
        var trashFuncNumber : Number = bindedFuncNumber;
        checkFlag(true, flagNumber, "Number 3f");
        if (trashFuncNumber != 102) println("FAILED Number 4f");
        assertEquals("{trashFuncNumber as Number}", "{102 as Number}");
        flagNumber = false; // reset flag

        var valueFuncBdNumber : Number = 100 as Number;
        def bindedFuncBdNumber7 : Number = bind valueFuncBdNumber;
        var bindedFuncBdNumber6 : Number = bind bindedFuncBdNumber7;
        def bindedFuncBdNumber5 : Number = bind bindedFuncBdNumber6;
        var bindedFuncBdNumber4 : Number = bind bindedFuncBdNumber5;
        def bindedFuncBdNumber3 : Number = bind fNumber(bindedFuncBdNumber4);
        var bindedFuncBdNumber2 : Number = bind bindedFuncBdNumber3;
        def bindedFuncBdNumber : Number = bind bindedFuncBdNumber2;
        checkFlag(true, flagNumber, "Number 1Bf");
        flagNumber = false; // reset flag
        valueFuncBdNumber = 101 as Number;
        checkFlag(true, flagNumber, "Number 2Bf");
        flagNumber = false; // reset flag
        var trashFuncBdNumber : Number = bindedFuncBdNumber;
        checkFlag(false, flagNumber, "Number 3Bf");
        if (trashFuncBdNumber != 102) println("FAILED Number 4Bf");
        assertEquals("{trashFuncBdNumber as Number}", "{102 as Number}");
        flagNumber = false; // reset flag

        flagString = false; // reset flag
        var valueFuncString : String = "-50" as String;
        def bindedFuncString7 : String = bind lazy valueFuncString;
        var bindedFuncString6 : String = bind bindedFuncString7;
        def bindedFuncString5 : String = bind lazy bindedFuncString6;
        var bindedFuncString4 : String = bind bindedFuncString5;
        def bindedFuncString3 : String = bind lazy fString(bindedFuncString4);
        var bindedFuncString2 : String = bind lazy bindedFuncString3;
        def bindedFuncString : String = bind lazy bindedFuncString2;
        checkFlag(false, flagString, "String 1f");
        valueFuncString = "-51" as String;
        checkFlag(false, flagString, "String 2f");
        var trashFuncString : String = bindedFuncString;
        checkFlag(true, flagString, "String 3f");
        if (trashFuncString != "-52") println("FAILED String 4f");
        assertEquals("{trashFuncString as String}", "-52");
        flagString = false; // reset flag

        var valueFuncBdString : String = "-50" as String;
        def bindedFuncBdString7 : String = bind valueFuncBdString;
        var bindedFuncBdString6 : String = bind bindedFuncBdString7;
        def bindedFuncBdString5 : String = bind bindedFuncBdString6;
        var bindedFuncBdString4 : String = bind bindedFuncBdString5;
        def bindedFuncBdString3 : String = bind fString(bindedFuncBdString4);
        var bindedFuncBdString2 : String = bind bindedFuncBdString3;
        def bindedFuncBdString : String = bind bindedFuncBdString2;
        checkFlag(true, flagString, "String 1Bf");
        flagString = false; // reset flag
        valueFuncBdString = "-51" as String;
        checkFlag(true, flagString, "String 2Bf");
        flagString = false; // reset flag
        var trashFuncBdString : String = bindedFuncBdString;
        checkFlag(false, flagString, "String 3Bf");
        if (trashFuncBdString != "-52") println("FAILED String 4Bf");
        assertEquals("{trashFuncBdString as String}", "-52");
        flagString = false; // reset flag

        flagBoolean = false; // reset flag
        var valueFuncBoolean : Boolean = true as Boolean;
        def bindedFuncBoolean7 : Boolean = bind lazy valueFuncBoolean;
        var bindedFuncBoolean6 : Boolean = bind bindedFuncBoolean7;
        def bindedFuncBoolean5 : Boolean = bind lazy bindedFuncBoolean6;
        var bindedFuncBoolean4 : Boolean = bind bindedFuncBoolean5;
        def bindedFuncBoolean3 : Boolean = bind lazy fBoolean(bindedFuncBoolean4);
        var bindedFuncBoolean2 : Boolean = bind lazy bindedFuncBoolean3;
        def bindedFuncBoolean : Boolean = bind lazy bindedFuncBoolean2;
        checkFlag(false, flagBoolean, "Boolean 1f");
        valueFuncBoolean = false as Boolean;
        checkFlag(false, flagBoolean, "Boolean 2f");
        var trashFuncBoolean : Boolean = bindedFuncBoolean;
        checkFlag(true, flagBoolean, "Boolean 3f");
        if (trashFuncBoolean != true) println("FAILED Boolean 4f");
        assertEquals("{trashFuncBoolean as Boolean}", "{true as Boolean}");
        flagBoolean = false; // reset flag

        var valueFuncBdBoolean : Boolean = true as Boolean;
        def bindedFuncBdBoolean7 : Boolean = bind valueFuncBdBoolean;
        var bindedFuncBdBoolean6 : Boolean = bind bindedFuncBdBoolean7;
        def bindedFuncBdBoolean5 : Boolean = bind bindedFuncBdBoolean6;
        var bindedFuncBdBoolean4 : Boolean = bind bindedFuncBdBoolean5;
        def bindedFuncBdBoolean3 : Boolean = bind fBoolean(bindedFuncBdBoolean4);
        var bindedFuncBdBoolean2 : Boolean = bind bindedFuncBdBoolean3;
        def bindedFuncBdBoolean : Boolean = bind bindedFuncBdBoolean2;
        checkFlag(true, flagBoolean, "Boolean 1Bf");
        flagBoolean = false; // reset flag
        valueFuncBdBoolean = false as Boolean;
        checkFlag(true, flagBoolean, "Boolean 2Bf");
        flagBoolean = false; // reset flag
        var trashFuncBdBoolean : Boolean = bindedFuncBdBoolean;
        checkFlag(false, flagBoolean, "Boolean 3Bf");
        if (trashFuncBdBoolean != true) println("FAILED Boolean 4Bf");
        assertEquals("{trashFuncBdBoolean as Boolean}", "{true as Boolean}");
        flagBoolean = false; // reset flag

        flagDuration = false; // reset flag
        var valueFuncDuration : Duration = 10s as Duration;
        def bindedFuncDuration7 : Duration = bind lazy valueFuncDuration;
        var bindedFuncDuration6 : Duration = bind bindedFuncDuration7;
        def bindedFuncDuration5 : Duration = bind lazy bindedFuncDuration6;
        var bindedFuncDuration4 : Duration = bind bindedFuncDuration5;
        def bindedFuncDuration3 : Duration = bind lazy fDuration(bindedFuncDuration4);
        var bindedFuncDuration2 : Duration = bind lazy bindedFuncDuration3;
        def bindedFuncDuration : Duration = bind lazy bindedFuncDuration2;
        checkFlag(false, flagDuration, "Duration 1f");
        valueFuncDuration = 11s as Duration;
        checkFlag(false, flagDuration, "Duration 2f");
        var trashFuncDuration : Duration = bindedFuncDuration;
        checkFlag(true, flagDuration, "Duration 3f");
        if (trashFuncDuration != 12s) println("FAILED Duration 4f");
        assertEquals("{trashFuncDuration as Duration}", "{12s as Duration}");
        flagDuration = false; // reset flag

        var valueFuncBdDuration : Duration = 10s as Duration;
        def bindedFuncBdDuration7 : Duration = bind valueFuncBdDuration;
        var bindedFuncBdDuration6 : Duration = bind bindedFuncBdDuration7;
        def bindedFuncBdDuration5 : Duration = bind bindedFuncBdDuration6;
        var bindedFuncBdDuration4 : Duration = bind bindedFuncBdDuration5;
        def bindedFuncBdDuration3 : Duration = bind fDuration(bindedFuncBdDuration4);
        var bindedFuncBdDuration2 : Duration = bind bindedFuncBdDuration3;
        def bindedFuncBdDuration : Duration = bind bindedFuncBdDuration2;
        checkFlag(true, flagDuration, "Duration 1Bf");
        flagDuration = false; // reset flag
        valueFuncBdDuration = 11s as Duration;
        checkFlag(true, flagDuration, "Duration 2Bf");
        flagDuration = false; // reset flag
        var trashFuncBdDuration : Duration = bindedFuncBdDuration;
        checkFlag(false, flagDuration, "Duration 3Bf");
        if (trashFuncBdDuration != 12s) println("FAILED Duration 4Bf");
        assertEquals("{trashFuncBdDuration as Duration}", "{12s as Duration}");
        flagDuration = false; // reset flag

    }
/********** END: Lazy/eager Binding for variables in a class function **********/

}
