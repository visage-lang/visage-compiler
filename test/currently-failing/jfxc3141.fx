/*
 * @test
 * @run/fail
 * 
 */

// When this isuue will be fixed, please, update test
// features/F25-lazy-bind/Lz/lzIfGlobal.fx

var flag : Boolean = false as Boolean;
function checkFlag (expected : Boolean, msg : String) {
   if (expected != flag) println("FAILED {msg}");
}
function fByte(x : Byte) : Byte{
   flag = true;
   return x;
}

var valueByte : Byte = 120 as Byte;
def bindedByte : Byte = bind lazy
       if(fByte(valueByte) == 122) then 1 else 0;
checkFlag(false, "Byte 1");
flag = false;
valueByte = 122 as Byte;
checkFlag(false, "Byte 2");
flag = false;
var trashByte : Byte = bindedByte;
checkFlag(true, "Byte 3");
if (trashByte != 1) println("FAILED Byte 4");
flag = false;
