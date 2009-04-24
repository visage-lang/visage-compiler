/*
 * Eager binding and for loop, global sequence variables
 *
 * @test
 * @run
 */

// Please, uncomment Character and Duration sections and add expected value
// when issues 3137 and 3138 will be fixed

/** flag **/
var flag : Boolean = false as Boolean;
function checkFlag (expected : Boolean, msg : String) {
    if (expected != flag) println("FAILED {msg}");
}
function resetFlag() {
    flag = false;
}
/** flag **/


/***** Byte *****/
function fByte(x : Byte) : Byte{
    flag = true;
    return x;
}

println("Byte eager+for");
var valueBdByte : Byte[] = [120 as Byte, 121 as Byte] as Byte[];
def bindedBdByte7 : Byte[] = bind valueBdByte;
var bindedBdByte6 : Byte[] = bind bindedBdByte7;
def bindedBdByte5 : Byte[] = bind bindedBdByte6;
var bindedBdByte4 : Byte[] = bind bindedBdByte5;
def bindedBdByte3 : Byte[] = bind
        for (item in bindedBdByte4) item + fByte(1 as Byte);
var bindedBdByte2 : Byte[] = bind bindedBdByte3;
def bindedBdByte : Byte[] = bind bindedBdByte2;
checkFlag(true, "Byte 1B");
resetFlag();
insert 122 into valueBdByte;
checkFlag(true, "Byte 2B");
resetFlag();
var trashBdByte : Byte[] = bindedBdByte;
checkFlag(false, "Byte 3B");
if (trashBdByte != [120 + (1 as Byte), 121 + (1 as Byte), 122 + (1 as Byte)]) println("FAILED Byte 4B");
resetFlag();

/***** Short *****/
function fShort(x : Short) : Short{
    flag = true;
    return x;
}

println("Short eager+for");
var valueBdShort : Short[] = [30000 as Short, 30001 as Short] as Short[];
def bindedBdShort7 : Short[] = bind valueBdShort;
var bindedBdShort6 : Short[] = bind bindedBdShort7;
def bindedBdShort5 : Short[] = bind bindedBdShort6;
var bindedBdShort4 : Short[] = bind bindedBdShort5;
def bindedBdShort3 : Short[] = bind
        for (item in bindedBdShort4) item + fShort(300 as Short);
var bindedBdShort2 : Short[] = bind bindedBdShort3;
def bindedBdShort : Short[] = bind bindedBdShort2;
checkFlag(true, "Short 1B");
resetFlag();
insert 30002 into valueBdShort;
checkFlag(true, "Short 2B");
resetFlag();
var trashBdShort : Short[] = bindedBdShort;
checkFlag(false, "Short 3B");
if (trashBdShort != [30000 + (300 as Short), 30001 + (300 as Short), 30002 + (300 as Short)]) println("FAILED Short 4B");
resetFlag();

/***** Character *****/
/*
function fCharacter(x : Character) : Character{
    flag = true;
    return x;
}

println("Character eager+for");
var valueBdCharacter : Character[] = [64 as Character, 61 as Character] as Character[];
def bindedBdCharacter7 : Character[] = bind valueBdCharacter;
var bindedBdCharacter6 : Character[] = bind bindedBdCharacter7;
def bindedBdCharacter5 : Character[] = bind bindedBdCharacter6;
var bindedBdCharacter4 : Character[] = bind bindedBdCharacter5;
def bindedBdCharacter3 : Character[] = bind
        for (item in bindedBdCharacter4) item + fCharacter(6 as Character);
var bindedBdCharacter2 : Character[] = bind bindedBdCharacter3;
def bindedBdCharacter : Character[] = bind bindedBdCharacter2;
checkFlag(true, "Character 1B");
resetFlag();
insert 62 into valueBdCharacter;
checkFlag(true, "Character 2B");
resetFlag();
var trashBdCharacter : Character[] = bindedBdCharacter;
checkFlag(false, "Character 3B");
if (trashBdCharacter != [64 + (6 as Character), 61 + (6 as Character), 62 + (6 as Character)]) println("FAILED Character 4B");
resetFlag();
*/

/***** Integer *****/
function fInteger(x : Integer) : Integer{
    flag = true;
    return x;
}

