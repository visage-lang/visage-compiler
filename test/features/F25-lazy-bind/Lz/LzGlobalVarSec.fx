/*
 * Lazy/eager binding for global sequence variables
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


/********** Lazy/eager Binding for global sequence variables **********/


/***** Byte *****/
function fByte(x : Byte[]) : Byte[]{
    flag = true;
    return [122, x];
}

println("Byte lazy+eager");
var valueByte : Byte[] = [120 as Byte, 121 as Byte] as Byte[];
def bindedByte7 : Byte[] = bind lazy valueByte;
var bindedByte6 : Byte[] = bind bindedByte7;
def bindedByte5 : Byte[] = bind lazy bindedByte6;
var bindedByte4 : Byte[] = bind bindedByte5;
def bindedByte3 : Byte[] = bind lazy fByte(bindedByte4);
var bindedByte2 : Byte[] = bind lazy bindedByte3;
def bindedByte : Byte[] = bind lazy bindedByte2;
checkFlag(false, "Byte 1");
insert 121 into valueByte;
checkFlag(false, "Byte 2");
var trashByte : Byte[] = bindedByte;
checkFlag(true, "Byte 3");
if (trashByte != [122, 120, 121]) println("FAILED Byte 4");
resetFlag();

println("Byte eager");
var valueBdByte : Byte[] = [120 as Byte, 121 as Byte] as Byte[];
def bindedBdByte7 : Byte[] = bind valueBdByte;
var bindedBdByte6 : Byte[] = bind bindedBdByte7;
def bindedBdByte5 : Byte[] = bind bindedBdByte6;
var bindedBdByte4 : Byte[] = bind bindedBdByte5;
def bindedBdByte3 : Byte[] = bind fByte(bindedBdByte4);
var bindedBdByte2 : Byte[] = bind bindedBdByte3;
def bindedBdByte : Byte[] = bind bindedBdByte2;
checkFlag(true, "Byte 1B");
resetFlag();
insert 121 into valueBdByte;
checkFlag(true, "Byte 2B");
resetFlag();
var trashBdByte : Byte[] = bindedBdByte;
checkFlag(false, "Byte 3B");
if (trashBdByte != [122, 120, 121, 121]) println("FAILED Byte 4B");
resetFlag();

/***** Short *****/
function fShort(x : Short[]) : Short[]{
    flag = true;
    return [30002, x];
}

println("Short lazy+eager");
var valueShort : Short[] = [30000 as Short, 30001 as Short] as Short[];
def bindedShort7 : Short[] = bind lazy valueShort;
var bindedShort6 : Short[] = bind bindedShort7;
def bindedShort5 : Short[] = bind lazy bindedShort6;
var bindedShort4 : Short[] = bind bindedShort5;
def bindedShort3 : Short[] = bind lazy fShort(bindedShort4);
var bindedShort2 : Short[] = bind lazy bindedShort3;
def bindedShort : Short[] = bind lazy bindedShort2;
checkFlag(false, "Short 1");
insert 30001 into valueShort;
checkFlag(false, "Short 2");
var trashShort : Short[] = bindedShort;
checkFlag(true, "Short 3");
if (trashShort != [30002, 30000, 30001]) println("FAILED Short 4");
resetFlag();

println("Short eager");
var valueBdShort : Short[] = [30000 as Short, 30001 as Short] as Short[];
def bindedBdShort7 : Short[] = bind valueBdShort;
var bindedBdShort6 : Short[] = bind bindedBdShort7;
def bindedBdShort5 : Short[] = bind bindedBdShort6;
var bindedBdShort4 : Short[] = bind bindedBdShort5;
def bindedBdShort3 : Short[] = bind fShort(bindedBdShort4);
var bindedBdShort2 : Short[] = bind bindedBdShort3;
def bindedBdShort : Short[] = bind bindedBdShort2;
checkFlag(true, "Short 1B");
resetFlag();
insert 30001 into valueBdShort;
checkFlag(true, "Short 2B");
resetFlag();
var trashBdShort : Short[] = bindedBdShort;
checkFlag(false, "Short 3B");
if (trashBdShort != [30002, 30000, 30001, 30001]) println("FAILED Short 4B");
resetFlag();

/***** Character *****/
function fCharacter(x : Character[]) : Character[]{
    flag = true;
    return [62, x];
}

println("Character lazy+eager");
var valueCharacter : Character[] = [64 as Character, 61 as Character] as Character[];
def bindedCharacter7 : Character[] = bind lazy valueCharacter;
var bindedCharacter6 : Character[] = bind bindedCharacter7;
def bindedCharacter5 : Character[] = bind lazy bindedCharacter6;
var bindedCharacter4 : Character[] = bind bindedCharacter5;
def bindedCharacter3 : Character[] = bind lazy fCharacter(bindedCharacter4);
var bindedCharacter2 : Character[] = bind lazy bindedCharacter3;
def bindedCharacter : Character[] = bind lazy bindedCharacter2;
checkFlag(false, "Character 1");
insert 61 into valueCharacter;
checkFlag(false, "Character 2");
var trashCharacter : Character[] = bindedCharacter;
checkFlag(true, "Character 3");
if (trashCharacter != [62, 64, 61]) println("FAILED Character 4");
resetFlag();

println("Character eager");
var valueBdCharacter : Character[] = [64 as Character, 61 as Character] as Character[];
def bindedBdCharacter7 : Character[] = bind valueBdCharacter;
var bindedBdCharacter6 : Character[] = bind bindedBdCharacter7;
def bindedBdCharacter5 : Character[] = bind bindedBdCharacter6;
var bindedBdCharacter4 : Character[] = bind bindedBdCharacter5;
def bindedBdCharacter3 : Character[] = bind fCharacter(bindedBdCharacter4);
var bindedBdCharacter2 : Character[] = bind bindedBdCharacter3;
def bindedBdCharacter : Character[] = bind bindedBdCharacter2;
checkFlag(true, "Character 1B");
resetFlag();
insert 61 into valueBdCharacter;
checkFlag(true, "Character 2B");
resetFlag();
var trashBdCharacter : Character[] = bindedBdCharacter;
checkFlag(false, "Character 3B");
if (trashBdCharacter != [62, 64, 61, 61]) println("FAILED Character 4B");
resetFlag();

