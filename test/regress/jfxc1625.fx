/*
 * @test
 * @run
 */
import java.lang.System;
function f1 (b : Boolean) : Number [] {
        if(b)
             {   return [1.1,1.2]; }
        2.3

}
function f2 () : Number [] {
        try{
        }
        finally{
                return [1.1,0.2];
        }
1.1;
}
// This one is actually JFXC-1626, but it's the same concept:
function f3 () : String[] {
        if(true){
                return ["a"];
        }
"b";
}
System.out.println("f1(true) =>{for (r in f1(true)) " {r}"};");
System.out.println("f3 =>{for (r in f3()) " {r}"};");

