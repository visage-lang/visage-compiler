/*
 * @test/compile-error
 */

class A{
    /*
    function doIt(x:String[]):String[]{
            return x;
    }
    */
    attribute doIt=function ():String[]{
            ["Fx","Rocks"];
    }
}
var obj:A = A{};

obj.doIt(["Fx","Rocks"]);