/***** Integer *****/
function fInteger(x : Integer[]) : Integer[]{
    flag = true;
    return [1000002, x];
}

println("Integer lazy+eager");
var valueInteger : Integer[] = [1000000 as Integer, 1000001 as Integer] as Integer[];
def bindedInteger7 : Integer[] = bind lazy valueInteger;
var bindedInteger6 : Integer[] = bind bindedInteger7;
def bindedInteger5 : Integer[] = bind lazy bindedInteger6;
var bindedInteger4 : Integer[] = bind bindedInteger5;
def bindedInteger3 : Integer[] = bind lazy fInteger(bindedInteger4);
var bindedInteger2 : Integer[] = bind lazy bindedInteger3;
def bindedInteger : Integer[] = bind lazy bindedInteger2;
checkFlag(false, "Integer 1");
insert 1000001 into valueInteger;
checkFlag(false, "Integer 2");
var trashInteger : Integer[] = bindedInteger;
checkFlag(true, "Integer 3");
if (trashInteger != [1000002, 1000000, 1000001]) println("FAILED Integer 4");
resetFlag();

println("Integer eager");
var valueBdInteger : Integer[] = [1000000 as Integer, 1000001 as Integer] as Integer[];
def bindedBdInteger7 : Integer[] = bind valueBdInteger;
var bindedBdInteger6 : Integer[] = bind bindedBdInteger7;
def bindedBdInteger5 : Integer[] = bind bindedBdInteger6;
var bindedBdInteger4 : Integer[] = bind bindedBdInteger5;
def bindedBdInteger3 : Integer[] = bind fInteger(bindedBdInteger4);
var bindedBdInteger2 : Integer[] = bind bindedBdInteger3;
def bindedBdInteger : Integer[] = bind bindedBdInteger2;
checkFlag(true, "Integer 1B");
resetFlag();
insert 1000001 into valueBdInteger;
checkFlag(true, "Integer 2B");
resetFlag();
var trashBdInteger : Integer[] = bindedBdInteger;
checkFlag(false, "Integer 3B");
if (trashBdInteger != [1000002, 1000000, 1000001, 1000001]) println("FAILED Integer 4B");
resetFlag();

/***** Long *****/
function fLong(x : Long[]) : Long[]{
    flag = true;
    return [1000000002, x];
}

println("Long lazy+eager");
var valueLong : Long[] = [1000000000 as Long, 1000000001 as Long] as Long[];
def bindedLong7 : Long[] = bind lazy valueLong;
var bindedLong6 : Long[] = bind bindedLong7;
def bindedLong5 : Long[] = bind lazy bindedLong6;
var bindedLong4 : Long[] = bind bindedLong5;
def bindedLong3 : Long[] = bind lazy fLong(bindedLong4);
var bindedLong2 : Long[] = bind lazy bindedLong3;
def bindedLong : Long[] = bind lazy bindedLong2;
checkFlag(false, "Long 1");
insert 1000000001 into valueLong;
checkFlag(false, "Long 2");
var trashLong : Long[] = bindedLong;
checkFlag(true, "Long 3");
if (trashLong != [1000000002, 1000000000, 1000000001]) println("FAILED Long 4");
resetFlag();

println("Long eager");
var valueBdLong : Long[] = [1000000000 as Long, 1000000001 as Long] as Long[];
def bindedBdLong7 : Long[] = bind valueBdLong;
var bindedBdLong6 : Long[] = bind bindedBdLong7;
def bindedBdLong5 : Long[] = bind bindedBdLong6;
var bindedBdLong4 : Long[] = bind bindedBdLong5;
def bindedBdLong3 : Long[] = bind fLong(bindedBdLong4);
var bindedBdLong2 : Long[] = bind bindedBdLong3;
def bindedBdLong : Long[] = bind bindedBdLong2;
checkFlag(true, "Long 1B");
resetFlag();
insert 1000000001 into valueBdLong;
checkFlag(true, "Long 2B");
resetFlag();
var trashBdLong : Long[] = bindedBdLong;
checkFlag(false, "Long 3B");
if (trashBdLong != [1000000002, 1000000000, 1000000001, 1000000001]) println("FAILED Long 4B");
resetFlag();

/***** Float *****/
function fFloat(x : Float[]) : Float[]{
    flag = true;
    return [12.5, x];
}

println("Float lazy+eager");
var valueFloat : Float[] = [10.5 as Float, 11.5 as Float] as Float[];
def bindedFloat7 : Float[] = bind lazy valueFloat;
var bindedFloat6 : Float[] = bind bindedFloat7;
def bindedFloat5 : Float[] = bind lazy bindedFloat6;
var bindedFloat4 : Float[] = bind bindedFloat5;
def bindedFloat3 : Float[] = bind lazy fFloat(bindedFloat4);
var bindedFloat2 : Float[] = bind lazy bindedFloat3;
def bindedFloat : Float[] = bind lazy bindedFloat2;
checkFlag(false, "Float 1");
insert 11.5 into valueFloat;
checkFlag(false, "Float 2");
var trashFloat : Float[] = bindedFloat;
checkFlag(true, "Float 3");
if (trashFloat != [12.5, 10.5, 11.5]) println("FAILED Float 4");
resetFlag();

println("Float eager");
var valueBdFloat : Float[] = [10.5 as Float, 11.5 as Float] as Float[];
def bindedBdFloat7 : Float[] = bind valueBdFloat;
var bindedBdFloat6 : Float[] = bind bindedBdFloat7;
def bindedBdFloat5 : Float[] = bind bindedBdFloat6;
var bindedBdFloat4 : Float[] = bind bindedBdFloat5;
def bindedBdFloat3 : Float[] = bind fFloat(bindedBdFloat4);
var bindedBdFloat2 : Float[] = bind bindedBdFloat3;
def bindedBdFloat : Float[] = bind bindedBdFloat2;
checkFlag(true, "Float 1B");
resetFlag();
insert 11.5 into valueBdFloat;
checkFlag(true, "Float 2B");
resetFlag();
var trashBdFloat : Float[] = bindedBdFloat;
checkFlag(false, "Float 3B");
if (trashBdFloat != [12.5, 10.5, 11.5, 11.5]) println("FAILED Float 4B");
resetFlag();

