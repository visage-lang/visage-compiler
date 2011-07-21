/*
 * @subtest jfxc3121
 */

public mixin class jfxc3121Mixin {

    var root:String = "test";

    public bound function failsToCompile():String {
        return root;
    }

    public function worksOk():String {
        return root;
    }


}

public class jfxc3121Impl extends jfxc3121Mixin {

}

public function get():jfxc3121Mixin {
    return jfxc3121Impl{};
}


