/*
 * Lazy/eager binding for global variables
 *
 * @test
 * @run
 */

/** flag **/
var flag : Boolean = false as Boolean;
function checkFlag (expected : Boolean, msg : String) {
    if (expected != flag) println("FAILED {msg}");
}
function resetFlag() {
    flag = false;
}
/** flag **/


/********** Lazy/eager Binding for global variables **********/


/***** Byte *****/
function fByte(x : Byte) : Byte{
    flag = true;
    return 122;
}

println("Byte lazy+eager");
var valueByte : Byte = 120 as Byte;
def bindedByte7 : Byte = bind lazy valueByte;
var bindedByte6 : Byte = bind bindedByte7;
def bindedByte5 : Byte = bind lazy bindedByte6;
var bindedByte4 : Byte = bind bindedByte5;
def bindedByte3 : Byte = bind lazy fByte(bindedByte4);
var bindedByte2 : Byte = bind lazy bindedByte3;
def bindedByte : Byte = bind lazy bindedByte2;
checkFlag(false, "Byte 1");
valueByte = 121 as Byte;
checkFlag(false, "Byte 2");
var trashByte : Byte = bindedByte;
checkFlag(true, "Byte 3");
if (trashByte != 122) println("FAILED Byte 4");
resetFlag();

println("Byte eager");
var valueBByte : Byte = 120 as Byte;
def bindedBByte7 : Byte = bind valueBByte;
var bindedBByte6 : Byte = bind bindedBByte7;
def bindedBByte5 : Byte = bind bindedBByte6;
var bindedBByte4 : Byte = bind bindedBByte5;
def bindedBByte3 : Byte = bind fByte(bindedBByte4);
var bindedBByte2 : Byte = bind bindedBByte3;
def bindedBByte : Byte = bind bindedBByte2;
checkFlag(true, "Byte 1B");
resetFlag();
valueBByte = 121 as Byte;
checkFlag(true, "Byte 2B");
resetFlag();
var trashBByte : Byte = bindedBByte;
checkFlag(false, "Byte 3B");
if (trashBByte != 122) println("FAILED Byte 4B");
resetFlag();

/***** Short *****/
function fShort(x : Short) : Short{
    flag = true;
    return 30002;
}

println("Short lazy+eager");
var valueShort : Short = 30000 as Short;
def bindedShort7 : Short = bind lazy valueShort;
var bindedShort6 : Short = bind bindedShort7;
def bindedShort5 : Short = bind lazy bindedShort6;
var bindedShort4 : Short = bind bindedShort5;
def bindedShort3 : Short = bind lazy fShort(bindedShort4);
var bindedShort2 : Short = bind lazy bindedShort3;
def bindedShort : Short = bind lazy bindedShort2;
checkFlag(false, "Short 1");
valueShort = 30001 as Short;
checkFlag(false, "Short 2");
var trashShort : Short = bindedShort;
checkFlag(true, "Short 3");
if (trashShort != 30002) println("FAILED Short 4");
resetFlag();

println("Short eager");
var valueBShort : Short = 30000 as Short;
def bindedBShort7 : Short = bind valueBShort;
var bindedBShort6 : Short = bind bindedBShort7;
def bindedBShort5 : Short = bind bindedBShort6;
var bindedBShort4 : Short = bind bindedBShort5;
def bindedBShort3 : Short = bind fShort(bindedBShort4);
var bindedBShort2 : Short = bind bindedBShort3;
def bindedBShort : Short = bind bindedBShort2;
checkFlag(true, "Short 1B");
resetFlag();
valueBShort = 30001 as Short;
checkFlag(true, "Short 2B");
resetFlag();
var trashBShort : Short = bindedBShort;
checkFlag(false, "Short 3B");
if (trashBShort != 30002) println("FAILED Short 4B");
resetFlag();

/***** Character *****/
function fCharacter(x : Character) : Character{
    flag = true;
    return 62;
}

println("Character lazy+eager");
var valueCharacter : Character = 64 as Character;
def bindedCharacter7 : Character = bind lazy valueCharacter;
var bindedCharacter6 : Character = bind bindedCharacter7;
def bindedCharacter5 : Character = bind lazy bindedCharacter6;
var bindedCharacter4 : Character = bind bindedCharacter5;
def bindedCharacter3 : Character = bind lazy fCharacter(bindedCharacter4);
var bindedCharacter2 : Character = bind lazy bindedCharacter3;
def bindedCharacter : Character = bind lazy bindedCharacter2;
checkFlag(false, "Character 1");
valueCharacter = 61 as Character;
checkFlag(false, "Character 2");
var trashCharacter : Character = bindedCharacter;
checkFlag(true, "Character 3");
if (trashCharacter != 62) println("FAILED Character 4");
resetFlag();

println("Character eager");
var valueBCharacter : Character = 64 as Character;
def bindedBCharacter7 : Character = bind valueBCharacter;
var bindedBCharacter6 : Character = bind bindedBCharacter7;
def bindedBCharacter5 : Character = bind bindedBCharacter6;
var bindedBCharacter4 : Character = bind bindedBCharacter5;
def bindedBCharacter3 : Character = bind fCharacter(bindedBCharacter4);
var bindedBCharacter2 : Character = bind bindedBCharacter3;
def bindedBCharacter : Character = bind bindedBCharacter2;
checkFlag(true, "Character 1B");
resetFlag();
valueBCharacter = 61 as Character;
checkFlag(true, "Character 2B");
resetFlag();
var trashBCharacter : Character = bindedBCharacter;
checkFlag(false, "Character 3B");
if (trashBCharacter != 62) println("FAILED Character 4B");
resetFlag();