/***** Double *****/
function fDouble(x : Double[]) : Double[]{
    flag = true;
    return [1.22e4, x];
}

println("Double lazy+eager");
var valueDouble : Double[] = [1.25e4 as Double, 1.21e4 as Double] as Double[];
def bindedDouble7 : Double[] = bind lazy valueDouble;
var bindedDouble6 : Double[] = bind bindedDouble7;
def bindedDouble5 : Double[] = bind lazy bindedDouble6;
var bindedDouble4 : Double[] = bind bindedDouble5;
def bindedDouble3 : Double[] = bind lazy fDouble(bindedDouble4);
var bindedDouble2 : Double[] = bind lazy bindedDouble3;
def bindedDouble : Double[] = bind lazy bindedDouble2;
checkFlag(false, "Double 1");
insert 1.21e4 into valueDouble;
checkFlag(false, "Double 2");
var trashDouble : Double[] = bindedDouble;
checkFlag(true, "Double 3");
if (trashDouble != [1.22e4, 1.25e4, 1.21e4]) println("FAILED Double 4");
resetFlag();

println("Double eager");
var valueBdDouble : Double[] = [1.25e4 as Double, 1.21e4 as Double] as Double[];
def bindedBdDouble7 : Double[] = bind valueBdDouble;
var bindedBdDouble6 : Double[] = bind bindedBdDouble7;
def bindedBdDouble5 : Double[] = bind bindedBdDouble6;
var bindedBdDouble4 : Double[] = bind bindedBdDouble5;
def bindedBdDouble3 : Double[] = bind fDouble(bindedBdDouble4);
var bindedBdDouble2 : Double[] = bind bindedBdDouble3;
def bindedBdDouble : Double[] = bind bindedBdDouble2;
checkFlag(true, "Double 1B");
resetFlag();
insert 1.21e4 into valueBdDouble;
checkFlag(true, "Double 2B");
resetFlag();
var trashBdDouble : Double[] = bindedBdDouble;
checkFlag(false, "Double 3B");
if (trashBdDouble != [1.22e4, 1.25e4, 1.21e4, 1.21e4]) println("FAILED Double 4B");
resetFlag();

/***** Number *****/
function fNumber(x : Number[]) : Number[]{
    flag = true;
    return [102, x];
}

println("Number lazy+eager");
var valueNumber : Number[] = [100 as Number, 101 as Number] as Number[];
def bindedNumber7 : Number[] = bind lazy valueNumber;
var bindedNumber6 : Number[] = bind bindedNumber7;
def bindedNumber5 : Number[] = bind lazy bindedNumber6;
var bindedNumber4 : Number[] = bind bindedNumber5;
def bindedNumber3 : Number[] = bind lazy fNumber(bindedNumber4);
var bindedNumber2 : Number[] = bind lazy bindedNumber3;
def bindedNumber : Number[] = bind lazy bindedNumber2;
checkFlag(false, "Number 1");
insert 101 into valueNumber;
checkFlag(false, "Number 2");
var trashNumber : Number[] = bindedNumber;
checkFlag(true, "Number 3");
if (trashNumber != [102, 100, 101]) println("FAILED Number 4");
resetFlag();

println("Number eager");
var valueBdNumber : Number[] = [100 as Number, 101 as Number] as Number[];
def bindedBdNumber7 : Number[] = bind valueBdNumber;
var bindedBdNumber6 : Number[] = bind bindedBdNumber7;
def bindedBdNumber5 : Number[] = bind bindedBdNumber6;
var bindedBdNumber4 : Number[] = bind bindedBdNumber5;
def bindedBdNumber3 : Number[] = bind fNumber(bindedBdNumber4);
var bindedBdNumber2 : Number[] = bind bindedBdNumber3;
def bindedBdNumber : Number[] = bind bindedBdNumber2;
checkFlag(true, "Number 1B");
resetFlag();
insert 101 into valueBdNumber;
checkFlag(true, "Number 2B");
resetFlag();
var trashBdNumber : Number[] = bindedBdNumber;
checkFlag(false, "Number 3B");
if (trashBdNumber != [102, 100, 101, 101]) println("FAILED Number 4B");
resetFlag();

/***** String *****/
function fString(x : String[]) : String[]{
    flag = true;
    return ["-52", x];
}

println("String lazy+eager");
var valueString : String[] = ["-50" as String, "-51" as String] as String[];
def bindedString7 : String[] = bind lazy valueString;
var bindedString6 : String[] = bind bindedString7;
def bindedString5 : String[] = bind lazy bindedString6;
var bindedString4 : String[] = bind bindedString5;
def bindedString3 : String[] = bind lazy fString(bindedString4);
var bindedString2 : String[] = bind lazy bindedString3;
def bindedString : String[] = bind lazy bindedString2;
checkFlag(false, "String 1");
insert "-51" into valueString;
checkFlag(false, "String 2");
var trashString : String[] = bindedString;
checkFlag(true, "String 3");
if (trashString != ["-52", "-50", "-51"]) println("FAILED String 4");
resetFlag();

println("String eager");
var valueBdString : String[] = ["-50" as String, "-51" as String] as String[];
def bindedBdString7 : String[] = bind valueBdString;
var bindedBdString6 : String[] = bind bindedBdString7;
def bindedBdString5 : String[] = bind bindedBdString6;
var bindedBdString4 : String[] = bind bindedBdString5;
def bindedBdString3 : String[] = bind fString(bindedBdString4);
var bindedBdString2 : String[] = bind bindedBdString3;
def bindedBdString : String[] = bind bindedBdString2;
checkFlag(true, "String 1B");
resetFlag();
insert "-51" into valueBdString;
checkFlag(true, "String 2B");
resetFlag();
var trashBdString : String[] = bindedBdString;
checkFlag(false, "String 3B");
if (trashBdString != ["-52", "-50", "-51", "-51"]) println("FAILED String 4B");
resetFlag();

