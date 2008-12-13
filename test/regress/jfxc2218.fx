/**
 * jfx2218:On cast, convert nullable String/Duration value
 * @test
 * @run
 */

sun.io.ByteToCharEUC_TW.unicodeCNS2 = null;

println("cast from java String should be empty: /{sun.io.ByteToCharEUC_TW.unicodeCNS2 as String}/");
println("cast from java String method should be empty: /{java.lang.System.getProperty("abc") as String}/");

var obj1: Object = null;
println("cast to Duration from Java Object should be 0ms: {obj1 as Duration}");

obj1 = java.lang.System.getProperty("abc");
println("cast to Duration from Java Object should be 0ms: {obj1 as Duration}");

// negative tests

def str1 = "hi";
function strFunc(): String {
    return "there";
}

// There should be no null checks on these
str1 as String;
strFunc() as String;