/***** Integer *****/
function fInteger(x : Integer) : Integer{
    flag = true;
    return 1000002;
}

println("Integer lazy+eager");
var valueInteger : Integer = 1000000 as Integer;
def bindedInteger7 : Integer = bind lazy valueInteger;
var bindedInteger6 : Integer = bind bindedInteger7;
def bindedInteger5 : Integer = bind lazy bindedInteger6;
var bindedInteger4 : Integer = bind bindedInteger5;
def bindedInteger3 : Integer = bind lazy fInteger(bindedInteger4);
var bindedInteger2 : Integer = bind lazy bindedInteger3;
def bindedInteger : Integer = bind lazy bindedInteger2;
checkFlag(false, "Integer 1");
valueInteger = 1000001 as Integer;
checkFlag(false, "Integer 2");
var trashInteger : Integer = bindedInteger;
checkFlag(true, "Integer 3");
if (trashInteger != 1000002) println("FAILED Integer 4");
resetFlag();

println("Integer eager");
var valueBInteger : Integer = 1000000 as Integer;
def bindedBInteger7 : Integer = bind valueBInteger;
var bindedBInteger6 : Integer = bind bindedBInteger7;
def bindedBInteger5 : Integer = bind bindedBInteger6;
var bindedBInteger4 : Integer = bind bindedBInteger5;
def bindedBInteger3 : Integer = bind fInteger(bindedBInteger4);
var bindedBInteger2 : Integer = bind bindedBInteger3;
def bindedBInteger : Integer = bind bindedBInteger2;
checkFlag(true, "Integer 1B");
resetFlag();
valueBInteger = 1000001 as Integer;
checkFlag(true, "Integer 2B");
resetFlag();
var trashBInteger : Integer = bindedBInteger;
checkFlag(false, "Integer 3B");
if (trashBInteger != 1000002) println("FAILED Integer 4B");
resetFlag();

/***** Long *****/
function fLong(x : Long) : Long{
    flag = true;
    return 1000000002;
}

println("Long lazy+eager");
var valueLong : Long = 1000000000 as Long;
def bindedLong7 : Long = bind lazy valueLong;
var bindedLong6 : Long = bind bindedLong7;
def bindedLong5 : Long = bind lazy bindedLong6;
var bindedLong4 : Long = bind bindedLong5;
def bindedLong3 : Long = bind lazy fLong(bindedLong4);
var bindedLong2 : Long = bind lazy bindedLong3;
def bindedLong : Long = bind lazy bindedLong2;
checkFlag(false, "Long 1");
valueLong = 1000000001 as Long;
checkFlag(false, "Long 2");
var trashLong : Long = bindedLong;
checkFlag(true, "Long 3");
if (trashLong != 1000000002) println("FAILED Long 4");
resetFlag();

println("Long eager");
var valueBLong : Long = 1000000000 as Long;
def bindedBLong7 : Long = bind valueBLong;
var bindedBLong6 : Long = bind bindedBLong7;
def bindedBLong5 : Long = bind bindedBLong6;
var bindedBLong4 : Long = bind bindedBLong5;
def bindedBLong3 : Long = bind fLong(bindedBLong4);
var bindedBLong2 : Long = bind bindedBLong3;
def bindedBLong : Long = bind bindedBLong2;
checkFlag(true, "Long 1B");
resetFlag();
valueBLong = 1000000001 as Long;
checkFlag(true, "Long 2B");
resetFlag();
var trashBLong : Long = bindedBLong;
checkFlag(false, "Long 3B");
if (trashBLong != 1000000002) println("FAILED Long 4B");
resetFlag();

/***** Float *****/
function fFloat(x : Float) : Float{
    flag = true;
    return 12.5;
}

println("Float lazy+eager");
var valueFloat : Float = 10.5 as Float;
def bindedFloat7 : Float = bind lazy valueFloat;
var bindedFloat6 : Float = bind bindedFloat7;
def bindedFloat5 : Float = bind lazy bindedFloat6;
var bindedFloat4 : Float = bind bindedFloat5;
def bindedFloat3 : Float = bind lazy fFloat(bindedFloat4);
var bindedFloat2 : Float = bind lazy bindedFloat3;
def bindedFloat : Float = bind lazy bindedFloat2;
checkFlag(false, "Float 1");
valueFloat = 11.5 as Float;
checkFlag(false, "Float 2");
var trashFloat : Float = bindedFloat;
checkFlag(true, "Float 3");
if (trashFloat != 12.5) println("FAILED Float 4");
resetFlag();

println("Float eager");
var valueBFloat : Float = 10.5 as Float;
def bindedBFloat7 : Float = bind valueBFloat;
var bindedBFloat6 : Float = bind bindedBFloat7;
def bindedBFloat5 : Float = bind bindedBFloat6;
var bindedBFloat4 : Float = bind bindedBFloat5;
def bindedBFloat3 : Float = bind fFloat(bindedBFloat4);
var bindedBFloat2 : Float = bind bindedBFloat3;
def bindedBFloat : Float = bind bindedBFloat2;
checkFlag(true, "Float 1B");
resetFlag();
valueBFloat = 11.5 as Float;
checkFlag(true, "Float 2B");
resetFlag();
var trashBFloat : Float = bindedBFloat;
checkFlag(false, "Float 3B");
if (trashBFloat != 12.5) println("FAILED Float 4B");
resetFlag();

