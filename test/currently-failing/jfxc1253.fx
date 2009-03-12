/* jfxc1253 Class with overriden method cannot be compiled.
 * @test
 *
 */
import java.lang.*;


public function me(s:String):String {
    HappyBabyGoodBaby {
        boo: "Booo"
        moo: "Moo"

        override public function doMoo(): String {
            "BoooMoooBooo";
        }

    }.doMoo();
}

class HappyBabyGoodBaby {
    var boo:String;
    var moo:String;
    public function doMoo(): String {
        return "Moo";
    }
    
}


/* Compiler error message:
 *
   jfxc1253.fx:11: doMoo() is already defined in HappyBabyGoodBaby$anon1
      HappyBabyGoodBaby {
      ^
  1 error
 **/