println("Integer eager+for");
var valueBdInteger : Integer[] = [1000000 as Integer, 1000001 as Integer] as Integer[];
def bindedBdInteger7 : Integer[] = bind valueBdInteger;
var bindedBdInteger6 : Integer[] = bind bindedBdInteger7;
def bindedBdInteger5 : Integer[] = bind bindedBdInteger6;
var bindedBdInteger4 : Integer[] = bind bindedBdInteger5;
def bindedBdInteger3 : Integer[] = bind
        for (item in bindedBdInteger4) item + fInteger(1000 as Integer);
var bindedBdInteger2 : Integer[] = bind bindedBdInteger3;
def bindedBdInteger : Integer[] = bind bindedBdInteger2;
checkFlag(true, "Integer 1B");
resetFlag();
insert 1000002 into valueBdInteger;
checkFlag(true, "Integer 2B");
resetFlag();
var trashBdInteger : Integer[] = bindedBdInteger;
checkFlag(false, "Integer 3B");
if (trashBdInteger != [1000000 + (1000 as Integer), 1000001 + (1000 as Integer), 1000002 + (1000 as Integer)]) println("FAILED Integer 4B");
resetFlag();

/***** Long *****/
function fLong(x : Long) : Long{
    flag = true;
    return x;
}

println("Long eager+for");
var valueBdLong : Long[] = [1000000000 as Long, 1000000001 as Long] as Long[];
def bindedBdLong7 : Long[] = bind valueBdLong;
var bindedBdLong6 : Long[] = bind bindedBdLong7;
def bindedBdLong5 : Long[] = bind bindedBdLong6;
var bindedBdLong4 : Long[] = bind bindedBdLong5;
def bindedBdLong3 : Long[] = bind
        for (item in bindedBdLong4) item + fLong(10000 as Long);
var bindedBdLong2 : Long[] = bind bindedBdLong3;
def bindedBdLong : Long[] = bind bindedBdLong2;
checkFlag(true, "Long 1B");
resetFlag();
insert 1000000002 into valueBdLong;
checkFlag(true, "Long 2B");
resetFlag();
var trashBdLong : Long[] = bindedBdLong;
checkFlag(false, "Long 3B");
if (trashBdLong != [1000000000 + (10000 as Long), 1000000001 + (10000 as Long), 1000000002 + (10000 as Long)]) println("FAILED Long 4B");
resetFlag();

/***** Float *****/
function fFloat(x : Float) : Float{
    flag = true;
    return x;
}

println("Float eager+for");
var valueBdFloat : Float[] = [10.5 as Float, 11.5 as Float] as Float[];
def bindedBdFloat7 : Float[] = bind valueBdFloat;
var bindedBdFloat6 : Float[] = bind bindedBdFloat7;
def bindedBdFloat5 : Float[] = bind bindedBdFloat6;
var bindedBdFloat4 : Float[] = bind bindedBdFloat5;
def bindedBdFloat3 : Float[] = bind
        for (item in bindedBdFloat4) item + fFloat(1.5 as Float);
var bindedBdFloat2 : Float[] = bind bindedBdFloat3;
def bindedBdFloat : Float[] = bind bindedBdFloat2;
checkFlag(true, "Float 1B");
resetFlag();
insert 12.5 into valueBdFloat;
checkFlag(true, "Float 2B");
resetFlag();
var trashBdFloat : Float[] = bindedBdFloat;
checkFlag(false, "Float 3B");
if (trashBdFloat != [10.5 + (1.5 as Float), 11.5 + (1.5 as Float), 12.5 + (1.5 as Float)]) println("FAILED Float 4B");
resetFlag();

/***** Double *****/
function fDouble(x : Double) : Double{
    flag = true;
    return x;
}

println("Double eager+for");
var valueBdDouble : Double[] = [1.25e4 as Double, 1.21e4 as Double] as Double[];
def bindedBdDouble7 : Double[] = bind valueBdDouble;
var bindedBdDouble6 : Double[] = bind bindedBdDouble7;
def bindedBdDouble5 : Double[] = bind bindedBdDouble6;
var bindedBdDouble4 : Double[] = bind bindedBdDouble5;
def bindedBdDouble3 : Double[] = bind
        for (item in bindedBdDouble4) item + fDouble(1.15e4 as Double);
