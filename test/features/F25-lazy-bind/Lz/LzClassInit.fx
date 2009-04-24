/*
 * Lazy/eager Binding for variables in a class init
 *
 * @test/fxunit
 * @run
 */

import javafx.fxunit.FXTestCase;

public class LzClassInit extends FXTestCase {
    function checkFlag (expected : Boolean, current : Boolean, msg : String) {
        if (expected != current) println("FAILED {msg}");
        assertEquals("{expected as Boolean}", "{current as Boolean}");
    }

    var flagByte : Boolean = false as Boolean;
    var flagBdByte : Boolean = false as Boolean;

    function fByte(x : Byte) : Byte{
        flagByte = true;
        return 122;
    }

    var flagShort : Boolean = false as Boolean;
    var flagBdShort : Boolean = false as Boolean;

    function fShort(x : Short) : Short{
        flagShort = true;
        return 30002;
    }

    var flagCharacter : Boolean = false as Boolean;
    var flagBdCharacter : Boolean = false as Boolean;

    function fCharacter(x : Character) : Character{
        flagCharacter = true;
        return 62;
    }

    var flagInteger : Boolean = false as Boolean;
    var flagBdInteger : Boolean = false as Boolean;

    function fInteger(x : Integer) : Integer{
        flagInteger = true;
        return 1000002;
    }

    var flagLong : Boolean = false as Boolean;
    var flagBdLong : Boolean = false as Boolean;

    function fLong(x : Long) : Long{
        flagLong = true;
        return 1000000002;
    }

    var flagFloat : Boolean = false as Boolean;
    var flagBdFloat : Boolean = false as Boolean;

    function fFloat(x : Float) : Float{
        flagFloat = true;
        return 12.5;
    }

    var flagDouble : Boolean = false as Boolean;
    var flagBdDouble : Boolean = false as Boolean;

    function fDouble(x : Double) : Double{
        flagDouble = true;
        return 1.22e4;
    }

    var flagNumber : Boolean = false as Boolean;
    var flagBdNumber : Boolean = false as Boolean;

    function fNumber(x : Number) : Number{
        flagNumber = true;
        return 102;
    }

    var flagString : Boolean = false as Boolean;
    var flagBdString : Boolean = false as Boolean;

    function fString(x : String) : String{
        flagString = true;
        return "-52";
    }

    var flagBoolean : Boolean = false as Boolean;
    var flagBdBoolean : Boolean = false as Boolean;

    function fBoolean(x : Boolean) : Boolean{
        flagBoolean = true;
        return true;
    }

    var flagDuration : Boolean = false as Boolean;
    var flagBdDuration : Boolean = false as Boolean;

    function fDuration(x : Duration) : Duration{
        flagDuration = true;
        return 12s;
    }


    init {
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

    function testClassInit() {}

}