/***** Double *****/
function fDouble(x : Double) : Double{
    flag = true;
    return 1.22e4;
}

println("Double lazy+eager");
var valueDouble : Double = 1.25e4 as Double;
def bindedDouble7 : Double = bind lazy valueDouble;
var bindedDouble6 : Double = bind bindedDouble7;
def bindedDouble5 : Double = bind lazy bindedDouble6;
var bindedDouble4 : Double = bind bindedDouble5;
def bindedDouble3 : Double = bind lazy fDouble(bindedDouble4);
var bindedDouble2 : Double = bind lazy bindedDouble3;
def bindedDouble : Double = bind lazy bindedDouble2;
checkFlag(false, "Double 1");
valueDouble = 1.21e4 as Double;
checkFlag(false, "Double 2");
var trashDouble : Double = bindedDouble;
checkFlag(true, "Double 3");
if (trashDouble != 1.22e4) println("FAILED Double 4");
resetFlag();

println("Double eager");
var valueBDouble : Double = 1.25e4 as Double;
def bindedBDouble7 : Double = bind valueBDouble;
var bindedBDouble6 : Double = bind bindedBDouble7;
def bindedBDouble5 : Double = bind bindedBDouble6;
var bindedBDouble4 : Double = bind bindedBDouble5;
def bindedBDouble3 : Double = bind fDouble(bindedBDouble4);
var bindedBDouble2 : Double = bind bindedBDouble3;
def bindedBDouble : Double = bind bindedBDouble2;
checkFlag(true, "Double 1B");
resetFlag();
valueBDouble = 1.21e4 as Double;
checkFlag(true, "Double 2B");
resetFlag();
var trashBDouble : Double = bindedBDouble;
checkFlag(false, "Double 3B");
if (trashBDouble != 1.22e4) println("FAILED Double 4B");
resetFlag();

/***** Number *****/
function fNumber(x : Number) : Number{
    flag = true;
    return 102;
}

println("Number lazy+eager");
var valueNumber : Number = 100 as Number;
def bindedNumber7 : Number = bind lazy valueNumber;
var bindedNumber6 : Number = bind bindedNumber7;
def bindedNumber5 : Number = bind lazy bindedNumber6;
var bindedNumber4 : Number = bind bindedNumber5;
def bindedNumber3 : Number = bind lazy fNumber(bindedNumber4);
var bindedNumber2 : Number = bind lazy bindedNumber3;
def bindedNumber : Number = bind lazy bindedNumber2;
checkFlag(false, "Number 1");
valueNumber = 101 as Number;
checkFlag(false, "Number 2");
var trashNumber : Number = bindedNumber;
checkFlag(true, "Number 3");
if (trashNumber != 102) println("FAILED Number 4");
resetFlag();

println("Number eager");
var valueBNumber : Number = 100 as Number;
def bindedBNumber7 : Number = bind valueBNumber;
var bindedBNumber6 : Number = bind bindedBNumber7;
def bindedBNumber5 : Number = bind bindedBNumber6;
var bindedBNumber4 : Number = bind bindedBNumber5;
def bindedBNumber3 : Number = bind fNumber(bindedBNumber4);
var bindedBNumber2 : Number = bind bindedBNumber3;
def bindedBNumber : Number = bind bindedBNumber2;
checkFlag(true, "Number 1B");
resetFlag();
valueBNumber = 101 as Number;
checkFlag(true, "Number 2B");
resetFlag();
var trashBNumber : Number = bindedBNumber;
checkFlag(false, "Number 3B");
if (trashBNumber != 102) println("FAILED Number 4B");
resetFlag();

/***** String *****/
function fString(x : String) : String{
    flag = true;
    return "-52";
}

println("String lazy+eager");
var valueString : String = "-50" as String;
def bindedString7 : String = bind lazy valueString;
var bindedString6 : String = bind bindedString7;
def bindedString5 : String = bind lazy bindedString6;
var bindedString4 : String = bind bindedString5;
def bindedString3 : String = bind lazy fString(bindedString4);
var bindedString2 : String = bind lazy bindedString3;
def bindedString : String = bind lazy bindedString2;
checkFlag(false, "String 1");
valueString = "-51" as String;
checkFlag(false, "String 2");
var trashString : String = bindedString;
checkFlag(true, "String 3");
if (trashString != "-52") println("FAILED String 4");
resetFlag();

println("String eager");
var valueBString : String = "-50" as String;
def bindedBString7 : String = bind valueBString;
var bindedBString6 : String = bind bindedBString7;
def bindedBString5 : String = bind bindedBString6;
var bindedBString4 : String = bind bindedBString5;
def bindedBString3 : String = bind fString(bindedBString4);
var bindedBString2 : String = bind bindedBString3;
def bindedBString : String = bind bindedBString2;
checkFlag(true, "String 1B");
resetFlag();
valueBString = "-51" as String;
checkFlag(true, "String 2B");
resetFlag();
var trashBString : String = bindedBString;
checkFlag(false, "String 3B");
if (trashBString != "-52") println("FAILED String 4B");
resetFlag();

/***** Boolean *****/
function fBoolean(x : Boolean) : Boolean{
    flag = true;
    return true;
}