var bindedBdDouble2 : Double[] = bind bindedBdDouble3;
def bindedBdDouble : Double[] = bind bindedBdDouble2;
checkFlag(true, "Double 1B");
resetFlag();
insert 1.22e4 into valueBdDouble;
checkFlag(true, "Double 2B");
resetFlag();
var trashBdDouble : Double[] = bindedBdDouble;
checkFlag(false, "Double 3B");
if (trashBdDouble != [1.25e4 + (1.15e4 as Double), 1.21e4 + (1.15e4 as Double), 1.22e4 + (1.15e4 as Double)]) println("FAILED Double 4B");
resetFlag();

/***** Number *****/
function fNumber(x : Number) : Number{
    flag = true;
    return x;
}

println("Number eager+for");
var valueBdNumber : Number[] = [100 as Number, 101 as Number] as Number[];
def bindedBdNumber7 : Number[] = bind valueBdNumber;
var bindedBdNumber6 : Number[] = bind bindedBdNumber7;
def bindedBdNumber5 : Number[] = bind bindedBdNumber6;
var bindedBdNumber4 : Number[] = bind bindedBdNumber5;
def bindedBdNumber3 : Number[] = bind
        for (item in bindedBdNumber4) item + fNumber(10 as Number);
var bindedBdNumber2 : Number[] = bind bindedBdNumber3;
def bindedBdNumber : Number[] = bind bindedBdNumber2;
checkFlag(true, "Number 1B");
resetFlag();
insert 102 into valueBdNumber;
checkFlag(true, "Number 2B");
resetFlag();
var trashBdNumber : Number[] = bindedBdNumber;
checkFlag(false, "Number 3B");
if (trashBdNumber != [100 + (10 as Number), 101 + (10 as Number), 102 + (10 as Number)]) println("FAILED Number 4B");
resetFlag();

/***** Duration *****/
/*
function fDuration(x : Duration) : Duration{
    flag = true;
    return x;
}

println("Duration eager+for");
var valueBdDuration : Duration[] = [10s as Duration, 11s as Duration] as Duration[];
def bindedBdDuration7 : Duration[] = bind valueBdDuration;
var bindedBdDuration6 : Duration[] = bind bindedBdDuration7;
def bindedBdDuration5 : Duration[] = bind bindedBdDuration6;
var bindedBdDuration4 : Duration[] = bind bindedBdDuration5;
def bindedBdDuration3 : Duration[] = bind
        for (item in bindedBdDuration4) item + fDuration(1s as Duration);
var bindedBdDuration2 : Duration[] = bind bindedBdDuration3;
def bindedBdDuration : Duration[] = bind bindedBdDuration2;
checkFlag(true, "Duration 1B");
resetFlag();
insert 12s into valueBdDuration;
checkFlag(true, "Duration 2B");
resetFlag();
var trashBdDuration : Duration[] = bindedBdDuration;
checkFlag(false, "Duration 3B");
if (trashBdDuration != [10s + (1s as Duration), 11s + (1s as Duration), 12s + (1s as Duration)]) println("FAILED Duration 4B");
resetFlag();

 */

/***** String *****/
function fString(x : String) : String{
    flag = true;
    return x;
}

println("String eager+for");
var valueBdString : String[] = ["xx" as String, "yy" as String] as String[];
def bindedBdString7 : String[] = bind valueBdString;
var bindedBdString6 : String[] = bind bindedBdString7;
def bindedBdString5 : String[] = bind bindedBdString6;
var bindedBdString4 : String[] = bind bindedBdString5;
def bindedBdString3 : String[] = bind
        for (item in bindedBdString4) "{item}{fString(valueBdString[0])}";
var bindedBdString2 : String[] = bind bindedBdString3;
def bindedBdString : String[] = bind bindedBdString2;
checkFlag(true, "String 1B");
resetFlag();
insert "ss" into valueBdString;
checkFlag(true, "String 2B");
resetFlag();
var trashBdString : String[] = bindedBdString;
checkFlag(false, "String 3B");
if (trashBdString != ["xxxx", "yyxx", "ssxx"]) println("FAILED String 4B");
resetFlag();