/***** Boolean *****/
function fBoolean(x : Boolean[]) : Boolean[]{
    flag = true;
    return [true, x];
}

println("Boolean lazy+eager");
var valueBoolean : Boolean[] = [true as Boolean, false as Boolean] as Boolean[];
def bindedBoolean7 : Boolean[] = bind lazy valueBoolean;
var bindedBoolean6 : Boolean[] = bind bindedBoolean7;
def bindedBoolean5 : Boolean[] = bind lazy bindedBoolean6;
var bindedBoolean4 : Boolean[] = bind bindedBoolean5;
def bindedBoolean3 : Boolean[] = bind lazy fBoolean(bindedBoolean4);
var bindedBoolean2 : Boolean[] = bind lazy bindedBoolean3;
def bindedBoolean : Boolean[] = bind lazy bindedBoolean2;
checkFlag(false, "Boolean 1");
insert false into valueBoolean;
checkFlag(false, "Boolean 2");
var trashBoolean : Boolean[] = bindedBoolean;
checkFlag(true, "Boolean 3");
if (trashBoolean != [true, true, false]) println("FAILED Boolean 4");
resetFlag();

println("Boolean eager");
var valueBdBoolean : Boolean[] = [true as Boolean, false as Boolean] as Boolean[];
def bindedBdBoolean7 : Boolean[] = bind valueBdBoolean;
var bindedBdBoolean6 : Boolean[] = bind bindedBdBoolean7;
def bindedBdBoolean5 : Boolean[] = bind bindedBdBoolean6;
var bindedBdBoolean4 : Boolean[] = bind bindedBdBoolean5;
def bindedBdBoolean3 : Boolean[] = bind fBoolean(bindedBdBoolean4);
var bindedBdBoolean2 : Boolean[] = bind bindedBdBoolean3;
def bindedBdBoolean : Boolean[] = bind bindedBdBoolean2;
checkFlag(true, "Boolean 1B");
resetFlag();
insert false into valueBdBoolean;
checkFlag(true, "Boolean 2B");
resetFlag();
var trashBdBoolean : Boolean[] = bindedBdBoolean;
checkFlag(false, "Boolean 3B");
if (trashBdBoolean != [true, true, false, false]) println("FAILED Boolean 4B");
resetFlag();

/***** Duration *****/
function fDuration(x : Duration[]) : Duration[]{
    flag = true;
    return [12s, x];
}

println("Duration lazy+eager");
var valueDuration : Duration[] = [10s as Duration, 11s as Duration] as Duration[];
def bindedDuration7 : Duration[] = bind lazy valueDuration;
var bindedDuration6 : Duration[] = bind bindedDuration7;
def bindedDuration5 : Duration[] = bind lazy bindedDuration6;
var bindedDuration4 : Duration[] = bind bindedDuration5;
def bindedDuration3 : Duration[] = bind lazy fDuration(bindedDuration4);
var bindedDuration2 : Duration[] = bind lazy bindedDuration3;
def bindedDuration : Duration[] = bind lazy bindedDuration2;
checkFlag(false, "Duration 1");
insert 11s into valueDuration;
checkFlag(false, "Duration 2");
var trashDuration : Duration[] = bindedDuration;
checkFlag(true, "Duration 3");
if (trashDuration != [12s, 10s, 11s]) println("FAILED Duration 4");
resetFlag();

println("Duration eager");
var valueBdDuration : Duration[] = [10s as Duration, 11s as Duration] as Duration[];
def bindedBdDuration7 : Duration[] = bind valueBdDuration;
var bindedBdDuration6 : Duration[] = bind bindedBdDuration7;
def bindedBdDuration5 : Duration[] = bind bindedBdDuration6;
var bindedBdDuration4 : Duration[] = bind bindedBdDuration5;
def bindedBdDuration3 : Duration[] = bind fDuration(bindedBdDuration4);
var bindedBdDuration2 : Duration[] = bind bindedBdDuration3;
def bindedBdDuration : Duration[] = bind bindedBdDuration2;
checkFlag(true, "Duration 1B");
resetFlag();
insert 11s into valueBdDuration;
checkFlag(true, "Duration 2B");
resetFlag();
var trashBdDuration : Duration[] = bindedBdDuration;
checkFlag(false, "Duration 3B");
if (trashBdDuration != [12s, 10s, 11s, 11s]) println("FAILED Duration 4B");
resetFlag();

/********** End: Lazy/eager Binding for global sequence variables **********/