println("Boolean lazy+eager");
var valueBoolean : Boolean = true as Boolean;
def bindedBoolean7 : Boolean = bind lazy valueBoolean;
var bindedBoolean6 : Boolean = bind bindedBoolean7;
def bindedBoolean5 : Boolean = bind lazy bindedBoolean6;
var bindedBoolean4 : Boolean = bind bindedBoolean5;
def bindedBoolean3 : Boolean = bind lazy fBoolean(bindedBoolean4);
var bindedBoolean2 : Boolean = bind lazy bindedBoolean3;
def bindedBoolean : Boolean = bind lazy bindedBoolean2;
checkFlag(false, "Boolean 1");
valueBoolean = false as Boolean;
checkFlag(false, "Boolean 2");
var trashBoolean : Boolean = bindedBoolean;
checkFlag(true, "Boolean 3");
if (trashBoolean != true) println("FAILED Boolean 4");
resetFlag();

println("Boolean eager");
var valueBBoolean : Boolean = true as Boolean;
def bindedBBoolean7 : Boolean = bind valueBBoolean;
var bindedBBoolean6 : Boolean = bind bindedBBoolean7;
def bindedBBoolean5 : Boolean = bind bindedBBoolean6;
var bindedBBoolean4 : Boolean = bind bindedBBoolean5;
def bindedBBoolean3 : Boolean = bind fBoolean(bindedBBoolean4);
var bindedBBoolean2 : Boolean = bind bindedBBoolean3;
def bindedBBoolean : Boolean = bind bindedBBoolean2;
checkFlag(true, "Boolean 1B");
resetFlag();
valueBBoolean = false as Boolean;
checkFlag(true, "Boolean 2B");
resetFlag();
var trashBBoolean : Boolean = bindedBBoolean;
checkFlag(false, "Boolean 3B");
if (trashBBoolean != true) println("FAILED Boolean 4B");
resetFlag();

/***** Duration *****/
function fDuration(x : Duration) : Duration{
    flag = true;
    return 12s;
}

println("Duration lazy+eager");
var valueDuration : Duration = 10s as Duration;
def bindedDuration7 : Duration = bind lazy valueDuration;
var bindedDuration6 : Duration = bind bindedDuration7;
def bindedDuration5 : Duration = bind lazy bindedDuration6;
var bindedDuration4 : Duration = bind bindedDuration5;
def bindedDuration3 : Duration = bind lazy fDuration(bindedDuration4);
var bindedDuration2 : Duration = bind lazy bindedDuration3;
def bindedDuration : Duration = bind lazy bindedDuration2;
checkFlag(false, "Duration 1");
valueDuration = 11s as Duration;
checkFlag(false, "Duration 2");
var trashDuration : Duration = bindedDuration;
checkFlag(true, "Duration 3");
if (trashDuration != 12s) println("FAILED Duration 4");
resetFlag();

println("Duration eager");
var valueBDuration : Duration = 10s as Duration;
def bindedBDuration7 : Duration = bind valueBDuration;
var bindedBDuration6 : Duration = bind bindedBDuration7;
def bindedBDuration5 : Duration = bind bindedBDuration6;
var bindedBDuration4 : Duration = bind bindedBDuration5;
def bindedBDuration3 : Duration = bind fDuration(bindedBDuration4);
var bindedBDuration2 : Duration = bind bindedBDuration3;
def bindedBDuration : Duration = bind bindedBDuration2;
checkFlag(true, "Duration 1B");
resetFlag();
valueBDuration = 11s as Duration;
checkFlag(true, "Duration 2B");
resetFlag();
var trashBDuration : Duration = bindedBDuration;
checkFlag(false, "Duration 3B");
if (trashBDuration != 12s) println("FAILED Duration 4B");
resetFlag();

/********** End: Lazy/eager Binding for global variables **********/

