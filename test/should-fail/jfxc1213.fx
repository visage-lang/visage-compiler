/*
 * @test/compile-error
 */

class BoundFuncTest{
   attribute arr:String [] =["This ", "is ", "weird"];
   function func(){
        delete arr;
   }
   bound function func2(){
        func();
   }
}