/********** Lazy/eager Binding for sequence variables in a global function **********/
function testGlobalFuncSec() {
    /***** Byte[] *****/
    println("Byte lazy+eager in function");
    var valueFuncByte : Byte[] = [120 as Byte, 121 as Byte] as Byte[];
    def bindedFuncByte7 : Byte[] = bind lazy valueFuncByte;
    var bindedFuncByte6 : Byte[] = bind bindedFuncByte7;
    def bindedFuncByte5 : Byte[] = bind lazy bindedFuncByte6;
    var bindedFuncByte4 : Byte[] = bind bindedFuncByte5;
    def bindedFuncByte3 : Byte[] = bind lazy fByte(bindedFuncByte4);
    var bindedFuncByte2 : Byte[] = bind lazy bindedFuncByte3;
    def bindedFuncByte : Byte[] = bind lazy bindedFuncByte2;
    checkFlag(false, "Byte 1f");
    insert 121 into valueFuncByte;
    checkFlag(false, "Byte 2f");
    var trashFuncByte : Byte[] = bindedFuncByte;
    checkFlag(true, "Byte 3f");
    if (trashFuncByte != [122, 120, 121]) println("FAILED Byte 4f");
    resetFlag();

    println("Byte eager in function");
    var valueFuncBdByte : Byte[] = [120 as Byte, 121 as Byte] as Byte[];
    def bindedFuncBdByte7 : Byte[] = bind valueFuncBdByte;
    var bindedFuncBdByte6 : Byte[] = bind bindedFuncBdByte7;
    def bindedFuncBdByte5 : Byte[] = bind bindedFuncBdByte6;
    var bindedFuncBdByte4 : Byte[] = bind bindedFuncBdByte5;
    def bindedFuncBdByte3 : Byte[] = bind fByte(bindedFuncBdByte4);
    var bindedFuncBdByte2 : Byte[] = bind bindedFuncBdByte3;
    def bindedFuncBdByte : Byte[] = bind bindedFuncBdByte2;
    checkFlag(true, "Byte 1Bf");
    resetFlag();
    insert 121 into valueFuncBdByte;
    checkFlag(true, "Byte 2Bf");
    resetFlag();
    var trashFuncBdByte : Byte[] = bindedFuncBdByte;
    checkFlag(false, "Byte 3Bf");
    if (trashFuncBdByte != [122, 120, 121, 121]) println("FAILED Byte 4Bf");
    resetFlag();

    /***** Short[] *****/
    println("Short lazy+eager in function");
    var valueFuncShort : Short[] = [30000 as Short, 30001 as Short] as Short[];
    def bindedFuncShort7 : Short[] = bind lazy valueFuncShort;
    var bindedFuncShort6 : Short[] = bind bindedFuncShort7;
    def bindedFuncShort5 : Short[] = bind lazy bindedFuncShort6;
    var bindedFuncShort4 : Short[] = bind bindedFuncShort5;
    def bindedFuncShort3 : Short[] = bind lazy fShort(bindedFuncShort4);
    var bindedFuncShort2 : Short[] = bind lazy bindedFuncShort3;
    def bindedFuncShort : Short[] = bind lazy bindedFuncShort2;
    checkFlag(false, "Short 1f");
    insert 30001 into valueFuncShort;
    checkFlag(false, "Short 2f");
    var trashFuncShort : Short[] = bindedFuncShort;
    checkFlag(true, "Short 3f");
    if (trashFuncShort != [30002, 30000, 30001]) println("FAILED Short 4f");
    resetFlag();

    println("Short eager in function");
    var valueFuncBdShort : Short[] = [30000 as Short, 30001 as Short] as Short[];
    def bindedFuncBdShort7 : Short[] = bind valueFuncBdShort;
    var bindedFuncBdShort6 : Short[] = bind bindedFuncBdShort7;
    def bindedFuncBdShort5 : Short[] = bind bindedFuncBdShort6;
    var bindedFuncBdShort4 : Short[] = bind bindedFuncBdShort5;
    def bindedFuncBdShort3 : Short[] = bind fShort(bindedFuncBdShort4);
    var bindedFuncBdShort2 : Short[] = bind bindedFuncBdShort3;
    def bindedFuncBdShort : Short[] = bind bindedFuncBdShort2;
    checkFlag(true, "Short 1Bf");
    resetFlag();
    insert 30001 into valueFuncBdShort;
    checkFlag(true, "Short 2Bf");
    resetFlag();
    var trashFuncBdShort : Short[] = bindedFuncBdShort;
    checkFlag(false, "Short 3Bf");
    if (trashFuncBdShort != [30002, 30000, 30001, 30001]) println("FAILED Short 4Bf");
    resetFlag();

    /***** Character[] *****/
    println("Character lazy+eager in function");
    var valueFuncCharacter : Character[] = [64 as Character, 61 as Character] as Character[];
    def bindedFuncCharacter7 : Character[] = bind lazy valueFuncCharacter;
    var bindedFuncCharacter6 : Character[] = bind bindedFuncCharacter7;
    def bindedFuncCharacter5 : Character[] = bind lazy bindedFuncCharacter6;
    var bindedFuncCharacter4 : Character[] = bind bindedFuncCharacter5;
    def bindedFuncCharacter3 : Character[] = bind lazy fCharacter(bindedFuncCharacter4);
    var bindedFuncCharacter2 : Character[] = bind lazy bindedFuncCharacter3;
    def bindedFuncCharacter : Character[] = bind lazy bindedFuncCharacter2;
    checkFlag(false, "Character 1f");
    insert 61 into valueFuncCharacter;
    checkFlag(false, "Character 2f");
    var trashFuncCharacter : Character[] = bindedFuncCharacter;
    checkFlag(true, "Character 3f");
    if (trashFuncCharacter != [62, 64, 61]) println("FAILED Character 4f");
    resetFlag();

    println("Character eager in function");
    var valueFuncBdCharacter : Character[] = [64 as Character, 61 as Character] as Character[];
    def bindedFuncBdCharacter7 : Character[] = bind valueFuncBdCharacter;
    var bindedFuncBdCharacter6 : Character[] = bind bindedFuncBdCharacter7;
    def bindedFuncBdCharacter5 : Character[] = bind bindedFuncBdCharacter6;
    var bindedFuncBdCharacter4 : Character[] = bind bindedFuncBdCharacter5;
    def bindedFuncBdCharacter3 : Character[] = bind fCharacter(bindedFuncBdCharacter4);
    var bindedFuncBdCharacter2 : Character[] = bind bindedFuncBdCharacter3;
    def bindedFuncBdCharacter : Character[] = bind bindedFuncBdCharacter2;
    checkFlag(true, "Character 1Bf");
    resetFlag();
    insert 61 into valueFuncBdCharacter;
    checkFlag(true, "Character 2Bf");
    resetFlag();
    var trashFuncBdCharacter : Character[] = bindedFuncBdCharacter;
    checkFlag(false, "Character 3Bf");
    if (trashFuncBdCharacter != [62, 64, 61, 61]) println("FAILED Character 4Bf");
    resetFlag();

    /***** Integer[] *****/
    println("Integer lazy+eager in function");
    var valueFuncInteger : Integer[] = [1000000 as Integer, 1000001 as Integer] as Integer[];
    def bindedFuncInteger7 : Integer[] = bind lazy valueFuncInteger;
    var bindedFuncInteger6 : Integer[] = bind bindedFuncInteger7;
    def bindedFuncInteger5 : Integer[] = bind lazy bindedFuncInteger6;
    var bindedFuncInteger4 : Integer[] = bind bindedFuncInteger5;
    def bindedFuncInteger3 : Integer[] = bind lazy fInteger(bindedFuncInteger4);
    var bindedFuncInteger2 : Integer[] = bind lazy bindedFuncInteger3;
    def bindedFuncInteger : Integer[] = bind lazy bindedFuncInteger2;
    checkFlag(false, "Integer 1f");
    insert 1000001 into valueFuncInteger;
    checkFlag(false, "Integer 2f");
    var trashFuncInteger : Integer[] = bindedFuncInteger;
    checkFlag(true, "Integer 3f");
    if (trashFuncInteger != [1000002, 1000000, 1000001]) println("FAILED Integer 4f");
    resetFlag();

    println("Integer eager in function");
    var valueFuncBdInteger : Integer[] = [1000000 as Integer, 1000001 as Integer] as Integer[];
    def bindedFuncBdInteger7 : Integer[] = bind valueFuncBdInteger;
    var bindedFuncBdInteger6 : Integer[] = bind bindedFuncBdInteger7;
    def bindedFuncBdInteger5 : Integer[] = bind bindedFuncBdInteger6;
    var bindedFuncBdInteger4 : Integer[] = bind bindedFuncBdInteger5;
    def bindedFuncBdInteger3 : Integer[] = bind fInteger(bindedFuncBdInteger4);
    var bindedFuncBdInteger2 : Integer[] = bind bindedFuncBdInteger3;
    def bindedFuncBdInteger : Integer[] = bind bindedFuncBdInteger2;
    checkFlag(true, "Integer 1Bf");
    resetFlag();
    insert 1000001 into valueFuncBdInteger;
    checkFlag(true, "Integer 2Bf");
    resetFlag();
    var trashFuncBdInteger : Integer[] = bindedFuncBdInteger;
    checkFlag(false, "Integer 3Bf");
    if (trashFuncBdInteger != [1000002, 1000000, 1000001, 1000001]) println("FAILED Integer 4Bf");
    resetFlag();

    /***** Long[] *****/
    println("Long lazy+eager in function");
    var valueFuncLong : Long[] = [1000000000 as Long, 1000000001 as Long] as Long[];
    def bindedFuncLong7 : Long[] = bind lazy valueFuncLong;
    var bindedFuncLong6 : Long[] = bind bindedFuncLong7;
    def bindedFuncLong5 : Long[] = bind lazy bindedFuncLong6;
    var bindedFuncLong4 : Long[] = bind bindedFuncLong5;
    def bindedFuncLong3 : Long[] = bind lazy fLong(bindedFuncLong4);
    var bindedFuncLong2 : Long[] = bind lazy bindedFuncLong3;
    def bindedFuncLong : Long[] = bind lazy bindedFuncLong2;
    checkFlag(false, "Long 1f");
    insert 1000000001 into valueFuncLong;
    checkFlag(false, "Long 2f");
    var trashFuncLong : Long[] = bindedFuncLong;
    checkFlag(true, "Long 3f");
    if (trashFuncLong != [1000000002, 1000000000, 1000000001]) println("FAILED Long 4f");
    resetFlag();

    println("Long eager in function");
    var valueFuncBdLong : Long[] = [1000000000 as Long, 1000000001 as Long] as Long[];
    def bindedFuncBdLong7 : Long[] = bind valueFuncBdLong;
    var bindedFuncBdLong6 : Long[] = bind bindedFuncBdLong7;
    def bindedFuncBdLong5 : Long[] = bind bindedFuncBdLong6;
    var bindedFuncBdLong4 : Long[] = bind bindedFuncBdLong5;
    def bindedFuncBdLong3 : Long[] = bind fLong(bindedFuncBdLong4);
    var bindedFuncBdLong2 : Long[] = bind bindedFuncBdLong3;
    def bindedFuncBdLong : Long[] = bind bindedFuncBdLong2;
    checkFlag(true, "Long 1Bf");
    resetFlag();
    insert 1000000001 into valueFuncBdLong;
    checkFlag(true, "Long 2Bf");
    resetFlag();
    var trashFuncBdLong : Long[] = bindedFuncBdLong;
    checkFlag(false, "Long 3Bf");
    if (trashFuncBdLong != [1000000002, 1000000000, 1000000001, 1000000001]) println("FAILED Long 4Bf");
    resetFlag();

    /***** Float[] *****/
    println("Float lazy+eager in function");
    var valueFuncFloat : Float[] = [10.5 as Float, 11.5 as Float] as Float[];
    def bindedFuncFloat7 : Float[] = bind lazy valueFuncFloat;
    var bindedFuncFloat6 : Float[] = bind bindedFuncFloat7;
    def bindedFuncFloat5 : Float[] = bind lazy bindedFuncFloat6;
    var bindedFuncFloat4 : Float[] = bind bindedFuncFloat5;
    def bindedFuncFloat3 : Float[] = bind lazy fFloat(bindedFuncFloat4);
    var bindedFuncFloat2 : Float[] = bind lazy bindedFuncFloat3;
    def bindedFuncFloat : Float[] = bind lazy bindedFuncFloat2;
    checkFlag(false, "Float 1f");
    insert 11.5 into valueFuncFloat;
    checkFlag(false, "Float 2f");
    var trashFuncFloat : Float[] = bindedFuncFloat;
    checkFlag(true, "Float 3f");
    if (trashFuncFloat != [12.5, 10.5, 11.5]) println("FAILED Float 4f");
    resetFlag();

    println("Float eager in function");
    var valueFuncBdFloat : Float[] = [10.5 as Float, 11.5 as Float] as Float[];
    def bindedFuncBdFloat7 : Float[] = bind valueFuncBdFloat;
    var bindedFuncBdFloat6 : Float[] = bind bindedFuncBdFloat7;
    def bindedFuncBdFloat5 : Float[] = bind bindedFuncBdFloat6;
    var bindedFuncBdFloat4 : Float[] = bind bindedFuncBdFloat5;
    def bindedFuncBdFloat3 : Float[] = bind fFloat(bindedFuncBdFloat4);
    var bindedFuncBdFloat2 : Float[] = bind bindedFuncBdFloat3;
    def bindedFuncBdFloat : Float[] = bind bindedFuncBdFloat2;
    checkFlag(true, "Float 1Bf");
    resetFlag();
    insert 11.5 into valueFuncBdFloat;
    checkFlag(true, "Float 2Bf");
    resetFlag();
    var trashFuncBdFloat : Float[] = bindedFuncBdFloat;
    checkFlag(false, "Float 3Bf");
    if (trashFuncBdFloat != [12.5, 10.5, 11.5, 11.5]) println("FAILED Float 4Bf");
    resetFlag();

    /***** Double[] *****/
    println("Double lazy+eager in function");
    var valueFuncDouble : Double[] = [1.25e4 as Double, 1.21e4 as Double] as Double[];
    def bindedFuncDouble7 : Double[] = bind lazy valueFuncDouble;
    var bindedFuncDouble6 : Double[] = bind bindedFuncDouble7;
    def bindedFuncDouble5 : Double[] = bind lazy bindedFuncDouble6;
    var bindedFuncDouble4 : Double[] = bind bindedFuncDouble5;
    def bindedFuncDouble3 : Double[] = bind lazy fDouble(bindedFuncDouble4);
    var bindedFuncDouble2 : Double[] = bind lazy bindedFuncDouble3;
    def bindedFuncDouble : Double[] = bind lazy bindedFuncDouble2;
    checkFlag(false, "Double 1f");
    insert 1.21e4 into valueFuncDouble;
    checkFlag(false, "Double 2f");
    var trashFuncDouble : Double[] = bindedFuncDouble;
    checkFlag(true, "Double 3f");
    if (trashFuncDouble != [1.22e4, 1.25e4, 1.21e4]) println("FAILED Double 4f");
    resetFlag();

    println("Double eager in function");
    var valueFuncBdDouble : Double[] = [1.25e4 as Double, 1.21e4 as Double] as Double[];
    def bindedFuncBdDouble7 : Double[] = bind valueFuncBdDouble;
    var bindedFuncBdDouble6 : Double[] = bind bindedFuncBdDouble7;
    def bindedFuncBdDouble5 : Double[] = bind bindedFuncBdDouble6;
    var bindedFuncBdDouble4 : Double[] = bind bindedFuncBdDouble5;
    def bindedFuncBdDouble3 : Double[] = bind fDouble(bindedFuncBdDouble4);
    var bindedFuncBdDouble2 : Double[] = bind bindedFuncBdDouble3;
    def bindedFuncBdDouble : Double[] = bind bindedFuncBdDouble2;
    checkFlag(true, "Double 1Bf");
    resetFlag();
    insert 1.21e4 into valueFuncBdDouble;
    checkFlag(true, "Double 2Bf");
    resetFlag();
    var trashFuncBdDouble : Double[] = bindedFuncBdDouble;
    checkFlag(false, "Double 3Bf");
    if (trashFuncBdDouble != [1.22e4, 1.25e4, 1.21e4, 1.21e4]) println("FAILED Double 4Bf");
    resetFlag();

    /***** Number[] *****/
    println("Number lazy+eager in function");
    var valueFuncNumber : Number[] = [100 as Number, 101 as Number] as Number[];
    def bindedFuncNumber7 : Number[] = bind lazy valueFuncNumber;
    var bindedFuncNumber6 : Number[] = bind bindedFuncNumber7;
    def bindedFuncNumber5 : Number[] = bind lazy bindedFuncNumber6;
    var bindedFuncNumber4 : Number[] = bind bindedFuncNumber5;
    def bindedFuncNumber3 : Number[] = bind lazy fNumber(bindedFuncNumber4);
    var bindedFuncNumber2 : Number[] = bind lazy bindedFuncNumber3;
    def bindedFuncNumber : Number[] = bind lazy bindedFuncNumber2;
    checkFlag(false, "Number 1f");
    insert 101 into valueFuncNumber;
    checkFlag(false, "Number 2f");
    var trashFuncNumber : Number[] = bindedFuncNumber;
    checkFlag(true, "Number 3f");
    if (trashFuncNumber != [102, 100, 101]) println("FAILED Number 4f");
    resetFlag();

    println("Number eager in function");
    var valueFuncBdNumber : Number[] = [100 as Number, 101 as Number] as Number[];
    def bindedFuncBdNumber7 : Number[] = bind valueFuncBdNumber;
    var bindedFuncBdNumber6 : Number[] = bind bindedFuncBdNumber7;
    def bindedFuncBdNumber5 : Number[] = bind bindedFuncBdNumber6;
    var bindedFuncBdNumber4 : Number[] = bind bindedFuncBdNumber5;
    def bindedFuncBdNumber3 : Number[] = bind fNumber(bindedFuncBdNumber4);
    var bindedFuncBdNumber2 : Number[] = bind bindedFuncBdNumber3;
    def bindedFuncBdNumber : Number[] = bind bindedFuncBdNumber2;
    checkFlag(true, "Number 1Bf");
    resetFlag();
    insert 101 into valueFuncBdNumber;
    checkFlag(true, "Number 2Bf");
    resetFlag();
    var trashFuncBdNumber : Number[] = bindedFuncBdNumber;
    checkFlag(false, "Number 3Bf");
    if (trashFuncBdNumber != [102, 100, 101, 101]) println("FAILED Number 4Bf");
    resetFlag();

    /***** String[] *****/
    println("String lazy+eager in function");
    var valueFuncString : String[] = ["-50" as String, "-51" as String] as String[];
    def bindedFuncString7 : String[] = bind lazy valueFuncString;
    var bindedFuncString6 : String[] = bind bindedFuncString7;
    def bindedFuncString5 : String[] = bind lazy bindedFuncString6;
    var bindedFuncString4 : String[] = bind bindedFuncString5;
    def bindedFuncString3 : String[] = bind lazy fString(bindedFuncString4);
    var bindedFuncString2 : String[] = bind lazy bindedFuncString3;
    def bindedFuncString : String[] = bind lazy bindedFuncString2;
    checkFlag(false, "String 1f");
    insert "-51" into valueFuncString;
    checkFlag(false, "String 2f");
    var trashFuncString : String[] = bindedFuncString;
    checkFlag(true, "String 3f");
    if (trashFuncString != ["-52", "-50", "-51"]) println("FAILED String 4f");
    resetFlag();

    println("String eager in function");
    var valueFuncBdString : String[] = ["-50" as String, "-51" as String] as String[];
    def bindedFuncBdString7 : String[] = bind valueFuncBdString;
    var bindedFuncBdString6 : String[] = bind bindedFuncBdString7;
    def bindedFuncBdString5 : String[] = bind bindedFuncBdString6;
    var bindedFuncBdString4 : String[] = bind bindedFuncBdString5;
    def bindedFuncBdString3 : String[] = bind fString(bindedFuncBdString4);
    var bindedFuncBdString2 : String[] = bind bindedFuncBdString3;
    def bindedFuncBdString : String[] = bind bindedFuncBdString2;
    checkFlag(true, "String 1Bf");
    resetFlag();
    insert "-51" into valueFuncBdString;
    checkFlag(true, "String 2Bf");
    resetFlag();
    var trashFuncBdString : String[] = bindedFuncBdString;
    checkFlag(false, "String 3Bf");
    if (trashFuncBdString != ["-52", "-50", "-51", "-51"]) println("FAILED String 4Bf");
    resetFlag();

    /***** Boolean[] *****/
    println("Boolean lazy+eager in function");
    var valueFuncBoolean : Boolean[] = [true as Boolean, false as Boolean] as Boolean[];
    def bindedFuncBoolean7 : Boolean[] = bind lazy valueFuncBoolean;
    var bindedFuncBoolean6 : Boolean[] = bind bindedFuncBoolean7;
    def bindedFuncBoolean5 : Boolean[] = bind lazy bindedFuncBoolean6;
    var bindedFuncBoolean4 : Boolean[] = bind bindedFuncBoolean5;
    def bindedFuncBoolean3 : Boolean[] = bind lazy fBoolean(bindedFuncBoolean4);
    var bindedFuncBoolean2 : Boolean[] = bind lazy bindedFuncBoolean3;
    def bindedFuncBoolean : Boolean[] = bind lazy bindedFuncBoolean2;
    checkFlag(false, "Boolean 1f");
    insert false into valueFuncBoolean;
    checkFlag(false, "Boolean 2f");
    var trashFuncBoolean : Boolean[] = bindedFuncBoolean;
    checkFlag(true, "Boolean 3f");
    if (trashFuncBoolean != [true, true, false]) println("FAILED Boolean 4f");
    resetFlag();

    println("Boolean eager in function");
    var valueFuncBdBoolean : Boolean[] = [true as Boolean, false as Boolean] as Boolean[];
    def bindedFuncBdBoolean7 : Boolean[] = bind valueFuncBdBoolean;
    var bindedFuncBdBoolean6 : Boolean[] = bind bindedFuncBdBoolean7;
    def bindedFuncBdBoolean5 : Boolean[] = bind bindedFuncBdBoolean6;
    var bindedFuncBdBoolean4 : Boolean[] = bind bindedFuncBdBoolean5;
    def bindedFuncBdBoolean3 : Boolean[] = bind fBoolean(bindedFuncBdBoolean4);
    var bindedFuncBdBoolean2 : Boolean[] = bind bindedFuncBdBoolean3;
    def bindedFuncBdBoolean : Boolean[] = bind bindedFuncBdBoolean2;
    checkFlag(true, "Boolean 1Bf");
    resetFlag();
    insert false into valueFuncBdBoolean;
    checkFlag(true, "Boolean 2Bf");
    resetFlag();
    var trashFuncBdBoolean : Boolean[] = bindedFuncBdBoolean;
    checkFlag(false, "Boolean 3Bf");
    if (trashFuncBdBoolean != [true, true, false, false]) println("FAILED Boolean 4Bf");
    resetFlag();

    /***** Duration[] *****/
    println("Duration lazy+eager in function");
    var valueFuncDuration : Duration[] = [10s as Duration, 11s as Duration] as Duration[];
    def bindedFuncDuration7 : Duration[] = bind lazy valueFuncDuration;
    var bindedFuncDuration6 : Duration[] = bind bindedFuncDuration7;
    def bindedFuncDuration5 : Duration[] = bind lazy bindedFuncDuration6;
    var bindedFuncDuration4 : Duration[] = bind bindedFuncDuration5;
    def bindedFuncDuration3 : Duration[] = bind lazy fDuration(bindedFuncDuration4);
    var bindedFuncDuration2 : Duration[] = bind lazy bindedFuncDuration3;
    def bindedFuncDuration : Duration[] = bind lazy bindedFuncDuration2;
    checkFlag(false, "Duration 1f");
    insert 11s into valueFuncDuration;
    checkFlag(false, "Duration 2f");
    var trashFuncDuration : Duration[] = bindedFuncDuration;
    checkFlag(true, "Duration 3f");
    if (trashFuncDuration != [12s, 10s, 11s]) println("FAILED Duration 4f");
    resetFlag();

    println("Duration eager in function");
    var valueFuncBdDuration : Duration[] = [10s as Duration, 11s as Duration] as Duration[];
    def bindedFuncBdDuration7 : Duration[] = bind valueFuncBdDuration;
    var bindedFuncBdDuration6 : Duration[] = bind bindedFuncBdDuration7;
    def bindedFuncBdDuration5 : Duration[] = bind bindedFuncBdDuration6;
    var bindedFuncBdDuration4 : Duration[] = bind bindedFuncBdDuration5;
    def bindedFuncBdDuration3 : Duration[] = bind fDuration(bindedFuncBdDuration4);
    var bindedFuncBdDuration2 : Duration[] = bind bindedFuncBdDuration3;
    def bindedFuncBdDuration : Duration[] = bind bindedFuncBdDuration2;
    checkFlag(true, "Duration 1Bf");
    resetFlag();
    insert 11s into valueFuncBdDuration;
    checkFlag(true, "Duration 2Bf");
    resetFlag();
    var trashFuncBdDuration : Duration[] = bindedFuncBdDuration;
    checkFlag(false, "Duration 3Bf");
    if (trashFuncBdDuration != [12s, 10s, 11s, 11s]) println("FAILED Duration 4Bf");
    resetFlag();

}
testGlobalFuncSec();
/********** END: Lazy/eager Binding for sequence variables in a global function **********/