/********** Lazy/eager Binding for variables in a global function **********/
function testGlobalFunc() {
    /***** Byte *****/
    println("Byte lazy+eager in function");
    var valueFuncByte : Byte = 120 as Byte;
    def bindedFuncByte7 : Byte = bind lazy valueFuncByte;
    var bindedFuncByte6 : Byte = bind bindedFuncByte7;
    def bindedFuncByte5 : Byte = bind lazy bindedFuncByte6;
    var bindedFuncByte4 : Byte = bind bindedFuncByte5;
    def bindedFuncByte3 : Byte = bind lazy fByte(bindedFuncByte4);
    var bindedFuncByte2 : Byte = bind lazy bindedFuncByte3;
    def bindedFuncByte : Byte = bind lazy bindedFuncByte2;
    checkFlag(false, "Byte 1f");
    valueFuncByte = 121 as Byte;
    checkFlag(false, "Byte 2f");
    var trashFuncByte : Byte = bindedFuncByte;
    checkFlag(true, "Byte 3f");
    if (trashFuncByte != 122) println("FAILED Byte 4f");
    resetFlag();

    println("Byte eager in function");
    var valueFuncBByte : Byte = 120 as Byte;
    def bindedFuncBByte7 : Byte = bind valueFuncBByte;
    var bindedFuncBByte6 : Byte = bind bindedFuncBByte7;
    def bindedFuncBByte5 : Byte = bind bindedFuncBByte6;
    var bindedFuncBByte4 : Byte = bind bindedFuncBByte5;
    def bindedFuncBByte3 : Byte = bind fByte(bindedFuncBByte4);
    var bindedFuncBByte2 : Byte = bind bindedFuncBByte3;
    def bindedFuncBByte : Byte = bind bindedFuncBByte2;
    checkFlag(true, "Byte 1Bf");
    resetFlag();
    valueFuncBByte = 121 as Byte;
    checkFlag(true, "Byte 2Bf");
    resetFlag();
    var trashFuncBByte : Byte = bindedFuncBByte;
    checkFlag(false, "Byte 3Bf");
    if (trashFuncBByte != 122) println("FAILED Byte 4Bf");
    resetFlag();

    /***** Short *****/
    println("Short lazy+eager in function");
    var valueFuncShort : Short = 30000 as Short;
    def bindedFuncShort7 : Short = bind lazy valueFuncShort;
    var bindedFuncShort6 : Short = bind bindedFuncShort7;
    def bindedFuncShort5 : Short = bind lazy bindedFuncShort6;
    var bindedFuncShort4 : Short = bind bindedFuncShort5;
    def bindedFuncShort3 : Short = bind lazy fShort(bindedFuncShort4);
    var bindedFuncShort2 : Short = bind lazy bindedFuncShort3;
    def bindedFuncShort : Short = bind lazy bindedFuncShort2;
    checkFlag(false, "Short 1f");
    valueFuncShort = 30001 as Short;
    checkFlag(false, "Short 2f");
    var trashFuncShort : Short = bindedFuncShort;
    checkFlag(true, "Short 3f");
    if (trashFuncShort != 30002) println("FAILED Short 4f");
    resetFlag();

    println("Short eager in function");
    var valueFuncBShort : Short = 30000 as Short;
    def bindedFuncBShort7 : Short = bind valueFuncBShort;
    var bindedFuncBShort6 : Short = bind bindedFuncBShort7;
    def bindedFuncBShort5 : Short = bind bindedFuncBShort6;
    var bindedFuncBShort4 : Short = bind bindedFuncBShort5;
    def bindedFuncBShort3 : Short = bind fShort(bindedFuncBShort4);
    var bindedFuncBShort2 : Short = bind bindedFuncBShort3;
    def bindedFuncBShort : Short = bind bindedFuncBShort2;
    checkFlag(true, "Short 1Bf");
    resetFlag();
    valueFuncBShort = 30001 as Short;
    checkFlag(true, "Short 2Bf");
    resetFlag();
    var trashFuncBShort : Short = bindedFuncBShort;
    checkFlag(false, "Short 3Bf");
    if (trashFuncBShort != 30002) println("FAILED Short 4Bf");
    resetFlag();

    /***** Character *****/
    println("Character lazy+eager in function");
    var valueFuncCharacter : Character = 64 as Character;
    def bindedFuncCharacter7 : Character = bind lazy valueFuncCharacter;
    var bindedFuncCharacter6 : Character = bind bindedFuncCharacter7;
    def bindedFuncCharacter5 : Character = bind lazy bindedFuncCharacter6;
    var bindedFuncCharacter4 : Character = bind bindedFuncCharacter5;
    def bindedFuncCharacter3 : Character = bind lazy fCharacter(bindedFuncCharacter4);
    var bindedFuncCharacter2 : Character = bind lazy bindedFuncCharacter3;
    def bindedFuncCharacter : Character = bind lazy bindedFuncCharacter2;
    checkFlag(false, "Character 1f");
    valueFuncCharacter = 61 as Character;
    checkFlag(false, "Character 2f");
    var trashFuncCharacter : Character = bindedFuncCharacter;
    checkFlag(true, "Character 3f");
    if (trashFuncCharacter != 62) println("FAILED Character 4f");
    resetFlag();

    println("Character eager in function");
    var valueFuncBCharacter : Character = 64 as Character;
    def bindedFuncBCharacter7 : Character = bind valueFuncBCharacter;
    var bindedFuncBCharacter6 : Character = bind bindedFuncBCharacter7;
    def bindedFuncBCharacter5 : Character = bind bindedFuncBCharacter6;
    var bindedFuncBCharacter4 : Character = bind bindedFuncBCharacter5;
    def bindedFuncBCharacter3 : Character = bind fCharacter(bindedFuncBCharacter4);
    var bindedFuncBCharacter2 : Character = bind bindedFuncBCharacter3;
    def bindedFuncBCharacter : Character = bind bindedFuncBCharacter2;
    checkFlag(true, "Character 1Bf");
    resetFlag();
    valueFuncBCharacter = 61 as Character;
    checkFlag(true, "Character 2Bf");
    resetFlag();
    var trashFuncBCharacter : Character = bindedFuncBCharacter;
    checkFlag(false, "Character 3Bf");
    if (trashFuncBCharacter != 62) println("FAILED Character 4Bf");
    resetFlag();

    /***** Integer *****/
    println("Integer lazy+eager in function");
    var valueFuncInteger : Integer = 1000000 as Integer;
    def bindedFuncInteger7 : Integer = bind lazy valueFuncInteger;
    var bindedFuncInteger6 : Integer = bind bindedFuncInteger7;
    def bindedFuncInteger5 : Integer = bind lazy bindedFuncInteger6;
    var bindedFuncInteger4 : Integer = bind bindedFuncInteger5;
    def bindedFuncInteger3 : Integer = bind lazy fInteger(bindedFuncInteger4);
    var bindedFuncInteger2 : Integer = bind lazy bindedFuncInteger3;
    def bindedFuncInteger : Integer = bind lazy bindedFuncInteger2;
    checkFlag(false, "Integer 1f");
    valueFuncInteger = 1000001 as Integer;
    checkFlag(false, "Integer 2f");
    var trashFuncInteger : Integer = bindedFuncInteger;
    checkFlag(true, "Integer 3f");
    if (trashFuncInteger != 1000002) println("FAILED Integer 4f");
    resetFlag();

    println("Integer eager in function");
    var valueFuncBInteger : Integer = 1000000 as Integer;
    def bindedFuncBInteger7 : Integer = bind valueFuncBInteger;
    var bindedFuncBInteger6 : Integer = bind bindedFuncBInteger7;
    def bindedFuncBInteger5 : Integer = bind bindedFuncBInteger6;
    var bindedFuncBInteger4 : Integer = bind bindedFuncBInteger5;
    def bindedFuncBInteger3 : Integer = bind fInteger(bindedFuncBInteger4);
    var bindedFuncBInteger2 : Integer = bind bindedFuncBInteger3;
    def bindedFuncBInteger : Integer = bind bindedFuncBInteger2;
    checkFlag(true, "Integer 1Bf");
    resetFlag();
    valueFuncBInteger = 1000001 as Integer;
    checkFlag(true, "Integer 2Bf");
    resetFlag();
    var trashFuncBInteger : Integer = bindedFuncBInteger;
    checkFlag(false, "Integer 3Bf");
    if (trashFuncBInteger != 1000002) println("FAILED Integer 4Bf");
    resetFlag();

    /***** Long *****/
    println("Long lazy+eager in function");
    var valueFuncLong : Long = 1000000000 as Long;
    def bindedFuncLong7 : Long = bind lazy valueFuncLong;
    var bindedFuncLong6 : Long = bind bindedFuncLong7;
    def bindedFuncLong5 : Long = bind lazy bindedFuncLong6;
    var bindedFuncLong4 : Long = bind bindedFuncLong5;
    def bindedFuncLong3 : Long = bind lazy fLong(bindedFuncLong4);
    var bindedFuncLong2 : Long = bind lazy bindedFuncLong3;
    def bindedFuncLong : Long = bind lazy bindedFuncLong2;
    checkFlag(false, "Long 1f");
    valueFuncLong = 1000000001 as Long;
    checkFlag(false, "Long 2f");
    var trashFuncLong : Long = bindedFuncLong;
    checkFlag(true, "Long 3f");
    if (trashFuncLong != 1000000002) println("FAILED Long 4f");
    resetFlag();

    println("Long eager in function");
    var valueFuncBLong : Long = 1000000000 as Long;
    def bindedFuncBLong7 : Long = bind valueFuncBLong;
    var bindedFuncBLong6 : Long = bind bindedFuncBLong7;
    def bindedFuncBLong5 : Long = bind bindedFuncBLong6;
    var bindedFuncBLong4 : Long = bind bindedFuncBLong5;
    def bindedFuncBLong3 : Long = bind fLong(bindedFuncBLong4);
    var bindedFuncBLong2 : Long = bind bindedFuncBLong3;
    def bindedFuncBLong : Long = bind bindedFuncBLong2;
    checkFlag(true, "Long 1Bf");
    resetFlag();
    valueFuncBLong = 1000000001 as Long;
    checkFlag(true, "Long 2Bf");
    resetFlag();
    var trashFuncBLong : Long = bindedFuncBLong;
    checkFlag(false, "Long 3Bf");
    if (trashFuncBLong != 1000000002) println("FAILED Long 4Bf");
    resetFlag();

    /***** Float *****/
    println("Float lazy+eager in function");
    var valueFuncFloat : Float = 10.5 as Float;
    def bindedFuncFloat7 : Float = bind lazy valueFuncFloat;
    var bindedFuncFloat6 : Float = bind bindedFuncFloat7;
    def bindedFuncFloat5 : Float = bind lazy bindedFuncFloat6;
    var bindedFuncFloat4 : Float = bind bindedFuncFloat5;
    def bindedFuncFloat3 : Float = bind lazy fFloat(bindedFuncFloat4);
    var bindedFuncFloat2 : Float = bind lazy bindedFuncFloat3;
    def bindedFuncFloat : Float = bind lazy bindedFuncFloat2;
    checkFlag(false, "Float 1f");
    valueFuncFloat = 11.5 as Float;
    checkFlag(false, "Float 2f");
    var trashFuncFloat : Float = bindedFuncFloat;
    checkFlag(true, "Float 3f");
    if (trashFuncFloat != 12.5) println("FAILED Float 4f");
    resetFlag();

    println("Float eager in function");
    var valueFuncBFloat : Float = 10.5 as Float;
    def bindedFuncBFloat7 : Float = bind valueFuncBFloat;
    var bindedFuncBFloat6 : Float = bind bindedFuncBFloat7;
    def bindedFuncBFloat5 : Float = bind bindedFuncBFloat6;
    var bindedFuncBFloat4 : Float = bind bindedFuncBFloat5;
    def bindedFuncBFloat3 : Float = bind fFloat(bindedFuncBFloat4);
    var bindedFuncBFloat2 : Float = bind bindedFuncBFloat3;
    def bindedFuncBFloat : Float = bind bindedFuncBFloat2;
    checkFlag(true, "Float 1Bf");
    resetFlag();
    valueFuncBFloat = 11.5 as Float;
    checkFlag(true, "Float 2Bf");
    resetFlag();
    var trashFuncBFloat : Float = bindedFuncBFloat;
    checkFlag(false, "Float 3Bf");
    if (trashFuncBFloat != 12.5) println("FAILED Float 4Bf");
    resetFlag();

    /***** Double *****/
    println("Double lazy+eager in function");
    var valueFuncDouble : Double = 1.25e4 as Double;
    def bindedFuncDouble7 : Double = bind lazy valueFuncDouble;
    var bindedFuncDouble6 : Double = bind bindedFuncDouble7;
    def bindedFuncDouble5 : Double = bind lazy bindedFuncDouble6;
    var bindedFuncDouble4 : Double = bind bindedFuncDouble5;
    def bindedFuncDouble3 : Double = bind lazy fDouble(bindedFuncDouble4);
    var bindedFuncDouble2 : Double = bind lazy bindedFuncDouble3;
    def bindedFuncDouble : Double = bind lazy bindedFuncDouble2;
    checkFlag(false, "Double 1f");
    valueFuncDouble = 1.21e4 as Double;
    checkFlag(false, "Double 2f");
    var trashFuncDouble : Double = bindedFuncDouble;
    checkFlag(true, "Double 3f");
    if (trashFuncDouble != 1.22e4) println("FAILED Double 4f");
    resetFlag();

    println("Double eager in function");
    var valueFuncBDouble : Double = 1.25e4 as Double;
    def bindedFuncBDouble7 : Double = bind valueFuncBDouble;
    var bindedFuncBDouble6 : Double = bind bindedFuncBDouble7;
    def bindedFuncBDouble5 : Double = bind bindedFuncBDouble6;
    var bindedFuncBDouble4 : Double = bind bindedFuncBDouble5;
    def bindedFuncBDouble3 : Double = bind fDouble(bindedFuncBDouble4);
    var bindedFuncBDouble2 : Double = bind bindedFuncBDouble3;
    def bindedFuncBDouble : Double = bind bindedFuncBDouble2;
    checkFlag(true, "Double 1Bf");
    resetFlag();
    valueFuncBDouble = 1.21e4 as Double;
    checkFlag(true, "Double 2Bf");
    resetFlag();
    var trashFuncBDouble : Double = bindedFuncBDouble;
    checkFlag(false, "Double 3Bf");
    if (trashFuncBDouble != 1.22e4) println("FAILED Double 4Bf");
    resetFlag();

    /***** Number *****/
    println("Number lazy+eager in function");
    var valueFuncNumber : Number = 100 as Number;
    def bindedFuncNumber7 : Number = bind lazy valueFuncNumber;
    var bindedFuncNumber6 : Number = bind bindedFuncNumber7;
    def bindedFuncNumber5 : Number = bind lazy bindedFuncNumber6;
    var bindedFuncNumber4 : Number = bind bindedFuncNumber5;
    def bindedFuncNumber3 : Number = bind lazy fNumber(bindedFuncNumber4);
    var bindedFuncNumber2 : Number = bind lazy bindedFuncNumber3;
    def bindedFuncNumber : Number = bind lazy bindedFuncNumber2;
    checkFlag(false, "Number 1f");
    valueFuncNumber = 101 as Number;
    checkFlag(false, "Number 2f");
    var trashFuncNumber : Number = bindedFuncNumber;
    checkFlag(true, "Number 3f");
    if (trashFuncNumber != 102) println("FAILED Number 4f");
    resetFlag();

    println("Number eager in function");
    var valueFuncBNumber : Number = 100 as Number;
    def bindedFuncBNumber7 : Number = bind valueFuncBNumber;
    var bindedFuncBNumber6 : Number = bind bindedFuncBNumber7;
    def bindedFuncBNumber5 : Number = bind bindedFuncBNumber6;
    var bindedFuncBNumber4 : Number = bind bindedFuncBNumber5;
    def bindedFuncBNumber3 : Number = bind fNumber(bindedFuncBNumber4);
    var bindedFuncBNumber2 : Number = bind bindedFuncBNumber3;
    def bindedFuncBNumber : Number = bind bindedFuncBNumber2;
    checkFlag(true, "Number 1Bf");
    resetFlag();
    valueFuncBNumber = 101 as Number;
    checkFlag(true, "Number 2Bf");
    resetFlag();
    var trashFuncBNumber : Number = bindedFuncBNumber;
    checkFlag(false, "Number 3Bf");
    if (trashFuncBNumber != 102) println("FAILED Number 4Bf");
    resetFlag();

    /***** String *****/
    println("String lazy+eager in function");
    var valueFuncString : String = "-50" as String;
    def bindedFuncString7 : String = bind lazy valueFuncString;
    var bindedFuncString6 : String = bind bindedFuncString7;
    def bindedFuncString5 : String = bind lazy bindedFuncString6;
    var bindedFuncString4 : String = bind bindedFuncString5;
    def bindedFuncString3 : String = bind lazy fString(bindedFuncString4);
    var bindedFuncString2 : String = bind lazy bindedFuncString3;
    def bindedFuncString : String = bind lazy bindedFuncString2;
    checkFlag(false, "String 1f");
    valueFuncString = "-51" as String;
    checkFlag(false, "String 2f");
    var trashFuncString : String = bindedFuncString;
    checkFlag(true, "String 3f");
    if (trashFuncString != "-52") println("FAILED String 4f");
    resetFlag();

    println("String eager in function");
    var valueFuncBString : String = "-50" as String;
    def bindedFuncBString7 : String = bind valueFuncBString;
    var bindedFuncBString6 : String = bind bindedFuncBString7;
    def bindedFuncBString5 : String = bind bindedFuncBString6;
    var bindedFuncBString4 : String = bind bindedFuncBString5;
    def bindedFuncBString3 : String = bind fString(bindedFuncBString4);
    var bindedFuncBString2 : String = bind bindedFuncBString3;
    def bindedFuncBString : String = bind bindedFuncBString2;
    checkFlag(true, "String 1Bf");
    resetFlag();
    valueFuncBString = "-51" as String;
    checkFlag(true, "String 2Bf");
    resetFlag();
    var trashFuncBString : String = bindedFuncBString;
    checkFlag(false, "String 3Bf");
    if (trashFuncBString != "-52") println("FAILED String 4Bf");
    resetFlag();

    /***** Boolean *****/
    println("Boolean lazy+eager in function");
    var valueFuncBoolean : Boolean = true as Boolean;
    def bindedFuncBoolean7 : Boolean = bind lazy valueFuncBoolean;
    var bindedFuncBoolean6 : Boolean = bind bindedFuncBoolean7;
    def bindedFuncBoolean5 : Boolean = bind lazy bindedFuncBoolean6;
    var bindedFuncBoolean4 : Boolean = bind bindedFuncBoolean5;
    def bindedFuncBoolean3 : Boolean = bind lazy fBoolean(bindedFuncBoolean4);
    var bindedFuncBoolean2 : Boolean = bind lazy bindedFuncBoolean3;
    def bindedFuncBoolean : Boolean = bind lazy bindedFuncBoolean2;
    checkFlag(false, "Boolean 1f");
    valueFuncBoolean = false as Boolean;
    checkFlag(false, "Boolean 2f");
    var trashFuncBoolean : Boolean = bindedFuncBoolean;
    checkFlag(true, "Boolean 3f");
    if (trashFuncBoolean != true) println("FAILED Boolean 4f");
    resetFlag();

    println("Boolean eager in function");
    var valueFuncBBoolean : Boolean = true as Boolean;
    def bindedFuncBBoolean7 : Boolean = bind valueFuncBBoolean;
    var bindedFuncBBoolean6 : Boolean = bind bindedFuncBBoolean7;
    def bindedFuncBBoolean5 : Boolean = bind bindedFuncBBoolean6;
    var bindedFuncBBoolean4 : Boolean = bind bindedFuncBBoolean5;
    def bindedFuncBBoolean3 : Boolean = bind fBoolean(bindedFuncBBoolean4);
    var bindedFuncBBoolean2 : Boolean = bind bindedFuncBBoolean3;
    def bindedFuncBBoolean : Boolean = bind bindedFuncBBoolean2;
    checkFlag(true, "Boolean 1Bf");
    resetFlag();
    valueFuncBBoolean = false as Boolean;
    checkFlag(true, "Boolean 2Bf");
    resetFlag();
    var trashFuncBBoolean : Boolean = bindedFuncBBoolean;
    checkFlag(false, "Boolean 3Bf");
    if (trashFuncBBoolean != true) println("FAILED Boolean 4Bf");
    resetFlag();

    /***** Duration *****/
    println("Duration lazy+eager in function");
    var valueFuncDuration : Duration = 10s as Duration;
    def bindedFuncDuration7 : Duration = bind lazy valueFuncDuration;
    var bindedFuncDuration6 : Duration = bind bindedFuncDuration7;
    def bindedFuncDuration5 : Duration = bind lazy bindedFuncDuration6;
    var bindedFuncDuration4 : Duration = bind bindedFuncDuration5;
    def bindedFuncDuration3 : Duration = bind lazy fDuration(bindedFuncDuration4);
    var bindedFuncDuration2 : Duration = bind lazy bindedFuncDuration3;
    def bindedFuncDuration : Duration = bind lazy bindedFuncDuration2;
    checkFlag(false, "Duration 1f");
    valueFuncDuration = 11s as Duration;
    checkFlag(false, "Duration 2f");
    var trashFuncDuration : Duration = bindedFuncDuration;
    checkFlag(true, "Duration 3f");
    if (trashFuncDuration != 12s) println("FAILED Duration 4f");
    resetFlag();

    println("Duration eager in function");
    var valueFuncBDuration : Duration = 10s as Duration;
    def bindedFuncBDuration7 : Duration = bind valueFuncBDuration;
    var bindedFuncBDuration6 : Duration = bind bindedFuncBDuration7;
    def bindedFuncBDuration5 : Duration = bind bindedFuncBDuration6;
    var bindedFuncBDuration4 : Duration = bind bindedFuncBDuration5;
    def bindedFuncBDuration3 : Duration = bind fDuration(bindedFuncBDuration4);
    var bindedFuncBDuration2 : Duration = bind bindedFuncBDuration3;
    def bindedFuncBDuration : Duration = bind bindedFuncBDuration2;
    checkFlag(true, "Duration 1Bf");
    resetFlag();
    valueFuncBDuration = 11s as Duration;
    checkFlag(true, "Duration 2Bf");
    resetFlag();
    var trashFuncBDuration : Duration = bindedFuncBDuration;
    checkFlag(false, "Duration 3Bf");
    if (trashFuncBDuration != 12s) println("FAILED Duration 4Bf");
    resetFlag();

}
testGlobalFunc();
/********** END: Lazy/eager Binding for variables in a global function **********/
