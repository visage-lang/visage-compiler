/*
 * Lazy binding and for loop, global sequence variables
 *
 * @test
 * @run
 */

// Please, uncomment and add expected value when the issue 3136 will be fixed

/** flag **/
var flag : Boolean = false as Boolean;
function checkFlag (expected : Boolean, msg : String) {
    if (expected != flag) println("FAILED {msg}");
}
function resetFlag() {
    flag = false;
}
/** flag **/

/**** LAZY ****
//***** Byte *****
function fByte(x : Byte) : Byte{
    flag = true;
    return x;
}


println("Byte lazy+for");
var valueByte : Byte[] = [120 as Byte, 121 as Byte] as Byte[];
def bindedByte7 : Byte[] = bind lazy valueByte;
var bindedByte6 : Byte[] = bind bindedByte7;
def bindedByte5 : Byte[] = bind lazy bindedByte6;
var bindedByte4 : Byte[] = bind bindedByte5;
var bindedByte3 : Byte[] = bind lazy
        for (item in bindedByte4) item + fByte(1 as Byte);
var bindedByte2 : Byte[] = bind lazy bindedByte3;
def bindedByte : Byte[] = bind lazy bindedByte2;
checkFlag(false, "Byte 1");
insert 122 into valueByte;
checkFlag(false, "Byte 2");
var trashByte : Byte[] = bindedByte;
checkFlag(true, "Byte 3");
if (trashByte != [120 + (1 as Byte), 121 + (1 as Byte), 122 + (1 as Byte)]) println("FAILED Byte 4");
resetFlag();


//***** Short *****
function fShort(x : Short) : Short{
    flag = true;
    return x;
}


println("Short lazy+for");
var valueShort : Short[] = [30000 as Short, 30001 as Short] as Short[];
def bindedShort7 : Short[] = bind lazy valueShort;
var bindedShort6 : Short[] = bind bindedShort7;
def bindedShort5 : Short[] = bind lazy bindedShort6;
var bindedShort4 : Short[] = bind bindedShort5;
var bindedShort3 : Short[] = bind lazy
        for (item in bindedShort4) item + fShort(300 as Short);
var bindedShort2 : Short[] = bind lazy bindedShort3;
def bindedShort : Short[] = bind lazy bindedShort2;
checkFlag(false, "Short 1");
insert 30002 into valueShort;
checkFlag(false, "Short 2");
var trashShort : Short[] = bindedShort;
checkFlag(true, "Short 3");
if (trashShort != [30000 + (300 as Short), 30001 + (300 as Short), 30002 + (300 as Short)]) println("FAILED Short 4");
resetFlag();


//***** Character *****
function fCharacter(x : Character) : Character{
    flag = true;
    return x;
}


println("Character lazy+for");
var valueCharacter : Character[] = [64 as Character, 61 as Character] as Character[];
def bindedCharacter7 : Character[] = bind lazy valueCharacter;
var bindedCharacter6 : Character[] = bind bindedCharacter7;
def bindedCharacter5 : Character[] = bind lazy bindedCharacter6;
var bindedCharacter4 : Character[] = bind bindedCharacter5;
var bindedCharacter3 : Character[] = bind lazy
        for (item in bindedCharacter4) item + fCharacter(6 as Character);
var bindedCharacter2 : Character[] = bind lazy bindedCharacter3;
def bindedCharacter : Character[] = bind lazy bindedCharacter2;
checkFlag(false, "Character 1");
insert 62 into valueCharacter;
checkFlag(false, "Character 2");
var trashCharacter : Character[] = bindedCharacter;
checkFlag(true, "Character 3");
if (trashCharacter != [64 + (6 as Character), 61 + (6 as Character), 62 + (6 as Character)]) println("FAILED Character 4");
resetFlag();


//***** Integer *****
function fInteger(x : Integer) : Integer{
    flag = true;
    return x;
}


println("Integer lazy+for");
var valueInteger : Integer[] = [1000000 as Integer, 1000001 as Integer] as Integer[];
def bindedInteger7 : Integer[] = bind lazy valueInteger;
var bindedInteger6 : Integer[] = bind bindedInteger7;
def bindedInteger5 : Integer[] = bind lazy bindedInteger6;
var bindedInteger4 : Integer[] = bind bindedInteger5;
var bindedInteger3 : Integer[] = bind lazy
        for (item in bindedInteger4) item + fInteger(1000 as Integer);
var bindedInteger2 : Integer[] = bind lazy bindedInteger3;
def bindedInteger : Integer[] = bind lazy bindedInteger2;
checkFlag(false, "Integer 1");
insert 1000002 into valueInteger;
checkFlag(false, "Integer 2");
var trashInteger : Integer[] = bindedInteger;
checkFlag(true, "Integer 3");
if (trashInteger != [1000000 + (1000 as Integer), 1000001 + (1000 as Integer), 1000002 + (1000 as Integer)]) println("FAILED Integer 4");
resetFlag();


//***** Long *****
function fLong(x : Long) : Long{
    flag = true;
    return x;
}


println("Long lazy+for");
var valueLong : Long[] = [1000000000 as Long, 1000000001 as Long] as Long[];
def bindedLong7 : Long[] = bind lazy valueLong;
var bindedLong6 : Long[] = bind bindedLong7;
def bindedLong5 : Long[] = bind lazy bindedLong6;
var bindedLong4 : Long[] = bind bindedLong5;
var bindedLong3 : Long[] = bind lazy
        for (item in bindedLong4) item + fLong(10000 as Long);
var bindedLong2 : Long[] = bind lazy bindedLong3;
def bindedLong : Long[] = bind lazy bindedLong2;
checkFlag(false, "Long 1");
insert 1000000002 into valueLong;
checkFlag(false, "Long 2");
var trashLong : Long[] = bindedLong;
checkFlag(true, "Long 3");
if (trashLong != [1000000000 + (10000 as Long), 1000000001 + (10000 as Long), 1000000002 + (10000 as Long)]) println("FAILED Long 4");
resetFlag();


//***** Float *****
function fFloat(x : Float) : Float{
    flag = true;
    return x;
}


println("Float lazy+for");
var valueFloat : Float[] = [10.5 as Float, 11.5 as Float] as Float[];
def bindedFloat7 : Float[] = bind lazy valueFloat;
var bindedFloat6 : Float[] = bind bindedFloat7;
def bindedFloat5 : Float[] = bind lazy bindedFloat6;
var bindedFloat4 : Float[] = bind bindedFloat5;
var bindedFloat3 : Float[] = bind lazy
        for (item in bindedFloat4) item + fFloat(1.5 as Float);
var bindedFloat2 : Float[] = bind lazy bindedFloat3;
def bindedFloat : Float[] = bind lazy bindedFloat2;
checkFlag(false, "Float 1");
insert 12.5 into valueFloat;
checkFlag(false, "Float 2");
var trashFloat : Float[] = bindedFloat;
checkFlag(true, "Float 3");
if (trashFloat != [10.5 + (1.5 as Float), 11.5 + (1.5 as Float), 12.5 + (1.5 as Float)]) println("FAILED Float 4");
resetFlag();


//***** Double *****
function fDouble(x : Double) : Double{
    flag = true;
    return x;
}


println("Double lazy+for");
var valueDouble : Double[] = [1.25e4 as Double, 1.21e4 as Double] as Double[];
def bindedDouble7 : Double[] = bind lazy valueDouble;
var bindedDouble6 : Double[] = bind bindedDouble7;
def bindedDouble5 : Double[] = bind lazy bindedDouble6;
var bindedDouble4 : Double[] = bind bindedDouble5;
var bindedDouble3 : Double[] = bind lazy
        for (item in bindedDouble4) item + fDouble(1.15e4 as Double);
var bindedDouble2 : Double[] = bind lazy bindedDouble3;
def bindedDouble : Double[] = bind lazy bindedDouble2;
checkFlag(false, "Double 1");
insert 1.22e4 into valueDouble;
checkFlag(false, "Double 2");
var trashDouble : Double[] = bindedDouble;
checkFlag(true, "Double 3");
if (trashDouble != [1.25e4 + (1.15e4 as Double), 1.21e4 + (1.15e4 as Double), 1.22e4 + (1.15e4 as Double)]) println("FAILED Double 4");
resetFlag();


//***** Number *****
function fNumber(x : Number) : Number{
    flag = true;
    return x;
}


println("Number lazy+for");
var valueNumber : Number[] = [100 as Number, 101 as Number] as Number[];
def bindedNumber7 : Number[] = bind lazy valueNumber;
var bindedNumber6 : Number[] = bind bindedNumber7;
def bindedNumber5 : Number[] = bind lazy bindedNumber6;
var bindedNumber4 : Number[] = bind bindedNumber5;
var bindedNumber3 : Number[] = bind lazy
        for (item in bindedNumber4) item + fNumber(10 as Number);
var bindedNumber2 : Number[] = bind lazy bindedNumber3;
def bindedNumber : Number[] = bind lazy bindedNumber2;
checkFlag(false, "Number 1");
insert 102 into valueNumber;
checkFlag(false, "Number 2");
var trashNumber : Number[] = bindedNumber;
checkFlag(true, "Number 3");
if (trashNumber != [100 + (10 as Number), 101 + (10 as Number), 102 + (10 as Number)]) println("FAILED Number 4");
resetFlag();


//***** Duration *****
function fDuration(x : Duration) : Duration{
    flag = true;
    return x;
}


println("Duration lazy+for");
var valueDuration : Duration[] = [10s as Duration, 11s as Duration] as Duration[];
def bindedDuration7 : Duration[] = bind lazy valueDuration;
var bindedDuration6 : Duration[] = bind bindedDuration7;
def bindedDuration5 : Duration[] = bind lazy bindedDuration6;
var bindedDuration4 : Duration[] = bind bindedDuration5;
var bindedDuration3 : Duration[] = bind lazy
        for (item in bindedDuration4) item + fDuration(1s as Duration);
var bindedDuration2 : Duration[] = bind lazy bindedDuration3;
def bindedDuration : Duration[] = bind lazy bindedDuration2;
checkFlag(false, "Duration 1");
insert 12s into valueDuration;
checkFlag(false, "Duration 2");
var trashDuration : Duration[] = bindedDuration;
checkFlag(true, "Duration 3");
if (trashDuration != [10s + (1s as Duration), 11s + (1s as Duration), 12s + (1s as Duration)]) println("FAILED Duration 4");
resetFlag();

//***** String *****
println("String eager+for");
var valueString : String[] = ["xx" as String, "yy" as String] as String[];
def bindedString7 : String[] = bind valueString;
var bindedString6 : String[] = bind bindedString7;
def bindedString5 : String[] = bind bindedString6;
var bindedString4 : String[] = bind bindedString5;
def bindedString3 : String[] = bind lazy
        for (item in bindedString4) "{item}{fString(valueString[0])}";
var bindedString2 : String[] = bind bindedString3;
def bindedString : String[] = bind bindedString2;
checkFlag(true, "String 1B");
resetFlag();
insert "ss" into valueString;
checkFlag(true, "String 2B");
resetFlag();
var trashString : String[] = bindedString;
checkFlag(false, "String 3B");
if (trashString != ["xxxx", "yyxx", "ssxx"]) println("FAILED String 4B");
resetFlag();


**** LAZY ****/
